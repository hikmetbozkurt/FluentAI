package com.seanora.fluentai.feature.reading

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkAdd
import androidx.compose.material.icons.rounded.BookmarkAdded
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seanora.fluentai.app.navigation.ReadingSessionMode
import com.seanora.fluentai.core.audio.OnDeviceTtsSpeaker
import com.seanora.fluentai.core.audio.TtsAccent
import com.seanora.fluentai.core.audio.rememberOnDeviceTtsSpeaker
import com.seanora.fluentai.core.designsystem.components.ButtonVariant
import com.seanora.fluentai.core.designsystem.components.FluentButton
import com.seanora.fluentai.core.designsystem.components.FluentSelectableChip
import com.seanora.fluentai.core.designsystem.theme.FluentTheme
import com.seanora.fluentai.core.model.ReadingArticle
import com.seanora.fluentai.core.model.ReadingComprehensionQuestion
import com.seanora.fluentai.core.model.VocabItem
import com.seanora.fluentai.core.model.VocabularyAnnotation
import com.seanora.fluentai.data.dictionary.DictionaryEntry

private enum class ReadingLength(val label: String) {
    SHORT("Under 500 words"), MEDIUM("500–999 words"), LONG("1,000+ words");

    fun matches(wordCount: Int) = when (this) {
        SHORT -> wordCount < 500
        MEDIUM -> wordCount in 500..999
        LONG -> wordCount >= 1_000
    }
}

private data class ReadingWordLookup(
    val displayWord: String,
    val annotation: VocabularyAnnotation? = null,
    val vocabItem: VocabItem? = null,
)

// region Library

