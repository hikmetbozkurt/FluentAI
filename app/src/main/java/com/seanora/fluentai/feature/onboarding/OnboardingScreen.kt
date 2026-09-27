package com.seanora.fluentai.feature.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.seanora.fluentai.core.designsystem.components.BadgeSize
import com.seanora.fluentai.core.designsystem.components.ButtonVariant
import com.seanora.fluentai.core.designsystem.components.CardVariant
import com.seanora.fluentai.core.designsystem.components.FluentButton
import com.seanora.fluentai.core.designsystem.components.FluentCard
import com.seanora.fluentai.core.designsystem.components.LevelBadge
import com.seanora.fluentai.core.designsystem.components.SectionHeader
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.designsystem.theme.FluentTheme
import com.seanora.fluentai.core.model.AssessmentDomain
import com.seanora.fluentai.core.model.LearningInterest
import com.seanora.fluentai.core.model.SelfAssessedLevel
import com.seanora.fluentai.core.model.UserAvatar
import com.seanora.fluentai.core.model.UserGoal
import kotlin.math.roundToInt

@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isCompleted) {
        if (uiState.isCompleted) {
            onOnboardingComplete()
        }
    }

    OnboardingScreenContent(
        uiState = uiState,
        onSelectAvatar = viewModel::selectAvatar,
        onSelectGoal = viewModel::selectGoal,
        onToggleInterest = viewModel::toggleInterest,
        onSelectSelfAssessment = viewModel::selectSelfAssessment,
        onSelectDailyTarget = viewModel::selectDailyTarget,
        onNextStep = viewModel::nextStep,
        onPreviousStep = viewModel::previousStep,
        onStartPlacementTest = viewModel::startPlacementTest,
        onSelectOption = viewModel::selectOption,
        onSubmitProbeAnswer = viewModel::submitProbeAnswer,
        onNextProbe = viewModel::nextProbe,
        onCompleteOnboarding = viewModel::completeOnboarding,
        onDisplayNameChange = viewModel::setDisplayName,
        modifier = modifier
    )
}

