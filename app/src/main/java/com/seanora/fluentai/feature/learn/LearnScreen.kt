package com.seanora.fluentai.feature.learn

import androidx.compose.ui.res.painterResource
import com.seanora.fluentai.R
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
import com.seanora.fluentai.core.designsystem.kit.FluentActionTile
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
    onNavigateToChunksDashboard: (() -> Unit)? = null,
    onNavigateToReview: (() -> Unit)? = null,
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
        else -> LearnLanding(modifier, onNavigateToReview) { module ->
            if (module == "vocabulary" && onNavigateToVocabularyDashboard != null) {
                onNavigateToVocabularyDashboard()
            } else if (module == "grammar" && onNavigateToGrammarDashboard != null) {
                onNavigateToGrammarDashboard()
            } else if (module == "chunks" && onNavigateToChunksDashboard != null) {
                onNavigateToChunksDashboard()
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
private fun LearnLanding(modifier: Modifier, onNavigateToReview: (() -> Unit)?, onOpenModule: (String) -> Unit) {
    BoxWithConstraints(
        modifier.fillMaxSize().background(DashboardColors.BackgroundPrimary),
    ) {
        val columns = when { maxWidth >= 1100.dp -> 3; maxWidth >= 680.dp -> 2; else -> 1 }
        val modules = listOf(
            LearnModule("vocabulary", "Vocabulary", "Everyday and workplace words with spaced repetition and pronunciation", "Word of the day · Flash cards", LearningModule.VOCABULARY),
            LearnModule("chunks", "Chunks and patterns", "Ready-to-use sentence starters, collocations, and SVO blueprints", "Turkish traps · SVO builder", LearningModule.CHUNKS),
            LearnModule("grammar", "Grammar", "Clear structures, Turkish transfer traps, and sentence practice", "Rules · Sentence builder", LearningModule.GRAMMAR),
            LearnModule("reading", "Reading", "Structured articles with instant lookup and comprehension checks", "Articles · Live feed", LearningModule.READING),
            LearnModule("listening", "Listening", "Work and daily dialogues with transcripts and dictation drills", "Dialogues · Dictation", LearningModule.LISTENING),
        )
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(DashboardSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(DashboardSpacing.lg),
        ) {
            FluentPageHeader(
                title = "Learn",
                module = LearningModule.LEARN,
                eyebrow = "Offline curriculum · A2 to C2",
                illustration = painterResource(R.drawable.continue_learning_background),
                subtitle = "Choose a skill area and continue through your offline curriculum",
            )
            FluentSectionTitle(
                title = "Skill areas",
                subtitle = "Every core module works offline",
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
            if (onNavigateToReview != null) {
                FluentActionTile(
                    title = "Smart review and mistake bank",
                    detail = "Clear due spaced-repetition cards and fix recurring Turkish transfer traps",
                    module = LearningModule.REVIEW,
                    actionLabel = "Open review",
                    emphasized = true,
                    onClick = onNavigateToReview,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun ModuleCard(module: LearnModule, onClick: () -> Unit, modifier: Modifier = Modifier) {
    FluentSurfaceCard(
        modifier = modifier,
        accent = module.identity.accent,
        onClick = onClick,
        contentPadding = PaddingValues(20.dp),
        contentSpacing = 12.dp,
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            ModuleIconTile(module = module.identity, size = 44.dp, showEmojiBadge = false)
            Icon(
                Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = null,
                tint = DashboardColors.TextMuted,
                modifier = Modifier.size(18.dp),
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(module.title, color = DashboardColors.TextPrimary, style = DashboardTypography.SectionTitle)
            Text(module.highlights, color = module.identity.accent, style = DashboardTypography.Caption)
        }
        Text(module.description, color = DashboardColors.TextSecondary, style = DashboardTypography.Body)
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