@Composable
fun ReadingLibraryScreen(
    onNavigateBack: () -> Unit,
    onOpenArticle: (String) -> Unit,
    modifier: Modifier = Modifier,
    initialSavedOnly: Boolean = false,
    viewModel: ReadingViewModel = hiltViewModel(),
    supportViewModel: ReadingReaderSupportViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val savedIds by supportViewModel.savedReadingIds.collectAsStateWithLifecycle()
    val completedIds by supportViewModel.completedArticleIds.collectAsStateWithLifecycle()
    val surfaceStyle by supportViewModel.surfaceStyle.collectAsStateWithLifecycle()

    var category by rememberSaveable { mutableStateOf<String?>(null) }
    var length by rememberSaveable { mutableStateOf<ReadingLength?>(null) }
    var savedOnly by rememberSaveable(initialSavedOnly) { mutableStateOf(initialSavedOnly) }

    val categories = remember(state.articles) { state.articles.map { it.category }.distinct().sorted() }
    val visible = remember(state.articles, category, length, savedOnly, savedIds) {
        state.articles.filter { article ->
            (category == null || article.category == category) &&
                (length == null || length!!.matches(article.wordCount)) &&
                (!savedOnly || article.id in savedIds)
        }
    }

    ReadingTheme(style = surfaceStyle) {
        val tokens = LocalReadingTokens.current
        BoxWithConstraints(modifier.fillMaxSize().background(tokens.paper)) {
            val horizontalPadding = if (maxWidth >= ReadingBreakpoints.Medium) 40.dp else 20.dp
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 260.dp),
                contentPadding = PaddingValues(start = horizontalPadding, end = horizontalPadding, top = 24.dp, bottom = 40.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        ReadingBackButton(label = "Back", onClick = onNavigateBack)
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                if (savedOnly) "Saved reading" else "Reading library",
                                style = ReadingType.Display,
                                color = tokens.ink,
                            )
                            Text(
                                "${visible.size} article${if (visible.size == 1) "" else "s"}",
                                style = ReadingType.Meta,
                                color = tokens.inkSecondary,
                            )
                        }
                        LibrarySearchField(state.searchQuery, viewModel::updateSearchQuery)
                        ReadingFilters(
                            selectedLevel = state.selectedLevel,
                            onLevel = viewModel::selectLevel,
                            category = category,
                            onCategory = { category = it },
                            categories = categories,
                            length = length,
                            onLength = { length = it },
                            savedOnly = savedOnly,
                            savedCount = savedIds.size,
                            onToggleSaved = { savedOnly = !savedOnly },
                        )
                    }
                }
                when {
                    state.isLoading -> item(span = { GridItemSpan(maxLineSpan) }) {
                        Box(Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = tokens.terracotta)
                        }
                    }
                    state.errorMessage != null -> item(span = { GridItemSpan(maxLineSpan) }) {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            ReadingStateMessage(ReadingIllustration.ERROR, state.errorMessage!!)
                        }
                    }
                    visible.isEmpty() -> item(span = { GridItemSpan(maxLineSpan) }) {
                        val hasFilters = state.selectedLevel != null ||
                            state.searchQuery.isNotBlank() ||
                            category != null ||
                            length != null ||
                            savedOnly
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            ReadingStateMessage(
                                ReadingIllustration.EMPTY,
                                if (savedOnly) "No saved articles yet. Tap the bookmark icon on any article to save it here."
                                else "No articles match these filters.",
                                action = if (hasFilters) {
                                    {
                                        FluentButton(
                                            text = "Clear filters",
                                            variant = ButtonVariant.Outlined,
                                            onClick = {
                                                viewModel.selectLevel(null)
                                                viewModel.updateSearchQuery("")
                                                category = null
                                                length = null
                                                savedOnly = false
                                            },
                                        )
                                    }
                                } else null,
                            )
                        }
                    }
                    else -> items(visible, key = { it.id }) { article ->
                        LibraryArticleCard(
                            article = article,
                            isSaved = article.id in savedIds,
                            isCompleted = article.id in completedIds,
                            onToggleSave = { supportViewModel.toggleSavedReading(article.id) },
                            onClick = { onOpenArticle(article.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LibrarySearchField(query: String, onQueryChange: (String) -> Unit) {
    val tokens = LocalReadingTokens.current
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Search articles...") },
        leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
        trailingIcon = if (query.isNotEmpty()) {
            {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Rounded.Close, contentDescription = "Clear search")
                }
            }
        } else null,
        singleLine = true,
        modifier = Modifier.widthIn(max = 640.dp).fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = tokens.ink,
            unfocusedTextColor = tokens.ink,
            focusedContainerColor = tokens.card,
            unfocusedContainerColor = tokens.card,
            focusedBorderColor = tokens.ink,
            unfocusedBorderColor = tokens.border,
            focusedLeadingIconColor = tokens.ink,
            unfocusedLeadingIconColor = tokens.inkSecondary,
            focusedTrailingIconColor = tokens.ink,
            unfocusedTrailingIconColor = tokens.inkSecondary,
            focusedPlaceholderColor = tokens.inkMuted,
            unfocusedPlaceholderColor = tokens.inkMuted,
            cursorColor = tokens.terracotta,
        ),
        shape = RoundedCornerShape(12.dp),
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ReadingFilters(
    selectedLevel: String?,
    onLevel: (String?) -> Unit,
    category: String?,
    onCategory: (String?) -> Unit,
    categories: List<String>,
    length: ReadingLength?,
    onLength: (ReadingLength?) -> Unit,
    savedOnly: Boolean,
    savedCount: Int,
    onToggleSaved: () -> Unit,
) {
    FlowRow(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        listOf<String?>(null, "A2", "B1", "B2", "C1", "C2").forEach { level ->
            FluentSelectableChip(level ?: "All", selectedLevel == level, onClick = { onLevel(level) }, role = Role.RadioButton)
        }
        FluentSelectableChip(
            text = if (savedCount > 0) "Saved ($savedCount)" else "Saved",
            selected = savedOnly,
            onClick = onToggleSaved,
            role = Role.Checkbox,
        )
        Spacer(Modifier.width(4.dp))
        CompactFilterMenu(
            label = category?.readingDisplayName() ?: "Topics",
            values = listOf(null) + categories,
            valueLabel = { it?.readingDisplayName() ?: "All topics" },
            onSelect = onCategory,
        )
        CompactFilterMenu(
            label = length?.label ?: "Length",
            values = listOf(null) + ReadingLength.entries,
            valueLabel = { it?.label ?: "Any length" },
            onSelect = onLength,
        )
    }
}

@Composable
private fun <T> CompactFilterMenu(label: String, values: List<T?>, valueLabel: (T?) -> String, onSelect: (T?) -> Unit) {
    val tokens = LocalReadingTokens.current
    var expanded by remember { mutableStateOf(false) }
    Box {
        FluentButton(
            text = label,
            variant = ButtonVariant.Outlined,
            trailingIcon = { Icon(Icons.Rounded.ExpandMore, contentDescription = null, modifier = Modifier.size(18.dp)) },
            onClick = { expanded = true },
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, containerColor = tokens.card) {
            values.forEach { value ->
                DropdownMenuItem(
                    text = { Text(valueLabel(value), color = tokens.ink) },
                    onClick = { onSelect(value); expanded = false },
                )
            }
        }
    }
}

@Composable
private fun LibraryArticleCard(
    article: ReadingArticle,
    isSaved: Boolean,
    isCompleted: Boolean,
    onToggleSave: () -> Unit,
    onClick: () -> Unit,
) {
    val tokens = LocalReadingTokens.current
    ReadingCard(modifier = Modifier.fillMaxWidth(), onClick = onClick, onClickLabel = "Open ${article.title}") {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Box {
                ReadingCoverImage(article.id, Modifier.fillMaxWidth().aspectRatio(3f / 2f))
                if (isCompleted) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = tokens.sage,
                        modifier = Modifier.align(Alignment.TopStart).padding(10.dp),
                    ) {
                        Row(
                            Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Icon(Icons.Rounded.CheckCircle, null, tint = tokens.onSuccess, modifier = Modifier.size(14.dp))
                            Text("Read", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = tokens.onSuccess)
                        }
                    }
                }
                Surface(
                    onClick = onToggleSave,
                    shape = CircleShape,
                    color = tokens.card.copy(alpha = 0.94f),
                    border = BorderStroke(1.dp, tokens.border),
                    modifier = Modifier.align(Alignment.TopEnd).padding(6.dp).size(48.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            if (isSaved) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                            contentDescription = if (isSaved) "Remove bookmark" else "Save article",
                            tint = if (isSaved) tokens.terracotta else tokens.inkSecondary,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }
            Column(Modifier.padding(horizontal = 6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = tokens.sand,
                    ) {
                        Text(
                            article.cefrLevel,
                            style = ReadingType.Eyebrow,
                            fontWeight = FontWeight.Bold,
                            color = tokens.ink,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        )
                    }
                    ReadingEyebrow(article.category.readingDisplayName())
                }
                Text(article.title, style = ReadingType.CardTitle, color = tokens.ink, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(
                    article.summaryEn,
                    style = FluentTheme.typography.bodySmall,
                    color = tokens.inkSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Row(
                Modifier.fillMaxWidth().padding(start = 6.dp, end = 6.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("~${article.estimatedReadingMinutes} min", style = ReadingType.Meta, color = tokens.inkMuted)
                Text("${article.wordCount} words", style = ReadingType.Meta, color = tokens.inkMuted)
            }
        }
    }
}

// endregion

// region Session

@Composable
fun ReadingSessionScreen(
    articleId: String,
    mode: ReadingSessionMode,
    onNavigateBack: () -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReadingSessionViewModel = hiltViewModel(),
    supportViewModel: ReadingReaderSupportViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val surfaceStyle by supportViewModel.surfaceStyle.collectAsStateWithLifecycle()
    val fontSizeSp by supportViewModel.fontSizeSp.collectAsStateWithLifecycle()
    val savedIds by supportViewModel.savedReadingIds.collectAsStateWithLifecycle()
    val savedToReviewWordIds by supportViewModel.savedToReviewWordIds.collectAsStateWithLifecycle()
    val activeDictionaryEntry by supportViewModel.activeDictionaryEntry.collectAsStateWithLifecycle()
    val tts = rememberOnDeviceTtsSpeaker()

    LaunchedEffect(articleId, mode) {
        supportViewModel.clearDictionaryEntry()
        viewModel.openArticle(articleId, mode)
    }

    ReadingTheme(style = surfaceStyle) {
        ReadingSessionContent(
            state = state,
            viewModel = viewModel,
            surfaceStyle = surfaceStyle,
            fontSizeSp = fontSizeSp,
            isSaved = articleId in savedIds,
            savedToReviewWordIds = savedToReviewWordIds,
            activeDictionaryEntry = activeDictionaryEntry,
            tts = tts,
            onSetSurfaceStyle = supportViewModel::setSurfaceStyle,
            onDecreaseFont = supportViewModel::decreaseFontSize,
            onIncreaseFont = supportViewModel::increaseFontSize,
            onToggleSave = { supportViewModel.toggleSavedReading(articleId) },
            onLookupWord = supportViewModel::lookupLocalVocab,
            onResolveDictionaryWord = supportViewModel::resolveDictionaryEntry,
            onClearDictionaryWord = supportViewModel::clearDictionaryEntry,
            onSaveWordToReview = supportViewModel::saveWordToVocabularyReview,
            onNavigateBack = onNavigateBack,
            onFinish = onFinish,
            modifier = modifier,
        )
    }
}

@Composable
private fun ReadingSessionContent(
    state: ReadingSessionUiState,
    viewModel: ReadingSessionViewModel,
    surfaceStyle: ReadingSurfaceStyle,
    fontSizeSp: Float,
    isSaved: Boolean,
    savedToReviewWordIds: Set<String>,
    activeDictionaryEntry: DictionaryEntry?,
    tts: OnDeviceTtsSpeaker,
    onSetSurfaceStyle: (ReadingSurfaceStyle) -> Unit,
    onDecreaseFont: () -> Unit,
    onIncreaseFont: () -> Unit,
    onToggleSave: () -> Unit,
    onLookupWord: (String) -> VocabItem?,
    onResolveDictionaryWord: (String) -> Unit,
    onClearDictionaryWord: () -> Unit,
    onSaveWordToReview: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier,
) {
    val tokens = LocalReadingTokens.current
    BoxWithConstraints(modifier.fillMaxSize().background(tokens.paper)) {
        val wide = maxWidth >= ReadingBreakpoints.WideReader
        val stackedTopBar = maxWidth < ReadingBreakpoints.Expanded
        val horizontalPadding = if (maxWidth >= ReadingBreakpoints.Medium) 40.dp else 20.dp
        when {
            state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center), color = tokens.terracotta)
            state.article == null -> ReadingStateMessage(
                illustration = ReadingIllustration.ERROR,
                message = state.errorMessage ?: "Article unavailable.",
                modifier = Modifier.align(Alignment.Center),
                action = { FluentButton(text = "Back to Reading", onClick = onNavigateBack, variant = ButtonVariant.Outlined) },
            )
            else -> Column(Modifier.fillMaxSize()) {
                SessionTopBar(
                    section = state.section,
                    onSection = viewModel::selectSection,
                    onBack = {
                        tts.stop()
                        onNavigateBack()
                    },
                    stacked = stackedTopBar,
                    isSaved = isSaved,
                    onToggleSave = onToggleSave,
                    modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 14.dp),
                )
                Box(Modifier.fillMaxSize().padding(horizontal = horizontalPadding), contentAlignment = Alignment.TopCenter) {
                    when (state.section) {
                        ReadingSessionSection.ARTICLE -> ArticleSection(
                            state = state,
                            viewModel = viewModel,
                            surfaceStyle = surfaceStyle,
                            fontSizeSp = fontSizeSp,
                            savedToReviewWordIds = savedToReviewWordIds,
                            activeDictionaryEntry = activeDictionaryEntry,
                            tts = tts,
                            onSetSurfaceStyle = onSetSurfaceStyle,
                            onDecreaseFont = onDecreaseFont,
                            onIncreaseFont = onIncreaseFont,
                            onLookupWord = onLookupWord,
                            onResolveDictionaryWord = onResolveDictionaryWord,
                            onClearDictionaryWord = onClearDictionaryWord,
                            onSaveWordToReview = onSaveWordToReview,
                            onExit = {
                                tts.stop()
                                onNavigateBack()
                            },
                            wide = wide,
                        )
                        ReadingSessionSection.VOCABULARY -> VocabularySection(
                            article = state.article,
                            viewModel = viewModel,
                            tts = tts,
                            onLookupWord = onLookupWord,
                            savedToReviewWordIds = savedToReviewWordIds,
                            onSaveWordToReview = onSaveWordToReview,
                            wide = wide,
                        )
                        ReadingSessionSection.QUESTIONS -> QuestionsSection(state, viewModel, wide = wide)
                        ReadingSessionSection.REVIEW -> ReviewSection(state, viewModel, onFinish)
                    }
                }
            }
        }
    }
}

@Composable
private fun SessionTopBar(
    section: ReadingSessionSection,
    onSection: (ReadingSessionSection) -> Unit,
    onBack: () -> Unit,
    stacked: Boolean,
    isSaved: Boolean,
    onToggleSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tokens = LocalReadingTokens.current
    val bookmarkButton: @Composable () -> Unit = {
        Surface(
            onClick = onToggleSave,
            shape = RoundedCornerShape(12.dp),
            color = tokens.card,
            border = BorderStroke(1.dp, tokens.border),
            modifier = Modifier.defaultMinSize(minHeight = 44.dp),
        ) {
            Row(
                Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(
                    if (isSaved) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                    contentDescription = if (isSaved) "Saved" else "Save article",
                    tint = if (isSaved) tokens.terracotta else tokens.ink,
                    modifier = Modifier.size(18.dp),
                )
                Text(
                    if (isSaved) "Saved" else "Save",
                    style = FluentTheme.typography.labelLarge,
                    color = tokens.ink,
                )
            }
        }
    }
    if (stacked) {
        Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                ReadingBackButton(label = "Back to Reading", onClick = onBack)
                bookmarkButton()
            }
            SectionStepper(section, onSection, Modifier.horizontalScroll(rememberScrollState()))
        }
    } else {
        Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            ReadingBackButton(label = "Back to Reading", onClick = onBack)
            SectionStepper(section, onSection)
            bookmarkButton()
        }
    }
}

