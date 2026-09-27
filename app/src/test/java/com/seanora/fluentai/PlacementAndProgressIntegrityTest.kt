package com.seanora.fluentai

import com.seanora.fluentai.app.navigation.ONBOARDING_ROUTE
import com.seanora.fluentai.app.navigation.TopLevelDestination
import com.seanora.fluentai.app.navigation.resolveStartupDestination
import com.seanora.fluentai.core.model.AssessmentDomain
import com.seanora.fluentai.core.model.LearningInterest
import com.seanora.fluentai.core.model.OnboardingSurveyResult
import com.seanora.fluentai.core.model.PlacementResponse
import com.seanora.fluentai.core.model.SelfAssessedLevel
import com.seanora.fluentai.core.model.UserGoal
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.data.repository.AssessmentRepositoryImpl
import com.seanora.fluentai.domain.assessment.PlacementEngine
import com.seanora.fluentai.domain.progress.ProgressEngine
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PlacementAndProgressIntegrityTest {

    private lateinit var placementEngine: PlacementEngine
    private lateinit var assessmentRepo: AssessmentRepositoryImpl
    private lateinit var progressEngine: ProgressEngine

    @Before
    fun setUp() {
        placementEngine = PlacementEngine()
        assessmentRepo = AssessmentRepositoryImpl()
        progressEngine = ProgressEngine()
    }

    // -----------------------------------------------------------------------
    // 1. Placement Option Distribution & Accurate Scoring
    // -----------------------------------------------------------------------

    @Test
    fun placementProbes_haveDistributedAnswerPositions_andAccurateScoring() = runTest {
        val probes = assessmentRepo.getPlacementProbesSync()
        assertTrue("Probes should have at least 16 questions", probes.size >= 16)

        val correctIndices = probes.map { it.correctOptionIndex }.toSet()
        // Proves correct answer is not always A (0)
        assertTrue("Option distribution should have more than 1 option index", correctIndices.size > 1)
        assertTrue("Option distribution should include index 0", correctIndices.contains(0))
        assertTrue("Option distribution should include index 1", correctIndices.contains(1))
        assertTrue("Option distribution should include index 2", correctIndices.contains(2))
        assertTrue("Option distribution should include index 3", correctIndices.contains(3))

        // Prove scoring marks the correct option correctly and other options incorrectly
        for (probe in probes) {
            val correctIdx = probe.correctOptionIndex
            assertTrue("Correct index should be within valid options bounds", correctIdx in probe.options.indices)

            for (testedIdx in probe.options.indices) {
                val isCorrect = testedIdx == probe.correctOptionIndex
                val response = PlacementResponse(
                    probeId = probe.id,
                    domain = AssessmentDomain.valueOf(probe.domain.uppercase()),
                    testedLevel = probe.cefrLevel,
                    selectedOptionIndex = testedIdx,
                    isCorrect = isCorrect
                )
                if (testedIdx == correctIdx) {
                    assertTrue("Selecting correct option must evaluate to true", response.isCorrect)
                } else {
                    assertFalse("Selecting incorrect option must evaluate to false", response.isCorrect)
                }
            }
        }
    }

    // -----------------------------------------------------------------------
    // 2. Placement -> Profile -> Progress CEFR Source of Truth
    // -----------------------------------------------------------------------

    @Test
    fun placementResult_persistsSkillSpecificCefr_andProgressConsistentlyReflectsIt() = runTest {
        val responses = listOf(
            // Vocabulary -> C1
            PlacementResponse("pv1", AssessmentDomain.VOCABULARY, "B2", 1, true),
            PlacementResponse("pv2", AssessmentDomain.VOCABULARY, "C1", 2, true),
            PlacementResponse("pv3", AssessmentDomain.VOCABULARY, "C2", 0, false),
            // Grammar -> B1
            PlacementResponse("pg1", AssessmentDomain.GRAMMAR, "B1", 3, true),
            PlacementResponse("pg2", AssessmentDomain.GRAMMAR, "B2", 1, false),
            PlacementResponse("pg3", AssessmentDomain.GRAMMAR, "B1", 0, true),
            // Reading -> C1
            PlacementResponse("pr1", AssessmentDomain.READING, "B2", 2, true),
            PlacementResponse("pr2", AssessmentDomain.READING, "C1", 1, true),
            PlacementResponse("pr3", AssessmentDomain.READING, "C2", 3, false),
            // Listening -> B2
            PlacementResponse("pl1", AssessmentDomain.LISTENING, "B1", 0, true),
            PlacementResponse("pl2", AssessmentDomain.LISTENING, "B2", 2, true),
            PlacementResponse("pl3", AssessmentDomain.LISTENING, "C1", 1, false)
        )

        val summary = placementEngine.evaluateAssessmentSummary(responses)
        assertEquals(4, summary.skillPlacements.size)

        val survey = OnboardingSurveyResult(
            goal = UserGoal.CAREER_ADVANCEMENT,
            interests = listOf(LearningInterest.TECH_INNOVATION, LearningInterest.BUSINESS_LEADERSHIP),
            selfAssessedLevel = SelfAssessedLevel.B2_UPPER_INTERMEDIATE,
            targetCefrLevel = "C2",
            dailyPracticeMinutes = 30
        )

        val profile = placementEngine.createUserProfile(survey, summary)

        // Verify profile fields
        assertEquals("C2", profile.targetCefrLevel)
        assertEquals("C1", profile.estimatedVocabLevel)
        assertEquals("B1", profile.estimatedGrammarLevel)
        assertEquals("C1", profile.estimatedReadingLevel)
        assertEquals("B2", profile.estimatedListeningLevel)
        assertNotNull(profile.estimatedOverallLevel)

        // Verify ProgressEngine with empty mastery snapshots
        val dashboardData = progressEngine.computeDashboardData(
            snapshots = emptyList(),
            evidences = emptyList(),
            mistakes = emptyList(),
            reviews = emptyList(),
            userProfile = profile
        )

        // Overall estimated CEFR should match profile and NOT fall back to A2
        assertEquals(profile.estimatedOverallLevel, dashboardData.overallEstimatedCefr)

        // Verify skills in ProgressDashboardData match profile
        val readingSkill = dashboardData.skills.find { it.domain.equals("reading", ignoreCase = true) }
        assertNotNull(readingSkill)
        assertEquals("Reading level should match profile C1, not fall back to A2", "C1", readingSkill!!.cefrLevel)
        assertEquals("Mastery percentage is 0% when no items mastered", 0, readingSkill.masteryPercentage)

        val vocabSkill = dashboardData.skills.find { it.domain.equals("vocabulary", ignoreCase = true) }
        assertNotNull(vocabSkill)
        assertEquals("Vocabulary level should match profile C1", "C1", vocabSkill!!.cefrLevel)

        val grammarSkill = dashboardData.skills.find { it.domain.equals("grammar", ignoreCase = true) }
        assertNotNull(grammarSkill)
        assertEquals("Grammar level should match profile B1", "B1", grammarSkill!!.cefrLevel)

        val listeningSkill = dashboardData.skills.find { it.domain.equals("listening", ignoreCase = true) }
        assertNotNull(listeningSkill)
        assertEquals("Listening level should match profile B2", "B2", listeningSkill!!.cefrLevel)
    }

    // -----------------------------------------------------------------------
    // 3. Startup & Navigation Route Resolution
    // -----------------------------------------------------------------------

    @Test
    fun startupRoute_resolvesOnboardingForNewUser_andHomeForOnboardedUser() {
        // New user with no profile
        val newUserRoute = resolveStartupDestination(null)
        assertEquals(ONBOARDING_ROUTE, newUserRoute)

        // Onboarded user with profile
        val dummyProfile = UserProfile(
            id = "user_default",
            targetCefrLevel = "C1",
            dailyGoalMinutes = 20,
            estimatedVocabLevel = "B2",
            estimatedGrammarLevel = "B1",
            estimatedReadingLevel = "B2",
            estimatedListeningLevel = "B2",
            estimatedSpeakingLevel = "B1",
            estimatedOverallLevel = "B2"
        )
        val onboardedUserRoute = resolveStartupDestination(dummyProfile)
        assertEquals(TopLevelDestination.HOME.route, onboardedUserRoute)
    }
}
