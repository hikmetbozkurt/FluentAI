package com.seanora.fluentai

import com.seanora.fluentai.core.audio.AudioPlaybackEngine
import com.seanora.fluentai.core.audio.PlaybackState
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.ListeningScenario
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.MistakeStage
import com.seanora.fluentai.core.model.ReadingArticle
import com.seanora.fluentai.core.model.ReadingComprehensionQuestion
import com.seanora.fluentai.core.model.ReadingParagraph
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.Speaker
import com.seanora.fluentai.core.model.TranscriptItem
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.core.model.VocabularyAnnotation
import com.seanora.fluentai.app.navigation.ReadingSessionMode
import com.seanora.fluentai.data.repository.ListeningRepository
import com.seanora.fluentai.data.repository.ReadingRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.domain.listening.ListeningFeatureEngine
import com.seanora.fluentai.domain.mastery.MasteryEngine
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import com.seanora.fluentai.domain.mistakes.MistakeEngine
import com.seanora.fluentai.feature.listening.ListeningViewModel
import com.seanora.fluentai.feature.reading.ReadingSessionSection
import com.seanora.fluentai.feature.reading.ReadingSessionViewModel
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
class Phase2IntegrityTest {

    private val testDispatcher = StandardTestDispatcher()

    private val evidenceList = mutableListOf<LearningEvidence>()
    private val masteryMap = mutableMapOf<String, MasterySnapshot>()
    private val mistakeMap = mutableMapOf<String, MistakeRecord>()
    private val reviewMap = mutableMapOf<String, ReviewSchedule>()

    private val userRepo = object : UserLearningRepository {
        override suspend fun recordEvidence(evidence: LearningEvidence): Long {
            evidenceList.add(evidence)
            return evidenceList.size.toLong()
        }

        override fun getEvidenceForContent(contentId: String): Flow<List<LearningEvidence>> =
            flowOf(evidenceList.filter { it.contentId == contentId })

        override fun getRecentEvidence(limit: Int): Flow<List<LearningEvidence>> =
            flowOf(evidenceList.takeLast(limit))

        override fun getMasterySnapshot(contentId: String): Flow<MasterySnapshot?> =
            flowOf(masteryMap[contentId])

        override suspend fun getMasterySnapshotSync(contentId: String): MasterySnapshot? =
            masteryMap[contentId]

        override fun getAllMasterySnapshots(): Flow<List<MasterySnapshot>> =
            flowOf(masteryMap.values.toList())

        override suspend fun saveMasterySnapshot(snapshot: MasterySnapshot) {
            masteryMap[snapshot.contentId] = snapshot
        }

        override fun getActiveMistakes(): Flow<List<MistakeRecord>> =
            flowOf(mistakeMap.values.filter { it.stage != MistakeStage.RESOLVED })

        override fun getAllMistakes(): Flow<List<MistakeRecord>> =
            flowOf(mistakeMap.values.toList())

        override suspend fun findMistake(contentId: String, trapType: String): MistakeRecord? =
            mistakeMap["$contentId:$trapType"]

        override suspend fun saveMistake(mistake: MistakeRecord): Long {
            mistakeMap["${mistake.contentId}:${mistake.trapType}"] = mistake
            return 1L
        }

        override fun getDueReviews(currentTimestamp: Long): Flow<List<ReviewSchedule>> =
            flowOf(reviewMap.values.filter { it.nextReviewDueTimestamp <= currentTimestamp })

        override fun getReviewSchedule(contentId: String): Flow<ReviewSchedule?> =
            flowOf(reviewMap[contentId])

        override suspend fun getReviewScheduleSync(contentId: String): ReviewSchedule? =
            reviewMap[contentId]

        override suspend fun saveReviewSchedule(schedule: ReviewSchedule) {
            reviewMap[schedule.contentId] = schedule
        }

        override fun getUserProfile(id: String): Flow<UserProfile?> =
            flowOf(UserProfile(targetCefrLevel = "C1"))

        override suspend fun getUserProfileSync(id: String): UserProfile? =
            UserProfile(targetCefrLevel = "C1")

        override suspend fun saveUserProfile(profile: UserProfile) {}
    }

