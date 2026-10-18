package com.seanora.fluentai.ai.analysis

import com.seanora.fluentai.ai.analysis.gemini.AnalysisObservationSanitizer
import com.seanora.fluentai.ai.analysis.gemini.AnalysisSessionMetadata
import com.seanora.fluentai.ai.analysis.gemini.GeminiAnalysisRequestFactory
import com.seanora.fluentai.ai.analysis.gemini.GeminiAnalysisResponseParser
import com.seanora.fluentai.ai.analysis.gemini.HttpRetryPolicy
import com.seanora.fluentai.ai.analysis.gemini.SpeakingAnalysisPromptBuilder
import com.seanora.fluentai.ai.analysis.gemini.toDomain
import com.seanora.fluentai.ai.live.TranscriptEntry
import com.seanora.fluentai.ai.live.TranscriptRole
import com.seanora.fluentai.core.common.logging.AppLog
import com.seanora.fluentai.core.model.SpeakingScenario
import com.seanora.fluentai.core.model.SpeakingSessionAnalysis
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GeminiSpeakingAnalysisClient(
    private val fallbackClient: SpeakingAnalysisClient,
    private val okHttpClient: OkHttpClient,
    private val apiKeyProvider: () -> String,
    private val modelName: String = DEFAULT_MODEL,
    private val retryPolicy: HttpRetryPolicy = HttpRetryPolicy(),
    private val promptBuilder: SpeakingAnalysisPromptBuilder = SpeakingAnalysisPromptBuilder(),
    private val requestFactory: GeminiAnalysisRequestFactory = GeminiAnalysisRequestFactory(),
    private val responseParser: GeminiAnalysisResponseParser = GeminiAnalysisResponseParser(),
    private val sanitizer: AnalysisObservationSanitizer = AnalysisObservationSanitizer()
) : SpeakingAnalysisClient {

    companion object {
        const val DEFAULT_MODEL = "gemini-2.5-flash"
        private const val TAG = "GeminiSpeakingAnalysis"
        private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"
        private const val API_KEY_HEADER = "x-goog-api-key"
        private val JSON_MEDIA_TYPE = "application/json".toMediaType()
    }

    override suspend fun analyzeSession(
        scenario: SpeakingScenario,
        transcript: List<TranscriptEntry>,
        durationSeconds: Int
    ): Result<SpeakingSessionAnalysis> {
        val apiKey = apiKeyProvider().trim()
        val learnerTurns = transcript.count { it.role == TranscriptRole.USER && it.text.isNotBlank() }
        if (apiKey.isBlank() || learnerTurns == 0) {
            return fallbackClient.analyzeSession(scenario, transcript, durationSeconds)
        }

        val prompt = promptBuilder.build(scenario, transcript, durationSeconds)
        val request = Request.Builder()
            .url("$BASE_URL/$modelName:generateContent")
            .header(API_KEY_HEADER, apiKey)
            .post(requestFactory.create(prompt).toRequestBody(JSON_MEDIA_TYPE))
            .build()

        val responseBody = executeWithRetry(request)
        val payload = responseBody?.let(responseParser::parse)
        if (payload == null) {
            AppLog.w(TAG) { "Online analysis unavailable; using offline analyzer" }
            return fallbackClient.analyzeSession(scenario, transcript, durationSeconds)
        }

        val metadata = AnalysisSessionMetadata(
            analysisId = UUID.randomUUID().toString(),
            sessionId = "session_${System.currentTimeMillis()}",
            scenarioId = scenario.id,
            scenarioTitle = scenario.title,
            timestampMs = System.currentTimeMillis(),
            durationSeconds = durationSeconds,
            learnerTurns = learnerTurns,
            promptVersion = SpeakingAnalysisPromptBuilder.PROMPT_VERSION
        )
        return Result.success(sanitizer.sanitize(payload, scenario).toDomain(metadata))
    }

    private suspend fun executeWithRetry(request: Request): String? {
        for (attempt in 1..retryPolicy.maxAttempts) {
            val outcome = runCatching { okHttpClient.newCall(request).await().use { it.code to it.body.string() } }
                .onFailure { if (it is CancellationException) throw it }
            val (code, body) = outcome.getOrElse { error ->
                AppLog.w(TAG, error) { "Analysis request failed (attempt $attempt)" }
                if (attempt < retryPolicy.maxAttempts) delay(retryPolicy.delayBeforeRetry(attempt))
                null to null
            }
            when {
                code == null -> continue
                code in 200..299 -> return body
                retryPolicy.shouldRetry(code) && attempt < retryPolicy.maxAttempts -> {
                    AppLog.w(TAG) { "Analysis HTTP $code, retrying (attempt $attempt)" }
                    delay(retryPolicy.delayBeforeRetry(attempt))
                }
                else -> {
                    AppLog.w(TAG) { "Analysis HTTP $code, giving up" }
                    return null
                }
            }
        }
        return null
    }

    private suspend fun Call.await(): Response = suspendCancellableCoroutine { continuation ->
        continuation.invokeOnCancellation { cancel() }
        enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (continuation.isActive) continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (continuation.isActive) continuation.resume(response) else response.close()
            }
        })
    }
}
