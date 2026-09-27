package com.seanora.fluentai.feature.speaking

import android.Manifest
import android.app.Activity
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.seanora.fluentai.ai.live.TranscriptEntry
import com.seanora.fluentai.ai.live.TranscriptRole
import com.seanora.fluentai.ai.live.VoiceUiState
import com.seanora.fluentai.ai.live.SpeakingPersona
import com.seanora.fluentai.ai.live.SpeakingSessionType
import com.seanora.fluentai.core.designsystem.components.ButtonVariant
import com.seanora.fluentai.core.designsystem.components.CardVariant
import com.seanora.fluentai.core.designsystem.components.CefrLevel
import com.seanora.fluentai.core.designsystem.components.FluentButton
import com.seanora.fluentai.core.designsystem.components.FluentCard
import com.seanora.fluentai.core.designsystem.components.LevelBadge
import com.seanora.fluentai.core.designsystem.components.SectionHeader
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.designsystem.dashboard.DashboardDimensions
import com.seanora.fluentai.core.designsystem.theme.FluentColors
import com.seanora.fluentai.core.designsystem.theme.FluentTheme
import com.seanora.fluentai.core.designsystem.theme.LocalFluentColors
import com.seanora.fluentai.core.model.CorrectionMode
import com.seanora.fluentai.core.model.SpeakingCategory
import com.seanora.fluentai.core.model.SpeakingGrammarObservation
import com.seanora.fluentai.core.model.SpeakingScenario
import com.seanora.fluentai.core.model.SpeakingSessionAnalysis
import com.seanora.fluentai.core.model.SpeakingVocabObservation

