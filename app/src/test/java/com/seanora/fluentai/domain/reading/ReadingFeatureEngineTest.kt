package com.seanora.fluentai.domain.reading

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.ReadingArticle
import com.seanora.fluentai.core.model.ReadingComprehensionQuestion
import com.seanora.fluentai.core.model.ReadingParagraph
import com.seanora.fluentai.core.model.ReviewSchedule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ReadingFeatureEngineTest {
    private val engine = ReadingFeatureEngine()

    @Test
    fun dailySelection_prefersExactLevelAndIsDeterministicForDateAndDataset() {
        val articles = listOf(article("reading.b1.one", "B1"), article("reading.b2.two", "B2"), article("reading.b2.one", "B2"))

        val first = engine.selectDaily(articles, "B2", "2026-09-25")
        val second = engine.selectDaily(articles.reversed(), "B2", "2026-09-25")

        assertEquals("B2", first?.cefrLevel)
        assertEquals(first?.id, second?.id)
    }

    @Test
    fun dailySelection_usesNearestCefrFallbackAndHandlesNoContent() {
        val articles = listOf(article("reading.b1.one", "B1"), article("reading.c1.one", "C1"))

        assertEquals("reading.b1.one", engine.selectDaily(articles, "B2", "2026-09-25")?.id)
        assertNull(engine.selectDaily(emptyList(), "B2", "2026-09-25"))
    }

    @Test
    fun continue_usesLatestValidReadingEvidenceAndRejectsStaleIds() {
        val valid = article("reading.b2.valid", "B2")
        val evidence = listOf(
            LearningEvidence(contentId = "reading.stale", domain = "reading", activityType = "comprehension", isCorrect = true, score = 1f, timestamp = 30L),
            LearningEvidence(contentId = valid.id, domain = "reading", activityType = "comprehension", isCorrect = false, score = 0f, timestamp = 20L),
            LearningEvidence(contentId = "listening.other", domain = "listening", activityType = "question", isCorrect = true, score = 1f, timestamp = 40L),
        )

        assertEquals(valid.id, engine.resolveContinue(listOf(valid), evidence)?.id)
        assertNull(engine.resolveContinue(listOf(valid), evidence.filter { it.contentId != valid.id }))
    }

    @Test
    fun guidedEligibility_requiresRealParagraphsAndSkillsUseOnlyAvailableQuestionModel() {
        val eligible = article("reading.b1.guided", "B1", hasQuestion = true)
        val noParagraphs = eligible.copy(id = "reading.empty", paragraphs = emptyList())

        assertEquals(listOf(eligible.id), engine.guidedArticles(listOf(noParagraphs, eligible)).map { it.id })
        val skills = engine.deriveSkills(listOf(eligible, noParagraphs))
        assertEquals(listOf("comprehension"), skills.map { it.id })
        assertEquals(2, skills.single().items.size)
        assertTrue(engine.deriveSkills(listOf(eligible.copy(comprehensionQuestions = emptyList()))).isEmpty())
    }

    @Test
    fun weakSpots_useExistingMasteryStatusAndDueReviewsOnlyForRealArticles() {
        val weak = article("reading.b1.weak", "B1")
        val mastered = article("reading.b2.mastered", "B2")
        val snapshots = listOf(
            mastery(weak.id, MasteryStatus.LEARNING),
            mastery(mastered.id, MasteryStatus.MASTERED),
            mastery("reading.stale", MasteryStatus.LEARNING),
        )
        val due = listOf(ReviewSchedule(weak.id, "reading", 50L, 1f))

        val result = engine.weakSpots(listOf(weak, mastered), snapshots, due, now = 100L)

        assertEquals(listOf(weak.id), result.map { it.article.id })
        assertTrue(result.single().isReviewDue)
    }

    private fun article(id: String, level: String, hasQuestion: Boolean = false) = ReadingArticle(
        id = id,
        title = id,
        cefrLevel = level,
        category = "work",
        summaryEn = "Summary",
        summaryTr = "Özet",
        wordCount = 100,
        estimatedReadingMinutes = 2,
        paragraphs = listOf(ReadingParagraph(1, contentEn = "A real paragraph.", contentTr = "Gerçek paragraf.")),
        vocabularyAnnotations = emptyList(),
        comprehensionQuestions = if (hasQuestion) listOf(question("$id.q1")) else emptyList(),
        topicTags = listOf("work"),
        relatedIds = emptyList(),
    )

    private fun question(id: String) = ReadingComprehensionQuestion(
        id = id,
        questionEn = "What is the main point?",
        options = listOf("One", "Two"),
        correctAnswer = "One",
        explanationEn = "The paragraph supports one.",
        explanationTr = "Paragraf biri destekler.",
    )

    private fun mastery(id: String, status: MasteryStatus) = MasterySnapshot(
        contentId = id,
        domain = "reading",
        level = if (status == MasteryStatus.MASTERED) 0.9f else 0.3f,
        confidence = 0.5f,
        totalAttempts = 2,
        correctAttempts = 1,
        lastAttemptTimestamp = 1L,
        lastDecayTimestamp = 1L,
        status = status,
    )
}
