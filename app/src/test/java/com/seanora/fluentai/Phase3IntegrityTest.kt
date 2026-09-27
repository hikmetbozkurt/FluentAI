package com.seanora.fluentai

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.MistakeStage
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.domain.mastery.MasteryEngine
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import com.seanora.fluentai.domain.mistakes.MistakeEngine
import com.seanora.fluentai.domain.progress.ProgressEngine
import com.seanora.fluentai.domain.review.ReviewRating
import com.seanora.fluentai.domain.review.ReviewScheduler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

private const val MILLIS_PER_DAY = 86_400_000L

class Phase3IntegrityTest {

    private val evidenceList = mutableListOf<LearningEvidence>()
    private val masteryMap = mutableMapOf<String, MasterySnapshot>()
    private val mistakeList = mutableListOf<MistakeRecord>()
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
            flowOf(mistakeList.filter { it.stage != MistakeStage.RESOLVED })
        override fun getAllMistakes(): Flow<List<MistakeRecord>> =
            flowOf(mistakeList)
        override suspend fun findMistake(contentId: String, trapType: String): MistakeRecord? =
            mistakeList.find { it.contentId == contentId && it.trapType == trapType }
        override suspend fun saveMistake(mistake: MistakeRecord): Long {
            val idx = mistakeList.indexOfFirst {
                it.id == mistake.id || (it.contentId == mistake.contentId && it.trapType == mistake.trapType)
            }
            if (idx >= 0) mistakeList[idx] = mistake else mistakeList.add(mistake)
            return mistake.id
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

    private lateinit var reviewScheduler: ReviewScheduler
    private lateinit var mistakeEngine: MistakeEngine
    private lateinit var masteryEngine: MasteryEngine
    private lateinit var recordLearningEvidenceUseCase: RecordLearningEvidenceUseCase
    private lateinit var progressEngine: ProgressEngine

    @Before
    fun setUp() {
        evidenceList.clear()
        masteryMap.clear()
        mistakeList.clear()
        reviewMap.clear()

        reviewScheduler = ReviewScheduler()
        mistakeEngine = MistakeEngine()
        masteryEngine = MasteryEngine(reviewScheduler)
        recordLearningEvidenceUseCase = RecordLearningEvidenceUseCase(
            userLearningRepository = userRepo,
            masteryEngine = masteryEngine,
            mistakeEngine = mistakeEngine
        )
        progressEngine = ProgressEngine(mistakeEngine, reviewScheduler)
    }

    @Test
    fun exitCriteria_evidenceChangesMasteryDeterministically() = runTest {
        val now = System.currentTimeMillis()

        // 1. Initial attempt -> LEARNING
        val snapshot1 = recordLearningEvidenceUseCase(
            LearningEvidence(
                contentId = "vocab.articulate",
                domain = "vocabulary",
                activityType = "flashcard",
                isCorrect = true,
                score = 1.0f,
                timestamp = now
            )
        )
        assertEquals("vocab.articulate", snapshot1.contentId)
        assertEquals(1, snapshot1.totalAttempts)
        assertEquals(1, snapshot1.correctAttempts)
        assertEquals(MasteryStatus.LEARNING, snapshot1.status)

        // 2. Perform 3 more correct attempts -> PRACTICED
        for (i in 2..4) {
            recordLearningEvidenceUseCase(
                LearningEvidence(
                    contentId = "vocab.articulate",
                    domain = "vocabulary",
                    activityType = "exercise",
                    isCorrect = true,
                    score = 1.0f,
                    timestamp = now + (i * 1000)
                )
            )
        }
        val snapshotPracticed = userRepo.getMasterySnapshotSync("vocab.articulate")
        assertNotNull(snapshotPracticed)
        assertEquals(4, snapshotPracticed!!.totalAttempts)
        assertEquals(MasteryStatus.PRACTICED, snapshotPracticed.status)

        // 3. Perform 3 more correct attempts (total 7) -> MASTERED
        for (i in 5..7) {
            recordLearningEvidenceUseCase(
                LearningEvidence(
                    contentId = "vocab.articulate",
                    domain = "vocabulary",
                    activityType = "exercise",
                    isCorrect = true,
                    score = 1.0f,
                    timestamp = now + (i * 1000)
                )
            )
        }
        val snapshotMastered = userRepo.getMasterySnapshotSync("vocab.articulate")
        assertNotNull(snapshotMastered)
        assertEquals(7, snapshotMastered!!.totalAttempts)
        assertEquals(MasteryStatus.MASTERED, snapshotMastered.status)
        assertTrue(snapshotMastered.level >= 0.88f)
    }

    @Test
    fun exitCriteria_dueReviewSchedulingWorks() = runTest {
        val now = 10_000_000_000L

        // Record first correct evidence
        recordLearningEvidenceUseCase(
            LearningEvidence(
                contentId = "grammar.c1.inversion",
                domain = "grammar",
                activityType = "exercise",
                isCorrect = true,
                score = 1.0f,
                timestamp = now
            )
        )

        val sched1 = userRepo.getReviewScheduleSync("grammar.c1.inversion")
        assertNotNull(sched1)
        assertEquals(1, sched1!!.repetitionNumber)
        assertEquals(1.0f, sched1.intervalDays, 0.01f)
        assertEquals(now + MILLIS_PER_DAY, sched1.nextReviewDueTimestamp)

        // Second review on due date (score = 1.0f)
        recordLearningEvidenceUseCase(
            LearningEvidence(
                contentId = "grammar.c1.inversion",
                domain = "grammar",
                activityType = "srs_review",
                isCorrect = true,
                score = 1.0f,
                timestamp = sched1.nextReviewDueTimestamp
            )
        )

        val sched2 = userRepo.getReviewScheduleSync("grammar.c1.inversion")
        assertNotNull(sched2)
        assertEquals(2, sched2!!.repetitionNumber)
        assertEquals(3.0f, sched2.intervalDays, 0.01f)

        // Review rating AGAIN resets interval and decrements ease
        val resetSched = reviewScheduler.computeNextSchedule(
            previous = sched2,
            rating = ReviewRating.AGAIN,
            contentId = "grammar.c1.inversion",
            domain = "grammar",
            currentTimestamp = sched2.nextReviewDueTimestamp
        )
        assertEquals(0, resetSched.repetitionNumber)
        assertEquals(1.0f, resetSched.intervalDays, 0.01f)
        assertTrue(resetSched.easeFactor < sched2.easeFactor)
    }

    @Test
    fun exitCriteria_repeatedErrorsBecomeRecurringMistakesAndResolveViaDrills() = runTest {
        val now = System.currentTimeMillis()
        val contentId = "vocab.preposition.interested"
        val trapType = "preposition_trap"

        // Error 1 -> OBSERVED (not immediately labeled as recurring weakness)
        recordLearningEvidenceUseCase(
            evidence = LearningEvidence(
                contentId = contentId,
                domain = "vocabulary",
                activityType = "exercise",
                isCorrect = false,
                score = 0.0f,
                timestamp = now
            ),
            trapType = trapType,
            errorDescription = "interested with instead of interested in",
            userAnswer = "interested with",
            correctAnswer = "interested in"
        )
        val m1 = userRepo.findMistake(contentId, trapType)
        assertNotNull(m1)
        assertEquals(MistakeStage.OBSERVED, m1!!.stage)
        assertEquals(1, m1.occurrenceCount)

        // Error 2 -> POSSIBLE
        recordLearningEvidenceUseCase(
            evidence = LearningEvidence(
                contentId = contentId,
                domain = "vocabulary",
                activityType = "exercise",
                isCorrect = false,
                score = 0.0f,
                timestamp = now + 1000
            ),
            trapType = trapType,
            errorDescription = "interested with again",
            userAnswer = "interested with",
            correctAnswer = "interested in"
        )
        val m2 = userRepo.findMistake(contentId, trapType)
        assertNotNull(m2)
        assertEquals(MistakeStage.POSSIBLE, m2!!.stage)
        assertEquals(2, m2.occurrenceCount)

        // Error 3 -> RECURRING (now recognized as an active systematic trap)
        recordLearningEvidenceUseCase(
            evidence = LearningEvidence(
                contentId = contentId,
                domain = "vocabulary",
                activityType = "exercise",
                isCorrect = false,
                score = 0.0f,
                timestamp = now + 2000
            ),
            trapType = trapType,
            errorDescription = "interested with repeated",
            userAnswer = "interested with",
            correctAnswer = "interested in"
        )
        val m3 = userRepo.findMistake(contentId, trapType)
        assertNotNull(m3)
        assertEquals(MistakeStage.RECURRING, m3!!.stage)
        assertEquals(3, m3.occurrenceCount)

        // Step 1 of remediation -> TARGETED
        recordLearningEvidenceUseCase(
            evidence = LearningEvidence(
                contentId = contentId,
                domain = "vocabulary",
                activityType = "targeted_drill",
                isCorrect = true,
                score = 1.0f,
                timestamp = now + 3000
            ),
            trapType = trapType
        )
        val mTargeted = userRepo.findMistake(contentId, trapType)
        assertNotNull(mTargeted)
        assertEquals(MistakeStage.TARGETED, mTargeted!!.stage)

        // Step 2 of remediation -> MONITORING
        recordLearningEvidenceUseCase(
            evidence = LearningEvidence(
                contentId = contentId,
                domain = "vocabulary",
                activityType = "targeted_drill",
                isCorrect = true,
                score = 1.0f,
                timestamp = now + 4000
            ),
            trapType = trapType
        )
        val mMonitoring = userRepo.findMistake(contentId, trapType)
        assertNotNull(mMonitoring)
        assertEquals(MistakeStage.MONITORING, mMonitoring!!.stage)

        // Step 3 of remediation -> RESOLVED
        recordLearningEvidenceUseCase(
            evidence = LearningEvidence(
                contentId = contentId,
                domain = "vocabulary",
                activityType = "targeted_drill",
                isCorrect = true,
                score = 1.0f,
                timestamp = now + 5000
            ),
            trapType = trapType
        )
        val mResolved = userRepo.findMistake(contentId, trapType)
        assertNotNull(mResolved)
        assertEquals(MistakeStage.RESOLVED, mResolved!!.stage)
    }

    @Test
    fun exitCriteria_progressReflectsRealStoredActivityWithoutAI() = runTest {
        val now = System.currentTimeMillis()

        // Populate 15 mastered vocabulary items
        for (i in 1..16) {
            val cid = "vocab.item.$i"
            masteryMap[cid] = MasterySnapshot(
                contentId = cid,
                domain = "vocabulary",
                level = 0.85f,
                confidence = 0.80f,
                totalAttempts = 6,
                correctAttempts = 6,
                lastAttemptTimestamp = now,
                lastDecayTimestamp = now,
                status = MasteryStatus.MASTERED
            )
            evidenceList.add(
                LearningEvidence(
                    contentId = cid,
                    domain = "vocabulary",
                    activityType = "exercise",
                    isCorrect = true,
                    score = 1.0f,
                    timestamp = now
                )
            )
        }

        // Only 1 grammar item attempted (A2 level)
        val grammarCid = "grammar.present-simple"
        masteryMap[grammarCid] = MasterySnapshot(
            contentId = grammarCid,
            domain = "grammar",
            level = 0.40f,
            confidence = 0.30f,
            totalAttempts = 2,
            correctAttempts = 1,
            lastAttemptTimestamp = now,
            lastDecayTimestamp = now,
            status = MasteryStatus.LEARNING
        )
        evidenceList.add(
            LearningEvidence(
                contentId = grammarCid,
                domain = "grammar",
                activityType = "exercise",
                isCorrect = true,
                score = 0.5f,
                timestamp = now
            )
        )

        val dashboard = progressEngine.computeDashboardData(
            snapshots = masteryMap.values.toList(),
            evidences = evidenceList,
            mistakes = mistakeList,
            reviews = reviewMap.values.toList(),
            currentTimestamp = now
        )

        // 1. Vocabulary reflects B2 level due to 16 mastered items with 85% mastery
        val vocabProficiency = dashboard.skills.find { it.domain == "vocabulary" }
        assertNotNull(vocabProficiency)
        assertEquals("B2", vocabProficiency!!.cefrLevel)
        assertEquals(16, vocabProficiency.itemsMastered)
        assertEquals(85, vocabProficiency.masteryPercentage)

        // 2. Grammar is strictly separated and evaluates to A2 (no false inflation from Vocab)
        val grammarProficiency = dashboard.skills.find { it.domain == "grammar" }
        assertNotNull(grammarProficiency)
        assertEquals("A2", grammarProficiency!!.cefrLevel)
        assertEquals(0, grammarProficiency.itemsMastered)
        assertEquals(40, grammarProficiency.masteryPercentage)

        // 3. Activity streak is active today
        assertEquals(1, dashboard.streak.currentStreakDays)
        assertEquals(17, dashboard.streak.totalPracticedItems)

        // 4. Mistake bank health is clean (100) since no active mistakes exist
        assertEquals(100, dashboard.mistakeHealthScore)
    }
}
