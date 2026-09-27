package com.seanora.fluentai.feature.profile

import com.seanora.fluentai.core.model.UserAvatar
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.data.preferences.UserPreferencesRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val profileFlow = MutableStateFlow<UserProfile?>(null)
    private val avatarKeyFlow = MutableStateFlow<String?>(null)

    private val fakePreferencesRepo = object : UserPreferencesRepository {
        override val selectedAvatarKey: Flow<String?> = avatarKeyFlow
        override suspend fun getSelectedAvatarKeySync(): String? = avatarKeyFlow.value
        override suspend fun setSelectedAvatarKey(key: String) { avatarKeyFlow.value = key }
        override suspend fun clearSelectedAvatarKey() { avatarKeyFlow.value = null }
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

    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        profileFlow.value = null
        avatarKeyFlow.value = null
        viewModel = ProfileViewModel(fakeUserRepo, fakePreferencesRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun selectedAvatarKey_defaultsToNull_whenNotSet() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.selectedAvatarKey.collect()
        }
        advanceUntilIdle()

        assertNull(viewModel.selectedAvatarKey.value)
        assertNull(UserAvatar.getDrawableRes(viewModel.selectedAvatarKey.value))

        collectJob.cancel()
    }

    @Test
    fun selectAvatar_updatesPreferences_andResolvesCorrectDrawable() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.selectedAvatarKey.collect()
        }
        advanceUntilIdle()

        viewModel.selectAvatar("avatar_3")
        advanceUntilIdle()

        assertEquals("avatar_3", viewModel.selectedAvatarKey.value)
        assertEquals(com.seanora.fluentai.R.drawable.avatar_3, UserAvatar.getDrawableRes("avatar_3"))

        collectJob.cancel()
    }

    @Test
    fun avatarModel_handlesFallbackGracefully() {
        assertEquals(com.seanora.fluentai.R.drawable.avatar_1, UserAvatar.getDrawableRes("avatar_1"))
        assertEquals(com.seanora.fluentai.R.drawable.avatar_2, UserAvatar.getDrawableRes("avatar_2"))
        assertEquals(com.seanora.fluentai.R.drawable.avatar_3, UserAvatar.getDrawableRes("avatar_3"))
        assertNull(UserAvatar.getDrawableRes("avatar_999"))
        assertNull(UserAvatar.getDrawableRes(null))
        assertNull(UserAvatar.getDrawableRes(""))
    }
}
