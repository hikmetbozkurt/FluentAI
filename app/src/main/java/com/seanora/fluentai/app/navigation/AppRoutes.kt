package com.seanora.fluentai.app.navigation

import com.seanora.fluentai.core.model.TodayItemType
import com.seanora.fluentai.core.model.TodayPlanItem
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.core.model.GrammarFeatureState
import com.seanora.fluentai.core.model.GrammarResumeDestination
import com.seanora.fluentai.core.model.VocabularyFeatureState
import com.seanora.fluentai.core.model.VocabularyPracticeMode
import com.seanora.fluentai.core.model.VocabularyResumeDestination
import com.seanora.fluentai.feature.home.model.HomeResumeItem
import com.seanora.fluentai.feature.home.model.HomeResumeKind

internal fun performStandardBack(popBackStack: () -> Boolean): Boolean = popBackStack()

const val ONBOARDING_ROUTE = "onboarding"
const val VOCABULARY_DASHBOARD_ROUTE = "learn/vocabulary-dashboard"
const val VOCABULARY_LIBRARY_ROUTE = "$VOCABULARY_DASHBOARD_ROUTE/library"
const val VOCABULARY_COLLECTIONS_ROUTE = "$VOCABULARY_DASHBOARD_ROUTE/collections"
const val GRAMMAR_DASHBOARD_ROUTE = "learn/grammar-dashboard"
const val GRAMMAR_LIBRARY_ROUTE = "$GRAMMAR_DASHBOARD_ROUTE/library"
const val READING_DASHBOARD_ROUTE = "learn/reading-dashboard"
const val LISTENING_DASHBOARD_ROUTE = "learn/listening-dashboard"
const val GALADRIEL_FREE_TALK_ROUTE = "speak/galadriel-free-talk"

enum class VocabularyDestination(
    val routeSegment: String,
    val title: String,
    val description: String,
) {
    CONTINUE(
        routeSegment = "continue",
        title = "Continue",
        description = "Resume your latest vocabulary activity.",
    ),
    WORD_OF_DAY(
        routeSegment = "word-of-day",
        title = "Word of Day",
        description = "Learn one useful word in context.",
    ),
    LIBRARY(
        routeSegment = "library",
        title = "Vocabulary Library",
        description = "Explore vocabulary by level and topic.",
    ),
    FLASH_CARDS(
        routeSegment = "flash-cards",
        title = "Flash Cards",
        description = "Review and strengthen learned words.",
    ),
    COLLECTIONS(
        routeSegment = "collections",
        title = "Collections",
        description = "Explore vocabulary grouped by useful themes.",
    ),
    PRACTICE_LABS(
        routeSegment = "practice-labs",
        title = "Practice Labs",
        description = "Practice vocabulary with focused exercises.",
    );

    val route: String
        get() = "$VOCABULARY_DASHBOARD_ROUTE/feature/$routeSegment"
}

const val VOCABULARY_CONTENT_ID_ARG = "vocabularyContentId"
const val VOCABULARY_TOPIC_ID_ARG = "vocabularyTopicId"
const val VOCABULARY_PRACTICE_MODE_ARG = "vocabularyPracticeMode"
const val VOCABULARY_PRACTICE_SCOPE_ARG = "vocabularyPracticeScope"
val VOCABULARY_LIBRARY_CONTENT_ROUTE = "${VocabularyDestination.LIBRARY.route}/{$VOCABULARY_CONTENT_ID_ARG}"
val VOCABULARY_COLLECTION_TOPIC_ROUTE = "${VocabularyDestination.COLLECTIONS.route}/{$VOCABULARY_TOPIC_ID_ARG}"
val VOCABULARY_PRACTICE_MODE_ROUTE = "${VocabularyDestination.PRACTICE_LABS.route}/{$VOCABULARY_PRACTICE_MODE_ARG}"
val VOCABULARY_WORD_ROUTE = "$VOCABULARY_DASHBOARD_ROUTE/word/{$VOCABULARY_CONTENT_ID_ARG}"
val VOCABULARY_COLLECTION_DETAIL_ROUTE = "$VOCABULARY_DASHBOARD_ROUTE/collection/{$VOCABULARY_TOPIC_ID_ARG}"
val VOCABULARY_PRACTICE_SESSION_ROUTE = "$VOCABULARY_DASHBOARD_ROUTE/practice?mode={$VOCABULARY_PRACTICE_MODE_ARG}&scopeId={$VOCABULARY_PRACTICE_SCOPE_ARG}"

