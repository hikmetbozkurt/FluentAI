package com.seanora.fluentai.feature.vocabulary

import com.seanora.fluentai.core.model.*
import com.seanora.fluentai.data.repository.*
import com.seanora.fluentai.domain.mastery.MasteryEngine
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import com.seanora.fluentai.domain.mistakes.MistakeEngine
import com.seanora.fluentai.domain.review.ReviewScheduler
import com.seanora.fluentai.domain.vocabulary.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class VocabularyFeatureViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before fun setup() = Dispatchers.setMain(dispatcher)
    @After fun teardown() = Dispatchers.resetMain()

    @Test
    fun initialLoad_persistsDailyWordButDoesNotCreateResumeUntilMeaningfulAction() = runTest {
        val stateRepo = FakeStateRepository()
        val userRepo = FakeUserRepository()
        val viewModel = VocabularyFeatureViewModel(
            vocabularyRepository = FakeVocabularyRepository(),
            featureStateRepository = stateRepo,
            userLearningRepository = userRepo,
            recordEvidence = RecordLearningEvidenceUseCase(userRepo, MasteryEngine(ReviewScheduler()), MistakeEngine()),
            dailySelector = VocabularyDailySelector(),
            practiceEngine = VocabularyPracticeEngine(),
            flashCardSelector = VocabularyFlashCardSelector(),
            dateProvider = object : VocabularyDateProvider() { override fun today() = "2026-09-25" },
        )
        advanceUntilIdle()

        assertEquals("vocab.b2", stateRepo.state.dailyWordContentId)
        assertNull(stateRepo.state.resumeDestination)

        viewModel.toggleDailyTurkish()
        advanceUntilIdle()
        assertEquals(VocabularyResumeDestination.WORD_OF_DAY, stateRepo.state.resumeDestination)
    }

    @Test
    fun smartPracticeStartsOneDeterministicSupportedSessionWithoutModePicker() = runTest {
        val stateRepo = FakeStateRepository()
        val userRepo = FakeUserRepository()
        val viewModel = VocabularyFeatureViewModel(
            vocabularyRepository = FakeVocabularyRepository(),
            featureStateRepository = stateRepo,
            userLearningRepository = userRepo,
            recordEvidence = RecordLearningEvidenceUseCase(userRepo, MasteryEngine(ReviewScheduler()), MistakeEngine()),
            dailySelector = VocabularyDailySelector(),
            practiceEngine = VocabularyPracticeEngine(),
            flashCardSelector = VocabularyFlashCardSelector(),
            dateProvider = object : VocabularyDateProvider() { override fun today() = "2026-09-25" },
        )
        advanceUntilIdle()

        viewModel.startSmartPractice()
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.practiceSession)
        assertTrue(viewModel.uiState.value.practiceSession!!.questions.isNotEmpty())
        assertEquals(VocabularyResumeDestination.PRACTICE_LABS, stateRepo.state.resumeDestination)

        val question = viewModel.uiState.value.currentPracticeQuestion!!
        viewModel.selectPracticeAnswer(question.correctAnswer)
        viewModel.submitPracticeAnswer()
        viewModel.submitPracticeAnswer()
        advanceUntilIdle()

        assertEquals(1, userRepo.evidence.size)
        assertEquals(question.targetContentId, userRepo.evidence.single().contentId)
    }

    @Test
    fun availablePracticeModesOnlyReturnsModesSupportedByRealCandidateContent() = runTest {
        val stateRepo = FakeStateRepository()
        val userRepo = FakeUserRepository()
        val viewModel = VocabularyFeatureViewModel(
            vocabularyRepository = FakeVocabularyRepository(),
            featureStateRepository = stateRepo,
            userLearningRepository = userRepo,
            recordEvidence = RecordLearningEvidenceUseCase(userRepo, MasteryEngine(ReviewScheduler()), MistakeEngine()),
            dailySelector = VocabularyDailySelector(),
            practiceEngine = VocabularyPracticeEngine(),
            flashCardSelector = VocabularyFlashCardSelector(),
            dateProvider = object : VocabularyDateProvider() { override fun today() = "2026-09-25" },
        )
        advanceUntilIdle()

        assertEquals(
            listOf(
                VocabularyPracticeMode.WORD_MATCH,
                VocabularyPracticeMode.MEANING_MATCH,
                VocabularyPracticeMode.SPEED_DRILL,
            ),
            viewModel.availablePracticeModes(),
        )
    }

    @Test
    fun smartPracticeUsesFlashRecallWhenRealDueReviewStateExists() = runTest {
        val stateRepo = FakeStateRepository()
        val userRepo = FakeUserRepository().apply {
            addDueReview("vocab.b2")
        }
        val viewModel = VocabularyFeatureViewModel(
            vocabularyRepository = FakeVocabularyRepository(),
            featureStateRepository = stateRepo,
            userLearningRepository = userRepo,
            recordEvidence = RecordLearningEvidenceUseCase(userRepo, MasteryEngine(ReviewScheduler()), MistakeEngine()),
            dailySelector = VocabularyDailySelector(),
            practiceEngine = VocabularyPracticeEngine(),
            flashCardSelector = VocabularyFlashCardSelector(),
            dateProvider = object : VocabularyDateProvider() { override fun today() = "2026-09-25" },
        )
        advanceUntilIdle()

        viewModel.startSmartPractice()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isFlashPractice)
        assertEquals(listOf("vocab.b2"), viewModel.uiState.value.flashCards.map { it.id })
        assertEquals(VocabularyResumeDestination.FLASH_CARDS, stateRepo.state.resumeDestination)
    }
}

