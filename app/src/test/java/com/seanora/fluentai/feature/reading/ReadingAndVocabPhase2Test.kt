package com.seanora.fluentai.feature.reading

import com.seanora.fluentai.core.model.VocabItem
import com.seanora.fluentai.data.dictionary.DictionaryEntry
import com.seanora.fluentai.data.dictionary.OfflineDictionaryRepository
import com.seanora.fluentai.data.dictionary.OfflineDictionaryRepositoryImpl
import com.seanora.fluentai.data.preferences.UserPreferencesRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.data.repository.VocabularyRepository
import com.seanora.fluentai.feature.vocabulary.findHighlightRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReadingAndVocabPhase2Test {

    private val dispatcher = StandardTestDispatcher()

    @Before fun setUp() = Dispatchers.setMain(dispatcher)
    @After fun tearDown() = Dispatchers.resetMain()

    @Test
    fun findHighlightRange_matchesExactAndInflectedForms() {
        val exact = findHighlightRange("We need a reliable system.", "reliable")
        assertNotNull(exact)
        assertEquals("reliable", "We need a reliable system.".substring(exact!!))

        val inflected = findHighlightRange("The vendor complied with every audit rule.", "comply")
        assertNotNull(inflected)
        assertEquals("complied", "The vendor complied with every audit rule.".substring(inflected!!))

        val phrasal = findHighlightRange("Let's hang out after work.", "hang out")
        assertNotNull(phrasal)
        assertEquals("hang out", "Let's hang out after work.".substring(phrasal!!))

        assertNull(findHighlightRange("Unrelated sentence.", "resilient"))
    }

    @Test
    fun readingSurfaceStyle_fromKey_parsesAllThemesWithPaperFallback() {
        assertEquals(ReadingSurfaceStyle.PAPER, ReadingSurfaceStyle.fromKey(null))
        assertEquals(ReadingSurfaceStyle.PAPER, ReadingSurfaceStyle.fromKey("unknown"))
        assertEquals(ReadingSurfaceStyle.LIGHT, ReadingSurfaceStyle.fromKey("light"))
        assertEquals(ReadingSurfaceStyle.SEPIA, ReadingSurfaceStyle.fromKey("SEPIA"))
        assertEquals(ReadingSurfaceStyle.DARK, ReadingSurfaceStyle.fromKey("Dark"))
    }

    @Test
    fun readerSupportViewModel_persistsBookmarksFontSizeAndLooksUpInflectedWords() = runTest {
        val prefs = InMemoryReadingPrefs()
        val vocabRepo = InMemoryVocabRepo(
            listOf(
                VocabItem(
                    id = "vocab.comply",
                    headword = "comply",
                    cefrLevel = "B2",
                    partOfSpeech = "verb",
                    phonetic = "/kəmˈplaɪ/",
                    definitionEn = "To act in accordance with a rule or request.",
                    meaningTr = "uymak, riayet etmek",
                ),
            ),
        )
        val userRepo = InMemoryUserRepoForReadingSupport()
        val vm = ReadingReaderSupportViewModel(prefs, vocabRepo, userRepo)
        advanceUntilIdle()

        // Inflected lookup resolves base headword
        val found = vm.lookupLocalVocab("complied,")
        assertEquals("vocab.comply", found?.id)

        // Bookmark toggle persists
        vm.toggleSavedReading("reading.b2.article-001")
        advanceUntilIdle()
        assertTrue("reading.b2.article-001" in prefs.savedFlow.value)

        // Font size clamps within [16, 26]
        vm.increaseFontSize()
        advanceUntilIdle()
        assertEquals(22f, prefs.fontFlow.value)

        // Save word to vocabulary review creates a ReviewSchedule
        vm.saveWordToVocabularyReview("vocab.comply")
        advanceUntilIdle()
        assertTrue("vocab.comply" in vm.savedToReviewWordIds.value)
        assertEquals(1, userRepo.savedSchedules.size)
    }

    @Test
    fun offlineDictionary_morphologicalCandidatesAndAsyncLookupWork() = runTest {
        assertEquals("complied", OfflineDictionaryRepositoryImpl.normalizeToken("  Complied! "))
        val candidates = OfflineDictionaryRepositoryImpl.morphologicalCandidates("running")
        assertTrue("run" in candidates)
        assertTrue("comply" in OfflineDictionaryRepositoryImpl.morphologicalCandidates("complied"))

        val fakeDict = object : OfflineDictionaryRepository {
            override suspend fun lookup(rawWord: String): DictionaryEntry? =
                if (rawWord.equals("ubiquitous", ignoreCase = true)) {
                    DictionaryEntry(
                        word = "ubiquitous",
                        displayWord = "ubiquitous",
                        phonetic = "/juˈbɪkwətəs/",
                        partOfSpeech = "adjective",
                        cefrLevel = "C1",
                        definitionEn = "Being present everywhere at once.",
                    )
                } else null
        }
        val vm = ReadingReaderSupportViewModel(
            InMemoryReadingPrefs(),
            InMemoryVocabRepo(emptyList()),
            InMemoryUserRepoForReadingSupport(),
            fakeDict,
        )
        vm.resolveDictionaryEntry("ubiquitous")
        advanceUntilIdle()
        assertEquals("C1", vm.activeDictionaryEntry.value?.cefrLevel)
        assertEquals("/juˈbɪkwətəs/", vm.activeDictionaryEntry.value?.phonetic)
    }
}

