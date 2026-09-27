package com.seanora.fluentai.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class FluentSpacing(
    val none: Dp = 0.dp,
    val xxs: Dp = 2.dp,
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 12.dp,
    val lg: Dp = 16.dp,
    val xl: Dp = 24.dp,
    val xxl: Dp = 32.dp,
    val xxxl: Dp = 48.dp,

    // Tablet-aware layout helpers
    val screenHorizontal: Dp = 24.dp,
    val screenVertical: Dp = 20.dp,
    val cardInternal: Dp = 20.dp,
    val sectionSpacing: Dp = 28.dp,
    val itemSpacing: Dp = 12.dp,
)

val LocalFluentSpacing = staticCompositionLocalOf { FluentSpacing() }