    private val recordLearningEvidenceUseCase = RecordLearningEvidenceUseCase(
        userLearningRepository = userRepo,
        masteryEngine = MasteryEngine(),
        mistakeEngine = MistakeEngine()
    )

    private val mockReadingArticle = ReadingArticle(
        id = "reading.b2.microservices-tradeoffs",
        title = "Microservices vs Modular Monoliths",
        cefrLevel = "B2",
        category = "technology",
        summaryEn = "Engineering trade-offs between monolithic architectures and microservices.",
        summaryTr = "Monolitik mimariler ile mikro servisler arasındaki mühendislik ödünleşimleri.",
        wordCount = 310,
        estimatedReadingMinutes = 3,
        paragraphs = listOf(
            ReadingParagraph(
                paragraphIndex = 1,
                title = "The Allure of Microservices",
                contentEn = "Many engineering organizations adopt microservices to decentralize deployment pipelines.",
                contentTr = "Birçok mühendislik organizasyonu yayına alma hatlarını merkezsizleştirmek için mikro servisleri benimser."
            ),
            ReadingParagraph(
                paragraphIndex = 2,
                title = "The Hidden Operational Tax",
                contentEn = "However, distributed systems introduce network latency and distributed transaction complexity.",
                contentTr = "Ancak dağıtık sistemler ağ gecikmesi ve dağıtık işlem karmaşıklığı getirir."
            )
        ),
        vocabularyAnnotations = listOf(
            VocabularyAnnotation(
                word = "trade-off",
                vocabId = "vocab.trade-off",
                contextDefinitionEn = "A balance achieved between two desirable but incompatible features.",
                contextMeaningTr = "Ödünleşim, karşılıklı denge."
            )
        ),
        comprehensionQuestions = listOf(
            ReadingComprehensionQuestion(
                id = "q_micro_01",
                questionEn = "What operational cost do distributed microservices introduce?",
                options = listOf("Network latency and transaction complexity", "Cheaper physical hardware"),
                correctAnswer = "Network latency and transaction complexity",
                explanationEn = "Paragraph 2 states that distributed systems introduce network latency and transaction complexity.",
                explanationTr = "2. paragrafta dağıtık sistemlerin ağ gecikmesi ve işlem karmaşıklığı getirdiği ifade edilmiştir."
            )
        ),
        topicTags = listOf("microservices", "architecture"),
        relatedIds = listOf("vocab.trade-off")
    )

    private val fakeReadingRepository = object : ReadingRepository {
        override fun getAllArticles(): Flow<List<ReadingArticle>> = flowOf(listOf(mockReadingArticle))
        override fun getArticlesByLevel(level: String): Flow<List<ReadingArticle>> =
            flowOf(if (level == "B2") listOf(mockReadingArticle) else emptyList())
        override fun getArticleById(id: String): Flow<ReadingArticle?> =
            flowOf(if (id == mockReadingArticle.id) mockReadingArticle else null)
        override fun getArticlesByCategory(category: String): Flow<List<ReadingArticle>> =
            flowOf(if (category == "technology") listOf(mockReadingArticle) else emptyList())
        override fun searchArticles(query: String): Flow<List<ReadingArticle>> =
            flowOf(listOf(mockReadingArticle))
        override suspend fun getArticleCount(): Int = 1
    }

