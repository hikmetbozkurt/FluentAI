package com.seanora.fluentai.feature.vocabulary

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seanora.fluentai.app.navigation.VocabularySessionMode
import com.seanora.fluentai.core.audio.OnDeviceTtsSpeaker
import com.seanora.fluentai.core.audio.TtsAccent
import com.seanora.fluentai.core.audio.rememberOnDeviceTtsSpeaker
import com.seanora.fluentai.core.designsystem.components.BadgeSize
import com.seanora.fluentai.core.designsystem.components.LevelBadge
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.designsystem.dashboard.DashboardTypography
import com.seanora.fluentai.core.designsystem.kit.FluentAnswerVerdict
import com.seanora.fluentai.core.designsystem.kit.FluentBackButton
import com.seanora.fluentai.core.designsystem.kit.LearningModule
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
    val tts = rememberOnDeviceTtsSpeaker()

    LaunchedEffect(initialTopicId, state.isLoading, state.collections) {
        if (!state.isLoading && initialTopicId != null && state.selectedCollection?.topicId != initialTopicId) {
            state.collections.firstOrNull { it.topicId == initialTopicId }?.let(viewModel::selectCollection)
        }
    }

    Column(
        modifier
            .fillMaxSize()
            .background(DashboardColors.BackgroundPrimary)
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            FluentBackButton(
                onClick = {
                    if (state.selectedCollection != null && initialTopicId == null) viewModel.clearCollection()
                    else onNavigateBack()
                },
                label = if (state.selectedCollection != null && initialTopicId == null) "Collections" else "Vocabulary",
                accent = LearningModule.VOCABULARY.accent,
            )
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    state.selectedCollection?.name ?: "Collections",
                    style = DashboardTypography.PageTitle,
                    color = DashboardColors.TextPrimary,
                )
                Text(
                    state.selectedCollection?.let { "${state.collectionWords.size} curated words" }
                        ?: "${state.collections.size} thematic packs",
                    style = DashboardTypography.Caption.copy(fontFeatureSettings = "tnum"),
                    color = DashboardColors.TextSecondary,
                )
            }
            state.selectedCollection?.let { collection ->
                Button(
                    onClick = { onPracticeCollection(collection.topicId) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LearningModule.VOCABULARY.accent),
                ) {
                    Icon(Icons.Rounded.PlayArrow, null, Modifier.size(18.dp))
                    Spacer(Modifier.size(4.dp))
                    Text("Practice pack")
                }
            }
        }

        val selected = state.selectedCollection
        if (selected == null) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 260.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
                modifier = Modifier.weight(1f),
            ) {
                items(state.collections, key = { it.topicId }) { collection ->
                    Surface(
                        onClick = { viewModel.selectCollection(collection) },
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, DashboardColors.BorderSoft),
                    ) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    "${collection.wordCount} words",
                                    style = DashboardTypography.Caption.copy(fontFeatureSettings = "tnum"),
                                    color = DashboardColors.PrimaryPinkDark,
                                )
                                Icon(Icons.AutoMirrored.Rounded.ArrowForward, null, tint = DashboardColors.TextMuted, modifier = Modifier.size(16.dp))
                            }
                            Text(
                                collection.name,
                                style = DashboardTypography.CardTitle,
                                color = DashboardColors.TextPrimary,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
                modifier = Modifier.weight(1f),
            ) {
                items(state.collectionWords, key = { it.id }) { word ->
                    WordRow(word = word, onPronounce = { tts.speak(word.headword) }) { onOpenWord(word.id) }
                }
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
    val tts = rememberOnDeviceTtsSpeaker()
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

    Column(
        modifier
            .fillMaxSize()
            .background(DashboardColors.BackgroundPrimary)
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BoxWithConstraints(Modifier.widthIn(max = 960.dp).fillMaxWidth()) {
            val compact = maxWidth < 650.dp
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FluentBackButton(onClick = onNavigateBack, label = "Vocabulary", accent = LearningModule.VOCABULARY.accent)
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            if (state.isFlashPractice) "Flash cards" else state.practiceSession?.mode?.title ?: "Smart practice",
                            style = DashboardTypography.PageTitle,
                            color = DashboardColors.TextPrimary,
                        )
                        Text(
                            if (state.isFlashPractice) "Active recall with spaced repetition" else "Focused vocabulary drill",
                            style = DashboardTypography.Caption,
                            color = DashboardColors.TextSecondary,
                        )
                    }
                    if (!compact) PracticeTypePicker(pickerOpen, { pickerOpen = it }, scopeIds, availableModes, viewModel)
                }
                if (compact) PracticeTypePicker(pickerOpen, { pickerOpen = it }, scopeIds, availableModes, viewModel)
            }
        }

        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.TopCenter,
        ) {
            if (state.isFlashPractice) {
                FlashPractice(state, viewModel, tts, onNavigateBack)
            } else {
                StandardPractice(state, viewModel, onNavigateBack, onOpenWord)
            }
        }
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
        OutlinedButton(onClick = { onExpandedChange(true) }, shape = RoundedCornerShape(12.dp)) {
            Text("Switch mode", color = DashboardColors.TextPrimary)
            Icon(Icons.Rounded.ExpandMore, null, tint = DashboardColors.TextSecondary, modifier = Modifier.size(18.dp))
        }
        DropdownMenu(expanded, { onExpandedChange(false) }, containerColor = Color.White) {
            if (scopeIds == null || scopeIds.isNotEmpty()) {
                DropdownMenuItem(
                    text = { Text("Flash Cards", color = DashboardColors.TextPrimary) },
                    onClick = { viewModel.exitPractice(); viewModel.startFlashPractice(scopeIds); onExpandedChange(false) },
                )
            }
            availableModes.forEach { practiceMode ->
                DropdownMenuItem(
                    text = { Text(practiceMode.title, color = DashboardColors.TextPrimary) },
                    onClick = { viewModel.startPractice(practiceMode, scopeIds); onExpandedChange(false) },
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StandardPractice(
    state: VocabularyFeatureUiState,
    viewModel: VocabularyFeatureViewModel,
    onFinish: () -> Unit,
    onOpenWord: (String) -> Unit,
) {
    val session = state.practiceSession
    val question = state.currentPracticeQuestion
    when {
        session == null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = LearningModule.VOCABULARY.accent)
        }
        session.questions.isEmpty() -> Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = BorderStroke(1.dp, DashboardColors.BorderSoft),
            modifier = Modifier.widthIn(max = 560.dp).fillMaxWidth(),
        ) {
            Text(
                session.unavailableReason ?: "No compatible practice is available for this selection.",
                color = DashboardColors.TextSecondary,
                modifier = Modifier.padding(24.dp),
            )
        }
        state.isPracticeComplete || question == null -> {
            val total = session.questions.size.coerceAtLeast(1)
            val accuracy = (state.practiceCorrectCount * 100) / total
            Surface(
                modifier = Modifier.widthIn(max = 640.dp).fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(1.dp, DashboardColors.BorderSoft),
            ) {
                Column(
                    Modifier.padding(24.dp).verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text("Session complete", style = DashboardTypography.PageTitle, color = DashboardColors.TextPrimary)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = RoundedCornerShape(12.dp), color = DashboardColors.SurfacePink) {
                            Text(
                                "$accuracy% accuracy",
                                style = DashboardTypography.CardTitle.copy(fontFeatureSettings = "tnum"),
                                color = DashboardColors.PrimaryPinkDark,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            )
                        }
                        Text(
                            "${state.practiceCorrectCount} / $total correct · ${state.practiceIncorrectIds.size} to revisit",
                            style = DashboardTypography.Body.copy(fontFeatureSettings = "tnum"),
                            color = DashboardColors.TextSecondary,
                        )
                    }
                    if (state.practiceIncorrectIds.isNotEmpty()) {
                        Text("Words to revisit", style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            state.practiceIncorrectIds.forEach { id ->
                                state.allItems.firstOrNull { it.id == id }?.let { word ->
                                    Surface(
                                        onClick = { onOpenWord(id) },
                                        shape = RoundedCornerShape(10.dp),
                                        color = DashboardColors.BackgroundSecondary,
                                        border = BorderStroke(1.dp, DashboardColors.BorderSoft),
                                    ) {
                                        Text(
                                            word.headword,
                                            fontWeight = FontWeight.Medium,
                                            color = DashboardColors.PrimaryPinkDark,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (state.practiceIncorrectIds.isNotEmpty()) {
                            OutlinedButton(
                                onClick = { viewModel.startSmartPractice(state.practiceIncorrectIds) },
                                shape = RoundedCornerShape(12.dp),
                            ) {
                                Text("Review mistakes", color = DashboardColors.TextPrimary)
                            }
                        }
                        Button(
                            onClick = onFinish,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = LearningModule.VOCABULARY.accent),
                        ) {
                            Text("Finish")
                        }
                    }
                }
            }
        }
        else -> {
            val total = session.questions.size
            val progress = (state.practiceIndex + 1).toFloat() / total.coerceAtLeast(1)
            Column(
                modifier = Modifier
                    .widthIn(max = 720.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "Question ${state.practiceIndex + 1} of $total",
                        style = DashboardTypography.Caption.copy(fontFeatureSettings = "tnum"),
                        color = DashboardColors.TextSecondary,
                    )
                    state.practiceSecondsRemaining?.let { secs ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (secs <= 5) Color(0xFFFEE4E2) else DashboardColors.SurfacePink,
                        ) {
                            Row(
                                Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Icon(Icons.Rounded.Timer, null, tint = DashboardColors.PrimaryPinkDark, modifier = Modifier.size(14.dp))
                                Text(
                                    "${secs}s",
                                    style = DashboardTypography.Caption.copy(fontWeight = FontWeight.SemiBold, fontFeatureSettings = "tnum"),
                                    color = DashboardColors.PrimaryPinkDark,
                                )
                            }
                        }
                    }
                }
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                    color = LearningModule.VOCABULARY.accent,
                    trackColor = DashboardColors.ProgressTrack,
                )

                // Question Prompt Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, DashboardColors.BorderSoft),
                ) {
                    Text(
                        question.prompt,
                        fontSize = 20.sp,
                        lineHeight = 28.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DashboardColors.TextPrimary,
                        modifier = Modifier.padding(20.dp),
                    )
                }

                // Styled Option Cards
                question.options.forEachIndexed { index, option ->
                    val label = ('A'.code + index).toChar().toString()
                    val isSelected = state.selectedPracticeAnswer == option
                    val isCorrectOption = state.submittedPracticeAnswer && option == question.correctAnswer
                    val isWrongSelection = state.submittedPracticeAnswer && isSelected && option != question.correctAnswer
                    val container = when {
                        isCorrectOption -> Color(0xFFE6F6EC)
                        isWrongSelection -> Color(0xFFFDECEC)
                        isSelected -> DashboardColors.SurfacePink.copy(alpha = 0.7f)
                        else -> Color.White
                    }
                    val border = when {
                        isCorrectOption -> DashboardColors.PlantGreen
                        isWrongSelection -> Color(0xFFD92D20)
                        isSelected -> LearningModule.VOCABULARY.accent
                        else -> DashboardColors.BorderSoft
                    }
                    Surface(
                        onClick = { if (!state.submittedPracticeAnswer) viewModel.selectPracticeAnswer(option) },
                        enabled = !state.submittedPracticeAnswer,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = container,
                        border = BorderStroke(if (isSelected || isCorrectOption || isWrongSelection) 1.5.dp else 1.dp, border),
                    ) {
                        Row(
                            Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Box(
                                Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) LearningModule.VOCABULARY.accent else DashboardColors.BackgroundSecondary),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    label,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = if (isSelected) Color.White else DashboardColors.TextPrimary,
                                )
                            }
                            Text(
                                option,
                                modifier = Modifier.weight(1f),
                                fontSize = 15.sp,
                                color = DashboardColors.TextPrimary,
                            )
                            if (isCorrectOption) Icon(Icons.Rounded.CheckCircle, "Correct", tint = DashboardColors.PlantGreen)
                            if (isWrongSelection) Icon(Icons.Rounded.Cancel, "Incorrect", tint = Color(0xFFD92D20))
                        }
                    }
                }

                if (state.submittedPracticeAnswer) {
                    FluentAnswerVerdict(correct = state.lastPracticeCorrect == true)
                    if (state.lastPracticeCorrect != true) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, DashboardColors.BorderSoft),
                        ) {
                            Text(
                                "Correct answer: ${question.correctAnswer}",
                                fontWeight = FontWeight.Medium,
                                color = DashboardColors.TextPrimary,
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                            )
                        }
                    }
                    Button(
                        onClick = viewModel::nextPracticeQuestion,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LearningModule.VOCABULARY.accent),
                        modifier = Modifier.align(Alignment.End),
                    ) {
                        Text(if (state.practiceIndex == total - 1) "See results" else "Next question")
                    }
                } else {
                    Button(
                        onClick = viewModel::submitPracticeAnswer,
                        enabled = state.selectedPracticeAnswer != null,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LearningModule.VOCABULARY.accent),
                        modifier = Modifier.align(Alignment.End),
                    ) {
                        Text("Check answer")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlashPractice(
    state: VocabularyFeatureUiState,
    viewModel: VocabularyFeatureViewModel,
    tts: OnDeviceTtsSpeaker,
    onFinish: () -> Unit,
) {
    val card = state.currentFlashCard
    when {
        state.flashCards.isEmpty() -> Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = BorderStroke(1.dp, DashboardColors.BorderSoft),
            modifier = Modifier.widthIn(max = 560.dp).fillMaxWidth(),
        ) {
            Text(
                "No words are currently available for Flash Cards.",
                color = DashboardColors.TextSecondary,
                modifier = Modifier.padding(24.dp),
            )
        }
        state.isFlashComplete || card == null -> Surface(
            modifier = Modifier.widthIn(max = 560.dp).fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            border = BorderStroke(1.dp, DashboardColors.BorderSoft),
        ) {
            Column(
                Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(Icons.Rounded.CheckCircle, null, tint = DashboardColors.PlantGreen, modifier = Modifier.size(40.dp))
                Text("Flash card review complete", style = DashboardTypography.PageTitle, color = DashboardColors.TextPrimary)
                Text("${state.flashCards.size} words reviewed and scheduled", color = DashboardColors.TextSecondary)
                Button(
                    onClick = onFinish,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LearningModule.VOCABULARY.accent),
                ) {
                    Text("Done")
                }
            }
        }
        else -> {
            val total = state.flashCards.size
            val progress = (state.flashIndex + 1).toFloat() / total.coerceAtLeast(1)
            Column(
                modifier = Modifier
                    .widthIn(max = 660.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        "Card ${state.flashIndex + 1} of $total",
                        style = DashboardTypography.Caption.copy(fontFeatureSettings = "tnum"),
                        color = DashboardColors.TextSecondary,
                    )
                    Text(
                        "${(progress * 100).toInt()}%",
                        style = DashboardTypography.Caption.copy(fontFeatureSettings = "tnum"),
                        color = LearningModule.VOCABULARY.accent,
                    )
                }
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                    color = LearningModule.VOCABULARY.accent,
                    trackColor = DashboardColors.ProgressTrack,
                )

                // Flashcard Surface
                Surface(
                    onClick = { if (!state.isFlashRevealed) viewModel.revealFlashCard() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, if (state.isFlashRevealed) LearningModule.VOCABULARY.accent.copy(alpha = 0.45f) else DashboardColors.BorderSoft),
                    shadowElevation = 0.dp,
                ) {
                    Column(
                        Modifier.fillMaxWidth().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            LevelBadge(levelString = card.cefrLevel, size = BadgeSize.Compact)
                            Surface(shape = RoundedCornerShape(8.dp), color = DashboardColors.BackgroundSecondary) {
                                Text(
                                    card.partOfSpeech.replace('_', ' '),
                                    style = DashboardTypography.Caption,
                                    color = DashboardColors.TextSecondary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                )
                            }
                        }

                        Text(
                            card.headword,
                            fontSize = 34.sp,
                            lineHeight = 40.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DashboardColors.TextPrimary,
                            textAlign = TextAlign.Center,
                        )
                        if (card.phonetic.isNotBlank()) {
                            Text(card.phonetic, fontSize = 15.sp, color = DashboardColors.TextSecondary)
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            PronunciationPillButton(label = "US", onClick = { tts.speak(card.headword, TtsAccent.US) })
                            PronunciationPillButton(label = "UK", onClick = { tts.speak(card.headword, TtsAccent.UK) })
                        }

                        if (!state.isFlashRevealed) {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Recall the English meaning, then tap to flip",
                                style = DashboardTypography.Caption,
                                color = DashboardColors.TextMuted,
                            )
                            Button(
                                onClick = viewModel::revealFlashCard,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = LearningModule.VOCABULARY.accent),
                                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                            ) {
                                Text("Show answer")
                            }
                        } else {
                            Box(Modifier.fillMaxWidth().height(1.dp).background(DashboardColors.BorderSoft))
                            Text(
                                card.definitionEn,
                                fontSize = 19.sp,
                                lineHeight = 28.sp,
                                color = DashboardColors.TextPrimary,
                                textAlign = TextAlign.Center,
                            )
                            card.examples.firstOrNull()?.let { ex ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = DashboardColors.BackgroundPrimary,
                                    border = BorderStroke(1.dp, DashboardColors.BorderSoft),
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth().padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    ) {
                                        Box(Modifier.weight(1f)) { VocabularyExampleText(ex.en, card.headword) }
                                        IconButton(onClick = { tts.speak(ex.en) }, modifier = Modifier.size(32.dp)) {
                                            Icon(Icons.AutoMirrored.Rounded.VolumeUp, "Listen", tint = DashboardColors.PrimaryPinkDark, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }
                            }
                            if (card.collocations.isNotEmpty()) {
                                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    card.collocations.take(4).forEach { c ->
                                        Surface(shape = RoundedCornerShape(8.dp), color = DashboardColors.BackgroundSecondary) {
                                            Text(c.text, fontSize = 12.sp, color = DashboardColors.TextPrimary, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                                        }
                                    }
                                }
                            }
                            if (card.meaningTr.isNotBlank()) {
                                if (state.isFlashTurkishRevealed) {
                                    Surface(shape = RoundedCornerShape(10.dp), color = DashboardColors.SurfacePink.copy(alpha = 0.55f)) {
                                        Text(
                                            "TR · ${card.meaningTr}",
                                            fontWeight = FontWeight.Medium,
                                            color = DashboardColors.TextPrimary,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        )
                                    }
                                } else {
                                    TextButton(onClick = viewModel::toggleFlashTurkish) {
                                        Icon(Icons.Rounded.Translate, null, tint = DashboardColors.PrimaryPinkDark, modifier = Modifier.size(16.dp))
                                        Spacer(Modifier.size(4.dp))
                                        Text("Show Turkish", color = DashboardColors.PrimaryPinkDark)
                                    }
                                }
                            }
                        }
                    }
                }

                // 4 Color-Coded SRS Rating Buttons
                if (state.isFlashRevealed) {
                    Text("How well did you recall this word?", style = DashboardTypography.Caption, color = DashboardColors.TextSecondary)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        FlashRecallOutcome.entries.forEach { outcome ->
                            val (accent, intervalHint) = when (outcome) {
                                FlashRecallOutcome.AGAIN -> Color(0xFFD92D20) to "10 min"
                                FlashRecallOutcome.HARD -> DashboardColors.Warning to "1 day"
                                FlashRecallOutcome.GOOD -> DashboardColors.PlantGreen to "4 days"
                                FlashRecallOutcome.EASY -> Color(0xFF2F74E0) to "7+ days"
                            }
                            Surface(
                                onClick = { viewModel.rateFlashCard(outcome) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp),
                                color = accent.copy(alpha = 0.10f),
                                border = BorderStroke(1.2.dp, accent.copy(alpha = 0.35f)),
                            ) {
                                Column(
                                    Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(2.dp),
                                ) {
                                    Text(outcome.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = accent)
                                    Text(intervalHint, fontSize = 11.sp, color = DashboardColors.TextSecondary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WordRow(word: VocabItem, onPronounce: () -> Unit, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, DashboardColors.BorderSoft),
    ) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(word.headword, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DashboardColors.TextPrimary)
                    LevelBadge(levelString = word.cefrLevel, size = BadgeSize.Compact)
                }
                Text(word.definitionEn, maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 13.sp, color = DashboardColors.TextSecondary)
            }
            IconButton(onClick = onPronounce, modifier = Modifier.size(36.dp)) {
                Icon(Icons.AutoMirrored.Rounded.VolumeUp, "Pronounce", tint = DashboardColors.PrimaryPinkDark, modifier = Modifier.size(18.dp))
            }
            Icon(Icons.AutoMirrored.Rounded.ArrowForward, null, tint = DashboardColors.PrimaryPinkDark, modifier = Modifier.size(18.dp))
        }
    }
}
