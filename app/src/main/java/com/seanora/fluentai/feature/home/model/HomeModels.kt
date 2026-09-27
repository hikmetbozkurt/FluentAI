package com.seanora.fluentai.feature.home.model

import com.seanora.fluentai.core.designsystem.components.CefrLevel
import com.seanora.fluentai.core.model.TodayPracticePlan
import com.seanora.fluentai.core.model.TodayPlanItem
import com.seanora.fluentai.core.model.VocabularyPracticeMode

enum class HomeResumeKind {
    VOCABULARY_WORD,
    VOCABULARY_PRACTICE,
    VOCABULARY_FLASH_CARDS,
    VOCABULARY_COLLECTION,
    GRAMMAR_LEARN,
    GRAMMAR_QUICK,
    GRAMMAR_EXAMPLES,
    GRAMMAR_PRACTICE,
    READING_SESSION,
    LISTENING_SESSION,
    LISTENING_DICTATION,
    SPEAKING_SESSION,
}

data class HomeResumeItem(
    val kind: HomeResumeKind,
    val domain: String,
    val title: String,
    val timestamp: Long,
    val contentId: String? = null,
    val contextId: String? = null,
    val practiceMode: VocabularyPracticeMode? = null,
)

data class TodayPracticeItem(
    val id: String,
    val title: String,
    val domain: String,
    val durationMinutes: Int,
    val description: String,
    val targetLevel: CefrLevel,
    val actionButtonText: String = "Begin Session",
)

data class HomeReviewItem(
    val id: String,
    val title: String,
    val category: String,
    val level: CefrLevel,
    val subtitle: String,
    val statusText: String,
)

data class SkillProfileItem(
    val skill: String,
    val level: CefrLevel,
    val confidence: String,
    val masteryPercent: Int,
)

data class DailyMetrics(
    val practiceMinutesToday: Int,
    val reviewsClearedToday: Int,
    val streakDays: Int,
)

data class WeakAreaItem(
    val id: String,
    val title: String,
    val description: String,
)

data class RecentActivityItem(
    val id: String,
    val contentId: String,
    val domain: String,
    val title: String,
    val description: String,
    val timestamp: Long,
)

data class TodayPlan(
    val heroPractice: TodayPracticeItem,
    val dueReviews: List<HomeReviewItem>,
    val skillProfiles: List<SkillProfileItem>,
    val dailyMetrics: DailyMetrics,
    val practicePlan: TodayPracticePlan? = null,
    val currentLevel: CefrLevel = CefrLevel.B1,
    val targetLevel: CefrLevel = CefrLevel.C1,
    val dailyGoalMinutes: Int = 20,
    val weakAreas: List<WeakAreaItem> = emptyList(),
    val recentActivities: List<RecentActivityItem> = emptyList(),
    val focusItems: List<TodayPlanItem> = emptyList(),
    val displayName: String? = null,
    val continueLearning: HomeResumeItem? = null,
)
