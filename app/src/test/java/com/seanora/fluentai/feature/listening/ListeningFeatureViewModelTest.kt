package com.seanora.fluentai.feature.listening

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.ListeningComprehensionQuestion
import com.seanora.fluentai.core.model.ListeningScenario
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.Speaker
import com.seanora.fluentai.core.model.TranscriptItem
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.data.repository.ListeningRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.domain.listening.ListeningDateProvider
import com.seanora.fluentai.domain.listening.ListeningFeatureEngine
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class ListeningFeatureViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before fun setUp() = Dispatchers.setMain(dispatcher)
    @After fun tearDown() = Dispatchers.resetMain()

    @Test
    fun initialStateDerivesDailyAndContinueAndDictationSubmissionRecordsEvidence() = runTest {
        val userRepository = FeatureListeningUserRepository().apply {
            evidence += LearningEvidence(
                contentId = SCENARIO.id,
                domain = "listening",
                activityType = "comprehension_check",
                isCorrect = false,
                score = 0f,
                timestamp = 10L,
            )
        }
        val viewModel = ListeningFeatureViewModel(
            listeningRepository = FeatureListeningRepository(),
            userLearningRepository = userRepository,
            recordLearningEvidence = RecordLearningEvidenceUseCase(userRepository, MasteryEngine(), MistakeEngine()),
            engine = ListeningFeatureEngine(),
            dateProvider = object : ListeningDateProvider() { override fun today() = "2026-09-25" },
        )
        advanceUntilIdle()

        assertEquals(SCENARIO.id, viewModel.uiState.value.dailyScenario?.id)
        assertEquals(SCENARIO.id, viewModel.uiState.value.continueScenario?.id)
        assertEquals(1, viewModel.uiState.value.dictationItems.size)

        viewModel.updateDictationAnswer("HELLO, team!")
        viewModel.submitDictation()
        advanceUntilIdle()

        assertEquals(2, userRepository.evidence.size)
        assertEquals("dictation", userRepository.evidence.last().activityType)
        assertTrue(userRepository.evidence.last().isCorrect)
        assertTrue(viewModel.uiState.value.dictationResult?.isCorrect == true)
    }

    companion object {
        val QUESTION = ListeningComprehensionQuestion(
            id = "listening.b1.sample.q1",
            questionEn = "What was said?",
            options = listOf("Hello", "Goodbye"),
            correctAnswer = "Hello",
            explanationEn = "The speaker said hello.",
            explanationTr = "Konuşmacı merhaba dedi.",
        )
        val SCENARIO = ListeningScenario(
            id = "listening.b1.sample",
            title = "Sample",
            cefrLevel = "B1",
            category = "work",
            scenarioContext = "A real meeting.",
            speakers = listOf(Speaker("speaker", "Speaker", "Colleague")),
            audioRef = "audio/listening/sample.mp3",
            durationSeconds = 5,
            transcriptItems = listOf(TranscriptItem(1, "speaker", 0L, 3000L, "Hello team.", "Merhaba ekip.")),
            keyVocabulary = emptyList(),
            comprehensionQuestions = listOf(QUESTION),
            topicTags = listOf("work"),
            relatedIds = emptyList(),
        )
    }
}

private class FeatureListeningRepository : ListeningRepository {
    private val scenarios = listOf(ListeningFeatureViewModelTest.SCENARIO)
    override fun getAllScenarios(): Flow<List<ListeningScenario>> = flowOf(scenarios)
    override fun getScenariosByLevel(level: String) = flowOf(scenarios.filter { it.cefrLevel == level })
    override fun getScenarioById(id: String) = flowOf(scenarios.firstOrNull { it.id == id })
    override fun getScenariosByCategory(category: String) = flowOf(scenarios.filter { it.category == category })
    override fun searchScenarios(query: String) = flowOf(scenarios.filter { it.title.contains(query, true) })
    override suspend fun getScenarioCount() = scenarios.size
}

private class FeatureListeningUserRepository : UserLearningRepository {
    val evidence = mutableListOf<LearningEvidence>()
    private val mastery = mutableMapOf<String, MasterySnapshot>()
    private val reviews = mutableMapOf<String, ReviewSchedule>()
    override suspend fun recordEvidence(evidence: LearningEvidence): Long { this.evidence += evidence; return this.evidence.size.toLong() }
    override fun getEvidenceForContent(contentId: String) = flowOf(evidence.filter { it.contentId == contentId })
    override fun getRecentEvidence(limit: Int) = flowOf(evidence.sortedByDescending { it.timestamp }.take(limit))
    override fun getMasterySnapshot(contentId: String) = flowOf(mastery[contentId])
    override suspend fun getMasterySnapshotSync(contentId: String) = mastery[contentId]
    override fun getAllMasterySnapshots() = flowOf(mastery.values.toList())
    override suspend fun saveMasterySnapshot(snapshot: MasterySnapshot) { mastery[snapshot.contentId] = snapshot }
    override fun getActiveMistakes() = flowOf(emptyList<MistakeRecord>())
    override fun getAllMistakes() = flowOf(emptyList<MistakeRecord>())
    override suspend fun findMistake(contentId: String, trapType: String) = null
    override suspend fun saveMistake(mistake: MistakeRecord) = 0L
    override fun getDueReviews(currentTimestamp: Long) = flowOf(reviews.values.filter { it.nextReviewDueTimestamp <= currentTimestamp })
    override fun getReviewSchedule(contentId: String) = flowOf(reviews[contentId])
    override suspend fun getReviewScheduleSync(contentId: String) = reviews[contentId]
    override suspend fun saveReviewSchedule(schedule: ReviewSchedule) { reviews[schedule.contentId] = schedule }
    override fun getUserProfile(id: String) = flowOf(UserProfile(estimatedListeningLevel = "B1"))
    override suspend fun getUserProfileSync(id: String) = UserProfile(estimatedListeningLevel = "B1")
    override suspend fun saveUserProfile(profile: UserProfile) = Unit
}
