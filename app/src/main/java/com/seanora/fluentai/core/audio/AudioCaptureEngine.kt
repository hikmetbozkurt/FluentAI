package com.seanora.fluentai.core.audio

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.AudioManager
import android.media.audiofx.AcousticEchoCanceler
import android.media.audiofx.NoiseSuppressor
import android.util.Log
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
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

/**
 * Encapsulates a single chunk of PCM audio data captured from the microphone,
 * with associated metadata for streaming and UI visualization.
 */
data class AudioChunk(
    val data: ByteArray,
    val normalizedAmplitude: Float, // 0.0f to 1.0f for UI waveform visualizer
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

/**
 * Interface isolating microphone capture behind an abstraction per ADR-012.
 */
interface AudioCaptureEngine {
    val isRecording: StateFlow<Boolean>
    val amplitudeFlow: StateFlow<Float>
    val audioChunks: SharedFlow<AudioChunk>

    fun startCapture(): Result<Unit>
    fun stopCapture()
    fun release()
}

/**
 * Native Android implementation of AudioCaptureEngine using AudioRecord.
 * Captures 16kHz 16-bit Mono Linear PCM audio configured for full-duplex communication.
 */
@Singleton
class AndroidAudioCaptureEngine @Inject constructor(
    @ApplicationContext private val context: Context
) : AudioCaptureEngine {

    companion object {
        private const val TAG = "AudioCaptureEngine"
        const val SAMPLE_RATE_HZ = 16000
        const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
        const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
        // 100ms chunk = 1600 samples = 3200 bytes
        const val CHUNK_SIZE_BYTES = 3200
    }

    private var ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private var scope = CoroutineScope(SupervisorJob() + ioDispatcher)

    constructor(context: Context, dispatcher: CoroutineDispatcher) : this(context) {
        this.ioDispatcher = dispatcher
        this.scope = CoroutineScope(SupervisorJob() + dispatcher)
    }

    private var recordingJob: Job? = null
    private var audioRecord: AudioRecord? = null
    private var echoCanceler: AcousticEchoCanceler? = null
    private var noiseSuppressor: NoiseSuppressor? = null
    private var previousAudioMode: Int? = null
    private var previousSpeakerphoneOn: Boolean? = null

    private val _isRecording = MutableStateFlow(false)
    override val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _amplitudeFlow = MutableStateFlow(0f)
    override val amplitudeFlow: StateFlow<Float> = _amplitudeFlow.asStateFlow()

    private val _audioChunks = MutableSharedFlow<AudioChunk>(extraBufferCapacity = 64)
    override val audioChunks: SharedFlow<AudioChunk> = _audioChunks.asSharedFlow()

    @SuppressLint("MissingPermission")
    @Suppress("DEPRECATION")
    override fun startCapture(): Result<Unit> {
        if (_isRecording.value) {
            return Result.success(Unit)
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return Result.failure(SecurityException("Microphone permission (RECORD_AUDIO) not granted"))
        }

        return try {
            val minBufferSize = AudioRecord.getMinBufferSize(
                SAMPLE_RATE_HZ,
                CHANNEL_CONFIG,
                AUDIO_FORMAT
            )

            val bufferSize = maxOf(minBufferSize, CHUNK_SIZE_BYTES * 2)

            // Prefer VOICE_COMMUNICATION for hardware AEC and full-duplex alignment, with fallback to VOICE_RECOGNITION and MIC
            val sourcesToTry = intArrayOf(
                MediaRecorder.AudioSource.VOICE_COMMUNICATION,
                MediaRecorder.AudioSource.VOICE_RECOGNITION,
                MediaRecorder.AudioSource.MIC
            )

            var record: AudioRecord? = null
            for (source in sourcesToTry) {
                try {
                    val candidate = AudioRecord(
                        source,
                        SAMPLE_RATE_HZ,
                        CHANNEL_CONFIG,
                        AUDIO_FORMAT,
                        bufferSize
                    )
                    if (candidate.state == AudioRecord.STATE_INITIALIZED) {
                        record = candidate
                        Log.i(TAG, "AudioRecord initialized with audioSource=$source")
                        break
                    } else {
                        candidate.release()
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "AudioRecord creation failed for source $source: ${e.message}")
                }
            }

            if (record == null || record.state != AudioRecord.STATE_INITIALIZED) {
                record?.release()
                return Result.failure(IllegalStateException("AudioRecord failed to initialize across all attempted audio sources"))
            }

            // Configure AudioManager for full-duplex voice communication
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
            previousAudioMode = audioManager?.mode
            previousSpeakerphoneOn = audioManager?.isSpeakerphoneOn
            try {
                audioManager?.mode = AudioManager.MODE_IN_COMMUNICATION
                audioManager?.isSpeakerphoneOn = true
                Log.i(TAG, "AudioManager set to MODE_IN_COMMUNICATION, speakerphone=true")
            } catch (e: Exception) {
                Log.w(TAG, "Failed setting AudioManager mode: ${e.message}")
            }

            // Safely attach hardware AcousticEchoCanceler and NoiseSuppressor if supported
            val sessionId = record.audioSessionId
            if (sessionId != AudioManager.ERROR) {
                if (AcousticEchoCanceler.isAvailable()) {
                    try {
                        echoCanceler = AcousticEchoCanceler.create(sessionId)?.apply {
                            enabled = true
                        }
                        Log.i(TAG, "AEC available=true, enabled=${echoCanceler?.enabled == true}")
                    } catch (e: Exception) {
                        Log.w(TAG, "Failed to enable AcousticEchoCanceler: ${e.message}")
                    }
                } else {
                    Log.i(TAG, "AEC available=false")
                }

                if (NoiseSuppressor.isAvailable()) {
                    try {
                        noiseSuppressor = NoiseSuppressor.create(sessionId)?.apply {
                            enabled = true
                        }
                        Log.i(TAG, "NoiseSuppressor available=true, enabled=${noiseSuppressor?.enabled == true}")
                    } catch (e: Exception) {
                        Log.w(TAG, "Failed to enable NoiseSuppressor: ${e.message}")
                    }
                } else {
                    Log.i(TAG, "NoiseSuppressor available=false")
                }
            }

            record.startRecording()
            if (record.recordingState != AudioRecord.RECORDSTATE_RECORDING) {
                record.release()
                return Result.failure(IllegalStateException("AudioRecord failed to start recording (recordingState != RECORDSTATE_RECORDING)"))
            }

            Log.i(TAG, "microphone recording started")

            audioRecord = record
            _isRecording.value = true

            recordingJob = scope.launch {
                val buffer = ByteArray(CHUNK_SIZE_BYTES)
                while (isActive && _isRecording.value) {
                    val bytesRead = record.read(buffer, 0, buffer.size)
                    if (bytesRead > 0) {
                        val chunkData = buffer.copyOf(bytesRead)
                        val amp = calculateNormalizedAmplitude(chunkData, bytesRead)
                        Log.d(TAG, "PCM captured bytes=$bytesRead rms=${String.format("%.3f", amp)}")
                        _amplitudeFlow.value = amp
                        _audioChunks.tryEmit(
                            AudioChunk(
                                data = chunkData,
                                normalizedAmplitude = amp
                            )
                        )
                    } else if (bytesRead < 0) {
                        Log.e(TAG, "AudioRecord.read error: $bytesRead")
                        kotlinx.coroutines.delay(20)
                    }
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start audio recording", e)
            stopCapture()
            Result.failure(e)
        }
    }

    @Suppress("DEPRECATION")
    override fun stopCapture() {
        _isRecording.value = false
        recordingJob?.cancel()
        recordingJob = null

        // Release AEC and NoiseSuppressor effects
        try {
            echoCanceler?.release()
        } catch (e: Exception) {
            Log.w(TAG, "Error releasing AcousticEchoCanceler: ${e.message}")
        } finally {
            echoCanceler = null
        }

        try {
            noiseSuppressor?.release()
        } catch (e: Exception) {
            Log.w(TAG, "Error releasing NoiseSuppressor: ${e.message}")
        } finally {
            noiseSuppressor = null
        }

        // Release AudioRecord
        try {
            audioRecord?.apply {
                if (state == AudioRecord.STATE_INITIALIZED) {
                    if (recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                        stop()
                    }
                    release()
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error stopping/releasing AudioRecord", e)
        } finally {
            audioRecord = null
        }

        // Restore AudioManager mode and routing
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
        previousAudioMode?.let { prevMode ->
            try {
                audioManager?.mode = prevMode
                Log.i(TAG, "Restored AudioManager mode to $prevMode")
            } catch (e: Exception) {
                Log.w(TAG, "Failed to restore AudioManager mode: ${e.message}")
            }
            previousAudioMode = null
        }
        previousSpeakerphoneOn?.let { prevSpeaker ->
            try {
                audioManager?.isSpeakerphoneOn = prevSpeaker
            } catch (e: Exception) {
                Log.w(TAG, "Failed to restore speakerphone state: ${e.message}")
            }
            previousSpeakerphoneOn = null
        }

        _amplitudeFlow.value = 0f
    }

    override fun release() {
        stopCapture()
    }

    private fun calculateNormalizedAmplitude(buffer: ByteArray, length: Int): Float {
        if (length < 2) return 0f
        val shortBuffer = ByteBuffer.wrap(buffer, 0, length).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer()
        val numSamples = length / 2
        var sumSquares = 0.0

        for (i in 0 until numSamples) {
            val sample = shortBuffer.get(i).toDouble()
            sumSquares += sample * sample
        }

        val rms = sqrt(sumSquares / numSamples)
        // 16-bit PCM max absolute value is 32767. Max conversational speech peak RMS is ~12000-16000.
        return min(1.0f, (rms / 12000.0).toFloat())
    }
}
