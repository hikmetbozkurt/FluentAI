package com.seanora.fluentai.ai.live.protocol

import com.seanora.fluentai.core.model.CorrectionCategory
import com.seanora.fluentai.core.model.LiveCorrection
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject

object LiveCoachingTools {

    const val CORRECTION_TOOL_NAME = "log_correction"
    const val ARG_LEARNER_SAID = "learner_said"
    const val ARG_CORRECTION = "correction"
    const val ARG_EXPLANATION = "explanation"
    const val ARG_CATEGORY = "category"
    const val ARG_TRAP_TYPE = "trap_type"

    fun declarations(): JsonArray = JsonArray(
        listOf(
            buildJsonObject {
                put("functionDeclarations", JsonArray(listOf(correctionDeclaration())))
            }
        )
    )

    fun toCorrection(call: LiveFunctionCall): LiveCorrection? {
        if (call.name != CORRECTION_TOOL_NAME) return null
        val learnerSaid = call.args[ARG_LEARNER_SAID]?.trim().orEmpty()
        val correction = call.args[ARG_CORRECTION]?.trim().orEmpty()
        if (learnerSaid.isEmpty() || correction.isEmpty()) return null
        return LiveCorrection(
            id = call.id,
            learnerSaid = learnerSaid,
            correction = correction,
            explanation = call.args[ARG_EXPLANATION]?.trim().orEmpty(),
            category = parseCategory(call.args[ARG_CATEGORY]),
            trapType = call.args[ARG_TRAP_TYPE]?.trim()?.takeIf(String::isNotEmpty)
        )
    }

    private fun parseCategory(raw: String?): CorrectionCategory =
        CorrectionCategory.entries.firstOrNull { it.name.equals(raw?.trim(), ignoreCase = true) }
            ?: CorrectionCategory.GRAMMAR

    private fun correctionDeclaration(): JsonObject = buildJsonObject {
        put("name", CORRECTION_TOOL_NAME)
        put(
            "description",
            "Record a correction you just gave the learner out loud. Call it every time you correct a grammar, vocabulary, word-choice, pronunciation or fluency error."
        )
        putJsonObject("parameters") {
            put("type", "OBJECT")
            putJsonObject("properties") {
                stringProperty(ARG_LEARNER_SAID, "The learner's phrase containing the error, as they said it.")
                stringProperty(ARG_CORRECTION, "The corrected, natural version of the phrase.")
                stringProperty(ARG_EXPLANATION, "One short sentence explaining the correction.")
                putJsonObject(ARG_CATEGORY) {
                    put("type", "STRING")
                    put("description", "The type of error.")
                    put("enum", JsonArray(CorrectionCategory.entries.map { JsonPrimitive(it.name) }))
                }
                stringProperty(ARG_TRAP_TYPE, "Optional short label for a Turkish transfer pattern, such as since-for or discuss-about.")
            }
            put(
                "required",
                JsonArray(listOf(ARG_LEARNER_SAID, ARG_CORRECTION, ARG_EXPLANATION, ARG_CATEGORY).map(::JsonPrimitive))
            )
        }
    }

    private fun JsonObjectBuilder.stringProperty(name: String, description: String) {
        putJsonObject(name) {
            put("type", "STRING")
            put("description", description)
        }
    }
}

data class LiveFunctionCall(
    val id: String,
    val name: String,
    val args: Map<String, String>
)