    private val mockListeningScenario = ListeningScenario(
        id = "listening.b2.incident-triage",
        title = "Production Database Outage Triage",
        cefrLevel = "B2",
        category = "incident_response",
        scenarioContext = "An emergency incident response bridge triaging connection pool exhaustion.",
        speakers = listOf(
            Speaker(id = "kaan", name = "Kaan", role = "Staff SRE", accent = "Turkish"),
            Speaker(id = "rachel", name = "Rachel", role = "Principal Backend Lead", accent = "American")
        ),
        audioRef = "audio/listening/b2_incident_triage.mp3",
        durationSeconds = 55,
        transcriptItems = listOf(
            TranscriptItem(
                index = 1,
                speakerId = "kaan",
                startMs = 0L,
                endMs = 7800L,
                textEn = "Rachel, we have an active P1 incident.",
                textTr = "Rachel, aktif bir 1. öncelikli krizimiz var."
            ),
            TranscriptItem(
                index = 2,
                speakerId = "rachel",
                startMs = 8100L,
                endMs = 15900L,
                textEn = "What is causing the bottleneck?",
                textTr = "Darboğaza ne sebep oluyor?"
            )
        ),
        keyVocabulary = emptyList(),
        comprehensionQuestions = listOf(
            com.seanora.fluentai.core.model.ListeningComprehensionQuestion(
                id = "q_triage_01",
                questionEn = "What incident priority is active?",
                options = listOf("P1 incident", "P4 minor task"),
                correctAnswer = "P1 incident",
                explanationEn = "Kaan explicitly reports an active P1 incident.",
                explanationTr = "Kaan açıkça aktif bir P1 krizinden bahsetmektedir."
            )
        ),
        topicTags = listOf("incident-response"),
        relatedIds = emptyList()
    )

    private val fakeListeningRepository = object : ListeningRepository {
        override fun getAllScenarios(): Flow<List<ListeningScenario>> = flowOf(listOf(mockListeningScenario))
        override fun getScenariosByLevel(level: String): Flow<List<ListeningScenario>> =
            flowOf(if (level == "B2") listOf(mockListeningScenario) else emptyList())
        override fun getScenarioById(id: String): Flow<ListeningScenario?> =
            flowOf(if (id == mockListeningScenario.id) mockListeningScenario else null)
        override fun getScenariosByCategory(category: String): Flow<List<ListeningScenario>> =
            flowOf(if (category == "incident_response") listOf(mockListeningScenario) else emptyList())
        override fun searchScenarios(query: String): Flow<List<ListeningScenario>> =
            flowOf(listOf(mockListeningScenario))
        override suspend fun getScenarioCount(): Int = 1
    }

    private val fakePlaybackState = MutableStateFlow(PlaybackState())
    private var lastAudioSeekMs: Long = 0L

    private val fakeAudioEngine = object : AudioPlaybackEngine {
        override val playbackState = fakePlaybackState
        override fun play(uri: String, startPositionMs: Long) {
            lastAudioSeekMs = startPositionMs
            fakePlaybackState.value = PlaybackState(
                isPlaying = true,
                currentMediaUri = uri,
                currentPositionMs = startPositionMs,
                durationMs = 55000L
            )
        }
        override fun pause() {
            fakePlaybackState.value = fakePlaybackState.value.copy(isPlaying = false)
        }
        override fun resume() {
            fakePlaybackState.value = fakePlaybackState.value.copy(isPlaying = true)
        }
        override fun seekTo(positionMs: Long) {
            lastAudioSeekMs = positionMs
            fakePlaybackState.value = fakePlaybackState.value.copy(currentPositionMs = positionMs)
        }
        override fun stop() {
            fakePlaybackState.value = PlaybackState(isPlaying = false, currentPositionMs = 0L)
        }
        override fun release() {
            stop()
        }
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        evidenceList.clear()
        masteryMap.clear()
        mistakeMap.clear()
        reviewMap.clear()
        fakePlaybackState.value = PlaybackState()
        lastAudioSeekMs = 0L
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun readingSession_fullLearningLoop_persistsEvidenceAndUpdatesMastery() = runTest {
        val readingViewModel = ReadingSessionViewModel(fakeReadingRepository, recordLearningEvidenceUseCase)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            readingViewModel.uiState.collect()
        }
        readingViewModel.openArticle(mockReadingArticle.id, ReadingSessionMode.STANDARD)
        testScheduler.advanceUntilIdle()

        // 1. The requested semantic ID owns the canonical session.
        val initialState = readingViewModel.uiState.value
        assertEquals("reading.b2.microservices-tradeoffs", initialState.requestedArticleId)
        assertEquals("reading.b2.microservices-tradeoffs", initialState.article?.id)
        assertEquals(ReadingSessionMode.STANDARD, initialState.mode)
        assertEquals(ReadingSessionSection.ARTICLE, initialState.section)

        // 2. Local section navigation keeps the same article.
        readingViewModel.selectSection(ReadingSessionSection.VOCABULARY)
        assertEquals(mockReadingArticle.id, readingViewModel.uiState.value.article?.id)
        readingViewModel.selectSection(ReadingSessionSection.QUESTIONS)

        // 3. Answer the comprehension check once.
        val question = mockReadingArticle.comprehensionQuestions[0]
        readingViewModel.selectAnswer(question.id, "Network latency and transaction complexity")
        readingViewModel.checkAnswer()
        testScheduler.advanceUntilIdle()
        assertTrue(question.id in readingViewModel.uiState.value.submittedQuestionIds)
        assertEquals(true, readingViewModel.uiState.value.answerCorrectness[question.id])

        // 4. Evidence recorded in user database.
        assertEquals(1, evidenceList.size)
        val evidence = evidenceList[0]
        assertEquals("reading.b2.microservices-tradeoffs", evidence.contentId)
        assertEquals("reading", evidence.domain)
        assertTrue(evidence.isCorrect)
        assertEquals(1.0f, evidence.score)

        // 5. Deterministic mastery updated.
        val snapshot = masteryMap[mockReadingArticle.id]
        assertNotNull(snapshot)
        assertEquals("reading", snapshot!!.domain)
        assertEquals(1, snapshot.totalAttempts)
        assertEquals(1, snapshot.correctAttempts)
    }

