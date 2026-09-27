package com.seanora.fluentai.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seanora.fluentai.core.designsystem.theme.FluentTheme

@OptIn(ExperimentalLayoutApi::class)
@Preview(name = "Shared Base Components - Tablet", widthDp = 800, heightDp = 1280, showBackground = true)
@Composable
private fun ComponentsPreview() {
    FluentTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = FluentTheme.colors.surfaceBase,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(FluentTheme.spacing.screenHorizontal),
                verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.sectionSpacing),
            ) {
                // Header
                SectionHeader(
                    title = "FluentAI Shared Base Components",
                    subtitle = "Phase 0 design system foundations on tablet surface",
                )

                // 1. PracticeCard (Hero Today's Practice)
                SectionHeader(
                    title = "Today's Practice Card",
                    subtitle = "High-emphasis hero card for daily orchestrated practice",
                )
                PracticeCard(
                    title = "Executive Alignment Briefing",
                    domain = "Speaking Coach",
                    duration = "12 min",
                    description = "Practice articulating trade-offs and managing executive interruption in an English meeting simulation.",
                    level = CefrLevel.C1,
                    onStartClick = {},
                )

                // 2. Buttons
                SectionHeader(
                    title = "Button Variants",
                    subtitle = "Accessible touch targets (>= 48dp) across primary, secondary, tonal, outlined, and disabled states",
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(FluentTheme.spacing.md),
                    verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.sm),
                ) {
                    FluentButton(text = "Primary Action", variant = ButtonVariant.Primary, onClick = {})
                    FluentButton(text = "Secondary Action", variant = ButtonVariant.Secondary, onClick = {})
                    FluentButton(text = "Tonal Action", variant = ButtonVariant.Tonal, onClick = {})
                    FluentButton(text = "Outlined Action", variant = ButtonVariant.Outlined, onClick = {})
                    FluentButton(text = "Disabled Action", enabled = false, onClick = {})
                }

                // 3. Level Badges
                SectionHeader(
                    title = "CEFR Level Badges",
                    subtitle = "Semantic indicators for skill-specific CEFR levels A2 to C2",
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(FluentTheme.spacing.md),
                ) {
                    LevelBadge(level = CefrLevel.A2)
                    LevelBadge(level = CefrLevel.B1)
                    LevelBadge(level = CefrLevel.B2)
                    LevelBadge(level = CefrLevel.C1)
                    LevelBadge(level = CefrLevel.C2)
                }

                // 4. Content Cards
                SectionHeader(
                    title = "Curriculum Content Cards",
                    subtitle = "Standardized items for Vocabulary, Grammar, and Reading",
                    actionText = "View All",
                    onActionClick = {},
                )
                Column(verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.md)) {
                    ContentCard(
                        title = "ambiguity",
                        level = CefrLevel.B2,
                        category = "Vocabulary • Professional",
                        phonetic = "/ˌæm.bɪˈɡjuː.ə.ti/",
                        subtitle = "The quality of being open to more than one interpretation; inexactness.",
                        statusText = "Review Due",
                        onClick = {},
                    )
                    ContentCard(
                        title = "Third Conditional vs. Mixed Conditional",
                        level = CefrLevel.C1,
                        category = "Grammar • Nuance",
                        subtitle = "Expressing hypothetical past conditions with present outcomes versus pure past regrets.",
                        variant = CardVariant.Elevated,
                        onClick = {},
                    )
                }

                // 5. Card Variants
                SectionHeader(
                    title = "Card Surfaces",
                    subtitle = "Standard, Elevated, and Accent-Outlined containers",
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(FluentTheme.spacing.md),
                ) {
                    FluentCard(
                        modifier = Modifier.weight(1f),
                        variant = CardVariant.Standard,
                    ) {
                        Text(
                            text = "Standard Card Surface",
                            style = FluentTheme.typography.bodyMedium,
                            color = FluentTheme.colors.textPrimary,
                        )
                    }
                    FluentCard(
                        modifier = Modifier.weight(1f),
                        variant = CardVariant.Elevated,
                    ) {
                        Text(
                            text = "Elevated Card Surface",
                            style = FluentTheme.typography.bodyMedium,
                            color = FluentTheme.colors.textPrimary,
                        )
                    }
                    FluentCard(
                        modifier = Modifier.weight(1f),
                        variant = CardVariant.AccentOutlined,
                    ) {
                        Text(
                            text = "Accent Outlined Surface",
                            style = FluentTheme.typography.bodyMedium,
                            color = FluentTheme.colors.textPrimary,
                        )
                    }
                }
            }
        }
    }
}
