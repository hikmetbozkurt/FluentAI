package com.seanora.fluentai.ai.live

import com.seanora.fluentai.core.audio.FakeAudioCaptureEngine
import com.seanora.fluentai.core.audio.FakeStreamAudioPlaybackEngine
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import okio.ByteString.Companion.encodeUtf8
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException
import java.util.concurrent.TimeoutException

@OptIn(ExperimentalCoroutinesApi::class)
class GeminiLiveProtocolAndLifecycleTest {

    private val json = Json { ignoreUnknownKeys = true }

    private val testConfig = LiveScenarioConfig(
        scenarioId = "speaking.b2.tech.review",
        title = "Tech Review",
        systemPrompt = "You are a helpful English speaking coach.",
        voiceName = "Aoede"
    )

    private fun createDummyResponse(request: Request, code: Int = 101): Response {
        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(code)
            .message("Switching Protocols")
            .build()
    }

    private class TestWebSocket(
        private val request: Request,
        val listener: WebSocketListener
    ) : WebSocket {
        val sentMessages = mutableListOf<String>()
        var isClosed = false
        var isCancelled = false
        var closeCode: Int? = null
        var closeReason: String? = null

        override fun request(): Request = request
        override fun queueSize(): Long = 0L

        override fun send(text: String): Boolean {
            sentMessages.add(text)
            return true
        }

        override fun send(bytes: ByteString): Boolean {
            sentMessages.add(bytes.utf8())
            return true
        }

        override fun close(code: Int, reason: String?): Boolean {
            isClosed = true
            closeCode = code
            closeReason = reason
            return true
        }

        override fun cancel() {
            isCancelled = true
        }
    }

    private class TestWebSocketFactory : WebSocket.Factory {
        val createdSockets = mutableListOf<TestWebSocket>()
        var onSocketCreated: ((TestWebSocket) -> Unit)? = null

        override fun newWebSocket(request: Request, listener: WebSocketListener): WebSocket {
            val socket = TestWebSocket(request, listener)
            createdSockets.add(socket)
            onSocketCreated?.invoke(socket)
            return socket
        }
    }

