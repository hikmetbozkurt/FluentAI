package com.seanora.fluentai.feature.vocabulary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanora.fluentai.core.model.*
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.data.repository.VocabularyFeatureStateRepository
import com.seanora.fluentai.data.repository.VocabularyRepository
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import com.seanora.fluentai.domain.vocabulary.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

open class VocabularyDateProvider @Inject constructor() {
    open fun today(): String = LocalDate.now().toString()
}

data class VocabularyFeatureUiState(
    val isLoading: Boolean = true,
    val featureState: VocabularyFeatureState = VocabularyFeatureState(),
    val dailyWord: VocabItem? = null,
    val isDailyTurkishRevealed: Boolean = false,
    val collections: List<VocabularyCollection> = emptyList(),
    val selectedLevel: String? = null,
    val selectedCollection: VocabularyCollection? = null,
    val collectionWords: List<VocabItem> = emptyList(),
    val flashCards: List<VocabItem> = emptyList(),
    val allItems: List<VocabItem> = emptyList(),
    val isFlashPractice: Boolean = false,
    val flashIndex: Int = 0,
    val isFlashRevealed: Boolean = false,
    val isFlashTurkishRevealed: Boolean = false,
    val practiceSession: VocabularyPracticeSession? = null,
    val practiceIndex: Int = 0,
    val selectedPracticeAnswer: String? = null,
    val submittedPracticeAnswer: Boolean = false,
    val lastPracticeCorrect: Boolean? = null,
    val practiceCorrectCount: Int = 0,
    val practiceIncorrectIds: Set<String> = emptySet(),
    val practiceSecondsRemaining: Int? = null,
    val practiceQuestionStartedAt: Long = 0L,
    val errorMessage: String? = null,
) {
    val currentFlashCard: VocabItem? get() = flashCards.getOrNull(flashIndex)
    val currentPracticeQuestion: VocabularyPracticeQuestion? get() = practiceSession?.questions?.getOrNull(practiceIndex)
    val isFlashComplete: Boolean get() = flashCards.isNotEmpty() && flashIndex >= flashCards.size
    val isPracticeComplete: Boolean get() = practiceSession?.questions?.isNotEmpty() == true && practiceIndex >= practiceSession.questions.size
}

