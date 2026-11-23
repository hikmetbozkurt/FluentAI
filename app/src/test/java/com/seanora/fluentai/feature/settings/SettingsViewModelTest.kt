package com.seanora.fluentai.feature.settings

import com.seanora.fluentai.core.model.LearningEvidence
import com.seanora.fluentai.core.model.MasterySnapshot
import com.seanora.fluentai.core.model.MistakeRecord
import com.seanora.fluentai.core.model.ReviewSchedule
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.data.preferences.UserPreferencesRepository
import com.seanora.fluentai.data.repository.UserLearningRepository
import com.seanora.fluentai.feature.reading.ReadingSurfaceStyle
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before fun setUp() = Dispatchers.setMain(dispatcher)
    @After fun tearDown() = Dispatchers.resetMain()

    @Test
    fun editableSettings_persistsTargetGoalInterestsAndReaderComfort() = runTest {
        val userRepo = SettingsFakeUserRepo()
        val prefsRepo = SettingsFakePrefsRepo()
        val vm = SettingsViewModel(userRepo, prefsRepo)

        vm.updateTargetCefr("C2")
        vm.updateDailyGoalMinutes(30)
        vm.toggleInterest("Technology")
        vm.toggleInterest("IELTS")
        vm.setReadingSurfaceStyle(ReadingSurfaceStyle.DARK)
        vm.increaseReadingFontSize()
        advanceUntilIdle()

        val saved = userRepo.profileFlow.value!!
        assertEquals("C2", saved.targetCefrLevel)
        assertEquals(30, saved.dailyGoalMinutes)
        assertTrue("Technology" in saved.learningInterests)
        assertTrue("IELTS" in saved.learningInterests)
        assertEquals("DARK", prefsRepo.themeFlow.value)
        assertEquals(22f, prefsRepo.fontFlow.value)

        // Toggle removes an existing interest cleanly
        vm.toggleInterest("Technology")
        advanceUntilIdle()
        assertTrue("Technology" !in userRepo.profileFlow.value!!.learningInterests)
    }
}

private class SettingsFakeUserRepo : UserLearningRepository {
    val profileFlow = MutableStateFlow<UserProfile?>(UserProfile(targetCefrLevel = "B2", dailyGoalMinutes = 15))
    override suspend fun recordEvidence(evidence: LearningEvidence) = 1L
    override fun getEvidenceForContent(contentId: String) = flowOf(emptyList<LearningEvidence>())
    override fun getRecentEvidence(limit: Int) = flowOf(emptyList<LearningEvidence>())
    override fun getMasterySnapshot(contentId: String) = flowOf<MasterySnapshot?>(null)
    override suspend fun getMasterySnapshotSync(contentId: String): MasterySnapshot? = null
    override fun getAllMasterySnapshots() = flowOf(emptyList<MasterySnapshot>())
    override suspend fun saveMasterySnapshot(snapshot: MasterySnapshot) {}
    override fun getActiveMistakes() = flowOf(emptyList<MistakeRecord>())
    override fun getAllMistakes() = flowOf(emptyList<MistakeRecord>())
    override suspend fun findMistake(contentId: String, trapType: String): MistakeRecord? = null
    override suspend fun saveMistake(mistake: MistakeRecord) = 1L
    override fun getDueReviews(currentTimestamp: Long) = flowOf(emptyList<ReviewSchedule>())
    override fun getReviewSchedule(contentId: String) = flowOf<ReviewSchedule?>(null)
    override suspend fun getReviewScheduleSync(contentId: String): ReviewSchedule? = null
    override suspend fun saveReviewSchedule(schedule: ReviewSchedule) {}
    override fun getUserProfile(id: String) = profileFlow
    override suspend fun getUserProfileSync(id: String) = profileFlow.value
    override suspend fun saveUserProfile(profile: UserProfile) { profileFlow.value = profile }
}

private class SettingsFakePrefsRepo : UserPreferencesRepository {
    override val selectedAvatarKey = MutableStateFlow<String?>(null)
    override suspend fun getSelectedAvatarKeySync() = null
    override suspend fun setSelectedAvatarKey(key: String) {}
    override suspend fun clearSelectedAvatarKey() {}

    val themeFlow = MutableStateFlow("PAPER")
    val fontFlow = MutableStateFlow(20f)

    override val readingSurfaceTheme = themeFlow
    override suspend fun setReadingSurfaceTheme(theme: String) { themeFlow.value = theme }
    override val readingFontSizeSp = fontFlow
    override suspend fun setReadingFontSizeSp(sizeSp: Float) { fontFlow.value = sizeSp.coerceIn(16f, 26f) }
}
