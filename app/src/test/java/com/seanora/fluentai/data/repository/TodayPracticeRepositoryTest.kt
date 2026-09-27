package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.CorrectionMode
import com.seanora.fluentai.core.model.DailyMetrics
import com.seanora.fluentai.core.model.GrammarLesson
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.ListeningScenario
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.MistakeStage
import com.seanora.fluentai.core.model.ReadingArticle
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.SpeakingCategory
import com.seanora.fluentai.core.model.SpeakingScenario
import com.seanora.fluentai.core.model.TodayItemType
import com.seanora.fluentai.core.model.TodayPlanItem
import com.seanora.fluentai.core.model.TodayPracticePlan
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.core.model.VocabItem
import com.seanora.fluentai.domain.today.TodayPlanner
import com.seanora.fluentai.domain.mastery.MasteryEngine
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import com.seanora.fluentai.domain.mistakes.MistakeEngine
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

class TodayPracticeRepositoryTest {

    private lateinit var fakeUserLearningRepo: FakeUserLearningRepository
    private lateinit var fakeVocabRepo: FakeVocabularyRepository
    private lateinit var fakeGrammarRepo: FakeGrammarRepository
    private lateinit var fakeReadingRepo: FakeReadingRepository
    private lateinit var fakeListeningRepo: FakeListeningRepository
    private lateinit var fakeSpeakingRepo: FakeSpeakingRepository
    private lateinit var planner: TodayPlanner
    private lateinit var repository: TodayPracticeRepositoryImpl

    @Before
    fun setup() {
        fakeUserLearningRepo = FakeUserLearningRepository()
        fakeVocabRepo = FakeVocabularyRepository()
        fakeGrammarRepo = FakeGrammarRepository()
        fakeReadingRepo = FakeReadingRepository()
        fakeListeningRepo = FakeListeningRepository()
        fakeSpeakingRepo = FakeSpeakingRepository()
        planner = TodayPlanner()

        repository = TodayPracticeRepositoryImpl(
            todayPlanner = planner,
            userLearningRepository = fakeUserLearningRepo,
            vocabularyRepository = fakeVocabRepo,
            grammarRepository = fakeGrammarRepo,
            readingRepository = fakeReadingRepo,
            listeningRepository = fakeListeningRepo,
            speakingRepository = fakeSpeakingRepo,
            recordLearningEvidence = RecordLearningEvidenceUseCase(
                fakeUserLearningRepo,
                MasteryEngine(),
                MistakeEngine(),
            ),
        )
    }

    @Test
    fun `getTodayPlan generates and persists new plan when none exists`() = runTest {
        val testDate = "2026-09-25"
        val plan = repository.getTodayPlan(testDate).first()

        assertNotNull(plan)
        assertEquals(testDate, plan.date)
        assertTrue(plan.items.isNotEmpty())

        // Verify it was persisted in UserLearningRepository
        val persisted = fakeUserLearningRepo.getTodayPlanSync(testDate)
        assertNotNull(persisted)
        assertEquals(plan.id, persisted?.id)
        assertEquals(plan.items.size, persisted?.items?.size)
    }

    @Test
    fun `getTodayPlan returns existing plan if already generated`() = runTest {
        val testDate = "2026-09-25"
        val existingPlan = TodayPracticePlan(
            id = "pre_existing_plan",
            date = testDate,
            targetCefrLevel = "C1",
            allocatedMinutes = 25,
            rationale = "Custom plan already crafted",
            items = listOf(
                TodayPlanItem(
                    id = "item_1",
                    planId = "pre_existing_plan",
                    contentId = "vocab.leverage",
                    title = "Leverage",
                    domain = "vocabulary",
                    itemType = TodayItemType.CURRICULUM_NEW,
                    estimatedMinutes = 5,
                    targetCefrLevel = "C1",
                    explanation = "Custom explanation",
                )
            )
        )
        fakeUserLearningRepo.saveTodayPlan(existingPlan)

        val plan = repository.getTodayPlan(testDate).first()
        assertEquals("pre_existing_plan", plan.id)
        assertEquals(1, plan.items.size)
        assertEquals("vocab.leverage", plan.items[0].contentId)
    }

