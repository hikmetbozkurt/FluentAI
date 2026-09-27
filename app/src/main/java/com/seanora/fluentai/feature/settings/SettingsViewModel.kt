package com.seanora.fluentai.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.data.repository.UserLearningRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(repository: UserLearningRepository) : ViewModel() {
    val profile: StateFlow<UserProfile?> = repository.getUserProfile().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), null,
    )
}
