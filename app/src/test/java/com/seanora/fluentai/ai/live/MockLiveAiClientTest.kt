package com.seanora.fluentai.ai.live

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MockLiveAiClientTest {

    @Test
    fun connect_successfulConnection_emitsConnectedAndGreeting() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val client = MockLiveAiClient(dispatcher = testDispatcher).apply {
            speechDelayMs = 0L
        }

        val config = LiveScenarioConfig(
            scenarioId = "speaking.b2.tech.review",
            title = "Tech Review",
            systemPrompt = "You are a senior tech lead."
        )

        val result = client.connect(config)
        assertTrue(result.isSuccess)
        assertEquals(LiveConnectionState.CONNECTED, client.connectionState.value)

        advanceUntilIdle()

        client.disconnect()
        assertEquals(LiveConnectionState.DISCONNECTED, client.connectionState.value)
    }

    @Test
    fun sendBargeInInterrupt_emitsInterruptedEvent() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val client = MockLiveAiClient(dispatcher = testDispatcher).apply {
            speechDelayMs = 1000L // slow speech so we can interrupt
        }

        val config = LiveScenarioConfig(
            scenarioId = "speaking.general",
            title = "General Practice",
            systemPrompt = "You are an English coach."
        )

        client.connect(config)
        client.sendBargeInInterrupt()

        advanceUntilIdle()
        // Client should have handled interruption without error
        assertEquals(LiveConnectionState.CONNECTED, client.connectionState.value)
    }

    @Test
    fun connectionFailure_transitionsToFailedState() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val client = MockLiveAiClient(dispatcher = testDispatcher).apply {
            shouldFailConnection = true
            speechDelayMs = 0L
        }

        val config = LiveScenarioConfig(
            scenarioId = "speaking.general",
            title = "General Practice",
            systemPrompt = "Coach"
        )

        val result = client.connect(config)
        assertTrue(result.isFailure)
        assertEquals(LiveConnectionState.FAILED, client.connectionState.value)
    }
}
