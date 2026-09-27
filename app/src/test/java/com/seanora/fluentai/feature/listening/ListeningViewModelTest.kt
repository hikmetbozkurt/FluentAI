package com.seanora.fluentai.feature.listening

import com.seanora.fluentai.core.audio.AudioPlaybackEngine
import com.seanora.fluentai.core.audio.PlaybackState
import com.seanora.fluentai.core.model.KeyVocabulary
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.ListeningComprehensionQuestion
import com.seanora.fluentai.core.model.ListeningScenario
import com.seanora.fluentai.core.model.Speaker
import com.seanora.fluentai.core.model.TranscriptItem
import com.seanora.fluentai.data.repository.ListeningRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.domain.listening.ListeningFeatureEngine
import com.seanora.fluentai.domain.mastery.MasteryEngine
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import com.seanora.fluentai.domain.mistakes.MistakeEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
class ListeningViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val mockScenarios = listOf(
        ListeningScenario(
            id = "listening.b1.daily-standup",
            title = "Morning Standup",
            cefrLevel = "B1",
            category = "engineering_meeting",
            scenarioContext = "A morning virtual standup meeting.",
            speakers = listOf(
                Speaker(id = "selin", name = "Selin", role = "Android Engineer", accent = "Turkish"),
                Speaker(id = "marcus", name = "Marcus", role = "Scrum Master", accent = "British")
            ),
            audioRef = "audio/listening/b1_daily_standup.mp3",
            durationSeconds = 45,
            transcriptItems = listOf(
                TranscriptItem(
                    index = 1,
                    speakerId = "marcus",
                    startMs = 0L,
                    endMs = 6000L,
                    textEn = "Good morning everyone.",
                    textTr = "Herkese günaydın."
                ),
                TranscriptItem(
                    index = 2,
                    speakerId = "selin",
                    startMs = 6200L,
                    endMs = 12000L,
                    textEn = "Morning Marcus.",
                    textTr = "Günaydın Marcus."
                )
            ),
            keyVocabulary = listOf(
                KeyVocabulary(word = "deliverable", vocabId = "vocab.deliverable", contextNoteTr = "Teslimat.")
            ),
            comprehensionQuestions = listOf(
                ListeningComprehensionQuestion(
                    id = "q_standup_01",
                    questionEn = "Who spoke first?",
                    options = listOf("Marcus", "Selin"),
                    correctAnswer = "Marcus",
                    explanationEn = "Marcus began the standup.",
                    explanationTr = "Toplantıyı Marcus başlattı."
                )
            ),
            topicTags = listOf("agile"),
            relatedIds = listOf("vocab.deliverable")
        ),
        ListeningScenario(
            id = "listening.c1.architecture-review",
            title = "Architecture Review",
            cefrLevel = "C1",
            category = "engineering_meeting",
            scenarioContext = "Debating CQRS architecture.",
            speakers = listOf(
                Speaker(id = "deniz", name = "Deniz", role = "Architect", accent = "Turkish")
            ),
            audioRef = "audio/listening/c1_architecture_review.mp3",
            durationSeconds = 60,
            transcriptItems = listOf(
                TranscriptItem(
                    index = 1,
                    speakerId = "deniz",
                    startMs = 0L,
                    endMs = 5000L,
                    textEn = "Let's review the ledger subsystem.",
                    textTr = "Defter alt sistemini inceleyelim."
                )
            ),
            keyVocabulary = emptyList(),
            comprehensionQuestions = emptyList(),
            topicTags = listOf("architecture"),
            relatedIds = emptyList()
        )
    )

    private val fakeListeningRepository = object : ListeningRepository {
        override fun getAllScenarios(): Flow<List<ListeningScenario>> = flowOf(mockScenarios)
        override fun getScenariosByLevel(level: String): Flow<List<ListeningScenario>> =
            flowOf(mockScenarios.filter { it.cefrLevel == level })
        override fun getScenarioById(id: String): Flow<ListeningScenario?> =
            flowOf(mockScenarios.find { it.id == id })
        override fun getScenariosByCategory(category: String): Flow<List<ListeningScenario>> =
            flowOf(mockScenarios.filter { it.category == category })
        override fun searchScenarios(query: String): Flow<List<ListeningScenario>> =
            flowOf(mockScenarios.filter { it.title.contains(query, ignoreCase = true) })
        override suspend fun getScenarioCount(): Int = mockScenarios.size
    }

    private val fakePlaybackState = MutableStateFlow(PlaybackState())
    private var lastPlayedUri: String? = null
    private var lastSeekPosition: Long? = null

    private val fakeAudioEngine = object : AudioPlaybackEngine {
        override val playbackState = fakePlaybackState

        override fun play(uri: String, startPositionMs: Long) {
            lastPlayedUri = uri
            lastSeekPosition = startPositionMs
            fakePlaybackState.value = PlaybackState(
                isPlaying = true,
                currentMediaUri = uri,
                currentPositionMs = startPositionMs,
                durationMs = 45000L
            )
        }

        override fun pause() {
            fakePlaybackState.value = fakePlaybackState.value.copy(isPlaying = false)
        }

        override fun resume() {
            fakePlaybackState.value = fakePlaybackState.value.copy(isPlaying = true)
        }

        override fun seekTo(positionMs: Long) {
            lastSeekPosition = positionMs
            fakePlaybackState.value = fakePlaybackState.value.copy(currentPositionMs = positionMs)
        }

        override fun stop() {
            fakePlaybackState.value = PlaybackState(isPlaying = false, currentPositionMs = 0L)
        }

        override fun release() {
            stop()
        }
    }

    private val recordedEvidence = mutableListOf<LearningEvidence>()

    private val fakeUserLearningRepository = object : UserLearningRepository {
        override suspend fun recordEvidence(evidence: LearningEvidence): Long {
            recordedEvidence.add(evidence)
            return recordedEvidence.size.toLong()
        }
        override fun getEvidenceForContent(contentId: String): Flow<List<LearningEvidence>> =
            flowOf(recordedEvidence.filter { it.contentId == contentId })
        override fun getRecentEvidence(limit: Int): Flow<List<LearningEvidence>> =
            flowOf(recordedEvidence.takeLast(limit))
        override fun getMasterySnapshot(contentId: String): Flow<com.seanora.fluentai.core.model.MasterySnapshot?> = flowOf(null)
        override suspend fun getMasterySnapshotSync(contentId: String): com.seanora.fluentai.core.model.MasterySnapshot? = null
        override fun getAllMasterySnapshots(): Flow<List<com.seanora.fluentai.core.model.MasterySnapshot>> = flowOf(emptyList())
        override suspend fun saveMasterySnapshot(snapshot: com.seanora.fluentai.core.model.MasterySnapshot) {}
        override fun getActiveMistakes(): Flow<List<com.seanora.fluentai.core.model.MistakeRecord>> = flowOf(emptyList())
        override fun getAllMistakes(): Flow<List<com.seanora.fluentai.core.model.MistakeRecord>> = flowOf(emptyList())
        override suspend fun findMistake(contentId: String, trapType: String): com.seanora.fluentai.core.model.MistakeRecord? = null
        override suspend fun saveMistake(mistake: com.seanora.fluentai.core.model.MistakeRecord): Long = 1L
        override fun getDueReviews(currentTimestamp: Long): Flow<List<com.seanora.fluentai.core.model.ReviewSchedule>> = flowOf(emptyList())
        override fun getReviewSchedule(contentId: String): Flow<com.seanora.fluentai.core.model.ReviewSchedule?> = flowOf(null)
        override suspend fun getReviewScheduleSync(contentId: String): com.seanora.fluentai.core.model.ReviewSchedule? = null
        override suspend fun saveReviewSchedule(schedule: com.seanora.fluentai.core.model.ReviewSchedule) {}
        override fun getUserProfile(id: String): Flow<com.seanora.fluentai.core.model.UserProfile?> = flowOf(null)
        override suspend fun getUserProfileSync(id: String): com.seanora.fluentai.core.model.UserProfile? = null
        override suspend fun saveUserProfile(profile: com.seanora.fluentai.core.model.UserProfile) {}
    }

    private val recordLearningEvidenceUseCase = RecordLearningEvidenceUseCase(
        userLearningRepository = fakeUserLearningRepository,
        masteryEngine = MasteryEngine(),
        mistakeEngine = MistakeEngine()
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        recordedEvidence.clear()
        fakePlaybackState.value = PlaybackState()
        lastPlayedUri = null
        lastSeekPosition = null
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun uiState_initialLoad_populatesScenariosAndTranscriptIsHiddenByDefault() = runTest {
        val viewModel = createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(2, state.scenarios.size)
        assertEquals(null, state.selectedScenario)
        assertFalse("Initial transcript must be hidden for active listening ear training", state.isTranscriptRevealed)
        assertFalse(state.isPlaying)
    }

    @Test
    fun uiState_filterByLevel_filtersCorrectly() = runTest {
        val viewModel = createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        viewModel.selectLevel("C1")
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("C1", state.selectedLevel)
        assertEquals(1, state.scenarios.size)
        assertEquals("listening.c1.architecture-review", state.scenarios[0].id)
    }

    @Test
    fun uiState_togglePlayPause_controlsAudioEngine() = runTest {
        val viewModel = createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        assertFalse(fakePlaybackState.value.isPlaying)
        viewModel.selectScenarioById("listening.b1.daily-standup")
        testScheduler.advanceUntilIdle()
        viewModel.togglePlayPause()
        testScheduler.advanceUntilIdle()

        assertTrue(fakePlaybackState.value.isPlaying)
        assertEquals("audio/listening/b1_daily_standup.mp3", lastPlayedUri)

        viewModel.togglePlayPause()
        testScheduler.advanceUntilIdle()
        assertFalse(fakePlaybackState.value.isPlaying)
    }

    @Test
    fun uiState_replaySentence_playsFromStartMs() = runTest {
        val viewModel = createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        viewModel.selectScenarioById("listening.b1.daily-standup")
        testScheduler.advanceUntilIdle()
        val secondSentence = mockScenarios[0].transcriptItems[1]
        viewModel.replaySentence(secondSentence)
        testScheduler.advanceUntilIdle()

        assertEquals("audio/listening/b1_daily_standup.mp3", lastPlayedUri)
        assertEquals(6200L, lastSeekPosition)
        assertTrue(fakePlaybackState.value.isPlaying)
    }

    @Test
    fun uiState_toggleTranscriptAndSentenceTranslation_updatesState() = runTest {
        val viewModel = createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isTranscriptRevealed)
        viewModel.toggleTranscriptReveal()
        testScheduler.advanceUntilIdle()
        assertTrue(viewModel.uiState.value.isTranscriptRevealed)

        assertFalse(viewModel.uiState.value.revealedSentenceTranslations.contains(1))
        viewModel.toggleSentenceTranslation(1)
        testScheduler.advanceUntilIdle()
        assertTrue(viewModel.uiState.value.revealedSentenceTranslations.contains(1))

        viewModel.toggleSentenceTranslation(1)
        testScheduler.advanceUntilIdle()
        assertFalse(viewModel.uiState.value.revealedSentenceTranslations.contains(1))
    }

    @Test
    fun uiState_submitAnswer_evaluatesAndRecordsEvidence() = runTest {
        val viewModel = createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        val scenario = mockScenarios[0]
        val question = scenario.comprehensionQuestions[0]
        viewModel.selectScenarioById(scenario.id)
        testScheduler.advanceUntilIdle()

        // Select correct answer
        viewModel.selectAnswer(question.id, "Marcus")
        testScheduler.advanceUntilIdle()
        assertEquals("Marcus", viewModel.uiState.value.userAnswers[question.id])

        // Submit answer
        viewModel.submitAnswer(question)
        viewModel.submitAnswer(question)
        testScheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.submittedQuestions.contains(question.id))
        assertEquals(1, recordedEvidence.size)
        assertEquals("listening.b1.daily-standup", recordedEvidence[0].contentId)
        assertEquals("listening", recordedEvidence[0].domain)
        assertTrue(recordedEvidence[0].isCorrect)
        assertEquals(1.0f, recordedEvidence[0].score)
    }

    @Test
    fun uiState_playbackFailure_isVisibleAndStopsPlayback() = runTest {
        val viewModel = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect() }
        testScheduler.advanceUntilIdle()

        fakePlaybackState.value = PlaybackState(
            currentMediaUri = mockScenarios.first().audioRef,
            errorMessage = "Audio file is not packaged with this lesson."
        )
        testScheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isPlaying)
        assertEquals("Audio file is not packaged with this lesson.", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun explicitSessionId_neverFallsBackToFirstScenarioAndMissingIdFailsGracefully() = runTest {
        val viewModel = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect() }
        testScheduler.advanceUntilIdle()

        viewModel.selectScenarioById("listening.c1.architecture-review")
        testScheduler.advanceUntilIdle()
        assertEquals("listening.c1.architecture-review", viewModel.uiState.value.selectedScenario?.id)
        assertEquals("Let's review the ledger subsystem.", viewModel.uiState.value.selectedScenario?.transcriptItems?.single()?.textEn)

        viewModel.selectScenarioById("listening.missing")
        testScheduler.advanceUntilIdle()
        assertEquals(null, viewModel.uiState.value.selectedScenario)
        assertEquals("Listening recording not found.", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun questionState_survivesTranscriptRoundTripWithinSameSession() = runTest {
        val viewModel = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect() }
        testScheduler.advanceUntilIdle()
        viewModel.selectScenarioById("listening.b1.daily-standup")
        testScheduler.advanceUntilIdle()

        val question = mockScenarios.first().comprehensionQuestions.first()
        viewModel.selectSessionSection(ListeningSessionSection.QUESTIONS)
        viewModel.selectAnswer(question.id, "Marcus")
        viewModel.submitAnswer(question)
        testScheduler.advanceUntilIdle()
        viewModel.selectSessionSection(ListeningSessionSection.TRANSCRIPT)
        viewModel.selectSessionSection(ListeningSessionSection.QUESTIONS)

        val state = viewModel.uiState.value
        assertEquals(ListeningSessionSection.QUESTIONS, state.sessionSection)
        assertEquals("Marcus", state.userAnswers[question.id])
        assertTrue(question.id in state.submittedQuestions)
    }

    @Test
    fun dictation_isBoundToSelectedRecordingAndKeepsExistingNormalization() = runTest {
        val viewModel = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect() }
        testScheduler.advanceUntilIdle()
        viewModel.selectScenarioById("listening.b1.daily-standup")
        testScheduler.advanceUntilIdle()

        viewModel.startDictation()
        viewModel.updateDictationAnswer("GOOD morning, everyone!")
        viewModel.submitDictation()
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isDictationActive)
        assertEquals("Good morning everyone.", state.currentDictationTranscript?.textEn)
        assertTrue(state.dictationResult?.isCorrect == true)
        assertEquals("listening.b1.daily-standup", recordedEvidence.last().contentId)
        assertEquals("dictation", recordedEvidence.last().activityType)
    }

    private fun createViewModel() = ListeningViewModel(
        listeningRepository = fakeListeningRepository,
        audioPlaybackEngine = fakeAudioEngine,
        recordLearningEvidenceUseCase = recordLearningEvidenceUseCase,
        featureEngine = ListeningFeatureEngine(),
    )
}
