package com.seanora.fluentai.feature.home.data

import com.seanora.fluentai.core.designsystem.components.CefrLevel
import com.seanora.fluentai.core.model.DailyMetrics as CoreDailyMetrics
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.TodayItemType
import com.seanora.fluentai.core.model.TodayPlanItem
import com.seanora.fluentai.core.model.TodayPracticePlan
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.GrammarFeatureState
import com.seanora.fluentai.core.model.GrammarResumeDestination
import com.seanora.fluentai.core.model.VocabularyFeatureState
import com.seanora.fluentai.core.model.VocabularyPracticeMode
import com.seanora.fluentai.core.model.VocabularyResumeDestination
import com.seanora.fluentai.data.repository.GrammarFeatureStateRepository
import com.seanora.fluentai.data.repository.TodayPracticeRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.data.repository.VocabularyFeatureStateRepository
import com.seanora.fluentai.domain.progress.ProgressEngine
import com.seanora.fluentai.feature.home.model.HomeResumeKind
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RealHomeRepositoryTest {

    private lateinit var fakeTodayRepo: FakeTodayPracticeRepository
    private lateinit var fakeUserLearningRepo: FakeUserLearningRepoForHome
    private lateinit var fakeVocabularyStateRepo: FakeVocabularyStateRepository
    private lateinit var fakeGrammarStateRepo: FakeGrammarStateRepository
    private lateinit var repository: RealHomeRepository

    @Before
    fun setUp() {
        fakeTodayRepo = FakeTodayPracticeRepository()
        fakeUserLearningRepo = FakeUserLearningRepoForHome()
        fakeVocabularyStateRepo = FakeVocabularyStateRepository()
        fakeGrammarStateRepo = FakeGrammarStateRepository()
        repository = RealHomeRepository(
            todayPracticeRepository = fakeTodayRepo,
            userLearningRepository = fakeUserLearningRepo,
            vocabularyStateRepository = fakeVocabularyStateRepo,
            grammarStateRepository = fakeGrammarStateRepo,
            progressEngine = ProgressEngine(),
        )
    }

    @Test
    fun `getTodayPlan correctly maps plan, metrics, and skill profiles`() = runTest {
        val testPlan = TodayPracticePlan(
            id = "plan_2026-09-25",
            date = "2026-09-25",
            targetCefrLevel = "C1",
            allocatedMinutes = 20,
            rationale = "Personalized plan for C1 growth",
            items = listOf(
                TodayPlanItem(
                    id = "item_1",
                    planId = "plan_2026-09-25",
                    contentId = "vocab.review.001",
                    title = "Review: Deliberation",
                    domain = "vocabulary",
                    itemType = TodayItemType.REVIEW_DUE,
                    estimatedMinutes = 2,
                    targetCefrLevel = "B2",
                    explanation = "Due today for spaced review",
                    isCompleted = false,
                ),
                TodayPlanItem(
                    id = "item_2",
                    planId = "plan_2026-09-25",
                    contentId = "speaking.c1.defense",
                    title = "Architecture Defense",
                    domain = "speaking",
                    itemType = TodayItemType.SPEAKING_SESSION,
                    estimatedMinutes = 8,
                    targetCefrLevel = "C1",
                    explanation = "Oral fluency practice",
                    isCompleted = false,
                )
            )
        )
        fakeTodayRepo.emitPlan(testPlan)
        fakeTodayRepo.emitMetrics(CoreDailyMetrics(practiceMinutesToday = 10, reviewsClearedToday = 3, streakDays = 4))

        val homePlan = repository.getTodayPlan().first()

        assertNotNull(homePlan)
        assertNotNull(homePlan.practicePlan)
        assertEquals("plan_2026-09-25", homePlan.practicePlan?.id)

        // Hero practice should be next uncompleted item (item_1)
        assertEquals("vocab.review.001", homePlan.heroPractice.id)
        assertEquals("Review: Deliberation", homePlan.heroPractice.title)
        assertEquals(2, homePlan.heroPractice.durationMinutes)

        // Due reviews list should contain item_1
        assertEquals(1, homePlan.dueReviews.size)
        assertEquals("vocab.review.001", homePlan.dueReviews[0].id)

        // Daily metrics should match
        assertEquals(10, homePlan.dailyMetrics.practiceMinutesToday)
        assertEquals(3, homePlan.dailyMetrics.reviewsClearedToday)
        assertEquals(4, homePlan.dailyMetrics.streakDays)

        // Skill profiles match the five persisted domains used by Progress.
        assertEquals(5, homePlan.skillProfiles.size)
        assertEquals(85, homePlan.skillProfiles.single { it.skill == "Vocabulary" }.masteryPercent)
        assertTrue(homePlan.skillProfiles.none { it.skill == "Pronunciation" })

        // Today's Focus preserves the real planner items and semantic IDs.
        assertEquals(listOf("vocab.review.001", "speaking.c1.defense"), homePlan.focusItems.map { it.contentId })
    }

    @Test
    fun `getTodayPlan shows celebration hero item when all items completed`() = runTest {
        val testPlan = TodayPracticePlan(
            id = "plan_completed",
            date = "2026-09-25",
            targetCefrLevel = "C1",
            allocatedMinutes = 15,
            rationale = "Completed all items",
            items = listOf(
                TodayPlanItem(
                    id = "item_1",
                    planId = "plan_completed",
                    contentId = "vocab.leverage",
                    title = "Leverage",
                    domain = "vocabulary",
                    itemType = TodayItemType.CURRICULUM_NEW,
                    estimatedMinutes = 5,
                    targetCefrLevel = "C1",
                    explanation = "Vocabulary",
                    isCompleted = true,
                )
            )
        )
        fakeTodayRepo.emitPlan(testPlan)

        val homePlan = repository.getTodayPlan().first()
        assertEquals("complete", homePlan.heroPractice.id)
        assertEquals("Daily Practice Complete", homePlan.heroPractice.title)
    }

    @Test
    fun `markItemCompleted delegates to todayPracticeRepository`() = runTest {
        repository.markItemCompleted("plan_1", "item_1")
        assertEquals("plan_1" to "item_1", fakeTodayRepo.lastCompletedItem)
    }

    @Test
    fun `refreshPlan delegates to todayPracticeRepository`() = runTest {
        repository.refreshPlan()
        assertTrue(fakeTodayRepo.refreshCalled)
    }

    @Test
    fun `continue learning uses newest meaningful persisted resume state`() = runTest {
        fakeVocabularyStateRepo.state = VocabularyFeatureState(
            resumeDestination = VocabularyResumeDestination.PRACTICE_LABS,
            resumePracticeMode = VocabularyPracticeMode.CONTEXT_CHOICE,
            updatedAt = 300L,
        )
        fakeGrammarStateRepo.state = GrammarFeatureState(
            resumeDestination = GrammarResumeDestination.GRAMMAR_IN_CONTEXT,
            resumeContentId = "grammar.conditionals",
            updatedAt = 200L,
        )

        val resume = repository.getTodayPlan().first().continueLearning

        assertNotNull(resume)
        assertEquals(HomeResumeKind.VOCABULARY_PRACTICE, resume?.kind)
        assertEquals(VocabularyPracticeMode.CONTEXT_CHOICE, resume?.practiceMode)
        assertEquals(300L, resume?.timestamp)
    }

    @Test
    fun `continue learning is empty when no meaningful resume or evidence exists`() = runTest {
        assertEquals(null, repository.getTodayPlan().first().continueLearning)
    }

    @Test
    fun `recent activity is persisted evidence newest first and deduplicated by content`() = runTest {
        fakeUserLearningRepo.recentEvidence = listOf(
            LearningEvidence(contentId = "reading.b2.article", domain = "reading", activityType = "comprehension_check", isCorrect = true, score = 1f, timestamp = 300L),
            LearningEvidence(contentId = "reading.b2.article", domain = "reading", activityType = "comprehension_check", isCorrect = false, score = 0f, timestamp = 200L),
            LearningEvidence(contentId = "listening.b1.meeting", domain = "listening", activityType = "dictation", isCorrect = true, score = 1f, timestamp = 100L),
        )

        val activities = repository.getTodayPlan().first().recentActivities

        assertEquals(listOf("reading.b2.article", "listening.b1.meeting"), activities.map { it.contentId })
        assertEquals(listOf(300L, 100L), activities.map { it.timestamp })
        assertEquals(listOf("reading", "listening"), activities.map { it.domain })
    }
}

