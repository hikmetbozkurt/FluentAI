package com.seanora.fluentai.feature.chunks

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seanora.fluentai.core.audio.TtsAccent
import com.seanora.fluentai.core.audio.rememberOnDeviceTtsSpeaker
import com.seanora.fluentai.core.designsystem.components.BadgeSize
import com.seanora.fluentai.core.designsystem.components.LevelBadge
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.designsystem.dashboard.DashboardTypography
import com.seanora.fluentai.core.designsystem.kit.FluentActionTile
import com.seanora.fluentai.core.designsystem.kit.FluentAnswerVerdict
import com.seanora.fluentai.core.designsystem.kit.FluentBackButton
import com.seanora.fluentai.core.designsystem.kit.FluentPageHeader
import com.seanora.fluentai.core.designsystem.kit.FluentSectionTitle
import com.seanora.fluentai.core.designsystem.kit.FluentSurfaceCard
import com.seanora.fluentai.core.designsystem.kit.LearningModule
import com.seanora.fluentai.core.designsystem.kit.SentenceBlueprintCard
import com.seanora.fluentai.core.model.ChunkCategory
import com.seanora.fluentai.core.model.ChunkDrillMode
import com.seanora.fluentai.core.model.ChunkDrillQuestion
import com.seanora.fluentai.core.model.LexicalChunk

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChunksDashboardScreen(
    onNavigateBack: () -> Unit,
    onOpenChunk: (String) -> Unit,
    onStartDrill: (ChunkDrillMode) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChunksViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    BoxWithConstraints(
        modifier
            .fillMaxSize()
            .background(DashboardColors.BackgroundPrimary),
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = LearningModule.CHUNKS.accent,
            )
            return@BoxWithConstraints
        }
        val wide = maxWidth >= 840.dp
        val horizontalPadding = if (maxWidth >= 600.dp) 32.dp else 16.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = horizontalPadding, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.widthIn(max = 1160.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                FluentPageHeader(
                    title = "Chunks and patterns",
                    module = LearningModule.CHUNKS,
                    eyebrow = "Learn · Chunks",
                    subtitle = "Ready-made sentence starters, collocations, and SVO blueprints built for Turkish speakers",
                    onBack = onNavigateBack,
                    backLabel = "Learn",
                )

                // Pure typographic stats strip
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ChunkStatTile("Total chunks", "${state.allChunks.size}", Modifier.weight(1f))
                    ChunkStatTile("Daily pack", "${state.dailyPack.size}", Modifier.weight(1f))
                    ChunkStatTile("Categories", "${ChunkCategory.entries.size}", Modifier.weight(1f))
                }

                // Rapid drill CTA
                FluentActionTile(
                    title = "Rapid chunk drill",
                    detail = "Mix Turkish intent matching, trap busters, and SVO blueprint assembly",
                    module = LearningModule.CHUNKS,
                    icon = Icons.Rounded.PlayArrow,
                    actionLabel = "Start drill",
                    emphasized = true,
                    onClick = { onStartDrill(ChunkDrillMode.MIXED) },
                    modifier = Modifier.fillMaxWidth(),
                )

                // Daily pack
                if (state.dailyPack.isNotEmpty()) {
                    FluentSectionTitle(
                        title = "Today's chunk pack",
                        subtitle = "Three high-frequency patterns selected for active recall",
                        accent = LearningModule.CHUNKS.accent,
                    )
                    if (wide) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            state.dailyPack.forEach { chunk ->
                                ChunkCard(chunk = chunk, onClick = { onOpenChunk(chunk.id) }, modifier = Modifier.weight(1f))
                            }
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            state.dailyPack.forEach { chunk ->
                                ChunkCard(chunk = chunk, onClick = { onOpenChunk(chunk.id) }, modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }
                }

                // Browse all chunks
                FluentSectionTitle(
                    title = "Pattern library",
                    subtitle = "${state.filteredChunks.size} of ${state.allChunks.size} patterns",
                    accent = LearningModule.CHUNKS.accent,
                )

                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = viewModel::updateSearchQuery,
                    placeholder = { Text("Search English frame or Turkish intent", color = DashboardColors.TextSecondary) },
                    leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null, tint = DashboardColors.TextSecondary) },
                    trailingIcon = if (state.searchQuery.isNotEmpty()) {
                        {
                            IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                Icon(Icons.Rounded.Close, contentDescription = "Clear search", tint = DashboardColors.TextSecondary)
                            }
                        }
                    } else null,
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = DashboardColors.TextPrimary,
                        unfocusedTextColor = DashboardColors.TextPrimary,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = LearningModule.CHUNKS.accent,
                        unfocusedBorderColor = DashboardColors.BorderSoft,
                    ),
                )

                // CEFR & category filters
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    listOf<String?>(null, "A2", "B1", "B2").forEach { level ->
                        ChunkFilterChip(
                            label = level ?: "All levels",
                            selected = state.selectedLevel == level,
                            onClick = { viewModel.selectLevel(level) },
                        )
                    }
                    ChunkCategory.entries.forEach { category ->
                        ChunkFilterChip(
                            label = category.displayTitle,
                            selected = state.selectedCategory == category,
                            onClick = {
                                viewModel.selectCategory(if (state.selectedCategory == category) null else category)
                            },
                        )
                    }
                }

                val filtered = state.filteredChunks
                if (filtered.isEmpty()) {
                    FluentSurfaceCard(modifier = Modifier.fillMaxWidth()) {
                        Text("No chunks match your filters", style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
                        Text("Clear your search or switch CEFR and category filters", style = DashboardTypography.Body, color = DashboardColors.TextSecondary)
                    }
                } else {
                    val columns = if (wide) 2 else 1
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        filtered.chunked(columns).forEach { rowChunks ->
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                rowChunks.forEach { chunk ->
                                    ChunkCard(chunk = chunk, onClick = { onOpenChunk(chunk.id) }, modifier = Modifier.weight(1f))
                                }
                                repeat(columns - rowChunks.size) { Spacer(Modifier.weight(1f)) }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChunkStatTile(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = DashboardColors.Surface,
        border = BorderStroke(1.dp, DashboardColors.BorderSoft),
    ) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(label, style = DashboardTypography.Caption, color = DashboardColors.TextSecondary)
            Text(
                value,
                style = DashboardTypography.PageTitle.copy(fontSize = 24.sp, fontFeatureSettings = "tnum"),
                color = DashboardColors.TextPrimary,
            )
        }
    }
}

