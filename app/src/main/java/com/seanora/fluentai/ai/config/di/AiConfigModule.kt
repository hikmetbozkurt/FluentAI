package com.seanora.fluentai.ai.config.di

import com.seanora.fluentai.BuildConfig
import com.seanora.fluentai.ai.analysis.GeminiSpeakingAnalysisClient
import com.seanora.fluentai.ai.config.AiRuntimeConfig
import com.seanora.fluentai.ai.live.GeminiLiveWebSocketClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AiConfigModule {

    @Provides
    @Singleton
    fun provideAiRuntimeConfig(): AiRuntimeConfig = AiRuntimeConfig(
        geminiApiKey = BuildConfig.GEMINI_API_KEY,
        liveModel = BuildConfig.GEMINI_LIVE_MODEL.ifBlank { GeminiLiveWebSocketClient.DEFAULT_MODEL },
        analysisModel = BuildConfig.GEMINI_ANALYSIS_MODEL.ifBlank { GeminiSpeakingAnalysisClient.DEFAULT_MODEL }
    )
}