enum class VocabularySessionMode(val routeValue: String, val practiceMode: VocabularyPracticeMode?) {
    SMART("smart", null),
    FLASH_CARDS("flash_cards", null),
    WORD_MATCH("word_match", VocabularyPracticeMode.WORD_MATCH),
    MEANING_MATCH("meaning_match", VocabularyPracticeMode.MEANING_MATCH),
    FILL_IN_THE_BLANK("fill_in_the_blank", VocabularyPracticeMode.FILL_IN_THE_BLANK),
    CONTEXT_CHOICE("context_choice", VocabularyPracticeMode.CONTEXT_CHOICE),
    COLLOCATION_MATCH("collocation_match", VocabularyPracticeMode.COLLOCATION_MATCH),
    SPEED_DRILL("speed_drill", VocabularyPracticeMode.SPEED_DRILL);

    companion object {
        fun fromRoute(value: String?): VocabularySessionMode = entries.firstOrNull { it.routeValue == value } ?: SMART
        fun fromPracticeMode(mode: VocabularyPracticeMode): VocabularySessionMode = entries.first { it.practiceMode == mode }
    }
}

fun vocabularyLibraryRoute(contentId: String) = "${VocabularyDestination.LIBRARY.route}/$contentId"
fun vocabularyCollectionRoute(topicId: String) = "${VocabularyDestination.COLLECTIONS.route}/$topicId"
fun vocabularyPracticeRoute(mode: VocabularyPracticeMode) = "${VocabularyDestination.PRACTICE_LABS.route}/${mode.name.lowercase()}"
fun vocabularyWordRoute(contentId: String) = "$VOCABULARY_DASHBOARD_ROUTE/word/$contentId"
fun vocabularyCollectionDetailRoute(topicId: String) = "$VOCABULARY_DASHBOARD_ROUTE/collection/$topicId"
fun vocabularyPracticeSessionRoute(
    mode: VocabularySessionMode = VocabularySessionMode.SMART,
    scopeId: String? = null,
) = "$VOCABULARY_DASHBOARD_ROUTE/practice?mode=${mode.routeValue}&scopeId=${scopeId.orEmpty()}"

fun vocabularyResumeRoute(state: VocabularyFeatureState): String = when (state.resumeDestination) {
    VocabularyResumeDestination.WORD_OF_DAY,
    VocabularyResumeDestination.LIBRARY -> state.resumeContentId?.let(::vocabularyWordRoute) ?: VOCABULARY_LIBRARY_ROUTE
    VocabularyResumeDestination.FLASH_CARDS -> vocabularyPracticeSessionRoute(VocabularySessionMode.FLASH_CARDS)
    VocabularyResumeDestination.COLLECTIONS -> state.resumeContextId?.let(::vocabularyCollectionDetailRoute) ?: VOCABULARY_COLLECTIONS_ROUTE
    VocabularyResumeDestination.PRACTICE_LABS -> vocabularyPracticeSessionRoute(
        state.resumePracticeMode?.let(VocabularySessionMode::fromPracticeMode) ?: VocabularySessionMode.SMART
    )
    null -> VOCABULARY_LIBRARY_ROUTE
}

enum class GrammarDestination(
    val routeSegment: String,
    val title: String,
    val description: String,
) {
    CONTINUE(
        routeSegment = "continue",
        title = "Continue",
        description = "Resume your latest grammar activity.",
    ),
    LIBRARY(
        routeSegment = "library",
        title = "Grammar Library",
        description = "Explore grammar concepts from A2 to C2.",
    ),
    GRAMMAR_BITES(
        routeSegment = "grammar-bites",
        title = "Grammar Bites",
        description = "Learn a useful grammar rule in minutes.",
    ),
    SENTENCE_BUILDER(
        routeSegment = "sentence-builder",
        title = "Sentence Builder",
        description = "Build sentences with correct grammar and word order.",
    ),
    ERROR_SPOTTER(
        routeSegment = "error-spotter",
        title = "Error Spotter",
        description = "Find and correct grammar mistakes.",
    ),
    GRAMMAR_IN_CONTEXT(
        routeSegment = "grammar-in-context",
        title = "Grammar in Context",
        description = "Learn grammar through realistic language and dialogues.",
    ),
    COMPARE_STRUCTURES(
        routeSegment = "compare-structures",
        title = "Compare Structures",
        description = "Understand differences between similar grammar patterns.",
    ),
    WEAK_SPOTS(
        routeSegment = "weak-spots",
        title = "Weak Spots",
        description = "Review grammar areas that need more practice.",
    );

    val route: String
        get() = "$GRAMMAR_DASHBOARD_ROUTE/feature/$routeSegment"
}

