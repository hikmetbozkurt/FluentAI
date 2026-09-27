package com.seanora.fluentai.feature.grammar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanora.fluentai.core.model.Exercise
import com.seanora.fluentai.core.model.GrammarFeatureState
import com.seanora.fluentai.core.model.GrammarLesson
import com.seanora.fluentai.core.model.GrammarResumeDestination
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.data.repository.GrammarFeatureStateRepository
import com.seanora.fluentai.data.repository.GrammarRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.domain.grammar.ErrorSpotterQuestion
import com.seanora.fluentai.domain.grammar.GrammarFeatureEngine
import com.seanora.fluentai.domain.grammar.GrammarWeakSpot
import com.seanora.fluentai.domain.grammar.SentenceBuilderQuestion
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GrammarFeatureUiState(
    val isLoading: Boolean = true,
    val lessons: List<GrammarLesson> = emptyList(),
    val sentenceQuestions: List<SentenceBuilderQuestion> = emptyList(),
    val sentenceIndex: Int = 0,
    val builtTokens: List<String> = emptyList(),
    val sentenceSubmitted: Boolean = false,
    val sentenceCorrect: Boolean? = null,
    val errorQuestions: List<ErrorSpotterQuestion> = emptyList(),
    val errorIndex: Int = 0,
    val errorSelection: String? = null,
    val errorSubmitted: Boolean = false,
    val errorCorrect: Boolean? = null,
    val activeLessonId: String? = null,
    val selectedBiteId: String? = null,
    val selectedContextId: String? = null,
    val comparePrimaryId: String? = null,
    val compareSecondaryId: String? = null,
    val comparisonCandidates: List<GrammarLesson> = emptyList(),
    val weakSpots: List<GrammarWeakSpot> = emptyList(),
    val persistedState: GrammarFeatureState = GrammarFeatureState(),
    val resumableState: GrammarFeatureState? = null,
) {
    val currentSentence: SentenceBuilderQuestion? get() = sentenceQuestions.getOrNull(sentenceIndex)
    val currentError: ErrorSpotterQuestion? get() = errorQuestions.getOrNull(errorIndex)
    val selectedBite: GrammarLesson? get() = lessons.firstOrNull { it.id == selectedBiteId }
    val selectedContext: GrammarLesson? get() = lessons.firstOrNull { it.id == selectedContextId }
    val comparePrimary: GrammarLesson? get() = lessons.firstOrNull { it.id == comparePrimaryId }
    val compareSecondary: GrammarLesson? get() = lessons.firstOrNull { it.id == compareSecondaryId }
}

