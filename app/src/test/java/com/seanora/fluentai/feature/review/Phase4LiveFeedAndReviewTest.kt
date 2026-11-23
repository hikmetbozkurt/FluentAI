package com.seanora.fluentai.feature.review

import com.seanora.fluentai.app.isDistractionFreeReadingRoute
import com.seanora.fluentai.app.navigation.READING_LIVE_ARTICLE_ROUTE
import com.seanora.fluentai.core.model.GrammarLesson
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.ListeningScenario
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.MistakeStage
import com.seanora.fluentai.core.model.ReadingArticle
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.core.model.VocabItem
import com.seanora.fluentai.core.model.VocabularyCollection
import com.seanora.fluentai.data.feed.LiveReadingFeedRepositoryImpl
import com.seanora.fluentai.data.repository.GrammarRepository
import com.seanora.fluentai.data.repository.ListeningRepository
import com.seanora.fluentai.data.repository.ReadingRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.data.repository.VocabularyRepository
import com.seanora.fluentai.domain.mastery.MasteryEngine
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import com.seanora.fluentai.domain.mistakes.MistakeEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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

@OptIn(ExperimentalCoroutinesApi::class)
class Phase4LiveFeedAndReviewTest {

    private val dispatcher = StandardTestDispatcher()

    @Before fun setUp() = Dispatchers.setMain(dispatcher)
    @After fun tearDown() = Dispatchers.resetMain()

