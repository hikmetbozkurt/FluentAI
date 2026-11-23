package com.seanora.fluentai.core.designsystem.kit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                title,
                color = DashboardColors.TextPrimary,
                style = DashboardTypography.SectionTitle,
                modifier = Modifier.semantics { heading() }
            )
            subtitle?.let { Text(it, color = DashboardColors.TextSecondary, style = DashboardTypography.Body) }
        }
        if (actionLabel != null && onAction != null) {
            Surface(onClick = onAction, shape = RoundedCornerShape(10.dp), color = accent.copy(alpha = 0.08f)) {
                Text(
                    "$actionLabel →",
                    color = accent,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    }
}
