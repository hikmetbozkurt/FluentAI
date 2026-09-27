package com.seanora.fluentai.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanora.fluentai.core.model.AssessmentDomain
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.LearningInterest
import com.seanora.fluentai.core.model.OnboardingSurveyResult
import com.seanora.fluentai.core.model.PlacementProbe
import com.seanora.fluentai.core.model.PlacementResponse
import com.seanora.fluentai.core.model.SelfAssessedLevel
import com.seanora.fluentai.core.model.UserGoal
import com.seanora.fluentai.data.preferences.UserPreferencesRepository
import com.seanora.fluentai.data.repository.AssessmentRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.domain.assessment.PlacementEngine
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private val DOMAIN_SEQUENCE = listOf(
    AssessmentDomain.VOCABULARY,
    AssessmentDomain.GRAMMAR,
    AssessmentDomain.READING,
    AssessmentDomain.LISTENING
)

private const val PROBES_PER_DOMAIN = 3

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val assessmentRepository: AssessmentRepository,
    private val placementEngine: PlacementEngine,
    private val userLearningRepository: UserLearningRepository,
    private val recordLearningEvidenceUseCase: RecordLearningEvidenceUseCase,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    private var allProbes: List<PlacementProbe> = emptyList()

    init {
        loadProbes()
        loadSavedAvatar()
    }

    private fun loadSavedAvatar() {
        viewModelScope.launch {
            userPreferencesRepository.selectedAvatarKey.collect { key ->
                if (key != null && _uiState.value.selectedAvatarKey == null) {
                    _uiState.update { it.copy(selectedAvatarKey = key) }
                }
            }
        }
    }

    private fun loadProbes() {
        viewModelScope.launch {
            assessmentRepository.getPlacementProbes().collect { probes ->
                allProbes = probes
            }
        }
    }

    fun selectAvatar(key: String) {
        _uiState.update { it.copy(selectedAvatarKey = key) }
    }

    fun selectGoal(goal: UserGoal) {
        _uiState.update { it.copy(selectedGoal = goal) }
    }

    fun toggleInterest(interest: LearningInterest) {
        _uiState.update { current ->
            val set = current.selectedInterests.toMutableSet()
            if (set.contains(interest)) set.remove(interest) else set.add(interest)
            current.copy(selectedInterests = set)
        }
    }

    fun selectSelfAssessment(level: SelfAssessedLevel) {
        _uiState.update { it.copy(selectedSelfAssessment = level) }
    }

    fun selectDailyTarget(minutes: Int, targetCefr: String = "C1") {
        _uiState.update {
            it.copy(
                dailyPracticeMinutes = minutes,
                targetCefrLevel = targetCefr
            )
        }
    }

    fun setDisplayName(name: String) {
        _uiState.update { it.copy(displayName = name) }
    }

    fun nextStep() {
        val next = when (_uiState.value.currentStep) {
            OnboardingStep.WELCOME -> OnboardingStep.AVATAR_SELECTION
            OnboardingStep.AVATAR_SELECTION -> {
                val avatarKey = _uiState.value.selectedAvatarKey
                if (avatarKey != null) {
                    viewModelScope.launch {
                        userPreferencesRepository.setSelectedAvatarKey(avatarKey)
                    }
                }
                OnboardingStep.GOAL_SELECTION
            }
            OnboardingStep.GOAL_SELECTION -> OnboardingStep.INTERESTS_SELECTION
            OnboardingStep.INTERESTS_SELECTION -> OnboardingStep.SELF_ASSESSMENT
            OnboardingStep.SELF_ASSESSMENT -> OnboardingStep.DAILY_TARGET
            OnboardingStep.DAILY_TARGET -> {
                startPlacementTest()
                return
            }
            OnboardingStep.PLACEMENT_TEST -> OnboardingStep.PLACEMENT_RESULT
            OnboardingStep.PLACEMENT_RESULT -> OnboardingStep.PLACEMENT_RESULT
        }
        _uiState.update { it.copy(currentStep = next) }
    }

    fun previousStep() {
        val prev = when (_uiState.value.currentStep) {
            OnboardingStep.WELCOME -> OnboardingStep.WELCOME
            OnboardingStep.AVATAR_SELECTION -> OnboardingStep.WELCOME
            OnboardingStep.GOAL_SELECTION -> OnboardingStep.AVATAR_SELECTION
            OnboardingStep.INTERESTS_SELECTION -> OnboardingStep.GOAL_SELECTION
            OnboardingStep.SELF_ASSESSMENT -> OnboardingStep.INTERESTS_SELECTION
            OnboardingStep.DAILY_TARGET -> OnboardingStep.SELF_ASSESSMENT
            OnboardingStep.PLACEMENT_TEST -> OnboardingStep.PLACEMENT_TEST
            OnboardingStep.PLACEMENT_RESULT -> OnboardingStep.PLACEMENT_RESULT
        }
        _uiState.update { it.copy(currentStep = prev) }
    }

    // -----------------------------------------------------------------------
    // Adaptive Placement Diagnostic Flow
    // -----------------------------------------------------------------------

    fun startPlacementTest() {
        val initialLevel = _uiState.value.selectedSelfAssessment.initialProbeLevel
        val initialDomain = DOMAIN_SEQUENCE.first()

        val firstProbe = placementEngine.selectNextProbe(
            domain = initialDomain,
            targetLevel = initialLevel,
            availableProbes = allProbes,
            alreadyTestedProbeIds = emptySet()
        )

        _uiState.update {
            it.copy(
                currentStep = OnboardingStep.PLACEMENT_TEST,
                isPlacementActive = true,
                currentDomain = initialDomain,
                currentTargetLevel = initialLevel,
                currentProbe = firstProbe,
                selectedOptionIndex = null,
                isAnswerSubmitted = false,
                completedResponses = emptyList(),
                testedProbeIds = if (firstProbe != null) setOf(firstProbe.id) else emptySet()
            )
        }
    }

    fun selectOption(index: Int) {
        if (_uiState.value.isAnswerSubmitted) return
        _uiState.update { it.copy(selectedOptionIndex = index) }
    }

    fun submitProbeAnswer() {
        val probe = _uiState.value.currentProbe ?: return
        val selectedIdx = _uiState.value.selectedOptionIndex ?: return

        val isCorrect = selectedIdx == probe.correctOptionIndex
        val response = PlacementResponse(
            probeId = probe.id,
            domain = _uiState.value.currentDomain,
            testedLevel = probe.cefrLevel,
            selectedOptionIndex = selectedIdx,
            isCorrect = isCorrect
        )

        // Log evidence for diagnostic learning trace
        viewModelScope.launch {
            recordLearningEvidenceUseCase(
                evidence = LearningEvidence(
                    contentId = probe.id,
                    domain = probe.domain,
                    activityType = "placement_probe",
                    isCorrect = isCorrect,
                    score = if (isCorrect) 1.0f else 0.0f,
                    details = "Tested at ${probe.cefrLevel}"
                ),
                trapType = probe.trapType,
                errorDescription = probe.explanationEn,
                userAnswer = probe.options.getOrNull(selectedIdx) ?: "",
                correctAnswer = probe.options.getOrNull(probe.correctOptionIndex) ?: ""
            )
        }

        val updatedResponses = _uiState.value.completedResponses + response
        _uiState.update {
            it.copy(
                isAnswerSubmitted = true,
                completedResponses = updatedResponses
            )
        }
    }

    fun nextProbe() {
        val lastResponse = _uiState.value.completedResponses.lastOrNull() ?: return
        val currentDomain = _uiState.value.currentDomain
        val domainResponses = _uiState.value.completedResponses.filter { it.domain == currentDomain }

        val testedIds = _uiState.value.testedProbeIds

        if (domainResponses.size < PROBES_PER_DOMAIN) {
            // Continue same domain with adaptive probe level
            val nextLevel = placementEngine.getNextProbeLevel(lastResponse.testedLevel, lastResponse.isCorrect)
            val nextProbe = placementEngine.selectNextProbe(
                domain = currentDomain,
                targetLevel = nextLevel,
                availableProbes = allProbes,
                alreadyTestedProbeIds = testedIds
            )

            _uiState.update {
                it.copy(
                    currentTargetLevel = nextLevel,
                    currentProbe = nextProbe,
                    selectedOptionIndex = null,
                    isAnswerSubmitted = false,
                    testedProbeIds = if (nextProbe != null) testedIds + nextProbe.id else testedIds
                )
            }
        } else {
            // Domain completed, advance to next domain or finish test
            val currentDomainIdx = DOMAIN_SEQUENCE.indexOf(currentDomain)
            if (currentDomainIdx + 1 < DOMAIN_SEQUENCE.size) {
                val nextDomain = DOMAIN_SEQUENCE[currentDomainIdx + 1]
                val initialLevel = _uiState.value.selectedSelfAssessment.initialProbeLevel
                val nextProbe = placementEngine.selectNextProbe(
                    domain = nextDomain,
                    targetLevel = initialLevel,
                    availableProbes = allProbes,
                    alreadyTestedProbeIds = testedIds
                )

                _uiState.update {
                    it.copy(
                        currentDomain = nextDomain,
                        currentTargetLevel = initialLevel,
                        currentProbe = nextProbe,
                        selectedOptionIndex = null,
                        isAnswerSubmitted = false,
                        testedProbeIds = if (nextProbe != null) testedIds + nextProbe.id else testedIds
                    )
                }
            } else {
                // All domains completed -> compute placement summary!
                val summary = placementEngine.evaluateAssessmentSummary(_uiState.value.completedResponses)
                val survey = OnboardingSurveyResult(
                    goal = _uiState.value.selectedGoal ?: UserGoal.CAREER_ADVANCEMENT,
                    interests = _uiState.value.selectedInterests.toList(),
                    selfAssessedLevel = _uiState.value.selectedSelfAssessment,
                    targetCefrLevel = _uiState.value.targetCefrLevel,
                    dailyPracticeMinutes = _uiState.value.dailyPracticeMinutes,
                    displayName = _uiState.value.displayName.trim().ifBlank { null }
                )
                viewModelScope.launch {
                    val profile = placementEngine.createUserProfile(survey, summary)
                    userLearningRepository.saveUserProfile(profile)
                }
                _uiState.update {
                    it.copy(
                        currentStep = OnboardingStep.PLACEMENT_RESULT,
                        isPlacementActive = false,
                        placementSummary = summary,
                        currentProbe = null,
                        selectedOptionIndex = null,
                        isAnswerSubmitted = false
                    )
                }
            }
        }
    }

    fun completeOnboarding() {
        val summary = _uiState.value.placementSummary ?: return
        val survey = OnboardingSurveyResult(
            goal = _uiState.value.selectedGoal ?: UserGoal.CAREER_ADVANCEMENT,
            interests = _uiState.value.selectedInterests.toList(),
            selfAssessedLevel = _uiState.value.selectedSelfAssessment,
            targetCefrLevel = _uiState.value.targetCefrLevel,
            dailyPracticeMinutes = _uiState.value.dailyPracticeMinutes,
            displayName = _uiState.value.displayName.trim().ifBlank { null }
        )

        _uiState.update { it.copy(isSavingProfile = true) }

        viewModelScope.launch {
            val profile = placementEngine.createUserProfile(survey, summary)
            userLearningRepository.saveUserProfile(profile)
            _uiState.value.selectedAvatarKey?.let { avatarKey ->
                userPreferencesRepository.setSelectedAvatarKey(avatarKey)
            }

            _uiState.update {
                it.copy(
                    isSavingProfile = false,
                    isCompleted = true
                )
            }
        }
    }
}
