package com.seanora.fluentai.feature.learn

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.seanora.fluentai.core.designsystem.components.FluentButton
import com.seanora.fluentai.core.designsystem.dashboard.*
import com.seanora.fluentai.core.designsystem.kit.DecorativeBackdrop
import com.seanora.fluentai.core.designsystem.kit.FluentPageHeader
import com.seanora.fluentai.core.designsystem.kit.FluentSectionTitle
import com.seanora.fluentai.core.designsystem.kit.FluentSurfaceCard
import com.seanora.fluentai.core.designsystem.kit.LearningModule
import com.seanora.fluentai.core.designsystem.kit.ModuleIconTile
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.ui.unit.sp
import com.seanora.fluentai.app.navigation.GrammarSessionMode
import com.seanora.fluentai.app.navigation.GrammarSessionSection
import com.seanora.fluentai.feature.grammar.GrammarLibraryScreen
import com.seanora.fluentai.feature.grammar.GrammarSessionScreen
import com.seanora.fluentai.feature.listening.ListeningScreen
import com.seanora.fluentai.feature.reading.ReadingScreen
import com.seanora.fluentai.feature.vocabulary.VocabularyLibraryScreen
import com.seanora.fluentai.feature.vocabulary.VocabularyWordDetailScreen

@Composable
fun LearnScreen(
    modifier: Modifier = Modifier,
    initialModule: String? = null,
    initialContentId: String? = null,
    onAssignedActivityCompleted: (() -> Unit)? = null,
    onNavigateBack: (() -> Unit)? = null,
    onNavigateToVocabularyDashboard: (() -> Unit)? = null,
    onNavigateToGrammarDashboard: (() -> Unit)? = null,
    onNavigateToReadingDashboard: (() -> Unit)? = null,
    onNavigateToListeningDashboard: (() -> Unit)? = null,
) {
    var activeModule by rememberSaveable { mutableStateOf(initialModule) }
    val moduleBack = onNavigateBack ?: { activeModule = null }

    when (activeModule) {
        "vocabulary" -> AssignedModuleContainer(onAssignedActivityCompleted, modifier) {
            if (initialContentId != null) {
                VocabularyWordDetailScreen(
                    wordId = initialContentId,
                    onNavigateBack = moduleBack,
                    onPracticeWord = {},
                    onComplete = onAssignedActivityCompleted,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                VocabularyLibraryScreen(
                    onNavigateBack = moduleBack,
                    onOpenWord = {},
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
        "grammar" -> AssignedModuleContainer(onAssignedActivityCompleted, modifier) {
            if (initialContentId != null) {
                GrammarSessionScreen(
                    lessonId = initialContentId,
                    mode = GrammarSessionMode.NORMAL,
                    initialSection = GrammarSessionSection.LEARN,
                    onNavigateBack = moduleBack,
                    onFinish = onAssignedActivityCompleted ?: moduleBack,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                GrammarLibraryScreen(onNavigateBack = moduleBack, onOpenLesson = {}, modifier = Modifier.fillMaxSize())
            }
        }
        "reading" -> AssignedModuleContainer(onAssignedActivityCompleted, modifier) {
            ReadingScreen(onNavigateBack = moduleBack, initialContentId = initialContentId, modifier = Modifier.fillMaxSize())
        }
        "listening" -> AssignedModuleContainer(onAssignedActivityCompleted, modifier) {
            ListeningScreen(onNavigateBack = moduleBack, initialContentId = initialContentId, modifier = Modifier.fillMaxSize())
        }
        else -> LearnLanding(modifier) { module ->
            if (module == "vocabulary" && onNavigateToVocabularyDashboard != null) {
                onNavigateToVocabularyDashboard()
            } else if (module == "grammar" && onNavigateToGrammarDashboard != null) {
                onNavigateToGrammarDashboard()
            } else if (module == "reading" && onNavigateToReadingDashboard != null) {
                onNavigateToReadingDashboard()
            } else if (module == "listening" && onNavigateToListeningDashboard != null) {
                onNavigateToListeningDashboard()
            } else {
                activeModule = module
            }
        }
    }
}

@Composable
private fun LearnLanding(modifier: Modifier, onOpenModule: (String) -> Unit) {
    BoxWithConstraints(
        modifier.fillMaxSize().background(DashboardColors.BackgroundPrimary),
    ) {
        val columns = when { maxWidth >= 1200.dp -> 4; maxWidth >= 700.dp -> 2; else -> 1 }
        val modules = listOf(
            LearnModule("vocabulary", "Vocabulary", "Build useful words and expressions for everyday and professional English.", "Word of the day · Flash cards", LearningModule.VOCABULARY),
            LearnModule("grammar", "Grammar", "Understand patterns and use English structures with confidence.", "Rules · Turkish traps", LearningModule.GRAMMAR),
            LearnModule("reading", "Reading", "Practice comprehension with structured articles and vocabulary support.", "Articles · Comprehension", LearningModule.READING),
            LearnModule("listening", "Listening", "Train comprehension with dialogues, transcripts, and questions.", "Dialogues · Dictation", LearningModule.LISTENING),
        )
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(DashboardSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(DashboardSpacing.xl),
        ) {
            FluentPageHeader(
                title = "Learn",
                module = LearningModule.LEARN,
                eyebrow = "Offline curriculum · A2 → C2",
                subtitle = "Choose a skill area and continue through your offline curriculum.",
            )
            FluentSectionTitle(
                title = "Skill areas",
                emoji = "🧭",
                subtitle = "Every module works without internet",
                accent = LearningModule.LEARN.accent,
            )
            modules.chunked(columns).forEach { rowItems ->
                Row(
                    Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.md),
                ) {
                    rowItems.forEach { module ->
                        ModuleCard(module, { onOpenModule(module.id) }, Modifier.weight(1f).fillMaxHeight())
                    }
                    repeat(columns - rowItems.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
    }
}

@Composable
private fun ModuleCard(module: LearnModule, onClick: () -> Unit, modifier: Modifier = Modifier) {
    FluentSurfaceCard(modifier = modifier, onClick = onClick, contentPadding = PaddingValues(0.dp), contentSpacing = 0.dp) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(112.dp)
                .background(module.identity.heroBrush),
        ) {
            DecorativeBackdrop(accent = module.identity.accent, modifier = Modifier.matchParentSize())
            ModuleIconTile(module = module.identity, size = 56.dp, modifier = Modifier.align(Alignment.CenterStart).padding(start = 20.dp))
            Text(module.identity.emoji, fontSize = 52.sp, modifier = Modifier.align(Alignment.CenterEnd).padding(end = 20.dp))
        }
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(module.title, color = DashboardColors.TextPrimary, style = DashboardTypography.SectionTitle)
            Text(module.highlights, color = module.identity.accent, style = DashboardTypography.Caption)
            Text(module.description, color = DashboardColors.TextSecondary, style = DashboardTypography.Body)
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Open ${module.title}", color = module.identity.accent, style = DashboardTypography.Button, modifier = Modifier.weight(1f))
                Icon(Icons.AutoMirrored.Rounded.ArrowForward, null, tint = module.identity.accent)
            }
        }
    }
}

@Composable
private fun AssignedModuleContainer(onCompleted: (() -> Unit)?, modifier: Modifier, content: @Composable () -> Unit) {
    Box(modifier.fillMaxSize()) {
        content()
        if (onCompleted != null) {
            FluentButton(
                text = "Finish assigned activity",
                onClick = onCompleted,
                modifier = Modifier.align(Alignment.BottomEnd).padding(DashboardSpacing.xl),
            )
        }
    }
}

private data class LearnModule(
    val id: String,
    val title: String,
    val description: String,
    val highlights: String,
    val identity: LearningModule,
)
