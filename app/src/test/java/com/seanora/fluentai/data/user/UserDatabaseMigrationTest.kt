package com.seanora.fluentai.data.user

import androidx.sqlite.db.SupportSQLiteDatabase
import com.seanora.fluentai.core.model.UserProfile
import com.seanora.fluentai.data.user.dao.LearningEvidenceDao
import com.seanora.fluentai.data.user.dao.MasterySnapshotDao
import com.seanora.fluentai.data.user.dao.MistakeRecordDao
import com.seanora.fluentai.data.user.dao.ReviewScheduleDao
import com.seanora.fluentai.data.user.dao.TodayPlanDao
import com.seanora.fluentai.data.user.dao.UserProfileDao
import com.seanora.fluentai.data.user.entity.UserProfileEntity
import com.seanora.fluentai.data.repository.UserLearningRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.lang.reflect.Proxy

class UserDatabaseMigrationTest {

    @Test
    fun migration_4_to_5_executesAlterTableAddColumnDisplayName() {
        val executedSql = mutableListOf<String>()
        val dummyDb = Proxy.newProxyInstance(
            SupportSQLiteDatabase::class.java.classLoader,
            arrayOf(SupportSQLiteDatabase::class.java)
        ) { _, method, args ->
            if (method.name == "execSQL" && args != null && args.isNotEmpty()) {
                executedSql.add(args[0] as String)
            }
            null
        } as SupportSQLiteDatabase

        UserDatabaseMigrations.MIGRATION_4_5.migrate(dummyDb)

        assertEquals(1, executedSql.size)
        assertTrue(
            "Migration 4->5 must add display_name column to user_profiles",
            executedSql[0].contains("ALTER TABLE `user_profiles` ADD COLUMN `display_name` TEXT")
        )
    }

    @Test
    fun repository_loadProfileWithNullDisplayName_returnsNullWithoutCrashing() = runTest {
        val entityWithoutName = UserProfileEntity(
            id = "default_user",
            displayName = null,
            targetCefrLevel = "B2",
            estimatedOverallLevel = "B1",
            estimatedVocabLevel = "B1",
            estimatedGrammarLevel = "B1",
            estimatedSpeakingLevel = "B1",
            estimatedReadingLevel = "B1",
            estimatedListeningLevel = "B1",
            dailyGoalMinutes = 20,
            learningInterestsJson = "[\"tech\"]",
            streakDays = 5,
            lastActiveDate = "2026-09-24",
            createdAt = 1000L,
            updatedAt = 1000L
        )

        val profileMap = mutableMapOf("default_user" to entityWithoutName)

        val fakeUserDao = object : UserProfileDao {
            override fun getProfile(id: String): Flow<UserProfileEntity?> =
                flowOf(profileMap[id])

            override suspend fun getProfileSync(id: String): UserProfileEntity? =
                profileMap[id]

            override suspend fun insertOrUpdateProfile(profile: UserProfileEntity) {
                profileMap[profile.id] = profile
            }
        }

        val repository = createRepository(fakeUserDao)
        val loaded = repository.getUserProfile("default_user").first()

        assertNotNull(loaded)
        assertNull(loaded?.displayName)
        assertEquals("B2", loaded?.targetCefrLevel)
        assertEquals(5, loaded?.streakDays)
    }

