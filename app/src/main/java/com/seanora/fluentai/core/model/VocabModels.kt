package com.seanora.fluentai.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Collocation(
    val text: String,
    val tr: String? = null,
    val note: String? = null
)

@Serializable
data class ContextExample(
    val en: String,
    val tr: String,
    val context: String? = null
)

@Serializable
data class TurkishTrap(
    val trapType: String,
    val trapNoteTr: String,
    val incorrectExample: String? = null,
    val correctExample: String? = null
)

data class VocabItem(
    val id: String,
    val headword: String,
    val cefrLevel: String,
    val partOfSpeech: String,
    val phonetic: String,
    val audioRef: String? = null,
    val definitionEn: String,
    val meaningTr: String,
    val collocations: List<Collocation> = emptyList(),
    val examples: List<ContextExample> = emptyList(),
    val turkishTraps: List<TurkishTrap> = emptyList(),
    val register: String = "general",
    val topicTags: List<String> = emptyList(),
    val relatedIds: List<String> = emptyList(),
    val status: String = "APPROVED",
    val version: Int = 1
)
