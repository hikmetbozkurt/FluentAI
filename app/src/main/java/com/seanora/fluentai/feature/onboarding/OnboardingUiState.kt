package com.seanora.fluentai.feature.onboarding

import com.seanora.fluentai.core.model.AssessmentDomain
import com.seanora.fluentai.core.model.LearningInterest
import com.seanora.fluentai.core.model.PlacementAssessmentSummary
import com.seanora.fluentai.core.model.PlacementProbe
import com.seanora.fluentai.core.model.PlacementResponse
import com.seanora.fluentai.core.model.SelfAssessedLevel
import com.seanora.fluentai.core.model.UserGoal

enum class OnboardingStep {
    WELCOME,
    AVATAR_SELECTION,
    GOAL_SELECTION,
    INTERESTS_SELECTION,
    SELF_ASSESSMENT,
    DAILY_TARGET,
    PLACEMENT_TEST,
    PLACEMENT_RESULT
}

data class OnboardingUiState(
    val currentStep: OnboardingStep = OnboardingStep.WELCOME,
    val selectedAvatarKey: String? = null,
    val selectedGoal: UserGoal? = null,
    val selectedInterests: Set<LearningInterest> = emptySet(),
    val selectedSelfAssessment: SelfAssessedLevel = SelfAssessedLevel.B1_INTERMEDIATE,
    val targetCefrLevel: String = "C1",
    val dailyPracticeMinutes: Int = 20,
    val displayName: String = "",

    // Placement Test State
    val isPlacementActive: Boolean = false,
    val currentDomain: AssessmentDomain = AssessmentDomain.VOCABULARY,
    val currentProbe: PlacementProbe? = null,
    val currentTargetLevel: String = "B1",
    val selectedOptionIndex: Int? = null,
    val isAnswerSubmitted: Boolean = false,
    val completedResponses: List<PlacementResponse> = emptyList(),
    val testedProbeIds: Set<String> = emptySet(),
    val totalProbesTarget: Int = 12,

    // Final Outcome
    val placementSummary: PlacementAssessmentSummary? = null,
    val isSavingProfile: Boolean = false,
    val isCompleted: Boolean = false
)
