package com.seanora.fluentai.domain.today

import com.seanora.fluentai.core.model.GrammarLesson
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.ListeningScenario
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.MistakeStage
import com.seanora.fluentai.core.model.ReadingArticle
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.SpeakingScenario
import com.seanora.fluentai.core.model.TodayItemType
import com.seanora.fluentai.core.model.TodayPlanItem
import com.seanora.fluentai.core.model.TodayPracticePlan
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.core.model.VocabItem
import javax.inject.Inject
import javax.inject.Singleton
import java.time.LocalDate
import java.time.ZoneId

/**
 * Encapsulates all data required to generate a personalized Today's Practice plan.
 */
data class TodayPlannerInput(
    val userProfile: UserProfile,
    val dueReviews: List<ReviewSchedule> = emptyList(),
    val activeMistakes: List<MistakeRecord> = emptyList(),
    val masterySnapshots: List<MasterySnapshot> = emptyList(),
    val recentEvidence: List<LearningEvidence> = emptyList(),
    val availableVocab: List<VocabItem> = emptyList(),
    val availableGrammar: List<GrammarLesson> = emptyList(),
    val availableReadings: List<ReadingArticle> = emptyList(),
    val availableListening: List<ListeningScenario> = emptyList(),
    val availableSpeaking: List<SpeakingScenario> = emptyList(),
    val targetDate: String // "YYYY-MM-DD"
)

/**
 * Deterministic, local-first orchestrator engine for Today's Practice.
 *
 * Rules per AGENTS.md & PHASES.md:
 * - Deterministic & reproducible: Given identical input state, produces the exact same plan.
 * - Explainable: Every item has a human-readable explanation and the plan has an overarching rationale.
 * - Balances:
 *   1. Due Spaced Repetition reviews (retention first)
 *   2. Active recurring mistakes (L1 Turkish traps & grammar/vocab weaknesses)
 *   3. Speaking Coach practice (active production)
 *   4. Curriculum progression & weak skill reinforcement (variety/weekly balance)
 * - Zero runtime AI reliance (Section 4).
 */
@Singleton
class TodayPlanner @Inject constructor() {

