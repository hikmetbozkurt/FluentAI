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
            LearnModule("vocabulary", "Vocabulary", "Build useful words and expressions for everyday and professional English.", Icons.Rounded.MenuBook, DashboardColors.SurfacePink),
            LearnModule("grammar", "Grammar", "Understand patterns and use English structures with confidence.", Icons.Rounded.Description, DashboardColors.PurplePastel),
            LearnModule("reading", "Reading", "Practice comprehension with structured articles and vocabulary support.", Icons.Rounded.AutoStories, DashboardColors.PeachPastel),
            LearnModule("listening", "Listening", "Train comprehension with dialogues, transcripts, and questions.", Icons.Rounded.Headphones, DashboardColors.MintPastel),
        )
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(DashboardSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(DashboardSpacing.xl),
        ) {
            DashboardPageHeader("Learn", "Choose a skill area and continue through your offline curriculum.")
            modules.chunked(columns).forEach { rowItems ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.md), verticalAlignment = Alignment.Top) {
                    rowItems.forEach { module ->
                        ModuleCard(module, { onOpenModule(module.id) }, Modifier.weight(1f))
                    }
                    repeat(columns - rowItems.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
    }
}

@Composable
private fun ModuleCard(module: LearnModule, onClick: () -> Unit, modifier: Modifier = Modifier) {
    DashboardCard(modifier.clickable(onClick = onClick)) {
        Box(Modifier.size(52.dp).background(module.tint, CircleShape), contentAlignment = Alignment.Center) {
            Icon(module.icon, null, tint = DashboardColors.PrimaryPinkDark, modifier = Modifier.size(28.dp))
        }
        Text(module.title, color = DashboardColors.TextPrimary, style = DashboardTypography.CardTitle)
        Text(module.description, color = DashboardColors.TextSecondary, style = DashboardTypography.Body)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Open ${module.title}", color = DashboardColors.PrimaryPinkDark, style = DashboardTypography.Button, modifier = Modifier.weight(1f))
            Icon(Icons.Rounded.ArrowForward, null, tint = DashboardColors.PrimaryPinkDark)
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
    val icon: ImageVector,
    val tint: Color,
)
