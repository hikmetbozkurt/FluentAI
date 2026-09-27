package com.seanora.fluentai.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seanora.fluentai.core.designsystem.dashboard.*

@Composable
fun SettingsScreen(modifier: Modifier = Modifier, viewModel: SettingsViewModel = hiltViewModel()) {
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    BoxWithConstraints(modifier.fillMaxSize().background(DashboardColors.BackgroundPrimary)) {
        val twoColumns = maxWidth >= 700.dp
        val interests = profile?.learningInterests?.joinToString()?.ifBlank { "Not set" } ?: "Not set"
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(DashboardSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(DashboardSpacing.xl),
        ) {
            DashboardPageHeader("Settings", "Review the learning preferences currently saved for your profile.")
            if (twoColumns) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.md), verticalAlignment = Alignment.Top) {
                    PreferenceCard(
                        "Learning Preferences",
                        Icons.Rounded.Tune,
                        listOf(
                            PreferenceItem("Target level", profile?.targetCefrLevel ?: "Not set", Icons.Rounded.EmojiEvents),
                            PreferenceItem("Daily practice goal", "${profile?.dailyGoalMinutes ?: 0} minutes", Icons.Rounded.Schedule),
                            PreferenceItem("Interests", interests, Icons.Rounded.FavoriteBorder),
                        ),
                        Modifier.weight(2f),
                    )
                    PreferenceCard(
                        "About",
                        Icons.Rounded.Info,
                        listOf(
                            PreferenceItem("App version", "0.1.0", Icons.Rounded.Apps),
                            PreferenceItem("Curriculum range", "CEFR A2–C2", Icons.Rounded.AutoStories),
                        ),
                        Modifier.weight(1f),
                    )
                }
            } else {
                PreferenceCard(
                    "Learning Preferences",
                    Icons.Rounded.Tune,
                    listOf(
                        PreferenceItem("Target level", profile?.targetCefrLevel ?: "Not set", Icons.Rounded.EmojiEvents),
                        PreferenceItem("Daily practice goal", "${profile?.dailyGoalMinutes ?: 0} minutes", Icons.Rounded.Schedule),
                        PreferenceItem("Interests", interests, Icons.Rounded.FavoriteBorder),
                    ),
                )
                PreferenceCard(
                    "About",
                    Icons.Rounded.Info,
                    listOf(
                        PreferenceItem("App version", "0.1.0", Icons.Rounded.Apps),
                        PreferenceItem("Curriculum range", "CEFR A2–C2", Icons.Rounded.AutoStories),
                    ),
                )
            }
        }
    }
}

@Composable
private fun PreferenceCard(title: String, icon: ImageVector, items: List<PreferenceItem>, modifier: Modifier = Modifier) {
    DashboardCard(modifier) {
        DashboardHeader(icon, title, "Current profile information")
        items.forEach { item ->
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(DashboardColors.BackgroundSecondary).padding(DashboardSpacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(item.icon, null, tint = DashboardColors.PrimaryPink, modifier = Modifier.size(DashboardSpacing.xl))
                Column(Modifier.weight(1f).padding(start = DashboardSpacing.sm)) {
                    Text(item.label, color = DashboardColors.TextSecondary, style = DashboardTypography.Caption)
                    Text(item.value, color = DashboardColors.TextPrimary, style = DashboardTypography.Body, maxLines = 3)
                }
            }
        }
    }
}

private data class PreferenceItem(val label: String, val value: String, val icon: ImageVector)
