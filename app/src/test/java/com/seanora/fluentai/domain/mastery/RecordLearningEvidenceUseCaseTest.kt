package com.seanora.fluentai.domain.mastery

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.MistakeStage
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.domain.mistakes.MistakeEngine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RecordLearningEvidenceUseCaseTest {

    private val evidenceStore = mutableListOf<LearningEvidence>()
    private val masteryStore = mutableMapOf<String, MasterySnapshot>()
    private val mistakeStore = mutableMapOf<String, MistakeRecord>()
    private val reviewStore = mutableMapOf<String, ReviewSchedule>()

    private val fakeUserRepository = object : UserLearningRepository {
        override suspend fun recordEvidence(evidence: LearningEvidence): Long {
            evidenceStore.add(evidence)
            return evidenceStore.size.toLong()
        }

        override fun getEvidenceForContent(contentId: String): Flow<List<LearningEvidence>> =
            flowOf(evidenceStore.filter { it.contentId == contentId })

        override fun getRecentEvidence(limit: Int): Flow<List<LearningEvidence>> =
            flowOf(evidenceStore.takeLast(limit))

        override fun getMasterySnapshot(contentId: String): Flow<MasterySnapshot?> =
            flowOf(masteryStore[contentId])

        override suspend fun getMasterySnapshotSync(contentId: String): MasterySnapshot? =
            masteryStore[contentId]

        override fun getAllMasterySnapshots(): Flow<List<MasterySnapshot>> =
            flowOf(masteryStore.values.toList())

        override suspend fun saveMasterySnapshot(snapshot: MasterySnapshot) {
            masteryStore[snapshot.contentId] = snapshot
        }

        override fun getActiveMistakes(): Flow<List<MistakeRecord>> =
            flowOf(mistakeStore.values.filter { it.stage != MistakeStage.RESOLVED })

        override fun getAllMistakes(): Flow<List<MistakeRecord>> =
            flowOf(mistakeStore.values.toList())

        override suspend fun findMistake(contentId: String, trapType: String): MistakeRecord? =
            mistakeStore["$contentId:$trapType"]

        override suspend fun saveMistake(mistake: MistakeRecord): Long {
            val key = "${mistake.contentId}:${mistake.trapType}"
            mistakeStore[key] = mistake
            return 1L
        }

        override fun getDueReviews(currentTimestamp: Long): Flow<List<ReviewSchedule>> =
            flowOf(reviewStore.values.filter { it.nextReviewDueTimestamp <= currentTimestamp })

        override fun getReviewSchedule(contentId: String): Flow<ReviewSchedule?> =
            flowOf(reviewStore[contentId])

        override suspend fun getReviewScheduleSync(contentId: String): ReviewSchedule? =
            reviewStore[contentId]

        override suspend fun saveReviewSchedule(schedule: ReviewSchedule) {
            reviewStore[schedule.contentId] = schedule
        }

        override fun getUserProfile(id: String): Flow<UserProfile?> = flowOf(UserProfile())
        override suspend fun getUserProfileSync(id: String): UserProfile? = UserProfile()
        override suspend fun saveUserProfile(profile: UserProfile) {}
    }

    private lateinit var useCase: RecordLearningEvidenceUseCase

    @Before
    fun setUp() {
        evidenceStore.clear()
        masteryStore.clear()
        mistakeStore.clear()
        reviewStore.clear()

        useCase = RecordLearningEvidenceUseCase(
            userLearningRepository = fakeUserRepository,
            masteryEngine = MasteryEngine(),
            mistakeEngine = MistakeEngine()
        )
    }

    @Test
    fun recordEvidence_persistsEvidenceAndComputesMasterySnapshot() = runTest {
        val evidence = LearningEvidence(
            contentId = "vocab.consensus",
            domain = "vocabulary",
            activityType = "flashcard",
            isCorrect = true,
            score = 1.0f
        )

        val result = useCase(evidence)

        assertEquals("vocab.consensus", result.contentId)
        assertEquals(1, result.totalAttempts)
        assertEquals(1, result.correctAttempts)
        assertTrue(result.level > 0.0f)

        // Verify stored in repository
        assertEquals(1, evidenceStore.size)
        assertNotNull(masteryStore["vocab.consensus"])
        assertNotNull(reviewStore["vocab.consensus"])
    }

    @Test
    fun recordEvidence_withMistakeTrap_recordsAndProgressesMistakeLifecycle() = runTest {
        val errorEvidence = LearningEvidence(
            contentId = "grammar.present-perfect-vs-past-simple",
            domain = "grammar",
            activityType = "exercise",
            isCorrect = false,
            score = 0.0f
        )

        // 1st error -> OBSERVED
        useCase(
            evidence = errorEvidence,
            trapType = "aspect_confusion",
            errorDescription = "Used present perfect with yesterday",
            userAnswer = "have submitted yesterday",
            correctAnswer = "submitted yesterday"
        )

        val mistake1 = mistakeStore["grammar.present-perfect-vs-past-simple:aspect_confusion"]
        assertNotNull(mistake1)
        assertEquals(MistakeStage.OBSERVED, mistake1?.stage)
        assertEquals(1, mistake1?.occurrenceCount)

        // 2nd error -> POSSIBLE
        useCase(
            evidence = errorEvidence.copy(timestamp = System.currentTimeMillis() + 1000),
            trapType = "aspect_confusion",
            errorDescription = "Used present perfect with yesterday again",
            userAnswer = "have submitted yesterday",
            correctAnswer = "submitted yesterday"
        )

        val mistake2 = mistakeStore["grammar.present-perfect-vs-past-simple:aspect_confusion"]
        assertEquals(MistakeStage.POSSIBLE, mistake2?.stage)
        assertEquals(2, mistake2?.occurrenceCount)
    }
}
