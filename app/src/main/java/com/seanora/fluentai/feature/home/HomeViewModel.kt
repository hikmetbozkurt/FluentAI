package com.seanora.fluentai.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanora.fluentai.feature.home.data.HomeRepository
import com.seanora.fluentai.feature.home.model.TodayPlan
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val plan: TodayPlan) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = repository.getTodayPlan()
        .map<TodayPlan, HomeUiState> { HomeUiState.Success(it) }
        .catch { e ->
            android.util.Log.e("HomeViewModel", "Failed to load Today's Plan", e)
            emit(HomeUiState.Error("Content could not be loaded. Please try again."))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState.Loading,
        )

    fun markItemCompleted(planId: String, itemId: String) {
        viewModelScope.launch {
            repository.markItemCompleted(planId, itemId)
        }
    }

    fun refreshPlan() {
        viewModelScope.launch {
            repository.refreshPlan()
        }
    }
}