private class InMemoryReadingPrefs : UserPreferencesRepository {
    override val selectedAvatarKey = MutableStateFlow<String?>(null)
    override suspend fun getSelectedAvatarKeySync() = null
    override suspend fun setSelectedAvatarKey(key: String) {}
    override suspend fun clearSelectedAvatarKey() {}

    val savedFlow = MutableStateFlow<Set<String>>(emptySet())
    val themeFlow = MutableStateFlow("PAPER")
    val fontFlow = MutableStateFlow(20f)

    override val savedReadingIds = savedFlow
    override suspend fun toggleSavedReading(articleId: String) {
        savedFlow.value = if (articleId in savedFlow.value) savedFlow.value - articleId else savedFlow.value + articleId
    }
    override val readingSurfaceTheme = themeFlow
    override suspend fun setReadingSurfaceTheme(theme: String) { themeFlow.value = theme }
    override val readingFontSizeSp = fontFlow
    override suspend fun setReadingFontSizeSp(sizeSp: Float) { fontFlow.value = sizeSp.coerceIn(16f, 26f) }
}

private class InMemoryVocabRepo(private val items: List<VocabItem>) : VocabularyRepository {
    override fun getAllVocab() = flowOf(items)
    override fun getVocabByLevel(level: String) = flowOf(items.filter { it.cefrLevel == level })
    override fun getVocabById(id: String) = flowOf(items.firstOrNull { it.id == id })
    override fun searchVocab(query: String) = flowOf(items.filter { it.headword.contains(query, true) })
    override suspend fun getVocabCount() = items.size
    override suspend fun getAllVocabSnapshot() = items
    override suspend fun getCollectionSummaries(level: String?) = emptyList<com.seanora.fluentai.core.model.VocabularyCollection>()
    override suspend fun getCollectionWords(topicId: String, level: String?) = emptyList<VocabItem>()
}

private class InMemoryUserRepoForReadingSupport : UserLearningRepository {
    val savedSchedules = mutableListOf<com.seanora.fluentai.core.model.ReviewSchedule>()
    override suspend fun recordEvidence(evidence: com.seanora.fluentai.core.model.LearningEvidence) = 1L
    override fun getEvidenceForContent(contentId: String) = flowOf(emptyList<com.seanora.fluentai.core.model.LearningEvidence>())
    override fun getRecentEvidence(limit: Int) = flowOf(emptyList<com.seanora.fluentai.core.model.LearningEvidence>())
    override fun getMasterySnapshot(contentId: String) = flowOf(null)
    override suspend fun getMasterySnapshotSync(contentId: String) = null
    override fun getAllMasterySnapshots() = flowOf(emptyList<com.seanora.fluentai.core.model.MasterySnapshot>())
    override suspend fun saveMasterySnapshot(snapshot: com.seanora.fluentai.core.model.MasterySnapshot) {}
    override fun getActiveMistakes() = flowOf(emptyList<com.seanora.fluentai.core.model.MistakeRecord>())
    override fun getAllMistakes() = flowOf(emptyList<com.seanora.fluentai.core.model.MistakeRecord>())
    override suspend fun findMistake(contentId: String, trapType: String) = null
    override suspend fun saveMistake(mistake: com.seanora.fluentai.core.model.MistakeRecord) = 1L
    override fun getDueReviews(currentTimestamp: Long) = flowOf(emptyList<com.seanora.fluentai.core.model.ReviewSchedule>())
    override fun getReviewSchedule(contentId: String) = flowOf(null)
    override suspend fun getReviewScheduleSync(contentId: String) = savedSchedules.firstOrNull { it.contentId == contentId }
    override suspend fun saveReviewSchedule(schedule: com.seanora.fluentai.core.model.ReviewSchedule) { savedSchedules += schedule }
    override fun getUserProfile(id: String) = flowOf(null)
    override suspend fun getUserProfileSync(id: String) = null
    override suspend fun saveUserProfile(profile: com.seanora.fluentai.core.model.UserProfile) {}
}
