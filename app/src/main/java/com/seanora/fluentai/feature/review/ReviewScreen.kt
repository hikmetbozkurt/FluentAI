package com.seanora.fluentai.feature.review

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoFixHigh
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.seanora.fluentai.core.designsystem.components.BadgeSize
import com.seanora.fluentai.core.designsystem.components.LevelBadge
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.designsystem.dashboard.DashboardTypography
import com.seanora.fluentai.core.designsystem.kit.FluentPageHeader
import com.seanora.fluentai.core.designsystem.kit.LearningModule
import com.seanora.fluentai.core.model.MistakeStage

@Composable
fun ReviewScreen(
    onNavigateBack: () -> Unit,
    onOpenReviewItem: (domain: String, contentId: String) -> Unit,
    onStartVocabFlashCards: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReviewViewModel = hiltViewModel(),
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
                color = LearningModule.REVIEW.accent,
            )
            return@BoxWithConstraints
        }

        val wide = maxWidth >= 820.dp
        val horizontalPadding = if (maxWidth >= 600.dp) 32.dp else 16.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = horizontalPadding, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.widthIn(max = 1120.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                FluentPageHeader(
                    title = "Smart review and mistake bank",
                    module = LearningModule.REVIEW,
                    eyebrow = "Spaced repetition · L1 trap resolution",
                    subtitle = "Clear due reviews and turn recurring mistakes into lasting mastery",
                    onBack = onNavigateBack,
                    backLabel = "Back",
                )

                ReviewHealthStrip(
                    dueCount = state.dueReviews.size,
                    activeMistakes = state.mistakeSummary.totalActiveCount,
                    recurringMistakes = state.mistakeSummary.recurringCount,
                    resolvedMistakes = state.mistakeSummary.resolvedCount,
                    wide = wide,
                )

                // Tab Switcher
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ReviewTab.entries.forEach { tab ->
                        val selected = state.selectedTab == tab
                        val countSuffix = when (tab) {
                            ReviewTab.DUE_REVIEWS -> " (${state.dueReviews.size})"
                            ReviewTab.MISTAKE_BANK -> " (${state.activeMistakes.size})"
                            ReviewTab.FIX_IT -> ""
                        }
                        Surface(
                            onClick = { viewModel.selectTab(tab) },
                            shape = RoundedCornerShape(12.dp),
                            color = if (selected) LearningModule.REVIEW.accent else Color.White,
                            border = BorderStroke(1.dp, if (selected) LearningModule.REVIEW.accent else DashboardColors.BorderSoft),
                        ) {
                            Text(
                                "${tab.label}$countSuffix",
                                style = DashboardTypography.Button.copy(fontFeatureSettings = "tnum"),
                                color = if (selected) Color.White else DashboardColors.TextPrimary,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            )
                        }
                    }
                }

                when (state.selectedTab) {
                    ReviewTab.DUE_REVIEWS -> DueReviewsSection(
                        items = state.dueReviews,
                        onOpenReviewItem = onOpenReviewItem,
                        onStartVocabFlashCards = onStartVocabFlashCards,
                    )
                    ReviewTab.MISTAKE_BANK -> MistakeBankSection(
                        filter = state.mistakeFilter,
                        mistakes = state.filteredMistakes,
                        onSelectFilter = viewModel::selectMistakeFilter,
                        onStartFixIt = viewModel::startFixItFor,
                    )
                    ReviewTab.FIX_IT -> QuickFixItSection(
                        mistake = state.currentFixItMistake,
                        totalActive = state.activeMistakes.size,
                        currentIndex = state.fixItIndex,
                        isRevealed = state.isFixItRevealed,
                        onReveal = viewModel::revealFixIt,
                        onOutcome = viewModel::submitFixItOutcome,
                    )
                }
            }
        }
    }
}

