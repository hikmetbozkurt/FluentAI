package com.seanora.fluentai.domain.grammar

import com.seanora.fluentai.core.model.GrammarExample
import com.seanora.fluentai.core.model.GrammarFeatureState
import com.seanora.fluentai.core.model.GrammarLesson
import com.seanora.fluentai.core.model.GrammarResumeDestination
import com.seanora.fluentai.core.model.GrammarTrap
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.MistakeStage
import com.seanora.fluentai.core.model.ReviewSchedule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GrammarFeatureEngineTest {

    private val engine = GrammarFeatureEngine()

    @Test
    fun sentenceBuilder_usesOnlyRealEligibleExamplesAndEvaluatesExactTokenOrder() {
        val lesson = lesson(
            examples = listOf(
                GrammarExample("I've already sent the report.", "Raporu zaten gönderdim."),
                GrammarExample("Hi!", "Merhaba!"),
            ),
        )

        val questions = engine.buildSentenceBuilders(listOf(lesson))

        assertEquals(1, questions.size)
        val question = questions.single()
        assertEquals("I've already sent the report.", question.correctSentence)
        assertFalse(question.scrambledTokens == question.correctTokens)
        assertTrue(engine.evaluateSentence(question, question.correctTokens))
        assertFalse(engine.evaluateSentence(question, question.scrambledTokens))
    }

    @Test
    fun errorSpotter_usesOnlyExplicitKnownIncorrectAndCorrectPairs() {
        val complete = GrammarTrap(
            trapType = "l1_transfer",
            trapTitle = "Missing auxiliary",
            explanationTr = "Yardımcı fiil gerekir.",
            incorrectExample = "She working today.",
            correctExample = "She is working today.",
        )
        val incomplete = complete.copy(incorrectExample = "")

        val questions = engine.buildErrorSpotters(
            listOf(lesson(traps = listOf(complete, incomplete))),
        )

        assertEquals(1, questions.size)
        assertEquals("She working today.", questions.single().incorrectSentence)
        assertEquals("She is working today.", questions.single().correctSentence)
    }

    @Test
    fun weakSpots_onlyRanksRealGrammarStateAndHasHonestEmptyState() {
        val lesson = lesson()
        assertTrue(engine.rankWeakSpots(listOf(lesson), emptyList(), emptyList(), emptyList(), 100L).isEmpty())

        val mastery = MasterySnapshot(
            contentId = lesson.id,
            domain = "grammar",
            level = 0.25f,
            confidence = 0.4f,
            totalAttempts = 2,
            correctAttempts = 0,
            lastAttemptTimestamp = 10L,
            lastDecayTimestamp = 10L,
            status = MasteryStatus.LEARNING,
        )
        val mistake = MistakeRecord(
            contentId = lesson.id,
            trapType = "l1_transfer",
            errorDescription = "Transfer error",
            userAnswer = "wrong",
            correctAnswer = "right",
            stage = MistakeStage.POSSIBLE,
            occurrenceCount = 2,
            firstObservedAt = 1L,
            lastObservedAt = 2L,
        )
        val due = ReviewSchedule(
            contentId = lesson.id,
            domain = "grammar",
            nextReviewDueTimestamp = 50L,
            intervalDays = 1f,
        )

        val result = engine.rankWeakSpots(
            lessons = listOf(lesson),
            mastery = listOf(mastery, mastery.copy(contentId = "vocab.not-grammar", domain = "vocabulary")),
            mistakes = listOf(mistake),
            dueReviews = listOf(due),
            now = 100L,
        )

        assertEquals(listOf(lesson.id), result.map { it.lesson.id })
        assertEquals(2, result.single().mistakeCount)
        assertTrue(result.single().isReviewDue)
    }

    @Test
    fun continue_resolvesOnlyLatestValidSemanticContentId() {
        val state = GrammarFeatureState(
            resumeDestination = GrammarResumeDestination.SENTENCE_BUILDER,
            resumeContentId = "grammar.present-perfect",
        )

        assertEquals(state, engine.resolveResume(state, setOf("grammar.present-perfect")))
        assertNull(engine.resolveResume(state, setOf("grammar.other")))
        assertNull(engine.resolveResume(GrammarFeatureState(), setOf("grammar.present-perfect")))
    }

    @Test
    fun comparisonCandidates_resolveRealIdsAndPreferStoredRelationsAndMetadata() {
        val primary = lesson().copy(
            id = "grammar.primary",
            category = "aspect",
            relatedIds = listOf("grammar.related"),
            topicTags = listOf("finished-time"),
        )
        val related = lesson().copy(id = "grammar.related", title = "Related", category = "tense")
        val sameCategory = lesson().copy(id = "grammar.same-category", title = "Same category", category = "aspect")
        val unrelated = lesson().copy(id = "grammar.unrelated", title = "Unrelated", category = "modals", cefrLevel = "C1")

        val candidates = engine.comparisonCandidates(primary.id, listOf(unrelated, sameCategory, related, primary))

        assertEquals(listOf(related.id, sameCategory.id, unrelated.id), candidates.map { it.id })
        assertTrue(engine.comparisonCandidates("grammar.missing", listOf(primary, related)).isEmpty())
    }

    private fun lesson(
        examples: List<GrammarExample> = emptyList(),
        traps: List<GrammarTrap> = emptyList(),
    ) = GrammarLesson(
        id = "grammar.present-perfect",
        title = "Present Perfect",
        cefrLevel = "B1",
        category = "aspect",
        summaryEn = "Connect past actions to the present.",
        summaryTr = "Geçmişi şimdiye bağlar.",
        explanationTr = "Geçmiş ve şimdi arasındaki bağ.",
        examples = examples,
        turkishTraps = traps,
    )
}
