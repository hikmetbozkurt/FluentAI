package com.seanora.fluentai.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReadingParagraph(
    @SerialName("paragraph_index")
    val paragraphIndex: Int,
    val title: String? = null,
    @SerialName("content_en")
    val contentEn: String,
    @SerialName("content_tr")
    val contentTr: String
)

@Serializable
data class VocabularyAnnotation(
    val word: String,
    @SerialName("vocab_id")
    val vocabId: String? = null,
    @SerialName("context_definition_en")
    val contextDefinitionEn: String,
    @SerialName("context_meaning_tr")
    val contextMeaningTr: String
)

@Serializable
data class ReadingComprehensionQuestion(
    val id: String,
    @SerialName("question_en")
    val questionEn: String,
    @SerialName("question_tr_hint")
    val questionTrHint: String? = null,
    val options: List<String>,
    @SerialName("correct_answer")
    val correctAnswer: String,
    @SerialName("explanation_en")
    val explanationEn: String,
    @SerialName("explanation_tr")
    val explanationTr: String
)

data class ReadingArticle(
    val id: String,
    val title: String,
    val cefrLevel: String,
    val category: String,
    val summaryEn: String,
    val summaryTr: String,
    val wordCount: Int,
    val estimatedReadingMinutes: Int,
    val paragraphs: List<ReadingParagraph>,
    val vocabularyAnnotations: List<VocabularyAnnotation>,
    val comprehensionQuestions: List<ReadingComprehensionQuestion>,
    val topicTags: List<String>,
    val relatedIds: List<String>,
    val status: String = "APPROVED",
    val version: Int = 1
)
