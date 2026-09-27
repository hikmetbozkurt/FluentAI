package com.seanora.fluentai.core.audio

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mock & test implementation of StreamAudioPlaybackEngine for unit tests and previews.
 */
@Singleton
class FakeStreamAudioPlaybackEngine @Inject constructor() : StreamAudioPlaybackEngine {

    private val _isPlaying = MutableStateFlow(false)
    override val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _playbackState = MutableStateFlow(PlaybackLifecycleState.IDLE)
    override val playbackState: StateFlow<PlaybackLifecycleState> = _playbackState.asStateFlow()

    private val _amplitudeFlow = MutableStateFlow(0f)
    override val amplitudeFlow: StateFlow<Float> = _amplitudeFlow.asStateFlow()

    val writtenChunks = mutableListOf<ByteArray>()
    var stopAndFlushCallCount: Int = 0
    var finishTurnCallCount: Int = 0

    override fun writeChunk(pcmData: ByteArray) {
        if (pcmData.isNotEmpty()) {
            writtenChunks.add(pcmData)
            _isPlaying.value = true
            _playbackState.value = PlaybackLifecycleState.PLAYING
            _amplitudeFlow.value = 0.6f
        }
    }

    override fun stopAndFlush() {
        stopAndFlushCallCount++
        writtenChunks.clear()
        _isPlaying.value = false
        _playbackState.value = PlaybackLifecycleState.FLUSHED
        _amplitudeFlow.value = 0f
    }

    override fun finishTurn() {
        finishTurnCallCount++
        _isPlaying.value = false
        _playbackState.value = PlaybackLifecycleState.IDLE
        _amplitudeFlow.value = 0f
    }

    override fun release() {
        stopAndFlush()
    }

    fun simulatePlaybackComplete() {
        _isPlaying.value = false
        _playbackState.value = PlaybackLifecycleState.IDLE
        _amplitudeFlow.value = 0f
    }
}
