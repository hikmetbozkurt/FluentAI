package com.seanora.fluentai.ai.analysis

import com.seanora.fluentai.ai.live.TranscriptEntry
import com.seanora.fluentai.ai.live.TranscriptRole
import com.seanora.fluentai.core.model.CorrectionMode
import com.seanora.fluentai.core.model.NaturalAlternative
import com.seanora.fluentai.core.model.SpeakingGrammarObservation
import com.seanora.fluentai.core.model.SpeakingScenario
import com.seanora.fluentai.core.model.SpeakingSessionAnalysis
import com.seanora.fluentai.core.model.SpeakingVocabObservation
import com.seanora.fluentai.core.model.SuggestedPracticeItem
import java.util.UUID
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min

/**
 * Deterministic, offline-first speaking evaluator and linguistic analyzer.
 * Satisfies AGENTS.md Section 5 (Offline-First Rule) by providing rich language analysis
 * of user speech without requiring cloud network connectivity.
 */
@Singleton
class MockSpeakingAnalysisClient @Inject constructor() : SpeakingAnalysisClient {

    override suspend fun analyzeSession(
        scenario: SpeakingScenario,
        mode: CorrectionMode,
        transcript: List<TranscriptEntry>,
        durationSeconds: Int
    ): Result<SpeakingSessionAnalysis> {
        val userEntries = transcript.filter { it.role == TranscriptRole.USER && it.text.isNotBlank() }
        if (userEntries.isEmpty()) {
            return Result.failure(IllegalArgumentException("Speaking analysis requires at least one learner turn."))
        }
        val userText = userEntries.joinToString(" ") { it.text }

        val grammarObservations = mutableListOf<SpeakingGrammarObservation>()
        val vocabObservations = mutableListOf<SpeakingVocabObservation>()
        val naturalAlternatives = mutableListOf<NaturalAlternative>()
        val strengths = mutableListOf<String>()

        // 1. Detect Turkish L1 transfer patterns (ADR-010)
        detectTurkishTraps(userEntries, grammarObservations)

        // 2. Vocabulary upgrades
        detectVocabUpgrades(userEntries, vocabObservations)

        // 3. Natural reformulation alternatives
        generateNaturalAlternatives(userEntries, scenario, naturalAlternatives)

        // 4. Strengths identification
        if (userEntries.isNotEmpty()) {
            strengths.add("Maintained active turn-taking with ${userEntries.size} spoken conversational contributions.")
            if (userText.split("\\s+".toRegex()).size > 40) {
                strengths.add("Showed willingness to elaborate with extended sentence structures and contextual detail.")
            } else {
                strengths.add("Communicated concisely and responded directly to prompts.")
            }
            if (grammarObservations.isEmpty()) {
                strengths.add("Strong control of basic grammatical foundations and sentence cohesion.")
            } else {
                strengths.add("Expressed complex professional concepts despite minor grammatical interference.")
            }
        }

        // 5. Suggested next practice items linked to curriculum
        val suggestedPractice = mutableListOf<SuggestedPracticeItem>()
        if (scenario.targetGrammarIds.isNotEmpty()) {
            val primaryGrammar = scenario.targetGrammarIds.first()
            suggestedPractice.add(
                SuggestedPracticeItem(
                    title = "Review Target Grammar Structure",
                    description = "Consolidate the grammatical patterns emphasized in this scenario.",
                    domain = "grammar",
                    contentId = primaryGrammar
                )
            )
        }
        if (scenario.targetVocabIds.isNotEmpty()) {
            val primaryVocab = scenario.targetVocabIds.first()
            suggestedPractice.add(
                SuggestedPracticeItem(
                    title = "Vocabulary Precision Drill",
                    description = "Master high-impact lexical collocations from this dialogue.",
                    domain = "vocabulary",
                    contentId = primaryVocab
                )
            )
        }
        if (grammarObservations.isNotEmpty()) {
            suggestedPractice.add(
                SuggestedPracticeItem(
                    title = "Targeted Mistake Bank Drill",
                    description = "Revisit the ${grammarObservations.size} language pattern${if (grammarObservations.size == 1) "" else "s"} observed in this conversation.",
                    domain = "review",
                    contentId = "review.mistake_bank"
                )
            )
        }

        // 6. Deterministic transcript-derived scoring. These values measure observable text
        // characteristics only; they do not claim acoustic pronunciation accuracy.
        val words = userText.lowercase()
            .split(Regex("[^a-z']+"))
            .filter { it.isNotBlank() }
        val wordCount = words.size.coerceAtLeast(1)
        val uniqueWordRatio = words.toSet().size.toFloat() / wordCount
        val longerWordRatio = words.count { it.length >= 7 }.toFloat() / wordCount
        val averageWordsPerTurn = wordCount.toFloat() / userEntries.size
        val completedTurnRatio = userEntries.count {
            it.text.trimEnd().lastOrNull() in setOf('.', '?', '!')
        }.toFloat() / userEntries.size

        val mistakeDensity = grammarObservations.size.toFloat() / userEntries.size
        val grammarScore = (0.90f - mistakeDensity * 0.12f).coerceIn(0.40f, 0.95f)
        val vocabScore = (
            0.45f + uniqueWordRatio * 0.32f + longerWordRatio * 0.45f - vocabObservations.size * 0.03f
        ).coerceIn(0.40f, 0.95f)
        val fluencyScore = (
            0.45f + min(averageWordsPerTurn / 50f, 0.30f) + min(userEntries.size * 0.04f, 0.16f)
        ).coerceIn(0.40f, 0.95f)
        val coherenceScore = (
            0.45f + completedTurnRatio * 0.22f + min(averageWordsPerTurn / 60f, 0.25f)
        ).coerceIn(0.40f, 0.92f)

        val estimatedCefr = when {
            grammarScore >= 0.85f && vocabScore >= 0.80f -> scenario.cefrLevel
            grammarScore >= 0.70f -> "B2"
            else -> "B1"
        }

        val analysis = SpeakingSessionAnalysis(
            id = UUID.randomUUID().toString(),
            sessionId = "session_${System.currentTimeMillis()}",
            scenarioId = scenario.id,
            scenarioTitle = scenario.title,
            timestamp = System.currentTimeMillis(),
            durationSeconds = durationSeconds,
            turnsCount = userEntries.size,
            overallFeedback = buildOverallFeedback(
                scenario = scenario,
                mode = mode,
                mistakeCount = grammarObservations.size,
                turnsCount = userEntries.size,
                wordCount = wordCount
            ),
            estimatedTurnCefr = estimatedCefr,
            fluencyScore = fluencyScore,
            vocabularyScore = vocabScore,
            grammarScore = grammarScore,
            coherenceScore = coherenceScore,
            strengths = strengths,
            grammarObservations = grammarObservations,
            vocabularyObservations = vocabObservations,
            naturalAlternatives = naturalAlternatives,
            suggestedPractice = suggestedPractice
        )

        return Result.success(analysis)
    }

