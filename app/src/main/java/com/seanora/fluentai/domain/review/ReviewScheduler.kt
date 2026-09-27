package com.seanora.fluentai.domain.review

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.ReviewSchedule
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.exp
import kotlin.math.max

private const val MILLIS_PER_DAY = 86_400_000.0f
// Natural log of 0.9 is approximately -0.10536, meaning R = 90% when t == intervalDays
private const val DECAY_FACTOR_CONSTANT = 0.10536f

@Singleton
class ReviewScheduler @Inject constructor() {

    /**
     * Compute next review schedule given a user's self-assessed rating.
     */
    fun computeNextSchedule(
        previous: ReviewSchedule?,
        rating: ReviewRating,
        contentId: String,
        domain: String,
        currentTimestamp: Long = System.currentTimeMillis()
    ): ReviewSchedule {
        val previousEase = previous?.easeFactor ?: 2.5f
        val previousRep = previous?.repetitionNumber ?: 0
        val previousInterval = previous?.intervalDays ?: 1.0f

        val (nextRep, nextInterval, nextEase) = when (rating) {
            ReviewRating.AGAIN -> {
                // Reset repetitions on failure, decrease ease factor
                val updatedEase = (previousEase - 0.20f).coerceAtLeast(1.3f)
                Triple(0, 1.0f, updatedEase)
            }
            ReviewRating.HARD -> {
                // Slower interval growth, slight penalty on ease
                val updatedEase = (previousEase - 0.15f).coerceAtLeast(1.3f)
                val interval = if (previousRep == 0) 1.0f else max(1.0f, previousInterval * 1.2f)
                Triple(previousRep + 1, interval, updatedEase)
            }
            ReviewRating.GOOD -> {
                // Standard SM-2 interval growth
                val interval = when (previousRep) {
                    0 -> 1.0f
                    1 -> 3.0f
                    else -> max(1.0f, previousInterval * previousEase)
                }
                Triple(previousRep + 1, interval, previousEase)
            }
            ReviewRating.EASY -> {
                // Accelerated interval growth, bonus on ease
                val updatedEase = (previousEase + 0.15f).coerceAtMost(3.0f)
                val interval = when (previousRep) {
                    0 -> 1.0f
                    1 -> 3.0f
                    else -> max(2.0f, previousInterval * updatedEase * 1.3f)
                }
                Triple(previousRep + 1, interval, updatedEase)
            }
        }

        val nextDue = currentTimestamp + (nextInterval * MILLIS_PER_DAY).toLong()

        return ReviewSchedule(
            contentId = contentId,
            domain = domain,
            nextReviewDueTimestamp = nextDue,
            intervalDays = nextInterval,
            easeFactor = nextEase,
            repetitionNumber = nextRep,
            lastReviewTimestamp = currentTimestamp
        )
    }

    /**
     * Compute next review schedule from an objective LearningEvidence event.
     */
    fun computeNextScheduleFromEvidence(
        previous: ReviewSchedule?,
        evidence: LearningEvidence
    ): ReviewSchedule {
        val rating = when {
            !evidence.isCorrect -> ReviewRating.AGAIN
            evidence.score >= 0.95f -> ReviewRating.EASY
            evidence.score >= 0.75f -> ReviewRating.GOOD
            else -> ReviewRating.HARD
        }
        return computeNextSchedule(
            previous = previous,
            rating = rating,
            contentId = evidence.contentId,
            domain = evidence.domain,
            currentTimestamp = evidence.timestamp
        )
    }

    /**
     * Calculate retrievability (estimated memory retention) between 0.0 and 1.0.
     * Uses exponential memory decay: R = exp(-0.10536 * (daysElapsed / intervalDays)).
     * When daysElapsed == intervalDays, R = 0.90 (90% target retention).
     */
    fun calculateRetrievability(
        schedule: ReviewSchedule,
        currentTimestamp: Long = System.currentTimeMillis()
    ): Float {
        if (schedule.lastReviewTimestamp <= 0L) return 0.5f

        val elapsedMillis = (currentTimestamp - schedule.lastReviewTimestamp).coerceAtLeast(0L)
        val elapsedDays = elapsedMillis / MILLIS_PER_DAY
        val stability = schedule.intervalDays.coerceAtLeast(0.5f)

        val exponent = -DECAY_FACTOR_CONSTANT * (elapsedDays / stability)
        return exp(exponent).coerceIn(0.0f, 1.0f)
    }

