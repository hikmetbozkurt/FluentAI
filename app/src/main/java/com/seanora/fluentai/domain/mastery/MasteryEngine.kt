package com.seanora.fluentai.domain.mastery

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.domain.review.ReviewScheduler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MasteryEngine @Inject constructor(
    private val reviewScheduler: ReviewScheduler
) {
    constructor() : this(ReviewScheduler())

    fun computeNextSnapshot(
        previous: MasterySnapshot?,
        evidence: LearningEvidence
    ): MasterySnapshot {
        val totalAttempts = (previous?.totalAttempts ?: 0) + 1
        val correctAttempts = (previous?.correctAttempts ?: 0) + if (evidence.isCorrect) 1 else 0

        val previousLevel = previous?.level ?: 0f
        val newLevel = if (evidence.isCorrect) {
            val weight = (1.0f / (totalAttempts.coerceAtMost(5) + 1)).coerceIn(0.2f, 0.45f)
            ((previousLevel * (1f - weight)) + (evidence.score * weight)).coerceIn(0f, 1f)
        } else {
            (previousLevel * 0.65f).coerceIn(0f, 1f)
        }

        val confidence = (totalAttempts / 8f).coerceIn(0.15f, 1.0f)

        val newStatus = when {
            previous?.status == MasteryStatus.MASTERED && evidence.isCorrect -> MasteryStatus.MAINTAINING
            previous?.status == MasteryStatus.MAINTAINING && evidence.isCorrect -> MasteryStatus.MAINTAINING
            newLevel >= 0.88f && totalAttempts >= 6 -> MasteryStatus.MASTERED
            newLevel >= 0.70f && totalAttempts >= 3 -> MasteryStatus.PRACTICED
            else -> MasteryStatus.LEARNING
        }

        return MasterySnapshot(
            contentId = evidence.contentId,
            domain = evidence.domain,
            level = newLevel,
            confidence = confidence,
            totalAttempts = totalAttempts,
            correctAttempts = correctAttempts,
            lastAttemptTimestamp = evidence.timestamp,
            lastDecayTimestamp = evidence.timestamp,
            status = newStatus
        )
    }

    fun computeNextReviewSchedule(
        previous: ReviewSchedule?,
        evidence: LearningEvidence
    ): ReviewSchedule {
        return reviewScheduler.computeNextScheduleFromEvidence(previous, evidence)
    }
}
