package com.seanora.fluentai.ai.live

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Deterministic offline mock implementation of LiveAiClient.
 * Provides simulated bidirectional voice dialogues, turn-taking, and instant barge-in support.
 */
@Singleton
class MockLiveAiClient @Inject constructor() : LiveAiClient {

    private var dispatcher: CoroutineDispatcher = Dispatchers.Default
    private var scope = CoroutineScope(SupervisorJob() + dispatcher)

    constructor(dispatcher: CoroutineDispatcher) : this() {
        this.dispatcher = dispatcher
        this.scope = CoroutineScope(SupervisorJob() + dispatcher)
    }

    private var activeSpeechJob: Job? = null
    private var currentConfig: LiveScenarioConfig? = null

    private val _connectionState = MutableStateFlow(LiveConnectionState.DISCONNECTED)
    override val connectionState: StateFlow<LiveConnectionState> = _connectionState.asStateFlow()

    private val _sessionEvents = MutableSharedFlow<LiveSessionEvent>(extraBufferCapacity = 64)
    override val sessionEvents: SharedFlow<LiveSessionEvent> = _sessionEvents.asSharedFlow()

    var shouldFailConnection: Boolean = false
    var speechDelayMs: Long = 50L

    fun emitEvent(event: LiveSessionEvent) {
        _sessionEvents.tryEmit(event)
    }

    fun simulateUserSpeech(text: String) {
        _sessionEvents.tryEmit(
            LiveSessionEvent.TranscriptDelta(
                role = TranscriptRole.USER,
                text = text,
                isFinal = true
            )
        )
    }

    override suspend fun connect(config: LiveScenarioConfig): Result<Unit> {
        if (shouldFailConnection) {
            _connectionState.value = LiveConnectionState.FAILED
            _sessionEvents.tryEmit(LiveSessionEvent.Error("Connection to Live Coach failed (mock offline error)", isFatal = true))
            return Result.failure(IllegalStateException("Mock connection failure"))
        }

        currentConfig = config
        _connectionState.value = LiveConnectionState.CONNECTING
        delay(speechDelayMs)
        _connectionState.value = LiveConnectionState.CONNECTED
        _sessionEvents.tryEmit(LiveSessionEvent.Connected("mock-session-${System.currentTimeMillis()}"))

        // Launch initial welcome turn
        val greeting = when {
            config.persona == SpeakingPersona.GALADRIEL ->
                "Hi, I'm Galadriel. It's lovely to talk with you—how has your day been?"
            config.scenarioId?.contains("tech", ignoreCase = true) == true ->
                "Hello! Welcome to the technical review. How are you approaching our database migration schedule?"
            config.scenarioId?.contains("interview", ignoreCase = true) == true ->
                "Welcome to the interview. Could you start by introducing yourself and your background?"
            else ->
                "Hello! I am your FluentAI coach. What would you like to discuss today?"
        }

        speakCoachResponse(greeting)
        return Result.success(Unit)
    }

    override suspend fun sendAudioChunk(pcmData: ByteArray) {
        // In mock mode, we receive user mic audio frames.
        // If coach is speaking when audio is received, caller can trigger barge-in.
    }

    override suspend fun sendTextMessage(text: String) {
        if (_connectionState.value != LiveConnectionState.CONNECTED) return

        val isStartup = text.contains("Begin the speaking session now", ignoreCase = true)
        if (!isStartup) {
            _sessionEvents.tryEmit(
                LiveSessionEvent.TranscriptDelta(
                    role = TranscriptRole.USER,
                    text = text,
                    isFinal = true
                )
            )
        }

        // Generate context-aware mock reply
        val response = when {
            isStartup -> {
                when {
                    currentConfig?.scenarioId?.contains("tech", ignoreCase = true) == true ->
                        "Hello! Welcome to the technical review. How are you approaching our database migration schedule?"
                    currentConfig?.scenarioId?.contains("interview", ignoreCase = true) == true ->
                        "Welcome to the interview. Could you start by introducing yourself and your background?"
                    else ->
                        "Hello! I am your FluentAI coach. What would you like to discuss today?"
                }
            }
            text.contains("deadline", ignoreCase = true) ->
                "Understood. Tight deadlines often force trade-offs between technical debt and rapid delivery. How will you communicate that to stakeholders?"
            text.contains("architecture", ignoreCase = true) ->
                "That architectural decision makes sense. Did you consider potential latency bottlenecks in the networking layer?"
            else ->
                "That's an interesting point. Could you elaborate on how you would implement that in practice?"
        }

        speakCoachResponse(response)
    }

    override suspend fun sendBargeInInterrupt() {
        activeSpeechJob?.cancel()
        activeSpeechJob = null
        _sessionEvents.tryEmit(LiveSessionEvent.InterruptedByBargeIn)
    }

    override suspend fun disconnect() {
        activeSpeechJob?.cancel()
        activeSpeechJob = null
        _connectionState.value = LiveConnectionState.DISCONNECTED
        _sessionEvents.tryEmit(LiveSessionEvent.Disconnected("Session ended cleanly"))
    }

    private fun speakCoachResponse(text: String) {
        activeSpeechJob?.cancel()
        activeSpeechJob = scope.launch {
            // Emulate streaming transcript words
            val words = text.split(" ")
            val accumulated = StringBuilder()

            for (i in words.indices) {
                if (!isActive) break
                if (accumulated.isNotEmpty()) accumulated.append(" ")
                accumulated.append(words[i])

                val isFinal = (i == words.lastIndex)
                _sessionEvents.tryEmit(
                    LiveSessionEvent.TranscriptDelta(
                        role = TranscriptRole.COACH,
                        text = accumulated.toString(),
                        isFinal = isFinal
                    )
                )

                // Emulate synthesized PCM chunk (synthetic tone / speech frame)
                val mockPcm = ByteArray(960) { index -> (index % 127).toByte() }
                _sessionEvents.tryEmit(LiveSessionEvent.AudioOutputChunk(mockPcm))

                delay(speechDelayMs)
            }

            if (isActive) {
                _sessionEvents.tryEmit(LiveSessionEvent.TurnComplete)
            }
        }
    }
}
