package com.seanora.fluentai.feature.home

import com.seanora.fluentai.core.designsystem.components.CefrLevel
import com.seanora.fluentai.feature.home.data.MockHomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun homeViewModel_emitsSuccessWithMockRepositoryData() = runTest(testDispatcher) {
        val repository = MockHomeRepository()
        val viewModel = HomeViewModel(repository)

        // Subscribe to StateFlow so WhileSubscribed triggers upstream flow
        val collectJob = backgroundScope.launch(kotlinx.coroutines.test.UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("Expected HomeUiState.Success but was $state", state is HomeUiState.Success)

        val successState = state as HomeUiState.Success
        val plan = successState.plan

        // Verify Hero Practice item
        assertNotNull(plan.heroPractice)
        assertEquals("speaking.c1.executive-alignment-001", plan.heroPractice.id)
        assertEquals(CefrLevel.C1, plan.heroPractice.targetLevel)
        assertEquals(15, plan.heroPractice.durationMinutes)

        // Verify due reviews
        assertEquals(2, plan.dueReviews.size)
        assertEquals("vocab.deliberation", plan.dueReviews[0].id)
        assertEquals(CefrLevel.B2, plan.dueReviews[0].level)

        // Verify skill profiles
        assertEquals(4, plan.skillProfiles.size)
        assertEquals("Vocabulary", plan.skillProfiles[0].skill)

        // Verify daily metrics
        assertEquals(14, plan.dailyMetrics.streakDays)
    }

    @Test
    fun homeViewModel_emitsErrorWhenRepositoryFails() = runTest(testDispatcher) {
        val failingRepository = object : com.seanora.fluentai.feature.home.data.HomeRepository {
            override fun getTodayPlan(): kotlinx.coroutines.flow.Flow<com.seanora.fluentai.feature.home.model.TodayPlan> {
                return kotlinx.coroutines.flow.flow {
                    throw IllegalStateException("Database read error")
                }
            }
        }
        val viewModel = HomeViewModel(failingRepository)

        val collectJob = backgroundScope.launch(kotlinx.coroutines.test.UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("Expected HomeUiState.Error but was $state", state is HomeUiState.Error)
        assertEquals("Content could not be loaded. Please try again.", (state as HomeUiState.Error).message)
    }
}
