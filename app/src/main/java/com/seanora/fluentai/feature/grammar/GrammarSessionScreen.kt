package com.seanora.fluentai.feature.grammar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seanora.fluentai.app.navigation.GrammarSessionMode
import com.seanora.fluentai.app.navigation.GrammarSessionSection
import com.seanora.fluentai.core.designsystem.components.DetailSectionSelector
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.model.Exercise
import com.seanora.fluentai.core.model.GrammarLesson

private enum class GrammarPracticeType(val label: String) {
    QUICK_CHECK("Quick Check"), SENTENCE_BUILDER("Sentence Builder"), ERROR_SPOTTER("Error Spotter")
}

@Composable
fun GrammarSessionScreen(
    lessonId: String,
    mode: GrammarSessionMode,
    initialSection: GrammarSessionSection,
    onNavigateBack: () -> Unit,
    onFinish: () -> Unit = onNavigateBack,
    modifier: Modifier = Modifier,
    lessonViewModel: GrammarViewModel = hiltViewModel(),
    featureViewModel: GrammarFeatureViewModel = hiltViewModel(),
) {
    val lessonState by lessonViewModel.uiState.collectAsStateWithLifecycle()
    val featureState by featureViewModel.uiState.collectAsStateWithLifecycle()
    var section by rememberSaveable(lessonId) { mutableStateOf(initialSection) }
    var showTurkish by remember { mutableStateOf(false) }
    var showCompare by remember { mutableStateOf(false) }
    var attempted by rememberSaveable(lessonId) { mutableIntStateOf(0) }
    var correct by rememberSaveable(lessonId) { mutableIntStateOf(0) }

    LaunchedEffect(lessonId) {
        lessonViewModel.selectLessonById(lessonId)
    }
    LaunchedEffect(lessonId, featureState.isLoading) {
        if (!featureState.isLoading) featureViewModel.activateLesson(lessonId)
    }
    val lesson = lessonState.selectedLesson?.takeIf { it.id == lessonId }

    Surface(modifier.fillMaxSize(), color = DashboardColors.BackgroundPrimary, contentColor = DashboardColors.TextPrimary) {
    Column(Modifier.fillMaxSize().padding(horizontal = 30.dp, vertical = 22.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back") }
            Column(Modifier.weight(1f)) {
                Text(lesson?.let { "${it.cefrLevel} · ${it.category.humanize()}" } ?: "Grammar", color = DashboardColors.PrimaryPinkDark)
                Text(lesson?.title ?: if (lessonState.isLoading) "Loading lesson…" else "Lesson unavailable", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary)
                lesson?.let { Text(it.summaryEn, color = DashboardColors.TextSecondary) }
            }
            if (lesson != null) {
                TextButton(onClick = { showCompare = true }) { Text("Compare", color = DashboardColors.PrimaryPinkDark) }
                if (lesson.hasTurkishHelp()) {
                    TextButton(onClick = { showTurkish = true }) { Text("Turkish Help", color = DashboardColors.PrimaryPinkDark) }
                }
            }
        }
        if (lesson == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(if (lessonState.isLoading) "Loading…" else "This grammar lesson is not available.", color = DashboardColors.TextPrimary)
            }
            return@Column
        }

        if (mode == GrammarSessionMode.QUICK) {
            Text("Quick Grammar", Modifier.padding(top = 10.dp), fontWeight = FontWeight.SemiBold, color = DashboardColors.PrimaryPinkDark)
        } else {
            val sectionLabels = GrammarSessionSection.entries.map { it.label }
            DetailSectionSelector(
                sections = sectionLabels,
                selectedSection = section.label,
                onSectionSelected = { selected ->
                    section = GrammarSessionSection.entries.first { it.label == selected }
                },
                modifier = Modifier.padding(vertical = 16.dp),
            )
        }

        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(vertical = 8.dp)) {
            when (section) {
                GrammarSessionSection.LEARN -> LearnSection(lesson, mode) {
                    section = GrammarSessionSection.PRACTICE
                }
                GrammarSessionSection.EXAMPLES -> ExamplesSection(lesson, mode)
                GrammarSessionSection.PRACTICE -> PracticeSection(
                    lesson = lesson,
                    exercises = lessonState.exercises,
                    featureState = featureState,
                    featureViewModel = featureViewModel,
                    onResult = { wasCorrect -> attempted++; if (wasCorrect) correct++ },
                    onReview = { section = GrammarSessionSection.REVIEW },
                )
                GrammarSessionSection.REVIEW -> ReviewSection(lesson, attempted, correct, { section = GrammarSessionSection.PRACTICE }, onFinish)
            }
        }
    }
    }

    if (showTurkish && lesson != null) TurkishHelpDialog(lesson) { showTurkish = false }
    if (showCompare) GrammarCompareDialog(featureState.lessons, lessonId) { showCompare = false }
}

