package com.seanora.fluentai.core.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import java.util.UUID

enum class TtsAccent(val label: String, val locale: Locale) {
    US("US", Locale.US),
    UK("UK", Locale.UK),
}

/**
 * Offline-first on-device pronunciation and read-aloud helper backed by Android [TextToSpeech].
 * Falls back silently when the device has no TTS engine installed.
 */
class OnDeviceTtsSpeaker(context: Context) {
    private val appContext = context.applicationContext
    private var tts: TextToSpeech? = null
    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _activeUtteranceTag = MutableStateFlow<String?>(null)
    val activeUtteranceTag: StateFlow<String?> = _activeUtteranceTag.asStateFlow()

    init {
        runCatching {
            tts = TextToSpeech(appContext) { status ->
                val ready = status == TextToSpeech.SUCCESS
                _isReady.value = ready
                if (ready) {
                    tts?.language = Locale.US
                    tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {
                            _isSpeaking.value = true
                        }

                        override fun onDone(utteranceId: String?) {
                            _isSpeaking.value = false
                            _activeUtteranceTag.value = null
                        }

                        @Deprecated("Deprecated in Java")
                        override fun onError(utteranceId: String?) {
                            _isSpeaking.value = false
                            _activeUtteranceTag.value = null
                        }

                        override fun onError(utteranceId: String?, errorCode: Int) {
                            _isSpeaking.value = false
                            _activeUtteranceTag.value = null
                        }
                    })
                }
            }
        }
    }

    fun speak(
        text: String,
        accent: TtsAccent = TtsAccent.US,
        speechRate: Float = 0.96f,
        tag: String? = null,
    ) {
        val engine = tts ?: return
        if (!_isReady.value || text.isBlank()) return
        runCatching {
            val localeResult = engine.setLanguage(accent.locale)
            if (localeResult == TextToSpeech.LANG_MISSING_DATA || localeResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                engine.setLanguage(Locale.ENGLISH)
            }
            engine.setSpeechRate(speechRate.coerceIn(0.5f, 1.5f))
            _activeUtteranceTag.value = tag
            _isSpeaking.value = true
            engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, tag ?: UUID.randomUUID().toString())
        }
    }

    fun stop() {
        runCatching { tts?.stop() }
        _isSpeaking.value = false
        _activeUtteranceTag.value = null
    }

    fun release() {
        stop()
        runCatching { tts?.shutdown() }
        tts = null
        _isReady.value = false
    }
}

@Composable
fun rememberOnDeviceTtsSpeaker(): OnDeviceTtsSpeaker {
    val context = LocalContext.current
    val speaker = remember(context) { OnDeviceTtsSpeaker(context) }
    DisposableEffect(speaker) {
        onDispose { speaker.release() }
    }
    return speaker
}
