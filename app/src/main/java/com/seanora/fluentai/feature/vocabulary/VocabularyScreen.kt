package com.seanora.fluentai.feature.vocabulary

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.model.VocabItem

@Composable
fun VocabularyLibraryScreen(
    onNavigateBack: () -> Unit,
    onOpenWord: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VocabularyViewModel = hiltViewModel(),
    featureViewModel: VocabularyFeatureViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val featureState by featureViewModel.uiState.collectAsStateWithLifecycle()
    var topicMenu by remember { mutableStateOf(false) }
    var topicId by remember { mutableStateOf<String?>(null) }
    val topicIds = remember(featureState.collectionWords) { featureState.collectionWords.mapTo(hashSetOf()) { it.id } }
    val visible = remember(state.items, topicId, topicIds) {
        if (topicId == null) state.items else state.items.filter { it.id in topicIds }
    }
    Column(modifier.fillMaxSize().background(DashboardColors.BackgroundPrimary).padding(28.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back to Vocabulary") }
            Column {
                Text("Vocabulary Library", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary)
                Text("${visible.size} words", color = DashboardColors.TextSecondary)
            }
        }
        BoxWithConstraints(Modifier.fillMaxWidth().padding(vertical = 14.dp)) {
            val compact = maxWidth < 650.dp
            val topicControl: @Composable () -> Unit = {
                Box {
                    OutlinedButton(onClick = { topicMenu = true }) {
                        Text(featureState.collections.firstOrNull { it.topicId == topicId }?.name ?: "All topics", color = DashboardColors.TextPrimary)
                    }
                    DropdownMenu(topicMenu, { topicMenu = false }) {
                        DropdownMenuItem(text = { Text("All topics") }, onClick = { topicId = null; featureViewModel.clearCollection(); topicMenu = false })
                        featureState.collections.forEach { collection ->
                            DropdownMenuItem(text = { Text(collection.name) }, onClick = { topicId = collection.topicId; featureViewModel.selectCollection(collection); topicMenu = false })
                        }
                    }
                }
            }
            if (compact) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    VocabularySearchField(state.searchQuery, viewModel::updateSearchQuery, Modifier.fillMaxWidth())
                    topicControl()
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    VocabularySearchField(state.searchQuery, viewModel::updateSearchQuery, Modifier.weight(1f))
                    topicControl()
                }
            }
        }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 12.dp)) {
            listOf<String?>(null, "A2", "B1", "B2", "C1", "C2").forEach { level ->
                Surface(
                    Modifier.clickable { viewModel.selectLevel(level) },
                    shape = RoundedCornerShape(50),
                    color = if (state.selectedLevel == level) DashboardColors.SurfacePink else Color.White,
                ) { Text(level ?: "All", Modifier.padding(horizontal = 16.dp, vertical = 9.dp), color = DashboardColors.TextPrimary) }
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(visible, key = { it.id }) { word ->
                Surface(Modifier.fillMaxWidth().clickable { featureViewModel.recordLibrarySelection(word.id); onOpenWord(word.id) }, shape = RoundedCornerShape(14.dp), color = Color.White) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(word.headword, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary)
                            Text("${word.cefrLevel} · ${word.partOfSpeech}", fontSize = 13.sp, color = DashboardColors.PrimaryPinkDark)
                            Text(word.definitionEn, maxLines = 1, overflow = TextOverflow.Ellipsis, color = DashboardColors.TextSecondary)
                        }
                        Icon(Icons.AutoMirrored.Rounded.ArrowForward, "Open word", tint = DashboardColors.PrimaryPinkDark)
                    }
                }
            }
        }
    }
}

