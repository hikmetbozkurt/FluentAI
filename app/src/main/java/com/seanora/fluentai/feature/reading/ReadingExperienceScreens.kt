package com.seanora.fluentai.feature.reading

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.seanora.fluentai.app.navigation.ReadingSessionMode
import com.seanora.fluentai.core.designsystem.components.ButtonVariant
import com.seanora.fluentai.core.designsystem.components.DetailSectionSelector
import com.seanora.fluentai.core.designsystem.components.FluentButton
import com.seanora.fluentai.core.designsystem.components.FluentSelectableChip
import com.seanora.fluentai.core.designsystem.dashboard.LingoPinkTheme
import com.seanora.fluentai.core.designsystem.theme.FluentTheme
import com.seanora.fluentai.core.model.ReadingArticle
import com.seanora.fluentai.core.model.ReadingComprehensionQuestion

private enum class ReadingLength(val label: String) {
    SHORT("Under 500 words"), MEDIUM("500–999 words"), LONG("1,000+ words");

    fun matches(wordCount: Int) = when (this) {
        SHORT -> wordCount < 500
        MEDIUM -> wordCount in 500..999
        LONG -> wordCount >= 1_000
    }
}

@Composable
fun ReadingLibraryScreen(
    onNavigateBack: () -> Unit,
    onOpenArticle: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReadingViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var category by rememberSaveable { mutableStateOf<String?>(null) }
    var length by rememberSaveable { mutableStateOf<ReadingLength?>(null) }
    val categories = remember(state.articles) { state.articles.map { it.category }.distinct().sorted() }
    val visible = remember(state.articles, category, length) {
        state.articles.filter { article ->
            (category == null || article.category == category) && (length == null || length!!.matches(article.wordCount))
        }
    }
    LingoPinkTheme {
        Column(modifier.fillMaxSize().background(FluentTheme.colors.surfaceBase).padding(horizontal = 24.dp, vertical = 18.dp)) {
            ReadingHeader("Reading Library", "${visible.size} article${if (visible.size == 1) "" else "s"}", onNavigateBack)
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = viewModel::updateSearchQuery,
                placeholder = { Text("Search articles...") },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = FluentTheme.colors.textPrimary,
                    unfocusedTextColor = FluentTheme.colors.textPrimary,
                    focusedContainerColor = FluentTheme.colors.surfaceCard,
                    unfocusedContainerColor = FluentTheme.colors.surfaceCard,
                    focusedBorderColor = FluentTheme.colors.accentPrimary,
                    unfocusedBorderColor = FluentTheme.colors.surfaceBorder,
                    focusedLeadingIconColor = FluentTheme.colors.accentSecondary,
                    unfocusedLeadingIconColor = FluentTheme.colors.textSecondary,
                    focusedPlaceholderColor = FluentTheme.colors.textSecondary,
                    unfocusedPlaceholderColor = FluentTheme.colors.textSecondary,
                    cursorColor = FluentTheme.colors.accentPrimary,
                ),
                shape = RoundedCornerShape(14.dp),
            )
            ReadingFilters(
                selectedLevel = state.selectedLevel,
                onLevel = viewModel::selectLevel,
                category = category,
                onCategory = { category = it },
                categories = categories,
                length = length,
                onLength = { length = it },
            )
            Spacer(Modifier.height(12.dp))
            when {
                state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally), color = FluentTheme.colors.accentPrimary)
                state.errorMessage != null -> ReadingMessage(state.errorMessage!!)
                visible.isEmpty() -> ReadingMessage("No articles match these filters.")
                else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(visible, key = { it.id }) { article -> LibraryArticleCard(article) { onOpenArticle(article.id) } }
                }
            }
        }
    }
}

