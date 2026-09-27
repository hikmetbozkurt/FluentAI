package com.seanora.fluentai.core.model

import kotlinx.serialization.Serializable

/**
 * Structured post-session analysis produced after a live speaking conversation.
 * Per Phase 6 requirements: strengths, grammar observations, vocabulary observations,
 * natural alternatives, and suggested practice.
 */
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
    val fluencyScore: Float, // 0.0 to 1.0
    val vocabularyScore: Float, // 0.0 to 1.0
    val grammarScore: Float, // 0.0 to 1.0
    val coherenceScore: Float, // 0.0 to 1.0
    val strengths: List<String> = emptyList(),
    val grammarObservations: List<SpeakingGrammarObservation> = emptyList(),
    val vocabularyObservations: List<SpeakingVocabObservation> = emptyList(),
    val naturalAlternatives: List<NaturalAlternative> = emptyList(),
    val suggestedPractice: List<SuggestedPracticeItem> = emptyList()
)

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
    val domain: String, // "grammar", "vocabulary", "review", "speaking"
    val contentId: String? = null
)
