package com.seanora.fluentai.domain.progress

/**
 * Domain-specific representation of user proficiency in a single skill area.
 */
data class SkillProficiency(
    val domain: String,
    val displayName: String,
    val cefrLevel: String,
    val masteryPercentage: Int, // 0 to 100
    val confidence: Float,      // 0.0 to 1.0
    val itemsMastered: Int,
    val itemsLearning: Int,
    val totalAttempts: Int,
    val accuracyRate: Float     // 0.0 to 1.0
)

/**
 * Daily activity streak and aggregate volume statistics.
 */
data class ActivityStreak(
    val currentStreakDays: Int,
    val lastActiveDateFormatted: String,
    val totalPracticedItems: Int,
    val totalCorrectItems: Int,
    val overallAccuracyRate: Float
)

/**
 * Complete snapshot of the user's progress for dashboards and navigation.
 */
data class ProgressDashboardData(
    val skills: List<SkillProficiency>,
    val overallEstimatedCefr: String,
    val overallMasteryPercentage: Int,
    val streak: ActivityStreak,
    val mistakeHealthScore: Int,
    val reviewsDueCount: Int
)
