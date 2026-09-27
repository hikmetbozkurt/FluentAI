package com.seanora.fluentai.data.user.mapper

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.MistakeStage
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.TodayPlanItem
import com.seanora.fluentai.core.model.TodayPracticePlan
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.data.user.entity.LearningEvidenceEntity
import com.seanora.fluentai.data.user.entity.MasterySnapshotEntity
import com.seanora.fluentai.data.user.entity.MistakeRecordEntity
import com.seanora.fluentai.data.user.entity.ReviewScheduleEntity
import com.seanora.fluentai.data.user.entity.TodayPlanRecordEntity
import com.seanora.fluentai.data.user.entity.UserProfileEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

fun UserProfileEntity.toDomain(): UserProfile {
    val interests: List<String> = try {
        if (learningInterestsJson.isBlank()) emptyList()
        else json.decodeFromString(learningInterestsJson)
    } catch (_: Exception) {
        emptyList()
    }

    return UserProfile(
        id = id,
        displayName = displayName?.trim()?.ifBlank { null },
        targetCefrLevel = targetCefrLevel,
        estimatedOverallLevel = estimatedOverallLevel,
        estimatedVocabLevel = estimatedVocabLevel,
        estimatedGrammarLevel = estimatedGrammarLevel,
        estimatedReadingLevel = estimatedReadingLevel,
        estimatedListeningLevel = estimatedListeningLevel,
        estimatedSpeakingLevel = estimatedSpeakingLevel,
        dailyGoalMinutes = dailyGoalMinutes,
        learningInterests = interests,
        streakDays = streakDays,
        lastActiveDate = lastActiveDate,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun UserProfile.toEntity(): UserProfileEntity {
    val interestsJson = try {
        json.encodeToString(learningInterests)
    } catch (_: Exception) {
        "[]"
    }

    return UserProfileEntity(
        id = id,
        displayName = displayName?.trim()?.ifBlank { null },
        targetCefrLevel = targetCefrLevel,
        estimatedOverallLevel = estimatedOverallLevel,
        estimatedVocabLevel = estimatedVocabLevel,
        estimatedGrammarLevel = estimatedGrammarLevel,
        estimatedReadingLevel = estimatedReadingLevel,
        estimatedListeningLevel = estimatedListeningLevel,
        estimatedSpeakingLevel = estimatedSpeakingLevel,
        dailyGoalMinutes = dailyGoalMinutes,
        learningInterestsJson = interestsJson,
        streakDays = streakDays,
        lastActiveDate = lastActiveDate,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun LearningEvidenceEntity.toDomain(): LearningEvidence {
    return LearningEvidence(
        id = id,
        contentId = contentId,
        domain = domain,
        activityType = activityType,
        isCorrect = isCorrect,
        score = score,
        responseTimeMs = responseTimeMs,
        details = detailsJson,
        timestamp = timestamp
    )
}

fun LearningEvidence.toEntity(): LearningEvidenceEntity {
    return LearningEvidenceEntity(
        id = id,
        contentId = contentId,
        domain = domain,
        activityType = activityType,
        isCorrect = isCorrect,
        score = score,
        responseTimeMs = responseTimeMs,
        detailsJson = details,
        timestamp = timestamp
    )
}

fun MasterySnapshotEntity.toDomain(): MasterySnapshot {
    val masteryStatus = try {
        MasteryStatus.valueOf(status)
    } catch (_: Exception) {
        MasteryStatus.NEW
    }

    return MasterySnapshot(
        contentId = contentId,
        domain = domain,
        level = level,
        confidence = confidence,
        totalAttempts = totalAttempts,
        correctAttempts = correctAttempts,
        lastAttemptTimestamp = lastAttemptTimestamp,
        lastDecayTimestamp = lastDecayTimestamp,
        status = masteryStatus
    )
}

fun MasterySnapshot.toEntity(): MasterySnapshotEntity {
    return MasterySnapshotEntity(
        contentId = contentId,
        domain = domain,
        level = level,
        confidence = confidence,
        totalAttempts = totalAttempts,
        correctAttempts = correctAttempts,
        lastAttemptTimestamp = lastAttemptTimestamp,
        lastDecayTimestamp = lastDecayTimestamp,
        status = status.name
    )
}

fun ReviewScheduleEntity.toDomain(): ReviewSchedule {
    return ReviewSchedule(
        contentId = contentId,
        domain = domain,
        nextReviewDueTimestamp = nextReviewDueTimestamp,
        intervalDays = intervalDays,
        easeFactor = easeFactor,
        repetitionNumber = repetitionNumber,
        lastReviewTimestamp = lastReviewTimestamp
    )
}

fun ReviewSchedule.toEntity(): ReviewScheduleEntity {
    return ReviewScheduleEntity(
        contentId = contentId,
        domain = domain,
        nextReviewDueTimestamp = nextReviewDueTimestamp,
        intervalDays = intervalDays,
        easeFactor = easeFactor,
        repetitionNumber = repetitionNumber,
        lastReviewTimestamp = lastReviewTimestamp
    )
}

fun MistakeRecordEntity.toDomain(): MistakeRecord {
    val mistakeStage = try {
        MistakeStage.valueOf(stage)
    } catch (_: Exception) {
        MistakeStage.OBSERVED
    }

    return MistakeRecord(
        id = id,
        contentId = contentId,
        trapType = trapType,
        errorDescription = errorDescription,
        userAnswer = userAnswer,
        correctAnswer = correctAnswer,
        stage = mistakeStage,
        occurrenceCount = occurrenceCount,
        firstObservedAt = firstObservedAt,
        lastObservedAt = lastObservedAt
    )
}

fun MistakeRecord.toEntity(): MistakeRecordEntity {
    return MistakeRecordEntity(
        id = id,
        contentId = contentId,
        trapType = trapType,
        errorDescription = errorDescription,
        userAnswer = userAnswer,
        correctAnswer = correctAnswer,
        stage = stage.name,
        occurrenceCount = occurrenceCount,
        firstObservedAt = firstObservedAt,
        lastObservedAt = lastObservedAt
    )
}

fun TodayPlanRecordEntity.toDomain(): TodayPracticePlan {
    val items: List<TodayPlanItem> = try {
        if (itemsJson.isBlank()) emptyList()
        else json.decodeFromString(itemsJson)
    } catch (_: Exception) {
        emptyList()
    }

    return TodayPracticePlan(
        id = id,
        date = date,
        targetCefrLevel = targetCefrLevel,
        allocatedMinutes = allocatedMinutes,
        rationale = rationale,
        items = items,
        createdAt = createdAt
    )
}

fun TodayPracticePlan.toEntity(): TodayPlanRecordEntity {
    val itemsJson = try {
        json.encodeToString(items)
    } catch (_: Exception) {
        "[]"
    }

    return TodayPlanRecordEntity(
        id = id,
        date = date,
        targetCefrLevel = targetCefrLevel,
        allocatedMinutes = allocatedMinutes,
        rationale = rationale,
        itemsJson = itemsJson,
        completedItemsCount = completedItemsCount,
        totalItemsCount = totalItemsCount,
        isCompleted = isCompleted,
        createdAt = createdAt,
        updatedAt = System.currentTimeMillis()
    )
}
