package com.seanora.fluentai.core.designsystem.kit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.designsystem.dashboard.DashboardTypography

@Composable
fun FluentSectionTitle(
    title: String,
    modifier: Modifier = Modifier,
    emoji: String? = null,
    subtitle: String? = null,
    actionLabel: String? = null,
    accent: Color = DashboardColors.PrimaryPinkDark,
    onAction: (() -> Unit)? = null
) {
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            emoji?.let { EmojiBadge(emoji = it, size = 34.dp) }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    title,
                    color = DashboardColors.TextPrimary,
                    style = DashboardTypography.SectionTitle,
                    modifier = Modifier.semantics { heading() }
                )
                subtitle?.let { Text(it, color = DashboardColors.TextSecondary, style = DashboardTypography.Body) }
            }
        }
        if (actionLabel != null && onAction != null) {
            Surface(onClick = onAction, shape = RoundedCornerShape(50), color = accent.copy(alpha = 0.1f)) {
                Text(
                    "$actionLabel  →",
                    color = accent,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }
        }
    }
}
