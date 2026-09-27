package com.seanora.fluentai.core.designsystem.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

@Immutable
data class FluentShapes(
    val xs: CornerBasedShape = RoundedCornerShape(4.dp),
    val sm: CornerBasedShape = RoundedCornerShape(8.dp),
    val md: CornerBasedShape = RoundedCornerShape(12.dp),
    val lg: CornerBasedShape = RoundedCornerShape(16.dp),
    val xl: CornerBasedShape = RoundedCornerShape(24.dp),
    val pill: CornerBasedShape = CircleShape,
    val card: CornerBasedShape = RoundedCornerShape(16.dp),
    val badge: CornerBasedShape = RoundedCornerShape(6.dp),
)

val LocalFluentShapes = staticCompositionLocalOf { FluentShapes() }