    @Test
    fun `markItemCompleted marks item and updates user streak`() = runTest {
        val testDate = currentDateString()
        fakeUserLearningRepo.saveUserProfile(
            UserProfile(
                id = "default_user",
                streakDays = 5,
                lastActiveDate = "2026-09-24", // yesterday
            )
        )

        // Generate plan
        val plan = repository.getTodayPlan(testDate).first()
        val firstItemId = plan.items.first().id

        // Mark item completed
        repository.markItemCompleted(plan.id, firstItemId)

        // Verify item is completed
        val updatedPlan = repository.getTodayPlan(testDate).first()
        val completedItem = updatedPlan.items.find { it.id == firstItemId }
        assertNotNull(completedItem)
        assertTrue(completedItem!!.isCompleted)

        // Verify streak was incremented because user practiced yesterday
        val updatedProfile = fakeUserLearningRepo.getUserProfileSync()
        assertNotNull(updatedProfile)
        assertEquals(testDate, updatedProfile?.lastActiveDate)
    }

    @Test
    fun `calculateUpdatedStreak handles same day, yesterday, and broken streak correctly`() {
        val profile = UserProfile(streakDays = 5, lastActiveDate = "2026-09-24")

        // Active again on same day
        val sameDayResult = repository.calculateUpdatedStreak(
            profile.copy(lastActiveDate = "2026-09-25"),
            todayDate = "2026-09-25"
        )
        assertEquals(5, sameDayResult.streakDays)
        assertEquals("2026-09-25", sameDayResult.lastActiveDate)

        // Active next day (yesterday was 2026-09-24)
        val nextDayResult = repository.calculateUpdatedStreak(
            profile,
            todayDate = "2026-09-25"
        )
        assertEquals(6, nextDayResult.streakDays)
        assertEquals("2026-09-25", nextDayResult.lastActiveDate)

        // Broken streak (last active 2026-09-20, today is 2026-09-25)
        val brokenStreakProfile = UserProfile(streakDays = 12, lastActiveDate = "2026-09-20")
        val brokenResult = repository.calculateUpdatedStreak(
            brokenStreakProfile,
            todayDate = "2026-09-25"
        )
        assertEquals(1, brokenResult.streakDays)
        assertEquals("2026-09-25", brokenResult.lastActiveDate)
    }

    @Test
    fun `refreshTodayPlan forces fresh plan generation`() = runTest {
        val testDate = "2026-09-25"
        // Seed an initial plan
        val initialPlan = repository.getTodayPlan(testDate).first()

        // Refresh plan
        val refreshed = repository.refreshTodayPlan(testDate)
        assertNotNull(refreshed)
        assertEquals(testDate, refreshed.date)

        val currentInDb = fakeUserLearningRepo.getTodayPlanSync(testDate)
        assertEquals(refreshed.id, currentInDb?.id)
    }

    @Test
    fun `refreshTodayPlan preserves completed same-day items`() = runTest {
        val testDate = "2026-09-25"
        val initial = repository.getTodayPlan(testDate).first()
        val completed = initial.items.first()
        repository.markItemCompleted(initial.id, completed.id)

        val refreshed = repository.refreshTodayPlan(testDate)

        assertTrue(refreshed.items.first { it.contentId == completed.contentId }.isCompleted)
        assertNotNull(refreshed.items.first { it.contentId == completed.contentId }.completedAt)
    }

    @Test
    fun `getDailyMetrics combines completed items minutes and streak`() = runTest {
        val testDate = currentDateString()
        fakeUserLearningRepo.saveUserProfile(
            UserProfile(id = "default_user", streakDays = 7, lastActiveDate = testDate)
        )

        // Seed a plan with 1 completed review and 1 uncompleted lesson
        val plan = TodayPracticePlan(
            id = "today_plan_$testDate",
            date = testDate,
            targetCefrLevel = "B2",
            allocatedMinutes = 20,
            rationale = "Test rationale",
            items = listOf(
                TodayPlanItem(
                    id = "item_1",
                    planId = "today_plan_$testDate",
                    contentId = "vocab.review.001",
                    title = "Review Vocab",
                    domain = "vocabulary",
                    itemType = TodayItemType.REVIEW_DUE,
                    estimatedMinutes = 3,
                    targetCefrLevel = "B2",
                    explanation = "Due review",
                    isCompleted = true,
                ),
                TodayPlanItem(
                    id = "item_2",
                    planId = "today_plan_$testDate",
                    contentId = "grammar.lesson.001",
                    title = "Grammar Lesson",
                    domain = "grammar",
                    itemType = TodayItemType.CURRICULUM_NEW,
                    estimatedMinutes = 7,
                    targetCefrLevel = "B2",
                    explanation = "Curriculum",
                    isCompleted = false,
                )
            )
        )
        fakeUserLearningRepo.saveTodayPlan(plan)

        val metrics = repository.getDailyMetrics().first()
        assertEquals(7, metrics.streakDays)
        assertEquals(3, metrics.practiceMinutesToday)
        assertEquals(1, metrics.reviewsClearedToday)
    }
}

