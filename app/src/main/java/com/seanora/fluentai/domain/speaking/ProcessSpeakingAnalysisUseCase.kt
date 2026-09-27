package com.seanora.fluentai.domain.speaking

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.SpeakingSessionAnalysis
import com.seanora.fluentai.data.user.dao.SpeakingSessionDao
import com.seanora.fluentai.data.user.entity.SpeakingSessionRecordEntity
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

data class ProcessSpeakingAnalysisResult(
    val sessionRecordId: String,
    val overallSpeakingSnapshot: MasterySnapshot,
    val recordedGrammarEvidenceCount: Int,
    val recordedVocabEvidenceCount: Int,
    val recordedMistakesCount: Int
)

/**
 * Core learning integration bridge for Phase 6.
 * Turns AI speaking evaluation into deterministic LearningEvidence, updates MasterySnapshots,
 * creates MistakeRecords in the Mistake Bank, and updates Spaced Repetition Review schedules
 * per AGENTS.md Section 9 & ADR-006.
 */
@Singleton
class ProcessSpeakingAnalysisUseCase @Inject constructor(
    private val recordLearningEvidenceUseCase: RecordLearningEvidenceUseCase,
    private val speakingSessionDao: SpeakingSessionDao
) {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    suspend operator fun invoke(analysis: SpeakingSessionAnalysis): ProcessSpeakingAnalysisResult {
        // 1. Persist the complete speaking session record in UserDatabase
        val sessionEntity = SpeakingSessionRecordEntity(
            id = analysis.id,
            scenarioId = analysis.scenarioId,
            scenarioTitle = analysis.scenarioTitle,
            timestamp = analysis.timestamp,
            durationSeconds = analysis.durationSeconds,
            turnsCount = analysis.turnsCount,
            overallFeedback = analysis.overallFeedback,
            estimatedTurnCefr = analysis.estimatedTurnCefr,
            fluencyScore = analysis.fluencyScore,
            vocabularyScore = analysis.vocabularyScore,
            grammarScore = analysis.grammarScore,
            coherenceScore = analysis.coherenceScore,
            analysisJson = json.encodeToString(analysis)
        )
        speakingSessionDao.insertSession(sessionEntity)

        // 2. Convert overall speaking session into LearningEvidence for Speaking domain
        val overallScore = (analysis.fluencyScore + analysis.vocabularyScore + analysis.grammarScore + analysis.coherenceScore) / 4f
        val speakingEvidence = LearningEvidence(
            contentId = analysis.scenarioId,
            domain = "speaking",
            activityType = "live_speaking_session",
            isCorrect = overallScore >= 0.60f,
            score = overallScore,
            responseTimeMs = analysis.durationSeconds * 1000L,
            details = "Turns: ${analysis.turnsCount}, Estimated CEFR: ${analysis.estimatedTurnCefr}",
            timestamp = analysis.timestamp
        )
        val finalSpeakingSnapshot = recordLearningEvidenceUseCase(evidence = speakingEvidence)

        // 3. Convert Grammar Observations into Grammar LearningEvidence & Mistake Bank records
        var grammarCount = 0
        var mistakesCount = 0
        for (obs in analysis.grammarObservations) {
            val contentId = obs.targetGrammarId ?: "grammar.speaking.${obs.trapType ?: "general"}"
            val grammarEvidence = LearningEvidence(
                contentId = contentId,
                domain = "grammar",
                activityType = "speaking_spontaneous",
                isCorrect = false,
                score = 0.35f,
                details = "Spoken: \"${obs.errorSnippet}\" -> Correct: \"${obs.correctedSnippet}\"",
                timestamp = analysis.timestamp
            )
            recordLearningEvidenceUseCase(
                evidence = grammarEvidence,
                trapType = obs.trapType ?: "speaking_grammar",
                errorDescription = obs.explanation,
                userAnswer = obs.errorSnippet,
                correctAnswer = obs.correctedSnippet
            )
            grammarCount++
            mistakesCount++
        }

        // 4. Convert Vocabulary Observations into Vocabulary LearningEvidence
        var vocabCount = 0
        for (obs in analysis.vocabularyObservations) {
            val contentId = obs.targetVocabId ?: "vocab.upgrade.${obs.usedWord.lowercase().replace(" ", "_")}"
            val vocabEvidence = LearningEvidence(
                contentId = contentId,
                domain = "vocabulary",
                activityType = "speaking_lexical_upgrade",
                isCorrect = true,
                score = 0.85f,
                details = "Used \"${obs.usedWord}\", recommended \"${obs.suggestedBetterWord}\" (${obs.register})",
                timestamp = analysis.timestamp
            )
            recordLearningEvidenceUseCase(evidence = vocabEvidence)
            vocabCount++
        }

        return ProcessSpeakingAnalysisResult(
            sessionRecordId = analysis.id,
            overallSpeakingSnapshot = finalSpeakingSnapshot,
            recordedGrammarEvidenceCount = grammarCount,
            recordedVocabEvidenceCount = vocabCount,
            recordedMistakesCount = mistakesCount
        )
    }
}
