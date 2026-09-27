package com.seanora.fluentai.ai.analysis

import android.util.Log
import com.seanora.fluentai.ai.live.TranscriptEntry
import com.seanora.fluentai.ai.live.TranscriptRole
import com.seanora.fluentai.core.model.CorrectionMode
import com.seanora.fluentai.core.model.SpeakingScenario
import com.seanora.fluentai.core.model.SpeakingSessionAnalysis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Gemini Flash-compatible implementation of SpeakingAnalysisClient per AGENTS.md Section 4.
 * Uses Gemini 2.5 Flash for post-session structured language evaluation with graceful
 * deterministic offline fallback (Section 5).
 */
@Singleton
class GeminiSpeakingAnalysisClient @Inject constructor(
    private val fallbackClient: MockSpeakingAnalysisClient,
    private val okHttpClient: OkHttpClient = defaultClient,
    private val apiKeyProvider: () -> String
) : SpeakingAnalysisClient {

    companion object {
        private const val TAG = "GeminiSpeakingAnalysis"
        private const val GEMINI_FLASH_MODEL = "gemini-2.5-flash"
        private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"

        val defaultClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
        }

        private val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    override suspend fun analyzeSession(
        scenario: SpeakingScenario,
        mode: CorrectionMode,
        transcript: List<TranscriptEntry>,
        durationSeconds: Int
    ): Result<SpeakingSessionAnalysis> {
        val apiKey = apiKeyProvider().trim()
        if (apiKey.isBlank()) {
            Log.d(TAG, "Gemini API key blank, using offline deterministic analyzer")
            return fallbackClient.analyzeSession(scenario, mode, transcript, durationSeconds)
        }

        val userEntries = transcript.filter { it.role == TranscriptRole.USER }
        if (userEntries.isEmpty()) {
            return fallbackClient.analyzeSession(scenario, mode, transcript, durationSeconds)
        }

        return withContext(Dispatchers.IO) {
            try {
                val prompt = buildAnalysisPrompt(scenario, mode, transcript, durationSeconds)
                val requestBody = buildRequestBody(prompt)

                val request = Request.Builder()
                    .url("$BASE_URL/$GEMINI_FLASH_MODEL:generateContent?key=$apiKey")
                    .post(requestBody.toRequestBody("application/json".toMediaType()))
                    .build()

                val response = okHttpClient.newCall(request).execute()
                if (!response.isSuccessful) {
                    Log.w(TAG, "Gemini analysis HTTP failed: code=${response.code}, falling back to offline analyzer")
                    return@withContext fallbackClient.analyzeSession(scenario, mode, transcript, durationSeconds)
                }

                val responseJson = response.body.string()
                if (responseJson.isNullOrBlank()) {
                    return@withContext fallbackClient.analyzeSession(scenario, mode, transcript, durationSeconds)
                }

                val parsed = parseGeminiResponse(responseJson)
                if (parsed != null) {
                    Result.success(
                        parsed.copy(
                            scenarioId = scenario.id,
                            scenarioTitle = scenario.title,
                            durationSeconds = durationSeconds,
                            turnsCount = userEntries.size
                        )
                    )
                } else {
                    Log.w(TAG, "Failed to parse structured JSON from Gemini, using offline fallback")
                    fallbackClient.analyzeSession(scenario, mode, transcript, durationSeconds)
                }
            } catch (e: Exception) {
                Log.w(TAG, "Exception during Gemini analysis: ${e.message}, using offline fallback")
                fallbackClient.analyzeSession(scenario, mode, transcript, durationSeconds)
            }
        }
    }

    private fun buildAnalysisPrompt(
        scenario: SpeakingScenario,
        mode: CorrectionMode,
        transcript: List<TranscriptEntry>,
        durationSeconds: Int
    ): String {
        val dialogueText = transcript.joinToString("\n") { "[${it.role}]: ${it.text}" }

        return """
You are an expert English language assessor analyzing a spoken dialogue between a Turkish white-collar professional and an AI Coach.
Scenario: "${scenario.title}" (${scenario.category.displayName} - CEFR ${scenario.cefrLevel})
Correction Mode: ${mode.displayName}
Session Duration: $durationSeconds seconds

Target Vocabulary to inspect: ${scenario.targetVocabIds.joinToString(", ")}
Target Grammar to inspect: ${scenario.targetGrammarIds.joinToString(", ")}

Dialogue Transcript:
$dialogueText

Evaluate the USER's spoken English performance strictly according to CEFR standards and Turkish learner transfer habits.
Ground every score and observation in the supplied USER turns. Do not reuse the example values below or invent utterances, errors, strengths, or metrics. When the transcript has limited evidence, score conservatively and state that limitation in the feedback.
Produce a single valid JSON object with the following exact schema:
{
  "id": "${java.util.UUID.randomUUID()}",
  "sessionId": "session_${System.currentTimeMillis()}",
  "scenarioId": "${scenario.id}",
  "scenarioTitle": "${scenario.title}",
  "timestamp": ${System.currentTimeMillis()},
  "durationSeconds": $durationSeconds,
  "turnsCount": ${transcript.count { it.role == TranscriptRole.USER }},
  "overallFeedback": "Concise paragraph summarizing user performance and communicative effectiveness.",
  "estimatedTurnCefr": "B2",
  "fluencyScore": 0.0,
  "vocabularyScore": 0.0,
  "grammarScore": 0.0,
  "coherenceScore": 0.0,
  "strengths": [
    "Specific positive observation 1",
    "Specific positive observation 2"
  ],
  "grammarObservations": [
    {
      "id": "g1",
      "userUtterance": "Exact utterance user said",
      "errorSnippet": "Specific error substring",
      "correctedSnippet": "Correct English phrasing",
      "explanation": "Why this error occurred (noting Turkish L1 interference if applicable)",
      "targetGrammarId": null,
      "trapType": "tense_duration"
    }
  ],
  "vocabularyObservations": [
    {
      "id": "v1",
      "usedWord": "Word user used",
      "suggestedBetterWord": "More executive or idiomatic collocation",
      "explanation": "Why the upgrade elevates their register",
      "targetVocabId": null,
      "register": "executive"
    }
  ],
  "naturalAlternatives": [
    {
      "originalUtterance": "User sentence",
      "naturalAlternative": "How a native executive speaker would state this naturally",
      "explanation": "Contextual nuance",
      "register": "business_casual"
    }
  ],
  "suggestedPractice": [
    {
      "title": "Practice topic",
      "description": "Short drill recommendation",
      "domain": "grammar",
      "contentId": null
    }
  ]
}

Only return the raw JSON object. Do not wrap in markdown or backticks.
""".trimIndent()
    }

    private fun buildRequestBody(prompt: String): String {
        val root = buildJsonObject {
            putJsonArray("contents") {
                add(
                    buildJsonObject {
                        putJsonArray("parts") {
                            add(
                                buildJsonObject {
                                    put("text", prompt)
                                }
                            )
                        }
                    }
                )
            }
            putJsonObject("generationConfig") {
                put("responseMimeType", "application/json")
                put("temperature", 0.3)
            }
        }
        return root.toString()
    }

    internal fun parseGeminiResponse(responseJson: String): SpeakingSessionAnalysis? {
        return try {
            val root = json.parseToJsonElement(responseJson) as? kotlinx.serialization.json.JsonObject
            val candidates = root?.get("candidates") as? kotlinx.serialization.json.JsonArray
            val firstCandidate = candidates?.firstOrNull() as? kotlinx.serialization.json.JsonObject
            val content = firstCandidate?.get("content") as? kotlinx.serialization.json.JsonObject
            val parts = content?.get("parts") as? kotlinx.serialization.json.JsonArray
            val textElement = (parts?.firstOrNull() as? kotlinx.serialization.json.JsonObject)?.get("text")
            val rawText = when (textElement) {
                is kotlinx.serialization.json.JsonPrimitive -> textElement.content
                else -> textElement?.toString()
            }

            if (!rawText.isNullOrBlank()) {
                val cleanText = rawText
                    .trim()
                    .removePrefix("```json")
                    .removePrefix("```")
                    .removeSuffix("```")
                    .trim()
                json.decodeFromString<SpeakingSessionAnalysis>(cleanText)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error deserializing Gemini analysis response: ${e.message}")
            null
        }
    }
}