    @Test
    fun listeningModule_fullLearningLoop_withHiddenTranscriptAndSentenceReplay() = runTest {
        val listeningViewModel = ListeningViewModel(
            fakeListeningRepository,
            fakeAudioEngine,
            recordLearningEvidenceUseCase,
            ListeningFeatureEngine(),
        )

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            listeningViewModel.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        listeningViewModel.selectScenarioById(mockListeningScenario.id)
        testScheduler.advanceUntilIdle()

        // 1. Initial State: Hidden transcript requirement
        val initialState = listeningViewModel.uiState.value
        assertEquals(1, initialState.scenarios.size)
        assertEquals("listening.b2.incident-triage", initialState.selectedScenario?.id)
        assertFalse("Transcript must be hidden initially to train user ear", initialState.isTranscriptRevealed)

        // 2. Audio playback control
        assertFalse(initialState.isPlaying)
        listeningViewModel.togglePlayPause()
        testScheduler.advanceUntilIdle()
        assertTrue(fakePlaybackState.value.isPlaying)

        // 3. Sentence replay
        val secondSentence = mockListeningScenario.transcriptItems[1]
        listeningViewModel.replaySentence(secondSentence)
        testScheduler.advanceUntilIdle()
        assertEquals(8100L, lastAudioSeekMs)

        // 4. Reveal transcript
        listeningViewModel.toggleTranscriptReveal()
        testScheduler.advanceUntilIdle()
        assertTrue(listeningViewModel.uiState.value.isTranscriptRevealed)

        // 5. Answer comprehension check
        val question = mockListeningScenario.comprehensionQuestions[0]
        listeningViewModel.selectAnswer(question.id, "P1 incident")
        testScheduler.advanceUntilIdle()

        listeningViewModel.submitAnswer(question)
        testScheduler.advanceUntilIdle()

        // 6. Evidence recorded in user database
        assertEquals(1, evidenceList.size)
        val evidence = evidenceList[0]
        assertEquals("listening.b2.incident-triage", evidence.contentId)
        assertEquals("listening", evidence.domain)
        assertTrue(evidence.isCorrect)
        assertEquals(1.0f, evidence.score)

        // 7. Deterministic mastery updated
        val snapshot = masteryMap[mockListeningScenario.id]
        assertNotNull(snapshot)
        assertEquals("listening", snapshot!!.domain)
        assertEquals(1, snapshot.totalAttempts)
        assertEquals(1, snapshot.correctAttempts)
    }
}