@Composable
fun SpeakingScreen(
    modifier: Modifier = Modifier,
    viewModel: SpeakingViewModel = hiltViewModel(),
    initialContentId: String? = null,
    onAssignedActivityCompleted: (() -> Unit)? = null,
    showGaladrielStart: Boolean = false,
    onOpenGaladriel: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var pendingScenario by remember { mutableStateOf<SpeakingScenario?>(null) }
    var pendingMode by remember { mutableStateOf<CorrectionMode?>(null) }
    var pendingGaladrielStart by remember { mutableStateOf(false) }

    LaunchedEffect(initialContentId) {
        initialContentId?.let(viewModel::selectScenarioById)
    }
    LaunchedEffect(uiState.analysisResult) {
        if (uiState.analysisResult != null) onAssignedActivityCompleted?.invoke()
    }

    val prefs = remember(context) {
        context.getSharedPreferences("fluentai_speaking_prefs", android.content.Context.MODE_PRIVATE)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val hasPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
                if (hasPermission) {
                    viewModel.clearPermissionError()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        val scenarioToStart = pendingScenario
        val modeToStart = pendingMode
        val shouldStartGaladriel = pendingGaladrielStart
        pendingScenario = null
        pendingMode = null
        pendingGaladrielStart = false

        if (isGranted) {
            prefs.edit().putBoolean("mic_permission_requested_before", true).apply()
            viewModel.clearPermissionError()
            if (scenarioToStart != null) {
                viewModel.startSession(scenarioToStart, modeToStart)
            } else if (shouldStartGaladriel) {
                viewModel.startGaladrielSession()
            }
        } else {
            val activity = generateSequence(context) { (it as? ContextWrapper)?.baseContext }
                .filterIsInstance<Activity>()
                .firstOrNull()
            val wasRequestedBefore = prefs.getBoolean("mic_permission_requested_before", false)
            val shouldShowRationale = activity != null && ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.RECORD_AUDIO
            )
            val permanentlyDenied = wasRequestedBefore && activity != null && !shouldShowRationale
            prefs.edit().putBoolean("mic_permission_requested_before", true).apply()
            viewModel.onPermissionDenied(permanentlyDenied = permanentlyDenied)
        }
    }

    val handleStartSession: (SpeakingScenario, CorrectionMode) -> Unit = { scenario, mode ->
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            prefs.edit().putBoolean("mic_permission_requested_before", true).apply()
            viewModel.clearPermissionError()
            viewModel.startSession(scenario, mode)
        } else {
            if (pendingScenario == null) {
                pendingScenario = scenario
                pendingMode = mode
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    val handleStartGaladriel: () -> Unit = {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            prefs.edit().putBoolean("mic_permission_requested_before", true).apply()
            viewModel.clearPermissionError()
            viewModel.startGaladrielSession()
        } else if (!pendingGaladrielStart) {
            pendingGaladrielStart = true
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    SpeakingScreenContent(
        uiState = uiState,
        onStartSession = handleStartSession,
        onStartGaladriel = handleStartGaladriel,
        showGaladrielStart = showGaladrielStart,
        onOpenGaladriel = onOpenGaladriel,
        onNavigateBack = onNavigateBack,
        onTogglePause = { viewModel.togglePause() },
        onToggleMute = { viewModel.toggleMute() },
        onEndSession = { viewModel.endSession() },
        onSelectScenario = { viewModel.selectScenario(it) },
        onSelectCategory = { viewModel.selectCategory(it) },
        onSelectCefrLevel = { viewModel.selectCefrLevel(it) },
        onSelectMode = { viewModel.selectCorrectionMode(it) },
        onDismissReport = { viewModel.dismissReport() },
        onDismissError = { viewModel.dismissError() },
        modifier = modifier
    )
}

@Composable
fun SpeakingScreenContent(
    uiState: SpeakingUiState,
    onStartSession: (SpeakingScenario, CorrectionMode) -> Unit,
    onStartGaladriel: () -> Unit,
    showGaladrielStart: Boolean,
    onOpenGaladriel: () -> Unit,
    onNavigateBack: () -> Unit,
    onTogglePause: () -> Unit,
    onToggleMute: () -> Unit,
    onEndSession: () -> Unit,
    onSelectScenario: (SpeakingScenario) -> Unit,
    onSelectCategory: (SpeakingCategory?) -> Unit,
    onSelectCefrLevel: (String?) -> Unit,
    onSelectMode: (CorrectionMode) -> Unit,
    onDismissReport: () -> Unit,
    onDismissError: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lightColors = FluentColors(
        surfaceBase = DashboardColors.BackgroundPrimary,
        surfaceCard = DashboardColors.Surface,
        surfaceElevated = DashboardColors.SurfacePink,
        surfaceBorder = DashboardColors.BorderSoft,
        surfaceHighlight = DashboardColors.PrimaryPinkLight,
        accentPrimary = DashboardColors.PrimaryPink,
        accentSecondary = DashboardColors.PrimaryPinkDark,
        accentGlow = DashboardColors.PrimaryPink.copy(alpha = 0.18f),
        accentGradient = Brush.horizontalGradient(
            listOf(DashboardColors.PrimaryPink, DashboardColors.PrimaryPinkDark)
        ),
        textPrimary = DashboardColors.TextPrimary,
        textSecondary = DashboardColors.TextSecondary,
        textMuted = DashboardColors.TextSecondary.copy(alpha = 0.78f)
    )
    CompositionLocalProvider(LocalFluentColors provides lightColors) {
        when {
            uiState.isReportVisible -> {
                SpeakingPostSessionReportView(
                    uiState = uiState,
                    onPracticeAgain = {
                        if (uiState.activeLiveConfig?.persona == SpeakingPersona.GALADRIEL) {
                            onStartGaladriel()
                        } else {
                            uiState.selectedScenario?.let { scenario ->
                                onStartSession(scenario, uiState.selectedMode)
                            }
                        }
                    },
                    onDismiss = onDismissReport,
                    modifier = modifier
                )
            }
            uiState.isSessionActive -> {
                ActiveSpeakingSessionView(
                    uiState = uiState,
                    onTogglePause = onTogglePause,
                    onToggleMute = onToggleMute,
                    onEndSession = onEndSession,
                    modifier = modifier
                )
            }
            showGaladrielStart -> {
                GaladrielFreeTalkStartView(
                    errorMessage = uiState.errorMessage,
                    onStartConversation = onStartGaladriel,
                    onNavigateBack = onNavigateBack,
                    onDismissError = onDismissError,
                    modifier = modifier
                )
            }
            else -> {
                SpeakingScenarioCatalogView(
                    uiState = uiState,
                    onStartSession = onStartSession,
                    onSelectScenario = onSelectScenario,
                    onSelectCategory = onSelectCategory,
                    onSelectCefrLevel = onSelectCefrLevel,
                    onSelectMode = onSelectMode,
                    onOpenGaladriel = onOpenGaladriel,
                    onDismissError = onDismissError,
                    modifier = modifier
                )
            }
        }
    }
}

/**
 * Catalog browser for Speaking Scenarios across 8 categories with correction mode selector.
 */
@Composable
private fun SpeakingScenarioCatalogView(
    uiState: SpeakingUiState,
    onStartSession: (SpeakingScenario, CorrectionMode) -> Unit,
    onSelectScenario: (SpeakingScenario) -> Unit,
    onSelectCategory: (SpeakingCategory?) -> Unit,
    onSelectCefrLevel: (String?) -> Unit,
    onSelectMode: (CorrectionMode) -> Unit,
    onOpenGaladriel: () -> Unit,
    onDismissError: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DashboardColors.BackgroundPrimary)
            .verticalScroll(rememberScrollState())
            .padding(FluentTheme.spacing.screenHorizontal),
        verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.sectionSpacing),
    ) {
        LightSectionHeader(
            title = "Live Speaking Coach",
            subtitle = "Targeted spoken English scenarios with real-time coaching, L1 trap defense, and structured feedback."
        )

        // Error Banner
        if (uiState.errorMessage != null) {
            val context = LocalContext.current
            val isPermanentlyDenied = uiState.isPermissionError && uiState.errorMessage.contains("Settings", ignoreCase = true)
            LightSpeakSurface(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.sm)
                ) {
                    Text(
                        text = if (isPermanentlyDenied) "Microphone Access Blocked" else "Live Session Notice",
                        style = FluentTheme.typography.titleMedium,
                        color = FluentTheme.colors.error
                    )
                    Text(
                        text = uiState.errorMessage,
                        style = FluentTheme.typography.bodyMedium,
                        color = DashboardColors.TextPrimary
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(FluentTheme.spacing.sm, Alignment.End)
                    ) {
                        if (isPermanentlyDenied) {
                            SpeakButton(
                                text = "Open Settings",
                                primary = true,
                                onClick = {
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                        data = Uri.fromParts("package", context.packageName, null)
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    }
                                    context.startActivity(intent)
                                }
                            )
                        }
                        SpeakButton(
                            text = "Dismiss",
                            onClick = onDismissError
                        )
                    }
                }
            }
        }

        // Correction Mode Selector Section
        Column(verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.sm)) {
            Text(
                text = "Correction Mode",
                style = FluentTheme.typography.titleMedium,
                color = DashboardColors.TextPrimary
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(FluentTheme.spacing.sm)
            ) {
                CorrectionMode.entries.forEach { mode ->
                    val isSelected = (mode == uiState.selectedMode)
                    ModeCard(
                        mode = mode,
                        isSelected = isSelected,
                        onClick = { onSelectMode(mode) }
                    )
                }
                GaladrielPresetCard(onClick = onOpenGaladriel)
            }
        }

        // Category Filter Chips
        Column(verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.sm)) {
            Text(
                text = "Category Filter",
                style = FluentTheme.typography.titleMedium,
                color = DashboardColors.TextPrimary
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(FluentTheme.spacing.xs)
            ) {
                FilterPill(
                    label = "All Scenarios",
                    isSelected = (uiState.selectedCategory == null),
                    onClick = { onSelectCategory(null) }
                )
                SpeakingCategory.entries.forEach { cat ->
                    FilterPill(
                        label = cat.displayName,
                        isSelected = (uiState.selectedCategory == cat),
                        onClick = { onSelectCategory(cat) }
                    )
                }
            }
        }

        // CEFR Level Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(FluentTheme.spacing.xs)
        ) {
            FilterPill(
                label = "All Levels",
                isSelected = (uiState.selectedCefrLevel == null),
                onClick = { onSelectCefrLevel(null) }
            )
            listOf("A2", "B1", "B2", "C1", "C2").forEach { lvl ->
                FilterPill(
                    label = lvl,
                    isSelected = (uiState.selectedCefrLevel.equals(lvl, ignoreCase = true)),
                    onClick = { onSelectCefrLevel(lvl) }
                )
            }
        }

        // Scenarios List
        LightSectionHeader(
            title = "Curriculum Scenarios (${uiState.filteredScenarios.size})",
            subtitle = "Select a scenario to start real-time conversation."
        )

        Column(verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.md)) {
            uiState.filteredScenarios.forEach { scenario ->
                ScenarioCard(
                    scenario = scenario,
                    selectedMode = uiState.selectedMode,
                    onStart = { onStartSession(scenario, uiState.selectedMode) }
                )
            }
        }
    }
}