@Composable
private fun ChunkFilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = if (selected) LearningModule.CHUNKS.accent else Color.White,
        border = BorderStroke(1.dp, if (selected) LearningModule.CHUNKS.accent else DashboardColors.BorderSoft),
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
private fun ChunkCard(chunk: LexicalChunk, onClick: () -> Unit, modifier: Modifier = Modifier) {
    FluentSurfaceCard(
        modifier = modifier,
        onClick = onClick,
        contentPadding = PaddingValues(16.dp),
        contentSpacing = 8.dp,
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                LevelBadge(levelString = chunk.cefrLevel, size = BadgeSize.Compact)
                Text(chunk.category.displayTitle, style = DashboardTypography.Caption, color = DashboardColors.TextSecondary)
            }
            Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null, tint = DashboardColors.TextMuted, modifier = Modifier.size(16.dp))
        }
        Text(
            chunk.chunkEn,
            style = DashboardTypography.CardTitle,
            color = DashboardColors.TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            chunk.intentTr,
            style = DashboardTypography.Body,
            color = DashboardColors.TextSecondary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun ChunkDetailScreen(
    chunkId: String,
    onNavigateBack: () -> Unit,
    onPracticeChunk: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChunksViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val tts = rememberOnDeviceTtsSpeaker()
    val chunk = state.allChunks.firstOrNull { it.id == chunkId }

    BoxWithConstraints(
        modifier
            .fillMaxSize()
            .background(DashboardColors.BackgroundPrimary),
    ) {
        val horizontalPadding = if (maxWidth >= 600.dp) 32.dp else 16.dp
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = horizontalPadding, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.widthIn(max = 960.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    FluentBackButton(onClick = onNavigateBack, label = "Chunks", accent = LearningModule.CHUNKS.accent)
                    Spacer(Modifier.weight(1f))
                    if (chunk != null) {
                        Button(
                            onClick = { onPracticeChunk(chunk.id) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = LearningModule.CHUNKS.accent),
                        ) {
                            Icon(Icons.Rounded.PlayArrow, null, Modifier.size(18.dp))
                            Spacer(Modifier.size(4.dp))
                            Text("Practice chunk")
                        }
                    }
                }

                if (chunk == null) {
                    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        if (state.isLoading) {
                            CircularProgressIndicator(color = LearningModule.CHUNKS.accent)
                        } else {
                            Text("This chunk is not available", color = DashboardColors.TextSecondary)
                        }
                    }
                    return@Column
                }

                // Hero pattern card
                FluentSurfaceCard(
                    accent = LearningModule.CHUNKS.accent,
                    contentPadding = PaddingValues(20.dp),
                    contentSpacing = 12.dp,
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        LevelBadge(levelString = chunk.cefrLevel, size = BadgeSize.Compact)
                        Text(chunk.category.displayTitle, style = DashboardTypography.Caption, color = DashboardColors.TextSecondary)
                    }
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            chunk.chunkEn,
                            style = DashboardTypography.Display,
                            color = DashboardColors.TextPrimary,
                            modifier = Modifier.weight(1f),
                        )
                        IconButton(onClick = { tts.speak(chunk.chunkEn, TtsAccent.US) }) {
                            Icon(Icons.AutoMirrored.Rounded.VolumeUp, contentDescription = "Listen to chunk", tint = LearningModule.CHUNKS.accent)
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = DashboardColors.BackgroundPrimary,
                        border = BorderStroke(1.dp, DashboardColors.BorderSoft),
                    ) {
                        Text(
                            chunk.patternTemplate,
                            style = DashboardTypography.Body.copy(fontWeight = FontWeight.Medium),
                            color = DashboardColors.PrimaryPinkDark,
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                        )
                    }
                    Text(chunk.intentTr, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = DashboardColors.TextPrimary)
                    Text(chunk.meaningEn, style = DashboardTypography.Body, color = DashboardColors.TextSecondary)
                }

                // Turkish L1 Trap comparison card
                FluentSurfaceCard(
                    contentPadding = PaddingValues(20.dp),
                    contentSpacing = 12.dp,
                ) {
                    Text("Turkish transfer trap", style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFFDECEC),
                            border = BorderStroke(1.dp, Color(0xFFF5C2C0)),
                        ) {
                            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Avoid", style = DashboardTypography.Caption, color = Color(0xFFB42318))
                                Text(chunk.turkishTrap.incorrectEn, style = DashboardTypography.Body, color = DashboardColors.TextPrimary)
                            }
                        }
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFE6F6EC),
                            border = BorderStroke(1.dp, DashboardColors.PlantGreen.copy(alpha = 0.4f)),
                        ) {
                            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Natural English", style = DashboardTypography.Caption, color = DashboardColors.PlantGreen)
                                Text(
                                    chunk.turkishTrap.correctEn,
                                    style = DashboardTypography.Body.copy(fontWeight = FontWeight.Medium),
                                    color = DashboardColors.TextPrimary,
                                )
                            }
                        }
                    }
                    Text(chunk.turkishTrap.explanationTr, style = DashboardTypography.Body, color = DashboardColors.TextSecondary)
                }

                // Shared Turkish SOV vs. English SVO Blueprint Card
                SentenceBlueprintCard(
                    slots = chunk.blueprintSlots,
                    turkishSummary = chunk.slotVariations.firstOrNull()?.fullSentenceTr,
                )

                // Slot-swap variations
                if (chunk.slotVariations.isNotEmpty()) {
                    FluentSurfaceCard(
                        contentPadding = PaddingValues(20.dp),
                        contentSpacing = 12.dp,
                    ) {
                        Text("Slot-swap variations", style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
                        chunk.slotVariations.forEach { variation ->
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        variation.fullSentenceEn,
                                        style = DashboardTypography.Body.copy(fontWeight = FontWeight.Medium),
                                        color = DashboardColors.TextPrimary,
                                    )
                                    Text(variation.fullSentenceTr, style = DashboardTypography.Caption, color = DashboardColors.TextSecondary)
                                }
                                IconButton(onClick = { tts.speak(variation.fullSentenceEn, TtsAccent.US) }, modifier = Modifier.size(36.dp)) {
                                    Icon(Icons.AutoMirrored.Rounded.VolumeUp, contentDescription = "Listen", tint = LearningModule.CHUNKS.accent, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChunkPracticeSessionScreen(
    mode: ChunkDrillMode,
    focusChunkId: String?,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChunksViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.isLoading, mode, focusChunkId) {
        if (!state.isLoading && state.questions.isEmpty() && !state.isSessionComplete) {
            viewModel.startDrill(mode = mode, focusChunkId = focusChunkId)
        }
    }

    Column(
        modifier
            .fillMaxSize()
            .background(DashboardColors.BackgroundPrimary)
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            Modifier.widthIn(max = 760.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            FluentBackButton(onClick = onNavigateBack, label = "Chunks", accent = LearningModule.CHUNKS.accent)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(state.drillMode.title, style = DashboardTypography.PageTitle, color = DashboardColors.TextPrimary)
                Text("Offline pattern recall and SVO assembly", style = DashboardTypography.Caption, color = DashboardColors.TextSecondary)
            }
        }

        val question = state.currentQuestion
        when {
            state.isLoading -> Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = LearningModule.CHUNKS.accent)
            }
            state.isSessionComplete || question == null -> {
                val total = state.questions.size.coerceAtLeast(1)
                val accuracy = (state.correctCount * 100) / total
                FluentSurfaceCard(
                    modifier = Modifier.widthIn(max = 600.dp).fillMaxWidth(),
                    contentPadding = PaddingValues(24.dp),
                    contentSpacing = 16.dp,
                ) {
                    Text("Drill complete", style = DashboardTypography.PageTitle, color = DashboardColors.TextPrimary)
                    Text(
                        "$accuracy% accuracy · ${state.correctCount} / $total correct",
                        style = DashboardTypography.Body.copy(fontFeatureSettings = "tnum"),
                        color = DashboardColors.TextSecondary,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(
                            onClick = { viewModel.startDrill(mode = state.drillMode, focusChunkId = focusChunkId) },
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            Text("Practice again", color = DashboardColors.TextPrimary)
                        }
                        Button(
                            onClick = onNavigateBack,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = LearningModule.CHUNKS.accent),
                        ) {
                            Text("Done")
                        }
                    }
                }
            }
            else -> {
                val total = state.questions.size
                val progress = (state.questionIndex + 1).toFloat() / total.coerceAtLeast(1)
                Column(
                    modifier = Modifier
                        .widthIn(max = 760.dp)
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            "Item ${state.questionIndex + 1} of $total",
                            style = DashboardTypography.Caption.copy(fontFeatureSettings = "tnum"),
                            color = DashboardColors.TextSecondary,
                        )
                        LevelBadge(levelString = question.cefrLevel, size = BadgeSize.Compact)
                    }
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                        color = LearningModule.CHUNKS.accent,
                        trackColor = DashboardColors.ProgressTrack,
                    )

                    when (question) {
                        is ChunkDrillQuestion.Choice -> {
                            FluentSurfaceCard(contentPadding = PaddingValues(20.dp), contentSpacing = 8.dp) {
                                Text(question.promptSubtitle, style = DashboardTypography.Caption, color = LearningModule.CHUNKS.accent)
                                Text(question.promptTitle, style = DashboardTypography.SectionTitle, color = DashboardColors.TextPrimary)
                            }
                            question.options.forEach { option ->
                                val isSelected = state.selectedChoice == option
                                val isCorrectOpt = state.submitted && option == question.correctAnswer
                                val isWrongSel = state.submitted && isSelected && option != question.correctAnswer
                                val bg = when {
                                    isCorrectOpt -> Color(0xFFE6F6EC)
                                    isWrongSel -> Color(0xFFFDECEC)
                                    isSelected -> DashboardColors.SurfacePink.copy(alpha = 0.65f)
                                    else -> Color.White
                                }
                                val border = when {
                                    isCorrectOpt -> DashboardColors.PlantGreen
                                    isWrongSel -> Color(0xFFD92D20)
                                    isSelected -> LearningModule.CHUNKS.accent
                                    else -> DashboardColors.BorderSoft
                                }
                                Surface(
                                    onClick = { viewModel.selectChoice(option) },
                                    enabled = !state.submitted,
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    color = bg,
                                    border = BorderStroke(1.dp, border),
                                ) {
                                    Row(
                                        Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    ) {
                                        Text(option, modifier = Modifier.weight(1f), fontSize = 15.sp, color = DashboardColors.TextPrimary)
                                        if (isCorrectOpt) Icon(Icons.Rounded.CheckCircle, "Correct", tint = DashboardColors.PlantGreen)
                                        if (isWrongSel) Icon(Icons.Rounded.Cancel, "Incorrect", tint = Color(0xFFD92D20))
                                    }
                                }
                            }
                        }

                        is ChunkDrillQuestion.BlueprintAssembly -> {
                            FluentSurfaceCard(contentPadding = PaddingValues(20.dp), contentSpacing = 8.dp) {
                                Text("Assemble the English SVO blueprint", style = DashboardTypography.Caption, color = LearningModule.CHUNKS.accent)
                                Text(question.promptTr, style = DashboardTypography.SectionTitle, color = DashboardColors.TextPrimary)
                            }
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = DashboardColors.SurfacePink.copy(alpha = 0.35f),
                                border = BorderStroke(1.dp, DashboardColors.BorderSoft),
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Box(Modifier.padding(16.dp)) {
                                    if (state.selectedSlotIndices.isEmpty()) {
                                        Text("Tap the English chunks below in order", style = DashboardTypography.Body, color = DashboardColors.TextSecondary)
                                    } else {
                                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                            state.selectedSlotIndices.forEachIndexed { pos, slotIdx ->
                                                val slot = question.scrambledSlots.getOrNull(slotIdx) ?: return@forEachIndexed
                                                Surface(
                                                    modifier = Modifier.clickable(enabled = !state.submitted) { viewModel.removeBlueprintSlotAt(pos) },
                                                    shape = RoundedCornerShape(10.dp),
                                                    color = DashboardColors.Surface,
                                                    border = BorderStroke(1.dp, LearningModule.CHUNKS.accent),
                                                ) {
                                                    Text(
                                                        slot.textEn,
                                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                                        fontWeight = FontWeight.Medium,
                                                        color = DashboardColors.TextPrimary,
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                question.scrambledSlots.forEachIndexed { idx, slot ->
                                    val used = idx in state.selectedSlotIndices
                                    OutlinedButton(
                                        onClick = { viewModel.addBlueprintSlot(idx) },
                                        enabled = !used && !state.submitted,
                                        shape = RoundedCornerShape(10.dp),
                                    ) {
                                        Text(slot.textEn, color = if (used) DashboardColors.TextMuted else DashboardColors.TextPrimary)
                                    }
                                }
                            }
                            if (!state.submitted && state.selectedSlotIndices.isNotEmpty()) {
                                OutlinedButton(onClick = viewModel::clearBlueprintSlots, shape = RoundedCornerShape(12.dp)) {
                                    Text("Clear", color = DashboardColors.TextPrimary)
                                }
                            }
                        }
                    }

                    if (state.submitted) {
                        FluentAnswerVerdict(correct = state.lastCorrect == true)
                        FluentSurfaceCard(contentPadding = PaddingValues(16.dp), contentSpacing = 6.dp) {
                            if (state.lastCorrect != true && question is ChunkDrillQuestion.BlueprintAssembly) {
                                Text(
                                    "Natural order: ${question.correctSlots.joinToString(" ") { it.textEn }}",
                                    fontWeight = FontWeight.Medium,
                                    color = DashboardColors.TextPrimary,
                                )
                            }
                            Text(question.explanationTr, style = DashboardTypography.Body, color = DashboardColors.TextSecondary)
                        }
                        Button(
                            onClick = viewModel::nextQuestion,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = LearningModule.CHUNKS.accent),
                            modifier = Modifier.align(Alignment.End),
                        ) {
                            Text(if (state.questionIndex == total - 1) "See results" else "Next")
                        }
                    } else {
                        val canSubmit = when (question) {
                            is ChunkDrillQuestion.Choice -> state.selectedChoice != null
                            is ChunkDrillQuestion.BlueprintAssembly -> state.selectedSlotIndices.size == question.correctSlots.size
                        }
                        Button(
                            onClick = viewModel::submitAnswer,
                            enabled = canSubmit,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = LearningModule.CHUNKS.accent),
                            modifier = Modifier.align(Alignment.End),
                        ) {
                            Text("Check answer")
                        }
                    }
                }
            }
        }
    }
}
