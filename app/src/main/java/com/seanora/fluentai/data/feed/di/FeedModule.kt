package com.seanora.fluentai.data.feed.di

import com.seanora.fluentai.data.feed.LiveReadingFeedRepository
import com.seanora.fluentai.data.feed.LiveReadingFeedRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FeedModule {

    @Binds
    @Singleton
    abstract fun bindLiveReadingFeedRepository(
        impl: LiveReadingFeedRepositoryImpl,
    ): LiveReadingFeedRepository
}
