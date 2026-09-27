package com.seanora.fluentai.domain.vocabulary

import com.seanora.fluentai.core.model.VocabItem
import com.seanora.fluentai.core.model.VocabularyPracticeMode
import java.util.Random
import javax.inject.Inject

data class VocabularyPracticeQuestion(
    val id: String,
    val targetContentId: String,
    val prompt: String,
    val options: List<String>,
    val correctAnswer: String,
)

data class VocabularyPracticeSession(
    val mode: VocabularyPracticeMode,
    val questions: List<VocabularyPracticeQuestion>,
    val unavailableReason: String? = null,
    val timeLimitSeconds: Int? = if (mode == VocabularyPracticeMode.SPEED_DRILL) 10 else null,
)

class VocabularyPracticeEngine @Inject constructor() {
    fun buildSession(
        mode: VocabularyPracticeMode,
        items: List<VocabItem>,
        localDate: String,
        limit: Int = 10,
    ): VocabularyPracticeSession {
        val sorted = items.distinctBy { it.id }.sortedBy { it.id }
        val eligible = sorted.filter { it.isEligibleFor(mode) }
        if (eligible.size < 2) {
            val field = if (mode == VocabularyPracticeMode.COLLOCATION_MATCH) "collocation" else "eligible content"
            return VocabularyPracticeSession(mode, emptyList(), "Not enough real $field content is available for this mode.")
        }

        val seed = "$localDate|${mode.name}|${eligible.joinToString { it.id }}".hashCode().toLong()
        val ordered = eligible.shuffled(Random(seed)).take(limit)
        val questions = ordered.mapIndexedNotNull { index, target ->
            buildQuestion(mode, target, eligible, seed + index)
        }
        return if (questions.isEmpty()) {
            VocabularyPracticeSession(mode, emptyList(), "Not enough real content is available for this mode.")
        } else {
            VocabularyPracticeSession(mode, questions)
        }
    }

    fun evaluateAnswer(question: VocabularyPracticeQuestion, answer: String): Boolean =
        question.correctAnswer.trim().equals(answer.trim(), ignoreCase = true)

    private fun buildQuestion(
        mode: VocabularyPracticeMode,
        target: VocabItem,
        pool: List<VocabItem>,
        seed: Long,
    ): VocabularyPracticeQuestion? {
        val example = target.examples.firstOrNull { it.en.contains(target.headword, ignoreCase = true) }
        val collocation = target.collocations.firstOrNull()?.text
        val sameLevelAndPart = pool.filter {
            it.cefrLevel.equals(target.cefrLevel, true) && it.partOfSpeech.equals(target.partOfSpeech, true)
        }
        val sameLevel = pool.filter { it.cefrLevel.equals(target.cefrLevel, true) }
        val distractorPool = when {
            sameLevelAndPart.size >= 2 -> sameLevelAndPart
            sameLevel.size >= 2 -> sameLevel
            else -> pool
        }
        val (prompt, correct, choices) = when (mode) {
            VocabularyPracticeMode.WORD_MATCH,
            VocabularyPracticeMode.SPEED_DRILL -> Triple(
                target.headword,
                target.definitionEn,
                distractorPool.map { it.definitionEn },
            )
            VocabularyPracticeMode.MEANING_MATCH -> Triple(
                target.definitionEn,
                target.headword,
                distractorPool.map { it.headword },
            )
            VocabularyPracticeMode.FILL_IN_THE_BLANK -> {
                val source = example?.en ?: return null
                val blanked = source.replaceFirst(Regex(Regex.escape(target.headword), RegexOption.IGNORE_CASE), "_____")
                Triple(blanked, target.headword, distractorPool.map { it.headword })
            }
            VocabularyPracticeMode.CONTEXT_CHOICE -> Triple(
                example?.en ?: return null,
                target.headword,
                distractorPool.map { it.headword },
            )
            VocabularyPracticeMode.COLLOCATION_MATCH -> Triple(
                "Choose a collocation for “${target.headword}”.",
                collocation ?: return null,
                distractorPool.mapNotNull { it.collocations.firstOrNull()?.text },
            )
        }
        val options = choices.distinct().filter { it != correct }.shuffled(Random(seed)).take(3).plus(correct)
            .shuffled(Random(seed xor 0x5DEECE66DL))
        if (options.size < 2) return null
        return VocabularyPracticeQuestion(
            id = "${mode.name.lowercase()}:${target.id}",
            targetContentId = target.id,
            prompt = prompt,
            options = options,
            correctAnswer = correct,
        )
    }

    private fun VocabItem.isEligibleFor(mode: VocabularyPracticeMode): Boolean = when (mode) {
        VocabularyPracticeMode.FILL_IN_THE_BLANK,
        VocabularyPracticeMode.CONTEXT_CHOICE -> examples.any { it.en.contains(headword, ignoreCase = true) }
        VocabularyPracticeMode.COLLOCATION_MATCH -> collocations.isNotEmpty()
        else -> headword.isNotBlank() && definitionEn.isNotBlank()
    }
}
