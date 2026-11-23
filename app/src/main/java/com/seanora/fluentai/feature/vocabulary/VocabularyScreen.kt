package com.seanora.fluentai.feature.vocabulary

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.BookmarkAdd
import androidx.compose.material.icons.rounded.BookmarkAdded
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seanora.fluentai.core.audio.OnDeviceTtsSpeaker
import com.seanora.fluentai.core.audio.TtsAccent
import com.seanora.fluentai.core.audio.rememberOnDeviceTtsSpeaker
import com.seanora.fluentai.core.designsystem.components.BadgeSize
import com.seanora.fluentai.core.designsystem.components.LevelBadge
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.designsystem.dashboard.DashboardTypography
import com.seanora.fluentai.core.designsystem.kit.FluentBackButton
import com.seanora.fluentai.core.designsystem.kit.LearningModule
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MasteryStatus
import com.seanora.fluentai.core.model.VocabItem

private enum class VocabMasteryFilter(val label: String) {
    ALL("All"),
    NEW("New"),
    LEARNING("Learning"),
    MASTERED("Mastered"),
    DUE("Due"),
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VocabularyLibraryScreen(
    onNavigateBack: () -> Unit,
    onOpenWord: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VocabularyViewModel = hiltViewModel(),
    featureViewModel: VocabularyFeatureViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val featureState by featureViewModel.uiState.collectAsStateWithLifecycle()
    val tts = rememberOnDeviceTtsSpeaker()

    var topicMenu by remember { mutableStateOf(false) }
    var topicId by rememberSaveable { mutableStateOf<String?>(null) }
    var masteryFilter by rememberSaveable { mutableStateOf(VocabMasteryFilter.ALL) }
    var selectedPaneWordId by rememberSaveable { mutableStateOf<String?>(null) }

    val topicIds = remember(featureState.collectionWords) {
        featureState.collectionWords.mapTo(hashSetOf()) { it.id }
    }
    val visible = remember(
        state.items,
        topicId,
        topicIds,
        masteryFilter,
        featureState.masteryByContentId,
        featureState.dueReviewIds,
    ) {
        state.items.filter { word ->
            val topicOk = topicId == null || word.id in topicIds
            if (!topicOk) return@filter false
            val snapshot = featureState.masteryByContentId[word.id]
            when (masteryFilter) {
                VocabMasteryFilter.ALL -> true
                VocabMasteryFilter.NEW -> snapshot == null || snapshot.totalAttempts == 0
                VocabMasteryFilter.LEARNING -> snapshot != null && snapshot.status in setOf(MasteryStatus.LEARNING, MasteryStatus.PRACTICED)
                VocabMasteryFilter.MASTERED -> snapshot != null && snapshot.status in setOf(MasteryStatus.MASTERED, MasteryStatus.MAINTAINING)
                VocabMasteryFilter.DUE -> word.id in featureState.dueReviewIds
            }
        }
    }

    val activePaneWord = remember(visible, selectedPaneWordId) {
        visible.firstOrNull { it.id == selectedPaneWordId } ?: visible.firstOrNull()
    }

    BoxWithConstraints(
        modifier
            .fillMaxSize()
            .background(DashboardColors.BackgroundPrimary),
    ) {
        val twoPane = maxWidth >= 880.dp
        val horizontalPadding = if (maxWidth >= 600.dp) 24.dp else 16.dp

        Column(Modifier.fillMaxSize().padding(horizontal = horizontalPadding, vertical = 20.dp)) {
            // Top bar
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                FluentBackButton(onClick = onNavigateBack, label = "Vocabulary", accent = LearningModule.VOCABULARY.accent)
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("Vocabulary library", style = DashboardTypography.PageTitle, color = DashboardColors.TextPrimary)
                    Text(
                        "${visible.size} of ${state.items.size} words",
                        style = DashboardTypography.Caption.copy(fontFeatureSettings = "tnum"),
                        color = DashboardColors.TextSecondary,
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Search + Topic filter row
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                VocabularySearchField(state.searchQuery, viewModel::updateSearchQuery, Modifier.weight(1f))
                Box {
                    OutlinedButton(
                        onClick = { topicMenu = true },
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                    ) {
                        Text(
                            featureState.collections.firstOrNull { it.topicId == topicId }?.name ?: "All topics",
                            color = DashboardColors.TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Icon(Icons.Rounded.ExpandMore, null, tint = DashboardColors.TextSecondary, modifier = Modifier.size(18.dp))
                    }
                    DropdownMenu(topicMenu, { topicMenu = false }, containerColor = Color.White) {
                        DropdownMenuItem(
                            text = { Text("All topics", color = DashboardColors.TextPrimary) },
                            onClick = { topicId = null; featureViewModel.clearCollection(); topicMenu = false },
                        )
                        featureState.collections.forEach { collection ->
                            DropdownMenuItem(
                                text = { Text("${collection.name} (${collection.wordCount})", color = DashboardColors.TextPrimary) },
                                onClick = { topicId = collection.topicId; featureViewModel.selectCollection(collection); topicMenu = false },
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // CEFR & Mastery filters
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            ) {
                listOf<String?>(null, "A2", "B1", "B2", "C1", "C2").forEach { level ->
                    val selected = state.selectedLevel == level
                    FilterPill(
                        label = level ?: "All levels",
                        selected = selected,
                        accent = LearningModule.VOCABULARY.accent,
                        onClick = { viewModel.selectLevel(level) },
                    )
                }
                Spacer(Modifier.size(4.dp))
                VocabMasteryFilter.entries.forEach { filter ->
                    FilterPill(
                        label = filter.label,
                        selected = masteryFilter == filter,
                        accent = DashboardColors.PrimaryPinkDark,
                        onClick = { masteryFilter = filter },
                    )
                }
            }

            if (state.isLoading) {
                Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = LearningModule.VOCABULARY.accent)
                }
                return@Column
            }

            if (visible.isEmpty()) {
                Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, DashboardColors.BorderSoft),
                    ) {
                        Column(
                            Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text("No words match your filters", style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
                            Text("Clear your search or adjust the CEFR and mastery filters", style = DashboardTypography.Body, color = DashboardColors.TextSecondary)
                        }
                    }
                }
                return@Column
            }

            if (twoPane) {
                Row(
                    Modifier.weight(1f).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    // Left Master List
                    LazyColumn(
                        modifier = Modifier.weight(0.42f).fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 24.dp),
                    ) {
                        items(visible, key = { it.id }) { word ->
                            val isSelected = activePaneWord?.id == word.id
                            VocabularyLibraryRow(
                                word = word,
                                mastery = featureState.masteryByContentId[word.id],
                                isDue = word.id in featureState.dueReviewIds,
                                selected = isSelected,
                                onPronounce = { tts.speak(word.headword) },
                                onClick = {
                                    selectedPaneWordId = word.id
                                    featureViewModel.recordLibrarySelection(word.id)
                                },
                            )
                        }
                    }

                    // Right Live Detail Pane
                    Box(Modifier.weight(0.58f).fillMaxHeight()) {
                        activePaneWord?.let { word ->
                            VocabularyWordDetailCardLayout(
                                word = word,
                                mastery = featureState.masteryByContentId[word.id],
                                isScheduledForReview = word.id in featureState.scheduledReviewIds,
                                tts = tts,
                                onAddToReview = { featureViewModel.addWordToReview(word.id) },
                                onPracticeWord = { onOpenWord(word.id) },
                                practiceButtonLabel = "Open full view",
                                onComplete = null,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 24.dp),
                ) {
                    items(visible, key = { it.id }) { word ->
                        VocabularyLibraryRow(
                            word = word,
                            mastery = featureState.masteryByContentId[word.id],
                            isDue = word.id in featureState.dueReviewIds,
                            selected = false,
                            onPronounce = { tts.speak(word.headword) },
                            onClick = {
                                featureViewModel.recordLibrarySelection(word.id)
                                onOpenWord(word.id)
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterPill(
    label: String,
    selected: Boolean,
    accent: Color,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = if (selected) accent else Color.White,
        border = BorderStroke(1.dp, if (selected) accent else DashboardColors.BorderSoft),
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            color = if (selected) Color.White else DashboardColors.TextPrimary,
        )
    }
}

@Composable
private fun VocabularyLibraryRow(
    word: VocabItem,
    mastery: MasterySnapshot?,
    isDue: Boolean,
    selected: Boolean,
    onPronounce: () -> Unit,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = if (selected) DashboardColors.SurfacePink.copy(alpha = 0.6f) else Color.White,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) LearningModule.VOCABULARY.accent else DashboardColors.BorderSoft,
        ),
    ) {
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            MasteryDot(mastery = mastery, isDue = isDue)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        word.headword,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DashboardColors.TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    LevelBadge(levelString = word.cefrLevel, size = BadgeSize.Compact)
                }
                Text(
                    word.definitionEn,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = DashboardColors.TextSecondary,
                )
            }
            IconButton(onClick = onPronounce, modifier = Modifier.size(36.dp)) {
                Icon(
                    Icons.AutoMirrored.Rounded.VolumeUp,
                    contentDescription = "Pronounce ${word.headword}",
                    tint = DashboardColors.PrimaryPinkDark,
                    modifier = Modifier.size(18.dp),
                )
            }
            if (!selected) {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = null,
                    tint = DashboardColors.TextMuted,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
    }
}

@Composable
private fun MasteryDot(mastery: MasterySnapshot?, isDue: Boolean) {
    val color = when {
        isDue -> DashboardColors.Warning
        mastery?.status in setOf(MasteryStatus.MASTERED, MasteryStatus.MAINTAINING) -> DashboardColors.PlantGreen
        mastery != null && mastery.totalAttempts > 0 -> LearningModule.GRAMMAR.accent
        else -> DashboardColors.BorderStrong
    }
    Box(Modifier.size(10.dp).clip(CircleShape).background(color))
}

@Composable
fun VocabularyWordDetailScreen(
    wordId: String,
    onNavigateBack: () -> Unit,
    onPracticeWord: (String) -> Unit,
    onComplete: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    viewModel: VocabularyViewModel = hiltViewModel(),
    featureViewModel: VocabularyFeatureViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val featureState by featureViewModel.uiState.collectAsStateWithLifecycle()
    val tts = rememberOnDeviceTtsSpeaker()

    LaunchedEffect(wordId) {
        viewModel.selectItemById(wordId)
        featureViewModel.recordLibrarySelection(wordId)
    }

    val word = state.selectedItem?.takeIf { it.id == wordId }

    Surface(
        modifier.fillMaxSize(),
        color = DashboardColors.BackgroundPrimary,
        contentColor = DashboardColors.TextPrimary,
    ) {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val horizontalPadding = if (maxWidth >= 600.dp) 32.dp else 18.dp
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = horizontalPadding, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(Modifier.widthIn(max = 1040.dp).fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FluentBackButton(onClick = onNavigateBack, label = "Back", accent = LearningModule.VOCABULARY.accent)
                        Spacer(Modifier.weight(1f))
                        if (word != null && onComplete != null) {
                            OutlinedButton(onClick = onComplete, shape = RoundedCornerShape(12.dp)) {
                                Icon(Icons.Rounded.CheckCircle, null, tint = DashboardColors.PlantGreen, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.size(6.dp))
                                Text("Complete", color = DashboardColors.TextPrimary)
                            }
                        }
                    }

                    if (word == null) {
                        Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                            if (state.isLoading) {
                                CircularProgressIndicator(color = LearningModule.VOCABULARY.accent)
                            } else {
                                Text("This vocabulary item is not available.", color = DashboardColors.TextSecondary)
                            }
                        }
                        return@Column
                    }

                    VocabularyWordDetailCardLayout(
                        word = word,
                        mastery = featureState.masteryByContentId[word.id],
                        isScheduledForReview = word.id in featureState.scheduledReviewIds,
                        tts = tts,
                        onAddToReview = { featureViewModel.addWordToReview(word.id) },
                        onPracticeWord = { onPracticeWord(word.id) },
                        practiceButtonLabel = "Practice Word",
                        onComplete = null,
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun VocabularyWordDetailCardLayout(
    word: VocabItem,
    mastery: MasterySnapshot?,
    isScheduledForReview: Boolean,
    tts: OnDeviceTtsSpeaker,
    onAddToReview: () -> Unit,
    onPracticeWord: () -> Unit,
    practiceButtonLabel: String,
    onComplete: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    var showTurkishInline by rememberSaveable(word.id) { mutableStateOf(false) }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // 1. Hero Word Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            border = BorderStroke(1.dp, DashboardColors.BorderSoft),
        ) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    LevelBadge(levelString = word.cefrLevel, size = BadgeSize.Compact)
                    MetaChip(word.partOfSpeech.replace('_', ' '))
                    if (word.register.isNotBlank()) MetaChip(word.register.replace('_', ' '))
                    mastery?.let { snap ->
                        val statusColor = when (snap.status) {
                            MasteryStatus.MASTERED, MasteryStatus.MAINTAINING -> DashboardColors.PlantGreen
                            MasteryStatus.LEARNING, MasteryStatus.PRACTICED -> LearningModule.GRAMMAR.accent
                            else -> DashboardColors.TextSecondary
                        }
                        MetaChip(snap.status.name.lowercase().replaceFirstChar { it.uppercase() }, accent = statusColor)
                    }
                }

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            word.headword,
                            fontSize = 32.sp,
                            lineHeight = 38.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DashboardColors.TextPrimary,
                        )
                        if (word.phonetic.isNotBlank()) {
                            Text(word.phonetic, fontSize = 15.sp, color = DashboardColors.TextSecondary)
                        }
                    }
                    PronunciationPillButton(label = "US", onClick = { tts.speak(word.headword, TtsAccent.US) })
                    PronunciationPillButton(label = "UK", onClick = { tts.speak(word.headword, TtsAccent.UK) })
                }

                // English Definition right inside the hero for instant single-glance comprehension
                Text(
                    word.definitionEn,
                    fontSize = 18.sp,
                    lineHeight = 26.sp,
                    color = DashboardColors.TextPrimary,
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Button(
                        onClick = onPracticeWord,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LearningModule.VOCABULARY.accent),
                    ) {
                        Icon(Icons.Rounded.PlayArrow, null, Modifier.size(18.dp))
                        Spacer(Modifier.size(4.dp))
                        Text(practiceButtonLabel)
                    }
                    OutlinedButton(onClick = onAddToReview, shape = RoundedCornerShape(12.dp)) {
                        Icon(
                            if (isScheduledForReview) Icons.Rounded.BookmarkAdded else Icons.Rounded.BookmarkAdd,
                            null,
                            tint = if (isScheduledForReview) DashboardColors.PlantGreen else DashboardColors.PrimaryPinkDark,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(Modifier.size(8.dp))
                        Text(
                            if (isScheduledForReview) "In review" else "Add to review",
                            color = DashboardColors.TextPrimary,
                        )
                    }
                    if (onComplete != null) {
                        OutlinedButton(onClick = onComplete, shape = RoundedCornerShape(12.dp)) {
                            Text("Complete", color = DashboardColors.TextPrimary)
                        }
                    }
                }
            }
        }

