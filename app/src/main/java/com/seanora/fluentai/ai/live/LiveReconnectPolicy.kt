package com.seanora.fluentai.ai.live

import kotlin.math.min

data class LiveReconnectPolicy(
    val maxAttempts: Int = 3,
    val initialDelayMs: Long = 500L,
    val maxDelayMs: Long = 4_000L
) {
    fun delayBeforeAttempt(attempt: Int): Long {
        val exponent = (attempt - 1).coerceAtLeast(0)
        return min(maxDelayMs, initialDelayMs shl exponent)
    }
}
