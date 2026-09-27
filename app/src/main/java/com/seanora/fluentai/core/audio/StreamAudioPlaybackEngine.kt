package com.seanora.fluentai.core.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min
import kotlin.math.sqrt

enum class PlaybackLifecycleState {
    IDLE,
    PLAYING,
    FLUSHED
}

interface StreamAudioPlaybackEngine {
    val isPlaying: StateFlow<Boolean>
    val playbackState: StateFlow<PlaybackLifecycleState>
    val amplitudeFlow: StateFlow<Float>

    fun writeChunk(pcmData: ByteArray)
    fun finishTurn()
    fun stopAndFlush()
    fun release()
}

/** Coalesces ordered PCM fragments before the single AudioTrack writer consumes them. */
internal class PcmPlaybackBuffer(
    private val prebufferBytes: Int,
    private val minimumWriteBytes: Int
) {
    private var pending = ByteArray(0)
    private var playbackStarted = false

    @Synchronized
    fun offer(bytes: ByteArray): List<ByteArray> {
        if (bytes.isEmpty()) return emptyList()
        pending += bytes
        val threshold = if (playbackStarted) minimumWriteBytes else prebufferBytes
        if (pending.size < threshold) return emptyList()
        playbackStarted = true
        return listOf(takePending())
    }

    @Synchronized
    fun drainTail(): List<ByteArray> {
        if (pending.isEmpty()) {
            playbackStarted = false
            return emptyList()
        }
        val tail = takePending()
        playbackStarted = false
        return listOf(tail)
    }

    @Synchronized
    fun clear() {
        pending = ByteArray(0)
        playbackStarted = false
    }

    @Synchronized
    fun restartPrebuffering() {
        playbackStarted = false
    }

    private fun takePending(): ByteArray = pending.also { pending = ByteArray(0) }
}

internal fun writePcmFully(
    pcmData: ByteArray,
    writer: (ByteArray, Int, Int) -> Int
): Int {
    var offset = 0
    while (offset < pcmData.size) {
        val written = writer(pcmData, offset, pcmData.size - offset)
        if (written <= 0) return if (offset == 0) written else offset
        offset += written
    }
    return offset
}

private sealed interface PlaybackCommand {
    val epoch: Long

    data class Audio(override val epoch: Long, val pcmData: ByteArray) : PlaybackCommand
    data class FinishTurn(override val epoch: Long) : PlaybackCommand
}

@Singleton
class AndroidStreamAudioPlaybackEngine @Inject constructor() : StreamAudioPlaybackEngine {

    companion object {
        private const val TAG = "StreamAudioPlayback"
        const val DEFAULT_SAMPLE_RATE_HZ = 24000
        const val CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO
        const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
        private const val BYTES_PER_SAMPLE = 2
        private const val PREBUFFER_MS = 80
        private const val MINIMUM_WRITE_MS = 20
        private const val DRAIN_POLL_MS = 10L
    }

    private var ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private var sampleRateHz: Int = DEFAULT_SAMPLE_RATE_HZ
    private var scope = CoroutineScope(SupervisorJob() + ioDispatcher)

    constructor(dispatcher: CoroutineDispatcher, sampleRate: Int = DEFAULT_SAMPLE_RATE_HZ) : this() {
        ioDispatcher = dispatcher
        sampleRateHz = sampleRate
        scope = CoroutineScope(SupervisorJob() + dispatcher)
        pcmBuffer = createPcmBuffer()
    }

    private val trackLock = Any()
    private val playbackEpoch = AtomicLong(0L)
    private var playbackJob: Job? = null
    private var audioTrack: AudioTrack? = null
    private val commandQueue = Channel<PlaybackCommand>(Channel.UNLIMITED)
    private var pcmBuffer = createPcmBuffer()
    private var totalFramesWritten: Long = 0L

