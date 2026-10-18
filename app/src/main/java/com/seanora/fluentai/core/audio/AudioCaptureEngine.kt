package com.seanora.fluentai.core.audio

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.audiofx.AcousticEchoCanceler
import android.media.audiofx.NoiseSuppressor
import androidx.core.content.ContextCompat
import com.seanora.fluentai.core.common.di.IoDispatcher
import com.seanora.fluentai.core.common.logging.AppLog
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min
import kotlin.math.sqrt

data class AudioChunk(
    val data: ByteArray,
    val normalizedAmplitude: Float,
    val timestampMs: Long = System.currentTimeMillis()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AudioChunk
        if (!data.contentEquals(other.data)) return false
        if (normalizedAmplitude != other.normalizedAmplitude) return false
        if (timestampMs != other.timestampMs) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + normalizedAmplitude.hashCode()
        result = 31 * result + timestampMs.hashCode()
        return result
    }
}

interface AudioCaptureEngine {
    val isRecording: StateFlow<Boolean>
    val amplitudeFlow: StateFlow<Float>
    val audioChunks: SharedFlow<AudioChunk>

    fun startCapture(): Result<Unit>
    fun stopCapture()
    fun release()
}

@Singleton
class AndroidAudioCaptureEngine @Inject constructor(
    @ApplicationContext private val context: Context,
    private val audioRoute: VoiceCommunicationAudioRoute,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : AudioCaptureEngine {

    companion object {
        private const val TAG = "AudioCaptureEngine"
        const val SAMPLE_RATE_HZ = 16000
        const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
        const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
        const val CHUNK_SIZE_BYTES = 3200
        private const val CHUNK_BUFFER_CAPACITY = 64
        private const val READ_ERROR_BACKOFF_MS = 20L
        private const val SPEECH_RMS_REFERENCE = 12000.0
        private val PREFERRED_SOURCES = intArrayOf(
            MediaRecorder.AudioSource.VOICE_COMMUNICATION,
            MediaRecorder.AudioSource.VOICE_RECOGNITION,
            MediaRecorder.AudioSource.MIC
        )
    }

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)
    private val lock = Any()
    private var recordingJob: Job? = null
    private var audioRecord: AudioRecord? = null
    private var echoCanceler: AcousticEchoCanceler? = null
    private var noiseSuppressor: NoiseSuppressor? = null

    private val _isRecording = MutableStateFlow(false)
    override val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _amplitudeFlow = MutableStateFlow(0f)
    override val amplitudeFlow: StateFlow<Float> = _amplitudeFlow.asStateFlow()

    private val _audioChunks = MutableSharedFlow<AudioChunk>(extraBufferCapacity = CHUNK_BUFFER_CAPACITY)
    override val audioChunks: SharedFlow<AudioChunk> = _audioChunks.asSharedFlow()

    @SuppressLint("MissingPermission")
    override fun startCapture(): Result<Unit> {
        synchronized(lock) {
            if (_isRecording.value) return Result.success(Unit)
            if (!hasRecordPermission()) {
                return Result.failure(SecurityException("Microphone permission (RECORD_AUDIO) not granted"))
            }
            return startRecordingLocked()
        }
    }

    @SuppressLint("MissingPermission")
    private fun startRecordingLocked(): Result<Unit> =
        runCatching {
            val record = createAudioRecord()
                ?: error("AudioRecord failed to initialize across all attempted audio sources")
            audioRecord = record
            audioRoute.acquire()
            attachVoiceEffects(record.audioSessionId)
            record.startRecording()
            check(record.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                "AudioRecord failed to start recording"
            }
            _isRecording.value = true
            recordingJob = scope.launch { readLoop(record) }
            AppLog.i(TAG) { "Microphone capture started" }
        }.onFailure { error ->
            AppLog.e(TAG, error) { "Failed to start audio recording" }
            stopCaptureLocked()
        }

    override fun stopCapture() {
        synchronized(lock) { stopCaptureLocked() }
    }

    override fun release() = stopCapture()

    private suspend fun readLoop(record: AudioRecord) {
        val buffer = ByteArray(CHUNK_SIZE_BYTES)
        while (scope.isActive && _isRecording.value) {
            val bytesRead = record.read(buffer, 0, buffer.size)
            when {
                bytesRead > 0 -> {
                    val chunkData = buffer.copyOf(bytesRead)
                    val amplitude = normalizedAmplitude(chunkData)
                    _amplitudeFlow.value = amplitude
                    if (!_audioChunks.tryEmit(AudioChunk(data = chunkData, normalizedAmplitude = amplitude))) {
                        AppLog.w(TAG) { "Microphone chunk dropped: consumer is too slow" }
                    }
                }
                bytesRead < 0 -> {
                    AppLog.e(TAG) { "AudioRecord.read error: $bytesRead" }
                    delay(READ_ERROR_BACKOFF_MS)
                }
            }
        }
    }

    private fun hasRecordPermission(): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    private fun createAudioRecord(): AudioRecord? {
        val minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_HZ, CHANNEL_CONFIG, AUDIO_FORMAT)
        val bufferSize = maxOf(minBufferSize, CHUNK_SIZE_BYTES * 2)
        PREFERRED_SOURCES.forEach { source ->
            val candidate = runCatching {
                AudioRecord(source, SAMPLE_RATE_HZ, CHANNEL_CONFIG, AUDIO_FORMAT, bufferSize)
            }.getOrNull() ?: return@forEach
            if (candidate.state == AudioRecord.STATE_INITIALIZED) {
                AppLog.i(TAG) { "AudioRecord initialized with source=$source" }
                return candidate
            }
            candidate.release()
        }
        return null
    }

    private fun attachVoiceEffects(sessionId: Int) {
        if (AcousticEchoCanceler.isAvailable()) {
            echoCanceler = runCatching { AcousticEchoCanceler.create(sessionId)?.apply { enabled = true } }.getOrNull()
        }
        if (NoiseSuppressor.isAvailable()) {
            noiseSuppressor = runCatching { NoiseSuppressor.create(sessionId)?.apply { enabled = true } }.getOrNull()
        }
    }

    private fun stopCaptureLocked() {
        _isRecording.value = false
        recordingJob?.cancel()
        recordingJob = null
        runCatching { echoCanceler?.release() }
        echoCanceler = null
        runCatching { noiseSuppressor?.release() }
        noiseSuppressor = null
        runCatching {
            audioRecord?.takeIf { it.state == AudioRecord.STATE_INITIALIZED }?.apply {
                if (recordingState == AudioRecord.RECORDSTATE_RECORDING) stop()
                release()
            }
        }.onFailure { AppLog.w(TAG, it) { "Error releasing AudioRecord" } }
        if (audioRecord != null) audioRoute.release()
        audioRecord = null
        _amplitudeFlow.value = 0f
    }

    private fun normalizedAmplitude(buffer: ByteArray): Float {
        if (buffer.size < 2) return 0f
        val samples = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer()
        val sampleCount = buffer.size / 2
        var sumSquares = 0.0
        for (index in 0 until sampleCount) {
            val sample = samples.get(index).toDouble()
            sumSquares += sample * sample
        }
        return min(1.0f, (sqrt(sumSquares / sampleCount) / SPEECH_RMS_REFERENCE).toFloat())
    }
}
