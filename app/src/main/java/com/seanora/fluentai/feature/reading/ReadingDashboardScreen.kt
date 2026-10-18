package com.seanora.fluentai.feature.reading

import androidx.compose.foundation.background
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.FactCheck
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.seanora.fluentai.core.designsystem.kit.FluentActionTile
import com.seanora.fluentai.core.designsystem.kit.FluentPageHeader
import com.seanora.fluentai.core.designsystem.kit.FluentSectionTitle
import com.seanora.fluentai.core.designsystem.kit.LearningModule
import com.seanora.fluentai.app.navigation.ReadingSessionMode
import com.seanora.fluentai.core.designsystem.dashboard.DashboardCard
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.designsystem.dashboard.DashboardDimensions
import com.seanora.fluentai.core.designsystem.dashboard.LingoPinkTheme
import com.seanora.fluentai.core.designsystem.theme.FluentTheme
import com.seanora.fluentai.core.model.ReadingArticle

@Composable
fun ReadingDashboardScreen(
    onNavigateBack: () -> Unit,
    onOpenArticle: (String, ReadingSessionMode) -> Unit,
    onOpenLibrary: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReadingFeatureViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LingoPinkTheme {
        ReadingHomeContent(state, onNavigateBack, onOpenArticle, onOpenLibrary, modifier)
    }
}

@Composable
private fun ReadingHomeContent(
    state: ReadingFeatureUiState,
    onNavigateBack: () -> Unit,
    onOpenArticle: (String, ReadingSessionMode) -> Unit,
    onOpenLibrary: () -> Unit,
    modifier: Modifier,
) {
    val primary = state.continueArticle ?: state.dailyArticle
    var showSkills by rememberSaveable { mutableStateOf(false) }
    Box(modifier.fillMaxSize().background(DashboardColors.BackgroundPrimary)) {
        if (state.isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center), color = DashboardColors.PrimaryPinkDark)
            return@Box
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(DashboardDimensions.PagePadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            FluentPageHeader(
                title = "Reading",
                module = LearningModule.READING,
                eyebrow = "Learn · Reading",
                subtitle = "Build academic reading comprehension through real texts.",
                onBack = onNavigateBack,
                backLabel = "Learn",
            )
            if (primary != null) {
                PrimaryReadingCard(
                    label = if (state.continueArticle != null) "Continue Reading" else "Recommended Reading",
                    article = primary,
                    onClick = { onOpenArticle(primary.id, ReadingSessionMode.STANDARD) },
                )
            } else {
                CompactEntryCard(Icons.AutoMirrored.Rounded.MenuBook, "Find your next article", "Browse the Reading Library", onClick = onOpenLibrary, emoji = "🔎")
            }
            FluentSectionTitle(
                title = "Today’s Reading",
                emoji = "📅",
                subtitle = "A fresh article picked for your level",
                accent = LearningModule.READING.accent,
            )
            state.dailyArticle?.let { article ->
                CompactEntryCard(
                    Icons.Rounded.AutoStories,
                    article.title,
                    articleMeta(article),
                    onClick = { onOpenArticle(article.id, ReadingSessionMode.STANDARD) },
                )
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CompactEntryCard(Icons.AutoMirrored.Rounded.MenuBook, "Browse Articles", "Explore the complete Reading Library", onClick = onOpenLibrary, modifier = Modifier.weight(1f), emoji = "🗂️")
                val skillItem = state.currentSkillItem
                CompactEntryCard(
                    Icons.AutoMirrored.Rounded.FactCheck,
                    "Practice a Skill",
                    if (skillItem == null) "No tagged skill practice is available" else "Comprehension in article context",
                    onClick = { if (skillItem != null) showSkills = !showSkills },
                    modifier = Modifier.weight(1f),
                    emoji = "🏋️",
                )
            }
            if (showSkills) {
                state.skills.forEach { skill ->
                    skill.items.firstOrNull()?.let { item ->
                        CompactEntryCard(
                            Icons.AutoMirrored.Rounded.FactCheck,
                            skill.title,
                            "Practice with ${item.article.title}",
                            onClick = { onOpenArticle(item.article.id, ReadingSessionMode.STANDARD) },
                        )
                    }
                }
            }
            state.weakSpots.firstOrNull()?.let { weak ->
                CompactEntryCard(
                    Icons.Rounded.TrackChanges,
                    "Recommended review",
                    weak.article.title,
                    onClick = { onOpenArticle(weak.article.id, ReadingSessionMode.STANDARD) },
                    emoji = "🎯",
                    emphasized = true,
                )
            }
        }
    }
}

@Composable
private fun PrimaryReadingCard(label: String, article: ReadingArticle, onClick: () -> Unit) {
    DashboardCard(Modifier.fillMaxWidth().height(196.dp).clickable(onClick = onClick), background = DashboardColors.PrimaryPink) {
        Text("📖  $label", style = FluentTheme.typography.labelLarge, color = Color.White.copy(alpha = .92f))
        Text(article.title, style = FluentTheme.typography.headlineLarge, color = Color.White, fontWeight = FontWeight.Bold)
        Text(article.summaryEn, style = FluentTheme.typography.bodyMedium, color = Color.White.copy(alpha = .88f), maxLines = 2)
        Spacer(Modifier.weight(1f))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(articleMeta(article), style = FluentTheme.typography.labelMedium, color = Color.White.copy(alpha = .84f))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Continue", color = Color.White, fontWeight = FontWeight.SemiBold)
                Icon(Icons.AutoMirrored.Rounded.ArrowForward, null, tint = Color.White)
            }
        }
    }
}

@Composable
private fun CompactEntryCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    emoji: String = LearningModule.READING.emoji,
    emphasized: Boolean = false,
) {
    FluentActionTile(
        title = title,
        detail = description,
        module = LearningModule.READING,
        icon = icon,
        emoji = emoji,
        emphasized = emphasized,
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    )
}

private fun articleMeta(article: ReadingArticle) =
    "${article.cefrLevel} · ${article.category.replace('_', ' ').replaceFirstChar { it.uppercase() }} · ${article.wordCount} words · ~${article.estimatedReadingMinutes} min"
