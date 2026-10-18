package com.seanora.fluentai.ai.live.protocol

object LiveConnectionErrorMapper {

    const val NORMAL_CLOSURE_CODE = 1000
    private const val POLICY_VIOLATION_CODE = 1008
    private const val INTERNAL_ERROR_CODE = 1011
    private const val HTTP_UNAUTHORIZED = 401
    private const val HTTP_FORBIDDEN = 403

    const val AUTHENTICATION_FAILED = "Gemini authentication failed."
    const val MODEL_UNAVAILABLE = "Gemini Live session setup failed: Model unavailable."
    const val SETUP_FAILED = "Gemini Live session setup failed."
    const val CONNECTION_TIMED_OUT = "Live session connection timed out."
    const val CONNECTION_LOST = "Connection lost."
    const val UNABLE_TO_CONNECT = "Unable to connect to Gemini Live."
    const val QUOTA_OR_POLICY = "Gemini Live closed the session (quota or policy limit)."
    const val SERVER_INTERNAL = "Gemini Live had an internal error. Please try again."
    const val MISSING_CREDENTIAL = "Gemini authentication failed: No API key provided."

    fun fromFailure(throwable: Throwable, httpCode: Int?): String {
        val message = throwable.message?.lowercase().orEmpty()
        return when {
            httpCode == HTTP_UNAUTHORIZED || httpCode == HTTP_FORBIDDEN ||
                message.contains("401") || message.contains("403") ||
                message.contains("api key") || message.contains("unauthorized") -> AUTHENTICATION_FAILED
            message.contains("timeout") || message.contains("timed out") -> CONNECTION_TIMED_OUT
            message.contains("closed") || message.contains("reset") ||
                message.contains("broken pipe") || message.contains("eof") -> CONNECTION_LOST
            else -> UNABLE_TO_CONNECT
        }
    }

    fun fromServerError(signal: LiveServerSignal.ServerError): String {
        val text = signal.message
        return when {
            signal.code == "401" || signal.code == "403" || signal.status == "PERMISSION_DENIED" ||
                text.contains("API key", ignoreCase = true) -> AUTHENTICATION_FAILED
            signal.code == "404" || signal.status == "NOT_FOUND" ||
                text.contains("model", ignoreCase = true) -> MODEL_UNAVAILABLE
            else -> SETUP_FAILED
        }
    }

    fun fromClose(code: Int, reason: String): String = when {
        reason.contains("API key", ignoreCase = true) -> AUTHENTICATION_FAILED
        code == POLICY_VIOLATION_CODE -> QUOTA_OR_POLICY
        code == INTERNAL_ERROR_CODE -> SERVER_INTERNAL
        else -> CONNECTION_LOST
    }
}
