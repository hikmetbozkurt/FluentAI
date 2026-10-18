package com.seanora.fluentai.core.designsystem.kit

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    Column(modifier.fillMaxWidth().entranceReveal(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        if (onBack != null || trailing != null) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                if (onBack != null) FluentBackButton(onClick = onBack, label = backLabel, accent = module.accent)
                Spacer(Modifier.weight(1f))
                trailing?.invoke(this)
            }
        }
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = DashboardColors.Surface,
            border = BorderStroke(1.5.dp, module.accent.copy(alpha = 0.22f)),
            shadowElevation = 4.dp
        ) {
            Box(Modifier.background(module.heroBrush)) {
                DecorativeBackdrop(accent = module.accent, modifier = Modifier.matchParentSize())
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 22.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ModuleIconTile(module = module, size = 64.dp)
                    Spacer(Modifier.width(18.dp))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        eyebrow?.let {
                            Text(it.uppercase(), color = module.accent, style = DashboardTypography.Eyebrow)
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
                            modifier = Modifier.padding(start = 12.dp).height(132.dp).widthIn(max = 340.dp)
                        )
                    } else {
                        FloatingEmoji(module.emoji)
                    }
                }
            }
        }
    }
}

@Composable
private fun FloatingEmoji(emoji: String) {
    val transition = rememberInfiniteTransition(label = "floatingEmoji")
    val offset by transition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(tween(1_800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "floatingEmojiOffset"
    )
    Text(
        emoji,
        fontSize = 64.sp,
        modifier = Modifier
            .padding(start = 12.dp)
            .graphicsLayer { translationY = offset.dp.toPx() }
    )
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
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
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