    /**
     * Prioritize schedules that are due for review, sorted by highest urgency.
     * Urgency combines memory decay risk and difficulty (lower easeFactor).
     */
    fun prioritizeReviews(
        schedules: List<ReviewSchedule>,
        currentTimestamp: Long = System.currentTimeMillis(),
        limit: Int = 20
    ): List<PrioritizedReviewItem> {
        return schedules
            .filter { it.nextReviewDueTimestamp <= currentTimestamp }
            .map { schedule ->
                val retrievability = calculateRetrievability(schedule, currentTimestamp)
                val overdueMillis = currentTimestamp - schedule.nextReviewDueTimestamp
                val overdueDays = max(0.0f, overdueMillis / MILLIS_PER_DAY)
                // Urgency formula: risk of forgetting + overdue penalty + difficulty weight
                val urgency = ((1.0f - retrievability) * 50.0f) + (overdueDays * 10.0f) + ((3.0f - schedule.easeFactor) * 5.0f)
                PrioritizedReviewItem(
                    schedule = schedule,
                    retrievability = retrievability,
                    urgencyScore = urgency
                )
            }
            .sortedByDescending { it.urgencyScore }
            .take(limit)
    }

    /**
     * Compute aggregate review summary across all scheduled items.
     */
    fun computeSummary(
        schedules: List<ReviewSchedule>,
        currentTimestamp: Long = System.currentTimeMillis()
    ): ReviewSummary {
        if (schedules.isEmpty()) {
            return ReviewSummary(
                totalScheduled = 0,
                dueCount = 0,
                overdueCount = 0,
                averageRetention = 1.0f,
                nextDueTimestamp = null
            )
        }

        var dueCount = 0
        var overdueCount = 0
        var totalRetention = 0.0f
        var earliestFutureDue: Long? = null

        val oneDayMillis = MILLIS_PER_DAY.toLong()

        for (schedule in schedules) {
            val retrievability = calculateRetrievability(schedule, currentTimestamp)
            totalRetention += retrievability

            if (schedule.nextReviewDueTimestamp <= currentTimestamp) {
                dueCount++
                if (currentTimestamp - schedule.nextReviewDueTimestamp > oneDayMillis) {
                    overdueCount++
                }
            } else {
                if (earliestFutureDue == null || schedule.nextReviewDueTimestamp < earliestFutureDue) {
                    earliestFutureDue = schedule.nextReviewDueTimestamp
                }
            }
        }

        return ReviewSummary(
            totalScheduled = schedules.size,
            dueCount = dueCount,
            overdueCount = overdueCount,
            averageRetention = (totalRetention / schedules.size).coerceIn(0.0f, 1.0f),
            nextDueTimestamp = earliestFutureDue
        )
    }

    /**
     * Apply deterministic memory decay to a MasterySnapshot after prolonged absence.
     * Inactivity of 7+ days reduces mastery by 2% per inactive week down to a safety floor.
     */
    fun applyDecay(
        snapshot: MasterySnapshot,
        currentTimestamp: Long = System.currentTimeMillis()
    ): MasterySnapshot {
        val daysElapsed = ((currentTimestamp - snapshot.lastDecayTimestamp).coerceAtLeast(0L)) / MILLIS_PER_DAY
        if (daysElapsed < 7.0f) {
            return snapshot // No decay within the first week
        }

        val weeksElapsed = (daysElapsed / 7.0f)
        val decayRatePerWeek = 0.02f // 2% per week of inactivity
        val totalDecay = weeksElapsed * decayRatePerWeek

        val minFloor = when (snapshot.status) {
            MasteryStatus.MASTERED, MasteryStatus.MAINTAINING -> 0.65f
            MasteryStatus.PRACTICED -> 0.45f
            MasteryStatus.LEARNING -> 0.15f
            MasteryStatus.NEW -> 0.0f
        }

        val newLevel = max(minFloor, snapshot.level - totalDecay)

        val newStatus = when {
            newLevel < 0.60f && (snapshot.status == MasteryStatus.MASTERED || snapshot.status == MasteryStatus.MAINTAINING) -> MasteryStatus.PRACTICED
            newLevel < 0.40f && snapshot.status == MasteryStatus.PRACTICED -> MasteryStatus.LEARNING
            else -> snapshot.status
        }

        return snapshot.copy(
            level = newLevel,
            lastDecayTimestamp = currentTimestamp,
            status = newStatus
        )
    }
}
