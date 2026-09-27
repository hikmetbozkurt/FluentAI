package com.seanora.fluentai.domain.assessment

import com.seanora.fluentai.core.model.AssessmentDomain
import com.seanora.fluentai.core.model.LearningInterest
import com.seanora.fluentai.core.model.OnboardingSurveyResult
import com.seanora.fluentai.core.model.PlacementProbe
import com.seanora.fluentai.core.model.PlacementResponse
import com.seanora.fluentai.core.model.SelfAssessedLevel
import com.seanora.fluentai.core.model.UserGoal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PlacementEngineTest {

    private lateinit var placementEngine: PlacementEngine

    @Before
    fun setUp() {
        placementEngine = PlacementEngine()
    }

    @Test
    fun getNextProbeLevel_probesAdaptively() {
        // Correct answers advance level
        assertEquals("B2", placementEngine.getNextProbeLevel("B1", true))
        assertEquals("C1", placementEngine.getNextProbeLevel("B2", true))
        assertEquals("C2", placementEngine.getNextProbeLevel("C1", true))
        assertEquals("C2", placementEngine.getNextProbeLevel("C2", true)) // Ceiling

        // Incorrect answers drop level
        assertEquals("B1", placementEngine.getNextProbeLevel("B2", false))
        assertEquals("A2", placementEngine.getNextProbeLevel("B1", false))
        assertEquals("A2", placementEngine.getNextProbeLevel("A2", false)) // Floor
    }

    @Test
    fun selectNextProbe_respectsLevelAndAvoidsDuplicates() {
        val probes = listOf(
            PlacementProbe(
                id = "p.vocab.b1.01",
                domain = "vocabulary",
                cefrLevel = "B1",
                promptEn = "Word test",
                options = listOf("A", "B"),
                correctOptionIndex = 0,
                explanationEn = "exp",
                turkishNote = "note"
            ),
            PlacementProbe(
                id = "p.vocab.b2.01",
                domain = "vocabulary",
                cefrLevel = "B2",
                promptEn = "Word test 2",
                options = listOf("A", "B"),
                correctOptionIndex = 1,
                explanationEn = "exp",
                turkishNote = "note"
            )
        )

        // Selects B2 probe
        val selected = placementEngine.selectNextProbe(
            domain = AssessmentDomain.VOCABULARY,
            targetLevel = "B2",
            availableProbes = probes,
            alreadyTestedProbeIds = emptySet()
        )
        assertNotNull(selected)
        assertEquals("p.vocab.b2.01", selected!!.id)

        // Avoids already tested probe
        val fallback = placementEngine.selectNextProbe(
            domain = AssessmentDomain.VOCABULARY,
            targetLevel = "B2",
            availableProbes = probes,
            alreadyTestedProbeIds = setOf("p.vocab.b2.01")
        )
        assertNotNull(fallback)
        assertEquals("p.vocab.b1.01", fallback!!.id)
    }

    @Test
    fun evaluateSkillPlacement_capsLevelIfFoundationProbeFails() {
        val responses = listOf(
            PlacementResponse("p1", AssessmentDomain.GRAMMAR, "B1", 0, true),
            PlacementResponse("p2", AssessmentDomain.GRAMMAR, "B2", 1, true),
            PlacementResponse("p3", AssessmentDomain.GRAMMAR, "C1", 0, true),
            PlacementResponse("p4", AssessmentDomain.GRAMMAR, "A2", 1, false) // Failed foundation check
        )

        val result = placementEngine.evaluateSkillPlacement(AssessmentDomain.GRAMMAR, responses)
        // Despite passing C1, failed fundamental A2 structure caps placement at B1 for remediation
        assertEquals("B1", result.estimatedLevel)
        assertTrue(result.confidence in 0.5f..0.95f)
    }

    @Test
    fun evaluateAssessmentSummary_and_createUserProfile_worksEndToEnd() {
        val responses = listOf(
            PlacementResponse("v1", AssessmentDomain.VOCABULARY, "B1", 0, true),
            PlacementResponse("v2", AssessmentDomain.VOCABULARY, "B2", 1, true),
            PlacementResponse("g1", AssessmentDomain.GRAMMAR, "B1", 0, true),
            PlacementResponse("g2", AssessmentDomain.GRAMMAR, "B2", 1, false),
            PlacementResponse("r1", AssessmentDomain.READING, "B1", 0, true),
            PlacementResponse("l1", AssessmentDomain.LISTENING, "B1", 0, true)
        )

        val summary = placementEngine.evaluateAssessmentSummary(responses)
        assertEquals(4, summary.skillPlacements.size)

        val survey = OnboardingSurveyResult(
            goal = UserGoal.GLOBAL_MEETINGS,
            interests = listOf(LearningInterest.TECH_INNOVATION, LearningInterest.BUSINESS_LEADERSHIP),
            selfAssessedLevel = SelfAssessedLevel.B2_UPPER_INTERMEDIATE,
            targetCefrLevel = "C1",
            dailyPracticeMinutes = 25
        )

        val profile = placementEngine.createUserProfile(survey, summary)
        assertEquals("C1", profile.targetCefrLevel)
        assertEquals(25, profile.dailyGoalMinutes)
        assertTrue(profile.learningInterests.contains("technology"))
        assertTrue(profile.learningInterests.contains("leadership"))
        assertEquals("B2", profile.estimatedVocabLevel)
        assertEquals("B1", profile.estimatedGrammarLevel)
        assertEquals("B1", profile.estimatedReadingLevel)
        assertEquals("B1", profile.estimatedListeningLevel)
    }
}
