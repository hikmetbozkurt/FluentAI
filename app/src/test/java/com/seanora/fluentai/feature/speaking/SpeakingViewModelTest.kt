package com.seanora.fluentai.feature.speaking

import com.seanora.fluentai.ai.analysis.MockSpeakingAnalysisClient
import com.seanora.fluentai.ai.live.LiveCoachSessionManager
import com.seanora.fluentai.ai.live.LiveScenarioConfig
import com.seanora.fluentai.ai.live.MockLiveAiClient
import com.seanora.fluentai.ai.live.TranscriptEntry
import com.seanora.fluentai.ai.live.TranscriptRole
import com.seanora.fluentai.ai.live.VoiceUiState
import com.seanora.fluentai.ai.live.SpeakingPersona
import com.seanora.fluentai.ai.live.SpeakingSessionType
import com.seanora.fluentai.core.audio.FakeAudioCaptureEngine
import com.seanora.fluentai.core.audio.FakeStreamAudioPlaybackEngine
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SpeakingViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val sampleScenario1 = SpeakingScenario(
        id = "speaking.b2.meeting.standup",
        title = "Agile Standup & Blockers",
        category = SpeakingCategory.MEETING,
        cefrLevel = "B2",
        systemPrompt = "Scrum master daily standup.",
        contextDescription = "Sprint status update.",
        recommendedCorrectionMode = CorrectionMode.DRILL,
        voiceName = "Puck"
    )

    private val sampleScenario2 = SpeakingScenario(
        id = "speaking.c1.jobinterview.system-design",
        title = "Principal Engineer Architecture Defense",
        category = SpeakingCategory.JOB_INTERVIEW,
        cefrLevel = "C1",
        systemPrompt = "Architect interview.",
        contextDescription = "System design defense.",
        recommendedCorrectionMode = CorrectionMode.MOCK,
        voiceName = "Fenrir"
    )

    private val defaultTestScenarios = listOf(sampleScenario1, sampleScenario2)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createTestViewModel(
        sessionManager: LiveCoachSessionManager,
        scenarios: List<SpeakingScenario> = defaultTestScenarios
    ): Pair<SpeakingViewModel, FakeUserLearningRepository> {
        val fakeRepo = object : SpeakingRepository {
            override fun getAllScenarios(): Flow<List<SpeakingScenario>> = flowOf(scenarios)
            override fun getScenariosByLevel(level: String): Flow<List<SpeakingScenario>> =
                flowOf(scenarios.filter { it.cefrLevel == level })
            override fun getScenariosByCategory(category: SpeakingCategory): Flow<List<SpeakingScenario>> =
                flowOf(scenarios.filter { it.category == category })
            override fun getScenarioById(id: String): Flow<SpeakingScenario?> =
                flowOf(scenarios.find { it.id == id })
            override suspend fun getScenarioByIdSync(id: String): SpeakingScenario? =
                scenarios.find { it.id == id }
            override fun searchScenarios(query: String): Flow<List<SpeakingScenario>> =
                flowOf(scenarios.filter { it.title.contains(query, ignoreCase = true) })
            override suspend fun getScenarioCount(): Int = scenarios.size
        }

        val fakeUserRepo = FakeUserLearningRepository()

        val fakeSpeakingSessionDao = object : SpeakingSessionDao {
            val sessions = mutableListOf<SpeakingSessionRecordEntity>()
            override fun getAllSessions(): Flow<List<SpeakingSessionRecordEntity>> = flowOf(sessions)
            override fun getRecentSessions(limit: Int): Flow<List<SpeakingSessionRecordEntity>> = flowOf(sessions)
            override fun getSessionById(id: String): Flow<SpeakingSessionRecordEntity?> = flowOf(sessions.find { it.id == id })
            override suspend fun getSessionByIdSync(id: String): SpeakingSessionRecordEntity? = sessions.find { it.id == id }
            override fun getSessionsForScenario(scenarioId: String): Flow<List<SpeakingSessionRecordEntity>> =
                flowOf(sessions.filter { it.scenarioId == scenarioId })
            override suspend fun getSessionCount(): Int = sessions.size
            override suspend fun insertSession(session: SpeakingSessionRecordEntity) { sessions.add(session) }
        }

        val promptBuilder = SpeakingPromptBuilder()
        val analysisClient = MockSpeakingAnalysisClient()
        val masteryEngine = MasteryEngine()
        val mistakeEngine = MistakeEngine()
        val recordUseCase = RecordLearningEvidenceUseCase(fakeUserRepo, masteryEngine, mistakeEngine)
        val processAnalysisUseCase = ProcessSpeakingAnalysisUseCase(recordUseCase, fakeSpeakingSessionDao)

        val vm = SpeakingViewModel(
            sessionManager = sessionManager,
            speakingRepository = fakeRepo,
            promptBuilder = promptBuilder,
            analysisClient = analysisClient,
            processSpeakingAnalysisUseCase = processAnalysisUseCase,
            userLearningRepository = fakeUserRepo
        )

        return vm to fakeUserRepo
    }

    private class FakeUserLearningRepository : UserLearningRepository {
        val evidenceList = mutableListOf<LearningEvidence>()
        val snapshots = mutableListOf<MasterySnapshot>()
        val mistakes = mutableListOf<MistakeRecord>()

        override suspend fun recordEvidence(evidence: LearningEvidence): Long {
            evidenceList.add(evidence)
            return evidenceList.size.toLong()
        }
        override fun getEvidenceForContent(contentId: String): Flow<List<LearningEvidence>> = flowOf(evidenceList.filter { it.contentId == contentId })
        override fun getRecentEvidence(limit: Int): Flow<List<LearningEvidence>> = flowOf(evidenceList.takeLast(limit))
        override fun getMasterySnapshot(contentId: String): Flow<MasterySnapshot?> = flowOf(snapshots.find { it.contentId == contentId })
        override suspend fun getMasterySnapshotSync(contentId: String): MasterySnapshot? = snapshots.find { it.contentId == contentId }
        override fun getAllMasterySnapshots(): Flow<List<MasterySnapshot>> = flowOf(snapshots)
        override suspend fun saveMasterySnapshot(snapshot: MasterySnapshot) {
            snapshots.removeAll { it.contentId == snapshot.contentId }
            snapshots.add(snapshot)
        }
        override fun getActiveMistakes(): Flow<List<MistakeRecord>> = flowOf(mistakes)
        override fun getAllMistakes(): Flow<List<MistakeRecord>> = flowOf(mistakes)
        override suspend fun findMistake(contentId: String, trapType: String): MistakeRecord? = mistakes.find { it.contentId == contentId && it.trapType == trapType }
        override suspend fun saveMistake(mistake: MistakeRecord): Long {
            mistakes.removeAll { it.contentId == mistake.contentId && it.trapType == mistake.trapType }
            mistakes.add(mistake)
            return mistake.id
        }
        override fun getDueReviews(currentTimestamp: Long): Flow<List<ReviewSchedule>> = flowOf(emptyList())
        override fun getReviewSchedule(contentId: String): Flow<ReviewSchedule?> = flowOf(null)
        override suspend fun getReviewScheduleSync(contentId: String): ReviewSchedule? = null
        override suspend fun saveReviewSchedule(schedule: ReviewSchedule) {}
        override fun getUserProfile(id: String): Flow<UserProfile?> = flowOf(null)
        override suspend fun getUserProfileSync(id: String): UserProfile? = null
        override suspend fun saveUserProfile(profile: UserProfile) {}
    }

    @Test
    fun initialState_loadsScenariosAndDefaultsToInactive() = runTest(testDispatcher) {
        val captureEngine = FakeAudioCaptureEngine()
        val playbackEngine = FakeStreamAudioPlaybackEngine()
        val mockClient = MockLiveAiClient(dispatcher = testDispatcher)

        val sessionManager = LiveCoachSessionManager(
            audioCaptureEngine = captureEngine,
            playbackEngine = playbackEngine,
            liveAiClient = mockClient,
            dispatcher = testDispatcher
        )

        val (viewModel, _) = createTestViewModel(sessionManager)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isSessionActive)
        assertEquals(VoiceUiState.IDLE, state.voiceState)
        assertEquals(2, state.availableScenarios.size)
        assertEquals("speaking.b2.meeting.standup", state.selectedScenario?.id)
        assertEquals(CorrectionMode.DRILL, state.selectedMode)
    }

    @Test
    fun categoryAndCefrFilters_filterScenariosCorrectly() = runTest(testDispatcher) {
        val captureEngine = FakeAudioCaptureEngine()
        val playbackEngine = FakeStreamAudioPlaybackEngine()
        val mockClient = MockLiveAiClient(dispatcher = testDispatcher)

        val sessionManager = LiveCoachSessionManager(
            audioCaptureEngine = captureEngine,
            playbackEngine = playbackEngine,
            liveAiClient = mockClient,
            dispatcher = testDispatcher
        )

        val (viewModel, _) = createTestViewModel(sessionManager)
        advanceUntilIdle()

        // Filter by Category
        viewModel.selectCategory(SpeakingCategory.JOB_INTERVIEW)
        assertEquals(1, viewModel.uiState.value.filteredScenarios.size)
        assertEquals("speaking.c1.jobinterview.system-design", viewModel.uiState.value.filteredScenarios.first().id)

        // Clear Category
        viewModel.selectCategory(null)
        assertEquals(2, viewModel.uiState.value.filteredScenarios.size)

        // Filter by CEFR
        viewModel.selectCefrLevel("B2")
        assertEquals(1, viewModel.uiState.value.filteredScenarios.size)
        assertEquals("speaking.b2.meeting.standup", viewModel.uiState.value.filteredScenarios.first().id)
    }

    @Test
    fun selectCorrectionMode_updatesActiveMode() = runTest(testDispatcher) {
        val captureEngine = FakeAudioCaptureEngine()
        val playbackEngine = FakeStreamAudioPlaybackEngine()
        val mockClient = MockLiveAiClient(dispatcher = testDispatcher)

        val sessionManager = LiveCoachSessionManager(
            audioCaptureEngine = captureEngine,
            playbackEngine = playbackEngine,
            liveAiClient = mockClient,
            dispatcher = testDispatcher
        )

        val (viewModel, _) = createTestViewModel(sessionManager)
        advanceUntilIdle()

        viewModel.selectCorrectionMode(CorrectionMode.FLOW)
        assertEquals(CorrectionMode.FLOW, viewModel.uiState.value.selectedMode)

        viewModel.selectCorrectionMode(CorrectionMode.MOCK)
        assertEquals(CorrectionMode.MOCK, viewModel.uiState.value.selectedMode)
    }

    @Test
    fun startSession_transitionsStateToActive() = runTest(testDispatcher) {
        val captureEngine = FakeAudioCaptureEngine()
        val playbackEngine = FakeStreamAudioPlaybackEngine()
        val mockClient = MockLiveAiClient(dispatcher = testDispatcher).apply { speechDelayMs = 0L }

        val sessionManager = LiveCoachSessionManager(
            audioCaptureEngine = captureEngine,
            playbackEngine = playbackEngine,
            liveAiClient = mockClient,
            dispatcher = testDispatcher
        )

        val (viewModel, _) = createTestViewModel(sessionManager)
        advanceUntilIdle()

        viewModel.startSession(sampleScenario1, CorrectionMode.DRILL)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isSessionActive)
        assertEquals(sampleScenario1.id, state.selectedScenario?.id)
        assertEquals(CorrectionMode.DRILL, state.selectedMode)
    }

    @Test
    fun startGaladrielSession_usesExistingLiveFlowWithoutScenario() = runTest(testDispatcher) {
        val sessionManager = LiveCoachSessionManager(
            audioCaptureEngine = FakeAudioCaptureEngine(),
            playbackEngine = FakeStreamAudioPlaybackEngine(),
            liveAiClient = MockLiveAiClient(dispatcher = testDispatcher).apply { speechDelayMs = 0L },
            dispatcher = testDispatcher,
        )
        val (viewModel, _) = createTestViewModel(sessionManager)
        advanceUntilIdle()

        viewModel.startGaladrielSession()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isSessionActive)
        assertEquals(null, state.selectedScenario)
        assertEquals(null, state.activeLiveConfig?.scenarioId)
        assertEquals(SpeakingSessionType.FREE_TALK, state.activeLiveConfig?.sessionType)
        assertEquals(SpeakingPersona.GALADRIEL, state.activeLiveConfig?.persona)
        assertEquals(CorrectionMode.FLOW, state.selectedMode)
        assertEquals("Galadriel", sessionManager.currentScenario?.title)
    }

    @Test
    fun interruptAndControls_manageSessionState() = runTest(testDispatcher) {
        val captureEngine = FakeAudioCaptureEngine()
        val playbackEngine = FakeStreamAudioPlaybackEngine()
        val mockClient = MockLiveAiClient(dispatcher = testDispatcher).apply { speechDelayMs = 0L }

        val sessionManager = LiveCoachSessionManager(
            audioCaptureEngine = captureEngine,
            playbackEngine = playbackEngine,
            liveAiClient = mockClient,
            dispatcher = testDispatcher
        )

        val (viewModel, _) = createTestViewModel(sessionManager)
        advanceUntilIdle()

        viewModel.startSession(sampleScenario1)
        advanceUntilIdle()

        viewModel.interruptCoach()
        advanceUntilIdle()
        assertEquals(VoiceUiState.USER_SPEAKING, viewModel.uiState.value.voiceState)

        viewModel.togglePause()
        advanceUntilIdle()
        assertEquals(VoiceUiState.PAUSED, viewModel.uiState.value.voiceState)

        viewModel.endSession()
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isSessionActive)
        assertTrue(viewModel.uiState.value.isReportVisible)
    }

    @Test
    fun toggleMute_stopsOnlyMicrophoneAndKeepsSessionUnpaused() = runTest(testDispatcher) {
        val captureEngine = FakeAudioCaptureEngine()
        val sessionManager = LiveCoachSessionManager(
            audioCaptureEngine = captureEngine,
            playbackEngine = FakeStreamAudioPlaybackEngine(),
            liveAiClient = MockLiveAiClient(dispatcher = testDispatcher).apply { speechDelayMs = 0L },
            dispatcher = testDispatcher
        )
        val (viewModel, _) = createTestViewModel(sessionManager)
        advanceUntilIdle()
        viewModel.startSession(sampleScenario1)
        advanceUntilIdle()

        assertTrue(captureEngine.isRecording.value)
        val voiceStateBeforeMute = viewModel.uiState.value.voiceState
        viewModel.toggleMute()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isMuted)
        assertFalse(captureEngine.isRecording.value)
        assertEquals(voiceStateBeforeMute, viewModel.uiState.value.voiceState)
        assertTrue(viewModel.uiState.value.voiceState != VoiceUiState.PAUSED)

        viewModel.toggleMute()
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isMuted)
        assertTrue(captureEngine.isRecording.value)
    }

    @Test
    fun endSession_withTranscript_triggersAnalysisAndReport() = runTest(testDispatcher) {
        val captureEngine = FakeAudioCaptureEngine()
        val playbackEngine = FakeStreamAudioPlaybackEngine()
        val mockClient = MockLiveAiClient(dispatcher = testDispatcher).apply { speechDelayMs = 0L }

        val sessionManager = LiveCoachSessionManager(
            audioCaptureEngine = captureEngine,
            playbackEngine = playbackEngine,
            liveAiClient = mockClient,
            dispatcher = testDispatcher
        )

        val (viewModel, userRepo) = createTestViewModel(sessionManager)
        advanceUntilIdle()

        viewModel.startSession(sampleScenario1, CorrectionMode.COACH)
        advanceUntilIdle()

        // Add user speech transcript
        mockClient.simulateUserSpeech("I have worked since 2 years on this and I am agree with the team.")
        advanceUntilIdle()

        viewModel.endSession()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isSessionActive)
        assertTrue(state.isReportVisible)
        assertFalse(state.isAnalyzing)
        assertNotNull(state.postSessionAnalysis)

        val analysis = state.postSessionAnalysis!!
        assertEquals(sampleScenario1.id, analysis.scenarioId)
        assertTrue(analysis.grammarObservations.isNotEmpty())

        // Verify evidence entered UserLearningRepository
        assertTrue(userRepo.evidenceList.isNotEmpty())
        assertTrue(userRepo.mistakes.isNotEmpty())

        // Test dismissReport
        viewModel.dismissReport()
        assertFalse(viewModel.uiState.value.isReportVisible)
    }

    @Test
    fun endGaladrielSession_withTranscript_runsCompatibleAnalysisAndEvidence() = runTest(testDispatcher) {
        val mockClient = MockLiveAiClient(dispatcher = testDispatcher).apply { speechDelayMs = 0L }
        val sessionManager = LiveCoachSessionManager(
            audioCaptureEngine = FakeAudioCaptureEngine(),
            playbackEngine = FakeStreamAudioPlaybackEngine(),
            liveAiClient = mockClient,
            dispatcher = testDispatcher,
        )
        val (viewModel, userRepo) = createTestViewModel(sessionManager)
        advanceUntilIdle()

        viewModel.startGaladrielSession()
        advanceUntilIdle()
        mockClient.simulateUserSpeech("I am agree that this is a very important supply chain issue.")
        advanceUntilIdle()

        viewModel.endSession()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isReportVisible)
        assertFalse(state.isAnalyzing)
        assertEquals(SpeakingPromptBuilder.GALADRIEL_ANALYSIS_CONTENT_ID, state.postSessionAnalysis?.scenarioId)
        assertEquals("Galadriel Free Talk", state.postSessionAnalysis?.scenarioTitle)
        assertTrue(userRepo.evidenceList.any { it.contentId == SpeakingPromptBuilder.GALADRIEL_ANALYSIS_CONTENT_ID })
    }

    @Test
    fun permissionDenied_showsVisibleError_remainsOnLauncher() = runTest(testDispatcher) {
        val captureEngine = FakeAudioCaptureEngine()
        val playbackEngine = FakeStreamAudioPlaybackEngine()
        val mockClient = MockLiveAiClient(dispatcher = testDispatcher)

        val sessionManager = LiveCoachSessionManager(
            audioCaptureEngine = captureEngine,
            playbackEngine = playbackEngine,
            liveAiClient = mockClient,
            dispatcher = testDispatcher
        )

        val (viewModel, _) = createTestViewModel(sessionManager)
        advanceUntilIdle()

        viewModel.onPermissionDenied(permanentlyDenied = false)

        val state = viewModel.uiState.value
        assertFalse(state.isSessionActive)
        assertEquals(VoiceUiState.IDLE, state.voiceState)
        assertEquals("Microphone permission is required to start live speaking.", state.errorMessage)
        assertFalse(captureEngine.isRecording.value)
    }

    @Test
    fun repeatedDenial_showsSettingsGuidanceError() = runTest(testDispatcher) {
        val captureEngine = FakeAudioCaptureEngine()
        val playbackEngine = FakeStreamAudioPlaybackEngine()
        val mockClient = MockLiveAiClient(dispatcher = testDispatcher)

        val sessionManager = LiveCoachSessionManager(
            audioCaptureEngine = captureEngine,
            playbackEngine = playbackEngine,
            liveAiClient = mockClient,
            dispatcher = testDispatcher
        )

        val (viewModel, _) = createTestViewModel(sessionManager)
        advanceUntilIdle()

        viewModel.onPermissionDenied(permanentlyDenied = true)

        val state = viewModel.uiState.value
        assertFalse(state.isSessionActive)
        assertEquals(VoiceUiState.IDLE, state.voiceState)
        assertTrue(state.errorMessage?.contains("Settings") == true)
        assertFalse(captureEngine.isRecording.value)
    }

    @Test
    fun grantAfterPriorDenial_clearsErrorAndStartsSession() = runTest(testDispatcher) {
        val captureEngine = FakeAudioCaptureEngine()
        val playbackEngine = FakeStreamAudioPlaybackEngine()
        val mockClient = MockLiveAiClient(dispatcher = testDispatcher).apply { speechDelayMs = 0L }

        val sessionManager = LiveCoachSessionManager(
            audioCaptureEngine = captureEngine,
            playbackEngine = playbackEngine,
            liveAiClient = mockClient,
            dispatcher = testDispatcher
        )

        val (viewModel, _) = createTestViewModel(sessionManager)
        advanceUntilIdle()

        viewModel.onPermissionDenied(permanentlyDenied = false)
        assertEquals("Microphone permission is required to start live speaking.", viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isSessionActive)

        viewModel.startSession(sampleScenario1)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isSessionActive)
        assertEquals(null, state.errorMessage)
        assertTrue(captureEngine.isRecording.value)
    }

    @Test
    fun startButton_doesNotTriggerDuplicateSessions() = runTest(testDispatcher) {
        val captureEngine = FakeAudioCaptureEngine()
        val playbackEngine = FakeStreamAudioPlaybackEngine()
        val mockClient = MockLiveAiClient(dispatcher = testDispatcher).apply { speechDelayMs = 0L }

        val sessionManager = LiveCoachSessionManager(
            audioCaptureEngine = captureEngine,
            playbackEngine = playbackEngine,
            liveAiClient = mockClient,
            dispatcher = testDispatcher
        )

        val (viewModel, _) = createTestViewModel(sessionManager)
        advanceUntilIdle()

        viewModel.startSession(sampleScenario1)
        viewModel.startSession(sampleScenario1)
        viewModel.startSession(sampleScenario1)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isSessionActive)
        assertEquals(sampleScenario1.id, state.selectedScenario?.id)
    }
}
