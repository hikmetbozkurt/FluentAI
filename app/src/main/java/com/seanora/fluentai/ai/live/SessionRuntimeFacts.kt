package com.seanora.fluentai.ai.live

internal class SessionRuntimeFacts {
    var connectionReady = false
    var isReconnecting = false
    var isPaused = false
    var isEnding = false
    var error: String? = null
    var modelGenerationActive = false
    var modelPlaybackActive = false
    var userSpeechActive = false
    var waitingForModel = false
    var isMicrophoneMuted = false
    var suppressCoachAudio = false
    var playbackTailDrainRequested = false
    var coachTurnRequestedAtMs: Long? = null

    fun reset() {
        connectionReady = false
        isReconnecting = false
        isPaused = false
        isEnding = false
        error = null
        modelGenerationActive = false
        modelPlaybackActive = false
        userSpeechActive = false
        waitingForModel = false
        isMicrophoneMuted = false
        suppressCoachAudio = false
        playbackTailDrainRequested = false
        coachTurnRequestedAtMs = null
    }

    fun stopCoachOutput() {
        modelPlaybackActive = false
        modelGenerationActive = false
        playbackTailDrainRequested = false
        userSpeechActive = true
        waitingForModel = false
    }

    fun hasCoachOutput(): Boolean = modelGenerationActive || modelPlaybackActive

    fun canCaptureAudio(): Boolean = !isPaused && connectionReady && !isEnding && error == null

    fun deriveVoiceState(): VoiceUiState = when {
        isEnding -> VoiceUiState.ENDING
        error != null -> VoiceUiState.ERROR
        isReconnecting -> VoiceUiState.RECONNECTING
        !connectionReady -> VoiceUiState.CONNECTING
        isPaused -> VoiceUiState.PAUSED
        userSpeechActive -> VoiceUiState.USER_SPEAKING
        hasCoachOutput() -> VoiceUiState.COACH_SPEAKING
        waitingForModel -> VoiceUiState.PROCESSING
        else -> VoiceUiState.LISTENING
    }
}
