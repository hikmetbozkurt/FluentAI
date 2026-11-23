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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.BookmarkAdd
import androidx.compose.material.icons.rounded.BookmarkAdded
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material.icons.rounded.Style
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.seanora.fluentai.core.designsystem.kit.FluentPageHeader
import com.seanora.fluentai.core.designsystem.kit.FluentSectionTitle
import com.seanora.fluentai.core.designsystem.kit.FluentSurfaceCard
import com.seanora.fluentai.core.designsystem.kit.LearningModule
import com.seanora.fluentai.core.model.VocabItem
import com.seanora.fluentai.core.model.VocabularyFeatureState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VocabularyDashboardScreen(
    onNavigateBack: () -> Unit,
    onContinue: (VocabularyFeatureState) -> Unit,
    onOpenWord: (String) -> Unit,
    onSmartPractice: () -> Unit,
    onOpenLibrary: () -> Unit,
    onOpenCollections: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VocabularyFeatureViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val tts = rememberOnDeviceTtsSpeaker()

    BoxWithConstraints(
        modifier
            .fillMaxSize()
            .background(DashboardColors.BackgroundPrimary),
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = LearningModule.VOCABULARY.accent,
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
                    title = "Vocabulary",
                    module = LearningModule.VOCABULARY,
                    eyebrow = "Learn · Vocabulary",
                    subtitle = "Spaced repetition, context examples, and pronunciation",
                    onBack = onNavigateBack,
                    backLabel = "Learn",
                )

                // Stats strip
                VocabularyStatsStrip(
                    totalWords = state.allItems.size,
                    masteredCount = state.masteredCount,
                    learningCount = state.learningCount,
                    dueCount = state.dueCount,
                    wide = wide,
                )

                // Continue card if available
                state.featureState.resumeDestination?.let {
                    FluentActionTile(
                        title = "Continue learning",
                        detail = state.featureState.resumeContextLabel(),
                        module = LearningModule.VOCABULARY,
                        icon = Icons.Rounded.PlayArrow,
                        actionLabel = "Resume",
                        emphasized = true,
                        onClick = { onContinue(state.featureState) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                // Word of the Day + Smart Practice
                if (wide) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Top,
                    ) {
                        state.dailyWord?.let { word ->
                            WordOfTheDayHeroCard(
                                word = word,
                                isTurkishRevealed = state.isDailyTurkishRevealed,
                                isScheduledForReview = word.id in state.scheduledReviewIds,
                                onToggleTurkish = viewModel::toggleDailyTurkish,
                                onAddToReview = viewModel::addDailyWordToReview,
                                onPronounce = { accent -> tts.speak(word.headword, accent) },
                                onOpenWord = { onOpenWord(word.id) },
                                modifier = Modifier.weight(1.3f),
                            )
                        }
                        PracticeQuickColumn(
                            dueCount = state.dueCount,
                            flashCardCount = state.flashCards.size,
                            onSmartPractice = onSmartPractice,
                            modifier = Modifier.weight(1f),
                        )
                    }
                } else {
                    state.dailyWord?.let { word ->
                        WordOfTheDayHeroCard(
                            word = word,
                            isTurkishRevealed = state.isDailyTurkishRevealed,
                            isScheduledForReview = word.id in state.scheduledReviewIds,
                            onToggleTurkish = viewModel::toggleDailyTurkish,
                            onAddToReview = viewModel::addDailyWordToReview,
                            onPronounce = { accent -> tts.speak(word.headword, accent) },
                            onOpenWord = { onOpenWord(word.id) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                    PracticeQuickColumn(
                        dueCount = state.dueCount,
                        flashCardCount = state.flashCards.size,
                        onSmartPractice = onSmartPractice,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                FluentSectionTitle(
                    title = "Explore curriculum",
                    subtitle = "Browse the full dictionary or drill by topic",
                    accent = LearningModule.VOCABULARY.accent,
                )

                if (wide) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        FluentActionTile(
                            title = "Vocabulary library",
                            detail = "Search and filter by CEFR and mastery across ${state.allItems.size} words",
                            module = LearningModule.VOCABULARY,
                            icon = Icons.AutoMirrored.Rounded.MenuBook,
                            actionLabel = "Browse",
                            onClick = onOpenLibrary,
                            modifier = Modifier.weight(1f),
                        )
                        FluentActionTile(
                            title = "Collections",
                            detail = "Explore ${state.collections.size} thematic packs from A2 to C2",
                            module = LearningModule.VOCABULARY,
                            icon = Icons.Rounded.Category,
                            actionLabel = "Open",
                            onClick = onOpenCollections,
                            modifier = Modifier.weight(1f),
                        )
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        FluentActionTile(
                            title = "Vocabulary library",
                            detail = "Search and filter ${state.allItems.size} words",
                            module = LearningModule.VOCABULARY,
                            icon = Icons.AutoMirrored.Rounded.MenuBook,
                            onClick = onOpenLibrary,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        FluentActionTile(
                            title = "Collections",
                            detail = "Explore ${state.collections.size} thematic packs",
                            module = LearningModule.VOCABULARY,
                            icon = Icons.Rounded.Category,
                            onClick = onOpenCollections,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VocabularyStatsStrip(
    totalWords: Int,
    masteredCount: Int,
    learningCount: Int,
    dueCount: Int,
    wide: Boolean,
) {
    val stats = listOf(
        Triple("Total words", "$totalWords", DashboardColors.TextPrimary),
        Triple("Mastered", "$masteredCount", DashboardColors.PlantGreen),
        Triple("In progress", "$learningCount", DashboardColors.TextPrimary),
        Triple("Due review", "$dueCount", if (dueCount > 0) DashboardColors.Warning else DashboardColors.TextPrimary),
    )
    if (wide) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            stats.forEach { (label, value, valueColor) ->
                StatMiniCard(label, value, valueColor, Modifier.weight(1f))
            }
        }
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            stats.chunked(2).forEach { pair ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    pair.forEach { (label, value, valueColor) ->
                        StatMiniCard(label, value, valueColor, Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun StatMiniCard(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = DashboardColors.Surface,
        border = BorderStroke(1.dp, DashboardColors.BorderSoft),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(label, style = DashboardTypography.Caption, color = DashboardColors.TextSecondary)
            Text(
                value,
                style = DashboardTypography.PageTitle.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFeatureSettings = "tnum",
                ),
                color = valueColor,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WordOfTheDayHeroCard(
    word: VocabItem,
    isTurkishRevealed: Boolean,
    isScheduledForReview: Boolean,
    onToggleTurkish: () -> Unit,
    onAddToReview: () -> Unit,
    onPronounce: (TtsAccent) -> Unit,
    onOpenWord: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FluentSurfaceCard(
        modifier = modifier,
        accent = LearningModule.VOCABULARY.accent,
        contentPadding = PaddingValues(20.dp),
        contentSpacing = 12.dp,
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                "Word of the day",
                style = DashboardTypography.Eyebrow,
                color = LearningModule.VOCABULARY.accent,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                LevelBadge(levelString = word.cefrLevel, size = BadgeSize.Compact)
                Surface(shape = RoundedCornerShape(8.dp), color = DashboardColors.BackgroundSecondary) {
                    Text(
                        word.partOfSpeech.replace('_', ' '),
                        style = DashboardTypography.Caption,
                        color = DashboardColors.TextSecondary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    )
                }
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
                    fontSize = 28.sp,
                    lineHeight = 34.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DashboardColors.TextPrimary,
                )
                if (word.phonetic.isNotBlank()) {
                    Text(word.phonetic, fontSize = 14.sp, color = DashboardColors.TextSecondary)
                }
            }
            PronunciationPillButton(label = "US", onClick = { onPronounce(TtsAccent.US) })
            PronunciationPillButton(label = "UK", onClick = { onPronounce(TtsAccent.UK) })
        }

        Text(
            word.definitionEn,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            color = DashboardColors.TextPrimary,
        )

        word.examples.firstOrNull()?.let { ex ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = DashboardColors.BackgroundPrimary,
                border = BorderStroke(1.dp, DashboardColors.BorderSoft),
            ) {
                Text(
                    "“${ex.en}”",
                    style = DashboardTypography.Body,
                    color = DashboardColors.TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                )
            }
        }

        if (isTurkishRevealed && word.meaningTr.isNotBlank()) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = DashboardColors.SurfacePink.copy(alpha = 0.5f),
            ) {
                Text(
                    "TR · ${word.meaningTr}",
                    style = DashboardTypography.Body.copy(fontWeight = FontWeight.Medium),
                    color = DashboardColors.TextPrimary,
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                )
            }
        }

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(
                onClick = onOpenWord,
                colors = ButtonDefaults.buttonColors(containerColor = LearningModule.VOCABULARY.accent),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text("Explore word")
                Spacer(Modifier.size(8.dp))
                Icon(Icons.AutoMirrored.Rounded.ArrowForward, null, Modifier.size(16.dp))
            }
            OutlinedButton(
                onClick = onAddToReview,
                shape = RoundedCornerShape(12.dp),
            ) {
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
            if (word.meaningTr.isNotBlank()) {
                TextButton(onClick = onToggleTurkish) {
                    Icon(Icons.Rounded.Translate, null, tint = DashboardColors.PrimaryPinkDark, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.size(4.dp))
                    Text(
                        if (isTurkishRevealed) "Hide Turkish" else "Turkish",
                        color = DashboardColors.PrimaryPinkDark,
                    )
                }
            }
        }
    }
}

@Composable
private fun PracticeQuickColumn(
    dueCount: Int,
    flashCardCount: Int,
    onSmartPractice: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        FluentSurfaceCard(
            modifier = Modifier.fillMaxWidth(),
            accent = LearningModule.VOCABULARY.accent,
            emphasized = true,
            onClick = onSmartPractice,
            contentPadding = PaddingValues(20.dp),
            contentSpacing = 12.dp,
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Smart practice", style = DashboardTypography.SectionTitle, color = DashboardColors.TextPrimary)
                if (dueCount > 0) {
                    Surface(shape = RoundedCornerShape(8.dp), color = DashboardColors.PrimaryPink) {
                        Text(
                            "$dueCount due",
                            style = DashboardTypography.Caption.copy(fontFeatureSettings = "tnum"),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        )
                    }
                }
            }
            Text(
                if (dueCount > 0) {
                    "Review $dueCount due card${if (dueCount == 1) "" else "s"} or switch between match, context, collocation, and speed drills"
                } else {
                    "Start a focused drill from $flashCardCount prioritized words across 6 practice modes"
                },
                style = DashboardTypography.Body,
                color = DashboardColors.TextSecondary,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Start session", color = LearningModule.VOCABULARY.accent, style = DashboardTypography.Button)
                Spacer(Modifier.size(8.dp))
                Icon(Icons.AutoMirrored.Rounded.ArrowForward, null, tint = LearningModule.VOCABULARY.accent, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
internal fun PronunciationPillButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = DashboardColors.SurfacePink,
        border = BorderStroke(1.dp, DashboardColors.BorderSoft),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                Icons.AutoMirrored.Rounded.VolumeUp,
                contentDescription = "Pronounce ($label)",
                tint = DashboardColors.PrimaryPinkDark,
                modifier = Modifier.size(16.dp),
            )
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = DashboardColors.PrimaryPinkDark)
        }
    }
}

private fun VocabularyFeatureState.resumeContextLabel(): String = when (resumeDestination) {
    com.seanora.fluentai.core.model.VocabularyResumeDestination.WORD_OF_DAY -> "Return to today’s word"
    com.seanora.fluentai.core.model.VocabularyResumeDestination.LIBRARY -> "Return to your selected word"
    com.seanora.fluentai.core.model.VocabularyResumeDestination.FLASH_CARDS -> "Continue Flash Cards"
    com.seanora.fluentai.core.model.VocabularyResumeDestination.COLLECTIONS -> "Return to your selected collection"
    com.seanora.fluentai.core.model.VocabularyResumeDestination.PRACTICE_LABS -> "Continue ${resumePracticeMode?.title ?: "Practice"}"
    null -> ""
}
