package com.seanora.fluentai.feature.reading

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.BookmarkAdd
import androidx.compose.material.icons.rounded.BookmarkAdded
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.CloudDone
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
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
import com.seanora.fluentai.core.audio.OnDeviceTtsSpeaker
import com.seanora.fluentai.core.audio.TtsAccent
import com.seanora.fluentai.core.audio.rememberOnDeviceTtsSpeaker
import com.seanora.fluentai.core.designsystem.components.ButtonVariant
import com.seanora.fluentai.core.designsystem.components.FluentButton
import com.seanora.fluentai.core.designsystem.components.FluentSelectableChip
import com.seanora.fluentai.core.designsystem.theme.FluentTheme
import com.seanora.fluentai.data.dictionary.DictionaryEntry
import com.seanora.fluentai.data.feed.LiveReadingArticle

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LiveReadingFeedScreen(
    onNavigateBack: () -> Unit,
    onOpenLiveArticle: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LiveReadingFeedViewModel = hiltViewModel(),
    supportViewModel: ReadingReaderSupportViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val surfaceStyle by supportViewModel.surfaceStyle.collectAsStateWithLifecycle()

    ReadingTheme(style = surfaceStyle) {
        val tokens = LocalReadingTokens.current
        BoxWithConstraints(modifier.fillMaxSize().background(tokens.paper)) {
            val horizontalPadding = if (maxWidth >= ReadingBreakpoints.Medium) 40.dp else 20.dp
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 300.dp),
                contentPadding = PaddingValues(start = horizontalPadding, end = horizontalPadding, top = 24.dp, bottom = 40.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            ReadingBackButton(label = "Reading", onClick = onNavigateBack)
                            FluentButton(
                                text = if (state.isRefreshing) "Refreshing…" else "Refresh Feed",
                                variant = ButtonVariant.Outlined,
                                enabled = !state.isRefreshing,
                                leadingIcon = { Icon(Icons.Rounded.Refresh, null, Modifier.size(18.dp)) },
                                onClick = viewModel::refresh,
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            ReadingEyebrow("Live Real-World English · Auto-Cached Offline")
                            Text("Discover Live Articles", style = ReadingType.Display, color = tokens.ink)
                            Text(
                                "Fresh journalism from The Conversation (CC BY-ND) and The Guardian Open Platform, cached locally for offline reading with 107k-word tap lookup.",
                                style = FluentTheme.typography.bodyMedium,
                                color = tokens.inkSecondary,
                            )
                        }

                        state.statusMessage?.let { msg ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = tokens.elevated,
                                border = BorderStroke(1.dp, tokens.border),
                            ) {
                                Row(
                                    Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                ) {
                                    Icon(
                                        if (state.isFromCache) Icons.Rounded.CloudOff else Icons.Rounded.CloudDone,
                                        null,
                                        tint = tokens.terracotta,
                                        modifier = Modifier.size(18.dp),
                                    )
                                    Text(msg, style = ReadingType.Meta, color = tokens.inkSecondary)
                                }
                            }
                        }

                        // Filters
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            listOf<String?>(null, "A2", "B1", "B2", "C1", "C2").forEach { level ->
                                FluentSelectableChip(
                                    text = level ?: "All Levels",
                                    selected = state.selectedLevel == level,
                                    onClick = { viewModel.selectLevel(level) },
                                    role = Role.RadioButton,
                                )
                            }
                            state.availableCategories.forEach { cat ->
                                FluentSelectableChip(
                                    text = cat,
                                    selected = state.selectedCategory == cat,
                                    onClick = {
                                        viewModel.selectCategory(if (state.selectedCategory == cat) null else cat)
                                    },
                                    role = Role.Checkbox,
                                )
                            }
                        }
                    }
                }

                when {
                    state.isLoading -> item(span = { GridItemSpan(maxLineSpan) }) {
                        Box(Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = tokens.terracotta)
                        }
                    }
                    state.visibleArticles.isEmpty() -> item(span = { GridItemSpan(maxLineSpan) }) {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            ReadingStateMessage(
                                ReadingIllustration.EMPTY,
                                "No live articles available yet. Connect to the internet and tap Refresh Feed to cache fresh real-world articles.",
                            )
                        }
                    }
                    else -> items(state.visibleArticles, key = { it.id }) { article ->
                        LiveArticleCard(article) { onOpenLiveArticle(article.id) }
                    }
                }
            }
        }
    }
}

