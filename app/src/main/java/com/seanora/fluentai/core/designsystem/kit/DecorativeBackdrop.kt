package com.seanora.fluentai.core.designsystem.kit

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun DecorativeBackdrop(accent: Color, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        drawSoftBlob(accent, center = Offset(size.width * 0.92f, size.height * 0.1f), radiusFactor = 0.42f, alpha = 0.16f)
        drawSoftBlob(accent, center = Offset(size.width * 0.72f, size.height * 1.05f), radiusFactor = 0.3f, alpha = 0.10f)
        drawCircle(
            color = accent.copy(alpha = 0.22f),
            radius = size.minDimension * 0.18f,
            center = Offset(size.width * 0.82f, size.height * 0.55f),
            style = Stroke(width = size.minDimension * 0.012f)
        )
        SPARKLES.forEach { (x, y, r) ->
            drawCircle(accent.copy(alpha = 0.35f), radius = size.minDimension * r, center = Offset(size.width * x, size.height * y))
        }
    }
}

private fun DrawScope.drawSoftBlob(color: Color, center: Offset, radiusFactor: Float, alpha: Float) {
    drawCircle(color = color.copy(alpha = alpha), radius = size.maxDimension * radiusFactor, center = center)
}

private val SPARKLES = listOf(
    Triple(0.62f, 0.18f, 0.012f),
    Triple(0.68f, 0.78f, 0.018f),
    Triple(0.97f, 0.62f, 0.010f),
    Triple(0.55f, 0.48f, 0.008f)
)
