package com.seanora.fluentai.ai.analysis.di

import com.seanora.fluentai.ai.analysis.GeminiSpeakingAnalysisClient
import com.seanora.fluentai.ai.analysis.MockSpeakingAnalysisClient
import com.seanora.fluentai.ai.analysis.SpeakingAnalysisClient
import com.seanora.fluentai.ai.config.AiRuntimeConfig
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
        config: AiRuntimeConfig,
        okHttpClient: OkHttpClient,
        offlineClient: MockSpeakingAnalysisClient
    ): SpeakingAnalysisClient = if (config.isOnlineAiEnabled) {
        GeminiSpeakingAnalysisClient(
            fallbackClient = offlineClient,
            okHttpClient = okHttpClient,
            apiKeyProvider = { config.geminiApiKey },
            modelName = config.analysisModel
        )
    } else {
        offlineClient
    }
}
