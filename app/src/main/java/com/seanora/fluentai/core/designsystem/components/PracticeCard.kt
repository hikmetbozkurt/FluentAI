package com.seanora.fluentai.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.seanora.fluentai.core.designsystem.theme.FluentTheme

@Composable
fun PracticeCard(
    title: String,
    domain: String,
    duration: String,
    description: String,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier,
    actionButtonText: String = "Start Practice",
    level: CefrLevel? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(FluentTheme.shapes.card)
            .background(FluentTheme.colors.surfaceElevated)
            .border(
                1.dp,
                FluentTheme.colors.accentPrimary.copy(alpha = 0.4f),
                FluentTheme.shapes.card,
            )
            .clickable(onClick = onStartClick)
            .padding(FluentTheme.spacing.cardInternal),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.md),
        ) {
            // Header: Domain, Level, Duration
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(FluentTheme.spacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .clip(FluentTheme.shapes.badge)
                            .background(FluentTheme.colors.accentPrimary.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                    ) {
                        Text(
                            text = domain.uppercase(),
                            style = FluentTheme.typography.badge,
                            color = FluentTheme.colors.accentSecondary,
                        )
                    }

                    if (level != null) {
                        LevelBadge(level = level, size = BadgeSize.Compact)
                    }
                }

                Text(
                    text = duration,
                    style = FluentTheme.typography.labelMedium,
                    color = FluentTheme.colors.textSecondary,
                )
            }

            // Title & Description
            Column(verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.xxs)) {
                Text(
                    text = title,
                    style = FluentTheme.typography.headlineMedium,
                    color = FluentTheme.colors.textPrimary,
                )
                Text(
                    text = description,
                    style = FluentTheme.typography.bodyMedium,
                    color = FluentTheme.colors.textSecondary,
                )
            }

            // Action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FluentButton(
                    text = actionButtonText,
                    variant = ButtonVariant.Primary,
                    onClick = onStartClick,
                )
            }
        }
    }
}
