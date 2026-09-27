package com.seanora.fluentai.core.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

object FluentTheme {
    val colors: FluentColors
        @Composable
        @ReadOnlyComposable
        get() = LocalFluentColors.current

    val typography: FluentTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalFluentTypography.current

    val spacing: FluentSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalFluentSpacing.current

    val shapes: FluentShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalFluentShapes.current
}

@Composable
fun FluentTheme(
    colors: FluentColors = FluentColors(),
    typography: FluentTypography = FluentTypography(),
    spacing: FluentSpacing = FluentSpacing(),
    shapes: FluentShapes = FluentShapes(),
    content: @Composable () -> Unit,
) {
    val materialColorScheme = materialColorSchemeFor(colors)

    val materialTypography = Typography(
        displayLarge = typography.displayLarge,
        headlineLarge = typography.headlineLarge,
        headlineMedium = typography.headlineMedium,
        titleLarge = typography.titleLarge,
        titleMedium = typography.titleMedium,
        titleSmall = typography.titleSmall,
        bodyLarge = typography.bodyLarge,
        bodyMedium = typography.bodyMedium,
        bodySmall = typography.bodySmall,
        labelLarge = typography.labelLarge,
        labelMedium = typography.labelMedium,
        labelSmall = typography.labelSmall,
    )

    val materialShapes = Shapes(
        extraSmall = shapes.xs,
        small = shapes.sm,
        medium = shapes.md,
        large = shapes.lg,
        extraLarge = shapes.xl,
    )

    CompositionLocalProvider(
        LocalFluentColors provides colors,
        LocalFluentTypography provides typography,
        LocalFluentSpacing provides spacing,
        LocalFluentShapes provides shapes,
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = materialTypography,
            shapes = materialShapes,
            content = content,
        )
    }
}

internal fun materialColorSchemeFor(colors: FluentColors) =
    if (colors.surfaceBase.luminance() > 0.5f) lightColorScheme(
        primary = colors.accentPrimary,
        onPrimary = Color.White,
        primaryContainer = colors.surfaceElevated,
        onPrimaryContainer = colors.textPrimary,
        secondary = colors.accentSecondary,
        onSecondary = Color.White,
        secondaryContainer = colors.surfaceElevated,
        onSecondaryContainer = colors.textPrimary,
        tertiary = colors.accentTertiary,
        onTertiary = Color.White,
        background = colors.surfaceBase,
        onBackground = colors.textPrimary,
        surface = colors.surfaceCard,
        onSurface = colors.textPrimary,
        surfaceVariant = colors.surfaceElevated,
        onSurfaceVariant = colors.textSecondary,
        outline = colors.surfaceBorder,
        outlineVariant = colors.surfaceBorder,
        error = colors.error,
        onError = Color.White,
    ) else darkColorScheme(
        primary = colors.accentPrimary,
        onPrimary = Color.White,
        primaryContainer = Color(0xFF4A0E23),
        onPrimaryContainer = Color(0xFFFFD9E2),
        secondary = colors.accentSecondary,
        onSecondary = Color(0xFF1E0A10),
        secondaryContainer = Color(0xFF3E1622),
        onSecondaryContainer = Color(0xFFFFD9E2),
        tertiary = colors.accentTertiary,
        onTertiary = Color.White,
        background = colors.surfaceBase,
        onBackground = colors.textPrimary,
        surface = colors.surfaceCard,
        onSurface = colors.textPrimary,
        surfaceVariant = colors.surfaceElevated,
        onSurfaceVariant = colors.textSecondary,
        outline = colors.surfaceBorder,
        outlineVariant = colors.surfaceBorder,
        error = colors.error,
        onError = Color.White,
    )