private class FakeTodayPracticeRepository : TodayPracticeRepository {
    private val planFlow = MutableStateFlow(
        TodayPracticePlan(
            id = "default_plan",
            date = "2026-09-25",
            targetCefrLevel = "B2",
            allocatedMinutes = 20,
            rationale = "Default plan",
            items = emptyList()
        )
    )
    private val metricsFlow = MutableStateFlow(CoreDailyMetrics())

    var lastCompletedItem: Pair<String, String>? = null
    var refreshCalled = false

    fun emitPlan(plan: TodayPracticePlan) {
        planFlow.value = plan
    }

    fun emitMetrics(metrics: CoreDailyMetrics) {
        metricsFlow.value = metrics
    }

    override fun getTodayPlan(date: String): Flow<TodayPracticePlan> = planFlow.asStateFlow()

    override suspend fun refreshTodayPlan(date: String): TodayPracticePlan {
        refreshCalled = true
        return planFlow.value
    }

    override suspend fun markItemCompleted(planId: String, itemId: String) {
        lastCompletedItem = planId to itemId
    }

    override fun getDailyMetrics(): Flow<CoreDailyMetrics> = metricsFlow.asStateFlow()
}

private class FakeUserLearningRepoForHome : UserLearningRepository {
    var recentEvidence: List<LearningEvidence> = emptyList()
    private val profile = UserProfile(
        id = "default_user",
        targetCefrLevel = "C1",
        estimatedOverallLevel = "B2",
        estimatedVocabLevel = "B2",
        estimatedGrammarLevel = "B1",
        estimatedSpeakingLevel = "B1",
    )
    private val snapshots = listOf(
        MasterySnapshot(
            contentId = "vocab.leverage",
            domain = "vocabulary",
            level = 0.85f,
            confidence = 0.9f,
            totalAttempts = 5,
            correctAttempts = 5,
            lastAttemptTimestamp = 1000L,
            lastDecayTimestamp = 1000L,
            status = MasteryStatus.MASTERED
        )
    )

