package com.seanora.fluentai.feature.reading

import com.seanora.fluentai.core.model.ReadingArticle
import com.seanora.fluentai.core.model.VocabularyAnnotation

data class ReadingUiState(
    val articles: List<ReadingArticle> = emptyList(),
    val selectedArticle: ReadingArticle? = null,
    val selectedLevel: String? = null,
    val searchQuery: String = "",
    val revealedParagraphIndices: Set<Int> = emptySet(),
    val revealedSummary: Boolean = false,
    val selectedAnnotation: VocabularyAnnotation? = null,
    val userAnswers: Map<String, String> = emptyMap(),
    val submittedQuestions: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
