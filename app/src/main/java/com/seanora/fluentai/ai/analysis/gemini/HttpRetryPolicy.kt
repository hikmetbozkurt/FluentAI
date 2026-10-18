package com.seanora.fluentai.ai.analysis.gemini

data class HttpRetryPolicy(
    val maxAttempts: Int = 3,
    val initialDelayMs: Long = 750L,
    val retryableStatusCodes: Set<Int> = setOf(408, 429, 500, 502, 503, 504)
) {
    fun shouldRetry(statusCode: Int): Boolean = statusCode in retryableStatusCodes

    fun delayBeforeRetry(attempt: Int): Long = initialDelayMs shl (attempt - 1).coerceAtLeast(0)
}
