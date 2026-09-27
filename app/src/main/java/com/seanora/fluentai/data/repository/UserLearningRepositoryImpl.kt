package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.TodayPracticePlan
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.data.user.dao.LearningEvidenceDao
import com.seanora.fluentai.data.user.dao.MasterySnapshotDao
import com.seanora.fluentai.data.user.dao.MistakeRecordDao
import com.seanora.fluentai.data.user.dao.ReviewScheduleDao
import com.seanora.fluentai.data.user.dao.TodayPlanDao
import com.seanora.fluentai.data.user.dao.UserProfileDao
import com.seanora.fluentai.data.user.mapper.toDomain
import com.seanora.fluentai.data.user.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLearningRepositoryImpl @Inject constructor(
    private val learningEvidenceDao: LearningEvidenceDao,
    private val masterySnapshotDao: MasterySnapshotDao,
    private val mistakeRecordDao: MistakeRecordDao,
    private val reviewScheduleDao: ReviewScheduleDao,
    private val userProfileDao: UserProfileDao,
    private val todayPlanDao: TodayPlanDao
) : UserLearningRepository {

    override suspend fun recordEvidence(evidence: LearningEvidence): Long {
        return learningEvidenceDao.insertEvidence(evidence.toEntity())
    }

    override fun getEvidenceForContent(contentId: String): Flow<List<LearningEvidence>> {
        return learningEvidenceDao.getEvidenceForContent(contentId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getRecentEvidence(limit: Int): Flow<List<LearningEvidence>> {
        return learningEvidenceDao.getRecentEvidence(limit).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getMasterySnapshot(contentId: String): Flow<MasterySnapshot?> {
        return masterySnapshotDao.getSnapshot(contentId).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun getMasterySnapshotSync(contentId: String): MasterySnapshot? {
        return masterySnapshotDao.getSnapshotSync(contentId)?.toDomain()
    }

    override fun getAllMasterySnapshots(): Flow<List<MasterySnapshot>> {
        return masterySnapshotDao.getAllSnapshots().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveMasterySnapshot(snapshot: MasterySnapshot) {
        masterySnapshotDao.upsertSnapshot(snapshot.toEntity())
    }

    override fun getActiveMistakes(): Flow<List<MistakeRecord>> {
        return mistakeRecordDao.getActiveMistakes().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllMistakes(): Flow<List<MistakeRecord>> {
        return mistakeRecordDao.getAllMistakes().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun findMistake(contentId: String, trapType: String): MistakeRecord? {
        return mistakeRecordDao.findMistake(contentId, trapType)?.toDomain()
    }

    override suspend fun saveMistake(mistake: MistakeRecord): Long {
        val entity = mistake.toEntity()
        return if (mistake.id == 0L) {
            mistakeRecordDao.insertMistake(entity)
        } else {
            mistakeRecordDao.updateMistake(entity)
            mistake.id
        }
    }

    override fun getDueReviews(currentTimestamp: Long): Flow<List<ReviewSchedule>> {
        return reviewScheduleDao.getDueReviews(currentTimestamp).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getReviewSchedule(contentId: String): Flow<ReviewSchedule?> {
        return reviewScheduleDao.getSchedule(contentId).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun getReviewScheduleSync(contentId: String): ReviewSchedule? {
        return reviewScheduleDao.getScheduleSync(contentId)?.toDomain()
    }

    override suspend fun saveReviewSchedule(schedule: ReviewSchedule) {
        reviewScheduleDao.upsertSchedule(schedule.toEntity())
    }

    override fun getUserProfile(id: String): Flow<UserProfile?> {
        return userProfileDao.getProfile(id).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun getUserProfileSync(id: String): UserProfile? {
        return userProfileDao.getProfileSync(id)?.toDomain()
    }

    override suspend fun saveUserProfile(profile: UserProfile) {
        userProfileDao.insertOrUpdateProfile(profile.toEntity())
    }

    override suspend fun updateDisplayName(name: String?, id: String) {
        val existing = getUserProfileSync(id) ?: return
        val trimmed = name?.trim()?.ifBlank { null }
        saveUserProfile(existing.copy(displayName = trimmed, updatedAt = System.currentTimeMillis()))
    }

    override fun getTodayPlan(date: String): Flow<TodayPracticePlan?> {
        return todayPlanDao.getPlanForDate(date).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun getTodayPlanSync(date: String): TodayPracticePlan? {
        return todayPlanDao.getPlanForDateSync(date)?.toDomain()
    }

    override suspend fun getTodayPlanByIdSync(planId: String): TodayPracticePlan? {
        return todayPlanDao.getPlanByIdSync(planId)?.toDomain()
    }

    override suspend fun saveTodayPlan(plan: TodayPracticePlan) {
        todayPlanDao.insertOrUpdatePlan(plan.toEntity())
    }

    override suspend fun markTodayPlanItemCompleted(planId: String, itemId: String) {
        val existing = todayPlanDao.getPlanByIdSync(planId) ?: return
        val domainPlan = existing.toDomain()
        val updatedItems = domainPlan.items.map { item ->
            if (item.id == itemId) {
                item.copy(isCompleted = true, completedAt = System.currentTimeMillis())
            } else {
                item
            }
        }
        val updatedPlan = domainPlan.copy(items = updatedItems)
        todayPlanDao.insertOrUpdatePlan(updatedPlan.toEntity())
    }
}
