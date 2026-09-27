package com.seanora.fluentai.data.user.mapper

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.MistakeStage
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.UserProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UserMappersTest {

    @Test
    fun userProfile_roundTrip_preservesAllFields() {
        val domain = UserProfile(
            id = "user_001",
            displayName = "Baris",
            targetCefrLevel = "C1",
            estimatedOverallLevel = "B2",
            estimatedVocabLevel = "B2",
            estimatedGrammarLevel = "B1",
            estimatedSpeakingLevel = "B1",
            dailyGoalMinutes = 25,
            learningInterests = listOf("management", "system-design", "negotiations"),
            streakDays = 7,
            lastActiveDate = "2026-09-24"
        )

        val entity = domain.toEntity()
        val restored = entity.toDomain()

        assertEquals("user_001", restored.id)
        assertEquals("Baris", restored.displayName)
        assertEquals("C1", restored.targetCefrLevel)
        assertEquals("B2", restored.estimatedOverallLevel)
        assertEquals(25, restored.dailyGoalMinutes)
        assertEquals(listOf("management", "system-design", "negotiations"), restored.learningInterests)
        assertEquals(7, restored.streakDays)
    }

    @Test
    fun userProfile_nullDisplayName_roundTrip_preservesNull() {
        val domain = UserProfile(
            id = "user_default",
            displayName = null,
            targetCefrLevel = "B2",
            estimatedOverallLevel = "B1",
            estimatedVocabLevel = "B1",
            estimatedGrammarLevel = "B1",
            estimatedSpeakingLevel = "B1",
            dailyGoalMinutes = 15,
            learningInterests = listOf("tech"),
            streakDays = 1,
            lastActiveDate = "2026-09-25"
        )

        val entity = domain.toEntity()
        val restored = entity.toDomain()

        assertEquals("user_default", restored.id)
        assertEquals(null, restored.displayName)
    }

    @Test
    fun learningEvidence_roundTrip_preservesEvidenceIntegrity() {
        val evidence = LearningEvidence(
            id = 101L,
            contentId = "vocab.deliberation",
            domain = "vocabulary",
            activityType = "exercise",
            isCorrect = true,
            score = 1.0f,
            responseTimeMs = 3450,
            details = """{"trapChecked": "false_friend"}"""
        )

        val entity = evidence.toEntity()
        val restored = entity.toDomain()

        assertEquals(101L, restored.id)
        assertEquals("vocab.deliberation", restored.contentId)
        assertEquals("vocabulary", restored.domain)
        assertTrue(restored.isCorrect)
        assertEquals(1.0f, restored.score, 0.001f)
        assertEquals(3450L, restored.responseTimeMs)
    }

    @Test
    fun masterySnapshot_roundTrip_preservesMasteryStatus() {
        val snapshot = MasterySnapshot(
            contentId = "grammar.present-perfect-vs-past-simple",
            domain = "grammar",
            level = 0.85f,
            confidence = 0.90f,
            totalAttempts = 12,
            correctAttempts = 10,
            lastAttemptTimestamp = 1770000000000L,
            lastDecayTimestamp = 1770000000000L,
            status = MasteryStatus.PRACTICED
        )

        val entity = snapshot.toEntity()
        val restored = entity.toDomain()

        assertEquals("grammar.present-perfect-vs-past-simple", restored.contentId)
        assertEquals(0.85f, restored.level, 0.001f)
        assertEquals(0.90f, restored.confidence, 0.001f)
        assertEquals(MasteryStatus.PRACTICED, restored.status)
        assertEquals(12, restored.totalAttempts)
    }

    @Test
    fun reviewSchedule_roundTrip_preservesSpacedRepetitionData() {
        val schedule = ReviewSchedule(
            contentId = "vocab.leverage",
            domain = "vocabulary",
            nextReviewDueTimestamp = 1770500000000L,
            intervalDays = 6.0f,
            easeFactor = 2.6f,
            repetitionNumber = 3,
            lastReviewTimestamp = 1769900000000L
        )

        val entity = schedule.toEntity()
        val restored = entity.toDomain()

        assertEquals("vocab.leverage", restored.contentId)
        assertEquals(6.0f, restored.intervalDays, 0.001f)
        assertEquals(2.6f, restored.easeFactor, 0.001f)
        assertEquals(3, restored.repetitionNumber)
    }

    @Test
    fun mistakeRecord_preservesAllLifecycleStages() {
        for (stage in MistakeStage.entries) {
            val mistake = MistakeRecord(
                id = 1L,
                contentId = "vocab.schedule",
                trapType = "preposition_confusion",
                errorDescription = "Used 'in schedule' instead of 'on schedule'",
                userAnswer = "in schedule",
                correctAnswer = "on schedule",
                stage = stage,
                occurrenceCount = 2,
                firstObservedAt = 1000L,
                lastObservedAt = 2000L
            )

            val entity = mistake.toEntity()
            val restored = entity.toDomain()

            assertEquals(stage, restored.stage)
            assertEquals("vocab.schedule", restored.contentId)
            assertEquals("preposition_confusion", restored.trapType)
            assertEquals(2, restored.occurrenceCount)
        }
    }
}
