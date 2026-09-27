package com.seanora.fluentai.feature.listening

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanora.fluentai.core.audio.AudioPlaybackEngine
import com.seanora.fluentai.core.audio.PlaybackState
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.ListeningComprehensionQuestion
import com.seanora.fluentai.core.model.ListeningScenario
import com.seanora.fluentai.core.model.TranscriptItem
import com.seanora.fluentai.data.repository.ListeningRepository
import com.seanora.fluentai.domain.listening.DictationResult
import com.seanora.fluentai.domain.listening.ListeningFeatureEngine
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private data class ListeningSessionState(
    val requestedContentId: String? = null,
    val selectedScenario: ListeningScenario? = null,
    val section: ListeningSessionSection = ListeningSessionSection.LISTEN,
    val currentQuestionIndex: Int = 0,
    val transcriptRevealed: Boolean = false,
    val revealedTranslations: Set<Int> = emptySet(),
    val answers: Map<String, String> = emptyMap(),
    val submittedQuestions: Set<String> = emptySet(),
    val isDictationActive: Boolean = false,
    val dictationIndex: Int = 0,
    val dictationAnswer: String = "",
    val dictationResult: DictationResult? = null,
    val contentError: String? = null,
    val isResolving: Boolean = false,
)

@HiltViewModel
class ListeningViewModel @Inject constructor(
    private val listeningRepository: ListeningRepository,
    private val audioPlaybackEngine: AudioPlaybackEngine,
    private val recordLearningEvidenceUseCase: RecordLearningEvidenceUseCase,
    private val featureEngine: ListeningFeatureEngine,
) : ViewModel() {

    private val _selectedLevel = MutableStateFlow<String?>(null)
    private val _selectedCategory = MutableStateFlow<String?>(null)
    private val _searchQuery = MutableStateFlow("")
    private val _session = MutableStateFlow(ListeningSessionState())

    private val libraryState = combine(
        listeningRepository.getAllScenarios(),
        _selectedLevel,
        _selectedCategory,
        _searchQuery,
    ) { allScenarios, level, category, query ->
        val normalizedQuery = query.trim()
        LibraryState(
            scenarios = allScenarios.filter { scenario ->
                (level == null || scenario.cefrLevel.equals(level, ignoreCase = true)) &&
                    (category == null || scenario.category.equals(category, ignoreCase = true)) &&
                    (normalizedQuery.isBlank() || listOf(
                        scenario.title,
                        scenario.scenarioContext,
                        scenario.category,
                        scenario.topicTags.joinToString(" "),
                    ).any { it.contains(normalizedQuery, ignoreCase = true) })
            },
            categories = allScenarios.map { it.category }.filter { it.isNotBlank() }.distinct().sorted(),
            level = level,
            category = category,
            query = query,
        )
    }

    val uiState: StateFlow<ListeningUiState> = combine(
        libraryState,
        _session,
        audioPlaybackEngine.playbackState,
    ) { library, session, playback ->
        session.toUiState(library, playback)
    }.catch { throwable ->
        android.util.Log.e("ListeningViewModel", "Failed to load listening content", throwable)
        emit(ListeningUiState(errorMessage = "Content could not be loaded. Please try again."))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ListeningUiState(isLoading = true),
    )

    fun selectLevel(level: String?) {
        _selectedLevel.value = level
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectScenario(scenario: ListeningScenario) {
        if (_session.value.selectedScenario?.id != scenario.id) {
            audioPlaybackEngine.stop()
            _session.value = ListeningSessionState(
                requestedContentId = scenario.id,
                selectedScenario = scenario,
            )
        }
    }

    fun selectScenarioById(contentId: String) {
        if (_session.value.selectedScenario?.id == contentId) return
        _session.value = ListeningSessionState(requestedContentId = contentId, isResolving = true)
        viewModelScope.launch {
            val scenario = listeningRepository.getScenarioById(contentId).first()
            if (_session.value.requestedContentId != contentId) return@launch
            if (scenario == null) {
                audioPlaybackEngine.stop()
                _session.update {
                    it.copy(
                        selectedScenario = null,
                        contentError = "Listening recording not found.",
                        isResolving = false,
                    )
                }
            } else {
                selectScenario(scenario)
            }
        }
    }

    fun clearSelectedScenario() {
        audioPlaybackEngine.stop()
        _session.value = ListeningSessionState()
    }

    fun selectSessionSection(section: ListeningSessionSection) {
        _session.update { it.copy(section = section, isDictationActive = false) }
    }

    fun showNextQuestion() {
        _session.update { state ->
            val lastIndex = state.selectedScenario?.comprehensionQuestions?.lastIndex ?: 0
            state.copy(currentQuestionIndex = (state.currentQuestionIndex + 1).coerceAtMost(lastIndex))
        }
    }

    fun showPreviousQuestion() {
        _session.update { it.copy(currentQuestionIndex = (it.currentQuestionIndex - 1).coerceAtLeast(0)) }
    }

    fun togglePlayPause() {
        val scenario = _session.value.selectedScenario ?: return
        val playback = audioPlaybackEngine.playbackState.value
        if (playback.isPlaying) {
            audioPlaybackEngine.pause()
        } else if (playback.currentMediaUri == scenario.audioRef && playback.currentPositionMs > 0 && !playback.isCompleted) {
            audioPlaybackEngine.resume()
        } else {
            audioPlaybackEngine.play(scenario.audioRef, 0L)
        }
    }

    fun replayAudio() {
        val scenario = _session.value.selectedScenario ?: return
        audioPlaybackEngine.play(scenario.audioRef, 0L)
    }

    fun seekTo(positionMs: Long) {
        val duration = uiState.value.durationMs
        audioPlaybackEngine.seekTo(positionMs.coerceIn(0L, duration.coerceAtLeast(0L)))
    }

    fun seekBy(deltaMs: Long) {
        seekTo(audioPlaybackEngine.playbackState.value.currentPositionMs + deltaMs)
    }

    fun replaySentence(item: TranscriptItem) {
        val scenario = _session.value.selectedScenario ?: return
        val belongsToScenario = scenario.transcriptItems.any {
            it.index == item.index && it.startMs == item.startMs && it.textEn == item.textEn
        }
        if (!belongsToScenario) return
        val playback = audioPlaybackEngine.playbackState.value
        if (playback.currentMediaUri == scenario.audioRef) {
            audioPlaybackEngine.seekTo(item.startMs)
            if (!playback.isPlaying) audioPlaybackEngine.resume()
        } else {
            audioPlaybackEngine.play(scenario.audioRef, item.startMs)
        }
    }

    fun toggleTranscriptReveal() {
        _session.update { it.copy(transcriptRevealed = !it.transcriptRevealed) }
    }

    fun toggleSentenceTranslation(index: Int) {
        _session.update { state ->
            state.copy(
                revealedTranslations = if (index in state.revealedTranslations) {
                    state.revealedTranslations - index
                } else {
                    state.revealedTranslations + index
                },
            )
        }
    }

    fun selectAnswer(questionId: String, option: String) {
        val state = _session.value
        val question = state.selectedScenario?.comprehensionQuestions?.firstOrNull { it.id == questionId } ?: return
        if (questionId in state.submittedQuestions || option !in question.options) return
        _session.update { it.copy(answers = it.answers + (questionId to option)) }
    }

    fun submitAnswer(question: ListeningComprehensionQuestion) {
        val state = _session.value
        val scenario = state.selectedScenario ?: return
        val boundQuestion = scenario.comprehensionQuestions.firstOrNull { it.id == question.id } ?: return
        val selectedOption = state.answers[question.id] ?: return
        if (question.id in state.submittedQuestions || selectedOption !in boundQuestion.options) return

        _session.update { it.copy(submittedQuestions = it.submittedQuestions + question.id) }
        val isCorrect = selectedOption.trim() == boundQuestion.correctAnswer.trim()
        viewModelScope.launch {
            recordLearningEvidenceUseCase(
                LearningEvidence(
                    contentId = scenario.id,
                    domain = "listening",
                    activityType = "comprehension_check",
                    isCorrect = isCorrect,
                    score = if (isCorrect) 1f else 0f,
                    responseTimeMs = 5000L,
                    details = "question_id=${question.id},selected=$selectedOption,correct=${boundQuestion.correctAnswer}",
                ),
            )
        }
    }

    @Deprecated("The active session owns the listening ID")
    fun submitAnswer(scenarioId: String, question: ListeningComprehensionQuestion) {
        if (_session.value.selectedScenario?.id == scenarioId) submitAnswer(question)
    }

    fun startDictation() {
        val hasSegments = _session.value.selectedScenario?.transcriptItems?.any {
            it.textEn.isNotBlank() && it.startMs >= 0L && it.endMs > it.startMs
        } == true
        if (hasSegments) {
            _session.update {
                it.copy(
                    isDictationActive = true,
                    dictationIndex = 0,
                    dictationAnswer = "",
                    dictationResult = null,
                )
            }
        }
    }

    fun stopDictation() {
        _session.update { it.copy(isDictationActive = false, dictationAnswer = "", dictationResult = null) }
    }

    fun updateDictationAnswer(answer: String) {
        if (_session.value.dictationResult == null) {
            _session.update { it.copy(dictationAnswer = answer) }
        }
    }

    fun replayDictationChunk() {
        uiState.value.currentDictationTranscript?.let(::replaySentence)
    }

    fun submitDictation() {
        val state = _session.value
        val scenario = state.selectedScenario ?: return
        val transcript = uiState.value.currentDictationTranscript ?: return
        if (state.dictationAnswer.isBlank() || state.dictationResult != null) return
        val result = featureEngine.evaluateDictation(state.dictationAnswer, transcript.textEn)
        _session.update { it.copy(dictationResult = result) }
        viewModelScope.launch {
            recordLearningEvidenceUseCase(
                LearningEvidence(
                    contentId = scenario.id,
                    domain = "listening",
                    activityType = "dictation",
                    isCorrect = result.isCorrect,
                    score = if (result.isCorrect) 1f else 0f,
                    details = "transcript_index=${transcript.index},matched=${result.matchedTokens}/${result.totalTokens}",
                ),
            )
        }
    }

    fun nextDictationChunk() {
        val transcripts = _session.value.selectedScenario?.transcriptItems?.filter {
            it.textEn.isNotBlank() && it.startMs >= 0L && it.endMs > it.startMs
        }.orEmpty()
        if (transcripts.isEmpty()) return
        _session.update {
            it.copy(
                dictationIndex = (it.dictationIndex + 1).coerceAtMost(transcripts.lastIndex),
                dictationAnswer = "",
                dictationResult = null,
            )
        }
    }

    fun restartSession() {
        val scenario = _session.value.selectedScenario ?: return
        audioPlaybackEngine.stop()
        _session.value = ListeningSessionState(
            requestedContentId = scenario.id,
            selectedScenario = scenario,
        )
    }

    override fun onCleared() {
        audioPlaybackEngine.stop()
        super.onCleared()
    }

    private data class LibraryState(
        val scenarios: List<ListeningScenario>,
        val categories: List<String>,
        val level: String?,
        val category: String?,
        val query: String,
    )

    private fun ListeningSessionState.toUiState(
        library: LibraryState,
        playback: PlaybackState,
    ): ListeningUiState {
        val activeSentence = selectedScenario?.transcriptItems?.find {
            playback.currentPositionMs in it.startMs until it.endMs
        }?.index
        return ListeningUiState(
            scenarios = library.scenarios,
            selectedScenario = selectedScenario,
            selectedLevel = library.level,
            selectedCategory = library.category,
            availableCategories = library.categories,
            searchQuery = library.query,
            isPlaying = playback.isPlaying,
            currentPositionMs = playback.currentPositionMs,
            durationMs = playback.durationMs.takeIf { it > 0L }
                ?: ((selectedScenario?.durationSeconds ?: 0) * 1000L),
            isTranscriptRevealed = transcriptRevealed,
            revealedSentenceTranslations = revealedTranslations,
            activeSentenceIndex = activeSentence,
            userAnswers = answers,
            submittedQuestions = submittedQuestions,
            sessionSection = section,
            currentQuestionIndex = currentQuestionIndex,
            isDictationActive = isDictationActive,
            dictationIndex = dictationIndex,
            dictationAnswer = dictationAnswer,
            dictationResult = dictationResult,
            isLoading = isResolving,
            errorMessage = contentError ?: playback.errorMessage,
        )
    }
}
