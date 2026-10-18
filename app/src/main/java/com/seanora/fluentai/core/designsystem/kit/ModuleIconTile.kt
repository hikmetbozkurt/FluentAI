package com.seanora.fluentai.core.designsystem.kit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ModuleIconTile(
    module: LearningModule,
    modifier: Modifier = Modifier,
    size: Dp = 52.dp,
    showEmojiBadge: Boolean = true
) {
    IconTile(
        icon = module.icon,
        accent = module.accent,
        container = module.container,
        modifier = modifier,
        size = size,
        badgeEmoji = module.emoji.takeIf { showEmojiBadge }
    )
}

@Composable
fun IconTile(
    icon: ImageVector,
    accent: Color,
    container: Color,
    modifier: Modifier = Modifier,
    size: Dp = 52.dp,
    badgeEmoji: String? = null
) {
    Box(modifier.size(size + if (badgeEmoji != null) 6.dp else 0.dp)) {
        Box(
            modifier = Modifier
                .size(size)
                .background(container, RoundedCornerShape(size * 0.32f))
                .border(1.5.dp, accent.copy(alpha = 0.28f), RoundedCornerShape(size * 0.32f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(size * 0.52f))
        }
        if (badgeEmoji != null) {
            EmojiBadge(
                emoji = badgeEmoji,
                modifier = Modifier.align(Alignment.BottomEnd).offset(x = 2.dp, y = 2.dp),
                size = size * 0.46f
            )
        }
    }
}

@Composable
fun EmojiBadge(
    emoji: String,
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    background: Color = Color.White
) {
    Box(
        modifier = modifier
            .size(size)
            .background(background, CircleShape)
            .border(1.dp, Color(0x1A000000), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(emoji, fontSize = (size.value * 0.55f).sp)
    }
}
