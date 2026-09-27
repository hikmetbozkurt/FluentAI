package com.seanora.fluentai.ai.live

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface isolating live AI speaking models behind an abstraction per AGENTS.md Section 4 & Section 11.
 * Never couple domain or UI logic to a concrete model name or direct WebSocket implementation.
 */
interface LiveAiClient {
    val connectionState: StateFlow<LiveConnectionState>
    val sessionEvents: SharedFlow<LiveSessionEvent>

    suspend fun connect(config: LiveScenarioConfig): Result<Unit>
    suspend fun sendAudioChunk(pcmData: ByteArray)
    suspend fun sendTextMessage(text: String)
    suspend fun sendBargeInInterrupt()
    suspend fun disconnect()
}
