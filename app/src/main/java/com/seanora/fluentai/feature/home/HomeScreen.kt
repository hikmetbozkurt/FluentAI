package com.seanora.fluentai.feature.home

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
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.automirrored.rounded.ShowChart
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.seanora.fluentai.R
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seanora.fluentai.core.designsystem.dashboard.*
import com.seanora.fluentai.core.model.TodayPlanItem
import com.seanora.fluentai.feature.home.model.TodayPlan
import com.seanora.fluentai.feature.home.model.HomeResumeItem
import java.text.DateFormat
import java.util.Date

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToActivity: (TodayPlanItem) -> Unit = {},
    onNavigateToResume: (HomeResumeItem) -> Unit = {},
    onNavigateToSpeak: () -> Unit = {},
    onNavigateToLearn: () -> Unit = {},
    onNavigateToModule: (String) -> Unit = { onNavigateToLearn() },
    onNavigateToProgress: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreenContent(
        uiState = uiState,
        modifier = modifier,
        onNavigateToActivity = onNavigateToActivity,
        onNavigateToResume = onNavigateToResume,
        onNavigateToSpeak = onNavigateToSpeak,
        onNavigateToLearn = onNavigateToLearn,
        onNavigateToModule = onNavigateToModule,
        onNavigateToProgress = onNavigateToProgress,
        onRefreshPlan = viewModel::refreshPlan,
    )
}

@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    modifier: Modifier = Modifier,
    onNavigateToActivity: (TodayPlanItem) -> Unit = {},
    onNavigateToResume: (HomeResumeItem) -> Unit = {},
    onNavigateToSpeak: () -> Unit = {},
    onNavigateToLearn: () -> Unit = {},
    onNavigateToModule: (String) -> Unit = { onNavigateToLearn() },
    onNavigateToProgress: () -> Unit = {},
    onRefreshPlan: () -> Unit = {},
) {
    Box(modifier.fillMaxSize().background(DashboardColors.BackgroundPrimary)) {
        when (uiState) {
            HomeUiState.Loading -> CircularProgressIndicator(
                Modifier.align(Alignment.Center),
                color = DashboardColors.PrimaryPink
            )
            is HomeUiState.Error -> Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(DashboardSpacing.xl),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    uiState.message,
                    color = DashboardColors.TextSecondary,
                )
                DashboardPrimaryButton(
                    text = "Try again",
                    icon = Icons.Rounded.Refresh,
                    onClick = onRefreshPlan,
                    minHeight = 40.dp,
                )
            }
            is HomeUiState.Success -> Dashboard(
                plan = uiState.plan,
                onActivity = onNavigateToActivity,
                onResume = onNavigateToResume,
                onSpeak = onNavigateToSpeak,
                onLearn = onNavigateToLearn,
                onModule = onNavigateToModule,
                onProgress = onNavigateToProgress,
                onRefreshPlan = onRefreshPlan,
            )
        }
    }
}

@Composable
private fun Dashboard(
    plan: TodayPlan,
    onActivity: (TodayPlanItem) -> Unit,
    onResume: (HomeResumeItem) -> Unit,
    onSpeak: () -> Unit,
    onLearn: () -> Unit,
    onModule: (String) -> Unit,
    onProgress: () -> Unit,
    onRefreshPlan: () -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = DashboardSpacing.md, vertical = DashboardSpacing.sm),
            verticalArrangement = Arrangement.spacedBy(DashboardSpacing.sm),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.sm),
                verticalAlignment = Alignment.Top,
            ) {
                WelcomeCard(plan, Modifier.weight(1.65f).fillMaxHeight())
                CurrentLevelCard(plan, onProgress, Modifier.weight(1f).fillMaxHeight())
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.sm),
                verticalAlignment = Alignment.Top,
            ) {
                ContinueLearningCard(plan, onResume, onLearn, Modifier.weight(1f).fillMaxHeight())
                DailyPracticeCard(plan, Modifier.weight(1f).fillMaxHeight())
                SpeakingPracticeCard(onSpeak, Modifier.weight(1f).fillMaxHeight())
            }

            FocusCard(
                plan = plan,
                onActivity = onActivity,
                onModule = onModule,
                onLearn = onLearn,
                onRefreshPlan = onRefreshPlan,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.sm),
                verticalAlignment = Alignment.Top,
            ) {
                ProgressCard(plan, onProgress, Modifier.weight(1.22f).fillMaxHeight())
                RecentActivityCard(plan, onProgress, Modifier.weight(1f).fillMaxHeight())
            }
        }
    }
}


