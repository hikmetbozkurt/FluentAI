package com.seanora.fluentai.core.model

data class Exercise(
    val id: String,
    val targetContentId: String,
    val cefrLevel: String,
    val skillDomain: String,
    val exerciseType: String,
    val promptEn: String,
    val promptTrHint: String? = null,
    val stem: String,
    val options: List<String> = emptyList(),
    val correctAnswer: String,
    val explanationEn: String,
    val explanationTr: String,
    val distractorExplanations: Map<String, String> = emptyMap(),
    val difficulty: String = "standard",
    val status: String = "APPROVED",
    val version: Int = 1
)
