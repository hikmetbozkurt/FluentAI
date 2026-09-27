package com.seanora.fluentai.core.designsystem.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seanora.fluentai.core.designsystem.theme.FluentColors
import com.seanora.fluentai.core.designsystem.theme.FluentTheme

object DashboardColors {
    val BackgroundPrimary = Color(0xFFFFF7FA)
    val BackgroundSecondary = Color(0xFFFFEDF4)
    val Surface = Color(0xFFFFFFFF)
    val SurfacePink = Color(0xFFFFE4EF)
    val PrimaryPink = Color(0xFFEC4C92)
    val PrimaryPinkDark = Color(0xFFC92F76)
    val PrimaryPinkLight = Color(0xFFF9A8CB)
    val TextPrimary = Color(0xFF352A32)
    val TextSecondary = Color(0xFF756871)
    val BorderSoft = Color(0xFFF3DCE6)
    val ProgressTrack = Color(0xFFF6DEE8)
    val PurplePastel = Color(0xFFF0E8FF)
    val PeachPastel = Color(0xFFFFEBDD)
    val MintPastel = Color(0xFFE2F6EC)
    val BluePastel = Color(0xFFE6F1FF)
    val Warning = Color(0xFFE88C55)
    val PlantGreen = Color(0xFF67A57C)
}

val LingoPinkFluentColors = FluentColors(
    surfaceBase = DashboardColors.BackgroundPrimary,
    surfaceCard = DashboardColors.Surface,
    surfaceElevated = DashboardColors.SurfacePink,
    surfaceBorder = DashboardColors.BorderSoft,
    surfaceHighlight = DashboardColors.PrimaryPinkLight,
    accentPrimary = DashboardColors.PrimaryPink,
    accentSecondary = DashboardColors.PrimaryPinkDark,
    accentGlow = DashboardColors.PrimaryPink.copy(alpha = 0.18f),
    accentGradient = Brush.horizontalGradient(
        listOf(DashboardColors.PrimaryPink, DashboardColors.PrimaryPinkDark)
    ),
    textPrimary = DashboardColors.TextPrimary,
    textSecondary = DashboardColors.TextSecondary,
    textMuted = DashboardColors.TextSecondary.copy(alpha = 0.78f),
)

/** Keeps custom Fluent tokens and Material component colors on the same light palette. */
@Composable
fun LingoPinkTheme(content: @Composable () -> Unit) {
    FluentTheme(colors = LingoPinkFluentColors, content = content)
}

object DashboardTypography {
    val Display = TextStyle(fontSize = 28.sp, lineHeight = 34.sp, fontWeight = FontWeight.Bold)
    val PageTitle = TextStyle(fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.Bold)
    val CardTitle = TextStyle(fontSize = 17.sp, lineHeight = 22.sp, fontWeight = FontWeight.Bold)
    val Body = TextStyle(fontSize = 13.sp, lineHeight = 19.sp)
    val Caption = TextStyle(fontSize = 11.sp, lineHeight = 15.sp)
    val Button = TextStyle(fontSize = 14.sp, lineHeight = 18.sp, fontWeight = FontWeight.SemiBold)
    val Numeric = TextStyle(fontSize = 36.sp, lineHeight = 40.sp, fontWeight = FontWeight.Bold)
}

object DashboardSpacing {
    val xxs = 4.dp
    val xs = 8.dp
    val sm = 12.dp
    val md = 16.dp
    val lg = 20.dp
    val xl = 24.dp
    val xxl = 32.dp
}

object DashboardDimensions {
    val CardRadius = 16.dp
    val ButtonRadius = 14.dp
    val CardPadding = 20.dp
    val ControlHeight = 52.dp
    val PagePadding = 24.dp
    val GridGap = 16.dp
    val LearningCardHeight = 108.dp
    val LearningContinueCardHeight = 96.dp
    val LearningCardHorizontalPadding = 14.dp
    val LearningCardVerticalPadding = 10.dp
    val LearningCardContentSpacing = 6.dp
    val LearningIconContainerSize = 40.dp
    val LearningIconSize = 22.dp
}