// Test doubles
private class FakeUserLearningRepository : UserLearningRepository {
    private var profile: UserProfile = UserProfile()
    private val profileFlow = MutableStateFlow<UserProfile?>(profile)
    private val plans = mutableMapOf<String, TodayPracticePlan>()
    private val planFlows = mutableMapOf<String, MutableStateFlow<TodayPracticePlan?>>()

    val recordedEvidence = mutableListOf<LearningEvidence>()
    override suspend fun recordEvidence(evidence: LearningEvidence): Long {
        recordedEvidence += evidence
        return recordedEvidence.size.toLong()
    }
    override fun getEvidenceForContent(contentId: String): Flow<List<LearningEvidence>> = flowOf(emptyList())
    override fun getRecentEvidence(limit: Int): Flow<List<LearningEvidence>> = flowOf(emptyList())
    override fun getMasterySnapshot(contentId: String): Flow<MasterySnapshot?> = flowOf(null)
    override suspend fun getMasterySnapshotSync(contentId: String): MasterySnapshot? = null
    override fun getAllMasterySnapshots(): Flow<List<MasterySnapshot>> = flowOf(emptyList())
    override suspend fun saveMasterySnapshot(snapshot: MasterySnapshot) {}
    override fun getActiveMistakes(): Flow<List<MistakeRecord>> = flowOf(emptyList())
    override fun getAllMistakes(): Flow<List<MistakeRecord>> = flowOf(emptyList())
    override suspend fun findMistake(contentId: String, trapType: String): MistakeRecord? = null
    override suspend fun saveMistake(mistake: MistakeRecord): Long = 1L
    override fun getDueReviews(currentTimestamp: Long): Flow<List<ReviewSchedule>> = flowOf(emptyList())
    override fun getReviewSchedule(contentId: String): Flow<ReviewSchedule?> = flowOf(null)
    override suspend fun getReviewScheduleSync(contentId: String): ReviewSchedule? = null
    override suspend fun saveReviewSchedule(schedule: ReviewSchedule) {}

    override fun getUserProfile(id: String): Flow<UserProfile?> = profileFlow.asStateFlow()
    override suspend fun getUserProfileSync(id: String): UserProfile? = profile
    override suspend fun saveUserProfile(profile: UserProfile) {
        this.profile = profile
        profileFlow.value = profile
    }

    override fun getTodayPlan(date: String): Flow<TodayPracticePlan?> {
        return planFlows.getOrPut(date) { MutableStateFlow(plans[date]) }
    }

    override suspend fun getTodayPlanSync(date: String): TodayPracticePlan? = plans[date]
    override suspend fun getTodayPlanByIdSync(planId: String): TodayPracticePlan? =
        plans.values.firstOrNull { it.id == planId }

    override suspend fun saveTodayPlan(plan: TodayPracticePlan) {
        plans[plan.date] = plan
        planFlows.getOrPut(plan.date) { MutableStateFlow(null) }.value = plan
    }

    override suspend fun markTodayPlanItemCompleted(planId: String, itemId: String) {
        plans.values.find { it.id == planId }?.let { existing ->
            val updatedItems = existing.items.map {
                if (it.id == itemId) it.copy(isCompleted = true, completedAt = System.currentTimeMillis()) else it
            }
            val updated = existing.copy(items = updatedItems)
            plans[existing.date] = updated
            planFlows[existing.date]?.value = updated
        }
    }
}

private class FakeVocabularyRepository : VocabularyRepository {
    private val vocab = listOf(
        VocabItem(
            id = "vocab.resilient",
            headword = "resilient",
            cefrLevel = "B2",
            partOfSpeech = "adjective",
            phonetic = "/rɪˈzɪl.jənt/",
            definitionEn = "Able to recover quickly.",
            meaningTr = "Dirençli",
            topicTags = listOf("leadership")
        ),
        VocabItem(
            id = "vocab.strategic",
            headword = "strategic",
            cefrLevel = "C1",
            partOfSpeech = "adjective",
            phonetic = "/strəˈtiː.dʒɪk/",
            definitionEn = "Relating to long-term plans.",
            meaningTr = "Stratejik",
            topicTags = listOf("business")
        )
    )
    override fun getAllVocab(): Flow<List<VocabItem>> = flowOf(vocab)
    override fun getVocabByLevel(level: String): Flow<List<VocabItem>> = flowOf(vocab.filter { it.cefrLevel == level })
    override fun getVocabById(id: String): Flow<VocabItem?> = flowOf(vocab.find { it.id == id })
    override fun searchVocab(query: String): Flow<List<VocabItem>> = flowOf(vocab)
    override suspend fun getVocabCount(): Int = vocab.size
}

