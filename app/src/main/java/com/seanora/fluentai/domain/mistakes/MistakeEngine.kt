package com.seanora.fluentai.domain.mistakes

import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.MistakeStage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MistakeEngine @Inject constructor() {

    fun recordMistakeOccurrence(
        existing: MistakeRecord?,
        contentId: String,
        trapType: String,
        errorDescription: String,
        userAnswer: String,
        correctAnswer: String,
        timestamp: Long = System.currentTimeMillis()
    ): MistakeRecord {
        if (existing == null) {
            // First time mistake observed: OBSERVED
            return MistakeRecord(
                id = 0,
                contentId = contentId,
                trapType = trapType,
                errorDescription = errorDescription,
                userAnswer = userAnswer,
                correctAnswer = correctAnswer,
                stage = MistakeStage.OBSERVED,
                occurrenceCount = 1,
                firstObservedAt = timestamp,
                lastObservedAt = timestamp
            )
        }

        val newCount = existing.occurrenceCount + 1
        val nextStage = when (existing.stage) {
            MistakeStage.OBSERVED -> MistakeStage.POSSIBLE
            MistakeStage.POSSIBLE -> MistakeStage.RECURRING
            MistakeStage.RECURRING -> MistakeStage.RECURRING
            MistakeStage.TARGETED -> MistakeStage.RECURRING
            MistakeStage.MONITORING -> MistakeStage.RECURRING // Relapse
            MistakeStage.RESOLVED -> MistakeStage.OBSERVED // New emergence
        }

        return existing.copy(
            errorDescription = errorDescription,
            userAnswer = userAnswer,
            correctAnswer = correctAnswer,
            stage = nextStage,
            occurrenceCount = newCount,
            lastObservedAt = timestamp
        )
    }

    fun recordSuccessfulResolutionStep(
        existing: MistakeRecord,
        timestamp: Long = System.currentTimeMillis()
    ): MistakeRecord {
        val nextStage = when (existing.stage) {
            MistakeStage.OBSERVED -> MistakeStage.RESOLVED
            MistakeStage.POSSIBLE -> MistakeStage.RESOLVED
            MistakeStage.RECURRING -> MistakeStage.TARGETED
            MistakeStage.TARGETED -> MistakeStage.MONITORING
            MistakeStage.MONITORING -> MistakeStage.RESOLVED
            MistakeStage.RESOLVED -> MistakeStage.RESOLVED
        }

        return existing.copy(
            stage = nextStage,
            lastObservedAt = timestamp
        )
    }

    /**
     * Compute aggregate summary and health score of the mistake bank.
     */
    fun computeSummary(mistakes: List<MistakeRecord>): MistakeSummary {
        var recurring = 0
        var targeted = 0
        var possible = 0
        var observed = 0
        var resolved = 0

        for (m in mistakes) {
            when (m.stage) {
                MistakeStage.RECURRING -> recurring++
                MistakeStage.TARGETED -> targeted++
                MistakeStage.POSSIBLE -> possible++
                MistakeStage.OBSERVED -> observed++
                MistakeStage.MONITORING -> targeted++ // Monitoring counts with targeted
                MistakeStage.RESOLVED -> resolved++
            }
        }

        val totalActive = recurring + targeted + possible + observed
        val healthScore = computeMistakeHealth(mistakes)

        return MistakeSummary(
            totalActiveCount = totalActive,
            recurringCount = recurring,
            targetedCount = targeted,
            possibleCount = possible,
            observedCount = observed,
            resolvedCount = resolved,
            healthScore = healthScore
        )
    }

    /**
     * Compute a deterministic health score from 0 to 100 based on active mistakes and their severity.
     */
    fun computeMistakeHealth(mistakes: List<MistakeRecord>): Int {
        var penalty = 0
        for (m in mistakes) {
            penalty += when (m.stage) {
                MistakeStage.RECURRING -> 15
                MistakeStage.TARGETED -> 10
                MistakeStage.MONITORING -> 6
                MistakeStage.POSSIBLE -> 4
                MistakeStage.OBSERVED -> 2
                MistakeStage.RESOLVED -> 0
            }
        }
        return (100 - penalty).coerceIn(0, 100)
    }

    /**
     * Filter active mistakes that need immediate targeted practice.
     * Orders RECURRING first, then TARGETED, then POSSIBLE.
     */
    fun filterPriorityMistakes(
        mistakes: List<MistakeRecord>,
        limit: Int = 10
    ): List<MistakeRecord> {
        return mistakes
            .filter { it.stage != MistakeStage.RESOLVED }
            .sortedWith(
                compareBy<MistakeRecord> {
                    when (it.stage) {
                        MistakeStage.RECURRING -> 1
                        MistakeStage.TARGETED -> 2
                        MistakeStage.MONITORING -> 3
                        MistakeStage.POSSIBLE -> 4
                        MistakeStage.OBSERVED -> 5
                        MistakeStage.RESOLVED -> 6
                    }
                }.thenByDescending { it.occurrenceCount }
                    .thenByDescending { it.lastObservedAt }
            )
            .take(limit)
    }

    /**
     * Group mistakes by high-level TrapCategory for targeted drill generation.
     */
    fun groupMistakesByTrap(mistakes: List<MistakeRecord>): List<TrapGroup> {
        val active = mistakes.filter { it.stage != MistakeStage.RESOLVED }
        return active
            .groupBy { TrapCategory.fromTrapType(it.trapType) }
            .map { (cat, list) ->
                TrapGroup(
                    category = cat,
                    mistakes = list.sortedByDescending { it.occurrenceCount }
                )
            }
            .sortedByDescending { it.mistakes.size }
    }

    /**
     * Check if a mistake is ready for / requires active targeting in practice.
     */
    fun shouldTarget(mistake: MistakeRecord): Boolean {
        return mistake.stage == MistakeStage.RECURRING ||
                mistake.stage == MistakeStage.TARGETED ||
                mistake.stage == MistakeStage.MONITORING
    }
}
