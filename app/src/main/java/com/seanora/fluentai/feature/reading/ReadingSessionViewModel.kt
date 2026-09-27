package com.seanora.fluentai.feature.reading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanora.fluentai.app.navigation.ReadingSessionMode
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.ReadingArticle
import com.seanora.fluentai.data.repository.ReadingRepository
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ReadingSessionSection(val label: String) {
    ARTICLE("Article"),
    VOCABULARY("Vocabulary"),
    QUESTIONS("Questions"),
    REVIEW("Review"),
}

data class ReadingSessionUiState(
    val isLoading: Boolean = true,
    val requestedArticleId: String? = null,
    val article: ReadingArticle? = null,
    val mode: ReadingSessionMode = ReadingSessionMode.STANDARD,
    val section: ReadingSessionSection = ReadingSessionSection.ARTICLE,
    val guidedParagraphIndex: Int = 0,
    val questionIndex: Int = 0,
    val selectedAnswers: Map<String, String> = emptyMap(),
    val submittedQuestionIds: Set<String> = emptySet(),
    val answerCorrectness: Map<String, Boolean> = emptyMap(),
    val errorMessage: String? = null,
) {
    val currentQuestion get() = article?.comprehensionQuestions?.getOrNull(questionIndex)
    val correctCount get() = answerCorrectness.values.count { it }
}

@HiltViewModel
class ReadingSessionViewModel @Inject constructor(
    private val readingRepository: ReadingRepository,
    private val recordLearningEvidence: RecordLearningEvidenceUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReadingSessionUiState())
    val uiState: StateFlow<ReadingSessionUiState> = _uiState.asStateFlow()

    fun openArticle(contentId: String, mode: ReadingSessionMode) {
        if (_uiState.value.requestedArticleId == contentId && _uiState.value.mode == mode) return
        _uiState.value = ReadingSessionUiState(requestedArticleId = contentId, mode = mode)
        viewModelScope.launch {
            val article = readingRepository.getArticleById(contentId).first()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    article = article,
                    errorMessage = if (article == null) "This article is no longer available." else null,
                )
            }
        }
    }

    fun selectSection(section: ReadingSessionSection) = _uiState.update { it.copy(section = section) }

    fun previousParagraph() = _uiState.update {
        it.copy(guidedParagraphIndex = (it.guidedParagraphIndex - 1).coerceAtLeast(0))
    }

    fun nextParagraph() = _uiState.update { state ->
        val lastIndex = (state.article?.paragraphs?.lastIndex ?: 0).coerceAtLeast(0)
        state.copy(guidedParagraphIndex = (state.guidedParagraphIndex + 1).coerceAtMost(lastIndex))
    }

    fun selectAnswer(questionId: String, answer: String) = _uiState.update { state ->
        if (questionId in state.submittedQuestionIds) state
        else state.copy(selectedAnswers = state.selectedAnswers + (questionId to answer))
    }

    fun checkAnswer() {
        val state = _uiState.value
        val article = state.article ?: return
        val question = state.currentQuestion ?: return
        val answer = state.selectedAnswers[question.id] ?: return
        if (question.id in state.submittedQuestionIds || answer !in question.options) return
        val correct = answer.trim() == question.correctAnswer.trim()
        _uiState.update {
            it.copy(
                submittedQuestionIds = it.submittedQuestionIds + question.id,
                answerCorrectness = it.answerCorrectness + (question.id to correct),
            )
        }
        viewModelScope.launch {
            recordLearningEvidence(
                LearningEvidence(
                    contentId = article.id,
                    domain = "reading",
                    activityType = "comprehension_check",
                    isCorrect = correct,
                    score = if (correct) 1f else 0f,
                    details = "question_id=${question.id},selected=$answer,correct=${question.correctAnswer}",
                ),
            )
        }
    }

    fun nextQuestion() = _uiState.update { state ->
        val last = (state.article?.comprehensionQuestions?.lastIndex ?: 0).coerceAtLeast(0)
        state.copy(questionIndex = (state.questionIndex + 1).coerceAtMost(last))
    }

    fun previousQuestion() = _uiState.update {
        it.copy(questionIndex = (it.questionIndex - 1).coerceAtLeast(0))
    }

    fun readAgain() = _uiState.update {
        it.copy(section = ReadingSessionSection.ARTICLE, guidedParagraphIndex = 0)
    }

    fun reviewMistakes() = _uiState.update { state ->
        val firstIncorrect = state.article?.comprehensionQuestions?.indexOfFirst {
            state.answerCorrectness[it.id] == false
        } ?: -1
        state.copy(
            section = ReadingSessionSection.QUESTIONS,
            questionIndex = firstIncorrect.coerceAtLeast(0),
        )
    }
}