@OptIn(ExperimentalLayoutApi::class)
@Preview(name = "FluentTheme Preview - Tablet Portrait", widthDp = 800, heightDp = 1280, showBackground = true)
@Composable
private fun FluentThemePreview() {
    FluentTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = FluentTheme.colors.surfaceBase,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(FluentTheme.spacing.screenHorizontal),
                verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.sectionSpacing),
            ) {
                // Header
                Column {
                    Text(
                        text = "FluentAI Design System",
                        style = FluentTheme.typography.headlineLarge,
                        color = FluentTheme.colors.textPrimary,
                    )
                    Text(
                        text = "Tablet-first, calm, premium, dark-first English learning experience",
                        style = FluentTheme.typography.bodyMedium,
                        color = FluentTheme.colors.textSecondary,
                    )
                }

                // Accent Palette & Glow
                Column(verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.sm)) {
                    Text(
                        text = "Brand Accents & Gradients",
                        style = FluentTheme.typography.titleMedium,
                        color = FluentTheme.colors.textPrimary,
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(FluentTheme.spacing.md),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        ColorSwatch(name = "Primary", color = FluentTheme.colors.accentPrimary)
                        ColorSwatch(name = "Secondary", color = FluentTheme.colors.accentSecondary)
                        ColorSwatch(name = "Tertiary", color = FluentTheme.colors.accentTertiary)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .clip(FluentTheme.shapes.md)
                                .background(FluentTheme.colors.accentGradient),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "Accent Gradient",
                                style = FluentTheme.typography.labelMedium,
                                color = Color.White,
                            )
                        }
                    }
                }

                // Surfaces & Layering
                Column(verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.sm)) {
                    Text(
                        text = "Surface Hierarchy",
                        style = FluentTheme.typography.titleMedium,
                        color = FluentTheme.colors.textPrimary,
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(FluentTheme.spacing.md),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        SurfaceCardPreview(
                            title = "Surface Card",
                            color = FluentTheme.colors.surfaceCard,
                            borderColor = FluentTheme.colors.surfaceBorder,
                            modifier = Modifier.weight(1f),
                        )
                        SurfaceCardPreview(
                            title = "Surface Elevated",
                            color = FluentTheme.colors.surfaceElevated,
                            borderColor = FluentTheme.colors.surfaceBorder,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }

                // CEFR Level Progression Indicators
                Column(verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.sm)) {
                    Text(
                        text = "CEFR Level Badges (A2 → C2)",
                        style = FluentTheme.typography.titleMedium,
                        color = FluentTheme.colors.textPrimary,
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(FluentTheme.spacing.sm),
                        verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.sm),
                    ) {
                        CefrBadgePreview(level = "A2 Elementary", color = FluentTheme.colors.cefrA2)
                        CefrBadgePreview(level = "B1 Intermediate", color = FluentTheme.colors.cefrB1)
                        CefrBadgePreview(level = "B2 Upper-Intermediate", color = FluentTheme.colors.cefrB2)
                        CefrBadgePreview(level = "C1 Advanced", color = FluentTheme.colors.cefrC1)
                        CefrBadgePreview(level = "C2 Mastery", color = FluentTheme.colors.cefrC2)
                    }
                }

                // Typography & Learning Elements
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(FluentTheme.shapes.card)
                        .background(FluentTheme.colors.surfaceCard)
                        .border(1.dp, FluentTheme.colors.surfaceBorder, FluentTheme.shapes.card)
                        .padding(FluentTheme.spacing.cardInternal),
                    verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.sm),
                ) {
                    Text(
                        text = "resilient",
                        style = FluentTheme.typography.headlineLarge,
                        color = FluentTheme.colors.textPrimary,
                    )
                    Text(
                        text = "/rɪˈzɪl.i.ənt/",
                        style = FluentTheme.typography.phonetic,
                    )
                    Text(
                        text = "Able to quickly recover from difficulties; tough and adaptable.",
                        style = FluentTheme.typography.bodyLarge,
                        color = FluentTheme.colors.textPrimary,
                    )
                    Spacer(modifier = Modifier.height(FluentTheme.spacing.xs))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Türkçe Karşılığı: ",
                            style = FluentTheme.typography.bodySmall,
                            color = FluentTheme.colors.textMuted,
                        )
                        Text(
                            text = "dayanıklı, çabuk toparlanan",
                            style = FluentTheme.typography.translation,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorSwatch(
    name: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(width = 110.dp, height = 56.dp)
            .clip(FluentTheme.shapes.md)
            .background(color),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = name,
            style = FluentTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
        )
    }
}

@Composable
private fun SurfaceCardPreview(
    title: String,
    color: Color,
    borderColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(FluentTheme.shapes.card)
            .background(color)
            .border(1.dp, borderColor, FluentTheme.shapes.card)
            .padding(FluentTheme.spacing.cardInternal),
    ) {
        Column {
            Text(
                text = title,
                style = FluentTheme.typography.titleSmall,
                color = FluentTheme.colors.textPrimary,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Layered surface for content hierarchy",
                style = FluentTheme.typography.bodySmall,
                color = FluentTheme.colors.textSecondary,
            )
        }
    }
}

@Composable
private fun CefrBadgePreview(
    level: String,
    color: Color,
) {
    Box(
        modifier = Modifier
            .clip(FluentTheme.shapes.badge)
            .background(color.copy(alpha = 0.16f))
            .border(1.dp, color.copy(alpha = 0.4f), FluentTheme.shapes.badge)
            .padding(horizontal = 10.dp, vertical = 5.dp),
    ) {
        Text(
            text = level,
            style = FluentTheme.typography.badge,
            color = color,
        )
    }
}