    // 1. Current WebSocket endpoint uses v1beta
    @Test
    fun endpoint_usesV1BetaAndBidiGenerateContent() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )

        val connectJob = launch { client.connect(testConfig) }
        runCurrent()

        assertEquals(1, factory.createdSockets.size)
        val requestUrl = factory.createdSockets[0].request().url.toString()
        assertTrue("URL must use v1beta", requestUrl.contains("v1beta"))
        assertTrue("URL must use BidiGenerateContent", requestUrl.contains("GenerativeService.BidiGenerateContent"))
        assertTrue("URL must include API key parameter", requestUrl.contains("key=test-api-key"))

        connectJob.cancel()
    }

    // 2. Model is gemini-3.8-live
    // 3. First client message is setup
    // 4. responseModalities is inside generationConfig
    // 5. responseModalities == ["AUDIO"]
    // 6. real systemInstruction restored with scenario prompt
    // 7. speechConfig includes voiceName
    // 8. inputAudioTranscription & outputAudioTranscription included under setup
    @Test
    fun firstClientMessage_isSetupWithGemini38LiveAudioModalitySystemInstructionAndTranscriptions() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )

        val connectJob = launch { client.connect(testConfig) }
        runCurrent()

        val socket = factory.createdSockets[0]
        socket.listener.onOpen(socket, createDummyResponse(socket.request()))
        runCurrent()

        // 1. Setup is the first client message
        assertEquals(1, socket.sentMessages.size)
        val rawJson = socket.sentMessages[0]
        val root = json.parseToJsonElement(rawJson).jsonObject
        val setupObj = root["setup"]?.jsonObject
        assertNotNull("First message must contain setup object", setupObj)

        // 2. setup.model is models/gemini-3.8-live
        assertEquals("models/gemini-3.8-live", setupObj!!["model"]?.jsonPrimitive?.content)

        // 3. generationConfig exists
        val generationConfig = setupObj["generationConfig"]?.jsonObject
        assertNotNull("generationConfig must exist inside setup", generationConfig)

        // 4. responseModalities is inside generationConfig, NOT directly under setup
        assertNull("responseModalities must not be directly under setup", setupObj["responseModalities"])
        val responseModalities = generationConfig!!["responseModalities"]?.jsonArray
        assertNotNull("responseModalities must be inside generationConfig", responseModalities)

        // 5. responseModalities == ["AUDIO"]
        assertEquals(1, responseModalities!!.size)
        assertEquals("AUDIO", responseModalities[0].jsonPrimitive.content)

        // 6. Voice name in speechConfig
        val voiceName = generationConfig["speechConfig"]?.jsonObject
            ?.get("voiceConfig")?.jsonObject
            ?.get("prebuiltVoiceConfig")?.jsonObject
            ?.get("voiceName")?.jsonPrimitive?.content
        assertEquals(testConfig.voiceName, voiceName)

        // 7. System prompt restored in systemInstruction
        val systemPrompt = setupObj["systemInstruction"]?.jsonObject
            ?.get("parts")?.jsonArray?.get(0)?.jsonObject?.get("text")?.jsonPrimitive?.content
        assertEquals(testConfig.systemPrompt, systemPrompt)

        // 8. inputAudioTranscription and outputAudioTranscription present under setup
        assertTrue("Must include inputAudioTranscription", setupObj.containsKey("inputAudioTranscription"))
        assertTrue("Must include outputAudioTranscription", setupObj.containsKey("outputAudioTranscription"))

        connectJob.cancel()
    }

    // 7. {"setupComplete":{}} resolves connect() successfully
    @Test
    fun successfulSetup_transitionsToSessionReadyAndResolvesConnect() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )

        var connectResult: Result<Unit>? = null
        launch {
            connectResult = client.connect(testConfig)
        }
        runCurrent()

        val socket = factory.createdSockets[0]
        socket.listener.onOpen(socket, createDummyResponse(socket.request()))
        runCurrent()

        // Server acknowledges setup
        socket.listener.onMessage(socket, """{"setupComplete":{}}""")
        runCurrent()

        assertEquals(LiveConnectionState.SESSION_READY, client.connectionState.value)
        assertNotNull(connectResult)
        assertTrue(connectResult!!.isSuccess)
    }

    // 8. Arbitrary server error payload produces setup failure
    @Test
    fun arbitraryServerError_producesSetupFailure() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )

        var connectResult: Result<Unit>? = null
        launch {
            connectResult = client.connect(testConfig)
        }
        runCurrent()

        val socket = factory.createdSockets[0]
        socket.listener.onOpen(socket, createDummyResponse(socket.request()))
        runCurrent()

        // Server returns error payload with status and message
        socket.listener.onMessage(
            socket,
            """{"error":{"code":400,"status":"INVALID_ARGUMENT","message":"Field unsupported: testField"}}"""
        )
        runCurrent()

        assertEquals(LiveConnectionState.FAILED, client.connectionState.value)
        assertNotNull(connectResult)
        assertTrue(connectResult!!.isFailure)
        assertTrue(socket.isCancelled)
    }

    @Test
    fun server404NotFoundError_producesSetupFailureWithoutHanging() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )

        var connectResult: Result<Unit>? = null
        launch {
            connectResult = client.connect(testConfig)
        }
        runCurrent()

        val socket = factory.createdSockets[0]
        socket.listener.onOpen(socket, createDummyResponse(socket.request()))
        runCurrent()

        // Server returns 404 NOT_FOUND error payload
        socket.listener.onMessage(
            socket,
            """{"error":{"code":404,"status":"NOT_FOUND","message":"models/gemini-3.8-live is not found for API version v1beta"}}"""
        )
        runCurrent()

        assertEquals(LiveConnectionState.FAILED, client.connectionState.value)
        assertNotNull(connectResult)
        assertTrue(connectResult!!.isFailure)
        assertTrue(socket.isCancelled)
    }

    // 9. onClosing and onClosed do not leave connect() hanging
    @Test
    fun onClosingDuringSetup_completesConnectWithFailureWithoutHanging() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )

        var connectResult: Result<Unit>? = null
        launch {
            connectResult = client.connect(testConfig)
        }
        runCurrent()

        val socket = factory.createdSockets[0]
        socket.listener.onOpen(socket, createDummyResponse(socket.request()))
        runCurrent()

        // Server sends closing before setup complete
        socket.listener.onClosing(socket, 1008, "Policy violation")
        runCurrent()

        assertEquals(LiveConnectionState.CLOSED, client.connectionState.value)
        assertNotNull("connect() must complete immediately on server closing without hanging", connectResult)
        assertTrue(connectResult!!.isFailure)
    }

    @Test
    fun onClosedDuringSetup_completesConnectWithFailureWithoutHanging() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )

        var connectResult: Result<Unit>? = null
        launch {
            connectResult = client.connect(testConfig)
        }
        runCurrent()

        val socket = factory.createdSockets[0]
        socket.listener.onOpen(socket, createDummyResponse(socket.request()))
        runCurrent()

        // Server closes socket before setup complete
        socket.listener.onClosed(socket, 1006, "Abnormal closure")
        runCurrent()

        assertEquals(LiveConnectionState.CLOSED, client.connectionState.value)
        assertNotNull("connect() must complete immediately on server closed without hanging", connectResult)
        assertTrue(connectResult!!.isFailure)
    }

    // 10. Setup timeout still cancels the socket cleanly
    @Test
    fun setupTimeout_cancelsSocketCleanlyAndTransitionsToFailed() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )

        var connectResult: Result<Unit>? = null
        launch {
            connectResult = client.connect(testConfig)
        }
        runCurrent()

        val socket = factory.createdSockets[0]
        socket.listener.onOpen(socket, createDummyResponse(socket.request()))
        runCurrent()

        // No setupComplete arrives; advance virtual time past 1000ms timeout
        advanceTimeBy(1001L)
        runCurrent()

        assertEquals(LiveConnectionState.FAILED, client.connectionState.value)
        val result = requireNotNull(connectResult)
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is TimeoutException)
        assertTrue("Socket must be cancelled upon setup timeout", socket.isCancelled)
    }

    // 11. 16 kHz PCM produces realtimeInput.audio with audio/pcm;rate=16000
    @Test
    fun sendAudioChunk_producesRealtimeInputAudioWithCorrectMimeType() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )

        launch { client.connect(testConfig) }
        runCurrent()

        val socket = factory.createdSockets[0]
        socket.listener.onOpen(socket, createDummyResponse(socket.request()))
        socket.listener.onMessage(socket, """{"setupComplete":{}}""")
        runCurrent()

        assertEquals(LiveConnectionState.SESSION_READY, client.connectionState.value)

        // Send 16kHz PCM audio chunk
        val pcm = byteArrayOf(0x01, 0x02, 0x03, 0x04)
        client.sendAudioChunk(pcm)
        runCurrent()

        assertEquals(2, socket.sentMessages.size)
        val audioMsg = json.parseToJsonElement(socket.sentMessages[1]).jsonObject
        val realtimeInput = audioMsg["realtimeInput"]?.jsonObject
        assertNotNull("Must contain realtimeInput", realtimeInput)

        // Obsolete mediaChunks must not exist
        assertNull("mediaChunks must not exist", realtimeInput!!["mediaChunks"])

        val audioObj = realtimeInput["audio"]?.jsonObject
        assertNotNull("Must contain audio object", audioObj)
        assertEquals("audio/pcm;rate=16000", audioObj!!["mimeType"]?.jsonPrimitive?.content)
        assertEquals("AQIDBA==", audioObj["data"]?.jsonPrimitive?.content)
    }

    // 12. Session is not ready merely on onOpen
    @Test
    fun sessionIsNotReadyMerelyOnWebSocketOpen() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )

        var connectCompleted = false
        val connectJob = launch {
            client.connect(testConfig)
            connectCompleted = true
        }
        runCurrent()

        val socket = factory.createdSockets[0]
        socket.listener.onOpen(socket, createDummyResponse(socket.request()))
        runCurrent()

        // Socket is open, but session is not ready yet
        assertEquals(LiveConnectionState.SOCKET_OPEN, client.connectionState.value)
        assertFalse("connect() must not complete until setupComplete is received", connectCompleted)

        connectJob.cancel()
    }

    // 13. Duplicate connect/start does not create multiple active sessions
    @Test
    fun duplicateConnect_doesNotCreateMultipleWebSockets() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )

        launch { client.connect(testConfig) }
        runCurrent()

        val socket = factory.createdSockets[0]
        socket.listener.onOpen(socket, createDummyResponse(socket.request()))
        socket.listener.onMessage(socket, """{"setupComplete":{}}""")
        runCurrent()

        assertEquals(1, factory.createdSockets.size)

        // Attempt second connect while active
        launch { client.connect(testConfig) }
        runCurrent()

        assertEquals("No second socket should be created", 1, factory.createdSockets.size)
    }

    // 14. Disconnect clears and cancels the socket
    @Test
    fun disconnect_clearsAndCancelsSocket() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )

        launch { client.connect(testConfig) }
        runCurrent()

        val socket = factory.createdSockets[0]
        socket.listener.onOpen(socket, createDummyResponse(socket.request()))
        socket.listener.onMessage(socket, """{"setupComplete":{}}""")
        runCurrent()

        client.disconnect()
        runCurrent()

        assertTrue(socket.isCancelled)
        assertEquals(LiveConnectionState.CLOSED, client.connectionState.value)
    }

    // 15. Stale callbacks from an old socket cannot mutate the new session
    @Test
    fun staleCallbacksFromOldSocket_doNotAffectNewSession() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )

        // Start session 1
        launch { client.connect(testConfig) }
        runCurrent()
        val socket1 = factory.createdSockets[0]

        // Stop session 1
        client.disconnect()
        runCurrent()

        // Start session 2
        launch { client.connect(testConfig) }
        runCurrent()
        val socket2 = factory.createdSockets[1]
        socket2.listener.onOpen(socket2, createDummyResponse(socket2.request()))
        socket2.listener.onMessage(socket2, """{"setupComplete":{}}""")
        runCurrent()

        assertEquals(LiveConnectionState.SESSION_READY, client.connectionState.value)

        // Late failure on old socket 1 (e.g. OkHttp ping timeout after 20 seconds)
        socket1.listener.onFailure(socket1, IOException("sent ping but didn't receive pong"), null)
        runCurrent()

        // Session 2 state must NOT be mutated to FAILED!
        assertEquals("Old socket failure must not affect new session", LiveConnectionState.SESSION_READY, client.connectionState.value)
    }

    // 16. Server audio in serverContent.modelTurn is emitted as AudioOutputChunk
    @Test
    fun serverAudio_emitsAudioOutputChunk() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )

        val events = mutableListOf<LiveSessionEvent>()
        val collectJob = launch {
            client.sessionEvents.collect { events.add(it) }
        }

        launch { client.connect(testConfig) }
        runCurrent()

        val socket = factory.createdSockets[0]
        socket.listener.onOpen(socket, createDummyResponse(socket.request()))
        socket.listener.onMessage(socket, """{"setupComplete":{}}""")
        runCurrent()

        val serverAudioMsg = """
            {
              "serverContent": {
                "modelTurn": {
                  "parts": [
                    {
                      "inlineData": {
                        "mimeType": "audio/pcm;rate=24000",
                        "data": "AQID"
                      }
                    }
                  ]
                }
              }
            }
        """.trimIndent()

        socket.listener.onMessage(socket, serverAudioMsg)
        runCurrent()

        val audioEvent = events.filterIsInstance<LiveSessionEvent.AudioOutputChunk>().firstOrNull()
        assertNotNull("Must emit AudioOutputChunk", audioEvent)
        assertEquals(3, audioEvent!!.pcmData.size)
        assertEquals(0x01.toByte(), audioEvent.pcmData[0])
        assertEquals(0x02.toByte(), audioEvent.pcmData[1])
        assertEquals(0x03.toByte(), audioEvent.pcmData[2])

        collectJob.cancel()
    }

    // 17. Network/socket failure reaches visible ERROR state
    @Test
    fun networkFailure_reachesVisibleErrorState() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val captureEngine = FakeAudioCaptureEngine()
        val playbackEngine = FakeStreamAudioPlaybackEngine()
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )

        val manager = LiveCoachSessionManager(
            audioCaptureEngine = captureEngine,
            playbackEngine = playbackEngine,
            liveAiClient = client,
            dispatcher = testDispatcher
        )

        manager.startSession(testConfig)
        runCurrent()

        val socket = factory.createdSockets[0]
        socket.listener.onFailure(socket, IOException("Connection reset by peer"), null)
        runCurrent()

        assertEquals(VoiceUiState.ERROR, manager.voiceState.value)
        assertNotNull(manager.errorMessage.value)
        assertTrue(
            "Error message must be visible and user-friendly",
            manager.errorMessage.value!!.contains("Connection") ||
                manager.errorMessage.value!!.contains("connect") ||
                manager.errorMessage.value!!.contains("Live")
        )
        assertFalse(captureEngine.isRecording.value)
    }

    // 18. Gemini speaks first on startup via clientContent instruction, transitions to LISTENING on turnComplete
    @Test
    fun sessionStartup_geminiSpeaksFirstWithInternalClientContentAndTransitionsToListeningOnTurnComplete() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val captureEngine = FakeAudioCaptureEngine()
        val playbackEngine = FakeStreamAudioPlaybackEngine()
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )

        val manager = LiveCoachSessionManager(
            audioCaptureEngine = captureEngine,
            playbackEngine = playbackEngine,
            liveAiClient = client,
            dispatcher = testDispatcher
        )

        manager.startSession(testConfig)
        runCurrent()

        assertEquals(1, factory.createdSockets.size)
        val socket = factory.createdSockets[0]

        // 1. Socket opens
        socket.listener.onOpen(socket, createDummyResponse(socket.request()))
        runCurrent()

        // Setup message sent
        assertEquals(1, socket.sentMessages.size)

        // 2. Server sends setupComplete
        socket.listener.onMessage(socket, """{"setupComplete":{}}""")
        runCurrent()

        // 3. Microphone is capturing, and state is COACH_SPEAKING (Gemini speaks first!)
        assertTrue(captureEngine.isRecording.value)
        assertEquals(VoiceUiState.COACH_SPEAKING, manager.voiceState.value)

        // 4. Second client message was sent with clientContent startup instruction
        assertEquals(2, socket.sentMessages.size)
        val clientContentRaw = socket.sentMessages[1]
        val root = json.parseToJsonElement(clientContentRaw).jsonObject
        val clientContent = root["clientContent"]?.jsonObject
        assertNotNull("Must send clientContent", clientContent)
        val text = clientContent!!["turns"]?.jsonArray?.get(0)?.jsonObject
            ?.get("parts")?.jsonArray?.get(0)?.jsonObject
            ?.get("text")?.jsonPrimitive?.content
        assertEquals(LiveCoachSessionManager.STARTUP_INSTRUCTION, text)
        assertEquals("true", clientContent["turnComplete"]?.jsonPrimitive?.content)

        // 5. Internal startup instruction is NOT in learner's transcript
        assertTrue(manager.transcript.value.isEmpty())

        // 6. Gemini sends turnComplete -> transitions to LISTENING
        socket.listener.onMessage(socket, """{"serverContent":{"turnComplete":true}}""")
        runCurrent()

        assertEquals(VoiceUiState.LISTENING, manager.voiceState.value)
    }

    // 19. interimInputTranscription emits InterimInputTranscription
    @Test
    fun interimInputTranscription_emitsFirstClassEvent() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )

        val events = mutableListOf<LiveSessionEvent>()
        val collectJob = launch { client.sessionEvents.collect { events.add(it) } }

        launch { client.connect(testConfig) }
        runCurrent()
        val socket = factory.createdSockets[0]
        socket.listener.onOpen(socket, createDummyResponse(socket.request()))
        socket.listener.onMessage(socket, """{"setupComplete":{}}""")
        runCurrent()

        // Server sends interimInputTranscription
        val interimMsg = """{"serverContent":{"interimInputTranscription":{"text":"I would like"}}}"""
        socket.listener.onMessage(socket, interimMsg)
        runCurrent()

        val interimEvent = events.filterIsInstance<LiveSessionEvent.InterimInputTranscription>().firstOrNull()
        assertNotNull(interimEvent)
        assertEquals("I would like", interimEvent!!.text)

        collectJob.cancel()
    }

    // 20. inputTranscription emits InputTranscription
    @Test
    fun inputTranscription_emitsFirstClassFinalEvent() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )

        val events = mutableListOf<LiveSessionEvent>()
        val collectJob = launch { client.sessionEvents.collect { events.add(it) } }

        launch { client.connect(testConfig) }
        runCurrent()
        val socket = factory.createdSockets[0]
        socket.listener.onOpen(socket, createDummyResponse(socket.request()))
        socket.listener.onMessage(socket, """{"setupComplete":{}}""")
        runCurrent()

        // Server sends inputTranscription
        val finalMsg = """{"serverContent":{"inputTranscription":{"text":"I would like to order."}}}"""
        socket.listener.onMessage(socket, finalMsg)
        runCurrent()

        val finalEvent = events.filterIsInstance<LiveSessionEvent.InputTranscription>().firstOrNull()
        assertNotNull(finalEvent)
        assertEquals("I would like to order.", finalEvent!!.text)

        collectJob.cancel()
    }

    // 21. outputTranscription emits OutputTranscription
    @Test
    fun outputTranscription_emitsFirstClassEvent() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )

        val events = mutableListOf<LiveSessionEvent>()
        val collectJob = launch { client.sessionEvents.collect { events.add(it) } }

        launch { client.connect(testConfig) }
        runCurrent()
        val socket = factory.createdSockets[0]
        socket.listener.onOpen(socket, createDummyResponse(socket.request()))
        socket.listener.onMessage(socket, """{"setupComplete":{}}""")
        runCurrent()

        // Server sends outputTranscription
        val deltaMsg = """{"serverContent":{"outputTranscription":{"text":"Welcome to the store."}}}"""
        socket.listener.onMessage(socket, deltaMsg)
        runCurrent()

        val deltaEvent = events.filterIsInstance<LiveSessionEvent.OutputTranscription>().firstOrNull()
        assertNotNull(deltaEvent)
        assertEquals("Welcome to the store.", deltaEvent!!.text)
        assertEquals(0, events.filterIsInstance<LiveSessionEvent.TranscriptDelta>().size)

        collectJob.cancel()
    }

    // 22. serverContent.interrupted emits Interrupted event
    @Test
    fun serverInterruptedFlag_emitsInterruptedEvent() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )

        val events = mutableListOf<LiveSessionEvent>()
        val collectJob = launch { client.sessionEvents.collect { events.add(it) } }

        launch { client.connect(testConfig) }
        runCurrent()
        val socket = factory.createdSockets[0]
        socket.listener.onOpen(socket, createDummyResponse(socket.request()))
        socket.listener.onMessage(socket, """{"setupComplete":{}}""")
        runCurrent()

        // Server sends interrupted flag
        val interruptedMsg = """{"serverContent":{"interrupted":true}}"""
        socket.listener.onMessage(socket, interruptedMsg)
        runCurrent()

        val interruptedEvent = events.filterIsInstance<LiveSessionEvent.Interrupted>().firstOrNull()
        assertNotNull(interruptedEvent)
        assertEquals(0, events.filterIsInstance<LiveSessionEvent.InterruptedByBargeIn>().size)

        collectJob.cancel()
    }

    @Test
    fun oneInputTranscriptionFrame_emitsOneCanonicalTranscriptEvent() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )
        val events = mutableListOf<LiveSessionEvent>()
        val collectJob = launch { client.sessionEvents.collect { events.add(it) } }

        launch { client.connect(testConfig) }
        runCurrent()
        val socket = factory.createdSockets[0]
        socket.listener.onOpen(socket, createDummyResponse(socket.request()))
        socket.listener.onMessage(socket, """{"setupComplete":{}}""")
        runCurrent()
        events.clear()

        socket.listener.onMessage(socket, """{"serverContent":{"inputTranscription":{"text":"One turn."}}}""")
        runCurrent()

        assertEquals(1, events.filterIsInstance<LiveSessionEvent.InputTranscription>().size)
        assertEquals(0, events.filterIsInstance<LiveSessionEvent.TranscriptDelta>().size)
        collectJob.cancel()
    }

    @Test
    fun emptyServerObject_isIgnoredWithoutSessionEvents() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )
        val events = mutableListOf<LiveSessionEvent>()
        val collectJob = launch { client.sessionEvents.collect { events.add(it) } }

        launch { client.connect(testConfig) }
        runCurrent()
        val socket = factory.createdSockets[0]
        socket.listener.onOpen(socket, createDummyResponse(socket.request()))
        socket.listener.onMessage(socket, """{"setupComplete":{}}""")
        runCurrent()
        events.clear()

        socket.listener.onMessage(socket, "{}")
        runCurrent()

        assertTrue(events.isEmpty())
        collectJob.cancel()
    }

    // 23. generationComplete != turnComplete
    @Test
    fun generationComplete_and_turnComplete_areDistinctEvents() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )

        val events = mutableListOf<LiveSessionEvent>()
        val collectJob = launch { client.sessionEvents.collect { events.add(it) } }

        launch { client.connect(testConfig) }
        runCurrent()
        val socket = factory.createdSockets[0]
        socket.listener.onOpen(socket, createDummyResponse(socket.request()))
        socket.listener.onMessage(socket, """{"setupComplete":{}}""")
        runCurrent()

        // 1. First serverContent sends generationComplete=true only
        socket.listener.onMessage(socket, """{"serverContent":{"generationComplete":true}}""")
        runCurrent()

        assertEquals(1, events.filterIsInstance<LiveSessionEvent.GenerationComplete>().size)
        assertEquals(0, events.filterIsInstance<LiveSessionEvent.TurnComplete>().size)

        // 2. Later serverContent sends turnComplete=true
        socket.listener.onMessage(socket, """{"serverContent":{"turnComplete":true}}""")
        runCurrent()

        assertEquals(1, events.filterIsInstance<LiveSessionEvent.GenerationComplete>().size)
        assertEquals(1, events.filterIsInstance<LiveSessionEvent.TurnComplete>().size)

        collectJob.cancel()
    }

    // 24. Binary frame message handling
    @Test
    fun binaryWebSocketMessage_isParsedCorrectly() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )

        val events = mutableListOf<LiveSessionEvent>()
        val collectJob = launch { client.sessionEvents.collect { events.add(it) } }

        launch { client.connect(testConfig) }
        runCurrent()
        val socket = factory.createdSockets[0]
        socket.listener.onOpen(socket, createDummyResponse(socket.request()))

        // Deliver setupComplete as binary frame (ByteString)
        val binarySetup = """{"setupComplete":{}}""".encodeUtf8()
        socket.listener.onMessage(socket, binarySetup)
        runCurrent()

        assertEquals(LiveConnectionState.SESSION_READY, client.connectionState.value)
        assertTrue(events.any { it is LiveSessionEvent.Connected })

        collectJob.cancel()
    }

    // 25. waitingForInput and sessionResumptionUpdate
    @Test
    fun waitingForInput_and_sessionResumptionUpdate_areHandled() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val factory = TestWebSocketFactory()
        val client = GeminiLiveWebSocketClient(
            dispatcher = testDispatcher,
            apiKeyProvider = { "test-api-key" },
            webSocketFactory = factory,
            setupTimeoutMs = 1000L
        )

        val events = mutableListOf<LiveSessionEvent>()
        val collectJob = launch { client.sessionEvents.collect { events.add(it) } }

        launch { client.connect(testConfig) }
        runCurrent()
        val socket = factory.createdSockets[0]
        socket.listener.onOpen(socket, createDummyResponse(socket.request()))
        socket.listener.onMessage(socket, """{"setupComplete":{}}""")
        runCurrent()

        socket.listener.onMessage(socket, """{"sessionResumptionUpdate":{"newHandle":"handle_123"}}""")
        socket.listener.onMessage(socket, """{"serverContent":{"waitingForInput":true}}""")
        runCurrent()

        assertTrue(events.any { it is LiveSessionEvent.SessionResumptionUpdate })
        assertTrue(events.any { it is LiveSessionEvent.WaitingForInput })

        collectJob.cancel()
    }
}
