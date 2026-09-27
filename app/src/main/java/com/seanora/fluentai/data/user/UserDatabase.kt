package com.seanora.fluentai.data.user

import androidx.room.Database
import androidx.room.RoomDatabase
import com.seanora.fluentai.data.user.dao.LearningEvidenceDao
import com.seanora.fluentai.data.user.dao.MasterySnapshotDao
import com.seanora.fluentai.data.user.dao.MistakeRecordDao
import com.seanora.fluentai.data.user.dao.ReviewScheduleDao
import com.seanora.fluentai.data.user.dao.SpeakingSessionDao
import com.seanora.fluentai.data.user.dao.TodayPlanDao
import com.seanora.fluentai.data.user.dao.UserProfileDao
import com.seanora.fluentai.data.user.dao.VocabularyFeatureStateDao
import com.seanora.fluentai.data.user.dao.GrammarFeatureStateDao
import com.seanora.fluentai.data.user.entity.LearningEvidenceEntity
import com.seanora.fluentai.data.user.entity.MasterySnapshotEntity
import com.seanora.fluentai.data.user.entity.MistakeRecordEntity
import com.seanora.fluentai.data.user.entity.ReviewScheduleEntity
import com.seanora.fluentai.data.user.entity.SpeakingSessionRecordEntity
import com.seanora.fluentai.data.user.entity.TodayPlanRecordEntity
import com.seanora.fluentai.data.user.entity.UserProfileEntity
import com.seanora.fluentai.data.user.entity.VocabularyFeatureStateEntity
import com.seanora.fluentai.data.user.entity.GrammarFeatureStateEntity

@Database(
    entities = [
        UserProfileEntity::class,
        LearningEvidenceEntity::class,
        MasterySnapshotEntity::class,
        ReviewScheduleEntity::class,
        MistakeRecordEntity::class,
        SpeakingSessionRecordEntity::class,
        TodayPlanRecordEntity::class,
        VocabularyFeatureStateEntity::class,
        GrammarFeatureStateEntity::class
    ],
    version = 7,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun learningEvidenceDao(): LearningEvidenceDao
    abstract fun masterySnapshotDao(): MasterySnapshotDao
    abstract fun reviewScheduleDao(): ReviewScheduleDao
    abstract fun mistakeRecordDao(): MistakeRecordDao
    abstract fun speakingSessionDao(): SpeakingSessionDao
    abstract fun todayPlanDao(): TodayPlanDao
    abstract fun vocabularyFeatureStateDao(): VocabularyFeatureStateDao
    abstract fun grammarFeatureStateDao(): GrammarFeatureStateDao

    companion object {
        const val DATABASE_NAME = "user.db"
    }
}
