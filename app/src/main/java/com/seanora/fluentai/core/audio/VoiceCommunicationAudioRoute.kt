package com.seanora.fluentai.core.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioDeviceInfo
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import com.seanora.fluentai.core.common.logging.AppLog
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoiceCommunicationAudioRoute @Inject constructor(
    @ApplicationContext context: Context
) {
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
    private var previousMode: Int? = null
    private var previousSpeakerphoneOn: Boolean? = null
    private var focusRequest: AudioFocusRequest? = null

    fun acquire() {
        val manager = audioManager ?: return
        runCatching {
            previousMode = manager.mode
            manager.mode = AudioManager.MODE_IN_COMMUNICATION
            requestFocus(manager)
            routeToPreferredDevice(manager)
        }.onFailure { AppLog.w(TAG, it) { "Could not configure voice communication route" } }
    }

    fun release() {
        val manager = audioManager ?: return
        runCatching {
            restoreDevice(manager)
            focusRequest?.let(manager::abandonAudioFocusRequest)
            focusRequest = null
            previousMode?.let { manager.mode = it }
            previousMode = null
        }.onFailure { AppLog.w(TAG, it) { "Could not restore audio route" } }
    }

    private fun requestFocus(manager: AudioManager) {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()
        val request = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
            .setAudioAttributes(attributes)
            .build()
        manager.requestAudioFocus(request)
        focusRequest = request
    }

    @Suppress("DEPRECATION")
    private fun routeToPreferredDevice(manager: AudioManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val devices = manager.availableCommunicationDevices
            val preferred = devices.firstOrNull { it.type in HEADSET_TYPES }
                ?: devices.firstOrNull { it.type == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER }
            preferred?.let(manager::setCommunicationDevice)
        } else {
            previousSpeakerphoneOn = manager.isSpeakerphoneOn
            manager.isSpeakerphoneOn = !manager.isWiredHeadsetOn && !manager.isBluetoothScoOn
        }
    }

    @Suppress("DEPRECATION")
    private fun restoreDevice(manager: AudioManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            manager.clearCommunicationDevice()
        } else {
            previousSpeakerphoneOn?.let { manager.isSpeakerphoneOn = it }
            previousSpeakerphoneOn = null
        }
    }

    private companion object {
        const val TAG = "VoiceAudioRoute"
        val HEADSET_TYPES = setOf(
            AudioDeviceInfo.TYPE_WIRED_HEADSET,
            AudioDeviceInfo.TYPE_WIRED_HEADPHONES,
            AudioDeviceInfo.TYPE_USB_HEADSET,
            AudioDeviceInfo.TYPE_BLUETOOTH_SCO,
            AudioDeviceInfo.TYPE_BLE_HEADSET
        )
    }
}
