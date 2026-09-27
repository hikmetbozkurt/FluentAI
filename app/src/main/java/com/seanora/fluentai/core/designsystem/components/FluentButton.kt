package com.seanora.fluentai.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.seanora.fluentai.core.designsystem.theme.FluentTheme
import com.seanora.fluentai.core.designsystem.theme.FluentColors

enum class ButtonVariant {
    Primary,
    Secondary,
    Tonal,
    Outlined,
}

internal data class ControlColorRoles(
    val container: Color,
    val content: Color,
    val border: Color?,
)

internal fun resolveFluentButtonColors(
    colors: FluentColors,
    variant: ButtonVariant,
    enabled: Boolean,
): ControlColorRoles {
    val container = when (variant) {
        ButtonVariant.Primary -> if (enabled) colors.accentPrimary else colors.surfaceElevated
        ButtonVariant.Secondary -> if (enabled) colors.surfaceElevated else colors.surfaceCard
        ButtonVariant.Tonal -> if (enabled) colors.accentPrimary.copy(alpha = 0.15f) else colors.surfaceElevated.copy(alpha = 0.5f)
        ButtonVariant.Outlined -> Color.Transparent
    }
    val content = when {
        !enabled -> colors.textMuted
        variant == ButtonVariant.Primary -> Color.White
        variant == ButtonVariant.Tonal -> colors.accentSecondary
        else -> colors.textPrimary
    }
    val border = when (variant) {
        ButtonVariant.Primary, ButtonVariant.Tonal -> null
        ButtonVariant.Secondary -> colors.surfaceBorder
        ButtonVariant.Outlined -> if (enabled) colors.surfaceBorder else colors.surfaceBorder.copy(alpha = 0.5f)
    }
    return ControlColorRoles(container, content, border)
}

@Composable
fun FluentButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Primary,
    enabled: Boolean = true,
    shape: Shape = FluentTheme.shapes.md,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    text: String,
) {
    val colorRoles = resolveFluentButtonColors(FluentTheme.colors, variant, enabled)

    OutlinedButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = 48.dp),
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = colorRoles.container,
            contentColor = colorRoles.content,
            disabledContainerColor = colorRoles.container,
            disabledContentColor = colorRoles.content,
        ),
        border = colorRoles.border?.let { BorderStroke(1.dp, it) },
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(FluentTheme.spacing.sm),
        ) {
            leadingIcon?.invoke()
            Text(
                text = text,
                style = FluentTheme.typography.labelLarge,
                color = colorRoles.content,
            )
            trailingIcon?.invoke()
        }
    }
}
