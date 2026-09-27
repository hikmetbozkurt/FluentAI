package com.seanora.fluentai.feature.reading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.ReadingArticle
import com.seanora.fluentai.core.model.ReadingComprehensionQuestion
import com.seanora.fluentai.data.repository.ReadingRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.domain.mastery.RecordLearningEvidenceUseCase
import com.seanora.fluentai.domain.reading.ReadingDateProvider
import com.seanora.fluentai.domain.reading.ReadingFeatureEngine
import com.seanora.fluentai.domain.reading.ReadingSkill
import com.seanora.fluentai.domain.reading.ReadingSkillItem
import com.seanora.fluentai.domain.reading.ReadingWeakSpot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReadingFeatureUiState(
    val isLoading: Boolean = true,
    val articles: List<ReadingArticle> = emptyList(),
    val dailyArticle: ReadingArticle? = null,
    val continueArticle: ReadingArticle? = null,
    val guidedArticles: List<ReadingArticle> = emptyList(),
    val selectedGuidedId: String? = null,
    val guidedParagraphIndex: Int = 0,
    val skills: List<ReadingSkill> = emptyList(),
    val selectedSkillId: String? = null,
    val skillItemIndex: Int = 0,
    val weakSpots: List<ReadingWeakSpot> = emptyList(),
    val selectedAnswers: Map<String, String> = emptyMap(),
    val submittedQuestionIds: Set<String> = emptySet(),
    val answerCorrectness: Map<String, Boolean> = emptyMap(),
) {
    val selectedGuidedArticle: ReadingArticle? get() = guidedArticles.firstOrNull { it.id == selectedGuidedId }
    val selectedSkill: ReadingSkill? get() = skills.firstOrNull { it.id == selectedSkillId }
    val currentSkillItem: ReadingSkillItem? get() = selectedSkill?.items?.getOrNull(skillItemIndex)
}

@HiltViewModel
class ReadingFeatureViewModel @Inject constructor(
    readingRepository: ReadingRepository,
    userLearningRepository: UserLearningRepository,
    private val recordLearningEvidence: RecordLearningEvidenceUseCase,
    private val engine: ReadingFeatureEngine,
    private val dateProvider: ReadingDateProvider,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReadingFeatureUiState())
    val uiState: StateFlow<ReadingFeatureUiState> = _uiState.asStateFlow()

    init {
        val now = System.currentTimeMillis()
        viewModelScope.launch {
            combine(
                readingRepository.getAllArticles(),
                userLearningRepository.getRecentEvidence(100),
                userLearningRepository.getAllMasterySnapshots(),
                userLearningRepository.getDueReviews(now),
                userLearningRepository.getUserProfile(),
            ) { articles, evidence, mastery, due, profile ->
                val guided = engine.guidedArticles(articles)
                val skills = engine.deriveSkills(articles)
                FeatureReadingData(
                    articles = articles,
                    daily = engine.selectDaily(articles, profile?.estimatedReadingLevel, dateProvider.today()),
                    continued = engine.resolveContinue(articles, evidence),
                    guided = guided,
                    skills = skills,
                    weakSpots = engine.weakSpots(articles, mastery, due, now),
                )
            }.collect { data ->
                _uiState.update { old ->
                    val validGuidedIds = data.guided.mapTo(mutableSetOf()) { it.id }
                    val validSkillIds = data.skills.mapTo(mutableSetOf()) { it.id }
                    val guidedId = old.selectedGuidedId?.takeIf { it in validGuidedIds } ?: data.guided.firstOrNull()?.id
                    val paragraphCount = data.guided.firstOrNull { it.id == guidedId }?.paragraphs?.size ?: 0
                    old.copy(
                        isLoading = false,
                        articles = data.articles,
                        dailyArticle = data.daily,
                        continueArticle = data.continued,
                        guidedArticles = data.guided,
                        selectedGuidedId = guidedId,
                        guidedParagraphIndex = old.guidedParagraphIndex.coerceIn(0, (paragraphCount - 1).coerceAtLeast(0)),
                        skills = data.skills,
                        selectedSkillId = old.selectedSkillId?.takeIf { it in validSkillIds } ?: data.skills.firstOrNull()?.id,
                        skillItemIndex = old.skillItemIndex.coerceAtLeast(0),
                        weakSpots = data.weakSpots,
                    )
                }
            }
        }
    }

    fun selectGuidedArticle(contentId: String) {
        if (_uiState.value.guidedArticles.none { it.id == contentId }) return
        _uiState.update { it.copy(selectedGuidedId = contentId, guidedParagraphIndex = 0) }
    }

    fun previousGuidedParagraph() = _uiState.update {
        it.copy(guidedParagraphIndex = (it.guidedParagraphIndex - 1).coerceAtLeast(0))
    }

    fun nextGuidedParagraph() = _uiState.update { state ->
        val last = (state.selectedGuidedArticle?.paragraphs?.lastIndex ?: 0).coerceAtLeast(0)
        state.copy(guidedParagraphIndex = (state.guidedParagraphIndex + 1).coerceAtMost(last))
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

    fun submitSelectedAnswer(
        articleId: String,
        question: ReadingComprehensionQuestion,
        activityType: String,
    ) {
        val answer = _uiState.value.selectedAnswers[question.id] ?: return
        submitAnswer(articleId, question, answer, activityType)
    }

    fun submitAnswer(
        articleId: String,
        question: ReadingComprehensionQuestion,
        answer: String,
        activityType: String,
    ) {
        if (question.id in _uiState.value.submittedQuestionIds) return
        val articleExists = _uiState.value.articles.any { it.id == articleId && question in it.comprehensionQuestions }
        if (!articleExists || answer !in question.options) return
        val correct = answer.trim() == question.correctAnswer.trim()
        _uiState.update {
            it.copy(
                selectedAnswers = it.selectedAnswers + (question.id to answer),
                submittedQuestionIds = it.submittedQuestionIds + question.id,
                answerCorrectness = it.answerCorrectness + (question.id to correct),
            )
        }
        viewModelScope.launch {
            recordLearningEvidence(
                LearningEvidence(
                    contentId = articleId,
                    domain = "reading",
                    activityType = activityType,
                    isCorrect = correct,
                    score = if (correct) 1f else 0f,
                    details = "question_id=${question.id},selected=$answer,correct=${question.correctAnswer}",
                ),
            )
        }
    }

    private data class FeatureReadingData(
        val articles: List<ReadingArticle>,
        val daily: ReadingArticle?,
        val continued: ReadingArticle?,
        val guided: List<ReadingArticle>,
        val skills: List<ReadingSkill>,
        val weakSpots: List<ReadingWeakSpot>,
    )
}
