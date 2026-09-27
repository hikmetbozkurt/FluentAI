package com.seanora.fluentai.feature.home.data

import com.seanora.fluentai.core.designsystem.components.CefrLevel
import com.seanora.fluentai.feature.home.model.DailyMetrics
import com.seanora.fluentai.feature.home.model.HomeReviewItem
import com.seanora.fluentai.feature.home.model.SkillProfileItem
import com.seanora.fluentai.feature.home.model.TodayPlan
import com.seanora.fluentai.feature.home.model.TodayPracticeItem
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface HomeRepository {
    fun getTodayPlan(): Flow<TodayPlan>
    suspend fun markItemCompleted(planId: String, itemId: String) {}
    suspend fun refreshPlan() {}
}

@Singleton
class MockHomeRepository @Inject constructor() : HomeRepository {

    override fun getTodayPlan(): Flow<TodayPlan> {
        val mockPlan = TodayPlan(
            heroPractice = TodayPracticeItem(
                id = "speaking.c1.executive-alignment-001",
                title = "Strategy Alignment Simulation",
                domain = "Speaking Coach",
                durationMinutes = 15,
                description = "Defend quarterly goals and navigate rapid executive interruptions in a simulated English business sync.",
                targetLevel = CefrLevel.C1,
                actionButtonText = "Begin Session",
            ),
            dueReviews = listOf(
                HomeReviewItem(
                    id = "vocab.deliberation",
                    title = "deliberation",
                    category = "Vocabulary • B2",
                    level = CefrLevel.B2,
                    subtitle = "Long and careful consideration or discussion before making a crucial decision.",
                    statusText = "Due today",
                ),
                HomeReviewItem(
                    id = "grammar.present-perfect-vs-past-simple",
                    title = "Present Perfect vs. Past Simple",
                    category = "Common Trap • Turkish Learner",
                    level = CefrLevel.B1,
                    subtitle = "Avoiding Turkish direct tense translation for unfinished time spans ('I have lived here for 3 years').",
                    statusText = "Recurring",
                ),
            ),
            skillProfiles = listOf(
                SkillProfileItem(skill = "Vocabulary", level = CefrLevel.B2, confidence = "High", masteryPercent = 74),
                SkillProfileItem(skill = "Grammar", level = CefrLevel.B1, confidence = "Medium", masteryPercent = 62),
                SkillProfileItem(skill = "Reading", level = CefrLevel.C1, confidence = "High", masteryPercent = 88),
                SkillProfileItem(skill = "Speaking", level = CefrLevel.B2, confidence = "Calibrating", masteryPercent = 68),
            ),
            dailyMetrics = DailyMetrics(
                practiceMinutesToday = 15,
                reviewsClearedToday = 12,
                streakDays = 14,
            ),
        )
        return flowOf(mockPlan)
    }
}
