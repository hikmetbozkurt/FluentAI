package com.seanora.fluentai.domain.listening

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.ListeningComprehensionQuestion
import com.seanora.fluentai.core.model.ListeningScenario
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.Speaker
import com.seanora.fluentai.core.model.TranscriptItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ListeningFeatureEngineTest {
    private val engine = ListeningFeatureEngine()

    @Test
    fun dailySelection_prefersExactLevelIsOrderIndependentAndFallsBackNearest() {
        val exactA = scenario("listening.b2.a", "B2")
        val exactB = scenario("listening.b2.b", "B2")
        val lower = scenario("listening.b1.a", "B1")
        val exact = engine.selectDaily(listOf(exactB, lower, exactA), "B2", "2026-09-25")
        val repeated = engine.selectDaily(listOf(exactA, exactB, lower), "B2", "2026-09-25")

        assertEquals("B2", exact?.cefrLevel)
        assertEquals(exact?.id, repeated?.id)
        assertEquals(lower.id, engine.selectDaily(listOf(lower, scenario("listening.c1.a", "C1")), "B2", "2026-09-25")?.id)
        assertNull(engine.selectDaily(emptyList(), "B2", "2026-09-25"))
    }

    @Test
    fun continue_usesLatestValidListeningEvidenceAndIgnoresStaleOrOtherDomains() {
        val valid = scenario("listening.b1.valid", "B1")
        val evidence = listOf(
            LearningEvidence(contentId = "listening.stale", domain = "listening", activityType = "question", isCorrect = true, score = 1f, timestamp = 30L),
            LearningEvidence(contentId = valid.id, domain = "listening", activityType = "question", isCorrect = false, score = 0f, timestamp = 20L),
            LearningEvidence(contentId = "reading.other", domain = "reading", activityType = "question", isCorrect = true, score = 1f, timestamp = 40L),
        )

        assertEquals(valid.id, engine.resolveContinue(listOf(valid), evidence)?.id)
        assertNull(engine.resolveContinue(listOf(valid), evidence.filter { it.contentId != valid.id }))
    }

    @Test
    fun dictation_usesOnlyTimestampedTranscriptAndNormalizesCasePunctuationWhitespace() {
        val eligible = scenario("listening.b1.valid", "B1")
        val invalid = eligible.copy(
            id = "listening.invalid",
            transcriptItems = listOf(eligible.transcriptItems.single().copy(endMs = 0L)),
        )

        val items = engine.dictationItems(listOf(invalid, eligible))
        assertEquals(listOf(eligible.id), items.map { it.scenario.id })

        val correct = engine.evaluateDictation("  HELLO,   team! ", "Hello team.")
        assertTrue(correct.isCorrect)
        assertEquals(2, correct.matchedTokens)

        val incorrect = engine.evaluateDictation("Hello time", "Hello team.")
        assertFalse(incorrect.isCorrect)
        assertEquals(1, incorrect.matchedTokens)
        assertEquals(2, incorrect.totalTokens)
    }

    @Test
    fun skillsAndWeakSpots_areDerivedOnlyFromRealQuestionsAndLearningState() {
        val weak = scenario("listening.b1.weak", "B1", hasQuestion = true)
        val mastered = scenario("listening.b2.mastered", "B2")
        val skills = engine.deriveSkills(listOf(weak, mastered))
        assertEquals(listOf("comprehension"), skills.map { it.id })
        assertEquals(1, skills.single().items.size)

        val snapshots = listOf(mastery(weak.id, MasteryStatus.PRACTICED), mastery(mastered.id, MasteryStatus.MASTERED))
        val due = listOf(ReviewSchedule(weak.id, "listening", 50L, 1f))
        val result = engine.weakSpots(listOf(weak, mastered), snapshots, due, 100L)

        assertEquals(listOf(weak.id), result.map { it.scenario.id })
        assertTrue(result.single().isReviewDue)
    }

    private fun scenario(id: String, level: String, hasQuestion: Boolean = false) = ListeningScenario(
        id = id,
        title = id,
        cefrLevel = level,
        category = "work",
        scenarioContext = "A real workplace exchange.",
        speakers = listOf(Speaker("speaker", "Speaker", "Colleague")),
        audioRef = "audio/$id.mp3",
        durationSeconds = 10,
        transcriptItems = listOf(TranscriptItem(1, "speaker", 0L, 3000L, "Hello team.", "Merhaba ekip.")),
        keyVocabulary = emptyList(),
        comprehensionQuestions = if (hasQuestion) listOf(
            ListeningComprehensionQuestion(
                id = "$id.q1",
                questionEn = "What was said?",
                options = listOf("Hello", "Goodbye"),
                correctAnswer = "Hello",
                explanationEn = "The speaker said hello.",
                explanationTr = "Konuşmacı merhaba dedi.",
            ),
        ) else emptyList(),
        topicTags = listOf("work"),
        relatedIds = emptyList(),
    )

    private fun mastery(id: String, status: MasteryStatus) = MasterySnapshot(
        contentId = id,
        domain = "listening",
        level = if (status == MasteryStatus.MASTERED) 0.9f else 0.4f,
        confidence = 0.5f,
        totalAttempts = 2,
        correctAttempts = 1,
        lastAttemptTimestamp = 1L,
        lastDecayTimestamp = 1L,
        status = status,
    )
}
