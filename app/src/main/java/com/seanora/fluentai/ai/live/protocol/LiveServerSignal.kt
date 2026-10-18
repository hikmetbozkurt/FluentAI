package com.seanora.fluentai.ai.live.protocol

import com.seanora.fluentai.ai.live.LiveSessionEvent
import com.seanora.fluentai.ai.live.LiveTokenUsage

sealed interface LiveServerSignal {
    data object SetupComplete : LiveServerSignal
    data class ServerError(val code: String?, val status: String?, val message: String) : LiveServerSignal
    data class GoAway(val timeLeftMs: Long?) : LiveServerSignal
    data class ResumptionUpdate(val handle: String?, val resumable: Boolean) : LiveServerSignal
    data class Usage(val usage: LiveTokenUsage) : LiveServerSignal
    data class Event(val event: LiveSessionEvent) : LiveServerSignal
}
