package com.seanora.fluentai.ai.analysis.di

import com.seanora.fluentai.BuildConfig
import com.seanora.fluentai.ai.analysis.GeminiSpeakingAnalysisClient
import com.seanora.fluentai.ai.analysis.MockSpeakingAnalysisClient
import com.seanora.fluentai.ai.analysis.SpeakingAnalysisClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalysisModule {

    @Provides
    @Singleton
    fun provideSpeakingAnalysisClient(
        mockClient: MockSpeakingAnalysisClient
    ): SpeakingAnalysisClient {
        return if (BuildConfig.GEMINI_API_KEY.isNotBlank()) {
            GeminiSpeakingAnalysisClient(
                fallbackClient = mockClient,
                apiKeyProvider = { BuildConfig.GEMINI_API_KEY }
            )
        } else {
            mockClient
        }
    }
}