private class FakeStateRepository : VocabularyFeatureStateRepository {
    var state = VocabularyFeatureState()
    override fun observeState(): Flow<VocabularyFeatureState> = flowOf(state)
    override suspend fun getState() = state
    override suspend fun saveDailyWord(date: String, contentId: String) { state = state.copy(dailyWordDate = date, dailyWordContentId = contentId) }
    override suspend fun saveResume(destination: VocabularyResumeDestination, contentId: String?, contextId: String?, practiceMode: VocabularyPracticeMode?) {
        state = state.copy(resumeDestination = destination, resumeContentId = contentId, resumeContextId = contextId, resumePracticeMode = practiceMode)
    }
}

private class FakeVocabularyRepository : VocabularyRepository {
    private val words = listOf(
        VocabItem("vocab.b2", "align", "B2", "verb", "", definitionEn = "agree", meaningTr = "uyum"),
        VocabItem("vocab.b2.coordinate", "coordinate", "B2", "verb", "", definitionEn = "organize", meaningTr = "koordine etmek"),
    )
    override fun getAllVocab() = flowOf(words)
    override fun getVocabByLevel(level: String) = flowOf(words.filter { it.cefrLevel == level })
    override fun getVocabById(id: String) = flowOf(words.firstOrNull { it.id == id })
    override fun searchVocab(query: String) = flowOf(words)
    override suspend fun getVocabCount() = words.size
    override suspend fun getAllVocabSnapshot() = words
    override suspend fun getVocabByIdSync(id: String) = words.firstOrNull { it.id == id }
    override suspend fun getVocabByIds(ids: List<String>) = words.filter { it.id in ids }
    override suspend fun getVocabByLevelSnapshot(level: String) = words.filter { it.cefrLevel == level }
}

private class FakeUserRepository : UserLearningRepository {
    val evidence = mutableListOf<LearningEvidence>()
    private val schedules = mutableMapOf<String, ReviewSchedule>()
    private val snapshots = mutableMapOf<String, MasterySnapshot>()
    fun addDueReview(contentId: String) {
        schedules[contentId] = ReviewSchedule(contentId, "vocabulary", 0L, 1f)
    }
    override suspend fun recordEvidence(evidence: LearningEvidence): Long { this.evidence += evidence; return this.evidence.size.toLong() }
    override fun getEvidenceForContent(contentId: String) = flowOf(evidence.filter { it.contentId == contentId })
    override fun getRecentEvidence(limit: Int) = flowOf(evidence.takeLast(limit))
    override fun getMasterySnapshot(contentId: String) = flowOf(snapshots[contentId])
    override suspend fun getMasterySnapshotSync(contentId: String) = snapshots[contentId]
    override fun getAllMasterySnapshots() = flowOf(snapshots.values.toList())
    override suspend fun saveMasterySnapshot(snapshot: MasterySnapshot) { snapshots[snapshot.contentId] = snapshot }
    override fun getActiveMistakes() = flowOf(emptyList<MistakeRecord>())
    override fun getAllMistakes() = flowOf(emptyList<MistakeRecord>())
    override suspend fun findMistake(contentId: String, trapType: String) = null
    override suspend fun saveMistake(mistake: MistakeRecord) = 0L
    override fun getDueReviews(currentTimestamp: Long) = flowOf(schedules.values.filter { it.nextReviewDueTimestamp <= currentTimestamp })
    override fun getReviewSchedule(contentId: String) = flowOf(schedules[contentId])
    override suspend fun getReviewScheduleSync(contentId: String) = schedules[contentId]
    override suspend fun saveReviewSchedule(schedule: ReviewSchedule) { schedules[schedule.contentId] = schedule }
    override fun getUserProfile(id: String) = flowOf(UserProfile(estimatedVocabLevel = "B2"))
    override suspend fun getUserProfileSync(id: String) = UserProfile(estimatedVocabLevel = "B2")
    override suspend fun saveUserProfile(profile: UserProfile) = Unit
}
