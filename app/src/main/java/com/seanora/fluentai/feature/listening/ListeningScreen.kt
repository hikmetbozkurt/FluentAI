package com.seanora.fluentai.feature.listening

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Forward10
import androidx.compose.material.icons.rounded.Headphones
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Replay10
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seanora.fluentai.app.navigation.ListeningSessionMode
import com.seanora.fluentai.core.designsystem.components.ButtonVariant
import com.seanora.fluentai.core.designsystem.components.FluentButton
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.designsystem.dashboard.DashboardDimensions
import com.seanora.fluentai.core.designsystem.dashboard.DashboardPageHeader
import com.seanora.fluentai.core.designsystem.dashboard.DashboardSpacing
import com.seanora.fluentai.core.designsystem.dashboard.DashboardTypography
import com.seanora.fluentai.core.designsystem.dashboard.LingoPinkFluentColors
import com.seanora.fluentai.core.designsystem.theme.LocalFluentColors
import com.seanora.fluentai.core.model.ListeningComprehensionQuestion
import com.seanora.fluentai.core.model.ListeningScenario
import com.seanora.fluentai.core.model.TranscriptItem

@Composable
fun ListeningScreen(
    modifier: Modifier = Modifier,
    viewModel: ListeningViewModel = hiltViewModel(),
    onNavigateBack: (() -> Unit)? = null,
    initialContentId: String? = null,
) {
    var locallySelectedId by rememberSaveable { mutableStateOf(initialContentId) }
    if (locallySelectedId == null) {
        ListeningLibraryScreen(
            onNavigateBack = onNavigateBack ?: {},
            onOpenSession = { locallySelectedId = it },
            modifier = modifier,
            viewModel = viewModel,
        )
    } else {
        val sessionBack: () -> Unit = if (initialContentId != null && onNavigateBack != null) {
            onNavigateBack
        } else {
            { locallySelectedId = null }
        }
        ListeningSessionScreen(
            listeningId = locallySelectedId.orEmpty(),
            initialMode = ListeningSessionMode.STANDARD,
            onNavigateBack = sessionBack,
            onFinish = onNavigateBack ?: { locallySelectedId = null },
            modifier = modifier,
            viewModel = viewModel,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ListeningLibraryScreen(
    onNavigateBack: () -> Unit,
    onOpenSession: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListeningViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var categoryMenuOpen by remember { mutableStateOf(false) }
    CompositionLocalProvider(LocalFluentColors provides LingoPinkFluentColors) {
        Column(
            modifier.fillMaxSize().background(DashboardColors.BackgroundPrimary).padding(DashboardDimensions.PagePadding),
            verticalArrangement = Arrangement.spacedBy(DashboardSpacing.md),
        ) {
            ListeningPageHeader("Listening Library", "Browse ${state.scenarios.size} recordings", onNavigateBack)
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = viewModel::updateSearchQuery,
                placeholder = { Text("Search listening...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DashboardColors.PrimaryPink,
                    unfocusedBorderColor = DashboardColors.BorderSoft,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                ),
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listOf<String?>(null, "A2", "B1", "B2", "C1", "C2").forEach { level ->
                    val selected = state.selectedLevel == level
                    FilterChip(
                        selected = selected,
                        onClick = { viewModel.selectLevel(level) },
                        label = { Text(level ?: "All") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DashboardColors.SurfacePink,
                            selectedLabelColor = DashboardColors.PrimaryPinkDark,
                        ),
                    )
                }
                Box {
                    FilterChip(
                        selected = state.selectedCategory != null,
                        onClick = { categoryMenuOpen = true },
                        label = { Text(state.selectedCategory?.toDisplayLabel() ?: "Topic") },
                        trailingIcon = { Icon(Icons.Rounded.KeyboardArrowDown, null, Modifier.size(18.dp)) },
                    )
                    DropdownMenu(expanded = categoryMenuOpen, onDismissRequest = { categoryMenuOpen = false }) {
                        DropdownMenuItem(
                            text = { Text("All topics") },
                            onClick = { viewModel.selectCategory(null); categoryMenuOpen = false },
                        )
                        state.availableCategories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.toDisplayLabel()) },
                                onClick = { viewModel.selectCategory(category); categoryMenuOpen = false },
                            )
                        }
                    }
                }
            }
            Text(
                "${state.scenarios.size} results",
                style = DashboardTypography.Caption,
                color = DashboardColors.TextSecondary,
            )
            when {
                state.isLoading -> LoadingPane()
                state.errorMessage != null -> MessagePane(state.errorMessage.orEmpty())
                state.scenarios.isEmpty() -> MessagePane("No recordings match these filters.")
                else -> LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(state.scenarios, key = { it.id }) { scenario ->
                        LibraryRecordingRow(scenario) { onOpenSession(scenario.id) }
                    }
                }
            }
        }
    }
}

