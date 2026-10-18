package com.seanora.fluentai.ai.live

data class LiveSessionMetrics(
    val startedAtMs: Long? = null,
    val firstCoachAudioLatencyMs: Long? = null,
    val coachTurns: Int = 0,
    val learnerTurns: Int = 0,
    val interruptions: Int = 0,
    val reconnects: Int = 0,
    val tokenUsage: LiveTokenUsage = LiveTokenUsage()
)