@Composable
private fun WelcomeCard(plan: TodayPlan, modifier: Modifier = Modifier) = Card(
    modifier = modifier.border(
        1.dp,
        DashboardColors.BorderSoft,
        RoundedCornerShape(DashboardDimensions.CardRadius)
    ),
    shape = RoundedCornerShape(DashboardDimensions.CardRadius),
    colors = CardDefaults.cardColors(containerColor = DashboardColors.SurfacePink),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.welcome_back),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.CenterEnd,
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(DashboardDimensions.CardRadius)),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(0.64f)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            val name = plan.displayName?.trim()?.ifBlank { null }
            val headline = if (name != null) "Today's practice, $name" else "Today's practice"
            Text(
                text = headline,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = DashboardColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "${plan.currentLevel.label} · Target ${plan.targetLevel.label}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = DashboardColors.PrimaryPinkDark,
            )
            Text(
                text = "${plan.dailyMetrics.practiceMinutesToday} of ${plan.dailyGoalMinutes} min today",
                style = DashboardTypography.Caption.copy(fontFeatureSettings = "tnum"),
                color = DashboardColors.TextSecondary,
            )
        }
    }
}

@Composable
private fun CurrentLevelCard(plan: TodayPlan, onDetails: () -> Unit, modifier: Modifier = Modifier) = DashboardCard(
    modifier = modifier,
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    contentSpacing = 8.dp,
) {
    DashboardHeader(
        icon = Icons.Rounded.EmojiEvents,
        title = "Current level",
        action = "View details →",
        onActionClick = onDetails,
    )

    val measured = plan.skillProfiles.filter { it.confidence != "Calibrating" }
    val progress = if (measured.isEmpty()) 0 else measured.map { it.masteryPercent }.average().toInt()
    val percentText = if (measured.isEmpty()) "—" else "$progress%"

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Amber trophy badge + Level
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFF0D4)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Rounded.EmojiEvents,
                    contentDescription = null,
                    tint = Color(0xFFE5A100),
                    modifier = Modifier.size(18.dp),
                )
            }
            Column(Modifier.padding(start = 8.dp)) {
                Text(
                    plan.currentLevel.label,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DashboardColors.TextPrimary,
                    lineHeight = 24.sp,
                )
                Text(
                    levelName(plan.currentLevel.label),
                    color = DashboardColors.TextSecondary,
                    fontSize = 11.sp,
                )
            }
        }

        Spacer(Modifier.weight(1f))

        // Progress bar and percentage
        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Overall progress ",
                    fontSize = 11.sp,
                    color = DashboardColors.TextSecondary,
                )
                Text(
                    percentText,
                    style = DashboardTypography.CardTitle.copy(fontFeatureSettings = "tnum"),
                    color = DashboardColors.TextPrimary,
                )
            }
            LinearProgressIndicator(
                progress = { progress / 100f },
                modifier = Modifier
                    .width(96.dp)
                    .height(6.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onDetails),
                color = DashboardColors.PrimaryPink,
                trackColor = DashboardColors.ProgressTrack,
            )
            Text(
                "${100 - progress}% to ${plan.targetLevel.label}",
                style = DashboardTypography.Caption.copy(fontSize = 11.sp, fontFeatureSettings = "tnum"),
                color = DashboardColors.TextSecondary,
            )
        }
    }
}


@Composable
private fun ContinueLearningCard(
    plan: TodayPlan,
    onResume: (HomeResumeItem) -> Unit,
    onLearn: () -> Unit,
    modifier: Modifier = Modifier,
) = DashboardCard(
    modifier = modifier,
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    contentSpacing = 8.dp,
) {
    DashboardHeader(
        icon = Icons.AutoMirrored.Rounded.MenuBook,
        title = "Continue learning",
        subtitle = "Pick up where you left off",
    )

    val item = plan.continueLearning

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.continue_learning_background),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(64.dp)
                .height(60.dp),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFFFE5F0))
                    .padding(horizontal = 8.dp, vertical = 2.dp),
            ) {
                Text(
                    "Last lesson",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = DashboardColors.PrimaryPinkDark,
                )
            }
            Text(
                text = item?.title ?: "No resumable activity yet",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = DashboardColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = item?.domain ?: "Choose a lesson from Learn to begin",
                fontSize = 11.sp,
                color = DashboardColors.TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }

    DashboardPrimaryButton(
        text = "Continue learning →",
        icon = Icons.Rounded.PlayArrow,
        onClick = { if (item != null) onResume(item) else onLearn() },
        modifier = Modifier.fillMaxWidth(),
        minHeight = 36.dp,
    )
}

