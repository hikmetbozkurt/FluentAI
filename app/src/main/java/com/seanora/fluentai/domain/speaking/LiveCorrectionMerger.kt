package com.seanora.fluentai.domain.speaking

import com.seanora.fluentai.core.model.CorrectionCategory
import com.seanora.fluentai.core.model.LiveCorrection
import com.seanora.fluentai.core.model.NaturalAlternative
import com.seanora.fluentai.core.model.SpeakingGrammarObservation
import com.seanora.fluentai.core.model.SpeakingSessionAnalysis
import com.seanora.fluentai.core.model.SpeakingVocabObservation
import javax.inject.Inject

class LiveCorrectionMerger @Inject constructor() {

    fun merge(analysis: SpeakingSessionAnalysis, corrections: List<LiveCorrection>): SpeakingSessionAnalysis {
        if (corrections.isEmpty()) return analysis
        val knownCorrections = analysis.knownCorrectedSnippets().toMutableSet()
        val fresh = corrections.filter { knownCorrections.add(it.correction.normalized()) }
        if (fresh.isEmpty()) return analysis
        val byKind = fresh.groupBy { it.category.kind() }
        return analysis.copy(
            grammarObservations = analysis.grammarObservations + byKind[Kind.GRAMMAR].orEmpty().map(::toGrammarObservation),
            vocabularyObservations = analysis.vocabularyObservations + byKind[Kind.VOCABULARY].orEmpty().map(::toVocabularyObservation),
            naturalAlternatives = analysis.naturalAlternatives + byKind[Kind.DELIVERY].orEmpty().map(::toNaturalAlternative)
        )
    }

    private fun toGrammarObservation(correction: LiveCorrection) = SpeakingGrammarObservation(
        id = "live-${correction.id}",
        userUtterance = correction.learnerSaid,
        errorSnippet = correction.learnerSaid,
        correctedSnippet = correction.correction,
        explanation = correction.explanation,
        trapType = correction.trapType
    )

    private fun toVocabularyObservation(correction: LiveCorrection) = SpeakingVocabObservation(
        id = "live-${correction.id}",
        usedWord = correction.learnerSaid,
        suggestedBetterWord = correction.correction,
        explanation = correction.explanation
    )

    private fun toNaturalAlternative(correction: LiveCorrection) = NaturalAlternative(
        originalUtterance = correction.learnerSaid,
        naturalAlternative = correction.correction,
        explanation = correction.explanation
    )

    private fun SpeakingSessionAnalysis.knownCorrectedSnippets(): Set<String> =
        (grammarObservations.map { it.correctedSnippet } +
            vocabularyObservations.map { it.suggestedBetterWord } +
            naturalAlternatives.map { it.naturalAlternative })
            .map { it.normalized() }
            .toSet()

    private fun String.normalized(): String = lowercase().replace(NON_WORD, " ").trim().replace(SPACES, " ")

    private fun CorrectionCategory.kind(): Kind = when (this) {
        CorrectionCategory.GRAMMAR -> Kind.GRAMMAR
        CorrectionCategory.VOCABULARY, CorrectionCategory.WORD_CHOICE -> Kind.VOCABULARY
        CorrectionCategory.PRONUNCIATION, CorrectionCategory.FLUENCY -> Kind.DELIVERY
    }

    private enum class Kind { GRAMMAR, VOCABULARY, DELIVERY }

    private companion object {
        val NON_WORD = Regex("[^a-z0-9' ]")
        val SPACES = Regex("\\s+")
    }
}