    private fun detectTurkishTraps(
        userEntries: List<TranscriptEntry>,
        observations: MutableList<SpeakingGrammarObservation>
    ) {
        val patterns = listOf(
            Triple(
                Pattern.compile("(?i)\\bsince\\s+(\\d+|two|three|four|five|six|several|a few)\\s+(years?|months?|days?|weeks?|hours?)\\b"),
                "tense_duration",
                "Use 'for' with durations of time (e.g. 'for two years'). 'Since' is only used with specific starting points (e.g. 'since 2022')."
            ),
            Triple(
                Pattern.compile("(?i)\\bI am (agree|disagree)\\b"),
                "be_verb_overuse",
                "'Agree' and 'disagree' are verbs in English. Say 'I agree' or 'I disagree', not 'I am agree'."
            ),
            Triple(
                Pattern.compile("(?i)\\bexplain me\\b"),
                "preposition_omission",
                "'Explain' requires the preposition 'to' before the person receiving the explanation: 'explain to me'."
            ),
            Triple(
                Pattern.compile("(?i)\\b(make|making|made) an interview\\b"),
                "collocation_transfer",
                "In professional English, we 'have', 'hold', or 'conduct' an interview, not 'make' an interview."
            ),
            Triple(
                Pattern.compile("(?i)\\b(give|giving|gave) a decision\\b"),
                "collocation_transfer",
                "The natural English collocation is 'make a decision' or 'reach a decision'."
            ),
            Triple(
                Pattern.compile("(?i)\\bgraduated university\\b"),
                "preposition_omission",
                "Say 'graduated from university'. In English, 'graduate' requires 'from' when specifying the institution."
            ),
            Triple(
                Pattern.compile("(?i)\\blisten music\\b"),
                "preposition_omission",
                "The verb 'listen' requires the preposition 'to': 'listen to music'."
            )
        )

        for (entry in userEntries) {
            for ((pattern, trapType, explanation) in patterns) {
                val matcher = pattern.matcher(entry.text)
                if (matcher.find()) {
                    val errorSnippet = matcher.group()
                    val corrected = when (trapType) {
                        "tense_duration" -> errorSnippet.replace("since", "for", ignoreCase = true)
                        "be_verb_overuse" -> if (errorSnippet.contains("disagree", ignoreCase = true)) "I disagree" else "I agree"
                        "preposition_omission" -> when {
                            errorSnippet.contains("explain", ignoreCase = true) -> "explain to me"
                            errorSnippet.contains("graduated", ignoreCase = true) -> "graduated from university"
                            else -> "listen to music"
                        }
                        "collocation_transfer" -> if (errorSnippet.contains("interview", ignoreCase = true)) "conduct an interview" else "make a decision"
                        else -> errorSnippet
                    }

                    observations.add(
                        SpeakingGrammarObservation(
                            id = UUID.randomUUID().toString(),
                            userUtterance = entry.text,
                            errorSnippet = errorSnippet,
                            correctedSnippet = corrected,
                            explanation = explanation,
                            targetGrammarId = if (trapType.startsWith("tense")) "grammar.present-perfect-vs-past-simple" else null,
                            trapType = trapType
                        )
                    )
                }
            }
        }
    }

