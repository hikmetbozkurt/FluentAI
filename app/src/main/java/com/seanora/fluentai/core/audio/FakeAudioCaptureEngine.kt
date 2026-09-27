package com.seanora.fluentai.core.audio

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Test & mock implementation of AudioCaptureEngine for unit tests, previews, and emulators.
 */
@Singleton
class FakeAudioCaptureEngine @Inject constructor() : AudioCaptureEngine {

    private val _isRecording = MutableStateFlow(false)
    override val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _amplitudeFlow = MutableStateFlow(0f)
    override val amplitudeFlow: StateFlow<Float> = _amplitudeFlow.asStateFlow()

    private val _audioChunks = MutableSharedFlow<AudioChunk>(replay = 1, extraBufferCapacity = 64)
    override val audioChunks: SharedFlow<AudioChunk> = _audioChunks.asSharedFlow()

    var shouldFailStart: Boolean = false

    override fun startCapture(): Result<Unit> {
        if (shouldFailStart) {
            return Result.failure(IllegalStateException("Simulated microphone capture failure"))
        }
        _isRecording.value = true
        return Result.success(Unit)
    }

    override fun stopCapture() {
        _isRecording.value = false
        _amplitudeFlow.value = 0f
    }

    override fun release() {
        stopCapture()
    }

    /**
     * Test helper to simulate incoming captured PCM audio from the user's microphone.
     */
    fun emitChunk(chunk: AudioChunk) {
        if (_isRecording.value) {
            _amplitudeFlow.value = chunk.normalizedAmplitude
            _audioChunks.tryEmit(chunk)
        }
    }

    /**
     * Test helper to simulate voice amplitude changes (e.g. for speech detection tests).
     */
    fun setAmplitude(amp: Float) {
        _amplitudeFlow.value = amp.coerceIn(0f, 1f)
    }
}