@Composable
private fun ReviewHealthStrip(
    dueCount: Int,
    activeMistakes: Int,
    recurringMistakes: Int,
    resolvedMistakes: Int,
    wide: Boolean,
) {
    val cards = listOf(
        Triple("Due reviews", "$dueCount", DashboardColors.TextPrimary),
        Triple("Active mistakes", "$activeMistakes", if (activeMistakes > 0) DashboardColors.Warning else DashboardColors.TextPrimary),
        Triple("Recurring traps", "$recurringMistakes", if (recurringMistakes > 0) Color(0xFFD92D20) else DashboardColors.TextPrimary),
        Triple("Resolved", "$resolvedMistakes", DashboardColors.PlantGreen),
    )
    if (wide) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            cards.forEach { (label, value, valueColor) ->
                ReviewStatCard(label, value, valueColor, Modifier.weight(1f))
            }
        }
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            cards.chunked(2).forEach { pair ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    pair.forEach { (label, value, valueColor) ->
                        ReviewStatCard(label, value, valueColor, Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewStatCard(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, DashboardColors.BorderSoft),
    ) {
        Column(
            Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(label, style = DashboardTypography.Caption, color = DashboardColors.TextSecondary)
            Text(
                value,
                style = DashboardTypography.PageTitle.copy(fontSize = 24.sp, fontFeatureSettings = "tnum"),
                color = valueColor,
            )
        }
    }
}

@Composable
private fun DueReviewsSection(
    items: List<ResolvedDueReview>,
    onOpenReviewItem: (String, String) -> Unit,
    onStartVocabFlashCards: () -> Unit,
) {
    if (items.isEmpty()) {
        ReviewEmptyCard(
            title = "All caught up on reviews",
            subtitle = "No overdue spaced-repetition cards right now. Practice Vocabulary, Chunks, Reading, Grammar, or Speaking to build new review items",
        )
        return
    }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        val vocabDueCount = items.count { it.schedule.domain.equals("vocabulary", ignoreCase = true) }
        if (vocabDueCount > 0) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = LearningModule.VOCABULARY.container,
                border = BorderStroke(1.dp, LearningModule.VOCABULARY.accent.copy(alpha = 0.3f)),
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            "$vocabDueCount Vocabulary Flash Card${if (vocabDueCount == 1) "" else "s"} Ready",
                            style = DashboardTypography.CardTitle,
                            color = DashboardColors.TextPrimary,
                        )
                        Text(
                            "Clear all due vocabulary items in one continuous SRS session.",
                            style = DashboardTypography.Body,
                            color = DashboardColors.TextSecondary,
                        )
                    }
                    Button(
                        onClick = onStartVocabFlashCards,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LearningModule.VOCABULARY.accent),
                    ) {
                        Text("Start Flash Cards")
                    }
                }
            }
        }

        items.forEach { item ->
            val module = LearningModule.forDomain(item.schedule.domain)
            Surface(
                onClick = { onOpenReviewItem(item.schedule.domain, item.schedule.contentId) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(1.dp, DashboardColors.BorderSoft),
            ) {
                Row(
                    Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Box(
                        Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(module.container),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(module.icon, null, tint = module.accent, modifier = Modifier.size(20.dp))
                    }
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                item.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = DashboardColors.TextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            item.cefrLevel?.let { LevelBadge(levelString = it, size = BadgeSize.Compact) }
                        }
                        Text(
                            "${item.schedule.domain.replaceFirstChar { it.uppercase() }} · Interval ${item.schedule.intervalDays.toInt()}d · Rep #${item.schedule.repetitionNumber}",
                            style = DashboardTypography.Caption,
                            color = DashboardColors.TextSecondary,
                        )
                    }
                    Text("Review", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = module.accent)
                    Icon(Icons.AutoMirrored.Rounded.ArrowForward, null, tint = module.accent, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MistakeBankSection(
    filter: MistakeFilter,
    mistakes: List<ResolvedMistake>,
    onSelectFilter: (MistakeFilter) -> Unit,
    onStartFixIt: (Long) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            MistakeFilter.entries.forEach { item ->
                val active = item == filter
                Surface(
                    onClick = { onSelectFilter(item) },
                    shape = RoundedCornerShape(50),
                    color = if (active) DashboardColors.PrimaryPink else Color.White,
                    border = BorderStroke(1.dp, if (active) DashboardColors.PrimaryPink else DashboardColors.BorderSoft),
                ) {
                    Text(
                        item.label,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (active) Color.White else DashboardColors.TextPrimary,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                    )
                }
            }
        }

        if (mistakes.isEmpty()) {
            ReviewEmptyCard(
                title = "No mistakes in this view",
                subtitle = if (filter == MistakeFilter.RESOLVED) {
                    "Resolved mistakes will appear here once you conquer them in Quick Fix-It or Speaking."
                } else {
                    "Great work! As you practice Grammar, Vocabulary, and Speaking, any recurring patterns are tracked here automatically."
                },
            )
            return
        }

        mistakes.forEach { item ->
            val rec = item.record
            val stageColor = when (rec.stage) {
                MistakeStage.RECURRING -> Color(0xFFD92D20)
                MistakeStage.TARGETED, MistakeStage.MONITORING -> DashboardColors.Warning
                MistakeStage.RESOLVED -> DashboardColors.PlantGreen
                else -> LearningModule.GRAMMAR.accent
            }
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = Color.White,
                border = BorderStroke(1.dp, DashboardColors.BorderSoft),
            ) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(Modifier.weight(1f)) {
                            Text(item.title, style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
                            Text(
                                "${rec.trapType.replace('_', ' ')} · Seen ${rec.occurrenceCount}×",
                                style = DashboardTypography.Caption,
                                color = DashboardColors.TextSecondary,
                            )
                        }
                        Surface(shape = RoundedCornerShape(50), color = stageColor.copy(alpha = 0.12f)) {
                            Text(
                                rec.stage.name,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = stageColor,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            )
                        }
                    }

                    if (rec.errorDescription.isNotBlank()) {
                        Text(rec.errorDescription, style = DashboardTypography.Body, color = DashboardColors.TextSecondary)
                    }

                    if (rec.userAnswer.isNotBlank() || rec.correctAnswer.isNotBlank()) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            if (rec.userAnswer.isNotBlank()) {
                                Surface(shape = RoundedCornerShape(10.dp), color = Color(0xFFFDECEC)) {
                                    Text(
                                        "✗  ${rec.userAnswer}",
                                        fontSize = 14.sp,
                                        color = Color(0xFFB42318),
                                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                                    )
                                }
                            }
                            if (rec.correctAnswer.isNotBlank()) {
                                Surface(shape = RoundedCornerShape(10.dp), color = Color(0xFFE6F6EC)) {
                                    Text(
                                        "✓  ${rec.correctAnswer}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = DashboardColors.PlantGreen,
                                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                                    )
                                }
                            }
                        }
                    }

                    if (rec.stage != MistakeStage.RESOLVED) {
                        OutlinedButton(
                            onClick = { onStartFixIt(rec.id) },
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            Icon(Icons.Rounded.AutoFixHigh, null, tint = LearningModule.REVIEW.accent, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.size(6.dp))
                            Text("Practice in Quick Fix-It", color = DashboardColors.TextPrimary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickFixItSection(
    mistake: ResolvedMistake?,
    totalActive: Int,
    currentIndex: Int,
    isRevealed: Boolean,
    onReveal: () -> Unit,
    onOutcome: (Boolean) -> Unit,
) {
    if (mistake == null) {
        ReviewEmptyCard(
            title = "No active mistakes to drill",
            subtitle = "All tracked mistakes are resolved. Keep practicing across modules to maintain accuracy!",
        )
        return
    }
    val rec = mistake.record
    Surface(
        modifier = Modifier.widthIn(max = 680.dp).fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.5.dp, LearningModule.REVIEW.accent.copy(alpha = 0.35f)),
    ) {
        Column(Modifier.padding(26.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "FIX-IT DRILL · ${currentIndex + 1} OF $totalActive",
                    style = DashboardTypography.Eyebrow,
                    color = LearningModule.REVIEW.accent,
                )
                Surface(shape = RoundedCornerShape(50), color = DashboardColors.BackgroundSecondary) {
                    Text(
                        rec.stage.name,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DashboardColors.PrimaryPinkDark,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    )
                }
            }

            Text(mistake.title, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = DashboardColors.TextPrimary)
            Text(rec.errorDescription, style = DashboardTypography.Body, color = DashboardColors.TextSecondary)

            Surface(shape = RoundedCornerShape(14.dp), color = Color(0xFFFDECEC)) {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("SPOT & FIX THIS PATTERN", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB42318))
                    Text(
                        rec.userAnswer.ifBlank { "Previous attempt needs correction" },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFB42318),
                    )
                }
            }

            if (!isRevealed) {
                Button(
                    onClick = onReveal,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LearningModule.REVIEW.accent),
                ) {
                    Text("Reveal Natural English")
                }
            } else {
                Surface(shape = RoundedCornerShape(14.dp), color = Color(0xFFE6F6EC)) {
                    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("NATURAL ENGLISH", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DashboardColors.PlantGreen)
                        Text(
                            rec.correctAnswer,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = DashboardColors.PlantGreen,
                        )
                    }
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = { onOutcome(false) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text("Still Tricky", color = Color(0xFFD92D20))
                    }
                    Button(
                        onClick = { onOutcome(true) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DashboardColors.PlantGreen),
                    ) {
                        Icon(Icons.Rounded.CheckCircle, null, Modifier.size(18.dp))
                        Spacer(Modifier.size(6.dp))
                        Text("Got It!")
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewEmptyCard(title: String, subtitle: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(1.dp, DashboardColors.BorderSoft),
    ) {
        Column(
            Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(Icons.Rounded.Verified, null, tint = DashboardColors.PlantGreen, modifier = Modifier.size(40.dp))
            Text(title, style = DashboardTypography.SectionTitle, color = DashboardColors.TextPrimary)
            Text(subtitle, style = DashboardTypography.Body, color = DashboardColors.TextSecondary)
        }
    }
}
