package com.seanora.fluentai.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.seanora.fluentai.feature.home.HomeScreen
import com.seanora.fluentai.feature.grammar.GrammarDashboardScreen
import com.seanora.fluentai.feature.grammar.GrammarLegacyEntryScreen
import com.seanora.fluentai.feature.grammar.GrammarLibraryScreen
import com.seanora.fluentai.feature.grammar.GrammarSessionScreen
import com.seanora.fluentai.feature.learn.LearnScreen
import com.seanora.fluentai.feature.listening.ListeningDashboardScreen
import com.seanora.fluentai.feature.listening.ListeningLegacyEntryScreen
import com.seanora.fluentai.feature.listening.ListeningLibraryScreen
import com.seanora.fluentai.feature.listening.ListeningSessionScreen
import com.seanora.fluentai.feature.progress.ProgressScreen
import com.seanora.fluentai.feature.profile.ProfileScreen
import com.seanora.fluentai.feature.reading.ReadingDashboardScreen
import com.seanora.fluentai.feature.reading.ReadingLibraryScreen
import com.seanora.fluentai.feature.reading.ReadingSessionScreen
import com.seanora.fluentai.feature.settings.SettingsScreen
import com.seanora.fluentai.feature.speaking.SpeakingScreen
import com.seanora.fluentai.feature.vocabulary.VocabularyCollectionsScreen
import com.seanora.fluentai.feature.vocabulary.VocabularyDailyWordDetailScreen
import com.seanora.fluentai.feature.vocabulary.VocabularyDashboardScreen
import com.seanora.fluentai.feature.vocabulary.VocabularyLibraryScreen
import com.seanora.fluentai.feature.vocabulary.VocabularyPracticeSessionScreen
import com.seanora.fluentai.feature.vocabulary.VocabularyWordDetailScreen
import com.seanora.fluentai.core.model.VocabularyPracticeMode

