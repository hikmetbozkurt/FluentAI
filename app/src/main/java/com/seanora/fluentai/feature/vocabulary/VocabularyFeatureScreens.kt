package com.seanora.fluentai.feature.vocabulary

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seanora.fluentai.app.navigation.VocabularySessionMode
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.model.VocabItem
import com.seanora.fluentai.core.model.VocabularyPracticeMode
import com.seanora.fluentai.domain.vocabulary.FlashRecallOutcome
import kotlinx.coroutines.delay

@Composable
fun VocabularyCollectionsScreen(
    onNavigateBack: () -> Unit,
    onOpenWord: (String) -> Unit,
    onPracticeCollection: (String) -> Unit,
    initialTopicId: String? = null,
    modifier: Modifier = Modifier,
    viewModel: VocabularyFeatureViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(initialTopicId, state.isLoading, state.collections) {
        if (!state.isLoading && initialTopicId != null && state.selectedCollection?.topicId != initialTopicId) {
            state.collections.firstOrNull { it.topicId == initialTopicId }?.let(viewModel::selectCollection)
        }
    }
    Column(modifier.fillMaxSize().background(DashboardColors.BackgroundPrimary).padding(28.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back to Vocabulary") }
            Column(Modifier.weight(1f)) {
                Text(state.selectedCollection?.name ?: "Collections", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary)
                Text(state.selectedCollection?.let { "${state.collectionWords.size} words" } ?: "Explore real curriculum topics.", color = DashboardColors.TextSecondary)
            }
            state.selectedCollection?.let { collection -> Button(onClick = { onPracticeCollection(collection.topicId) }) { Text("Practice collection") } }
        }
        val selected = state.selectedCollection
        if (selected == null) LazyColumn(verticalArrangement = Arrangement.spacedBy(9.dp)) {
            items(state.collections, key = { it.topicId }) { collection ->
                Surface(Modifier.fillMaxWidth().clickable { viewModel.selectCollection(collection) }, shape = RoundedCornerShape(15.dp), color = Color.White) {
                    Row(Modifier.padding(17.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) { Text(collection.name, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary); Text("${collection.wordCount} words", color = DashboardColors.TextSecondary) }
                        Icon(Icons.AutoMirrored.Rounded.ArrowForward, null, tint = DashboardColors.PrimaryPinkDark)
                    }
                }
            }
        } else {
            TextButton(onClick = viewModel::clearCollection) { Text("← All collections", color = DashboardColors.PrimaryPinkDark) }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.collectionWords, key = { it.id }) { word -> WordRow(word) { onOpenWord(word.id) } }
            }
        }
    }
}

@Composable
fun VocabularyPracticeSessionScreen(
    mode: VocabularySessionMode,
    scopeId: String?,
    onNavigateBack: () -> Unit,
    onOpenWord: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VocabularyFeatureViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var pickerOpen by remember { mutableStateOf(false) }
    val effectiveScope = scopeId?.takeIf { it.isNotBlank() }
    val scopeIds = when {
        effectiveScope == null -> null
        effectiveScope.startsWith("vocab.") -> setOf(effectiveScope)
        state.selectedCollection?.topicId == effectiveScope -> state.collectionWords.mapTo(hashSetOf()) { it.id }
        else -> emptySet()
    }
    val availableModes = remember(state.allItems, scopeIds) { viewModel.availablePracticeModes(scopeIds) }
    LaunchedEffect(effectiveScope, state.isLoading, state.collections) {
        if (!state.isLoading && effectiveScope != null && !effectiveScope.startsWith("vocab.") && state.selectedCollection?.topicId != effectiveScope) {
            state.collections.firstOrNull { it.topicId == effectiveScope }?.let(viewModel::selectCollection)
        }
    }
    LaunchedEffect(mode, effectiveScope, state.isLoading, scopeIds) {
        if (!state.isLoading && (effectiveScope == null || scopeIds?.isNotEmpty() == true) && state.practiceSession == null && !state.isFlashPractice) {
            when (mode) {
                VocabularySessionMode.SMART -> if (scopeIds?.size == 1) {
                    viewModel.startFlashPractice(scopeIds)
                } else {
                    viewModel.startSmartPractice(scopeIds)
                }
                VocabularySessionMode.FLASH_CARDS -> viewModel.startFlashPractice(scopeIds)
                else -> mode.practiceMode?.let { viewModel.startPractice(it, scopeIds) }
            }
        }
    }
    LaunchedEffect(state.practiceSession?.mode, state.practiceIndex, state.practiceSecondsRemaining, state.submittedPracticeAnswer) {
        if (state.practiceSession?.mode == VocabularyPracticeMode.SPEED_DRILL && !state.submittedPracticeAnswer && state.practiceSecondsRemaining != null) {
            delay(1_000); viewModel.tickPracticeTimer()
        }
    }

    Column(modifier.fillMaxSize().background(DashboardColors.BackgroundPrimary).padding(30.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            val compact = maxWidth < 650.dp
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back to Vocabulary") }
                    Column(Modifier.weight(1f)) {
                        Text("Smart Practice", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary)
                        Text(if (state.isFlashPractice) "Flash Cards" else state.practiceSession?.mode?.title ?: "Preparing a focused session", color = DashboardColors.TextSecondary)
                    }
                    if (!compact) PracticeTypePicker(pickerOpen, { pickerOpen = it }, scopeIds, availableModes, viewModel)
                }
                if (compact) PracticeTypePicker(pickerOpen, { pickerOpen = it }, scopeIds, availableModes, viewModel)
            }
        }
        if (state.isFlashPractice) FlashPractice(state, viewModel, onNavigateBack)
        else StandardPractice(state, viewModel, onNavigateBack, onOpenWord)
    }
}

