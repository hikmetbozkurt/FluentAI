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

/**
 * 4 Real-Time Speaking Correction Modes defined in Phase 6.
 */
enum class CorrectionMode(
    val displayName: String,
    val shortDescription: String,
    val coachGuidance: String
) {
    FLOW(
        displayName = "Flow",
        shortDescription = "Fluency focus without interruptions",
        coachGuidance = "Prioritize conversational momentum, pacing, and confidence. Do not interrupt or correct minor grammar or vocabulary slips during the turn. Acknowledge and keep the conversation flowing naturally."
    ),
    COACH(
        displayName = "Coach",
        shortDescription = "Supportive real-time guidance & alternatives",
        coachGuidance = "Act as an encouraging executive language coach. Gently rephrase awkward structures into natural white-collar idioms when appropriate, maintaining a warm and educational atmosphere."
    ),
    DRILL(
        displayName = "Drill",
        shortDescription = "Active target targeting & immediate feedback",
        coachGuidance = "Challenge the user specifically to use the target vocabulary and grammar structures. If the user makes an error on a target concept or uses a common Turkish learner trap, prompt them immediately to reformulate."
    ),
    MOCK(
        displayName = "Mock",
        shortDescription = "Formal interview & exam simulation",
        coachGuidance = "Maintain formal professional distance. Act strictly in character as an interviewer or IELTS examiner. Ask probing follow-up questions, do not provide informal coaching during the test, and evaluate rigorously."
    )
}

/**
 * Domain model for a curriculum Speaking Scenario loaded from ContentDatabase.
 */
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
    val recommendedCorrectionMode: CorrectionMode = CorrectionMode.COACH,
    val voiceName: String = "Aoede",
    val topicTags: List<String> = emptyList(),
    val status: String = "APPROVED",
    val version: Int = 1
)
