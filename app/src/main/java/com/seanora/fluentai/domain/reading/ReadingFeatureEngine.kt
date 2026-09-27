package com.seanora.fluentai.domain.reading

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.ReadingArticle
import com.seanora.fluentai.core.model.ReadingComprehensionQuestion
import com.seanora.fluentai.core.model.ReviewSchedule
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

data class ReadingSkillItem(
    val article: ReadingArticle,
    val question: ReadingComprehensionQuestion,
)

data class ReadingSkill(
    val id: String,
    val title: String,
    val items: List<ReadingSkillItem>,
)

data class ReadingWeakSpot(
    val article: ReadingArticle,
    val mastery: MasterySnapshot?,
    val isReviewDue: Boolean,
)

open class ReadingDateProvider @Inject constructor() {
    open fun today(): String = LocalDate.now().toString()
}

@Singleton
class ReadingFeatureEngine @Inject constructor() {
    fun selectDaily(
        articles: List<ReadingArticle>,
        preferredLevel: String?,
        localDate: String,
    ): ReadingArticle? {
        if (articles.isEmpty()) return null
        val availableLevels = articles.map { it.cefrLevel.uppercase() }.toSet()
        val level = nearestLevel(preferredLevel, availableLevels)
        val candidates = articles.filter { it.cefrLevel.equals(level, true) }.sortedBy { it.id }
            .ifEmpty { articles.sortedBy { it.id } }
        val hash = "$localDate|${level.orEmpty()}".hashCode() and Int.MAX_VALUE
        return candidates[hash % candidates.size]
    }

    fun resolveContinue(
        articles: List<ReadingArticle>,
        recentEvidence: List<LearningEvidence>,
    ): ReadingArticle? {
        val byId = articles.associateBy { it.id }
        return recentEvidence.asSequence()
            .filter { it.domain.equals("reading", true) }
            .sortedByDescending { it.timestamp }
            .mapNotNull { byId[it.contentId] }
            .firstOrNull()
    }

    fun guidedArticles(articles: List<ReadingArticle>): List<ReadingArticle> =
        articles.filter { it.paragraphs.isNotEmpty() }.sortedWith(compareBy({ cefrIndex(it.cefrLevel) }, { it.title }))

    fun deriveSkills(articles: List<ReadingArticle>): List<ReadingSkill> {
        val items = articles.flatMap { article ->
            article.comprehensionQuestions.map { ReadingSkillItem(article, it) }
        }
        return if (items.isEmpty()) emptyList()
        else listOf(ReadingSkill("comprehension", "Comprehension", items))
    }

    fun weakSpots(
        articles: List<ReadingArticle>,
        snapshots: List<MasterySnapshot>,
        dueReviews: List<ReviewSchedule>,
        now: Long,
    ): List<ReadingWeakSpot> {
        val byId = articles.associateBy { it.id }
        val masteryById = snapshots.filter {
            it.domain.equals("reading", true) && it.contentId in byId && it.totalAttempts > 0
        }.associateBy { it.contentId }
        val dueIds = dueReviews.filter {
            it.domain.equals("reading", true) && it.contentId in byId && it.nextReviewDueTimestamp <= now
        }.mapTo(mutableSetOf()) { it.contentId }
        return articles.mapNotNull { article ->
            val mastery = masteryById[article.id]
            val due = article.id in dueIds
            val needsPractice = mastery != null && mastery.status !in setOf(MasteryStatus.MASTERED, MasteryStatus.MAINTAINING)
            if (!needsPractice && !due) null else ReadingWeakSpot(article, mastery, due)
        }.sortedWith(
            compareByDescending<ReadingWeakSpot> { it.isReviewDue }
                .thenBy { it.mastery?.level ?: 1f }
                .thenBy { it.article.title },
        )
    }

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
