package com.seanora.fluentai.feature.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.OfflineBolt
import androidx.compose.material.icons.rounded.RecordVoiceOver
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seanora.fluentai.core.designsystem.dashboard.DashboardCard
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.designsystem.dashboard.DashboardHeader
import com.seanora.fluentai.core.designsystem.dashboard.DashboardSpacing
import com.seanora.fluentai.core.designsystem.dashboard.DashboardTypography
import com.seanora.fluentai.core.designsystem.kit.FluentPageHeader
import com.seanora.fluentai.core.designsystem.kit.LearningModule
import com.seanora.fluentai.feature.reading.ReadingSurfaceStyle

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(modifier: Modifier = Modifier, viewModel: SettingsViewModel = hiltViewModel()) {
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val readingStyle by viewModel.readingSurfaceStyle.collectAsStateWithLifecycle()
    val readingFontSize by viewModel.readingFontSizeSp.collectAsStateWithLifecycle()

    val currentTarget = profile?.targetCefrLevel ?: "C1"
    val currentGoal = profile?.dailyGoalMinutes ?: 20
    val currentInterests = profile?.learningInterests.orEmpty().toSet()

    BoxWithConstraints(modifier.fillMaxSize().background(DashboardColors.BackgroundPrimary)) {
        val twoColumns = maxWidth >= 820.dp
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(DashboardSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(DashboardSpacing.xl),
        ) {
            FluentPageHeader(
                title = "Settings",
                module = LearningModule.SETTINGS,
                eyebrow = "Preferences",
                subtitle = "Customize your daily practice target, interests, and reading comfort defaults.",
            )

            val learningPrefsCard: @Composable (Modifier) -> Unit = { cardMod ->
                DashboardCard(cardMod) {
                    DashboardHeader(Icons.Rounded.Tune, "Learning Preferences", "Changes are saved automatically")

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Target CEFR Level", style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            SettingsViewModel.AVAILABLE_TARGET_LEVELS.forEach { level ->
                                SelectableSettingChip(
                                    label = level,
                                    selected = currentTarget.equals(level, ignoreCase = true),
                                    onClick = { viewModel.updateTargetCefr(level) },
                                )
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Daily Practice Goal", style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            SettingsViewModel.AVAILABLE_DAILY_GOALS.forEach { minutes ->
                                SelectableSettingChip(
                                    label = "$minutes min",
                                    selected = currentGoal == minutes,
                                    onClick = { viewModel.updateDailyGoalMinutes(minutes) },
                                )
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Learning Interests", style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            SettingsViewModel.AVAILABLE_INTERESTS.forEach { interest ->
                                SelectableSettingChip(
                                    label = interest,
                                    selected = interest in currentInterests,
                                    showCheck = true,
                                    onClick = { viewModel.toggleInterest(interest) },
                                )
                            }
                        }
                    }
                }
            }

            val readingAndAboutColumn: @Composable (Modifier) -> Unit = { colMod ->
                Column(colMod, verticalArrangement = Arrangement.spacedBy(DashboardSpacing.md)) {
                    DashboardCard(Modifier.fillMaxWidth()) {
                        DashboardHeader(Icons.Rounded.AutoStories, "Reading Comfort Defaults", "Applies to all articles & live feed")
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Reader Surface", style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                ReadingSurfaceStyle.entries.forEach { style ->
                                    SelectableSettingChip(
                                        label = style.label,
                                        selected = readingStyle == style,
                                        onClick = { viewModel.setReadingSurfaceStyle(style) },
                                    )
                                }
                            }
                        }
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Column {
                                Text("Body Font Size", style = DashboardTypography.CardTitle, color = DashboardColors.TextPrimary)
                                Text("${readingFontSize.toInt()}sp serif", style = DashboardTypography.Caption, color = DashboardColors.TextSecondary)
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    onClick = viewModel::decreaseReadingFontSize,
                                    shape = CircleShape,
                                    color = DashboardColors.BackgroundSecondary,
                                    border = BorderStroke(1.dp, DashboardColors.BorderSoft),
                                ) {
                                    Text("A−", fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary, modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp))
                                }
                                Surface(
                                    onClick = viewModel::increaseReadingFontSize,
                                    shape = CircleShape,
                                    color = DashboardColors.BackgroundSecondary,
                                    border = BorderStroke(1.dp, DashboardColors.BorderSoft),
                                ) {
                                    Text("A+", fontWeight = FontWeight.Bold, color = DashboardColors.TextPrimary, modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp))
                                }
                            }
                        }
                    }

                    DashboardCard(Modifier.fillMaxWidth()) {
                        DashboardHeader(Icons.Rounded.Info, "About & Offline Readiness", "Local-first learning stack")
                        listOf(
                            Triple("App version", "0.1.0", Icons.Rounded.Apps),
                            Triple("Curriculum range", "CEFR A2–C2", Icons.Rounded.AutoStories),
                            Triple("Offline Dictionary", "107,694 English entries (Wordset + CEFR-J + IPA)", Icons.Rounded.MenuBook),
                            Triple("On-Device Pronunciation", "Offline US / UK Text-to-Speech", Icons.Rounded.RecordVoiceOver),
                            Triple("Offline-First Mode", "Core learning & cached articles work without internet", Icons.Rounded.OfflineBolt),
                        ).forEach { (label, value, icon) ->
                            Row(
                                Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(DashboardColors.BackgroundSecondary).padding(DashboardSpacing.sm),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(icon, null, tint = DashboardColors.PrimaryPink, modifier = Modifier.size(22.dp))
                                Column(Modifier.weight(1f).padding(start = DashboardSpacing.sm)) {
                                    Text(label, color = DashboardColors.TextSecondary, style = DashboardTypography.Caption)
                                    Text(value, color = DashboardColors.TextPrimary, style = DashboardTypography.Body, maxLines = 2)
                                }
                            }
                        }
                    }
                }
            }

            if (twoColumns) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.md), verticalAlignment = Alignment.Top) {
                    learningPrefsCard(Modifier.weight(1.25f))
                    readingAndAboutColumn(Modifier.weight(1f))
                }
            } else {
                learningPrefsCard(Modifier.fillMaxWidth())
                readingAndAboutColumn(Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun SelectableSettingChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    showCheck: Boolean = false,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = if (selected) DashboardColors.PrimaryPink else Color.White,
        border = BorderStroke(1.dp, if (selected) DashboardColors.PrimaryPink else DashboardColors.BorderSoft),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (showCheck && selected) {
                Icon(Icons.Rounded.Check, null, tint = Color.White, modifier = Modifier.size(15.dp))
            }
            Text(
                label,
                fontSize = 13.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = if (selected) Color.White else DashboardColors.TextPrimary,
            )
        }
    }
}