@Composable
private fun LearnSection(lesson: GrammarLesson, mode: GrammarSessionMode, onPractice: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        lesson.explanationEn.take(if (mode == GrammarSessionMode.QUICK) 1 else lesson.explanationEn.size).forEach { explanation ->
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(explanation.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary)
                Text(explanation.content, lineHeight = 24.sp, color = DashboardColors.TextPrimary)
                explanation.patterns.forEach { StructureBlock(it) }
            }
        }
        lesson.rules.take(if (mode == GrammarSessionMode.QUICK) 1 else lesson.rules.size).forEach { rule ->
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(rule.name, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary)
                StructureBlock(rule.pattern)
                rule.useCases.forEach { Text("• $it", color = DashboardColors.TextSecondary) }
                rule.timeMarkers.forEach { Text(it, fontSize = 13.sp, color = DashboardColors.TextSecondary) }
            }
        }
        if (mode == GrammarSessionMode.QUICK) Button(onClick = onPractice) { Text("Practice this rule") }
    }
}

@Composable
private fun StructureBlock(text: String) {
    if (text.isBlank()) return
    Surface(shape = RoundedCornerShape(12.dp), color = DashboardColors.SurfacePink.copy(alpha = .65f)) {
        Text(text, Modifier.fillMaxWidth().padding(14.dp), fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = DashboardColors.TextPrimary)
    }
}

@Composable
private fun ExamplesSection(lesson: GrammarLesson, mode: GrammarSessionMode) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        lesson.examples.take(if (mode == GrammarSessionMode.QUICK) 2 else lesson.examples.size).forEach { example ->
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(example.en, fontSize = 18.sp, fontWeight = FontWeight.Medium, color = DashboardColors.TextPrimary)
                example.ruleHighlight?.takeIf(String::isNotBlank)?.let { Text(it, color = DashboardColors.PrimaryPinkDark) }
                example.context?.takeIf(String::isNotBlank)?.let { Text(it, color = DashboardColors.TextSecondary) }
            }
        }
    }
}

@Composable
private fun PracticeSection(
    lesson: GrammarLesson,
    exercises: List<Exercise>,
    featureState: GrammarFeatureUiState,
    featureViewModel: GrammarFeatureViewModel,
    onResult: (Boolean) -> Unit,
    onReview: () -> Unit,
) {
    val available = buildList {
        if (exercises.isNotEmpty()) add(GrammarPracticeType.QUICK_CHECK)
        if (featureState.currentSentence?.lessonId == lesson.id) add(GrammarPracticeType.SENTENCE_BUILDER)
        if (featureState.currentError?.lessonId == lesson.id) add(GrammarPracticeType.ERROR_SPOTTER)
    }
    var type by remember(lesson.id, available) { mutableStateOf(available.firstOrNull()) }
    var menu by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Recommended Practice", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary)
        if (available.isEmpty()) Text("No compatible practice is available for this lesson yet.", color = DashboardColors.TextPrimary) else {
            Box {
                TextButton(onClick = { menu = true }) { Text("Change practice type · ${type?.label}", color = DashboardColors.PrimaryPinkDark) }
                DropdownMenu(menu, { menu = false }) {
                    available.forEach { item -> DropdownMenuItem(text = { Text(item.label) }, onClick = { type = item; menu = false }) }
                }
            }
            when (type) {
                GrammarPracticeType.QUICK_CHECK -> QuickCheck(exercises, featureViewModel, onResult)
                GrammarPracticeType.SENTENCE_BUILDER -> SentenceBuilderPractice(featureState, featureViewModel, onResult)
                GrammarPracticeType.ERROR_SPOTTER -> ErrorSpotterPractice(featureState, featureViewModel, onResult)
                null -> Unit
            }
        }
        OutlinedButton(onClick = onReview) { Text("Review session", color = DashboardColors.TextPrimary) }
    }
}

@Composable
private fun QuickCheck(exercises: List<Exercise>, viewModel: GrammarFeatureViewModel, onResult: (Boolean) -> Unit) {
    var index by remember(exercises) { mutableIntStateOf(0) }
    val exercise = exercises[index.coerceIn(0, exercises.lastIndex)]
    var selected by remember(exercise.id) { mutableStateOf<String?>(null) }
    var submitted by remember(exercise.id) { mutableStateOf(false) }
    Text(exercise.promptEn, fontWeight = FontWeight.SemiBold, color = DashboardColors.TextPrimary)
    Text(exercise.stem, fontSize = 18.sp, color = DashboardColors.TextPrimary)
    exercise.options.forEach { option ->
        OutlinedButton(onClick = { if (!submitted) selected = option }, modifier = Modifier.fillMaxWidth()) { Text(if (selected == option) "• $option" else option, color = DashboardColors.TextPrimary) }
    }
    Button(onClick = { selected?.let { answer -> viewModel.recordLibraryExercise(exercise, answer); submitted = true; onResult(answer == exercise.correctAnswer) } }, enabled = selected != null && !submitted) { Text("Check answer") }
    if (submitted) Feedback(selected == exercise.correctAnswer, exercise.correctAnswer, exercise.explanationEn)
    if (submitted && exercises.size > 1) OutlinedButton(onClick = { index = (index + 1) % exercises.size }) { Text("Next exercise", color = DashboardColors.TextPrimary) }
}

