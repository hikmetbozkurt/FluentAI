package com.seanora.fluentai.data.user.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "default_user",

    @ColumnInfo(name = "display_name")
    val displayName: String? = null,

    @ColumnInfo(name = "target_cefr_level")
    val targetCefrLevel: String = "C1",

    @ColumnInfo(name = "estimated_overall_level")
    val estimatedOverallLevel: String = "B2",

    @ColumnInfo(name = "estimated_vocab_level")
    val estimatedVocabLevel: String = "B2",

    @ColumnInfo(name = "estimated_grammar_level")
    val estimatedGrammarLevel: String = "B1",

    @ColumnInfo(name = "estimated_reading_level")
    val estimatedReadingLevel: String = "B1",

    @ColumnInfo(name = "estimated_listening_level")
    val estimatedListeningLevel: String = "B1",

    @ColumnInfo(name = "estimated_speaking_level")
    val estimatedSpeakingLevel: String = "B1",

    @ColumnInfo(name = "daily_goal_minutes")
    val dailyGoalMinutes: Int = 20,

    @ColumnInfo(name = "learning_interests_json")
    val learningInterestsJson: String = "[]",

    @ColumnInfo(name = "streak_days")
    val streakDays: Int = 0,

    @ColumnInfo(name = "last_active_date")
    val lastActiveDate: String = "",

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "learning_evidence",
    indices = [
        Index(value = ["content_id"], name = "idx_evidence_content"),
        Index(value = ["domain"], name = "idx_evidence_domain"),
        Index(value = ["timestamp"], name = "idx_evidence_timestamp")
    ]
)
data class LearningEvidenceEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "content_id")
    val contentId: String,

    @ColumnInfo(name = "domain")
    val domain: String,

    @ColumnInfo(name = "activity_type")
    val activityType: String,

    @ColumnInfo(name = "is_correct")
    val isCorrect: Boolean,

    @ColumnInfo(name = "score")
    val score: Float,

    @ColumnInfo(name = "response_time_ms")
    val responseTimeMs: Long = 0,

    @ColumnInfo(name = "details_json")
    val detailsJson: String? = null,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "mastery_snapshots",
    indices = [
        Index(value = ["domain"], name = "idx_mastery_domain")
    ]
)
data class MasterySnapshotEntity(
    @PrimaryKey
    @ColumnInfo(name = "content_id")
    val contentId: String,

    @ColumnInfo(name = "domain")
    val domain: String,

    @ColumnInfo(name = "level")
    val level: Float,

    @ColumnInfo(name = "confidence")
    val confidence: Float,

    @ColumnInfo(name = "total_attempts")
    val totalAttempts: Int,

    @ColumnInfo(name = "correct_attempts")
    val correctAttempts: Int,

    @ColumnInfo(name = "last_attempt_timestamp")
    val lastAttemptTimestamp: Long,

    @ColumnInfo(name = "last_decay_timestamp")
    val lastDecayTimestamp: Long,

    @ColumnInfo(name = "status")
    val status: String
)

@Entity(
    tableName = "review_schedules",
    indices = [
        Index(value = ["next_review_due_timestamp"], name = "idx_review_due")
    ]
)
data class ReviewScheduleEntity(
    @PrimaryKey
    @ColumnInfo(name = "content_id")
    val contentId: String,

    @ColumnInfo(name = "domain")
    val domain: String,

    @ColumnInfo(name = "next_review_due_timestamp")
    val nextReviewDueTimestamp: Long,

    @ColumnInfo(name = "interval_days")
    val intervalDays: Float,

    @ColumnInfo(name = "ease_factor")
    val easeFactor: Float = 2.5f,

    @ColumnInfo(name = "repetition_number")
    val repetitionNumber: Int = 0,

    @ColumnInfo(name = "last_review_timestamp")
    val lastReviewTimestamp: Long = 0
)

@Entity(
    tableName = "mistake_records",
    indices = [
        Index(value = ["content_id"], name = "idx_mistake_content"),
        Index(value = ["stage"], name = "idx_mistake_stage")
    ]
)
data class MistakeRecordEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "content_id")
    val contentId: String,

    @ColumnInfo(name = "trap_type")
    val trapType: String,

    @ColumnInfo(name = "error_description")
    val errorDescription: String,

    @ColumnInfo(name = "user_answer")
    val userAnswer: String,

    @ColumnInfo(name = "correct_answer")
    val correctAnswer: String,

    @ColumnInfo(name = "stage")
    val stage: String,

    @ColumnInfo(name = "occurrence_count")
    val occurrenceCount: Int = 1,

    @ColumnInfo(name = "first_observed_at")
    val firstObservedAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "last_observed_at")
    val lastObservedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "speaking_sessions",
    indices = [
        Index(value = ["scenario_id"], name = "idx_speaking_session_scenario"),
        Index(value = ["timestamp"], name = "idx_speaking_session_timestamp")
    ]
)
data class SpeakingSessionRecordEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "scenario_id")
    val scenarioId: String,

    @ColumnInfo(name = "scenario_title")
    val scenarioTitle: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long,

    @ColumnInfo(name = "duration_seconds")
    val durationSeconds: Int,

    @ColumnInfo(name = "turns_count")
    val turnsCount: Int,

    @ColumnInfo(name = "overall_feedback")
    val overallFeedback: String,

    @ColumnInfo(name = "estimated_turn_cefr")
    val estimatedTurnCefr: String,

    @ColumnInfo(name = "fluency_score")
    val fluencyScore: Float,

    @ColumnInfo(name = "vocabulary_score")
    val vocabularyScore: Float,

    @ColumnInfo(name = "grammar_score")
    val grammarScore: Float,

    @ColumnInfo(name = "coherence_score")
    val coherenceScore: Float,

    @ColumnInfo(name = "analysis_json")
    val analysisJson: String
)

@Entity(
    tableName = "today_plans",
    indices = [
        Index(value = ["date"], name = "idx_today_plan_date")
    ]
)
data class TodayPlanRecordEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "target_cefr_level")
    val targetCefrLevel: String,

    @ColumnInfo(name = "allocated_minutes")
    val allocatedMinutes: Int,

    @ColumnInfo(name = "rationale")
    val rationale: String,

    @ColumnInfo(name = "items_json")
    val itemsJson: String,

    @ColumnInfo(name = "completed_items_count")
    val completedItemsCount: Int = 0,

    @ColumnInfo(name = "total_items_count")
    val totalItemsCount: Int = 0,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "vocabulary_feature_state")
data class VocabularyFeatureStateEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "vocabulary",

    @ColumnInfo(name = "daily_word_date")
    val dailyWordDate: String? = null,

    @ColumnInfo(name = "daily_word_content_id")
    val dailyWordContentId: String? = null,

    @ColumnInfo(name = "resume_destination")
    val resumeDestination: String? = null,

    @ColumnInfo(name = "resume_content_id")
    val resumeContentId: String? = null,

    @ColumnInfo(name = "resume_context_id")
    val resumeContextId: String? = null,

    @ColumnInfo(name = "resume_practice_mode")
    val resumePracticeMode: String? = null,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = 0L,
)

@Entity(tableName = "grammar_feature_state")
data class GrammarFeatureStateEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "grammar",

    @ColumnInfo(name = "resume_destination")
    val resumeDestination: String? = null,

    @ColumnInfo(name = "resume_content_id")
    val resumeContentId: String? = null,

    @ColumnInfo(name = "resume_secondary_content_id")
    val resumeSecondaryContentId: String? = null,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = 0L,
)
