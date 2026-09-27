package com.seanora.fluentai.feature.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seanora.fluentai.core.designsystem.components.ButtonVariant
import com.seanora.fluentai.core.designsystem.components.FluentButton
import com.seanora.fluentai.core.designsystem.dashboard.*
import com.seanora.fluentai.core.model.UserAvatar

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    LingoPinkTheme {
        ProfileContent(viewModel)
    }
}

@Composable
private fun ProfileContent(viewModel: ProfileViewModel) {
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val selectedAvatarKey by viewModel.selectedAvatarKey.collectAsStateWithLifecycle()
    var showEditNameDialog by remember { mutableStateOf(false) }
    var showAvatarDialog by remember { mutableStateOf(false) }
    var nameInput by remember { mutableStateOf("") }

    val currentName = profile?.displayName?.trim()?.ifBlank { null } ?: "Learner"
    val avatarRes = UserAvatar.getDrawableRes(selectedAvatarKey)

    if (showEditNameDialog) {
        AlertDialog(
            onDismissRequest = { showEditNameDialog = false },
            title = {
                Text(
                    text = "Edit Display Name",
                    color = DashboardColors.TextPrimary,
                    style = DashboardTypography.CardTitle
                )
            },
            text = {
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { if (it.length <= 50) nameInput = it },
                    label = { Text("Your name", color = DashboardColors.TextSecondary) },
                    placeholder = { Text("e.g. Alex", color = DashboardColors.TextSecondary) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DashboardColors.PrimaryPink,
                        cursorColor = DashboardColors.PrimaryPink,
                        focusedTextColor = DashboardColors.TextPrimary,
                        unfocusedTextColor = DashboardColors.TextPrimary
                    )
                )
            },
            confirmButton = {
                FluentButton(
                    text = "Save",
                    onClick = {
                        viewModel.updateDisplayName(nameInput)
                        showEditNameDialog = false
                    }
                )
            },
            dismissButton = {
                FluentButton(
                    text = "Cancel",
                    variant = ButtonVariant.Outlined,
                    onClick = { showEditNameDialog = false }
                )
            },
            containerColor = DashboardColors.Surface
        )
    }

    if (showAvatarDialog) {
        AlertDialog(
            onDismissRequest = { showAvatarDialog = false },
            title = {
                Text(
                    text = "Choose your avatar",
                    color = DashboardColors.TextPrimary,
                    style = DashboardTypography.CardTitle
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "Pick the one that feels most like you.",
                        color = DashboardColors.TextSecondary,
                        style = DashboardTypography.Body
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        UserAvatar.ALL.forEach { avatar ->
                            val isSelected = selectedAvatarKey == avatar.key
                            val borderColor = if (isSelected) DashboardColors.PrimaryPink else DashboardColors.BorderSoft
                            val borderWidth = if (isSelected) 3.dp else 1.dp
                            val bg = if (isSelected) DashboardColors.SurfacePink.copy(alpha = 0.5f) else DashboardColors.Surface

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(bg)
                                    .border(borderWidth, borderColor, RoundedCornerShape(16.dp))
                                    .clickable {
                                        viewModel.selectAvatar(avatar.key)
                                        showAvatarDialog = false
                                    }
                                    .semantics {
                                        this.selected = isSelected
                                        this.contentDescription = avatar.displayName
                                    }
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Image(
                                        painter = painterResource(avatar.drawableRes),
                                        contentDescription = avatar.displayName,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(CircleShape)
                                            .border(
                                                if (isSelected) 2.dp else 0.dp,
                                                if (isSelected) DashboardColors.PrimaryPink else Color.Transparent,
                                                CircleShape
                                            )
                                    )
                                    Text(
                                        text = avatar.displayName,
                                        style = DashboardTypography.Caption,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) DashboardColors.PrimaryPinkDark else DashboardColors.TextPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                FluentButton(
                    text = "Cancel",
                    variant = ButtonVariant.Outlined,
                    onClick = { showAvatarDialog = false }
                )
            },
            containerColor = DashboardColors.Surface
        )
    }

    BoxWithConstraints(Modifier.fillMaxSize().background(DashboardColors.BackgroundPrimary)) {
        val columns = when { maxWidth >= 1200.dp -> 3; maxWidth >= 700.dp -> 2; else -> 1 }
        val summary = listOf(
            ProfileMetric("Current level", profile?.estimatedOverallLevel ?: "Calibrating", Icons.Rounded.EmojiEvents),
            ProfileMetric("Target level", profile?.targetCefrLevel ?: "Not set", Icons.Rounded.TrackChanges),
            ProfileMetric("Daily goal", "${profile?.dailyGoalMinutes ?: 0} minutes", Icons.Rounded.Schedule),
        )
        val skills = listOf(
            ProfileMetric("Vocabulary", profile?.estimatedVocabLevel ?: "Calibrating", Icons.Rounded.MenuBook),
            ProfileMetric("Grammar", profile?.estimatedGrammarLevel ?: "Calibrating", Icons.Rounded.Description),
            ProfileMetric("Reading", profile?.estimatedReadingLevel ?: "Calibrating", Icons.Rounded.AutoStories),
            ProfileMetric("Listening", profile?.estimatedListeningLevel ?: "Calibrating", Icons.Rounded.Headphones),
            ProfileMetric("Speaking", profile?.estimatedSpeakingLevel ?: "Calibrating", Icons.Rounded.Mic),
        )
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(DashboardSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(DashboardSpacing.xl),
        ) {
            DashboardPageHeader("Learning Profile", "Your goals and skill-level estimates in one place.")

            DashboardCard(Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.md)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .clickable { showAvatarDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            if (avatarRes != null) {
                                Image(
                                    painter = painterResource(avatarRes),
                                    contentDescription = "Profile avatar",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Box(
                                    modifier = Modifier.fillMaxSize().background(DashboardColors.SurfacePink),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Rounded.Person, contentDescription = "Default avatar", tint = DashboardColors.PrimaryPinkDark, modifier = Modifier.size(32.dp))
                                }
                            }
                        }
                        Column {
                            Text("Display Name", color = DashboardColors.TextSecondary, style = DashboardTypography.Caption)
                            Text(currentName, color = DashboardColors.TextPrimary, style = DashboardTypography.CardTitle.copy(fontWeight = FontWeight.Bold))
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.sm)) {
                        FluentButton(
                            text = "Change Avatar",
                            variant = ButtonVariant.Outlined,
                            onClick = { showAvatarDialog = true }
                        )
                        FluentButton(
                            text = "Edit Name",
                            variant = ButtonVariant.Outlined,
                            onClick = {
                                nameInput = profile?.displayName ?: ""
                                showEditNameDialog = true
                            }
                        )
                    }
                }
            }

            ResponsiveMetrics(summary, columns)
            DashboardHeader(Icons.Rounded.ShowChart, "Skill Levels", "Each skill is estimated independently")
            ResponsiveMetrics(skills, columns)
            DashboardCard(Modifier.fillMaxWidth()) {
                DashboardHeader(Icons.Rounded.FavoriteBorder, "Learning Interests", "Topics used to personalize practice")
                Text(
                    profile?.learningInterests?.joinToString()?.ifBlank { "No interests selected" } ?: "No interests selected",
                    color = DashboardColors.TextPrimary,
                    style = DashboardTypography.Body,
                )
            }
        }
    }
}

@Composable
private fun ResponsiveMetrics(items: List<ProfileMetric>, columns: Int) {
    items.chunked(columns).forEach { rowItems ->
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.md), verticalAlignment = Alignment.Top) {
            rowItems.forEach { ProfileMetricCard(it, Modifier.weight(1f)) }
            repeat(columns - rowItems.size) { Spacer(Modifier.weight(1f)) }
        }
    }
}

@Composable
private fun ProfileMetricCard(metric: ProfileMetric, modifier: Modifier = Modifier) {
    DashboardCard(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(48.dp).background(DashboardColors.SurfacePink, CircleShape), contentAlignment = Alignment.Center) {
                Icon(metric.icon, null, tint = DashboardColors.PrimaryPinkDark)
            }
            Column(Modifier.padding(start = DashboardSpacing.sm)) {
                Text(metric.label, color = DashboardColors.TextSecondary, style = DashboardTypography.Caption)
                Text(metric.value, color = DashboardColors.TextPrimary, style = DashboardTypography.CardTitle.copy(fontWeight = FontWeight.Bold))
            }
        }
    }
}

private data class ProfileMetric(val label: String, val value: String, val icon: ImageVector)
