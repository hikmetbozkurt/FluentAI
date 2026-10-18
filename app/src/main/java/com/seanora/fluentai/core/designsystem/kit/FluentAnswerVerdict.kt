package com.seanora.fluentai.core.designsystem.kit

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val CorrectColor = Color(0xFF1F9D68)
private val IncorrectColor = Color(0xFFC2416B)

@Composable
fun FluentAnswerVerdict(correct: Boolean, modifier: Modifier = Modifier) {
    var revealed by remember(correct) { mutableStateOf(false) }
    LaunchedEffect(correct) { revealed = true }
    val scale by animateFloatAsState(
        targetValue = if (revealed) 1f else 0.6f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "verdictScale"
    )
    val tint = if (correct) CorrectColor else IncorrectColor
    Box(modifier) {
        Surface(
            modifier = Modifier.graphicsLayer {
                scaleX = scale
                scaleY = scale
            }.semantics { liveRegion = LiveRegionMode.Polite },
            shape = RoundedCornerShape(50),
            color = tint.copy(alpha = 0.12f),
            border = BorderStroke(1.5.dp, tint.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(if (correct) "🎉" else "💡", fontSize = 20.sp)
                Text(if (correct) "Correct!" else "Not quite", color = tint, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
            }
        }
        if (correct) ConfettiBurst(trigger = correct, modifier = Modifier.matchParentSize(), pieceCount = 24)
    }
}
