package com.seanora.fluentai.ai.analysis

import com.seanora.fluentai.ai.analysis.gemini.AnalysisObservationSanitizer
import com.seanora.fluentai.ai.analysis.gemini.GeminiAnalysisPayload
import com.seanora.fluentai.ai.analysis.gemini.GeminiAnalysisResponseParser
import com.seanora.fluentai.ai.live.TranscriptEntry
import com.seanora.fluentai.ai.live.TranscriptRole
import com.seanora.fluentai.core.model.CorrectionMode
import com.seanora.fluentai.core.model.SpeakingCategory
import com.seanora.fluentai.core.model.SpeakingScenario
import com.seanora.fluentai.core.model.SpeakingGrammarObservation
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SpeakingAnalysisClientTest {

    private val mockClient = MockSpeakingAnalysisClient()

    private val testScenario = SpeakingScenario(
        id = "speaking.b2.meeting.standup",
        title = "Agile Standup & Blockers",
        category = SpeakingCategory.MEETING,
        cefrLevel = "B2",
        systemPrompt = "Scrum master daily standup.",
        contextDescription = "Sprint status update.",
        targetVocabIds = listOf("vocab.bottleneck", "vocab.escalate"),
        targetGrammarIds = listOf("grammar.present-perfect-vs-past-simple"),
        suggestedStarterPhrases = listOf("Yesterday I deployed the auth service.")
    )

    @Test
    fun mockClient_detectsTurkishTrapsAndGeneratesStructuredReport() = runTest {
        val transcript = listOf(
            TranscriptEntry(id = "1", role = TranscriptRole.COACH, text = "Good morning! What did you work on yesterday?"),
            TranscriptEntry(id = "2", role = TranscriptRole.USER, text = "I have lived in Istanbul since 5 years and yesterday I made an interview for new job. Also I am agree with your proposal."),
            TranscriptEntry(id = "3", role = TranscriptRole.COACH, text = "That sounds like a busy day. How did the interview go?"),
            TranscriptEntry(id = "4", role = TranscriptRole.USER, text = "We have a big problem with database latency because of this.")
        )

        val result = mockClient.analyzeSession(testScenario, CorrectionMode.COACH, transcript, 120)
        assertTrue(result.isSuccess)

        val analysis = result.getOrThrow()
        assertEquals("speaking.b2.meeting.standup", analysis.scenarioId)
        assertEquals(2, analysis.turnsCount)
        assertTrue(analysis.strengths.isNotEmpty())

        // Verify Turkish traps detected
        val traps = analysis.grammarObservations.mapNotNull { it.trapType }
        assertTrue("Expected tense_duration trap", traps.contains("tense_duration"))
        assertTrue("Expected be_verb_overuse trap", traps.contains("be_verb_overuse"))
        assertTrue("Expected collocation_transfer trap", traps.contains("collocation_transfer"))

        // Verify vocabulary upgrades detected
        val vocabUpgrades = analysis.vocabularyObservations.map { it.suggestedBetterWord }
        assertTrue("Expected bottleneck upgrade", vocabUpgrades.any { it.contains("bottleneck") })

        // Verify suggested practice contains curriculum links
        val practiceContentIds = analysis.suggestedPractice.mapNotNull { it.contentId }
        assertTrue(practiceContentIds.contains("grammar.present-perfect-vs-past-simple"))
        assertTrue(practiceContentIds.contains("vocab.bottleneck"))
        assertTrue(practiceContentIds.contains("review.mistake_bank"))

        // Verify natural alternatives
        assertTrue(analysis.naturalAlternatives.isNotEmpty())
    }

    @Test
    fun geminiClient_fallsBackToMockWhenApiKeyIsBlank() = runTest {
        val geminiClient = GeminiSpeakingAnalysisClient(
            fallbackClient = mockClient,
            okHttpClient = OkHttpClient(),
            apiKeyProvider = { "" }
        )

        val transcript = listOf(
            TranscriptEntry(id = "1", role = TranscriptRole.USER, text = "I agree that we need to fix the bug.")
        )

        val result = geminiClient.analyzeSession(testScenario, CorrectionMode.FLOW, transcript, 45)
        assertTrue(result.isSuccess)
        val analysis = result.getOrThrow()
        assertNotNull(analysis.id)
        assertEquals(1, analysis.turnsCount)
    }

    @Test
    fun offlineAnalysis_scoresAreDerivedFromActualLearnerTurns() = runTest {
        val fragmented = listOf(
            TranscriptEntry(id = "1", role = TranscriptRole.USER, text = "Yes"),
            TranscriptEntry(id = "2", role = TranscriptRole.USER, text = "Maybe"),
            TranscriptEntry(id = "3", role = TranscriptRole.USER, text = "Good")
        )
        val developed = listOf(
            TranscriptEntry(id = "1", role = TranscriptRole.USER, text = "I deployed the authentication service yesterday, and the release completed successfully."),
            TranscriptEntry(id = "2", role = TranscriptRole.USER, text = "The remaining bottleneck is database latency, so I am coordinating a measured optimization plan."),
            TranscriptEntry(id = "3", role = TranscriptRole.USER, text = "Consequently, the team can validate each change before we expand the rollout.")
        )

        val fragmentedAnalysis = mockClient.analyzeSession(testScenario, CorrectionMode.FLOW, fragmented, 45).getOrThrow()
        val developedAnalysis = mockClient.analyzeSession(testScenario, CorrectionMode.FLOW, developed, 45).getOrThrow()

        assertTrue(developedAnalysis.fluencyScore > fragmentedAnalysis.fluencyScore)
        assertTrue(developedAnalysis.vocabularyScore > fragmentedAnalysis.vocabularyScore)
        assertTrue(developedAnalysis.coherenceScore > fragmentedAnalysis.coherenceScore)
    }

    @Test
    fun offlineAnalysis_rejectsSessionWithoutLearnerSpeech() = runTest {
        val result = mockClient.analyzeSession(
            testScenario,
            CorrectionMode.COACH,
            listOf(TranscriptEntry(id = "coach", role = TranscriptRole.COACH, text = "Hello, ready to begin?")),
            15
        )

        assertTrue(result.isFailure)
    }

    @Test
    fun responseParser_parsesJsonAndStripsMarkdownFences() {
        val parser = GeminiAnalysisResponseParser()

        val rawApiResponse = """
            {
              "candidates": [
                {
                  "content": {
                    "parts": [
                      {
                        "text": "```json\n{\n  \"id\": \"analysis-123\",\n  \"sessionId\": \"session-456\",\n  \"scenarioId\": \"speaking.b2.meeting.standup\",\n  \"scenarioTitle\": \"Agile Standup\",\n  \"timestamp\": 1700000000000,\n  \"durationSeconds\": 60,\n  \"turnsCount\": 2,\n  \"overallFeedback\": \"Good job on speaking clearly.\",\n  \"estimatedTurnCefr\": \"B2\",\n  \"fluencyScore\": 0.85,\n  \"vocabularyScore\": 0.80,\n  \"grammarScore\": 0.75,\n  \"coherenceScore\": 0.80,\n  \"strengths\": [\"Clear cadence\"],\n  \"grammarObservations\": [],\n  \"vocabularyObservations\": [],\n  \"naturalAlternatives\": [],\n  \"suggestedPractice\": []\n}\n```"
                      }
                    ]
                  }
                }
              ]
            }
        """.trimIndent()

        val parsed = parser.parse(rawApiResponse)
        assertNotNull(parsed)
        assertEquals("Good job on speaking clearly.", parsed!!.overallFeedback)
        assertEquals("B2", parsed.estimatedTurnCefr)
        assertEquals(0.85f, parsed.fluencyScore, 0.01f)
    }

    @Test
    fun responseParser_returnsNullOnInvalidJson() {
        val parser = GeminiAnalysisResponseParser()

        val invalidApiResponse = """
            {
              "candidates": [
                {
                  "content": {
                    "parts": [
                      {
                        "text": "I could not analyze your session."
                      }
                    ]
                  }
                }
              ]
            }
        """.trimIndent()

        val parsed = parser.parse(invalidApiResponse)
        org.junit.Assert.assertNull(parsed)
    }

    @Test
    fun sanitizer_dropsTargetIdsOutsideScenarioAndClampsScores() {
        val payload = GeminiAnalysisPayload(
            overallFeedback = "ok",
            estimatedTurnCefr = "B2",
            fluencyScore = 1.4f,
            vocabularyScore = -0.2f,
            grammarScore = 0.5f,
            coherenceScore = 0.5f,
            grammarObservations = listOf(
                SpeakingGrammarObservation(
                    id = "g1",
                    userUtterance = "since two years",
                    errorSnippet = "since",
                    correctedSnippet = "for",
                    explanation = "Duration uses for.",
                    targetGrammarId = "grammar.invented-by-model",
                    trapType = "Tense Duration!"
                ),
                SpeakingGrammarObservation(
                    id = "g2",
                    userUtterance = "I have went",
                    errorSnippet = "have went",
                    correctedSnippet = "have gone",
                    explanation = "Past participle.",
                    targetGrammarId = "grammar.present-perfect-vs-past-simple",
                    trapType = "past_participle"
                )
            )
        )

        val sanitized = AnalysisObservationSanitizer().sanitize(payload, testScenario)

        assertEquals(1f, sanitized.fluencyScore, 0.0f)
        assertEquals(0f, sanitized.vocabularyScore, 0.0f)
        org.junit.Assert.assertNull(sanitized.grammarObservations[0].targetGrammarId)
        org.junit.Assert.assertNull(sanitized.grammarObservations[0].trapType)
        assertEquals("grammar.present-perfect-vs-past-simple", sanitized.grammarObservations[1].targetGrammarId)
        assertEquals("past_participle", sanitized.grammarObservations[1].trapType)
    }
}
