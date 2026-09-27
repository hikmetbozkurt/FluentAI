package com.seanora.fluentai.app.navigation

import com.seanora.fluentai.core.model.TodayItemType
import com.seanora.fluentai.core.model.TodayPlanItem
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.core.model.GrammarFeatureState
import com.seanora.fluentai.core.model.GrammarResumeDestination
import com.seanora.fluentai.core.model.VocabularyPracticeMode
import com.seanora.fluentai.feature.home.model.HomeResumeItem
import com.seanora.fluentai.feature.home.model.HomeResumeKind
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class NavigationTest {

    @Test
    fun ordinaryBack_popsExactlyOneActualPreviousDestination() {
        val paths = listOf(
            mutableListOf("learn", VOCABULARY_DASHBOARD_ROUTE, VOCABULARY_LIBRARY_ROUTE, vocabularyWordRoute("vocab.reliable")),
            mutableListOf(VOCABULARY_DASHBOARD_ROUTE, vocabularyWordRoute("vocab.daily")),
            mutableListOf("learn", GRAMMAR_DASHBOARD_ROUTE, GRAMMAR_LIBRARY_ROUTE, grammarSessionRoute("grammar.conditionals")),
            mutableListOf("home", "learn/grammar/grammar.conditionals?planId=plan&itemId=item"),
            mutableListOf("learn", READING_DASHBOARD_ROUTE, ReadingDestination.LIBRARY.route, readingSessionRoute("reading.b2.article")),
            mutableListOf("home", "learn/reading/reading.b2.article?planId=plan&itemId=item"),
            mutableListOf("learn", LISTENING_DASHBOARD_ROUTE, LISTENING_LIBRARY_ROUTE, listeningSessionRoute("listening.b2.meeting")),
            mutableListOf("home", "learn/listening/listening.b2.meeting?planId=plan&itemId=item"),
        )

        paths.forEach { stack ->
            val expectedPrevious = stack[stack.lastIndex - 1]
            val originalSize = stack.size

            performStandardBack { stack.removeAt(stack.lastIndex); true }

            assertEquals(expectedPrevious, stack.last())
            assertEquals(originalSize - 1, stack.size)
            assertEquals(stack.size, stack.distinct().size)
        }
    }

    @Test
    fun homeResumeRoutes_resolveCanonicalLearnDestinations() {
        assertEquals(
            vocabularyWordRoute("vocab.reliable"),
            homeResumeRoute(HomeResumeItem(HomeResumeKind.VOCABULARY_WORD, "Vocabulary", "Reliable", 1L, contentId = "vocab.reliable")),
        )
        assertEquals(
            vocabularyPracticeSessionRoute(VocabularySessionMode.FLASH_CARDS),
            homeResumeRoute(HomeResumeItem(HomeResumeKind.VOCABULARY_FLASH_CARDS, "Vocabulary", "Flash cards", 1L)),
        )
        assertEquals(
            grammarSessionRoute("grammar.conditionals", section = GrammarSessionSection.EXAMPLES),
            homeResumeRoute(HomeResumeItem(HomeResumeKind.GRAMMAR_EXAMPLES, "Grammar", "Conditionals", 2L, contentId = "grammar.conditionals")),
        )
        assertEquals(
            readingSessionRoute("reading.b2.article"),
            homeResumeRoute(HomeResumeItem(HomeResumeKind.READING_SESSION, "Reading", "Article", 3L, contentId = "reading.b2.article")),
        )
        assertEquals(
            listeningSessionRoute("listening.b1.meeting"),
            homeResumeRoute(HomeResumeItem(HomeResumeKind.LISTENING_SESSION, "Listening", "Meeting", 4L, contentId = "listening.b1.meeting")),
        )
    }

    @Test
    fun topLevelDestinations_allSixExistAndHaveUniqueRoutes() {
        val destinations = TopLevelDestination.entries
        assertEquals(6, destinations.size)

        val routes = destinations.map { it.route }
        assertEquals(6, routes.distinct().size)

        assertTrue(routes.contains("home"))
        assertTrue(routes.contains("learn"))
        assertTrue(routes.contains("speak"))
        assertTrue(routes.contains("progress"))
        assertTrue(routes.contains("profile"))
        assertTrue(routes.contains("settings"))
    }

    @Test
    fun topLevelDestinations_fromRoute_resolvesCorrectly() {
        assertEquals(TopLevelDestination.HOME, TopLevelDestination.fromRoute("home"))
        assertEquals(TopLevelDestination.LEARN, TopLevelDestination.fromRoute("learn"))
        assertEquals(TopLevelDestination.SPEAK, TopLevelDestination.fromRoute("speak"))
        assertEquals(TopLevelDestination.PROGRESS, TopLevelDestination.fromRoute("progress"))
        assertEquals(TopLevelDestination.PROFILE, TopLevelDestination.fromRoute("profile"))
        assertEquals(TopLevelDestination.SETTINGS, TopLevelDestination.fromRoute("settings"))
        assertEquals(TopLevelDestination.LEARN, TopLevelDestination.fromRoute("learn/reading/article.1"))

        // Fallbacks
        assertEquals(TopLevelDestination.HOME, TopLevelDestination.fromRoute(null))
        assertEquals(TopLevelDestination.HOME, TopLevelDestination.fromRoute("unknown_route"))
    }

    @Test
    fun sidebarSelection_doesNotMislabelReviewOrUnknownRoutesAsHome() {
        assertEquals(TopLevelDestination.HOME, TopLevelDestination.fromRouteOrNull("home"))
        assertEquals(TopLevelDestination.LEARN, TopLevelDestination.fromRouteOrNull("learn/grammar/item"))
        assertEquals(null, TopLevelDestination.fromRouteOrNull("review"))
        assertEquals(null, TopLevelDestination.fromRouteOrNull("unknown_route"))
        assertEquals(null, TopLevelDestination.fromRouteOrNull(null))
    }

    @Test
    fun sidebarHome_doesNotSaveOrRestoreAChildDestinationOverCanonicalHome() {
        val policy = topLevelNavigationPolicy(TopLevelDestination.HOME)

        assertFalse(policy.saveState)
        assertFalse(policy.restoreState)
    }

    @Test
    fun sidebarNonHomeDestinations_preserveExistingSavedStateBehavior() {
        listOf(
            TopLevelDestination.LEARN,
            TopLevelDestination.SPEAK,
            TopLevelDestination.PROGRESS,
            TopLevelDestination.PROFILE,
            TopLevelDestination.SETTINGS,
        ).forEach { destination ->
            val policy = topLevelNavigationPolicy(destination)

            assertTrue("$destination should save its previous state", policy.saveState)
            assertTrue("$destination should restore its previous state", policy.restoreState)
        }
    }

    @Test
    fun homeLaunchedTopLevelRoutes_remainDistinctFromCanonicalHome() {
        assertEquals(TopLevelDestination.SPEAK, TopLevelDestination.fromRouteOrNull("speak"))
        assertEquals(TopLevelDestination.PROGRESS, TopLevelDestination.fromRouteOrNull("progress"))
        assertEquals(TopLevelDestination.LEARN, TopLevelDestination.fromRouteOrNull("learn"))
        assertEquals(TopLevelDestination.HOME, TopLevelDestination.fromRouteOrNull("home"))
    }

    @Test
    fun galadrielFreeTalk_hasSpecificStaticSpeakRoute() {
        assertEquals("speak/galadriel-free-talk", GALADRIEL_FREE_TALK_ROUTE)
        assertEquals(TopLevelDestination.SPEAK, TopLevelDestination.fromRouteOrNull(GALADRIEL_FREE_TALK_ROUTE))
        assertFalse(GALADRIEL_FREE_TALK_ROUTE.contains("{contentId}"))
        assertTrue(GALADRIEL_FREE_TALK_ROUTE.startsWith("${TopLevelDestination.SPEAK.route}/"))
    }

    @Test
    fun topLevelDestinations_haveLabelsAndDescriptions() {
        TopLevelDestination.entries.forEach { destination ->
            assertTrue(destination.label.isNotBlank())
            assertTrue(destination.description.isNotBlank())
        }
    }

    @Test
    fun startupDestination_requiresOnboardingUntilProfileExists() {
        assertEquals("onboarding", resolveStartupDestination(null))
        assertEquals("home", resolveStartupDestination(UserProfile()))
    }

    @Test
    fun todayDestination_routesReviewAndCurriculumItemsToExactTargets() {
        val review = TodayPlanItem(
            id = "item.review",
            planId = "plan",
            contentId = "vocab.reliable",
            title = "Reliable",
            domain = "vocabulary",
            itemType = TodayItemType.REVIEW_DUE,
            estimatedMinutes = 2,
            targetCefrLevel = "B2",
            explanation = "Due"
        )
        val reading = review.copy(
            id = "item.reading",
            contentId = "reading.b2.tech.001",
            domain = "reading",
            itemType = TodayItemType.COMPREHENSION_READING
        )
        val grammar = review.copy(
            id = "item.grammar",
            contentId = "grammar.present-perfect",
            domain = "grammar",
            itemType = TodayItemType.CURRICULUM_NEW,
        )
        val listening = review.copy(
            id = "item.listening",
            contentId = "listening.b2.work.meeting-001",
            domain = "listening",
            itemType = TodayItemType.COMPREHENSION_LISTENING,
        )

        assertTrue(todayActivityRoute(review).startsWith("learn/vocabulary/vocab.reliable"))
        assertTrue(todayActivityRoute(reading).startsWith("learn/reading/reading.b2.tech.001"))
        assertTrue(todayActivityRoute(grammar).startsWith("learn/grammar/grammar.present-perfect"))
        assertTrue(todayActivityRoute(listening).startsWith("learn/listening/listening.b2.work.meeting-001"))
    }

    @Test
    fun vocabularyPrimaryRoutes_areHomeLibraryWordCollectionsAndPracticeSession() {
        assertEquals("learn/vocabulary-dashboard", VOCABULARY_DASHBOARD_ROUTE)
        assertEquals("learn/vocabulary-dashboard/library", VOCABULARY_LIBRARY_ROUTE)
        assertEquals("learn/vocabulary-dashboard/collections", VOCABULARY_COLLECTIONS_ROUTE)
        assertEquals("learn/vocabulary-dashboard/word/vocab.reliable", vocabularyWordRoute("vocab.reliable"))
        assertEquals("learn/vocabulary-dashboard/collection/topic.work", vocabularyCollectionDetailRoute("topic.work"))
    }

    @Test
    fun vocabularyDashboardRoutes_remainInsideLearnNavigationSelection() {
        assertEquals(TopLevelDestination.LEARN, TopLevelDestination.fromRouteOrNull(VOCABULARY_DASHBOARD_ROUTE))
        assertEquals(TopLevelDestination.LEARN, TopLevelDestination.fromRouteOrNull(VOCABULARY_LIBRARY_ROUTE))
        assertEquals(TopLevelDestination.LEARN, TopLevelDestination.fromRouteOrNull(vocabularyWordRoute("vocab.reliable")))
        assertEquals(TopLevelDestination.LEARN, TopLevelDestination.fromRouteOrNull(vocabularyPracticeSessionRoute()))
    }

    @Test
    fun vocabularyFeatureRoutes_keepStableIdsScopesAndPracticeModes() {
        assertEquals(
            "learn/vocabulary-dashboard/practice?mode=smart&scopeId=",
            vocabularyPracticeSessionRoute(),
        )
        assertEquals(
            "learn/vocabulary-dashboard/practice?mode=fill_in_the_blank&scopeId=topic.work",
            vocabularyPracticeSessionRoute(VocabularySessionMode.FILL_IN_THE_BLANK, "topic.work"),
        )
        assertEquals(VocabularyPracticeMode.WORD_MATCH, VocabularySessionMode.WORD_MATCH.practiceMode)
        assertEquals(null, VocabularySessionMode.FLASH_CARDS.practiceMode)
    }

    @Test
    fun vocabularyContinueMapsLegacyResumeStateIntoCanonicalSurfaces() {
        assertEquals(
            vocabularyWordRoute("vocab.reliable"),
            vocabularyResumeRoute(
                com.seanora.fluentai.core.model.VocabularyFeatureState(
                    resumeDestination = com.seanora.fluentai.core.model.VocabularyResumeDestination.WORD_OF_DAY,
                    resumeContentId = "vocab.reliable",
                )
            ),
        )
        assertEquals(
            vocabularyCollectionDetailRoute("topic.work"),
            vocabularyResumeRoute(
                com.seanora.fluentai.core.model.VocabularyFeatureState(
                    resumeDestination = com.seanora.fluentai.core.model.VocabularyResumeDestination.COLLECTIONS,
                    resumeContextId = "topic.work",
                )
            ),
        )
        assertEquals(
            vocabularyPracticeSessionRoute(VocabularySessionMode.FLASH_CARDS),
            vocabularyResumeRoute(
                com.seanora.fluentai.core.model.VocabularyFeatureState(
                    resumeDestination = com.seanora.fluentai.core.model.VocabularyResumeDestination.FLASH_CARDS,
                )
            ),
        )
        assertEquals(
            vocabularyPracticeSessionRoute(VocabularySessionMode.CONTEXT_CHOICE),
            vocabularyResumeRoute(
                com.seanora.fluentai.core.model.VocabularyFeatureState(
                    resumeDestination = com.seanora.fluentai.core.model.VocabularyResumeDestination.PRACTICE_LABS,
                    resumePracticeMode = VocabularyPracticeMode.CONTEXT_CHOICE,
                )
            ),
        )
        assertEquals(VOCABULARY_LIBRARY_ROUTE, vocabularyResumeRoute(com.seanora.fluentai.core.model.VocabularyFeatureState()))
    }

    @Test
    fun grammarPrimaryRoutes_areHomeLibraryAndOneCanonicalSession() {
        assertEquals("learn/grammar-dashboard", GRAMMAR_DASHBOARD_ROUTE)
        assertEquals("learn/grammar-dashboard/library", GRAMMAR_LIBRARY_ROUTE)
        assertEquals(
            "learn/grammar-dashboard/session/grammar.present-perfect?mode=normal&section=learn",
            grammarSessionRoute("grammar.present-perfect"),
        )
    }

    @Test
    fun grammarDashboardRoutes_remainInsideLearnNavigationSelection() {
        assertEquals(TopLevelDestination.LEARN, TopLevelDestination.fromRouteOrNull(GRAMMAR_DASHBOARD_ROUTE))
        assertEquals(TopLevelDestination.LEARN, TopLevelDestination.fromRouteOrNull(GRAMMAR_LIBRARY_ROUTE))
        assertEquals(
            TopLevelDestination.LEARN,
            TopLevelDestination.fromRouteOrNull(grammarSessionRoute("grammar.present-perfect")),
        )
    }

    @Test
    fun grammarSessionRoute_preservesStableSemanticIdModeAndLocalSection() {
        assertEquals(
            "learn/grammar-dashboard/session/grammar.present-perfect?mode=quick&section=practice",
            grammarSessionRoute(
                "grammar.present-perfect",
                GrammarSessionMode.QUICK,
                GrammarSessionSection.PRACTICE,
            ),
        )
        assertTrue(grammarSessionRoute("grammar.present-perfect").startsWith(GRAMMAR_DASHBOARD_ROUTE))
    }

    @Test
    fun grammarContinue_resolvesRetiredFeatureStateIntoCanonicalSession() {
        assertEquals(
            GrammarResolvedEntry.session(
                lessonId = "grammar.present-perfect",
                section = GrammarSessionSection.PRACTICE,
            ),
            resolveGrammarResumeEntry(
                GrammarFeatureState(
                    resumeDestination = GrammarResumeDestination.SENTENCE_BUILDER,
                    resumeContentId = "grammar.present-perfect",
                ),
            ),
        )
        assertEquals(
            GrammarResolvedEntry.session(
                lessonId = "grammar.relative-clauses",
                mode = GrammarSessionMode.QUICK,
            ),
            resolveGrammarResumeEntry(
                GrammarFeatureState(
                    resumeDestination = GrammarResumeDestination.GRAMMAR_BITES,
                    resumeContentId = "grammar.relative-clauses",
                ),
            ),
        )
        assertEquals(GrammarResolvedEntry.home(), resolveGrammarResumeEntry(null))
    }

    @Test
    fun grammarLegacyRoutes_mapToCanonicalHomeLibraryOrSessionSurfaces() {
        val persisted = GrammarFeatureState(
            resumeDestination = GrammarResumeDestination.GRAMMAR_IN_CONTEXT,
            resumeContentId = "grammar.conditionals",
        )

        assertEquals(
            GrammarResolvedEntry.session("grammar.conditionals", section = GrammarSessionSection.EXAMPLES),
            resolveGrammarLegacyEntry(GrammarDestination.GRAMMAR_IN_CONTEXT, persisted, "grammar.fallback", null),
        )
        assertEquals(
            GrammarResolvedEntry.session("grammar.fallback", mode = GrammarSessionMode.QUICK),
            resolveGrammarLegacyEntry(GrammarDestination.GRAMMAR_BITES, persisted, "grammar.fallback", null),
        )
        assertEquals(
            GrammarResolvedEntry.session("grammar.weak", section = GrammarSessionSection.PRACTICE),
            resolveGrammarLegacyEntry(GrammarDestination.WEAK_SPOTS, persisted, "grammar.fallback", "grammar.weak"),
        )
        assertEquals(
            GrammarResolvedEntry.library(compareLessonId = "grammar.conditionals"),
            resolveGrammarLegacyEntry(GrammarDestination.COMPARE_STRUCTURES, persisted, "grammar.fallback", null),
        )
        assertEquals(
            GrammarResolvedEntry.library(),
            resolveGrammarLegacyEntry(GrammarDestination.LIBRARY, persisted, "grammar.fallback", null),
        )
    }

    @Test
    fun readingDashboard_definesExactlySevenUniqueChildDestinations() {
        val destinations = ReadingDestination.entries

        assertEquals(7, destinations.size)
        assertEquals(7, destinations.map { it.route }.distinct().size)
        assertTrue(destinations.all { it.route.startsWith("$READING_DASHBOARD_ROUTE/") })
        assertTrue(destinations.all { it.description.isNotBlank() })
    }

    @Test
    fun readingDashboard_usesExactFeatureTitles() {
        assertEquals(
            listOf(
                "Continue",
                "Daily Reading",
                "Reading Library",
                "Guided Reading",
                "Reading Skills",
                "Saved Reading",
                "Weak Spots",
            ),
            ReadingDestination.entries.map { it.title },
        )
    }

    @Test
    fun readingDashboardRoutes_remainInsideLearnNavigationSelection() {
        assertEquals(TopLevelDestination.LEARN, TopLevelDestination.fromRouteOrNull(READING_DASHBOARD_ROUTE))
        ReadingDestination.entries.forEach { destination ->
            assertEquals(TopLevelDestination.LEARN, TopLevelDestination.fromRouteOrNull(destination.route))
        }
    }

    @Test
    fun readingLibraryRoute_preservesStableSemanticContentIdInsideSpecificChildRoute() {
        assertEquals(
            "${ReadingDestination.LIBRARY.route}/reading.b2.work.article-001",
            readingLibraryRoute("reading.b2.work.article-001"),
        )
        assertEquals(TopLevelDestination.LEARN, TopLevelDestination.fromRouteOrNull(readingLibraryRoute("reading.b2.work.article-001")))
    }

    @Test
    fun readingSessionRoute_keepsArticleAndModeInOneSpecificRoute() {
        assertEquals(
            "$READING_DASHBOARD_ROUTE/session/reading.b2.work.article-001?mode=guided",
            readingSessionRoute("reading.b2.work.article-001", ReadingSessionMode.GUIDED),
        )
        assertEquals(
            "$READING_DASHBOARD_ROUTE/session/reading.b2.work.article-001?mode=standard",
            readingSessionRoute("reading.b2.work.article-001"),
        )
        assertEquals(
            TopLevelDestination.LEARN,
            TopLevelDestination.fromRouteOrNull(readingSessionRoute("reading.b2.work.article-001")),
        )
    }

    @Test
    fun listeningPrimaryRoutes_areHomeLibraryAndOneCanonicalSession() {
        assertEquals("learn/listening-dashboard", LISTENING_DASHBOARD_ROUTE)
        assertEquals("learn/listening-dashboard/library", LISTENING_LIBRARY_ROUTE)
        assertEquals(
            "learn/listening-dashboard/session/listening.b2.work.meeting-001?mode=standard",
            listeningSessionRoute("listening.b2.work.meeting-001"),
        )
        assertEquals(
            "learn/listening-dashboard/session/listening.b2.work.meeting-001?mode=dictation",
            listeningSessionRoute("listening.b2.work.meeting-001", ListeningSessionMode.DICTATION),
        )
    }

    @Test
    fun listeningPrimaryAndLegacyRoutes_remainInsideLearnNavigationSelection() {
        assertEquals(TopLevelDestination.LEARN, TopLevelDestination.fromRouteOrNull(LISTENING_DASHBOARD_ROUTE))
        assertEquals(TopLevelDestination.LEARN, TopLevelDestination.fromRouteOrNull(LISTENING_LIBRARY_ROUTE))
        assertEquals(
            TopLevelDestination.LEARN,
            TopLevelDestination.fromRouteOrNull(listeningSessionRoute("listening.b2.work.meeting-001")),
        )
        ListeningDestination.entries.forEach { destination ->
            assertEquals(TopLevelDestination.LEARN, TopLevelDestination.fromRouteOrNull(destination.route))
        }
    }

    @Test
    fun listeningLegacyEntries_resolveIntoCanonicalSurfacesWithoutDuplicateFeatureWorlds() {
        assertEquals(
            ListeningResolvedEntry.session("listening.b2.work.continue"),
            resolveListeningLegacyEntry(
                destination = ListeningDestination.CONTINUE,
                continueId = "listening.b2.work.continue",
                dailyId = "listening.b1.daily",
                fallbackId = "listening.a2.fallback",
                weakId = "listening.c1.weak",
            ),
        )
        assertEquals(
            ListeningResolvedEntry.session("listening.b1.daily"),
            resolveListeningLegacyEntry(ListeningDestination.DAILY_LISTENING, null, "listening.b1.daily", "listening.a2.fallback", null),
        )
        assertEquals(
            ListeningResolvedEntry.session("listening.a2.fallback", ListeningSessionMode.DICTATION),
            resolveListeningLegacyEntry(ListeningDestination.DICTATION, null, null, "listening.a2.fallback", null),
        )
        assertEquals(
            ListeningResolvedEntry.session("listening.c1.weak", ListeningSessionMode.COMPREHENSION),
            resolveListeningLegacyEntry(ListeningDestination.WEAK_SPOTS, null, null, "listening.a2.fallback", "listening.c1.weak"),
        )
        assertEquals(
            ListeningResolvedEntry.library(),
            resolveListeningLegacyEntry(ListeningDestination.SAVED_LISTENING, null, null, "listening.a2.fallback", null),
        )
        assertEquals(
            ListeningResolvedEntry.library(),
            resolveListeningLegacyEntry(ListeningDestination.WEAK_SPOTS, null, null, "listening.a2.fallback", null),
        )
    }
}