@Composable
private fun ReadingFilters(
    selectedLevel: String?,
    onLevel: (String?) -> Unit,
    category: String?,
    onCategory: (String?) -> Unit,
    categories: List<String>,
    length: ReadingLength?,
    onLength: (ReadingLength?) -> Unit,
) {
    Column(Modifier.fillMaxWidth().padding(top = 10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf<String?>(null, "A2", "B1", "B2", "C1", "C2").forEach { level ->
                FluentSelectableChip(level ?: "All", selectedLevel == level, onClick = { onLevel(level) })
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CompactFilterMenu(
                label = category?.displayName() ?: "Topics",
                values = listOf(null) + categories,
                valueLabel = { it?.displayName() ?: "All topics" },
                onSelect = onCategory,
            )
            CompactFilterMenu(
                label = length?.label ?: "Length",
                values = listOf(null) + ReadingLength.entries,
                valueLabel = { it?.label ?: "Any length" },
                onSelect = onLength,
            )
        }
    }
}

@Composable
private fun <T> CompactFilterMenu(label: String, values: List<T?>, valueLabel: (T?) -> String, onSelect: (T?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        FluentButton(
            text = label,
            variant = ButtonVariant.Outlined,
            trailingIcon = { Icon(Icons.Rounded.ExpandMore, contentDescription = null, modifier = Modifier.size(18.dp)) },
            onClick = { expanded = true },
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, containerColor = FluentTheme.colors.surfaceCard) {
            values.forEach { value ->
                DropdownMenuItem(
                    text = { Text(valueLabel(value), color = FluentTheme.colors.textPrimary) },
                    onClick = { onSelect(value); expanded = false },
                )
            }
        }
    }
}

@Composable
private fun LibraryArticleCard(article: ReadingArticle, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        color = FluentTheme.colors.surfaceCard,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, FluentTheme.colors.surfaceBorder),
    ) {
        Row(Modifier.padding(horizontal = 18.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(article.title, style = FluentTheme.typography.titleMedium, color = FluentTheme.colors.textPrimary, fontWeight = FontWeight.SemiBold)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${article.cefrLevel} · ${article.category.displayName()}", style = FluentTheme.typography.labelMedium, color = FluentTheme.colors.accentSecondary)
                    Text("~${article.estimatedReadingMinutes} min", style = FluentTheme.typography.labelMedium, color = FluentTheme.colors.textSecondary)
                }
                Text("${article.wordCount} words", style = FluentTheme.typography.labelSmall, color = FluentTheme.colors.textMuted)
                Text(article.summaryEn, style = FluentTheme.typography.bodySmall, color = FluentTheme.colors.textSecondary, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
            Icon(Icons.AutoMirrored.Rounded.ArrowForward, "Open ${article.title}", tint = FluentTheme.colors.accentSecondary, modifier = Modifier.padding(start = 12.dp))
        }
    }
}

@Composable
fun ReadingSessionScreen(
    articleId: String,
    mode: ReadingSessionMode,
    onNavigateBack: () -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReadingSessionViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(articleId, mode) { viewModel.openArticle(articleId, mode) }
    LingoPinkTheme { ReadingSessionContent(state, viewModel, onNavigateBack, onFinish, modifier) }
}

@Composable
private fun ReadingSessionContent(
    state: ReadingSessionUiState,
    viewModel: ReadingSessionViewModel,
    onNavigateBack: () -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier,
) {
    Box(modifier.fillMaxSize().background(FluentTheme.colors.surfaceBase)) {
        when {
            state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center), color = FluentTheme.colors.accentPrimary)
            state.article == null -> Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                ReadingMessage(state.errorMessage ?: "Article unavailable.")
                FluentButton(text = "Back to Reading", onClick = onNavigateBack, variant = ButtonVariant.Outlined)
            }
            else -> Column(Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                val article = state.article
                ReadingHeader("Reading", "Back to Reading", onNavigateBack, compact = true)
                Column(Modifier.widthIn(max = 820.dp).fillMaxWidth()) {
                    Text(
                        "${article.cefrLevel} · ${article.category.displayName()} · ${article.wordCount} words · ~${article.estimatedReadingMinutes} min",
                        style = FluentTheme.typography.labelMedium,
                        color = FluentTheme.colors.accentSecondary,
                    )
                    Text(article.title, style = FluentTheme.typography.headlineLarge, color = FluentTheme.colors.textPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
                    Text(article.summaryEn, style = FluentTheme.typography.bodyMedium, color = FluentTheme.colors.textSecondary, modifier = Modifier.padding(top = 5.dp))
                    DetailSectionSelector(
                        sections = ReadingSessionSection.entries.map { it.label },
                        selectedSection = state.section.label,
                        onSectionSelected = { label -> ReadingSessionSection.entries.firstOrNull { it.label == label }?.let(viewModel::selectSection) },
                        modifier = Modifier.padding(vertical = 12.dp),
                    )
                }
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                    when (state.section) {
                        ReadingSessionSection.ARTICLE -> ArticleSection(state, viewModel, onNavigateBack)
                        ReadingSessionSection.VOCABULARY -> VocabularySection(state.article, viewModel)
                        ReadingSessionSection.QUESTIONS -> QuestionsSection(state, viewModel)
                        ReadingSessionSection.REVIEW -> ReviewSection(state, viewModel, onFinish)
                    }
                }
            }
        }
    }
}

