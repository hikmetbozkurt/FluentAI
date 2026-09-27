package com.seanora.fluentai.domain.mastery

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.domain.mistakes.MistakeEngine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordLearningEvidenceUseCase @Inject constructor(
    private val userLearningRepository: UserLearningRepository,
    private val masteryEngine: MasteryEngine,
    private val mistakeEngine: MistakeEngine
) {

    suspend operator fun invoke(
        evidence: LearningEvidence,
        trapType: String? = null,
        errorDescription: String? = null,
        userAnswer: String? = null,
        correctAnswer: String? = null
    ): MasterySnapshot {
        // 1. Persist raw learning evidence into user database
        userLearningRepository.recordEvidence(evidence)

        // 2. Fetch current mastery snapshot
        val previousSnapshot = userLearningRepository.getMasterySnapshotSync(evidence.contentId)

        // 3. Compute deterministic next mastery snapshot
        val updatedSnapshot = masteryEngine.computeNextSnapshot(previousSnapshot, evidence)
        userLearningRepository.saveMasterySnapshot(updatedSnapshot)

        // 4. Compute and update spaced repetition schedule
        val previousSchedule = userLearningRepository.getReviewScheduleSync(evidence.contentId)
        val updatedSchedule = masteryEngine.computeNextReviewSchedule(previousSchedule, evidence)
        userLearningRepository.saveReviewSchedule(updatedSchedule)

        // 5. Update mistake lifecycle if relevant (ADR-010 & Section 9)
        if (trapType != null) {
            val existingMistake = userLearningRepository.findMistake(evidence.contentId, trapType)
            if (!evidence.isCorrect) {
                val updatedMistake = mistakeEngine.recordMistakeOccurrence(
                    existing = existingMistake,
                    contentId = evidence.contentId,
                    trapType = trapType,
                    errorDescription = errorDescription ?: "Error with $trapType",
                    userAnswer = userAnswer ?: "",
                    correctAnswer = correctAnswer ?: "",
                    timestamp = evidence.timestamp
                )
                userLearningRepository.saveMistake(updatedMistake)
            } else if (existingMistake != null) {
                val resolvedStep = mistakeEngine.recordSuccessfulResolutionStep(
                    existing = existingMistake,
                    timestamp = evidence.timestamp
                )
                userLearningRepository.saveMistake(resolvedStep)
            }
        }

        return updatedSnapshot
    }
}