@Composable
private fun LightSectionHeader(title: String, subtitle: String? = null) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = FluentTheme.typography.titleLarge,
            color = DashboardColors.TextPrimary
        )
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = FluentTheme.typography.bodyMedium,
                color = DashboardColors.TextSecondary
            )
        }
    }
}

@Composable
private fun LightSpeakSurface(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(DashboardDimensions.CardRadius),
    background: Color = DashboardColors.Surface,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(background)
            .border(1.dp, DashboardColors.BorderSoft, shape)
            .padding(FluentTheme.spacing.cardInternal)
    ) {
        content()
    }
}

@Composable
private fun SpeakButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    primary: Boolean = false,
    danger: Boolean = false
) {
    val background = when {
        danger -> FluentTheme.colors.error
        primary -> DashboardColors.PrimaryPink
        else -> DashboardColors.Surface
    }
    val foreground = if (primary || danger) Color.White else DashboardColors.TextPrimary
    val border = when {
        danger -> FluentTheme.colors.error
        primary -> DashboardColors.PrimaryPink
        else -> DashboardColors.BorderSoft
    }
    Box(
        modifier = modifier
            .heightIn(min = 48.dp)
            .clip(RoundedCornerShape(DashboardDimensions.ButtonRadius))
            .background(background)
            .border(1.dp, border, RoundedCornerShape(DashboardDimensions.ButtonRadius))
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = FluentTheme.typography.labelLarge,
            color = foreground
        )
    }
}

@Composable
private fun ModeCard(
    mode: CorrectionMode,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) DashboardColors.PrimaryPink else DashboardColors.BorderSoft
    val bgColor = if (isSelected) DashboardColors.SurfacePink else DashboardColors.Surface

    Box(
        modifier = Modifier
            .width(160.dp)
            .clip(FluentTheme.shapes.card)
            .background(bgColor)
            .border(width = if (isSelected) 2.dp else 1.dp, color = borderColor, shape = FluentTheme.shapes.card)
            .clickable(onClick = onClick)
            .padding(FluentTheme.spacing.cardInternal)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.xs)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = mode.displayName,
                    style = FluentTheme.typography.titleMedium,
                    color = if (isSelected) DashboardColors.PrimaryPinkDark else DashboardColors.TextPrimary
                )
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(DashboardColors.PrimaryPink)
                    )
                }
            }
            Text(
                text = mode.shortDescription,
                style = FluentTheme.typography.labelSmall,
                color = DashboardColors.TextSecondary
            )
        }
    }
}

@Composable
private fun GaladrielPresetCard(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .clip(FluentTheme.shapes.card)
            .background(DashboardColors.Surface)
            .border(1.dp, DashboardColors.BorderSoft, FluentTheme.shapes.card)
            .clickable(onClick = onClick)
            .padding(FluentTheme.spacing.cardInternal)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.xs)) {
            Text(
                text = "Galadriel",
                style = FluentTheme.typography.titleMedium,
                color = DashboardColors.TextPrimary
            )
            Text(
                text = "Free Talk",
                style = FluentTheme.typography.labelSmall,
                color = DashboardColors.TextSecondary
            )
            Text(
                text = "Talk naturally about anything.",
                style = FluentTheme.typography.labelSmall,
                color = DashboardColors.TextSecondary.copy(alpha = 0.82f)
            )
        }
    }
}

