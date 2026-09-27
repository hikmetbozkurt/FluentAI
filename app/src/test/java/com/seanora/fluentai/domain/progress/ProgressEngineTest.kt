package com.seanora.fluentai.domain.progress

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.MistakeStage
import com.seanora.fluentai.core.model.ReviewSchedule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

private const val MILLIS_PER_DAY = 86_400_000L

class ProgressEngineTest {

    private lateinit var progressEngine: ProgressEngine

    @Before
    fun setUp() {
        progressEngine = ProgressEngine()
    }

    @Test
    fun computeSkillProficiency_respectsSkillSpecificSeparation() {
        val now = 10_000_000_000L

        // High vocabulary mastery (C1 level)
        val vocabSnapshots = (1..35).map { i ->
            MasterySnapshot(
                contentId = "vocab.$i",
                domain = "vocabulary",
                level = 0.90f,
                confidence = 0.85f,
                totalAttempts = 7,
                correctAttempts = 7,
                lastAttemptTimestamp = now,
                lastDecayTimestamp = now,
                status = MasteryStatus.MASTERED
            )
        }

        // Low grammar mastery (only 1 item attempted, A2/B1 level)
        val grammarSnapshots = listOf(
            MasterySnapshot(
                contentId = "grammar.present-simple",
                domain = "grammar",
                level = 0.40f,
                confidence = 0.30f,
                totalAttempts = 2,
                correctAttempts = 1,
                lastAttemptTimestamp = now,
                lastDecayTimestamp = now,
                status = MasteryStatus.LEARNING
            )
        )

        val vocabProficiency = progressEngine.computeSkillProficiency(
            "vocabulary", "Vocabulary", vocabSnapshots + grammarSnapshots, emptyList()
        )
        val grammarProficiency = progressEngine.computeSkillProficiency(
            "grammar", "Grammar", vocabSnapshots + grammarSnapshots, emptyList()
        )

        // Vocabulary is C1
        assertEquals("C1", vocabProficiency.cefrLevel)
        assertEquals(35, vocabProficiency.itemsMastered)
        assertEquals(90, vocabProficiency.masteryPercentage)

        // Grammar remains A2 despite high vocabulary mastery!
        assertEquals("A2", grammarProficiency.cefrLevel)
        assertEquals(0, grammarProficiency.itemsMastered)
        assertEquals(40, grammarProficiency.masteryPercentage)
    }

    @Test
    fun computeActivityStreak_calculatesConsecutiveDays() {
        val now = 10_000_000_000L

        val evidences = listOf(
            LearningEvidence(contentId = "v1", domain = "vocabulary", activityType = "card", isCorrect = true, score = 1.0f, timestamp = now),
            LearningEvidence(contentId = "v2", domain = "vocabulary", activityType = "card", isCorrect = true, score = 1.0f, timestamp = now - MILLIS_PER_DAY),
            LearningEvidence(contentId = "v3", domain = "vocabulary", activityType = "card", isCorrect = true, score = 1.0f, timestamp = now - (2 * MILLIS_PER_DAY))
        )

        val streak = progressEngine.computeActivityStreak(evidences, now)
        assertEquals(3, streak.currentStreakDays)
        assertEquals(3, streak.totalPracticedItems)
        assertEquals(1.0f, streak.overallAccuracyRate, 0.01f)
    }

    @Test
    fun computeDashboardData_integratesAllSubsystemsDeterministically() {
        val now = 10_000_000_000L

        val snapshots = listOf(
            MasterySnapshot(
                contentId = "vocab.leadership",
                domain = "vocabulary",
                level = 0.85f,
                confidence = 0.80f,
                totalAttempts = 5,
                correctAttempts = 5,
                lastAttemptTimestamp = now,
                lastDecayTimestamp = now,
                status = MasteryStatus.MASTERED
            )
        )
        val evidences = listOf(
            LearningEvidence(contentId = "vocab.leadership", domain = "vocabulary", activityType = "card", isCorrect = true, score = 1.0f, timestamp = now)
        )
        val mistakes = listOf(
            MistakeRecord(
                id = 1,
                contentId = "vocab.preposition",
                trapType = "preposition_trap",
                errorDescription = "Wrong prep",
                userAnswer = "at",
                correctAnswer = "in",
                stage = MistakeStage.OBSERVED,
                occurrenceCount = 1,
                firstObservedAt = now,
                lastObservedAt = now
            )
        )
        val reviews = listOf(
            ReviewSchedule(
                contentId = "vocab.leadership",
                domain = "vocabulary",
                nextReviewDueTimestamp = now - 1000L, // Due
                intervalDays = 1.0f,
                repetitionNumber = 1
            )
        )

        val dashboard = progressEngine.computeDashboardData(snapshots, evidences, mistakes, reviews, now)

        assertEquals(5, dashboard.skills.size)
        assertEquals(1, dashboard.reviewsDueCount)
        assertEquals(98, dashboard.mistakeHealthScore) // 100 - 2 (observed penalty) = 98
        assertTrue(dashboard.streak.currentStreakDays >= 1)
    }
}
