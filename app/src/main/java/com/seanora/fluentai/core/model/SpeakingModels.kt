package com.seanora.fluentai.core.model

/**
 * 8 Core Speaking Scenario Categories defined in Phase 6.
 */
enum class SpeakingCategory(val displayName: String, val description: String) {
    FREE_TALK("Free Talk", "Casual, open-ended discussion on diverse topics"),
    DAILY_LIFE("Daily Life", "Practical situations, ordering, travel, and social interactions"),
    CAREER("Career", "Professional workplace communication, feedback, and leadership"),
    JOB_INTERVIEW("Job Interview", "Behavioral questions, technical defense, and STAR framework"),
    MEETING("Meeting", "Agile standups, quarterly reviews, and stakeholder alignment"),
    IELTS("IELTS", "Simulations of IELTS Speaking Parts 1, 2, and 3"),
    VOCAB_DRILL("Vocabulary Drill", "Targeted elicitation of high-level workplace vocabulary"),
    GRAMMAR_DRILL("Grammar Drill", "Targeted practice on tenses, conditionals, and Turkish traps")
}

enum class CorrectionLevel(val displayName: String, val emoji: String, val shortDescription: String) {
    LIGHT("Light", "🌱", "Fixes errors that change meaning and mistakes you repeat"),
    BALANCED("Balanced", "⚖️", "Fixes every clear grammar and word-choice error, one at a time"),
    THOROUGH("Thorough", "🎯", "Fixes every error and asks you to say it again correctly")
}

enum class CorrectionCategory(val displayName: String, val emoji: String) {
    GRAMMAR("Grammar", "🧩"),
    VOCABULARY("Vocabulary", "📚"),
    WORD_CHOICE("Word choice", "🔤"),
    PRONUNCIATION("Pronunciation", "🗣️"),
    FLUENCY("Fluency", "🌊")
}

data class LiveCorrection(
    val id: String,
    val learnerSaid: String,
    val correction: String,
    val explanation: String,
    val category: CorrectionCategory,
    val trapType: String? = null,
    val timestampMs: Long = System.currentTimeMillis()
)

data class SpeakingScenario(
    val id: String,
    val title: String,
    val category: SpeakingCategory,
    val cefrLevel: String,
    val systemPrompt: String,
    val contextDescription: String,
    val targetVocabIds: List<String> = emptyList(),
    val targetGrammarIds: List<String> = emptyList(),
    val suggestedStarterPhrases: List<String> = emptyList(),
    val voiceName: String = "Aoede",
    val topicTags: List<String> = emptyList(),
    val status: String = "APPROVED",
    val version: Int = 1
)
