package com.seanora.fluentai.feature.reading

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.seanora.fluentai.core.designsystem.theme.FluentColors
import com.seanora.fluentai.core.designsystem.theme.FluentTheme

enum class ReadingSurfaceStyle(val label: String) {
    PAPER("Paper"),
    LIGHT("Light"),
    SEPIA("Sepia"),
    DARK("Dark");

    companion object {
        fun fromKey(key: String?): ReadingSurfaceStyle =
            entries.firstOrNull { it.name.equals(key, ignoreCase = true) } ?: PAPER
    }
}

@Immutable
internal data class ReadingSurfaceTokens(
    val paper: Color,
    val card: Color,
    val elevated: Color,
    val border: Color,
    val sand: Color,
    val clay: Color,
    val ink: Color,
    val inkSecondary: Color,
    val inkMuted: Color,
    val terracotta: Color,
    val sage: Color,
    val sageContainer: Color,
    val error: Color,
    val errorContainer: Color,
)

/**
 * Quiet "paper" palette used by the Reading module (ADR-020).
 * Extended in Phase 2 with Paper / Light / Sepia / Dark reader surface styles.
 */
internal object ReadingPalette {
    val Paper = Color(0xFFF5F1E8)
    val Card = Color(0xFFFFFDF8)
    val Elevated = Color(0xFFEFE9DC)
    val Border = Color(0xFFE4DED1)
    val Sand = Color(0xFFF3E2C7)
    val Clay = Color(0xFFF0D4C6)
    val Ink = Color(0xFF1E2A3A)
    val InkSecondary = Color(0xFF515B68)
    val InkMuted = Color(0xFF5A636F)
    val Terracotta = Color(0xFFA3452A)
    val Sage = Color(0xFF3F6A4A)
    val SageContainer = Color(0xFFE3ECE2)
    val Error = Color(0xFFA63D2A)
    val ErrorContainer = Color(0xFFF7E3DD)
}

internal fun ReadingSurfaceStyle.tokens(): ReadingSurfaceTokens = when (this) {
    ReadingSurfaceStyle.PAPER -> ReadingSurfaceTokens(
        paper = ReadingPalette.Paper,
        card = ReadingPalette.Card,
        elevated = ReadingPalette.Elevated,
        border = ReadingPalette.Border,
        sand = ReadingPalette.Sand,
        clay = ReadingPalette.Clay,
        ink = ReadingPalette.Ink,
        inkSecondary = ReadingPalette.InkSecondary,
        inkMuted = ReadingPalette.InkMuted,
        terracotta = ReadingPalette.Terracotta,
        sage = ReadingPalette.Sage,
        sageContainer = ReadingPalette.SageContainer,
        error = ReadingPalette.Error,
        errorContainer = ReadingPalette.ErrorContainer,
    )
    ReadingSurfaceStyle.LIGHT -> ReadingSurfaceTokens(
        paper = Color(0xFFF8F9FB),
        card = Color(0xFFFFFFFF),
        elevated = Color(0xFFF0F2F6),
        border = Color(0xFFE1E5EC),
        sand = Color(0xFFFDE6CE),
        clay = Color(0xFFFCE0D5),
        ink = Color(0xFF171D26),
        inkSecondary = Color(0xFF4B5563),
        inkMuted = Color(0xFF6B7280),
        terracotta = Color(0xFFB5472A),
        sage = Color(0xFF2E6B47),
        sageContainer = Color(0xFFE4F2E9),
        error = Color(0xFFB42318),
        errorContainer = Color(0xFFFEE4E2),
    )
    ReadingSurfaceStyle.SEPIA -> ReadingSurfaceTokens(
        paper = Color(0xFFF3E7CE),
        card = Color(0xFFFAF2E1),
        elevated = Color(0xFFEAD9BA),
        border = Color(0xFFDBC5A0),
        sand = Color(0xFFE7CA9B),
        clay = Color(0xFFE7C2AC),
        ink = Color(0xFF36281A),
        inkSecondary = Color(0xFF624E3B),
        inkMuted = Color(0xFF745F4A),
        terracotta = Color(0xFF9C3E24),
        sage = Color(0xFF3D6644),
        sageContainer = Color(0xFFDFE8D7),
        error = Color(0xFFA03624),
        errorContainer = Color(0xFFF3D8CE),
    )
    ReadingSurfaceStyle.DARK -> ReadingSurfaceTokens(
        paper = Color(0xFF13161E),
        card = Color(0xFF1B1F2A),
        elevated = Color(0xFF242937),
        border = Color(0xFF31384A),
        sand = Color(0xFF4A3B28),
        clay = Color(0xFF4A2E26),
        ink = Color(0xFFECEFF4),
        inkSecondary = Color(0xFFA8B3C5),
        inkMuted = Color(0xFF8894A8),
        terracotta = Color(0xFFE57C5C),
        sage = Color(0xFF68B684),
        sageContainer = Color(0xFF1E3527),
        error = Color(0xFFF07167),
        errorContainer = Color(0xFF3B1F21),
    )
}

internal val LocalReadingTokens = staticCompositionLocalOf { ReadingSurfaceStyle.PAPER.tokens() }

internal fun ReadingSurfaceTokens.toFluentColors() = FluentColors(
    surfaceBase = paper,
    surfaceCard = card,
    surfaceElevated = elevated,
    surfaceBorder = border,
    surfaceHighlight = sand,
    accentPrimary = terracotta,
    accentSecondary = ink,
    accentTertiary = sage,
    accentGlow = terracotta.copy(alpha = 0.14f),
    accentGradient = Brush.horizontalGradient(listOf(terracotta, terracotta)),
    textPrimary = ink,
    textSecondary = inkSecondary,
    textMuted = inkMuted,
    success = sage,
    error = error,
)

internal val ReadingFluentColors = ReadingSurfaceStyle.PAPER.tokens().toFluentColors()

/** Typography used for long-form reading. System serif keeps the app offline and asset-free. */
internal object ReadingType {
    val Display = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold, fontSize = 34.sp, lineHeight = 42.sp, letterSpacing = (-0.3).sp)
    val Title = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold, fontSize = 26.sp, lineHeight = 34.sp)
    val CardTitle = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold, fontSize = 19.sp, lineHeight = 25.sp)
    val Heading = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 30.sp)
    val Body = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Normal, fontSize = 20.sp, lineHeight = 34.sp)
    val Eyebrow = TextStyle(fontWeight = FontWeight.Bold, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 1.sp)
    val Meta = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp)

    fun bodyForSize(fontSizeSp: Float): TextStyle = Body.copy(
        fontSize = fontSizeSp.sp,
        lineHeight = (fontSizeSp * 1.68f).sp,
    )
}

@Composable
internal fun ReadingTheme(
    style: ReadingSurfaceStyle = ReadingSurfaceStyle.PAPER,
    content: @Composable () -> Unit,
) {
    val tokens = style.tokens()
    CompositionLocalProvider(LocalReadingTokens provides tokens) {
        FluentTheme(colors = tokens.toFluentColors(), content = content)
    }
}
