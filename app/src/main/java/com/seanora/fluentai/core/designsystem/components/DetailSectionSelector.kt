package com.seanora.fluentai.core.designsystem.components

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.seanora.fluentai.core.designsystem.theme.FluentTheme
import com.seanora.fluentai.core.designsystem.theme.FluentColors

internal data class SelectableControlColorRoles(
    val container: Color,
    val content: Color,
    val border: Color,
)

internal fun resolveSelectableControlColors(
    colors: FluentColors,
    selected: Boolean,
    enabled: Boolean,
): SelectableControlColorRoles = when {
    !enabled -> SelectableControlColorRoles(
        colors.surfaceElevated.copy(alpha = 0.65f),
        colors.textMuted,
        colors.surfaceBorder.copy(alpha = 0.55f),
    )
    selected -> SelectableControlColorRoles(colors.accentSecondary, Color.White, colors.accentSecondary)
    else -> SelectableControlColorRoles(colors.surfaceCard, colors.textPrimary, colors.surfaceBorder)
}

/** Compact, horizontally adaptive section navigation for fixed Learn detail panes. */
@Composable
fun DetailSectionSelector(
    sections: List<String>,
    selectedSection: String,
    onSectionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .selectableGroup()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        sections.forEach { section ->
            val selected = section == selectedSection
            FluentSelectableChip(
                text = section,
                selected = selected,
                onClick = { onSectionSelected(section) },
                role = Role.Tab,
            )
        }
    }
}

/** Shared readable light/dark selectable control used by tabs and compact filters. */
@Composable
fun FluentSelectableChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    role: Role = Role.Checkbox,
) {
    val colorRoles = resolveSelectableControlColors(FluentTheme.colors, selected, enabled)
    Surface(
        color = colorRoles.container,
        contentColor = colorRoles.content,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .border(1.dp, colorRoles.border, RoundedCornerShape(12.dp))
            .selectable(
                selected = selected,
                enabled = enabled,
                onClick = onClick,
                role = role,
            ),
    ) {
        Text(
            text = text,
            color = colorRoles.content,
            style = FluentTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
        )
    }
}