        // 2. Inline Expandable Turkish Meaning & L1 Traps Card (English-first, reveal on demand)
        if (word.meaningTr.isNotBlank() || word.turkishTraps.isNotEmpty()) {
            Surface(
                onClick = { showTurkishInline = !showTurkishInline },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = if (showTurkishInline) DashboardColors.SurfacePink.copy(alpha = 0.55f) else Color.White,
                border = BorderStroke(1.dp, if (showTurkishInline) DashboardColors.PrimaryPinkLight else DashboardColors.BorderSoft),
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Translate, null, tint = DashboardColors.PrimaryPinkDark, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.size(12.dp))
                        Text(
                            if (showTurkishInline) "Turkish meaning and traps" else "Reveal Turkish help",
                            style = DashboardTypography.CardTitle,
                            color = DashboardColors.TextPrimary,
                            modifier = Modifier.weight(1f),
                        )
                        Icon(
                            if (showTurkishInline) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                            contentDescription = null,
                            tint = DashboardColors.PrimaryPinkDark,
                        )
                    }
                    AnimatedVisibility(visible = showTurkishInline) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            if (word.meaningTr.isNotBlank()) {
                                Text(
                                    word.meaningTr,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = DashboardColors.TextPrimary,
                                )
                            }
                            word.turkishTraps.forEach { trap ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.White,
                                    border = BorderStroke(1.dp, DashboardColors.BorderSoft),
                                ) {
                                    Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Icon(Icons.Rounded.WarningAmber, null, tint = DashboardColors.Warning, modifier = Modifier.size(16.dp))
                                            Text(
                                                trap.trapType.replace('_', ' ').replaceFirstChar { it.uppercase() },
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 13.sp,
                                                color = DashboardColors.TextPrimary,
                                            )
                                        }
                                        Text(trap.trapNoteTr, style = DashboardTypography.Body, color = DashboardColors.TextSecondary)
                                        trap.incorrectExample?.let { Text("✗  $it", fontSize = 13.sp, color = Color(0xFFB42318)) }
                                        trap.correctExample?.let { Text("✓  $it", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = DashboardColors.PlantGreen) }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Examples Card (with inflection-aware highlight + per-sentence TTS)
        if (word.examples.isNotEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(1.dp, DashboardColors.BorderSoft),
            ) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Examples in context", style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
                    word.examples.forEach { example ->
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                VocabularyExampleText(example.en, word.headword)
                                example.context?.takeIf { it.isNotBlank() }?.let {
                                    Text(it, style = DashboardTypography.Caption, color = DashboardColors.TextSecondary)
                                }
                            }
                            IconButton(
                                onClick = { tts.speak(example.en) },
                                modifier = Modifier.size(36.dp),
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Rounded.VolumeUp,
                                    contentDescription = "Listen to example",
                                    tint = DashboardColors.PrimaryPinkDark,
                                    modifier = Modifier.size(18.dp),
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. Collocations & Topics Card
        if (word.collocations.isNotEmpty() || word.topicTags.isNotEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(1.dp, DashboardColors.BorderSoft),
            ) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (word.collocations.isNotEmpty()) {
                        Text("Common collocations", style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            word.collocations.forEach { colloc ->
                                Surface(
                                    onClick = { tts.speak(colloc.text) },
                                    shape = RoundedCornerShape(10.dp),
                                    color = DashboardColors.BackgroundSecondary,
                                    border = BorderStroke(1.dp, DashboardColors.BorderSoft),
                                ) {
                                    Text(
                                        colloc.text,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = DashboardColors.TextPrimary,
                                    )
                                }
                            }
                        }
                    }
                    if (word.topicTags.isNotEmpty()) {
                        Text("Topics", style = DashboardTypography.Caption, color = DashboardColors.TextSecondary)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            word.topicTags.forEach { tag ->
                                MetaChip(tag.replace('-', ' ').replace('_', ' '))
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun MetaChip(text: String, accent: Color = DashboardColors.TextSecondary) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = accent.copy(alpha = 0.10f),
    ) {
        Text(
            text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = accent,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }
}

@Composable
private fun VocabularySearchField(value: String, onValueChange: (String) -> Unit, modifier: Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = { Text("Search headword, definition, or meaning…", color = DashboardColors.TextSecondary) },
        leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null, tint = DashboardColors.TextSecondary) },
        trailingIcon = if (value.isNotEmpty()) {
            {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(Icons.Rounded.Close, contentDescription = "Clear search", tint = DashboardColors.TextSecondary)
                }
            }
        } else null,
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = DashboardColors.TextPrimary,
            unfocusedTextColor = DashboardColors.TextPrimary,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = LearningModule.VOCABULARY.accent,
            unfocusedBorderColor = DashboardColors.BorderSoft,
        ),
    )
}

