package com.seanora.fluentai.feature.grammar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seanora.fluentai.app.navigation.GrammarSessionMode
import com.seanora.fluentai.app.navigation.GrammarSessionSection
import com.seanora.fluentai.app.navigation.GrammarDestination
import com.seanora.fluentai.app.navigation.GrammarSurface
import com.seanora.fluentai.app.navigation.resolveGrammarLegacyEntry
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors

@Composable
fun GrammarDashboardScreen(
    onNavigateBack: () -> Unit,
    onOpenLibrary: () -> Unit,
    onOpenLesson: (String, GrammarSessionMode, GrammarSessionSection) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GrammarFeatureViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val continueLesson = state.resumableState?.resumeContentId?.let { id -> state.lessons.firstOrNull { it.id == id } }
    val quickLesson = state.selectedBite ?: state.lessons.firstOrNull()
    val weakSpot = state.weakSpots.firstOrNull()
    val recommendedPracticeLesson = weakSpot?.lesson ?: continueLesson ?: quickLesson
    var showPracticeChooser by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize().background(DashboardColors.BackgroundPrimary).padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back to Learn", tint = DashboardColors.PrimaryPinkDark)
            }
            Column {
                Text("Grammar", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary)
                Text("Build accurate, natural English structures.", color = DashboardColors.TextSecondary)
            }
        }

        continueLesson?.let { lesson ->
            ActionSurface(
                title = "Continue Grammar",
                detail = "${lesson.title}\n${lesson.cefrLevel} · ${lesson.category.humanize()}",
                action = "Continue",
                emphasized = true,
                onClick = { onOpenLesson(lesson.id, GrammarSessionMode.NORMAL, GrammarSessionSection.LEARN) },
            )
        }

        quickLesson?.let { lesson ->
            ActionSurface(
                title = "Quick Grammar",
                detail = "${lesson.title} · ${lesson.cefrLevel}\nReview one useful rule in a few minutes.",
                action = "Start",
                onClick = { onOpenLesson(lesson.id, GrammarSessionMode.QUICK, GrammarSessionSection.LEARN) },
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            UtilityAction("Browse Grammar", "Explore all ${state.lessons.size} lessons", onOpenLibrary, Modifier.weight(1f))
            UtilityAction(
                "Practice Grammar",
                "Start recommended practice",
                { showPracticeChooser = true },
                Modifier.weight(1f),
            )
        }

        weakSpot?.let { spot ->
            Spacer(Modifier.height(2.dp))
            Text("Needs attention", fontWeight = FontWeight.SemiBold, color = DashboardColors.TextPrimary)
            ActionSurface(
                title = spot.lesson.title,
                detail = listOfNotNull(
                    spot.lesson.cefrLevel,
                    if (spot.mistakeCount > 0) "${spot.mistakeCount} recent mistake${if (spot.mistakeCount == 1) "" else "s"}" else null,
                    if (spot.isReviewDue) "Review due" else null,
                ).joinToString(" · "),
                action = "Practice",
                onClick = { onOpenLesson(spot.lesson.id, GrammarSessionMode.NORMAL, GrammarSessionSection.PRACTICE) },
            )
        }
    }

    if (showPracticeChooser) {
        GrammarPracticeChooser(
            lessons = state.lessons,
            recommendedLessonId = recommendedPracticeLesson?.id,
            onDismiss = { showPracticeChooser = false },
            onStart = { lessonId ->
                showPracticeChooser = false
                onOpenLesson(lessonId, GrammarSessionMode.NORMAL, GrammarSessionSection.PRACTICE)
            },
        )
    }
}

