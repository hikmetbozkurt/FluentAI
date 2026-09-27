package com.seanora.fluentai.domain.vocabulary

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.VocabItem
import javax.inject.Inject

enum class FlashRecallOutcome(val title: String) {
    AGAIN("Again"),
    HARD("Hard"),
    GOOD("Good"),
    EASY("Easy"),
}

class VocabularyFlashCardSelector @Inject constructor() {
    fun selectFlashCards(
        items: List<VocabItem>,
        schedules: List<ReviewSchedule>,
        mastery: List<MasterySnapshot>,
        now: Long,
        limit: Int = 20,
    ): List<VocabItem> {
        val byId = items.associateBy { it.id }
        val due = schedules
            .filter { it.domain.equals("vocabulary", true) && it.nextReviewDueTimestamp <= now }
            .sortedBy { it.nextReviewDueTimestamp }
            .mapNotNull { byId[it.contentId] }
            .distinctBy { it.id }
            .take(limit)
        if (due.isNotEmpty()) return due

        return mastery
            .filter {
                it.domain.equals("vocabulary", true) &&
                    (it.status == MasteryStatus.LEARNING || it.status == MasteryStatus.PRACTICED)
            }
            .sortedWith(compareBy<MasterySnapshot> { it.level }.thenBy { it.contentId })
            .mapNotNull { byId[it.contentId] }
            .distinctBy { it.id }
            .take(limit)
    }

    fun recallEvidence(contentId: String, outcome: FlashRecallOutcome, timestamp: Long): LearningEvidence {
        val score = when (outcome) {
            FlashRecallOutcome.AGAIN -> 0f
            FlashRecallOutcome.HARD -> 0.6f
            FlashRecallOutcome.GOOD -> 0.85f
            FlashRecallOutcome.EASY -> 1f
        }
        return LearningEvidence(
            contentId = contentId,
            domain = "vocabulary",
            activityType = "flash_card_review",
            isCorrect = outcome != FlashRecallOutcome.AGAIN,
            score = score,
            details = "outcome=${outcome.name.lowercase()}",
            timestamp = timestamp,
        )
    }
}