@Composable
private fun PracticeTypePicker(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    scopeIds: Set<String>?,
    availableModes: List<VocabularyPracticeMode>,
    viewModel: VocabularyFeatureViewModel,
) {
    Box {
        OutlinedButton(onClick = { onExpandedChange(true) }) { Text("Choose practice type", color = DashboardColors.TextPrimary) }
        DropdownMenu(expanded, { onExpandedChange(false) }) {
            if (scopeIds == null || scopeIds.isNotEmpty()) {
                DropdownMenuItem(text = { Text("Flash Cards") }, onClick = { viewModel.exitPractice(); viewModel.startFlashPractice(scopeIds); onExpandedChange(false) })
            }
            availableModes.forEach { practiceMode ->
                DropdownMenuItem(text = { Text(practiceMode.title) }, onClick = { viewModel.startPractice(practiceMode, scopeIds); onExpandedChange(false) })
            }
        }
    }
}

@Composable
private fun StandardPractice(state: VocabularyFeatureUiState, viewModel: VocabularyFeatureViewModel, onFinish: () -> Unit, onOpenWord: (String) -> Unit) {
    val session = state.practiceSession
    val question = state.currentPracticeQuestion
    when {
        session == null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Preparing practice…", color = DashboardColors.TextPrimary) }
        session.questions.isEmpty() -> Text(session.unavailableReason ?: "No compatible practice is available.", color = DashboardColors.TextSecondary)
        state.isPracticeComplete || question == null -> {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Practice complete", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary)
                Text("${session.questions.size} words practiced", color = DashboardColors.TextPrimary)
                Text("${state.practiceCorrectCount} correct · ${state.practiceIncorrectIds.size} to revisit", color = DashboardColors.TextSecondary)
                state.practiceIncorrectIds.forEach { id -> state.allItems.firstOrNull { it.id == id }?.let { word -> TextButton(onClick = { onOpenWord(id) }) { Text(word.headword, color = DashboardColors.PrimaryPinkDark) } } }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (state.practiceIncorrectIds.isNotEmpty()) OutlinedButton(onClick = { viewModel.startSmartPractice(state.practiceIncorrectIds) }) { Text("Review mistakes", color = DashboardColors.TextPrimary) }
                    Button(onClick = onFinish) { Text("Finish") }
                }
            }
        }
        else -> {
            Column(verticalArrangement = Arrangement.spacedBy(13.dp)) {
                Text("${state.practiceIndex + 1} of ${session.questions.size}", color = DashboardColors.TextSecondary)
                state.practiceSecondsRemaining?.let { Text("${it}s", fontWeight = FontWeight.Bold, color = DashboardColors.PrimaryPinkDark) }
                Text(question.prompt, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary)
                question.options.forEach { option ->
                    OutlinedButton(onClick = { viewModel.selectPracticeAnswer(option) }, enabled = !state.submittedPracticeAnswer, modifier = Modifier.fillMaxWidth()) { Text(if (state.selectedPracticeAnswer == option) "• $option" else option, color = DashboardColors.TextPrimary) }
                }
                if (state.submittedPracticeAnswer) {
                    Text(if (state.lastPracticeCorrect == true) "Correct" else "Not quite", fontWeight = FontWeight.Bold, color = DashboardColors.PrimaryPinkDark)
                    if (state.lastPracticeCorrect != true) Text("Correct answer: ${question.correctAnswer}", color = DashboardColors.TextPrimary)
                    Button(onClick = viewModel::nextPracticeQuestion) { Text("Next") }
                } else Button(onClick = viewModel::submitPracticeAnswer, enabled = state.selectedPracticeAnswer != null) { Text("Check answer") }
            }
        }
    }
}

@Composable
private fun FlashPractice(state: VocabularyFeatureUiState, viewModel: VocabularyFeatureViewModel, onFinish: () -> Unit) {
    val card = state.currentFlashCard
    when {
        state.flashCards.isEmpty() -> Text("No words are currently available for Flash Cards.", color = DashboardColors.TextSecondary)
        state.isFlashComplete || card == null -> Column(verticalArrangement = Arrangement.spacedBy(12.dp)) { Text("Practice complete", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary); Text("${state.flashCards.size} words reviewed", color = DashboardColors.TextSecondary); Button(onClick = onFinish) { Text("Finish") } }
        else -> Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("${state.flashIndex + 1} of ${state.flashCards.size}", color = DashboardColors.TextSecondary)
            Text(card.headword, fontSize = 34.sp, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary)
            if (!state.isFlashRevealed) Button(onClick = viewModel::revealFlashCard) { Text("Reveal") } else {
                Text(card.definitionEn, fontSize = 20.sp, color = DashboardColors.TextPrimary)
                card.examples.firstOrNull()?.let { Text(it.en, color = DashboardColors.TextSecondary) }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FlashRecallOutcome.entries.forEach { outcome -> OutlinedButton(onClick = { viewModel.rateFlashCard(outcome) }, modifier = Modifier.weight(1f)) { Text(outcome.title, color = DashboardColors.TextPrimary) } }
                }
            }
        }
    }
}

@Composable
private fun WordRow(word: VocabItem, onClick: () -> Unit) {
    Surface(Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(14.dp), color = Color.White) {
        Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) { Text(word.headword, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary); Text("${word.cefrLevel} · ${word.partOfSpeech}", color = DashboardColors.PrimaryPinkDark); Text(word.definitionEn, maxLines = 1, overflow = TextOverflow.Ellipsis, color = DashboardColors.TextSecondary) }
            Icon(Icons.AutoMirrored.Rounded.ArrowForward, null, tint = DashboardColors.PrimaryPinkDark)
        }
    }
}