@Composable
private fun ArticleSection(state: ReadingSessionUiState, viewModel: ReadingSessionViewModel, onExit: () -> Unit) {
    val article = state.article ?: return
    Column(Modifier.widthIn(max = 820.dp).fillMaxWidth().verticalScroll(rememberScrollState()).padding(bottom = 28.dp)) {
        Surface(color = Color(0xFFFFFEFC), shape = RoundedCornerShape(18.dp), border = BorderStroke(1.dp, FluentTheme.colors.surfaceBorder), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(horizontal = 28.dp, vertical = 24.dp)) {
                val paragraphs = if (state.mode == ReadingSessionMode.GUIDED) listOfNotNull(article.paragraphs.getOrNull(state.guidedParagraphIndex)) else article.paragraphs
                paragraphs.forEach { paragraph ->
                    paragraph.title?.let {
                        Text(it, style = FluentTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = FluentTheme.colors.textPrimary, modifier = Modifier.padding(top = 12.dp, bottom = 5.dp))
                    }
                    Text(
                        paragraph.contentEn,
                        style = FluentTheme.typography.bodyLarge.copy(lineHeight = FluentTheme.typography.bodyLarge.lineHeight * 1.18f),
                        color = FluentTheme.colors.textPrimary,
                        modifier = Modifier.padding(vertical = 9.dp),
                    )
                }
                if (state.mode == ReadingSessionMode.GUIDED) {
                    Row(Modifier.fillMaxWidth().padding(top = 18.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        FluentButton(text = "Previous paragraph", onClick = viewModel::previousParagraph, variant = ButtonVariant.Outlined, enabled = state.guidedParagraphIndex > 0)
                        FluentButton(text = "Next paragraph", onClick = viewModel::nextParagraph, enabled = state.guidedParagraphIndex < article.paragraphs.lastIndex)
                    }
                }
            }
        }
        Surface(color = FluentTheme.colors.surfaceElevated, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Ready to check your understanding?", style = FluentTheme.typography.titleMedium, color = FluentTheme.colors.textPrimary, fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (article.comprehensionQuestions.isNotEmpty()) FluentButton(text = "Continue to Questions", onClick = { viewModel.selectSection(ReadingSessionSection.QUESTIONS) })
                    if (article.vocabularyAnnotations.isNotEmpty()) FluentButton(text = "Review Vocabulary", onClick = { viewModel.selectSection(ReadingSessionSection.VOCABULARY) }, variant = ButtonVariant.Outlined)
                }
            }
        }
        FluentButton(text = "Exit", onClick = onExit, variant = ButtonVariant.Outlined, modifier = Modifier.padding(top = 12.dp))
    }
}

