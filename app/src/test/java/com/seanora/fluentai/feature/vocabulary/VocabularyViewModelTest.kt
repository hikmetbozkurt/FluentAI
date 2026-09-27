package com.seanora.fluentai.feature.vocabulary

import com.seanora.fluentai.core.model.Collocation
import com.seanora.fluentai.core.model.ContextExample
import com.seanora.fluentai.core.model.TurkishTrap
import com.seanora.fluentai.core.model.VocabItem
import com.seanora.fluentai.data.repository.VocabularyRepository
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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class VocabularyViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val mockVocabList = listOf(
        VocabItem(
            id = "vocab.schedule",
            headword = "schedule",
            cefrLevel = "A2",
            partOfSpeech = "noun",
            phonetic = "/ˈʃedʒ.uːl/",
            definitionEn = "A plan of events or tasks.",
            meaningTr = "program, takvim",
            collocations = listOf(Collocation("on schedule", "takvime uygun")),
            examples = listOf(ContextExample("On schedule", "Takvime uygun")),
            turkishTraps = listOf(TurkishTrap("preposition_confusion", "on schedule kullanılır."))
        ),
        VocabItem(
            id = "vocab.deadline",
            headword = "deadline",
            cefrLevel = "B1",
            partOfSpeech = "noun",
            phonetic = "/ˈded.laɪn/",
            definitionEn = "A date or time by which something must be finished.",
            meaningTr = "son teslim tarihi",
            collocations = listOf(Collocation("meet a deadline", "teslim tarihine yetişmek")),
            examples = listOf(ContextExample("Meet the deadline", "Tarihe yetişmek")),
            turkishTraps = listOf(TurkishTrap("collocation_error", "catch deadline denmez."))
        ),
        VocabItem(
            id = "vocab.deliberation",
            headword = "deliberation",
            cefrLevel = "C1",
            partOfSpeech = "noun",
            phonetic = "/dɪˌlɪb.əˈreɪ.ʃən/",
            definitionEn = "Long, careful, and serious discussion.",
            meaningTr = "ayrıntılı müzakere",
            collocations = listOf(Collocation("after much deliberation", "uzun müzakerelerin ardından")),
            examples = listOf(ContextExample("Intense deliberation", "Yoğun müzakere")),
            turkishTraps = listOf(TurkishTrap("false_friend", "Kasıt ile karıştırmayınız."))
        )
    )

    private val fakeRepository = object : VocabularyRepository {
        override fun getAllVocab(): Flow<List<VocabItem>> = flowOf(mockVocabList)

        override fun getVocabByLevel(level: String): Flow<List<VocabItem>> = flowOf(
            mockVocabList.filter { it.cefrLevel == level }
        )

        override fun getVocabById(id: String): Flow<VocabItem?> = flowOf(
            mockVocabList.firstOrNull { it.id == id }
        )

        override fun searchVocab(query: String): Flow<List<VocabItem>> = flowOf(
            mockVocabList.filter { it.headword.contains(query, ignoreCase = true) || it.meaningTr.contains(query, ignoreCase = true) }
        )

        override suspend fun getVocabCount(): Int = mockVocabList.size
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
    fun uiState_initialLoad_populatesLibraryWithoutImplicitWordSelection() = runTest {
        val viewModel = VocabularyViewModel(fakeRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(3, state.items.size)
        assertNull(state.selectedItem)
        assertFalse(state.isTurkishRevealed)
    }

    @Test
    fun unknownExplicitWord_clearsPreviousSelectionInsteadOfShowingStaleDetail() = runTest {
        val viewModel = VocabularyViewModel(fakeRepository)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect() }
        viewModel.selectItemById("vocab.deadline")
        testScheduler.advanceUntilIdle()
        assertEquals("vocab.deadline", viewModel.uiState.value.selectedItem?.id)

        viewModel.selectItemById("vocab.missing")
        testScheduler.advanceUntilIdle()

        assertNull(viewModel.uiState.value.selectedItem)
    }

    @Test
    fun uiState_filterByLevel_returnsMatchingItemsOnly() = runTest {
        val viewModel = VocabularyViewModel(fakeRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        viewModel.selectLevel("C1")
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("C1", state.selectedLevel)
        assertEquals(1, state.items.size)
        assertEquals("vocab.deliberation", state.items[0].id)
    }

    @Test
    fun uiState_toggleTurkishReveal_updatesRevealState() = runTest {
        val viewModel = VocabularyViewModel(fakeRepository)

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
    fun uiState_selectingNewItem_resetsTurkishRevealToEnglishFirst() = runTest {
        val viewModel = VocabularyViewModel(fakeRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        // Reveal Turkish for first item
        viewModel.toggleTurkishReveal()
        testScheduler.advanceUntilIdle()
        assertTrue(viewModel.uiState.value.isTurkishRevealed)

        // Select a different item -> must reset to English first (ADR-009)
        viewModel.selectItem(mockVocabList[2])
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("vocab.deliberation", state.selectedItem?.id)
        assertFalse("Turkish reveal must reset when switching words to maintain English-first UX", state.isTurkishRevealed)
    }

    @Test
    fun uiState_searchQuery_filtersByMatchingWord() = runTest {
        val viewModel = VocabularyViewModel(fakeRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        testScheduler.advanceUntilIdle()

        viewModel.updateSearchQuery("dead")
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.items.size)
        assertEquals("vocab.deadline", state.items[0].id)
    }
}
