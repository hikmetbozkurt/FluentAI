package com.seanora.fluentai.ai.live.protocol

import com.seanora.fluentai.ai.live.LiveSessionEvent
import com.seanora.fluentai.ai.live.LiveTokenUsage
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import java.util.Base64

class LiveServerMessageParser {

    private val json = Json { ignoreUnknownKeys = true }

    fun parse(messageText: String): List<LiveServerSignal> {
        val root = json.parseToJsonElement(messageText) as? JsonObject ?: return emptyList()
        if (root.isEmpty()) return emptyList()

        val signals = mutableListOf<LiveServerSignal>()
        root.objectField("usageMetadata")?.let { signals += LiveServerSignal.Usage(parseUsage(it)) }

        root.objectField("sessionResumptionUpdate")?.let { update ->
            val handle = update.stringField("newHandle")?.takeIf { it.isNotBlank() }
            val resumable = update.booleanField("resumable") ?: (handle != null)
            signals += LiveServerSignal.ResumptionUpdate(handle, resumable)
            signals += LiveServerSignal.Event(LiveSessionEvent.SessionResumptionUpdate)
            return signals
        }

        if (root.containsKey("setupComplete")) {
            signals += LiveServerSignal.SetupComplete
            return signals
        }

        root.objectField("goAway")?.let { goAway ->
            signals += LiveServerSignal.GoAway(parseDurationMs(goAway.stringField("timeLeft")))
            return signals
        }

        root["error"]?.let { error ->
            signals += parseError(error)
            return signals
        }

        val serverContent = root.objectField("serverContent")

        extractText(root, serverContent, INTERIM_INPUT_KEYS)?.takeIf { it.isNotBlank() }?.let {
            signals += LiveServerSignal.Event(LiveSessionEvent.InterimInputTranscription(it))
        }
        extractText(root, serverContent, INPUT_KEYS)?.takeIf { it.isNotBlank() }?.let {
            signals += LiveServerSignal.Event(LiveSessionEvent.InputTranscription(it))
        }
        val outputText = extractText(root, serverContent, OUTPUT_KEYS)
        outputText?.takeIf { it.isNotBlank() }?.let {
            signals += LiveServerSignal.Event(LiveSessionEvent.OutputTranscription(it))
        }

        if (serverContent != null) {
            if (serverContent.booleanField("interrupted") == true) {
                signals += LiveServerSignal.Event(LiveSessionEvent.Interrupted)
            }
            serverContent.objectField("modelTurn")?.get("parts")?.jsonArray?.forEach { element ->
                val part = element as? JsonObject ?: return@forEach
                part.stringField("text")?.takeIf { it.isNotBlank() && outputText == null }?.let {
                    signals += LiveServerSignal.Event(LiveSessionEvent.OutputTranscription(it))
                }
                decodeInlineAudio(part)?.let {
                    signals += LiveServerSignal.Event(LiveSessionEvent.AudioOutputChunk(it))
                }
            }
            if (serverContent.booleanField("generationComplete") == true) {
                signals += LiveServerSignal.Event(LiveSessionEvent.GenerationComplete)
            }
            if (serverContent.booleanField("turnComplete") == true) {
                signals += LiveServerSignal.Event(LiveSessionEvent.TurnComplete)
            }
        }

        val waitingForInput = root.booleanField("waitingForInput") ?: serverContent?.booleanField("waitingForInput")
        if (waitingForInput == true) {
            signals += LiveServerSignal.Event(LiveSessionEvent.WaitingForInput)
        }

        val interactionStatus = root.stringField("interactionStatus") ?: serverContent?.stringField("interactionStatus")
        interactionStatus?.let { signals += LiveServerSignal.Event(LiveSessionEvent.InteractionStatus(it)) }

        return signals
    }

    private fun parseError(error: JsonElement): LiveServerSignal.ServerError = when (error) {
        is JsonObject -> LiveServerSignal.ServerError(
            code = error.stringField("code"),
            status = error.stringField("status"),
            message = error.stringField("message") ?: error.toString()
        )
        is JsonPrimitive -> LiveServerSignal.ServerError(code = null, status = null, message = error.content)
        else -> LiveServerSignal.ServerError(code = null, status = null, message = error.toString())
    }

    private fun parseUsage(usage: JsonObject) = LiveTokenUsage(
        promptTokens = usage.intField("promptTokenCount") ?: 0,
        responseTokens = usage.intField("responseTokenCount") ?: 0,
        totalTokens = usage.intField("totalTokenCount") ?: 0
    )

    private fun decodeInlineAudio(part: JsonObject): ByteArray? {
        val encoded = part.objectField("inlineData")?.stringField("data")?.takeIf { it.isNotEmpty() } ?: return null
        return runCatching { Base64.getMimeDecoder().decode(encoded.trim()) }.getOrNull()
    }

    private fun extractText(root: JsonObject, serverContent: JsonObject?, keys: List<String>): String? {
        keys.forEach { key -> textOf(root[key])?.let { return it } }
        keys.forEach { key -> textOf(serverContent?.get(key))?.let { return it } }
        return null
    }

    private fun textOf(element: JsonElement?): String? = when (element) {
        null -> null
        is JsonPrimitive -> element.content.takeIf { element.isString }
        is JsonObject -> element.stringField("text")
            ?: element["parts"]?.jsonArray
                ?.mapNotNull { (it as? JsonObject)?.stringField("text") }
                ?.joinToString(separator = "")
                ?.takeIf { it.isNotEmpty() }
        else -> null
    }

    private fun parseDurationMs(value: String?): Long? {
        val seconds = value?.removeSuffix("s")?.toDoubleOrNull() ?: return null
        return (seconds * MILLIS_PER_SECOND).toLong()
    }

    private fun JsonObject.objectField(key: String): JsonObject? = this[key] as? JsonObject

    private fun JsonObject.stringField(key: String): String? =
        (this[key] as? JsonPrimitive)?.takeIf { it !is JsonNull }?.content

    private fun JsonObject.booleanField(key: String): Boolean? =
        (this[key] as? JsonPrimitive)?.takeIf { it !is JsonNull }?.let { it.booleanOrNull ?: it.content.toBooleanStrictOrNull() }

    private fun JsonObject.intField(key: String): Int? = (this[key] as? JsonPrimitive)?.intOrNull

    private companion object {
        const val MILLIS_PER_SECOND = 1000
        val INTERIM_INPUT_KEYS = listOf("interimInputTranscription", "interimInputAudioTranscription")
        val INPUT_KEYS = listOf("inputTranscription", "inputAudioTranscription")
        val OUTPUT_KEYS = listOf("outputTranscription", "outputAudioTranscription")
    }
}
