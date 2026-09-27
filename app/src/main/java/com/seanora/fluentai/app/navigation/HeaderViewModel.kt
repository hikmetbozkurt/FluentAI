package com.seanora.fluentai.app.navigation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanora.fluentai.core.model.SearchResultItem
import com.seanora.fluentai.data.repository.SearchRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.data.preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeaderViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    userLearningRepository: UserLearningRepository,
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val displayName: StateFlow<String> = userLearningRepository.getUserProfile()
        .map { profile -> profile?.displayName?.trim()?.ifBlank { null } ?: "Learner" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "Learner")

    val selectedAvatarKey: StateFlow<String?> = userPreferencesRepository.selectedAvatarKey
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<SearchResultItem>>(emptyList())
    val searchResults: StateFlow<List<SearchResultItem>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _searchError = MutableStateFlow<String?>(null)
    val searchError: StateFlow<String?> = _searchError.asStateFlow()

    private var searchJob: Job? = null

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        val trimmed = query.trim()
        if (trimmed.isBlank()) {
            searchJob?.cancel()
            _searchResults.value = emptyList()
            _isSearching.value = false
            _searchError.value = null
            return
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(250) // Debounce
            _isSearching.value = true
            _searchError.value = null
            try {
                val results = searchRepository.searchContent(trimmed)
                _searchResults.value = results
            } catch (e: Exception) {
                Log.e("HeaderViewModel", "Search failed for query '$trimmed'", e)
                _searchError.value = "Content could not be loaded. Please try again."
            } finally {
                _isSearching.value = false
            }
        }
    }

    fun clearSearch() {
        searchJob?.cancel()
        _searchQuery.value = ""
        _searchResults.value = emptyList()
        _isSearching.value = false
        _searchError.value = null
    }
}
