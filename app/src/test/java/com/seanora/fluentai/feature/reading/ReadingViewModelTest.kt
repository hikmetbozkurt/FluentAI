package com.seanora.fluentai.feature.reading

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.ReadingArticle
import com.seanora.fluentai.core.model.ReadingComprehensionQuestion
import com.seanora.fluentai.core.model.ReadingParagraph
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.core.model.VocabularyAnnotation
import com.seanora.fluentai.data.repository.ReadingRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.domain.mastery.MasteryEngine
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import com.seanora.fluentai.domain.mistakes.MistakeEngine
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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReadingViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val mockArticles = listOf(
        ReadingArticle(
            id = "reading.b1.agile-sprint-rituals",
            title = "Agile Sprint Rituals",
            cefrLevel = "B1",
            category = "engineering_culture",
            summaryEn = "An overview of daily standups and sprint planning.",
            summaryTr = "Günlük standup ve sprint planlama üzerine genel bakış.",
            wordCount = 280,
            estimatedReadingMinutes = 3,
            paragraphs = listOf(
                ReadingParagraph(
                    paragraphIndex = 1,
                    title = "Cadence",
                    contentEn = "Agile teams work in fixed cadences.",
                    contentTr = "Çevik ekipler sabit döngülerle çalışır."
                ),
                ReadingParagraph(
                    paragraphIndex = 2,
                    title = "Standup",
                    contentEn = "Daily standups unblock work.",
                    contentTr = "Günlük toplantılar engelleri çözer."
                )
            ),
            vocabularyAnnotations = listOf(
                VocabularyAnnotation(
                    word = "deliverable",
                    vocabId = "vocab.deliverable",
                    contextDefinitionEn = "A tangible product outcome.",
                    contextMeaningTr = "Teslimat çıktısı."
                )
            ),
            comprehensionQuestions = listOf(
                ReadingComprehensionQuestion(
                    id = "q_agile_01",
                    questionEn = "What is the primary function of standups?",
                    options = listOf("Unblock work", "Drink coffee"),
                    correctAnswer = "Unblock work",
                    explanationEn = "Standups surface blockers.",
                    explanationTr = "Standup engelleri çözer."
                )
            ),
            topicTags = listOf("agile"),
            relatedIds = listOf("vocab.deliverable")
        ),
        ReadingArticle(
            id = "reading.c1.platform-economics",
            title = "Platform Economics",
            cefrLevel = "C1",
            category = "business_strategy",
            summaryEn = "Strategic mechanics of digital platforms.",
            summaryTr = "Dijital platform ekonomisi.",
            wordCount = 360,
            estimatedReadingMinutes = 4,
            paragraphs = listOf(
                ReadingParagraph(
                    paragraphIndex = 1,
                    title = "Network Effects",
                    contentEn = "Two-sided markets coordinate value exchange.",
                    contentTr = "İki taraflı pazarlar değer alışverişini koordine eder."
                )
            ),
            vocabularyAnnotations = emptyList(),
            comprehensionQuestions = emptyList(),
            topicTags = listOf("strategy"),
            relatedIds = emptyList()
        )
    )

    private val fakeReadingRepository = object : ReadingRepository {
        override fun getAllArticles(): Flow<List<ReadingArticle>> = flowOf(mockArticles)

        override fun getArticlesByLevel(level: String): Flow<List<ReadingArticle>> =
            flowOf(mockArticles.filter { it.cefrLevel == level })

        override fun getArticleById(id: String): Flow<ReadingArticle?> =
            flowOf(mockArticles.find { it.id == id })

        override fun getArticlesByCategory(category: String): Flow<List<ReadingArticle>> =
            flowOf(mockArticles.filter { it.category == category })

        override fun searchArticles(query: String): Flow<List<ReadingArticle>> =
            flowOf(mockArticles.filter { it.title.contains(query, ignoreCase = true) })

        override suspend fun getArticleCount(): Int = mockArticles.size
    }

    private val recordedEvidence = mutableListOf<LearningEvidence>()

    private val fakeUserLearningRepository = object : UserLearningRepository {
        override suspend fun recordEvidence(evidence: LearningEvidence): Long {
            recordedEvidence.add(evidence)
            return recordedEvidence.size.toLong()
        }
        override fun getEvidenceForContent(contentId: String): Flow<List<LearningEvidence>> =
            flowOf(recordedEvidence.filter { it.contentId == contentId })
        override fun getRecentEvidence(limit: Int): Flow<List<LearningEvidence>> =
            flowOf(recordedEvidence.takeLast(limit))
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
        override fun getUserProfile(id: String): Flow<UserProfile?> = flowOf(null)
        override suspend fun getUserProfileSync(id: String): UserProfile? = null
        override suspend fun saveUserProfile(profile: UserProfile) {}
    }

    private val recordLearningEvidenceUseCase = RecordLearningEvidenceUseCase(
        userLearningRepository = fakeUserLearningRepository,
        masteryEngine = MasteryEngine(),
        mistakeEngine = MistakeEngine()
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        recordedEvidence.clear()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun uiState_initialLoad_populatesLibraryWithoutSelectingAnArticle() = runTest {
        val viewModel = ReadingViewModel(fakeReadingRepository, recordLearningEvidenceUseCase)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(2, state.articles.size)
        assertNull(state.selectedArticle)
        assertFalse(state.revealedSummary)
        assertTrue(state.revealedParagraphIndices.isEmpty())
    }

    @Test
    fun uiState_filterByLevel_returnsMatchingArticlesOnly() = runTest {
        val viewModel = ReadingViewModel(fakeReadingRepository, recordLearningEvidenceUseCase)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        viewModel.selectLevel("C1")
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("C1", state.selectedLevel)
        assertEquals(1, state.articles.size)
        assertEquals("reading.c1.platform-economics", state.articles[0].id)
    }

    @Test
    fun uiState_toggleSummaryAndParagraphReveal_updatesState() = runTest {
        val viewModel = ReadingViewModel(fakeReadingRepository, recordLearningEvidenceUseCase)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.revealedSummary)
        viewModel.toggleSummaryReveal()
        testScheduler.advanceUntilIdle()
        assertTrue(viewModel.uiState.value.revealedSummary)

        assertTrue(viewModel.uiState.value.revealedParagraphIndices.isEmpty())
        viewModel.toggleParagraphReveal(1)
        testScheduler.advanceUntilIdle()
        assertTrue(viewModel.uiState.value.revealedParagraphIndices.contains(1))

        viewModel.toggleParagraphReveal(1)
        testScheduler.advanceUntilIdle()
        assertFalse(viewModel.uiState.value.revealedParagraphIndices.contains(1))
    }

    @Test
    fun uiState_selectAnnotation_togglesSelectedAnnotation() = runTest {
        val viewModel = ReadingViewModel(fakeReadingRepository, recordLearningEvidenceUseCase)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        val annotation = mockArticles[0].vocabularyAnnotations[0]
        viewModel.selectAnnotation(annotation)
        testScheduler.advanceUntilIdle()

        assertEquals("deliverable", viewModel.uiState.value.selectedAnnotation?.word)

        viewModel.selectAnnotation(null)
        testScheduler.advanceUntilIdle()
        assertEquals(null, viewModel.uiState.value.selectedAnnotation)
    }

    @Test
    fun uiState_submitAnswer_evaluatesAndRecordsEvidence() = runTest {
        val viewModel = ReadingViewModel(fakeReadingRepository, recordLearningEvidenceUseCase)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        val article = mockArticles[0]
        val question = article.comprehensionQuestions[0]

        // Select correct answer
        viewModel.selectAnswer(question.id, "Unblock work")
        testScheduler.advanceUntilIdle()
        assertEquals("Unblock work", viewModel.uiState.value.userAnswers[question.id])

        // Submit answer
        viewModel.submitAnswer(article.id, question)
        testScheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.submittedQuestions.contains(question.id))
        assertEquals(1, recordedEvidence.size)
        assertEquals("reading.b1.agile-sprint-rituals", recordedEvidence[0].contentId)
        assertEquals("reading", recordedEvidence[0].domain)
        assertTrue(recordedEvidence[0].isCorrect)
        assertEquals(1.0f, recordedEvidence[0].score)
    }
}
