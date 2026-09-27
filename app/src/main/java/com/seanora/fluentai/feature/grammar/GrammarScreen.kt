package com.seanora.fluentai.feature.grammar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.CompareArrows
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.model.GrammarLesson

@Composable
fun GrammarLibraryScreen(
    onNavigateBack: () -> Unit,
    onOpenLesson: (String) -> Unit,
    modifier: Modifier = Modifier,
    initialCompareLessonId: String? = null,
    showCompareInitially: Boolean = false,
    viewModel: GrammarViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var category by remember { mutableStateOf<String?>(null) }
    var categoryMenu by remember { mutableStateOf(false) }
    var showCompare by remember(showCompareInitially, initialCompareLessonId) {
        mutableStateOf(showCompareInitially)
    }
    val categories = state.lessons.map { it.category }.filter { it.isNotBlank() }.distinct().sorted()
    val visibleLessons = state.lessons.filter { category == null || it.category == category }

    Column(modifier.fillMaxSize().background(DashboardColors.BackgroundPrimary).padding(28.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back to Grammar") }
            Column(Modifier.weight(1f)) {
                Text("Grammar Library", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary)
                Text("${visibleLessons.size} lessons", color = DashboardColors.TextSecondary)
            }
            OutlinedButton(onClick = { showCompare = true }) {
                Icon(Icons.AutoMirrored.Rounded.CompareArrows, null, tint = DashboardColors.PrimaryPinkDark)
                Text(" Compare", color = DashboardColors.TextPrimary)
            }
        }
        Row(Modifier.padding(vertical = 14.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = viewModel::updateSearchQuery,
                placeholder = { Text("Search grammar...", color = DashboardColors.TextSecondary) },
                singleLine = true,
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = DashboardColors.TextPrimary,
                    unfocusedTextColor = DashboardColors.TextPrimary,
                    focusedLabelColor = DashboardColors.PrimaryPinkDark,
                    unfocusedLabelColor = DashboardColors.TextSecondary,
                    focusedBorderColor = DashboardColors.PrimaryPinkDark,
                    unfocusedBorderColor = DashboardColors.TextSecondary,
                ),
            )
            Box {
                OutlinedButton(onClick = { categoryMenu = true }) {
                    Text(category?.humanize() ?: "All topics", color = DashboardColors.TextPrimary)
                }
                DropdownMenu(expanded = categoryMenu, onDismissRequest = { categoryMenu = false }) {
                    DropdownMenuItem(text = { Text("All topics") }, onClick = { category = null; categoryMenu = false })
                    categories.forEach { value ->
                        DropdownMenuItem(text = { Text(value.humanize()) }, onClick = { category = value; categoryMenu = false })
                    }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 12.dp)) {
            listOf<String?>(null, "A2", "B1", "B2", "C1", "C2").forEach { level ->
                val selected = state.selectedLevel == level
                Surface(
                    modifier = Modifier.clickable { viewModel.selectLevel(level) },
                    shape = RoundedCornerShape(50),
                    color = if (selected) DashboardColors.SurfacePink else Color.White,
                ) { Text(level ?: "All", Modifier.padding(horizontal = 16.dp, vertical = 9.dp), color = DashboardColors.TextPrimary) }
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(visibleLessons, key = { it.id }) { lesson ->
                Surface(
                    modifier = Modifier.fillMaxWidth().clickable { onOpenLesson(lesson.id) },
                    shape = RoundedCornerShape(16.dp), color = Color.White,
                ) {
                    Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(lesson.title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary)
                            Text("${lesson.cefrLevel} · ${lesson.category.humanize()}", fontSize = 13.sp, color = DashboardColors.PrimaryPinkDark)
                            Text(lesson.summaryEn, maxLines = 2, overflow = TextOverflow.Ellipsis, color = DashboardColors.TextSecondary)
                        }
                        Icon(Icons.AutoMirrored.Rounded.ArrowForward, "Open lesson", tint = DashboardColors.PrimaryPinkDark)
                    }
                }
            }
        }
    }
    if (showCompare) GrammarCompareDialog(
        lessons = state.lessons,
        initialPrimaryId = initialCompareLessonId,
        onDismiss = { showCompare = false },
    )
}

