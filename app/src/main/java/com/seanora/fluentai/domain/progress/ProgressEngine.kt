package com.seanora.fluentai.domain.progress

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.domain.mistakes.MistakeEngine
import com.seanora.fluentai.domain.review.ReviewScheduler
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

private const val MILLIS_PER_DAY = 86_400_000L

@Singleton
class ProgressEngine @Inject constructor(
    private val mistakeEngine: MistakeEngine,
    private val reviewScheduler: ReviewScheduler
) {
    // Secondary constructor for tests without Dagger injection
    constructor() : this(MistakeEngine(), ReviewScheduler())

    /**
     * Compute comprehensive progress dashboard data from current user database records.
     */
    fun computeDashboardData(
        snapshots: List<MasterySnapshot>,
        evidences: List<LearningEvidence>,
        mistakes: List<MistakeRecord>,
        reviews: List<ReviewSchedule>,
        currentTimestamp: Long
    ): ProgressDashboardData = computeDashboardData(
        snapshots = snapshots,
        evidences = evidences,
        mistakes = mistakes,
        reviews = reviews,
        userProfile = null,
        currentTimestamp = currentTimestamp
    )

    fun computeDashboardData(
        snapshots: List<MasterySnapshot>,
        evidences: List<LearningEvidence>,
        mistakes: List<MistakeRecord>,
        reviews: List<ReviewSchedule>,
        userProfile: UserProfile? = null,
        currentTimestamp: Long = System.currentTimeMillis()
    ): ProgressDashboardData {
        val vocabProficiency = computeSkillProficiency("vocabulary", "Vocabulary", snapshots, evidences, userProfile?.estimatedVocabLevel)
        val grammarProficiency = computeSkillProficiency("grammar", "Grammar", snapshots, evidences, userProfile?.estimatedGrammarLevel)
        val readingProficiency = computeSkillProficiency("reading", "Reading", snapshots, evidences, userProfile?.estimatedReadingLevel)
        val listeningProficiency = computeSkillProficiency("listening", "Listening", snapshots, evidences, userProfile?.estimatedListeningLevel)
        val speakingProficiency = computeSkillProficiency("speaking", "Speaking", snapshots, evidences, userProfile?.estimatedSpeakingLevel)

        val skills = listOf(
            vocabProficiency,
            grammarProficiency,
            readingProficiency,
            listeningProficiency,
            speakingProficiency
        )

        val overallCefr = userProfile?.estimatedOverallLevel?.ifBlank { null } ?: computeOverallCefr(skills)
        val overallMasteryPercentage = if (skills.isNotEmpty()) {
            skills.map { it.masteryPercentage }.average().roundToInt()
        } else {
            0
        }

        val streak = computeActivityStreak(evidences, currentTimestamp)
        val mistakeHealth = mistakeEngine.computeMistakeHealth(mistakes)
        val dueReviewsCount = reviews.count { it.nextReviewDueTimestamp <= currentTimestamp }

        return ProgressDashboardData(
            skills = skills,
            overallEstimatedCefr = overallCefr,
            overallMasteryPercentage = overallMasteryPercentage,
            streak = streak,
            mistakeHealthScore = mistakeHealth,
            reviewsDueCount = dueReviewsCount
        )
    }

    /**
     * Compute proficiency metrics for a specific domain.
     */
    fun computeSkillProficiency(
        domainKey: String,
        displayName: String,
        allSnapshots: List<MasterySnapshot>,
        allEvidences: List<LearningEvidence>,
        profileCefrLevel: String? = null
    ): SkillProficiency {
        val domainSnapshots = allSnapshots.filter { it.domain.equals(domainKey, ignoreCase = true) }
        val domainEvidences = allEvidences.filter { it.domain.equals(domainKey, ignoreCase = true) }

        val masteredCount = domainSnapshots.count {
            it.status == MasteryStatus.MASTERED || it.status == MasteryStatus.MAINTAINING
        }
        val learningCount = domainSnapshots.count {
            it.status == MasteryStatus.LEARNING || it.status == MasteryStatus.PRACTICED
        }

        val totalAttempts = if (domainSnapshots.isNotEmpty()) {
            domainSnapshots.sumOf { it.totalAttempts }
        } else {
            domainEvidences.size
        }

        val correctAttempts = if (domainSnapshots.isNotEmpty()) {
            domainSnapshots.sumOf { it.correctAttempts }
        } else {
            domainEvidences.count { it.isCorrect }
        }

        val accuracy = if (totalAttempts > 0) {
            correctAttempts.toFloat() / totalAttempts.toFloat()
        } else {
            0.0f
        }

        val avgLevel = if (domainSnapshots.isNotEmpty()) {
            domainSnapshots.map { it.level }.average().toFloat()
        } else if (domainEvidences.isNotEmpty()) {
            accuracy * 0.5f
        } else {
            0.0f
        }

        val avgConfidence = if (domainSnapshots.isNotEmpty()) {
            domainSnapshots.map { it.confidence }.average().toFloat()
        } else {
            0.0f
        }

        val cefrLevel = profileCefrLevel?.ifBlank { null } ?: evaluateCefrLevel(domainKey, masteredCount, avgLevel, totalAttempts)

        return SkillProficiency(
            domain = domainKey,
            displayName = displayName,
            cefrLevel = cefrLevel,
            masteryPercentage = (avgLevel * 100f).roundToInt().coerceIn(0, 100),
            confidence = avgConfidence.coerceIn(0.0f, 1.0f),
            itemsMastered = masteredCount,
            itemsLearning = learningCount,
            totalAttempts = totalAttempts,
            accuracyRate = accuracy.coerceIn(0.0f, 1.0f)
        )
    }

    /**
     * Compute activity streak and historical volume from learning evidence.
     */
    fun computeActivityStreak(
        evidences: List<LearningEvidence>,
        currentTimestamp: Long = System.currentTimeMillis()
    ): ActivityStreak {
        if (evidences.isEmpty()) {
            return ActivityStreak(
                currentStreakDays = 0,
                lastActiveDateFormatted = "No activity yet",
                totalPracticedItems = 0,
                totalCorrectItems = 0,
                overallAccuracyRate = 0.0f
            )
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
            timeZone = TimeZone.getDefault()
        }

        // Distinct active dates in descending chronological order
        val activeDates = evidences
            .map { dateFormat.format(Date(it.timestamp)) }
            .distinct()
            .sortedDescending()

        val todayStr = dateFormat.format(Date(currentTimestamp))
        val yesterdayStr = dateFormat.format(Date(currentTimestamp - MILLIS_PER_DAY))

        var streak = 0
        if (activeDates.contains(todayStr) || activeDates.contains(yesterdayStr)) {
            // Count consecutive days backward
            var checkDate = if (activeDates.contains(todayStr)) currentTimestamp else currentTimestamp - MILLIS_PER_DAY
            while (true) {
                val formatted = dateFormat.format(Date(checkDate))
                if (activeDates.contains(formatted)) {
                    streak++
                    checkDate -= MILLIS_PER_DAY
                } else {
                    break
                }
            }
        }

        val totalItems = evidences.size
        val correctItems = evidences.count { it.isCorrect }
        val accuracy = if (totalItems > 0) correctItems.toFloat() / totalItems.toFloat() else 0.0f

        return ActivityStreak(
            currentStreakDays = streak,
            lastActiveDateFormatted = activeDates.firstOrNull() ?: todayStr,
            totalPracticedItems = totalItems,
            totalCorrectItems = correctItems,
            overallAccuracyRate = accuracy
        )
    }

    private fun evaluateCefrLevel(
        domain: String,
        masteredCount: Int,
        avgLevel: Float,
        totalAttempts: Int
    ): String {
        if (totalAttempts == 0) return "A2" // Default foundation floor

        return when (domain.lowercase()) {
            "vocabulary" -> when {
                masteredCount >= 30 && avgLevel >= 0.85f -> "C1"
                masteredCount >= 15 && avgLevel >= 0.70f -> "B2"
                masteredCount >= 5 && avgLevel >= 0.50f -> "B1"
                else -> "A2"
            }
            "grammar" -> when {
                masteredCount >= 8 && avgLevel >= 0.85f -> "C1"
                masteredCount >= 4 && avgLevel >= 0.70f -> "B2"
                masteredCount >= 2 && avgLevel >= 0.50f -> "B1"
                else -> "A2"
            }
            "reading" -> when {
                masteredCount >= 4 && avgLevel >= 0.80f -> "C1"
                masteredCount >= 2 && avgLevel >= 0.65f -> "B2"
                masteredCount >= 1 -> "B1"
                else -> "A2"
            }
            "listening" -> when {
                masteredCount >= 4 && avgLevel >= 0.80f -> "C1"
                masteredCount >= 2 && avgLevel >= 0.65f -> "B2"
                masteredCount >= 1 -> "B1"
                else -> "A2"
            }
            "speaking" -> when {
                masteredCount >= 4 && avgLevel >= 0.80f -> "C1"
                masteredCount >= 2 && avgLevel >= 0.65f -> "B2"
                masteredCount >= 1 -> "B1"
                else -> "A2"
            }
            else -> "A2"
        }
    }

    private fun computeOverallCefr(skills: List<SkillProficiency>): String {
        val levelRanks = mapOf("A2" to 1, "B1" to 2, "B2" to 3, "C1" to 4, "C2" to 5)
        val rankToLevel = mapOf(1 to "A2", 2 to "B1", 3 to "B2", 4 to "C1", 5 to "C2")

        val activeSkills = skills.filter { it.totalAttempts > 0 }
        if (activeSkills.isEmpty()) return "B1" // Sensible initial profile floor for target user

        val avgRank = activeSkills.map { levelRanks[it.cefrLevel] ?: 1 }.average().roundToInt()
        return rankToLevel[avgRank] ?: "B1"
    }
}
