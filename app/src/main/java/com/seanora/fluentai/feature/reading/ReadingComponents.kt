package com.seanora.fluentai.feature.reading

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seanora.fluentai.app.navigation.ReadingSessionMode
import com.seanora.fluentai.core.designsystem.theme.FluentTheme
import com.seanora.fluentai.core.model.ReadingArticle

/** Width breakpoints for Reading layouts (tablet-first, phone as fallback). */
internal object ReadingBreakpoints {
    val Medium: Dp = 600.dp
    val Expanded: Dp = 840.dp
    val WideReader: Dp = 1000.dp
}

internal val ReadingCardShape = RoundedCornerShape(16.dp)
internal val ReadingImageShape = RoundedCornerShape(12.dp)

@Composable
internal fun ReadingBackButton(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val tokens = LocalReadingTokens.current
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = tokens.card,
        border = BorderStroke(1.dp, tokens.border),
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .semantics {
                role = Role.Button
                contentDescription = label
            },
    ) {
        Row(
            Modifier.padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null, tint = tokens.ink, modifier = Modifier.size(20.dp))
            Text(label, style = FluentTheme.typography.labelLarge, color = tokens.ink)
        }
    }
}

/** Small sentence-case label above a title. */
@Composable
internal fun ReadingEyebrow(text: String, modifier: Modifier = Modifier, color: androidx.compose.ui.graphics.Color = LocalReadingTokens.current.terracotta) {
    Text(text, style = ReadingType.Eyebrow, color = color, modifier = modifier)
}

/** Plain card surface used across Reading. */
@Composable
internal fun ReadingCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    color: androidx.compose.ui.graphics.Color = LocalReadingTokens.current.card,
    content: @Composable () -> Unit,
) {
    val tokens = LocalReadingTokens.current
    val semanticsModifier = if (onClickLabel != null) Modifier.semantics { contentDescription = onClickLabel } else Modifier
    if (onClick != null) {
        Surface(onClick = onClick, shape = ReadingCardShape, color = color, border = BorderStroke(1.dp, tokens.border), modifier = modifier.then(semanticsModifier)) { content() }
    } else {
        Surface(shape = ReadingCardShape, color = color, border = BorderStroke(1.dp, tokens.border), modifier = modifier) { content() }
    }
}

/** Centered illustration + message, used for empty and error states. */
@Composable
internal fun ReadingStateMessage(
    illustration: ReadingIllustration,
    message: String,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null,
) {
    val tokens = LocalReadingTokens.current
    Column(
        modifier.widthIn(max = 420.dp).fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ReadingIllustrationImage(
            illustration = illustration,
            contentScale = ContentScale.Fit,
            modifier = Modifier.widthIn(max = 220.dp).fillMaxWidth().aspectRatio(1f),
        )
        Text(message, style = FluentTheme.typography.bodyLarge, color = tokens.inkSecondary, textAlign = TextAlign.Center)
        action?.invoke()
    }
}

@Composable
internal fun ReadingCoverImage(articleId: String, modifier: Modifier = Modifier) {
    val tokens = LocalReadingTokens.current
    Surface(
        shape = ReadingImageShape,
        color = tokens.elevated,
        border = BorderStroke(1.dp, tokens.border),
        modifier = modifier,
    ) {
        ReadingCover(articleId = articleId, modifier = Modifier.fillMaxSize())
    }
}

