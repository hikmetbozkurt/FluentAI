package com.seanora.fluentai.feature.listening

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.ListeningComprehensionQuestion
import com.seanora.fluentai.core.model.ListeningScenario
import com.seanora.fluentai.data.repository.ListeningRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.domain.listening.DictationItem
import com.seanora.fluentai.domain.listening.DictationResult
import com.seanora.fluentai.domain.listening.ListeningDateProvider
import com.seanora.fluentai.domain.listening.ListeningFeatureEngine
import com.seanora.fluentai.domain.listening.ListeningSkill
import com.seanora.fluentai.domain.listening.ListeningSkillItem
import com.seanora.fluentai.domain.listening.ListeningWeakSpot
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ListeningFeatureUiState(
    val isLoading: Boolean = true,
    val scenarios: List<ListeningScenario> = emptyList(),
    val dailyScenario: ListeningScenario? = null,
    val continueScenario: ListeningScenario? = null,
    val dictationItems: List<DictationItem> = emptyList(),
    val dictationIndex: Int = 0,
    val dictationAnswer: String = "",
    val dictationResult: DictationResult? = null,
    val skills: List<ListeningSkill> = emptyList(),
    val selectedSkillId: String? = null,
    val skillItemIndex: Int = 0,
    val selectedAnswers: Map<String, String> = emptyMap(),
    val submittedQuestionIds: Set<String> = emptySet(),
    val answerCorrectness: Map<String, Boolean> = emptyMap(),
    val weakSpots: List<ListeningWeakSpot> = emptyList(),
) {
    val currentDictation: DictationItem? get() = dictationItems.getOrNull(dictationIndex)
    val selectedSkill: ListeningSkill? get() = skills.firstOrNull { it.id == selectedSkillId }
    val currentSkillItem: ListeningSkillItem? get() = selectedSkill?.items?.getOrNull(skillItemIndex)
}

@HiltViewModel
class ListeningFeatureViewModel @Inject constructor(
    listeningRepository: ListeningRepository,
    userLearningRepository: UserLearningRepository,
    private val recordLearningEvidence: RecordLearningEvidenceUseCase,
    private val engine: ListeningFeatureEngine,
    private val dateProvider: ListeningDateProvider,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ListeningFeatureUiState())
    val uiState: StateFlow<ListeningFeatureUiState> = _uiState.asStateFlow()

    init {
        val now = System.currentTimeMillis()
        viewModelScope.launch {
            combine(
                listeningRepository.getAllScenarios(),
                userLearningRepository.getRecentEvidence(100),
                userLearningRepository.getAllMasterySnapshots(),
                userLearningRepository.getDueReviews(now),
                userLearningRepository.getUserProfile(),
            ) { scenarios, evidence, mastery, due, profile ->
                FeatureListeningData(
                    scenarios = scenarios,
                    daily = engine.selectDaily(scenarios, profile?.estimatedListeningLevel, dateProvider.today()),
                    continued = engine.resolveContinue(scenarios, evidence),
                    dictation = engine.dictationItems(scenarios),
                    skills = engine.deriveSkills(scenarios),
                    weakSpots = engine.weakSpots(scenarios, mastery, due, now),
                )
            }.collect { data ->
                _uiState.update { old ->
                    val validSkillIds = data.skills.mapTo(mutableSetOf()) { it.id }
                    old.copy(
                        isLoading = false,
                        scenarios = data.scenarios,
                        dailyScenario = data.daily,
                        continueScenario = data.continued,
                        dictationItems = data.dictation,
                        dictationIndex = old.dictationIndex.coerceIn(0, (data.dictation.lastIndex).coerceAtLeast(0)),
                        skills = data.skills,
                        selectedSkillId = old.selectedSkillId?.takeIf { it in validSkillIds } ?: data.skills.firstOrNull()?.id,
                        weakSpots = data.weakSpots,
                    )
                }
            }
        }
    }

    fun updateDictationAnswer(answer: String) {
        if (_uiState.value.dictationResult != null) return
        _uiState.update { it.copy(dictationAnswer = answer) }
    }

    fun submitDictation() {
        val state = _uiState.value
        val item = state.currentDictation ?: return
        if (state.dictationAnswer.isBlank() || state.dictationResult != null) return
        val result = engine.evaluateDictation(state.dictationAnswer, item.transcript.textEn)
        _uiState.update { it.copy(dictationResult = result) }
        viewModelScope.launch {
            recordLearningEvidence(
                LearningEvidence(
                    contentId = item.scenario.id,
                    domain = "listening",
                    activityType = "dictation",
                    isCorrect = result.isCorrect,
                    score = if (result.isCorrect) 1f else 0f,
                    details = "transcript_index=${item.transcript.index},matched=${result.matchedTokens}/${result.totalTokens}",
                ),
            )
        }
    }

    fun nextDictation() {
        val size = _uiState.value.dictationItems.size
        if (size == 0) return
        _uiState.update {
            it.copy(
                dictationIndex = (it.dictationIndex + 1) % size,
                dictationAnswer = "",
                dictationResult = null,
            )
        }
    }

    fun selectSkill(skillId: String) {
        if (_uiState.value.skills.none { it.id == skillId }) return
        _uiState.update { it.copy(selectedSkillId = skillId, skillItemIndex = 0) }
    }

    fun nextSkillItem() = _uiState.update { state ->
        val size = state.selectedSkill?.items?.size ?: 0
        state.copy(skillItemIndex = if (size == 0) 0 else (state.skillItemIndex + 1) % size)
    }

    fun selectAnswer(questionId: String, answer: String) {
        if (questionId in _uiState.value.submittedQuestionIds) return
        _uiState.update { it.copy(selectedAnswers = it.selectedAnswers + (questionId to answer)) }
    }

    fun submitSelectedAnswer(scenarioId: String, question: ListeningComprehensionQuestion) {
        val answer = _uiState.value.selectedAnswers[question.id] ?: return
        if (question.id in _uiState.value.submittedQuestionIds) return
        val scenarioExists = _uiState.value.scenarios.any { it.id == scenarioId && question in it.comprehensionQuestions }
        if (!scenarioExists || answer !in question.options) return
        val correct = answer.trim() == question.correctAnswer.trim()
        _uiState.update {
            it.copy(
                submittedQuestionIds = it.submittedQuestionIds + question.id,
                answerCorrectness = it.answerCorrectness + (question.id to correct),
            )
        }
        viewModelScope.launch {
            recordLearningEvidence(
                LearningEvidence(
                    contentId = scenarioId,
                    domain = "listening",
                    activityType = "listening_skills",
                    isCorrect = correct,
                    score = if (correct) 1f else 0f,
                    details = "question_id=${question.id},selected=$answer,correct=${question.correctAnswer}",
                ),
            )
        }
    }

    private data class FeatureListeningData(
        val scenarios: List<ListeningScenario>,
        val daily: ListeningScenario?,
        val continued: ListeningScenario?,
        val dictation: List<DictationItem>,
        val skills: List<ListeningSkill>,
        val weakSpots: List<ListeningWeakSpot>,
    )
}
