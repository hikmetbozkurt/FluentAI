package com.seanora.fluentai.ai.analysis.gemini

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject

class GeminiAnalysisRequestFactory(private val temperature: Double = DEFAULT_TEMPERATURE) {

    fun create(prompt: String): String = buildJsonObject {
        putJsonArray("contents") {
            add(buildJsonObject {
                put("role", "user")
                putJsonArray("parts") { add(buildJsonObject { put("text", prompt) }) }
            })
        }
        putJsonObject("generationConfig") {
            put("responseMimeType", "application/json")
            put("responseJsonSchema", AnalysisResponseSchema.root)
            put("temperature", temperature)
        }
    }.toString()

    private companion object {
        const val DEFAULT_TEMPERATURE = 0.3
    }
}

internal object AnalysisResponseSchema {

    val root: JsonObject = objectSchema(
        required = listOf(
            "overallFeedback", "estimatedTurnCefr", "fluencyScore", "vocabularyScore",
            "grammarScore", "coherenceScore", "strengths", "grammarObservations",
            "vocabularyObservations", "naturalAlternatives", "suggestedPractice"
        )
    ) {
        putJsonObject("overallFeedback") { put("type", "string") }
        putJsonObject("estimatedTurnCefr") {
            put("type", "string")
            put("enum", stringArray("A2", "B1", "B2", "C1", "C2"))
        }
        listOf("fluencyScore", "vocabularyScore", "grammarScore", "coherenceScore").forEach { name ->
            putJsonObject(name) {
                put("type", "number")
                put("minimum", 0)
                put("maximum", 1)
            }
        }
        put("strengths", arraySchema(buildJsonObject { put("type", "string") }))
        put(
            "grammarObservations",
            arraySchema(
                objectSchema(required = listOf("id", "userUtterance", "errorSnippet", "correctedSnippet", "explanation")) {
                    stringProperties("id", "userUtterance", "errorSnippet", "correctedSnippet", "explanation")
                    nullableStringProperties("targetGrammarId", "trapType")
                }
            )
        )
        put(
            "vocabularyObservations",
            arraySchema(
                objectSchema(required = listOf("id", "usedWord", "suggestedBetterWord", "explanation")) {
                    stringProperties("id", "usedWord", "suggestedBetterWord", "explanation", "register")
                    nullableStringProperties("targetVocabId")
                }
            )
        )
        put(
            "naturalAlternatives",
            arraySchema(
                objectSchema(required = listOf("originalUtterance", "naturalAlternative", "explanation")) {
                    stringProperties("originalUtterance", "naturalAlternative", "explanation", "register")
                }
            )
        )
        put(
            "suggestedPractice",
            arraySchema(
                objectSchema(required = listOf("title", "description", "domain")) {
                    stringProperties("title", "description")
                    putJsonObject("domain") {
                        put("type", "string")
                        put("enum", stringArray("grammar", "vocabulary", "review", "speaking"))
                    }
                    nullableStringProperties("contentId")
                }
            )
        )
    }

    private fun objectSchema(required: List<String>, properties: JsonObjectBuilder.() -> Unit): JsonObject =
        buildJsonObject {
            put("type", "object")
            putJsonObject("properties", properties)
            put("required", stringArray(*required.toTypedArray()))
        }

    private fun arraySchema(items: JsonObject): JsonObject = buildJsonObject {
        put("type", "array")
        put("items", items)
    }

    private fun JsonObjectBuilder.stringProperties(vararg names: String) {
        names.forEach { name -> putJsonObject(name) { put("type", "string") } }
    }

    private fun JsonObjectBuilder.nullableStringProperties(vararg names: String) {
        names.forEach { name -> putJsonObject(name) { put("type", stringArray("string", "null")) } }
    }

    private fun stringArray(vararg values: String) = JsonArray(values.map(::JsonPrimitive))
}