const val GRAMMAR_CONTENT_ID_ARG = "grammarContentId"
const val GRAMMAR_SESSION_MODE_ARG = "grammarSessionMode"
const val GRAMMAR_SESSION_SECTION_ARG = "grammarSessionSection"
val GRAMMAR_LIBRARY_CONTENT_ROUTE = "${GrammarDestination.LIBRARY.route}/{$GRAMMAR_CONTENT_ID_ARG}"
val GRAMMAR_SESSION_ROUTE = "$GRAMMAR_DASHBOARD_ROUTE/session/{$GRAMMAR_CONTENT_ID_ARG}?mode={$GRAMMAR_SESSION_MODE_ARG}&section={$GRAMMAR_SESSION_SECTION_ARG}"

enum class GrammarSessionMode(val routeValue: String) {
    NORMAL("normal"), QUICK("quick");

    companion object {
        fun fromRoute(value: String?): GrammarSessionMode = entries.firstOrNull { it.routeValue == value } ?: NORMAL
    }
}

enum class GrammarSessionSection(val routeValue: String) {
    LEARN("learn"), EXAMPLES("examples"), PRACTICE("practice"), REVIEW("review");

    companion object {
        fun fromRoute(value: String?): GrammarSessionSection = entries.firstOrNull { it.routeValue == value } ?: LEARN
    }
}

fun grammarLibraryRoute(contentId: String) = "${GrammarDestination.LIBRARY.route}/$contentId"
fun grammarSessionRoute(
    contentId: String,
    mode: GrammarSessionMode = GrammarSessionMode.NORMAL,
    section: GrammarSessionSection = GrammarSessionSection.LEARN,
) = "$GRAMMAR_DASHBOARD_ROUTE/session/$contentId?mode=${mode.routeValue}&section=${section.routeValue}"

enum class GrammarSurface { HOME, LIBRARY, SESSION }

data class GrammarResolvedEntry(
    val surface: GrammarSurface,
    val lessonId: String? = null,
    val mode: GrammarSessionMode = GrammarSessionMode.NORMAL,
    val section: GrammarSessionSection = GrammarSessionSection.LEARN,
    val compareLessonId: String? = null,
    val showCompare: Boolean = false,
) {
    companion object {
        fun home() = GrammarResolvedEntry(GrammarSurface.HOME)
        fun library(
            compareLessonId: String? = null,
            showCompare: Boolean = compareLessonId != null,
        ) = GrammarResolvedEntry(
            surface = GrammarSurface.LIBRARY,
            compareLessonId = compareLessonId,
            showCompare = showCompare,
        )
        fun session(
            lessonId: String,
            mode: GrammarSessionMode = GrammarSessionMode.NORMAL,
            section: GrammarSessionSection = GrammarSessionSection.LEARN,
        ) = GrammarResolvedEntry(
            surface = GrammarSurface.SESSION,
            lessonId = lessonId,
            mode = mode,
            section = section,
        )
    }
}

fun resolveGrammarResumeEntry(state: GrammarFeatureState?): GrammarResolvedEntry {
    val lessonId = state?.resumeContentId ?: return GrammarResolvedEntry.home()
    return when (state.resumeDestination) {
        GrammarResumeDestination.GRAMMAR_BITES -> GrammarResolvedEntry.session(
            lessonId,
            mode = GrammarSessionMode.QUICK,
        )
        GrammarResumeDestination.SENTENCE_BUILDER,
        GrammarResumeDestination.ERROR_SPOTTER,
        GrammarResumeDestination.WEAK_SPOTS -> GrammarResolvedEntry.session(
            lessonId,
            section = GrammarSessionSection.PRACTICE,
        )
        GrammarResumeDestination.GRAMMAR_IN_CONTEXT -> GrammarResolvedEntry.session(
            lessonId,
            section = GrammarSessionSection.EXAMPLES,
        )
        GrammarResumeDestination.LIBRARY,
        GrammarResumeDestination.COMPARE_STRUCTURES -> GrammarResolvedEntry.session(lessonId)
        null -> GrammarResolvedEntry.home()
    }
}