@Composable
internal fun ReadingSurfaceStyleStrip(
    surfaceStyle: ReadingSurfaceStyle,
    onSelectStyle: (ReadingSurfaceStyle) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tokens = LocalReadingTokens.current
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ReadingSurfaceStyle.entries.forEach { style ->
            val active = style == surfaceStyle
            Surface(
                onClick = { onSelectStyle(style) },
                shape = RoundedCornerShape(12.dp),
                color = if (active) tokens.terracotta else tokens.elevated,
                border = if (active) null else BorderStroke(1.dp, tokens.border),
                modifier = Modifier
                    .defaultMinSize(minHeight = 44.dp)
                    .semantics {
                        role = Role.RadioButton
                        selected = active
                        contentDescription = "${style.label} reading surface"
                    },
            ) {
                Row(
                    Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        style.label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (active) tokens.onAccent else tokens.inkSecondary,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ReaderComfortToolbar(
    surfaceStyle: ReadingSurfaceStyle,
    fontSizeSp: Float,
    isSpeakingArticle: Boolean,
    onSelectStyle: (ReadingSurfaceStyle) -> Unit,
    onDecreaseFont: () -> Unit,
    onIncreaseFont: () -> Unit,
    onToggleReadAloud: () -> Unit,
    modifier: Modifier = Modifier,
    readingMode: ReadingSessionMode? = null,
    onSelectMode: ((ReadingSessionMode) -> Unit)? = null,
    progressLabel: String? = null,
    listenLabel: String = "Listen",
    stopLabel: String = "Stop Audio",
) {
    val tokens = LocalReadingTokens.current
    val canDecrease = fontSizeSp > 16f
    val canIncrease = fontSizeSp < 26f
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = tokens.card,
        border = BorderStroke(1.dp, tokens.border),
    ) {
        FlowRow(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            itemVerticalAlignment = Alignment.CenterVertically,
        ) {
            ReadingSurfaceStyleStrip(
                surfaceStyle = surfaceStyle,
                onSelectStyle = onSelectStyle,
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    onClick = onDecreaseFont,
                    enabled = canDecrease,
                    shape = RoundedCornerShape(12.dp),
                    color = if (canDecrease) tokens.elevated else tokens.elevated.copy(alpha = 0.45f),
                    border = BorderStroke(1.dp, tokens.border),
                    modifier = Modifier
                        .defaultMinSize(minWidth = 44.dp, minHeight = 44.dp)
                        .semantics { contentDescription = "Decrease font size" },
                ) {
                    Row(
                        Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            "A−",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (canDecrease) tokens.ink else tokens.inkMuted,
                        )
                    }
                }
                Text(
                    "${fontSizeSp.toInt()}sp",
                    style = ReadingType.Meta,
                    color = tokens.inkSecondary,
                    modifier = Modifier.padding(horizontal = 4.dp),
                )
                Surface(
                    onClick = onIncreaseFont,
                    enabled = canIncrease,
                    shape = RoundedCornerShape(12.dp),
                    color = if (canIncrease) tokens.elevated else tokens.elevated.copy(alpha = 0.45f),
                    border = BorderStroke(1.dp, tokens.border),
                    modifier = Modifier
                        .defaultMinSize(minWidth = 44.dp, minHeight = 44.dp)
                        .semantics { contentDescription = "Increase font size" },
                ) {
                    Row(
                        Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            "A+",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (canIncrease) tokens.ink else tokens.inkMuted,
                        )
                    }
                }
            }

            if (readingMode != null && onSelectMode != null) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    listOf(
                        ReadingSessionMode.STANDARD to "Standard",
                        ReadingSessionMode.GUIDED to "Guided",
                    ).forEach { (modeOption, label) ->
                        val active = readingMode == modeOption
                        Surface(
                            onClick = { onSelectMode(modeOption) },
                            shape = RoundedCornerShape(12.dp),
                            color = if (active) tokens.ink else tokens.elevated,
                            border = if (active) null else BorderStroke(1.dp, tokens.border),
                            modifier = Modifier
                                .defaultMinSize(minHeight = 44.dp)
                                .semantics {
                                    role = Role.RadioButton
                                    selected = active
                                    contentDescription = "$label reading mode"
                                },
                        ) {
                            Row(
                                Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    label,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (active) tokens.card else tokens.inkSecondary,
                                )
                            }
                        }
                    }
                }
            }

            Surface(
                onClick = onToggleReadAloud,
                shape = RoundedCornerShape(12.dp),
                color = if (isSpeakingArticle) tokens.terracotta else tokens.elevated,
                border = if (isSpeakingArticle) null else BorderStroke(1.dp, tokens.border),
                modifier = Modifier.defaultMinSize(minHeight = 44.dp),
            ) {
                Row(
                    Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(
                        if (isSpeakingArticle) Icons.Rounded.Stop else Icons.AutoMirrored.Rounded.VolumeUp,
                        contentDescription = null,
                        tint = if (isSpeakingArticle) tokens.onAccent else tokens.ink,
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        if (isSpeakingArticle) stopLabel else listenLabel,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isSpeakingArticle) tokens.onAccent else tokens.ink,
                    )
                }
            }

            if (!progressLabel.isNullOrBlank()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = tokens.elevated,
                ) {
                    Text(
                        progressLabel,
                        style = ReadingType.Meta,
                        color = tokens.inkSecondary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    )
                }
            }
        }
    }
}

internal fun String.readingDisplayName() = replace('_', ' ').replaceFirstChar { it.uppercase() }

internal fun ReadingArticle.levelAndTopic() = "$cefrLevel · ${category.readingDisplayName()}"

internal fun ReadingArticle.fullMeta() =
    "$cefrLevel · ${category.readingDisplayName()} · $wordCount words · ~$estimatedReadingMinutes min"
