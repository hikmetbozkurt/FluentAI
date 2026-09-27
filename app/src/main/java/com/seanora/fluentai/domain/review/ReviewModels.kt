package com.seanora.fluentai.domain.review

import com.seanora.fluentai.core.model.ReviewSchedule

/**
 * Standard user rating for a spaced repetition review item.
 */
enum class ReviewRating {
    AGAIN, // Failed recall; reset repetition, decrease ease
    HARD,  // Recalled with significant struggle; minimal interval increase
    GOOD,  // Recalled correctly with normal effort; standard interval progression
    EASY;  // Recalled immediately and effortlessly; accelerated interval progression

    val isCorrect: Boolean
        get() = this != AGAIN

    val score: Float
        get() = when (this) {
            AGAIN -> 0.20f
            HARD -> 0.65f
            GOOD -> 0.85f
            EASY -> 1.0f
        }
}

/**
 * Snapshot of review statistics for dashboard and practice planning.
 */
data class ReviewSummary(
    val totalScheduled: Int,
    val dueCount: Int,
    val overdueCount: Int,
    val averageRetention: Float,
    val nextDueTimestamp: Long?
)

/**
 * Review item paired with calculated urgency and estimated retrievability.
 */
data class PrioritizedReviewItem(
    val schedule: ReviewSchedule,
    val retrievability: Float, // 0.0 (forgotten) to 1.0 (fresh)
    val urgencyScore: Float    // Higher score = more urgent to review
)
