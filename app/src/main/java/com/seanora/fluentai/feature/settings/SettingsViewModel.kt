package com.seanora.fluentai.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.data.preferences.UserPreferencesRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.feature.reading.ReadingSurfaceStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: UserLearningRepository,
    private val preferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    val profile: StateFlow<UserProfile?> = repository.getUserProfile().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        null,
    )

    val readingSurfaceStyle: StateFlow<ReadingSurfaceStyle> = preferencesRepository.readingSurfaceTheme
        .map(ReadingSurfaceStyle::fromKey)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ReadingSurfaceStyle.PAPER)

    val readingFontSizeSp: StateFlow<Float> = preferencesRepository.readingFontSizeSp
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 20f)

    fun updateTargetCefr(level: String) {
        if (level !in AVAILABLE_TARGET_LEVELS) return
        viewModelScope.launch {
            val current = repository.getUserProfileSync() ?: UserProfile()
            repository.saveUserProfile(current.copy(targetCefrLevel = level, updatedAt = System.currentTimeMillis()))
        }
    }

    fun updateDailyGoalMinutes(minutes: Int) {
        val clamped = minutes.coerceIn(5, 120)
        viewModelScope.launch {
            val current = repository.getUserProfileSync() ?: UserProfile()
            repository.saveUserProfile(current.copy(dailyGoalMinutes = clamped, updatedAt = System.currentTimeMillis()))
        }
    }

    fun toggleInterest(interest: String) {
        viewModelScope.launch {
            val current = repository.getUserProfileSync() ?: UserProfile()
            val updated = if (interest in current.learningInterests) {
                current.learningInterests - interest
            } else {
                current.learningInterests + interest
            }
            repository.saveUserProfile(current.copy(learningInterests = updated, updatedAt = System.currentTimeMillis()))
        }
    }

    fun setReadingSurfaceStyle(style: ReadingSurfaceStyle) {
        viewModelScope.launch {
            preferencesRepository.setReadingSurfaceTheme(style.name)
        }
    }

    fun decreaseReadingFontSize() {
        viewModelScope.launch {
            preferencesRepository.setReadingFontSizeSp((readingFontSizeSp.value - 2f).coerceAtLeast(16f))
        }
    }

    fun increaseReadingFontSize() {
        viewModelScope.launch {
            preferencesRepository.setReadingFontSizeSp((readingFontSizeSp.value + 2f).coerceAtMost(26f))
        }
    }

    companion object {
        val AVAILABLE_TARGET_LEVELS = listOf("B1", "B2", "C1", "C2")
        val AVAILABLE_DAILY_GOALS = listOf(10, 15, 20, 30, 45)
        val AVAILABLE_INTERESTS = listOf(
            "Technology",
            "Business & Career",
            "Daily Life",
            "Travel",
            "IELTS",
            "Science & Culture",
            "Leadership",
            "Finance",
        )
    }
}
