package com.seanora.fluentai.feature.reading

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.ReadingArticle
import com.seanora.fluentai.core.model.ReadingComprehensionQuestion
import com.seanora.fluentai.core.model.ReadingParagraph
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.data.repository.ReadingRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.domain.mastery.MasteryEngine
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import com.seanora.fluentai.domain.mistakes.MistakeEngine
import com.seanora.fluentai.domain.reading.ReadingDateProvider
import com.seanora.fluentai.domain.reading.ReadingFeatureEngine
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
class ReadingFeatureViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before fun setUp() = Dispatchers.setMain(dispatcher)
    @After fun tearDown() = Dispatchers.resetMain()

    @Test
    fun initialStateDerivesDailyAndContinueWhileOnlyAnswersRecordEvidence() = runTest {
        val userRepository = FeatureReadingUserRepository().apply {
            evidence += LearningEvidence(
                contentId = ARTICLE.id,
                domain = "reading",
                activityType = "comprehension_check",
                isCorrect = false,
                score = 0f,
                timestamp = 10L,
            )
        }
        val viewModel = ReadingFeatureViewModel(
            readingRepository = FeatureReadingRepository(),
            userLearningRepository = userRepository,
            recordLearningEvidence = RecordLearningEvidenceUseCase(userRepository, MasteryEngine(), MistakeEngine()),
            engine = ReadingFeatureEngine(),
            dateProvider = object : ReadingDateProvider() { override fun today() = "2026-09-25" },
        )
        advanceUntilIdle()

        assertEquals(ARTICLE.id, viewModel.uiState.value.dailyArticle?.id)
        assertEquals(ARTICLE.id, viewModel.uiState.value.continueArticle?.id)

        viewModel.selectGuidedArticle(ARTICLE.id)
        advanceUntilIdle()
        assertEquals(1, userRepository.evidence.size)

        viewModel.submitAnswer(ARTICLE.id, QUESTION, QUESTION.correctAnswer, "guided_reading")
        advanceUntilIdle()

        assertEquals(2, userRepository.evidence.size)
        assertEquals("guided_reading", userRepository.evidence.last().activityType)
        assertTrue(userRepository.evidence.last().isCorrect)
        assertTrue(viewModel.uiState.value.submittedQuestionIds.contains(QUESTION.id))
    }

    companion object {
        val QUESTION = ReadingComprehensionQuestion(
            id = "reading.b1.article.q1",
            questionEn = "What is the key point?",
            options = listOf("Coordination", "Isolation"),
            correctAnswer = "Coordination",
            explanationEn = "The paragraph emphasizes coordination.",
            explanationTr = "Paragraf koordinasyonu vurgular.",
        )
        val ARTICLE = ReadingArticle(
            id = "reading.b1.article",
            title = "Working Together",
            cefrLevel = "B1",
            category = "work",
            summaryEn = "Teams coordinate.",
            summaryTr = "Ekipler koordine olur.",
            wordCount = 100,
            estimatedReadingMinutes = 2,
            paragraphs = listOf(ReadingParagraph(1, contentEn = "Teams coordinate daily.", contentTr = "Ekipler günlük koordine olur.")),
            vocabularyAnnotations = emptyList(),
            comprehensionQuestions = listOf(QUESTION),
            topicTags = listOf("work"),
            relatedIds = emptyList(),
        )
    }
}

private class FeatureReadingRepository : ReadingRepository {
    private val articles = listOf(ReadingFeatureViewModelTest.ARTICLE)
    override fun getAllArticles(): Flow<List<ReadingArticle>> = flowOf(articles)
    override fun getArticlesByLevel(level: String) = flowOf(articles.filter { it.cefrLevel == level })
    override fun getArticleById(id: String) = flowOf(articles.firstOrNull { it.id == id })
    override fun getArticlesByCategory(category: String) = flowOf(articles.filter { it.category == category })
    override fun searchArticles(query: String) = flowOf(articles.filter { it.title.contains(query, true) })
    override suspend fun getArticleCount() = articles.size
}

private class FeatureReadingUserRepository : UserLearningRepository {
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
    override fun getUserProfile(id: String) = flowOf(UserProfile(estimatedReadingLevel = "B1"))
    override suspend fun getUserProfileSync(id: String) = UserProfile(estimatedReadingLevel = "B1")
    override suspend fun saveUserProfile(profile: UserProfile) = Unit
}