    private val _isPlaying = MutableStateFlow(false)
    override val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()
    private val _playbackState = MutableStateFlow(PlaybackLifecycleState.IDLE)
    override val playbackState: StateFlow<PlaybackLifecycleState> = _playbackState.asStateFlow()
    private val _amplitudeFlow = MutableStateFlow(0f)
    override val amplitudeFlow: StateFlow<Float> = _amplitudeFlow.asStateFlow()

    init {
        initAudioTrack()
        startPlaybackLoop()
    }

    private fun createPcmBuffer() = PcmPlaybackBuffer(
        prebufferBytes = sampleRateHz * BYTES_PER_SAMPLE * PREBUFFER_MS / 1000,
        minimumWriteBytes = sampleRateHz * BYTES_PER_SAMPLE * MINIMUM_WRITE_MS / 1000
    )

    private fun initAudioTrack() {
        synchronized(trackLock) {
            try {
                val minimum = AudioTrack.getMinBufferSize(sampleRateHz, CHANNEL_CONFIG, AUDIO_FORMAT)
                val prebufferBytes = sampleRateHz * BYTES_PER_SAMPLE * PREBUFFER_MS / 1000
                val bufferSize = maxOf(minimum.coerceAtLeast(0) * 2, prebufferBytes * 4, 8192)
                val format = AudioFormat.Builder()
                    .setSampleRate(sampleRateHz)
                    .setChannelMask(CHANNEL_CONFIG)
                    .setEncoding(AUDIO_FORMAT)
                    .build()
                val communicationAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()

                var track = try {
                    AudioTrack.Builder()
                        .setAudioAttributes(communicationAttributes)
                        .setAudioFormat(format)
                        .setBufferSizeInBytes(bufferSize)
                        .setTransferMode(AudioTrack.MODE_STREAM)
                        .build()
                } catch (_: Exception) {
                    null
                }

                if (track == null || track.state != AudioTrack.STATE_INITIALIZED) {
                    Log.w(TAG, "AudioTrack voice route unavailable; falling back to media route")
                    track?.release()
                    track = AudioTrack.Builder()
                        .setAudioAttributes(
                            AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                                .build()
                        )
                        .setAudioFormat(format)
                        .setBufferSizeInBytes(bufferSize)
                        .setTransferMode(AudioTrack.MODE_STREAM)
                        .build()
                }

                audioTrack = track
                if (track.state == AudioTrack.STATE_INITIALIZED) {
                    Log.i(TAG, "AudioTrack initialized at ${sampleRateHz}Hz mono PCM16, buffer=$bufferSize")
                } else {
                    Log.e(TAG, "AudioTrack failed to initialize")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to initialize AudioTrack", e)
            }
        }
    }

    private fun startPlaybackLoop() {
        playbackJob?.cancel()
        playbackJob = scope.launch {
            for (command in commandQueue) {
                if (!isActive) break
                if (command.epoch != playbackEpoch.get()) continue
                when (command) {
                    is PlaybackCommand.Audio -> {
                        if (trackDrainedMidTurn()) {
                            pcmBuffer.restartPrebuffering()
                            Log.d(TAG, "Playback starved mid-turn; rebuilding the 80ms jitter buffer")
                        }
                        pcmBuffer.offer(command.pcmData).forEach(::writeBufferedPcm)
                    }
                    is PlaybackCommand.FinishTurn -> {
                        pcmBuffer.drainTail().forEach(::writeBufferedPcm)
                        waitUntilTrackDrains(command.epoch)
                    }
                }
            }
        }
    }

    private fun trackDrainedMidTurn(): Boolean = synchronized(trackLock) {
        val track = audioTrack ?: return@synchronized false
        if (!_isPlaying.value || totalFramesWritten <= 0L || track.state != AudioTrack.STATE_INITIALIZED) {
            return@synchronized false
        }
        val playbackHead = track.playbackHeadPosition.toLong() and 0xFFFFFFFFL
        playbackHead >= totalFramesWritten
    }

    private fun writeBufferedPcm(chunk: ByteArray) {
        if (chunk.isEmpty()) return
        _amplitudeFlow.value = calculateNormalizedAmplitude(chunk)
        try {
            synchronized(trackLock) {
                var track = audioTrack
                if (track == null || track.state != AudioTrack.STATE_INITIALIZED) {
                    initAudioTrack()
                    track = audioTrack
                }
                if (track == null || track.state != AudioTrack.STATE_INITIALIZED) return
                if (track.playState != AudioTrack.PLAYSTATE_PLAYING) track.play()
                val written = writePcmFully(chunk) { data, offset, length ->
                    track.write(data, offset, length)
                }
                if (written == chunk.size) {
                    totalFramesWritten += written / BYTES_PER_SAMPLE
                } else {
                    Log.e(TAG, "AudioTrack write incomplete: wrote=$written requested=${chunk.size}")
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "AudioTrack write failed", e)
        }
    }

    private suspend fun waitUntilTrackDrains(epoch: Long) {
        while (scope.isActive && playbackEpoch.get() == epoch) {
            val drained = synchronized(trackLock) {
                val track = audioTrack
                if (track == null || track.state != AudioTrack.STATE_INITIALIZED) {
                    true
                } else {
                    val playbackHead = track.playbackHeadPosition.toLong() and 0xFFFFFFFFL
                    playbackHead >= totalFramesWritten
                }
            }
            if (drained) break
            delay(DRAIN_POLL_MS)
        }
        if (playbackEpoch.get() == epoch) {
            _isPlaying.value = false
            _playbackState.value = PlaybackLifecycleState.IDLE
            _amplitudeFlow.value = 0f
            Log.i(TAG, "Gemini audio playback finished/drained")
        }
    }

    override fun writeChunk(pcmData: ByteArray) {
        if (pcmData.isEmpty()) return
        val epoch = playbackEpoch.get()
        _isPlaying.value = true
        _playbackState.value = PlaybackLifecycleState.PLAYING
        commandQueue.trySend(PlaybackCommand.Audio(epoch, pcmData.copyOf()))
    }

    override fun finishTurn() {
        commandQueue.trySend(PlaybackCommand.FinishTurn(playbackEpoch.get()))
    }

    override fun stopAndFlush() {
        playbackEpoch.incrementAndGet()
        while (commandQueue.tryReceive().isSuccess) {
            // Drop stale queued data from the interrupted turn.
        }
        pcmBuffer.clear()
        synchronized(trackLock) {
            try {
                audioTrack?.takeIf { it.state == AudioTrack.STATE_INITIALIZED }?.apply {
                    pause()
                    flush()
                }
            } catch (e: Exception) {
                Log.w(TAG, "Error flushing AudioTrack", e)
            }
            totalFramesWritten = 0L
        }
        _isPlaying.value = false
        _playbackState.value = PlaybackLifecycleState.FLUSHED
        _amplitudeFlow.value = 0f
        Log.i(TAG, "Gemini audio playback queue flushed")
    }

    override fun release() {
        stopAndFlush()
        playbackJob?.cancel()
        playbackJob = null
        commandQueue.close()
        synchronized(trackLock) {
            try {
                audioTrack?.release()
            } catch (e: Exception) {
                Log.w(TAG, "Error releasing AudioTrack", e)
            }
            audioTrack = null
        }
    }

    private fun calculateNormalizedAmplitude(buffer: ByteArray): Float {
        if (buffer.size < BYTES_PER_SAMPLE) return 0f
        val shortBuffer = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer()
        val sampleCount = buffer.size / BYTES_PER_SAMPLE
        var sumSquares = 0.0
        for (index in 0 until sampleCount) {
            val sample = shortBuffer.get(index).toDouble()
            sumSquares += sample * sample
        }
        val rms = sqrt(sumSquares / sampleCount)
        return min(1f, (rms / 12000.0).toFloat())
    }
}
