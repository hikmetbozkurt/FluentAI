package com.seanora.fluentai.domain.mistakes

import com.seanora.fluentai.core.model.MistakeStage
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MistakeEngineTest {

    private lateinit var engine: MistakeEngine

    @Before
    fun setUp() {
        engine = MistakeEngine()
    }

    @Test
    fun mistakeLifecycle_progressesThroughCanonicalStages() {
        // 1. First error -> OBSERVED (not immediately labeled as recurring weakness)
        val firstOccurrence = engine.recordMistakeOccurrence(
            existing = null,
            contentId = "grammar.present-perfect-vs-past-simple",
            trapType = "aspect_confusion",
            errorDescription = "Used present perfect with yesterday",
            userAnswer = "We have completed yesterday",
            correctAnswer = "We completed yesterday"
        )
        assertEquals(MistakeStage.OBSERVED, firstOccurrence.stage)
        assertEquals(1, firstOccurrence.occurrenceCount)

        // 2. Second error -> POSSIBLE
        val secondOccurrence = engine.recordMistakeOccurrence(
            existing = firstOccurrence,
            contentId = "grammar.present-perfect-vs-past-simple",
            trapType = "aspect_confusion",
            errorDescription = "Used present perfect with yesterday",
            userAnswer = "I have arrived yesterday",
            correctAnswer = "I arrived yesterday"
        )
        assertEquals(MistakeStage.POSSIBLE, secondOccurrence.stage)
        assertEquals(2, secondOccurrence.occurrenceCount)

        // 3. Third error -> RECURRING
        val thirdOccurrence = engine.recordMistakeOccurrence(
            existing = secondOccurrence,
            contentId = "grammar.present-perfect-vs-past-simple",
            trapType = "aspect_confusion",
            errorDescription = "Used present perfect with yesterday",
            userAnswer = "They have met yesterday",
            correctAnswer = "They met yesterday"
        )
        assertEquals(MistakeStage.RECURRING, thirdOccurrence.stage)
        assertEquals(3, thirdOccurrence.occurrenceCount)

        // 4. User practices targeted exercise correctly -> TARGETED
        val targetedStep = engine.recordSuccessfulResolutionStep(thirdOccurrence)
        assertEquals(MistakeStage.TARGETED, targetedStep.stage)

        // 5. Subsequent success under monitoring -> MONITORING
        val monitoringStep = engine.recordSuccessfulResolutionStep(targetedStep)
        assertEquals(MistakeStage.MONITORING, monitoringStep.stage)

        // 6. Confirmed mastery over time -> RESOLVED
        val resolvedStep = engine.recordSuccessfulResolutionStep(monitoringStep)
        assertEquals(MistakeStage.RESOLVED, resolvedStep.stage)
    }

    @Test
    fun mistakeLifecycle_relapsesFromMonitoringIfErrorRepeats() {
        // Start from a mistake in MONITORING stage
        val monitoringMistake = engine.recordMistakeOccurrence(
            existing = null,
            contentId = "vocab.schedule",
            trapType = "preposition_trap",
            errorDescription = "in schedule instead of on schedule",
            userAnswer = "in schedule",
            correctAnswer = "on schedule"
        ).copy(stage = MistakeStage.MONITORING, occurrenceCount = 3)

        // Error occurs again during monitoring -> relapse to RECURRING
        val relapsedMistake = engine.recordMistakeOccurrence(
            existing = monitoringMistake,
            contentId = "vocab.schedule",
            trapType = "preposition_trap",
            errorDescription = "in schedule again",
            userAnswer = "in schedule",
            correctAnswer = "on schedule"
        )

        assertEquals(MistakeStage.RECURRING, relapsedMistake.stage)
        assertEquals(4, relapsedMistake.occurrenceCount)
    }

    @Test
    fun computeSummaryAndHealth_calculatesDeterministicScores() {
        val m1 = engine.recordMistakeOccurrence(null, "v.1", "preposition_trap", "err", "a", "b")
            .copy(stage = MistakeStage.RECURRING)
        val m2 = engine.recordMistakeOccurrence(null, "v.2", "collocation", "err", "a", "b")
            .copy(stage = MistakeStage.TARGETED)
        val m3 = engine.recordMistakeOccurrence(null, "v.3", "aspect_confusion", "err", "a", "b")
            .copy(stage = MistakeStage.OBSERVED)
        val m4 = engine.recordMistakeOccurrence(null, "v.4", "register", "err", "a", "b")
            .copy(stage = MistakeStage.RESOLVED)

        val summary = engine.computeSummary(listOf(m1, m2, m3, m4))

        assertEquals(3, summary.totalActiveCount)
        assertEquals(1, summary.recurringCount)
        assertEquals(1, summary.targetedCount)
        assertEquals(1, summary.observedCount)
        assertEquals(1, summary.resolvedCount)
        // Health penalty: 15 (recurring) + 10 (targeted) + 2 (observed) = 27 penalty -> score = 73
        assertEquals(73, summary.healthScore)
    }

    @Test
    fun filterPriorityMistakes_ordersBySeverityAndOccurrences() {
        val mObserved = engine.recordMistakeOccurrence(null, "v.obs", "general", "err", "a", "b")
            .copy(stage = MistakeStage.OBSERVED, occurrenceCount = 1)
        val mPossible = engine.recordMistakeOccurrence(null, "v.pos", "general", "err", "a", "b")
            .copy(stage = MistakeStage.POSSIBLE, occurrenceCount = 2)
        val mRecurring = engine.recordMistakeOccurrence(null, "v.rec", "general", "err", "a", "b")
            .copy(stage = MistakeStage.RECURRING, occurrenceCount = 4)
        val mResolved = engine.recordMistakeOccurrence(null, "v.res", "general", "err", "a", "b")
            .copy(stage = MistakeStage.RESOLVED, occurrenceCount = 3)

        val priority = engine.filterPriorityMistakes(listOf(mObserved, mPossible, mRecurring, mResolved))

        assertEquals(3, priority.size)
        // RECURRING must come first
        assertEquals("v.rec", priority[0].contentId)
        assertEquals("v.pos", priority[1].contentId)
        assertEquals("v.obs", priority[2].contentId)
    }

    @Test
    fun groupMistakesByTrap_mapsCorrectlyToTrapCategories() {
        val mPrep = engine.recordMistakeOccurrence(null, "v.1", "preposition_trap", "err", "a", "b")
        val mTense = engine.recordMistakeOccurrence(null, "v.2", "aspect_confusion", "err", "a", "b")
        val mColloc = engine.recordMistakeOccurrence(null, "v.3", "make_vs_do_collocation", "err", "a", "b")

        val groups = engine.groupMistakesByTrap(listOf(mPrep, mTense, mColloc))

        assertEquals(3, groups.size)
        val categories = groups.map { it.category }
        org.junit.Assert.assertTrue(categories.contains(TrapCategory.L1_PREPOSITION))
        org.junit.Assert.assertTrue(categories.contains(TrapCategory.L1_TENSE_ASPECT))
        org.junit.Assert.assertTrue(categories.contains(TrapCategory.COLLOCATION))
    }
}
