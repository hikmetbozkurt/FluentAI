package com.seanora.fluentai.ai.live

import com.seanora.fluentai.core.audio.AudioCaptureEngine
import com.seanora.fluentai.core.audio.StreamAudioPlaybackEngine
import com.seanora.fluentai.core.common.di.DefaultDispatcher
import com.seanora.fluentai.core.common.logging.AppLog
import com.seanora.fluentai.core.model.LiveCorrection
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LiveCoachSessionManager @Inject constructor(
    private val audioCaptureEngine: AudioCaptureEngine,
    private val playbackEngine: StreamAudioPlaybackEngine,
    private val liveAiClient: LiveAiClient,
    @DefaultDispatcher dispatcher: CoroutineDispatcher
) {
    companion object {
        private const val TAG = "LiveCoachSession"
        const val STARTUP_INSTRUCTION = "Begin the speaking session now. Greet the learner briefly, remind them in one sentence that you will correct their English as you talk, and start the conversation with an appropriate question. From now on follow your coaching protocol on every learner turn."
        private const val STARTUP_INSTRUCTION_MARKER = "Begin the speaking session now"
    }

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)
    private val stateLock = Any()
    private val runtime = SessionRuntimeFacts()
    private val transcriptBuffer = LiveTranscriptBuffer()

    private val _voiceState = MutableStateFlow(VoiceUiState.IDLE)
    val voiceState: StateFlow<VoiceUiState> = _voiceState.asStateFlow()

    private val _transcript = MutableStateFlow<List<TranscriptEntry>>(emptyList())
    val transcript: StateFlow<List<TranscriptEntry>> = _transcript.asStateFlow()

    private val _activeAmplitude = MutableStateFlow(0f)
    val activeAmplitude: StateFlow<Float> = _activeAmplitude.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _corrections = MutableStateFlow<List<LiveCorrection>>(emptyList())
    val corrections: StateFlow<List<LiveCorrection>> = _corrections.asStateFlow()

    private val _metrics = MutableStateFlow(LiveSessionMetrics())
    val metrics: StateFlow<LiveSessionMetrics> = _metrics.asStateFlow()

    private var sessionJob: Job? = null
    private val sessionScopedJobs = mutableListOf<Job>()

    var currentScenario: LiveScenarioConfig? = null
        private set

    fun startSession(config: LiveScenarioConfig) {
        synchronized(stateLock) {
            if (_voiceState.value !in VoiceUiState.restartableStates) return
            currentScenario = config
            runtime.reset()
            transcriptBuffer.clear()
            _transcript.value = emptyList()
            _corrections.value = emptyList()
            _errorMessage.value = null
            _metrics.value = LiveSessionMetrics(startedAtMs = System.currentTimeMillis())
            _voiceState.value = VoiceUiState.CONNECTING
            sessionJob?.cancel()
            sessionJob = scope.launch { runSession(config) }
        }
    }

    fun interruptCoach() {
        synchronized(stateLock) {
            if (_voiceState.value in VoiceUiState.inactiveStates) return
            AppLog.i(TAG) { "Learner interrupted the coach" }
            playbackEngine.stopAndFlush()
            runtime.stopCoachOutput()
            runtime.suppressCoachAudio = true
            transcriptBuffer.finalizeCoachTurn()
            _metrics.update { it.copy(interruptions = it.interruptions + 1) }
            publishTranscript()
            publishVoiceState()
        }
        scope.launch { liveAiClient.interruptModelTurn() }
    }

    fun pauseSession() {
        synchronized(stateLock) {
            if (_voiceState.value !in VoiceUiState.pausableStates) return
            runtime.isPaused = true
            audioCaptureEngine.stopCapture()
            playbackEngine.stopAndFlush()
            runtime.modelPlaybackActive = false
            publishVoiceState()
        }
        scope.launch { liveAiClient.sendAudioStreamEnd() }
    }

    fun resumeSession() {
        synchronized(stateLock) {
            if (!runtime.isPaused) return
            runtime.isPaused = false
            if (!runtime.isMicrophoneMuted) audioCaptureEngine.startCapture()
            publishVoiceState()
        }
    }

    fun setMicrophoneMuted(muted: Boolean) {
        synchronized(stateLock) {
            if (runtime.isMicrophoneMuted == muted) return
            runtime.isMicrophoneMuted = muted
            if (muted) {
                audioCaptureEngine.stopCapture()
            } else if (runtime.canCaptureAudio()) {
                audioCaptureEngine.startCapture()
            }
        }
        if (muted) scope.launch { liveAiClient.sendAudioStreamEnd() }
    }

    fun endSession() {
        synchronized(stateLock) {
            runtime.isEnding = true
            publishVoiceState()
            sessionJob?.cancel()
            sessionJob = null
            transcriptBuffer.finalizeCoachTurn()
            transcriptBuffer.finalizePendingLearnerTurn()
            publishTranscript()
            releaseSessionResources()
            _voiceState.value = VoiceUiState.ENDED
        }
    }

    fun sendTextMessage(text: String) {
        if (text.isBlank()) return
        scope.launch {
            liveAiClient.sendTextMessage(text)
            synchronized(stateLock) {
                runtime.waitingForModel = true
                publishVoiceState()
            }
        }
    }

    private fun recordCorrection(correction: LiveCorrection) {
        val isDuplicate = _corrections.value.any {
            it.learnerSaid.equals(correction.learnerSaid, ignoreCase = true) &&
                it.correction.equals(correction.correction, ignoreCase = true)
        }
        if (isDuplicate) return
        _corrections.update { it + correction }
        _metrics.update { it.copy(corrections = it.corrections + 1) }
    }

    private suspend fun runSession(config: LiveScenarioConfig) {
        launchSessionScoped { collectClientEvents() }
        launchSessionScoped { observePlayback() }

        val connectResult = liveAiClient.connect(config)
        if (connectResult.isFailure) {
            failSession(connectResult.exceptionOrNull()?.message ?: "Failed to connect to Live Coach")
            return
        }

        synchronized(stateLock) {
            runtime.connectionReady = true
            publishVoiceState()
        }

        startAudioPiping()

        val captureResult = audioCaptureEngine.startCapture()
        if (captureResult.isFailure) {
            failSession("Failed to start microphone: ${captureResult.exceptionOrNull()?.message}")
            return
        }

        synchronized(stateLock) {
            runtime.modelGenerationActive = true
            runtime.coachTurnRequestedAtMs = System.currentTimeMillis()
            publishVoiceState()
        }
        liveAiClient.sendTextMessage(STARTUP_INSTRUCTION)
    }

    private fun failSession(message: String) {
        synchronized(stateLock) {
            runtime.error = message
            _errorMessage.value = message
            releaseSessionResources()
            publishVoiceState()
        }
    }

    private fun launchSessionScoped(block: suspend () -> Unit) {
        synchronized(stateLock) { sessionScopedJobs += scope.launch { block() } }
    }

    private fun releaseSessionResources() {
        audioCaptureEngine.stopCapture()
        playbackEngine.stopAndFlush()
        _activeAmplitude.value = 0f
        sessionScopedJobs.forEach { it.cancel() }
        sessionScopedJobs.clear()
        scope.launch {
            runCatching { liveAiClient.disconnect() }
                .onFailure { AppLog.w(TAG, it) { "Disconnect failed" } }
        }
    }

    private suspend fun observePlayback() {
        playbackEngine.isPlaying.collect { playing ->
            synchronized(stateLock) {
                if (!playing) {
                    runtime.modelPlaybackActive = false
                } else if (runtime.modelGenerationActive) {
                    runtime.modelPlaybackActive = true
                }
                publishVoiceState()
            }
        }
    }

    private fun startAudioPiping() {
        launchSessionScoped {
            audioCaptureEngine.audioChunks.collect { chunk ->
                val shouldSend = synchronized(stateLock) {
                    !runtime.isPaused && _voiceState.value != VoiceUiState.ENDED && _voiceState.value != VoiceUiState.ERROR
                }
                if (shouldSend) liveAiClient.sendAudioChunk(chunk.data)
            }
        }
        launchSessionScoped {
            audioCaptureEngine.amplitudeFlow.collect { amplitude ->
                if (_voiceState.value == VoiceUiState.USER_SPEAKING || _voiceState.value == VoiceUiState.LISTENING) {
                    _activeAmplitude.value = amplitude
                }
            }
        }
        launchSessionScoped {
            playbackEngine.amplitudeFlow.collect { amplitude ->
                if (_voiceState.value == VoiceUiState.COACH_SPEAKING) _activeAmplitude.value = amplitude
            }
        }
    }

    private suspend fun collectClientEvents() {
        liveAiClient.sessionEvents.collect { event ->
            synchronized(stateLock) { handleEvent(event) }
        }
    }

    private fun handleEvent(event: LiveSessionEvent) {
        when (event) {
            is LiveSessionEvent.Connected -> {
                runtime.connectionReady = true
                runtime.isReconnecting = false
                runtime.error = null
            }
            is LiveSessionEvent.Reconnecting -> {
                runtime.connectionReady = false
                runtime.isReconnecting = true
                playbackEngine.stopAndFlush()
                _metrics.update { it.copy(reconnects = it.reconnects + 1) }
            }
            is LiveSessionEvent.InterimInputTranscription -> onInterimLearnerText(event.text)
            is LiveSessionEvent.InputTranscription -> onFinalLearnerText(event.text)
            is LiveSessionEvent.OutputTranscription -> onCoachText(event.text)
            is LiveSessionEvent.AudioOutputChunk -> onCoachAudio(event.pcmData)
            LiveSessionEvent.GenerationComplete -> {
                runtime.modelGenerationActive = false
                transcriptBuffer.markCoachGenerationComplete()
                requestPlaybackTailDrain()
            }
            LiveSessionEvent.TurnComplete -> onCoachTurnComplete()
            LiveSessionEvent.Interrupted, LiveSessionEvent.InterruptedByBargeIn -> onServerInterruption()
            LiveSessionEvent.WaitingForInput -> {
                runtime.modelGenerationActive = false
                if (!playbackEngine.isPlaying.value) runtime.modelPlaybackActive = false
            }
            is LiveSessionEvent.TranscriptDelta -> onTranscriptDelta(event)
            is LiveSessionEvent.UsageUpdated -> _metrics.update { it.copy(tokenUsage = event.usage) }
            is LiveSessionEvent.CorrectionLogged -> recordCorrection(event.correction)
            is LiveSessionEvent.InteractionStatus, LiveSessionEvent.SessionResumptionUpdate -> Unit
            is LiveSessionEvent.Error -> if (event.isFatal) {
                runtime.error = event.message
                _errorMessage.value = event.message
                releaseSessionResources()
            }
            is LiveSessionEvent.Disconnected -> if (_voiceState.value != VoiceUiState.ENDED && _voiceState.value != VoiceUiState.ERROR) {
                releaseSessionResources()
                _voiceState.value = VoiceUiState.ENDED
                return
            }
        }
        publishTranscript()
        publishVoiceState()
    }

    private fun onInterimLearnerText(text: String) {
        if (text.isBlank()) return
        transcriptBuffer.finalizeCoachTurn()
        runtime.userSpeechActive = true
        runtime.waitingForModel = false
        runtime.suppressCoachAudio = false
        transcriptBuffer.updateLearnerInterim(text)
    }

    private fun onFinalLearnerText(text: String) {
        if (text.contains(STARTUP_INSTRUCTION_MARKER, ignoreCase = true)) return
        transcriptBuffer.finalizeCoachTurn()
        if (transcriptBuffer.finalizeLearnerText(text)) {
            runtime.userSpeechActive = false
            runtime.waitingForModel = true
            runtime.coachTurnRequestedAtMs = System.currentTimeMillis()
            _metrics.update { it.copy(learnerTurns = it.learnerTurns + 1) }
        }
    }

    private fun onCoachText(text: String) {
        if (text.isBlank() || runtime.suppressCoachAudio) return
        runtime.waitingForModel = false
        runtime.modelGenerationActive = true
        transcriptBuffer.appendCoachText(text)
    }

    private fun onCoachAudio(pcmData: ByteArray) {
        if (runtime.isPaused || runtime.suppressCoachAudio || _voiceState.value in VoiceUiState.inactiveStates) return
        recordFirstAudioLatency()
        runtime.waitingForModel = false
        runtime.modelPlaybackActive = true
        runtime.playbackTailDrainRequested = false
        playbackEngine.writeChunk(pcmData)
    }

    private fun onCoachTurnComplete() {
        runtime.modelGenerationActive = false
        runtime.suppressCoachAudio = false
        requestPlaybackTailDrain()
        runtime.modelPlaybackActive = playbackEngine.isPlaying.value
        if (transcriptBuffer.finalizeCoachTurn()) {
            _metrics.update { it.copy(coachTurns = it.coachTurns + 1) }
        }
        transcriptBuffer.closeTurnBoundary()
    }

    private fun onServerInterruption() {
        if (runtime.hasCoachOutput() || playbackEngine.isPlaying.value || transcriptBuffer.hasActiveCoachTurn()) {
            playbackEngine.stopAndFlush()
        }
        runtime.stopCoachOutput()
        runtime.suppressCoachAudio = false
        transcriptBuffer.finalizeCoachTurn()
    }

    private fun onTranscriptDelta(event: LiveSessionEvent.TranscriptDelta) {
        if (event.text.contains(STARTUP_INSTRUCTION_MARKER, ignoreCase = true)) return
        when (event.role) {
            TranscriptRole.USER -> if (event.isFinal) {
                onFinalLearnerText(event.text)
            } else {
                onInterimLearnerText(event.text)
            }
            TranscriptRole.COACH -> {
                runtime.waitingForModel = false
                runtime.modelGenerationActive = !event.isFinal
                transcriptBuffer.appendCoachText(event.text)
                if (event.isFinal && transcriptBuffer.finalizeCoachTurn()) {
                    _metrics.update { it.copy(coachTurns = it.coachTurns + 1) }
                }
            }
            TranscriptRole.SYSTEM -> Unit
        }
    }

    private fun recordFirstAudioLatency() {
        val requestedAt = runtime.coachTurnRequestedAtMs ?: return
        if (_metrics.value.firstCoachAudioLatencyMs == null) {
            _metrics.update { it.copy(firstCoachAudioLatencyMs = System.currentTimeMillis() - requestedAt) }
        }
        runtime.coachTurnRequestedAtMs = null
    }

    private fun requestPlaybackTailDrain() {
        if (!runtime.playbackTailDrainRequested) {
            playbackEngine.finishTurn()
            runtime.playbackTailDrainRequested = true
        }
    }

    private fun publishTranscript() {
        _transcript.value = transcriptBuffer.snapshot()
    }

    private fun publishVoiceState() {
        val derived = runtime.deriveVoiceState()
        if (_voiceState.value != derived) {
            AppLog.d(TAG) { "VoiceUiState -> $derived" }
            _voiceState.value = derived
        }
    }
}
