package com.seanora.fluentai.domain.progress

import javax.inject.Inject

enum class AchievementTier { BRONZE, SILVER, GOLD }

data class Achievement(
    val id: String,
    val emoji: String,
    val title: String,
    val description: String,
    val tier: AchievementTier,
    val progress: Int,
    val target: Int
) {
    val isUnlocked: Boolean get() = progress >= target
    val completionRatio: Float get() = if (target <= 0) 1f else (progress.toFloat() / target).coerceIn(0f, 1f)
}

class AchievementEvaluator @Inject constructor() {

    fun evaluate(dashboard: ProgressDashboardData): List<Achievement> {
        val practicedSkills = dashboard.skills.count { it.totalAttempts > 0 }
        val masteredItems = dashboard.skills.sumOf { it.itemsMastered }
        val streak = dashboard.streak.currentStreakDays
        val practiced = dashboard.streak.totalPracticedItems
        val accuracyPercent = (dashboard.streak.overallAccuracyRate * 100).toInt()

        return listOf(
            Achievement("first-steps", "👣", "First Steps", "Complete your first activity", AchievementTier.BRONZE, practiced, 1),
            Achievement("streak-3", "🔥", "On Fire", "Practice 3 days in a row", AchievementTier.BRONZE, streak, 3),
            Achievement("streak-7", "🌟", "Week Warrior", "Practice 7 days in a row", AchievementTier.SILVER, streak, 7),
            Achievement("streak-30", "👑", "Habit Master", "Practice 30 days in a row", AchievementTier.GOLD, streak, 30),
            Achievement("explorer", "🧭", "Explorer", "Practice all five skills", AchievementTier.SILVER, practicedSkills, ALL_SKILLS),
            Achievement("collector-25", "💎", "Collector", "Master 25 items", AchievementTier.SILVER, masteredItems, 25),
            Achievement("marathon-100", "🏃", "Marathon", "Practice 100 items", AchievementTier.GOLD, practiced, 100),
            Achievement(
                "sharpshooter",
                "🎯",
                "Sharpshooter",
                "Reach 80% accuracy after 20 items",
                AchievementTier.GOLD,
                if (practiced >= MIN_ITEMS_FOR_ACCURACY) accuracyPercent else 0,
                ACCURACY_TARGET_PERCENT
            )
        ).sortedWith(compareByDescending<Achievement> { it.isUnlocked }.thenByDescending { it.completionRatio })
    }

    private companion object {
        const val ALL_SKILLS = 5
        const val MIN_ITEMS_FOR_ACCURACY = 20
        const val ACCURACY_TARGET_PERCENT = 80
    }
}
