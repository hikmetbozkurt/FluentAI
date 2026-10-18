package com.seanora.fluentai.feature.speaking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanora.fluentai.ai.analysis.SpeakingAnalysisClient
import com.seanora.fluentai.ai.live.LiveCoachSessionManager
import com.seanora.fluentai.ai.live.LiveScenarioConfig
import com.seanora.fluentai.ai.live.TranscriptRole
import com.seanora.fluentai.ai.live.VoiceUiState
import com.seanora.fluentai.ai.live.SpeakingPersona
import com.seanora.fluentai.ai.live.SpeakingSessionType
import com.seanora.fluentai.core.model.CorrectionLevel
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.SpeakingCategory
import com.seanora.fluentai.core.model.SpeakingScenario
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.data.repository.SpeakingRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.domain.speaking.LiveCorrectionMerger
import com.seanora.fluentai.domain.speaking.ProcessSpeakingAnalysisUseCase
import com.seanora.fluentai.domain.speaking.SpeakingPromptBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class SpeakingViewModel @Inject constructor(
    private val sessionManager: LiveCoachSessionManager,
    private val speakingRepository: SpeakingRepository,
    private val promptBuilder: SpeakingPromptBuilder,
    private val analysisClient: SpeakingAnalysisClient,
    private val processSpeakingAnalysisUseCase: ProcessSpeakingAnalysisUseCase,
    private val userLearningRepository: UserLearningRepository,
    private val liveCorrectionMerger: LiveCorrectionMerger
) : ViewModel() {

    private val _uiState = MutableStateFlow(SpeakingUiState())
    val uiState: StateFlow<SpeakingUiState> = _uiState.asStateFlow()

    private var sessionStartTimeMs: Long = 0L
    private var requestedScenarioId: String? = null
    private var cachedUserProfile: UserProfile? = null
    private var cachedRecentMistakes: List<MistakeRecord> = emptyList()

    init {
        observeSessionManager()
        loadScenarios()
        loadUserLearningContext()
    }

    private fun loadScenarios() {
        viewModelScope.launch {
            speakingRepository.getAllScenarios()
                .catch {
                    _uiState.update { it.copy(errorMessage = "Content could not be loaded. Please try again.") }
                }
                .collect { scenarios ->
                    _uiState.update { current ->
                        val selected = requestedScenarioId
                            ?.let { id -> scenarios.firstOrNull { it.id == id } }
                            ?: current.selectedScenario
                            ?: scenarios.firstOrNull()
                        current.copy(availableScenarios = scenarios, selectedScenario = selected)
                    }
                }
        }
    }

    private fun loadUserLearningContext() {
        viewModelScope.launch {
            userLearningRepository.getUserProfile().collect { profile ->
                cachedUserProfile = profile
            }
        }
        viewModelScope.launch {
            userLearningRepository.getActiveMistakes().collect { mistakes ->
                cachedRecentMistakes = mistakes
            }
        }
    }

    private fun observeSessionManager() {
        viewModelScope.launch {
            sessionManager.voiceState.collect { state ->
                _uiState.update { current ->
                    current.copy(
                        voiceState = state,
                        isSessionActive = (state != VoiceUiState.IDLE && state != VoiceUiState.ENDED && state != VoiceUiState.ERROR)
                    )
                }
            }
        }

        viewModelScope.launch {
            sessionManager.transcript.collect { transcript ->
                _uiState.update { it.copy(transcript = transcript) }
            }
        }

        viewModelScope.launch {
            sessionManager.corrections.collect { corrections ->
                _uiState.update { it.copy(liveCorrections = corrections) }
            }
        }

        viewModelScope.launch {
            sessionManager.activeAmplitude.collect { amplitude ->
                _uiState.update { it.copy(amplitude = amplitude) }
            }
        }

        viewModelScope.launch {
            sessionManager.errorMessage.collect { error ->
                _uiState.update { current ->
                    current.copy(
                        errorMessage = error,
                        isPermissionError = error?.contains("permission", ignoreCase = true) == true || (current.isPermissionError && error != null)
                    )
                }
            }
        }
    }

    fun selectCategory(category: SpeakingCategory?) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun selectCefrLevel(cefrLevel: String?) {
        _uiState.update { it.copy(selectedCefrLevel = cefrLevel) }
    }

    fun selectScenario(scenario: SpeakingScenario) {
        _uiState.update { it.copy(selectedScenario = scenario) }
    }

    fun selectScenarioById(contentId: String) {
        requestedScenarioId = contentId
        _uiState.value.availableScenarios
            .firstOrNull { it.id == contentId }
            ?.let(::selectScenario)
    }

    fun selectFreeTalkCorrectionLevel(level: CorrectionLevel) {
        _uiState.update { it.copy(freeTalkCorrectionLevel = level) }
    }

    fun startSession(scenario: SpeakingScenario? = null) {
        if (_uiState.value.isSessionActive || _uiState.value.voiceState == VoiceUiState.CONNECTING) {
            return
        }

        val targetScenario = scenario ?: _uiState.value.selectedScenario ?: _uiState.value.availableScenarios.firstOrNull()
        if (targetScenario == null) {
            _uiState.update { it.copy(errorMessage = "No scenario available to start speaking.") }
            return
        }

        val liveConfig = promptBuilder.createLiveConfig(
            scenario = targetScenario,
            profile = cachedUserProfile,
            recentMistakes = cachedRecentMistakes
        )

        sessionStartTimeMs = System.currentTimeMillis()

        _uiState.update {
            it.copy(
                selectedScenario = targetScenario,
                activeLiveConfig = liveConfig,
                voiceState = VoiceUiState.CONNECTING,
                isSessionActive = true,
                isReportVisible = false,
                postSessionAnalysis = null,
                analysisResult = null,
                errorMessage = null,
                isPermissionError = false
            )
        }

        sessionManager.startSession(liveConfig)
    }

    fun startSession(config: LiveScenarioConfig) {
        if (_uiState.value.isSessionActive || _uiState.value.voiceState == VoiceUiState.CONNECTING) {
            return
        }

        sessionStartTimeMs = System.currentTimeMillis()

        _uiState.update {
            it.copy(
                activeLiveConfig = config,
                voiceState = VoiceUiState.CONNECTING,
                isSessionActive = true,
                isReportVisible = false,
                postSessionAnalysis = null,
                analysisResult = null,
                errorMessage = null,
                isPermissionError = false
            )
        }

        sessionManager.startSession(config)
    }

    fun startGaladrielSession() {
        val config = promptBuilder.createGaladrielLiveConfig(
            profile = cachedUserProfile,
            recentMistakes = cachedRecentMistakes,
            memoryContext = null,
            correctionLevel = _uiState.value.freeTalkCorrectionLevel
        )
        _uiState.update { it.copy(selectedScenario = null) }
        startSession(config)
    }

    fun endSession() {
        val durationSeconds = max(5, ((System.currentTimeMillis() - sessionStartTimeMs) / 1000).toInt())
        sessionManager.endSession()
        val currentTranscript = sessionManager.transcript.value
        val liveCorrections = sessionManager.corrections.value

        val activeConfig = _uiState.value.activeLiveConfig
        val scenario = activeConfig
            ?.takeIf {
                it.sessionType == SpeakingSessionType.FREE_TALK &&
                    it.persona == SpeakingPersona.GALADRIEL
            }
            ?.let { promptBuilder.createGaladrielAnalysisDescriptor(it.cefrTarget) }
            ?: _uiState.value.selectedScenario
        val hasLearnerSpeech = currentTranscript.any {
            it.role == TranscriptRole.USER && it.text.isNotBlank()
        }

        _uiState.update {
            it.copy(
                isSessionActive = false,
                isReportVisible = true,
                isAnalyzing = hasLearnerSpeech,
                errorMessage = if (hasLearnerSpeech) null else "No learner speech was captured, so no analysis was generated."
            )
        }

        if (hasLearnerSpeech && scenario != null) {
            viewModelScope.launch {
                val outcome = runCatching {
                    val modelAnalysis = analysisClient.analyzeSession(
                        scenario = scenario,
                        transcript = currentTranscript,
                        durationSeconds = durationSeconds
                    ).getOrThrow()
                    val analysis = liveCorrectionMerger.merge(modelAnalysis, liveCorrections)
                    analysis to processSpeakingAnalysisUseCase(analysis)
                }
                _uiState.update { current ->
                    outcome.fold(
                        onSuccess = { (analysis, processed) ->
                            current.copy(isAnalyzing = false, postSessionAnalysis = analysis, analysisResult = processed)
                        },
                        onFailure = { error ->
                            current.copy(isAnalyzing = false, errorMessage = "Analysis error: ${error.message}")
                        }
                    )
                }
            }
        } else {
            _uiState.update { it.copy(isAnalyzing = false) }
        }
    }

    fun dismissReport() {
        _uiState.update { it.copy(isReportVisible = false) }
    }

    fun onPermissionDenied(permanentlyDenied: Boolean) {
        _uiState.update { current ->
            current.copy(
                isPermissionError = true,
                errorMessage = if (permanentlyDenied) {
                    "Microphone permission is permanently denied. Please enable it in Android Settings to use Live Speaking Coach."
                } else {
                    "Microphone permission is required to start live speaking."
                }
            )
        }
    }

    fun clearPermissionError() {
        _uiState.update { current ->
            if (current.isPermissionError) {
                current.copy(errorMessage = null, isPermissionError = false)
            } else {
                current
            }
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null, isPermissionError = false) }
    }

    fun interruptCoach() {
        sessionManager.interruptCoach()
    }

    fun togglePause() {
        if (_uiState.value.voiceState == VoiceUiState.PAUSED) {
            sessionManager.resumeSession()
        } else {
            sessionManager.pauseSession()
        }
    }

    fun toggleMute() {
        val muted = !_uiState.value.isMuted
        sessionManager.setMicrophoneMuted(muted)
        _uiState.update { it.copy(isMuted = muted) }
    }

    fun sendTextMessage(text: String) {
        sessionManager.sendTextMessage(text)
    }

    fun onScreenHidden() {
        if (_uiState.value.isSessionActive && _uiState.value.voiceState != VoiceUiState.PAUSED) {
            sessionManager.pauseSession()
        }
    }

    override fun onCleared() {
        if (sessionManager.voiceState.value !in VoiceUiState.restartableStates) {
            sessionManager.endSession()
        }
        super.onCleared()
    }
}
