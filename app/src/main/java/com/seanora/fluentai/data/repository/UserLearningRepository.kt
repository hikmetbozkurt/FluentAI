package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.TodayPracticePlan
import com.seanora.fluentai.core.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface UserLearningRepository {
    suspend fun recordEvidence(evidence: LearningEvidence): Long
    fun getEvidenceForContent(contentId: String): Flow<List<LearningEvidence>>
    fun getRecentEvidence(limit: Int = 50): Flow<List<LearningEvidence>>

    fun getMasterySnapshot(contentId: String): Flow<MasterySnapshot?>
    suspend fun getMasterySnapshotSync(contentId: String): MasterySnapshot?
    fun getAllMasterySnapshots(): Flow<List<MasterySnapshot>>
    suspend fun saveMasterySnapshot(snapshot: MasterySnapshot)

    fun getActiveMistakes(): Flow<List<MistakeRecord>>
    fun getAllMistakes(): Flow<List<MistakeRecord>>
    suspend fun findMistake(contentId: String, trapType: String): MistakeRecord?
    suspend fun saveMistake(mistake: MistakeRecord): Long

    fun getDueReviews(currentTimestamp: Long): Flow<List<ReviewSchedule>>
    fun getReviewSchedule(contentId: String): Flow<ReviewSchedule?>
    suspend fun getReviewScheduleSync(contentId: String): ReviewSchedule?
    suspend fun saveReviewSchedule(schedule: ReviewSchedule)

    fun getUserProfile(id: String = "default_user"): Flow<UserProfile?>
    suspend fun getUserProfileSync(id: String = "default_user"): UserProfile?
    suspend fun saveUserProfile(profile: UserProfile)
    suspend fun updateDisplayName(name: String?, id: String = "default_user") {}

    fun getTodayPlan(date: String): Flow<TodayPracticePlan?> = flowOf(null)
    suspend fun getTodayPlanSync(date: String): TodayPracticePlan? = null
    suspend fun getTodayPlanByIdSync(planId: String): TodayPracticePlan? = null
    suspend fun saveTodayPlan(plan: TodayPracticePlan) {}
    suspend fun markTodayPlanItemCompleted(planId: String, itemId: String) {}
}
