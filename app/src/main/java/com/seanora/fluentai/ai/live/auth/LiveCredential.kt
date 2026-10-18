package com.seanora.fluentai.ai.live.auth

sealed interface LiveCredential {
    val value: String

    data class ApiKey(override val value: String) : LiveCredential

    data class EphemeralToken(override val value: String) : LiveCredential
}

interface LiveCredentialProvider {
    suspend fun obtainCredential(): LiveCredential?
}

class StaticLiveCredentialProvider(private val credential: LiveCredential?) : LiveCredentialProvider {
    override suspend fun obtainCredential(): LiveCredential? = credential?.takeIf { it.value.isNotBlank() }
}
