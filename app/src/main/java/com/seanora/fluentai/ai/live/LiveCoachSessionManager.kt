package com.seanora.fluentai.ai.live

import android.util.Log
import com.seanora.fluentai.core.audio.AudioCaptureEngine
import com.seanora.fluentai.core.audio.StreamAudioPlaybackEngine
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Orchestrator for real-time speech coaching sessions.
 * Manages full-duplex conversational turn-taking, audio capture, streaming playback,
 * dialogue transcript accumulation, and natural server-authoritative interruptions per AGENTS.md.
 */
@Singleton
class LiveCoachSessionManager @Inject constructor(
    private val audioCaptureEngine: AudioCaptureEngine,
    private val playbackEngine: StreamAudioPlaybackEngine,
    private val liveAiClient: LiveAiClient
) {
    companion object {
        private const val TAG = "LiveCoachSession"
        const val STARTUP_INSTRUCTION = "Begin the speaking session now. Greet the learner naturally according to the active scenario and immediately start the conversation with an appropriate question or prompt."
    }

    private var dispatcher: CoroutineDispatcher = Dispatchers.Default
    private var scope = CoroutineScope(SupervisorJob() + dispatcher)

    constructor(
        audioCaptureEngine: AudioCaptureEngine,
        playbackEngine: StreamAudioPlaybackEngine,
        liveAiClient: LiveAiClient,
        dispatcher: CoroutineDispatcher
    ) : this(audioCaptureEngine, playbackEngine, liveAiClient) {
        this.dispatcher = dispatcher
        this.scope = CoroutineScope(SupervisorJob() + dispatcher)
    }

    private val _voiceState = MutableStateFlow(VoiceUiState.IDLE)
    val voiceState: StateFlow<VoiceUiState> = _voiceState.asStateFlow()

    private val _transcript = MutableStateFlow<List<TranscriptEntry>>(emptyList())
    val transcript: StateFlow<List<TranscriptEntry>> = _transcript.asStateFlow()

    private val _activeAmplitude = MutableStateFlow(0f)
    val activeAmplitude: StateFlow<Float> = _activeAmplitude.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Independent runtime facts (Requirement 6)
    private var connectionReady: Boolean = false
    private var isSessionPaused: Boolean = false
    private var sessionEnding: Boolean = false
    private var sessionError: String? = null
    private var modelGenerationActive: Boolean = false
    private var modelPlaybackActive: Boolean = false
    private var userSpeechActive: Boolean = false
    private var waitingForModel: Boolean = false
    private var isMicrophoneMuted: Boolean = false

    // Turn-based transcript buffers (Requirement 9)
    private val finalizedTranscripts = mutableListOf<TranscriptEntry>()
    private val activeCoachBuffer = StringBuilder()
    private var activeCoachTurnId: String? = null
    private var activeUserInterimText: String? = null
    private var activeUserTurnId: String? = null
    private var nextTranscriptTurnId: Long = 1L
    private var lastFinalizedUserText: String? = null
    private var userFinalizationBoundaryOpen: Boolean = false
    private var coachGenerationComplete: Boolean = false
    private var playbackTailDrainRequested: Boolean = false

    private var sessionJob: Job? = null
    private var startupWatchdogJob: Job? = null
    private var eventCollectionJob: Job? = null
    private var captureCollectionJob: Job? = null
    private var playbackStateJob: Job? = null
    private var micAmplitudeJob: Job? = null
    private var playbackAmplitudeJob: Job? = null

    var currentScenario: LiveScenarioConfig? = null
        private set

    /**
     * Starts a live speaking session with the specified scenario configuration.
     * Full-duplex sequence:
     * 1. Start event listener & playback observer
     * 2. Connect to Live AI client and await server setup acknowledgement (SESSION_READY)
     * 3. Start microphone capture and continuous audio piping
     * 4. Trigger initial Gemini model turn: Gemini speaks first (COACH_SPEAKING)
     * 5. When Gemini finishes greeting (turnComplete + playback drained), transition to LISTENING
     */
    fun startSession(config: LiveScenarioConfig) {
        if (_voiceState.value != VoiceUiState.IDLE &&
            _voiceState.value != VoiceUiState.ENDED &&
            _voiceState.value != VoiceUiState.ERROR
        ) {
            return
        }

        currentScenario = config
        resetRuntimeFacts()
        clearTranscriptBuffers()

        _errorMessage.value = null
        _voiceState.value = VoiceUiState.CONNECTING

        sessionJob?.cancel()
        sessionJob = scope.launch {
            // Start listening to AI client events
            startListeningToClientEvents()

            // Observe explicit playback lifecycle
            observePlaybackLifecycle()

            // Connect to Live AI client (suspends until setupComplete or error)
            val connectResult = liveAiClient.connect(config)
            if (connectResult.isFailure) {
                val error = connectResult.exceptionOrNull()
                val message = error?.message ?: "Failed to connect to Live Coach"
                sessionError = message
                _errorMessage.value = message
                cleanUpSessionResources()
                updateDerivedState()
                return@launch
            }

            connectionReady = true
            updateDerivedState()

            // Start audio piping receiver before capture begins so no chunks are dropped
            startAudioPiping()

            // Start microphone capture in full-duplex communication mode
            val captureResult = audioCaptureEngine.startCapture()
            if (captureResult.isFailure) {
                val message = "Failed to start microphone: ${captureResult.exceptionOrNull()?.message}"
                sessionError = message
                _errorMessage.value = message
                cleanUpSessionResources()
                updateDerivedState()
                return@launch
            }

            Log.i(TAG, "audio streaming started")

            // Gemini speaks first: request opening coach turn according to active scenario
            modelGenerationActive = true
            updateDerivedState()
            Log.i(TAG, "initial coach turn requested")
            liveAiClient.sendTextMessage(STARTUP_INSTRUCTION)
            startStartupResponseWatchdog()
        }
    }

    /**
     * Explicit user UI interruption action.
     * Stops AI coach speech immediately, flushes playback buffers, and transitions to USER_SPEAKING.
     */
    fun interruptCoach(reason: String = "User barge-in") {
        if (_voiceState.value != VoiceUiState.PAUSED &&
            _voiceState.value != VoiceUiState.ENDING &&
            _voiceState.value != VoiceUiState.ENDED &&
            _voiceState.value != VoiceUiState.ERROR
        ) {
            Log.i(TAG, "Manual coach interruption: $reason")
            playbackEngine.stopAndFlush()
            modelPlaybackActive = false
            modelGenerationActive = false
            userSpeechActive = true
            waitingForModel = false
            finalizeActiveCoachTurn()
            syncTranscriptState()
            updateDerivedState()
            scope.launch {
                liveAiClient.sendBargeInInterrupt()
            }
        }
    }

    /**
     * Pauses the ongoing session without terminating connection.
     */
    fun pauseSession() {
        if (_voiceState.value == VoiceUiState.COACH_SPEAKING ||
            _voiceState.value == VoiceUiState.USER_SPEAKING ||
            _voiceState.value == VoiceUiState.LISTENING ||
            _voiceState.value == VoiceUiState.PROCESSING
        ) {
            isSessionPaused = true
            audioCaptureEngine.stopCapture()
            playbackEngine.stopAndFlush()
            modelPlaybackActive = false
            updateDerivedState()
        }
    }

    /**
     * Resumes a paused session.
     */
    fun resumeSession() {
        if (isSessionPaused) {
            isSessionPaused = false
            if (!isMicrophoneMuted) {
                audioCaptureEngine.startCapture()
            }
            updateDerivedState()
        }
    }

    fun setMicrophoneMuted(muted: Boolean) {
        if (isMicrophoneMuted == muted) return
        isMicrophoneMuted = muted
        if (muted) {
            audioCaptureEngine.stopCapture()
        } else if (!isSessionPaused && connectionReady && !sessionEnding && sessionError == null) {
            audioCaptureEngine.startCapture()
        }
    }

    /**
     * Ends the current live speaking session cleanly.
     */
    fun endSession() {
        sessionEnding = true
        updateDerivedState()
        sessionJob?.cancel()
        sessionJob = null
        finalizeActiveCoachTurn()
        finalizeActiveUserTurn()
        syncTranscriptState()
        cleanUpSessionResources()
        _voiceState.value = VoiceUiState.ENDED
    }

    /**
     * Sends manual text to the live coach during the session.
     */
    fun sendTextMessage(text: String) {
        if (text.isBlank()) return
        scope.launch {
            liveAiClient.sendTextMessage(text)
            waitingForModel = true
            updateDerivedState()
        }
    }

    private fun resetRuntimeFacts() {
        connectionReady = false
        isSessionPaused = false
        sessionEnding = false
        sessionError = null
        modelGenerationActive = false
        modelPlaybackActive = false
        userSpeechActive = false
        waitingForModel = false
        isMicrophoneMuted = false
    }

    private fun clearTranscriptBuffers() {
        finalizedTranscripts.clear()
        activeCoachBuffer.clear()
        activeCoachTurnId = null
        activeUserInterimText = null
        activeUserTurnId = null
        nextTranscriptTurnId = 1L
        lastFinalizedUserText = null
        userFinalizationBoundaryOpen = false
        coachGenerationComplete = false
        playbackTailDrainRequested = false
        _transcript.value = emptyList()
    }

    /**
     * Deterministically derives public VoiceUiState from independent runtime facts (Requirement 6).
     */
    private fun updateDerivedState() {
        val derived = when {
            sessionEnding -> VoiceUiState.ENDING
            sessionError != null -> VoiceUiState.ERROR
            !connectionReady -> VoiceUiState.CONNECTING
            isSessionPaused -> VoiceUiState.PAUSED
            userSpeechActive -> VoiceUiState.USER_SPEAKING
            modelPlaybackActive || modelGenerationActive -> VoiceUiState.COACH_SPEAKING
            waitingForModel -> VoiceUiState.PROCESSING
            else -> VoiceUiState.LISTENING
        }
        if (_voiceState.value != derived) {
            Log.d(TAG, "VoiceUiState -> $derived (genActive=$modelGenerationActive, pbActive=$modelPlaybackActive, usrActive=$userSpeechActive, waiting=$waitingForModel)")
            _voiceState.value = derived
        }
    }

    private fun cleanUpSessionResources() {
        audioCaptureEngine.stopCapture()
        playbackEngine.stopAndFlush()
        _activeAmplitude.value = 0f

        startupWatchdogJob?.cancel()
        startupWatchdogJob = null

        captureCollectionJob?.cancel()
        captureCollectionJob = null

        playbackStateJob?.cancel()
        playbackStateJob = null

        micAmplitudeJob?.cancel()
        micAmplitudeJob = null

        playbackAmplitudeJob?.cancel()
        playbackAmplitudeJob = null

        eventCollectionJob?.cancel()
        eventCollectionJob = null

        scope.launch {
            try {
                liveAiClient.disconnect()
            } catch (e: Exception) {
                Log.w(TAG, "Error disconnecting live AI client: ${e.message}")
            }
        }
    }

    private fun startStartupResponseWatchdog() {
        startupWatchdogJob?.cancel()
        startupWatchdogJob = scope.launch {
            delay(7000L)
            if (modelGenerationActive && finalizedTranscripts.isEmpty() && activeCoachBuffer.isEmpty()) {
                Log.w(TAG, "Initial startup clientContent was sent successfully, but Gemini produced no serverContent yet. Inspecting server response.")
            }
        }
    }

    private fun observePlaybackLifecycle() {
        playbackStateJob?.cancel()
        playbackStateJob = scope.launch {
            playbackEngine.isPlaying.collect { playing ->
                if (!playing) {
                    modelPlaybackActive = false
                    updateDerivedState()
                } else if (modelGenerationActive) {
                    modelPlaybackActive = true
                    updateDerivedState()
                }
            }
        }
    }

    private fun startListeningToClientEvents() {
        eventCollectionJob?.cancel()
        eventCollectionJob = scope.launch {
            liveAiClient.sessionEvents.collect { event ->
                when (event) {
                    is LiveSessionEvent.Connected -> {
                        connectionReady = true
                        sessionError = null
                        updateDerivedState()
                    }

                    is LiveSessionEvent.InterimInputTranscription -> {
                        startupWatchdogJob?.cancel()
                        if (event.text.isNotBlank()) {
                            finalizeActiveCoachTurn()
                            userSpeechActive = true
                            waitingForModel = false
                            if (activeUserTurnId == null) {
                                activeUserTurnId = newTurnId(TranscriptRole.USER)
                                userFinalizationBoundaryOpen = false
                            }
                            activeUserInterimText = event.text
                            syncTranscriptState()
                            updateDerivedState()
                        }
                    }

                    is LiveSessionEvent.InputTranscription -> {
                        startupWatchdogJob?.cancel()
                        if (event.text.contains("Begin the speaking session now", ignoreCase = true)) {
                            // Internal startup instruction must never appear in learner transcript (Requirement 8)
                            return@collect
                        }
                        finalizeActiveCoachTurn()
                        finalizeUserText(event.text)
                        syncTranscriptState()
                        updateDerivedState()
                    }

                    is LiveSessionEvent.OutputTranscription -> {
                        startupWatchdogJob?.cancel()
                        if (event.text.isNotBlank()) {
                            if (coachGenerationComplete && activeCoachBuffer.isNotEmpty()) {
                                finalizeActiveCoachTurn()
                            }
                            waitingForModel = false
                            modelGenerationActive = true
                            coachGenerationComplete = false
                            userFinalizationBoundaryOpen = false
                            if (activeCoachTurnId == null) {
                                activeCoachTurnId = newTurnId(TranscriptRole.COACH)
                            }
                            val merged = mergeTranscriptText(activeCoachBuffer.toString(), event.text)
                            activeCoachBuffer.clear()
                            activeCoachBuffer.append(merged)
                            syncTranscriptState()
                            updateDerivedState()
                        }
                    }

                    is LiveSessionEvent.AudioOutputChunk -> {
                        startupWatchdogJob?.cancel()
                        if (!isSessionPaused &&
                            _voiceState.value != VoiceUiState.ENDING &&
                            _voiceState.value != VoiceUiState.ENDED &&
                            _voiceState.value != VoiceUiState.ERROR
                        ) {
                            waitingForModel = false
                            modelPlaybackActive = true
                            playbackTailDrainRequested = false
                            playbackEngine.writeChunk(event.pcmData)
                            updateDerivedState()
                        }
                    }

                    is LiveSessionEvent.GenerationComplete -> {
                        startupWatchdogJob?.cancel()
                        modelGenerationActive = false
                        coachGenerationComplete = true
                        requestPlaybackTailDrain()
                        syncTranscriptState()
                        updateDerivedState()
                    }

                    is LiveSessionEvent.TurnComplete -> {
                        startupWatchdogJob?.cancel()
                        modelGenerationActive = false
                        requestPlaybackTailDrain()
                        modelPlaybackActive = playbackEngine.isPlaying.value
                        finalizeActiveCoachTurn()
                        coachGenerationComplete = false
                        userFinalizationBoundaryOpen = false
                        syncTranscriptState()
                        updateDerivedState()
                    }

                    is LiveSessionEvent.Interrupted, is LiveSessionEvent.InterruptedByBargeIn -> {
                        startupWatchdogJob?.cancel()
                        val interruptionHasWork = activeCoachTurnId != null ||
                            modelGenerationActive || modelPlaybackActive || playbackEngine.isPlaying.value
                        if (interruptionHasWork) {
                            Log.i(TAG, "Authoritative server interruption received -> flushing playback")
                            playbackEngine.stopAndFlush()
                        }
                        modelPlaybackActive = false
                        modelGenerationActive = false
                        playbackTailDrainRequested = false
                        coachGenerationComplete = false
                        userSpeechActive = true
                        waitingForModel = false
                        finalizeActiveCoachTurn()
                        syncTranscriptState()
                        updateDerivedState()
                    }

                    is LiveSessionEvent.WaitingForInput -> {
                        modelGenerationActive = false
                        if (!playbackEngine.isPlaying.value) {
                            modelPlaybackActive = false
                        }
                        updateDerivedState()
                    }

                    is LiveSessionEvent.TranscriptDelta -> {
                        startupWatchdogJob?.cancel()
                        handleLegacyTranscriptDelta(event.role, event.text, event.isFinal)
                    }

                    is LiveSessionEvent.InteractionStatus -> {
                        Log.d(TAG, "InteractionStatus: ${event.status}")
                    }

                    is LiveSessionEvent.SessionResumptionUpdate -> {
                        Log.d(TAG, "SessionResumptionUpdate received")
                    }

                    is LiveSessionEvent.Error -> {
                        startupWatchdogJob?.cancel()
                        if (event.isFatal) {
                            sessionError = event.message
                            _errorMessage.value = event.message
                            cleanUpSessionResources()
                            updateDerivedState()
                        }
                    }

                    is LiveSessionEvent.Disconnected -> {
                        startupWatchdogJob?.cancel()
                        if (_voiceState.value != VoiceUiState.ENDED && _voiceState.value != VoiceUiState.ERROR) {
                            cleanUpSessionResources()
                            _voiceState.value = VoiceUiState.ENDED
                        }
                    }
                }
            }
        }
    }

    /**
     * Audio piping without local amplitude-based conversation control (Requirement 2 & Requirement 7).
     * Microphone is kept live for natural server-side VAD barge-in.
     * Amplitude is used exclusively for waveform visualization.
     */
    private fun startAudioPiping() {
        captureCollectionJob?.cancel()
        captureCollectionJob = scope.launch {
            audioCaptureEngine.audioChunks.collect { chunk ->
                if (!isSessionPaused &&
                    _voiceState.value != VoiceUiState.ENDED &&
                    _voiceState.value != VoiceUiState.ERROR
                ) {
                    liveAiClient.sendAudioChunk(chunk.data)
                }
            }
        }

        micAmplitudeJob?.cancel()
        micAmplitudeJob = scope.launch {
            audioCaptureEngine.amplitudeFlow.collect { amp ->
                if (_voiceState.value == VoiceUiState.USER_SPEAKING || _voiceState.value == VoiceUiState.LISTENING) {
                    _activeAmplitude.value = amp
                }
            }
        }

        playbackAmplitudeJob?.cancel()
        playbackAmplitudeJob = scope.launch {
            playbackEngine.amplitudeFlow.collect { amp ->
                if (_voiceState.value == VoiceUiState.COACH_SPEAKING) {
                    _activeAmplitude.value = amp
                }
            }
        }
    }

    private fun finalizeActiveCoachTurn() {
        val text = activeCoachBuffer.toString().trim()
        if (text.isNotEmpty() && activeCoachTurnId != null) {
            finalizedTranscripts.add(
                TranscriptEntry(
                    id = activeCoachTurnId!!,
                    role = TranscriptRole.COACH,
                    text = text,
                    isFinal = true
                )
            )
            activeCoachBuffer.clear()
            activeCoachTurnId = null
        }
    }

    private fun finalizeActiveUserTurn() {
        val text = activeUserInterimText?.trim().orEmpty()
        if (text.isNotEmpty()) {
            finalizeUserText(text)
        }
    }

    private fun finalizeUserText(rawText: String) {
        val text = rawText.trim()
        if (text.isEmpty()) return
        if (activeUserTurnId == null && userFinalizationBoundaryOpen && lastFinalizedUserText == text) {
            return
        }
        userSpeechActive = false
        waitingForModel = true
        val turnId = activeUserTurnId ?: newTurnId(TranscriptRole.USER)
        finalizedTranscripts.add(
            TranscriptEntry(
                id = turnId,
                role = TranscriptRole.USER,
                text = text,
                isFinal = true
            )
        )
        lastFinalizedUserText = text
        userFinalizationBoundaryOpen = true
        activeUserInterimText = null
        activeUserTurnId = null
    }

    private fun requestPlaybackTailDrain() {
        if (!playbackTailDrainRequested) {
            playbackEngine.finishTurn()
            playbackTailDrainRequested = true
        }
    }

    private fun newTurnId(role: TranscriptRole): String =
        "${role.name.lowercase()}-${nextTranscriptTurnId++}"

    private fun syncTranscriptState() {
        val list = ArrayList<TranscriptEntry>(finalizedTranscripts.size + 2)
        list.addAll(finalizedTranscripts)

        val coachText = activeCoachBuffer.toString().trim()
        if (coachText.isNotEmpty() && activeCoachTurnId != null) {
            list.add(
                TranscriptEntry(
                    id = activeCoachTurnId!!,
                    role = TranscriptRole.COACH,
                    text = coachText,
                    isFinal = false
                )
            )
        }

        val userInterim = activeUserInterimText?.trim()
        if (!userInterim.isNullOrEmpty()) {
            list.add(
                TranscriptEntry(
                    id = activeUserTurnId ?: "interim-user",
                    role = TranscriptRole.USER,
                    text = userInterim,
                    isFinal = false
                )
            )
        }

        _transcript.value = list
    }

    private fun handleLegacyTranscriptDelta(role: TranscriptRole, text: String, isFinal: Boolean) {
        if (text.contains("Begin the speaking session now", ignoreCase = true)) {
            return
        }

        when (role) {
            TranscriptRole.USER -> {
                if (isFinal) {
                    finalizeActiveCoachTurn()
                    finalizeUserText(text)
                } else {
                    finalizeActiveCoachTurn()
                    userSpeechActive = true
                    if (activeUserTurnId == null) {
                        activeUserTurnId = newTurnId(TranscriptRole.USER)
                        userFinalizationBoundaryOpen = false
                    }
                    activeUserInterimText = text
                }
            }

            TranscriptRole.COACH -> {
                waitingForModel = false
                modelGenerationActive = !isFinal
                userFinalizationBoundaryOpen = false
                if (activeCoachTurnId == null) {
                    activeCoachTurnId = newTurnId(TranscriptRole.COACH)
                }
                val merged = mergeTranscriptText(activeCoachBuffer.toString(), text)
                activeCoachBuffer.clear()
                activeCoachBuffer.append(merged)
                if (isFinal) {
                    finalizeActiveCoachTurn()
                }
            }

            TranscriptRole.SYSTEM -> {}
        }

        syncTranscriptState()
        updateDerivedState()
    }
}

internal fun mergeTranscriptText(current: String, incoming: String): String {
    if (current.isEmpty()) return incoming
    if (incoming.isEmpty()) return current
    if (incoming.startsWith(current)) return incoming
    if (current.startsWith(incoming)) return current

    val maxOverlap = minOf(current.length, incoming.length)
    for (length in maxOverlap downTo 1) {
        if (current.regionMatches(current.length - length, incoming, 0, length)) {
            return current + incoming.substring(length)
        }
    }

    return when {
        current.last().isWhitespace() && incoming.first().isWhitespace() ->
            current.trimEnd() + " " + incoming.trimStart()
        else -> current + incoming
    }
}