@Composable
private fun GaladrielFreeTalkStartView(
    errorMessage: String?,
    onStartConversation: () -> Unit,
    onNavigateBack: () -> Unit,
    onDismissError: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DashboardColors.BackgroundPrimary)
            .padding(FluentTheme.spacing.screenHorizontal),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.widthIn(max = 520.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.lg)
        ) {
            LightSectionHeader(title = "Galadriel", subtitle = "Free Talk")
            LightSpeakSurface(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.md)
                ) {
                    Text(
                        text = "A relaxed English conversation with your AI friend.",
                        style = FluentTheme.typography.bodyLarge,
                        color = DashboardColors.TextSecondary
                    )
                    if (!errorMessage.isNullOrBlank()) {
                        Text(
                            text = errorMessage,
                            style = FluentTheme.typography.bodyMedium,
                            color = FluentTheme.colors.error
                        )
                        SpeakButton(text = "Dismiss", onClick = onDismissError)
                    }
                    SpeakButton(
                        text = "Start Conversation",
                        primary = true,
                        onClick = onStartConversation,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            SpeakButton(text = "Back to Speak", onClick = onNavigateBack)
        }
    }
}

@Composable
private fun FilterPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) DashboardColors.PrimaryPink else DashboardColors.Surface
    val textColor = if (isSelected) Color.White else DashboardColors.TextPrimary

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(
                width = 1.dp,
                color = if (isSelected) DashboardColors.PrimaryPink else DashboardColors.BorderSoft,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = FluentTheme.typography.labelMedium,
            color = textColor
        )
    }
}

@Composable
private fun ScenarioCard(
    scenario: SpeakingScenario,
    selectedMode: CorrectionMode,
    onStart: () -> Unit
) {
    val level = parseCefrLevel(scenario.cefrLevel)

    LightSpeakSurface(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.sm)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(FluentTheme.spacing.xs)) {
                    LevelBadge(level = level)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(DashboardColors.SurfacePink)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = scenario.category.displayName,
                            style = FluentTheme.typography.labelSmall,
                            color = DashboardColors.PrimaryPinkDark
                        )
                    }
                }

                Text(
                    text = "Recommended: ${scenario.recommendedCorrectionMode.displayName}",
                    style = FluentTheme.typography.labelSmall,
                    color = DashboardColors.TextSecondary
                )
            }

            Text(
                text = scenario.title,
                style = FluentTheme.typography.titleLarge,
                color = DashboardColors.TextPrimary
            )

            Text(
                text = scenario.contextDescription,
                style = FluentTheme.typography.bodyMedium,
                color = DashboardColors.TextSecondary
            )

            if (scenario.suggestedStarterPhrases.isNotEmpty()) {
                Text(
                    text = "Starter: \"${scenario.suggestedStarterPhrases.first()}\"",
                    style = FluentTheme.typography.bodySmall,
                    color = DashboardColors.TextSecondary
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                SpeakButton(
                    text = "Start Session (${selectedMode.displayName})",
                    primary = true,
                    onClick = onStart
                )
            }
        }
    }
}

/**
 * Post-Session Analysis Report View per Phase 6 requirements.
 */
