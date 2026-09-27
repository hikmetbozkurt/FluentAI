package com.seanora.fluentai.feature.reading

import com.seanora.fluentai.app.navigation.ReadingSessionMode
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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class ReadingSessionViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before fun setUp() = Dispatchers.setMain(dispatcher)
    @After fun tearDown() = Dispatchers.resetMain()

    @Test
    fun semanticIdOwnsTheSessionAndMissingIdDoesNotFallBack() = runTest {
        val viewModel = sessionViewModel()

        viewModel.openArticle(ARTICLE.id, ReadingSessionMode.GUIDED)
        advanceUntilIdle()
        assertEquals(ARTICLE.id, viewModel.uiState.value.article?.id)
        assertEquals(ReadingSessionMode.GUIDED, viewModel.uiState.value.mode)

        viewModel.openArticle("reading.missing", ReadingSessionMode.STANDARD)
        advanceUntilIdle()
        assertNull(viewModel.uiState.value.article)
        assertEquals("This article is no longer available.", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun localSectionSwitchKeepsArticleAndQuestionProgress() = runTest {
        val users = SessionUserRepository()
        val viewModel = sessionViewModel(users)
        viewModel.openArticle(ARTICLE.id, ReadingSessionMode.STANDARD)
        advanceUntilIdle()

        viewModel.selectSection(ReadingSessionSection.QUESTIONS)
        viewModel.selectAnswer(QUESTION.id, QUESTION.correctAnswer)
        viewModel.checkAnswer()
        viewModel.checkAnswer()
        viewModel.selectSection(ReadingSessionSection.ARTICLE)
        viewModel.selectSection(ReadingSessionSection.QUESTIONS)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(ARTICLE.id, state.article?.id)
        assertEquals(QUESTION.correctAnswer, state.selectedAnswers[QUESTION.id])
        assertTrue(QUESTION.id in state.submittedQuestionIds)
        assertEquals(1, users.evidence.size)
        assertEquals(ARTICLE.id, users.evidence.single().contentId)
    }

    @Test
    fun articleCallToActionMovesToQuestionsWithoutChangingArticle() = runTest {
        val viewModel = sessionViewModel()
        viewModel.openArticle(ARTICLE.id, ReadingSessionMode.STANDARD)
        advanceUntilIdle()

        viewModel.selectSection(ReadingSessionSection.QUESTIONS)

        assertEquals(ReadingSessionSection.QUESTIONS, viewModel.uiState.value.section)
        assertEquals(ARTICLE.id, viewModel.uiState.value.article?.id)
    }

    private fun sessionViewModel(users: SessionUserRepository = SessionUserRepository()) =
        ReadingSessionViewModel(
            readingRepository = SessionReadingRepository(),
            recordLearningEvidence = RecordLearningEvidenceUseCase(users, MasteryEngine(), MistakeEngine()),
        )

    companion object {
        private val QUESTION = ReadingComprehensionQuestion(
            id = "reading.b2.work.q1",
            questionEn = "What matters most?",
            options = listOf("Clarity", "Speed"),
            correctAnswer = "Clarity",
            explanationEn = "The text emphasizes clarity.",
            explanationTr = "Metin açıklığı vurgular.",
        )
        val ARTICLE = ReadingArticle(
            id = "reading.b2.work.article-001",
            title = "Clear Decisions",
            cefrLevel = "B2",
            category = "work",
            summaryEn = "How teams make clear decisions.",
            summaryTr = "Ekiplerin net karar alma biçimi.",
            wordCount = 120,
            estimatedReadingMinutes = 2,
            paragraphs = listOf(ReadingParagraph(0, contentEn = "Clarity matters.", contentTr = "Açıklık önemlidir.")),
            vocabularyAnnotations = emptyList(),
            comprehensionQuestions = listOf(QUESTION),
            topicTags = listOf("work"),
            relatedIds = emptyList(),
        )
    }
}

private class SessionReadingRepository : ReadingRepository {
    private val articles = listOf(ReadingSessionViewModelTest.ARTICLE)
    override fun getAllArticles(): Flow<List<ReadingArticle>> = flowOf(articles)
    override fun getArticlesByLevel(level: String) = flowOf(articles.filter { it.cefrLevel == level })
    override fun getArticleById(id: String) = flowOf(articles.firstOrNull { it.id == id })
    override fun getArticlesByCategory(category: String) = flowOf(articles.filter { it.category == category })
    override fun searchArticles(query: String) = flowOf(articles.filter { it.title.contains(query, true) })
    override suspend fun getArticleCount() = articles.size
}

private class SessionUserRepository : UserLearningRepository {
    val evidence = mutableListOf<LearningEvidence>()
    override suspend fun recordEvidence(evidence: LearningEvidence): Long { this.evidence += evidence; return this.evidence.size.toLong() }
    override fun getEvidenceForContent(contentId: String) = flowOf(evidence.filter { it.contentId == contentId })
    override fun getRecentEvidence(limit: Int) = flowOf(evidence.take(limit))
    override fun getMasterySnapshot(contentId: String) = flowOf<MasterySnapshot?>(null)
    override suspend fun getMasterySnapshotSync(contentId: String): MasterySnapshot? = null
    override fun getAllMasterySnapshots() = flowOf(emptyList<MasterySnapshot>())
    override suspend fun saveMasterySnapshot(snapshot: MasterySnapshot) = Unit
    override fun getActiveMistakes() = flowOf(emptyList<MistakeRecord>())
    override fun getAllMistakes() = flowOf(emptyList<MistakeRecord>())
    override suspend fun findMistake(contentId: String, trapType: String): MistakeRecord? = null
    override suspend fun saveMistake(mistake: MistakeRecord) = 0L
    override fun getDueReviews(currentTimestamp: Long) = flowOf(emptyList<ReviewSchedule>())
    override fun getReviewSchedule(contentId: String) = flowOf<ReviewSchedule?>(null)
    override suspend fun getReviewScheduleSync(contentId: String): ReviewSchedule? = null
    override suspend fun saveReviewSchedule(schedule: ReviewSchedule) = Unit
    override fun getUserProfile(id: String) = flowOf<UserProfile?>(null)
    override suspend fun getUserProfileSync(id: String): UserProfile? = null
    override suspend fun saveUserProfile(profile: UserProfile) = Unit
}
