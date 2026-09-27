package com.seanora.fluentai

import com.seanora.fluentai.ai.analysis.MockSpeakingAnalysisClient
import com.seanora.fluentai.ai.live.LiveCoachSessionManager
import com.seanora.fluentai.ai.live.MockLiveAiClient
import com.seanora.fluentai.ai.live.TranscriptEntry
import com.seanora.fluentai.ai.live.TranscriptRole
import com.seanora.fluentai.ai.live.VoiceUiState
import com.seanora.fluentai.core.audio.FakeAudioCaptureEngine
import com.seanora.fluentai.core.audio.FakeStreamAudioPlaybackEngine
import com.seanora.fluentai.core.model.CorrectionMode
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.MistakeStage
import com.seanora.fluentai.core.model.NaturalAlternative
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.SpeakingCategory
import com.seanora.fluentai.core.model.SpeakingGrammarObservation
import com.seanora.fluentai.core.model.SpeakingScenario
import com.seanora.fluentai.core.model.SpeakingSessionAnalysis
import com.seanora.fluentai.core.model.SpeakingVocabObservation
import com.seanora.fluentai.core.model.SuggestedPracticeItem
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.data.repository.SpeakingRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.data.user.dao.SpeakingSessionDao
import com.seanora.fluentai.data.user.entity.SpeakingSessionRecordEntity
import com.seanora.fluentai.domain.mastery.MasteryEngine
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import com.seanora.fluentai.domain.mistakes.MistakeEngine
import com.seanora.fluentai.domain.review.ReviewScheduler
import com.seanora.fluentai.domain.speaking.ProcessSpeakingAnalysisUseCase
import com.seanora.fluentai.domain.speaking.SpeakingPromptBuilder
import com.seanora.fluentai.feature.speaking.SpeakingViewModel
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

