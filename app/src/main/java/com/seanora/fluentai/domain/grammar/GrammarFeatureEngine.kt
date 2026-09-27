package com.seanora.fluentai.domain.grammar

import com.seanora.fluentai.core.model.GrammarFeatureState
import com.seanora.fluentai.core.model.GrammarLesson
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.MistakeStage
import com.seanora.fluentai.core.model.ReviewSchedule
import javax.inject.Inject
import javax.inject.Singleton

data class SentenceBuilderQuestion(
    val id: String,
    val lessonId: String,
    val lessonTitle: String,
    val correctSentence: String,
    val correctTokens: List<String>,
    val scrambledTokens: List<String>,
    val explanation: String,
)

data class ErrorSpotterQuestion(
    val id: String,
    val lessonId: String,
    val lessonTitle: String,
    val trapType: String,
    val trapTitle: String,
    val incorrectSentence: String,
    val correctSentence: String,
    val explanation: String,
)

data class GrammarWeakSpot(
    val lesson: GrammarLesson,
    val mastery: MasterySnapshot?,
    val mistakeCount: Int,
    val isReviewDue: Boolean,
)

@Singleton
class GrammarFeatureEngine @Inject constructor() {

    fun buildSentenceBuilders(lessons: List<GrammarLesson>): List<SentenceBuilderQuestion> =
        lessons.sortedBy { it.id }.flatMap { lesson ->
            lesson.examples.mapIndexedNotNull { index, example ->
                val sentence = example.en.trim()
                val tokens = tokenize(sentence)
                if (tokens.size !in 4..24 || tokens.distinct().size < 3) return@mapIndexedNotNull null
                val shift = positiveHash("${lesson.id}:$index") % (tokens.size - 1) + 1
                val scrambled = tokens.drop(shift) + tokens.take(shift)
                if (scrambled == tokens) return@mapIndexedNotNull null
                SentenceBuilderQuestion(
                    id = "${lesson.id}:example:$index",
                    lessonId = lesson.id,
                    lessonTitle = lesson.title,
                    correctSentence = sentence,
                    correctTokens = tokens,
                    scrambledTokens = scrambled,
                    explanation = lesson.summaryEn,
                )
            }
        }

    fun evaluateSentence(question: SentenceBuilderQuestion, answer: List<String>): Boolean =
        answer == question.correctTokens

    fun buildErrorSpotters(lessons: List<GrammarLesson>): List<ErrorSpotterQuestion> =
        lessons.sortedBy { it.id }.flatMap { lesson ->
            lesson.turkishTraps.mapIndexedNotNull { index, trap ->
                val incorrect = trap.incorrectExample.trim()
                val correct = trap.correctExample.trim()
                if (incorrect.isBlank() || correct.isBlank() || incorrect == correct) return@mapIndexedNotNull null
                ErrorSpotterQuestion(
                    id = "${lesson.id}:trap:$index",
                    lessonId = lesson.id,
                    lessonTitle = lesson.title,
                    trapType = trap.trapType,
                    trapTitle = trap.trapTitle,
                    incorrectSentence = incorrect,
                    correctSentence = correct,
                    explanation = trap.explanationTr,
                )
            }
        }

    fun rankWeakSpots(
        lessons: List<GrammarLesson>,
        mastery: List<MasterySnapshot>,
        mistakes: List<MistakeRecord>,
        dueReviews: List<ReviewSchedule>,
        now: Long,
    ): List<GrammarWeakSpot> {
        val lessonById = lessons.associateBy { it.id }
        val grammarMastery = mastery.filter { it.domain.equals("grammar", true) && it.contentId in lessonById }
            .associateBy { it.contentId }
        val activeMistakes = mistakes.filter { it.stage != MistakeStage.RESOLVED && it.contentId in lessonById }
            .groupBy { it.contentId }
        val dueIds = dueReviews.filter {
            it.domain.equals("grammar", true) && it.nextReviewDueTimestamp <= now && it.contentId in lessonById
        }.mapTo(mutableSetOf()) { it.contentId }

        return lessons.mapNotNull { lesson ->
            val snapshot = grammarMastery[lesson.id]
            val count = activeMistakes[lesson.id].orEmpty().sumOf { it.occurrenceCount }
            val due = lesson.id in dueIds
            val supportedLowMastery = snapshot != null && snapshot.totalAttempts > 0 &&
                snapshot.status !in setOf(MasteryStatus.MASTERED, MasteryStatus.MAINTAINING)
            if (!supportedLowMastery && count == 0 && !due) null
            else GrammarWeakSpot(lesson, snapshot, count, due)
        }.sortedWith(
            compareByDescending<GrammarWeakSpot> { it.mistakeCount }
                .thenByDescending { it.isReviewDue }
                .thenBy { it.mastery?.level ?: 1f }
                .thenBy { it.lesson.title },
        )
    }

    fun comparisonCandidates(primaryId: String, lessons: List<GrammarLesson>): List<GrammarLesson> {
        val primary = lessons.firstOrNull { it.id == primaryId } ?: return emptyList()
        return lessons.asSequence()
            .filter { it.id != primaryId }
            .sortedWith(
                compareByDescending<GrammarLesson> { candidate ->
                    var score = 0
                    if (candidate.id in primary.relatedIds || primary.id in candidate.relatedIds) score += 100
                    if (candidate.category == primary.category) score += 20
                    score += candidate.topicTags.intersect(primary.topicTags.toSet()).size * 10
                    if (candidate.cefrLevel == primary.cefrLevel) score += 5
                    score
                }.thenBy { it.title },
            )
            .toList()
    }

    fun resolveResume(state: GrammarFeatureState, validLessonIds: Set<String>): GrammarFeatureState? {
        if (state.resumeDestination == null) return null
        val contentId = state.resumeContentId ?: return null
        if (contentId !in validLessonIds) return null
        val secondary = state.resumeSecondaryContentId
        if (secondary != null && secondary !in validLessonIds) return null
        return state
    }

    private fun tokenize(sentence: String): List<String> =
        TOKEN_REGEX.findAll(sentence).map { it.value }.toList()

    private fun positiveHash(value: String): Int = value.hashCode() and Int.MAX_VALUE

    private companion object {
        val TOKEN_REGEX = Regex("""[\p{L}\p{N}]+(?:['’][\p{L}\p{N}]+)*|[^\s]""")
    }
}
