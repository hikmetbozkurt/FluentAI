package com.seanora.fluentai.feature.listening

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seanora.fluentai.app.navigation.ListeningDestination
import com.seanora.fluentai.app.navigation.ListeningSurface
import com.seanora.fluentai.app.navigation.resolveListeningLegacyEntry
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors

/**
 * Compatibility host for old Listening routes. It deliberately renders one of the three
 * canonical surfaces rather than retaining the former standalone feature screens.
 */
@Composable
fun ListeningLegacyEntryScreen(
    destination: ListeningDestination,
    onNavigateBack: () -> Unit,
    onOpenSession: (String) -> Unit,
    modifier: Modifier = Modifier,
    featureViewModel: ListeningFeatureViewModel = hiltViewModel(),
) {
    val state by featureViewModel.uiState.collectAsStateWithLifecycle()
    if (state.isLoading) {
        Box(
            modifier.fillMaxSize().background(DashboardColors.BackgroundPrimary),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = DashboardColors.PrimaryPink)
        }
        return
    }

    val resolved = resolveListeningLegacyEntry(
        destination = destination,
        continueId = state.continueScenario?.id,
        dailyId = state.dailyScenario?.id,
        fallbackId = state.dailyScenario?.id ?: state.scenarios.firstOrNull()?.id,
        weakId = state.weakSpots.firstOrNull()?.scenario?.id,
    )
    when (resolved.surface) {
        ListeningSurface.HOME -> ListeningDashboardScreen(
            onNavigateBack = onNavigateBack,
            onOpenLibrary = onNavigateBack,
            onOpenSession = { id, _ -> onOpenSession(id) },
            modifier = modifier,
            viewModel = featureViewModel,
        )
        ListeningSurface.LIBRARY -> ListeningLibraryScreen(
            onNavigateBack = onNavigateBack,
            onOpenSession = onOpenSession,
            modifier = modifier,
        )
        ListeningSurface.SESSION -> ListeningSessionScreen(
            listeningId = resolved.listeningId.orEmpty(),
            initialMode = resolved.mode,
            onNavigateBack = onNavigateBack,
            onFinish = onNavigateBack,
            modifier = modifier,
        )
    }
}
