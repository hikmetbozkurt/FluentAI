package com.seanora.fluentai.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanora.fluentai.app.navigation.resolveStartupDestination
import com.seanora.fluentai.data.repository.UserLearningRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed interface AppStartupState {
    data object Loading : AppStartupState
    data class Ready(val startDestination: String) : AppStartupState
}

@HiltViewModel
class AppStartupViewModel @Inject constructor(
    userLearningRepository: UserLearningRepository
) : ViewModel() {
    val state: StateFlow<AppStartupState> = userLearningRepository.getUserProfile()
        .map { profile -> AppStartupState.Ready(resolveStartupDestination(profile)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AppStartupState.Loading)
}
