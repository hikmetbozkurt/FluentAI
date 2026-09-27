package com.seanora.fluentai.feature.grammar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanora.fluentai.core.model.Exercise
import com.seanora.fluentai.core.model.GrammarLesson
import com.seanora.fluentai.data.repository.GrammarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GrammarUiState(
    val isLoading: Boolean = true,
    val lessons: List<GrammarLesson> = emptyList(),
    val selectedLevel: String? = null, // null means "ALL"
    val searchQuery: String = "",
    val selectedLesson: GrammarLesson? = null,
    val exercises: List<Exercise> = emptyList(),
    val isTurkishRevealed: Boolean = false,
    val errorMessage: String? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class GrammarViewModel @Inject constructor(
    private val grammarRepository: GrammarRepository
) : ViewModel() {

    private val _selectedLevel = MutableStateFlow<String?>(null)
    private val _searchQuery = MutableStateFlow("")
    private val _selectedLesson = MutableStateFlow<GrammarLesson?>(null)
    private val _isTurkishRevealed = MutableStateFlow(false)

    val uiState: StateFlow<GrammarUiState> = combine(
        _selectedLevel,
        _searchQuery
    ) { level, query ->
        Pair(level, query)
    }.flatMapLatest { (level, query) ->
        when {
            query.isNotBlank() -> grammarRepository.searchLessons(query)
            level != null -> grammarRepository.getLessonsByLevel(level)
            else -> grammarRepository.getAllLessons()
        }
    }.combine(_selectedLevel) { lessons, level ->
        Pair(lessons, level)
    }.combine(_searchQuery) { (lessons, level), query ->
        Triple(lessons, level, query)
    }.combine(_selectedLesson) { (lessons, level, query), selected ->
        Tuple4(lessons, level, query, selected)
    }.flatMapLatest { tuple ->
        val currentSelected = tuple.selected
        val exercisesFlow = if (currentSelected != null) {
            grammarRepository.getExercisesForLesson(currentSelected.id)
        } else {
            flowOf(emptyList())
        }
        exercisesFlow.combine(_isTurkishRevealed) { exercises, isRevealed ->
            GrammarUiState(
                isLoading = false,
                lessons = tuple.lessons,
                selectedLevel = tuple.level,
                searchQuery = tuple.query,
                selectedLesson = tuple.selected,
                exercises = exercises,
                isTurkishRevealed = isRevealed,
                errorMessage = null
            )
        }
    }.catch { throwable ->
        android.util.Log.e("GrammarViewModel", "Failed to load grammar lessons", throwable)
        emit(
            GrammarUiState(
                isLoading = false,
                errorMessage = "Content could not be loaded. Please try again."
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = GrammarUiState(isLoading = true)
    )

    fun selectLevel(level: String?) {
        _selectedLevel.value = level
        _isTurkishRevealed.value = false
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectLesson(lesson: GrammarLesson) {
        _selectedLesson.value = lesson
        // ADR-009: When switching lessons, reset reveal to English-first
        _isTurkishRevealed.value = false
    }

    fun selectLessonById(contentId: String) {
        viewModelScope.launch {
            _selectedLesson.value = grammarRepository.getLessonById(contentId).first()
            _isTurkishRevealed.value = false
        }
    }

    fun toggleTurkishReveal() {
        _isTurkishRevealed.update { !it }
    }

    fun clearSelection() {
        _selectedLesson.value = null
        _isTurkishRevealed.value = false
    }

    private data class Tuple4<A, B, C, D>(
        val lessons: A,
        val level: B,
        val query: C,
        val selected: D
    )
}
