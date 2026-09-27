package com.seanora.fluentai.feature.vocabulary

import androidx.compose.foundation.background
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
    Column(modifier.fillMaxSize().background(DashboardColors.BackgroundPrimary).padding(32.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back to Learn", tint = DashboardColors.PrimaryPinkDark) }
            Column {
                Text("Vocabulary", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary)
                Text("Build a precise, useful English vocabulary.", color = DashboardColors.TextSecondary)
            }
        }
        state.featureState.resumeDestination?.let {
            VocabularyHomeCard(
                "Continue Learning",
                state.featureState.resumeContextLabel(),
                "Continue",
                emphasized = true,
                onClick = { onContinue(state.featureState) },
            )
        }
        state.dailyWord?.let { word ->
            VocabularyHomeCard(
                "Word of Day · ${word.headword}",
                "${word.cefrLevel} · ${word.partOfSpeech}\n${word.definitionEn}",
                "Explore word",
                onClick = { onOpenWord(word.id) },
            )
        }
        VocabularyHomeCard(
            "Smart Practice",
            "Continue with a focused activity selected from your real review and learning state.",
            "Start practice",
            emphasized = true,
            onClick = onSmartPractice,
        )
        Text("Explore", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary)
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            if (maxWidth < 620.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ExploreAction("Vocabulary Library", "Browse all ${state.allItems.size} words", onOpenLibrary, Modifier.fillMaxWidth())
                    ExploreAction("Collections", "Explore ${state.collections.size} real topics", onOpenCollections, Modifier.fillMaxWidth())
                }
            } else {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    ExploreAction("Vocabulary Library", "Browse all ${state.allItems.size} words", onOpenLibrary, Modifier.weight(1f))
                    ExploreAction("Collections", "Explore ${state.collections.size} real topics", onOpenCollections, Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun VocabularyHomeCard(title: String, detail: String, action: String, emphasized: Boolean = false, onClick: () -> Unit) {
    Surface(Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(20.dp), color = if (emphasized) DashboardColors.SurfacePink else Color.White) {
        Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(title, fontSize = if (emphasized) 21.sp else 18.sp, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary)
                Text(detail, color = DashboardColors.TextSecondary, maxLines = 2)
            }
            Text("$action →", fontWeight = FontWeight.Bold, color = DashboardColors.PrimaryPinkDark)
        }
    }
}

@Composable
private fun ExploreAction(title: String, detail: String, onClick: () -> Unit, modifier: Modifier) {
    Surface(modifier.clickable(onClick = onClick), shape = RoundedCornerShape(16.dp), color = Color.White) {
        Row(Modifier.padding(17.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary)
                Text(detail, fontSize = 13.sp, color = DashboardColors.TextSecondary)
            }
            Icon(Icons.AutoMirrored.Rounded.ArrowForward, null, tint = DashboardColors.PrimaryPinkDark)
        }
    }
}

private fun VocabularyFeatureState.resumeContextLabel(): String = when (resumeDestination) {
    com.seanora.fluentai.core.model.VocabularyResumeDestination.WORD_OF_DAY -> "Return to today’s word"
    com.seanora.fluentai.core.model.VocabularyResumeDestination.LIBRARY -> "Return to your selected word"
    com.seanora.fluentai.core.model.VocabularyResumeDestination.FLASH_CARDS -> "Continue Flash Cards"
    com.seanora.fluentai.core.model.VocabularyResumeDestination.COLLECTIONS -> "Return to your selected collection"
    com.seanora.fluentai.core.model.VocabularyResumeDestination.PRACTICE_LABS -> "Continue ${resumePracticeMode?.title ?: "Practice"}"
    null -> ""
}