    private fun detectVocabUpgrades(
        userEntries: List<TranscriptEntry>,
        observations: MutableList<SpeakingVocabObservation>
    ) {
        val upgrades = listOf(
            Pair("big problem", Pair("critical bottleneck", "In an executive tech context, 'bottleneck' specifically conveys an impediment that restricts capacity.")),
            Pair("good idea", Pair("viable strategy", "'Viable strategy' demonstrates higher commercial maturity than 'good idea'.")),
            Pair("very important", Pair("imperative", "'Imperative' concisely conveys urgent strategic necessity.")),
            Pair("talk with", Pair("align with", "'Align with stakeholders' reflects executive corporate terminology.")),
            Pair("change it", Pair("iterate or pivot", "'Iterate' or 'pivot' conveys structured agile adaptation.")),
            Pair("make better", Pair("enhance or optimize", "'Optimize' or 'enhance' conveys deliberate engineering improvement."))
        )

        for (entry in userEntries) {
            for ((simple, upgradeInfo) in upgrades) {
                if (entry.text.contains(simple, ignoreCase = true)) {
                    observations.add(
                        SpeakingVocabObservation(
                            id = UUID.randomUUID().toString(),
                            usedWord = simple,
                            suggestedBetterWord = upgradeInfo.first,
                            explanation = upgradeInfo.second,
                            targetVocabId = if (upgradeInfo.first.contains("bottleneck")) "vocab.bottleneck" else null,
                            register = "executive"
                        )
                    )
                }
            }
        }
    }

    private fun generateNaturalAlternatives(
        userEntries: List<TranscriptEntry>,
        scenario: SpeakingScenario,
        alternatives: MutableList<NaturalAlternative>
    ) {
        for (entry in userEntries.take(3)) {
            val text = entry.text.trim()
            if (text.length > 15) {
                val elevated = elevateExpression(text)
                if (elevated != text) {
                    alternatives.add(
                        NaturalAlternative(
                            originalUtterance = text,
                            naturalAlternative = elevated,
                            explanation = "Smoother professional phrasing suitable for a ${scenario.category.displayName} context.",
                            register = "executive"
                        )
                    )
                }
            }
        }
    }

    private fun elevateExpression(text: String): String {
        return text
            .replace("I think that", "In my view,", ignoreCase = true)
            .replace("we should do", "we ought to implement", ignoreCase = true)
            .replace("a lot of problems", "substantial friction", ignoreCase = true)
            .replace("because of this", "consequently,", ignoreCase = true)
            .replace("talk about", "deliberate on", ignoreCase = true)
            .replace("fix the bug", "remediate the issue", ignoreCase = true)
    }

    private fun buildOverallFeedback(
        scenario: SpeakingScenario,
        mode: CorrectionMode,
        mistakeCount: Int,
        turnsCount: Int,
        wordCount: Int
    ): String {
        return when {
            turnsCount < 2 -> "You contributed one learner turn with $wordCount words in ${scenario.title}. Continue the conversation longer to support a more representative analysis."
            mistakeCount == 0 -> "Across $turnsCount learner turns and $wordCount words in ${scenario.title}, no supported Turkish-transfer pattern was detected. Your responses stayed communicatively clear in ${mode.displayName} mode."
            mistakeCount <= 2 -> "Across $turnsCount learner turns and $wordCount words in ${scenario.title}, your message remained clear while ${mistakeCount} supported language pattern${if (mistakeCount == 1) " was" else "s were"} identified for review."
            else -> "Across $turnsCount learner turns and $wordCount words in ${scenario.title}, the transcript shows $mistakeCount recurring language patterns worth targeting in the next practice."
        }
    }
}