@Composable
private fun DailyPracticeCard(plan: TodayPlan, modifier: Modifier = Modifier) = DashboardCard(
    modifier = modifier,
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    contentSpacing = 8.dp,
) {
    val minutes = plan.dailyMetrics.practiceMinutesToday
    val goal = plan.dailyGoalMinutes.coerceAtLeast(1)

    DashboardHeader(
        icon = Icons.Rounded.CalendarToday,
        title = "Daily practice",
        subtitle = "Goal: $goal minutes",
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        // Progress ring with minutes in center
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = { (minutes.toFloat() / goal).coerceIn(0f, 1f) },
                modifier = Modifier.size(52.dp),
                color = DashboardColors.PrimaryPink,
                trackColor = DashboardColors.ProgressTrack,
                strokeWidth = 4.dp,
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "$minutes",
                    style = DashboardTypography.CardTitle.copy(fontSize = 14.sp, lineHeight = 16.sp, fontFeatureSettings = "tnum"),
                    color = DashboardColors.TextPrimary,
                )
                Text(
                    "min",
                    fontSize = 11.sp,
                    color = DashboardColors.TextSecondary,
                    lineHeight = 12.sp,
                )
            }
        }

        // Streak indicator
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Rounded.LocalFireDepartment,
                contentDescription = null,
                tint = Color(0xFFFF5722),
                modifier = Modifier.size(22.dp),
            )
            Column(Modifier.padding(start = 4.dp)) {
                Text(
                    "${plan.dailyMetrics.streakDays}",
                    style = DashboardTypography.CardTitle.copy(fontFeatureSettings = "tnum"),
                    color = DashboardColors.TextPrimary,
                )
                Text(
                    "day streak",
                    fontSize = 11.sp,
                    color = DashboardColors.TextSecondary,
                )
            }
        }
    }

    // Goal status bar
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFFFF0F5))
            .padding(horizontal = 8.dp, vertical = 6.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Rounded.TrackChanges,
                contentDescription = null,
                tint = DashboardColors.PrimaryPink,
                modifier = Modifier.size(16.dp),
            )
            Column(Modifier.padding(start = 8.dp)) {
                Text(
                    when {
                        minutes >= goal -> "Goal reached"
                        minutes == 0 -> "Not started"
                        else -> "In progress"
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DashboardColors.PrimaryPinkDark,
                )
                Text(
                    when {
                        minutes >= goal -> "Today's goal is complete"
                        minutes == 0 -> "$goal min remaining today"
                        else -> "${goal - minutes} min remaining today"
                    },
                    style = DashboardTypography.Caption.copy(fontSize = 11.sp, fontFeatureSettings = "tnum"),
                    color = DashboardColors.TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun SpeakingPracticeCard(onSpeak: () -> Unit, modifier: Modifier = Modifier) = DashboardCard(
    modifier = modifier,
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    contentSpacing = 8.dp,
) {
    DashboardHeader(
        icon = Icons.Rounded.Mic,
        title = "Speaking practice",
        subtitle = "Natural conversation with AI",
    )

    Image(
        painter = painterResource(R.drawable.speaking_practice),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .padding(horizontal = 4.dp),
    )

    DashboardPrimaryButton(
        text = "Start conversation →",
        icon = Icons.Rounded.Mic,
        onClick = onSpeak,
        modifier = Modifier.fillMaxWidth(),
        minHeight = 36.dp,
    )
}


@Composable
private fun FocusCard(
    plan: TodayPlan,
    onActivity: (TodayPlanItem) -> Unit,
    onModule: (String) -> Unit,
    onLearn: () -> Unit,
    onRefreshPlan: () -> Unit,
) = DashboardCard(
    modifier = Modifier.fillMaxWidth(),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    contentSpacing = 8.dp,
) {
    val practicePlan = plan.practicePlan
    val rationale = practicePlan?.rationale?.ifBlank { null } ?: "Practice key skills for steady progress"
    DashboardHeader(
        icon = Icons.Rounded.TrackChanges,
        title = "Today's focus",
        subtitle = rationale,
        action = "Refresh",
        onActionClick = onRefreshPlan,
    )

    val nextPlanItem = plan.focusItems.firstOrNull()
    if (nextPlanItem != null) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(DashboardColors.BackgroundSecondary)
                .border(1.dp, DashboardColors.BorderSoft, RoundedCornerShape(12.dp))
                .clickable { onActivity(nextPlanItem) }
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(DashboardColors.PrimaryPink)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    "Up next",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                )
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = nextPlanItem.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DashboardColors.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "${nextPlanItem.domain.replaceFirstChar { it.uppercase() }} · ${nextPlanItem.estimatedMinutes} min · ${nextPlanItem.explanation}",
                    fontSize = 11.sp,
                    color = DashboardColors.TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                text = "Start →",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = DashboardColors.PrimaryPinkDark,
            )
        }
    } else if (practicePlan != null && practicePlan.isCompleted) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFE3F7EC))
                .border(1.dp, Color(0xFFB7E8CE), RoundedCornerShape(12.dp))
                .clickable(onClick = onLearn)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                Icons.Rounded.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF1EA86A),
                modifier = Modifier.size(20.dp),
            )
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = plan.heroPractice.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DashboardColors.TextPrimary,
                )
                Text(
                    text = "All ${practicePlan.totalItemsCount} planned activities finished today.",
                    fontSize = 11.sp,
                    color = DashboardColors.TextSecondary,
                )
            }
            Text(
                text = "Explore →",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF157A4D),
            )
        }
    }

    if (practicePlan != null && practicePlan.items.size > 1) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Today's plan · ${practicePlan.completedItemsCount}/${practicePlan.totalItemsCount} completed",
                style = DashboardTypography.Caption.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    fontFeatureSettings = "tnum",
                ),
                color = DashboardColors.TextSecondary,
            )
            practicePlan.items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onActivity(item) }
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        imageVector = if (item.isCompleted) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                        contentDescription = if (item.isCompleted) "Completed" else "Pending",
                        tint = if (item.isCompleted) Color(0xFF1EA86A) else DashboardColors.PrimaryPink,
                        modifier = Modifier.size(16.dp),
                    )
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = item.title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (item.isCompleted) DashboardColors.TextSecondary else DashboardColors.TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = "${item.itemType.displayName} · ${item.estimatedMinutes} min",
                            fontSize = 11.sp,
                            color = DashboardColors.TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Text(
                        text = if (item.isCompleted) "Done" else "Start",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (item.isCompleted) Color(0xFF157A4D) else DashboardColors.PrimaryPinkDark,
                    )
                }
            }
        }
    }

    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val columns = focusColumnCountForWidth(maxWidth.value)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            homeFocusCardSpecs().chunked(columns).forEach { rowCards ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    rowCards.forEach { card ->
                        val item = plan.focusItems.firstOrNull {
                            it.domain.equals(card.domain, ignoreCase = true)
                        }
                        SkillFocusTile(
                            title = card.title,
                            subtitle = if (item != null) "${item.estimatedMinutes} min" else "Explore",
                            icon = card.icon,
                            background = card.background,
                            iconTint = card.iconTint,
                            modifier = Modifier.weight(1f),
                            onClick = { if (item != null) onActivity(item) else onModule(card.domain) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SkillFocusTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    background: Color,
    iconTint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .height(82.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(30.dp),
            )
            Spacer(Modifier.height(4.dp))
            Text(
                title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = DashboardColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                subtitle,
                fontSize = 10.sp,
                color = DashboardColors.TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}


@Composable
private fun ProgressCard(plan: TodayPlan, onDetails: () -> Unit, modifier: Modifier = Modifier) = DashboardCard(
    modifier = modifier,
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    contentSpacing = 8.dp,
) {
    DashboardHeader(
        icon = Icons.AutoMirrored.Rounded.ShowChart,
        title = "Progress overview",
        subtitle = "Skill development at a glance",
        action = "See details →",
        onActionClick = onDetails,
    )

    val skills = listOf(
        Triple("Speaking", Color(0xFFEC4C92), plan.skillProfiles.find { it.skill.equals("Speaking", true) }),
        Triple("Vocabulary", Color(0xFF8B5CF6), plan.skillProfiles.find { it.skill.equals("Vocabulary", true) }),
        Triple("Grammar", Color(0xFFF97316), plan.skillProfiles.find { it.skill.equals("Grammar", true) }),
        Triple("Listening", Color(0xFF10B981), plan.skillProfiles.find { it.skill.equals("Listening", true) }),
        Triple("Reading", Color(0xFF3B82F6), plan.skillProfiles.find { it.skill.equals("Reading", true) }),
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        skills.forEach { (name, color, profile) ->
            val pct = profile?.masteryPercent ?: 0
            val txt = if (profile == null || profile.confidence == "Calibrating") "—" else "$pct%"
            val progressFloat = (pct.toFloat() / 100f).coerceIn(0f, 1f)

            Column(
                modifier = Modifier.clickable(onClick = onDetails),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { progressFloat },
                        modifier = Modifier.size(44.dp),
                        color = color,
                        trackColor = DashboardColors.ProgressTrack,
                        strokeWidth = 4.dp,
                    )
                    Text(
                        txt,
                        style = DashboardTypography.Caption.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFeatureSettings = "tnum",
                        ),
                        color = DashboardColors.TextPrimary,
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    name,
                    fontSize = 11.sp,
                    color = DashboardColors.TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun RecentActivityCard(plan: TodayPlan, onDetails: () -> Unit, modifier: Modifier = Modifier) = DashboardCard(
    modifier = modifier,
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    contentSpacing = 8.dp,
) {
    DashboardHeader(
        icon = Icons.Rounded.Schedule,
        title = "Recent activity",
        subtitle = "Latest learning activity",
        action = "See all →",
        onActionClick = onDetails,
    )

    if (plan.recentActivities.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                "Complete an activity to start your history",
                fontSize = 12.sp,
                color = DashboardColors.TextSecondary,
            )
        }
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            plan.recentActivities.take(3).forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onDetails),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val icon = when {
                        item.title.contains("conversation", true) || item.title.contains("speak", true) -> Icons.Rounded.ChatBubbleOutline
                        item.title.contains("word", true) || item.title.contains("vocab", true) || item.title.contains("read", true) -> Icons.AutoMirrored.Rounded.MenuBook
                        else -> Icons.Rounded.CheckCircleOutline
                    }
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = DashboardColors.PrimaryPink,
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        text = item.title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = DashboardColors.TextPrimary,
                        modifier = Modifier.padding(start = 8.dp),
                        maxLines = 1,
                    )
                    Text(
                        text = item.description,
                        fontSize = 11.sp,
                        color = DashboardColors.TextSecondary,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = formatRelativeTime(item.timestamp),
                        fontSize = 11.sp,
                        color = DashboardColors.TextSecondary,
                    )
                }
            }
        }
    }
}


