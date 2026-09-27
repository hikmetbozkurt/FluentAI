package com.seanora.fluentai.feature.onboarding

import com.seanora.fluentai.core.model.AssessmentDomain
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.LearningInterest
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.PlacementProbe
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.SelfAssessedLevel
import com.seanora.fluentai.core.model.UserAvatar
import com.seanora.fluentai.core.model.UserGoal
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.data.preferences.UserPreferencesRepository
import com.seanora.fluentai.data.repository.AssessmentRepository
import com.seanora.fluentai.data.repository.AssessmentRepositoryImpl
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.domain.assessment.PlacementEngine
import com.seanora.fluentai.domain.mastery.MasteryEngine
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import com.seanora.fluentai.domain.mistakes.MistakeEngine
import com.seanora.fluentai.domain.review.ReviewScheduler
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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private var savedProfile: UserProfile? = null
    private val recordedEvidence = mutableListOf<LearningEvidence>()
    private val avatarKeyFlow = MutableStateFlow<String?>(null)

    private val fakePreferencesRepo = object : UserPreferencesRepository {
        override val selectedAvatarKey: Flow<String?> = avatarKeyFlow
        override suspend fun getSelectedAvatarKeySync(): String? = avatarKeyFlow.value
        override suspend fun setSelectedAvatarKey(key: String) { avatarKeyFlow.value = key }
        override suspend fun clearSelectedAvatarKey() { avatarKeyFlow.value = null }
    }

    private val fakeUserRepo = object : UserLearningRepository {
        override suspend fun recordEvidence(evidence: LearningEvidence): Long {
            recordedEvidence.add(evidence)
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
        override fun getUserProfile(id: String): Flow<UserProfile?> = flowOf(savedProfile)
        override suspend fun getUserProfileSync(id: String): UserProfile? = savedProfile
        override suspend fun saveUserProfile(profile: UserProfile) {
            savedProfile = profile
        }
    }

    private lateinit var assessmentRepo: AssessmentRepository
    private lateinit var placementEngine: PlacementEngine
    private lateinit var recordLearningEvidenceUseCase: RecordLearningEvidenceUseCase
    private lateinit var viewModel: OnboardingViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        savedProfile = null
        recordedEvidence.clear()
        avatarKeyFlow.value = null

        assessmentRepo = AssessmentRepositoryImpl()
        placementEngine = PlacementEngine()
        val reviewScheduler = ReviewScheduler()
        val mistakeEngine = MistakeEngine()
        recordLearningEvidenceUseCase = RecordLearningEvidenceUseCase(
            userLearningRepository = fakeUserRepo,
            masteryEngine = MasteryEngine(reviewScheduler),
            mistakeEngine = mistakeEngine
        )

        viewModel = OnboardingViewModel(
            assessmentRepository = assessmentRepo,
            placementEngine = placementEngine,
            userLearningRepository = fakeUserRepo,
            recordLearningEvidenceUseCase = recordLearningEvidenceUseCase,
            userPreferencesRepository = fakePreferencesRepo
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun surveyProgression_navigatesThroughSteps() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        testScheduler.advanceUntilIdle()

        assertEquals(OnboardingStep.WELCOME, viewModel.uiState.value.currentStep)

        viewModel.nextStep()
        assertEquals(OnboardingStep.AVATAR_SELECTION, viewModel.uiState.value.currentStep)

        viewModel.selectAvatar("avatar_2")
        assertEquals("avatar_2", viewModel.uiState.value.selectedAvatarKey)

        viewModel.nextStep()
        assertEquals(OnboardingStep.GOAL_SELECTION, viewModel.uiState.value.currentStep)

        viewModel.selectGoal(UserGoal.GLOBAL_MEETINGS)
        assertEquals(UserGoal.GLOBAL_MEETINGS, viewModel.uiState.value.selectedGoal)

        viewModel.nextStep()
        assertEquals(OnboardingStep.INTERESTS_SELECTION, viewModel.uiState.value.currentStep)

        viewModel.toggleInterest(LearningInterest.TECH_INNOVATION)
        assertTrue(viewModel.uiState.value.selectedInterests.contains(LearningInterest.TECH_INNOVATION))

        viewModel.nextStep()
        assertEquals(OnboardingStep.SELF_ASSESSMENT, viewModel.uiState.value.currentStep)

        viewModel.selectSelfAssessment(SelfAssessedLevel.B2_UPPER_INTERMEDIATE)
        assertEquals(SelfAssessedLevel.B2_UPPER_INTERMEDIATE, viewModel.uiState.value.selectedSelfAssessment)

        viewModel.nextStep()
        assertEquals(OnboardingStep.DAILY_TARGET, viewModel.uiState.value.currentStep)

        viewModel.selectDailyTarget(25, "C1")
        assertEquals(25, viewModel.uiState.value.dailyPracticeMinutes)
        assertEquals("C1", viewModel.uiState.value.targetCefrLevel)

        collectJob.cancel()
    }

    @Test
    fun avatarSelection_persistsAndResolvesDrawable() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        viewModel.nextStep()
        assertEquals(OnboardingStep.AVATAR_SELECTION, viewModel.uiState.value.currentStep)

        // Select avatar 1
        viewModel.selectAvatar("avatar_1")
        assertEquals("avatar_1", viewModel.uiState.value.selectedAvatarKey)

        viewModel.nextStep()
        testScheduler.advanceUntilIdle()

        assertEquals("avatar_1", fakePreferencesRepo.getSelectedAvatarKeySync())
        assertEquals(com.seanora.fluentai.R.drawable.avatar_1, UserAvatar.getDrawableRes("avatar_1"))
        assertEquals(com.seanora.fluentai.R.drawable.avatar_2, UserAvatar.getDrawableRes("avatar_2"))
        assertEquals(com.seanora.fluentai.R.drawable.avatar_3, UserAvatar.getDrawableRes("avatar_3"))
        assertNull(UserAvatar.getDrawableRes("unknown_key"))
        assertNull(UserAvatar.getDrawableRes(null))

        collectJob.cancel()
    }

    @Test
    fun placementTest_completesAndGeneratesProfile() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        testScheduler.advanceUntilIdle()

        viewModel.selectGoal(UserGoal.CAREER_ADVANCEMENT)
        viewModel.toggleInterest(LearningInterest.BUSINESS_LEADERSHIP)
        viewModel.selectSelfAssessment(SelfAssessedLevel.B1_INTERMEDIATE)

        viewModel.startPlacementTest()
        testScheduler.advanceUntilIdle()

        assertEquals(OnboardingStep.PLACEMENT_TEST, viewModel.uiState.value.currentStep)
        assertNotNull(viewModel.uiState.value.currentProbe)

        // Answer 12 probes
        for (i in 1..12) {
            val currentProbe = viewModel.uiState.value.currentProbe
            assertNotNull(currentProbe)

            // Select correct option
            viewModel.selectOption(currentProbe!!.correctOptionIndex)
            viewModel.submitProbeAnswer()
            testScheduler.advanceUntilIdle()

            viewModel.nextProbe()
            testScheduler.advanceUntilIdle()
        }

        // Diagnostic summary generated
        assertEquals(OnboardingStep.PLACEMENT_RESULT, viewModel.uiState.value.currentStep)
        val summary = viewModel.uiState.value.placementSummary
        assertNotNull(summary)
        assertEquals(4, summary!!.skillPlacements.size)

        // Complete onboarding
        viewModel.completeOnboarding()
        testScheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isCompleted)
        assertNotNull(savedProfile)
        assertEquals("C1", savedProfile!!.targetCefrLevel)
        assertTrue(savedProfile!!.learningInterests.contains("leadership"))

        collectJob.cancel()
    }
}
