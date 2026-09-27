package com.seanora.fluentai.core.model

data class UserProfile(
    val id: String = "default_user",
    val displayName: String? = null,
    val targetCefrLevel: String = "C1",
    val estimatedOverallLevel: String = "B2",
    val estimatedVocabLevel: String = "B2",
    val estimatedGrammarLevel: String = "B1",
    val estimatedReadingLevel: String = "B1",
    val estimatedListeningLevel: String = "B1",
    val estimatedSpeakingLevel: String = "B1",
    val dailyGoalMinutes: Int = 20,
    val learningInterests: List<String> = listOf("business", "leadership", "technology"),
    val streakDays: Int = 0,
    val lastActiveDate: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class LearningEvidence(
    val id: Long = 0,
    val contentId: String,
    val domain: String,
    val activityType: String,
    val isCorrect: Boolean,
    val score: Float,
    val responseTimeMs: Long = 0,
    val details: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

enum class MasteryStatus {
    NEW,
    LEARNING,
    PRACTICED,
    MASTERED,
    MAINTAINING
}

data class MasterySnapshot(
    val contentId: String,
    val domain: String,
    val level: Float, // 0.0 to 1.0 deterministic mastery
    val confidence: Float, // 0.0 to 1.0
    val totalAttempts: Int,
    val correctAttempts: Int,
    val lastAttemptTimestamp: Long,
    val lastDecayTimestamp: Long,
    val status: MasteryStatus
)

data class ReviewSchedule(
    val contentId: String,
    val domain: String,
    val nextReviewDueTimestamp: Long,
    val intervalDays: Float,
    val easeFactor: Float = 2.5f,
    val repetitionNumber: Int = 0,
    val lastReviewTimestamp: Long = 0
)

enum class MistakeStage {
    OBSERVED,
    POSSIBLE,
    RECURRING,
    TARGETED,
    MONITORING,
    RESOLVED
}

data class MistakeRecord(
    val id: Long = 0,
    val contentId: String,
    val trapType: String,
    val errorDescription: String,
    val userAnswer: String,
    val correctAnswer: String,
    val stage: MistakeStage,
    val occurrenceCount: Int,
    val firstObservedAt: Long,
    val lastObservedAt: Long
)
