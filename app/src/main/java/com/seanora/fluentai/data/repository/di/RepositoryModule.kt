package com.seanora.fluentai.data.repository.di

import com.seanora.fluentai.data.repository.GrammarRepository
import com.seanora.fluentai.data.repository.GrammarRepositoryImpl
import com.seanora.fluentai.data.repository.GrammarFeatureStateRepository
import com.seanora.fluentai.data.repository.GrammarFeatureStateRepositoryImpl
import com.seanora.fluentai.data.repository.ListeningRepository
import com.seanora.fluentai.data.repository.ListeningRepositoryImpl
import com.seanora.fluentai.data.repository.ReadingRepository
import com.seanora.fluentai.data.repository.ReadingRepositoryImpl
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.data.repository.UserLearningRepositoryImpl
import com.seanora.fluentai.data.repository.VocabularyRepository
import com.seanora.fluentai.data.repository.VocabularyRepositoryImpl
import com.seanora.fluentai.data.repository.VocabularyFeatureStateRepository
import com.seanora.fluentai.data.repository.VocabularyFeatureStateRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindVocabularyRepository(
        impl: VocabularyRepositoryImpl
    ): VocabularyRepository

    @Binds
    @Singleton
    abstract fun bindVocabularyFeatureStateRepository(
        impl: VocabularyFeatureStateRepositoryImpl
    ): VocabularyFeatureStateRepository

    @Binds
    @Singleton
    abstract fun bindGrammarRepository(
        impl: GrammarRepositoryImpl
    ): GrammarRepository

    @Binds
    @Singleton
    abstract fun bindGrammarFeatureStateRepository(
        impl: GrammarFeatureStateRepositoryImpl
    ): GrammarFeatureStateRepository

    @Binds
    @Singleton
    abstract fun bindReadingRepository(
        impl: ReadingRepositoryImpl
    ): ReadingRepository

    @Binds
    @Singleton
    abstract fun bindListeningRepository(
        impl: ListeningRepositoryImpl
    ): ListeningRepository

    @Binds
    @Singleton
    abstract fun bindUserLearningRepository(
        impl: UserLearningRepositoryImpl
    ): UserLearningRepository

    @Binds
    @Singleton
    abstract fun bindAssessmentRepository(
        impl: com.seanora.fluentai.data.repository.AssessmentRepositoryImpl
    ): com.seanora.fluentai.data.repository.AssessmentRepository

    @Binds
    @Singleton
    abstract fun bindSpeakingRepository(
        impl: com.seanora.fluentai.data.repository.SpeakingRepositoryImpl
    ): com.seanora.fluentai.data.repository.SpeakingRepository

    @Binds
    @Singleton
    abstract fun bindTodayPracticeRepository(
        impl: com.seanora.fluentai.data.repository.TodayPracticeRepositoryImpl
    ): com.seanora.fluentai.data.repository.TodayPracticeRepository

    @Binds
    @Singleton
    abstract fun bindSearchRepository(
        impl: com.seanora.fluentai.data.repository.SearchRepositoryImpl
    ): com.seanora.fluentai.data.repository.SearchRepository
}
