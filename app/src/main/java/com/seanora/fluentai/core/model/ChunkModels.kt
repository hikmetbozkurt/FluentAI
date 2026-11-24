package com.seanora.fluentai.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class ChunkCategory(val wireValue: String, val displayTitle: String, val shortSubtitle: String) {
    SENTENCE_STARTER(
        wireValue = "SENTENCE_STARTER",
        displayTitle = "Sentence starters",
        shortSubtitle = "Ready-made openings for everyday and workplace speech",
    ),
    ACTION_COLLOCATION(
        wireValue = "ACTION_COLLOCATION",
        displayTitle = "Action collocations",
        shortSubtitle = "Natural verb + noun pairs that prevent Turkish transfer",
    ),
    CONVERSATION_BRIDGE(
        wireValue = "CONVERSATION_BRIDGE",
        displayTitle = "Conversation bridges",
        shortSubtitle = "Connectors for clarifying, hedging, and disagreeing politely",
    ),
    IELTS_ACADEMIC_FRAME(
        wireValue = "IELTS_ACADEMIC_FRAME",
        displayTitle = "Academic and IELTS frames",
        shortSubtitle = "Structured frames for analytical speaking and presentations",
    );

    companion object {
        fun fromWire(value: String?): ChunkCategory =
            entries.firstOrNull { it.wireValue.equals(value, ignoreCase = true) } ?: SENTENCE_STARTER
    }
}

@Serializable
enum class BlueprintSlotRole {
    SUBJECT,
    VERB_BRIDGE,
    OBJECT,
    TIME_PLACE,
}

@Serializable
data class BlueprintSlot(
    val role: BlueprintSlotRole = BlueprintSlotRole.OBJECT,
    val label: String,
    @SerialName("text_en") val textEn: String,
    @SerialName("text_tr") val textTr: String,
)

@Serializable
data class ChunkTurkishTrap(
    @SerialName("incorrect_en") val incorrectEn: String,
    @SerialName("correct_en") val correctEn: String,
    @SerialName("explanation_tr") val explanationTr: String,
    @SerialName("trap_type") val trapType: String,
)

@Serializable
data class ChunkSlotVariation(
    @SerialName("slot_en") val slotEn: String,
    @SerialName("full_sentence_en") val fullSentenceEn: String,
    @SerialName("full_sentence_tr") val fullSentenceTr: String,
)

data class LexicalChunk(
    val id: String,
    val chunkEn: String,
    val patternTemplate: String,
    val cefrLevel: String,
    val category: ChunkCategory,
    val intentTr: String,
    val meaningEn: String,
    val turkishTrap: ChunkTurkishTrap,
    val blueprintSlots: List<BlueprintSlot>,
    val slotVariations: List<ChunkSlotVariation>,
    val relatedVocabIds: List<String> = emptyList(),
    val relatedGrammarIds: List<String> = emptyList(),
    val topicTags: List<String> = emptyList(),
    val status: String = "PUBLISHED",
    val version: Int = 1,
)

enum class ChunkDrillMode(val title: String) {
    MIXED("Mixed chunk drill"),
    TURKISH_INTENT_MATCH("Turkish intent match"),
    TRAP_BUSTER("Turkish trap buster"),
    BLUEPRINT_ASSEMBLY("SVO blueprint builder"),
}

sealed interface ChunkDrillQuestion {
    val id: String
    val chunkId: String
    val chunkEn: String
    val cefrLevel: String
    val explanationTr: String

    data class Choice(
        override val id: String,
        override val chunkId: String,
        override val chunkEn: String,
        override val cefrLevel: String,
        val promptTitle: String,
        val promptSubtitle: String,
        val options: List<String>,
        val correctAnswer: String,
        override val explanationTr: String,
        val isTrapQuestion: Boolean,
    ) : ChunkDrillQuestion

    data class BlueprintAssembly(
        override val id: String,
        override val chunkId: String,
        override val chunkEn: String,
        override val cefrLevel: String,
        val promptTr: String,
        val correctSlots: List<BlueprintSlot>,
        val scrambledSlots: List<BlueprintSlot>,
        override val explanationTr: String,
    ) : ChunkDrillQuestion
}
