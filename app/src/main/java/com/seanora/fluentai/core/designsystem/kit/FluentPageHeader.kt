package com.seanora.fluentai.core.designsystem.kit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.designsystem.dashboard.DashboardTypography

@Composable
fun FluentPageHeader(
    title: String,
    module: LearningModule,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    eyebrow: String? = null,
    onBack: (() -> Unit)? = null,
    backLabel: String = "Back",
    illustration: Painter? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null
) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (onBack != null || trailing != null) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                if (onBack != null) FluentBackButton(onClick = onBack, label = backLabel, accent = module.accent)
                Spacer(Modifier.weight(1f))
                trailing?.invoke(this)
            }
        }
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = DashboardColors.Surface,
            border = BorderStroke(1.dp, DashboardColors.BorderSoft),
            shadowElevation = 0.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ModuleIconTile(module = module, size = 56.dp, showEmojiBadge = false)
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    eyebrow?.let {
                        Text(it, color = module.accent, style = DashboardTypography.Eyebrow)
                    }
                    Text(
                        title,
                        color = DashboardColors.TextPrimary,
                        style = DashboardTypography.Display,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.semantics { heading() }
                    )
                    subtitle?.let {
                        Text(it, color = DashboardColors.TextSecondary, style = DashboardTypography.Subtitle)
                    }
                }
                if (illustration != null) {
                    Image(
                        painter = illustration,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.padding(start = 16.dp).height(116.dp).widthIn(max = 300.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FluentCompactHeader(
    title: String,
    module: LearningModule,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    backLabel: String = "Back",
    trailing: (@Composable RowScope.() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (onBack != null) FluentBackButton(onClick = onBack, label = backLabel, accent = module.accent)
        ModuleIconTile(module = module, size = 44.dp, showEmojiBadge = false)
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                title,
                color = DashboardColors.TextPrimary,
                style = DashboardTypography.PageTitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.semantics { heading() }
            )
            subtitle?.let {
                Text(it, color = DashboardColors.TextSecondary, style = DashboardTypography.Body, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
        trailing?.invoke(this)
    }
}
