package com.seanora.fluentai

import com.seanora.fluentai.core.model.GrammarFeatureState
import com.seanora.fluentai.core.model.GrammarLesson
import com.seanora.fluentai.core.model.GrammarResumeDestination
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.ListeningScenario
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.MistakeStage
import com.seanora.fluentai.core.model.ReadingArticle
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.SpeakingCategory
import com.seanora.fluentai.core.model.SpeakingScenario
import com.seanora.fluentai.core.model.TodayItemType
import com.seanora.fluentai.core.model.TodayPracticePlan
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.core.model.VocabItem
import com.seanora.fluentai.core.model.VocabularyFeatureState
import com.seanora.fluentai.core.model.VocabularyPracticeMode
import com.seanora.fluentai.core.model.VocabularyResumeDestination
import com.seanora.fluentai.data.repository.GrammarFeatureStateRepository
import com.seanora.fluentai.data.repository.GrammarRepository
import com.seanora.fluentai.data.repository.ListeningRepository
import com.seanora.fluentai.data.repository.ReadingRepository
import com.seanora.fluentai.data.repository.SpeakingRepository
import com.seanora.fluentai.data.repository.TodayPracticeRepositoryImpl
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.data.repository.VocabularyFeatureStateRepository
import com.seanora.fluentai.data.repository.VocabularyRepository
import com.seanora.fluentai.data.repository.currentDateString
import com.seanora.fluentai.domain.mastery.MasteryEngine
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import com.seanora.fluentai.domain.mistakes.MistakeEngine
import com.seanora.fluentai.domain.progress.ProgressEngine
import com.seanora.fluentai.domain.today.TodayPlanner
import com.seanora.fluentai.domain.today.TodayPlannerInput
import com.seanora.fluentai.feature.home.data.RealHomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class Phase7IntegrityTest {

    private lateinit var planner: TodayPlanner

    private val testUser = UserProfile(
        id = "white_collar_dennis",
        targetCefrLevel = "C1",
        estimatedOverallLevel = "B2",
        estimatedVocabLevel = "B2",
        estimatedGrammarLevel = "B1",
        estimatedSpeakingLevel = "B1",
        dailyGoalMinutes = 20,
        learningInterests = listOf("technology", "leadership", "business"),
        streakDays = 5,
        lastActiveDate = "2026-09-24",
    )

    private val vocabPool = listOf(
        VocabItem(
            id = "vocab.resilient",
            headword = "resilient",
            cefrLevel = "B2",
            partOfSpeech = "adjective",
            phonetic = "/rɪˈzɪl.jənt/",
            definitionEn = "Able to recover quickly from difficulties.",
            meaningTr = "Dirençli",
            topicTags = listOf("leadership")
        ),
        VocabItem(
            id = "vocab.consensus",
            headword = "consensus",
            cefrLevel = "C1",
            partOfSpeech = "noun",
            phonetic = "/kənˈsen.səs/",
            definitionEn = "A general agreement.",
            meaningTr = "Uzlaşı",
            topicTags = listOf("business", "leadership")
        )
    )

    private val grammarPool = listOf(
        GrammarLesson(
            id = "grammar.c1.inversion",
            title = "Negative Inversion for Emphasis",
            cefrLevel = "C1",
            category = "Advanced Structure",
            summaryEn = "Inversion after negative adverbials.",
            summaryTr = "Olumsuz zarflarla ters devrik yapı.",
            explanationTr = "Açıklama"
        )
    )

    private val readingPool = listOf(
        ReadingArticle(
            id = "reading.c1.tech.cloud",
            title = "Cloud Resilience",
            cefrLevel = "C1",
            category = "Technology",
            summaryEn = "Distributed cloud systems.",
            summaryTr = "Bulut sistemleri.",
            wordCount = 400,
            estimatedReadingMinutes = 5,
            paragraphs = emptyList(),
            vocabularyAnnotations = emptyList(),
            comprehensionQuestions = emptyList(),
            topicTags = listOf("technology"),
            relatedIds = emptyList()
        )
    )

    private val listeningPool = listOf(
        ListeningScenario(
            id = "listening.b2.sync",
            title = "Sprint Planning Sync",
            cefrLevel = "B2",
            category = "Workplace",
            scenarioContext = "Agile sprint sync",
            speakers = emptyList(),
            audioRef = "ref_001",
            durationSeconds = 120,
            transcriptItems = emptyList(),
            keyVocabulary = emptyList(),
            comprehensionQuestions = emptyList(),
            topicTags = listOf("business"),
            relatedIds = emptyList()
        )
    )

    private val speakingPool = listOf(
        SpeakingScenario(
            id = "speaking.c1.interview.arch",
            title = "Principal Engineer Architecture Defense",
            category = SpeakingCategory.JOB_INTERVIEW,
            cefrLevel = "C1",
            systemPrompt = "You are interviewing a principal architect.",
            contextDescription = "Defending high throughput architecture.",
            topicTags = listOf("technology", "leadership"),
        )
    )

    @Before
    fun setUp() {
        planner = TodayPlanner()
    }

    @Test
    fun exitCriteria_planIsDeterministicAndReproducible() {
        val input = TodayPlannerInput(
            userProfile = testUser,
            availableVocab = vocabPool,
            availableGrammar = grammarPool,
            availableReadings = readingPool,
            availableListening = listeningPool,
            availableSpeaking = speakingPool,
            targetDate = "2026-09-25",
        )

        val plan1 = planner.createPlan(input)
        val plan2 = planner.createPlan(input)

        assertEquals("Plan IDs must match", plan1.id, plan2.id)
        assertEquals("Allocated minutes must match", plan1.allocatedMinutes, plan2.allocatedMinutes)
        assertEquals("Rationale must match", plan1.rationale, plan2.rationale)
        assertEquals("Items count must match", plan1.items.size, plan2.items.size)

        for (i in plan1.items.indices) {
            val item1 = plan1.items[i]
            val item2 = plan2.items[i]
            assertEquals(item1.id, item2.id)
            assertEquals(item1.contentId, item2.contentId)
            assertEquals(item1.title, item2.title)
            assertEquals(item1.domain, item2.domain)
            assertEquals(item1.itemType, item2.itemType)
            assertEquals(item1.estimatedMinutes, item2.estimatedMinutes)
            assertEquals(item1.explanation, item2.explanation)
        }
    }

    @Test
    fun exitCriteria_planIsExplainableWithPedagogicalRationale() {
        val input = TodayPlannerInput(
            userProfile = testUser,
            dueReviews = listOf(
                ReviewSchedule(
                    contentId = "vocab.resilient",
                    domain = "vocabulary",
                    nextReviewDueTimestamp = 1000L,
                    intervalDays = 3f,
                    repetitionNumber = 1
                )
            ),
            activeMistakes = listOf(
                MistakeRecord(
                    id = 1L,
                    contentId = "grammar.c1.inversion",
                    trapType = "turkish-preposition-transfer",
                    stage = MistakeStage.RECURRING,
                    userAnswer = "depend of",
                    correctAnswer = "depend on",
                    occurrenceCount = 3,
                    errorDescription = "Incorrect preposition",
                    firstObservedAt = 1L,
                    lastObservedAt = 2L
                )
            ),
            availableVocab = vocabPool,
            availableGrammar = grammarPool,
            availableReadings = readingPool,
            availableListening = listeningPool,
            availableSpeaking = speakingPool,
            targetDate = "2026-09-25",
        )

        val plan = planner.createPlan(input)

        // Rationale must explain C1 goal and scheduled components
        assertTrue("Rationale must be non-empty", plan.rationale.isNotBlank())
        assertTrue("Rationale must mention C1 goal", plan.rationale.contains("C1"))
        assertTrue("Rationale must mention due review", plan.rationale.contains("retention"))
        assertTrue("Rationale must mention recurring trap", plan.rationale.contains("turkish-preposition-transfer"))

        // Every item must have a clear human-readable explanation
        plan.items.forEach { item ->
            assertTrue("Item explanation must not be blank", item.explanation.isNotBlank())
            when (item.itemType) {
                TodayItemType.REVIEW_DUE -> assertTrue(item.explanation.contains("Spaced repetition review"))
                TodayItemType.MISTAKE_TARGETED -> assertTrue(item.explanation.contains("Targeted drill for recurring trap"))
                TodayItemType.SPEAKING_SESSION -> assertTrue(item.explanation.contains("speaking practice"))
                TodayItemType.CURRICULUM_NEW -> assertTrue(item.explanation.contains("Curriculum progression"))
                TodayItemType.COMPREHENSION_READING -> assertTrue(item.explanation.contains("Reading comprehension"))
                TodayItemType.COMPREHENSION_LISTENING -> assertTrue(item.explanation.contains("Listening comprehension"))
                TodayItemType.WEAK_SKILL_BOOST -> assertTrue(item.explanation.contains("Reinforcement"))
            }
        }
    }

    @Test
    fun exitCriteria_planPrioritizesRealUserEvidence_RetentionAndMistakesFirst() {
        val input = TodayPlannerInput(
            userProfile = testUser,
            dueReviews = listOf(
                ReviewSchedule(
                    contentId = "vocab.resilient",
                    domain = "vocabulary",
                    nextReviewDueTimestamp = 1000L,
                    intervalDays = 4f,
                    repetitionNumber = 2
                )
            ),
            activeMistakes = listOf(
                MistakeRecord(
                    id = 2L,
                    contentId = "vocab.consensus",
                    trapType = "l1-interference-preposition",
                    stage = MistakeStage.RECURRING,
                    userAnswer = "reach to consensus",
                    correctAnswer = "reach a consensus",
                    occurrenceCount = 4,
                    errorDescription = "Unnecessary preposition",
                    firstObservedAt = 1L,
                    lastObservedAt = 2L
                )
            ),
            availableVocab = vocabPool,
            availableGrammar = grammarPool,
            availableReadings = readingPool,
            availableListening = listeningPool,
            availableSpeaking = speakingPool,
            targetDate = "2026-09-25",
        )

        val plan = planner.createPlan(input)

        // Priority 1: Due Spaced Review
        assertEquals("First item should be due review", TodayItemType.REVIEW_DUE, plan.items[0].itemType)
        assertEquals("vocab.resilient", plan.items[0].contentId)

        // Priority 2: Targeted Mistake Remediation
        assertEquals("Second item should be targeted mistake drill", TodayItemType.MISTAKE_TARGETED, plan.items[1].itemType)
        assertEquals("vocab.consensus", plan.items[1].contentId)

        // Priority 3: Speaking Coach Practice
        val speakingItem = plan.items.find { it.itemType == TodayItemType.SPEAKING_SESSION }
        assertNotNull("Speaking item must be planned", speakingItem)
        assertEquals("speaking.c1.interview.arch", speakingItem?.contentId)
    }

    @Test
    fun exitCriteria_planCompletionLifecycleUpdatesPlanMetrics() {
        val input = TodayPlannerInput(
            userProfile = testUser,
            availableVocab = vocabPool,
            availableSpeaking = speakingPool,
            targetDate = "2026-09-25",
        )

        val initialPlan = planner.createPlan(input)
        assertEquals(0, initialPlan.completedItemsCount)
        assertEquals(0f, initialPlan.progressPercent, 0.001f)
        assertFalse(initialPlan.isCompleted)

        val nextItem = initialPlan.nextUncompletedItem
        assertNotNull(nextItem)

        // Simulate completing first item
        val updatedItems = initialPlan.items.mapIndexed { index, item ->
            if (index == 0) item.copy(isCompleted = true, completedAt = 2000L) else item
        }
        val inProgressPlan = initialPlan.copy(items = updatedItems)

        assertEquals(1, inProgressPlan.completedItemsCount)
        assertTrue(inProgressPlan.progressPercent > 0f)

        // Complete all items
        val allDoneItems = initialPlan.items.map { it.copy(isCompleted = true, completedAt = 3000L) }
        val completedPlan = initialPlan.copy(items = allDoneItems)

        assertTrue("Plan should be marked completed when all items done", completedPlan.isCompleted)
        assertEquals(1f, completedPlan.progressPercent, 0.001f)
        assertEquals(null, completedPlan.nextUncompletedItem)
    }

    @Test
    fun exitCriteria_reproducibilityHoldsRegardlessOfReviewInputOrdering() {
        val reviewA = ReviewSchedule("vocab.consensus", "vocabulary", nextReviewDueTimestamp = 1000L, intervalDays = 2f, repetitionNumber = 1)
        val reviewB = ReviewSchedule("vocab.resilient", "vocabulary", nextReviewDueTimestamp = 1000L, intervalDays = 3f, repetitionNumber = 2)

        val planAB = planner.createPlan(
            TodayPlannerInput(
                userProfile = testUser,
                dueReviews = listOf(reviewA, reviewB),
                availableVocab = vocabPool,
                availableGrammar = grammarPool,
                targetDate = "2026-09-25",
            )
        )
        val planBA = planner.createPlan(
            TodayPlannerInput(
                userProfile = testUser,
                dueReviews = listOf(reviewB, reviewA),
                availableVocab = vocabPool,
                availableGrammar = grammarPool,
                targetDate = "2026-09-25",
            )
        )

        assertEquals(planAB.items.map { it.contentId }, planBA.items.map { it.contentId })
        assertEquals(planAB.rationale, planBA.rationale)
    }

    @Test
    fun exitCriteria_offlineMinimalContentFallsBackSafelyWithoutAI() {
        val plan = planner.createPlan(
            TodayPlannerInput(
                userProfile = testUser.copy(dailyGoalMinutes = 10),
                availableVocab = listOf(vocabPool.first()),
                targetDate = "2026-09-25",
            )
        )

        assertTrue("Fallback plan must contain at least one local activity", plan.items.isNotEmpty())
        assertEquals("vocab.resilient", plan.items.first().contentId)
        assertTrue(plan.rationale.isNotBlank())
    }

    @Test
    fun exitCriteria_endToEndTodayLoopUpdatesHomeMetricsEvidenceAndCompletion() = runTest {
        val today = currentDateString()
        val userRepo = InMemoryPhase7UserRepo(testUser.copy(lastActiveDate = "2026-01-01"))
        val todayRepo = TodayPracticeRepositoryImpl(
            todayPlanner = planner,
            userLearningRepository = userRepo,
            vocabularyRepository = object : VocabularyRepository {
                override fun getAllVocab() = flowOf(vocabPool)
                override fun getVocabByLevel(level: String) = flowOf(vocabPool)
                override fun getVocabById(id: String) = flowOf(vocabPool.find { it.id == id })
                override fun searchVocab(query: String) = flowOf(vocabPool)
                override suspend fun getVocabCount() = vocabPool.size
            },
            grammarRepository = object : GrammarRepository {
                override fun getAllLessons() = flowOf(grammarPool)
                override fun getLessonsByLevel(level: String) = flowOf(grammarPool)
                override fun getLessonsByCategory(category: String) = flowOf(grammarPool)
                override fun getLessonById(id: String) = flowOf(grammarPool.find { it.id == id })
                override fun searchLessons(query: String) = flowOf(grammarPool)
                override fun getExercisesForLesson(lessonId: String) = flowOf(emptyList<com.seanora.fluentai.core.model.Exercise>())
                override suspend fun getLessonCount() = grammarPool.size
            },
            readingRepository = object : ReadingRepository {
                override fun getAllArticles() = flowOf(readingPool)
                override fun getArticlesByLevel(level: String) = flowOf(readingPool)
                override fun getArticleById(id: String) = flowOf(readingPool.find { it.id == id })
                override fun getArticlesByCategory(category: String) = flowOf(readingPool)
                override fun searchArticles(query: String) = flowOf(readingPool)
                override suspend fun getArticleCount() = readingPool.size
            },
            listeningRepository = object : ListeningRepository {
                override fun getAllScenarios() = flowOf(listeningPool)
                override fun getScenariosByLevel(level: String) = flowOf(listeningPool)
                override fun getScenarioById(id: String) = flowOf(listeningPool.find { it.id == id })
                override fun getScenariosByCategory(category: String) = flowOf(listeningPool)
                override fun searchScenarios(query: String) = flowOf(listeningPool)
                override suspend fun getScenarioCount() = listeningPool.size
            },
            speakingRepository = object : SpeakingRepository {
                override fun getAllScenarios() = flowOf(speakingPool)
                override fun getScenariosByLevel(level: String) = flowOf(speakingPool)
                override fun getScenariosByCategory(category: SpeakingCategory) = flowOf(speakingPool)
                override fun getScenarioById(id: String) = flowOf(speakingPool.find { it.id == id })
                override suspend fun getScenarioByIdSync(id: String) = speakingPool.find { it.id == id }
                override fun searchScenarios(query: String) = flowOf(speakingPool)
                override suspend fun getScenarioCount() = speakingPool.size
            },
            recordLearningEvidence = RecordLearningEvidenceUseCase(userRepo, MasteryEngine(), MistakeEngine()),
        )
        val homeRepo = RealHomeRepository(
            todayPracticeRepository = todayRepo,
            userLearningRepository = userRepo,
            vocabularyStateRepository = object : VocabularyFeatureStateRepository {
                override fun observeState() = flowOf(VocabularyFeatureState())
                override suspend fun getState() = VocabularyFeatureState()
                override suspend fun saveDailyWord(date: String, contentId: String) = Unit
                override suspend fun saveResume(destination: VocabularyResumeDestination, contentId: String?, contextId: String?, practiceMode: VocabularyPracticeMode?) = Unit
            },
            grammarStateRepository = object : GrammarFeatureStateRepository {
                override fun observeState() = flowOf(GrammarFeatureState())
                override suspend fun getState() = GrammarFeatureState()
                override suspend fun saveResume(destination: GrammarResumeDestination, contentId: String, secondaryContentId: String?) = Unit
            },
            progressEngine = ProgressEngine(),
        )

        val initialHome = homeRepo.getTodayPlan().first()
        val plan = requireNotNull(initialHome.practicePlan)
        assertEquals(today, plan.date)
        assertTrue(initialHome.focusItems.isNotEmpty())
        assertEquals(0, initialHome.dailyMetrics.practiceMinutesToday)

        // Complete every item in Today's Practice
        plan.items.forEach { item ->
            homeRepo.markItemCompleted(plan.id, item.id)
        }

        val completedHome = homeRepo.getTodayPlan().first()
        assertEquals("complete", completedHome.heroPractice.id)
        assertEquals("Daily Practice Complete", completedHome.heroPractice.title)
        assertTrue(completedHome.focusItems.isEmpty())
        assertEquals(plan.totalMinutes, completedHome.dailyMetrics.practiceMinutesToday)
        assertTrue("Non-review completions must persist LearningEvidence", userRepo.evidences.isNotEmpty())
        assertEquals(today, userRepo.getUserProfileSync()?.lastActiveDate)
    }
}