fun resolveGrammarLegacyEntry(
    destination: GrammarDestination,
    state: GrammarFeatureState?,
    fallbackLessonId: String?,
    weakLessonId: String?,
): GrammarResolvedEntry {
    fun matchingLesson(expected: GrammarResumeDestination): String? =
        state?.resumeContentId?.takeIf { state.resumeDestination == expected } ?: fallbackLessonId

    return when (destination) {
        GrammarDestination.CONTINUE -> resolveGrammarResumeEntry(state)
        GrammarDestination.LIBRARY -> GrammarResolvedEntry.library()
        GrammarDestination.GRAMMAR_BITES -> matchingLesson(GrammarResumeDestination.GRAMMAR_BITES)
            ?.let { GrammarResolvedEntry.session(it, mode = GrammarSessionMode.QUICK) }
            ?: GrammarResolvedEntry.home()
        GrammarDestination.SENTENCE_BUILDER -> matchingLesson(GrammarResumeDestination.SENTENCE_BUILDER)
            ?.let { GrammarResolvedEntry.session(it, section = GrammarSessionSection.PRACTICE) }
            ?: GrammarResolvedEntry.home()
        GrammarDestination.ERROR_SPOTTER -> matchingLesson(GrammarResumeDestination.ERROR_SPOTTER)
            ?.let { GrammarResolvedEntry.session(it, section = GrammarSessionSection.PRACTICE) }
            ?: GrammarResolvedEntry.home()
        GrammarDestination.GRAMMAR_IN_CONTEXT -> matchingLesson(GrammarResumeDestination.GRAMMAR_IN_CONTEXT)
            ?.let { GrammarResolvedEntry.session(it, section = GrammarSessionSection.EXAMPLES) }
            ?: GrammarResolvedEntry.home()
        GrammarDestination.COMPARE_STRUCTURES -> GrammarResolvedEntry.library(
            compareLessonId = state?.resumeContentId,
            showCompare = true,
        )
        GrammarDestination.WEAK_SPOTS -> (weakLessonId
            ?: state?.resumeContentId?.takeIf { state.resumeDestination == GrammarResumeDestination.WEAK_SPOTS })
            ?.let { GrammarResolvedEntry.session(it, section = GrammarSessionSection.PRACTICE) }
            ?: GrammarResolvedEntry.home()
    }
}

enum class ReadingDestination(
    val routeSegment: String,
    val title: String,
    val description: String,
) {
    CONTINUE(
        routeSegment = "continue",
        title = "Continue",
        description = "Resume your latest reading activity.",
    ),
    DAILY_READING(
        routeSegment = "daily-reading",
        title = "Daily Reading",
        description = "Practice with a short reading selected for today.",
    ),
    LIBRARY(
        routeSegment = "library",
        title = "Reading Library",
        description = "Explore reading content by level and topic.",
    ),
    GUIDED_READING(
        routeSegment = "guided-reading",
        title = "Guided Reading",
        description = "Read with vocabulary and comprehension support.",
    ),
    READING_SKILLS(
        routeSegment = "reading-skills",
        title = "Reading Skills",
        description = "Practice specific reading comprehension skills.",
    ),
    SAVED_READING(
        routeSegment = "saved-reading",
        title = "Saved Reading",
        description = "Return to reading content you saved.",
    ),
    WEAK_SPOTS(
        routeSegment = "weak-spots",
        title = "Weak Spots",
        description = "Review reading skills that need more practice.",
    );

    val route: String
        get() = "$READING_DASHBOARD_ROUTE/feature/$routeSegment"
}

