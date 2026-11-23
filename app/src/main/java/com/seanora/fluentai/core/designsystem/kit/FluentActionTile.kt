package com.seanora.fluentai.core.designsystem.kit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.designsystem.dashboard.DashboardTypography

@Composable
fun FluentActionTile(
    title: String,
    detail: String,
    module: LearningModule,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    emoji: String = module.emoji,
    icon: ImageVector = module.icon,
    actionLabel: String? = null,
    emphasized: Boolean = false,
    revealDelayMillis: Int = staggerDelay(1)
) {
    FluentSurfaceCard(
        modifier = modifier,
        accent = module.accent.takeIf { emphasized },
        emphasized = emphasized,
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            IconTile(icon = icon, accent = module.accent, container = module.container, size = 44.dp, badgeEmoji = null)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(title, color = DashboardColors.TextPrimary, style = DashboardTypography.CardTitle, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(detail, color = DashboardColors.TextSecondary, style = DashboardTypography.Body, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
            if (actionLabel != null) {
                Text(actionLabel, color = module.accent, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            Icon(
                Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = null,
                tint = DashboardColors.TextMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
