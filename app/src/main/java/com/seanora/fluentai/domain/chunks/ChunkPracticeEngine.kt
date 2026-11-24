package com.seanora.fluentai.domain.chunks

import com.seanora.fluentai.core.model.BlueprintSlot
import com.seanora.fluentai.core.model.ChunkDrillMode
import com.seanora.fluentai.core.model.ChunkDrillQuestion
import com.seanora.fluentai.core.model.LexicalChunk
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class ChunkPracticeEngine @Inject constructor() {

    fun selectDailyPack(chunks: List<LexicalChunk>, daySeed: Long, count: Int = 3): List<LexicalChunk> {
        if (chunks.isEmpty()) return emptyList()
        val sorted = chunks.sortedBy { it.id }
        val startIndex = ((daySeed % sorted.size) + sorted.size).toInt() % sorted.size
        return List(count.coerceAtMost(sorted.size)) { offset ->
            sorted[(startIndex + offset) % sorted.size]
        }
    }

    fun buildSession(
        chunks: List<LexicalChunk>,
        mode: ChunkDrillMode = ChunkDrillMode.MIXED,
        focusChunkId: String? = null,
        maxQuestions: Int = 8,
    ): List<ChunkDrillQuestion> {
        if (chunks.isEmpty()) return emptyList()
        val orderedPool = chunks.sortedBy { it.id }
        val targets = if (focusChunkId != null) {
            val focused = orderedPool.firstOrNull { it.id == focusChunkId } ?: return emptyList()
            listOf(focused)
        } else {
            orderedPool.take(maxQuestions)
        }

        val questions = when (mode) {
            ChunkDrillMode.TURKISH_INTENT_MATCH -> targets.mapNotNull { buildIntentChoice(it, orderedPool) }
            ChunkDrillMode.TRAP_BUSTER -> targets.mapNotNull { buildTrapChoice(it) }
            ChunkDrillMode.BLUEPRINT_ASSEMBLY -> targets.mapNotNull { buildBlueprintQuestion(it) }
            ChunkDrillMode.MIXED -> {
                if (focusChunkId != null) {
                    val single = targets.first()
                    listOfNotNull(
                        buildIntentChoice(single, orderedPool),
                        buildTrapChoice(single),
                        buildBlueprintQuestion(single),
                    )
                } else {
                    targets.mapIndexedNotNull { idx, chunk ->
                        when (idx % 3) {
                            0 -> buildTrapChoice(chunk)
                            1 -> buildBlueprintQuestion(chunk) ?: buildIntentChoice(chunk, orderedPool)
                            else -> buildIntentChoice(chunk, orderedPool)
                        }
                    }
                }
            }
        }
        return questions.take(maxQuestions)
    }

    fun evaluateBlueprint(question: ChunkDrillQuestion.BlueprintAssembly, selected: List<BlueprintSlot>): Boolean =
        selected.map { it.textEn } == question.correctSlots.map { it.textEn }

    private fun buildIntentChoice(chunk: LexicalChunk, allChunks: List<LexicalChunk>): ChunkDrillQuestion.Choice? {
        val distractors = allChunks
            .filter { it.id != chunk.id && !it.chunkEn.equals(chunk.chunkEn, ignoreCase = true) }
            .sortedBy { positiveHash("${chunk.id}:${it.id}") }
            .take(3)
            .map { it.chunkEn }
        if (distractors.isEmpty()) return null

        val options = deterministicShuffle(listOf(chunk.chunkEn) + distractors, "${chunk.id}:intent")
        return ChunkDrillQuestion.Choice(
            id = "${chunk.id}:intent",
            chunkId = chunk.id,
            chunkEn = chunk.chunkEn,
            cefrLevel = chunk.cefrLevel,
            promptTitle = chunk.intentTr,
            promptSubtitle = "Pick the natural English chunk for this Turkish intent",
            options = options,
            correctAnswer = chunk.chunkEn,
            explanationTr = chunk.turkishTrap.explanationTr,
            isTrapQuestion = false,
        )
    }

    private fun buildTrapChoice(chunk: LexicalChunk): ChunkDrillQuestion.Choice? {
        val incorrect = chunk.turkishTrap.incorrectEn.trim()
        val correct = chunk.turkishTrap.correctEn.trim()
        if (incorrect.isBlank() || correct.isBlank() || incorrect == correct) return null

        val options = if (positiveHash("${chunk.id}:trap") % 2 == 0) {
            listOf(correct, incorrect)
        } else {
            listOf(incorrect, correct)
        }
        return ChunkDrillQuestion.Choice(
            id = "${chunk.id}:trap",
            chunkId = chunk.id,
            chunkEn = chunk.chunkEn,
            cefrLevel = chunk.cefrLevel,
            promptTitle = "Which sentence sounds natural in English?",
            promptSubtitle = chunk.intentTr,
            options = options,
            correctAnswer = correct,
            explanationTr = chunk.turkishTrap.explanationTr,
            isTrapQuestion = true,
        )
    }

    private fun buildBlueprintQuestion(chunk: LexicalChunk): ChunkDrillQuestion.BlueprintAssembly? {
        if (chunk.blueprintSlots.size < 3) return null
        val scrambled = deterministicShuffle(chunk.blueprintSlots, "${chunk.id}:blueprint")
        val promptTr = chunk.slotVariations.firstOrNull()?.fullSentenceTr
            ?.takeIf { it.isNotBlank() }
            ?: chunk.intentTr
        return ChunkDrillQuestion.BlueprintAssembly(
            id = "${chunk.id}:blueprint",
            chunkId = chunk.id,
            chunkEn = chunk.chunkEn,
            cefrLevel = chunk.cefrLevel,
            promptTr = promptTr,
            correctSlots = chunk.blueprintSlots,
            scrambledSlots = scrambled,
            explanationTr = chunk.turkishTrap.explanationTr,
        )
    }

    private fun <T> deterministicShuffle(items: List<T>, seedKey: String): List<T> {
        if (items.size <= 1) return items
        val baseSeed = positiveHash(seedKey).toLong()
        for (attempt in 0 until 6) {
            val candidate = items.shuffled(Random(baseSeed + attempt * 31L))
            if (candidate != items) return candidate
        }
        return items.reversed()
    }

    private fun positiveHash(value: String): Int = value.hashCode() and Int.MAX_VALUE
}
