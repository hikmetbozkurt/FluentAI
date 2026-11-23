package com.seanora.fluentai.feature.reading

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.seanora.fluentai.core.designsystem.theme.FluentTheme
import com.seanora.fluentai.core.model.ReadingArticle

/** Width breakpoints for Reading layouts (tablet-first, phone as fallback). */
internal object ReadingBreakpoints {
    val Medium: Dp = 600.dp
    val Expanded: Dp = 840.dp
    val WideReader: Dp = 1000.dp
}

internal val ReadingCardShape = RoundedCornerShape(20.dp)
internal val ReadingImageShape = RoundedCornerShape(16.dp)

@Composable
internal fun ReadingBackButton(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val tokens = LocalReadingTokens.current
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
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
            Modifier.padding(start = 12.dp, end = 18.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null, tint = tokens.ink, modifier = Modifier.size(20.dp))
            Text(label, style = FluentTheme.typography.labelLarge, color = tokens.ink)
        }
    }
}

/** Small uppercase label above a title. */
@Composable
internal fun ReadingEyebrow(text: String, modifier: Modifier = Modifier, color: androidx.compose.ui.graphics.Color = LocalReadingTokens.current.terracotta) {
    Text(text.uppercase(), style = ReadingType.Eyebrow, color = color, modifier = modifier)
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
    ReadingCover(articleId = articleId, modifier = modifier.clip(ReadingImageShape))
}

internal fun String.readingDisplayName() = replace('_', ' ').replaceFirstChar { it.uppercase() }

internal fun ReadingArticle.levelAndTopic() = "$cefrLevel · ${category.readingDisplayName()}"

internal fun ReadingArticle.fullMeta() =
    "$cefrLevel · ${category.readingDisplayName()} · $wordCount words · ~$estimatedReadingMinutes min"
