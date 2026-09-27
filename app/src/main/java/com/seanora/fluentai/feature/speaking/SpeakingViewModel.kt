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
import com.seanora.fluentai.core.model.CorrectionMode
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.SpeakingCategory
import com.seanora.fluentai.core.model.SpeakingScenario
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.data.repository.SpeakingRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.data.user.dao.SpeakingSessionDao
import com.seanora.fluentai.data.user.entity.SpeakingSessionRecordEntity
import com.seanora.fluentai.domain.mastery.MasteryEngine
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import com.seanora.fluentai.domain.mistakes.MistakeEngine
import com.seanora.fluentai.domain.speaking.ProcessSpeakingAnalysisUseCase
import com.seanora.fluentai.domain.speaking.SpeakingPromptBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
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
    private val userLearningRepository: UserLearningRepository
) : ViewModel() {

    constructor(sessionManager: LiveCoachSessionManager) : this(
        sessionManager = sessionManager,
        speakingRepository = EmptySpeakingRepository(),
        promptBuilder = SpeakingPromptBuilder(),
        analysisClient = com.seanora.fluentai.ai.analysis.MockSpeakingAnalysisClient(),
        processSpeakingAnalysisUseCase = ProcessSpeakingAnalysisUseCase(
            RecordLearningEvidenceUseCase(
                EmptyUserLearningRepository(),
                MasteryEngine(),
                MistakeEngine()
            ),
            EmptySpeakingSessionDao()
        ),
        userLearningRepository = EmptyUserLearningRepository()
    )

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
                .catch { e ->
                    android.util.Log.e("SpeakingViewModel", "Failed to load speaking scenarios", e)
                    _uiState.update { it.copy(errorMessage = "Content could not be loaded. Please try again.") }
                }
                .collect { scenarios ->
                    _uiState.update { current ->
                        val selected = requestedScenarioId
                            ?.let { id -> scenarios.firstOrNull { it.id == id } }
                            ?: current.selectedScenario
                            ?: scenarios.firstOrNull()
                        current.copy(
                            availableScenarios = scenarios,
                            selectedScenario = selected,
                            selectedMode = selected?.recommendedCorrectionMode ?: current.selectedMode
                        )
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
        _uiState.update {
            it.copy(
                selectedScenario = scenario,
                selectedMode = scenario.recommendedCorrectionMode
            )
        }
    }

    fun selectScenarioById(contentId: String) {
        requestedScenarioId = contentId
        _uiState.value.availableScenarios
            .firstOrNull { it.id == contentId }
            ?.let(::selectScenario)
    }

    fun selectCorrectionMode(mode: CorrectionMode) {
        _uiState.update { it.copy(selectedMode = mode) }
    }

    fun startSession(scenario: SpeakingScenario? = null, mode: CorrectionMode? = null) {
        if (_uiState.value.isSessionActive || _uiState.value.voiceState == VoiceUiState.CONNECTING) {
            return
        }

        val targetScenario = scenario ?: _uiState.value.selectedScenario ?: _uiState.value.availableScenarios.firstOrNull()
        if (targetScenario == null) {
            _uiState.update { it.copy(errorMessage = "No scenario available to start speaking.") }
            return
        }

        val targetMode = mode ?: _uiState.value.selectedMode
        val liveConfig = promptBuilder.createLiveConfig(
            scenario = targetScenario,
            mode = targetMode,
            profile = cachedUserProfile,
            recentMistakes = cachedRecentMistakes
        )

        sessionStartTimeMs = System.currentTimeMillis()

        _uiState.update {
            it.copy(
                selectedScenario = targetScenario,
                selectedMode = targetMode,
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
            memoryContext = null
        )
        _uiState.update {
            it.copy(
                selectedScenario = null,
                selectedMode = CorrectionMode.FLOW
            )
        }
        startSession(config)
    }

    fun endSession() {
        val durationSeconds = max(5, ((System.currentTimeMillis() - sessionStartTimeMs) / 1000).toInt())
        sessionManager.endSession()
        val currentTranscript = sessionManager.transcript.value

        val activeConfig = _uiState.value.activeLiveConfig
        val scenario = activeConfig
            ?.takeIf {
                it.sessionType == SpeakingSessionType.FREE_TALK &&
                    it.persona == SpeakingPersona.GALADRIEL
            }
            ?.let { promptBuilder.createGaladrielAnalysisDescriptor(it.cefrTarget) }
            ?: _uiState.value.selectedScenario
        val mode = activeConfig?.correctionMode ?: _uiState.value.selectedMode
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
                val analysisResult = analysisClient.analyzeSession(
                    scenario = scenario,
                    mode = mode,
                    transcript = currentTranscript,
                    durationSeconds = durationSeconds
                )

                if (analysisResult.isSuccess) {
                    val analysis = analysisResult.getOrThrow()
                    val processedResult = processSpeakingAnalysisUseCase(analysis)
                    _uiState.update {
                        it.copy(
                            isAnalyzing = false,
                            postSessionAnalysis = analysis,
                            analysisResult = processedResult
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isAnalyzing = false,
                            errorMessage = "Analysis error: ${analysisResult.exceptionOrNull()?.message}"
                        )
                    }
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
        sessionManager.interruptCoach("User tapped interrupt")
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
}

private class EmptySpeakingRepository : SpeakingRepository {
    override fun getAllScenarios(): Flow<List<SpeakingScenario>> = flowOf(emptyList())
    override fun getScenariosByLevel(level: String): Flow<List<SpeakingScenario>> = flowOf(emptyList())
    override fun getScenariosByCategory(category: SpeakingCategory): Flow<List<SpeakingScenario>> = flowOf(emptyList())
    override fun getScenarioById(id: String): Flow<SpeakingScenario?> = flowOf(null)
    override suspend fun getScenarioByIdSync(id: String): SpeakingScenario? = null
    override fun searchScenarios(query: String): Flow<List<SpeakingScenario>> = flowOf(emptyList())
    override suspend fun getScenarioCount(): Int = 0
}

private class EmptySpeakingSessionDao : SpeakingSessionDao {
    override fun getAllSessions(): Flow<List<SpeakingSessionRecordEntity>> = flowOf(emptyList())
    override fun getRecentSessions(limit: Int): Flow<List<SpeakingSessionRecordEntity>> = flowOf(emptyList())
    override fun getSessionById(id: String): Flow<SpeakingSessionRecordEntity?> = flowOf(null)
    override suspend fun getSessionByIdSync(id: String): SpeakingSessionRecordEntity? = null
    override fun getSessionsForScenario(scenarioId: String): Flow<List<SpeakingSessionRecordEntity>> = flowOf(emptyList())
    override suspend fun getSessionCount(): Int = 0
    override suspend fun insertSession(session: SpeakingSessionRecordEntity) {}
}

private class EmptyUserLearningRepository : UserLearningRepository {
    override suspend fun recordEvidence(evidence: LearningEvidence): Long = 0L
    override fun getEvidenceForContent(contentId: String): Flow<List<LearningEvidence>> = flowOf(emptyList())
    override fun getRecentEvidence(limit: Int): Flow<List<LearningEvidence>> = flowOf(emptyList())
    override fun getMasterySnapshot(contentId: String): Flow<MasterySnapshot?> = flowOf(null)
    override suspend fun getMasterySnapshotSync(contentId: String): MasterySnapshot? = null
    override fun getAllMasterySnapshots(): Flow<List<MasterySnapshot>> = flowOf(emptyList())
    override suspend fun saveMasterySnapshot(snapshot: MasterySnapshot) {}
    override fun getActiveMistakes(): Flow<List<MistakeRecord>> = flowOf(emptyList())
    override fun getAllMistakes(): Flow<List<MistakeRecord>> = flowOf(emptyList())
    override suspend fun findMistake(contentId: String, trapType: String): MistakeRecord? = null
    override suspend fun saveMistake(mistake: MistakeRecord): Long = 0L
    override fun getDueReviews(currentTimestamp: Long): Flow<List<ReviewSchedule>> = flowOf(emptyList())
    override fun getReviewSchedule(contentId: String): Flow<ReviewSchedule?> = flowOf(null)
    override suspend fun getReviewScheduleSync(contentId: String): ReviewSchedule? = null
    override suspend fun saveReviewSchedule(schedule: ReviewSchedule) {}
    override fun getUserProfile(id: String): Flow<UserProfile?> = flowOf(null)
    override suspend fun getUserProfileSync(id: String): UserProfile? = null
    override suspend fun saveUserProfile(profile: UserProfile) {}
}