@Composable
private fun LiveArticleCard(article: LiveReadingArticle, onClick: () -> Unit) {
    val tokens = LocalReadingTokens.current
    ReadingCard(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Rounded.Public, null, tint = tokens.terracotta, modifier = Modifier.size(16.dp))
                    Text("${article.sourceName} · ${article.category}", style = ReadingType.Meta, color = tokens.terracotta)
                }
                Surface(shape = RoundedCornerShape(50), color = tokens.elevated) {
                    Text(
                        article.cefrLevel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = tokens.ink,
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp),
                    )
                }
            }
            Text(
                article.title,
                style = ReadingType.CardTitle,
                color = tokens.ink,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                article.summary,
                style = FluentTheme.typography.bodySmall,
                color = tokens.inkSecondary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("~${article.estimatedMinutes} min · ${article.wordCount} words", style = ReadingType.Meta, color = tokens.inkMuted)
                if (article.publishedAt.isNotBlank()) {
                    Text(article.publishedAt, style = ReadingType.Meta, color = tokens.inkMuted)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LiveReadingArticleScreen(
    articleId: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    feedViewModel: LiveReadingFeedViewModel = hiltViewModel(),
    supportViewModel: ReadingReaderSupportViewModel = hiltViewModel(),
) {
    val feedState by feedViewModel.uiState.collectAsStateWithLifecycle()
    val surfaceStyle by supportViewModel.surfaceStyle.collectAsStateWithLifecycle()
    val fontSizeSp by supportViewModel.fontSizeSp.collectAsStateWithLifecycle()
    val activeDictEntry by supportViewModel.activeDictionaryEntry.collectAsStateWithLifecycle()
    val savedToReviewIds by supportViewModel.savedToReviewWordIds.collectAsStateWithLifecycle()
    val tts = rememberOnDeviceTtsSpeaker()
    val isSpeaking by tts.isSpeaking.collectAsStateWithLifecycle()
    val activeUtteranceTag by tts.activeUtteranceTag.collectAsStateWithLifecycle()

    var selectedWord by remember(articleId) { mutableStateOf<String?>(null) }

    LaunchedEffect(articleId) {
        supportViewModel.clearDictionaryEntry()
        feedViewModel.openArticle(articleId)
    }

    ReadingTheme(style = surfaceStyle) {
        val tokens = LocalReadingTokens.current
        val article = feedState.activeArticle
        val scrollState = rememberScrollState()
        val scrollProgress = if (scrollState.maxValue > 0) {
            (scrollState.value.toFloat() / scrollState.maxValue.toFloat()).coerceIn(0f, 1f)
        } else 0f

        BoxWithConstraints(modifier.fillMaxSize().background(tokens.paper)) {
            val wide = maxWidth >= ReadingBreakpoints.WideReader
            val horizontalPadding = if (maxWidth >= ReadingBreakpoints.Medium) 40.dp else 20.dp

            if (article == null) {
                ReadingStateMessage(
                    illustration = ReadingIllustration.EMPTY,
                    message = "This cached article is no longer available.",
                    modifier = Modifier.align(Alignment.Center),
                    action = { FluentButton(text = "Back to Live Feed", onClick = onNavigateBack, variant = ButtonVariant.Outlined) },
                )
                return@BoxWithConstraints
            }

            Column(Modifier.fillMaxSize()) {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = horizontalPadding, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    ReadingBackButton(label = "Back to Live Feed", onClick = { tts.stop(); onNavigateBack() })
                    Surface(shape = RoundedCornerShape(50), color = tokens.elevated) {
                        Text(
                            "${article.sourceName} · ${article.cefrLevel}",
                            style = ReadingType.Meta,
                            color = tokens.ink,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        )
                    }
                }

                LinearProgressIndicator(
                    progress = { scrollProgress },
                    modifier = Modifier.fillMaxWidth().height(3.dp),
                    color = tokens.terracotta,
                    trackColor = tokens.border,
                )

                Row(
                    Modifier.weight(1f).fillMaxWidth().padding(horizontal = horizontalPadding),
                    horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
                ) {
                    Column(
                        Modifier
                            .weight(1f, fill = false)
                            .widthIn(max = 740.dp)
                            .fillMaxWidth()
                            .verticalScroll(scrollState)
                            .padding(top = 14.dp, bottom = 40.dp),
                    ) {
                        val speakingThis = isSpeaking && activeUtteranceTag == "live:${article.id}"
                        ReaderComfortToolbar(
                            surfaceStyle = surfaceStyle,
                            fontSizeSp = fontSizeSp,
                            isSpeakingArticle = speakingThis,
                            onSelectStyle = supportViewModel::setSurfaceStyle,
                            onDecreaseFont = supportViewModel::decreaseFontSize,
                            onIncreaseFont = supportViewModel::increaseFontSize,
                            onToggleReadAloud = {
                                if (speakingThis) tts.stop()
                                else tts.speak(article.paragraphs.joinToString("\n\n"), TtsAccent.US, 0.92f, "live:${article.id}")
                            },
                            progressLabel = "${(scrollProgress * 100).toInt()}% read · ~${article.estimatedMinutes} min",
                            stopLabel = "Stop",
                        )

                        Spacer(Modifier.height(22.dp))
                        ReadingEyebrow("${article.cefrLevel} · ${article.category} · ${article.wordCount} words · ~${article.estimatedMinutes} min")
                        Text(article.title, style = ReadingType.Display, color = tokens.ink, modifier = Modifier.padding(top = 8.dp))
                        Text(
                            "Source: ${article.sourceName} (${article.sourceLicense}) · Tap any word for instant 107k offline lookup.",
                            style = ReadingType.Meta,
                            color = tokens.inkMuted,
                            modifier = Modifier.padding(top = 10.dp),
                        )
                        Box(Modifier.padding(vertical = 20.dp).fillMaxWidth().height(1.dp).background(tokens.border))

                        val bodyStyle = ReadingType.bodyForSize(fontSizeSp)
                        article.paragraphs.forEach { para ->
                            Text(
                                text = tappablePlainParagraph(para, selectedWord, tokens) { word ->
                                    selectedWord = word
                                    supportViewModel.resolveDictionaryEntry(word)
                                },
                                style = bodyStyle,
                                color = tokens.ink,
                                modifier = Modifier.padding(vertical = 10.dp),
                            )
                        }
                    }

                    if (wide) {
                        LiveDictionarySidePanel(
                            selectedWord = selectedWord,
                            entry = activeDictEntry,
                            savedIds = savedToReviewIds,
                            tts = tts,
                            onSaveToReview = supportViewModel::saveWordToVocabularyReview,
                            onClose = { selectedWord = null; supportViewModel.clearDictionaryEntry() },
                            modifier = Modifier.width(330.dp).padding(top = 14.dp),
                        )
                    }
                }
            }

            if (!wide && selectedWord != null) {
                val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                ModalBottomSheet(
                    onDismissRequest = { selectedWord = null; supportViewModel.clearDictionaryEntry() },
                    sheetState = sheetState,
                    containerColor = tokens.card,
                ) {
                    LiveDictionaryDetail(
                        selectedWord = selectedWord!!,
                        entry = activeDictEntry,
                        savedIds = savedToReviewIds,
                        tts = tts,
                        onSaveToReview = supportViewModel::saveWordToVocabularyReview,
                        onClose = { selectedWord = null; supportViewModel.clearDictionaryEntry() },
                        modifier = Modifier.padding(start = 28.dp, end = 28.dp, bottom = 36.dp),
                    )
                }
            }
        }
    }
}

private fun tappablePlainParagraph(
    text: String,
    selectedWord: String?,
    tokens: ReadingSurfaceTokens,
    onWordClick: (String) -> Unit,
): AnnotatedString {
    val wordRegex = Regex("[\\p{L}]+(?:['’-][\\p{L}]+)*")
    return buildAnnotatedString {
        var cursor = 0
        wordRegex.findAll(text).forEach { m ->
            if (m.range.first > cursor) append(text.substring(cursor, m.range.first))
            val tok = m.value
            val isSelected = selectedWord != null && tok.equals(selectedWord, ignoreCase = true)
            withLink(
                LinkAnnotation.Clickable(
                    tag = tok,
                    styles = TextLinkStyles(
                        style = if (isSelected) SpanStyle(background = tokens.sand, fontWeight = FontWeight.SemiBold) else SpanStyle(),
                    ),
                    linkInteractionListener = { onWordClick(tok) },
                ),
            ) { append(tok) }
            cursor = m.range.last + 1
        }
        if (cursor < text.length) append(text.substring(cursor))
    }
}

@Composable
private fun LiveDictionarySidePanel(
    selectedWord: String?,
    entry: DictionaryEntry?,
    savedIds: Set<String>,
    tts: OnDeviceTtsSpeaker,
    onSaveToReview: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tokens = LocalReadingTokens.current
    ReadingCard(modifier = modifier) {
        if (selectedWord == null) {
            Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ReadingEyebrow("Offline Dictionary", color = tokens.inkSecondary)
                Text(
                    "Tap any word in this live article to view its CEFR level, IPA pronunciation, definition, and example sentence.",
                    style = FluentTheme.typography.bodyMedium,
                    color = tokens.inkSecondary,
                )
            }
        } else {
            LiveDictionaryDetail(selectedWord, entry, savedIds, tts, onSaveToReview, onClose, Modifier.padding(22.dp))
        }
    }
}

