package com.seanora.fluentai.feature.grammar

import com.seanora.fluentai.core.model.Exercise
import com.seanora.fluentai.core.model.GrammarExample
import com.seanora.fluentai.core.model.GrammarFeatureState
import com.seanora.fluentai.core.model.GrammarLesson
import com.seanora.fluentai.core.model.GrammarResumeDestination
import com.seanora.fluentai.core.model.GrammarTrap
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.MistakeStage
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.data.repository.GrammarFeatureStateRepository
import com.seanora.fluentai.data.repository.GrammarRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.domain.grammar.GrammarFeatureEngine
import com.seanora.fluentai.domain.mastery.MasteryEngine
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import com.seanora.fluentai.domain.mistakes.MistakeEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class GrammarFeatureViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before fun setUp() = Dispatchers.setMain(dispatcher)
    @After fun tearDown() = Dispatchers.resetMain()

    @Test
    fun librarySelectionCreatesResumeButOnlySubmittedAnswersCreateEvidenceAndMistakesProgressNormally() = runTest {
        val stateRepository = FakeGrammarStateRepository()
        val userRepository = FakeGrammarUserRepository()
        val viewModel = GrammarFeatureViewModel(
            grammarRepository = FakeGrammarRepository(),
            stateRepository = stateRepository,
            userLearningRepository = userRepository,
            recordLearningEvidence = RecordLearningEvidenceUseCase(userRepository, MasteryEngine(), MistakeEngine()),
            engine = GrammarFeatureEngine(),
        )
        advanceUntilIdle()

        viewModel.recordLibrarySelection(LESSON.id)
        advanceUntilIdle()
        assertTrue(userRepository.evidence.isEmpty())
        assertEquals(GrammarResumeDestination.LIBRARY, stateRepository.state.resumeDestination)

        viewModel.recordLibraryExercise(EXERCISE, "have finished yesterday")
        viewModel.recordLibraryExercise(EXERCISE, "have finished yesterday")
        advanceUntilIdle()

        assertEquals(2, userRepository.evidence.size)
        assertTrue(userRepository.evidence.all { it.domain == "grammar" })
        assertEquals(2, userRepository.mastery[LESSON.id]?.totalAttempts)
        assertNotNull(userRepository.reviews[LESSON.id])
        assertEquals(MistakeStage.POSSIBLE, userRepository.mistakes["${LESSON.id}:aspect_confusion"]?.stage)
    }

    @Test
    fun clearSentenceRestoresCurrentQuestionForImmediateRetryWithoutRecordingEvidence() = runTest {
        val stateRepository = FakeGrammarStateRepository()
        val userRepository = FakeGrammarUserRepository()
        val viewModel = GrammarFeatureViewModel(
            grammarRepository = FakeGrammarRepository(),
            stateRepository = stateRepository,
            userLearningRepository = userRepository,
            recordLearningEvidence = RecordLearningEvidenceUseCase(userRepository, MasteryEngine(), MistakeEngine()),
            engine = GrammarFeatureEngine(),
        )
        advanceUntilIdle()

        val question = requireNotNull(viewModel.uiState.value.currentSentence)
        question.scrambledTokens.forEach(viewModel::addSentenceToken)
        viewModel.submitSentence()
        advanceUntilIdle()
        val evidenceBeforeClear = userRepository.evidence.size

        viewModel.clearSentence()
        advanceUntilIdle()

        assertEquals(question, viewModel.uiState.value.currentSentence)
        assertTrue(viewModel.uiState.value.builtTokens.isEmpty())
        assertEquals(false, viewModel.uiState.value.sentenceSubmitted)
        assertEquals(null, viewModel.uiState.value.sentenceCorrect)
        assertEquals(evidenceBeforeClear, userRepository.evidence.size)
    }

    @Test
    fun sentenceSubmissionReturnsOneSessionResultAndDoesNotDoubleRecordEvidence() = runTest {
        val userRepository = FakeGrammarUserRepository()
        val viewModel = GrammarFeatureViewModel(
            grammarRepository = FakeGrammarRepository(),
            stateRepository = FakeGrammarStateRepository(),
            userLearningRepository = userRepository,
            recordLearningEvidence = RecordLearningEvidenceUseCase(userRepository, MasteryEngine(), MistakeEngine()),
            engine = GrammarFeatureEngine(),
        )
        advanceUntilIdle()

        val question = requireNotNull(viewModel.uiState.value.currentSentence)
        question.correctTokens.forEach(viewModel::addSentenceToken)

        assertEquals(true, viewModel.submitSentence())
        assertEquals(null, viewModel.submitSentence())
        advanceUntilIdle()
        assertEquals(1, userRepository.evidence.size)
    }

    @Test
    fun activatingSessionKeepsPracticeBoundToExactSemanticLessonId() = runTest {
        val viewModel = GrammarFeatureViewModel(
            grammarRepository = FakeGrammarRepository(),
            stateRepository = FakeGrammarStateRepository(),
            userLearningRepository = FakeGrammarUserRepository(),
            recordLearningEvidence = RecordLearningEvidenceUseCase(FakeGrammarUserRepository(), MasteryEngine(), MistakeEngine()),
            engine = GrammarFeatureEngine(),
        )
        advanceUntilIdle()

        assertTrue(viewModel.activateLesson(LESSON.id))
        assertEquals(LESSON.id, viewModel.uiState.value.activeLessonId)
        assertEquals(LESSON.id, viewModel.uiState.value.currentSentence?.lessonId)
        assertEquals(LESSON.id, viewModel.uiState.value.currentError?.lessonId)
    }

    @Test
    fun activatingUnknownSessionFailsGracefullyWithoutSelectingAnotherLesson() = runTest {
        val viewModel = GrammarFeatureViewModel(
            grammarRepository = FakeGrammarRepository(),
            stateRepository = FakeGrammarStateRepository(),
            userLearningRepository = FakeGrammarUserRepository(),
            recordLearningEvidence = RecordLearningEvidenceUseCase(FakeGrammarUserRepository(), MasteryEngine(), MistakeEngine()),
            engine = GrammarFeatureEngine(),
        )
        advanceUntilIdle()

        assertTrue(viewModel.activateLesson(LESSON.id))
        assertEquals(false, viewModel.activateLesson("grammar.missing"))
        assertEquals(null, viewModel.uiState.value.activeLessonId)
    }

    companion object {
        val LESSON = GrammarLesson(
            id = "grammar.present-perfect",
            title = "Present Perfect",
            cefrLevel = "B1",
            category = "aspect",
            summaryEn = "Connect past and present.",
            summaryTr = "Geçmişi şimdiye bağlar.",
            explanationTr = "Zaman bağlantısı.",
            examples = listOf(GrammarExample("I have finished the report.", "Raporu bitirdim.")),
            turkishTraps = listOf(
                GrammarTrap(
                    trapType = "aspect_confusion",
                    trapTitle = "Finished time marker",
                    explanationTr = "Yesterday ile Past Simple kullanılır.",
                    incorrectExample = "have finished yesterday",
                    correctExample = "finished yesterday",
                ),
            ),
        )
        val EXERCISE = Exercise(
            id = "exercise.grammar.present-perfect.1",
            targetContentId = LESSON.id,
            cefrLevel = "B1",
            skillDomain = "grammar",
            exerciseType = "multiple_choice",
            promptEn = "Choose the correct form",
            stem = "I ___ the report yesterday.",
            options = listOf("finished yesterday", "have finished yesterday"),
            correctAnswer = "finished yesterday",
            explanationEn = "Finished time requires Past Simple.",
            explanationTr = "Bitmiş zaman Past Simple gerektirir.",
        )
    }
}

