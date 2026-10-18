package com.seanora.fluentai.ai.live.di

import com.seanora.fluentai.ai.config.AiRuntimeConfig
import com.seanora.fluentai.ai.live.GeminiLiveWebSocketClient
import com.seanora.fluentai.ai.live.LiveAiClient
import com.seanora.fluentai.ai.live.MockLiveAiClient
import com.seanora.fluentai.ai.live.auth.LiveCredential
import com.seanora.fluentai.ai.live.auth.LiveCredentialProvider
import com.seanora.fluentai.ai.live.auth.StaticLiveCredentialProvider
import com.seanora.fluentai.core.common.di.IoDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.WebSocket
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AiModule {

    @Provides
    @Singleton
    fun provideLiveCredentialProvider(config: AiRuntimeConfig): LiveCredentialProvider =
        StaticLiveCredentialProvider(LiveCredential.ApiKey(config.geminiApiKey))

    @Provides
    @Singleton
    fun provideLiveAiClient(
        config: AiRuntimeConfig,
        credentialProvider: LiveCredentialProvider,
        webSocketFactory: WebSocket.Factory,
        @IoDispatcher dispatcher: CoroutineDispatcher,
        mockClient: Provider<MockLiveAiClient>
    ): LiveAiClient = if (config.isOnlineAiEnabled) {
        GeminiLiveWebSocketClient(
            dispatcher = dispatcher,
            credentialProvider = credentialProvider,
            webSocketFactory = webSocketFactory,
            modelName = config.liveModel
        )
    } else {
        mockClient.get()
    }
}
