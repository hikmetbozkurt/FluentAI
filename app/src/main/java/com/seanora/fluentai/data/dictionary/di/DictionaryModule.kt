package com.seanora.fluentai.data.dictionary.di

import com.seanora.fluentai.data.dictionary.OfflineDictionaryRepository
import com.seanora.fluentai.data.dictionary.OfflineDictionaryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DictionaryModule {

    @Binds
    @Singleton
    abstract fun bindOfflineDictionaryRepository(
        impl: OfflineDictionaryRepositoryImpl,
    ): OfflineDictionaryRepository
}
