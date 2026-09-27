package com.seanora.fluentai.feature.listening

import com.seanora.fluentai.core.model.ListeningScenario
import com.seanora.fluentai.core.model.TranscriptItem
import com.seanora.fluentai.domain.listening.DictationResult

enum class ListeningSessionSection(val label: String) {
    LISTEN("Listen"),
    QUESTIONS("Questions"),
    TRANSCRIPT("Transcript"),
    REVIEW("Review"),
}

data class ListeningUiState(
    val scenarios: List<ListeningScenario> = emptyList(),
    val selectedScenario: ListeningScenario? = null,
    val selectedLevel: String? = null,
    val selectedCategory: String? = null,
    val availableCategories: List<String> = emptyList(),
    val searchQuery: String = "",
    val isPlaying: Boolean = false,
    val currentPositionMs: Long = 0L,
    val durationMs: Long = 0L,
    val isTranscriptRevealed: Boolean = false,
    val revealedSentenceTranslations: Set<Int> = emptySet(),
    val activeSentenceIndex: Int? = null,
    val userAnswers: Map<String, String> = emptyMap(),
    val submittedQuestions: Set<String> = emptySet(),
    val sessionSection: ListeningSessionSection = ListeningSessionSection.LISTEN,
    val currentQuestionIndex: Int = 0,
    val isDictationActive: Boolean = false,
    val dictationIndex: Int = 0,
    val dictationAnswer: String = "",
    val dictationResult: DictationResult? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val currentQuestion get() = selectedScenario?.comprehensionQuestions?.getOrNull(currentQuestionIndex)
    val currentDictationTranscript: TranscriptItem?
        get() = selectedScenario?.transcriptItems
            ?.filter { it.textEn.isNotBlank() && it.startMs >= 0L && it.endMs > it.startMs }
            ?.getOrNull(dictationIndex)
}
