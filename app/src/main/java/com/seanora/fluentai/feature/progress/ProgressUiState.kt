package com.seanora.fluentai.feature.progress

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.domain.progress.ProgressDashboardData
import com.seanora.fluentai.domain.progress.SkillProficiency

data class ProgressUiState(
    val isLoading: Boolean = true,
    val dashboardData: ProgressDashboardData? = null,
    val selectedSkill: SkillProficiency? = null,
    val recentEvidence: List<LearningEvidence> = emptyList(),
    val userProfile: UserProfile? = null
)
