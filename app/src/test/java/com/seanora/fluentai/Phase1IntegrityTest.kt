package com.seanora.fluentai

import com.seanora.fluentai.core.model.Exercise
import com.seanora.fluentai.core.model.GrammarLesson
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.MistakeStage
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.core.model.VocabItem
import com.seanora.fluentai.data.repository.GrammarRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.data.repository.VocabularyRepository
import com.seanora.fluentai.domain.mastery.MasteryEngine
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import com.seanora.fluentai.domain.mistakes.MistakeEngine
import com.seanora.fluentai.feature.grammar.GrammarViewModel
import com.seanora.fluentai.feature.vocabulary.VocabularyViewModel
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class Phase1IntegrityTest {

    private val testDispatcher = StandardTestDispatcher()

    // In-memory repositories for integration verification
    private val evidenceList = mutableListOf<LearningEvidence>()
    private val masteryMap = mutableMapOf<String, MasterySnapshot>()
    private val mistakeMap = mutableMapOf<String, MistakeRecord>()
    private val reviewMap = mutableMapOf<String, ReviewSchedule>()

    private val userRepo = object : UserLearningRepository {
        override suspend fun recordEvidence(evidence: LearningEvidence): Long {
            evidenceList.add(evidence)
            return evidenceList.size.toLong()
        }

        override fun getEvidenceForContent(contentId: String): Flow<List<LearningEvidence>> =
            flowOf(evidenceList.filter { it.contentId == contentId })

        override fun getRecentEvidence(limit: Int): Flow<List<LearningEvidence>> =
            flowOf(evidenceList.takeLast(limit))

        override fun getMasterySnapshot(contentId: String): Flow<MasterySnapshot?> =
            flowOf(masteryMap[contentId])

        override suspend fun getMasterySnapshotSync(contentId: String): MasterySnapshot? =
            masteryMap[contentId]

        override fun getAllMasterySnapshots(): Flow<List<MasterySnapshot>> =
            flowOf(masteryMap.values.toList())

        override suspend fun saveMasterySnapshot(snapshot: MasterySnapshot) {
            masteryMap[snapshot.contentId] = snapshot
        }

        override fun getActiveMistakes(): Flow<List<MistakeRecord>> =
            flowOf(mistakeMap.values.filter { it.stage != MistakeStage.RESOLVED })

        override fun getAllMistakes(): Flow<List<MistakeRecord>> =
            flowOf(mistakeMap.values.toList())

        override suspend fun findMistake(contentId: String, trapType: String): MistakeRecord? =
            mistakeMap["$contentId:$trapType"]

        override suspend fun saveMistake(mistake: MistakeRecord): Long {
            mistakeMap["${mistake.contentId}:${mistake.trapType}"] = mistake
            return 1L
        }

        override fun getDueReviews(currentTimestamp: Long): Flow<List<ReviewSchedule>> =
            flowOf(reviewMap.values.filter { it.nextReviewDueTimestamp <= currentTimestamp })

        override fun getReviewSchedule(contentId: String): Flow<ReviewSchedule?> =
            flowOf(reviewMap[contentId])

        override suspend fun getReviewScheduleSync(contentId: String): ReviewSchedule? =
            reviewMap[contentId]

        override suspend fun saveReviewSchedule(schedule: ReviewSchedule) {
            reviewMap[schedule.contentId] = schedule
        }

        override fun getUserProfile(id: String): Flow<UserProfile?> = flowOf(UserProfile())
        override suspend fun getUserProfileSync(id: String): UserProfile? = UserProfile()
        override suspend fun saveUserProfile(profile: UserProfile) {}
    }

    private val vocabRepo = object : VocabularyRepository {
        val items = listOf(
            VocabItem(
                id = "vocab.consensus",
                headword = "consensus",
                cefrLevel = "C2",
                partOfSpeech = "noun",
                phonetic = "/kənˈsen.səs/",
                definitionEn = "A general agreement among members of a group.",
                meaningTr = "fikir birliği, mutabakat"
            )
        )
        override fun getAllVocab(): Flow<List<VocabItem>> = flowOf(items)
        override fun getVocabByLevel(level: String): Flow<List<VocabItem>> = flowOf(items.filter { it.cefrLevel == level })
        override fun getVocabById(id: String): Flow<VocabItem?> = flowOf(items.firstOrNull { it.id == id })
        override fun searchVocab(query: String): Flow<List<VocabItem>> = flowOf(items)
        override suspend fun getVocabCount(): Int = items.size
    }

    private val grammarRepo = object : GrammarRepository {
        val lessons = listOf(
            GrammarLesson(
                id = "grammar.c1.inversion-negative-adverbials",
                title = "Inversion with Negative Adverbials",
                cefrLevel = "C1",
                category = "inversion_and_emphasis",
                summaryEn = "Invert after negative adverbials.",
                summaryTr = "Devrik cümle kuralı.",
                explanationTr = "Türkçe aktarım tuzağı açıklaması."
            )
        )
        override fun getAllLessons(): Flow<List<GrammarLesson>> = flowOf(lessons)
        override fun getLessonsByLevel(level: String): Flow<List<GrammarLesson>> = flowOf(lessons.filter { it.cefrLevel == level })
        override fun getLessonsByCategory(category: String): Flow<List<GrammarLesson>> = flowOf(lessons.filter { it.category == category })
        override fun getLessonById(id: String): Flow<GrammarLesson?> = flowOf(lessons.firstOrNull { it.id == id })
        override fun searchLessons(query: String): Flow<List<GrammarLesson>> = flowOf(lessons)
        override fun getExercisesForLesson(lessonId: String): Flow<List<Exercise>> = flowOf(emptyList())
        override suspend fun getLessonCount(): Int = lessons.size
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        evidenceList.clear()
        masteryMap.clear()
        mistakeMap.clear()
        reviewMap.clear()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun phase1Exit_vocabularyEnglishFirstWithOnDemandTurkishReveal() = runTest {
        val vm = VocabularyViewModel(vocabRepo)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            vm.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        // 1. Initial view: Turkish meaning must NOT be revealed (ADR-009)
        val initial = vm.uiState.value
        assertEquals("consensus", initial.selectedItem?.headword)
        assertFalse("Turkish meaning must be hidden initially to reinforce English immersion", initial.isTurkishRevealed)

        // 2. On user demand, reveal Turkish
        vm.toggleTurkishReveal()
        testScheduler.advanceUntilIdle()
        assertTrue(vm.uiState.value.isTurkishRevealed)
    }

    @Test
    fun phase1Exit_grammarEnglishFirstWithTurkishTraps() = runTest {
        val vm = GrammarViewModel(grammarRepo)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            vm.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        val initial = vm.uiState.value
        assertEquals("grammar.c1.inversion-negative-adverbials", initial.selectedLesson?.id)
        assertFalse(initial.isTurkishRevealed)

        vm.toggleTurkishReveal()
        testScheduler.advanceUntilIdle()
        assertTrue(vm.uiState.value.isTurkishRevealed)
    }

    @Test
    fun phase1Exit_deterministicLearningEvidenceToMasteryLoop() = runTest {
        val useCase = RecordLearningEvidenceUseCase(userRepo, MasteryEngine(), MistakeEngine())

        // Step 1: User practices vocabulary item and makes an error
        val errEvidence = LearningEvidence(
            contentId = "vocab.consensus",
            domain = "vocabulary",
            activityType = "flashcard",
            isCorrect = false,
            score = 0.0f
        )
        val snapshot1 = useCase(
            evidence = errEvidence,
            trapType = "false_friend",
            errorDescription = "Confused consensus with concession",
            userAnswer = "concession",
            correctAnswer = "consensus"
        )

        // Verify mistake created in OBSERVED stage
        val mistake1 = mistakeMap["vocab.consensus:false_friend"]
        assertNotNull(mistake1)
        assertEquals(MistakeStage.OBSERVED, mistake1?.stage)
        assertEquals(MasteryStatus.LEARNING, snapshot1.status)

        // Step 2: User repeats mistake
        useCase(
            evidence = errEvidence.copy(timestamp = System.currentTimeMillis() + 1000),
            trapType = "false_friend",
            errorDescription = "Confused consensus with concession",
            userAnswer = "concession",
            correctAnswer = "consensus"
        )
        assertEquals(MistakeStage.POSSIBLE, mistakeMap["vocab.consensus:false_friend"]?.stage)

        // Step 3: User resolves mistake through practice
        val successEvidence = LearningEvidence(
            contentId = "vocab.consensus",
            domain = "vocabulary",
            activityType = "exercise",
            isCorrect = true,
            score = 1.0f,
            timestamp = System.currentTimeMillis() + 2000
        )
        val snapshotSuccess = useCase(
            evidence = successEvidence,
            trapType = "false_friend"
        )

        // Mistake must advance toward resolution
        assertEquals(MistakeStage.RESOLVED, mistakeMap["vocab.consensus:false_friend"]?.stage)
        assertTrue(snapshotSuccess.level > snapshot1.level)
        assertNotNull(reviewMap["vocab.consensus"])
    }
}