/** Quiet step indicator for the four session sections. Each step stays directly selectable. */
@Composable
private fun SectionStepper(current: ReadingSessionSection, onSection: (ReadingSessionSection) -> Unit, modifier: Modifier = Modifier) {
    val tokens = LocalReadingTokens.current
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
        ReadingSessionSection.entries.forEachIndexed { index, section ->
            val selected = section == current
            val done = section.ordinal < current.ordinal
            Row(
                Modifier
                    .clip(RoundedCornerShape(50))
                    .selectable(selected = selected, role = Role.Tab, onClick = { onSection(section) })
                    .background(if (selected) tokens.ink else Color.Transparent)
                    .padding(horizontal = 14.dp, vertical = 12.dp)
                    .semantics { stateDescription = if (selected) "Current step" else if (done) "Completed step" else "Upcoming step" },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    Modifier.size(22.dp).clip(CircleShape).background(
                        when {
                            selected -> tokens.card
                            done -> tokens.sage
                            else -> tokens.elevated
                        },
                    ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "${index + 1}",
                        style = FluentTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            selected -> tokens.ink
                            done -> tokens.onSuccess
                            else -> tokens.inkSecondary
                        },
                    )
                }
                Text(
                    section.label,
                    style = FluentTheme.typography.labelLarge,
                    color = if (selected) tokens.card else tokens.inkSecondary,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun ArticleSection(
    state: ReadingSessionUiState,
    viewModel: ReadingSessionViewModel,
    surfaceStyle: ReadingSurfaceStyle,
    fontSizeSp: Float,
    savedToReviewWordIds: Set<String>,
    activeDictionaryEntry: DictionaryEntry?,
    tts: OnDeviceTtsSpeaker,
    onSetSurfaceStyle: (ReadingSurfaceStyle) -> Unit,
    onDecreaseFont: () -> Unit,
    onIncreaseFont: () -> Unit,
    onLookupWord: (String) -> VocabItem?,
    onResolveDictionaryWord: (String) -> Unit,
    onClearDictionaryWord: () -> Unit,
    onSaveWordToReview: (String) -> Unit,
    onExit: () -> Unit,
    wide: Boolean,
) {
    val tokens = LocalReadingTokens.current
    val article = state.article ?: return
    val scrollState = rememberScrollState()
    val isSpeaking by tts.isSpeaking.collectAsStateWithLifecycle()
    val activeUtteranceTag by tts.activeUtteranceTag.collectAsStateWithLifecycle()

    var selectedLookup by remember(article.id) { mutableStateOf<ReadingWordLookup?>(null) }
    var showTurkishSummary by rememberSaveable(article.id) { mutableStateOf(false) }

    val scrollProgress = if (scrollState.maxValue > 0) {
        (scrollState.value.toFloat() / scrollState.maxValue.toFloat()).coerceIn(0f, 1f)
    } else 0f

    Column(Modifier.fillMaxSize()) {
        // Live Scroll Reading Progress Bar
        LinearProgressIndicator(
            progress = { scrollProgress },
            modifier = Modifier.fillMaxWidth().height(3.dp).clip(RoundedCornerShape(2.dp)),
            color = tokens.terracotta,
            trackColor = tokens.border,
        )
        Spacer(Modifier.height(10.dp))

        Row(Modifier.weight(1f).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally)) {
            Column(
                Modifier
                    .weight(1f, fill = false)
                    .widthIn(max = 740.dp)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(bottom = 40.dp),
            ) {
                val remainingMinutes = ((1f - scrollProgress) * article.estimatedReadingMinutes)
                    .toInt()
                    .coerceAtLeast(if (scrollProgress >= 0.98f) 0 else 1)
                val toolbarProgressLabel = if (state.mode == ReadingSessionMode.GUIDED) {
                    "Paragraph ${state.guidedParagraphIndex + 1} / ${article.paragraphs.size.coerceAtLeast(1)}"
                } else {
                    "${(scrollProgress * 100).toInt()}% · ~$remainingMinutes min left"
                }

                // Shared Reader Comfort Controls Bar
                ReaderComfortToolbar(
                    surfaceStyle = surfaceStyle,
                    fontSizeSp = fontSizeSp,
                    isSpeakingArticle = isSpeaking && activeUtteranceTag == "article:${article.id}",
                    onSelectStyle = onSetSurfaceStyle,
                    onDecreaseFont = onDecreaseFont,
                    onIncreaseFont = onIncreaseFont,
                    onToggleReadAloud = {
                        if (isSpeaking && activeUtteranceTag == "article:${article.id}") {
                            tts.stop()
                        } else {
                            val fullText = article.paragraphs.joinToString("\n\n") { it.contentEn }
                            tts.speak(fullText, TtsAccent.US, speechRate = 0.92f, tag = "article:${article.id}")
                        }
                    },
                    readingMode = state.mode,
                    onSelectMode = viewModel::setMode,
                    progressLabel = toolbarProgressLabel,
                )
                Spacer(Modifier.height(18.dp))

                ReadingCoverImage(article.id, Modifier.fillMaxWidth().aspectRatio(if (wide) 2.6f else 2.2f))
                Spacer(Modifier.height(24.dp))
                ReadingEyebrow(article.fullMeta())
                Text(article.title, style = ReadingType.Display, color = tokens.ink, modifier = Modifier.padding(top = 8.dp))
                Text(article.summaryEn, style = FluentTheme.typography.bodyLarge, color = tokens.inkSecondary, modifier = Modifier.padding(top = 10.dp))

                // Optional Turkish Summary Reveal (English-first)
                if (article.summaryTr.isNotBlank()) {
                    TextButton(
                        onClick = { showTurkishSummary = !showTurkishSummary },
                        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 4.dp),
                    ) {
                        Icon(Icons.Rounded.Translate, null, tint = tokens.terracotta, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(
                            if (showTurkishSummary) "Hide Turkish summary" else "Show Turkish summary",
                            style = ReadingType.Meta,
                            color = tokens.terracotta,
                        )
                    }
                    AnimatedVisibility(visible = showTurkishSummary) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = tokens.elevated,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                        ) {
                            Text(
                                article.summaryTr,
                                style = FluentTheme.typography.bodyMedium,
                                color = tokens.inkSecondary,
                                modifier = Modifier.padding(14.dp),
                            )
                        }
                    }
                }

                Text(
                    "Tap any highlighted term or word in the text for lookup and pronunciation",
                    style = ReadingType.Meta,
                    color = tokens.inkMuted,
                    modifier = Modifier.padding(top = 8.dp),
                )
                Box(Modifier.padding(vertical = 20.dp).fillMaxWidth().height(1.dp).background(tokens.border))

                val paragraphs = if (state.mode == ReadingSessionMode.GUIDED) {
                    listOfNotNull(article.paragraphs.getOrNull(state.guidedParagraphIndex))
                } else {
                    article.paragraphs
                }
                if (state.mode == ReadingSessionMode.GUIDED) {
                    GuidedProgress(state.guidedParagraphIndex, article.paragraphs.size)
                }

                val bodyTextStyle = ReadingType.bodyForSize(fontSizeSp)
                paragraphs.forEachIndexed { idx, paragraph ->
                    val paraTag = "para:${article.id}:$idx"
                    val isSpeakingPara = isSpeaking && activeUtteranceTag == paraTag
                    Row(
                        Modifier.fillMaxWidth().padding(top = 14.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        paragraph.title?.let {
                            Text(it, style = ReadingType.Heading, color = tokens.ink, modifier = Modifier.weight(1f))
                        } ?: Spacer(Modifier.weight(1f))
                        IconButton(
                            onClick = {
                                if (isSpeakingPara) tts.stop()
                                else tts.speak(paragraph.contentEn, TtsAccent.US, speechRate = 0.92f, tag = paraTag)
                            },
                            modifier = Modifier.size(44.dp),
                        ) {
                            Icon(
                                if (isSpeakingPara) Icons.Rounded.Stop else Icons.AutoMirrored.Rounded.VolumeUp,
                                contentDescription = if (isSpeakingPara) "Stop reading paragraph" else "Read paragraph aloud",
                                tint = if (isSpeakingPara) tokens.terracotta else tokens.inkMuted,
                                modifier = Modifier.size(18.dp),
                            )
                        }
                    }
                    Text(
                        text = annotatedInteractiveParagraph(
                            text = paragraph.contentEn,
                            annotations = article.vocabularyAnnotations,
                            selectedWord = selectedLookup?.displayWord,
                            tokens = tokens,
                            onAnnotationClick = { ann ->
                                onResolveDictionaryWord(ann.word)
                                selectedLookup = ReadingWordLookup(
                                    displayWord = ann.word,
                                    annotation = ann,
                                    vocabItem = onLookupWord(ann.word),
                                )
                            },
                            onWordClick = { word ->
                                onResolveDictionaryWord(word)
                                val matchedAnn = article.vocabularyAnnotations.firstOrNull { it.word.equals(word, ignoreCase = true) }
                                selectedLookup = ReadingWordLookup(
                                    displayWord = word,
                                    annotation = matchedAnn,
                                    vocabItem = onLookupWord(word),
                                )
                            },
                        ),
                        style = bodyTextStyle,
                        color = tokens.ink,
                        modifier = Modifier.padding(vertical = 8.dp),
                    )
                }

                if (state.mode == ReadingSessionMode.GUIDED) {
                    Row(Modifier.fillMaxWidth().padding(top = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        FluentButton(text = "Previous paragraph", onClick = viewModel::previousParagraph, variant = ButtonVariant.Outlined, enabled = state.guidedParagraphIndex > 0)
                        FluentButton(text = "Next paragraph", onClick = viewModel::nextParagraph, enabled = state.guidedParagraphIndex < article.paragraphs.lastIndex)
                    }
                }

                ReadingCard(modifier = Modifier.fillMaxWidth().padding(top = 32.dp), color = tokens.elevated) {
                    Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("Ready to check your understanding?", style = ReadingType.Heading, color = tokens.ink)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            if (article.comprehensionQuestions.isNotEmpty()) FluentButton(text = "Continue to Questions", onClick = { viewModel.selectSection(ReadingSessionSection.QUESTIONS) })
                            if (article.vocabularyAnnotations.isNotEmpty()) FluentButton(text = "Review Vocabulary", onClick = { viewModel.selectSection(ReadingSessionSection.VOCABULARY) }, variant = ButtonVariant.Outlined)
                        }
                    }
                }
                FluentButton(text = "Exit", onClick = onExit, variant = ButtonVariant.Outlined, modifier = Modifier.padding(top = 16.dp))
            }

            if (wide) {
                WordSidePanel(
                    lookup = selectedLookup,
                    dictionaryEntry = activeDictionaryEntry,
                    savedToReviewWordIds = savedToReviewWordIds,
                    tts = tts,
                    onSaveWordToReview = onSaveWordToReview,
                    onClose = { selectedLookup = null; onClearDictionaryWord() },
                    modifier = Modifier.width(330.dp).padding(top = 4.dp),
                )
            }
        }
    }

    if (!wide && selectedLookup != null) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { selectedLookup = null; onClearDictionaryWord() },
            sheetState = sheetState,
            containerColor = tokens.card,
        ) {
            WordLookupDetail(
                lookup = selectedLookup!!,
                dictionaryEntry = activeDictionaryEntry,
                savedToReviewWordIds = savedToReviewWordIds,
                tts = tts,
                onSaveWordToReview = onSaveWordToReview,
                onClose = { selectedLookup = null; onClearDictionaryWord() },
                modifier = Modifier.padding(start = 28.dp, end = 28.dp, bottom = 36.dp),
            )
        }
    }
}

