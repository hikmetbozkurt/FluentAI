package com.seanora.fluentai

import com.seanora.fluentai.core.model.AssessmentDomain
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.LearningInterest
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.OnboardingSurveyResult
import com.seanora.fluentai.core.model.PlacementResponse
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.SelfAssessedLevel
import com.seanora.fluentai.core.model.UserGoal
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.data.repository.AssessmentRepositoryImpl
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.domain.assessment.PlacementEngine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class Phase4IntegrityTest {

    private var persistedProfile: UserProfile? = null

    private val userRepo = object : UserLearningRepository {
        override suspend fun recordEvidence(evidence: LearningEvidence): Long = 1L
        override fun getEvidenceForContent(contentId: String): Flow<List<LearningEvidence>> = flowOf(emptyList())
        override fun getRecentEvidence(limit: Int): Flow<List<LearningEvidence>> = flowOf(emptyList())
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
        override fun getUserProfile(id: String): Flow<UserProfile?> = flowOf(persistedProfile)
        override suspend fun getUserProfileSync(id: String): UserProfile? = persistedProfile
        override suspend fun saveUserProfile(profile: UserProfile) {
            persistedProfile = profile
        }
    }

    private lateinit var placementEngine: PlacementEngine
    private lateinit var assessmentRepo: AssessmentRepositoryImpl

    @Before
    fun setUp() {
        persistedProfile = null
        placementEngine = PlacementEngine()
        assessmentRepo = AssessmentRepositoryImpl()
    }

    @Test
    fun exitCriteria_initialProfileCreatedAndPersistedWithSkillSpecificCefrEstimates() = runTest {
        // 1. Onboarding Survey
        val survey = OnboardingSurveyResult(
            goal = UserGoal.CAREER_ADVANCEMENT,
            interests = listOf(LearningInterest.TECH_INNOVATION, LearningInterest.BUSINESS_LEADERSHIP),
            selfAssessedLevel = SelfAssessedLevel.B2_UPPER_INTERMEDIATE,
            targetCefrLevel = "C1",
            dailyPracticeMinutes = 20
        )

        // 2. Adaptive Placement Probing simulation across all 4 domains
        val responses = mutableListOf<PlacementResponse>()

        // Vocabulary: answers B2 and C1 correctly -> evaluates to C1
        responses.add(PlacementResponse("pv1", AssessmentDomain.VOCABULARY, "B2", 0, true))
        responses.add(PlacementResponse("pv2", AssessmentDomain.VOCABULARY, "C1", 0, true))
        responses.add(PlacementResponse("pv3", AssessmentDomain.VOCABULARY, "C2", 1, false))

        // Grammar: answers B1 correctly, fails B2 -> evaluates to B1
        responses.add(PlacementResponse("pg1", AssessmentDomain.GRAMMAR, "B1", 0, true))
        responses.add(PlacementResponse("pg2", AssessmentDomain.GRAMMAR, "B2", 1, false))
        responses.add(PlacementResponse("pg3", AssessmentDomain.GRAMMAR, "B1", 0, true))

        // Reading: answers B1 and B2 correctly -> evaluates to B2
        responses.add(PlacementResponse("pr1", AssessmentDomain.READING, "B1", 0, true))
        responses.add(PlacementResponse("pr2", AssessmentDomain.READING, "B2", 0, true))
        responses.add(PlacementResponse("pr3", AssessmentDomain.READING, "C1", 2, false))

        // Listening: answers B1 and B2 correctly -> evaluates to B2
        responses.add(PlacementResponse("pl1", AssessmentDomain.LISTENING, "B1", 0, true))
        responses.add(PlacementResponse("pl2", AssessmentDomain.LISTENING, "B2", 0, true))
        responses.add(PlacementResponse("pl3", AssessmentDomain.LISTENING, "C1", 1, false))

        // 3. Evaluate Diagnostic Assessment Summary
        val summary = placementEngine.evaluateAssessmentSummary(responses)
        assertEquals(4, summary.skillPlacements.size)

        // Verify independent skill evaluations
        val vocabResult = summary.skillPlacements.find { it.domain == AssessmentDomain.VOCABULARY }
        assertNotNull(vocabResult)
        assertEquals("C1", vocabResult!!.estimatedLevel)
        assertTrue(vocabResult.confidence in 0.5f..0.95f)

        val grammarResult = summary.skillPlacements.find { it.domain == AssessmentDomain.GRAMMAR }
        assertNotNull(grammarResult)
        assertEquals("B1", grammarResult!!.estimatedLevel)

        val readingResult = summary.skillPlacements.find { it.domain == AssessmentDomain.READING }
        assertNotNull(readingResult)
        assertEquals("B2", readingResult!!.estimatedLevel)

        val listeningResult = summary.skillPlacements.find { it.domain == AssessmentDomain.LISTENING }
        assertNotNull(listeningResult)
        assertEquals("B2", listeningResult!!.estimatedLevel)

        // 4. Create and Persist UserProfile
        val profile = placementEngine.createUserProfile(survey, summary)
        userRepo.saveUserProfile(profile)

        // 5. Verify persisted profile in repository
        val loadedProfile = userRepo.getUserProfileSync()
        assertNotNull(loadedProfile)
        assertEquals("C1", loadedProfile!!.targetCefrLevel)
        assertEquals(20, loadedProfile.dailyGoalMinutes)
        assertEquals("C1", loadedProfile.estimatedVocabLevel)
        assertEquals("B1", loadedProfile.estimatedGrammarLevel)
        assertTrue(loadedProfile.learningInterests.contains("technology"))
        assertTrue(loadedProfile.learningInterests.contains("leadership"))
    }

    @Test
    fun exitCriteria_goldenDiagnosticProbesAvailableOffline() = runTest {
        val probes = assessmentRepo.getPlacementProbesSync()
        assertTrue("Expected at least 16 diagnostic probes", probes.size >= 16)

        val domains = probes.map { it.domain.lowercase() }.distinct()
        assertTrue(domains.contains("vocabulary"))
        assertTrue(domains.contains("grammar"))
        assertTrue(domains.contains("reading"))
        assertTrue(domains.contains("listening"))

        val levels = probes.map { it.cefrLevel.uppercase() }.distinct()
        assertTrue(levels.contains("A2"))
        assertTrue(levels.contains("B1"))
        assertTrue(levels.contains("B2"))
        assertTrue(levels.contains("C1"))
    }
}
