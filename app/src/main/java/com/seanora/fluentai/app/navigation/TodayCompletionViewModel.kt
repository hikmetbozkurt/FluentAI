package com.seanora.fluentai.app.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanora.fluentai.data.repository.TodayPracticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayCompletionViewModel @Inject constructor(
    private val todayPracticeRepository: TodayPracticeRepository,
) : ViewModel() {
    fun complete(planId: String?, itemId: String?) {
        if (planId.isNullOrBlank() || itemId.isNullOrBlank()) return
        viewModelScope.launch {
            todayPracticeRepository.markItemCompleted(planId, itemId)
        }
    }
}