@Composable
private fun LiveDictionaryDetail(
    selectedWord: String,
    entry: DictionaryEntry?,
    savedIds: Set<String>,
    tts: OnDeviceTtsSpeaker,
    onSaveToReview: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tokens = LocalReadingTokens.current
    var showTurkish by remember(selectedWord) { mutableStateOf(false) }
    val title = entry?.displayWord ?: selectedWord
    val vocabId = entry?.vocabId?.takeIf { it.isNotBlank() }
    val isSaved = vocabId != null && vocabId in savedIds

    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                val meta = listOfNotNull("Offline Dictionary", entry?.cefrLevel?.takeIf { it.isNotBlank() }, entry?.partOfSpeech?.takeIf { it.isNotBlank() })
                ReadingEyebrow(meta.joinToString(" · "))
                Text(title, style = ReadingType.Title, color = tokens.ink)
                entry?.phonetic?.takeIf { it.isNotBlank() }?.let { Text(it, style = ReadingType.Meta, color = tokens.inkSecondary) }
            }
            IconButton(onClick = onClose, modifier = Modifier.size(40.dp)) {
                Icon(Icons.Rounded.Close, "Close", tint = tokens.inkSecondary)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(onClick = { tts.speak(title, TtsAccent.US) }, shape = RoundedCornerShape(50), color = tokens.elevated) {
                Row(Modifier.padding(horizontal = 12.dp, vertical = 7.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Icon(Icons.AutoMirrored.Rounded.VolumeUp, null, tint = tokens.terracotta, modifier = Modifier.size(16.dp))
                    Text("US", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = tokens.ink)
                }
            }
            Surface(onClick = { tts.speak(title, TtsAccent.UK) }, shape = RoundedCornerShape(50), color = tokens.elevated) {
                Row(Modifier.padding(horizontal = 12.dp, vertical = 7.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Icon(Icons.AutoMirrored.Rounded.VolumeUp, null, tint = tokens.terracotta, modifier = Modifier.size(16.dp))
                    Text("UK", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = tokens.ink)
                }
            }
        }
        if (entry != null) {
            Text(entry.definitionEn, style = FluentTheme.typography.bodyLarge, color = tokens.ink)
            if (entry.exampleEn.isNotBlank()) {
                Surface(shape = RoundedCornerShape(10.dp), color = tokens.elevated) {
                    Text("“${entry.exampleEn}”", style = FluentTheme.typography.bodySmall, color = tokens.inkSecondary, modifier = Modifier.fillMaxWidth().padding(10.dp))
                }
            }
            if (entry.meaningTr.isNotBlank()) {
                if (showTurkish) {
                    Surface(shape = RoundedCornerShape(10.dp), color = tokens.elevated) {
                        Text("TR · ${entry.meaningTr}", style = FluentTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = tokens.ink, modifier = Modifier.fillMaxWidth().padding(12.dp))
                    }
                } else {
                    TextButton(onClick = { showTurkish = true }, contentPadding = PaddingValues(0.dp)) {
                        Icon(Icons.Rounded.Translate, null, tint = tokens.terracotta, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Show Turkish meaning", color = tokens.terracotta)
                    }
                }
            }
        } else {
            Text("No offline dictionary entry found for \"$selectedWord\".", style = FluentTheme.typography.bodyMedium, color = tokens.inkSecondary)
        }
        if (vocabId != null) {
            FluentButton(
                text = if (isSaved) "In Vocabulary Review" else "Add to Vocabulary Review",
                variant = if (isSaved) ButtonVariant.Outlined else ButtonVariant.Primary,
                leadingIcon = { Icon(if (isSaved) Icons.Rounded.BookmarkAdded else Icons.Rounded.BookmarkAdd, null, Modifier.size(18.dp)) },
                onClick = { onSaveToReview(vocabId) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
