package com.seanora.fluentai.app.navigation

import com.seanora.fluentai.core.model.SearchContentType
import com.seanora.fluentai.core.model.SearchResultItem
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.data.preferences.UserPreferencesRepository
import com.seanora.fluentai.data.repository.SearchRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HeaderViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val profileFlow = MutableStateFlow<UserProfile?>(null)
    private val avatarKeyFlow = MutableStateFlow<String?>(null)

    private val fakePreferencesRepo = object : UserPreferencesRepository {
        override val selectedAvatarKey = avatarKeyFlow
        override suspend fun getSelectedAvatarKeySync() = avatarKeyFlow.value
        override suspend fun setSelectedAvatarKey(key: String) { avatarKeyFlow.value = key }
        override suspend fun clearSelectedAvatarKey() { avatarKeyFlow.value = null }
    }

    private val fakeSearchRepo = object : SearchRepository {
        var throwError = false
        override suspend fun searchContent(query: String, limitPerCategory: Int): List<SearchResultItem> {
            if (throwError) throw RuntimeException("DB error")
            if (query.isBlank()) return emptyList()
            if (query == "test") {
                return listOf(
                    SearchResultItem(
                        id = "vocab.test",
                        title = "test",
                        subtitle = "A test term",
                        contentType = SearchContentType.VOCABULARY,
                        targetRoute = "learn/vocabulary/vocab.test"
                    )
                )
            }
            return emptyList()
        }
    }

    private val fakeUserRepo = object : UserLearningRepository {
        override suspend fun recordEvidence(evidence: com.seanora.fluentai.core.model.LearningEvidence) = 0L
        override fun getEvidenceForContent(contentId: String) = flowOf(emptyList<com.seanora.fluentai.core.model.LearningEvidence>())
        override fun getRecentEvidence(limit: Int) = flowOf(emptyList<com.seanora.fluentai.core.model.LearningEvidence>())
        override fun getMasterySnapshot(contentId: String) = flowOf(null)
        override suspend fun getMasterySnapshotSync(contentId: String) = null
        override fun getAllMasterySnapshots() = flowOf(emptyList<com.seanora.fluentai.core.model.MasterySnapshot>())
        override suspend fun saveMasterySnapshot(snapshot: com.seanora.fluentai.core.model.MasterySnapshot) {}
        override fun getActiveMistakes() = flowOf(emptyList<com.seanora.fluentai.core.model.MistakeRecord>())
        override fun getAllMistakes() = flowOf(emptyList<com.seanora.fluentai.core.model.MistakeRecord>())
        override suspend fun findMistake(contentId: String, trapType: String) = null
        override suspend fun saveMistake(mistake: com.seanora.fluentai.core.model.MistakeRecord) = 0L
        override fun getDueReviews(currentTimestamp: Long) = flowOf(emptyList<com.seanora.fluentai.core.model.ReviewSchedule>())
        override fun getReviewSchedule(contentId: String) = flowOf(null)
        override suspend fun getReviewScheduleSync(contentId: String) = null
        override suspend fun saveReviewSchedule(schedule: com.seanora.fluentai.core.model.ReviewSchedule) {}
        override fun getUserProfile(id: String) = profileFlow
        override suspend fun getUserProfileSync(id: String) = profileFlow.value
        override suspend fun saveUserProfile(profile: UserProfile) { profileFlow.value = profile }
        override suspend fun updateDisplayName(name: String?, id: String) {
            val cur = profileFlow.value ?: return
            profileFlow.value = cur.copy(displayName = name?.trim()?.ifBlank { null })
        }
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
    fun displayName_defaultsToLearner_whenProfileNullOrBlank() = runTest {
        profileFlow.value = null
        val viewModel = HeaderViewModel(fakeSearchRepo, fakeUserRepo, fakePreferencesRepo)
        backgroundScope.launch(kotlinx.coroutines.test.UnconfinedTestDispatcher(testScheduler)) {
            viewModel.displayName.collect()
        }
        advanceUntilIdle()

        assertEquals("Learner", viewModel.displayName.value)

        // Set profile with blank name
        profileFlow.value = UserProfile(
            id = "default_user",
            displayName = "   ",
            targetCefrLevel = "B2",
            estimatedOverallLevel = "B1",
            estimatedVocabLevel = "B1",
            estimatedGrammarLevel = "B1",
            estimatedSpeakingLevel = "B1",
            dailyGoalMinutes = 20,
            learningInterests = emptyList(),
            streakDays = 0,
            lastActiveDate = "2026-09-25"
        )
        advanceUntilIdle()
        assertEquals("Learner", viewModel.displayName.value)
    }

    @Test
    fun displayName_emitsActualName_whenNonBlank() = runTest {
        profileFlow.value = UserProfile(
            id = "default_user",
            displayName = "Hikmet Deniz",
            targetCefrLevel = "C1",
            estimatedOverallLevel = "B2",
            estimatedVocabLevel = "B2",
            estimatedGrammarLevel = "B1",
            estimatedSpeakingLevel = "B2",
            dailyGoalMinutes = 30,
            learningInterests = emptyList(),
            streakDays = 5,
            lastActiveDate = "2026-09-25"
        )
        val viewModel = HeaderViewModel(fakeSearchRepo, fakeUserRepo, fakePreferencesRepo)
        backgroundScope.launch(kotlinx.coroutines.test.UnconfinedTestDispatcher(testScheduler)) {
            viewModel.displayName.collect()
        }
        advanceUntilIdle()

        assertEquals("Hikmet Deniz", viewModel.displayName.value)
    }

    @Test
    fun search_queryBlank_clearsResultsImmediately() = runTest {
        val viewModel = HeaderViewModel(fakeSearchRepo, fakeUserRepo, fakePreferencesRepo)

        viewModel.onSearchQueryChange("test")
        advanceTimeBy(300)
        advanceUntilIdle()

        assertEquals(1, viewModel.searchResults.value.size)

        // Now clear by blanking
        viewModel.onSearchQueryChange("")
        advanceUntilIdle()

        assertTrue(viewModel.searchResults.value.isEmpty())
        assertFalse(viewModel.isSearching.value)
    }

    @Test
    fun search_clearSearch_resetsQueryAndResults() = runTest {
        val viewModel = HeaderViewModel(fakeSearchRepo, fakeUserRepo, fakePreferencesRepo)

        viewModel.onSearchQueryChange("test")
        advanceTimeBy(300)
        advanceUntilIdle()

        assertEquals(1, viewModel.searchResults.value.size)

        viewModel.clearSearch()
        assertEquals("", viewModel.searchQuery.value)
        assertTrue(viewModel.searchResults.value.isEmpty())
    }

    @Test
    fun selectedAvatarKey_emitsFromPreferencesRepo() = runTest {
        avatarKeyFlow.value = "avatar_1"
        val viewModel = HeaderViewModel(fakeSearchRepo, fakeUserRepo, fakePreferencesRepo)
        backgroundScope.launch(kotlinx.coroutines.test.UnconfinedTestDispatcher(testScheduler)) {
            viewModel.selectedAvatarKey.collect()
        }
        advanceUntilIdle()

        assertEquals("avatar_1", viewModel.selectedAvatarKey.value)
    }
}
