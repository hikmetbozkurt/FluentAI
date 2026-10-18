package com.seanora.fluentai.ai.live.auth

object LiveEndpoint {

    private const val HOST = "generativelanguage.googleapis.com"
    private const val SERVICE_PATH = "/ws/google.ai.generativelanguage.v1beta.GenerativeService"
    private const val API_KEY_METHOD = "BidiGenerateContent"
    private const val EPHEMERAL_TOKEN_METHOD = "BidiGenerateContentConstrained"

    fun urlFor(credential: LiveCredential): String = when (credential) {
        is LiveCredential.ApiKey -> "wss://$HOST$SERVICE_PATH.$API_KEY_METHOD?key=${credential.value}"
        is LiveCredential.EphemeralToken -> "wss://$HOST$SERVICE_PATH.$EPHEMERAL_TOKEN_METHOD?access_token=${credential.value}"
    }
}