const val READING_CONTENT_ID_ARG = "readingContentId"
const val READING_SESSION_MODE_ARG = "readingSessionMode"
val READING_LIBRARY_CONTENT_ROUTE = "${ReadingDestination.LIBRARY.route}/{$READING_CONTENT_ID_ARG}"
val READING_SESSION_ROUTE = "$READING_DASHBOARD_ROUTE/session/{$READING_CONTENT_ID_ARG}?mode={$READING_SESSION_MODE_ARG}"

enum class ReadingSessionMode(val routeValue: String) {
    STANDARD("standard"),
    GUIDED("guided");

    companion object {
        fun fromRoute(value: String?): ReadingSessionMode = entries.firstOrNull { it.routeValue == value } ?: STANDARD
    }
}

fun readingLibraryRoute(contentId: String) = "${ReadingDestination.LIBRARY.route}/$contentId"
fun readingSessionRoute(contentId: String, mode: ReadingSessionMode = ReadingSessionMode.STANDARD) =
    "$READING_DASHBOARD_ROUTE/session/$contentId?mode=${mode.routeValue}"

enum class ListeningDestination(
    val routeSegment: String,
    val title: String,
    val description: String,
) {
    CONTINUE(
        routeSegment = "continue",
        title = "Continue",
        description = "Resume your latest listening activity.",
    ),
    DAILY_LISTENING(
        routeSegment = "daily-listening",
        title = "Daily Listening",
        description = "Practice with a short listening selected for today.",
    ),
    LIBRARY(
        routeSegment = "library",
        title = "Listening Library",
        description = "Explore listening content by level and topic.",
    ),
    LISTENING_LAB(
        routeSegment = "listening-lab",
        title = "Listening Lab",
        description = "Practice with replay, transcript, and focused listening tools.",
    ),
    DICTATION(
        routeSegment = "dictation",
        title = "Dictation",
        description = "Listen carefully and write what you hear.",
    ),
    LISTENING_SKILLS(
        routeSegment = "listening-skills",
        title = "Listening Skills",
        description = "Practice specific listening comprehension skills.",
    ),
    SAVED_LISTENING(
        routeSegment = "saved-listening",
        title = "Saved Listening",
        description = "Return to listening content you saved.",
    ),
    WEAK_SPOTS(
        routeSegment = "weak-spots",
        title = "Weak Spots",
        description = "Review listening skills that need more practice.",
    );

    val route: String
        get() = "$LISTENING_DASHBOARD_ROUTE/feature/$routeSegment"
}

const val LISTENING_CONTENT_ID_ARG = "listeningContentId"
const val LISTENING_SESSION_MODE_ARG = "listeningSessionMode"
const val LISTENING_LIBRARY_ROUTE = "$LISTENING_DASHBOARD_ROUTE/library"
val LISTENING_SESSION_ROUTE = "$LISTENING_DASHBOARD_ROUTE/session/{$LISTENING_CONTENT_ID_ARG}?mode={$LISTENING_SESSION_MODE_ARG}"
@Deprecated("Use LISTENING_SESSION_ROUTE")
val LISTENING_LIBRARY_CONTENT_ROUTE = "${ListeningDestination.LIBRARY.route}/{$LISTENING_CONTENT_ID_ARG}"

enum class ListeningSessionMode(val routeValue: String) {
    STANDARD("standard"),
    COMPREHENSION("comprehension"),
    DICTATION("dictation");

    companion object {
        fun fromRoute(value: String?): ListeningSessionMode = entries.firstOrNull { it.routeValue == value } ?: STANDARD
    }
}

fun listeningSessionRoute(
    contentId: String,
    mode: ListeningSessionMode = ListeningSessionMode.STANDARD,
) = "$LISTENING_DASHBOARD_ROUTE/session/$contentId?mode=${mode.routeValue}"

