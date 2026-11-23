package com.seanora.fluentai.feature.reading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.FactCheck
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seanora.fluentai.app.navigation.ReadingSessionMode
import com.seanora.fluentai.core.designsystem.theme.FluentTheme
import com.seanora.fluentai.core.model.ReadingArticle

@Composable
fun ReadingDashboardScreen(
    onNavigateBack: () -> Unit,
    onOpenArticle: (String, ReadingSessionMode) -> Unit,
    onOpenLibrary: () -> Unit,
    modifier: Modifier = Modifier,
    onOpenLiveFeed: (() -> Unit)? = null,
    viewModel: ReadingFeatureViewModel = hiltViewModel(),
    supportViewModel: ReadingReaderSupportViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val surfaceStyle by supportViewModel.surfaceStyle.collectAsStateWithLifecycle()
    val savedIds by supportViewModel.savedReadingIds.collectAsStateWithLifecycle()
    ReadingTheme(style = surfaceStyle) {
        ReadingHomeContent(state, savedIds, onNavigateBack, onOpenArticle, onOpenLibrary, onOpenLiveFeed, modifier)
    }
}

@Composable
private fun ReadingHomeContent(
    state: ReadingFeatureUiState,
    savedIds: Set<String>,
    onNavigateBack: () -> Unit,
    onOpenArticle: (String, ReadingSessionMode) -> Unit,
    onOpenLibrary: () -> Unit,
    onOpenLiveFeed: (() -> Unit)?,
    modifier: Modifier,
) {
    val tokens = LocalReadingTokens.current
    val primary = state.continueArticle ?: state.dailyArticle
    var showSkills by rememberSaveable { mutableStateOf(false) }
    BoxWithConstraints(modifier.fillMaxSize().background(tokens.paper)) {
        if (state.isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center), color = tokens.terracotta)
            return@BoxWithConstraints
        }
        val expanded = maxWidth >= ReadingBreakpoints.Expanded
        val horizontalPadding = if (maxWidth >= ReadingBreakpoints.Medium) 40.dp else 20.dp
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = horizontalPadding, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(Modifier.widthIn(max = 1200.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                ReadingBackButton(label = "Learn", onClick = onNavigateBack)
                ReadingHero(expanded = expanded)

                val primaryCard: @Composable (Modifier) -> Unit = { cardModifier ->
                    if (primary != null) {
                        PrimaryReadingCard(
                            label = if (state.continueArticle != null) "Continue reading" else "Recommended reading",
                            article = primary,
                            stacked = !expanded,
                            onClick = { onOpenArticle(primary.id, ReadingSessionMode.STANDARD) },
                            modifier = cardModifier,
                        )
                    } else {
                        EntryCard(Icons.AutoMirrored.Rounded.MenuBook, "Find your next article", "Browse the reading library", onClick = onOpenLibrary, modifier = cardModifier)
                    }
                }
                val sideColumn: @Composable (Modifier) -> Unit = { columnModifier ->
                    Column(columnModifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Today’s reading", style = ReadingType.Heading, color = ReadingPalette.Ink)
                            Text("A fresh article picked for your level", style = ReadingType.Meta, color = ReadingPalette.InkSecondary)
                        }
                        state.dailyArticle?.let { article ->
                            ArticleRowCard(article, onClick = { onOpenArticle(article.id, ReadingSessionMode.STANDARD) })
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            EntryCard(
                                Icons.AutoMirrored.Rounded.MenuBook,
                                "Browse articles",
                                if (savedIds.isNotEmpty()) "${state.articles.size} articles · ${savedIds.size} saved" else "Explore all ${state.articles.size} articles",
                                onClick = onOpenLibrary,
                                modifier = Modifier.weight(1f),
                            )
                            val skillItem = state.currentSkillItem
                            EntryCard(
                                Icons.AutoMirrored.Rounded.FactCheck,
                                "Practice a skill",
                                if (skillItem == null) "No tagged skill practice available" else "Comprehension in article context",
                                onClick = { if (skillItem != null) showSkills = !showSkills },
                                modifier = Modifier.weight(1f),
                            )
                        }
                        if (onOpenLiveFeed != null) {
                            EntryCard(
                                Icons.Rounded.Public,
                                "Discover live articles",
                                "Fresh journalism from The Conversation and The Guardian, cached offline",
                                onClick = onOpenLiveFeed,
                            )
                        }
                        if (showSkills) {
                            state.skills.forEach { skill ->
                                skill.items.firstOrNull()?.let { item ->
                                    EntryCard(
                                        Icons.AutoMirrored.Rounded.FactCheck,
                                        skill.title,
                                        "Practice with ${item.article.title}",
                                        onClick = { onOpenArticle(item.article.id, ReadingSessionMode.STANDARD) },
                                    )
                                }
                            }
                        }
                        state.weakSpots.firstOrNull()?.let { weak ->
                            EntryCard(
                                Icons.Rounded.TrackChanges,
                                "Recommended review",
                                weak.article.title,
                                onClick = { onOpenArticle(weak.article.id, ReadingSessionMode.STANDARD) },
                                emphasized = true,
                            )
                        }
                    }
                }

                if (expanded) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        primaryCard(Modifier.weight(1.15f))
                        sideColumn(Modifier.weight(1f))
                    }
                } else {
                    primaryCard(Modifier.fillMaxWidth())
                    sideColumn(Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
private fun ReadingHero(expanded: Boolean) {
    val titleBlock: @Composable (Modifier) -> Unit = { m ->
        Column(m, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ReadingEyebrow("Learn · Reading")
            Text("Reading", style = ReadingType.Display, color = ReadingPalette.Ink)
            Text(
                "Build academic reading comprehension through real texts",
                style = FluentTheme.typography.bodyLarge,
                color = ReadingPalette.InkSecondary,
            )
        }
    }
    if (expanded) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(32.dp), verticalAlignment = Alignment.CenterVertically) {
            titleBlock(Modifier.weight(1f))
            ReadingIllustrationImage(
                ReadingIllustration.HERO,
                modifier = Modifier.weight(1.2f).height(220.dp).clip(RoundedCornerShape(16.dp)),
            )
        }
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            ReadingIllustrationImage(
                ReadingIllustration.HERO,
                modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(16.dp)),
            )
            titleBlock(Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun PrimaryReadingCard(
    label: String,
    article: ReadingArticle,
    stacked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ReadingCard(modifier = modifier, onClick = onClick, onClickLabel = "$label: ${article.title}") {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ReadingCoverImage(article.id, Modifier.fillMaxWidth().aspectRatio(if (stacked) 2f else 3f / 2f))
            Column(Modifier.padding(horizontal = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ReadingEyebrow(label)
                Text(article.title, style = ReadingType.Title, color = ReadingPalette.Ink)
                Text(
                    article.summaryEn,
                    style = FluentTheme.typography.bodyLarge,
                    color = ReadingPalette.InkSecondary,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Row(
                Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(article.fullMeta(), style = ReadingType.Meta, color = ReadingPalette.InkMuted, modifier = Modifier.weight(1f))
                Surface(shape = RoundedCornerShape(12.dp), color = ReadingPalette.Ink) {
                    Row(
                        Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text("Continue", style = FluentTheme.typography.labelLarge, color = ReadingPalette.Card)
                        Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null, tint = ReadingPalette.Card, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ArticleRowCard(article: ReadingArticle, onClick: () -> Unit) {
    ReadingCard(modifier = Modifier.fillMaxWidth(), onClick = onClick, onClickLabel = article.title) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
            ReadingCoverImage(article.id, Modifier.width(112.dp).aspectRatio(1f))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(article.title, style = ReadingType.CardTitle, color = ReadingPalette.Ink, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(article.fullMeta(), style = ReadingType.Meta, color = ReadingPalette.InkMuted)
            }
            Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null, tint = ReadingPalette.InkMuted)
        }
    }
}

@Composable
private fun EntryCard(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    emphasized: Boolean = false,
) {
    ReadingCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        onClickLabel = "$title. $description",
        color = if (emphasized) ReadingPalette.Clay else ReadingPalette.Card,
    ) {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(if (emphasized) ReadingPalette.Card else ReadingPalette.Elevated),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, tint = if (emphasized) ReadingPalette.Terracotta else ReadingPalette.Ink, modifier = Modifier.size(20.dp))
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(title, style = FluentTheme.typography.titleMedium, color = ReadingPalette.Ink, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(description, style = FluentTheme.typography.bodySmall, color = ReadingPalette.InkSecondary, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

