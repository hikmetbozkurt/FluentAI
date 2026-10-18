package com.seanora.fluentai.ai.analysis.gemini

import com.seanora.fluentai.core.model.SpeakingScenario

class AnalysisObservationSanitizer {

    companion object {
        const val MISTAKE_BANK_CONTENT_ID = "review.mistake_bank"
        private val TRAP_TYPE_PATTERN = Regex("^[a-z][a-z0-9_]{1,39}$")
    }

    fun sanitize(payload: GeminiAnalysisPayload, scenario: SpeakingScenario): GeminiAnalysisPayload {
        val grammarIds = scenario.targetGrammarIds.toSet()
        val vocabIds = scenario.targetVocabIds.toSet()
        val practiceIds = grammarIds + vocabIds + scenario.id + MISTAKE_BANK_CONTENT_ID

        return payload.copy(
            fluencyScore = payload.fluencyScore.toUnitInterval(),
            vocabularyScore = payload.vocabularyScore.toUnitInterval(),
            grammarScore = payload.grammarScore.toUnitInterval(),
            coherenceScore = payload.coherenceScore.toUnitInterval(),
            grammarObservations = payload.grammarObservations.map { observation ->
                observation.copy(
                    targetGrammarId = observation.targetGrammarId?.takeIf { it in grammarIds },
                    trapType = observation.trapType?.trim()?.lowercase()?.takeIf { TRAP_TYPE_PATTERN.matches(it) }
                )
            },
            vocabularyObservations = payload.vocabularyObservations.map { observation ->
                observation.copy(targetVocabId = observation.targetVocabId?.takeIf { it in vocabIds })
            },
            suggestedPractice = payload.suggestedPractice.map { item ->
                item.copy(contentId = item.contentId?.takeIf { it in practiceIds })
            }
        )
    }

    private fun Float.toUnitInterval(): Float = if (isNaN()) 0f else coerceIn(0f, 1f)
}