@Composable
fun OnboardingScreenContent(
    uiState: OnboardingUiState,
    onSelectAvatar: (String) -> Unit,
    onSelectGoal: (UserGoal) -> Unit,
    onToggleInterest: (LearningInterest) -> Unit,
    onSelectSelfAssessment: (SelfAssessedLevel) -> Unit,
    onSelectDailyTarget: (Int, String) -> Unit,
    onNextStep: () -> Unit,
    onPreviousStep: () -> Unit,
    onStartPlacementTest: () -> Unit,
    onSelectOption: (Int) -> Unit,
    onSubmitProbeAnswer: () -> Unit,
    onNextProbe: () -> Unit,
    onCompleteOnboarding: () -> Unit,
    onDisplayNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(FluentTheme.colors.surfaceBase)
            .padding(FluentTheme.spacing.screenHorizontal)
    ) {
        when (uiState.currentStep) {
            OnboardingStep.WELCOME -> WelcomeStepView(
                displayName = uiState.displayName,
                onDisplayNameChange = onDisplayNameChange,
                onNext = onNextStep
            )
            OnboardingStep.AVATAR_SELECTION -> AvatarSelectionStepView(
                selectedAvatarKey = uiState.selectedAvatarKey,
                onSelectAvatar = onSelectAvatar,
                onNext = onNextStep,
                onBack = onPreviousStep
            )
            OnboardingStep.GOAL_SELECTION -> GoalSelectionStepView(
                selectedGoal = uiState.selectedGoal,
                onSelectGoal = onSelectGoal,
                onNext = onNextStep,
                onBack = onPreviousStep
            )
            OnboardingStep.INTERESTS_SELECTION -> InterestsSelectionStepView(
                selectedInterests = uiState.selectedInterests,
                onToggleInterest = onToggleInterest,
                onNext = onNextStep,
                onBack = onPreviousStep
            )
            OnboardingStep.SELF_ASSESSMENT -> SelfAssessmentStepView(
                selectedLevel = uiState.selectedSelfAssessment,
                onSelectLevel = onSelectSelfAssessment,
                onNext = onNextStep,
                onBack = onPreviousStep
            )
            OnboardingStep.DAILY_TARGET -> DailyTargetStepView(
                selectedMinutes = uiState.dailyPracticeMinutes,
                targetCefr = uiState.targetCefrLevel,
                onSelectTarget = onSelectDailyTarget,
                onStartPlacement = onNextStep,
                onBack = onPreviousStep
            )
            OnboardingStep.PLACEMENT_TEST -> PlacementTestStepView(
                uiState = uiState,
                onSelectOption = onSelectOption,
                onSubmit = onSubmitProbeAnswer,
                onNext = onNextProbe
            )
            OnboardingStep.PLACEMENT_RESULT -> PlacementResultStepView(
                uiState = uiState,
                onComplete = onCompleteOnboarding
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Step 1: Welcome
// ---------------------------------------------------------------------------

@Composable
private fun WelcomeStepView(
    displayName: String,
    onDisplayNameChange: (String) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "FluentAI",
            style = FluentTheme.typography.displayLarge,
            color = FluentTheme.colors.accentPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Executive English for Turkish Professionals",
            style = FluentTheme.typography.titleLarge,
            color = FluentTheme.colors.textPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tablet-first, offline-first personal English learning designed for international workplace excellence. Deterministic progression, habit breaking, and adaptive diagnostic assessment.",
            style = FluentTheme.typography.bodyLarge,
            color = FluentTheme.colors.textSecondary,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(36.dp))

        FluentCard(
            modifier = Modifier.fillMaxWidth(0.85f),
            variant = CardVariant.Elevated
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                WelcomeFeatureRow(
                    title = "Independent Multi-Skill CEFR (A2 → C2)",
                    subtitle = "Vocabulary, Grammar, Reading, and Listening evaluated separately."
                )
                WelcomeFeatureRow(
                    title = "L1 Turkish Transfer Habit Breaker",
                    subtitle = "Preposition traps, tense confusion, and false friends remediated deterministically."
                )
                WelcomeFeatureRow(
                    title = "100% Offline Core Modules",
                    subtitle = "Your curriculum and personal progress function seamlessly without internet."
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        androidx.compose.material3.OutlinedTextField(
            value = displayName,
            onValueChange = { if (it.length <= 50) onDisplayNameChange(it) },
            label = { Text("What should we call you?", color = FluentTheme.colors.textSecondary) },
            placeholder = { Text("Your name (optional)", color = FluentTheme.colors.textMuted) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.6f),
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                focusedBorderColor = FluentTheme.colors.accentPrimary,
                unfocusedBorderColor = FluentTheme.colors.surfaceBorder,
                focusedTextColor = FluentTheme.colors.textPrimary,
                unfocusedTextColor = FluentTheme.colors.textPrimary,
                cursorColor = FluentTheme.colors.accentPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        FluentButton(
            text = "Begin Personalization & Placement",
            variant = ButtonVariant.Primary,
            onClick = onNext,
            modifier = Modifier.fillMaxWidth(0.6f)
        )
    }
}

@Composable
private fun WelcomeFeatureRow(title: String, subtitle: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(FluentTheme.colors.accentPrimary)
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = "✓",
                style = FluentTheme.typography.badge,
                color = FluentTheme.colors.surfaceBase
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = title,
                style = FluentTheme.typography.titleMedium,
                color = FluentTheme.colors.textPrimary
            )
            Text(
                text = subtitle,
                style = FluentTheme.typography.bodySmall,
                color = FluentTheme.colors.textSecondary
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Step 2: Avatar Selection
// ---------------------------------------------------------------------------

@Composable
private fun AvatarSelectionStepView(
    selectedAvatarKey: String?,
    onSelectAvatar: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.md)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "STEP 1 OF 5",
                style = FluentTheme.typography.badge,
                color = FluentTheme.colors.accentSecondary
            )
            Text(
                text = "Choose your avatar",
                style = FluentTheme.typography.headlineMedium,
                color = FluentTheme.colors.textPrimary
            )
            Text(
                text = "Pick the one that feels most like you.",
                style = FluentTheme.typography.bodyLarge,
                color = FluentTheme.colors.textSecondary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserAvatar.ALL.forEach { avatar ->
                val isSelected = selectedAvatarKey == avatar.key
                AvatarCard(
                    avatar = avatar,
                    isSelected = isSelected,
                    onClick = { onSelectAvatar(avatar.key) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        NavigationButtonsRow(
            onBack = onBack,
            onNext = onNext,
            nextEnabled = selectedAvatarKey != null
        )
    }
}

@Composable
private fun AvatarCard(
    avatar: UserAvatar,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) DashboardColors.PrimaryPink else FluentTheme.colors.surfaceBorder
    val borderWidth = if (isSelected) 3.dp else 1.dp
    val backgroundColor = if (isSelected) DashboardColors.SurfacePink.copy(alpha = 0.5f) else FluentTheme.colors.surfaceElevated

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .border(borderWidth, borderColor, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .semantics {
                this.selected = isSelected
                this.contentDescription = avatar.displayName
            }
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .border(
                        if (isSelected) 3.dp else 1.dp,
                        if (isSelected) DashboardColors.PrimaryPink else FluentTheme.colors.surfaceBorder,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(avatar.drawableRes),
                    contentDescription = avatar.displayName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = avatar.displayName,
                    style = FluentTheme.typography.titleMedium,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) DashboardColors.PrimaryPinkDark else FluentTheme.colors.textPrimary
                )
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(DashboardColors.PrimaryPink),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✓",
                            style = FluentTheme.typography.badge,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Step 3: Goal Selection
// ---------------------------------------------------------------------------

@Composable
private fun GoalSelectionStepView(
    selectedGoal: UserGoal?,
    onSelectGoal: (UserGoal) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.md)
    ) {
        StepHeader(stepNumber = 2, totalSteps = 5, title = "What is your primary English goal?")

        UserGoal.values().forEach { goal ->
            val isSelected = selectedGoal == goal
            val cardVariant = if (isSelected) CardVariant.AccentOutlined else CardVariant.Standard

            FluentCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectGoal(goal) },
                variant = cardVariant
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = goal.onboardingTitle(),
                            style = FluentTheme.typography.titleMedium,
                            color = if (isSelected) FluentTheme.colors.accentPrimary else FluentTheme.colors.textPrimary
                        )
                        Text(
                            text = goal.onboardingSubtitle(),
                            style = FluentTheme.typography.bodySmall,
                            color = FluentTheme.colors.textSecondary
                        )
                    }
                    if (isSelected) {
                        Text(
                            text = "SELECTED",
                            style = FluentTheme.typography.badge,
                            color = FluentTheme.colors.accentPrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        NavigationButtonsRow(
            onBack = onBack,
            onNext = onNext,
            nextEnabled = selectedGoal != null
        )
    }
}

// ---------------------------------------------------------------------------
// Step 4: Interests Selection
// ---------------------------------------------------------------------------

@Composable
private fun InterestsSelectionStepView(
    selectedInterests: Set<LearningInterest>,
    onToggleInterest: (LearningInterest) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.md)
    ) {
        StepHeader(stepNumber = 3, totalSteps = 5, title = "Select your professional interests")

        LearningInterest.values().forEach { interest ->
            val isSelected = selectedInterests.contains(interest)
            val cardVariant = if (isSelected) CardVariant.AccentOutlined else CardVariant.Standard

            FluentCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleInterest(interest) },
                variant = cardVariant
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = interest.onboardingLabel(),
                        style = FluentTheme.typography.titleMedium,
                        color = if (isSelected) FluentTheme.colors.accentPrimary else FluentTheme.colors.textPrimary
                    )
                    if (isSelected) {
                        Text(
                            text = "✓ ADDED",
                            style = FluentTheme.typography.badge,
                            color = FluentTheme.colors.accentPrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        NavigationButtonsRow(
            onBack = onBack,
            onNext = onNext,
            nextEnabled = selectedInterests.isNotEmpty()
        )
    }
}

// ---------------------------------------------------------------------------
// Step 5: Self Assessment
// ---------------------------------------------------------------------------

@Composable
private fun SelfAssessmentStepView(
    selectedLevel: SelfAssessedLevel,
    onSelectLevel: (SelfAssessedLevel) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.md)
    ) {
        StepHeader(stepNumber = 4, totalSteps = 5, title = "Where do you estimate your current level?")

        SelfAssessedLevel.values().forEach { level ->
            val isSelected = selectedLevel == level
            val cardVariant = if (isSelected) CardVariant.AccentOutlined else CardVariant.Standard

            FluentCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectLevel(level) },
                variant = cardVariant
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = level.onboardingTitle(),
                                style = FluentTheme.typography.titleMedium,
                                color = if (isSelected) FluentTheme.colors.accentPrimary else FluentTheme.colors.textPrimary
                            )
                            LevelBadge(levelString = level.initialProbeLevel, size = BadgeSize.Compact)
                        }
                        Text(
                            text = level.onboardingDescription(),
                            style = FluentTheme.typography.bodySmall,
                            color = FluentTheme.colors.textSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        NavigationButtonsRow(
            onBack = onBack,
            onNext = onNext,
            nextEnabled = true
        )
    }
}

