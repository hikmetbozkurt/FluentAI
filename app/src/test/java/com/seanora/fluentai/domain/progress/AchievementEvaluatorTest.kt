package com.seanora.fluentai.domain.progress

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AchievementEvaluatorTest {

    private val evaluator = AchievementEvaluator()

    private fun dashboard(streak: Int, practiced: Int, accuracy: Float, practicedSkills: Int) = ProgressDashboardData(
        skills = (0 until 5).map { index ->
            SkillProficiency(
                domain = "skill$index",
                displayName = "Skill $index",
                cefrLevel = "B1",
                masteryPercentage = 40,
                confidence = 0.5f,
                itemsMastered = if (index < practicedSkills) 6 else 0,
                itemsLearning = 2,
                totalAttempts = if (index < practicedSkills) 4 else 0,
                accuracyRate = accuracy
            )
        },
        overallEstimatedCefr = "B1",
        overallMasteryPercentage = 40,
        streak = ActivityStreak(streak, "Oct 18", practiced, (practiced * accuracy).toInt(), accuracy),
        mistakeHealthScore = 80,
        reviewsDueCount = 0
    )

    @Test
    fun newLearner_hasNoUnlockedAchievements() {
        val achievements = evaluator.evaluate(dashboard(streak = 0, practiced = 0, accuracy = 0f, practicedSkills = 0))
        assertTrue(achievements.none { it.isUnlocked })
    }

    @Test
    fun streakAndVolume_unlockMatchingTiersAndSortUnlockedFirst() {
        val achievements = evaluator.evaluate(dashboard(streak = 7, practiced = 30, accuracy = 0.85f, practicedSkills = 5))
        val unlockedIds = achievements.filter { it.isUnlocked }.map { it.id }.toSet()

        assertTrue(unlockedIds.containsAll(setOf("first-steps", "streak-3", "streak-7", "explorer", "collector-25", "sharpshooter")))
        assertFalse("streak-30" in unlockedIds)
        assertTrue(achievements.first().isUnlocked)
        assertEquals(0.3f, achievements.first { it.id == "marathon-100" }.completionRatio, 0.001f)
    }

    @Test
    fun accuracyAchievement_requiresMinimumVolume() {
        val achievements = evaluator.evaluate(dashboard(streak = 1, practiced = 5, accuracy = 1f, practicedSkills = 1))
        assertFalse(achievements.first { it.id == "sharpshooter" }.isUnlocked)
    }
}
