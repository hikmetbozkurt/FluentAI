package com.seanora.fluentai.domain.mastery

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MasteryEngineTest {

    private lateinit var engine: MasteryEngine

    @Before
    fun setUp() {
        engine = MasteryEngine()
    }

    @Test
    fun computeNextSnapshot_firstCorrectAttempt_startsInLearningStatus() {
        val evidence = LearningEvidence(
            contentId = "vocab.schedule",
            domain = "vocabulary",
            activityType = "flashcard",
            isCorrect = true,
            score = 1.0f
        )

        val snapshot = engine.computeNextSnapshot(null, evidence)

        assertEquals("vocab.schedule", snapshot.contentId)
        assertEquals(1, snapshot.totalAttempts)
        assertEquals(1, snapshot.correctAttempts)
        assertTrue(snapshot.level > 0.0f)
        assertEquals(MasteryStatus.LEARNING, snapshot.status)
    }

    @Test
    fun computeNextSnapshot_multipleCorrectAttempts_advancesToPracticedAndMastered() {
        var snapshot: MasterySnapshot? = null

        // Perform 4 successful attempts
        for (i in 1..4) {
            val evidence = LearningEvidence(
                contentId = "vocab.schedule",
                domain = "vocabulary",
                activityType = "exercise",
                isCorrect = true,
                score = 1.0f,
                timestamp = System.currentTimeMillis() + i * 1000
            )
            snapshot = engine.computeNextSnapshot(snapshot, evidence)
        }

        assertEquals(4, snapshot?.totalAttempts)
        assertEquals(4, snapshot?.correctAttempts)
        assertEquals(MasteryStatus.PRACTICED, snapshot?.status)

        // Perform 3 more successful attempts (total 7)
        for (i in 5..7) {
            val evidence = LearningEvidence(
                contentId = "vocab.schedule",
                domain = "vocabulary",
                activityType = "exercise",
                isCorrect = true,
                score = 1.0f,
                timestamp = System.currentTimeMillis() + i * 1000
            )
            snapshot = engine.computeNextSnapshot(snapshot, evidence)
        }

        assertEquals(7, snapshot?.totalAttempts)
        assertEquals(MasteryStatus.MASTERED, snapshot?.status)
    }

    @Test
    fun computeNextSnapshot_incorrectAttempt_decreasesMasteryLevel() {
        var snapshot: MasterySnapshot? = null

        // 3 correct attempts
        for (i in 1..3) {
            val evidence = LearningEvidence(
                contentId = "grammar.present-perfect-vs-past-simple",
                domain = "grammar",
                activityType = "exercise",
                isCorrect = true,
                score = 1.0f
            )
            snapshot = engine.computeNextSnapshot(snapshot, evidence)
        }

        val levelBeforeError = snapshot!!.level

        // 1 error
        val errorEvidence = LearningEvidence(
            contentId = "grammar.present-perfect-vs-past-simple",
            domain = "grammar",
            activityType = "exercise",
            isCorrect = false,
            score = 0.0f
        )
        snapshot = engine.computeNextSnapshot(snapshot, errorEvidence)

        assertEquals(4, snapshot.totalAttempts)
        assertEquals(3, snapshot.correctAttempts)
        assertTrue(snapshot.level < levelBeforeError)
    }

    @Test
    fun computeNextReviewSchedule_spacedRepetitionIntervalGrowsDeterministically() {
        var schedule = engine.computeNextReviewSchedule(
            null,
            LearningEvidence(contentId = "vocab.deliberation", domain = "vocab", activityType = "test", isCorrect = true, score = 1.0f)
        )

        assertEquals(1, schedule.repetitionNumber)
        assertEquals(1.0f, schedule.intervalDays, 0.01f)

        schedule = engine.computeNextReviewSchedule(
            schedule,
            LearningEvidence(contentId = "vocab.deliberation", domain = "vocab", activityType = "test", isCorrect = true, score = 1.0f)
        )

        assertEquals(2, schedule.repetitionNumber)
        assertEquals(3.0f, schedule.intervalDays, 0.01f)

        // On mistake, interval resets
        schedule = engine.computeNextReviewSchedule(
            schedule,
            LearningEvidence(contentId = "vocab.deliberation", domain = "vocab", activityType = "test", isCorrect = false, score = 0.0f)
        )

        assertEquals(0, schedule.repetitionNumber)
        assertEquals(1.0f, schedule.intervalDays, 0.01f)
    }
}
