package com.seanora.fluentai.feature.home.di

import com.seanora.fluentai.feature.home.data.HomeRepository
import com.seanora.fluentai.feature.home.data.MockHomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        impl: com.seanora.fluentai.feature.home.data.RealHomeRepository,
    ): HomeRepository
}
