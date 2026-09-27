package com.seanora.fluentai.data.user.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.seanora.fluentai.data.user.entity.LearningEvidenceEntity
import com.seanora.fluentai.data.user.entity.MasterySnapshotEntity
import com.seanora.fluentai.data.user.entity.MistakeRecordEntity
import com.seanora.fluentai.data.user.entity.ReviewScheduleEntity
import com.seanora.fluentai.data.user.entity.SpeakingSessionRecordEntity
import com.seanora.fluentai.data.user.entity.TodayPlanRecordEntity
import com.seanora.fluentai.data.user.entity.UserProfileEntity
import com.seanora.fluentai.data.user.entity.VocabularyFeatureStateEntity
import com.seanora.fluentai.data.user.entity.GrammarFeatureStateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles WHERE id = :id")
    fun getProfile(id: String = "default_user"): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profiles WHERE id = :id")
    suspend fun getProfileSync(id: String = "default_user"): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)
}

@Dao
interface LearningEvidenceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvidence(evidence: LearningEvidenceEntity): Long

    @Query("SELECT * FROM learning_evidence WHERE content_id = :contentId ORDER BY timestamp DESC")
    fun getEvidenceForContent(contentId: String): Flow<List<LearningEvidenceEntity>>

    @Query("SELECT * FROM learning_evidence WHERE content_id = :contentId ORDER BY timestamp DESC")
    suspend fun getEvidenceForContentSync(contentId: String): List<LearningEvidenceEntity>

    @Query("SELECT * FROM learning_evidence ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentEvidence(limit: Int = 50): Flow<List<LearningEvidenceEntity>>

    @Query("SELECT COUNT(*) FROM learning_evidence")
    suspend fun getEvidenceCount(): Int

    @Query("SELECT COUNT(*) FROM learning_evidence WHERE domain = :domain AND is_correct = 1")
    suspend fun getCorrectCountForDomain(domain: String): Int
}

@Dao
interface MasterySnapshotDao {
    @Query("SELECT * FROM mastery_snapshots WHERE content_id = :contentId")
    fun getSnapshot(contentId: String): Flow<MasterySnapshotEntity?>

    @Query("SELECT * FROM mastery_snapshots WHERE content_id = :contentId")
    suspend fun getSnapshotSync(contentId: String): MasterySnapshotEntity?

    @Query("SELECT * FROM mastery_snapshots WHERE domain = :domain")
    fun getSnapshotsForDomain(domain: String): Flow<List<MasterySnapshotEntity>>

    @Query("SELECT * FROM mastery_snapshots")
    fun getAllSnapshots(): Flow<List<MasterySnapshotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSnapshot(snapshot: MasterySnapshotEntity)
}

@Dao
interface ReviewScheduleDao {
    @Query("SELECT * FROM review_schedules WHERE next_review_due_timestamp <= :currentTimestamp ORDER BY next_review_due_timestamp ASC")
    fun getDueReviews(currentTimestamp: Long): Flow<List<ReviewScheduleEntity>>

    @Query("SELECT * FROM review_schedules WHERE content_id = :contentId")
    fun getSchedule(contentId: String): Flow<ReviewScheduleEntity?>

    @Query("SELECT * FROM review_schedules WHERE content_id = :contentId")
    suspend fun getScheduleSync(contentId: String): ReviewScheduleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSchedule(schedule: ReviewScheduleEntity)

    @Query("DELETE FROM review_schedules WHERE content_id = :contentId")
    suspend fun deleteSchedule(contentId: String)
}

@Dao
interface MistakeRecordDao {
    @Query("SELECT * FROM mistake_records WHERE stage != 'RESOLVED' ORDER BY last_observed_at DESC")
    fun getActiveMistakes(): Flow<List<MistakeRecordEntity>>

    @Query("SELECT * FROM mistake_records ORDER BY last_observed_at DESC")
    fun getAllMistakes(): Flow<List<MistakeRecordEntity>>

    @Query("SELECT * FROM mistake_records WHERE content_id = :contentId")
    fun getMistakesForContent(contentId: String): Flow<List<MistakeRecordEntity>>

    @Query("SELECT * FROM mistake_records WHERE content_id = :contentId AND trap_type = :trapType LIMIT 1")
    suspend fun findMistake(contentId: String, trapType: String): MistakeRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMistake(mistake: MistakeRecordEntity): Long

    @Update
    suspend fun updateMistake(mistake: MistakeRecordEntity)
}

@Dao
interface SpeakingSessionDao {
    @Query("SELECT * FROM speaking_sessions ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<SpeakingSessionRecordEntity>>

    @Query("SELECT * FROM speaking_sessions ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentSessions(limit: Int): Flow<List<SpeakingSessionRecordEntity>>

    @Query("SELECT * FROM speaking_sessions WHERE id = :id")
    fun getSessionById(id: String): Flow<SpeakingSessionRecordEntity?>

    @Query("SELECT * FROM speaking_sessions WHERE id = :id")
    suspend fun getSessionByIdSync(id: String): SpeakingSessionRecordEntity?

    @Query("SELECT * FROM speaking_sessions WHERE scenario_id = :scenarioId ORDER BY timestamp DESC")
    fun getSessionsForScenario(scenarioId: String): Flow<List<SpeakingSessionRecordEntity>>

    @Query("SELECT COUNT(*) FROM speaking_sessions")
    suspend fun getSessionCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SpeakingSessionRecordEntity)
}

@Dao
interface TodayPlanDao {
    @Query("SELECT * FROM today_plans WHERE date = :date LIMIT 1")
    fun getPlanForDate(date: String): Flow<TodayPlanRecordEntity?>

    @Query("SELECT * FROM today_plans WHERE date = :date LIMIT 1")
    suspend fun getPlanForDateSync(date: String): TodayPlanRecordEntity?

    @Query("SELECT * FROM today_plans WHERE id = :id LIMIT 1")
    fun getPlanById(id: String): Flow<TodayPlanRecordEntity?>

    @Query("SELECT * FROM today_plans WHERE id = :id LIMIT 1")
    suspend fun getPlanByIdSync(id: String): TodayPlanRecordEntity?

    @Query("SELECT * FROM today_plans ORDER BY date DESC LIMIT :limit")
    fun getRecentPlans(limit: Int = 7): Flow<List<TodayPlanRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePlan(plan: TodayPlanRecordEntity)

    @Query("UPDATE today_plans SET completed_items_count = :completedCount, is_completed = :isCompleted, items_json = :itemsJson, updated_at = :updatedAt WHERE id = :id")
    suspend fun updatePlanCompletion(id: String, completedCount: Int, isCompleted: Boolean, itemsJson: String, updatedAt: Long = System.currentTimeMillis())

    @Query("DELETE FROM today_plans WHERE date = :date")
    suspend fun deletePlanForDate(date: String)
}

@Dao
interface VocabularyFeatureStateDao {
    @Query("SELECT * FROM vocabulary_feature_state WHERE id = 'vocabulary'")
    fun observeState(): Flow<VocabularyFeatureStateEntity?>

    @Query("SELECT * FROM vocabulary_feature_state WHERE id = 'vocabulary'")
    suspend fun getState(): VocabularyFeatureStateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(state: VocabularyFeatureStateEntity)
}

@Dao
interface GrammarFeatureStateDao {
    @Query("SELECT * FROM grammar_feature_state WHERE id = 'grammar'")
    fun observeState(): Flow<GrammarFeatureStateEntity?>

    @Query("SELECT * FROM grammar_feature_state WHERE id = 'grammar'")
    suspend fun getState(): GrammarFeatureStateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(state: GrammarFeatureStateEntity)
}