@HiltViewModel
class GrammarFeatureViewModel @Inject constructor(
    private val grammarRepository: GrammarRepository,
    private val stateRepository: GrammarFeatureStateRepository,
    private val userLearningRepository: UserLearningRepository,
    private val recordLearningEvidence: RecordLearningEvidenceUseCase,
    private val engine: GrammarFeatureEngine,
) : ViewModel() {
    private val _uiState = MutableStateFlow(GrammarFeatureUiState())
    val uiState: StateFlow<GrammarFeatureUiState> = _uiState.asStateFlow()

    init {
        val now = System.currentTimeMillis()
        viewModelScope.launch {
            combine(
                grammarRepository.getAllLessons(),
                userLearningRepository.getAllMasterySnapshots(),
                userLearningRepository.getActiveMistakes(),
                userLearningRepository.getDueReviews(now),
                stateRepository.observeState(),
            ) { lessons, mastery, mistakes, dueReviews, persisted ->
                FeatureData(lessons, mastery, mistakes, dueReviews, persisted)
            }.collect { data ->
                val validIds = data.lessons.mapTo(mutableSetOf()) { it.id }
                val resumed = engine.resolveResume(data.persisted, validIds)
                _uiState.update { old ->
                    val preferred = resumed?.resumeContentId?.takeIf { it in validIds }
                    val first = data.lessons.firstOrNull()?.id
                    val sentenceQuestions = engine.buildSentenceBuilders(data.lessons)
                    val errorQuestions = engine.buildErrorSpotters(data.lessons)
                    val sentenceIndex = if (old.isLoading && resumed?.resumeDestination == GrammarResumeDestination.SENTENCE_BUILDER) {
                        sentenceQuestions.indexOfFirst { it.lessonId == preferred }.coerceAtLeast(0)
                    } else old.sentenceIndex.coerceIn(0, (sentenceQuestions.lastIndex).coerceAtLeast(0))
                    val errorIndex = if (old.isLoading && resumed?.resumeDestination == GrammarResumeDestination.ERROR_SPOTTER) {
                        errorQuestions.indexOfFirst { it.lessonId == preferred }.coerceAtLeast(0)
                    } else old.errorIndex.coerceIn(0, (errorQuestions.lastIndex).coerceAtLeast(0))
                    val persistedSecondary = resumed?.resumeSecondaryContentId?.takeIf { it in validIds && it != (preferred ?: first) }
                    val comparePrimaryId = old.comparePrimaryId?.takeIf { it in validIds } ?: preferred ?: first
                    val comparisonCandidates = comparePrimaryId?.let { engine.comparisonCandidates(it, data.lessons) }.orEmpty()
                    old.copy(
                        isLoading = false,
                        lessons = data.lessons,
                        sentenceQuestions = sentenceQuestions,
                        sentenceIndex = sentenceIndex,
                        errorQuestions = errorQuestions,
                        errorIndex = errorIndex,
                        selectedBiteId = old.selectedBiteId?.takeIf { it in validIds } ?: preferred ?: first,
                        selectedContextId = old.selectedContextId?.takeIf { it in validIds } ?: preferred ?: first,
                        comparePrimaryId = comparePrimaryId,
                        compareSecondaryId = old.compareSecondaryId?.takeIf { it in validIds && it != (preferred ?: first) }
                            ?: persistedSecondary
                            ?: comparisonCandidates.firstOrNull()?.id,
                        comparisonCandidates = comparisonCandidates,
                        weakSpots = engine.rankWeakSpots(data.lessons, data.mastery, data.mistakes, data.dueReviews, now),
                        persistedState = data.persisted,
                        resumableState = resumed,
                    )
                }
            }
        }
    }

    fun selectBite(id: String) = selectLesson(id, GrammarResumeDestination.GRAMMAR_BITES) {
        copy(selectedBiteId = id)
    }

    fun activateLesson(id: String): Boolean {
        val state = _uiState.value
        if (state.lessons.none { it.id == id }) {
            _uiState.update { it.copy(activeLessonId = null) }
            return false
        }
        val sentenceIndex = state.sentenceQuestions.indexOfFirst { it.lessonId == id }.takeIf { it >= 0 }
            ?: state.sentenceIndex
        val errorIndex = state.errorQuestions.indexOfFirst { it.lessonId == id }.takeIf { it >= 0 }
            ?: state.errorIndex
        _uiState.update {
            it.copy(
                activeLessonId = id,
                sentenceIndex = sentenceIndex,
                builtTokens = emptyList(),
                sentenceSubmitted = false,
                sentenceCorrect = null,
                errorIndex = errorIndex,
                errorSelection = null,
                errorSubmitted = false,
                errorCorrect = null,
            )
        }
        saveResume(GrammarResumeDestination.LIBRARY, id)
        return true
    }

    fun selectContext(id: String) = selectLesson(id, GrammarResumeDestination.GRAMMAR_IN_CONTEXT) {
        copy(selectedContextId = id)
    }

    fun selectComparePrimary(id: String) {
        val candidates = engine.comparisonCandidates(id, _uiState.value.lessons)
        val secondary = _uiState.value.compareSecondaryId?.takeIf { it != id }
            ?: candidates.firstOrNull()?.id
        _uiState.update {
            it.copy(
                comparePrimaryId = id,
                compareSecondaryId = secondary,
                comparisonCandidates = candidates,
            )
        }
        saveResume(GrammarResumeDestination.COMPARE_STRUCTURES, id, secondary)
    }

    fun selectCompareSecondary(id: String) {
        if (id == _uiState.value.comparePrimaryId) return
        _uiState.update { it.copy(compareSecondaryId = id) }
        _uiState.value.comparePrimaryId?.let {
            primary -> saveResume(GrammarResumeDestination.COMPARE_STRUCTURES, primary, id)
        }
    }

    fun addSentenceToken(token: String) {
        val question = _uiState.value.currentSentence ?: return
        if (_uiState.value.sentenceSubmitted) return
        val usedCount = _uiState.value.builtTokens.count { it == token }
        val availableCount = question.scrambledTokens.count { it == token }
        if (usedCount < availableCount) _uiState.update { it.copy(builtTokens = it.builtTokens + token) }
        saveResume(GrammarResumeDestination.SENTENCE_BUILDER, question.lessonId)
    }

    fun removeSentenceToken(index: Int) {
        if (_uiState.value.sentenceSubmitted) return
        _uiState.update { state -> state.copy(builtTokens = state.builtTokens.filterIndexed { i, _ -> i != index }) }
    }

    fun clearSentence() = _uiState.update {
        it.copy(
            builtTokens = emptyList(),
            sentenceSubmitted = false,
            sentenceCorrect = null,
        )
    }

    fun submitSentence(): Boolean? {
        val state = _uiState.value
        val question = state.currentSentence ?: return null
        if (state.sentenceSubmitted || state.builtTokens.isEmpty()) return null
        val correct = engine.evaluateSentence(question, state.builtTokens)
        _uiState.update { it.copy(sentenceSubmitted = true, sentenceCorrect = correct) }
        recordAnswer(
            contentId = question.lessonId,
            activityType = "sentence_builder",
            correct = correct,
            userAnswer = state.builtTokens.joinToString(" "),
            correctAnswer = question.correctSentence,
            trapType = if (correct) null else "sentence_order",
            errorDescription = "Sentence tokens were not arranged in the validated example order",
        )
        return correct
    }

    fun nextSentence() {
        val questions = _uiState.value.sentenceQuestions
        if (questions.isEmpty()) return
        val eligibleIndices = questions.indices.filter { index ->
            _uiState.value.activeLessonId == null || questions[index].lessonId == _uiState.value.activeLessonId
        }
        val nextIndex = eligibleIndices.firstOrNull { it > _uiState.value.sentenceIndex }
            ?: eligibleIndices.firstOrNull()
            ?: return
        _uiState.update {
            it.copy(
                sentenceIndex = nextIndex,
                builtTokens = emptyList(),
                sentenceSubmitted = false,
                sentenceCorrect = null,
            )
        }
    }

    fun selectErrorAnswer(answer: String) {
        val question = _uiState.value.currentError ?: return
        if (_uiState.value.errorSubmitted) return
        _uiState.update { it.copy(errorSelection = answer) }
        saveResume(GrammarResumeDestination.ERROR_SPOTTER, question.lessonId)
    }

    fun submitErrorAnswer(): Boolean? {
        val state = _uiState.value
        val question = state.currentError ?: return null
        val answer = state.errorSelection ?: return null
        if (state.errorSubmitted) return null
        val correct = answer == question.correctSentence
        _uiState.update { it.copy(errorSubmitted = true, errorCorrect = correct) }
        recordAnswer(
            contentId = question.lessonId,
            activityType = "error_spotter",
            correct = correct,
            userAnswer = answer,
            correctAnswer = question.correctSentence,
            trapType = question.trapType,
            errorDescription = question.trapTitle,
        )
        return correct
    }

    fun nextError() {
        val questions = _uiState.value.errorQuestions
        if (questions.isEmpty()) return
        val eligibleIndices = questions.indices.filter { index ->
            _uiState.value.activeLessonId == null || questions[index].lessonId == _uiState.value.activeLessonId
        }
        val nextIndex = eligibleIndices.firstOrNull { it > _uiState.value.errorIndex }
            ?: eligibleIndices.firstOrNull()
            ?: return
        _uiState.update {
            it.copy(
                errorIndex = nextIndex,
                errorSelection = null,
                errorSubmitted = false,
                errorCorrect = null,
            )
        }
    }

    fun recordLibrarySelection(contentId: String) =
        saveResume(GrammarResumeDestination.LIBRARY, contentId)

    fun recordLibraryExercise(exercise: Exercise, answer: String) {
        val correct = answer == exercise.correctAnswer
        val lesson = _uiState.value.lessons.firstOrNull { it.id == exercise.targetContentId }
        val matchingTrap = lesson?.turkishTraps?.firstOrNull { trap ->
            trap.incorrectExample == answer || trap.correctExample == exercise.correctAnswer
        }
        recordAnswer(
            contentId = exercise.targetContentId,
            activityType = "grammar_library_exercise",
            correct = correct,
            userAnswer = answer,
            correctAnswer = exercise.correctAnswer,
            trapType = matchingTrap?.trapType,
            errorDescription = matchingTrap?.trapTitle,
        )
        recordLibrarySelection(exercise.targetContentId)
    }

    fun targetWeakSpot(contentId: String) =
        saveResume(GrammarResumeDestination.WEAK_SPOTS, contentId)

    private fun selectLesson(
        id: String,
        destination: GrammarResumeDestination,
        transform: GrammarFeatureUiState.() -> GrammarFeatureUiState,
    ) {
        if (_uiState.value.lessons.none { it.id == id }) return
        _uiState.update { it.transform() }
        saveResume(destination, id)
    }

    private fun recordAnswer(
        contentId: String,
        activityType: String,
        correct: Boolean,
        userAnswer: String,
        correctAnswer: String,
        trapType: String?,
        errorDescription: String?,
    ) {
        viewModelScope.launch {
            recordLearningEvidence(
                evidence = LearningEvidence(
                    contentId = contentId,
                    domain = "grammar",
                    activityType = activityType,
                    isCorrect = correct,
                    score = if (correct) 1f else 0f,
                    details = "selected=$userAnswer;expected=$correctAnswer",
                ),
                trapType = trapType,
                errorDescription = errorDescription,
                userAnswer = userAnswer,
                correctAnswer = correctAnswer,
            )
        }
    }

    private fun saveResume(
        destination: GrammarResumeDestination,
        contentId: String,
        secondaryContentId: String? = null,
    ) {
        viewModelScope.launch { stateRepository.saveResume(destination, contentId, secondaryContentId) }
    }

    private data class FeatureData(
        val lessons: List<GrammarLesson>,
        val mastery: List<com.seanora.fluentai.core.model.MasterySnapshot>,
        val mistakes: List<com.seanora.fluentai.core.model.MistakeRecord>,
        val dueReviews: List<com.seanora.fluentai.core.model.ReviewSchedule>,
        val persisted: GrammarFeatureState,
    )
}