@Composable
private fun SpeakingPostSessionReportView(
    uiState: SpeakingUiState,
    onPracticeAgain: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.isAnalyzing) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(FluentTheme.colors.surfaceBase)
                .padding(FluentTheme.spacing.screenHorizontal),
            contentAlignment = Alignment.Center
        ) {
            FluentCard(
                modifier = Modifier.width(420.dp),
                variant = CardVariant.Elevated
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(FluentTheme.spacing.lg),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.md)
                ) {
                    CircularProgressIndicator(
                        color = FluentTheme.colors.accentPrimary,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = "Analyzing Spoken Dialogue...",
                        style = FluentTheme.typography.headlineMedium,
                        color = FluentTheme.colors.textPrimary
                    )
                    Text(
                        text = "Evaluating grammatical precision, lexical diversity, Turkish L1 transfer patterns, and CEFR alignment.",
                        style = FluentTheme.typography.bodyMedium,
                        color = FluentTheme.colors.textSecondary,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
        return
    }

    val analysis = uiState.postSessionAnalysis
    if (analysis == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(FluentTheme.colors.surfaceBase)
                .padding(FluentTheme.spacing.screenHorizontal),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.md)
            ) {
                Text(
                    text = uiState.errorMessage ?: "No report available for this session.",
                    style = FluentTheme.typography.bodyLarge,
                    color = FluentTheme.colors.textSecondary
                )
                FluentButton(
                    text = "Back to Scenarios",
                    variant = ButtonVariant.Primary,
                    onClick = onDismiss
                )
            }
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FluentTheme.colors.surfaceBase)
            .padding(FluentTheme.spacing.screenHorizontal),
        verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.md)
    ) {
        // Report Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.xs)) {
                Text(
                    text = "Speaking Session Analysis",
                    style = FluentTheme.typography.headlineMedium,
                    color = FluentTheme.colors.textPrimary
                )
                Text(
                    text = "${analysis.scenarioTitle} • ${analysis.durationSeconds}s • ${analysis.turnsCount} turns",
                    style = FluentTheme.typography.bodyMedium,
                    color = FluentTheme.colors.textSecondary
                )
            }
            LevelBadge(level = parseCefrLevel(analysis.estimatedTurnCefr))
        }

        // Scores Overview Card
        FluentCard(
            modifier = Modifier.fillMaxWidth(),
            variant = CardVariant.AccentOutlined
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.sm)) {
                Text(
                    text = "Linguistic Evaluation",
                    style = FluentTheme.typography.titleMedium,
                    color = FluentTheme.colors.textPrimary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ScorePill(label = "Fluency", score = analysis.fluencyScore)
                    ScorePill(label = "Grammar", score = analysis.grammarScore)
                    ScorePill(label = "Vocabulary", score = analysis.vocabularyScore)
                    ScorePill(label = "Coherence", score = analysis.coherenceScore)
                }

                Text(
                    text = analysis.overallFeedback,
                    style = FluentTheme.typography.bodyMedium,
                    color = FluentTheme.colors.textPrimary
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.md)
        ) {
            // Strengths Section
            if (analysis.strengths.isNotEmpty()) {
            FluentCard(
                modifier = Modifier.fillMaxWidth(),
                variant = CardVariant.Elevated
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.sm)) {
                    Text(
                        text = "Key Strengths",
                        style = FluentTheme.typography.titleMedium,
                        color = FluentTheme.colors.textPrimary
                    )
                    analysis.strengths.forEach { strength ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(FluentTheme.spacing.xs),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(text = "✓", color = FluentTheme.colors.accentPrimary, style = FluentTheme.typography.bodyLarge)
                            Text(text = strength, style = FluentTheme.typography.bodyMedium, color = FluentTheme.colors.textPrimary)
                        }
                    }
                }
            }
            }

        // Grammar Observations & L1 Traps Section
            if (analysis.grammarObservations.isNotEmpty()) {
            SectionHeader(
                title = "Grammar & Turkish L1 Traps (${analysis.grammarObservations.size})",
                subtitle = "Errors automatically cataloged into your personal Mistake Bank."
            )
            analysis.grammarObservations.forEach { obs ->
                GrammarObservationCard(obs = obs)
            }
            }

        // Vocabulary Observations & Upgrades
            if (analysis.vocabularyObservations.isNotEmpty()) {
            SectionHeader(
                title = "Executive Vocabulary Upgrades (${analysis.vocabularyObservations.size})",
                subtitle = "High-impact register elevation for corporate and technical discussions."
            )
            analysis.vocabularyObservations.forEach { obs ->
                VocabObservationCard(obs = obs)
            }
            }

        // Natural Reformulations
            if (analysis.naturalAlternatives.isNotEmpty()) {
            SectionHeader(
                title = "Natural Alternatives",
                subtitle = "How native executive speakers phrase these concepts."
            )
            analysis.naturalAlternatives.forEach { alt ->
                FluentCard(
                    modifier = Modifier.fillMaxWidth(),
                    variant = CardVariant.Standard
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.xs)) {
                        Text(
                            text = "You said: \"${alt.originalUtterance}\"",
                            style = FluentTheme.typography.bodySmall,
                            color = FluentTheme.colors.textMuted
                        )
                        Text(
                            text = "Natural: \"${alt.naturalAlternative}\"",
                            style = FluentTheme.typography.titleMedium,
                            color = FluentTheme.colors.accentPrimary
                        )
                        Text(
                            text = alt.explanation,
                            style = FluentTheme.typography.bodySmall,
                            color = FluentTheme.colors.textSecondary
                        )
                    }
                }
            }
            }

        // Suggested Practice Section
            if (analysis.suggestedPractice.isNotEmpty()) {
            SectionHeader(
                title = "Suggested Next Practice",
                subtitle = "Direct curriculum recommendations based on this session."
            )
            analysis.suggestedPractice.forEach { practice ->
                FluentCard(
                    modifier = Modifier.fillMaxWidth(),
                    variant = CardVariant.Standard
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.xs)) {
                            Text(
                                text = practice.title,
                                style = FluentTheme.typography.titleMedium,
                                color = FluentTheme.colors.textPrimary
                            )
                            Text(
                                text = practice.description,
                                style = FluentTheme.typography.bodySmall,
                                color = FluentTheme.colors.textSecondary
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(FluentTheme.colors.surfaceElevated)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = practice.domain.uppercase(),
                                style = FluentTheme.typography.labelSmall,
                                color = FluentTheme.colors.accentSecondary
                            )
                        }
                    }
                }
            }
            }
        }

        // Bottom Actions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = FluentTheme.spacing.md),
            horizontalArrangement = Arrangement.spacedBy(FluentTheme.spacing.md, Alignment.End)
        ) {
            FluentButton(
                text = "Back to Scenarios",
                variant = ButtonVariant.Secondary,
                onClick = onDismiss
            )
            FluentButton(
                text = "Practice Again",
                variant = ButtonVariant.Primary,
                onClick = onPracticeAgain
            )
        }
    }
}

@Composable
private fun ScorePill(label: String, score: Float) {
    val percentage = (score * 100).toInt()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "$percentage%",
            style = FluentTheme.typography.headlineMedium,
            color = if (percentage >= 80) FluentTheme.colors.accentPrimary else FluentTheme.colors.textPrimary
        )
        Text(
            text = label,
            style = FluentTheme.typography.labelSmall,
            color = FluentTheme.colors.textSecondary
        )
    }
}

