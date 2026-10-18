package com.seanora.fluentai.core.designsystem.kit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.designsystem.dashboard.DashboardTypography

@Composable
fun FluentStatPill(
    emoji: String,
    value: String,
    label: String,
    module: LearningModule,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = module.container,
        border = BorderStroke(1.dp, module.accent.copy(alpha = 0.25f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(emoji, fontSize = 22.sp)
            Column {
                Text(value, color = DashboardColors.TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                Text(label, color = DashboardColors.TextSecondary, style = DashboardTypography.Caption)
            }
        }
    }
}
