package com.seanora.fluentai.domain.speaking

import com.seanora.fluentai.ai.live.SpeakingPersona
import com.seanora.fluentai.ai.live.SpeakingSessionType
import com.seanora.fluentai.core.model.CorrectionLevel
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.MistakeStage
import com.seanora.fluentai.core.model.SpeakingCategory
import com.seanora.fluentai.core.model.SpeakingScenario
import com.seanora.fluentai.core.model.UserProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
        voiceName = "Puck"
    )

    @Test
    fun correctionLevels_areLightBalancedAndThorough() {
        assertEquals(
            listOf(CorrectionLevel.LIGHT, CorrectionLevel.BALANCED, CorrectionLevel.THOROUGH),
            CorrectionLevel.entries
        )
    }

    @Test
    fun scenarioPrompt_alwaysUsesExplicitCoachingProtocol() {
        val prompt = builder.buildSystemPrompt(testScenario)

        assertTrue(prompt.contains("You are FluentAI Coach"))
        assertTrue(prompt.contains("Coaching protocol"))
        assertTrue(prompt.contains("Correct explicitly, never silently"))
        assertTrue(prompt.contains("Quick fix:"))
        assertTrue(prompt.contains("Correction level: Thorough"))
        assertTrue(prompt.contains("ask the learner to say the corrected sentence once"))
        assertTrue(prompt.contains("call the log_correction function"))
        assertFalse(prompt.contains("subtly rephrase", ignoreCase = true))
        assertFalse(prompt.contains("Do NOT interrupt", ignoreCase = true))
    }

    @Test
    fun scenarioPrompt_putsCoachRoleAbovePersona() {
        val prompt = builder.buildSystemPrompt(testScenario)

        assertTrue(prompt.contains("Scenario role: Act as Scrum Master running daily standup."))
        assertTrue(prompt.contains("you are always the learner's coach first"))
        assertTrue(prompt.contains("Step out of the role briefly to correct"))
    }

    @Test
    fun curriculumTargets_areRenderedAsReadableLabels() {
        val prompt = builder.buildSystemPrompt(testScenario)

        assertTrue(prompt.contains("Words to elicit and practise: bottleneck, escalate."))
        assertTrue(prompt.contains("Grammar to practise: present perfect vs past simple."))
        assertFalse(prompt.contains("vocab.bottleneck"))
    }

    @Test
    fun turkishTransferPatterns_areAlwaysIncluded() {
        val prompt = builder.buildSystemPrompt(testScenario)

        assertTrue(prompt.contains("Turkish transfer patterns to correct"))
        assertTrue(prompt.contains("since two years -> for two years"))
        assertTrue(prompt.contains("discuss about -> discuss"))
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

        val prompt = builder.buildSystemPrompt(testScenario, recentMistakes = mistakes)

        assertTrue(prompt.contains("This learner's recurring mistakes"))
        assertTrue(prompt.contains("tense_aspect: Used since instead of for with 2 years"))
    }

    @Test
    fun userProfile_isIncludedInPrompt() {
        val profile = UserProfile(
            targetCefrLevel = "C1",
            estimatedOverallLevel = "B2",
            learningInterests = listOf("cloud_computing", "leadership")
        )

        val prompt = builder.buildSystemPrompt(testScenario, profile = profile)

        assertTrue(prompt.contains("Target CEFR Level: C1"))
        assertTrue(prompt.contains("cloud_computing, leadership"))
    }

    @Test
    fun createLiveConfig_mapsParametersCorrectly() {
        val config = builder.createLiveConfig(testScenario)

        assertEquals("speaking.b2.meeting.standup", config.scenarioId)
        assertEquals("Agile Standup & Blockers", config.title)
        assertEquals("Puck", config.voiceName)
        assertEquals("B2", config.cefrTarget)
        assertEquals(CorrectionLevel.THOROUGH, config.correctionLevel)
        assertEquals(SpeakingSessionType.SCENARIO, config.sessionType)
        assertEquals(null, config.persona)
        assertTrue(config.coachingToolsEnabled)
    }

    @Test
    fun galadrielConfig_isFreeTalkPersonaWithBalancedDefault() {
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
        assertEquals(CorrectionLevel.BALANCED, config.correctionLevel)
        assertEquals(null, config.memoryContext)
        assertEquals("B2", config.cefrTarget)
        assertTrue(config.systemPrompt.contains("Deniz"))
    }

    @Test
    fun galadrielPrompt_keepsPersonaAndStillCorrects() {
        val prompt = builder.buildGaladrielPrompt()

        assertTrue(prompt.contains("Galadriel"))
        assertTrue(prompt.contains("30-year-old female logistics specialist"))
        assertTrue(prompt.contains("Tolkien's published Legendarium"))
        assertTrue(prompt.contains("culturally familiar with Türkiye"))
        assertTrue(prompt.contains("Do not fabricate real-world personal history"))
        assertTrue(prompt.contains("Correction level: Balanced"))
        assertTrue(prompt.contains("Correct explicitly, never silently"))
        assertFalse(prompt.contains("Ignore harmless small mistakes"))
    }

    @Test
    fun galadrielPrompt_followsSelectedCorrectionLevel() {
        val light = builder.buildGaladrielPrompt(correctionLevel = CorrectionLevel.LIGHT)
        val thorough = builder.buildGaladrielPrompt(correctionLevel = CorrectionLevel.THOROUGH)

        assertTrue(light.contains("Correction level: Light"))
        assertTrue(light.contains("Keep at least one correction or upgrade"))
        assertTrue(thorough.contains("Correction level: Thorough"))
        assertEquals(
            CorrectionLevel.LIGHT,
            builder.createGaladrielLiveConfig(correctionLevel = CorrectionLevel.LIGHT).correctionLevel
        )
    }

    @Test
    fun prompt_keepsEnglishPrimaryAndTurkishAsLimitedSupport() {
        val prompt = builder.buildSystemPrompt(testScenario)

        assertTrue(prompt.contains("English is the primary and default coaching language"))
        assertTrue(prompt.contains("Turkish is the only support language"))
        assertTrue(prompt.contains("short Turkish clarification"))
        assertTrue(prompt.contains("immediately return to English"))
    }

    @Test
    fun prompt_redirectsThirdLanguagesAndRejectsGeneralAssistantBehavior() {
        val prompt = builder.buildSystemPrompt(testScenario)

        assertTrue(prompt.contains("do not converse in any third language"))
        assertTrue(prompt.contains("not a general-purpose voice assistant"))
        assertTrue(prompt.contains("redirect the learner back to English"))
    }

    @Test
    fun freeTalkScenario_followsInterestsButKeepsPractice() {
        val prompt = builder.buildSystemPrompt(testScenario.copy(category = SpeakingCategory.FREE_TALK))

        assertTrue(prompt.contains("follow the learner's interests"))
        assertTrue(prompt.contains("Encourage longer answers"))
    }

    @Test
    fun structuredScenario_retainsDynamicContextAndGentleRedirection() {
        val prompt = builder.buildSystemPrompt(testScenario)

        assertTrue(prompt.contains("Agile Standup & Blockers"))
        assertTrue(prompt.contains("Giving status and discussing architecture blockers"))
        assertTrue(prompt.contains("gently steer back to this scenario"))
    }
}
