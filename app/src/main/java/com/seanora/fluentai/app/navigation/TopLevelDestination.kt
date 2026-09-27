package com.seanora.fluentai.app.navigation

enum class TopLevelDestination(
    val route: String,
    val label: String,
    val description: String,
) {
    HOME(
        route = "home",
        label = "Home",
        description = "Today's practice and daily overview",
    ),
    LEARN(
        route = "learn",
        label = "Learn",
        description = "Vocabulary, Grammar, Reading, and Listening curriculum",
    ),
    SPEAK(
        route = "speak",
        label = "Speak",
        description = "Real-time AI speaking coach and scenarios",
    ),
    PROGRESS(
        route = "progress",
        label = "Progress",
        description = "Skill-specific CEFR mastery and learning history",
    ),
    PROFILE(
        route = "profile",
        label = "Profile",
        description = "Personal goals and skill estimates",
    ),
    SETTINGS(
        route = "settings",
        label = "Settings",
        description = "Application preferences and practice goals",
    );

    companion object {
        val START_DESTINATION = HOME

        fun fromRouteOrNull(route: String?): TopLevelDestination? {
            return entries.firstOrNull { destination ->
                route == destination.route || route?.startsWith("${destination.route}/") == true
            }
        }

        fun fromRoute(route: String?): TopLevelDestination =
            fromRouteOrNull(route) ?: START_DESTINATION
    }
}

internal data class TopLevelNavigationPolicy(
    val saveState: Boolean,
    val restoreState: Boolean,
)

internal fun topLevelNavigationPolicy(destination: TopLevelDestination): TopLevelNavigationPolicy =
    if (destination == TopLevelDestination.HOME) {
        TopLevelNavigationPolicy(
            saveState = false,
            restoreState = false,
        )
    } else {
        TopLevelNavigationPolicy(
            saveState = true,
            restoreState = true,
        )
    }