@Composable
private fun GuidedProgress(index: Int, total: Int) {
    val tokens = LocalReadingTokens.current
    Column(Modifier.fillMaxWidth().padding(bottom = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Paragraph ${index + 1} of $total", style = ReadingType.Meta, color = tokens.inkSecondary)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            repeat(total.coerceAtLeast(1)) { i ->
                Box(
                    Modifier.weight(1f).height(4.dp).clip(RoundedCornerShape(2.dp))
                        .background(if (i <= index) tokens.terracotta else tokens.border),
                )
            }
        }
    }
}

/**
 * Makes every annotated key term highlighted & tappable AND every normal word tappable for instant local lookup.
 */
private fun annotatedInteractiveParagraph(
    text: String,
    annotations: List<VocabularyAnnotation>,
    selectedWord: String?,
    tokens: ReadingSurfaceTokens,
    onAnnotationClick: (VocabularyAnnotation) -> Unit,
    onWordClick: (String) -> Unit,
): AnnotatedString {
    data class AnnMatch(val range: IntRange, val annotation: VocabularyAnnotation)
    val annMatches = annotations
        .flatMap { annotation ->
            Regex("(?i)(?<![\\p{L}\\p{N}])${Regex.escape(annotation.word)}(?![\\p{L}\\p{N}])")
                .findAll(text)
                .map { AnnMatch(it.range, annotation) }
                .toList()
        }
        .sortedWith(compareBy<AnnMatch> { it.range.first }.thenByDescending { it.range.last })
    val keptAnns = mutableListOf<AnnMatch>()
    annMatches.forEach { m -> if (keptAnns.none { it.range.last >= m.range.first }) keptAnns += m }

    val wordRegex = Regex("[\\p{L}]+(?:['’-][\\p{L}]+)*")

    fun AnnotatedString.Builder.appendPlainWithTappableWords(slice: String) {
        var c = 0
        wordRegex.findAll(slice).forEach { m ->
            if (m.range.first > c) append(slice.substring(c, m.range.first))
            val token = m.value
            val isSelected = selectedWord != null && token.equals(selectedWord, ignoreCase = true)
            val linkStyle = if (isSelected) {
                SpanStyle(background = tokens.sand, fontWeight = FontWeight.SemiBold)
            } else {
                SpanStyle()
            }
            withLink(
                LinkAnnotation.Clickable(
                    tag = "word:$token",
                    styles = TextLinkStyles(style = linkStyle),
                    linkInteractionListener = { onWordClick(token) },
                ),
            ) { append(token) }
            c = m.range.last + 1
        }
        if (c < slice.length) append(slice.substring(c))
    }

    return buildAnnotatedString {
        var cursor = 0
        keptAnns.forEach { match ->
            if (match.range.first > cursor) {
                appendPlainWithTappableWords(text.substring(cursor, match.range.first))
            }
            val selected = selectedWord != null && match.annotation.word.equals(selectedWord, ignoreCase = true)
            val style = SpanStyle(
                background = if (selected) tokens.sand else tokens.clay.copy(alpha = 0.55f),
                fontWeight = if (selected) FontWeight.SemiBold else null,
            )
            withLink(
                LinkAnnotation.Clickable(
                    tag = "ann:${match.annotation.word}",
                    styles = TextLinkStyles(style = style),
                    linkInteractionListener = { onAnnotationClick(match.annotation) },
                ),
            ) { append(text.substring(match.range.first, match.range.last + 1)) }
            cursor = match.range.last + 1
        }
        if (cursor < text.length) {
            appendPlainWithTappableWords(text.substring(cursor))
        }
    }
}

