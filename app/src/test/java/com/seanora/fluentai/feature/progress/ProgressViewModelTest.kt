package com.seanora.fluentai.feature.progress

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.MistakeStage
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.domain.mistakes.MistakeEngine
import com.seanora.fluentai.domain.progress.ProgressEngine
import com.seanora.fluentai.domain.review.ReviewScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProgressViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val now = System.currentTimeMillis()

    private val mockSnapshots = listOf(
        MasterySnapshot(
            contentId = "vocab.leadership",
            domain = "vocabulary",
            level = 0.88f,
            confidence = 0.85f,
            totalAttempts = 6,
            correctAttempts = 6,
            lastAttemptTimestamp = now,
            lastDecayTimestamp = now,
            status = MasteryStatus.MASTERED
        ),
        MasterySnapshot(
            contentId = "grammar.inversion",
            domain = "grammar",
            level = 0.75f,
            confidence = 0.70f,
            totalAttempts = 4,
            correctAttempts = 3,
            lastAttemptTimestamp = now,
            lastDecayTimestamp = now,
            status = MasteryStatus.PRACTICED
        )
    )

    private val mockEvidence = listOf(
        LearningEvidence(
            contentId = "vocab.leadership",
            domain = "vocabulary",
            activityType = "srs_review",
            isCorrect = true,
            score = 1.0f,
            timestamp = now
        )
    )

    private val mockMistakes = listOf(
        MistakeRecord(
            id = 1L,
            contentId = "vocab.prep",
            trapType = "preposition_trap",
            errorDescription = "in vs on",
            userAnswer = "in",
            correctAnswer = "on",
            stage = MistakeStage.OBSERVED,
            occurrenceCount = 1,
            firstObservedAt = now,
            lastObservedAt = now
        )
    )

    private val mockSchedules = listOf(
        ReviewSchedule(
            contentId = "vocab.leadership",
            domain = "vocabulary",
            nextReviewDueTimestamp = now - 5000L,
            intervalDays = 3.0f,
            repetitionNumber = 2
        )
    )

    private val fakeRepo = object : UserLearningRepository {
        override suspend fun recordEvidence(evidence: LearningEvidence): Long = 1L
        override fun getEvidenceForContent(contentId: String): Flow<List<LearningEvidence>> = flowOf(emptyList())
        override fun getRecentEvidence(limit: Int): Flow<List<LearningEvidence>> = flowOf(mockEvidence)
        override fun getMasterySnapshot(contentId: String): Flow<MasterySnapshot?> = flowOf(null)
        override suspend fun getMasterySnapshotSync(contentId: String): MasterySnapshot? = null
        override fun getAllMasterySnapshots(): Flow<List<MasterySnapshot>> = flowOf(mockSnapshots)
        override suspend fun saveMasterySnapshot(snapshot: MasterySnapshot) {}
        override fun getActiveMistakes(): Flow<List<MistakeRecord>> = flowOf(mockMistakes)
        override fun getAllMistakes(): Flow<List<MistakeRecord>> = flowOf(mockMistakes)
        override suspend fun findMistake(contentId: String, trapType: String): MistakeRecord? = null
        override suspend fun saveMistake(mistake: MistakeRecord): Long = 1L
        override fun getDueReviews(currentTimestamp: Long): Flow<List<ReviewSchedule>> = flowOf(mockSchedules)
        override fun getReviewSchedule(contentId: String): Flow<ReviewSchedule?> = flowOf(null)
        override suspend fun getReviewScheduleSync(contentId: String): ReviewSchedule? = null
        override suspend fun saveReviewSchedule(schedule: ReviewSchedule) {}
        override fun getUserProfile(id: String): Flow<UserProfile?> = flowOf(UserProfile())
        override suspend fun getUserProfileSync(id: String): UserProfile? = UserProfile()
        override suspend fun saveUserProfile(profile: UserProfile) {}
    }

    private lateinit var progressEngine: ProgressEngine
    private lateinit var viewModel: ProgressViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        progressEngine = ProgressEngine(MistakeEngine(), ReviewScheduler())
        viewModel = ProgressViewModel(fakeRepo, progressEngine)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialization_loadsDashboardDataDeterministically() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.dashboardData)

        val dashboard = state.dashboardData!!
        assertEquals(5, dashboard.skills.size)
        assertEquals(1, dashboard.reviewsDueCount)
        assertEquals(98, dashboard.mistakeHealthScore) // 100 - 2 (observed penalty)
        assertTrue(dashboard.streak.currentStreakDays >= 1)
        assertEquals(1, state.recentEvidence.size)

        collectJob.cancel()
    }

    @Test
    fun selectSkill_updatesStateCorrectly() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        testScheduler.advanceUntilIdle()

        val vocabSkill = viewModel.uiState.value.dashboardData?.skills?.find { it.domain == "vocabulary" }
        assertNotNull(vocabSkill)

        viewModel.selectSkill(vocabSkill)
        assertEquals("vocabulary", viewModel.uiState.value.selectedSkill?.domain)

        viewModel.selectSkill(null)
        assertNull(viewModel.uiState.value.selectedSkill)

        collectJob.cancel()
    }
}
