package com.seanora.fluentai.core.model

import kotlinx.serialization.Serializable

@Serializable
data class SpeakingSessionAnalysis(
    val id: String,
    val sessionId: String,
    val scenarioId: String,
    val scenarioTitle: String,
    val timestamp: Long,
    val durationSeconds: Int,
    val turnsCount: Int,
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
    val suggestedPractice: List<SuggestedPracticeItem> = emptyList(),
    val source: AnalysisSource = AnalysisSource.OFFLINE_RULES,
    val promptVersion: String? = null
)

@Serializable
enum class AnalysisSource {
    AI_MODEL,
    OFFLINE_RULES
}

@Serializable
data class SpeakingGrammarObservation(
    val id: String,
    val userUtterance: String,
    val errorSnippet: String,
    val correctedSnippet: String,
    val explanation: String,
    val targetGrammarId: String? = null,
    val trapType: String? = null
)

@Serializable
data class SpeakingVocabObservation(
    val id: String,
    val usedWord: String,
    val suggestedBetterWord: String,
    val explanation: String,
    val targetVocabId: String? = null,
    val register: String = "executive"
)

@Serializable
data class NaturalAlternative(
    val originalUtterance: String,
    val naturalAlternative: String,
    val explanation: String,
    val register: String = "business_casual"
)

@Serializable
data class SuggestedPracticeItem(
    val title: String,
    val description: String,
    val domain: String,
    val contentId: String? = null
)
