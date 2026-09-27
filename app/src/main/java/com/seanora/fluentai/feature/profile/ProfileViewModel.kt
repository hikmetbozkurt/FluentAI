package com.seanora.fluentai.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.data.preferences.UserPreferencesRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserLearningRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val profile: StateFlow<UserProfile?> = repository.getUserProfile().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        null,
    )

    val selectedAvatarKey: StateFlow<String?> = userPreferencesRepository.selectedAvatarKey.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        null,
    )

    fun updateDisplayName(name: String) {
        viewModelScope.launch {
            repository.updateDisplayName(name)
        }
    }

    fun selectAvatar(key: String) {
        viewModelScope.launch {
            userPreferencesRepository.setSelectedAvatarKey(key)
        }
    }
}