@Composable
private fun VocabularySection(article: ReadingArticle, viewModel: ReadingSessionViewModel) {
    Column(Modifier.widthIn(max = 820.dp).fillMaxWidth().verticalScroll(rememberScrollState()).padding(bottom = 28.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Key Vocabulary", style = FluentTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = FluentTheme.colors.textPrimary)
        Text("${article.vocabularyAnnotations.size} item${if (article.vocabularyAnnotations.size == 1) "" else "s"}", color = FluentTheme.colors.textSecondary)
        if (article.vocabularyAnnotations.isEmpty()) ReadingMessage("No article-specific vocabulary support is available for this text.")
        article.vocabularyAnnotations.forEach { item ->
            Surface(color = FluentTheme.colors.surfaceCard, shape = RoundedCornerShape(14.dp), border = BorderStroke(1.dp, FluentTheme.colors.surfaceBorder), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(item.word, style = FluentTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = FluentTheme.colors.textPrimary)
                    Text(item.contextDefinitionEn, style = FluentTheme.typography.bodyMedium, color = FluentTheme.colors.textPrimary)
                    Text(item.contextMeaningTr, style = FluentTheme.typography.bodySmall, color = FluentTheme.colors.textSecondary)
                }
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            FluentButton(text = "Back to Article", onClick = { viewModel.selectSection(ReadingSessionSection.ARTICLE) }, variant = ButtonVariant.Outlined)
            if (article.comprehensionQuestions.isNotEmpty()) FluentButton(text = "Continue to Questions", onClick = { viewModel.selectSection(ReadingSessionSection.QUESTIONS) })
        }
    }
}

@Composable
private fun QuestionsSection(state: ReadingSessionUiState, viewModel: ReadingSessionViewModel) {
    val article = state.article ?: return
    val question = state.currentQuestion
    if (question == null) { ReadingMessage("No comprehension questions are available for this article."); return }
    val submitted = question.id in state.submittedQuestionIds
    Column(Modifier.widthIn(max = 760.dp).fillMaxWidth().verticalScroll(rememberScrollState()).padding(bottom = 28.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Comprehension", style = FluentTheme.typography.headlineMedium, color = FluentTheme.colors.textPrimary, fontWeight = FontWeight.Bold)
        Text("Question ${state.questionIndex + 1} of ${article.comprehensionQuestions.size}", style = FluentTheme.typography.labelMedium, color = FluentTheme.colors.accentSecondary)
        Text(question.questionEn, style = FluentTheme.typography.titleLarge, color = FluentTheme.colors.textPrimary, fontWeight = FontWeight.SemiBold)
        question.options.forEachIndexed { index, option ->
            QuestionOption(
                label = ('A'.code + index).toChar().toString(),
                text = option,
                selected = state.selectedAnswers[question.id] == option,
                submitted = submitted,
                correct = option == question.correctAnswer,
                onClick = { viewModel.selectAnswer(question.id, option) },
            )
        }
        if (submitted) QuestionFeedback(question, state.selectedAnswers[question.id].orEmpty(), state.answerCorrectness[question.id] == true)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            FluentButton(text = "View article", onClick = { viewModel.selectSection(ReadingSessionSection.ARTICLE) }, variant = ButtonVariant.Outlined)
            if (!submitted) {
                FluentButton(text = "Check answer", onClick = viewModel::checkAnswer, enabled = state.selectedAnswers[question.id] != null)
            } else if (state.questionIndex == article.comprehensionQuestions.lastIndex) {
                FluentButton(text = "Review Session", onClick = { viewModel.selectSection(ReadingSessionSection.REVIEW) })
            } else {
                FluentButton(text = "Next", onClick = viewModel::nextQuestion)
            }
        }
        if (state.questionIndex > 0) FluentButton(text = "Previous question", onClick = viewModel::previousQuestion, variant = ButtonVariant.Outlined)
    }
}

@Composable
private fun QuestionOption(label: String, text: String, selected: Boolean, submitted: Boolean, correct: Boolean, onClick: () -> Unit) {
    val isWrongSelection = submitted && selected && !correct
    val isCorrectAnswer = submitted && correct
    val borderColor = when { isCorrectAnswer -> Color(0xFF2E7D57); isWrongSelection -> Color(0xFFB33A5B); selected -> FluentTheme.colors.accentPrimary; else -> FluentTheme.colors.surfaceBorder }
    val container = when { isCorrectAnswer -> Color(0xFFE4F5EC); isWrongSelection -> Color(0xFFFFE8EE); selected -> FluentTheme.colors.surfaceElevated; else -> FluentTheme.colors.surfaceCard }
    val stateText = when { isCorrectAnswer -> "Correct answer"; isWrongSelection -> "Incorrect selection"; selected -> "Selected"; else -> "Answer option" }
    Surface(
        modifier = Modifier.fillMaxWidth().selectable(selected = selected, enabled = !submitted, role = Role.RadioButton, onClick = onClick).semantics { contentDescription = "$label. $text. $stateText" },
        color = container,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(if (selected || isCorrectAnswer) 2.dp else 1.dp, borderColor),
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Surface(shape = CircleShape, color = borderColor.copy(alpha = .13f)) { Text(label, Modifier.padding(horizontal = 10.dp, vertical = 6.dp), color = FluentTheme.colors.textPrimary, fontWeight = FontWeight.Bold) }
            Text(text, Modifier.weight(1f), color = FluentTheme.colors.textPrimary, style = FluentTheme.typography.bodyLarge)
            if (isCorrectAnswer) Icon(Icons.Rounded.CheckCircle, "Correct", tint = Color(0xFF2E7D57))
            if (isWrongSelection) Icon(Icons.Rounded.Cancel, "Incorrect", tint = Color(0xFFB33A5B))
        }
    }
}

