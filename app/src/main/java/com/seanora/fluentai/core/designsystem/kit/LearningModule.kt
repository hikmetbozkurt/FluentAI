package com.seanora.fluentai.core.designsystem.kit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Headphones
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.RecordVoiceOver
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material.icons.rounded.RocketLaunch
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Spellcheck
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class LearningModule(
    val emoji: String,
    val icon: ImageVector,
    val accent: Color,
    val container: Color,
    val highlight: Color
) {
    HOME("🏠", Icons.Rounded.Home, Color(0xFFEC4C92), Color(0xFFFFE4EF), Color(0xFFFFB8D5)),
    LEARN("🎓", Icons.Rounded.School, Color(0xFFD9467F), Color(0xFFFFE6F0), Color(0xFFFFC2DA)),
    VOCABULARY("📚", Icons.AutoMirrored.Rounded.MenuBook, Color(0xFFE23E86), Color(0xFFFFE3EF), Color(0xFFFFB3D2)),
    GRAMMAR("🧩", Icons.Rounded.Spellcheck, Color(0xFF7A55E8), Color(0xFFEFE8FF), Color(0xFFCDBBFF)),
    READING("📖", Icons.Rounded.AutoStories, Color(0xFFE56A2C), Color(0xFFFFEBDD), Color(0xFFFFC9A6)),
    LISTENING("🎧", Icons.Rounded.Headphones, Color(0xFF1F9D68), Color(0xFFE2F6EC), Color(0xFFA9E5C8)),
    SPEAKING("🎙️", Icons.Rounded.RecordVoiceOver, Color(0xFF2F74E0), Color(0xFFE6F0FF), Color(0xFFB3CEFF)),
    REVIEW("🔁", Icons.Rounded.Replay, Color(0xFFC98A00), Color(0xFFFFF4D6), Color(0xFFFFDD85)),
    PROGRESS("📈", Icons.AutoMirrored.Rounded.TrendingUp, Color(0xFF0E9594), Color(0xFFDDF6F5), Color(0xFF9EE3E1)),
    PROFILE("🙋", Icons.Rounded.Person, Color(0xFF5B5BD6), Color(0xFFE9E9FF), Color(0xFFC3C3FF)),
    SETTINGS("⚙️", Icons.Rounded.Settings, Color(0xFF5E6B80), Color(0xFFECEFF5), Color(0xFFC9D1DE)),
    ONBOARDING("🚀", Icons.Rounded.RocketLaunch, Color(0xFFEC4C92), Color(0xFFFFE4EF), Color(0xFFFFB8D5));

    val heroBrush: Brush get() = Brush.linearGradient(listOf(container, highlight.copy(alpha = 0.55f)))

    val accentBrush: Brush get() = Brush.horizontalGradient(listOf(accent, accent.copy(alpha = 0.78f)))

    companion object {
        fun forDomain(domain: String): LearningModule = when (domain.lowercase()) {
            "vocabulary" -> VOCABULARY
            "grammar" -> GRAMMAR
            "reading" -> READING
            "listening" -> LISTENING
            "speaking" -> SPEAKING
            "review" -> REVIEW
            else -> PROGRESS
        }
    }
}
