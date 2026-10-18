package com.seanora.fluentai.feature.vocabulary

import androidx.compose.foundation.background
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.designsystem.kit.FluentActionTile
import com.seanora.fluentai.core.designsystem.kit.FluentPageHeader
import com.seanora.fluentai.core.designsystem.kit.FluentSectionTitle
import com.seanora.fluentai.core.designsystem.kit.LearningModule
import com.seanora.fluentai.core.model.VocabularyFeatureState

@Composable
fun VocabularyDashboardScreen(
    onNavigateBack: () -> Unit,
    onContinue: (VocabularyFeatureState) -> Unit,
    onOpenWord: (String) -> Unit,
    onSmartPractice: () -> Unit,
    onOpenLibrary: () -> Unit,
    onOpenCollections: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VocabularyFeatureViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Column(
        modifier
            .fillMaxSize()
            .background(DashboardColors.BackgroundPrimary)
            .verticalScroll(rememberScrollState())
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        FluentPageHeader(
            title = "Vocabulary",
            module = LearningModule.VOCABULARY,
            eyebrow = "Learn · Vocabulary",
            subtitle = "Build a precise, useful English vocabulary.",
            onBack = onNavigateBack,
            backLabel = "Learn",
        )
        state.featureState.resumeDestination?.let {
            VocabularyHomeCard(
                "⏯️",
                "Continue Learning",
                state.featureState.resumeContextLabel(),
                "Continue",
                emphasized = true,
                onClick = { onContinue(state.featureState) },
            )
        }
        state.dailyWord?.let { word ->
            VocabularyHomeCard(
                "🌟",
                "Word of Day · ${word.headword}",
                "${word.cefrLevel} · ${word.partOfSpeech}\n${word.definitionEn}",
                "Explore word",
                onClick = { onOpenWord(word.id) },
            )
        }
        VocabularyHomeCard(
            "🧠",
            "Smart Practice",
            "Continue with a focused activity selected from your real review and learning state.",
            "Start practice",
            emphasized = true,
            onClick = onSmartPractice,
        )
        FluentSectionTitle(
            title = "Explore",
            emoji = "🧭",
            subtitle = "Browse every word or dive into a topic",
            accent = LearningModule.VOCABULARY.accent,
        )
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            if (maxWidth < 620.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ExploreAction("🗂️", "Vocabulary Library", "Browse all ${state.allItems.size} words", onOpenLibrary, Modifier.fillMaxWidth())
                    ExploreAction("🏷️", "Collections", "Explore ${state.collections.size} real topics", onOpenCollections, Modifier.fillMaxWidth())
                }
            } else {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    ExploreAction("🗂️", "Vocabulary Library", "Browse all ${state.allItems.size} words", onOpenLibrary, Modifier.weight(1f))
                    ExploreAction("🏷️", "Collections", "Explore ${state.collections.size} real topics", onOpenCollections, Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun VocabularyHomeCard(emoji: String, title: String, detail: String, action: String, emphasized: Boolean = false, onClick: () -> Unit) {
    FluentActionTile(
        title = title,
        detail = detail,
        module = LearningModule.VOCABULARY,
        emoji = emoji,
        actionLabel = action,
        emphasized = emphasized,
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun ExploreAction(emoji: String, title: String, detail: String, onClick: () -> Unit, modifier: Modifier) {
    FluentActionTile(
        title = title,
        detail = detail,
        module = LearningModule.VOCABULARY,
        emoji = emoji,
        onClick = onClick,
        modifier = modifier,
    )
}

private fun VocabularyFeatureState.resumeContextLabel(): String = when (resumeDestination) {
    com.seanora.fluentai.core.model.VocabularyResumeDestination.WORD_OF_DAY -> "Return to today’s word"
    com.seanora.fluentai.core.model.VocabularyResumeDestination.LIBRARY -> "Return to your selected word"
    com.seanora.fluentai.core.model.VocabularyResumeDestination.FLASH_CARDS -> "Continue Flash Cards"
    com.seanora.fluentai.core.model.VocabularyResumeDestination.COLLECTIONS -> "Return to your selected collection"
    com.seanora.fluentai.core.model.VocabularyResumeDestination.PRACTICE_LABS -> "Continue ${resumePracticeMode?.title ?: "Practice"}"
    null -> ""
}
