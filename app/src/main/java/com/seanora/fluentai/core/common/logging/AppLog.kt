package com.seanora.fluentai.core.common.logging

import android.util.Log
import com.seanora.fluentai.BuildConfig

object AppLog {

    private val enabled: Boolean = BuildConfig.DEBUG

    fun d(tag: String, message: () -> String) {
        if (enabled) Log.d(tag, SecretRedactor.redact(message()))
    }

    fun i(tag: String, message: () -> String) {
        if (enabled) Log.i(tag, SecretRedactor.redact(message()))
    }

    fun w(tag: String, throwable: Throwable? = null, message: () -> String) {
        if (enabled) Log.w(tag, SecretRedactor.redact(message()), throwable)
    }

    fun e(tag: String, throwable: Throwable? = null, message: () -> String) {
        Log.e(tag, SecretRedactor.redact(message()), throwable)
    }
}

object SecretRedactor {

    private val queryKeyPattern = Regex("(?i)((?:key|access_token)=)[^&\\s\"']+")
    private val jsonKeyPattern = Regex("(?i)(\"(?:apiKey|accessToken)\"\\s*:\\s*\")[^\"]+(\")")
    private val googleKeyPattern = Regex("AIza[0-9A-Za-z_-]{35}")

    fun redact(text: String): String = text
        .replace(queryKeyPattern, "$1[REDACTED]")
        .replace(jsonKeyPattern, "$1[REDACTED]$2")
        .replace(googleKeyPattern, "[REDACTED]")
}
