package com.seanora.fluentai.feature.progress

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.designsystem.dashboard.DashboardTypography
import com.seanora.fluentai.domain.progress.Achievement
import com.seanora.fluentai.domain.progress.AchievementTier

@Composable
internal fun AchievementBadge(achievement: Achievement, modifier: Modifier = Modifier) {
    val tierColor = achievement.tier.color()
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = DashboardColors.Surface,
        border = BorderStroke(1.5.dp, if (achievement.isUnlocked) tierColor.copy(alpha = 0.6f) else DashboardColors.BorderSoft),
        shadowElevation = if (achievement.isUnlocked) 4.dp else 1.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        if (achievement.isUnlocked) Brush.linearGradient(listOf(tierColor.copy(alpha = 0.25f), tierColor.copy(alpha = 0.6f)))
                        else Brush.linearGradient(listOf(LOCKED_TINT, LOCKED_TINT)),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (achievement.isUnlocked) achievement.emoji else "🔒",
                    fontSize = 30.sp,
                    modifier = Modifier.alpha(if (achievement.isUnlocked) 1f else 0.7f)
                )
            }
            Text(
                achievement.title,
                color = DashboardColors.TextPrimary,
                style = DashboardTypography.CardTitle,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                achievement.description,
                color = DashboardColors.TextSecondary,
                style = DashboardTypography.Caption,
                textAlign = TextAlign.Center,
                maxLines = 2,
                minLines = 2
            )
            if (!achievement.isUnlocked) {
                LinearProgressIndicator(
                    progress = { achievement.completionRatio },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                    color = tierColor,
                    trackColor = DashboardColors.ProgressTrack
                )
                Text("${achievement.progress.coerceAtMost(achievement.target)} / ${achievement.target}", color = DashboardColors.TextMuted, style = DashboardTypography.Caption)
            } else {
                Text(achievement.tier.label(), color = tierColor, style = DashboardTypography.Eyebrow)
            }
        }
    }
}

private val LOCKED_TINT = Color(0xFFF1E7EC)

private fun AchievementTier.color(): Color = when (this) {
    AchievementTier.BRONZE -> Color(0xFFC07A3A)
    AchievementTier.SILVER -> Color(0xFF6E7E96)
    AchievementTier.GOLD -> Color(0xFFD49A00)
}

private fun AchievementTier.label(): String = when (this) {
    AchievementTier.BRONZE -> "BRONZE"
    AchievementTier.SILVER -> "SILVER"
    AchievementTier.GOLD -> "GOLD"
}
