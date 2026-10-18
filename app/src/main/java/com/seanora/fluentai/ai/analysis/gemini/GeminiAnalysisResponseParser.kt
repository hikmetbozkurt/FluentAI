package com.seanora.fluentai.ai.analysis.gemini

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class GeminiAnalysisResponseParser {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    fun parse(responseBody: String): GeminiAnalysisPayload? {
        val text = extractCandidateText(responseBody) ?: return null
        return runCatching { json.decodeFromString<GeminiAnalysisPayload>(stripCodeFence(text)) }.getOrNull()
    }

    private fun extractCandidateText(responseBody: String): String? = runCatching {
        val root = json.parseToJsonElement(responseBody) as? JsonObject
        val candidate = (root?.get("candidates") as? JsonArray)?.firstOrNull() as? JsonObject
        val parts = (candidate?.get("content") as? JsonObject)?.get("parts") as? JsonArray
        val text = (parts?.firstOrNull() as? JsonObject)?.get("text") as? JsonPrimitive
        text?.content?.takeIf { it.isNotBlank() }
    }.getOrNull()

    private fun stripCodeFence(text: String): String = text.trim()
        .removePrefix("```json")
        .removePrefix("```")
        .removeSuffix("```")
        .trim()
}
