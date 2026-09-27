package com.seanora.fluentai.feature.grammar

import com.seanora.fluentai.core.model.Exercise
import com.seanora.fluentai.core.model.ExplanationSection
import com.seanora.fluentai.core.model.GrammarContrast
import com.seanora.fluentai.core.model.GrammarExample
import com.seanora.fluentai.core.model.GrammarLesson
import com.seanora.fluentai.core.model.GrammarRule
import com.seanora.fluentai.core.model.GrammarTrap
import com.seanora.fluentai.data.repository.GrammarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GrammarViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val mockLessons = listOf(
        GrammarLesson(
            id = "grammar.a2.modal-verbs-necessity",
            title = "Modals of Obligation and Necessity",
            cefrLevel = "A2",
            category = "modals_and_semi_modals",
            summaryEn = "Express internal duty with must and external with have to.",
            summaryTr = "İçsel ve dışsal zorunluluk kipleri.",
            explanationEn = listOf(ExplanationSection("Must vs Have to", "Must expresses internal feeling.")),
            explanationTr = "Türkçede zorunluluk yokluğu ile yasak farkı.",
            rules = listOf(GrammarRule("Must", "Subject + must + V1", listOf("Rules"))),
            contrasts = listOf(GrammarContrast("Mustn't", "Don't have to", "Forbidden vs optional", "Yasak vs serbest")),
            turkishTraps = listOf(GrammarTrap("tense_transfer", "Mustn't Hatası", "Mustn't yasak bildirir.", "You mustn't come", "You don't have to come")),
            examples = listOf(GrammarExample("You have to wear a badge.", "Rozet takmanız zorunludur."))
        ),
        GrammarLesson(
            id = "grammar.present-perfect-vs-past-simple",
            title = "Present Perfect vs. Past Simple",
            cefrLevel = "B1",
            category = "tenses_and_aspect",
            summaryEn = "Use Past Simple for finished time and Present Perfect for open time.",
            summaryTr = "Bitmiş ve açık zaman farkı.",
            explanationEn = listOf(ExplanationSection("Definite vs Indefinite", "Definite past takes Past Simple.")),
            explanationTr = "Geçmiş zaman ayrımı.",
            rules = listOf(GrammarRule("Past Simple", "Subject + V2", listOf("Narrative"))),
            contrasts = listOf(GrammarContrast("I lived", "I have lived", "Finished vs ongoing", "Bitti vs sürüyor")),
            turkishTraps = listOf(GrammarTrap("aspect_confusion", "Dün Hatası", "Yesterday ile present perfect olmaz.", "I have arrived yesterday", "I arrived yesterday")),
            examples = listOf(GrammarExample("We deployed the patch last night.", "Yamayı dün gece yayınladık."))
        ),
        GrammarLesson(
            id = "grammar.c1.inversion-negative-adverbials",
            title = "Inversion with Negative Adverbials",
            cefrLevel = "C1",
            category = "inversion_and_emphasis",
            summaryEn = "Invert subject and auxiliary after fronted negative adverbials.",
            summaryTr = "Devrik cümle ve retorik vurgu.",
            explanationEn = listOf(ExplanationSection("Inversion Syntax", "Auxiliary precedes subject.")),
            explanationTr = "Devrik soru sözdizimi.",
            rules = listOf(GrammarRule("Negative Inversion", "Negative + Aux + S + V", listOf("Legal emphasis"))),
            contrasts = listOf(GrammarContrast("Rarely has he", "He has rarely", "Inverted vs linear", "Vurgulu vs standart")),
            turkishTraps = listOf(GrammarTrap("word_order_svo_vs_sov", "Devrik Sıra Hatası", "Devrik yapıda soru sırası uygulanır.", "Under no circumstances we will", "Under no circumstances will we")),
            examples = listOf(GrammarExample("Under no circumstances will we compromise.", "Hiçbir koşulda taviz vermeyeceğiz."))
        )
    )

    private val mockExercises = listOf(
        Exercise(
            id = "exercise.grammar.present-perfect-01",
            targetContentId = "grammar.present-perfect-vs-past-simple",
            cefrLevel = "B1",
            skillDomain = "grammar",
            exerciseType = "multiple_choice",
            promptEn = "Choose the correct form:",
            stem = "We ___ the report yesterday.",
            options = listOf("completed", "has completed", "is completing"),
            correctAnswer = "completed",
            explanationEn = "Yesterday requires Past Simple.",
            explanationTr = "Dün belirteci Past Simple gerektirir.",
            difficulty = "standard"
        )
    )

    private val fakeRepository = object : GrammarRepository {
        override fun getAllLessons(): Flow<List<GrammarLesson>> = flowOf(mockLessons)

        override fun getLessonsByLevel(level: String): Flow<List<GrammarLesson>> = flowOf(
            mockLessons.filter { it.cefrLevel == level }
        )

        override fun getLessonsByCategory(category: String): Flow<List<GrammarLesson>> = flowOf(
            mockLessons.filter { it.category == category }
        )

        override fun getLessonById(id: String): Flow<GrammarLesson?> = flowOf(
            mockLessons.firstOrNull { it.id == id }
        )

        override fun searchLessons(query: String): Flow<List<GrammarLesson>> = flowOf(
            mockLessons.filter {
                it.title.contains(query, ignoreCase = true) ||
                    it.summaryEn.contains(query, ignoreCase = true) ||
                    it.summaryTr.contains(query, ignoreCase = true)
            }
        )

        override fun getExercisesForLesson(lessonId: String): Flow<List<Exercise>> = flowOf(
            mockExercises.filter { it.targetContentId == lessonId }
        )

        override suspend fun getLessonCount(): Int = mockLessons.size
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun uiState_initialLoad_populatesLibraryWithoutImplicitLessonSelection() = runTest {
        val viewModel = GrammarViewModel(fakeRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(3, state.lessons.size)
        assertEquals(null, state.selectedLesson)
        assertFalse("Turkish insights must remain collapsed initially (ADR-009)", state.isTurkishRevealed)
    }

    @Test
    fun explicitUnknownLessonId_doesNotFallBackToFirstLesson() = runTest {
        val viewModel = GrammarViewModel(fakeRepository)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect() }
        viewModel.selectLessonById("grammar.missing")
        testScheduler.advanceUntilIdle()

        assertEquals(null, viewModel.uiState.value.selectedLesson)
        assertTrue(viewModel.uiState.value.exercises.isEmpty())
    }

    @Test
    fun explicitUnknownLessonId_clearsPreviouslySelectedLessonInsteadOfShowingStaleDetail() = runTest {
        val viewModel = GrammarViewModel(fakeRepository)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect() }
        viewModel.selectLessonById(mockLessons[1].id)
        testScheduler.advanceUntilIdle()
        assertEquals(mockLessons[1].id, viewModel.uiState.value.selectedLesson?.id)

        viewModel.selectLessonById("grammar.missing")
        testScheduler.advanceUntilIdle()

        assertEquals(null, viewModel.uiState.value.selectedLesson)
        assertTrue(viewModel.uiState.value.exercises.isEmpty())
    }

    @Test
    fun uiState_filterByLevel_returnsMatchingLessonsOnly() = runTest {
        val viewModel = GrammarViewModel(fakeRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        viewModel.selectLevel("C1")
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("C1", state.selectedLevel)
        assertEquals(1, state.lessons.size)
        assertEquals("grammar.c1.inversion-negative-adverbials", state.lessons[0].id)
    }

    @Test
    fun uiState_toggleTurkishReveal_togglesStateCorrectly() = runTest {
        val viewModel = GrammarViewModel(fakeRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isTurkishRevealed)

        viewModel.toggleTurkishReveal()
        testScheduler.advanceUntilIdle()
        assertTrue(viewModel.uiState.value.isTurkishRevealed)

        viewModel.toggleTurkishReveal()
        testScheduler.advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isTurkishRevealed)
    }

    @Test
    fun uiState_selectingNewLesson_resetsTurkishRevealAndLoadsExercises() = runTest {
        val viewModel = GrammarViewModel(fakeRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        // Reveal Turkish on first lesson
        viewModel.toggleTurkishReveal()
        testScheduler.advanceUntilIdle()
        assertTrue(viewModel.uiState.value.isTurkishRevealed)

        // Select second lesson (which has an exercise attached)
        viewModel.selectLesson(mockLessons[1])
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("grammar.present-perfect-vs-past-simple", state.selectedLesson?.id)
        assertFalse("Selecting new lesson must reset Turkish reveal to English-first (ADR-009)", state.isTurkishRevealed)
        assertEquals(1, state.exercises.size)
        assertEquals("exercise.grammar.present-perfect-01", state.exercises[0].id)
    }

    @Test
    fun uiState_searchQuery_filtersByMatchingTitleOrContent() = runTest {
        val viewModel = GrammarViewModel(fakeRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        viewModel.updateSearchQuery("inversion")
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.lessons.size)
        assertEquals("grammar.c1.inversion-negative-adverbials", state.lessons[0].id)
    }
}