    override suspend fun recordEvidence(evidence: com.seanora.fluentai.core.model.LearningEvidence): Long = 1L
    override fun getEvidenceForContent(contentId: String) = flowOf(emptyList<com.seanora.fluentai.core.model.LearningEvidence>())
    override fun getRecentEvidence(limit: Int) = flowOf(recentEvidence.take(limit))
    override fun getMasterySnapshot(contentId: String) = flowOf(snapshots.find { it.contentId == contentId })
    override suspend fun getMasterySnapshotSync(contentId: String) = snapshots.find { it.contentId == contentId }
    override fun getAllMasterySnapshots() = flowOf(snapshots)
    override suspend fun saveMasterySnapshot(snapshot: MasterySnapshot) {}
    override fun getActiveMistakes() = flowOf(emptyList<com.seanora.fluentai.core.model.MistakeRecord>())
    override fun getAllMistakes() = flowOf(emptyList<com.seanora.fluentai.core.model.MistakeRecord>())
    override suspend fun findMistake(contentId: String, trapType: String) = null
    override suspend fun saveMistake(mistake: com.seanora.fluentai.core.model.MistakeRecord): Long = 1L
    override fun getDueReviews(currentTimestamp: Long) = flowOf(emptyList<com.seanora.fluentai.core.model.ReviewSchedule>())
    override fun getReviewSchedule(contentId: String) = flowOf(null)
    override suspend fun getReviewScheduleSync(contentId: String) = null
    override suspend fun saveReviewSchedule(schedule: com.seanora.fluentai.core.model.ReviewSchedule) {}
    override fun getUserProfile(id: String) = flowOf(profile)
    override suspend fun getUserProfileSync(id: String) = profile
    override suspend fun saveUserProfile(profile: UserProfile) {}
}

private class FakeVocabularyStateRepository : VocabularyFeatureStateRepository {
    var state = VocabularyFeatureState()
    override fun observeState() = flowOf(state)
    override suspend fun getState() = state
    override suspend fun saveDailyWord(date: String, contentId: String) = Unit
    override suspend fun saveResume(destination: VocabularyResumeDestination, contentId: String?, contextId: String?, practiceMode: VocabularyPracticeMode?) = Unit
}

private class FakeGrammarStateRepository : GrammarFeatureStateRepository {
    var state = GrammarFeatureState()
    override fun observeState() = flowOf(state)
    override suspend fun getState() = state
    override suspend fun saveResume(destination: GrammarResumeDestination, contentId: String, secondaryContentId: String?) = Unit
}
