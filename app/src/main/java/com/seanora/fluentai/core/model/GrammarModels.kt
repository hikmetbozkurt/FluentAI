package com.seanora.fluentai.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ExplanationSection(
    val title: String,
    val content: String,
    val patterns: List<String> = emptyList()
)

@Serializable
data class GrammarRule(
    val name: String,
    val pattern: String,
    val useCases: List<String> = emptyList(),
    val timeMarkers: List<String> = emptyList()
)

@Serializable
data class GrammarContrast(
    val structureA: String,
    val structureB: String,
    val differenceExplanationEn: String,
    val differenceExplanationTr: String
)

@Serializable
data class GrammarTrap(
    val trapType: String,
    val trapTitle: String,
    val explanationTr: String,
    val incorrectExample: String,
    val correctExample: String
)

@Serializable
data class GrammarExample(
    val en: String,
    val tr: String,
    val ruleHighlight: String? = null,
    val context: String? = null
)

data class GrammarLesson(
    val id: String,
    val title: String,
    val cefrLevel: String,
    val category: String,
    val summaryEn: String,
    val summaryTr: String,
    val explanationEn: List<ExplanationSection> = emptyList(),
    val explanationTr: String,
    val rules: List<GrammarRule> = emptyList(),
    val contrasts: List<GrammarContrast> = emptyList(),
    val turkishTraps: List<GrammarTrap> = emptyList(),
    val examples: List<GrammarExample> = emptyList(),
    val topicTags: List<String> = emptyList(),
    val relatedIds: List<String> = emptyList(),
    val status: String = "APPROVED",
    val version: Int = 1
)
