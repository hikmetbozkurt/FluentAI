package com.seanora.fluentai.feature.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.seanora.fluentai.core.designsystem.dashboard.*
import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.domain.progress.ProgressDashboardData
import com.seanora.fluentai.domain.progress.SkillProficiency
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun ProgressScreen(modifier: Modifier = Modifier, viewModel: ProgressViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    ProgressScreenContent(uiState, viewModel::selectSkill, modifier)
}

@Composable
fun ProgressScreenContent(
    uiState: ProgressUiState,
    onSelectSkill: (SkillProficiency?) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.isLoading) {
        Box(modifier.fillMaxSize().background(DashboardColors.BackgroundPrimary), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = DashboardColors.PrimaryPink)
        }
        return
    }
    BoxWithConstraints(modifier.fillMaxSize().background(DashboardColors.BackgroundPrimary)) {
        val columns = if (maxWidth >= 700.dp) 2 else 1
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(DashboardSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(DashboardSpacing.xl),
        ) {
            DashboardPageHeader("Your Progress", "See how your English skills are developing over time.")
            uiState.dashboardData?.let { SummaryGrid(it, columns) }

            DashboardHeader(Icons.Rounded.ShowChart, "Skill Progress", "Your level is tracked separately for each skill")
            val skills = uiState.dashboardData?.skills.orEmpty().filter {
                it.domain.lowercase() in setOf("vocabulary", "grammar", "reading", "listening", "speaking")
            }
            if (skills.isEmpty()) EmptyProgressCard("Complete learning activities to begin tracking your skills.")
            else skills.chunked(columns).forEach { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.md), verticalAlignment = Alignment.Top) {
                    row.forEach { skill ->
                        SkillCard(
                            skill,
                            uiState.selectedSkill?.domain == skill.domain,
                            { onSelectSkill(if (uiState.selectedSkill?.domain == skill.domain) null else skill) },
                            Modifier.weight(1f),
                        )
                    }
                    repeat(columns - row.size) { Spacer(Modifier.weight(1f)) }
                }
            }

            DashboardHeader(Icons.Rounded.History, "Recent Learning", "Your latest practice activity")
            if (uiState.recentEvidence.isEmpty()) EmptyProgressCard("Complete an activity to start your learning history.")
            else DashboardCard(Modifier.fillMaxWidth()) {
                uiState.recentEvidence.take(10).forEach { EvidenceRow(it) }
            }
        }
    }
}

@Composable
private fun SummaryGrid(dashboard: ProgressDashboardData, columns: Int) {
    val metrics = listOf(
        SummaryMetric("Current Level", dashboard.overallEstimatedCefr, "Your overall estimate", Icons.Rounded.EmojiEvents),
        SummaryMetric("Learning Streak", "${dashboard.streak.currentStreakDays} days", "Keep your routine going", Icons.Rounded.LocalFireDepartment),
        SummaryMetric("Skills Practiced", "${dashboard.skills.count { it.totalAttempts > 0 }}", "Across your learning areas", Icons.Rounded.AutoStories),
        SummaryMetric("Reviews Due", "${dashboard.reviewsDueCount}", "Ready when you are", Icons.Rounded.EventRepeat),
    )
    metrics.chunked(columns.coerceAtLeast(1)).forEach { row ->
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.md)) {
            row.forEach { SummaryCard(it, Modifier.weight(1f)) }
            repeat(columns - row.size) { Spacer(Modifier.weight(1f)) }
        }
    }
}

@Composable
private fun SummaryCard(metric: SummaryMetric, modifier: Modifier = Modifier) {
    DashboardCard(modifier) {
        Icon(metric.icon, null, tint = DashboardColors.PrimaryPink)
        Text(metric.label, color = DashboardColors.TextSecondary, style = DashboardTypography.Caption)
        Text(metric.value, color = DashboardColors.TextPrimary, style = DashboardTypography.PageTitle)
        Text(metric.description, color = DashboardColors.TextSecondary, style = DashboardTypography.Caption)
    }
}

@Composable
private fun SkillCard(skill: SkillProficiency, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    DashboardCard(modifier.clickable(onClick = onClick), if (selected) DashboardColors.SurfacePink else DashboardColors.Surface) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(skill.displayName, color = DashboardColors.TextPrimary, style = DashboardTypography.CardTitle)
                Text("${skill.cefrLevel} level", color = DashboardColors.PrimaryPinkDark, style = DashboardTypography.Caption.copy(fontWeight = FontWeight.SemiBold))
            }
            Text("${skill.masteryPercentage}%", color = DashboardColors.PrimaryPinkDark, style = DashboardTypography.PageTitle)
        }
        LinearProgressIndicator(
            progress = { (skill.masteryPercentage / 100f).coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
            color = DashboardColors.PrimaryPink,
            trackColor = DashboardColors.ProgressTrack,
        )
        Text(
            "${skill.itemsMastered} mastered · ${skill.itemsLearning} practicing · ${(skill.accuracyRate * 100).roundToInt()}% accuracy",
            color = DashboardColors.TextSecondary,
            style = DashboardTypography.Caption,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun EvidenceRow(evidence: LearningEvidence) {
    val formattedTime = SimpleDateFormat("MMM d, HH:mm", Locale.US).format(Date(evidence.timestamp))
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(DashboardColors.BackgroundSecondary).padding(DashboardSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(evidenceIcon(evidence.domain), null, tint = DashboardColors.PrimaryPink, modifier = Modifier.size(24.dp))
        Column(Modifier.weight(1f).padding(horizontal = DashboardSpacing.sm)) {
            Text(activityLabel(evidence), color = DashboardColors.TextPrimary, style = DashboardTypography.Body.copy(fontWeight = FontWeight.SemiBold))
            Text(if (evidence.isCorrect) "Practice completed" else "Added to your review", color = DashboardColors.TextSecondary, style = DashboardTypography.Caption)
        }
        Text(formattedTime, color = DashboardColors.TextSecondary, style = DashboardTypography.Caption)
    }
}

@Composable
private fun EmptyProgressCard(message: String) = DashboardCard(Modifier.fillMaxWidth()) {
    Text(message, color = DashboardColors.TextSecondary, style = DashboardTypography.Body)
}

private fun activityLabel(evidence: LearningEvidence): String = when {
    evidence.activityType.contains("placement", true) -> "Placement Assessment"
    evidence.activityType.contains("review", true) -> "Review Completed"
    evidence.domain.equals("vocabulary", true) -> "Vocabulary Practice"
    evidence.domain.equals("grammar", true) -> "Grammar Practice"
    evidence.domain.equals("reading", true) -> "Reading Practice"
    evidence.domain.equals("listening", true) -> "Listening Practice"
    evidence.domain.equals("speaking", true) -> "Speaking Session"
    else -> "English Practice"
}

private fun evidenceIcon(domain: String): ImageVector = when (domain.lowercase()) {
    "vocabulary" -> Icons.Rounded.MenuBook
    "grammar" -> Icons.Rounded.Description
    "reading" -> Icons.Rounded.AutoStories
    "listening" -> Icons.Rounded.Headphones
    "speaking" -> Icons.Rounded.Mic
    else -> Icons.Rounded.CheckCircle
}

private data class SummaryMetric(val label: String, val value: String, val description: String, val icon: ImageVector)
