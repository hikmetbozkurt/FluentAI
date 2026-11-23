package com.seanora.fluentai.feature.reading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanora.fluentai.data.feed.LiveReadingArticle
import com.seanora.fluentai.data.feed.LiveReadingFeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LiveReadingFeedUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val articles: List<LiveReadingArticle> = emptyList(),
    val selectedCategory: String? = null,
    val selectedLevel: String? = null,
    val isFromCache: Boolean = false,
    val statusMessage: String? = null,
    val activeArticle: LiveReadingArticle? = null,
) {
    val availableCategories: List<String>
        get() = articles.map { it.category }.distinct().sorted()

    val visibleArticles: List<LiveReadingArticle>
        get() = articles.filter { article ->
            (selectedCategory == null || article.category == selectedCategory) &&
                (selectedLevel == null || article.cefrLevel == selectedLevel)
        }
}

@HiltViewModel
class LiveReadingFeedViewModel @Inject constructor(
    private val feedRepository: LiveReadingFeedRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LiveReadingFeedUiState())
    val uiState: StateFlow<LiveReadingFeedUiState> = _uiState.asStateFlow()

    init {
        loadInitial()
    }

    private fun loadInitial() {
        viewModelScope.launch {
            val cached = feedRepository.getCachedArticles()
            if (cached.isNotEmpty()) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        articles = cached,
                        isFromCache = true,
                    )
                }
            }
            refresh()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, statusMessage = null) }
            val result = feedRepository.refreshFeed()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isRefreshing = false,
                    articles = result.articles,
                    isFromCache = result.isFromCache,
                    statusMessage = result.errorMessage,
                )
            }
        }
    }

    fun selectCategory(category: String?) = _uiState.update { it.copy(selectedCategory = category) }

    fun selectLevel(level: String?) = _uiState.update { it.copy(selectedLevel = level) }

    fun openArticle(articleId: String) {
        viewModelScope.launch {
            val found = _uiState.value.articles.firstOrNull { it.id == articleId }
                ?: feedRepository.getArticleById(articleId)
            _uiState.update { it.copy(activeArticle = found) }
        }
    }
}
