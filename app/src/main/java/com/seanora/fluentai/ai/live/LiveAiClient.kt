package com.seanora.fluentai.ai.live

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface LiveAiClient {
    val connectionState: StateFlow<LiveConnectionState>
    val sessionEvents: SharedFlow<LiveSessionEvent>

    suspend fun connect(config: LiveScenarioConfig): Result<Unit>
    suspend fun sendAudioChunk(pcmData: ByteArray)
    suspend fun sendAudioStreamEnd()
    suspend fun sendTextMessage(text: String)
    suspend fun interruptModelTurn()
    suspend fun disconnect()
}
