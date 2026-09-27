package com.seanora.fluentai.domain.speaking

import com.seanora.fluentai.core.model.CorrectionMode
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.MistakeStage
import com.seanora.fluentai.core.model.SpeakingCategory
import com.seanora.fluentai.core.model.SpeakingScenario
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.ai.live.SpeakingPersona
import com.seanora.fluentai.ai.live.SpeakingSessionType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SpeakingPromptBuilderTest {

    private val builder = SpeakingPromptBuilder()

    private val testScenario = SpeakingScenario(
        id = "speaking.b2.meeting.standup",
        title = "Agile Standup & Blockers",
        category = SpeakingCategory.MEETING,
        cefrLevel = "B2",
        systemPrompt = "Act as Scrum Master running daily standup.",
        contextDescription = "Giving status and discussing architecture blockers.",
        targetVocabIds = listOf("vocab.bottleneck", "vocab.escalate"),
        targetGrammarIds = listOf("grammar.present-perfect-vs-past-simple"),
        suggestedStarterPhrases = listOf("Yesterday I deployed the auth service."),
        recommendedCorrectionMode = CorrectionMode.COACH,
        voiceName = "Puck"
    )

    @Test
    fun correctionModes_remainExactlyTheExistingFour() {
        assertEquals(
            listOf(CorrectionMode.FLOW, CorrectionMode.COACH, CorrectionMode.DRILL, CorrectionMode.MOCK),
            CorrectionMode.entries,
        )
    }

    @Test
    fun flowMode_containsFlowDirectives() {
        val prompt = builder.buildSystemPrompt(testScenario, CorrectionMode.FLOW)
        assertTrue(prompt.contains("Active Correction Mode: Flow"))
        assertTrue(prompt.contains("Do NOT interrupt the learner for minor grammatical"))
        assertTrue(prompt.contains("Turkish Learner Layer Awareness"))
    }

    @Test
    fun coachMode_containsCoachingDirectives() {
        val prompt = builder.buildSystemPrompt(testScenario, CorrectionMode.COACH)
        assertTrue(prompt.contains("Active Correction Mode: Coach"))
        assertTrue(prompt.contains("Act as a supportive executive language coach"))
    }

    @Test
    fun drillMode_containsTargetVocabAndGrammar() {
        val prompt = builder.buildSystemPrompt(testScenario, CorrectionMode.DRILL)
        assertTrue(prompt.contains("Active Correction Mode: Drill"))
        assertTrue(prompt.contains("vocab.bottleneck"))
        assertTrue(prompt.contains("grammar.present-perfect-vs-past-simple"))
        assertTrue(prompt.contains("Actively prompt the learner to use the targeted vocabulary"))
    }

    @Test
    fun mockMode_setsFormalExaminerRole() {
        val mockScenario = testScenario.copy(
            category = SpeakingCategory.JOB_INTERVIEW,
            recommendedCorrectionMode = CorrectionMode.MOCK
        )
        val prompt = builder.buildSystemPrompt(mockScenario, CorrectionMode.MOCK)
        assertTrue(prompt.contains("Active Correction Mode: Mock"))
        assertTrue(prompt.contains("Do NOT break character"))
        assertTrue(prompt.contains("Reserve detailed pedagogical feedback for post-session analysis"))
    }

    @Test
    fun recentMistakes_areInjectedIntoPrompt() {
        val mistakes = listOf(
            MistakeRecord(
                id = 1L,
                contentId = "grammar.present-perfect",
                trapType = "tense_aspect",
                errorDescription = "Used since instead of for with 2 years",
                userAnswer = "since 2 years",
                correctAnswer = "for 2 years",
                stage = MistakeStage.RECURRING,
                occurrenceCount = 3,
                firstObservedAt = 1000L,
                lastObservedAt = 2000L
            )
        )
        val prompt = builder.buildSystemPrompt(testScenario, CorrectionMode.COACH, recentMistakes = mistakes)
        assertTrue(prompt.contains("Learner's recent recurring traps to watch"))
        assertTrue(prompt.contains("tense_aspect: Used since instead of for with 2 years"))
    }

    @Test
    fun userProfile_isIncludedInPrompt() {
        val profile = UserProfile(
            targetCefrLevel = "C1",
            estimatedOverallLevel = "B2",
            learningInterests = listOf("cloud_computing", "leadership")
        )
        val prompt = builder.buildSystemPrompt(testScenario, CorrectionMode.COACH, profile = profile)
        assertTrue(prompt.contains("Target CEFR Level: C1"))
        assertTrue(prompt.contains("cloud_computing, leadership"))
    }

    @Test
    fun createLiveConfig_mapsParametersCorrectly() {
        val config = builder.createLiveConfig(testScenario, CorrectionMode.DRILL)
        assertEquals("speaking.b2.meeting.standup", config.scenarioId)
        assertEquals("Agile Standup & Blockers", config.title)
        assertEquals("Puck", config.voiceName)
        assertEquals("B2", config.cefrTarget)
        assertEquals(CorrectionMode.DRILL, config.correctionMode)
        assertEquals(SpeakingSessionType.SCENARIO, config.sessionType)
        assertEquals(null, config.persona)
        assertTrue(config.systemPrompt.contains("Active Correction Mode: Drill"))
    }

    @Test
    fun galadrielConfig_isFreeTalkPersonaWithoutCurriculumScenario() {
        val config = builder.createGaladrielLiveConfig(
            profile = UserProfile(
                displayName = "Deniz",
                estimatedSpeakingLevel = "B2",
                learningInterests = listOf("technology", "films"),
            ),
        )

        assertEquals(null, config.scenarioId)
        assertEquals("Galadriel", config.title)
        assertEquals(SpeakingSessionType.FREE_TALK, config.sessionType)
        assertEquals(SpeakingPersona.GALADRIEL, config.persona)
        assertEquals(CorrectionMode.FLOW, config.correctionMode)
        assertEquals(null, config.memoryContext)
        assertEquals("B2", config.cefrTarget)
    }

    @Test
    fun galadrielPrompt_containsPersonaLanguageSafetyAndFlowRules() {
        val prompt = builder.buildGaladrielPrompt()

        assertTrue(prompt.contains("Galadriel"))
        assertTrue(prompt.contains("30-year-old female logistics specialist"))
        assertTrue(prompt.contains("supply chain"))
        assertTrue(prompt.contains("domestic and international trade"))
        assertTrue(prompt.contains("import and export"))
        assertTrue(prompt.contains("painting, films, TV series, fantasy literature"))
        assertTrue(prompt.contains("Tolkien's published Legendarium"))
        assertTrue(prompt.contains("trusted English-speaking social friend"))
        assertTrue(prompt.contains("native English speaker"))
        assertTrue(prompt.contains("culturally familiar with Türkiye"))
        assertTrue(prompt.contains("English is the primary conversation language"))
        assertTrue(prompt.contains("brief Turkish rescue"))
        assertTrue(prompt.contains("immediately return to English"))
        assertTrue(prompt.contains("Never converse in any language other than English or Turkish"))
        assertTrue(prompt.contains("Do not fabricate real-world personal history"))
        assertTrue(prompt.contains("Ignore harmless small mistakes"))
        assertTrue(prompt.contains("meaningful or repeated mistakes"))
        assertTrue(prompt.contains("immediately continue the conversation"))
    }

    @Test
    fun prompt_keepsEnglishPrimaryAndTurkishAsLimitedSupport() {
        val prompt = builder.buildSystemPrompt(testScenario, CorrectionMode.COACH)

        assertTrue(prompt.contains("English is the primary and default coaching language"))
        assertTrue(prompt.contains("Turkish is the only support language"))
        assertTrue(prompt.contains("short Turkish clarification"))
        assertTrue(prompt.contains("immediately return to English"))
    }

    @Test
    fun prompt_redirectsThirdLanguagesAndRejectsGeneralAssistantBehavior() {
        val prompt = builder.buildSystemPrompt(testScenario, CorrectionMode.FLOW)

        assertTrue(prompt.contains("Do not continue conversationally in any third language"))
        assertTrue(prompt.contains("not a general-purpose voice assistant"))
        assertTrue(prompt.contains("redirect the learner back to English"))
    }

    @Test
    fun freeTalk_remainsFlexibleButEnglishLearningFocused() {
        val freeTalk = testScenario.copy(category = SpeakingCategory.FREE_TALK)

        val prompt = builder.buildSystemPrompt(freeTalk, CorrectionMode.FLOW)

        assertTrue(prompt.contains("Free Talk is flexible"))
        assertTrue(prompt.contains("English speaking practice"))
        assertTrue(prompt.contains("encourage longer learner responses"))
    }

    @Test
    fun structuredScenario_retainsDynamicContextAndGentleRedirection() {
        val prompt = builder.buildSystemPrompt(testScenario, CorrectionMode.COACH)

        assertTrue(prompt.contains("Agile Standup & Blockers"))
        assertTrue(prompt.contains("Giving status and discussing architecture blockers"))
        assertTrue(prompt.contains("gently steer back to this scenario"))
        assertTrue(prompt.contains("Active Correction Mode: Coach"))
    }
}
