package com.seanora.fluentai.ai.live

import com.seanora.fluentai.core.audio.AudioChunk
import com.seanora.fluentai.core.audio.FakeAudioCaptureEngine
import com.seanora.fluentai.core.audio.FakeStreamAudioPlaybackEngine
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LiveCoachSessionManagerTest {

    @Test
    fun startSession_transitionsThroughConnectingAndStartsCapture() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val captureEngine = FakeAudioCaptureEngine()
        val playbackEngine = FakeStreamAudioPlaybackEngine()
        val mockClient = MockLiveAiClient(dispatcher = testDispatcher).apply { speechDelayMs = 0L }

        val manager = LiveCoachSessionManager(
            audioCaptureEngine = captureEngine,
            playbackEngine = playbackEngine,
            liveAiClient = mockClient,
            dispatcher = testDispatcher
        )

        assertEquals(VoiceUiState.IDLE, manager.voiceState.value)

        val config = LiveScenarioConfig(
            scenarioId = "speaking.tech.migration",
            title = "Tech Migration QBR",
            systemPrompt = "Lead the review."
        )

        manager.startSession(config)
        advanceUntilIdle()

        // Capture started
        assertTrue(captureEngine.isRecording.value)
        // Dialogue transcript has initial coach turn
        assertTrue(manager.transcript.value.isNotEmpty())
        assertEquals(TranscriptRole.COACH, manager.transcript.value.first().role)
    }

    @Test
    fun interruptCoach_immediatelyStopsPlaybackAndTransitionsToUserSpeaking() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val captureEngine = FakeAudioCaptureEngine()
        val playbackEngine = FakeStreamAudioPlaybackEngine()
        val mockClient = MockLiveAiClient(dispatcher = testDispatcher).apply { speechDelayMs = 5000L }

        val manager = LiveCoachSessionManager(
            audioCaptureEngine = captureEngine,
            playbackEngine = playbackEngine,
            liveAiClient = mockClient,
            dispatcher = testDispatcher
        )

        val config = LiveScenarioConfig(
            scenarioId = "speaking.general",
            title = "Free Talk",
            systemPrompt = "Coach"
        )

        manager.startSession(config)
        advanceUntilIdle()

        // Simulate coach speech chunk arriving
        playbackEngine.writeChunk(byteArrayOf(0x01, 0x02))
        // Trigger manual barge-in
        manager.interruptCoach()

        assertEquals(VoiceUiState.USER_SPEAKING, manager.voiceState.value)
        assertEquals(1, playbackEngine.stopAndFlushCallCount)
    }

    @Test
    fun localRmsAmplitude_doesNotInterruptCoach() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val captureEngine = FakeAudioCaptureEngine()
        val playbackEngine = FakeStreamAudioPlaybackEngine()
        val mockClient = MockLiveAiClient(dispatcher = testDispatcher).apply { speechDelayMs = 5000L }

        val manager = LiveCoachSessionManager(
            audioCaptureEngine = captureEngine,
            playbackEngine = playbackEngine,
            liveAiClient = mockClient,
            dispatcher = testDispatcher
        )

        val config = LiveScenarioConfig(
            scenarioId = "speaking.general",
            title = "Free Talk",
            systemPrompt = "Coach"
        )

        manager.startSession(config)
        advanceUntilIdle()

        // Coach is speaking
        mockClient.emitEvent(LiveSessionEvent.AudioOutputChunk(byteArrayOf(0x01, 0x02)))
        advanceUntilIdle()
        assertEquals(VoiceUiState.COACH_SPEAKING, manager.voiceState.value)

        // Simulate loud user voice / acoustic echo captured from mic
        val flushCountBefore = playbackEngine.stopAndFlushCallCount
        captureEngine.emitChunk(
            AudioChunk(
                data = byteArrayOf(0x10, 0x20),
                normalizedAmplitude = 0.95f
            )
        )
        advanceUntilIdle()

        // Local RMS must NOT interrupt coach or flush playback (Requirement 2)
        assertEquals(VoiceUiState.COACH_SPEAKING, manager.voiceState.value)
        assertEquals(flushCountBefore, playbackEngine.stopAndFlushCallCount)
    }

    @Test
    fun serverInterruptedEvent_flushesPlaybackAndTransitionsToUserSpeaking() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val captureEngine = FakeAudioCaptureEngine()
        val playbackEngine = FakeStreamAudioPlaybackEngine()
        val mockClient = MockLiveAiClient(dispatcher = testDispatcher).apply { speechDelayMs = 5000L }

        val manager = LiveCoachSessionManager(
            audioCaptureEngine = captureEngine,
            playbackEngine = playbackEngine,
            liveAiClient = mockClient,
            dispatcher = testDispatcher
        )

        val config = LiveScenarioConfig(
            scenarioId = "speaking.general",
            title = "Free Talk",
            systemPrompt = "Coach"
        )

        manager.startSession(config)
        advanceUntilIdle()

        // Coach speaking
        mockClient.emitEvent(LiveSessionEvent.OutputTranscription("Hello! How can I "))
        mockClient.emitEvent(LiveSessionEvent.AudioOutputChunk(byteArrayOf(0x01, 0x02)))
        advanceUntilIdle()
        assertEquals(VoiceUiState.COACH_SPEAKING, manager.voiceState.value)

        // Authoritative server interruption arrives
        mockClient.emitEvent(LiveSessionEvent.Interrupted)
        advanceUntilIdle()

        // Playback must be flushed and state must transition to USER_SPEAKING (Requirement 3)
        assertEquals(VoiceUiState.USER_SPEAKING, manager.voiceState.value)
        assertTrue(playbackEngine.stopAndFlushCallCount >= 1)
    }

    @Test
    fun multiTurnTranscript_accumulatesAndPreservesAllTurns() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val captureEngine = FakeAudioCaptureEngine()
        val playbackEngine = FakeStreamAudioPlaybackEngine()
        val mockClient = MockLiveAiClient(dispatcher = testDispatcher).apply { speechDelayMs = 0L }

        val manager = LiveCoachSessionManager(
            audioCaptureEngine = captureEngine,
            playbackEngine = playbackEngine,
            liveAiClient = mockClient,
            dispatcher = testDispatcher
        )

        val config = LiveScenarioConfig(
            scenarioId = "speaking.restaurant",
            title = "Restaurant Ordering",
            systemPrompt = "Act as the waiter."
        )

        manager.startSession(config)
        advanceUntilIdle()

        // Turn 1: Initial Coach greeting was already generated by startSession
        val initialTranscript = manager.transcript.value
        assertEquals(1, initialTranscript.size)
        assertEquals(TranscriptRole.COACH, initialTranscript[0].role)
        assertTrue(initialTranscript[0].text.contains("FluentAI coach"))
        assertTrue(initialTranscript[0].isFinal)

        // Turn 2: User answers
        mockClient.emitEvent(LiveSessionEvent.InterimInputTranscription("Yes, a table for "))
        mockClient.emitEvent(LiveSessionEvent.InputTranscription("Yes, a table for two please."))
        advanceUntilIdle()

        // Turn 3: Coach replies
        mockClient.emitEvent(LiveSessionEvent.OutputTranscription("Right this way. Here is your menu."))
        mockClient.emitEvent(LiveSessionEvent.GenerationComplete)
        mockClient.emitEvent(LiveSessionEvent.TurnComplete)
        advanceUntilIdle()

        // Turn 4: User answers again
        mockClient.emitEvent(LiveSessionEvent.InputTranscription("Thank you very much."))
        advanceUntilIdle()

        val transcript = manager.transcript.value
        assertEquals(4, transcript.size)
        assertEquals(TranscriptRole.COACH, transcript[0].role)
        assertTrue(transcript[0].text.contains("FluentAI coach"))
        assertTrue(transcript[0].isFinal)

        assertEquals(TranscriptRole.USER, transcript[1].role)
        assertEquals("Yes, a table for two please.", transcript[1].text)
        assertTrue(transcript[1].isFinal)

        assertEquals(TranscriptRole.COACH, transcript[2].role)
        assertTrue(transcript[2].text.contains("Right this way"))
        assertTrue(transcript[2].isFinal)

        assertEquals(TranscriptRole.USER, transcript[3].role)
        assertEquals("Thank you very much.", transcript[3].text)
        assertTrue(transcript[3].isFinal)
    }

    @Test
    fun interimThenFinalInput_updatesOneUserTurnAndFinalizesItOnce() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val playbackEngine = FakeStreamAudioPlaybackEngine()
        val mockClient = MockLiveAiClient(dispatcher = testDispatcher).apply { speechDelayMs = 0L }
        val manager = LiveCoachSessionManager(
            audioCaptureEngine = FakeAudioCaptureEngine(),
            playbackEngine = playbackEngine,
            liveAiClient = mockClient,
            dispatcher = testDispatcher
        )

        manager.startSession(testConfig())
        advanceUntilIdle()

        mockClient.emitEvent(LiveSessionEvent.InterimInputTranscription("Hello, how are"))
        advanceUntilIdle()
        assertEquals(1, manager.transcript.value.count { it.role == TranscriptRole.USER })
        assertFalse(manager.transcript.value.last().isFinal)

        mockClient.emitEvent(LiveSessionEvent.InputTranscription("Hello, how are you?"))
        advanceUntilIdle()

        val userTurns = manager.transcript.value.filter { it.role == TranscriptRole.USER }
        assertEquals(1, userTurns.size)
        assertEquals("Hello, how are you?", userTurns.single().text)
        assertTrue(userTurns.single().isFinal)
    }

    @Test
    fun repeatedFinalInput_isIdempotentWithinOneTurn() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val mockClient = MockLiveAiClient(dispatcher = testDispatcher).apply { speechDelayMs = 0L }
        val manager = LiveCoachSessionManager(
            audioCaptureEngine = FakeAudioCaptureEngine(),
            playbackEngine = FakeStreamAudioPlaybackEngine(),
            liveAiClient = mockClient,
            dispatcher = testDispatcher
        )

        manager.startSession(testConfig())
        advanceUntilIdle()
        mockClient.emitEvent(LiveSessionEvent.InputTranscription("Yes, I'm ready."))
        mockClient.emitEvent(LiveSessionEvent.InputTranscription("Yes, I'm ready."))
        advanceUntilIdle()

        assertEquals(
            listOf("Yes, I'm ready."),
            manager.transcript.value.filter { it.role == TranscriptRole.USER }.map { it.text }
        )
    }

    @Test
    fun identicalInputInTwoConversationTurns_isPreservedTwice() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val mockClient = MockLiveAiClient(dispatcher = testDispatcher).apply { speechDelayMs = 0L }
        val manager = LiveCoachSessionManager(
            audioCaptureEngine = FakeAudioCaptureEngine(),
            playbackEngine = FakeStreamAudioPlaybackEngine(),
            liveAiClient = mockClient,
            dispatcher = testDispatcher
        )

        manager.startSession(testConfig())
        advanceUntilIdle()
        mockClient.emitEvent(LiveSessionEvent.InputTranscription("Yes, I'm ready."))
        mockClient.emitEvent(LiveSessionEvent.OutputTranscription("Great, let's begin."))
        mockClient.emitEvent(LiveSessionEvent.TurnComplete)
        mockClient.emitEvent(LiveSessionEvent.InputTranscription("Yes, I'm ready."))
        advanceUntilIdle()

        assertEquals(
            listOf("Yes, I'm ready.", "Yes, I'm ready."),
            manager.transcript.value.filter { it.role == TranscriptRole.USER }.map { it.text }
        )
    }

    @Test
    fun outputFragmentsAndCumulativeSnapshots_mergeIntoOneCoachTurnUntilTurnComplete() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val playbackEngine = FakeStreamAudioPlaybackEngine()
        val mockClient = MockLiveAiClient(dispatcher = testDispatcher).apply { speechDelayMs = 0L }
        val manager = LiveCoachSessionManager(
            audioCaptureEngine = FakeAudioCaptureEngine(),
            playbackEngine = playbackEngine,
            liveAiClient = mockClient,
            dispatcher = testDispatcher
        )

        manager.startSession(testConfig())
        advanceUntilIdle()
        val initialCoachCount = manager.transcript.value.count { it.role == TranscriptRole.COACH }

        mockClient.emitEvent(LiveSessionEvent.OutputTranscription("Would you like"))
        mockClient.emitEvent(LiveSessionEvent.OutputTranscription(" to see the"))
        mockClient.emitEvent(LiveSessionEvent.OutputTranscription("Would you like to see the dinner"))
        mockClient.emitEvent(LiveSessionEvent.OutputTranscription(" dinner menu?"))
        mockClient.emitEvent(LiveSessionEvent.GenerationComplete)
        advanceUntilIdle()

        val activeCoach = manager.transcript.value.filter { it.role == TranscriptRole.COACH }.last()
        assertEquals(initialCoachCount + 1, manager.transcript.value.count { it.role == TranscriptRole.COACH })
        assertEquals("Would you like to see the dinner menu?", activeCoach.text)
        assertFalse(activeCoach.isFinal)

        mockClient.emitEvent(LiveSessionEvent.TurnComplete)
        mockClient.emitEvent(LiveSessionEvent.TurnComplete)
        advanceUntilIdle()

        val coachTurns = manager.transcript.value.filter { it.role == TranscriptRole.COACH }
        assertEquals(initialCoachCount + 1, coachTurns.size)
        assertTrue(coachTurns.last().isFinal)
        assertEquals(1, playbackEngine.finishTurnCallCount)
    }

    @Test
    fun interrupted_finalizesPartialCoachOnceAndFlushesPlayback() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val playbackEngine = FakeStreamAudioPlaybackEngine()
        val mockClient = MockLiveAiClient(dispatcher = testDispatcher).apply { speechDelayMs = 0L }
        val manager = LiveCoachSessionManager(
            audioCaptureEngine = FakeAudioCaptureEngine(),
            playbackEngine = playbackEngine,
            liveAiClient = mockClient,
            dispatcher = testDispatcher
        )

        manager.startSession(testConfig())
        advanceUntilIdle()
        val initialCoachCount = manager.transcript.value.count { it.role == TranscriptRole.COACH }
        mockClient.emitEvent(LiveSessionEvent.OutputTranscription("This is a partial response"))
        mockClient.emitEvent(LiveSessionEvent.Interrupted)
        mockClient.emitEvent(LiveSessionEvent.Interrupted)
        advanceUntilIdle()

        val coachTurns = manager.transcript.value.filter { it.role == TranscriptRole.COACH }
        assertEquals(initialCoachCount + 1, coachTurns.size)
        assertEquals("This is a partial response", coachTurns.last().text)
        assertTrue(coachTurns.last().isFinal)
        assertTrue(playbackEngine.stopAndFlushCallCount >= 1)
    }

    @Test
    fun pauseAndResume_updatesStateAndCaptureLifecycle() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val captureEngine = FakeAudioCaptureEngine()
        val playbackEngine = FakeStreamAudioPlaybackEngine()
        val mockClient = MockLiveAiClient(dispatcher = testDispatcher).apply { speechDelayMs = 0L }

        val manager = LiveCoachSessionManager(
            audioCaptureEngine = captureEngine,
            playbackEngine = playbackEngine,
            liveAiClient = mockClient,
            dispatcher = testDispatcher
        )

        manager.startSession(
            LiveScenarioConfig(
                scenarioId = "speaking.test",
                title = "Test",
                systemPrompt = "Coach"
            )
        )
        advanceUntilIdle()

        manager.pauseSession()
        advanceUntilIdle()
        assertEquals(VoiceUiState.PAUSED, manager.voiceState.value)
        assertFalse(captureEngine.isRecording.value)

        manager.resumeSession()
        advanceUntilIdle()
        assertEquals(VoiceUiState.LISTENING, manager.voiceState.value)
        assertTrue(captureEngine.isRecording.value)

        manager.endSession()
        advanceUntilIdle()
        assertEquals(VoiceUiState.ENDED, manager.voiceState.value)
        assertFalse(captureEngine.isRecording.value)
    }

    private fun testConfig() = LiveScenarioConfig(
        scenarioId = "speaking.test",
        title = "Test",
        systemPrompt = "Coach"
    )
}