@HiltViewModel
class VocabularyFeatureViewModel @Inject constructor(
    private val vocabularyRepository: VocabularyRepository,
    private val featureStateRepository: VocabularyFeatureStateRepository,
    private val userLearningRepository: UserLearningRepository,
    private val recordEvidence: RecordLearningEvidenceUseCase,
    private val dailySelector: VocabularyDailySelector,
    private val practiceEngine: VocabularyPracticeEngine,
    private val flashCardSelector: VocabularyFlashCardSelector,
    private val dateProvider: VocabularyDateProvider,
) : ViewModel() {
    private val _uiState = MutableStateFlow(VocabularyFeatureUiState())
    val uiState: StateFlow<VocabularyFeatureUiState> = _uiState.asStateFlow()

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            runCatching {
                val saved = featureStateRepository.getState()
                val profile = userLearningRepository.getUserProfileSync()
                val items = vocabularyRepository.getAllVocabSnapshot()
                val today = dateProvider.today()
                val daily = dailySelector.selectDailyWord(
                    localDate = today,
                    preferredLevel = profile?.estimatedVocabLevel ?: "B2",
                    persistedDate = saved.dailyWordDate,
                    persistedContentId = saved.dailyWordContentId,
                    items = items,
                )
                if (daily != null && (saved.dailyWordDate != today || saved.dailyWordContentId != daily.id)) {
                    featureStateRepository.saveDailyWord(today, daily.id)
                }
                val effectiveState = featureStateRepository.getState()
                val due = userLearningRepository.getDueReviews(System.currentTimeMillis()).first()
                val mastery = userLearningRepository.getAllMasterySnapshots().first()
                val cards = flashCardSelector.selectFlashCards(items, due, mastery, System.currentTimeMillis())
                VocabularyFeatureUiState(
                    isLoading = false,
                    featureState = effectiveState,
                    dailyWord = daily,
                    collections = vocabularyRepository.getCollectionSummaries(),
                    flashCards = cards,
                    allItems = items,
                )
            }.onSuccess { _uiState.value = it }
                .onFailure { _uiState.value = VocabularyFeatureUiState(isLoading = false, errorMessage = "Vocabulary could not be loaded.") }
        }
    }

    fun toggleDailyTurkish() {
        _uiState.update { it.copy(isDailyTurkishRevealed = !it.isDailyTurkishRevealed) }
        _uiState.value.dailyWord?.let { word -> saveResume(VocabularyResumeDestination.WORD_OF_DAY, word.id) }
    }

    fun addDailyWordToReview() {
        val word = _uiState.value.dailyWord ?: return
        viewModelScope.launch {
            val existing = userLearningRepository.getReviewScheduleSync(word.id)
            if (existing == null) {
                userLearningRepository.saveReviewSchedule(
                    ReviewSchedule(word.id, "vocabulary", System.currentTimeMillis(), 1f)
                )
            }
            saveResumeNow(VocabularyResumeDestination.WORD_OF_DAY, word.id)
        }
    }

    fun filterCollections(level: String?) {
        viewModelScope.launch {
            val collections = vocabularyRepository.getCollectionSummaries(level)
            _uiState.update { it.copy(selectedLevel = level, collections = collections, selectedCollection = null, collectionWords = emptyList()) }
        }
    }

    fun selectCollection(collection: VocabularyCollection) {
        viewModelScope.launch {
            val words = vocabularyRepository.getCollectionWords(collection.topicId, _uiState.value.selectedLevel)
            _uiState.update { it.copy(selectedCollection = collection, collectionWords = words) }
            saveResumeNow(VocabularyResumeDestination.COLLECTIONS, contextId = collection.topicId)
        }
    }

    fun clearCollection() = _uiState.update { it.copy(selectedCollection = null, collectionWords = emptyList()) }

    fun revealFlashCard() {
        val card = _uiState.value.currentFlashCard ?: return
        _uiState.update { it.copy(isFlashRevealed = true) }
        saveResume(VocabularyResumeDestination.FLASH_CARDS, card.id)
    }

    fun toggleFlashTurkish() = _uiState.update { it.copy(isFlashTurkishRevealed = !it.isFlashTurkishRevealed) }

    fun rateFlashCard(outcome: FlashRecallOutcome) {
        val card = _uiState.value.currentFlashCard ?: return
        viewModelScope.launch {
            recordEvidence(flashCardSelector.recallEvidence(card.id, outcome, System.currentTimeMillis()))
            _uiState.update {
                it.copy(flashIndex = it.flashIndex + 1, isFlashRevealed = false, isFlashTurkishRevealed = false)
            }
        }
    }

    fun startPractice(mode: VocabularyPracticeMode, candidateIds: Set<String>? = null) {
        viewModelScope.launch {
            val items = vocabularyRepository.getAllVocabSnapshot().filter { candidateIds == null || it.id in candidateIds }
            val session = practiceEngine.buildSession(mode, items, dateProvider.today())
            _uiState.update {
                it.copy(practiceSession = session, isFlashPractice = false, practiceIndex = 0, selectedPracticeAnswer = null, submittedPracticeAnswer = false, lastPracticeCorrect = null)
                    .copy(practiceCorrectCount = 0, practiceIncorrectIds = emptySet(), practiceSecondsRemaining = session.timeLimitSeconds, practiceQuestionStartedAt = System.currentTimeMillis())
            }
            saveResumeNow(VocabularyResumeDestination.PRACTICE_LABS, practiceMode = mode)
        }
    }

    fun availablePracticeModes(candidateIds: Set<String>? = null): List<VocabularyPracticeMode> {
        val candidates = _uiState.value.allItems.filter { candidateIds == null || it.id in candidateIds }
        return VocabularyPracticeMode.entries.filter { mode ->
            practiceEngine.buildSession(mode, candidates, dateProvider.today()).questions.isNotEmpty()
        }
    }

    fun startSmartPractice(candidateIds: Set<String>? = null) {
        viewModelScope.launch {
            val state = _uiState.value
            val scoped = state.allItems.filter { candidateIds == null || it.id in candidateIds }
            val prioritized = state.flashCards.filter { candidateIds == null || it.id in candidateIds }
            if (prioritized.isNotEmpty()) {
                _uiState.update {
                    it.copy(
                        isFlashPractice = true,
                        practiceSession = null,
                        flashCards = prioritized,
                        flashIndex = 0,
                        isFlashRevealed = false,
                        isFlashTurkishRevealed = false,
                    )
                }
                saveResumeNow(VocabularyResumeDestination.FLASH_CARDS, prioritized.first().id)
                return@launch
            }
            val modes = listOf(
                VocabularyPracticeMode.WORD_MATCH,
                VocabularyPracticeMode.MEANING_MATCH,
                VocabularyPracticeMode.FILL_IN_THE_BLANK,
                VocabularyPracticeMode.CONTEXT_CHOICE,
                VocabularyPracticeMode.COLLOCATION_MATCH,
                VocabularyPracticeMode.SPEED_DRILL,
            )
            val session = modes.asSequence()
                .map { practiceEngine.buildSession(it, scoped, dateProvider.today()) }
                .firstOrNull { it.questions.isNotEmpty() }
                ?: practiceEngine.buildSession(VocabularyPracticeMode.WORD_MATCH, scoped, dateProvider.today())
            _uiState.update {
                it.copy(
                    practiceSession = session,
                    isFlashPractice = false,
                    practiceIndex = 0,
                    selectedPracticeAnswer = null,
                    submittedPracticeAnswer = false,
                    lastPracticeCorrect = null,
                    practiceCorrectCount = 0,
                    practiceIncorrectIds = emptySet(),
                    practiceSecondsRemaining = session.timeLimitSeconds,
                    practiceQuestionStartedAt = System.currentTimeMillis(),
                )
            }
            saveResumeNow(VocabularyResumeDestination.PRACTICE_LABS, practiceMode = session.mode)
        }
    }

    fun startFlashPractice(candidateIds: Set<String>? = null) {
        val scopedCards = if (candidateIds == null) _uiState.value.flashCards else {
            val preferred = _uiState.value.flashCards.filter { it.id in candidateIds }
            if (preferred.isNotEmpty()) preferred else _uiState.value.allItems.filter { it.id in candidateIds }
        }
        _uiState.update {
            it.copy(isFlashPractice = true, practiceSession = null, flashCards = scopedCards, flashIndex = 0, isFlashRevealed = false, isFlashTurkishRevealed = false)
        }
        saveResume(VocabularyResumeDestination.FLASH_CARDS, _uiState.value.currentFlashCard?.id)
    }

    fun exitPractice() = _uiState.update { it.copy(practiceSession = null, isFlashPractice = false, practiceIndex = 0) }
    fun selectPracticeAnswer(answer: String) = _uiState.update { it.copy(selectedPracticeAnswer = answer) }

    fun submitPracticeAnswer() {
        val state = _uiState.value
        val question = state.currentPracticeQuestion ?: return
        val answer = state.selectedPracticeAnswer ?: return
        if (state.submittedPracticeAnswer) return
        val correct = practiceEngine.evaluateAnswer(question, answer)
        recordPracticeResult(question, correct)
    }

    fun tickPracticeTimer() {
        val state = _uiState.value
        val remaining = state.practiceSecondsRemaining ?: return
        val question = state.currentPracticeQuestion ?: return
        if (state.submittedPracticeAnswer) return
        if (remaining <= 1) {
            recordPracticeResult(question, false)
        } else {
            _uiState.update { it.copy(practiceSecondsRemaining = remaining - 1) }
        }
    }

    private fun recordPracticeResult(question: VocabularyPracticeQuestion, correct: Boolean) {
        val state = _uiState.value
        if (state.submittedPracticeAnswer) return
        _uiState.update {
            it.copy(
                submittedPracticeAnswer = true,
                lastPracticeCorrect = correct,
                practiceCorrectCount = it.practiceCorrectCount + if (correct) 1 else 0,
                practiceIncorrectIds = if (correct) it.practiceIncorrectIds else it.practiceIncorrectIds + question.targetContentId,
                practiceSecondsRemaining = 0,
            )
        }
        viewModelScope.launch {
            recordEvidence(
                LearningEvidence(
                    contentId = question.targetContentId,
                    domain = "vocabulary",
                    activityType = "practice_${state.practiceSession?.mode?.name?.lowercase()}",
                    isCorrect = correct,
                    score = if (correct) 1f else 0f,
                    responseTimeMs = (System.currentTimeMillis() - state.practiceQuestionStartedAt).coerceAtLeast(0L),
                    details = "question_id=${question.id}",
                )
            )
        }
    }

    fun nextPracticeQuestion() = _uiState.update {
        it.copy(
            practiceIndex = it.practiceIndex + 1,
            selectedPracticeAnswer = null,
            submittedPracticeAnswer = false,
            lastPracticeCorrect = null,
            practiceSecondsRemaining = it.practiceSession?.timeLimitSeconds,
            practiceQuestionStartedAt = System.currentTimeMillis(),
        )
    }

    fun recordLibrarySelection(contentId: String) = saveResume(VocabularyResumeDestination.LIBRARY, contentId)

    private fun saveResume(destination: VocabularyResumeDestination, contentId: String? = null, contextId: String? = null, practiceMode: VocabularyPracticeMode? = null) {
        viewModelScope.launch { saveResumeNow(destination, contentId, contextId, practiceMode) }
    }

    private suspend fun saveResumeNow(destination: VocabularyResumeDestination, contentId: String? = null, contextId: String? = null, practiceMode: VocabularyPracticeMode? = null) {
        featureStateRepository.saveResume(destination, contentId, contextId, practiceMode)
        _uiState.update { it.copy(featureState = featureStateRepository.getState()) }
    }
}
