package com.seanora.fluentai.feature.listening

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Headphones
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seanora.fluentai.app.navigation.ListeningSessionMode
import com.seanora.fluentai.core.designsystem.components.ButtonVariant
import com.seanora.fluentai.core.designsystem.components.FluentButton
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.designsystem.dashboard.DashboardDimensions
import com.seanora.fluentai.core.designsystem.dashboard.DashboardPageHeader
import com.seanora.fluentai.core.designsystem.dashboard.DashboardSpacing
import com.seanora.fluentai.core.designsystem.dashboard.DashboardTypography
import com.seanora.fluentai.core.model.ListeningScenario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListeningDashboardScreen(
    onNavigateBack: () -> Unit,
    onOpenLibrary: () -> Unit,
    onOpenSession: (String, ListeningSessionMode) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListeningFeatureViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showPractice by remember { mutableStateOf(false) }

    if (showPractice) {
        PracticeListeningSheet(
            state = state,
            onDismiss = { showPractice = false },
            onOpenSession = { id, mode ->
                showPractice = false
                onOpenSession(id, mode)
            },
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DashboardColors.BackgroundPrimary)
            .verticalScroll(rememberScrollState())
            .padding(DashboardDimensions.PagePadding),
        verticalArrangement = Arrangement.spacedBy(DashboardSpacing.lg),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back to Learn", tint = DashboardColors.PrimaryPinkDark)
            }
            DashboardPageHeader(
                title = "Listening",
                subtitle = "Train your ear for natural academic and professional English.",
                modifier = Modifier.weight(1f),
            )
        }

        if (state.isLoading) {
            Box(Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = DashboardColors.PrimaryPink)
            }
            return@Column
        }

        state.continueScenario?.let { scenario ->
            ListeningHero(
                eyebrow = "Continue Listening",
                scenario = scenario,
                action = "Continue",
                onClick = { onOpenSession(scenario.id, ListeningSessionMode.STANDARD) },
            )
        }

        state.dailyScenario?.let { scenario ->
            EditorialRecommendation(
                title = "Today's Listening",
                scenario = scenario,
                action = "Start",
                onClick = { onOpenSession(scenario.id, ListeningSessionMode.STANDARD) },
            )
        }

        BoxWithConstraints(Modifier.fillMaxWidth()) {
            if (maxWidth < 620.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(DashboardSpacing.md)) {
                    UtilityAction(
                        title = "Browse Listening",
                        supporting = "Explore all recordings",
                        icon = { Icon(Icons.Rounded.LibraryMusic, null) },
                        onClick = onOpenLibrary,
                    )
                    UtilityAction(
                        title = "Practice Listening",
                        supporting = "Comprehension or dictation",
                        icon = { Icon(Icons.Rounded.Tune, null) },
                        onClick = { showPractice = true },
                    )
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.md)) {
                    UtilityAction(
                        title = "Browse Listening",
                        supporting = "Explore all recordings",
                        icon = { Icon(Icons.Rounded.LibraryMusic, null) },
                        onClick = onOpenLibrary,
                        modifier = Modifier.weight(1f),
                    )
                    UtilityAction(
                        title = "Practice Listening",
                        supporting = "Comprehension or dictation",
                        icon = { Icon(Icons.Rounded.Tune, null) },
                        onClick = { showPractice = true },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        state.weakSpots.firstOrNull()?.let { weak ->
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, DashboardColors.BorderSoft, RoundedCornerShape(18.dp)),
            ) {
                Row(
                    modifier = Modifier.padding(DashboardSpacing.lg),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.md),
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("Needs attention", style = DashboardTypography.Caption, color = DashboardColors.TextSecondary)
                        Text(
                            weak.scenario.category.toDisplayLabel(),
                            style = DashboardTypography.CardTitle,
                            color = DashboardColors.TextPrimary,
                        )
                        Text(
                            weak.scenario.title,
                            style = DashboardTypography.Body,
                            color = DashboardColors.TextSecondary,
                        )
                    }
                    FluentButton(
                        text = "Practice",
                        onClick = { onOpenSession(weak.scenario.id, ListeningSessionMode.COMPREHENSION) },
                        variant = ButtonVariant.Secondary,
                    )
                }
            }
        }
    }
}

@Composable
private fun ListeningHero(
    eyebrow: String,
    scenario: ListeningScenario,
    action: String,
    onClick: () -> Unit,
) {
    Surface(
        color = DashboardColors.SurfacePink,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
    ) {
        Row(
            Modifier.padding(28.dp),
            horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.lg),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier.size(64.dp).background(DashboardColors.PrimaryPink, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Rounded.Headphones, null, tint = Color.White, modifier = Modifier.size(32.dp))
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(eyebrow, style = DashboardTypography.Caption, color = DashboardColors.PrimaryPinkDark)
                Text(scenario.title, style = DashboardTypography.PageTitle, color = DashboardColors.TextPrimary)
                Text(scenario.metadataLine(), style = DashboardTypography.Body, color = DashboardColors.TextSecondary)
            }
            Text(action, style = DashboardTypography.Button, color = DashboardColors.PrimaryPinkDark)
            Icon(Icons.AutoMirrored.Rounded.ArrowForward, null, tint = DashboardColors.PrimaryPinkDark)
        }
    }
}

