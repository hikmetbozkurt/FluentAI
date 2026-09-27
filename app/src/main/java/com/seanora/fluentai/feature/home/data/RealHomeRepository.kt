package com.seanora.fluentai.feature.home.data

import com.seanora.fluentai.core.designsystem.components.CefrLevel
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.TodayItemType
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.core.model.GrammarFeatureState
import com.seanora.fluentai.core.model.GrammarResumeDestination
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.VocabularyFeatureState
import com.seanora.fluentai.core.model.VocabularyResumeDestination
import com.seanora.fluentai.data.repository.GrammarFeatureStateRepository
import com.seanora.fluentai.data.repository.TodayPracticeRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.data.repository.VocabularyFeatureStateRepository
import com.seanora.fluentai.domain.progress.ProgressEngine
import com.seanora.fluentai.feature.home.model.HomeResumeItem
import com.seanora.fluentai.feature.home.model.HomeResumeKind
import com.seanora.fluentai.feature.home.model.HomeReviewItem
import com.seanora.fluentai.feature.home.model.SkillProfileItem
import com.seanora.fluentai.feature.home.model.TodayPlan
import com.seanora.fluentai.feature.home.model.TodayPracticeItem
import com.seanora.fluentai.feature.home.model.WeakAreaItem
import com.seanora.fluentai.feature.home.model.RecentActivityItem
import com.seanora.fluentai.feature.home.model.DailyMetrics as HomeDailyMetrics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealHomeRepository @Inject constructor(
    private val todayPracticeRepository: TodayPracticeRepository,
    private val userLearningRepository: UserLearningRepository,
    private val vocabularyStateRepository: VocabularyFeatureStateRepository,
    private val grammarStateRepository: GrammarFeatureStateRepository,
    private val progressEngine: ProgressEngine,
) : HomeRepository {

    override fun getTodayPlan(): Flow<TodayPlan> {
        val primary = combine(
            todayPracticeRepository.getTodayPlan(),
            todayPracticeRepository.getDailyMetrics(),
            userLearningRepository.getUserProfile(),
            userLearningRepository.getAllMasterySnapshots()
        ) { practicePlan, metrics, profile, snapshots ->
            PrimaryHomeData(practicePlan, metrics, profile, snapshots)
        }
        return combine(
            primary,
            userLearningRepository.getActiveMistakes(),
            userLearningRepository.getRecentEvidence(20),
            vocabularyStateRepository.observeState(),
            grammarStateRepository.observeState(),
        ) { primaryData, mistakes, evidence, vocabularyState, grammarState ->
            val practicePlan = primaryData.practicePlan
            val metrics = primaryData.metrics
            val profile = primaryData.profile
            val snapshots = primaryData.snapshots
            val userProfile = profile ?: UserProfile()
            val nextItem = practicePlan.nextUncompletedItem

            val heroPractice = if (nextItem != null) {
                TodayPracticeItem(
                    id = nextItem.contentId,
                    title = nextItem.title,
                    domain = nextItem.itemType.displayName,
                    durationMinutes = nextItem.estimatedMinutes,
                    description = nextItem.explanation,
                    targetLevel = CefrLevel.entries.find { it.name.equals(nextItem.targetCefrLevel, ignoreCase = true) } ?: CefrLevel.B2,
                    actionButtonText = if (nextItem.isCompleted) "Completed" else "Begin Session"
                )
            } else if (practicePlan.isCompleted) {
                TodayPracticeItem(
                    id = "complete",
                    title = "Daily Practice Complete",
                    domain = "Celebration",
                    durationMinutes = 0,
                    description = "You've completed all assigned activities for today! Continue exploring below.",
                    targetLevel = CefrLevel.entries.find { it.name.equals(practicePlan.targetCefrLevel, ignoreCase = true) } ?: CefrLevel.B2,
                    actionButtonText = "Explore More"
                )
            } else {
                val fallbackItem = practicePlan.items.firstOrNull()
                if (fallbackItem != null) {
                    TodayPracticeItem(
                        id = fallbackItem.contentId,
                        title = fallbackItem.title,
                        domain = fallbackItem.itemType.displayName,
                        durationMinutes = fallbackItem.estimatedMinutes,
                        description = fallbackItem.explanation,
                        targetLevel = CefrLevel.entries.find { it.name.equals(fallbackItem.targetCefrLevel, ignoreCase = true) } ?: CefrLevel.B2,
                        actionButtonText = "Begin Session"
                    )
                } else {
                    TodayPracticeItem(
                        id = "complete",
                        title = "Daily Practice Complete",
                        domain = "Celebration",
                        durationMinutes = 0,
                        description = "You've completed all assigned activities for today! Continue exploring below.",
                        targetLevel = CefrLevel.entries.find { it.name.equals(practicePlan.targetCefrLevel, ignoreCase = true) } ?: CefrLevel.B2,
                        actionButtonText = "Explore More"
                    )
                }
            }

            val dueReviews = practicePlan.items
                .filter { it.itemType == TodayItemType.REVIEW_DUE || it.itemType == TodayItemType.MISTAKE_TARGETED }
                .map { item ->
                    HomeReviewItem(
                        id = item.contentId,
                        title = item.title,
                        category = if (item.itemType == TodayItemType.REVIEW_DUE) "Spaced Review • ${item.domain.replaceFirstChar { it.uppercase() }}" else "Common Trap • Turkish Learner",
                        level = CefrLevel.entries.find { it.name.equals(item.targetCefrLevel, ignoreCase = true) } ?: CefrLevel.B2,
                        subtitle = item.explanation,
                        statusText = if (item.isCompleted) "Cleared" else if (item.itemType == TodayItemType.REVIEW_DUE) "Due today" else "Recurring"
                    )
                }

            val skillProfiles = buildSkillProfiles(userProfile, snapshots, evidence)
            val continueLearning = buildContinueLearning(
                vocabularyState = vocabularyState,
                grammarState = grammarState,
                evidence = evidence,
                planItems = practicePlan.items,
            )
            val recentActivities = evidence
                .sortedByDescending { it.timestamp }
                .distinctBy { it.contentId }
                .take(5)
                .map { item ->
                    RecentActivityItem(
                        id = "${item.contentId}-${item.timestamp}",
                        contentId = item.contentId,
                        domain = item.domain,
                        title = practicePlan.items.firstOrNull { it.contentId == item.contentId }?.title
                            ?: semanticTitle(item.contentId),
                        description = "${item.domain.replaceFirstChar { it.uppercase() }} · ${(item.score * 100).toInt()}%",
                        timestamp = item.timestamp,
                    )
                }

            TodayPlan(
                heroPractice = heroPractice,
                dueReviews = dueReviews,
                skillProfiles = skillProfiles,
                dailyMetrics = HomeDailyMetrics(
                    practiceMinutesToday = metrics.practiceMinutesToday,
                    reviewsClearedToday = metrics.reviewsClearedToday,
                    streakDays = metrics.streakDays
                ),
                practicePlan = practicePlan,
                currentLevel = cefr(userProfile.estimatedOverallLevel),
                targetLevel = cefr(userProfile.targetCefrLevel),
                dailyGoalMinutes = userProfile.dailyGoalMinutes,
                weakAreas = mistakes.take(3).map { mistake ->
                    WeakAreaItem(
                        id = mistake.contentId,
                        title = mistake.contentId.substringAfterLast('.').replace('-', ' ').replaceFirstChar { it.uppercase() },
                        description = mistake.errorDescription,
                    )
                },
                recentActivities = recentActivities,
                focusItems = practicePlan.items.filterNot { it.isCompleted }.take(5),
                displayName = userProfile.displayName,
                continueLearning = continueLearning,
            )
        }
    }

    override suspend fun markItemCompleted(planId: String, itemId: String) {
        todayPracticeRepository.markItemCompleted(planId, itemId)
    }

    override suspend fun refreshPlan() {
        todayPracticeRepository.refreshTodayPlan()
    }

    private fun buildSkillProfiles(
        userProfile: UserProfile,
        snapshots: List<MasterySnapshot>,
        evidence: List<LearningEvidence>,
    ): List<SkillProfileItem> {
        val skills = listOf(
            Triple("Vocabulary", userProfile.estimatedVocabLevel, "vocabulary"),
            Triple("Grammar", userProfile.estimatedGrammarLevel, "grammar"),
            Triple("Listening", userProfile.estimatedListeningLevel, "listening"),
            Triple("Speaking", userProfile.estimatedSpeakingLevel, "speaking"),
            Triple("Reading", userProfile.estimatedReadingLevel, "reading"),
        )

        return skills.map { (skillName, profileLevelStr, domainKey) ->
            val proficiency = progressEngine.computeSkillProficiency(
                domainKey = domainKey,
                displayName = skillName,
                allSnapshots = snapshots,
                allEvidences = evidence,
                profileCefrLevel = profileLevelStr,
            )
            SkillProfileItem(
                skill = skillName,
                level = cefr(proficiency.cefrLevel),
                confidence = if (proficiency.totalAttempts == 0) "Calibrating" else "Measured",
                masteryPercent = proficiency.masteryPercentage,
            )
        }
    }

    private fun buildContinueLearning(
        vocabularyState: VocabularyFeatureState,
        grammarState: GrammarFeatureState,
        evidence: List<LearningEvidence>,
        planItems: List<com.seanora.fluentai.core.model.TodayPlanItem>,
    ): HomeResumeItem? {
        val candidates = mutableListOf<HomeResumeItem>()
        vocabularyResume(vocabularyState)?.let(candidates::add)
        grammarResume(grammarState)?.let(candidates::add)
        evidence.maxByOrNull { it.timestamp }?.let { item ->
            val kind = when (item.domain.lowercase()) {
                "vocabulary" -> if (item.activityType.startsWith("practice_")) HomeResumeKind.VOCABULARY_PRACTICE else HomeResumeKind.VOCABULARY_WORD
                "grammar" -> if (item.activityType.contains("exercise") || item.activityType.contains("builder") || item.activityType.contains("spotter")) HomeResumeKind.GRAMMAR_PRACTICE else HomeResumeKind.GRAMMAR_LEARN
                "reading" -> HomeResumeKind.READING_SESSION
                "listening" -> if (item.activityType == "dictation") HomeResumeKind.LISTENING_DICTATION else HomeResumeKind.LISTENING_SESSION
                "speaking", "pronunciation" -> HomeResumeKind.SPEAKING_SESSION
                else -> null
            }
            if (kind != null) candidates += HomeResumeItem(
                kind = kind,
                domain = item.domain.replaceFirstChar { it.uppercase() },
                title = planItems.firstOrNull { it.contentId == item.contentId }?.title ?: semanticTitle(item.contentId),
                timestamp = item.timestamp,
                contentId = item.contentId,
            )
        }
        return candidates.maxByOrNull { it.timestamp }
    }

    private fun vocabularyResume(state: VocabularyFeatureState): HomeResumeItem? {
        val destination = state.resumeDestination ?: return null
        val kind = when (destination) {
            VocabularyResumeDestination.WORD_OF_DAY,
            VocabularyResumeDestination.LIBRARY -> HomeResumeKind.VOCABULARY_WORD
            VocabularyResumeDestination.FLASH_CARDS -> HomeResumeKind.VOCABULARY_FLASH_CARDS
            VocabularyResumeDestination.PRACTICE_LABS -> HomeResumeKind.VOCABULARY_PRACTICE
            VocabularyResumeDestination.COLLECTIONS -> HomeResumeKind.VOCABULARY_COLLECTION
        }
        val contentId = state.resumeContentId
        if (kind == HomeResumeKind.VOCABULARY_WORD && contentId == null) return null
        return HomeResumeItem(
            kind = kind,
            domain = "Vocabulary",
            title = contentId?.let(::semanticTitle) ?: when (kind) {
                HomeResumeKind.VOCABULARY_COLLECTION -> "Vocabulary collection"
                else -> "Vocabulary practice"
            },
            timestamp = state.updatedAt,
            contentId = contentId,
            contextId = state.resumeContextId,
            practiceMode = state.resumePracticeMode,
        )
    }

    private fun grammarResume(state: GrammarFeatureState): HomeResumeItem? {
        val contentId = state.resumeContentId ?: return null
        val kind = when (state.resumeDestination ?: return null) {
            GrammarResumeDestination.GRAMMAR_BITES -> HomeResumeKind.GRAMMAR_QUICK
            GrammarResumeDestination.GRAMMAR_IN_CONTEXT -> HomeResumeKind.GRAMMAR_EXAMPLES
            GrammarResumeDestination.SENTENCE_BUILDER,
            GrammarResumeDestination.ERROR_SPOTTER,
            GrammarResumeDestination.WEAK_SPOTS -> HomeResumeKind.GRAMMAR_PRACTICE
            else -> HomeResumeKind.GRAMMAR_LEARN
        }
        return HomeResumeItem(kind, "Grammar", semanticTitle(contentId), state.updatedAt, contentId)
    }

    private fun semanticTitle(contentId: String): String = contentId.substringAfterLast('.')
        .replace('-', ' ')
        .replaceFirstChar { it.uppercase() }

    private fun cefr(value: String): CefrLevel =
        CefrLevel.entries.find { it.name.equals(value, ignoreCase = true) } ?: CefrLevel.B1

    private data class PrimaryHomeData(
        val practicePlan: com.seanora.fluentai.core.model.TodayPracticePlan,
        val metrics: com.seanora.fluentai.core.model.DailyMetrics,
        val profile: UserProfile?,
        val snapshots: List<MasterySnapshot>,
    )
}
