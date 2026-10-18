package com.seanora.fluentai.domain.speaking

import com.seanora.fluentai.core.common.transaction.TransactionRunner
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.SpeakingGrammarObservation
import com.seanora.fluentai.core.model.SpeakingSessionAnalysis
import com.seanora.fluentai.core.model.SpeakingVocabObservation
import com.seanora.fluentai.data.repository.SpeakingSessionRepository
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import javax.inject.Inject
import javax.inject.Singleton

data class ProcessSpeakingAnalysisResult(
    val sessionRecordId: String,
    val overallSpeakingSnapshot: MasterySnapshot,
    val recordedGrammarEvidenceCount: Int,
    val recordedVocabEvidenceCount: Int,
    val recordedMistakesCount: Int
)

@Singleton
class ProcessSpeakingAnalysisUseCase @Inject constructor(
    private val recordLearningEvidenceUseCase: RecordLearningEvidenceUseCase,
    private val speakingSessionRepository: SpeakingSessionRepository,
    private val transactionRunner: TransactionRunner
) {

    companion object {
        private const val SPEAKING_PASS_THRESHOLD = 0.60f
        private const val GRAMMAR_MISTAKE_SCORE = 0.35f
        private const val VOCABULARY_UPGRADE_SCORE = 0.85f
        private const val GENERAL_TRAP_TYPE = "general"
        private const val SPEAKING_GRAMMAR_TRAP_TYPE = "speaking_grammar"
    }

    suspend operator fun invoke(analysis: SpeakingSessionAnalysis): ProcessSpeakingAnalysisResult =
        transactionRunner.runInTransaction {
            speakingSessionRepository.saveAnalysis(analysis)
            val speakingSnapshot = recordLearningEvidenceUseCase(evidence = analysis.toSpeakingEvidence())
            analysis.grammarObservations.forEach { recordGrammarObservation(it, analysis.timestamp) }
            analysis.vocabularyObservations.forEach { recordVocabularyObservation(it, analysis.timestamp) }

            ProcessSpeakingAnalysisResult(
                sessionRecordId = analysis.id,
                overallSpeakingSnapshot = speakingSnapshot,
                recordedGrammarEvidenceCount = analysis.grammarObservations.size,
                recordedVocabEvidenceCount = analysis.vocabularyObservations.size,
                recordedMistakesCount = analysis.grammarObservations.size
            )
        }

    private fun SpeakingSessionAnalysis.toSpeakingEvidence(): LearningEvidence {
        val overallScore = (fluencyScore + vocabularyScore + grammarScore + coherenceScore) / 4f
        return LearningEvidence(
            contentId = scenarioId,
            domain = "speaking",
            activityType = "live_speaking_session",
            isCorrect = overallScore >= SPEAKING_PASS_THRESHOLD,
            score = overallScore,
            responseTimeMs = durationSeconds * 1000L,
            details = "Turns: $turnsCount, Estimated CEFR: $estimatedTurnCefr, Source: $source",
            timestamp = timestamp
        )
    }

    private suspend fun recordGrammarObservation(observation: SpeakingGrammarObservation, timestamp: Long) {
        val contentId = observation.targetGrammarId ?: "grammar.speaking.${observation.trapType ?: GENERAL_TRAP_TYPE}"
        recordLearningEvidenceUseCase(
            evidence = LearningEvidence(
                contentId = contentId,
                domain = "grammar",
                activityType = "speaking_spontaneous",
                isCorrect = false,
                score = GRAMMAR_MISTAKE_SCORE,
                details = "Spoken: \"${observation.errorSnippet}\" -> Correct: \"${observation.correctedSnippet}\"",
                timestamp = timestamp
            ),
            trapType = observation.trapType ?: SPEAKING_GRAMMAR_TRAP_TYPE,
            errorDescription = observation.explanation,
            userAnswer = observation.errorSnippet,
            correctAnswer = observation.correctedSnippet
        )
    }

    private suspend fun recordVocabularyObservation(observation: SpeakingVocabObservation, timestamp: Long) {
        val contentId = observation.targetVocabId
            ?: "vocab.upgrade.${observation.usedWord.lowercase().replace(" ", "_")}"
        recordLearningEvidenceUseCase(
            evidence = LearningEvidence(
                contentId = contentId,
                domain = "vocabulary",
                activityType = "speaking_lexical_upgrade",
                isCorrect = true,
                score = VOCABULARY_UPGRADE_SCORE,
                details = "Used \"${observation.usedWord}\", recommended \"${observation.suggestedBetterWord}\" (${observation.register})",
                timestamp = timestamp
            )
        )
    }
}
