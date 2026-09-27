package com.seanora.fluentai.ai.live

import com.seanora.fluentai.core.model.CorrectionMode

/**
 * 11 explicit voice states defined in AGENTS.md Section 11.
 */
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
    ERROR
}

/**
 * Connection lifecycle states for the live bidirectional WebSocket.
 */
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

/**
 * Role of speaker in the real-time conversation transcript.
 */
enum class TranscriptRole {
    USER,
    COACH,
    SYSTEM
}

/** Distinguishes curriculum-backed sessions from runtime-only conversation experiences. */
enum class SpeakingSessionType {
    SCENARIO,
    FREE_TALK
}

/** Runtime persona identifiers. This is intentionally small until another real persona exists. */
enum class SpeakingPersona(val displayName: String) {
    GALADRIEL("Galadriel")
}

/**
 * A dialogue transcript entry, supporting real-time streaming updates.
 */
data class TranscriptEntry(
    val id: String,
    val role: TranscriptRole,
    val text: String,
    val timestampMs: Long = System.currentTimeMillis(),
    val isFinal: Boolean = false
)

/**
 * Configuration for a live speaking coaching scenario.
 */
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

/**
 * Real-time events emitted by the Live AI client.
 */
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
    data class Error(val message: String, val isFatal: Boolean = false) : LiveSessionEvent
    data class Disconnected(val reason: String) : LiveSessionEvent
}