    @Test
    fun repository_updateDisplayName_persistsAndPreservesAllOtherFields() = runTest {
        val originalEntity = UserProfileEntity(
            id = "default_user",
            displayName = null,
            targetCefrLevel = "C1",
            estimatedOverallLevel = "B2",
            estimatedVocabLevel = "B2",
            estimatedGrammarLevel = "B1",
            estimatedSpeakingLevel = "B2",
            estimatedReadingLevel = "B2",
            estimatedListeningLevel = "B2",
            dailyGoalMinutes = 30,
            learningInterestsJson = "[\"management\",\"tech\"]",
            streakDays = 14,
            lastActiveDate = "2026-09-24",
            createdAt = 1000L,
            updatedAt = 1000L
        )

        val profileMap = mutableMapOf("default_user" to originalEntity)

        val fakeUserDao = object : UserProfileDao {
            override fun getProfile(id: String): Flow<UserProfileEntity?> =
                flowOf(profileMap[id])

            override suspend fun getProfileSync(id: String): UserProfileEntity? =
                profileMap[id]

            override suspend fun insertOrUpdateProfile(profile: UserProfileEntity) {
                profileMap[profile.id] = profile
            }
        }

        val repository = createRepository(fakeUserDao)

        // Update display name
        repository.updateDisplayName("Hikmet Deniz", "default_user")

        val updated = repository.getUserProfile("default_user").first()
        assertNotNull(updated)
        assertEquals("Hikmet Deniz", updated?.displayName)
        // Verify all other fields were preserved!
        assertEquals("C1", updated?.targetCefrLevel)
        assertEquals("B2", updated?.estimatedOverallLevel)
        assertEquals("B2", updated?.estimatedVocabLevel)
        assertEquals("B1", updated?.estimatedGrammarLevel)
        assertEquals("B2", updated?.estimatedSpeakingLevel)
        assertEquals("B2", updated?.estimatedReadingLevel)
        assertEquals("B2", updated?.estimatedListeningLevel)
        assertEquals(30, updated?.dailyGoalMinutes)
        assertEquals(listOf("management", "tech"), updated?.learningInterests)
        assertEquals(14, updated?.streakDays)
    }

    @Test
    fun repository_updateDisplayName_blankTreatedAsNull() = runTest {
        val originalEntity = UserProfileEntity(
            id = "default_user",
            displayName = "Hikmet",
            targetCefrLevel = "B2",
            estimatedOverallLevel = "B1",
            estimatedVocabLevel = "B1",
            estimatedGrammarLevel = "B1",
            estimatedSpeakingLevel = "B1",
            estimatedReadingLevel = "B1",
            estimatedListeningLevel = "B1",
            dailyGoalMinutes = 15,
            learningInterestsJson = "[]",
            streakDays = 1,
            lastActiveDate = "2026-09-25",
            createdAt = 1000L,
            updatedAt = 1000L
        )

        val profileMap = mutableMapOf("default_user" to originalEntity)

        val fakeUserDao = object : UserProfileDao {
            override fun getProfile(id: String): Flow<UserProfileEntity?> =
                flowOf(profileMap[id])

            override suspend fun getProfileSync(id: String): UserProfileEntity? =
                profileMap[id]

            override suspend fun insertOrUpdateProfile(profile: UserProfileEntity) {
                profileMap[profile.id] = profile
            }
        }

        val repository = createRepository(fakeUserDao)

        // Clear name with blank string
        repository.updateDisplayName("   ", "default_user")

        val updated = repository.getUserProfile("default_user").first()
        assertNotNull(updated)
        assertNull(updated?.displayName)
    }

    @Test
    fun presentationFallback_showsLearnerWhenDisplayNameAbsent() {
        val profileWithNullName = UserProfile(
            id = "default_user",
            displayName = null,
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
        val profileWithBlankName = profileWithNullName.copy(displayName = "   ")
        val profileWithValidName = profileWithNullName.copy(displayName = "Deniz")

        fun getPresentationName(profile: UserProfile?): String {
            return profile?.displayName?.trim()?.ifBlank { null } ?: "Learner"
        }

        assertEquals("Learner", getPresentationName(null))
        assertEquals("Learner", getPresentationName(profileWithNullName))
        assertEquals("Learner", getPresentationName(profileWithBlankName))
        assertEquals("Deniz", getPresentationName(profileWithValidName))
    }

    private inline fun <reified T> createDummyProxy(): T {
        return Proxy.newProxyInstance(
            T::class.java.classLoader,
            arrayOf(T::class.java)
        ) { _, _, _ -> null } as T
    }

    private fun createRepository(userProfileDao: UserProfileDao): UserLearningRepositoryImpl {
        return UserLearningRepositoryImpl(
            learningEvidenceDao = createDummyProxy<LearningEvidenceDao>(),
            masterySnapshotDao = createDummyProxy<MasterySnapshotDao>(),
            mistakeRecordDao = createDummyProxy<MistakeRecordDao>(),
            reviewScheduleDao = createDummyProxy<ReviewScheduleDao>(),
            userProfileDao = userProfileDao,
            todayPlanDao = createDummyProxy<TodayPlanDao>()
        )
    }
}
