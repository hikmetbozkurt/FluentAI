package com.seanora.fluentai.ai.live

import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.io.IOException
import java.util.Base64
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * WebSocket client communicating with the live bidirectional AI endpoint.
 * Encapsulates audio PCM serialization, streaming response parsing, and barge-in signal handling
 * per AGENTS.md Section 4 & Section 11 and current Gemini Live API specifications.
 */
@Singleton
class GeminiLiveWebSocketClient @Inject constructor(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val customBaseUrl: String? = null,
    private val apiKeyProvider: () -> String? = { com.seanora.fluentai.BuildConfig.GEMINI_API_KEY.ifBlank { null } },
    private val webSocketFactory: WebSocket.Factory = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .pingInterval(20, TimeUnit.SECONDS)
        .build(),
    private val setupTimeoutMs: Long = DEFAULT_SETUP_TIMEOUT_MS
) : LiveAiClient {

    companion object {
        private const val TAG = "GeminiLiveClient"
        const val DEFAULT_HOST = "generativelanguage.googleapis.com"
        const val DEFAULT_PATH = "/ws/google.ai.generativelanguage.v1beta.GenerativeService.BidiGenerateContent"
        const val LIVE_MODEL = "models/gemini-3.8-live"
        const val DEFAULT_SETUP_TIMEOUT_MS = 15000L
    }

    private val json = Json { ignoreUnknownKeys = true }
    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    private var webSocket: WebSocket? = null
    private var currentConfig: LiveScenarioConfig? = null
    private var sessionGeneration: Long = 0L
    private var setupDeferred: CompletableDeferred<Result<Unit>>? = null
    private val connectionMutex = Mutex()

    private val _connectionState = MutableStateFlow(LiveConnectionState.DISCONNECTED)
    override val connectionState: StateFlow<LiveConnectionState> = _connectionState.asStateFlow()

    private val _sessionEvents = MutableSharedFlow<LiveSessionEvent>(extraBufferCapacity = 128)
    override val sessionEvents: SharedFlow<LiveSessionEvent> = _sessionEvents.asSharedFlow()

    override suspend fun connect(config: LiveScenarioConfig): Result<Unit> = connectionMutex.withLock {
        // FIX D1: Guard against duplicate concurrent start calls
        if (_connectionState.value == LiveConnectionState.SESSION_READY ||
            _connectionState.value == LiveConnectionState.CONNECTED ||
            _connectionState.value == LiveConnectionState.SOCKET_OPEN ||
            _connectionState.value == LiveConnectionState.CONNECTING
        ) {
            Log.w(TAG, "Connection already active or connecting (${_connectionState.value}); ignoring duplicate connect")
            return Result.success(Unit)
        }

        val apiKey = apiKeyProvider()
        val url = if (customBaseUrl != null) {
            customBaseUrl
        } else if (!apiKey.isNullOrBlank()) {
            "wss://$DEFAULT_HOST$DEFAULT_PATH?key=$apiKey"
        } else {
            _connectionState.value = LiveConnectionState.FAILED
            _sessionEvents.tryEmit(
                LiveSessionEvent.Error(
                    message = "Gemini authentication failed: No API key provided.",
                    isFatal = true
                )
            )
            return Result.failure(IllegalStateException("No Gemini API key available"))
        }

        // Clean up any stale socket resources before creating a new one
        cleanUpSocket(cancelSocket = true, reason = "Fresh connection starting")

        val currentGen = ++sessionGeneration
        val deferred = CompletableDeferred<Result<Unit>>()
        setupDeferred = deferred

        currentConfig = config
        _connectionState.value = LiveConnectionState.CONNECTING
        Log.i(TAG, "connecting to ${sanitize(url)} [gen: $currentGen]")

        val request = Request.Builder().url(url).build()

        val socketListener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                if (currentGen != sessionGeneration) {
                    Log.d(TAG, "Ignoring onOpen from stale generation $currentGen")
                    webSocket.cancel()
                    return
                }
                Log.i(TAG, "WebSocket opened [gen: $currentGen]")
                // FIX C3: Socket open is NOT session ready yet
                _connectionState.value = LiveConnectionState.SOCKET_OPEN
                sendInitialSetupMessage(webSocket, config, currentGen)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                if (currentGen != sessionGeneration) {
                    return
                }
                if (text.contains("\"data\"") || text.length > 500) {
                    Log.d(TAG, "inbound server frame [gen: $currentGen, len=${text.length}]")
                } else {
                    Log.d(TAG, "inbound server message [gen: $currentGen]: ${sanitize(text)}")
                }
                parseServerMessage(text, currentGen)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                if (currentGen != sessionGeneration) {
                    return
                }
                val text = try { bytes.utf8() } catch (e: Exception) { null }
                Log.d(TAG, "inbound binary frame [gen: $currentGen, len=${bytes.size}]")
                if (text != null && text.isNotBlank()) {
                    parseServerMessage(text, currentGen)
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                if (currentGen != sessionGeneration) return
                Log.i(TAG, "websocket closing: code=$code, reason=$reason [gen: $currentGen]")
                _connectionState.value = LiveConnectionState.CLOSED
                _sessionEvents.tryEmit(LiveSessionEvent.Disconnected(reason))
                if (deferred.isActive) {
                    deferred.complete(Result.failure(IOException("WebSocket closing before setup completed: code=$code, reason=$reason")))
                }
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                if (currentGen != sessionGeneration) return
                Log.i(TAG, "websocket closed: code=$code, reason=$reason [gen: $currentGen]")
                _connectionState.value = LiveConnectionState.CLOSED
                if (deferred.isActive) {
                    deferred.complete(Result.failure(IOException("WebSocket closed before setup completed: code=$code, reason=$reason")))
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                if (currentGen != sessionGeneration) {
                    Log.d(TAG, "Ignoring onFailure from stale generation $currentGen: ${sanitize(t.message ?: "")}")
                    return
                }
                val code = response?.code
                val responseMsg = response?.message
                val responseBody = try { response?.body?.string() } catch (e: Exception) { null }
                val sanitizedBody = responseBody?.let { sanitize(it) }
                val sanitizedErr = sanitize(t.message ?: "")
                Log.e(
                    TAG,
                    "websocket failure: code=$code, message=$responseMsg, body=$sanitizedBody, error=${t::class.java.simpleName}: $sanitizedErr [gen: $currentGen]",
                    t
                )
                _connectionState.value = LiveConnectionState.FAILED
                val errorMsg = mapConnectionError(t, response)
                _sessionEvents.tryEmit(LiveSessionEvent.Error(errorMsg, isFatal = true))
                if (deferred.isActive) {
                    deferred.complete(Result.failure(IOException(errorMsg, t)))
                }
            }
        }

        webSocket = webSocketFactory.newWebSocket(request, socketListener)

        // FIX C6: connect() suspends until server confirms setup or failure
        return try {
            withTimeout(setupTimeoutMs) {
                deferred.await()
            }
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Live session setup timed out waiting for server setupComplete [gen: $currentGen]")
            _connectionState.value = LiveConnectionState.FAILED
            _sessionEvents.tryEmit(LiveSessionEvent.Error("Live session connection timed out.", isFatal = true))
            cleanUpSocket(cancelSocket = true, reason = "Setup timeout")
            Result.failure(TimeoutException("Live session setup timed out waiting for server setupComplete"))
        } catch (e: Exception) {
            _connectionState.value = LiveConnectionState.FAILED
            cleanUpSocket(cancelSocket = true, reason = "Setup failed")
            Result.failure(e)
        }
    }

    private fun sendInitialSetupMessage(webSocket: WebSocket, config: LiveScenarioConfig, gen: Long) {
        val setupPayload = buildJsonObject {
            put("setup", buildJsonObject {
                put("model", JsonPrimitive(LIVE_MODEL))
                put("generationConfig", buildJsonObject {
                    put("responseModalities", JsonArray(listOf(JsonPrimitive("AUDIO"))))
                    if (config.voiceName.isNotBlank()) {
                        put("speechConfig", buildJsonObject {
                            put("voiceConfig", buildJsonObject {
                                put("prebuiltVoiceConfig", buildJsonObject {
                                    put("voiceName", JsonPrimitive(config.voiceName))
                                })
                            })
                        })
                    }
                })
                if (config.systemPrompt.isNotBlank()) {
                    put("systemInstruction", buildJsonObject {
                        put("parts", JsonArray(listOf(buildJsonObject {
                            put("text", JsonPrimitive(config.systemPrompt))
                        })))
                    })
                }
                put("inputAudioTranscription", buildJsonObject {})
                put("outputAudioTranscription", buildJsonObject {})
            })
        }

        val jsonString = setupPayload.toString()
        Log.i(TAG, "outbound setup [gen: $gen]: $jsonString")
        val sent = webSocket.send(jsonString)
        Log.i(TAG, "setup sent=$sent [gen: $gen]")
        if (!sent) {
            _connectionState.value = LiveConnectionState.FAILED
            _sessionEvents.tryEmit(LiveSessionEvent.Error("Failed to send setup message over WebSocket", isFatal = true))
            setupDeferred?.complete(Result.failure(IOException("Failed to send setup message over WebSocket")))
        }
    }

    override suspend fun sendAudioChunk(pcmData: ByteArray) {
        if (pcmData.isEmpty()) return
        if ((_connectionState.value != LiveConnectionState.SESSION_READY &&
             _connectionState.value != LiveConnectionState.CONNECTED) || webSocket == null
        ) {
            return
        }

        val base64Data = Base64.getEncoder().encodeToString(pcmData)
        val payload = buildJsonObject {
            put("realtimeInput", buildJsonObject {
                put("audio", buildJsonObject {
                    put("mimeType", JsonPrimitive("audio/pcm;rate=16000"))
                    put("data", JsonPrimitive(base64Data))
                })
            })
        }

        val sent = webSocket?.send(payload.toString()) ?: false
        Log.d(TAG, "PCM chunk sent=$sent bytes=${pcmData.size} [gen: $sessionGeneration]")
        if (!sent) {
            Log.w(TAG, "Failed to send PCM chunk over WebSocket [gen: $sessionGeneration]")
        }
    }

    override suspend fun sendTextMessage(text: String) {
        if ((_connectionState.value != LiveConnectionState.SESSION_READY &&
             _connectionState.value != LiveConnectionState.CONNECTED) || webSocket == null
        ) {
            Log.w(TAG, "sendTextMessage ignored: connectionState=${_connectionState.value}, webSocket=$webSocket")
            return
        }

        val payload = buildJsonObject {
            put("clientContent", buildJsonObject {
                put("turns", JsonArray(listOf(buildJsonObject {
                    put("role", JsonPrimitive("user"))
                    put("parts", JsonArray(listOf(buildJsonObject {
                        put("text", JsonPrimitive(text))
                    })))
                })))
                put("turnComplete", JsonPrimitive(true))
            })
        }

        val jsonString = payload.toString()
        val sent = webSocket?.send(jsonString) ?: false
        Log.i(TAG, "clientContent text message sent=$sent [gen: $sessionGeneration]")
        if (!sent) {
            Log.w(TAG, "Failed to send clientContent text message over WebSocket [gen: $sessionGeneration]")
        }
    }

    override suspend fun sendBargeInInterrupt() {
        _sessionEvents.tryEmit(LiveSessionEvent.InterruptedByBargeIn)
    }

    override suspend fun disconnect(): Unit = connectionMutex.withLock {
        Log.i(TAG, "websocket closing: clean termination requested")
        cleanUpSocket(cancelSocket = true, reason = "Clean session termination")
        _connectionState.value = LiveConnectionState.CLOSED
        _sessionEvents.tryEmit(LiveSessionEvent.Disconnected("Session ended"))
    }

    private fun cleanUpSocket(cancelSocket: Boolean, reason: String) {
        sessionGeneration++ // Invalidate any subsequent callbacks from the current socket
        setupDeferred?.let {
            if (it.isActive) {
                it.complete(Result.failure(CancellationException(reason)))
            }
        }
        setupDeferred = null

        try {
            if (cancelSocket) {
                webSocket?.cancel()
            } else {
                webSocket?.close(1000, reason)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error closing/cancelling WebSocket: ${e.message}")
        } finally {
            webSocket = null
        }
    }

    private fun parseServerMessage(messageText: String, gen: Long) {
        if (_connectionState.value != LiveConnectionState.SESSION_READY &&
            _connectionState.value != LiveConnectionState.CONNECTED
        ) {
            Log.i(TAG, "inbound during setup [gen: $gen]: $messageText")
        }

        try {
            val root = json.parseToJsonElement(messageText).jsonObject
            if (root.isEmpty()) {
                Log.d(TAG, "Ignoring empty server message [gen: $gen]")
                return
            }
            var handledMessage = false

            // Check for sessionResumptionUpdate
            if (root.containsKey("sessionResumptionUpdate")) {
                handledMessage = true
                Log.i(TAG, "sessionResumptionUpdate received [gen: $gen]")
                _sessionEvents.tryEmit(LiveSessionEvent.SessionResumptionUpdate)
                return
            }

            // 1. Check for setupComplete confirmation
            if (root.containsKey("setupComplete")) {
                Log.i(TAG, "setupComplete received [gen: $gen]")
                Log.i(TAG, "session ready [gen: $gen]")
                _connectionState.value = LiveConnectionState.SESSION_READY
                _sessionEvents.tryEmit(LiveSessionEvent.Connected("gemini-live-$gen"))
                setupDeferred?.complete(Result.success(Unit))
                return
            }

            // 2. Check for server errors
            val errorElement = root["error"]
            if (errorElement != null) {
                var errorCode: String? = null
                var errorStatus: String? = null
                val errorMessage = when (errorElement) {
                    is JsonObject -> {
                        errorCode = errorElement["code"]?.jsonPrimitive?.content
                        errorStatus = errorElement["status"]?.jsonPrimitive?.content
                        errorElement["message"]?.jsonPrimitive?.content ?: errorElement.toString()
                    }
                    is JsonPrimitive -> errorElement.content
                    else -> errorElement.toString()
                }
                val sanitizedMsg = sanitize(errorMessage)
                Log.e(
                    TAG,
                    "Protocol/server error: code=$errorCode, status=$errorStatus, message=$sanitizedMsg [gen: $gen]"
                )
                val userMsg = when {
                    sanitizedMsg.contains("401") || sanitizedMsg.contains("403") || sanitizedMsg.contains("API key", ignoreCase = true) || sanitizedMsg.contains("PERMISSION_DENIED") ->
                        "Gemini authentication failed."
                    sanitizedMsg.contains("NOT_FOUND") || sanitizedMsg.contains("404") || sanitizedMsg.contains("model", ignoreCase = true) ->
                        "Gemini Live session setup failed: Model unavailable."
                    else ->
                        "Gemini Live session setup failed."
                }
                _connectionState.value = LiveConnectionState.FAILED
                _sessionEvents.tryEmit(LiveSessionEvent.Error(userMsg, isFatal = true))
                setupDeferred?.complete(Result.failure(IOException("Server error: code=$errorCode, status=$errorStatus, message=$sanitizedMsg")))
                cleanUpSocket(cancelSocket = true, reason = "Server error: $sanitizedMsg")
                return
            }

            // 3. Check for interimInputTranscription (live user speech preview)
            val interimInputText = extractInterimInputTranscription(root)
            interimInputText?.let { interimText ->
                if (interimText.isNotBlank()) {
                    handledMessage = true
                    Log.i(TAG, "interimInputTranscription: $interimText [gen: $gen]")
                    _sessionEvents.tryEmit(LiveSessionEvent.InterimInputTranscription(interimText))
                }
            }

            // 4. Check for inputTranscription (final committed user speech transcript)
            val inputText = extractInputTranscription(root)
            inputText?.let { userText ->
                if (userText.isNotBlank()) {
                    handledMessage = true
                    Log.i(TAG, "inputTranscription: $userText [gen: $gen]")
                    _sessionEvents.tryEmit(LiveSessionEvent.InputTranscription(userText))
                }
            }

            // 5. Check for outputTranscription (coach speech transcript)
            val outputText = extractOutputTranscription(root)
            outputText?.let { coachText ->
                if (coachText.isNotBlank()) {
                    handledMessage = true
                    Log.i(TAG, "output transcription delta length=${coachText.length} [gen: $gen]")
                    _sessionEvents.tryEmit(LiveSessionEvent.OutputTranscription(coachText))
                }
            }

            // 6. Check serverContent (modelTurn audio, parts, barge-in, completions)
            val serverContent = root["serverContent"]?.jsonObject
            if (serverContent != null) {
                handledMessage = true
                Log.i(TAG, "serverContent received [gen: $gen]")

                val isInterrupted = serverContent["interrupted"]?.jsonPrimitive?.content?.toBooleanStrictOrNull() ?: false
                if (isInterrupted) {
                    Log.i(TAG, "server interrupted=true [gen: $gen]")
                    _sessionEvents.tryEmit(LiveSessionEvent.Interrupted)
                }

                val modelTurn = serverContent["modelTurn"]?.jsonObject
                if (modelTurn != null) {
                    val parts = modelTurn["parts"]?.jsonArray
                    parts?.forEach { partElement ->
                        val part = partElement.jsonObject
                        // Text transcript delta if present in parts
                        part["text"]?.jsonPrimitive?.content?.let { textDelta ->
                            if (textDelta.isNotBlank()) {
                                if (outputText == null) {
                                    Log.i(TAG, "modelTurn text delta length=${textDelta.length} [gen: $gen]")
                                    _sessionEvents.tryEmit(LiveSessionEvent.OutputTranscription(textDelta))
                                }
                            }
                        }

                        // Audio PCM inline data
                        val inlineData = part["inlineData"]?.jsonObject
                        val base64Pcm = inlineData?.get("data")?.jsonPrimitive?.content
                        if (!base64Pcm.isNullOrEmpty()) {
                            try {
                                val cleanBase64 = base64Pcm.replace("\n", "").replace("\r", "").trim()
                                val decoded = Base64.getMimeDecoder().decode(cleanBase64)
                                val mimeType = inlineData["mimeType"]?.jsonPrimitive?.content ?: "audio/pcm;rate=24000"
                                Log.i(TAG, "model audio mimeType=$mimeType bytes=${decoded.size} [gen: $gen]")
                                _sessionEvents.tryEmit(LiveSessionEvent.AudioOutputChunk(decoded))
                            } catch (e: Exception) {
                                Log.w(TAG, "Failed to decode base64 audio chunk: ${e.message}")
                            }
                        }
                    }
                }

                val generationComplete = serverContent["generationComplete"]?.jsonPrimitive?.content?.toBooleanStrictOrNull() ?: false
                if (generationComplete) {
                    Log.i(TAG, "generationComplete=true received [gen: $gen]")
                    _sessionEvents.tryEmit(LiveSessionEvent.GenerationComplete)
                }

                val turnComplete = serverContent["turnComplete"]?.jsonPrimitive?.content?.toBooleanStrictOrNull() ?: false
                if (turnComplete) {
                    Log.i(TAG, "turnComplete=true received [gen: $gen]")
                    _sessionEvents.tryEmit(LiveSessionEvent.TurnComplete)
                }
            }

            // 7. Check for waitingForInput
            val waitingForInput = root["waitingForInput"]?.jsonPrimitive?.content?.toBooleanStrictOrNull()
                ?: serverContent?.get("waitingForInput")?.jsonPrimitive?.content?.toBooleanStrictOrNull()
                ?: false
            if (waitingForInput) {
                handledMessage = true
                Log.i(TAG, "waitingForInput=true received [gen: $gen]")
                _sessionEvents.tryEmit(LiveSessionEvent.WaitingForInput)
            }

            // 8. Check for interactionStatus
            val interactionStatus = root["interactionStatus"]?.jsonPrimitive?.content
                ?: serverContent?.get("interactionStatus")?.jsonPrimitive?.content
            if (interactionStatus != null) {
                handledMessage = true
                Log.i(TAG, "interactionStatus=$interactionStatus received [gen: $gen]")
                _sessionEvents.tryEmit(LiveSessionEvent.InteractionStatus(interactionStatus))
            }

            if (!handledMessage) {
                Log.w(TAG, "Unhandled non-empty server message [gen: $gen]: ${sanitize(messageText)}")
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to parse server message [gen: $gen]: ${e.message}", e)
        }
    }

    private fun extractInterimInputTranscription(root: JsonObject): String? {
        val serverContent = root["serverContent"]?.jsonObject
        return extractText(root["interimInputTranscription"])
            ?: extractText(root["interimInputAudioTranscription"])
            ?: extractText(serverContent?.get("interimInputTranscription"))
            ?: extractText(serverContent?.get("interimInputAudioTranscription"))
    }

    private fun extractInputTranscription(root: JsonObject): String? {
        val serverContent = root["serverContent"]?.jsonObject
        return extractText(root["inputTranscription"])
            ?: extractText(root["inputAudioTranscription"])
            ?: extractText(serverContent?.get("inputTranscription"))
            ?: extractText(serverContent?.get("inputAudioTranscription"))
    }

    private fun extractOutputTranscription(root: JsonObject): String? {
        val serverContent = root["serverContent"]?.jsonObject
        return extractText(root["outputTranscription"])
            ?: extractText(root["outputAudioTranscription"])
            ?: extractText(serverContent?.get("outputTranscription"))
            ?: extractText(serverContent?.get("outputAudioTranscription"))
    }

    private fun extractText(element: JsonElement?): String? {
        if (element == null) return null
        if (element is JsonPrimitive && element.isString) return element.content
        if (element is JsonObject) {
            element["text"]?.jsonPrimitive?.content?.let { return it }
            val parts = element["parts"]?.jsonArray
            if (parts != null) {
                val sb = StringBuilder()
                for (p in parts) {
                    val t = p.jsonObject["text"]?.jsonPrimitive?.content
                    if (!t.isNullOrEmpty()) sb.append(t)
                }
                if (sb.isNotEmpty()) return sb.toString()
            }
        }
        return null
    }

    private fun sanitize(text: String): String {
        return text
            .replace(Regex("(?i)(key=)[^&\\s\"']+"), "$1[REDACTED]")
            .replace(Regex("(?i)(\"apiKey\"\\s*:\\s*\")[^\"]+(\")"), "$1[REDACTED]$2")
            .replace(Regex("AIza[0-9A-Za-z_-]{35}"), "[REDACTED]")
    }

    private fun mapConnectionError(t: Throwable, response: Response?): String {
        val code = response?.code ?: 0
        val msg = t.message?.lowercase() ?: ""
        return when {
            code == 401 || code == 403 || msg.contains("401") || msg.contains("403") || msg.contains("api key") || msg.contains("unauthorized") ->
                "Gemini authentication failed."
            msg.contains("timeout") || msg.contains("timed out") ->
                "Live session connection timed out."
            msg.contains("closed") || msg.contains("reset") || msg.contains("broken pipe") || msg.contains("eof") ->
                "Connection lost."
            else ->
                "Unable to connect to Gemini Live."
        }
    }
}
