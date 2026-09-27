package com.seanora.fluentai.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.seanora.fluentai.core.designsystem.theme.FluentTheme

enum class CardVariant {
    Standard,
    Elevated,
    AccentOutlined,
}

@Composable
fun FluentCard(
    modifier: Modifier = Modifier,
    variant: CardVariant = CardVariant.Standard,
    shape: Shape = FluentTheme.shapes.card,
    contentPadding: Dp = FluentTheme.spacing.cardInternal,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val backgroundColor = when (variant) {
        CardVariant.Standard -> FluentTheme.colors.surfaceCard
        CardVariant.Elevated -> FluentTheme.colors.surfaceElevated
        CardVariant.AccentOutlined -> FluentTheme.colors.surfaceCard
    }

    val borderStroke = when (variant) {
        CardVariant.Standard -> BorderStroke(1.dp, FluentTheme.colors.surfaceBorder)
        CardVariant.Elevated -> BorderStroke(1.dp, FluentTheme.colors.surfaceHighlight.copy(alpha = 0.5f))
        CardVariant.AccentOutlined -> BorderStroke(1.dp, FluentTheme.colors.accentPrimary.copy(alpha = 0.5f))
    }

    val clickableModifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier
    }

    Surface(
        modifier = modifier
            .clip(shape)
            .then(clickableModifier),
        shape = shape,
        color = backgroundColor,
        border = borderStroke,
    ) {
        Box(
            modifier = Modifier.padding(contentPadding),
            content = content,
        )
    }
}
