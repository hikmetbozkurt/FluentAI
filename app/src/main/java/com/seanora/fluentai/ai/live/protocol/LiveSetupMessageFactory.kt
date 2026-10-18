package com.seanora.fluentai.ai.live.protocol

import com.seanora.fluentai.ai.live.LiveScenarioConfig
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject

class LiveSetupMessageFactory(private val modelName: String) {

    fun create(config: LiveScenarioConfig, resumptionHandle: String?): JsonObject = buildJsonObject {
        putJsonObject("setup") {
            put("model", modelName)
            putJsonObject("generationConfig") {
                put("responseModalities", JsonArray(listOf(JsonPrimitive(RESPONSE_MODALITY_AUDIO))))
                if (config.voiceName.isNotBlank()) {
                    putJsonObject("speechConfig") {
                        putJsonObject("voiceConfig") {
                            putJsonObject("prebuiltVoiceConfig") {
                                put("voiceName", config.voiceName)
                            }
                        }
                    }
                }
            }
            if (config.systemPrompt.isNotBlank()) {
                putJsonObject("systemInstruction") {
                    put("parts", JsonArray(listOf(buildJsonObject { put("text", config.systemPrompt) })))
                }
            }
            putJsonObject("inputAudioTranscription") {}
            putJsonObject("outputAudioTranscription") {}
            putJsonObject("sessionResumption") {
                if (!resumptionHandle.isNullOrBlank()) put("handle", resumptionHandle)
            }
            putJsonObject("contextWindowCompression") {
                putJsonObject("slidingWindow") {}
            }
        }
    }

    private companion object {
        const val RESPONSE_MODALITY_AUDIO = "AUDIO"
    }
}

object LiveClientMessageFactory {

    private const val INPUT_AUDIO_MIME_TYPE = "audio/pcm;rate=16000"

    fun audioChunk(base64Pcm: String): JsonObject = buildJsonObject {
        putJsonObject("realtimeInput") {
            putJsonObject("audio") {
                put("mimeType", INPUT_AUDIO_MIME_TYPE)
                put("data", base64Pcm)
            }
        }
    }

    fun audioStreamEnd(): JsonObject = buildJsonObject {
        putJsonObject("realtimeInput") {
            put("audioStreamEnd", true)
        }
    }

    fun userText(text: String): JsonObject = buildJsonObject {
        putJsonObject("clientContent") {
            put(
                "turns",
                JsonArray(
                    listOf(
                        buildJsonObject {
                            put("role", "user")
                            put("parts", JsonArray(listOf(buildJsonObject { put("text", text) })))
                        }
                    )
                )
            )
            put("turnComplete", true)
        }
    }
}
