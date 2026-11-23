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
import com.seanora.fluentai.app.navigation.READING_DASHBOARD_ROUTE
import com.seanora.fluentai.app.navigation.READING_LIBRARY_CONTENT_ROUTE
import com.seanora.fluentai.app.navigation.READING_LIVE_ARTICLE_ROUTE
import com.seanora.fluentai.app.navigation.READING_SESSION_ROUTE
import com.seanora.fluentai.app.navigation.topLevelNavigationPolicy
import androidx.compose.ui.unit.dp
import com.seanora.fluentai.core.designsystem.dashboard.DashboardColors
import com.seanora.fluentai.core.designsystem.dashboard.LingoPinkTheme
import com.seanora.fluentai.feature.reading.ReadingPalette

internal fun isDistractionFreeReadingRoute(route: String?, domainArg: String? = null): Boolean {
    if (route == null) return false
    if (route == READING_SESSION_ROUTE || route == READING_LIBRARY_CONTENT_ROUTE || route == READING_LIVE_ARTICLE_ROUTE) return true
    if (route.startsWith("$READING_DASHBOARD_ROUTE/session/") || route.startsWith("$READING_DASHBOARD_ROUTE/live-article/")) return true
    if (route.startsWith("learn/{domain}/") && domainArg.equals("reading", ignoreCase = true)) return true
    return false
}

internal fun isReadingRoute(route: String?, domainArg: String? = null): Boolean {
    if (route == null) return false
    return isDistractionFreeReadingRoute(route, domainArg) ||
        route.startsWith(READING_DASHBOARD_ROUTE)
}

@Composable
fun FluentAiApp(
    startupViewModel: AppStartupViewModel = hiltViewModel()
) {
    LingoPinkTheme {
        val startupState by startupViewModel.state.collectAsStateWithLifecycle()
        val readyState = startupState as? AppStartupState.Ready
        if (readyState == null) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = DashboardColors.BackgroundPrimary,
            ) {}
            return@LingoPinkTheme
        }

        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val domainArg = navBackStackEntry?.arguments?.getString("domain")
        val selectedDestination = TopLevelDestination.fromRouteOrNull(currentRoute)
        val isOnboarding = currentRoute == ONBOARDING_ROUTE || (currentRoute == null && readyState.startDestination == ONBOARDING_ROUTE)
        val isDistractionFreeReading = isDistractionFreeReadingRoute(currentRoute, domainArg)
        val showNavigation = !isOnboarding && !isDistractionFreeReading
        val surfaceColor = if (isReadingRoute(currentRoute, domainArg)) {
            ReadingPalette.Paper
        } else {
            DashboardColors.BackgroundPrimary
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            color = surfaceColor,
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