internal fun findHighlightRange(sentence: String, headword: String): IntRange? {
    val cleanHeadword = headword.trim()
    if (cleanHeadword.isBlank()) return null
    val exactMatch = Regex("(?i)(?<![\\p{L}])${Regex.escape(cleanHeadword)}(?![\\p{L}])").find(sentence)
    if (exactMatch != null) return exactMatch.range
    // Inflection-aware stem fallback (e.g. comply -> complied, complying, complies)
    if (' ' !in cleanHeadword && cleanHeadword.length >= 4) {
        val stem = when {
            cleanHeadword.endsWith("y", ignoreCase = true) -> cleanHeadword.dropLast(1)
            cleanHeadword.endsWith("e", ignoreCase = true) -> cleanHeadword.dropLast(1)
            else -> cleanHeadword
        }
        val stemMatch = Regex("(?i)(?<![\\p{L}])${Regex.escape(stem)}[\\p{L}]{0,5}(?![\\p{L}])").find(sentence)
        if (stemMatch != null) return stemMatch.range
    }
    return null
}

@Composable
internal fun VocabularyExampleText(example: String, headword: String) {
    val range = remember(example, headword) { findHighlightRange(example, headword) }
    if (range == null) {
        Text(example, fontSize = 16.sp, lineHeight = 24.sp, color = DashboardColors.TextPrimary)
        return
    }
    Text(
        buildAnnotatedString {
            append(example.substring(0, range.first))
            withStyle(
                SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = DashboardColors.PrimaryPinkDark,
                    background = DashboardColors.SurfacePink,
                ),
            ) {
                append(example.substring(range.first, range.last + 1))
            }
            append(example.substring(range.last + 1))
        },
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = DashboardColors.TextPrimary,
    )
}

@Composable
fun VocabularyDailyWordDetailScreen(
    onNavigateBack: () -> Unit,
    onPracticeWord: (String) -> Unit,
    modifier: Modifier = Modifier,
    featureViewModel: VocabularyFeatureViewModel = hiltViewModel(),
) {
    val state by featureViewModel.uiState.collectAsStateWithLifecycle()
    val word = state.dailyWord
    if (word != null) {
        VocabularyWordDetailScreen(
            wordId = word.id,
            onNavigateBack = onNavigateBack,
            onPracticeWord = onPracticeWord,
            modifier = modifier,
        )
    } else {
        Box(
            modifier.fillMaxSize().background(DashboardColors.BackgroundPrimary),
            contentAlignment = Alignment.Center,
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(color = LearningModule.VOCABULARY.accent)
            } else {
                Text("Word of Day is not available.", color = DashboardColors.TextPrimary)
            }
        }
    }
}
