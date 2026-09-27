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
        onNavigateToProgress = onNavigateToProgress
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
    onNavigateToProgress: () -> Unit = {},
) {
    Box(modifier.fillMaxSize().background(DashboardColors.BackgroundPrimary)) {
        when (uiState) {
            HomeUiState.Loading -> CircularProgressIndicator(
                Modifier.align(Alignment.Center),
                color = DashboardColors.PrimaryPink
            )
            is HomeUiState.Error -> Text(
                uiState.message,
                Modifier.align(Alignment.Center).padding(DashboardSpacing.xl),
                color = DashboardColors.TextSecondary
            )
            is HomeUiState.Success -> Dashboard(
                plan = uiState.plan,
                onActivity = onNavigateToActivity,
                onResume = onNavigateToResume,
                onSpeak = onNavigateToSpeak,
                onLearn = onNavigateToLearn,
                onProgress = onNavigateToProgress
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
    onProgress: () -> Unit,
) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val isConstrainedHeight = maxHeight < 560.dp
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(if (isConstrainedHeight) Modifier.verticalScroll(scrollState) else Modifier)
                .padding(horizontal = DashboardSpacing.md, vertical = DashboardSpacing.xs),
            verticalArrangement = Arrangement.spacedBy(DashboardSpacing.xs),
        ) {
            // Row 1: Welcome back + Current Level
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.xs),
                verticalAlignment = Alignment.Top,
            ) {
                WelcomeCard(plan, Modifier.weight(1.65f).fillMaxHeight())
                CurrentLevelCard(plan, onProgress, Modifier.weight(1f).fillMaxHeight())
            }

            // Row 2: Continue Learning + Daily Practice + Speaking Practice
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.xs),
                verticalAlignment = Alignment.Top,
            ) {
                ContinueLearningCard(plan, onResume, onLearn, Modifier.weight(1f).fillMaxHeight())
                DailyPracticeCard(plan, Modifier.weight(1f).fillMaxHeight())
                SpeakingPracticeCard(onSpeak, Modifier.weight(1f).fillMaxHeight())
            }

            // Row 3: Today's Focus only (Full width)
            FocusCard(
                plan = plan,
                onActivity = onActivity,
            )

            // Row 4: Progress Overview + Recent Activity
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(DashboardSpacing.xs),
                verticalAlignment = Alignment.Top,
            ) {
                ProgressCard(plan, onProgress, Modifier.weight(1.22f).fillMaxHeight())
                RecentActivityCard(plan, onProgress, Modifier.weight(1f).fillMaxHeight())
            }
        }
    }
}

// ==========================================
// ROW 1: Welcome back & Current Level
// ==========================================

@Composable
private fun WelcomeCard(plan: TodayPlan, modifier: Modifier = Modifier) = Card(
    modifier = modifier.border(
        1.dp,
        DashboardColors.BorderSoft,
        RoundedCornerShape(DashboardDimensions.CardRadius)
    ),
    shape = RoundedCornerShape(DashboardDimensions.CardRadius),
    colors = CardDefaults.cardColors(containerColor = DashboardColors.SurfacePink),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
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
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            val name = plan.displayName?.trim()?.ifBlank { null }
            val greeting = if (name != null) "Welcome back, $name 👋" else "Welcome back 👋"
            Text(
                text = greeting,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DashboardColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "Ready to practice today?",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = DashboardColors.PrimaryPinkDark,
            )
            Text(
                text = "“Better English. A brighter you.” 💖",
                fontSize = 11.sp,
                fontStyle = FontStyle.Italic,
                color = DashboardColors.PrimaryPinkDark.copy(alpha = 0.85f),
            )
        }
    }
}