@Composable
private fun GrammarObservationCard(obs: SpeakingGrammarObservation) {
    FluentCard(
        modifier = Modifier.fillMaxWidth(),
        variant = CardVariant.Elevated
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.xs)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(FluentTheme.colors.surfaceElevated)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = obs.trapType ?: "Grammar Trap",
                        style = FluentTheme.typography.labelSmall,
                        color = FluentTheme.colors.error
                    )
                }
                Text(
                    text = "✓ Saved to Mistake Bank",
                    style = FluentTheme.typography.labelSmall,
                    color = FluentTheme.colors.accentSecondary
                )
            }

            Text(
                text = "Spoken: \"${obs.errorSnippet}\"",
                style = FluentTheme.typography.bodyMedium,
                color = FluentTheme.colors.error
            )
            Text(
                text = "Correction: \"${obs.correctedSnippet}\"",
                style = FluentTheme.typography.titleMedium,
                color = FluentTheme.colors.accentPrimary
            )
            Text(
                text = obs.explanation,
                style = FluentTheme.typography.bodySmall,
                color = FluentTheme.colors.textSecondary
            )
        }
    }
}

@Composable
private fun VocabObservationCard(obs: SpeakingVocabObservation) {
    FluentCard(
        modifier = Modifier.fillMaxWidth(),
        variant = CardVariant.Standard
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.xs)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${obs.usedWord} → ${obs.suggestedBetterWord}",
                    style = FluentTheme.typography.titleMedium,
                    color = FluentTheme.colors.accentPrimary
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(FluentTheme.colors.surfaceElevated)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = obs.register.uppercase(),
                        style = FluentTheme.typography.labelSmall,
                        color = FluentTheme.colors.accentSecondary
                    )
                }
            }
            Text(
                text = obs.explanation,
                style = FluentTheme.typography.bodySmall,
                color = FluentTheme.colors.textSecondary
            )
        }
    }
}

/**
 * Active speaking session screen with responsive tablet layout and audio visualizer.
 */
@Composable
private fun ActiveSpeakingSessionView(
    uiState: SpeakingUiState,
    onTogglePause: () -> Unit,
    onToggleMute: () -> Unit,
    onEndSession: () -> Unit,
    modifier: Modifier = Modifier
) {
    var transcriptSize by remember { mutableStateOf(initialTranscriptPanelSize()) }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        DashboardColors.BackgroundPrimary,
                        DashboardColors.SurfacePink.copy(alpha = 0.72f),
                        DashboardColors.BackgroundSecondary
                    )
                )
            )
            .padding(FluentTheme.spacing.screenHorizontal)
    ) {
        val scenarioTitle = uiState.selectedScenario?.title ?: uiState.activeLiveConfig?.title ?: "Live Coaching"
        val cefrLevel = uiState.selectedScenario?.cefrLevel ?: uiState.activeLiveConfig?.cefrTarget ?: "B2"
        val category = uiState.selectedScenario?.category?.displayName
            ?: uiState.activeLiveConfig?.takeIf { it.sessionType == SpeakingSessionType.FREE_TALK }?.let { "Free Talk" }
        val voiceName = uiState.activeLiveConfig?.voiceName
        val coachName = uiState.activeLiveConfig?.persona?.displayName ?: speakingPersonaNameForVoice(voiceName)
        val voiceWeight = when (transcriptSize) {
            TranscriptPanelSize.PEEK -> 0.72f
            TranscriptPanelSize.NORMAL -> 0.55f
            TranscriptPanelSize.EXPANDED -> 0.34f
        }
        val transcriptWeight = 1f - voiceWeight

        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 980.dp)
                .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.sm)
        ) {
            SessionInfoHeader(
                scenarioTitle = scenarioTitle,
                cefrLevel = cefrLevel,
                category = category,
                mode = uiState.selectedMode
            )

            LiveVoicePanel(
                voiceState = uiState.voiceState,
                amplitude = uiState.amplitude,
                voiceName = voiceName,
                isMuted = uiState.isMuted,
                compact = transcriptSize == TranscriptPanelSize.EXPANDED,
                onToggleMute = onToggleMute,
                onTogglePause = onTogglePause,
                onEndSession = onEndSession,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(voiceWeight)
            )

            LiveTranscriptPanel(
                transcript = uiState.transcript,
                voiceState = uiState.voiceState,
                coachName = coachName,
                panelSize = transcriptSize,
                onToggleSize = {
                    transcriptSize = nextTranscriptPanelSize(transcriptSize)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(transcriptWeight)
            )
        }
    }
}

internal enum class TranscriptPanelSize {
    PEEK,
    NORMAL,
    EXPANDED
}

internal fun initialTranscriptPanelSize(): TranscriptPanelSize = TranscriptPanelSize.PEEK

internal fun nextTranscriptPanelSize(currentSize: TranscriptPanelSize): TranscriptPanelSize =
    when (currentSize) {
        TranscriptPanelSize.PEEK -> TranscriptPanelSize.NORMAL
        TranscriptPanelSize.NORMAL -> TranscriptPanelSize.EXPANDED
        TranscriptPanelSize.EXPANDED -> TranscriptPanelSize.PEEK
    }

@Composable
private fun LiveVoicePanel(
    voiceState: VoiceUiState,
    amplitude: Float,
    voiceName: String?,
    isMuted: Boolean,
    compact: Boolean,
    onToggleMute: () -> Unit,
    onTogglePause: () -> Unit,
    onEndSession: () -> Unit,
    modifier: Modifier = Modifier
) {
    LightSpeakSurface(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            ConnectionStatus(voiceState = voiceState, voiceName = voiceName)
            VoiceOrb(voiceState = voiceState, amplitude = amplitude, compact = compact)
            ConversationStateLabel(voiceState)
            AudioWaveform(amplitude = amplitude, voiceState = voiceState)
            SessionControls(
                voiceState = voiceState,
                isMuted = isMuted,
                onToggleMute = onToggleMute,
                onTogglePause = onTogglePause,
                onEndSession = onEndSession
            )
        }
    }
}