private class InMemoryPhase7UserRepo(initialProfile: UserProfile) : UserLearningRepository {
    private val profileFlow = MutableStateFlow<UserProfile?>(initialProfile)
    private val plans = mutableMapOf<String, TodayPracticePlan>()
    private val planFlows = mutableMapOf<String, MutableStateFlow<TodayPracticePlan?>>()
    val evidences = mutableListOf<LearningEvidence>()

    override suspend fun recordEvidence(evidence: LearningEvidence): Long {
        evidences += evidence
        return evidences.size.toLong()
    }
    override fun getEvidenceForContent(contentId: String) = flowOf(evidences.filter { it.contentId == contentId })
    override fun getRecentEvidence(limit: Int) = flowOf(evidences.takeLast(limit))
    override fun getMasterySnapshot(contentId: String) = flowOf<MasterySnapshot?>(null)
    override suspend fun getMasterySnapshotSync(contentId: String): MasterySnapshot? = null
    override fun getAllMasterySnapshots() = flowOf(emptyList<MasterySnapshot>())
    override suspend fun saveMasterySnapshot(snapshot: MasterySnapshot) = Unit
    override fun getActiveMistakes() = flowOf(emptyList<MistakeRecord>())
    override fun getAllMistakes() = flowOf(emptyList<MistakeRecord>())
    override suspend fun findMistake(contentId: String, trapType: String): MistakeRecord? = null
    override suspend fun saveMistake(mistake: MistakeRecord): Long = 1L
    override fun getDueReviews(currentTimestamp: Long) = flowOf(emptyList<ReviewSchedule>())
    override fun getReviewSchedule(contentId: String) = flowOf<ReviewSchedule?>(null)
    override suspend fun getReviewScheduleSync(contentId: String): ReviewSchedule? = null
    override suspend fun saveReviewSchedule(schedule: ReviewSchedule) = Unit
    override fun getUserProfile(id: String): Flow<UserProfile?> = profileFlow.asStateFlow()
    override suspend fun getUserProfileSync(id: String): UserProfile? = profileFlow.value
    override suspend fun saveUserProfile(profile: UserProfile) { profileFlow.value = profile }
    override fun getTodayPlan(date: String): Flow<TodayPracticePlan?> =
        planFlows.getOrPut(date) { MutableStateFlow(plans[date]) }
    override suspend fun getTodayPlanSync(date: String): TodayPracticePlan? = plans[date]
    override suspend fun getTodayPlanByIdSync(planId: String): TodayPracticePlan? =
        plans.values.firstOrNull { it.id == planId }
    override suspend fun saveTodayPlan(plan: TodayPracticePlan) {
        plans[plan.date] = plan
        planFlows.getOrPut(plan.date) { MutableStateFlow(null) }.value = plan
    }
    override suspend fun markTodayPlanItemCompleted(planId: String, itemId: String) {
        plans.values.firstOrNull { it.id == planId }?.let { existing ->
            val updated = existing.copy(
                items = existing.items.map {
                    if (it.id == itemId) it.copy(isCompleted = true, completedAt = 5000L) else it
                }
            )
            plans[existing.date] = updated
            planFlows[existing.date]?.value = updated
        }
    }
}
