package com.seanora.fluentai.ai.analysis.eval

import com.seanora.fluentai.ai.analysis.GeminiSpeakingAnalysisClient
import com.seanora.fluentai.ai.analysis.MockSpeakingAnalysisClient
import com.seanora.fluentai.ai.analysis.gemini.SpeakingAnalysisPromptBuilder
import com.seanora.fluentai.ai.live.TranscriptEntry
import com.seanora.fluentai.ai.live.TranscriptRole
import com.seanora.fluentai.core.model.AnalysisSource
import com.seanora.fluentai.core.model.SpeakingCategory
import com.seanora.fluentai.core.model.SpeakingScenario
import com.seanora.fluentai.core.model.SpeakingSessionAnalysis
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Test
import java.util.concurrent.TimeUnit

class SpeakingAnalysisEvalTest {

    @Serializable
    private data class GoldenCase(val id: String, val utterance: String, val expectedErrors: List<String>)

    private data class CaseOutcome(val case: GoldenCase, val detected: Int, val falsePositives: Int)

    private val apiKey: String = System.getenv(API_KEY_ENV).orEmpty()

    private val scenario = SpeakingScenario(
        id = "speaking.eval.workplace",
        title = "Workplace Update",
        category = SpeakingCategory.MEETING,
        cefrLevel = "B1",
        systemPrompt = "Workplace small talk.",
        contextDescription = "A learner describes their work week."
    )

    @Test
    fun geminiAnalysis_meetsRecallAndPrecisionTargetsOnGoldenSet() = runBlocking {
        assumeTrue("Set $API_KEY_ENV to run the live evaluation", apiKey.isNotBlank())

        val client = GeminiSpeakingAnalysisClient(
            fallbackClient = MockSpeakingAnalysisClient(),
            okHttpClient = OkHttpClient.Builder().readTimeout(REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS).build(),
            apiKeyProvider = { apiKey }
        )

        val outcomes = loadGoldenCases().map { case ->
            val analysis = client.analyzeSession(scenario, transcriptFor(case), durationSeconds = 30).getOrThrow()
            check(analysis.source == AnalysisSource.AI_MODEL) { "Case ${case.id} fell back to the offline analyzer" }
            evaluate(case, analysis)
        }

        val expectedTotal = outcomes.sumOf { it.case.expectedErrors.size }
        val detectedTotal = outcomes.sumOf { it.detected }
        val reportedTotal = detectedTotal + outcomes.sumOf { it.falsePositives }
        val recall = detectedTotal.toDouble() / expectedTotal
        val precision = if (reportedTotal == 0) 1.0 else detectedTotal.toDouble() / reportedTotal

        println("Speaking analysis eval (${SpeakingAnalysisPromptBuilder.PROMPT_VERSION})")
        outcomes.forEach { println("  ${it.case.id}: detected=${it.detected}/${it.case.expectedErrors.size}, extra=${it.falsePositives}") }
        println("  recall=%.2f precision=%.2f".format(recall, precision))

        assertTrue("Recall $recall is below $MIN_RECALL", recall >= MIN_RECALL)
        assertTrue("Precision $precision is below $MIN_PRECISION", precision >= MIN_PRECISION)
    }

    private fun evaluate(case: GoldenCase, analysis: SpeakingSessionAnalysis): CaseOutcome {
        val reported = analysis.grammarObservations.map { "${it.errorSnippet} ${it.userUtterance}".lowercase() }
        val detected = case.expectedErrors.count { expected -> reported.any { it.contains(expected.lowercase()) } }
        val matchedReports = reported.count { report -> case.expectedErrors.any { report.contains(it.lowercase()) } }
        return CaseOutcome(case, detected, falsePositives = reported.size - matchedReports)
    }

    private fun transcriptFor(case: GoldenCase) = listOf(
        TranscriptEntry(id = "coach-1", role = TranscriptRole.COACH, text = "How has your week been at work?"),
        TranscriptEntry(id = "learner-1", role = TranscriptRole.USER, text = case.utterance)
    )

    private fun loadGoldenCases(): List<GoldenCase> {
        val raw = requireNotNull(javaClass.classLoader?.getResource(GOLDEN_SET_PATH)) { "Missing $GOLDEN_SET_PATH" }.readText()
        return Json.decodeFromString(raw)
    }

    private companion object {
        const val API_KEY_ENV = "GEMINI_API_KEY"
        const val GOLDEN_SET_PATH = "evals/speaking_analysis_golden.json"
        const val REQUEST_TIMEOUT_SECONDS = 60L
        const val MIN_RECALL = 0.7
        const val MIN_PRECISION = 0.6
    }
}
