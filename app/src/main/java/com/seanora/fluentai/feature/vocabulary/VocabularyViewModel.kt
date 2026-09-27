package com.seanora.fluentai.feature.vocabulary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanora.fluentai.core.model.VocabItem
import com.seanora.fluentai.data.repository.VocabularyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VocabularyUiState(
    val isLoading: Boolean = true,
    val items: List<VocabItem> = emptyList(),
    val selectedLevel: String? = null, // null means "ALL"
    val searchQuery: String = "",
    val selectedItem: VocabItem? = null,
    val isTurkishRevealed: Boolean = false,
    val errorMessage: String? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class VocabularyViewModel @Inject constructor(
    private val vocabularyRepository: VocabularyRepository
) : ViewModel() {

    private val _selectedLevel = MutableStateFlow<String?>(null)
    private val _searchQuery = MutableStateFlow("")
    private val _selectedItem = MutableStateFlow<VocabItem?>(null)
    private val _isTurkishRevealed = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val uiState: StateFlow<VocabularyUiState> = combine(
        _selectedLevel,
        _searchQuery
    ) { level, query ->
        Pair(level, query)
    }.flatMapLatest { (level, query) ->
        when {
            query.isNotBlank() -> vocabularyRepository.searchVocab(query)
            level != null -> vocabularyRepository.getVocabByLevel(level)
            else -> vocabularyRepository.getAllVocab()
        }
    }.combine(_selectedLevel) { items, level ->
        Pair(items, level)
    }.combine(_searchQuery) { (items, level), query ->
        Triple(items, level, query)
    }.combine(_selectedItem) { (items, level, query), selected ->
        Tuple4(items, level, query, selected)
    }.combine(_isTurkishRevealed) { tuple, isRevealed ->
        VocabularyUiState(
            isLoading = false,
            items = tuple.items,
            selectedLevel = tuple.level,
            searchQuery = tuple.query,
            selectedItem = tuple.selected,
            isTurkishRevealed = isRevealed,
            errorMessage = null
        )
    }.catch { throwable ->
        android.util.Log.e("VocabularyViewModel", "Failed to load vocabulary", throwable)
        emit(
            VocabularyUiState(
                isLoading = false,
                errorMessage = "Content could not be loaded. Please try again."
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = VocabularyUiState(isLoading = true)
    )

    fun selectLevel(level: String?) {
        _selectedLevel.value = level
        _isTurkishRevealed.value = false
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectItem(item: VocabItem) {
        _selectedItem.value = item
        // ADR-009: When switching items, reset reveal to English-first
        _isTurkishRevealed.value = false
    }

    fun selectItemById(contentId: String) {
        viewModelScope.launch {
            _selectedItem.value = vocabularyRepository.getVocabById(contentId).first()
            _isTurkishRevealed.value = false
        }
    }

    fun toggleTurkishReveal() {
        _isTurkishRevealed.update { !it }
    }

    fun clearSelection() {
        _selectedItem.value = null
        _isTurkishRevealed.value = false
    }

    private data class Tuple4<A, B, C, D>(
        val items: A,
        val level: B,
        val query: C,
        val selected: D
    )
}
