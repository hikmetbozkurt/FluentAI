package com.seanora.fluentai.ai.live

import com.seanora.fluentai.core.model.CorrectionMode

enum class VoiceUiState {
    IDLE,
    CONNECTING,
    LISTENING,
    USER_SPEAKING,
    PROCESSING,
    COACH_SPEAKING,
    PAUSED,
    RECONNECTING,
    ENDING,
    ENDED,
    ERROR;

    companion object {
        val restartableStates: Set<VoiceUiState> = setOf(IDLE, ENDED, ERROR)
        val inactiveStates: Set<VoiceUiState> = setOf(PAUSED, ENDING, ENDED, ERROR)
        val pausableStates: Set<VoiceUiState> = setOf(COACH_SPEAKING, USER_SPEAKING, LISTENING, PROCESSING)
    }
}

enum class LiveConnectionState {
    DISCONNECTED,
    CONNECTING,
    SOCKET_OPEN,
    SESSION_READY,
    CONNECTED,
    RECONNECTING,
    FAILED,
    CLOSED
}

enum class TranscriptRole {
    USER,
    COACH,
    SYSTEM
}

enum class SpeakingSessionType {
    SCENARIO,
    FREE_TALK
}

enum class SpeakingPersona(val displayName: String) {
    GALADRIEL("Galadriel")
}

data class TranscriptEntry(
    val id: String,
    val role: TranscriptRole,
    val text: String,
    val timestampMs: Long = System.currentTimeMillis(),
    val isFinal: Boolean = false
)

data class LiveScenarioConfig(
    val scenarioId: String?,
    val title: String,
    val systemPrompt: String,
    val voiceName: String = "Aoede",
    val sampleRateHz: Int = 24000,
    val cefrTarget: String = "B2",
    val correctionMode: CorrectionMode = CorrectionMode.COACH,
    val sessionType: SpeakingSessionType = SpeakingSessionType.SCENARIO,
    val persona: SpeakingPersona? = null,
    val memoryContext: String? = null
)

sealed interface LiveSessionEvent {
    data class Connected(val sessionId: String) : LiveSessionEvent
    data class AudioOutputChunk(val pcmData: ByteArray) : LiveSessionEvent
    data class InterimInputTranscription(val text: String) : LiveSessionEvent
    data class InputTranscription(val text: String) : LiveSessionEvent
    data class OutputTranscription(val text: String) : LiveSessionEvent
    data class TranscriptDelta(
        val role: TranscriptRole,
        val text: String,
        val isFinal: Boolean = false
    ) : LiveSessionEvent
    data object GenerationComplete : LiveSessionEvent
    data object TurnComplete : LiveSessionEvent
    data object Interrupted : LiveSessionEvent
    data object InterruptedByBargeIn : LiveSessionEvent
    data object WaitingForInput : LiveSessionEvent
    data class InteractionStatus(val status: String) : LiveSessionEvent
    data object SessionResumptionUpdate : LiveSessionEvent
    data class Reconnecting(val attempt: Int) : LiveSessionEvent
    data class UsageUpdated(val usage: LiveTokenUsage) : LiveSessionEvent
    data class Error(val message: String, val isFatal: Boolean = false) : LiveSessionEvent
    data class Disconnected(val reason: String) : LiveSessionEvent
}

data class LiveTokenUsage(
    val promptTokens: Int = 0,
    val responseTokens: Int = 0,
    val totalTokens: Int = 0
)