fun homeResumeRoute(item: HomeResumeItem): String? = when (item.kind) {
    HomeResumeKind.VOCABULARY_WORD -> item.contentId?.let(::vocabularyWordRoute)
    HomeResumeKind.VOCABULARY_PRACTICE -> vocabularyPracticeSessionRoute(
        item.practiceMode?.let(VocabularySessionMode::fromPracticeMode) ?: VocabularySessionMode.SMART,
        item.contentId,
    )
    HomeResumeKind.VOCABULARY_FLASH_CARDS -> vocabularyPracticeSessionRoute(VocabularySessionMode.FLASH_CARDS)
    HomeResumeKind.VOCABULARY_COLLECTION -> item.contextId?.let(::vocabularyCollectionDetailRoute)
        ?: VOCABULARY_COLLECTIONS_ROUTE
    HomeResumeKind.GRAMMAR_LEARN -> item.contentId?.let(::grammarSessionRoute)
    HomeResumeKind.GRAMMAR_QUICK -> item.contentId?.let { grammarSessionRoute(it, mode = GrammarSessionMode.QUICK) }
    HomeResumeKind.GRAMMAR_EXAMPLES -> item.contentId?.let {
        grammarSessionRoute(it, section = GrammarSessionSection.EXAMPLES)
    }
    HomeResumeKind.GRAMMAR_PRACTICE -> item.contentId?.let {
        grammarSessionRoute(it, section = GrammarSessionSection.PRACTICE)
    }
    HomeResumeKind.READING_SESSION -> item.contentId?.let(::readingSessionRoute)
    HomeResumeKind.LISTENING_SESSION -> item.contentId?.let(::listeningSessionRoute)
    HomeResumeKind.LISTENING_DICTATION -> item.contentId?.let { listeningSessionRoute(it, ListeningSessionMode.DICTATION) }
    HomeResumeKind.SPEAKING_SESSION -> item.contentId?.let { "speak/$it" }
}

@Deprecated("Listening content opens in the canonical session")
fun listeningLibraryRoute(contentId: String) = listeningSessionRoute(contentId)

enum class ListeningSurface { HOME, LIBRARY, SESSION }

data class ListeningResolvedEntry(
    val surface: ListeningSurface,
    val listeningId: String? = null,
    val mode: ListeningSessionMode = ListeningSessionMode.STANDARD,
) {
    companion object {
        fun home() = ListeningResolvedEntry(ListeningSurface.HOME)
        fun library() = ListeningResolvedEntry(ListeningSurface.LIBRARY)
        fun session(
            listeningId: String,
            mode: ListeningSessionMode = ListeningSessionMode.STANDARD,
        ) = ListeningResolvedEntry(ListeningSurface.SESSION, listeningId, mode)
    }
}

fun resolveListeningLegacyEntry(
    destination: ListeningDestination,
    continueId: String?,
    dailyId: String?,
    fallbackId: String?,
    weakId: String?,
): ListeningResolvedEntry = when (destination) {
    ListeningDestination.CONTINUE -> continueId?.let(ListeningResolvedEntry::session) ?: ListeningResolvedEntry.library()
    ListeningDestination.DAILY_LISTENING -> dailyId?.let(ListeningResolvedEntry::session) ?: ListeningResolvedEntry.library()
    ListeningDestination.LIBRARY,
    ListeningDestination.SAVED_LISTENING -> ListeningResolvedEntry.library()
    ListeningDestination.LISTENING_LAB -> fallbackId?.let(ListeningResolvedEntry::session) ?: ListeningResolvedEntry.library()
    ListeningDestination.DICTATION -> fallbackId
        ?.let { ListeningResolvedEntry.session(it, ListeningSessionMode.DICTATION) }
        ?: ListeningResolvedEntry.library()
    ListeningDestination.LISTENING_SKILLS -> fallbackId
        ?.let { ListeningResolvedEntry.session(it, ListeningSessionMode.COMPREHENSION) }
        ?: ListeningResolvedEntry.library()
    ListeningDestination.WEAK_SPOTS -> weakId
        ?.let { ListeningResolvedEntry.session(it, ListeningSessionMode.COMPREHENSION) }
        ?: ListeningResolvedEntry.library()
}

fun resolveStartupDestination(profile: UserProfile?): String =
    if (profile == null) ONBOARDING_ROUTE else TopLevelDestination.HOME.route

fun todayActivityRoute(item: TodayPlanItem): String {
    val completion = "planId=${item.planId}&itemId=${item.id}"
    return when {
        item.domain.equals("speaking", ignoreCase = true) ||
        item.domain.equals("pronunciation", ignoreCase = true) ||
        item.itemType == TodayItemType.SPEAKING_SESSION ->
            "speak/${item.contentId}?$completion"
        else ->
            "learn/${item.domain}/${item.contentId}?$completion"
    }
}