@Composable
private fun CurrentLevelCard(plan: TodayPlan, onDetails: () -> Unit, modifier: Modifier = Modifier) = DashboardCard(
    modifier = modifier,
    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
    contentSpacing = 6.dp,
) {
    DashboardHeader(
        icon = Icons.Rounded.EmojiEvents,
        title = "Current Level",
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
                    .clip(RoundedCornerShape(9.dp))
                    .background(Color(0xFFFFF0D4)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Rounded.EmojiEvents,
                    contentDescription = null,
                    tint = Color(0xFFE5A100),
                    modifier = Modifier.size(19.dp),
                )
            }
            Column(Modifier.padding(start = 8.dp)) {
                Text(
                    plan.currentLevel.label,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DashboardColors.TextPrimary,
                    lineHeight = 22.sp,
                )
                Text(
                    levelName(plan.currentLevel.label),
                    color = DashboardColors.TextSecondary,
                    fontSize = 10.sp,
                )
            }
        }

        Spacer(Modifier.weight(1f))

        // Progress bar and percentage
        Column(horizontalAlignment = Alignment.End) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Overall Progress ",
                    fontSize = 10.sp,
                    color = DashboardColors.TextSecondary,
                )
                Text(
                    percentText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DashboardColors.TextPrimary,
                )
            }
            Spacer(Modifier.height(3.dp))
            LinearProgressIndicator(
                progress = { progress / 100f },
                modifier = Modifier
                    .width(88.dp)
                    .height(5.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onDetails),
                color = DashboardColors.PrimaryPink,
                trackColor = DashboardColors.ProgressTrack,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                "${100 - progress}% to reach ${plan.targetLevel.label}",
                color = DashboardColors.TextSecondary,
                fontSize = 8.5.sp,
            )
        }
    }
}

// ==========================================
// ROW 2: Continue Learning, Daily Practice & Speaking Practice
// ==========================================

@Composable
private fun ContinueLearningCard(
    plan: TodayPlan,
    onResume: (HomeResumeItem) -> Unit,
    onLearn: () -> Unit,
    modifier: Modifier = Modifier,
) = DashboardCard(
    modifier = modifier,
    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
    contentSpacing = 6.dp,
) {
    DashboardHeader(
        icon = Icons.AutoMirrored.Rounded.MenuBook,
        title = "Continue Learning",
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
                .width(68.dp)
                .height(62.dp),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFFFE5F0))
                    .padding(horizontal = 5.dp, vertical = 1.dp),
            ) {
                Text(
                    "Last lesson",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = DashboardColors.PrimaryPinkDark,
                )
            }
            Text(
                text = item?.title ?: "No resumable activity yet",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = DashboardColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = item?.domain ?: "Choose a lesson from Learn to begin.",
                fontSize = 9.sp,
                color = DashboardColors.TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }

    DashboardPrimaryButton(
        text = "Continue Learning →",
        icon = Icons.Rounded.PlayArrow,
        onClick = { if (item != null) onResume(item) else onLearn() },
        modifier = Modifier.fillMaxWidth(),
        minHeight = 32.dp,
    )
}