@Composable
private fun GrammarPracticeChooser(
    lessons: List<com.seanora.fluentai.core.model.GrammarLesson>,
    recommendedLessonId: String?,
    onDismiss: () -> Unit,
    onStart: (String) -> Unit,
) {
    var selectedId by remember(recommendedLessonId, lessons) {
        mutableStateOf(recommendedLessonId ?: lessons.firstOrNull()?.id)
    }
    var menuExpanded by remember { mutableStateOf(false) }
    val selected = lessons.firstOrNull { it.id == selectedId }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        titleContentColor = DashboardColors.TextPrimary,
        textContentColor = DashboardColors.TextPrimary,
        title = { Text("Practice Grammar", color = DashboardColors.TextPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    if (selectedId == recommendedLessonId) "Recommended for you" else "Choose a lesson",
                    fontWeight = FontWeight.SemiBold,
                    color = DashboardColors.PrimaryPinkDark,
                )
                Text(
                    "Practice stays inside the selected grammar lesson.",
                    color = DashboardColors.TextSecondary,
                )
                Box {
                    OutlinedButton(onClick = { menuExpanded = true }) {
                        Text(
                            selected?.let { "${it.title} · ${it.cefrLevel}" } ?: "Choose lesson",
                            color = DashboardColors.TextPrimary,
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                    ) {
                        lessons.forEach { lesson ->
                            DropdownMenuItem(
                                text = { Text("${lesson.title} · ${lesson.cefrLevel}") },
                                onClick = {
                                    selectedId = lesson.id
                                    menuExpanded = false
                                },
                            )
                        }
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = DashboardColors.TextPrimary)
            }
        },
        confirmButton = {
            Button(
                onClick = { selectedId?.let(onStart) },
                enabled = selectedId != null,
            ) { Text("Start practice") }
        },
    )
}

@Composable
fun GrammarLegacyEntryScreen(
    destination: GrammarDestination,
    onNavigateBack: () -> Unit,
    onOpenLibrary: () -> Unit,
    onOpenLesson: (String, GrammarSessionMode, GrammarSessionSection) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GrammarFeatureViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    if (state.isLoading) {
        Box(
            modifier.fillMaxSize().background(DashboardColors.BackgroundPrimary),
            contentAlignment = Alignment.Center,
        ) { Text("Loading Grammar…", color = DashboardColors.TextPrimary) }
        return
    }

    val entry = resolveGrammarLegacyEntry(
        destination = destination,
        state = state.resumableState,
        fallbackLessonId = when (destination) {
            GrammarDestination.SENTENCE_BUILDER -> state.currentSentence?.lessonId
            GrammarDestination.ERROR_SPOTTER -> state.currentError?.lessonId
            GrammarDestination.GRAMMAR_IN_CONTEXT -> state.selectedContextId ?: state.lessons.firstOrNull()?.id
            else -> state.selectedBiteId ?: state.lessons.firstOrNull()?.id
        },
        weakLessonId = state.weakSpots.firstOrNull()?.lesson?.id,
    )
    when (entry.surface) {
        GrammarSurface.HOME -> GrammarDashboardScreen(
            onNavigateBack = onNavigateBack,
            onOpenLibrary = onOpenLibrary,
            onOpenLesson = onOpenLesson,
            modifier = modifier,
            viewModel = viewModel,
        )
        GrammarSurface.LIBRARY -> GrammarLibraryScreen(
            onNavigateBack = onNavigateBack,
            onOpenLesson = { onOpenLesson(it, GrammarSessionMode.NORMAL, GrammarSessionSection.LEARN) },
            modifier = modifier,
            initialCompareLessonId = entry.compareLessonId,
            showCompareInitially = entry.showCompare,
        )
        GrammarSurface.SESSION -> GrammarSessionScreen(
            lessonId = requireNotNull(entry.lessonId),
            mode = entry.mode,
            initialSection = entry.section,
            onNavigateBack = onNavigateBack,
            onFinish = onNavigateBack,
            modifier = modifier,
            featureViewModel = viewModel,
        )
    }
}

@Composable
private fun ActionSurface(
    title: String,
    detail: String,
    action: String,
    emphasized: Boolean = false,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        color = if (emphasized) DashboardColors.SurfacePink else Color.White,
        tonalElevation = if (emphasized) 2.dp else 0.dp,
    ) {
        Row(Modifier.padding(22.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(title, fontSize = if (emphasized) 22.sp else 18.sp, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary)
                Text(detail, color = DashboardColors.TextSecondary)
            }
            Text("$action →", fontWeight = FontWeight.Bold, color = DashboardColors.PrimaryPinkDark)
        }
    }
}

@Composable
private fun UtilityAction(title: String, detail: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(modifier.clickable(onClick = onClick), shape = RoundedCornerShape(18.dp), color = Color.White) {
        Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary)
                Text(detail, fontSize = 13.sp, color = DashboardColors.TextSecondary)
            }
            Icon(Icons.AutoMirrored.Rounded.ArrowForward, null, tint = DashboardColors.PrimaryPinkDark)
        }
    }
}

internal fun String.humanize(): String = replace('_', ' ').split(' ').joinToString(" ") { word ->
    word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}
