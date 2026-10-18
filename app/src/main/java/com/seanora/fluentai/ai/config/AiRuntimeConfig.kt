package com.seanora.fluentai.ai.config

data class AiRuntimeConfig(
    val geminiApiKey: String,
    val liveModel: String,
    val analysisModel: String
) {
    val isOnlineAiEnabled: Boolean get() = geminiApiKey.isNotBlank()
}