@Composable
private fun ConnectionStatus(voiceState: VoiceUiState, voiceName: String?) {
    val connected = voiceState !in setOf(
        VoiceUiState.IDLE,
        VoiceUiState.CONNECTING,
        VoiceUiState.RECONNECTING,
        VoiceUiState.ENDED,
        VoiceUiState.ERROR
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(if (connected) DashboardColors.PlantGreen else DashboardColors.Warning)
        )
        Text(
            text = if (connected) "Connected" else conversationStateText(voiceState),
            style = FluentTheme.typography.labelMedium,
            color = DashboardColors.TextSecondary
        )
        if (!voiceName.isNullOrBlank()) {
            Text(text = "•", color = DashboardColors.BorderSoft)
            Text(
                text = voiceName,
                style = FluentTheme.typography.labelMedium,
                color = DashboardColors.TextSecondary
            )
        }
    }
}

@Composable
private fun ConversationStateLabel(voiceState: VoiceUiState) {
    Text(
        text = conversationStateText(voiceState),
        style = FluentTheme.typography.titleLarge,
        color = DashboardColors.TextPrimary
    )
}

private fun conversationStateText(voiceState: VoiceUiState): String = when (voiceState) {
    VoiceUiState.IDLE -> "Ready"
    VoiceUiState.CONNECTING -> "Connecting..."
    VoiceUiState.LISTENING -> "Listening..."
    VoiceUiState.USER_SPEAKING -> "Listening to you..."
    VoiceUiState.PROCESSING -> "Thinking..."
    VoiceUiState.COACH_SPEAKING -> "Speaking..."
    VoiceUiState.PAUSED -> "Paused"
    VoiceUiState.RECONNECTING -> "Reconnecting..."
    VoiceUiState.ENDING -> "Ending..."
    VoiceUiState.ENDED -> "Ended"
    VoiceUiState.ERROR -> "Connection error"
}

@Composable
private fun AudioWaveform(amplitude: Float, voiceState: VoiceUiState) {
    val active = voiceState == VoiceUiState.COACH_SPEAKING || voiceState == VoiceUiState.USER_SPEAKING
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(9) { index ->
            val centerFactor = 1f - (kotlin.math.abs(index - 4) / 5f)
            val height = if (active) 8.dp + (28.dp * (amplitude.coerceIn(0f, 1f) * centerFactor)) else 8.dp
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(height)
                    .clip(CircleShape)
                    .background(DashboardColors.PrimaryPink.copy(alpha = if (active) 0.9f else 0.35f))
            )
        }
    }
}

@Composable
private fun SessionInfoHeader(
    scenarioTitle: String,
    cefrLevel: String,
    category: String?,
    mode: CorrectionMode
) {
    val level = parseCefrLevel(cefrLevel)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = scenarioTitle,
                style = FluentTheme.typography.titleMedium,
                color = DashboardColors.TextPrimary
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LevelBadge(level = level)
                if (!category.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(DashboardColors.SurfacePink)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = category,
                            style = FluentTheme.typography.labelSmall,
                            color = DashboardColors.PrimaryPinkDark
                        )
                    }
                }
                Text(
                    text = mode.displayName,
                    style = FluentTheme.typography.labelMedium,
                    color = DashboardColors.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun VoiceOrb(
    voiceState: VoiceUiState,
    amplitude: Float,
    compact: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "aura")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val targetAuraColor = when (voiceState) {
        VoiceUiState.COACH_SPEAKING -> DashboardColors.PrimaryPink
        VoiceUiState.USER_SPEAKING -> DashboardColors.PrimaryPinkDark
        VoiceUiState.LISTENING -> DashboardColors.PrimaryPinkLight
        VoiceUiState.CONNECTING, VoiceUiState.PROCESSING -> DashboardColors.PrimaryPink.copy(alpha = 0.65f)
        else -> DashboardColors.BorderSoft
    }

    val animatedAuraColor by animateColorAsState(
        targetValue = targetAuraColor,
        animationSpec = tween(400),
        label = "color"
    )

    val effectiveScale = (pulseScale + (amplitude * 0.4f)).coerceIn(0.9f, 1.6f)
    val outerSize = if (compact) 112.dp else 196.dp
    val middleSize = if (compact) 76.dp else 132.dp
    val coreSize = if (compact) 52.dp else 78.dp

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(outerSize)
                .scale(effectiveScale)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            animatedAuraColor.copy(alpha = 0.35f),
                            animatedAuraColor.copy(alpha = 0.10f),
                            Color.Transparent
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .size(middleSize)
                .scale((effectiveScale * 0.9f).coerceIn(0.85f, 1.4f))
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            animatedAuraColor.copy(alpha = 0.6f),
                            animatedAuraColor.copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .size(coreSize)
                .clip(CircleShape)
                .background(DashboardColors.Surface)
                .border(2.dp, animatedAuraColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.GraphicEq,
                contentDescription = "Live voice waveform",
                tint = DashboardColors.PrimaryPinkDark,
                modifier = Modifier.size(if (compact) 28.dp else 38.dp)
            )
        }
    }
}

