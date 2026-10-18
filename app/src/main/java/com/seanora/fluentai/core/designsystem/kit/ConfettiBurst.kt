package com.seanora.fluentai.core.designsystem.kit

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class ConfettiPiece(
    val angle: Float,
    val speed: Float,
    val size: Float,
    val spin: Float,
    val color: Color,
    val isCircle: Boolean
)

@Composable
fun ConfettiBurst(
    trigger: Any?,
    modifier: Modifier = Modifier,
    pieceCount: Int = 48,
    durationMillis: Int = 1_600,
    colors: List<Color> = LearningModule.entries.map { it.accent }
) {
    if (trigger == null) return
    val progress = remember(trigger) { Animatable(0f) }
    val pieces = remember(trigger) { createPieces(trigger.hashCode(), pieceCount, colors) }
    LaunchedEffect(trigger) {
        progress.snapTo(0f)
        progress.animateTo(1f, tween(durationMillis, easing = LinearEasing))
    }
    Canvas(modifier) {
        val t = progress.value
        if (t >= 1f) return@Canvas
        val origin = Offset(size.width / 2f, size.height * 0.45f)
        val reach = size.minDimension * 0.75f
        val gravity = size.height * 0.9f
        pieces.forEach { piece ->
            val distance = piece.speed * reach * t
            val position = Offset(
                x = origin.x + cos(piece.angle) * distance,
                y = origin.y + sin(piece.angle) * distance + gravity * t * t
            )
            val alpha = (1f - t).coerceIn(0f, 1f)
            val pieceSize = piece.size * size.minDimension
            if (piece.isCircle) {
                drawCircle(piece.color.copy(alpha = alpha), radius = pieceSize / 2f, center = position)
            } else {
                rotate(degrees = piece.spin * t * 720f, pivot = position) {
                    drawRect(
                        color = piece.color.copy(alpha = alpha),
                        topLeft = Offset(position.x - pieceSize / 2f, position.y - pieceSize / 4f),
                        size = Size(pieceSize, pieceSize / 2f)
                    )
                }
            }
        }
    }
}

private fun createPieces(seed: Int, count: Int, colors: List<Color>): List<ConfettiPiece> {
    val random = Random(seed)
    return List(count) {
        ConfettiPiece(
            angle = (random.nextFloat() * PI * 2).toFloat(),
            speed = 0.35f + random.nextFloat() * 0.65f,
            size = 0.018f + random.nextFloat() * 0.022f,
            spin = random.nextFloat() * 2f - 1f,
            color = colors[random.nextInt(colors.size)],
            isCircle = random.nextBoolean()
        )
    }
}
