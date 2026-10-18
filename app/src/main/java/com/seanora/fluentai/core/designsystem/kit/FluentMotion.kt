package com.seanora.fluentai.core.designsystem.kit

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

object FluentMotion {
    const val ENTRANCE_DURATION_MS = 420
    const val STAGGER_STEP_MS = 70
    const val PRESSED_SCALE = 0.97f
    val ENTRANCE_OFFSET = 24.dp
}

fun Modifier.pressScale(
    interactionSource: InteractionSource,
    pressedScale: Float = FluentMotion.PRESSED_SCALE
): Modifier = composed {
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) pressedScale else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "pressScale"
    )
    graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}

fun Modifier.entranceReveal(delayMillis: Int = 0): Modifier = composed {
    var started by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { started = true }
    val progress by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(FluentMotion.ENTRANCE_DURATION_MS, delayMillis, FastOutSlowInEasing),
        label = "entranceReveal"
    )
    graphicsLayer {
        alpha = progress
        translationY = (1f - progress) * FluentMotion.ENTRANCE_OFFSET.toPx()
    }
}

fun staggerDelay(index: Int): Int = index.coerceAtLeast(0) * FluentMotion.STAGGER_STEP_MS