// ---------------------------------------------------------------------------
// Step 6: Daily Target
// ---------------------------------------------------------------------------

@Composable
private fun DailyTargetStepView(
    selectedMinutes: Int,
    targetCefr: String,
    onSelectTarget: (Int, String) -> Unit,
    onStartPlacement: () -> Unit,
    onBack: () -> Unit
) {
    val durationOptions = listOf(10, 20, 30)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.md)
    ) {
        StepHeader(stepNumber = 5, totalSteps = 5, title = "Daily Commitment & Target CEFR")

        Text(
            text = "Preferred Daily Practice Duration",
            style = FluentTheme.typography.titleSmall,
            color = FluentTheme.colors.textSecondary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            durationOptions.forEach { minutes ->
                val isSelected = selectedMinutes == minutes
                val cardVariant = if (isSelected) CardVariant.AccentOutlined else CardVariant.Standard

                FluentCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSelectTarget(minutes, targetCefr) },
                    variant = cardVariant
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "$minutes min",
                            style = FluentTheme.typography.titleLarge,
                            color = if (isSelected) FluentTheme.colors.accentPrimary else FluentTheme.colors.textPrimary
                        )
                        Text(
                            text = if (minutes == 20) "Recommended" else "Daily",
                            style = FluentTheme.typography.labelSmall,
                            color = FluentTheme.colors.textMuted
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Target CEFR Level",
            style = FluentTheme.typography.titleSmall,
            color = FluentTheme.colors.textSecondary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf("B2", "C1", "C2").forEach { lvl ->
                val isSelected = targetCefr == lvl
                val cardVariant = if (isSelected) CardVariant.AccentOutlined else CardVariant.Standard

                FluentCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSelectTarget(selectedMinutes, lvl) },
                    variant = cardVariant
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        LevelBadge(levelString = lvl)
                        Text(
                            text = if (lvl == "C1") "Executive Goal" else "Goal",
                            style = FluentTheme.typography.labelSmall,
                            color = FluentTheme.colors.textMuted
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FluentButton(
                text = "Back",
                variant = ButtonVariant.Outlined,
                onClick = onBack
            )
            FluentButton(
                text = "Start Diagnostic Placement Test",
                variant = ButtonVariant.Primary,
                onClick = onStartPlacement
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Step 6: Placement Test View
// ---------------------------------------------------------------------------

@Composable
private fun PlacementTestStepView(
    uiState: OnboardingUiState,
    onSelectOption: (Int) -> Unit,
    onSubmit: () -> Unit,
    onNext: () -> Unit
) {
    val probe = uiState.currentProbe
    val currentProbeNumber = uiState.completedResponses.size + 1
    val totalProbes = uiState.totalProbesTarget

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.md)
    ) {
        // Progress Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Question $currentProbeNumber of $totalProbes",
                style = FluentTheme.typography.titleMedium,
                color = FluentTheme.colors.textPrimary
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = uiState.currentDomain.displayName,
                    style = FluentTheme.typography.labelMedium,
                    color = FluentTheme.colors.accentSecondary
                )
                LevelBadge(levelString = uiState.currentTargetLevel, size = BadgeSize.Compact)
            }
        }

        if (probe == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = FluentTheme.colors.accentPrimary)
            }
            return
        }

        // Probe Card
        FluentCard(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            variant = CardVariant.Elevated
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (probe.promptContext != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(FluentTheme.shapes.card)
                            .background(FluentTheme.colors.surfaceElevated)
                            .padding(FluentTheme.spacing.md)
                    ) {
                        Text(
                            text = probe.promptContext,
                            style = FluentTheme.typography.bodyMedium,
                            color = FluentTheme.colors.textSecondary
                        )
                    }
                }

                Text(
                    text = probe.promptEn,
                    style = FluentTheme.typography.titleLarge,
                    color = FluentTheme.colors.textPrimary
                )

                // Options List
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    probe.options.forEachIndexed { index, optionText ->
                        val isSelected = uiState.selectedOptionIndex == index
                        val isSubmitted = uiState.isAnswerSubmitted
                        val isCorrectOption = index == probe.correctOptionIndex

                        val borderColor = when {
                            isSubmitted && isCorrectOption -> Color(0xFF10B981)
                            isSubmitted && isSelected && !isCorrectOption -> Color(0xFFEF4444)
                            isSelected -> FluentTheme.colors.accentPrimary
                            else -> FluentTheme.colors.surfaceBorder
                        }

                        val bg = when {
                            isSubmitted && isCorrectOption -> Color(0xFF10B981).copy(alpha = 0.16f)
                            isSubmitted && isSelected && !isCorrectOption -> Color(0xFFEF4444).copy(alpha = 0.16f)
                            isSelected -> FluentTheme.colors.accentPrimary.copy(alpha = 0.16f)
                            else -> FluentTheme.colors.surfaceElevated
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(FluentTheme.shapes.md)
                                .background(bg)
                                .border(1.dp, borderColor, FluentTheme.shapes.md)
                                .clickable(enabled = !isSubmitted) { onSelectOption(index) }
                                .padding(14.dp)
                        ) {
                            Text(
                                text = optionText,
                                style = FluentTheme.typography.bodyLarge,
                                color = FluentTheme.colors.textPrimary
                            )
                        }
                    }
                }

                // Feedback after submit
                if (uiState.isAnswerSubmitted) {
                    val isCorrect = uiState.selectedOptionIndex == probe.correctOptionIndex
                    val feedbackColor = if (isCorrect) Color(0xFF10B981) else Color(0xFFEF4444)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(FluentTheme.shapes.md)
                            .background(feedbackColor.copy(alpha = 0.12f))
                            .border(1.dp, feedbackColor.copy(alpha = 0.4f), FluentTheme.shapes.md)
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = if (isCorrect) "Correct!" else "Incorrect.",
                                style = FluentTheme.typography.titleMedium,
                                color = feedbackColor
                            )
                            Text(
                                text = probe.explanationEn,
                                style = FluentTheme.typography.bodySmall,
                                color = FluentTheme.colors.textPrimary
                            )
                            Text(
                                text = probe.turkishNote,
                                style = FluentTheme.typography.labelSmall,
                                color = FluentTheme.colors.textMuted
                            )
                        }
                    }
                }
            }
        }

        // Action CTA
        if (!uiState.isAnswerSubmitted) {
            FluentButton(
                text = "Submit Answer",
                variant = ButtonVariant.Primary,
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            FluentButton(
                text = if (currentProbeNumber < totalProbes) "Next Question" else "Finish Placement Test",
                variant = ButtonVariant.Primary,
                onClick = onNext,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Step 7: Placement Result & Profile Summary
// ---------------------------------------------------------------------------

@Composable
private fun PlacementResultStepView(
    uiState: OnboardingUiState,
    onComplete: () -> Unit
) {
    val summary = uiState.placementSummary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.md)
    ) {
        SectionHeader(
            title = "Placement Result",
            subtitle = "Your skill-specific baseline has been established."
        )

        if (summary != null) {
            // Overall Metric Card
            FluentCard(
                modifier = Modifier.fillMaxWidth(),
                variant = CardVariant.AccentOutlined
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Estimated Overall Level",
                            style = FluentTheme.typography.labelMedium,
                            color = FluentTheme.colors.textMuted
                        )
                        Text(
                            text = summary.overallEstimatedLevel,
                            style = FluentTheme.typography.displayLarge,
                            color = FluentTheme.colors.accentPrimary
                        )
                        Text(
                            text = "${(summary.confidence * 100).roundToInt()}% placement confidence",
                            style = FluentTheme.typography.labelSmall,
                            color = FluentTheme.colors.textSecondary
                        )
                    }
                    LevelBadge(levelString = summary.overallEstimatedLevel)
                }
            }

            // Recommended Focus
            FluentCard(
                modifier = Modifier.fillMaxWidth(),
                variant = CardVariant.Standard
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Initial Recommended Focus",
                        style = FluentTheme.typography.labelSmall,
                        color = FluentTheme.colors.accentSecondary
                    )
                    Text(
                        text = summary.recommendedFocus,
                        style = FluentTheme.typography.bodyMedium,
                        color = FluentTheme.colors.textPrimary
                    )
                }
            }

            // Skill Breakdown List
            SectionHeader(
                title = "Independent Skill Breakdown",
                subtitle = "Your proficiency is estimated separately for each language skill."
            )

            summary.skillPlacements.forEach { skill ->
                FluentCard(
                    modifier = Modifier.fillMaxWidth(),
                    variant = CardVariant.Standard
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = skill.domain.displayName,
                                style = FluentTheme.typography.titleMedium,
                                color = FluentTheme.colors.textPrimary
                            )
                            Text(
                                text = "${skill.correctProbes} of ${skill.totalProbes} questions correct (${(skill.confidence * 100).roundToInt()}% confidence)",
                                style = FluentTheme.typography.bodySmall,
                                color = FluentTheme.colors.textSecondary
                            )
                        }
                        LevelBadge(levelString = skill.estimatedLevel)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        FluentButton(
            text = if (uiState.isSavingProfile) "Saving Profile..." else "Continue to Home",
            variant = ButtonVariant.Primary,
            onClick = onComplete,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// ---------------------------------------------------------------------------
// Shared Helpers
// ---------------------------------------------------------------------------

@Composable
private fun StepHeader(stepNumber: Int, totalSteps: Int, title: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "STEP $stepNumber OF $totalSteps",
            style = FluentTheme.typography.badge,
            color = FluentTheme.colors.accentSecondary
        )
        Text(
            text = title,
            style = FluentTheme.typography.headlineMedium,
            color = FluentTheme.colors.textPrimary
        )
    }
}

@Composable
private fun NavigationButtonsRow(
    onBack: () -> Unit,
    onNext: () -> Unit,
    nextEnabled: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FluentButton(
            text = "Back",
            variant = ButtonVariant.Outlined,
            onClick = onBack
        )
        FluentButton(
            text = "Continue",
            variant = ButtonVariant.Primary,
            enabled = nextEnabled,
            onClick = onNext
        )
    }
}