@Composable
private fun DailyPracticeCard(plan: TodayPlan, modifier: Modifier = Modifier) = DashboardCard(
    modifier = modifier,
    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
    contentSpacing = 6.dp,
) {
    val minutes = plan.dailyMetrics.practiceMinutesToday
    val goal = plan.dailyGoalMinutes.coerceAtLeast(1)

    DashboardHeader(
        icon = Icons.Rounded.CalendarToday,
        title = "Daily Practice",
        subtitle = "Today's goal: $goal minutes",
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
                modifier = Modifier.size(46.dp),
                color = DashboardColors.PrimaryPink,
                trackColor = DashboardColors.ProgressTrack,
                strokeWidth = 4.5.dp,
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "$minutes",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DashboardColors.TextPrimary,
                    lineHeight = 15.sp,
                )
                Text(
                    "min",
                    fontSize = 7.5.sp,
                    color = DashboardColors.TextSecondary,
                    lineHeight = 9.sp,
                )
            }
        }

        // Streak indicator
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Rounded.LocalFireDepartment,
                contentDescription = null,
                tint = Color(0xFFFF5722),
                modifier = Modifier.size(24.dp),
            )
            Column(Modifier.padding(start = 4.dp)) {
                Text(
                    "${plan.dailyMetrics.streakDays}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DashboardColors.TextPrimary,
                    lineHeight = 18.sp,
                )
                Text(
                    "day streak",
                    fontSize = 9.sp,
                    color = DashboardColors.TextSecondary,
                )
            }
        }
    }

    // Goal motivation banner
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFFFF0F5))
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Rounded.TrackChanges,
                contentDescription = null,
                tint = DashboardColors.PrimaryPink,
                modifier = Modifier.size(14.dp),
            )
            Column(Modifier.padding(start = 5.dp)) {
                Text(
                    "You're doing great!",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = DashboardColors.PrimaryPinkDark,
                )
                Text(
                    if (minutes >= goal) "Today's goal is complete." else "${goal - minutes} minutes to complete today's goal.",
                    fontSize = 8.sp,
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
    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
    contentSpacing = 6.dp,
) {
    DashboardHeader(
        icon = Icons.Rounded.Mic,
        title = "Speaking Practice",
        subtitle = "Have a natural conversation with AI",
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
        text = "Start Conversation →",
        icon = Icons.Rounded.Mic,
        onClick = onSpeak,
        modifier = Modifier.fillMaxWidth(),
        minHeight = 32.dp,
    )
}

// ==========================================
// ROW 3: Today's Focus only (Full Width)
// ==========================================

@Composable
private fun FocusCard(
    plan: TodayPlan,
    onActivity: (TodayPlanItem) -> Unit,
) = DashboardCard(
    modifier = Modifier.fillMaxWidth(),
    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
    contentSpacing = 6.dp,
) {
    DashboardHeader(
        icon = Icons.Rounded.TrackChanges,
        title = "Today's Focus",
        subtitle = "Practice key skills for faster progress.",
    )

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
                            icon = card.icon,
                            background = card.background,
                            iconTint = card.iconTint,
                            enabled = item != null,
                            modifier = Modifier.weight(1f),
                            onClick = { item?.let(onActivity) },
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
    icon: ImageVector,
    background: Color,
    iconTint: Color,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .height(76.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .clickable(enabled = enabled, onClick = onClick),
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
                modifier = Modifier.size(46.dp),
            )
            Spacer(Modifier.height(3.dp))
            Text(
                title,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = DashboardColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

// ==========================================
// ROW 4: Progress Overview & Recent Activity
// ==========================================

@Composable
private fun ProgressCard(plan: TodayPlan, onDetails: () -> Unit, modifier: Modifier = Modifier) = DashboardCard(
    modifier = modifier,
    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
    contentSpacing = 6.dp,
) {
    DashboardHeader(
        icon = Icons.AutoMirrored.Rounded.ShowChart,
        title = "Progress Overview",
        subtitle = "Your skill development at a glance.",
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
                        modifier = Modifier.size(38.dp),
                        color = color,
                        trackColor = DashboardColors.ProgressTrack,
                        strokeWidth = 3.8.dp,
                    )
                    Text(
                        txt,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = DashboardColors.TextPrimary,
                    )
                }
                Spacer(Modifier.height(3.dp))
                Text(
                    name,
                    fontSize = 9.sp,
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
    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
    contentSpacing = 6.dp,
) {
    DashboardHeader(
        icon = Icons.Rounded.Schedule,
        title = "Recent Activity",
        subtitle = "Your latest learning activity.",
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
                "Complete an activity to start your history.",
                fontSize = 10.5.sp,
                color = DashboardColors.TextSecondary,
            )
        }
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
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
                        modifier = Modifier.size(13.dp),
                    )
                    Text(
                        text = item.title,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DashboardColors.TextPrimary,
                        modifier = Modifier.padding(start = 5.dp),
                        maxLines = 1,
                    )
                    Text(
                        text = item.description,
                        fontSize = 9.sp,
                        color = DashboardColors.TextSecondary,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = formatRelativeTime(item.timestamp),
                        fontSize = 8.5.sp,
                        color = DashboardColors.TextSecondary,
                    )
                }
            }
        }
    }
}

// ==========================================
// Helpers
// ==========================================

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