@Composable
fun ListeningSessionScreen(
    listeningId: String,
    initialMode: ListeningSessionMode,
    onNavigateBack: () -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListeningViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(listeningId) { viewModel.selectScenarioById(listeningId) }
    LaunchedEffect(state.selectedScenario?.id, initialMode) {
        if (state.selectedScenario?.id == listeningId) {
            when (initialMode) {
                ListeningSessionMode.STANDARD -> Unit
                ListeningSessionMode.COMPREHENSION -> viewModel.selectSessionSection(ListeningSessionSection.QUESTIONS)
                ListeningSessionMode.DICTATION -> viewModel.startDictation()
            }
        }
    }

    CompositionLocalProvider(LocalFluentColors provides LingoPinkFluentColors) {
        Column(
            modifier.fillMaxSize().background(DashboardColors.BackgroundPrimary).padding(DashboardDimensions.PagePadding),
            verticalArrangement = Arrangement.spacedBy(DashboardSpacing.md),
        ) {
            val scenario = state.selectedScenario
            when {
                state.isLoading -> LoadingPane()
                scenario == null -> {
                    ListeningPageHeader("Listening Session", "Recording unavailable", onNavigateBack)
                    MessagePane(state.errorMessage ?: "Listening recording not found.")
                }
                else -> {
                    SessionHeader(scenario, onNavigateBack)
                    SessionNavigation(state.sessionSection, viewModel::selectSessionSection)
                    Box(Modifier.fillMaxWidth().weight(1f)) {
                        if (state.isDictationActive) {
                            DictationPane(state, viewModel)
                        } else {
                            when (state.sessionSection) {
                                ListeningSessionSection.LISTEN -> ListenPane(state, viewModel)
                                ListeningSessionSection.QUESTIONS -> QuestionsPane(state, viewModel)
                                ListeningSessionSection.TRANSCRIPT -> TranscriptPane(state, viewModel)
                                ListeningSessionSection.REVIEW -> ReviewPane(state, viewModel, onFinish)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SessionHeader(scenario: ListeningScenario, onNavigateBack: () -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onNavigateBack) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back", tint = DashboardColors.PrimaryPinkDark)
        }
        Column(Modifier.weight(1f)) {
            Text(scenario.title, style = DashboardTypography.PageTitle, color = DashboardColors.TextPrimary)
            Text(scenario.metadataLine(), style = DashboardTypography.Body, color = DashboardColors.TextSecondary)
        }
    }
}

@Composable
private fun SessionNavigation(
    selected: ListeningSessionSection,
    onSelect: (ListeningSessionSection) -> Unit,
) {
    Row(
        Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(14.dp)).padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        ListeningSessionSection.entries.forEach { section ->
            val active = section == selected
            Surface(
                color = if (active) DashboardColors.SurfacePink else Color.Transparent,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f).clickable { onSelect(section) },
            ) {
                Text(
                    section.label,
                    modifier = Modifier.padding(vertical = 11.dp),
                    style = DashboardTypography.Button,
                    color = if (active) DashboardColors.PrimaryPinkDark else DashboardColors.TextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun ListenPane(state: ListeningUiState, viewModel: ListeningViewModel) {
    val scenario = state.selectedScenario ?: return
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(DashboardSpacing.lg),
    ) {
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth().border(1.dp, DashboardColors.BorderSoft, RoundedCornerShape(24.dp)),
        ) {
            Column(
                Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(DashboardSpacing.md),
            ) {
                Box(Modifier.size(72.dp).background(DashboardColors.SurfacePink, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.Headphones, null, tint = DashboardColors.PrimaryPinkDark, modifier = Modifier.size(36.dp))
                }
                Text(scenario.title, style = DashboardTypography.PageTitle, color = DashboardColors.TextPrimary)
                Text(
                    "Listen for the main idea before checking the transcript.",
                    style = DashboardTypography.Body,
                    color = DashboardColors.TextSecondary,
                )
                FullPlayer(state, viewModel)
                state.errorMessage?.let { Text(it, color = DashboardColors.PrimaryPinkDark, style = DashboardTypography.Body) }
            }
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.md),
        ) {
            FluentButton(
                text = "Continue to Questions",
                onClick = { viewModel.selectSessionSection(ListeningSessionSection.QUESTIONS) },
                modifier = Modifier.weight(1f),
                variant = ButtonVariant.Primary,
                enabled = scenario.comprehensionQuestions.isNotEmpty(),
            )
            FluentButton(
                text = "View Transcript",
                onClick = { viewModel.selectSessionSection(ListeningSessionSection.TRANSCRIPT) },
                modifier = Modifier.weight(1f),
                variant = ButtonVariant.Secondary,
            )
        }
        FluentButton(
            text = "Practice this recording: Dictation",
            onClick = viewModel::startDictation,
            variant = ButtonVariant.Tonal,
        )
    }
}

@Composable
private fun FullPlayer(state: ListeningUiState, viewModel: ListeningViewModel) {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Slider(
            value = state.currentPositionMs.toFloat().coerceAtMost(state.durationMs.toFloat()),
            onValueChange = { viewModel.seekTo(it.toLong()) },
            valueRange = 0f..state.durationMs.coerceAtLeast(1L).toFloat(),
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(formatListeningDuration(state.currentPositionMs), style = DashboardTypography.Caption, color = DashboardColors.TextSecondary)
            Text(formatListeningDuration(state.durationMs), style = DashboardTypography.Caption, color = DashboardColors.TextSecondary)
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { viewModel.seekBy(-10_000L) }) { Icon(Icons.Rounded.Replay10, "Rewind 10 seconds") }
            IconButton(
                onClick = viewModel::togglePlayPause,
                modifier = Modifier.size(64.dp).background(DashboardColors.PrimaryPink, CircleShape),
            ) {
                Icon(
                    if (state.isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                    if (state.isPlaying) "Pause" else "Play",
                    tint = Color.White,
                    modifier = Modifier.size(34.dp),
                )
            }
            IconButton(onClick = { viewModel.seekBy(10_000L) }) { Icon(Icons.Rounded.Forward10, "Forward 10 seconds") }
        }
    }
}

@Composable
private fun CompactPlayer(state: ListeningUiState, viewModel: ListeningViewModel) {
    Surface(color = Color.White, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            IconButton(onClick = viewModel::togglePlayPause) {
                Icon(if (state.isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, if (state.isPlaying) "Pause" else "Play")
            }
            Slider(
                value = state.currentPositionMs.toFloat().coerceAtMost(state.durationMs.toFloat()),
                onValueChange = { viewModel.seekTo(it.toLong()) },
                valueRange = 0f..state.durationMs.coerceAtLeast(1L).toFloat(),
                modifier = Modifier.weight(1f),
            )
            Text(
                "${formatListeningDuration(state.currentPositionMs)} / ${formatListeningDuration(state.durationMs)}",
                style = DashboardTypography.Caption,
                color = DashboardColors.TextSecondary,
            )
        }
    }
}

@Composable
private fun QuestionsPane(state: ListeningUiState, viewModel: ListeningViewModel) {
    val scenario = state.selectedScenario ?: return
    val question = state.currentQuestion
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(DashboardSpacing.md)) {
        CompactPlayer(state, viewModel)
        if (question == null) {
            MessagePane("This recording has no comprehension questions.")
            return@Column
        }
        Column(
            Modifier.fillMaxWidth().weight(1f).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(DashboardSpacing.md),
        ) {
            Text("Comprehension", style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
            Text(
                "Question ${state.currentQuestionIndex + 1} of ${scenario.comprehensionQuestions.size}",
                style = DashboardTypography.Caption,
                color = DashboardColors.PrimaryPinkDark,
            )
            QuestionSurface(
                question = question,
                selectedOption = state.userAnswers[question.id],
                submitted = question.id in state.submittedQuestions,
                onSelect = { viewModel.selectAnswer(question.id, it) },
                onSubmit = { viewModel.submitAnswer(question) },
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.sm)) {
                FluentButton(
                    text = "Replay Audio",
                    onClick = viewModel::replayAudio,
                    variant = ButtonVariant.Secondary,
                    modifier = Modifier.weight(1f),
                )
                FluentButton(
                    text = "View Transcript",
                    onClick = { viewModel.selectSessionSection(ListeningSessionSection.TRANSCRIPT) },
                    variant = ButtonVariant.Tonal,
                    modifier = Modifier.weight(1f),
                )
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                FluentButton(
                    text = "Previous",
                    onClick = viewModel::showPreviousQuestion,
                    enabled = state.currentQuestionIndex > 0,
                    variant = ButtonVariant.Tonal,
                )
                if (state.currentQuestionIndex < scenario.comprehensionQuestions.lastIndex) {
                    FluentButton(text = "Next", onClick = viewModel::showNextQuestion, variant = ButtonVariant.Primary)
                } else {
                    FluentButton(
                        text = "Continue to Review",
                        onClick = { viewModel.selectSessionSection(ListeningSessionSection.REVIEW) },
                        variant = ButtonVariant.Primary,
                    )
                }
            }
        }
    }
}

@Composable
private fun QuestionSurface(
    question: ListeningComprehensionQuestion,
    selectedOption: String?,
    submitted: Boolean,
    onSelect: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(question.questionEn, style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
        question.options.forEach { option ->
            val selected = option == selectedOption
            Surface(
                color = if (selected) DashboardColors.SurfacePink else Color.White,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
                    .border(1.dp, if (selected) DashboardColors.PrimaryPink else DashboardColors.BorderSoft, RoundedCornerShape(14.dp))
                    .clickable(enabled = !submitted) { onSelect(option) },
            ) {
                Text(option, Modifier.padding(16.dp), style = DashboardTypography.Body, color = DashboardColors.TextPrimary)
            }
        }
        if (!submitted) {
            FluentButton(text = "Check Answer", onClick = onSubmit, enabled = selectedOption != null)
        } else {
            val correct = selectedOption?.trim() == question.correctAnswer.trim()
            Surface(color = if (correct) Color(0xFFE8F5EE) else Color(0xFFFFF0F4), shape = RoundedCornerShape(14.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(if (correct) "Correct" else "Not quite", style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
                    if (!correct) {
                        Text("Your answer: ${selectedOption.orEmpty()}", style = DashboardTypography.Body, color = DashboardColors.TextSecondary)
                        Text("Correct answer: ${question.correctAnswer}", style = DashboardTypography.Body, color = DashboardColors.TextPrimary)
                    }
                    if (question.explanationEn.isNotBlank()) {
                        Text(question.explanationEn, style = DashboardTypography.Body, color = DashboardColors.TextSecondary)
                    }
                }
            }
        }
    }
}

@Composable
private fun TranscriptPane(state: ListeningUiState, viewModel: ListeningViewModel) {
    val scenario = state.selectedScenario ?: return
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(DashboardSpacing.md)) {
        CompactPlayer(state, viewModel)
        LazyColumn(
            Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            item {
                Text("Transcript", style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
                Text(
                    "Tap a timestamped segment to play from that point.",
                    style = DashboardTypography.Body,
                    color = DashboardColors.TextSecondary,
                )
                Spacer(Modifier.height(8.dp))
            }
            items(scenario.transcriptItems, key = { it.index }) { item ->
                TranscriptRow(
                    item = item,
                    active = item.index == state.activeSentenceIndex,
                    supportVisible = item.index in state.revealedSentenceTranslations,
                    onSeek = { viewModel.replaySentence(item) },
                    onToggleSupport = { viewModel.toggleSentenceTranslation(item.index) },
                )
            }
            if (scenario.keyVocabulary.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(16.dp))
                    Text("Key Vocabulary", style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
                    Text(
                        "Contextual support from this recording",
                        style = DashboardTypography.Body,
                        color = DashboardColors.TextSecondary,
                    )
                }
                items(scenario.keyVocabulary, key = { it.word }) { vocabulary ->
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text(vocabulary.word, Modifier.weight(0.35f), style = DashboardTypography.CardTitle, color = DashboardColors.PrimaryPinkDark)
                        Text(vocabulary.contextNoteTr, Modifier.weight(0.65f), style = DashboardTypography.Body, color = DashboardColors.TextSecondary)
                    }
                }
            }
        }
    }
}

@Composable
private fun TranscriptRow(
    item: TranscriptItem,
    active: Boolean,
    supportVisible: Boolean,
    onSeek: () -> Unit,
    onToggleSupport: () -> Unit,
) {
    Column(
        Modifier.fillMaxWidth()
            .background(if (active) DashboardColors.SurfacePink else Color.Transparent, RoundedCornerShape(10.dp))
            .clickable(onClick = onSeek)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.Top) {
            Text(formatListeningDuration(item.startMs), style = DashboardTypography.Caption, color = DashboardColors.PrimaryPinkDark)
            Text(item.textEn, Modifier.weight(1f), style = DashboardTypography.Body, color = DashboardColors.TextPrimary)
            Text(
                if (supportVisible) "Hide support" else "Show support",
                style = DashboardTypography.Caption,
                color = DashboardColors.TextSecondary,
                modifier = Modifier.clickable(onClick = onToggleSupport),
            )
        }
        if (supportVisible && item.textTr.isNotBlank()) {
            Text(
                item.textTr,
                modifier = Modifier.padding(start = 54.dp),
                style = DashboardTypography.Body,
                fontStyle = FontStyle.Italic,
                color = DashboardColors.TextSecondary,
            )
        }
    }
}

@Composable
private fun DictationPane(state: ListeningUiState, viewModel: ListeningViewModel) {
    val scenario = state.selectedScenario ?: return
    val item = state.currentDictationTranscript ?: return
    val validItems = scenario.transcriptItems.count { it.textEn.isNotBlank() && it.endMs > it.startMs }
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(DashboardSpacing.md),
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Dictation", style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
                Text("Chunk ${state.dictationIndex + 1} of $validItems", style = DashboardTypography.Caption, color = DashboardColors.TextSecondary)
            }
            FluentButton(text = "Exit Dictation", onClick = viewModel::stopDictation, variant = ButtonVariant.Tonal)
        }
        Surface(color = Color.White, shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(DashboardSpacing.md)) {
                Text("Listen carefully and type exactly what you hear.", style = DashboardTypography.Body, color = DashboardColors.TextSecondary)
                FluentButton(text = "Play / Replay", onClick = viewModel::replayDictationChunk, variant = ButtonVariant.Primary)
                OutlinedTextField(
                    value = state.dictationAnswer,
                    onValueChange = viewModel::updateDictationAnswer,
                    enabled = state.dictationResult == null,
                    label = { Text("What did you hear?") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                )
                state.dictationResult?.let { result ->
                    Text(if (result.isCorrect) "Correct" else "Not quite", style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
                    Text("Your text: ${state.dictationAnswer}", style = DashboardTypography.Body, color = DashboardColors.TextSecondary)
                    Text("Reference: ${item.textEn}", style = DashboardTypography.Body, color = DashboardColors.TextPrimary)
                    if (!result.isCorrect) {
                        Text(
                            "${result.matchedTokens} of ${result.totalTokens} words matched",
                            style = DashboardTypography.Caption,
                            color = DashboardColors.TextSecondary,
                        )
                    }
                }
                FluentButton(
                    text = if (state.dictationResult == null) "Check" else "Next",
                    onClick = if (state.dictationResult == null) viewModel::submitDictation else viewModel::nextDictationChunk,
                    enabled = state.dictationResult != null || state.dictationAnswer.isNotBlank(),
                )
            }
        }
    }
}

