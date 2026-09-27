package com.seanora.fluentai.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Palette definitions — Calm, premium, dark-first with vibrant magenta/rose accents
val SurfaceBase = Color(0xFF0E0F14)
val SurfaceCard = Color(0xFF161820)
val SurfaceElevated = Color(0xFF20232E)
val SurfaceBorder = Color(0xFF2B2F3D)
val SurfaceHighlight = Color(0xFF363B4C)

val AccentPrimary = Color(0xFFFF2E74)
val AccentSecondary = Color(0xFFFB7185)
val AccentTertiary = Color(0xFF8B5CF6)
val AccentGlow = Color(0x33FF2E74)

val AccentGradient = Brush.horizontalGradient(
    colors = listOf(AccentPrimary, AccentTertiary)
)

val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)

val SemanticSuccess = Color(0xFF10B981)
val SemanticWarning = Color(0xFFF59E0B)
val SemanticError = Color(0xFFEF4444)
val SemanticInfo = Color(0xFF38BDF8)

// CEFR Level Color Indicators
val CefrA2Color = Color(0xFF06B6D4) // Cyan
val CefrB1Color = Color(0xFF3B82F6) // Blue
val CefrB2Color = Color(0xFF8B5CF6) // Purple
val CefrC1Color = Color(0xFFEC4899) // Pink
val CefrC2Color = Color(0xFFF43F5E) // Rose

@Immutable
data class FluentColors(
    val surfaceBase: Color = SurfaceBase,
    val surfaceCard: Color = SurfaceCard,
    val surfaceElevated: Color = SurfaceElevated,
    val surfaceBorder: Color = SurfaceBorder,
    val surfaceHighlight: Color = SurfaceHighlight,

    val accentPrimary: Color = AccentPrimary,
    val accentSecondary: Color = AccentSecondary,
    val accentTertiary: Color = AccentTertiary,
    val accentGlow: Color = AccentGlow,
    val accentGradient: Brush = AccentGradient,

    val textPrimary: Color = TextPrimary,
    val textSecondary: Color = TextSecondary,
    val textMuted: Color = TextMuted,

    val success: Color = SemanticSuccess,
    val warning: Color = SemanticWarning,
    val error: Color = SemanticError,
    val info: Color = SemanticInfo,

    val cefrA2: Color = CefrA2Color,
    val cefrB1: Color = CefrB1Color,
    val cefrB2: Color = CefrB2Color,
    val cefrC1: Color = CefrC1Color,
    val cefrC2: Color = CefrC2Color,
)

val LocalFluentColors = staticCompositionLocalOf { FluentColors() }
