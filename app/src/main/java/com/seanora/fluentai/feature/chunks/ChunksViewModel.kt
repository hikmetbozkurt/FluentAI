package com.seanora.fluentai.feature.chunks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanora.fluentai.core.model.BlueprintSlot
import com.seanora.fluentai.core.model.ChunkCategory
import com.seanora.fluentai.core.model.ChunkDrillMode
import com.seanora.fluentai.core.model.ChunkDrillQuestion
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.LexicalChunk
import com.seanora.fluentai.data.repository.ChunkRepository
import com.seanora.fluentai.domain.chunks.ChunkPracticeEngine
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChunksUiState(
    val isLoading: Boolean = true,
    val allChunks: List<LexicalChunk> = emptyList(),
    val dailyPack: List<LexicalChunk> = emptyList(),
    val selectedLevel: String? = null,
    val selectedCategory: ChunkCategory? = null,
    val searchQuery: String = "",
    // Practice session state
    val drillMode: ChunkDrillMode = ChunkDrillMode.MIXED,
    val questions: List<ChunkDrillQuestion> = emptyList(),
    val questionIndex: Int = 0,
    val selectedChoice: String? = null,
    val selectedSlotIndices: List<Int> = emptyList(),
    val submitted: Boolean = false,
    val lastCorrect: Boolean? = null,
    val correctCount: Int = 0,
    val isSessionComplete: Boolean = false,
) {
    val filteredChunks: List<LexicalChunk>
        get() = allChunks.filter { chunk ->
            val levelOk = selectedLevel == null || chunk.cefrLevel.equals(selectedLevel, ignoreCase = true)
            val catOk = selectedCategory == null || chunk.category == selectedCategory
            val q = searchQuery.trim()
            val queryOk = q.isEmpty() ||
                chunk.chunkEn.contains(q, ignoreCase = true) ||
                chunk.intentTr.contains(q, ignoreCase = true) ||
                chunk.meaningEn.contains(q, ignoreCase = true)
            levelOk && catOk && queryOk
        }

    val currentQuestion: ChunkDrillQuestion?
        get() = questions.getOrNull(questionIndex)
}

@HiltViewModel
class ChunksViewModel @Inject constructor(
    private val chunkRepository: ChunkRepository,
    private val practiceEngine: ChunkPracticeEngine,
    private val recordLearningEvidenceUseCase: RecordLearningEvidenceUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChunksUiState())
    val uiState: StateFlow<ChunksUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            chunkRepository.getAllChunks().collect { chunks ->
                val daySeed = System.currentTimeMillis() / 86_400_000L
                val daily = practiceEngine.selectDailyPack(chunks, daySeed, count = 3)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        allChunks = chunks,
                        dailyPack = daily,
                    )
                }
            }
        }
    }

    fun selectLevel(level: String?) {
        _uiState.update { it.copy(selectedLevel = level) }
    }

    fun selectCategory(category: ChunkCategory?) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun startDrill(mode: ChunkDrillMode = ChunkDrillMode.MIXED, focusChunkId: String? = null) {
        val state = _uiState.value
        val pool = if (focusChunkId != null) state.allChunks else state.filteredChunks.ifEmpty { state.allChunks }
        val built = practiceEngine.buildSession(
            chunks = pool,
            mode = mode,
            focusChunkId = focusChunkId,
            maxQuestions = if (focusChunkId != null) 3 else 8,
        )
        _uiState.update {
            it.copy(
                drillMode = mode,
                questions = built,
                questionIndex = 0,
                selectedChoice = null,
                selectedSlotIndices = emptyList(),
                submitted = false,
                lastCorrect = null,
                correctCount = 0,
                isSessionComplete = false,
            )
        }
    }

    fun selectChoice(option: String) {
        if (_uiState.value.submitted) return
        _uiState.update { it.copy(selectedChoice = option) }
    }

    fun addBlueprintSlot(index: Int) {
        val state = _uiState.value
        if (state.submitted || index in state.selectedSlotIndices) return
        _uiState.update { it.copy(selectedSlotIndices = it.selectedSlotIndices + index) }
    }

    fun removeBlueprintSlotAt(position: Int) {
        val state = _uiState.value
        if (state.submitted || position !in state.selectedSlotIndices.indices) return
        _uiState.update {
            it.copy(selectedSlotIndices = it.selectedSlotIndices.toMutableList().apply { removeAt(position) })
        }
    }

    fun clearBlueprintSlots() {
        if (_uiState.value.submitted) return
        _uiState.update { it.copy(selectedSlotIndices = emptyList()) }
    }

    fun submitAnswer() {
        val state = _uiState.value
        if (state.submitted) return
        val question = state.currentQuestion ?: return

        val (isCorrect, userAnswer, correctAnswer) = when (question) {
            is ChunkDrillQuestion.Choice -> {
                val chosen = state.selectedChoice ?: return
                Triple(chosen == question.correctAnswer, chosen, question.correctAnswer)
            }
            is ChunkDrillQuestion.BlueprintAssembly -> {
                if (state.selectedSlotIndices.isEmpty()) return
                val picked: List<BlueprintSlot> = state.selectedSlotIndices.mapNotNull {
                    question.scrambledSlots.getOrNull(it)
                }
                val ok = practiceEngine.evaluateBlueprint(question, picked)
                Triple(
                    ok,
                    picked.joinToString(" ") { it.textEn },
                    question.correctSlots.joinToString(" ") { it.textEn },
                )
            }
        }

        _uiState.update {
            it.copy(
                submitted = true,
                lastCorrect = isCorrect,
                correctCount = if (isCorrect) it.correctCount + 1 else it.correctCount,
            )
        }

        val chunk = state.allChunks.firstOrNull { it.id == question.chunkId }
        viewModelScope.launch {
            runCatching {
                recordLearningEvidenceUseCase(
                    evidence = LearningEvidence(
                        contentId = question.chunkId,
                        domain = "grammar",
                        activityType = if (question is ChunkDrillQuestion.BlueprintAssembly) {
                            "chunk_blueprint"
                        } else {
                            "chunk_drill"
                        },
                        isCorrect = isCorrect,
                        score = if (isCorrect) 1f else 0f,
                        responseTimeMs = 0L,
                        details = question.cefrLevel,
                        timestamp = System.currentTimeMillis(),
                    ),
                    trapType = chunk?.turkishTrap?.trapType,
                    errorDescription = chunk?.turkishTrap?.explanationTr,
                    userAnswer = userAnswer,
                    correctAnswer = correctAnswer,
                )
            }
        }
    }

    fun nextQuestion() {
        val state = _uiState.value
        val next = state.questionIndex + 1
        if (next >= state.questions.size) {
            _uiState.update { it.copy(isSessionComplete = true) }
        } else {
            _uiState.update {
                it.copy(
                    questionIndex = next,
                    selectedChoice = null,
                    selectedSlotIndices = emptyList(),
                    submitted = false,
                    lastCorrect = null,
                )
            }
        }
    }
}