private fun levelName(level: String): String = when (level.uppercase()) {
    "A2" -> "Elementary"
    "B1" -> "Intermediate"
    "B2" -> "Upper intermediate"
    "C1" -> "Advanced"
    "C2" -> "Proficient"
    else -> "Intermediate"
}

internal data class HomeFocusCardSpec(
    val title: String,
    val domain: String,
    val icon: ImageVector,
    val background: Color,
    val iconTint: Color,
)

internal fun homeFocusCardSpecs(): List<HomeFocusCardSpec> = listOf(
    HomeFocusCardSpec(
        title = "Vocabulary",
        domain = "vocabulary",
        icon = Icons.AutoMirrored.Rounded.MenuBook,
        background = Color(0xFFFFE8F0),
        iconTint = Color(0xFFE83A82),
    ),
    HomeFocusCardSpec(
        title = "Grammar",
        domain = "grammar",
        icon = Icons.Rounded.Description,
        background = Color(0xFFF1ECFF),
        iconTint = Color(0xFF7551E9),
    ),
    HomeFocusCardSpec(
        title = "Reading",
        domain = "reading",
        icon = Icons.Rounded.AutoStories,
        background = Color(0xFFFFF0E4),
        iconTint = Color(0xFFF07024),
    ),
    HomeFocusCardSpec(
        title = "Listening",
        domain = "listening",
        icon = Icons.Rounded.Headphones,
        background = Color(0xFFE3F7EC),
        iconTint = Color(0xFF1EA86A),
    ),
)

internal fun focusColumnCountForWidth(widthDp: Float): Int = if (widthDp >= 520f) 4 else 2

private fun formatRelativeTime(timestamp: Long): String {
    if (timestamp <= 0L) return "recently"
    val now = System.currentTimeMillis()
    val diff = (now - timestamp).coerceAtLeast(0)
    val minutes = diff / 60_000
    val hours = diff / 3_600_000
    val days = diff / 86_400_000
    return when {
        minutes < 1 -> "just now"
        minutes < 60 -> "$minutes min ago"
        hours < 24 -> "$hours hours ago"
        days == 1L -> "1 day ago"
        days < 7 -> "$days days ago"
        else -> DateFormat.getDateInstance(DateFormat.SHORT).format(Date(timestamp))
    }
}
