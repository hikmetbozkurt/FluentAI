package com.seanora.fluentai.feature.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.MistakeStage
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.data.repository.GrammarRepository
import com.seanora.fluentai.data.repository.ListeningRepository
import com.seanora.fluentai.data.repository.ReadingRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.data.repository.VocabularyRepository
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import com.seanora.fluentai.domain.mistakes.MistakeEngine
import com.seanora.fluentai.domain.mistakes.MistakeSummary
import com.seanora.fluentai.feature.home.data.formatSemanticTitle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ReviewTab(val label: String) {
    DUE_REVIEWS("Due Reviews"),
    MISTAKE_BANK("Mistake Bank"),
    FIX_IT("Quick Fix-It"),
}

enum class MistakeFilter(val label: String) {
    ACTIVE("Active"),
    RECURRING("Recurring"),
    RESOLVED("Resolved"),
}

data class ResolvedDueReview(
    val schedule: ReviewSchedule,
    val title: String,
    val cefrLevel: String?,
)

data class ResolvedMistake(
    val record: MistakeRecord,
    val title: String,
)

data class ReviewUiState(
    val isLoading: Boolean = true,
    val selectedTab: ReviewTab = ReviewTab.DUE_REVIEWS,
    val mistakeFilter: MistakeFilter = MistakeFilter.ACTIVE,
    val dueReviews: List<ResolvedDueReview> = emptyList(),
    val allMistakes: List<ResolvedMistake> = emptyList(),
    val mistakeSummary: MistakeSummary = MistakeSummary(0, 0, 0, 0, 0, 0, 100),
    val fixItIndex: Int = 0,
    val isFixItRevealed: Boolean = false,
) {
    val activeMistakes: List<ResolvedMistake>
        get() = allMistakes.filter { it.record.stage != MistakeStage.RESOLVED }

    val filteredMistakes: List<ResolvedMistake>
        get() = when (mistakeFilter) {
            MistakeFilter.ACTIVE -> activeMistakes
            MistakeFilter.RECURRING -> allMistakes.filter {
                it.record.stage in setOf(MistakeStage.RECURRING, MistakeStage.TARGETED)
            }
            MistakeFilter.RESOLVED -> allMistakes.filter { it.record.stage == MistakeStage.RESOLVED }
        }

    val currentFixItMistake: ResolvedMistake?
        get() = activeMistakes.getOrNull(fixItIndex.coerceIn(0, (activeMistakes.size - 1).coerceAtLeast(0)))
}

@HiltViewModel
class ReviewViewModel @Inject constructor(
    userLearningRepository: UserLearningRepository,
    vocabularyRepository: VocabularyRepository,
    grammarRepository: GrammarRepository,
    readingRepository: ReadingRepository,
    listeningRepository: ListeningRepository,
    private val mistakeEngine: MistakeEngine,
    private val recordLearningEvidence: RecordLearningEvidenceUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    init {
        val now = System.currentTimeMillis()
        viewModelScope.launch {
            val catalogFlow = combine(
                vocabularyRepository.getAllVocab(),
                grammarRepository.getAllLessons(),
                readingRepository.getAllArticles(),
                listeningRepository.getAllScenarios(),
            ) { vocab, grammar, readings, listenings ->
                val titleMap = HashMap<String, Pair<String, String?>>(
                    vocab.size + grammar.size + readings.size + listenings.size,
                )
                vocab.forEach { titleMap[it.id] = it.headword to it.cefrLevel }
                grammar.forEach { titleMap[it.id] = it.title to it.cefrLevel }
                readings.forEach { titleMap[it.id] = it.title to it.cefrLevel }
                listenings.forEach { titleMap[it.id] = it.title to it.cefrLevel }
                titleMap
            }

            combine(
                userLearningRepository.getDueReviews(now),
                userLearningRepository.getAllMistakes(),
                catalogFlow,
            ) { due, mistakes, catalog ->
                Triple(due, mistakes, catalog)
            }.collect { (due, mistakes, catalog) ->
                val resolvedDue = due
                    .sortedBy { it.nextReviewDueTimestamp }
                    .map { schedule ->
                        val meta = catalog[schedule.contentId]
                        ResolvedDueReview(
                            schedule = schedule,
                            title = meta?.first ?: formatSemanticTitle(schedule.contentId),
                            cefrLevel = meta?.second,
                        )
                    }

                val resolvedMistakes = mistakes
                    .sortedWith(
                        compareByDescending<MistakeRecord> { it.stage == MistakeStage.RECURRING }
                            .thenByDescending { it.occurrenceCount }
                            .thenByDescending { it.lastObservedAt },
                    )
                    .map { record ->
                        ResolvedMistake(
                            record = record,
                            title = catalog[record.contentId]?.first ?: formatSemanticTitle(record.contentId),
                        )
                    }

                val summary = mistakeEngine.computeSummary(mistakes)
                _uiState.update { old ->
                    val activeSize = resolvedMistakes.count { it.record.stage != MistakeStage.RESOLVED }
                    old.copy(
                        isLoading = false,
                        dueReviews = resolvedDue,
                        allMistakes = resolvedMistakes,
                        mistakeSummary = summary,
                        fixItIndex = if (activeSize == 0) 0 else old.fixItIndex.coerceIn(0, activeSize - 1),
                    )
                }
            }
        }
    }

    fun selectTab(tab: ReviewTab) = _uiState.update { it.copy(selectedTab = tab, isFixItRevealed = false) }

    fun selectMistakeFilter(filter: MistakeFilter) = _uiState.update { it.copy(mistakeFilter = filter) }

    fun startFixItFor(mistakeId: Long) {
        val idx = _uiState.value.activeMistakes.indexOfFirst { it.record.id == mistakeId }.coerceAtLeast(0)
        _uiState.update { it.copy(selectedTab = ReviewTab.FIX_IT, fixItIndex = idx, isFixItRevealed = false) }
    }

    fun revealFixIt() = _uiState.update { it.copy(isFixItRevealed = true) }

    fun submitFixItOutcome(understood: Boolean) {
        val target = _uiState.value.currentFixItMistake ?: return
        val record = target.record
        val domain = inferDomain(record.contentId)
        viewModelScope.launch {
            recordLearningEvidence(
                evidence = LearningEvidence(
                    contentId = record.contentId,
                    domain = domain,
                    activityType = "mistake_bank_fix_it",
                    isCorrect = understood,
                    score = if (understood) 1f else 0f,
                    details = "mistake_id=${record.id};trap=${record.trapType}",
                ),
                trapType = record.trapType,
                errorDescription = record.errorDescription,
                userAnswer = if (understood) record.correctAnswer else record.userAnswer,
                correctAnswer = record.correctAnswer,
            )
        }
        _uiState.update { old ->
            val nextIdx = if (old.activeMistakes.size <= 1) 0 else (old.fixItIndex + 1) % old.activeMistakes.size
            old.copy(fixItIndex = nextIdx, isFixItRevealed = false)
        }
    }

    private fun inferDomain(contentId: String): String = when {
        contentId.startsWith("vocab.") -> "vocabulary"
        contentId.startsWith("grammar.") -> "grammar"
        contentId.startsWith("reading.") -> "reading"
        contentId.startsWith("listening.") -> "listening"
        contentId.startsWith("speaking.") -> "speaking"
        else -> "grammar"
    }
}