@Composable
private fun SentenceBuilderPractice(state: GrammarFeatureUiState, viewModel: GrammarFeatureViewModel, onResult: (Boolean) -> Unit) {
    val question = state.currentSentence ?: return
    Text(question.lessonTitle, color = DashboardColors.TextSecondary)
    FlowRow(horizontalArrangement = Arrangement.spacedBy(7.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
        state.builtTokens.forEachIndexed { index, token -> Surface(Modifier.clickable { viewModel.removeSentenceToken(index) }, shape = RoundedCornerShape(8.dp), color = DashboardColors.SurfacePink) { Text(token, Modifier.padding(10.dp), color = DashboardColors.TextPrimary) } }
    }
    FlowRow(horizontalArrangement = Arrangement.spacedBy(7.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
        question.scrambledTokens.forEach { token -> OutlinedButton(onClick = { viewModel.addSentenceToken(token) }) { Text(token, color = DashboardColors.TextPrimary) } }
    }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton(onClick = viewModel::clearSentence) { Text("Clear", color = DashboardColors.TextPrimary) }
        Button(
            onClick = { viewModel.submitSentence()?.let(onResult) },
            enabled = state.builtTokens.isNotEmpty() && !state.sentenceSubmitted,
        ) { Text("Check") }
    }
    state.sentenceCorrect?.let { Feedback(it, question.correctSentence, question.explanation) }
    if (state.sentenceSubmitted) OutlinedButton(onClick = viewModel::nextSentence) { Text("Next sentence", color = DashboardColors.TextPrimary) }
}

@Composable
private fun ErrorSpotterPractice(state: GrammarFeatureUiState, viewModel: GrammarFeatureViewModel, onResult: (Boolean) -> Unit) {
    val question = state.currentError ?: return
    listOf(question.incorrectSentence, question.correctSentence).forEach { answer ->
        OutlinedButton(onClick = { viewModel.selectErrorAnswer(answer) }, modifier = Modifier.fillMaxWidth()) { Text(answer, color = DashboardColors.TextPrimary) }
    }
    Button(
        onClick = { viewModel.submitErrorAnswer()?.let(onResult) },
        enabled = state.errorSelection != null && !state.errorSubmitted,
    ) { Text("Check") }
    state.errorCorrect?.let { Feedback(it, question.correctSentence, question.explanation) }
    if (state.errorSubmitted) OutlinedButton(onClick = viewModel::nextError) { Text("Next example", color = DashboardColors.TextPrimary) }
}

@Composable
private fun Feedback(correct: Boolean, correctAnswer: String, explanation: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(if (correct) "Correct" else "Not quite", fontWeight = FontWeight.Bold, color = DashboardColors.PrimaryPinkDark)
        if (!correct) Text("Correct answer: $correctAnswer", color = DashboardColors.TextPrimary)
        if (explanation.isNotBlank()) Text(explanation, color = DashboardColors.TextSecondary)
    }
}

@Composable
private fun ReviewSection(lesson: GrammarLesson, attempted: Int, correct: Int, onPracticeAgain: () -> Unit, onFinish: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Lesson complete", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary)
        Text(lesson.title, fontSize = 18.sp, color = DashboardColors.TextPrimary)
        Text("$correct correct · $attempted attempted", color = DashboardColors.TextPrimary)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(onClick = onPracticeAgain) { Text(if (attempted > correct) "Review mistakes" else "Practice again", color = DashboardColors.TextPrimary) }
            Button(onClick = onFinish) { Text("Finish") }
        }
    }
}

@Composable
private fun TurkishHelpDialog(lesson: GrammarLesson, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        titleContentColor = DashboardColors.TextPrimary,
        textContentColor = DashboardColors.TextPrimary,
        title = { Text("Turkish Help") },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (lesson.explanationTr.isNotBlank()) Text(lesson.explanationTr)
                lesson.turkishTraps.forEach { trap ->
                    Column { Text(trap.trapTitle, fontWeight = FontWeight.Bold); Text(trap.explanationTr); Text("✗ ${trap.incorrectExample}"); Text("✓ ${trap.correctExample}") }
                }
                lesson.contrasts.forEach { contrast -> Text("${contrast.structureA} / ${contrast.structureB}\n${contrast.differenceExplanationTr}") }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = DashboardColors.PrimaryPinkDark)
            }
        },
    )
}

private val GrammarSessionSection.label: String
    get() = name.lowercase().replaceFirstChar(Char::titlecase)

private fun GrammarLesson.hasTurkishHelp(): Boolean =
    explanationTr.isNotBlank() || turkishTraps.isNotEmpty() || contrasts.any {
        it.differenceExplanationTr.isNotBlank()
    }
