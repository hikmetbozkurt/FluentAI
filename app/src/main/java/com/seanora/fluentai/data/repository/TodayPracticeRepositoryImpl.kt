package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.DailyMetrics
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.TodayItemType
import com.seanora.fluentai.core.model.TodayPracticePlan
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.domain.today.TodayPlanner
import com.seanora.fluentai.domain.today.TodayPlannerInput
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodayPracticeRepositoryImpl @Inject constructor(
    private val todayPlanner: TodayPlanner,
    private val userLearningRepository: UserLearningRepository,
    private val vocabularyRepository: VocabularyRepository,
    private val grammarRepository: GrammarRepository,
    private val readingRepository: ReadingRepository,
    private val listeningRepository: ListeningRepository,
    private val speakingRepository: SpeakingRepository,
    private val recordLearningEvidence: RecordLearningEvidenceUseCase,
) : TodayPracticeRepository {

    override fun getTodayPlan(date: String): Flow<TodayPracticePlan> = flow {
        val existing = userLearningRepository.getTodayPlanSync(date)
        if (existing == null) {
            generateAndSavePlan(date)
        }
        emitAll(
            userLearningRepository.getTodayPlan(date).filterNotNull()
        )
    }.distinctUntilChanged()

    override suspend fun refreshTodayPlan(date: String): TodayPracticePlan {
        val existing = userLearningRepository.getTodayPlanSync(date)
        val regenerated = createPlan(date)
        val completedByKey = existing?.items
            ?.filter { it.isCompleted }
            ?.associateBy { it.contentId to it.itemType }
            .orEmpty()
        val merged = regenerated.copy(
            items = regenerated.items.map { item ->
                val completed = completedByKey[item.contentId to item.itemType]
                if (completed != null) item.copy(isCompleted = true, completedAt = completed.completedAt) else item
            },
            createdAt = existing?.createdAt ?: regenerated.createdAt
        )
        userLearningRepository.saveTodayPlan(merged)
        return merged
    }

    override suspend fun markItemCompleted(planId: String, itemId: String) {
        val item = userLearningRepository.getTodayPlanByIdSync(planId)
            ?.items
            ?.firstOrNull { it.id == itemId }
            ?: return
        if (item.isCompleted) return

        if (item.itemType !in setOf(TodayItemType.REVIEW_DUE, TodayItemType.MISTAKE_TARGETED)) {
            recordLearningEvidence(
                LearningEvidence(
                    contentId = item.contentId,
                    domain = item.domain,
                    activityType = "today_activity_completed",
                    isCorrect = true,
                    score = 0.6f,
                    details = "Completed assigned Today practice activity",
                )
            )
        }
        userLearningRepository.markTodayPlanItemCompleted(planId, itemId)
        
        // Update user profile streak and last active date
        val profile = userLearningRepository.getUserProfileSync() ?: UserProfile()
        val today = currentDateString()
        val updatedProfile = calculateUpdatedStreak(profile, today)
        userLearningRepository.saveUserProfile(updatedProfile)
    }

    override fun getDailyMetrics(): Flow<DailyMetrics> {
        return combine(
            userLearningRepository.getUserProfile(),
            getTodayPlan(currentDateString())
        ) { profile, plan ->
            val practiceMinutes = plan.completedMinutes
            val reviewsCleared = plan.items.count { it.isCompleted && it.itemType == TodayItemType.REVIEW_DUE }
            DailyMetrics(
                practiceMinutesToday = practiceMinutes,
                reviewsClearedToday = reviewsCleared,
                streakDays = profile?.streakDays ?: 0
            )
        }
    }

    private suspend fun generateAndSavePlan(date: String): TodayPracticePlan {
        val plan = createPlan(date)
        userLearningRepository.saveTodayPlan(plan)
        return plan
    }

    private suspend fun createPlan(date: String): TodayPracticePlan {
        val profile = userLearningRepository.getUserProfileSync() ?: UserProfile()
        val dueCutoff = LocalDate.parse(date)
            .plusDays(1)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli() - 1
        val dueReviews = userLearningRepository.getDueReviews(dueCutoff).firstOrNull() ?: emptyList()
        val activeMistakes = userLearningRepository.getActiveMistakes().firstOrNull() ?: emptyList()
        val masterySnapshots = userLearningRepository.getAllMasterySnapshots().firstOrNull() ?: emptyList()
        val recentEvidence = userLearningRepository.getRecentEvidence(200).firstOrNull() ?: emptyList()

        val availableVocab = vocabularyRepository.getAllVocab().firstOrNull() ?: emptyList()
        val availableGrammar = grammarRepository.getAllLessons().firstOrNull() ?: emptyList()
        val availableReadings = readingRepository.getAllArticles().firstOrNull() ?: emptyList()
        val availableListening = listeningRepository.getAllScenarios().firstOrNull() ?: emptyList()
        val availableSpeaking = speakingRepository.getAllScenarios().firstOrNull() ?: emptyList()

        val input = TodayPlannerInput(
            userProfile = profile,
            dueReviews = dueReviews,
            activeMistakes = activeMistakes,
            masterySnapshots = masterySnapshots,
            recentEvidence = recentEvidence,
            availableVocab = availableVocab,
            availableGrammar = availableGrammar,
            availableReadings = availableReadings,
            availableListening = availableListening,
            availableSpeaking = availableSpeaking,
            targetDate = date
        )

        return todayPlanner.createPlan(input)
    }

    internal fun calculateUpdatedStreak(profile: UserProfile, todayDate: String): UserProfile {
        if (profile.lastActiveDate == todayDate) {
            return profile.copy(updatedAt = System.currentTimeMillis())
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val isYesterday = try {
            if (profile.lastActiveDate.isNotBlank()) {
                val todayCal = Calendar.getInstance().apply { time = sdf.parse(todayDate) ?: Date() }
                val lastActiveCal = Calendar.getInstance().apply { time = sdf.parse(profile.lastActiveDate) ?: Date() }
                todayCal.add(Calendar.DAY_OF_YEAR, -1)
                todayCal.get(Calendar.YEAR) == lastActiveCal.get(Calendar.YEAR) &&
                    todayCal.get(Calendar.DAY_OF_YEAR) == lastActiveCal.get(Calendar.DAY_OF_YEAR)
            } else {
                false
            }
        } catch (_: Exception) {
            false
        }

        val newStreak = if (isYesterday) {
            profile.streakDays + 1
        } else {
            1
        }

        return profile.copy(
            streakDays = newStreak,
            lastActiveDate = todayDate,
            updatedAt = System.currentTimeMillis()
        )
    }
}