private class FakeGrammarRepository : GrammarRepository {
    override fun getAllLessons(): Flow<List<GrammarLesson>> = flowOf(listOf(GrammarFeatureViewModelTest.LESSON))
    override fun getLessonsByLevel(level: String) = flowOf(listOf(GrammarFeatureViewModelTest.LESSON))
    override fun getLessonsByCategory(category: String) = flowOf(listOf(GrammarFeatureViewModelTest.LESSON))
    override fun getLessonById(id: String) = flowOf(GrammarFeatureViewModelTest.LESSON.takeIf { it.id == id })
    override fun searchLessons(query: String) = flowOf(listOf(GrammarFeatureViewModelTest.LESSON))
    override fun getExercisesForLesson(lessonId: String) = flowOf(listOf(GrammarFeatureViewModelTest.EXERCISE))
    override suspend fun getLessonCount() = 1
}

private class FakeGrammarStateRepository : GrammarFeatureStateRepository {
    var state = GrammarFeatureState()
    override fun observeState(): Flow<GrammarFeatureState> = flowOf(state)
    override suspend fun getState() = state
    override suspend fun saveResume(destination: GrammarResumeDestination, contentId: String, secondaryContentId: String?) {
        state = GrammarFeatureState(destination, contentId, secondaryContentId, 1L)
    }
}

private class FakeGrammarUserRepository : UserLearningRepository {
    val evidence = mutableListOf<LearningEvidence>()
    val mastery = mutableMapOf<String, MasterySnapshot>()
    val mistakes = mutableMapOf<String, MistakeRecord>()
    val reviews = mutableMapOf<String, ReviewSchedule>()
    override suspend fun recordEvidence(evidence: LearningEvidence): Long { this.evidence += evidence; return this.evidence.size.toLong() }
    override fun getEvidenceForContent(contentId: String) = flowOf(evidence.filter { it.contentId == contentId })
    override fun getRecentEvidence(limit: Int) = flowOf(evidence.takeLast(limit))
    override fun getMasterySnapshot(contentId: String) = flowOf(mastery[contentId])
    override suspend fun getMasterySnapshotSync(contentId: String) = mastery[contentId]
    override fun getAllMasterySnapshots() = flowOf(mastery.values.toList())
    override suspend fun saveMasterySnapshot(snapshot: MasterySnapshot) { mastery[snapshot.contentId] = snapshot }
    override fun getActiveMistakes() = flowOf(mistakes.values.filter { it.stage != MistakeStage.RESOLVED })
    override fun getAllMistakes() = flowOf(mistakes.values.toList())
    override suspend fun findMistake(contentId: String, trapType: String) = mistakes["$contentId:$trapType"]
    override suspend fun saveMistake(mistake: MistakeRecord): Long { mistakes["${mistake.contentId}:${mistake.trapType}"] = mistake; return 1L }
    override fun getDueReviews(currentTimestamp: Long) = flowOf(reviews.values.filter { it.nextReviewDueTimestamp <= currentTimestamp })
    override fun getReviewSchedule(contentId: String) = flowOf(reviews[contentId])
    override suspend fun getReviewScheduleSync(contentId: String) = reviews[contentId]
    override suspend fun saveReviewSchedule(schedule: ReviewSchedule) { reviews[schedule.contentId] = schedule }
    override fun getUserProfile(id: String) = flowOf(UserProfile())
    override suspend fun getUserProfileSync(id: String) = UserProfile()
    override suspend fun saveUserProfile(profile: UserProfile) = Unit
}