@Composable
private fun WordSidePanel(
    lookup: ReadingWordLookup?,
    dictionaryEntry: DictionaryEntry?,
    savedToReviewWordIds: Set<String>,
    tts: OnDeviceTtsSpeaker,
    onSaveWordToReview: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tokens = LocalReadingTokens.current
    ReadingCard(modifier = modifier) {
        if (lookup == null) {
            Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ReadingEyebrow("Instant Lookup", color = tokens.inkSecondary)
                Text(
                    "Tap any highlighted key term or word in the article to inspect its meaning from the 107,000+ offline dictionary, hear pronunciation, or add it to Vocabulary Review.",
                    style = FluentTheme.typography.bodyMedium,
                    color = tokens.inkSecondary,
                )
            }
        } else {
            WordLookupDetail(
                lookup = lookup,
                dictionaryEntry = dictionaryEntry,
                savedToReviewWordIds = savedToReviewWordIds,
                tts = tts,
                onSaveWordToReview = onSaveWordToReview,
                onClose = onClose,
                modifier = Modifier.padding(22.dp),
            )
        }
    }
}

@Composable
private fun WordLookupDetail(
    lookup: ReadingWordLookup,
    dictionaryEntry: DictionaryEntry?,
    savedToReviewWordIds: Set<String>,
    tts: OnDeviceTtsSpeaker,
    onSaveWordToReview: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tokens = LocalReadingTokens.current
    var showTurkish by remember(lookup.displayWord) { mutableStateOf(false) }
    val vocab = lookup.vocabItem
    val annotation = lookup.annotation
    val dict = dictionaryEntry
    val effectiveVocabId = vocab?.id ?: dict?.vocabId?.takeIf { it.isNotBlank() }
    val isSaved = effectiveVocabId != null && effectiveVocabId in savedToReviewWordIds
    val displayTitle = vocab?.headword ?: dict?.displayWord ?: lookup.displayWord
    val phonetic = vocab?.phonetic?.takeIf { it.isNotBlank() } ?: dict?.phonetic?.takeIf { it.isNotBlank() }
    val cefrLabel = vocab?.cefrLevel?.takeIf { it.isNotBlank() } ?: dict?.cefrLevel?.takeIf { it.isNotBlank() }
    val posLabel = vocab?.partOfSpeech?.takeIf { it.isNotBlank() } ?: dict?.partOfSpeech?.takeIf { it.isNotBlank() }

    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                val eyebrowParts = buildList {
                    add(if (annotation != null) "Key Vocabulary" else "Offline Dictionary")
                    cefrLabel?.let(::add)
                    posLabel?.replace('_', ' ')?.let(::add)
                }
                ReadingEyebrow(eyebrowParts.joinToString(" · "))
                Text(displayTitle, style = ReadingType.Title, color = tokens.ink)
                phonetic?.let {
                    Text(it, style = ReadingType.Meta, color = tokens.inkSecondary)
                }
            }
            IconButton(onClick = onClose, modifier = Modifier.size(40.dp)) {
                Icon(Icons.Rounded.Close, contentDescription = "Close", tint = tokens.inkSecondary)
            }
        }

        // Pronounce US / UK buttons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(
                onClick = { tts.speak(displayTitle, TtsAccent.US) },
                shape = RoundedCornerShape(12.dp),
                color = tokens.elevated,
            ) {
                Row(Modifier.padding(horizontal = 12.dp, vertical = 7.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Icon(Icons.AutoMirrored.Rounded.VolumeUp, null, tint = tokens.terracotta, modifier = Modifier.size(16.dp))
                    Text("US", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = tokens.ink)
                }
            }
            Surface(
                onClick = { tts.speak(displayTitle, TtsAccent.UK) },
                shape = RoundedCornerShape(12.dp),
                color = tokens.elevated,
            ) {
                Row(Modifier.padding(horizontal = 12.dp, vertical = 7.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Icon(Icons.AutoMirrored.Rounded.VolumeUp, null, tint = tokens.terracotta, modifier = Modifier.size(16.dp))
                    Text("UK", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = tokens.ink)
                }
            }
        }

        when {
            annotation != null -> {
                Text(annotation.contextDefinitionEn, style = FluentTheme.typography.bodyLarge, color = tokens.ink)
                val extraDef = vocab?.definitionEn ?: dict?.definitionEn
                if (extraDef != null && extraDef != annotation.contextDefinitionEn) {
                    Text(extraDef, style = FluentTheme.typography.bodySmall, color = tokens.inkSecondary)
                }
            }
            vocab != null -> {
                Text(vocab.definitionEn, style = FluentTheme.typography.bodyLarge, color = tokens.ink)
            }
            dict != null -> {
                Text(dict.definitionEn, style = FluentTheme.typography.bodyLarge, color = tokens.ink)
                if (dict.exampleEn.isNotBlank()) {
                    Surface(shape = RoundedCornerShape(10.dp), color = tokens.elevated) {
                        Text(
                            "“${dict.exampleEn}”",
                            style = FluentTheme.typography.bodySmall,
                            color = tokens.inkSecondary,
                            modifier = Modifier.fillMaxWidth().padding(10.dp),
                        )
                    }
                }
            }
            else -> {
                Text(
                    "No offline dictionary entry found for this form. You can still listen to its pronunciation above.",
                    style = FluentTheme.typography.bodyMedium,
                    color = tokens.inkSecondary,
                )
            }
        }

        val turkishText = annotation?.contextMeaningTr?.takeIf { it.isNotBlank() }
            ?: vocab?.meaningTr?.takeIf { it.isNotBlank() }
            ?: dict?.meaningTr?.takeIf { it.isNotBlank() }
        if (turkishText != null) {
            if (showTurkish) {
                Surface(shape = RoundedCornerShape(10.dp), color = tokens.elevated) {
                    Text(
                        "TR · $turkishText",
                        style = FluentTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = tokens.ink,
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                    )
                }
            } else {
                TextButton(
                    onClick = { showTurkish = true },
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Icon(Icons.Rounded.Translate, null, tint = tokens.terracotta, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Show Turkish meaning", color = tokens.terracotta)
                }
            }
        }

        if (effectiveVocabId != null) {
            FluentButton(
                text = if (isSaved) "In Vocabulary Review" else "Add to Vocabulary Review",
                variant = if (isSaved) ButtonVariant.Outlined else ButtonVariant.Primary,
                leadingIcon = {
                    Icon(
                        if (isSaved) Icons.Rounded.BookmarkAdded else Icons.Rounded.BookmarkAdd,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                },
                onClick = { onSaveWordToReview(effectiveVocabId) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun VocabularySection(
    article: ReadingArticle,
    viewModel: ReadingSessionViewModel,
    tts: OnDeviceTtsSpeaker,
    onLookupWord: (String) -> VocabItem?,
    savedToReviewWordIds: Set<String>,
    onSaveWordToReview: (String) -> Unit,
    wide: Boolean,
) {
    val tokens = LocalReadingTokens.current
    Column(
        Modifier.widthIn(max = 960.dp).fillMaxWidth().verticalScroll(rememberScrollState()).padding(bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Key Vocabulary", style = ReadingType.Title, color = tokens.ink)
            Text("${article.vocabularyAnnotations.size} item${if (article.vocabularyAnnotations.size == 1) "" else "s"}", style = ReadingType.Meta, color = tokens.inkSecondary)
        }
        if (article.vocabularyAnnotations.isEmpty()) {
            ReadingStateMessage(ReadingIllustration.EMPTY, "No article-specific vocabulary support is available for this text.", Modifier.align(Alignment.CenterHorizontally))
        }
        val columns = if (wide) 2 else 1
        article.vocabularyAnnotations.chunked(columns).forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                row.forEach { item ->
                    val matchedVocab = remember(item.word) { onLookupWord(item.word) }
                    val isSaved = matchedVocab != null && matchedVocab.id in savedToReviewWordIds
                    ReadingCard(modifier = Modifier.weight(1f)) {
                        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                Text(item.word, style = ReadingType.CardTitle, color = tokens.ink, modifier = Modifier.weight(1f))
                                IconButton(onClick = { tts.speak(item.word) }, modifier = Modifier.size(34.dp)) {
                                    Icon(Icons.AutoMirrored.Rounded.VolumeUp, "Pronounce", tint = tokens.terracotta, modifier = Modifier.size(18.dp))
                                }
                            }
                            Text(item.contextDefinitionEn, style = FluentTheme.typography.bodyMedium, color = tokens.inkSecondary)
                            if (matchedVocab != null) {
                                TextButton(
                                    onClick = { onSaveWordToReview(matchedVocab.id) },
                                    contentPadding = PaddingValues(0.dp),
                                ) {
                                    Icon(
                                        if (isSaved) Icons.Rounded.BookmarkAdded else Icons.Rounded.BookmarkAdd,
                                        null,
                                        tint = if (isSaved) tokens.sage else tokens.terracotta,
                                        modifier = Modifier.size(16.dp),
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text(
                                        if (isSaved) "Saved to Review" else "Add to Vocabulary Review",
                                        color = if (isSaved) tokens.sage else tokens.terracotta,
                                    )
                                }
                            }
                        }
                    }
                }
                repeat(columns - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }
        FlowRow(Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            FluentButton(text = "Back to Article", onClick = { viewModel.selectSection(ReadingSessionSection.ARTICLE) }, variant = ButtonVariant.Outlined)
            if (article.comprehensionQuestions.isNotEmpty()) FluentButton(text = "Continue to Questions", onClick = { viewModel.selectSection(ReadingSessionSection.QUESTIONS) })
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun QuestionsSection(
    state: ReadingSessionUiState,
    viewModel: ReadingSessionViewModel,
    wide: Boolean,
) {
    val tokens = LocalReadingTokens.current
    val article = state.article ?: return
    val question = state.currentQuestion
    if (question == null) {
        ReadingStateMessage(ReadingIllustration.EMPTY, "No comprehension questions are available for this article.")
        return
    }
    val submitted = question.id in state.submittedQuestionIds
    val total = article.comprehensionQuestions.size
    var showReferencePane by rememberSaveable(article.id) { mutableStateOf(true) }

    val questionColumn: @Composable (Modifier) -> Unit = { colModifier ->
        Column(
            colModifier.verticalScroll(rememberScrollState()).padding(top = 8.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Comprehension", style = ReadingType.Title, color = tokens.ink)
                    Text("Question ${state.questionIndex + 1} of $total", style = ReadingType.Meta, color = tokens.inkSecondary)
                }
                if (wide) {
                    FluentButton(
                        text = if (showReferencePane) "Hide article" else "Show article",
                        variant = ButtonVariant.Outlined,
                        onClick = { showReferencePane = !showReferencePane },
                    )
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(total) { i ->
                    Box(
                        Modifier.weight(1f).height(4.dp).clip(RoundedCornerShape(2.dp))
                            .background(if (i <= state.questionIndex) tokens.terracotta else tokens.border),
                    )
                }
            }
            Text(question.questionEn, style = ReadingType.Heading, color = tokens.ink, modifier = Modifier.padding(top = 12.dp, bottom = 4.dp))
            question.options.forEachIndexed { index, option ->
                QuestionOption(
                    label = ('A'.code + index).toChar().toString(),
                    text = option,
                    selected = state.selectedAnswers[question.id] == option,
                    submitted = submitted,
                    correct = option == question.correctAnswer,
                    onClick = { viewModel.selectAnswer(question.id, option) },
                )
            }
            if (submitted) QuestionFeedback(question, state.selectedAnswers[question.id].orEmpty(), state.answerCorrectness[question.id] == true)
            Row(Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1f)) {
                    if (!wide || !showReferencePane) {
                        FluentButton(text = "View article", onClick = { viewModel.selectSection(ReadingSessionSection.ARTICLE) }, variant = ButtonVariant.Outlined)
                    }
                    if (state.questionIndex > 0) FluentButton(text = "Previous question", onClick = viewModel::previousQuestion, variant = ButtonVariant.Outlined)
                }
                if (!submitted) {
                    FluentButton(text = "Check answer", onClick = viewModel::checkAnswer, enabled = state.selectedAnswers[question.id] != null)
                } else if (state.questionIndex == article.comprehensionQuestions.lastIndex) {
                    FluentButton(text = "Review Session", onClick = { viewModel.selectSection(ReadingSessionSection.REVIEW) })
                } else {
                    FluentButton(text = "Next", onClick = viewModel::nextQuestion)
                }
            }
        }
    }

    if (wide && showReferencePane) {
        Row(
            Modifier.widthIn(max = 1200.dp).fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            ReadingCard(
                modifier = Modifier.weight(0.92f).fillMaxSize().padding(top = 8.dp, bottom = 28.dp),
            ) {
                Column(
                    Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    ReadingEyebrow("Article Reference · ${article.cefrLevel}")
                    Text(article.title, style = ReadingType.Heading, color = tokens.ink)
                    Box(Modifier.fillMaxWidth().height(1.dp).background(tokens.border))
                    article.paragraphs.forEachIndexed { idx, para ->
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            val paraLabel = para.title?.takeIf { it.isNotBlank() }?.let { "${idx + 1}. $it" } ?: "Paragraph ${idx + 1}"
                            ReadingEyebrow(paraLabel, color = tokens.inkSecondary)
                            Text(para.contentEn, style = ReadingType.bodyForSize(17f), color = tokens.ink)
                        }
                    }
                }
            }
            questionColumn(Modifier.weight(1.08f).fillMaxSize())
        }
    } else {
        questionColumn(Modifier.widthIn(max = 720.dp).fillMaxWidth())
    }
}

@Composable
private fun QuestionOption(label: String, text: String, selected: Boolean, submitted: Boolean, correct: Boolean, onClick: () -> Unit) {
    val tokens = LocalReadingTokens.current
    val isWrongSelection = submitted && selected && !correct
    val isCorrectAnswer = submitted && correct
    val borderColor = when {
        isCorrectAnswer -> tokens.sage
        isWrongSelection -> tokens.error
        selected -> tokens.ink
        else -> tokens.border
    }
    val container = when {
        isCorrectAnswer -> tokens.sageContainer
        isWrongSelection -> tokens.errorContainer
        selected -> tokens.elevated
        else -> tokens.card
    }
    val stateText = when {
        isCorrectAnswer -> "Correct answer"
        isWrongSelection -> "Incorrect selection"
        selected -> "Selected"
        else -> "Answer option"
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, enabled = !submitted, role = Role.RadioButton, onClick = onClick)
            .semantics { contentDescription = "$label. $text. $stateText" },
        color = container,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(if (selected || isCorrectAnswer) 2.dp else 1.dp, borderColor),
    ) {
        Row(Modifier.padding(horizontal = 18.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Box(
                Modifier.size(32.dp).clip(CircleShape).background(if (selected) tokens.ink else tokens.elevated),
                contentAlignment = Alignment.Center,
            ) {
                Text(label, color = if (selected) tokens.card else tokens.ink, fontWeight = FontWeight.Bold, style = FluentTheme.typography.labelLarge)
            }
            Text(text, Modifier.weight(1f), color = tokens.ink, style = FluentTheme.typography.bodyLarge)
            if (isCorrectAnswer) Icon(Icons.Rounded.CheckCircle, "Correct", tint = tokens.sage)
            if (isWrongSelection) Icon(Icons.Rounded.Cancel, "Incorrect", tint = tokens.error)
        }
    }
}

@Composable
private fun QuestionFeedback(question: ReadingComprehensionQuestion, userAnswer: String, correct: Boolean) {
    val tokens = LocalReadingTokens.current
    Surface(
        color = if (correct) tokens.sageContainer else tokens.errorContainer,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, if (correct) tokens.sage else tokens.error),
    ) {
        Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    if (correct) Icons.Rounded.CheckCircle else Icons.Rounded.Cancel,
                    contentDescription = null,
                    tint = if (correct) tokens.sage else tokens.error,
                )
                Text(
                    if (correct) "Correct!" else "Not quite",
                    style = FluentTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (correct) tokens.sage else tokens.error,
                )
            }
            if (!correct) {
                Text("Your answer: $userAnswer", color = tokens.inkSecondary)
                Text("Correct answer: ${question.correctAnswer}", color = tokens.ink, fontWeight = FontWeight.SemiBold)
            }
            Text(question.explanationEn, color = tokens.ink)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ReviewSection(state: ReadingSessionUiState, viewModel: ReadingSessionViewModel, onFinish: () -> Unit) {
    val tokens = LocalReadingTokens.current
    val article = state.article ?: return
    val answered = state.submittedQuestionIds.size
    val incorrect = state.answerCorrectness.count { !it.value }
    Column(
        Modifier.widthIn(max = 720.dp).fillMaxWidth().verticalScroll(rememberScrollState()).padding(top = 8.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        ReadingIllustrationImage(
            ReadingIllustration.COMPLETE,
            modifier = Modifier.fillMaxWidth().aspectRatio(2.4f).clip(RoundedCornerShape(24.dp)),
        )
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Reading complete", style = ReadingType.Display, color = tokens.ink)
            Text(article.title, style = ReadingType.Heading, color = tokens.inkSecondary)
        }
        ReadingCard(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("${state.correctCount} / $answered correct", style = ReadingType.Title, color = tokens.ink)
                Text(
                    "$incorrect to revisit · ${article.vocabularyAnnotations.size} vocabulary item${if (article.vocabularyAnnotations.size == 1) "" else "s"}",
                    style = ReadingType.Meta,
                    color = tokens.inkSecondary,
                )
            }
        }
        if (article.comprehensionQuestions.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ReadingEyebrow("Question breakdown", color = tokens.inkSecondary)
                article.comprehensionQuestions.forEachIndexed { index, q ->
                    val isSubmitted = q.id in state.submittedQuestionIds
                    val isCorrect = state.answerCorrectness[q.id] == true
                    val statusLabel = when {
                        !isSubmitted -> "Unanswered"
                        isCorrect -> "Correct"
                        else -> "Revisit"
                    }
                    ReadingCard(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            viewModel.selectQuestion(index)
                            viewModel.selectSection(ReadingSessionSection.QUESTIONS)
                        },
                        onClickLabel = "Question ${index + 1}: $statusLabel. ${q.questionEn}",
                    ) {
                        Row(
                            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Text(
                                "Q${index + 1}",
                                style = ReadingType.Meta,
                                fontWeight = FontWeight.Bold,
                                color = tokens.inkSecondary,
                            )
                            Text(
                                q.questionEn,
                                style = FluentTheme.typography.bodyMedium,
                                color = tokens.ink,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f),
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                if (isSubmitted) {
                                    Icon(
                                        if (isCorrect) Icons.Rounded.CheckCircle else Icons.Rounded.Cancel,
                                        contentDescription = null,
                                        tint = if (isCorrect) tokens.sage else tokens.error,
                                        modifier = Modifier.size(16.dp),
                                    )
                                }
                                Text(
                                    statusLabel,
                                    style = ReadingType.Meta,
                                    color = when {
                                        !isSubmitted -> tokens.inkMuted
                                        isCorrect -> tokens.sage
                                        else -> tokens.error
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            FluentButton(text = "Finish", onClick = onFinish)
            if (incorrect > 0) FluentButton(text = "Review mistakes", onClick = viewModel::reviewMistakes, variant = ButtonVariant.Outlined)
            FluentButton(text = "Read again", onClick = viewModel::readAgain, variant = ButtonVariant.Outlined)
        }
    }
}

// endregion
