package com.seanora.fluentai.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.seanora.fluentai.core.designsystem.theme.FluentTheme

enum class CefrLevel(val label: String) {
    A2("A2"),
    B1("B1"),
    B2("B2"),
    C1("C1"),
    C2("C2");

    companion object {
        fun fromString(value: String): CefrLevel {
            return when (value.uppercase().trim().take(2)) {
                "A2" -> A2
                "B1" -> B1
                "B2" -> B2
                "C1" -> C1
                "C2" -> C2
                else -> B1 // Default fallback
            }
        }
    }
}

enum class BadgeSize {
    Compact,
    Standard,
}

@Composable
fun LevelBadge(
    level: CefrLevel,
    modifier: Modifier = Modifier,
    size: BadgeSize = BadgeSize.Standard,
    customText: String? = null,
) {
    val levelColor = when (level) {
        CefrLevel.A2 -> FluentTheme.colors.cefrA2
        CefrLevel.B1 -> FluentTheme.colors.cefrB1
        CefrLevel.B2 -> FluentTheme.colors.cefrB2
        CefrLevel.C1 -> FluentTheme.colors.cefrC1
        CefrLevel.C2 -> FluentTheme.colors.cefrC2
    }

    val horizontalPadding = if (size == BadgeSize.Compact) 6.dp else 10.dp
    val verticalPadding = if (size == BadgeSize.Compact) 2.dp else 4.dp
    val textStyle = if (size == BadgeSize.Compact) {
        FluentTheme.typography.badge.copy(fontSize = FluentTheme.typography.labelSmall.fontSize)
    } else {
        FluentTheme.typography.badge
    }

    Box(
        modifier = modifier
            .clip(FluentTheme.shapes.badge)
            .background(levelColor.copy(alpha = 0.16f))
            .border(1.dp, levelColor.copy(alpha = 0.45f), FluentTheme.shapes.badge)
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
    ) {
        Text(
            text = customText ?: level.label,
            style = textStyle,
            color = levelColor,
        )
    }
}

@Composable
fun LevelBadge(
    levelString: String,
    modifier: Modifier = Modifier,
    size: BadgeSize = BadgeSize.Standard,
    customText: String? = null,
) {
    LevelBadge(
        level = CefrLevel.fromString(levelString),
        modifier = modifier,
        size = size,
        customText = customText,
    )
}
