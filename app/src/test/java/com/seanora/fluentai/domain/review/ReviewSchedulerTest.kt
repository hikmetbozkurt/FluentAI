package com.seanora.fluentai.domain.review

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.ReviewSchedule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

private const val MILLIS_PER_DAY = 86_400_000L

class ReviewSchedulerTest {

    private lateinit var scheduler: ReviewScheduler

    @Before
    fun setUp() {
        scheduler = ReviewScheduler()
    }

    @Test
    fun computeNextSchedule_goodRating_followsSpacedProgression() {
        val now = 1_000_000_000L

        // Initial attempt
        val first = scheduler.computeNextSchedule(
            previous = null,
            rating = ReviewRating.GOOD,
            contentId = "vocab.resilient",
            domain = "vocabulary",
            currentTimestamp = now
        )

        assertEquals(1, first.repetitionNumber)
        assertEquals(1.0f, first.intervalDays, 0.01f)
        assertEquals(now + MILLIS_PER_DAY, first.nextReviewDueTimestamp)

        // Second attempt
        val second = scheduler.computeNextSchedule(
            previous = first,
            rating = ReviewRating.GOOD,
            contentId = "vocab.resilient",
            domain = "vocabulary",
            currentTimestamp = first.nextReviewDueTimestamp
        )

        assertEquals(2, second.repetitionNumber)
        assertEquals(3.0f, second.intervalDays, 0.01f)

        // Third attempt
        val third = scheduler.computeNextSchedule(
            previous = second,
            rating = ReviewRating.GOOD,
            contentId = "vocab.resilient",
            domain = "vocabulary",
            currentTimestamp = second.nextReviewDueTimestamp
        )

        assertEquals(3, third.repetitionNumber)
        assertTrue(third.intervalDays >= 7.5f) // 3.0 * 2.5 = 7.5
    }

    @Test
    fun computeNextSchedule_againRating_resetsRepetitionAndDecreasesEase() {
        val previous = ReviewSchedule(
            contentId = "vocab.resilient",
            domain = "vocabulary",
            nextReviewDueTimestamp = 2_000_000_000L,
            intervalDays = 14.0f,
            easeFactor = 2.5f,
            repetitionNumber = 4,
            lastReviewTimestamp = 1_900_000_000L
        )

        val next = scheduler.computeNextSchedule(
            previous = previous,
            rating = ReviewRating.AGAIN,
            contentId = "vocab.resilient",
            domain = "vocabulary",
            currentTimestamp = 2_000_000_000L
        )

        assertEquals(0, next.repetitionNumber)
        assertEquals(1.0f, next.intervalDays, 0.01f)
        assertEquals(2.30f, next.easeFactor, 0.01f)
    }

    @Test
    fun computeNextSchedule_easyRating_acceleratesInterval() {
        val previous = ReviewSchedule(
            contentId = "grammar.inversion",
            domain = "grammar",
            nextReviewDueTimestamp = 2_000_000_000L,
            intervalDays = 3.0f,
            easeFactor = 2.5f,
            repetitionNumber = 2,
            lastReviewTimestamp = 1_900_000_000L
        )

        val next = scheduler.computeNextSchedule(
            previous = previous,
            rating = ReviewRating.EASY,
            contentId = "grammar.inversion",
            domain = "grammar",
            currentTimestamp = 2_000_000_000L
        )

        assertEquals(3, next.repetitionNumber)
        assertEquals(2.65f, next.easeFactor, 0.01f)
        // 3.0 * 2.65 * 1.3 ≈ 10.33
        assertTrue(next.intervalDays > 10.0f)
    }

    @Test
    fun calculateRetrievability_decaysOverTime() {
        val now = 10_000_000_000L
        val schedule = ReviewSchedule(
            contentId = "vocab.deliberation",
            domain = "vocabulary",
            nextReviewDueTimestamp = now + (10 * MILLIS_PER_DAY),
            intervalDays = 10.0f,
            easeFactor = 2.5f,
            repetitionNumber = 3,
            lastReviewTimestamp = now
        )

        // At t = 0 (review time)
        val rAtStart = scheduler.calculateRetrievability(schedule, now)
        assertEquals(1.0f, rAtStart, 0.01f)

        // At t = intervalDays (on the due date, expected 90%)
        val rAtDue = scheduler.calculateRetrievability(schedule, now + (10 * MILLIS_PER_DAY))
        assertEquals(0.90f, rAtDue, 0.02f)

        // Overdue by another 10 days
        val rOverdue = scheduler.calculateRetrievability(schedule, now + (20 * MILLIS_PER_DAY))
        assertTrue(rOverdue < 0.82f)
    }

