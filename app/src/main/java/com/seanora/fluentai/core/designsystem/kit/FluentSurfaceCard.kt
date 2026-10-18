package com.seanora.fluentai.core.designsystem.kit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors

@Composable
fun FluentSurfaceCard(
    modifier: Modifier = Modifier,
    accent: Color? = null,
    emphasized: Boolean = false,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    contentSpacing: Dp = 10.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(22.dp)
    val borderColor = when {
        emphasized && accent != null -> accent.copy(alpha = 0.55f)
        else -> DashboardColors.BorderSoft
    }
    val background = if (emphasized && accent != null) accent.copy(alpha = 0.07f) else DashboardColors.Surface
    val body: @Composable () -> Unit = {
        Row(Modifier.height(IntrinsicSize.Min)) {
            if (accent != null) {
                Box(Modifier.width(6.dp).fillMaxHeight().background(accent))
            }
            Column(
                modifier = Modifier.weight(1f).padding(contentPadding),
                verticalArrangement = Arrangement.spacedBy(contentSpacing),
                content = content
            )
        }
    }
    if (onClick != null) {
        Surface(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            color = background,
            border = BorderStroke(1.5.dp, borderColor),
            shadowElevation = if (emphasized) 6.dp else 3.dp,
            content = body
        )
    } else {
        Surface(
            modifier = modifier,
            shape = shape,
            color = background,
            border = BorderStroke(1.5.dp, borderColor),
            shadowElevation = if (emphasized) 6.dp else 3.dp,
            content = body
        )
    }
}