@Composable
private fun EditorialRecommendation(
    title: String,
    scenario: ListeningScenario,
    action: String,
    onClick: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(DashboardSpacing.sm)) {
        Text(title, style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth().border(1.dp, DashboardColors.BorderSoft, RoundedCornerShape(18.dp)),
        ) {
            Row(
                Modifier.padding(DashboardSpacing.lg),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.md),
            ) {
                Column(Modifier.weight(1f)) {
                    Text(scenario.title, style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
                    Text(scenario.metadataLine(), style = DashboardTypography.Body, color = DashboardColors.TextSecondary)
                    if (scenario.scenarioContext.isNotBlank()) {
                        Text(scenario.scenarioContext, style = DashboardTypography.Body, color = DashboardColors.TextSecondary)
                    }
                }
                FluentButton(text = action, onClick = onClick, variant = ButtonVariant.Primary)
            }
        }
    }
}

@Composable
private fun UtilityAction(
    title: String,
    supporting: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth().border(1.dp, DashboardColors.BorderSoft, RoundedCornerShape(16.dp)).clickable(onClick = onClick),
    ) {
        Row(
            Modifier.padding(DashboardSpacing.lg),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.md),
        ) {
            Box(
                Modifier.size(44.dp).background(DashboardColors.SurfacePink, CircleShape),
                contentAlignment = Alignment.Center,
            ) { icon() }
            Column(Modifier.weight(1f)) {
                Text(title, style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
                Text(supporting, style = DashboardTypography.Caption, color = DashboardColors.TextSecondary)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PracticeListeningSheet(
    state: ListeningFeatureUiState,
    onDismiss: () -> Unit,
    onOpenSession: (String, ListeningSessionMode) -> Unit,
) {
    val recommended = state.weakSpots.firstOrNull()?.scenario ?: state.dailyScenario ?: state.scenarios.firstOrNull()
    val comprehension = state.scenarios.firstOrNull { it.comprehensionQuestions.isNotEmpty() }
    val dictation = state.dailyScenario?.takeIf { it.transcriptItems.isNotEmpty() }
        ?: state.scenarios.firstOrNull { it.transcriptItems.isNotEmpty() }
    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = DashboardColors.BackgroundPrimary) {
        Column(
            Modifier.fillMaxWidth().padding(horizontal = 28.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(DashboardSpacing.md),
        ) {
            Text("Practice Listening", style = DashboardTypography.PageTitle, color = DashboardColors.TextPrimary)
            Text(
                "Choose a focused practice mode. Every activity keeps its source recording available.",
                style = DashboardTypography.Body,
                color = DashboardColors.TextSecondary,
            )
            recommended?.let {
                PracticeOption("Automatic / Recommended", it, ListeningSessionMode.STANDARD, onOpenSession)
            }
            comprehension?.let {
                PracticeOption("Comprehension", it, ListeningSessionMode.COMPREHENSION, onOpenSession)
            }
            dictation?.let {
                PracticeOption("Dictation", it, ListeningSessionMode.DICTATION, onOpenSession)
            }
            Box(Modifier.padding(bottom = 24.dp))
        }
    }
}

@Composable
private fun PracticeOption(
    label: String,
    scenario: ListeningScenario,
    mode: ListeningSessionMode,
    onOpenSession: (String, ListeningSessionMode) -> Unit,
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().clickable { onOpenSession(scenario.id, mode) },
    ) {
        Row(Modifier.padding(DashboardSpacing.lg), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(label, style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
                Text(scenario.title, style = DashboardTypography.Body, color = DashboardColors.TextSecondary)
            }
            Icon(Icons.AutoMirrored.Rounded.ArrowForward, null, tint = DashboardColors.PrimaryPinkDark)
        }
    }
}

internal fun ListeningScenario.metadataLine(): String = buildList {
    add(cefrLevel)
    if (category.isNotBlank()) add(category.toDisplayLabel())
    if (durationSeconds > 0) add(formatListeningDuration(durationSeconds.toLong() * 1000L))
}.joinToString(" · ")

internal fun String.toDisplayLabel(): String = replace('_', ' ').replace('-', ' ')
    .split(' ')
    .filter { it.isNotBlank() }
    .joinToString(" ") { token -> token.replaceFirstChar { it.uppercase() } }

internal fun formatListeningDuration(milliseconds: Long): String {
    val seconds = (milliseconds.coerceAtLeast(0L) / 1000L)
    return "%d:%02d".format(seconds / 60L, seconds % 60L)
}
