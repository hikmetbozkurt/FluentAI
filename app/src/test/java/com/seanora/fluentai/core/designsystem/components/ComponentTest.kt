package com.seanora.fluentai.core.designsystem.components

import androidx.compose.ui.graphics.Color
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.designsystem.dashboard.LingoPinkFluentColors
import org.junit.Assert.assertEquals
import org.junit.Test

class ComponentTest {

    @Test
    fun cefrLevel_fromString_parsesCorrectly() {
        assertEquals(CefrLevel.A2, CefrLevel.fromString("a2"))
        assertEquals(CefrLevel.A2, CefrLevel.fromString("A2_ELEMENTARY"))
        assertEquals(CefrLevel.B1, CefrLevel.fromString("b1"))
        assertEquals(CefrLevel.B2, CefrLevel.fromString("B2"))
        assertEquals(CefrLevel.C1, CefrLevel.fromString("c1"))
        assertEquals(CefrLevel.C2, CefrLevel.fromString("C2"))
        // Fallback
        assertEquals(CefrLevel.B1, CefrLevel.fromString("INVALID"))
    }

    @Test
    fun cefrLevel_labelsAreAccurate() {
        assertEquals("A2", CefrLevel.A2.label)
        assertEquals("B1", CefrLevel.B1.label)
        assertEquals("B2", CefrLevel.B2.label)
        assertEquals("C1", CefrLevel.C1.label)
        assertEquals("C2", CefrLevel.C2.label)
    }

    @Test
    fun buttonVariants_exist() {
        val variants = ButtonVariant.values()
        assertEquals(4, variants.size)
    }

    @Test
    fun cardVariants_exist() {
        val variants = CardVariant.values()
        assertEquals(3, variants.size)
    }

    @Test
    fun lightThemeButtonRoles_keepReadableForegroundsForEveryState() {
        assertEquals(
            Color.White,
            resolveFluentButtonColors(LingoPinkFluentColors, ButtonVariant.Primary, enabled = true).content,
        )
        listOf(ButtonVariant.Secondary, ButtonVariant.Tonal, ButtonVariant.Outlined).forEach { variant ->
            assertEquals(
                "$variant must use a dark foreground on a light surface",
                if (variant == ButtonVariant.Tonal) DashboardColors.PrimaryPinkDark else DashboardColors.TextPrimary,
                resolveFluentButtonColors(LingoPinkFluentColors, variant, enabled = true).content,
            )
        }
        ButtonVariant.entries.forEach { variant ->
            assertEquals(
                LingoPinkFluentColors.textMuted,
                resolveFluentButtonColors(LingoPinkFluentColors, variant, enabled = false).content,
            )
        }
    }

    @Test
    fun lightThemeSelectableRoles_keepSelectedAndUnselectedTextReadable() {
        val selected = resolveSelectableControlColors(LingoPinkFluentColors, selected = true, enabled = true)
        val unselected = resolveSelectableControlColors(LingoPinkFluentColors, selected = false, enabled = true)
        val disabled = resolveSelectableControlColors(LingoPinkFluentColors, selected = false, enabled = false)

        assertEquals(LingoPinkFluentColors.accentSecondary, selected.container)
        assertEquals(Color.White, selected.content)
        assertEquals(LingoPinkFluentColors.surfaceCard, unselected.container)
        assertEquals(LingoPinkFluentColors.textPrimary, unselected.content)
        assertEquals(LingoPinkFluentColors.textMuted, disabled.content)
    }
}