    @Test
    fun parseConversationAtom_extractsCleanParagraphsAndCefrLevel() {
        val atomXml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <feed xmlns="http://www.w3.org/2005/Atom">
              <entry>
                <title>How Modern Battery Grids Stabilize Clean Energy</title>
                <published>2026-11-20T10:00:00Z</published>
                <link rel="alternate" href="https://theconversation.com/example"/>
                <content type="html">&lt;p&gt;Large utility batteries now store surplus wind and solar electricity during sunny afternoons and release power smoothly when evening demand peaks across urban neighborhoods.&lt;/p&gt;&lt;p&gt;Grid engineers rely on predictive forecasting software to coordinate thousands of distributed storage units while keeping wholesale electricity prices predictable for families and businesses.&lt;/p&gt;</content>
              </entry>
            </feed>
        """.trimIndent()

        val articles = LiveReadingFeedRepositoryImpl.parseConversationAtom(atomXml, "Technology")
        assertEquals(1, articles.size)
        val art = articles.single()
        assertEquals("How Modern Battery Grids Stabilize Clean Energy", art.title)
        assertEquals("The Conversation", art.sourceName)
        assertEquals("CC BY-ND", art.sourceLicense)
        assertEquals(2, art.paragraphs.size)
        assertTrue(art.cefrLevel in setOf("A2", "B1", "B2", "C1", "C2"))
        assertTrue(isDistractionFreeReadingRoute(READING_LIVE_ARTICLE_ROUTE))
    }

    @Test
    fun parseGuardianJson_extractsParagraphsAndAttribution() {
        val guardianJson = """
            {
              "response": {
                "results": [
                  {
                    "webTitle": "Remote teams rethink meeting culture in 2026",
                    "sectionName": "Business",
                    "webPublicationDate": "2026-11-21T08:30:00Z",
                    "webUrl": "https://www.theguardian.com/business/example",
                    "fields": {
                      "trailText": "Companies cut recurring status calls in favor of written updates.",
                      "bodyText": "Many software organizations are redesigning their weekly schedules to protect deep focus hours. Leaders noticed that fragmented calendars slowed down product delivery and raised burnout. Teams now publish concise asynchronous memos before deciding whether a live call is truly necessary. Early surveys show higher employee satisfaction across distributed time zones. Clear written summaries also help non-native English speakers review complex trade-offs at their own pace. Managers report fewer misunderstandings when action items are recorded in one shared place."
                    }
                  }
                ]
              }
            }
        """.trimIndent()

        val articles = LiveReadingFeedRepositoryImpl.parseGuardianJson(guardianJson)
        assertEquals(1, articles.size)
        assertEquals("The Guardian", articles.single().sourceName)
        assertEquals("Business", articles.single().category)
        assertEquals(2, articles.single().paragraphs.size)
    }

    @Test
    fun reviewViewModel_resolvesDueReviewsAndAdvancesMistakeLifecycleOnFixIt() = runTest {
        val userRepo = ReviewFakeUserRepo()
        val mistakeEngine = MistakeEngine()
        val vm = ReviewViewModel(
            userLearningRepository = userRepo,
            vocabularyRepository = ReviewFakeVocabRepo(),
            grammarRepository = ReviewFakeGrammarRepo(),
            readingRepository = ReviewFakeReadingRepo(),
            listeningRepository = ReviewFakeListeningRepo(),
            mistakeEngine = mistakeEngine,
            recordLearningEvidence = RecordLearningEvidenceUseCase(userRepo, MasteryEngine(), mistakeEngine),
        )
        advanceUntilIdle()

        val state = vm.uiState.value
        assertEquals(1, state.dueReviews.size)
        assertEquals("reliable", state.dueReviews.single().title)
        assertEquals(1, state.activeMistakes.size)
        assertEquals("Preposition Trap", state.activeMistakes.single().title)

        // Complete a Fix-It drill and verify LearningEvidence is recorded
        vm.startFixItFor(1L)
        vm.revealFixIt()
        vm.submitFixItOutcome(understood = true)
        advanceUntilIdle()

        assertEquals(1, userRepo.recordedEvidence.size)
        assertEquals("mistake_bank_fix_it", userRepo.recordedEvidence.single().activityType)
        assertTrue(userRepo.recordedEvidence.single().isCorrect)
    }
}

private class ReviewFakeUserRepo : UserLearningRepository {
    val recordedEvidence = mutableListOf<LearningEvidence>()
    private val mistakesFlow = MutableStateFlow(
        listOf(
            MistakeRecord(
                id = 1L,
                contentId = "grammar.preposition-trap",
                trapType = "l1_preposition",
                errorDescription = "Avoid 'about' after 'discuss'.",
                userAnswer = "We discussed about the roadmap.",
                correctAnswer = "We discussed the roadmap.",
                stage = MistakeStage.RECURRING,
                occurrenceCount = 3,
                firstObservedAt = 100L,
                lastObservedAt = 200L,
            ),
        ),
    )
    private val dueFlow = MutableStateFlow(
        listOf(ReviewSchedule("vocab.reliable", "vocabulary", 100L, 1f)),
    )

    override suspend fun recordEvidence(evidence: LearningEvidence): Long {
        recordedEvidence += evidence
        return recordedEvidence.size.toLong()
    }
    override fun getEvidenceForContent(contentId: String) = flowOf(emptyList<LearningEvidence>())
    override fun getRecentEvidence(limit: Int) = flowOf(recordedEvidence.take(limit))
    override fun getMasterySnapshot(contentId: String) = flowOf<MasterySnapshot?>(null)
    override suspend fun getMasterySnapshotSync(contentId: String): MasterySnapshot? = null
    override fun getAllMasterySnapshots() = flowOf(emptyList<MasterySnapshot>())
    override suspend fun saveMasterySnapshot(snapshot: MasterySnapshot) {}
    override fun getActiveMistakes() = flowOf(mistakesFlow.value.filter { it.stage != MistakeStage.RESOLVED })
    override fun getAllMistakes() = mistakesFlow
    override suspend fun findMistake(contentId: String, trapType: String) =
        mistakesFlow.value.firstOrNull { it.contentId == contentId && it.trapType == trapType }
    override suspend fun saveMistake(mistake: MistakeRecord): Long {
        mistakesFlow.value = mistakesFlow.value.map { if (it.id == mistake.id) mistake else it }
        return mistake.id
    }
    override fun getDueReviews(currentTimestamp: Long) = dueFlow
    override fun getReviewSchedule(contentId: String) = flowOf<ReviewSchedule?>(null)
    override suspend fun getReviewScheduleSync(contentId: String): ReviewSchedule? = null
    override suspend fun saveReviewSchedule(schedule: ReviewSchedule) {}
    override fun getUserProfile(id: String) = flowOf<UserProfile?>(null)
    override suspend fun getUserProfileSync(id: String): UserProfile? = null
    override suspend fun saveUserProfile(profile: UserProfile) {}
}

private class ReviewFakeVocabRepo : VocabularyRepository {
    private val items = listOf(
        VocabItem("vocab.reliable", "reliable", "B1", "adjective", "/rɪˈlaɪəbəl/", definitionEn = "Consistently good in quality.", meaningTr = "güvenilir"),
    )
    override fun getAllVocab() = flowOf(items)
    override fun getVocabByLevel(level: String) = flowOf(items)
    override fun getVocabById(id: String) = flowOf(items.firstOrNull { it.id == id })
    override fun searchVocab(query: String) = flowOf(items)
    override suspend fun getVocabCount() = items.size
    override suspend fun getAllVocabSnapshot() = items
    override suspend fun getCollectionSummaries(level: String?) = emptyList<VocabularyCollection>()
    override suspend fun getCollectionWords(topicId: String, level: String?) = emptyList<VocabItem>()
}

private class ReviewFakeGrammarRepo : GrammarRepository {
    override fun getAllLessons() = flowOf(emptyList<GrammarLesson>())
    override fun getLessonsByLevel(level: String) = flowOf(emptyList<GrammarLesson>())
    override fun getLessonsByCategory(category: String) = flowOf(emptyList<GrammarLesson>())
    override fun getLessonById(id: String) = flowOf<GrammarLesson?>(null)
    override fun searchLessons(query: String) = flowOf(emptyList<GrammarLesson>())
    override fun getExercisesForLesson(lessonId: String) = flowOf(emptyList<com.seanora.fluentai.core.model.Exercise>())
    override suspend fun getLessonCount() = 0
}

private class ReviewFakeReadingRepo : ReadingRepository {
    override fun getAllArticles() = flowOf(emptyList<ReadingArticle>())
    override fun getArticlesByLevel(level: String) = flowOf(emptyList<ReadingArticle>())
    override fun getArticleById(id: String) = flowOf<ReadingArticle?>(null)
    override fun getArticlesByCategory(category: String) = flowOf(emptyList<ReadingArticle>())
    override fun searchArticles(query: String) = flowOf(emptyList<ReadingArticle>())
    override suspend fun getArticleCount() = 0
}

private class ReviewFakeListeningRepo : ListeningRepository {
    override fun getAllScenarios() = flowOf(emptyList<ListeningScenario>())
    override fun getScenariosByLevel(level: String) = flowOf(emptyList<ListeningScenario>())
    override fun getScenarioById(id: String) = flowOf<ListeningScenario?>(null)
    override fun getScenariosByCategory(category: String) = flowOf(emptyList<ListeningScenario>())
    override fun searchScenarios(query: String) = flowOf(emptyList<ListeningScenario>())
    override suspend fun getScenarioCount() = 0
}
