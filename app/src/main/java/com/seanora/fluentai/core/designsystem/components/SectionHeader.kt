package com.seanora.fluentai.core.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    trailingAction: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f, fill = false)) {
            Text(
                text = title,
                style = FluentTheme.typography.titleLarge,
                color = FluentTheme.colors.textPrimary,
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(FluentTheme.spacing.xxs))
                Text(
                    text = subtitle,
                    style = FluentTheme.typography.bodyMedium,
                    color = FluentTheme.colors.textSecondary,
                )
            }
        }

        if (trailingAction != null) {
            trailingAction()
        } else if (actionText != null && onActionClick != null) {
            Text(
                text = actionText,
                style = FluentTheme.typography.labelMedium,
                color = FluentTheme.colors.accentSecondary,
                modifier = Modifier
                    .clip(FluentTheme.shapes.sm)
                    .clickable(onClick = onActionClick)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
            )
        }
    }
}