    @Test
    fun prioritizeReviews_sortsByUrgencyAndFiltersNonDue() {
        val now = 5_000_000_000L

        val due1 = ReviewSchedule(
            contentId = "vocab.one",
            domain = "vocabulary",
            nextReviewDueTimestamp = now - (2 * MILLIS_PER_DAY), // 2 days overdue
            intervalDays = 3.0f,
            easeFactor = 2.0f,
            repetitionNumber = 1,
            lastReviewTimestamp = now - (5 * MILLIS_PER_DAY)
        )
        val due2 = ReviewSchedule(
            contentId = "vocab.two",
            domain = "vocabulary",
            nextReviewDueTimestamp = now, // Exactly due now
            intervalDays = 10.0f,
            easeFactor = 2.5f,
            repetitionNumber = 2,
            lastReviewTimestamp = now - (10 * MILLIS_PER_DAY)
        )
        val notDue = ReviewSchedule(
            contentId = "vocab.three",
            domain = "vocabulary",
            nextReviewDueTimestamp = now + (3 * MILLIS_PER_DAY), // 3 days in future
            intervalDays = 7.0f,
            easeFactor = 2.5f,
            repetitionNumber = 2,
            lastReviewTimestamp = now - (4 * MILLIS_PER_DAY)
        )

        val prioritized = scheduler.prioritizeReviews(listOf(due1, due2, notDue), currentTimestamp = now, limit = 10)

        assertEquals(2, prioritized.size)
        // Overdue item with lower ease factor should have higher urgency
        assertEquals("vocab.one", prioritized[0].schedule.contentId)
        assertEquals("vocab.two", prioritized[1].schedule.contentId)
        assertTrue(prioritized[0].urgencyScore > prioritized[1].urgencyScore)
    }

    @Test
    fun computeSummary_aggregatesCorrectly() {
        val now = 5_000_000_000L
        val dueOverdue = ReviewSchedule(
            contentId = "vocab.one",
            domain = "vocabulary",
            nextReviewDueTimestamp = now - (2 * MILLIS_PER_DAY),
            intervalDays = 3.0f,
            repetitionNumber = 1,
            lastReviewTimestamp = now - (5 * MILLIS_PER_DAY)
        )
        val dueToday = ReviewSchedule(
            contentId = "vocab.two",
            domain = "vocabulary",
            nextReviewDueTimestamp = now,
            intervalDays = 5.0f,
            repetitionNumber = 2,
            lastReviewTimestamp = now - (5 * MILLIS_PER_DAY)
        )
        val future = ReviewSchedule(
            contentId = "vocab.three",
            domain = "vocabulary",
            nextReviewDueTimestamp = now + (4 * MILLIS_PER_DAY),
            intervalDays = 7.0f,
            repetitionNumber = 2,
            lastReviewTimestamp = now - (3 * MILLIS_PER_DAY)
        )

        val summary = scheduler.computeSummary(listOf(dueOverdue, dueToday, future), now)

        assertEquals(3, summary.totalScheduled)
        assertEquals(2, summary.dueCount)
        assertEquals(1, summary.overdueCount)
        assertEquals(now + (4 * MILLIS_PER_DAY), summary.nextDueTimestamp)
        assertTrue(summary.averageRetention in 0.8f..1.0f)
    }

    @Test
    fun applyDecay_decreasesMasteryAfterProlongedInactivity() {
        val now = 10_000_000_000L
        val activeSnapshot = MasterySnapshot(
            contentId = "vocab.leadership",
            domain = "vocabulary",
            level = 0.90f,
            confidence = 0.80f,
            totalAttempts = 8,
            correctAttempts = 7,
            lastAttemptTimestamp = now,
            lastDecayTimestamp = now,
            status = MasteryStatus.MASTERED
        )

        // 3 days later: no decay
        val threeDaysLater = scheduler.applyDecay(activeSnapshot, now + (3 * MILLIS_PER_DAY))
        assertEquals(0.90f, threeDaysLater.level, 0.001f)

        // 28 days later (4 weeks): decay applied
        val fourWeeksLater = scheduler.applyDecay(activeSnapshot, now + (28 * MILLIS_PER_DAY))
        // 4 weeks * 0.02 = 0.08 decay -> 0.90 - 0.08 = 0.82
        assertEquals(0.82f, fourWeeksLater.level, 0.01f)
        assertEquals(now + (28 * MILLIS_PER_DAY), fourWeeksLater.lastDecayTimestamp)
    }
}
