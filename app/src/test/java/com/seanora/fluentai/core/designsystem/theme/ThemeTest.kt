package com.seanora.fluentai.core.designsystem.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.designsystem.dashboard.LingoPinkFluentColors
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ThemeTest {

    @Test
    fun lingoPinkFluentColors_adaptLegacyComponentsToCurrentLightPalette() {
        assertEquals(DashboardColors.BackgroundPrimary, LingoPinkFluentColors.surfaceBase)
        assertEquals(DashboardColors.Surface, LingoPinkFluentColors.surfaceCard)
        assertEquals(DashboardColors.SurfacePink, LingoPinkFluentColors.surfaceElevated)
        assertEquals(DashboardColors.BorderSoft, LingoPinkFluentColors.surfaceBorder)
        assertEquals(DashboardColors.PrimaryPink, LingoPinkFluentColors.accentPrimary)
        assertEquals(DashboardColors.TextPrimary, LingoPinkFluentColors.textPrimary)
    }

    @Test
    fun lingoPinkPalette_usesReadableLightMaterialForegrounds() {
        val scheme = materialColorSchemeFor(LingoPinkFluentColors)

        assertEquals(DashboardColors.TextPrimary, scheme.onSurface)
        assertEquals(DashboardColors.TextSecondary, scheme.onSurfaceVariant)
        assertEquals(Color.White, scheme.onPrimary)
        assertEquals(DashboardColors.BackgroundPrimary, scheme.background)
    }

    @Test
    fun fluentColors_defaultsAreValidAndDistinct() {
        val colors = FluentColors()

        // Verify surfaces are dark
        assertEquals(SurfaceBase, colors.surfaceBase)
        assertEquals(SurfaceCard, colors.surfaceCard)
        assertEquals(SurfaceElevated, colors.surfaceElevated)
        assertNotEquals(colors.surfaceBase, colors.surfaceCard)
        assertNotEquals(colors.surfaceCard, colors.surfaceElevated)

        // Verify brand accents
        assertEquals(AccentPrimary, colors.accentPrimary)
        assertEquals(AccentSecondary, colors.accentSecondary)
        assertEquals(AccentTertiary, colors.accentTertiary)
        assertNotNull(colors.accentGradient)

        // Verify CEFR 5-level scale exists
        assertEquals(CefrA2Color, colors.cefrA2)
        assertEquals(CefrB1Color, colors.cefrB1)
        assertEquals(CefrB2Color, colors.cefrB2)
        assertEquals(CefrC1Color, colors.cefrC1)
        assertEquals(CefrC2Color, colors.cefrC2)

        // Verify semantic status colors
        assertEquals(Color(0xFF10B981), colors.success)
        assertEquals(Color(0xFFEF4444), colors.error)
    }

    @Test
    fun fluentSpacing_isMonotonicallyIncreasing() {
        val spacing = FluentSpacing()

        assertTrue(spacing.none < spacing.xxs)
        assertTrue(spacing.xxs < spacing.xs)
        assertTrue(spacing.xs < spacing.sm)
        assertTrue(spacing.sm < spacing.md)
        assertTrue(spacing.md < spacing.lg)
        assertTrue(spacing.lg < spacing.xl)
        assertTrue(spacing.xl < spacing.xxl)
        assertTrue(spacing.xxl < spacing.xxxl)

        assertEquals(24.dp, spacing.screenHorizontal)
        assertEquals(20.dp, spacing.cardInternal)
    }

    @Test
    fun fluentTypography_hasConsistentScaleAndSpecialStyles() {
        val typography = FluentTypography()

        assertTrue(typography.displayLarge.fontSize > typography.headlineLarge.fontSize)
        assertTrue(typography.headlineLarge.fontSize > typography.titleLarge.fontSize)
        assertTrue(typography.titleLarge.fontSize > typography.bodyLarge.fontSize)
        assertTrue(typography.bodyLarge.fontSize > typography.bodySmall.fontSize)

        // Verify learning-specific styles are defined
        assertNotNull(typography.phonetic)
        assertNotNull(typography.translation)
        assertNotNull(typography.badge)
    }

    @Test
    fun fluentShapes_definesExpectedCorners() {
        val shapes = FluentShapes()

        assertNotNull(shapes.xs)
        assertNotNull(shapes.sm)
        assertNotNull(shapes.md)
        assertNotNull(shapes.lg)
        assertNotNull(shapes.xl)
        assertNotNull(shapes.pill)
        assertNotNull(shapes.card)
        assertNotNull(shapes.badge)
    }
}
