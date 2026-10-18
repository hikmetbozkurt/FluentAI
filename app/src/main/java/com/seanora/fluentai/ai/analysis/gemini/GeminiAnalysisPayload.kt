package com.seanora.fluentai.ai.analysis.gemini

import com.seanora.fluentai.core.model.AnalysisSource
import com.seanora.fluentai.core.model.NaturalAlternative
import com.seanora.fluentai.core.model.SpeakingGrammarObservation
import com.seanora.fluentai.core.model.SpeakingSessionAnalysis
import com.seanora.fluentai.core.model.SpeakingVocabObservation
import com.seanora.fluentai.core.model.SuggestedPracticeItem
import kotlinx.serialization.Serializable

@Serializable
data class GeminiAnalysisPayload(
    val overallFeedback: String,
    val estimatedTurnCefr: String,
    val fluencyScore: Float,
    val vocabularyScore: Float,
    val grammarScore: Float,
    val coherenceScore: Float,
    val strengths: List<String> = emptyList(),
    val grammarObservations: List<SpeakingGrammarObservation> = emptyList(),
    val vocabularyObservations: List<SpeakingVocabObservation> = emptyList(),
    val naturalAlternatives: List<NaturalAlternative> = emptyList(),
    val suggestedPractice: List<SuggestedPracticeItem> = emptyList()
)

data class AnalysisSessionMetadata(
    val analysisId: String,
    val sessionId: String,
    val scenarioId: String,
    val scenarioTitle: String,
    val timestampMs: Long,
    val durationSeconds: Int,
    val learnerTurns: Int,
    val promptVersion: String
)

fun GeminiAnalysisPayload.toDomain(metadata: AnalysisSessionMetadata) = SpeakingSessionAnalysis(
    id = metadata.analysisId,
    sessionId = metadata.sessionId,
    scenarioId = metadata.scenarioId,
    scenarioTitle = metadata.scenarioTitle,
    timestamp = metadata.timestampMs,
    durationSeconds = metadata.durationSeconds,
    turnsCount = metadata.learnerTurns,
    overallFeedback = overallFeedback,
    estimatedTurnCefr = estimatedTurnCefr,
    fluencyScore = fluencyScore,
    vocabularyScore = vocabularyScore,
    grammarScore = grammarScore,
    coherenceScore = coherenceScore,
    strengths = strengths,
    grammarObservations = grammarObservations,
    vocabularyObservations = vocabularyObservations,
    naturalAlternatives = naturalAlternatives,
    suggestedPractice = suggestedPractice,
    source = AnalysisSource.AI_MODEL,
    promptVersion = metadata.promptVersion
)
