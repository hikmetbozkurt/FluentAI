package com.seanora.fluentai.ai.live.di

import com.seanora.fluentai.BuildConfig
import com.seanora.fluentai.ai.live.GeminiLiveWebSocketClient
import com.seanora.fluentai.ai.live.LiveAiClient
import com.seanora.fluentai.ai.live.MockLiveAiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AiModule {

    @Provides
    @Singleton
    fun provideLiveAiClient(
        mockClient: MockLiveAiClient
    ): LiveAiClient {
        return if (BuildConfig.GEMINI_API_KEY.isNotBlank()) {
            GeminiLiveWebSocketClient(
                apiKeyProvider = { BuildConfig.GEMINI_API_KEY }
            )
        } else {
            mockClient
        }
    }
}
