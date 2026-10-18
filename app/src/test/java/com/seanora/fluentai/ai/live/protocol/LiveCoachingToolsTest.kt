package com.seanora.fluentai.ai.live.protocol

import com.seanora.fluentai.ai.live.LiveScenarioConfig
import com.seanora.fluentai.core.model.CorrectionCategory
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LiveCoachingToolsTest {

    private val config = LiveScenarioConfig(scenarioId = "s1", title = "Standup", systemPrompt = "Coach")

    @Test
    fun setupMessage_declaresCorrectionTool() {
        val setup = LiveSetupMessageFactory("models/test").create(config, null)["setup"]!!.jsonObject
        val declaration = setup["tools"]!!.jsonArray.single().jsonObject["functionDeclarations"]!!.jsonArray.single().jsonObject

        assertEquals(LiveCoachingTools.CORRECTION_TOOL_NAME, declaration["name"]!!.jsonPrimitive.content)
        val required = declaration["parameters"]!!.jsonObject["required"]!!.jsonArray.map { it.jsonPrimitive.content }
        assertTrue(required.containsAll(listOf("learner_said", "correction", "explanation", "category")))
    }

    @Test
    fun setupMessage_omitsToolsWhenDisabled() {
        val setup = LiveSetupMessageFactory("models/test")
            .create(config.copy(coachingToolsEnabled = false), null)["setup"]!!.jsonObject

        assertFalse(setup.containsKey("tools"))
    }

    @Test
    fun parser_emitsToolCallSignal() {
        val frame = """
            {"toolCall":{"functionCalls":[{"id":"call-1","name":"log_correction","args":{
                "learner_said":"since two years","correction":"for two years",
                "explanation":"Use for with durations","category":"GRAMMAR","trap_type":"since-for"}}]}}
        """.trimIndent()

        val signal = LiveServerMessageParser().parse(frame).single() as LiveServerSignal.ToolCall
        val call = signal.calls.single()

        assertEquals("call-1", call.id)
        assertEquals("log_correction", call.name)
        assertEquals("for two years", call.args["correction"])
    }

    @Test
    fun toCorrection_mapsArgumentsAndFallsBackToGrammar() {
        val correction = LiveCoachingTools.toCorrection(
            LiveFunctionCall(
                id = "call-1",
                name = LiveCoachingTools.CORRECTION_TOOL_NAME,
                args = mapOf("learner_said" to "discuss about", "correction" to "discuss", "category" to "unknown")
            )
        )!!

        assertEquals("discuss about", correction.learnerSaid)
        assertEquals("discuss", correction.correction)
        assertEquals(CorrectionCategory.GRAMMAR, correction.category)
        assertNull(correction.trapType)
    }

    @Test
    fun toCorrection_rejectsUnknownToolOrMissingFields() {
        assertNull(LiveCoachingTools.toCorrection(LiveFunctionCall("1", "other_tool", emptyMap())))
        assertNull(
            LiveCoachingTools.toCorrection(
                LiveFunctionCall("2", LiveCoachingTools.CORRECTION_TOOL_NAME, mapOf("learner_said" to "x"))
            )
        )
    }

    @Test
    fun toolResponse_matchesLiveApiShape() {
        val response = LiveClientMessageFactory.toolResponse("call-1", "log_correction", "logged")
        val functionResponse = (response["toolResponse"] as JsonObject)["functionResponses"]!!.jsonArray.single().jsonObject

        assertEquals("call-1", functionResponse["id"]!!.jsonPrimitive.content)
        assertEquals("log_correction", functionResponse["name"]!!.jsonPrimitive.content)
        assertEquals("logged", functionResponse["response"]!!.jsonObject["result"]!!.jsonPrimitive.content)
    }
}