@Composable
fun VocabularyWordDetailScreen(
    wordId: String,
    onNavigateBack: () -> Unit,
    onPracticeWord: (String) -> Unit,
    onComplete: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    viewModel: VocabularyViewModel = hiltViewModel(),
    featureViewModel: VocabularyFeatureViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showTurkish by remember { mutableStateOf(false) }
    var section by rememberSaveable(wordId) { mutableStateOf("Definition") }
    LaunchedEffect(wordId) { viewModel.selectItemById(wordId); featureViewModel.recordLibrarySelection(wordId) }
    val word = state.selectedItem?.takeIf { it.id == wordId }
    Surface(modifier.fillMaxSize(), color = DashboardColors.BackgroundPrimary, contentColor = DashboardColors.TextPrimary) {
        Column(Modifier.fillMaxSize().padding(30.dp)) {
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                val compact = maxWidth < 760.dp
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back") }
                        Column(Modifier.weight(1f)) {
                            Text(word?.headword ?: if (state.isLoading) "Loading…" else "Word unavailable", fontSize = 34.sp, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary)
                            word?.let {
                                if (it.phonetic.isNotBlank()) Text(it.phonetic, color = DashboardColors.TextSecondary)
                                Text("${it.cefrLevel} · ${it.partOfSpeech}", color = DashboardColors.PrimaryPinkDark)
                            }
                        }
                        if (!compact) VocabularyWordActions(word, { showTurkish = true }, onPracticeWord, onComplete)
                    }
                    if (compact) FlowRow(Modifier.fillMaxWidth().padding(top = 10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        VocabularyWordActions(word, { showTurkish = true }, onPracticeWord, onComplete)
                    }
                }
            }
            if (word == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(if (state.isLoading) "Loading…" else "This vocabulary item is not available.", color = DashboardColors.TextPrimary) }
                return@Column
            }
            val sections = buildList {
                add("Definition")
                if (word.examples.isNotEmpty()) add("Examples")
                if (word.collocations.isNotEmpty()) add("Collocations")
                if (word.register.isNotBlank() || word.topicTags.isNotEmpty()) add("Usage")
            }
            if (sections.size > 1) Row(Modifier.padding(vertical = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                sections.forEach { item ->
                    Surface(Modifier.clickable { section = item }, shape = RoundedCornerShape(50), color = if (section == item) DashboardColors.SurfacePink else Color.White) {
                        Text(item, Modifier.padding(horizontal = 17.dp, vertical = 9.dp), color = DashboardColors.TextPrimary)
                    }
                }
            }
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                when (section) {
                    "Definition" -> Text(word.definitionEn, fontSize = 21.sp, lineHeight = 30.sp, color = DashboardColors.TextPrimary)
                    "Examples" -> word.examples.forEach { example -> Column { VocabularyExampleText(example.en, word.headword); example.context?.let { Text(it, color = DashboardColors.TextSecondary) } } }
                    "Collocations" -> FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { word.collocations.forEach { Surface(shape = RoundedCornerShape(10.dp), color = Color.White) { Text(it.text, Modifier.padding(12.dp), color = DashboardColors.TextPrimary) } } }
                    "Usage" -> { if (word.register.isNotBlank()) Text("Register: ${word.register}", color = DashboardColors.TextPrimary); if (word.topicTags.isNotEmpty()) Text(word.topicTags.joinToString(" · "), color = DashboardColors.TextSecondary) }
                }
            }
        }
    }
    if (showTurkish && word != null) VocabularyTurkishHelp(word) { showTurkish = false }
}

@Composable
private fun VocabularySearchField(value: String, onValueChange: (String) -> Unit, modifier: Modifier) {
    OutlinedTextField(
        value,
        onValueChange,
        modifier = modifier,
        placeholder = { Text("Search vocabulary...", color = DashboardColors.TextSecondary) },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = DashboardColors.TextPrimary,
            unfocusedTextColor = DashboardColors.TextPrimary,
            focusedBorderColor = DashboardColors.PrimaryPinkDark,
            unfocusedBorderColor = DashboardColors.TextSecondary,
        ),
    )
}

@Composable
private fun VocabularyWordActions(
    word: VocabItem?,
    onTurkishHelp: () -> Unit,
    onPracticeWord: (String) -> Unit,
    onComplete: (() -> Unit)?,
) {
    if (word != null && (word.meaningTr.isNotBlank() || word.turkishTraps.isNotEmpty())) TextButton(onClick = onTurkishHelp) { Text("Turkish Help", color = DashboardColors.PrimaryPinkDark) }
    if (word != null) Button(onClick = { onPracticeWord(word.id) }) { Text("Practice this word") }
    if (word != null && onComplete != null) OutlinedButton(onClick = onComplete) { Text("Complete", color = DashboardColors.TextPrimary) }
}

@Composable
private fun VocabularyExampleText(example: String, headword: String) {
    val start = example.indexOf(headword, ignoreCase = true)
    if (start < 0) {
        Text(example, fontSize = 18.sp, color = DashboardColors.TextPrimary)
        return
    }
    Text(
        buildAnnotatedString {
            append(example.substring(0, start))
            withStyle(SpanStyle(fontWeight = FontWeight.Bold, background = DashboardColors.SurfacePink)) {
                append(example.substring(start, start + headword.length))
            }
            append(example.substring(start + headword.length))
        },
        fontSize = 18.sp,
        color = DashboardColors.TextPrimary,
    )
}

@Composable
fun VocabularyDailyWordDetailScreen(
    onNavigateBack: () -> Unit,
    onPracticeWord: (String) -> Unit,
    modifier: Modifier = Modifier,
    featureViewModel: VocabularyFeatureViewModel = hiltViewModel(),
) {
    val state by featureViewModel.uiState.collectAsStateWithLifecycle()
    val word = state.dailyWord
    if (word != null) {
        VocabularyWordDetailScreen(
            wordId = word.id,
            onNavigateBack = onNavigateBack,
            onPracticeWord = onPracticeWord,
            modifier = modifier,
        )
    } else {
        Box(
            modifier.fillMaxSize().background(DashboardColors.BackgroundPrimary),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                if (state.isLoading) "Loading…" else "Word of Day is not available.",
                color = DashboardColors.TextPrimary,
            )
        }
    }
}

@Composable
private fun VocabularyTurkishHelp(word: VocabItem, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Turkish Help") },
        text = { Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            if (word.meaningTr.isNotBlank()) Text(word.meaningTr)
            word.turkishTraps.forEach { trap -> Column { Text(trap.trapType.replace('_', ' '), fontWeight = FontWeight.Bold); Text(trap.trapNoteTr); trap.incorrectExample?.let { Text("✗ $it") }; trap.correctExample?.let { Text("✓ $it") } } }
        } },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Close") } },
    )
}