    fun createPlan(input: TodayPlannerInput): TodayPracticePlan {
        val userProfile = input.userProfile
        val budgetMinutes = userProfile.dailyGoalMinutes.coerceIn(10, 60)
        var remainingMinutes = budgetMinutes
        val planId = "today_plan_${input.targetDate}"
        val plannedItems = mutableListOf<TodayPlanItem>()
        val rationalePoints = mutableListOf<String>()

        val userTargetCefr = userProfile.targetCefrLevel.ifBlank { "B2" }
        val userCurrentCefr = userProfile.estimatedOverallLevel.ifBlank { "B1" }

        // 1. Spaced Repetition Due Reviews (Priority 1: Retention First)
        if (input.dueReviews.isNotEmpty() && remainingMinutes >= 3) {
            val maxReviews = minOf(4, remainingMinutes / 2)
            val selectedReviews = input.dueReviews
                .sortedWith(compareBy<ReviewSchedule> { it.nextReviewDueTimestamp }.thenBy { it.contentId })
                .take(maxReviews)
            var reviewMinutesSpent = 0

            selectedReviews.forEach { review ->
                val estimated = 2 // 2 minutes per review card
                val title = resolveContentTitle(review.contentId, input)
                val item = TodayPlanItem(
                    id = "${planId}_rev_${review.contentId}",
                    planId = planId,
                    contentId = review.contentId,
                    title = title,
                    domain = review.domain,
                    itemType = TodayItemType.REVIEW_DUE,
                    estimatedMinutes = estimated,
                    targetCefrLevel = userCurrentCefr,
                    explanation = "Spaced repetition review due today (interval: ${review.intervalDays.toInt()}d, repetition #${review.repetitionNumber + 1})."
                )
                plannedItems.add(item)
                reviewMinutesSpent += estimated
            }
            remainingMinutes -= reviewMinutesSpent
            rationalePoints.add("${selectedReviews.size} due spaced review${if (selectedReviews.size > 1) "s" else ""} for retention")
        }

        // 2. Targeted Mistake Remediation (Priority 2: Fix Recurring Traps)
        if (input.activeMistakes.isNotEmpty() && remainingMinutes >= 4) {
            val prioritizedMistake = input.activeMistakes
                .sortedWith(
                    compareByDescending<MistakeRecord> { it.stage == MistakeStage.RECURRING }
                        .thenByDescending { it.stage == MistakeStage.TARGETED }
                        .thenByDescending { it.stage == MistakeStage.POSSIBLE }
                        .thenByDescending { it.occurrenceCount }
                )
                .firstOrNull()

            if (prioritizedMistake != null) {
                val estimated = 4
                val title = resolveContentTitle(prioritizedMistake.contentId, input).ifBlank {
                    prioritizedMistake.trapType.replace("-", " ").replace("_", " ").replaceFirstChar { it.uppercase() }
                }
                val explanation = "Targeted drill for recurring trap: '${prioritizedMistake.trapType}'. Error: '${prioritizedMistake.userAnswer}' -> '${prioritizedMistake.correctAnswer}'."
                val domain = if (prioritizedMistake.contentId.startsWith("vocab")) "vocabulary" else "grammar"
                val item = TodayPlanItem(
                    id = "${planId}_mistake_${prioritizedMistake.id}",
                    planId = planId,
                    contentId = prioritizedMistake.contentId,
                    title = title,
                    domain = domain,
                    itemType = TodayItemType.MISTAKE_TARGETED,
                    estimatedMinutes = estimated,
                    targetCefrLevel = userCurrentCefr,
                    explanation = explanation
                )
                plannedItems.add(item)
                remainingMinutes -= estimated
                rationalePoints.add("1 targeted drill on your recurring trap '${prioritizedMistake.trapType}'")
            }
        }

        // 3. Active Production / Speaking Practice (Priority 3: Speaking Coach)
        if (remainingMinutes >= 8 && input.availableSpeaking.isNotEmpty()) {
            val alreadyPlannedIds = plannedItems.map { it.contentId }.toSet()
            val candidateScenarios = input.availableSpeaking.filter { it.id !in alreadyPlannedIds }
            val scenario = candidateScenarios.find { candidate ->
                candidate.cefrLevel == userTargetCefr && userProfile.learningInterests.any { interest ->
                    candidate.topicTags.contains(interest) || candidate.title.contains(interest, ignoreCase = true)
                }
            } ?: candidateScenarios.find { it.cefrLevel == userTargetCefr }
              ?: candidateScenarios.find { it.cefrLevel == userCurrentCefr }
              ?: candidateScenarios.firstOrNull()

            if (scenario != null) {
                val estimated = 8
                val item = TodayPlanItem(
                    id = "${planId}_speak_${scenario.id}",
                    planId = planId,
                    contentId = scenario.id,
                    title = scenario.title,
                    domain = "speaking",
                    itemType = TodayItemType.SPEAKING_SESSION,
                    estimatedMinutes = estimated,
                    targetCefrLevel = scenario.cefrLevel,
                    explanation = "Live speaking practice in ${scenario.category.displayName} (${scenario.cefrLevel}) focused on active oral fluency."
                )
                plannedItems.add(item)
                remainingMinutes -= estimated
                rationalePoints.add("a ${scenario.category.displayName} speaking session ('${scenario.title}')")
            }
        }

        // 4. Curriculum Progression & Weak Skill Reinforcement (Priority 4: Next Learn Steps)
        val masteredContentIds = input.masterySnapshots
            .filter { it.status == MasteryStatus.MASTERED || it.status == MasteryStatus.MAINTAINING }
            .map { it.contentId }.toSet()
        val alreadyPlannedIds = plannedItems.map { it.contentId }.toMutableSet()

        // Use a real seven-day window ending on the requested plan date.
        val planDateEnd = LocalDate.parse(input.targetDate)
            .plusDays(1)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli() - 1
        val weekStart = planDateEnd - 7L * 24L * 60L * 60L * 1000L
        val recentDomains = input.recentEvidence
            .filter { it.timestamp in weekStart..planDateEnd }
            .map { it.domain }
        val grammarCount = recentDomains.count { it == "grammar" }
        val vocabCount = recentDomains.count { it == "vocabulary" }
        val readingCount = recentDomains.count { it == "reading" }
        val listeningCount = recentDomains.count { it == "listening" }

        // Domain selection priority favoring least recently practiced domain
        val recentCounts = mapOf(
            "grammar" to grammarCount,
            "vocabulary" to vocabCount,
            "reading" to readingCount,
            "listening" to listeningCount
        )
        val profileLevels = mapOf(
            "grammar" to userProfile.estimatedGrammarLevel,
            "vocabulary" to userProfile.estimatedVocabLevel,
            "reading" to userProfile.estimatedReadingLevel,
            "listening" to userProfile.estimatedListeningLevel
        )
        val candidateDomains = listOf("grammar", "vocabulary", "reading", "listening")
            .sortedWith(compareBy<String> { domain ->
                val mastery = input.masterySnapshots
                    .filter { it.domain.equals(domain, ignoreCase = true) }
                    .map { it.level }
                    .average()
                    .takeUnless { it.isNaN() } ?: 0.5
                val levelRank = listOf("A2", "B1", "B2", "C1", "C2")
                    .indexOf(profileLevels[domain]).coerceAtLeast(0)
                (recentCounts[domain] ?: 0) * 10 + mastery * 10 + levelRank
            }.thenBy { it })

        for (domain in candidateDomains) {
            if (remainingMinutes < 3 || plannedItems.size >= 5) break
            val skillLevel = profileLevels[domain] ?: userCurrentCefr

            when (domain) {
                "grammar" -> {
                    val eligible = input.availableGrammar.filter {
                        it.id !in masteredContentIds && it.id !in alreadyPlannedIds &&
                        (it.cefrLevel == skillLevel || it.cefrLevel == userTargetCefr || it.cefrLevel == userCurrentCefr)
                    }.sortedBy { if (it.cefrLevel == skillLevel) 0 else 1 }
                    val nextLesson = eligible.firstOrNull { lesson ->
                        lesson.topicTags.any { it in userProfile.learningInterests }
                    } ?: eligible.firstOrNull()
                        ?: input.availableGrammar.firstOrNull { it.id !in alreadyPlannedIds }

                    if (nextLesson != null && remainingMinutes >= 5) {
                        val estimated = 5
                        plannedItems.add(
                            TodayPlanItem(
                                id = "${planId}_gram_${nextLesson.id}",
                                planId = planId,
                                contentId = nextLesson.id,
                                title = nextLesson.title,
                                domain = "grammar",
                                itemType = TodayItemType.CURRICULUM_NEW,
                                estimatedMinutes = estimated,
                                targetCefrLevel = nextLesson.cefrLevel,
                                explanation = "Curriculum progression: Master new ${nextLesson.cefrLevel} grammar structure '${nextLesson.title}'."
                            )
                        )
                        alreadyPlannedIds.add(nextLesson.id)
                        remainingMinutes -= estimated
                        rationalePoints.add("1 grammar concept ('${nextLesson.title}')")
                    }
                }
                "vocabulary" -> {
                    val eligible = input.availableVocab.filter {
                        it.id !in masteredContentIds && it.id !in alreadyPlannedIds &&
                        (it.cefrLevel == skillLevel || it.cefrLevel == userTargetCefr || it.cefrLevel == userCurrentCefr)
                    }.sortedBy { if (it.cefrLevel == skillLevel) 0 else 1 }
                    val nextVocab = eligible.firstOrNull { vocab ->
                        vocab.topicTags.any { it in userProfile.learningInterests }
                    } ?: eligible.firstOrNull()
                        ?: input.availableVocab.firstOrNull { it.id !in alreadyPlannedIds }

                    if (nextVocab != null && remainingMinutes >= 3) {
                        val estimated = 3
                        plannedItems.add(
                            TodayPlanItem(
                                id = "${planId}_voc_${nextVocab.id}",
                                planId = planId,
                                contentId = nextVocab.id,
                                title = nextVocab.headword,
                                domain = "vocabulary",
                                itemType = TodayItemType.CURRICULUM_NEW,
                                estimatedMinutes = estimated,
                                targetCefrLevel = nextVocab.cefrLevel,
                                explanation = "Curriculum progression: Acquire high-impact ${nextVocab.cefrLevel} vocabulary '${nextVocab.headword}'."
                            )
                        )
                        alreadyPlannedIds.add(nextVocab.id)
                        remainingMinutes -= estimated
                        rationalePoints.add("1 vocabulary acquisition step ('${nextVocab.headword}')")
                    }
                }
                "reading" -> {
                    val eligible = input.availableReadings.filter {
                        it.id !in masteredContentIds && it.id !in alreadyPlannedIds &&
                        (it.cefrLevel == skillLevel || it.cefrLevel == userTargetCefr || it.cefrLevel == userCurrentCefr)
                    }.sortedBy { if (it.cefrLevel == skillLevel) 0 else 1 }
                    val nextArticle = eligible.firstOrNull { article ->
                        article.topicTags.any { it in userProfile.learningInterests }
                    } ?: eligible.firstOrNull()
                        ?: input.availableReadings.firstOrNull { it.id !in alreadyPlannedIds }

                    if (nextArticle != null && remainingMinutes >= 6) {
                        val estimated = 6
                        plannedItems.add(
                            TodayPlanItem(
                                id = "${planId}_read_${nextArticle.id}",
                                planId = planId,
                                contentId = nextArticle.id,
                                title = nextArticle.title,
                                domain = "reading",
                                itemType = TodayItemType.COMPREHENSION_READING,
                                estimatedMinutes = estimated,
                                targetCefrLevel = nextArticle.cefrLevel,
                                explanation = "Reading comprehension: Deep-dive article '${nextArticle.title}' (${nextArticle.cefrLevel})."
                            )
                        )
                        alreadyPlannedIds.add(nextArticle.id)
                        remainingMinutes -= estimated
                        rationalePoints.add("1 reading comprehension article")
                    }
                }
                "listening" -> {
                    val eligible = input.availableListening.filter {
                        it.id !in masteredContentIds && it.id !in alreadyPlannedIds &&
                        (it.cefrLevel == skillLevel || it.cefrLevel == userTargetCefr || it.cefrLevel == userCurrentCefr)
                    }.sortedBy { if (it.cefrLevel == skillLevel) 0 else 1 }
                    val nextListening = eligible.firstOrNull { listening ->
                        listening.topicTags.any { it in userProfile.learningInterests }
                    } ?: eligible.firstOrNull()
                        ?: input.availableListening.firstOrNull { it.id !in alreadyPlannedIds }

                    if (nextListening != null && remainingMinutes >= 5) {
                        val estimated = 5
                        plannedItems.add(
                            TodayPlanItem(
                                id = "${planId}_list_${nextListening.id}",
                                planId = planId,
                                contentId = nextListening.id,
                                title = nextListening.title,
                                domain = "listening",
                                itemType = TodayItemType.COMPREHENSION_LISTENING,
                                estimatedMinutes = estimated,
                                targetCefrLevel = nextListening.cefrLevel,
                                explanation = "Listening comprehension: Workplace dialogue '${nextListening.title}' (${nextListening.cefrLevel})."
                            )
                        )
                        alreadyPlannedIds.add(nextListening.id)
                        remainingMinutes -= estimated
                        rationalePoints.add("1 workplace listening dialogue")
                    }
                }
            }
        }

        // Fallback safety net if somehow no items were selected
        if (plannedItems.isEmpty()) {
            val fallbackVocab = input.availableVocab.firstOrNull()
            if (fallbackVocab != null) {
                plannedItems.add(
                    TodayPlanItem(
                        id = "${planId}_voc_fallback",
                        planId = planId,
                        contentId = fallbackVocab.id,
                        title = fallbackVocab.headword,
                        domain = "vocabulary",
                        itemType = TodayItemType.CURRICULUM_NEW,
                        estimatedMinutes = 5,
                        targetCefrLevel = fallbackVocab.cefrLevel,
                        explanation = "Daily vocabulary foundation: '${fallbackVocab.headword}'."
                    )
                )
                rationalePoints.add("daily vocabulary foundation")
            }
        }

        val rationale = if (rationalePoints.isNotEmpty()) {
            "Today's plan is personalized for your ${userTargetCefr} goal: " + rationalePoints.joinToString(", ") + "."
        } else {
            "Today's curated learning practice session."
        }

        return TodayPracticePlan(
            id = planId,
            date = input.targetDate,
            targetCefrLevel = userTargetCefr,
            allocatedMinutes = budgetMinutes,
            rationale = rationale,
            items = plannedItems.mapIndexed { index, item -> item.copy(orderIndex = index) },
            createdAt = LocalDate.parse(input.targetDate)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )
    }

    private fun resolveContentTitle(contentId: String, input: TodayPlannerInput): String {
        return input.availableVocab.find { it.id == contentId }?.headword
            ?: input.availableGrammar.find { it.id == contentId }?.title
            ?: input.availableSpeaking.find { it.id == contentId }?.title
            ?: input.availableReadings.find { it.id == contentId }?.title
            ?: input.availableListening.find { it.id == contentId }?.title
            ?: contentId.substringAfterLast(".").replace("-", " ").replaceFirstChar { it.uppercase() }
    }
}
