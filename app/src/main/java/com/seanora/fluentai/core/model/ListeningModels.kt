package com.seanora.fluentai.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Speaker(
    val id: String,
    val name: String,
    val role: String,
    val accent: String? = null
)

@Serializable
data class TranscriptItem(
    val index: Int,
    @SerialName("speaker_id")
    val speakerId: String,
    @SerialName("start_ms")
    val startMs: Long,
    @SerialName("end_ms")
    val endMs: Long,
    @SerialName("text_en")
    val textEn: String,
    @SerialName("text_tr")
    val textTr: String
)

@Serializable
data class KeyVocabulary(
    val word: String,
    @SerialName("vocab_id")
    val vocabId: String? = null,
    @SerialName("context_note_tr")
    val contextNoteTr: String
)

@Serializable
data class ListeningComprehensionQuestion(
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

data class ListeningScenario(
    val id: String,
    val title: String,
    val cefrLevel: String,
    val category: String,
    val scenarioContext: String,
    val speakers: List<Speaker>,
    val audioRef: String,
    val durationSeconds: Int,
    val transcriptItems: List<TranscriptItem>,
    val keyVocabulary: List<KeyVocabulary>,
    val comprehensionQuestions: List<ListeningComprehensionQuestion>,
    val topicTags: List<String>,
    val relatedIds: List<String>,
    val status: String = "APPROVED",
    val version: Int = 1
)
