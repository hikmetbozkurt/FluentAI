package com.seanora.fluentai.data.user.di

import android.content.Context
import androidx.room.Room
import com.seanora.fluentai.data.user.UserDatabase
import com.seanora.fluentai.data.user.UserDatabaseMigrations
import com.seanora.fluentai.data.user.dao.LearningEvidenceDao
import com.seanora.fluentai.data.user.dao.MasterySnapshotDao
import com.seanora.fluentai.data.user.dao.MistakeRecordDao
import com.seanora.fluentai.data.user.dao.ReviewScheduleDao
import com.seanora.fluentai.data.user.dao.UserProfileDao
import com.seanora.fluentai.data.user.dao.VocabularyFeatureStateDao
import com.seanora.fluentai.data.user.dao.GrammarFeatureStateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserDatabaseModule {

    @Provides
    @Singleton
    fun provideUserDatabase(
        @ApplicationContext context: Context
    ): UserDatabase {
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            UserDatabase.DATABASE_NAME
        )
            .addMigrations(*UserDatabaseMigrations.ALL)
            .build()
    }

    @Provides
    fun provideUserProfileDao(database: UserDatabase): UserProfileDao = database.userProfileDao()

    @Provides
    fun provideLearningEvidenceDao(database: UserDatabase): LearningEvidenceDao = database.learningEvidenceDao()

    @Provides
    fun provideMasterySnapshotDao(database: UserDatabase): MasterySnapshotDao = database.masterySnapshotDao()

    @Provides
    fun provideReviewScheduleDao(database: UserDatabase): ReviewScheduleDao = database.reviewScheduleDao()

    @Provides
    fun provideMistakeRecordDao(database: UserDatabase): MistakeRecordDao = database.mistakeRecordDao()

    @Provides
    fun provideSpeakingSessionDao(database: UserDatabase): com.seanora.fluentai.data.user.dao.SpeakingSessionDao = database.speakingSessionDao()

    @Provides
    fun provideTodayPlanDao(database: UserDatabase): com.seanora.fluentai.data.user.dao.TodayPlanDao = database.todayPlanDao()

    @Provides
    fun provideVocabularyFeatureStateDao(database: UserDatabase): VocabularyFeatureStateDao =
        database.vocabularyFeatureStateDao()

    @Provides
    fun provideGrammarFeatureStateDao(database: UserDatabase): GrammarFeatureStateDao =
        database.grammarFeatureStateDao()
}
