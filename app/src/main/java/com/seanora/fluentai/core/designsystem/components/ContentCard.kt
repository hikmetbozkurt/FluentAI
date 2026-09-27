package com.seanora.fluentai.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.seanora.fluentai.core.designsystem.theme.FluentTheme

@Composable
fun ContentCard(
    title: String,
    modifier: Modifier = Modifier,
    level: CefrLevel? = null,
    category: String? = null,
    subtitle: String? = null,
    phonetic: String? = null,
    statusText: String? = null,
    variant: CardVariant = CardVariant.Standard,
    onClick: (() -> Unit)? = null,
) {
    FluentCard(
        modifier = modifier.fillMaxWidth(),
        variant = variant,
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.xs),
        ) {
            // Top metadata row
            if (category != null || level != null || statusText != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(FluentTheme.spacing.sm),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (level != null) {
                            LevelBadge(level = level, size = BadgeSize.Compact)
                        }
                        if (category != null) {
                            Text(
                                text = category.uppercase(),
                                style = FluentTheme.typography.labelSmall,
                                color = FluentTheme.colors.textMuted,
                            )
                        }
                    }

                    if (statusText != null) {
                        Text(
                            text = statusText,
                            style = FluentTheme.typography.labelSmall,
                            color = FluentTheme.colors.accentSecondary,
                        )
                    }
                }
            }

            // Title & phonetic
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(FluentTheme.spacing.sm),
            ) {
                Text(
                    text = title,
                    style = FluentTheme.typography.titleLarge,
                    color = FluentTheme.colors.textPrimary,
                )
                if (phonetic != null) {
                    Text(
                        text = phonetic,
                        style = FluentTheme.typography.phonetic,
                    )
                }
            }

            // Subtitle / definition / preview
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = FluentTheme.typography.bodyMedium,
                    color = FluentTheme.colors.textSecondary,
                )
            }
        }
    }
}