/**
 * Comprehensive Phase 6 Integrity Test.
 *
 * Verifies all Phase 6 exit criteria per PHASES.md & AGENTS.md:
 * 1. Speaking scenarios across 8 categories and CEFR levels A2-C2.
 * 2. 4 Correction Modes (Flow, Coach, Drill, Mock).
 * 3. Dynamic coaching prompt engine targeting CEFR, correction modes, curriculum concepts, and Turkish learner traps.
 * 4. Deterministic offline post-session AI analysis (strengths, grammar with Turkish L1 transfer, vocab upgrades, natural alternatives).
 * 5. Learning model integration: Speaking session updates the same Room-backed LearningEvidence, MasterySnapshot,
 *    and MistakeRecord structures used by local modules.
 * 6. End-to-end ViewModel and UI state lifecycle.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class Phase6IntegrityTest {

    private val testDispatcher = StandardTestDispatcher()

    // In-memory repositories for learning model verification
    private val evidenceList = mutableListOf<LearningEvidence>()
    private val masteryMap = mutableMapOf<String, MasterySnapshot>()
    private val mistakeList = mutableListOf<MistakeRecord>()
    private val reviewMap = mutableMapOf<String, ReviewSchedule>()
    private val sessionRecords = mutableListOf<SpeakingSessionRecordEntity>()

    private val userLearningRepo = object : UserLearningRepository {
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
            flowOf(mistakeList.filter { it.stage != MistakeStage.RESOLVED })

        override fun getAllMistakes(): Flow<List<MistakeRecord>> =
            flowOf(mistakeList)

        override suspend fun findMistake(contentId: String, trapType: String): MistakeRecord? =
            mistakeList.find { it.contentId == contentId && it.trapType == trapType }

        override suspend fun saveMistake(mistake: MistakeRecord): Long {
            val idx = mistakeList.indexOfFirst {
                it.id == mistake.id || (it.contentId == mistake.contentId && it.trapType == mistake.trapType)
            }
            if (idx >= 0) {
                mistakeList[idx] = mistake
                return mistake.id
            } else {
                val newId = (mistakeList.size + 1).toLong()
                val assigned = mistake.copy(id = newId)
                mistakeList.add(assigned)
                return newId
            }
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
            flowOf(UserProfile(id = id, targetCefrLevel = "C1", estimatedOverallLevel = "B2"))

        override suspend fun getUserProfileSync(id: String): UserProfile? =
            UserProfile(id = id, targetCefrLevel = "C1", estimatedOverallLevel = "B2")

        override suspend fun saveUserProfile(profile: UserProfile) {}
    }

    private val speakingSessionDao = object : SpeakingSessionDao {
        override fun getAllSessions(): Flow<List<SpeakingSessionRecordEntity>> = flowOf(sessionRecords)
        override fun getRecentSessions(limit: Int): Flow<List<SpeakingSessionRecordEntity>> = flowOf(sessionRecords)
        override fun getSessionById(id: String): Flow<SpeakingSessionRecordEntity?> =
            flowOf(sessionRecords.find { it.id == id })

        override suspend fun getSessionByIdSync(id: String): SpeakingSessionRecordEntity? =
            sessionRecords.find { it.id == id }

        override fun getSessionsForScenario(scenarioId: String): Flow<List<SpeakingSessionRecordEntity>> =
            flowOf(sessionRecords.filter { it.scenarioId == scenarioId })

        override suspend fun getSessionCount(): Int = sessionRecords.size
        override suspend fun insertSession(session: SpeakingSessionRecordEntity) {
            sessionRecords.add(session)
        }
    }

    private val sampleScenarios = listOf(
        SpeakingScenario(
            id = "speaking.b2.meeting.standup-001",
            title = "Agile Standup & Technical Blockers",
            category = SpeakingCategory.MEETING,
            cefrLevel = "B2",
            systemPrompt = "Act as the Scrum Master in a daily engineering standup.",
            contextDescription = "Sprint retrospective and daily standup blockers.",
            recommendedCorrectionMode = CorrectionMode.COACH,
            targetGrammarIds = listOf("grammar.b2.conditionals.second", "grammar.b2.present-perfect-vs-past-simple"),
            targetVocabIds = listOf("vocab.b2.bottleneck", "vocab.b2.scalability"),
            suggestedStarterPhrases = listOf("Today I worked on...", "A major blocker is..."),
            voiceName = "Puck"
        ),
        SpeakingScenario(
            id = "speaking.c1.jobinterview.system-design-001",
            title = "System Design Interview: Event-Driven Architecture",
            category = SpeakingCategory.JOB_INTERVIEW,
            cefrLevel = "C1",
            systemPrompt = "Act as the Principal Staff Engineer interviewing the candidate.",
            contextDescription = "High-stakes technical interview on distributed systems.",
            recommendedCorrectionMode = CorrectionMode.MOCK,
            targetGrammarIds = listOf("grammar.c1.inversion", "grammar.c1.cleft-sentences"),
            targetVocabIds = listOf("vocab.c1.concurrency", "vocab.c1.resilience"),
            suggestedStarterPhrases = listOf("In our distributed topology..."),
            voiceName = "Fenrir"
        ),
        SpeakingScenario(
            id = "speaking.b1.career.salary-negotiation-001",
            title = "Salary Review Discussion",
            category = SpeakingCategory.CAREER,
            cefrLevel = "B1",
            systemPrompt = "Act as the Engineering Director conducting the annual compensation review.",
            contextDescription = "Annual 1-on-1 performance and compensation conversation.",
            recommendedCorrectionMode = CorrectionMode.FLOW,
            targetGrammarIds = listOf("grammar.b1.modal-verbs"),
            targetVocabIds = listOf("vocab.b1.compensation"),
            suggestedStarterPhrases = listOf("I would like to discuss my compensation..."),
            voiceName = "Aoede"
        ),
        SpeakingScenario(
            id = "speaking.b2.grammar.conditionals-drill-001",
            title = "Conditional Hypothesis Drill",
            category = SpeakingCategory.GRAMMAR_DRILL,
            cefrLevel = "B2",
            systemPrompt = "Challenge the user with hypothetical scenarios requiring conditional structures.",
            contextDescription = "Intensive grammar drill.",
            recommendedCorrectionMode = CorrectionMode.DRILL,
            targetGrammarIds = listOf("grammar.b2.conditionals.third"),
            targetVocabIds = emptyList(),
            suggestedStarterPhrases = listOf("If we had scaled earlier..."),
            voiceName = "Puck"
        )
    )

    private val speakingRepo = object : SpeakingRepository {
        override fun getAllScenarios(): Flow<List<SpeakingScenario>> = flowOf(sampleScenarios)
        override fun getScenariosByLevel(level: String): Flow<List<SpeakingScenario>> =
            flowOf(sampleScenarios.filter { it.cefrLevel == level })

        override fun getScenariosByCategory(category: SpeakingCategory): Flow<List<SpeakingScenario>> =
            flowOf(sampleScenarios.filter { it.category == category })

        override fun getScenarioById(id: String): Flow<SpeakingScenario?> =
            flowOf(sampleScenarios.find { it.id == id })

        override suspend fun getScenarioByIdSync(id: String): SpeakingScenario? =
            sampleScenarios.find { it.id == id }

        override fun searchScenarios(query: String): Flow<List<SpeakingScenario>> =
            flowOf(sampleScenarios.filter { it.title.contains(query, ignoreCase = true) })

        override suspend fun getScenarioCount(): Int = sampleScenarios.size
    }

    private lateinit var masteryEngine: MasteryEngine
    private lateinit var mistakeEngine: MistakeEngine
    private lateinit var reviewScheduler: ReviewScheduler
    private lateinit var recordLearningEvidenceUseCase: RecordLearningEvidenceUseCase
    private lateinit var processSpeakingAnalysisUseCase: ProcessSpeakingAnalysisUseCase
    private lateinit var promptBuilder: SpeakingPromptBuilder
    private lateinit var analysisClient: MockSpeakingAnalysisClient

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        evidenceList.clear()
        masteryMap.clear()
        mistakeList.clear()
        reviewMap.clear()
        sessionRecords.clear()

        reviewScheduler = ReviewScheduler()
        masteryEngine = MasteryEngine(reviewScheduler)
        mistakeEngine = MistakeEngine()
        recordLearningEvidenceUseCase = RecordLearningEvidenceUseCase(
            userLearningRepository = userLearningRepo,
            masteryEngine = masteryEngine,
            mistakeEngine = mistakeEngine
        )
        processSpeakingAnalysisUseCase = ProcessSpeakingAnalysisUseCase(
            recordLearningEvidenceUseCase = recordLearningEvidenceUseCase,
            speakingSessionDao = speakingSessionDao
        )
        promptBuilder = SpeakingPromptBuilder()
        analysisClient = MockSpeakingAnalysisClient()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun criterion1_allEightSpeakingCategoriesAndFourCorrectionModesExist() {
        val categories = SpeakingCategory.values()
        assertEquals(8, categories.size)
        val expectedCategories = setOf(
            "FREE_TALK", "DAILY_LIFE", "CAREER", "JOB_INTERVIEW",
            "MEETING", "IELTS", "VOCAB_DRILL", "GRAMMAR_DRILL"
        )
        assertEquals(expectedCategories, categories.map { it.name }.toSet())

        val modes = CorrectionMode.values()
        assertEquals(4, modes.size)
        val expectedModes = setOf("FLOW", "COACH", "DRILL", "MOCK")
        assertEquals(expectedModes, modes.map { it.name }.toSet())
    }

    @Test
    fun criterion2_promptBuilderIntegratesTargetCurriculumCorrectionModesAndTurkishTraps() {
        val scenario = sampleScenarios.first()
        val userProfile = UserProfile(
            targetCefrLevel = "C1",
            estimatedOverallLevel = "B2"
        )
        val activeMistakes = listOf(
            MistakeRecord(
                id = 1,
                contentId = "grammar.b2.present-perfect-vs-past-simple",
                trapType = "since-for-confusion",
                errorDescription = "Used 'since' with duration instead of 'for'",
                userAnswer = "I have been working here since two years",
                correctAnswer = "I have been working here for two years",
                stage = MistakeStage.RECURRING,
                occurrenceCount = 3,
                firstObservedAt = System.currentTimeMillis() - 86400000L,
                lastObservedAt = System.currentTimeMillis()
            )
        )

        // 1. COACH mode prompt check
        val coachConfig = promptBuilder.createLiveConfig(
            scenario = scenario,
            mode = CorrectionMode.COACH,
            profile = userProfile,
            recentMistakes = activeMistakes
        )
        assertTrue(coachConfig.systemPrompt.contains("Active Correction Mode: Coach"))
        assertTrue(coachConfig.systemPrompt.contains("grammar.b2.conditionals.second"))
        assertTrue(coachConfig.systemPrompt.contains("since-for-confusion"))

        // 2. MOCK mode prompt check
        val mockConfig = promptBuilder.createLiveConfig(
            scenario = scenario,
            mode = CorrectionMode.MOCK,
            profile = userProfile,
            recentMistakes = emptyList()
        )
        assertTrue(mockConfig.systemPrompt.contains("Active Correction Mode: Mock"))
        assertTrue(mockConfig.systemPrompt.contains("break character", ignoreCase = true))

        // 3. DRILL mode prompt check
        val drillConfig = promptBuilder.createLiveConfig(
            scenario = scenario,
            mode = CorrectionMode.DRILL,
            profile = userProfile,
            recentMistakes = emptyList()
        )
        assertTrue(drillConfig.systemPrompt.contains("Active Correction Mode: Drill"))
        assertTrue(drillConfig.systemPrompt.contains("prompt them to restate"))
    }

    @Test
    fun criterion3_mockSpeakingAnalysisClientProducesStructuredLanguageObservations() = runTest(testDispatcher) {
        val scenario = sampleScenarios.first()
        val transcript = listOf(
            TranscriptEntry(id = "t1", role = TranscriptRole.COACH, text = "How are you resolving the database latency issue?"),
            TranscriptEntry(id = "t2", role = TranscriptRole.USER, text = "I think that we have a big problem. I am agree with the team. I have worked on this since 3 years and it is a big problem for scalability.")
        )

        val result = analysisClient.analyzeSession(
            scenario = scenario,
            mode = CorrectionMode.COACH,
            transcript = transcript,
            durationSeconds = 120
        )

        assertTrue(result.isSuccess)
        val analysis = result.getOrThrow()
        assertEquals(scenario.id, analysis.scenarioId)
        assertTrue(analysis.strengths.isNotEmpty())
        assertTrue(analysis.grammarObservations.isNotEmpty())
        assertTrue(analysis.vocabularyObservations.isNotEmpty())
        assertTrue(analysis.naturalAlternatives.isNotEmpty())
        assertTrue(analysis.suggestedPractice.isNotEmpty())

        // Verify Turkish L1 transfer identification (tense duration or be verb overuse)
        val turkishGrammarTrap = analysis.grammarObservations.find { it.trapType != null }
        assertNotNull("Should identify L1 Turkish transfer error", turkishGrammarTrap)
        assertTrue(
            turkishGrammarTrap!!.explanation.contains("Turkish", ignoreCase = true) ||
            turkishGrammarTrap.explanation.contains("duration", ignoreCase = true) ||
            turkishGrammarTrap.explanation.contains("agree", ignoreCase = true)
        )

        // Verify vocabulary upgrades
        val vocabUpgrade = analysis.vocabularyObservations.find { it.suggestedBetterWord.isNotBlank() }
        assertNotNull("Should identify vocabulary upgrade candidates", vocabUpgrade)
    }

    @Test
    fun criterion4_speakingSessionAnalysisMeaningfullyUpdatesSameLearningModel() = runTest(testDispatcher) {
        val scenario = sampleScenarios.first()
        val analysis = SpeakingSessionAnalysis(
            id = "analysis_session_001",
            sessionId = "live_session_123",
            scenarioId = scenario.id,
            scenarioTitle = scenario.title,
            timestamp = System.currentTimeMillis(),
            durationSeconds = 180,
            turnsCount = 6,
            overallFeedback = "Commendable agility communication with notable lexical precision.",
            estimatedTurnCefr = "B2",
            fluencyScore = 0.80f,
            vocabularyScore = 0.82f,
            grammarScore = 0.74f,
            coherenceScore = 0.84f,
            strengths = listOf("Clear cadence", "Direct answer"),
            grammarObservations = listOf(
                SpeakingGrammarObservation(
                    id = "obs_g1",
                    userUtterance = "I have worked since 2 years on this",
                    errorSnippet = "since 2 years",
                    correctedSnippet = "for 2 years",
                    explanation = "L1 Turkish transfer: In English, duration uses 'for', not 'since'.",
                    targetGrammarId = "grammar.b2.present-perfect-vs-past-simple",
                    trapType = "tense_duration"
                )
            ),
            vocabularyObservations = listOf(
                SpeakingVocabObservation(
                    id = "obs_v1",
                    usedWord = "big problem",
                    suggestedBetterWord = "critical bottleneck",
                    explanation = "In executive tech discussions, 'critical bottleneck' is more precise.",
                    targetVocabId = "vocab.b2.bottleneck",
                    register = "executive"
                )
            ),
            naturalAlternatives = listOf(
                NaturalAlternative(
                    originalUtterance = "We encountered a constraint",
                    naturalAlternative = "We hit a bottleneck",
                    explanation = "More natural phrasing in agile standups",
                    register = "business_casual"
                )
            ),
            suggestedPractice = listOf(
                SuggestedPracticeItem(
                    title = "Present Perfect vs Past Simple",
                    description = "Review time duration expressions with since and for",
                    domain = "grammar",
                    contentId = "grammar.b2.present-perfect-vs-past-simple"
                )
            )
        )

        // Process analysis through learning model use case
        val processResult = processSpeakingAnalysisUseCase(analysis)

        // 1. Session record persisted in UserDatabase DAO
        assertEquals("analysis_session_001", processResult.sessionRecordId)
        val savedSession = speakingSessionDao.getSessionByIdSync("analysis_session_001")
        assertNotNull("Session entity must be persisted in SpeakingSessionDao", savedSession)
        assertEquals(scenario.id, savedSession!!.scenarioId)
        assertEquals(0.80f, savedSession.fluencyScore, 0.01f)

        // 2. Speaking LearningEvidence recorded
        val speakingEvidence = evidenceList.find { it.contentId == scenario.id && it.domain == "speaking" }
        assertNotNull("Speaking evidence must be recorded", speakingEvidence)
        assertTrue(speakingEvidence!!.isCorrect)
        assertEquals(0.80f, speakingEvidence.score, 0.05f)

        // 3. Speaking MasterySnapshot updated deterministically
        val speakingSnapshot = masteryMap[scenario.id]
        assertNotNull("Deterministic MasterySnapshot must be created for speaking scenario", speakingSnapshot)
        assertEquals("speaking", speakingSnapshot!!.domain)
        assertTrue(speakingSnapshot.totalAttempts >= 1)
        assertTrue(speakingSnapshot.level > 0.0f)

        // 4. Grammar observation recorded as LearningEvidence & Mistake in Mistake Bank
        val grammarEvidence = evidenceList.find { it.contentId == "grammar.b2.present-perfect-vs-past-simple" }
        assertNotNull("Grammar evidence from speaking must be recorded", grammarEvidence)
        assertFalse("Incorrect grammar observation must be marked false", grammarEvidence!!.isCorrect)

        val grammarMistake = mistakeList.find { it.contentId == "grammar.b2.present-perfect-vs-past-simple" }
        assertNotNull("Grammar mistake from speaking must enter Mistake Bank", grammarMistake)
        assertTrue(grammarMistake!!.stage == MistakeStage.OBSERVED || grammarMistake.stage == MistakeStage.POSSIBLE)
        assertEquals("tense_duration", grammarMistake.trapType)

        // 5. Vocab observation recorded as LearningEvidence
        val vocabEvidence = evidenceList.find { it.contentId == "vocab.b2.bottleneck" }
        assertNotNull("Vocab evidence from speaking must be recorded", vocabEvidence)
        assertEquals("vocabulary", vocabEvidence!!.domain)

        // 6. Review schedule scheduled by ReviewScheduler
        val review = reviewMap[scenario.id]
        assertNotNull("Speaking scenario must have Spaced Repetition review scheduled", review)
        assertTrue(review!!.nextReviewDueTimestamp > System.currentTimeMillis())
    }

    @Test
    fun criterion5_completeSpeakingViewModelLifecycleWithReportDisplay() = runTest(testDispatcher) {
        val captureEngine = FakeAudioCaptureEngine()
        val playbackEngine = FakeStreamAudioPlaybackEngine()
        val mockLiveAiClient = MockLiveAiClient(dispatcher = testDispatcher).apply { speechDelayMs = 0L }

        val sessionManager = LiveCoachSessionManager(
            audioCaptureEngine = captureEngine,
            playbackEngine = playbackEngine,
            liveAiClient = mockLiveAiClient,
            dispatcher = testDispatcher
        )

        val viewModel = SpeakingViewModel(
            sessionManager = sessionManager,
            speakingRepository = speakingRepo,
            promptBuilder = promptBuilder,
            analysisClient = analysisClient,
            processSpeakingAnalysisUseCase = processSpeakingAnalysisUseCase,
            userLearningRepository = userLearningRepo
        )

        advanceUntilIdle()

        // 1. Initial State
        val initialState = viewModel.uiState.value
        assertEquals(VoiceUiState.IDLE, initialState.voiceState)
        assertFalse(initialState.isSessionActive)
        assertFalse(initialState.isReportVisible)
        assertEquals(4, initialState.availableScenarios.size)

        // 2. Select Scenario & Mode
        val targetScenario = sampleScenarios[1] // System Design Interview
        viewModel.selectScenario(targetScenario)
        viewModel.selectCorrectionMode(CorrectionMode.MOCK)

        assertEquals(targetScenario.id, viewModel.uiState.value.selectedScenario?.id)
        assertEquals(CorrectionMode.MOCK, viewModel.uiState.value.selectedMode)

        // 3. Start Session
        viewModel.startSession()
        advanceUntilIdle()

        val activeState = viewModel.uiState.value
        assertTrue(activeState.isSessionActive)
        assertEquals(VoiceUiState.LISTENING, activeState.voiceState)
        assertTrue(captureEngine.isRecording.value)

        // 4. Simulate User Speech containing Turkish Trap and basic vocab words for upgrades
        viewModel.sendTextMessage("I am agree that latency is a big problem and we should make better the architecture.")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.transcript.any { it.role == TranscriptRole.USER })

        // 5. End Session -> Triggers Analysis & Report
        viewModel.endSession()
        advanceUntilIdle()

        val endedState = viewModel.uiState.value
        assertFalse("Session must be inactive after end", endedState.isSessionActive)
        assertTrue("Report must be visible", endedState.isReportVisible)
        assertFalse("Analyzing flag must be reset", endedState.isAnalyzing)
        assertNotNull("Post session analysis must be populated", endedState.postSessionAnalysis)
        assertNotNull("Processed result must be populated", endedState.analysisResult)

        // Verify learning evidence count
        val result = endedState.analysisResult!!
        assertTrue(result.recordedGrammarEvidenceCount >= 1)
        assertTrue(result.recordedVocabEvidenceCount >= 1)
        assertTrue(evidenceList.isNotEmpty())

        // 6. Dismiss Report
        viewModel.dismissReport()
        assertFalse(viewModel.uiState.value.isReportVisible)
    }
}