@Composable
private fun SessionControls(
    voiceState: VoiceUiState,
    isMuted: Boolean,
    onTogglePause: () -> Unit,
    onToggleMute: () -> Unit,
    onEndSession: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 560.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(if (isMuted) DashboardColors.PrimaryPink else DashboardColors.SurfacePink)
                .clickable(onClick = onToggleMute),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isMuted) Icons.Filled.MicOff else Icons.Filled.Mic,
                contentDescription = if (isMuted) "Unmute microphone" else "Mute microphone",
                tint = if (isMuted) Color.White else DashboardColors.PrimaryPinkDark,
                modifier = Modifier.size(24.dp)
            )
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 54.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(DashboardColors.SurfacePink)
                .clickable(onClick = onTogglePause)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (voiceState == VoiceUiState.PAUSED) Icons.Filled.PlayArrow else Icons.Filled.Pause,
                contentDescription = null,
                tint = DashboardColors.PrimaryPinkDark,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (voiceState == VoiceUiState.PAUSED) "Resume Session" else "Tap to Pause",
                style = FluentTheme.typography.labelLarge,
                color = DashboardColors.TextPrimary
            )
        }

        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(FluentTheme.colors.error)
                .clickable(onClick = onEndSession),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.CallEnd,
                contentDescription = "End call",
                tint = Color.White,
                modifier = Modifier.size(25.dp)
            )
        }
    }
}

@Composable
private fun LiveTranscriptPanel(
    transcript: List<TranscriptEntry>,
    voiceState: VoiceUiState,
    coachName: String,
    panelSize: TranscriptPanelSize,
    onToggleSize: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(transcript.size, transcript.lastOrNull()?.id) {
        if (transcript.isNotEmpty()) {
            listState.animateScrollToItem(transcript.size - 1)
        }
    }

    LightSpeakSurface(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp, bottomStart = 18.dp, bottomEnd = 18.dp),
        background = DashboardColors.BackgroundSecondary
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.xs),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(42.dp)
                    .height(5.dp)
                    .clip(CircleShape)
                    .background(DashboardColors.PrimaryPinkLight)
                    .clickable(onClick = onToggleSize)
            )
            TranscriptHeader(voiceState = voiceState, onToggleSize = onToggleSize)

            if (panelSize == TranscriptPanelSize.PEEK) {
                Text(
                    text = "Tap to open the conversation",
                    style = FluentTheme.typography.bodySmall,
                    color = DashboardColors.TextSecondary
                )
            } else if (transcript.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(FluentTheme.spacing.lg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Conversation transcript will stream here...",
                        style = FluentTheme.typography.bodyMedium,
                        color = DashboardColors.TextSecondary
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(FluentTheme.spacing.sm),
                    contentPadding = PaddingValues(vertical = FluentTheme.spacing.xs)
                ) {
                    items(transcript, key = { it.id }) { entry ->
                        TranscriptBubble(entry = entry, coachName = coachName)
                    }
                }
            }
        }
    }
}

@Composable
private fun TranscriptHeader(voiceState: VoiceUiState, onToggleSize: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggleSize),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Live Transcript",
            style = FluentTheme.typography.titleMedium,
            color = DashboardColors.TextPrimary
        )
        if (voiceState in setOf(VoiceUiState.LISTENING, VoiceUiState.USER_SPEAKING, VoiceUiState.PROCESSING, VoiceUiState.COACH_SPEAKING)) {
            Text(
                text = "REAL-TIME",
                style = FluentTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = DashboardColors.PrimaryPinkDark
            )
        }
    }
}

@Composable
private fun TranscriptBubble(entry: TranscriptEntry, coachName: String) {
    when (entry.role) {
        TranscriptRole.COACH -> AiMessageBubble(entry, coachName)
        TranscriptRole.USER -> UserMessageBubble(entry)
        TranscriptRole.SYSTEM -> Unit
    }
}

@Composable
private fun AiMessageBubble(entry: TranscriptEntry, coachName: String) {
    ConversationBubble(entry = entry, isCoach = true, coachName = coachName)
}

@Composable
private fun UserMessageBubble(entry: TranscriptEntry) {
    ConversationBubble(entry = entry, isCoach = false, coachName = "")
}

@Composable
private fun ConversationBubble(entry: TranscriptEntry, isCoach: Boolean, coachName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = if (isCoach) 0.dp else 24.dp),
        horizontalAlignment = if (isCoach) Alignment.Start else Alignment.End
    ) {
        Text(
            text = if (isCoach) coachName else "You",
            style = FluentTheme.typography.labelSmall,
            color = if (isCoach) DashboardColors.PrimaryPinkDark else DashboardColors.TextSecondary,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )

        Box(
            modifier = Modifier
                .clip(FluentTheme.shapes.card)
                .background(
                    if (isCoach) DashboardColors.Surface else DashboardColors.SurfacePink
                )
                .border(
                    width = 1.dp,
                    color = DashboardColors.BorderSoft,
                    shape = FluentTheme.shapes.card
                )
                .widthIn(max = 680.dp)
                .padding(FluentTheme.spacing.cardInternal)
        ) {
            Text(
                text = entry.text,
                style = FluentTheme.typography.bodyMedium,
                color = DashboardColors.TextPrimary
            )
        }
    }
}

internal fun speakingPersonaNameForVoice(voiceName: String?): String = when (voiceName?.lowercase()) {
    "puck" -> "David"
    "aoede" -> "Emma"
    "fenrir" -> "Daniel"
    "kore" -> "Sophie"
    "charon" -> "James"
    "zephyr" -> "Alex"
    else -> "Speaking Partner"
}

private fun parseCefrLevel(levelStr: String): CefrLevel {
    return try {
        CefrLevel.valueOf(levelStr.uppercase())
    } catch (_: Exception) {
        CefrLevel.B2
    }
}
