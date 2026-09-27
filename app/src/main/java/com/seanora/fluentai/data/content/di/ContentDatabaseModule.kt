package com.seanora.fluentai.data.content.di

import android.content.Context
import androidx.room.Room
import com.seanora.fluentai.data.content.ContentDatabase
import com.seanora.fluentai.data.content.ContentDatabaseMigrations
import com.seanora.fluentai.data.content.dao.ContentMetadataDao
import com.seanora.fluentai.data.content.dao.ContentRelationDao
import com.seanora.fluentai.data.content.dao.ExerciseDao
import com.seanora.fluentai.data.content.dao.GrammarDao
import com.seanora.fluentai.data.content.dao.ListeningDao
import com.seanora.fluentai.data.content.dao.ReadingDao
import com.seanora.fluentai.data.content.dao.TopicDao
import com.seanora.fluentai.data.content.dao.VocabDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ContentDatabaseModule {

    @Provides
    @Singleton
    fun provideContentDatabase(
        @ApplicationContext context: Context
    ): ContentDatabase {
        return Room.databaseBuilder(
            context,
            ContentDatabase::class.java,
            ContentDatabase.DATABASE_NAME
        )
            .createFromAsset("content.db")
            .addMigrations(*ContentDatabaseMigrations.ALL)
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    @Provides
    fun provideVocabDao(database: ContentDatabase): VocabDao = database.vocabDao()

    @Provides
    fun provideGrammarDao(database: ContentDatabase): GrammarDao = database.grammarDao()

    @Provides
    fun provideExerciseDao(database: ContentDatabase): ExerciseDao = database.exerciseDao()

    @Provides
    fun provideReadingDao(database: ContentDatabase): ReadingDao = database.readingDao()

    @Provides
    fun provideListeningDao(database: ContentDatabase): ListeningDao = database.listeningDao()

    @Provides
    fun provideSpeakingDao(database: ContentDatabase): com.seanora.fluentai.data.content.dao.SpeakingDao = database.speakingDao()

    @Provides
    fun provideTopicDao(database: ContentDatabase): TopicDao = database.topicDao()

    @Provides
    fun provideContentRelationDao(database: ContentDatabase): ContentRelationDao = database.contentRelationDao()

    @Provides
    fun provideContentMetadataDao(database: ContentDatabase): ContentMetadataDao = database.contentMetadataDao()
}