@Composable
private fun ReviewPane(state: ListeningUiState, viewModel: ListeningViewModel, onFinish: () -> Unit) {
    val scenario = state.selectedScenario ?: return
    val attempted = state.submittedQuestions.size
    val correct = scenario.comprehensionQuestions.count { question ->
        question.id in state.submittedQuestions && state.userAnswers[question.id]?.trim() == question.correctAnswer.trim()
    }
    val mistakes = scenario.comprehensionQuestions.filter { question ->
        question.id in state.submittedQuestions && state.userAnswers[question.id]?.trim() != question.correctAnswer.trim()
    }
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(DashboardSpacing.lg),
    ) {
        CompactPlayer(state, viewModel)
        Text("Listening complete", style = DashboardTypography.PageTitle, color = DashboardColors.TextPrimary)
        Text(scenario.title, style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
        Text("$correct of $attempted attempted questions correct", style = DashboardTypography.Body, color = DashboardColors.TextSecondary)
        if (mistakes.isNotEmpty()) {
            Text("Questions needing review", style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
            mistakes.forEach { question ->
                Surface(color = Color.White, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(question.questionEn, style = DashboardTypography.Body, color = DashboardColors.TextPrimary)
                        Text("Correct answer: ${question.correctAnswer}", style = DashboardTypography.Caption, color = DashboardColors.TextSecondary)
                    }
                }
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.md)) {
            FluentButton(
                text = "Review mistakes",
                onClick = { viewModel.selectSessionSection(ListeningSessionSection.QUESTIONS) },
                enabled = mistakes.isNotEmpty(),
                variant = ButtonVariant.Secondary,
                modifier = Modifier.weight(1f),
            )
            FluentButton(text = "Listen again", onClick = viewModel::restartSession, variant = ButtonVariant.Tonal, modifier = Modifier.weight(1f))
            FluentButton(text = "Finish", onClick = onFinish, variant = ButtonVariant.Primary, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun LibraryRecordingRow(scenario: ListeningScenario, onClick: () -> Unit) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().border(1.dp, DashboardColors.BorderSoft, RoundedCornerShape(16.dp)).clickable(onClick = onClick),
    ) {
        Row(
            Modifier.padding(DashboardSpacing.lg),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.md),
        ) {
            Box(Modifier.size(44.dp).background(DashboardColors.SurfacePink, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Rounded.Headphones, null, tint = DashboardColors.PrimaryPinkDark)
            }
            Column(Modifier.weight(1f)) {
                Text(scenario.title, style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
                Text(scenario.metadataLine(), style = DashboardTypography.Caption, color = DashboardColors.TextSecondary)
                if (scenario.scenarioContext.isNotBlank()) {
                    Text(
                        scenario.scenarioContext,
                        style = DashboardTypography.Body,
                        color = DashboardColors.TextSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Icon(Icons.AutoMirrored.Rounded.ArrowForward, "Open recording", tint = DashboardColors.PrimaryPinkDark)
        }
    }
}

@Composable
private fun ListeningPageHeader(title: String, subtitle: String, onBack: () -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back", tint = DashboardColors.PrimaryPinkDark)
        }
        DashboardPageHeader(title, subtitle, Modifier.weight(1f))
    }
}

@Composable
private fun LoadingPane() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = DashboardColors.PrimaryPink)
    }
}

@Composable
private fun MessagePane(message: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(message, style = DashboardTypography.Body, color = DashboardColors.TextSecondary)
    }
}
