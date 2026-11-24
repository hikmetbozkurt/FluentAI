package com.seanora.fluentai.domain.chunks

import com.seanora.fluentai.core.model.BlueprintSlot
import com.seanora.fluentai.core.model.BlueprintSlotRole
import com.seanora.fluentai.core.model.ChunkCategory
import com.seanora.fluentai.core.model.ChunkDrillMode
import com.seanora.fluentai.core.model.ChunkDrillQuestion
import com.seanora.fluentai.core.model.ChunkSlotVariation
import com.seanora.fluentai.core.model.ChunkTurkishTrap
import com.seanora.fluentai.core.model.LexicalChunk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ChunkPracticeEngineTest {

    private val engine = ChunkPracticeEngine()

    private fun sampleChunks(): List<LexicalChunk> = listOf(
        LexicalChunk(
            id = "chunk.a2.collocation.make-a-decision",
            chunkEn = "make a decision",
            patternTemplate = "[Subject] + make a decision + [about Topic]",
            cefrLevel = "A2",
            category = ChunkCategory.ACTION_COLLOCATION,
            intentTr = "Bir konuda karar vermek",
            meaningEn = "Choose one option after thinking.",
            turkishTrap = ChunkTurkishTrap(
                incorrectEn = "We need to give a decision today.",
                correctEn = "We need to make a decision today.",
                explanationTr = "'give a decision' yerine 'make a decision' kullanılır.",
                trapType = "collocation_transfer",
            ),
            blueprintSlots = listOf(
                BlueprintSlot(BlueprintSlotRole.SUBJECT, "Who", "We", "Biz"),
                BlueprintSlot(BlueprintSlotRole.VERB_BRIDGE, "Verb bridge", "need to make a decision", "karar vermeliyiz"),
                BlueprintSlot(BlueprintSlotRole.OBJECT, "About", "about the budget", "bütçe hakkında"),
            ),
            slotVariations = listOf(
                ChunkSlotVariation("about the budget", "We need to make a decision about the budget.", "Bütçe hakkında karar vermeliyiz."),
            ),
        ),
        LexicalChunk(
            id = "chunk.a2.collocation.have-a-meeting",
            chunkEn = "have a meeting",
            patternTemplate = "[Subject] + have a meeting + [at Time]",
            cefrLevel = "A2",
            category = ChunkCategory.ACTION_COLLOCATION,
            intentTr = "Toplantı yapmak",
            meaningEn = "Gather to discuss work.",
            turkishTrap = ChunkTurkishTrap(
                incorrectEn = "We will make a meeting at three.",
                correctEn = "We will have a meeting at three.",
                explanationTr = "'make a meeting' değil 'have a meeting' kullanılır.",
                trapType = "collocation_transfer",
            ),
            blueprintSlots = listOf(
                BlueprintSlot(BlueprintSlotRole.SUBJECT, "Who", "We", "Biz"),
                BlueprintSlot(BlueprintSlotRole.VERB_BRIDGE, "Verb bridge", "have a meeting", "toplantı yapıyoruz"),
                BlueprintSlot(BlueprintSlotRole.TIME_PLACE, "When", "at three", "saat üçte"),
            ),
            slotVariations = listOf(
                ChunkSlotVariation("at three", "We have a meeting at three.", "Saat üçte toplantımız var."),
            ),
        ),
        LexicalChunk(
            id = "chunk.b1.bridge.to-be-honest",
            chunkEn = "To be honest,...",
            patternTemplate = "To be honest, + [Clause]",
            cefrLevel = "B1",
            category = ChunkCategory.CONVERSATION_BRIDGE,
            intentTr = "Dürüst olmak gerekirse...",
            meaningEn = "Introduce a candid opinion.",
            turkishTrap = ChunkTurkishTrap(
                incorrectEn = "For being honest, I prefer this.",
                correctEn = "To be honest, I prefer this.",
                explanationTr = "'For being honest' yerine 'To be honest,' kullanılır.",
                trapType = "preposition_transfer",
            ),
            blueprintSlots = listOf(
                BlueprintSlot(BlueprintSlotRole.TIME_PLACE, "Bridge", "To be honest,", "Dürüst olmak gerekirse,"),
                BlueprintSlot(BlueprintSlotRole.SUBJECT, "Who", "I", "ben"),
                BlueprintSlot(BlueprintSlotRole.VERB_BRIDGE, "Verb", "prefer this", "bunu tercih ederim"),
            ),
            slotVariations = listOf(
                ChunkSlotVariation("I prefer this", "To be honest, I prefer this.", "Dürüst olmak gerekirse bunu tercih ederim."),
            ),
        ),
    )

    @Test
    fun selectDailyPack_isDeterministicAndBounded() {
        val chunks = sampleChunks()
        val packA = engine.selectDailyPack(chunks, daySeed = 42L, count = 2)
        val packB = engine.selectDailyPack(chunks, daySeed = 42L, count = 2)
        assertEquals(2, packA.size)
        assertEquals(packA.map { it.id }, packB.map { it.id })
    }

    @Test
    fun buildSession_blueprintAssemblyScramblesSlotsAndEvaluatesCorrectly() {
        val chunks = sampleChunks()
        val questions = engine.buildSession(chunks, mode = ChunkDrillMode.BLUEPRINT_ASSEMBLY)
        assertEquals(3, questions.size)
        val first = questions.first() as ChunkDrillQuestion.BlueprintAssembly
        assertNotEquals(first.correctSlots, first.scrambledSlots)
        assertTrue(engine.evaluateBlueprint(first, first.correctSlots))
        assertFalse(engine.evaluateBlueprint(first, first.scrambledSlots))
    }

    @Test
    fun buildSession_focusChunkMixedCreatesUpToThreeDistinctModes() {
        val chunks = sampleChunks()
        val questions = engine.buildSession(
            chunks = chunks,
            mode = ChunkDrillMode.MIXED,
            focusChunkId = "chunk.a2.collocation.make-a-decision",
        )
        assertEquals(3, questions.size)
        assertTrue(questions.any { it is ChunkDrillQuestion.Choice && !it.isTrapQuestion })
        assertTrue(questions.any { it is ChunkDrillQuestion.Choice && it.isTrapQuestion })
        assertTrue(questions.any { it is ChunkDrillQuestion.BlueprintAssembly })
    }
}