@Composable
private fun QuestionFeedback(question: ReadingComprehensionQuestion, userAnswer: String, correct: Boolean) {
    Surface(color = if (correct) Color(0xFFE4F5EC) else Color(0xFFFFE8EE), shape = RoundedCornerShape(14.dp), border = BorderStroke(1.dp, if (correct) Color(0xFF2E7D57) else Color(0xFFB33A5B))) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(if (correct) "Correct" else "Not quite", fontWeight = FontWeight.Bold, color = FluentTheme.colors.textPrimary)
            if (!correct) {
                Text("Your answer: $userAnswer", color = FluentTheme.colors.textSecondary)
                Text("Correct answer: ${question.correctAnswer}", color = FluentTheme.colors.textPrimary, fontWeight = FontWeight.SemiBold)
            }
            Text(question.explanationEn, color = FluentTheme.colors.textPrimary)
        }
    }
}

@Composable
private fun ReviewSection(state: ReadingSessionUiState, viewModel: ReadingSessionViewModel, onFinish: () -> Unit) {
    val article = state.article ?: return
    val answered = state.submittedQuestionIds.size
    val incorrect = state.answerCorrectness.count { !it.value }
    Column(Modifier.widthIn(max = 760.dp).fillMaxWidth().verticalScroll(rememberScrollState()).padding(bottom = 28.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Reading complete", style = FluentTheme.typography.headlineLarge, color = FluentTheme.colors.textPrimary, fontWeight = FontWeight.Bold)
        Text(article.title, style = FluentTheme.typography.titleLarge, color = FluentTheme.colors.textPrimary)
        Surface(color = FluentTheme.colors.surfaceElevated, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp)) {
                Text("${state.correctCount} / $answered correct", style = FluentTheme.typography.headlineMedium, color = FluentTheme.colors.accentSecondary, fontWeight = FontWeight.Bold)
                Text("$incorrect to revisit · ${article.vocabularyAnnotations.size} vocabulary item${if (article.vocabularyAnnotations.size == 1) "" else "s"}", color = FluentTheme.colors.textSecondary)
            }
        }
        if (incorrect > 0) FluentButton(text = "Review mistakes", onClick = viewModel::reviewMistakes)
        FluentButton(text = "Read again", onClick = viewModel::readAgain, variant = ButtonVariant.Outlined)
        FluentButton(text = "Finish", onClick = onFinish)
    }
}

@Composable
private fun ReadingHeader(title: String, subtitle: String, onBack: () -> Unit, compact: Boolean = false) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, subtitle, tint = FluentTheme.colors.accentSecondary) }
        if (!compact) Column {
            Text(title, style = FluentTheme.typography.titleLarge, color = FluentTheme.colors.textPrimary, fontWeight = FontWeight.Bold)
            Text(subtitle, style = FluentTheme.typography.bodySmall, color = FluentTheme.colors.textSecondary)
        } else Text(subtitle, style = FluentTheme.typography.labelMedium, color = FluentTheme.colors.textSecondary)
    }
}

@Composable
private fun ReadingMessage(message: String) {
    Text(message, style = FluentTheme.typography.bodyMedium, color = FluentTheme.colors.textSecondary, modifier = Modifier.padding(20.dp))
}

private fun String.displayName() = replace('_', ' ').replaceFirstChar { it.uppercase() }
