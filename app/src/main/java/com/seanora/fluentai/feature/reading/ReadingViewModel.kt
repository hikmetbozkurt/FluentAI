package com.seanora.fluentai.feature.reading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.ReadingArticle
import com.seanora.fluentai.core.model.ReadingComprehensionQuestion
import com.seanora.fluentai.core.model.VocabularyAnnotation
import com.seanora.fluentai.data.repository.ReadingRepository
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ReadingViewModel @Inject constructor(
    private val readingRepository: ReadingRepository,
    private val recordLearningEvidenceUseCase: RecordLearningEvidenceUseCase
) : ViewModel() {

    private val _selectedLevel = MutableStateFlow<String?>(null)
    private val _searchQuery = MutableStateFlow("")
    private val _selectedArticle = MutableStateFlow<ReadingArticle?>(null)
    private val _revealedParagraphs = MutableStateFlow<Set<Int>>(emptySet())
    private val _revealedSummary = MutableStateFlow(false)
    private val _selectedAnnotation = MutableStateFlow<VocabularyAnnotation?>(null)
    private val _userAnswers = MutableStateFlow<Map<String, String>>(emptyMap())
    private val _submittedQuestions = MutableStateFlow<Set<String>>(emptySet())

    val uiState: StateFlow<ReadingUiState> = combine(
        _selectedLevel,
        _searchQuery
    ) { level, query ->
        Pair(level, query)
    }.flatMapLatest { (level, query) ->
        when {
            query.isNotBlank() -> readingRepository.searchArticles(query)
            level != null -> readingRepository.getArticlesByLevel(level)
            else -> readingRepository.getAllArticles()
        }
    }.combine(_selectedLevel) { articles, level ->
        Pair(articles, level)
    }.combine(_searchQuery) { (articles, level), query ->
        Triple(articles, level, query)
    }.combine(_selectedArticle) { (articles, level, query), selected ->
        Tuple4(articles, level, query, selected)
    }.combine(_revealedParagraphs) { tuple, revealedParas ->
        Tuple5(tuple.a, tuple.b, tuple.c, tuple.d, revealedParas)
    }.combine(_revealedSummary) { tuple, revealedSum ->
        Tuple6(tuple.a, tuple.b, tuple.c, tuple.d, tuple.e, revealedSum)
    }.combine(_selectedAnnotation) { tuple, annotation ->
        Tuple7(tuple.a, tuple.b, tuple.c, tuple.d, tuple.e, tuple.f, annotation)
    }.combine(_userAnswers) { tuple, answers ->
        Tuple8(tuple.a, tuple.b, tuple.c, tuple.d, tuple.e, tuple.f, tuple.g, answers)
    }.combine(_submittedQuestions) { tuple, submitted ->
        ReadingUiState(
            isLoading = false,
            articles = tuple.a,
            selectedLevel = tuple.b,
            searchQuery = tuple.c,
            selectedArticle = tuple.d,
            revealedParagraphIndices = tuple.e,
            revealedSummary = tuple.f,
            selectedAnnotation = tuple.g,
            userAnswers = tuple.h,
            submittedQuestions = submitted,
            errorMessage = null
        )
    }.catch { throwable ->
        android.util.Log.e("ReadingViewModel", "Failed to load reading articles", throwable)
        emit(
            ReadingUiState(
                isLoading = false,
                errorMessage = "Content could not be loaded. Please try again."
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ReadingUiState(isLoading = true)
    )

    fun selectLevel(level: String?) {
        _selectedLevel.value = level
        resetInteractions()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectArticle(article: ReadingArticle) {
        _selectedArticle.value = article
        resetInteractions()
    }

    fun selectArticleById(contentId: String) {
        viewModelScope.launch {
            readingRepository.getArticleById(contentId).first()?.let(::selectArticle)
        }
    }

    fun clearSelectedArticle() {
        _selectedArticle.value = null
        resetInteractions()
    }

    fun toggleSummaryReveal() {
        _revealedSummary.update { !it }
    }

    fun toggleParagraphReveal(paragraphIndex: Int) {
        _revealedParagraphs.update { current ->
            if (current.contains(paragraphIndex)) current - paragraphIndex
            else current + paragraphIndex
        }
    }

    fun selectAnnotation(annotation: VocabularyAnnotation?) {
        _selectedAnnotation.value = annotation
    }

    fun selectAnswer(questionId: String, option: String) {
        _userAnswers.update { current ->
            current + (questionId to option)
        }
    }

    fun submitAnswer(articleId: String, question: ReadingComprehensionQuestion) {
        val selectedOption = _userAnswers.value[question.id] ?: return
        if (_submittedQuestions.value.contains(question.id)) return

        _submittedQuestions.update { it + question.id }
        val isCorrect = selectedOption.trim() == question.correctAnswer.trim()

        viewModelScope.launch {
            val evidence = LearningEvidence(
                contentId = articleId,
                domain = "reading",
                activityType = "comprehension_check",
                isCorrect = isCorrect,
                score = if (isCorrect) 1.0f else 0.0f,
                responseTimeMs = 5000L,
                details = "question_id=${question.id},selected=$selectedOption,correct=${question.correctAnswer}"
            )
            recordLearningEvidenceUseCase(evidence)
        }
    }

    private fun resetInteractions() {
        _revealedParagraphs.value = emptySet()
        _revealedSummary.value = false
        _selectedAnnotation.value = null
        _userAnswers.value = emptyMap()
        _submittedQuestions.value = emptySet()
    }

    private data class Tuple4<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)
    private data class Tuple5<A, B, C, D, E>(val a: A, val b: B, val c: C, val d: D, val e: E)
    private data class Tuple6<A, B, C, D, E, F>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F)
    private data class Tuple7<A, B, C, D, E, F, G>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G)
    private data class Tuple8<A, B, C, D, E, F, G, H>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G, val h: H)
}
