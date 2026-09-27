package com.seanora.fluentai.domain.listening

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.ListeningComprehensionQuestion
import com.seanora.fluentai.core.model.ListeningScenario
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.TranscriptItem
import java.time.LocalDate
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

data class DictationItem(val scenario: ListeningScenario, val transcript: TranscriptItem)

data class DictationResult(
    val isCorrect: Boolean,
    val matchedTokens: Int,
    val totalTokens: Int,
    val normalizedAnswer: String,
    val normalizedExpected: String,
)

data class ListeningSkillItem(
    val scenario: ListeningScenario,
    val question: ListeningComprehensionQuestion,
)

data class ListeningSkill(val id: String, val title: String, val items: List<ListeningSkillItem>)

data class ListeningWeakSpot(
    val scenario: ListeningScenario,
    val mastery: MasterySnapshot?,
    val isReviewDue: Boolean,
)

open class ListeningDateProvider @Inject constructor() {
    open fun today(): String = LocalDate.now().toString()
}

@Singleton
class ListeningFeatureEngine @Inject constructor() {
    fun selectDaily(scenarios: List<ListeningScenario>, preferredLevel: String?, localDate: String): ListeningScenario? {
        if (scenarios.isEmpty()) return null
        val available = scenarios.map { it.cefrLevel.uppercase() }.toSet()
        val level = nearestLevel(preferredLevel, available)
        val candidates = scenarios.filter { it.cefrLevel.equals(level, true) }.sortedBy { it.id }
            .ifEmpty { scenarios.sortedBy { it.id } }
        val hash = "$localDate|${level.orEmpty()}".hashCode() and Int.MAX_VALUE
        return candidates[hash % candidates.size]
    }

    fun resolveContinue(scenarios: List<ListeningScenario>, recentEvidence: List<LearningEvidence>): ListeningScenario? {
        val byId = scenarios.associateBy { it.id }
        return recentEvidence.asSequence()
            .filter { it.domain.equals("listening", true) }
            .sortedByDescending { it.timestamp }
            .mapNotNull { byId[it.contentId] }
            .firstOrNull()
    }

    fun dictationItems(scenarios: List<ListeningScenario>): List<DictationItem> =
        scenarios.sortedBy { it.id }.flatMap { scenario ->
            if (scenario.audioRef.isBlank()) emptyList()
            else scenario.transcriptItems.sortedBy { it.index }.mapNotNull { item ->
                item.takeIf { it.textEn.isNotBlank() && it.startMs >= 0L && it.endMs > it.startMs }
                    ?.let { DictationItem(scenario, it) }
            }
        }

    fun evaluateDictation(answer: String, expected: String): DictationResult {
        val normalizedAnswer = normalize(answer)
        val normalizedExpected = normalize(expected)
        val expectedTokens = normalizedExpected.split(' ').filter { it.isNotBlank() }
        val counts = expectedTokens.groupingBy { it }.eachCount().toMutableMap()
        val matched = normalizedAnswer.split(' ').filter { it.isNotBlank() }.count { token ->
            val remaining = counts[token] ?: 0
            if (remaining > 0) {
                counts[token] = remaining - 1
                true
            } else false
        }
        return DictationResult(
            isCorrect = normalizedAnswer.isNotBlank() && normalizedAnswer == normalizedExpected,
            matchedTokens = matched,
            totalTokens = expectedTokens.size,
            normalizedAnswer = normalizedAnswer,
            normalizedExpected = normalizedExpected,
        )
    }

    fun deriveSkills(scenarios: List<ListeningScenario>): List<ListeningSkill> {
        val items = scenarios.flatMap { scenario ->
            scenario.comprehensionQuestions.map { ListeningSkillItem(scenario, it) }
        }
        return if (items.isEmpty()) emptyList()
        else listOf(ListeningSkill("comprehension", "Comprehension", items))
    }

    fun weakSpots(
        scenarios: List<ListeningScenario>,
        snapshots: List<MasterySnapshot>,
        dueReviews: List<ReviewSchedule>,
        now: Long,
    ): List<ListeningWeakSpot> {
        val byId = scenarios.associateBy { it.id }
        val masteryById = snapshots.filter {
            it.domain.equals("listening", true) && it.contentId in byId && it.totalAttempts > 0
        }.associateBy { it.contentId }
        val dueIds = dueReviews.filter {
            it.domain.equals("listening", true) && it.contentId in byId && it.nextReviewDueTimestamp <= now
        }.mapTo(mutableSetOf()) { it.contentId }
        return scenarios.mapNotNull { scenario ->
            val mastery = masteryById[scenario.id]
            val due = scenario.id in dueIds
            val needsPractice = mastery != null && mastery.status !in setOf(MasteryStatus.MASTERED, MasteryStatus.MAINTAINING)
            if (!needsPractice && !due) null else ListeningWeakSpot(scenario, mastery, due)
        }.sortedWith(
            compareByDescending<ListeningWeakSpot> { it.isReviewDue }
                .thenBy { it.mastery?.level ?: 1f }
                .thenBy { it.scenario.title },
        )
    }

    private fun normalize(value: String): String = value
        .lowercase(Locale.ROOT)
        .replace(Regex("[^\\p{L}\\p{N}]+"), " ")
        .trim()
        .replace(Regex("\\s+"), " ")

    private fun nearestLevel(preferred: String?, available: Set<String>): String? {
        if (available.isEmpty()) return null
        val normalized = preferred?.uppercase()
        if (normalized in available) return normalized
        val preferredIndex = CEFR_LEVELS.indexOf(normalized).takeIf { it >= 0 }
            ?: return CEFR_LEVELS.firstOrNull { it in available } ?: available.sorted().first()
        return available.minWithOrNull(
            compareBy<String> { kotlin.math.abs(cefrIndex(it) - preferredIndex) }
                .thenBy { cefrIndex(it) },
        )
    }

    private fun cefrIndex(level: String): Int = CEFR_LEVELS.indexOf(level.uppercase()).takeIf { it >= 0 } ?: Int.MAX_VALUE

    private companion object {
        val CEFR_LEVELS = listOf("A2", "B1", "B2", "C1", "C2")
    }
}