@Composable
fun FluentAiNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = TopLevelDestination.START_DESTINATION.route,
) {
    val completionViewModel: TodayCompletionViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(TopLevelDestination.HOME.route) {
            HomeScreen(
                onNavigateToActivity = { item ->
                    navController.navigate(todayActivityRoute(item))
                },
                onNavigateToResume = { item ->
                    homeResumeRoute(item)?.let(navController::navigate)
                },
                onNavigateToSpeak = {
                    navController.navigate(TopLevelDestination.SPEAK.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToLearn = {
                    navController.navigate(TopLevelDestination.LEARN.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToProgress = {
                    navController.navigate(TopLevelDestination.PROGRESS.route) {
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(TopLevelDestination.LEARN.route) {
            LearnScreen(
                onNavigateToVocabularyDashboard = {
                    navController.navigate(VOCABULARY_DASHBOARD_ROUTE) {
                        launchSingleTop = true
                    }
                },
                onNavigateToGrammarDashboard = {
                    navController.navigate(GRAMMAR_DASHBOARD_ROUTE) {
                        launchSingleTop = true
                    }
                },
                onNavigateToReadingDashboard = {
                    navController.navigate(READING_DASHBOARD_ROUTE) {
                        launchSingleTop = true
                    }
                },
                onNavigateToListeningDashboard = {
                    navController.navigate(LISTENING_DASHBOARD_ROUTE) {
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(VOCABULARY_DASHBOARD_ROUTE) {
            VocabularyDashboardScreen(
                onNavigateBack = { navController.popBackStack() },
                onContinue = { state ->
                    navController.navigate(vocabularyResumeRoute(state))
                },
                onOpenWord = { navController.navigate(vocabularyWordRoute(it)) },
                onSmartPractice = { navController.navigate(vocabularyPracticeSessionRoute()) },
                onOpenLibrary = { navController.navigate(VOCABULARY_LIBRARY_ROUTE) },
                onOpenCollections = { navController.navigate(VOCABULARY_COLLECTIONS_ROUTE) },
            )
        }
        composable(VOCABULARY_LIBRARY_ROUTE) {
            VocabularyLibraryScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenWord = { navController.navigate(vocabularyWordRoute(it)) },
            )
        }
        composable(VOCABULARY_WORD_ROUTE) { entry ->
            VocabularyWordDetailScreen(
                wordId = entry.arguments?.getString(VOCABULARY_CONTENT_ID_ARG).orEmpty(),
                onNavigateBack = { navController.popBackStack() },
                onPracticeWord = { navController.navigate(vocabularyPracticeSessionRoute(scopeId = it)) },
            )
        }
        composable(VOCABULARY_COLLECTIONS_ROUTE) {
            VocabularyCollectionsScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenWord = { navController.navigate(vocabularyWordRoute(it)) },
                onPracticeCollection = { navController.navigate(vocabularyPracticeSessionRoute(scopeId = it)) },
            )
        }
        composable(VOCABULARY_COLLECTION_DETAIL_ROUTE) { entry ->
            VocabularyCollectionsScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenWord = { navController.navigate(vocabularyWordRoute(it)) },
                onPracticeCollection = { navController.navigate(vocabularyPracticeSessionRoute(scopeId = it)) },
                initialTopicId = entry.arguments?.getString(VOCABULARY_TOPIC_ID_ARG),
            )
        }
        composable(VOCABULARY_PRACTICE_SESSION_ROUTE) { entry ->
            VocabularyPracticeSessionScreen(
                mode = VocabularySessionMode.fromRoute(entry.arguments?.getString(VOCABULARY_PRACTICE_MODE_ARG)),
                scopeId = entry.arguments?.getString(VOCABULARY_PRACTICE_SCOPE_ARG),
                onNavigateBack = { navController.popBackStack() },
                onOpenWord = { navController.navigate(vocabularyWordRoute(it)) },
            )
        }
        composable(VOCABULARY_LIBRARY_CONTENT_ROUTE) { entry ->
            VocabularyWordDetailScreen(
                wordId = entry.arguments?.getString(VOCABULARY_CONTENT_ID_ARG).orEmpty(),
                onNavigateBack = { navController.popBackStack() },
                onPracticeWord = { navController.navigate(vocabularyPracticeSessionRoute(scopeId = it)) },
            )
        }
        composable(VocabularyDestination.LIBRARY.route) {
            VocabularyLibraryScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenWord = { navController.navigate(vocabularyWordRoute(it)) },
            )
        }
        composable(VocabularyDestination.COLLECTIONS.route) {
            VocabularyCollectionsScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenWord = { navController.navigate(vocabularyWordRoute(it)) },
                onPracticeCollection = { navController.navigate(vocabularyPracticeSessionRoute(scopeId = it)) },
            )
        }
        composable(VOCABULARY_COLLECTION_TOPIC_ROUTE) { entry ->
            VocabularyCollectionsScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenWord = { navController.navigate(vocabularyWordRoute(it)) },
                onPracticeCollection = { navController.navigate(vocabularyPracticeSessionRoute(scopeId = it)) },
                initialTopicId = entry.arguments?.getString(VOCABULARY_TOPIC_ID_ARG),
            )
        }
        composable(VocabularyDestination.PRACTICE_LABS.route) {
            VocabularyPracticeSessionScreen(VocabularySessionMode.SMART, null, { navController.popBackStack() }, { navController.navigate(vocabularyWordRoute(it)) })
        }
        composable(VOCABULARY_PRACTICE_MODE_ROUTE) { entry ->
            val mode = entry.arguments?.getString(VOCABULARY_PRACTICE_MODE_ARG)?.uppercase()?.let {
                runCatching { VocabularyPracticeMode.valueOf(it) }.getOrNull()
            }
            VocabularyPracticeSessionScreen(mode?.let(VocabularySessionMode::fromPracticeMode) ?: VocabularySessionMode.SMART, null, { navController.popBackStack() }, { navController.navigate(vocabularyWordRoute(it)) })
        }
        composable(VocabularyDestination.FLASH_CARDS.route) {
            VocabularyPracticeSessionScreen(VocabularySessionMode.FLASH_CARDS, null, { navController.popBackStack() }, { navController.navigate(vocabularyWordRoute(it)) })
        }
        composable(VocabularyDestination.WORD_OF_DAY.route) {
            VocabularyDailyWordDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onPracticeWord = { navController.navigate(vocabularyPracticeSessionRoute(scopeId = it)) },
            )
        }
        listOf(VocabularyDestination.CONTINUE).forEach { legacy ->
            composable(legacy.route) {
                VocabularyDashboardScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onContinue = { navController.navigate(VOCABULARY_DASHBOARD_ROUTE) },
                    onOpenWord = { navController.navigate(vocabularyWordRoute(it)) },
                    onSmartPractice = { navController.navigate(vocabularyPracticeSessionRoute()) },
                    onOpenLibrary = { navController.navigate(VOCABULARY_LIBRARY_ROUTE) },
                    onOpenCollections = { navController.navigate(VOCABULARY_COLLECTIONS_ROUTE) },
                )
            }
        }
        composable(GRAMMAR_DASHBOARD_ROUTE) {
            GrammarDashboardScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenLibrary = { navController.navigate(GRAMMAR_LIBRARY_ROUTE) },
                onOpenLesson = { id, mode, section -> navController.navigate(grammarSessionRoute(id, mode, section)) },
            )
        }
        composable(GRAMMAR_LIBRARY_ROUTE) {
            GrammarLibraryScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenLesson = { navController.navigate(grammarSessionRoute(it)) },
            )
        }
        composable(GRAMMAR_LIBRARY_CONTENT_ROUTE) { entry ->
            GrammarSessionScreen(
                lessonId = entry.arguments?.getString(GRAMMAR_CONTENT_ID_ARG).orEmpty(),
                mode = GrammarSessionMode.NORMAL,
                initialSection = GrammarSessionSection.LEARN,
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable(GRAMMAR_SESSION_ROUTE) { entry ->
            GrammarSessionScreen(
                lessonId = entry.arguments?.getString(GRAMMAR_CONTENT_ID_ARG).orEmpty(),
                mode = GrammarSessionMode.fromRoute(entry.arguments?.getString(GRAMMAR_SESSION_MODE_ARG)),
                initialSection = GrammarSessionSection.fromRoute(entry.arguments?.getString(GRAMMAR_SESSION_SECTION_ARG)),
                onNavigateBack = { navController.popBackStack() },
                onFinish = { navController.popBackStack() },
            )
        }
        GrammarDestination.entries.forEach { legacy ->
            if (legacy.route != GrammarDestination.LIBRARY.route) composable(legacy.route) {
                GrammarLegacyEntryScreen(
                    destination = legacy,
                    onNavigateBack = { navController.popBackStack() },
                    onOpenLibrary = { navController.navigate(GRAMMAR_LIBRARY_ROUTE) },
                    onOpenLesson = { id, mode, section -> navController.navigate(grammarSessionRoute(id, mode, section)) },
                )
            }
        }
        composable(GrammarDestination.LIBRARY.route) {
            GrammarLibraryScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenLesson = { navController.navigate(grammarSessionRoute(it)) },
            )
        }
        composable(READING_DASHBOARD_ROUTE) {
            ReadingDashboardScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenArticle = { id, mode -> navController.navigate(readingSessionRoute(id, mode)) },
                onOpenLibrary = { navController.navigate(ReadingDestination.LIBRARY.route) },
            )
        }
        composable(ReadingDestination.CONTINUE.route) {
            ReadingDashboardScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenArticle = { id, mode -> navController.navigate(readingSessionRoute(id, mode)) },
                onOpenLibrary = { navController.navigate(ReadingDestination.LIBRARY.route) },
            )
        }
        composable(ReadingDestination.DAILY_READING.route) {
            ReadingDashboardScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenArticle = { id, mode -> navController.navigate(readingSessionRoute(id, mode)) },
                onOpenLibrary = { navController.navigate(ReadingDestination.LIBRARY.route) },
            )
        }
        composable(ReadingDestination.LIBRARY.route) {
            ReadingLibraryScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenArticle = { navController.navigate(readingSessionRoute(it)) },
            )
        }
        composable(READING_LIBRARY_CONTENT_ROUTE) { entry ->
            ReadingSessionScreen(
                articleId = entry.arguments?.getString(READING_CONTENT_ID_ARG).orEmpty(),
                mode = ReadingSessionMode.STANDARD,
                onNavigateBack = { navController.popBackStack() },
                onFinish = { navController.popBackStack(READING_DASHBOARD_ROUTE, false) },
            )
        }
        composable(READING_SESSION_ROUTE) { entry ->
            ReadingSessionScreen(
                articleId = entry.arguments?.getString(READING_CONTENT_ID_ARG).orEmpty(),
                mode = ReadingSessionMode.fromRoute(entry.arguments?.getString(READING_SESSION_MODE_ARG)),
                onNavigateBack = { navController.popBackStack() },
                onFinish = { navController.popBackStack(READING_DASHBOARD_ROUTE, false) },
            )
        }
        composable(ReadingDestination.GUIDED_READING.route) {
            ReadingLibraryScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenArticle = { navController.navigate(readingSessionRoute(it, ReadingSessionMode.GUIDED)) },
            )
        }
        composable(ReadingDestination.READING_SKILLS.route) {
            ReadingDashboardScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenArticle = { id, mode -> navController.navigate(readingSessionRoute(id, mode)) },
                onOpenLibrary = { navController.navigate(ReadingDestination.LIBRARY.route) },
            )
        }
        composable(ReadingDestination.SAVED_READING.route) {
            ReadingDashboardScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenArticle = { id, mode -> navController.navigate(readingSessionRoute(id, mode)) },
                onOpenLibrary = { navController.navigate(ReadingDestination.LIBRARY.route) },
            )
        }
        composable(ReadingDestination.WEAK_SPOTS.route) {
            ReadingDashboardScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenArticle = { id, mode -> navController.navigate(readingSessionRoute(id, mode)) },
                onOpenLibrary = { navController.navigate(ReadingDestination.LIBRARY.route) },
            )
        }
        composable(LISTENING_DASHBOARD_ROUTE) {
            ListeningDashboardScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenLibrary = { navController.navigate(LISTENING_LIBRARY_ROUTE) },
                onOpenSession = { id, mode -> navController.navigate(listeningSessionRoute(id, mode)) },
            )
        }
        composable(LISTENING_LIBRARY_ROUTE) {
            ListeningLibraryScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenSession = { navController.navigate(listeningSessionRoute(it)) },
            )
        }
        composable(LISTENING_SESSION_ROUTE) { entry ->
            ListeningSessionScreen(
                listeningId = entry.arguments?.getString(LISTENING_CONTENT_ID_ARG).orEmpty(),
                initialMode = ListeningSessionMode.fromRoute(entry.arguments?.getString(LISTENING_SESSION_MODE_ARG)),
                onNavigateBack = { navController.popBackStack() },
                onFinish = { navController.popBackStack() },
            )
        }
        composable(LISTENING_LIBRARY_CONTENT_ROUTE) { entry ->
            ListeningSessionScreen(
                listeningId = entry.arguments?.getString(LISTENING_CONTENT_ID_ARG).orEmpty(),
                initialMode = ListeningSessionMode.STANDARD,
                onNavigateBack = { navController.popBackStack() },
                onFinish = { navController.popBackStack() },
            )
        }
        ListeningDestination.entries.forEach { destination ->
            composable(destination.route) {
                ListeningLegacyEntryScreen(
                    destination = destination,
                    onNavigateBack = { navController.popBackStack() },
                    onOpenSession = { navController.navigate(listeningSessionRoute(it)) },
                )
            }
        }
        composable("learn/{domain}/{contentId}?planId={planId}&itemId={itemId}") { entry ->
            val planId = entry.arguments?.getString("planId")
            val itemId = entry.arguments?.getString("itemId")
            if (entry.arguments?.getString("domain").equals("vocabulary", true)) {
                VocabularyWordDetailScreen(
                    wordId = entry.arguments?.getString("contentId").orEmpty(),
                    onNavigateBack = { performStandardBack(navController::popBackStack) },
                    onPracticeWord = { navController.navigate(vocabularyPracticeSessionRoute(scopeId = it)) },
                    onComplete = {
                        completionViewModel.complete(planId, itemId)
                        navController.popBackStack(TopLevelDestination.HOME.route, false)
                    },
                )
            } else if (entry.arguments?.getString("domain").equals("grammar", true)) {
                GrammarSessionScreen(
                    lessonId = entry.arguments?.getString("contentId").orEmpty(),
                    mode = GrammarSessionMode.NORMAL,
                    initialSection = GrammarSessionSection.LEARN,
                    onNavigateBack = { performStandardBack(navController::popBackStack) },
                    onFinish = {
                        completionViewModel.complete(planId, itemId)
                        navController.popBackStack(TopLevelDestination.HOME.route, false)
                    },
                )
            } else if (entry.arguments?.getString("domain").equals("reading", true)) {
                ReadingSessionScreen(
                    articleId = entry.arguments?.getString("contentId").orEmpty(),
                    mode = ReadingSessionMode.STANDARD,
                    onNavigateBack = { performStandardBack(navController::popBackStack) },
                    onFinish = {
                        completionViewModel.complete(planId, itemId)
                        navController.popBackStack(TopLevelDestination.HOME.route, false)
                    },
                )
            } else if (entry.arguments?.getString("domain").equals("listening", true)) {
                ListeningSessionScreen(
                    listeningId = entry.arguments?.getString("contentId").orEmpty(),
                    initialMode = ListeningSessionMode.STANDARD,
                    onNavigateBack = { performStandardBack(navController::popBackStack) },
                    onFinish = {
                        completionViewModel.complete(planId, itemId)
                        navController.popBackStack(TopLevelDestination.HOME.route, false)
                    },
                )
            } else {
                LearnScreen(
                    initialModule = entry.arguments?.getString("domain"),
                    initialContentId = entry.arguments?.getString("contentId"),
                    onNavigateBack = { performStandardBack(navController::popBackStack) },
                    onAssignedActivityCompleted = {
                        completionViewModel.complete(planId, itemId)
                        navController.popBackStack(TopLevelDestination.HOME.route, false)
                    },
                )
            }
        }
        composable("learn/{domain}/{contentId}") { entry ->
            if (entry.arguments?.getString("domain").equals("vocabulary", true)) {
                VocabularyWordDetailScreen(
                    wordId = entry.arguments?.getString("contentId").orEmpty(),
                    onNavigateBack = { navController.popBackStack() },
                    onPracticeWord = { navController.navigate(vocabularyPracticeSessionRoute(scopeId = it)) },
                )
            } else if (entry.arguments?.getString("domain").equals("grammar", true)) {
                GrammarSessionScreen(
                    lessonId = entry.arguments?.getString("contentId").orEmpty(),
                    mode = GrammarSessionMode.NORMAL,
                    initialSection = GrammarSessionSection.LEARN,
                    onNavigateBack = { navController.popBackStack() },
                    onFinish = { navController.popBackStack() },
                )
            } else if (entry.arguments?.getString("domain").equals("reading", true)) {
                ReadingSessionScreen(
                    articleId = entry.arguments?.getString("contentId").orEmpty(),
                    mode = ReadingSessionMode.STANDARD,
                    onNavigateBack = { navController.popBackStack() },
                    onFinish = { navController.popBackStack() },
                )
            } else if (entry.arguments?.getString("domain").equals("listening", true)) {
                ListeningSessionScreen(
                    listeningId = entry.arguments?.getString("contentId").orEmpty(),
                    initialMode = ListeningSessionMode.STANDARD,
                    onNavigateBack = { navController.popBackStack() },
                    onFinish = { navController.popBackStack() },
                )
            } else {
                LearnScreen(
                    initialModule = entry.arguments?.getString("domain"),
                    initialContentId = entry.arguments?.getString("contentId"),
                    onNavigateBack = { navController.popBackStack() },
                )
            }
        }
        composable(TopLevelDestination.SPEAK.route) {
            SpeakingScreen(
                onOpenGaladriel = { navController.navigate(GALADRIEL_FREE_TALK_ROUTE) },
            )
        }
        composable(GALADRIEL_FREE_TALK_ROUTE) {
            SpeakingScreen(
                showGaladrielStart = true,
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable("speak/{contentId}?planId={planId}&itemId={itemId}") { entry ->
            SpeakingScreen(
                initialContentId = entry.arguments?.getString("contentId"),
                onNavigateBack = { performStandardBack(navController::popBackStack) },
                onAssignedActivityCompleted = {
                    completionViewModel.complete(
                        entry.arguments?.getString("planId"),
                        entry.arguments?.getString("itemId"),
                    )
                },
            )
        }
        composable("speak/{contentId}") { entry ->
            SpeakingScreen(
                initialContentId = entry.arguments?.getString("contentId"),
                onNavigateBack = { performStandardBack(navController::popBackStack) },
            )
        }
        composable(TopLevelDestination.PROGRESS.route) {
            ProgressScreen()
        }
        composable(TopLevelDestination.PROFILE.route) {
            ProfileScreen()
        }
        composable(TopLevelDestination.SETTINGS.route) {
            SettingsScreen()
        }
        composable(ONBOARDING_ROUTE) {
            com.seanora.fluentai.feature.onboarding.OnboardingScreen(
                onOnboardingComplete = {
                    navController.navigate(TopLevelDestination.HOME.route) {
                        popUpTo(ONBOARDING_ROUTE) { inclusive = true }
                    }
                }
            )
        }
    }
}