@Composable
fun GrammarCompareDialog(
    lessons: List<GrammarLesson>,
    initialPrimaryId: String?,
    onDismiss: () -> Unit,
) {
    var primaryId by remember(initialPrimaryId, lessons) { mutableStateOf(initialPrimaryId ?: lessons.firstOrNull()?.id) }
    var secondaryId by remember(primaryId, lessons) { mutableStateOf(lessons.firstOrNull { it.id != primaryId }?.id) }
    val primary = lessons.firstOrNull { it.id == primaryId }
    val secondary = lessons.firstOrNull { it.id == secondaryId }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .widthIn(max = 1_000.dp)
                .heightIn(max = 700.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
        ) {
            BoxWithConstraints(Modifier.padding(24.dp)) {
                val wide = maxWidth >= 700.dp
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Compare Structures",
                            modifier = Modifier.weight(1f),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = DashboardColors.TextPrimary,
                        )
                        TextButton(onClick = onDismiss) {
                            Text("Close", color = DashboardColors.PrimaryPinkDark)
                        }
                    }
                    if (wide) {
                        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                            LessonSelector("Structure A", lessons, primaryId, Modifier.weight(1f)) {
                                primaryId = it
                                if (secondaryId == it) secondaryId = lessons.firstOrNull { item -> item.id != it }?.id
                            }
                            LessonSelector("Structure B", lessons.filter { it.id != primaryId }, secondaryId, Modifier.weight(1f)) {
                                secondaryId = it
                            }
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                            ComparePane(primary, Modifier.weight(1f))
                            ComparePane(secondary, Modifier.weight(1f))
                        }
                    } else {
                        LessonSelector("Structure A", lessons, primaryId, Modifier.fillMaxWidth()) {
                            primaryId = it
                            if (secondaryId == it) secondaryId = lessons.firstOrNull { item -> item.id != it }?.id
                        }
                        ComparePane(primary, Modifier.fillMaxWidth())
                        LessonSelector("Structure B", lessons.filter { it.id != primaryId }, secondaryId, Modifier.fillMaxWidth()) {
                            secondaryId = it
                        }
                        ComparePane(secondary, Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonSelector(
    label: String,
    lessons: List<GrammarLesson>,
    selectedId: String?,
    modifier: Modifier = Modifier,
    onSelect: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier) {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text(
                "$label: ${lessons.firstOrNull { it.id == selectedId }?.title ?: "Choose"}",
                maxLines = 1,
                color = DashboardColors.TextPrimary,
            )
        }
        DropdownMenu(expanded, { expanded = false }) {
            lessons.forEach { lesson -> DropdownMenuItem(text = { Text(lesson.title) }, onClick = { onSelect(lesson.id); expanded = false }) }
        }
    }
}

@Composable
private fun ComparePane(lesson: GrammarLesson?, modifier: Modifier) {
    Surface(modifier.heightIn(min = 220.dp, max = 430.dp), shape = RoundedCornerShape(14.dp), color = DashboardColors.BackgroundPrimary) {
        Column(Modifier.padding(14.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(lesson?.title.orEmpty(), fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary)
            lesson?.let {
                Text("Purpose", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DashboardColors.PrimaryPinkDark)
                Text(it.summaryEn, color = DashboardColors.TextSecondary)
                it.rules.firstOrNull()?.let { rule ->
                    Text("Structure", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DashboardColors.PrimaryPinkDark)
                    Text(rule.pattern, fontWeight = FontWeight.SemiBold, color = DashboardColors.TextPrimary)
                }
                it.examples.firstOrNull()?.let { example ->
                    Text("Example", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DashboardColors.PrimaryPinkDark)
                    Text(example.en, color = DashboardColors.TextPrimary)
                }
                it.turkishTraps.firstOrNull()?.let { trap ->
                    Text("Turkish learner trap", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DashboardColors.PrimaryPinkDark)
                    Text(trap.explanationTr, color = DashboardColors.TextSecondary)
                }
            }
        }
    }
}
