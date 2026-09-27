package com.seanora.fluentai.app

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seanora.fluentai.app.navigation.FluentAiNavHost
import com.seanora.fluentai.app.navigation.FluentAiNavRail
import com.seanora.fluentai.app.navigation.FluentAiAppHeader
import com.seanora.fluentai.app.navigation.TopLevelDestination
import com.seanora.fluentai.app.navigation.ONBOARDING_ROUTE
import com.seanora.fluentai.app.navigation.topLevelNavigationPolicy
import androidx.compose.ui.unit.dp
import com.seanora.fluentai.core.designsystem.theme.FluentTheme
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors

@Composable
fun FluentAiApp(
    startupViewModel: AppStartupViewModel = hiltViewModel()
) {
    FluentTheme {
        val startupState by startupViewModel.state.collectAsStateWithLifecycle()
        val readyState = startupState as? AppStartupState.Ready
        if (readyState == null) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = FluentTheme.colors.surfaceBase,
            ) {}
            return@FluentTheme
        }

        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val selectedDestination = TopLevelDestination.fromRouteOrNull(currentRoute)
        val showNavigation = currentRoute != ONBOARDING_ROUTE && (currentRoute != null || readyState.startDestination != ONBOARDING_ROUTE)

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            color = if (showNavigation) DashboardColors.BackgroundPrimary else FluentTheme.colors.surfaceBase,
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val compactNavigation = maxWidth < 900.dp
                Row(modifier = Modifier.fillMaxSize()) {
                    if (showNavigation) {
                        FluentAiNavRail(
                            selectedDestination = selectedDestination,
                            compact = compactNavigation,
                            onDestinationSelected = { destination ->
                                val navigationPolicy = topLevelNavigationPolicy(destination)
                                navController.navigate(destination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = navigationPolicy.saveState
                                    }
                                    launchSingleTop = true
                                    restoreState = navigationPolicy.restoreState
                                }
                            }
                        )
                    }
                    Column(Modifier.weight(1f).fillMaxSize()) {
                        if (showNavigation) {
                            FluentAiAppHeader(
                                compact = compactNavigation,
                                onNavigateToProfile = {
                                    navController.navigate(TopLevelDestination.PROFILE.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                onNavigateToSettings = {
                                    navController.navigate(TopLevelDestination.SETTINGS.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                onNavigateToContent = { route ->
                                    navController.navigate(route) {
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                        FluentAiNavHost(
                            navController = navController,
                            startDestination = readyState.startDestination,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    }
}