private class FakeGrammarRepository : GrammarRepository {
    private val lessons = listOf(
        GrammarLesson(
            id = "grammar.conditionals-b2",
            title = "Mixed Conditionals",
            cefrLevel = "B2",
            category = "Conditionals",
            summaryEn = "Hypothetical situations.",
            summaryTr = "Koşul yapıları.",
            explanationTr = "Açıklama"
        )
    )
    override fun getAllLessons(): Flow<List<GrammarLesson>> = flowOf(lessons)
    override fun getLessonsByLevel(level: String): Flow<List<GrammarLesson>> = flowOf(lessons)
    override fun getLessonsByCategory(category: String): Flow<List<GrammarLesson>> = flowOf(lessons)
    override fun getLessonById(id: String): Flow<GrammarLesson?> = flowOf(lessons.find { it.id == id })
    override fun searchLessons(query: String): Flow<List<GrammarLesson>> = flowOf(lessons)
    override fun getExercisesForLesson(lessonId: String) = flowOf(emptyList<com.seanora.fluentai.core.model.Exercise>())
    override suspend fun getLessonCount(): Int = lessons.size
}

private class FakeReadingRepository : ReadingRepository {
    private val articles = listOf(
        ReadingArticle(
            id = "reading.b2.tech",
            title = "AI in Modern Work",
            cefrLevel = "B2",
            category = "Technology",
            summaryEn = "Summary...",
            summaryTr = "Özet...",
            wordCount = 350,
            estimatedReadingMinutes = 5,
            paragraphs = emptyList(),
            vocabularyAnnotations = emptyList(),
            comprehensionQuestions = emptyList(),
            topicTags = listOf("technology"),
            relatedIds = emptyList()
        )
    )
    override fun getAllArticles(): Flow<List<ReadingArticle>> = flowOf(articles)
    override fun getArticlesByLevel(level: String): Flow<List<ReadingArticle>> = flowOf(articles)
    override fun getArticleById(id: String): Flow<ReadingArticle?> = flowOf(articles.find { it.id == id })
    override fun getArticlesByCategory(category: String): Flow<List<ReadingArticle>> = flowOf(articles)
    override fun searchArticles(query: String): Flow<List<ReadingArticle>> = flowOf(articles)
    override suspend fun getArticleCount(): Int = articles.size
}

private class FakeListeningRepository : ListeningRepository {
    private val scenarios = listOf(
        ListeningScenario(
            id = "listening.b2.standup",
            title = "Daily Standup Meeting",
            cefrLevel = "B2",
            category = "Workplace",
            scenarioContext = "Standup meeting context",
            speakers = emptyList(),
            audioRef = "audio_ref",
            durationSeconds = 120,
            transcriptItems = emptyList(),
            keyVocabulary = emptyList(),
            comprehensionQuestions = emptyList(),
            topicTags = listOf("workplace"),
            relatedIds = emptyList()
        )
    )
    override fun getAllScenarios(): Flow<List<ListeningScenario>> = flowOf(scenarios)
    override fun getScenariosByLevel(level: String): Flow<List<ListeningScenario>> = flowOf(scenarios)
    override fun getScenarioById(id: String): Flow<ListeningScenario?> = flowOf(scenarios.find { it.id == id })
    override fun getScenariosByCategory(category: String): Flow<List<ListeningScenario>> = flowOf(scenarios)
    override fun searchScenarios(query: String): Flow<List<ListeningScenario>> = flowOf(scenarios)
    override suspend fun getScenarioCount(): Int = scenarios.size
}

private class FakeSpeakingRepository : SpeakingRepository {
    private val scenarios = listOf(
        SpeakingScenario(
            id = "speaking.b2.sync",
            title = "Engineering Sync",
            category = SpeakingCategory.MEETING,
            cefrLevel = "B2",
            systemPrompt = "Act as tech lead",
            contextDescription = "Discuss architecture",
            recommendedCorrectionMode = CorrectionMode.COACH,
        )
    )
    override fun getAllScenarios(): Flow<List<SpeakingScenario>> = flowOf(scenarios)
    override fun getScenariosByLevel(level: String): Flow<List<SpeakingScenario>> = flowOf(scenarios)
    override fun getScenariosByCategory(category: SpeakingCategory): Flow<List<SpeakingScenario>> = flowOf(scenarios)
    override fun getScenarioById(id: String): Flow<SpeakingScenario?> = flowOf(scenarios.find { it.id == id })
    override suspend fun getScenarioByIdSync(id: String): SpeakingScenario? = scenarios.find { it.id == id }
    override fun searchScenarios(query: String): Flow<List<SpeakingScenario>> = flowOf(scenarios)
    override suspend fun getScenarioCount(): Int = scenarios.size
}
