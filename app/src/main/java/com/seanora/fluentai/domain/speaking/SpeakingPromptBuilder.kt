package com.seanora.fluentai.domain.speaking

import com.seanora.fluentai.ai.live.LiveScenarioConfig
import com.seanora.fluentai.ai.live.SpeakingPersona
import com.seanora.fluentai.ai.live.SpeakingSessionType
import com.seanora.fluentai.core.model.CorrectionLevel
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.SpeakingCategory
import com.seanora.fluentai.core.model.SpeakingScenario
import com.seanora.fluentai.core.model.UserProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpeakingPromptBuilder @Inject constructor() {

    companion object {
        const val GALADRIEL_ANALYSIS_CONTENT_ID = "speaking.free-talk.galadriel"
        val SCENARIO_CORRECTION_LEVEL = CorrectionLevel.THOROUGH
        val DEFAULT_FREE_TALK_CORRECTION_LEVEL = CorrectionLevel.BALANCED
        private const val GALADRIEL_VOICE = "Aoede"
        private const val RECENT_MISTAKE_LIMIT = 3

        private val TURKISH_TRANSFER_PATTERNS = listOf(
            "'since' instead of 'for' with durations (since two years -> for two years)",
            "Present Perfect with a finished past time (I have seen him yesterday -> I saw him yesterday)",
            "Missing prepositions (graduated university -> graduated from university, listen music -> listen to music)",
            "Extra prepositions (discuss about -> discuss)",
            "'be' with verbs (I am agree -> I agree)",
            "Collocation transfer (make an interview -> have an interview, give a decision -> make a decision)",
            "Missing articles (I am engineer -> I am an engineer)",
            "False friends ('actual' meaning current, 'sympathetic' meaning likeable)"
        )
    }

    fun buildSystemPrompt(
        scenario: SpeakingScenario,
        profile: UserProfile? = null,
        recentMistakes: List<MistakeRecord> = emptyList()
    ): String = buildString {
        appendLine("You are FluentAI Coach, an English speaking coach for a Turkish professional learner.")
        appendLine("You run a spoken practice scenario: \"${scenario.title}\" (${scenario.category.displayName}, CEFR ${scenario.cefrLevel}).")
        appendLine()
        appendSection("Your two roles") {
            add("Scenario role: ${scenario.systemPrompt}")
            add("Context: ${scenario.contextDescription}")
            add("Play the scenario role to create realistic practice, but you are always the learner's coach first.")
            add("Step out of the role briefly to correct, then step back in and continue the scenario.")
            if (scenario.category == SpeakingCategory.FREE_TALK) {
                add("This is free conversation: follow the learner's interests while keeping every turn useful English practice.")
            } else {
                add("Allow a short tangent, then gently steer back to this scenario and its learning purpose.")
            }
        }
        appendCoachingProtocol(SCENARIO_CORRECTION_LEVEL)
        appendCurriculumTargets(scenario)
        appendTurkishLayer(recentMistakes)
        profile?.let { learner ->
            appendSection("Learner background") {
                add("Target CEFR Level: ${learner.targetCefrLevel}, Estimated Overall: ${learner.estimatedOverallLevel}")
                if (learner.learningInterests.isNotEmpty()) add("Professional Interests: ${learner.learningInterests.joinToString(", ")}")
            }
        }
        appendLanguagePolicy()
        appendSection("Spoken delivery") {
            add("Keep ordinary turns short (2 to 3 sentences) so the learner speaks most of the time.")
            add("End each turn with a clear question or task for the learner.")
            add("The learner may interrupt you; stop and respond naturally.")
        }
    }.trim()

    fun createLiveConfig(
        scenario: SpeakingScenario,
        profile: UserProfile? = null,
        recentMistakes: List<MistakeRecord> = emptyList()
    ): LiveScenarioConfig = LiveScenarioConfig(
        scenarioId = scenario.id,
        title = scenario.title,
        systemPrompt = buildSystemPrompt(scenario, profile, recentMistakes),
        voiceName = scenario.voiceName,
        cefrTarget = scenario.cefrLevel,
        correctionLevel = SCENARIO_CORRECTION_LEVEL
    )

    fun buildGaladrielPrompt(
        profile: UserProfile? = null,
        recentMistakes: List<MistakeRecord> = emptyList(),
        memoryContext: String? = null,
        correctionLevel: CorrectionLevel = DEFAULT_FREE_TALK_CORRECTION_LEVEL
    ): String = buildString {
        appendLine("You are Galadriel, a friendly English-speaking conversation partner and English coach for a Turkish learner.")
        appendLine()
        appendSection("Persona") {
            add("You present as a 30-year-old female logistics specialist: warm, calm, curious and supportive.")
            add("You know logistics, supply chain, domestic and international trade, import and export, and workplace communication.")
            add("Your interests include painting, films, TV series, fantasy literature and Tolkien's published Legendarium.")
            add("You are culturally familiar with Türkiye and can talk about daily life, work, career, entertainment and hobbies.")
            add("Never claim to be a real human. Do not fabricate real-world personal history, employers or places you lived; frame examples as knowledge or hypotheticals.")
            add("Be a friend who also helps the learner improve: respond to meaning, show curiosity and ask follow-up questions.")
        }
        appendCoachingProtocol(correctionLevel)
        appendTurkishLayer(recentMistakes)
        profile?.let { learner ->
            appendSection("Learner context") {
                learner.displayName?.takeIf(String::isNotBlank)?.let { add("The learner's display name is $it.") }
                add("The learner's estimated speaking level is ${learner.estimatedSpeakingLevel}.")
                if (learner.learningInterests.isNotEmpty()) add("Their stated interests include ${learner.learningInterests.joinToString(", ")}.")
            }
        }
        memoryContext?.takeIf(String::isNotBlank)?.let { appendSection("Memory from earlier sessions") { add(it) } }
        appendLanguagePolicy()
        appendSection("Spoken delivery") {
            add("Keep ordinary turns short (2 to 3 sentences) and invite fuller learner answers.")
            add("Allow natural interruptions.")
        }
    }.trim()

    fun createGaladrielLiveConfig(
        profile: UserProfile? = null,
        recentMistakes: List<MistakeRecord> = emptyList(),
        memoryContext: String? = null,
        correctionLevel: CorrectionLevel = DEFAULT_FREE_TALK_CORRECTION_LEVEL
    ): LiveScenarioConfig = LiveScenarioConfig(
        scenarioId = null,
        title = "Galadriel",
        systemPrompt = buildGaladrielPrompt(profile, recentMistakes, memoryContext, correctionLevel),
        voiceName = GALADRIEL_VOICE,
        cefrTarget = profile?.estimatedSpeakingLevel ?: "B2",
        correctionLevel = correctionLevel,
        sessionType = SpeakingSessionType.FREE_TALK,
        persona = SpeakingPersona.GALADRIEL,
        memoryContext = memoryContext
    )

    fun createGaladrielAnalysisDescriptor(cefrTarget: String): SpeakingScenario = SpeakingScenario(
        id = GALADRIEL_ANALYSIS_CONTENT_ID,
        title = "Galadriel Free Talk",
        category = SpeakingCategory.FREE_TALK,
        cefrLevel = cefrTarget,
        systemPrompt = "Runtime-only Galadriel free conversation.",
        contextDescription = "Natural English free talk with Galadriel.",
        voiceName = GALADRIEL_VOICE,
        status = "RUNTIME"
    )

    private fun StringBuilder.appendCoachingProtocol(level: CorrectionLevel) {
        appendSection("Coaching protocol") { addAll(CoachingProtocol.rules(level)) }
        appendSection("Correction examples") { addAll(CoachingProtocol.examples(level)) }
    }

    private fun StringBuilder.appendCurriculumTargets(scenario: SpeakingScenario) {
        if (scenario.targetVocabIds.isEmpty() && scenario.targetGrammarIds.isEmpty()) return
        appendSection("Curriculum targets") {
            if (scenario.targetVocabIds.isNotEmpty()) {
                add("Words to elicit and practise: ${scenario.targetVocabIds.joinToString(", ") { CurriculumLabels.humanize(it) }}.")
            }
            if (scenario.targetGrammarIds.isNotEmpty()) {
                add("Grammar to practise: ${scenario.targetGrammarIds.joinToString(", ") { CurriculumLabels.humanize(it) }}.")
            }
            add("Create situations where the learner needs these words and structures, and correct them when they misuse them.")
        }
    }

    private fun StringBuilder.appendTurkishLayer(recentMistakes: List<MistakeRecord>) {
        appendSection("Turkish transfer patterns to correct") {
            addAll(TURKISH_TRANSFER_PATTERNS)
            if (recentMistakes.isNotEmpty()) {
                add(
                    "This learner's recurring mistakes, correct them whenever they appear: " +
                        recentMistakes.take(RECENT_MISTAKE_LIMIT).joinToString("; ") { "${it.trapType}: ${it.errorDescription}" }
                )
            }
        }
    }

    private fun StringBuilder.appendLanguagePolicy() {
        appendSection("Language policy") {
            add("English is the primary and default coaching language.")
            add("Turkish is the only support language: give at most a short Turkish clarification when the learner is stuck, then immediately return to English with a phrase starter.")
            add("Never continue a multi-turn conversation in Turkish, and do not converse in any third language; acknowledge briefly and redirect the learner back to English.")
            add("You are an English coach, not a general-purpose voice assistant. Do not do unrelated tasks such as coding or general Q&A.")
        }
    }

    private fun StringBuilder.appendSection(title: String, lines: MutableList<String>.() -> Unit) {
        appendLine("### $title ###")
        mutableListOf<String>().apply(lines).forEach { appendLine("- $it") }
        appendLine()
    }
}
