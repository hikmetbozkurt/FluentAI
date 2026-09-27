package com.seanora.fluentai.feature.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.domain.progress.ProgressEngine
import com.seanora.fluentai.domain.progress.SkillProficiency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val userLearningRepository: UserLearningRepository,
    private val progressEngine: ProgressEngine
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    init {
        loadProgressData()
    }

    private fun loadProgressData() {
        val now = System.currentTimeMillis()
        viewModelScope.launch {
            combine(
                userLearningRepository.getAllMasterySnapshots(),
                userLearningRepository.getRecentEvidence(50),
                userLearningRepository.getAllMistakes(),
                userLearningRepository.getDueReviews(now),
                userLearningRepository.getUserProfile()
            ) { snapshots, evidences, mistakes, reviews, profile ->
                val dashboard = progressEngine.computeDashboardData(
                    snapshots = snapshots,
                    evidences = evidences,
                    mistakes = mistakes,
                    reviews = reviews,
                    userProfile = profile,
                    currentTimestamp = now
                )

                _uiState.update { current ->
                    current.copy(
                        isLoading = false,
                        dashboardData = dashboard,
                        recentEvidence = evidences,
                        userProfile = profile
                    )
                }
            }.catch { e ->
                android.util.Log.e("ProgressViewModel", "Failed to load progress data", e)
                _uiState.update { it.copy(isLoading = false) }
            }.collect {}
        }
    }

    fun selectSkill(skill: SkillProficiency?) {
        _uiState.update { it.copy(selectedSkill = skill) }
    }
}
