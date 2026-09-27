package com.seanora.fluentai

import com.seanora.fluentai.ai.live.LiveCoachSessionManager
import com.seanora.fluentai.ai.live.LiveScenarioConfig
import com.seanora.fluentai.ai.live.LiveSessionEvent
import com.seanora.fluentai.ai.live.MockLiveAiClient
import com.seanora.fluentai.ai.live.TranscriptRole
import com.seanora.fluentai.ai.live.VoiceUiState
import com.seanora.fluentai.core.audio.AudioChunk
import com.seanora.fluentai.core.audio.FakeAudioCaptureEngine
import com.seanora.fluentai.core.audio.FakeStreamAudioPlaybackEngine
import com.seanora.fluentai.feature.speaking.SpeakingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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

/**
 * Phase 5 Exit Verification & Integrity Test.
 * Validates the complete real-time speaking loop against Phase 5 exit criteria:
 * - Start a session
 * - Speak naturally (microphone capture)
 * - Hear streaming response (playback engine chunks)
 * - Interrupt AI speech (barge-in flush & state transition)
 * - Continue conversation (transcript and turn taking)
 * - See transcript (real-time streaming dialogue entries)
 * - End session cleanly (resource release and state reset)
 */
@OptIn(ExperimentalCoroutinesApi::class)
class Phase5IntegrityTest {

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
    fun completeVoiceCoachLifecycle_satisfiesAllPhase5ExitCriteria() = runTest(testDispatcher) {
        val captureEngine = FakeAudioCaptureEngine()
        val playbackEngine = FakeStreamAudioPlaybackEngine()
        val mockLiveAiClient = MockLiveAiClient(dispatcher = testDispatcher).apply {
            speechDelayMs = 0L
        }

        val sessionManager = LiveCoachSessionManager(
            audioCaptureEngine = captureEngine,
            playbackEngine = playbackEngine,
            liveAiClient = mockLiveAiClient,
            dispatcher = testDispatcher
        )

        val viewModel = SpeakingViewModel(sessionManager)

        // 1. Initial State: IDLE and inactive
        val initialState = viewModel.uiState.value
        assertEquals(VoiceUiState.IDLE, initialState.voiceState)
        assertFalse(initialState.isSessionActive)
        assertFalse(captureEngine.isRecording.value)
        assertFalse(playbackEngine.isPlaying.value)

        // 2. Start Session (Quarterly Business Review Scenario)
        val scenario = LiveScenarioConfig(
            scenarioId = "speaking.b2.tech.review",
            title = "Quarterly Business Review: Tech Migration",
            systemPrompt = "Lead the review.",
            cefrTarget = "C1"
        )
        viewModel.startSession(scenario)
        advanceUntilIdle()

        // Verify active session & microphone capture started
        val activeState = viewModel.uiState.value
        assertTrue(activeState.isSessionActive)
        assertTrue(captureEngine.isRecording.value)

        // Verify greeting transcript arrived
        assertTrue(activeState.transcript.isNotEmpty())
        val firstTurn = activeState.transcript.first()
        assertEquals(TranscriptRole.COACH, firstTurn.role)
        assertTrue(firstTurn.text.isNotEmpty())

        // 3. User Speaks Naturally (User Mic Input Stream)
        captureEngine.emitChunk(
            AudioChunk(
                data = byteArrayOf(0x0A, 0x0B, 0x0C, 0x0D),
                normalizedAmplitude = 0.55f
            )
        )
        advanceUntilIdle()
        assertEquals(0.55f, captureEngine.amplitudeFlow.value, 0.01f)

        // 4. Coach Speaks Streaming Audio Response
        mockLiveAiClient.emitEvent(LiveSessionEvent.AudioOutputChunk(byteArrayOf(0x10, 0x20, 0x30)))
        advanceUntilIdle()
        assertEquals(VoiceUiState.COACH_SPEAKING, viewModel.uiState.value.voiceState)
        assertTrue(playbackEngine.writtenChunks.isNotEmpty())

        // 5. Natural Barge-In Interruption (User cuts in while Coach is speaking)
        assertEquals(0, playbackEngine.stopAndFlushCallCount)
        viewModel.interruptCoach()
        advanceUntilIdle()

        // Verify instant flush and immediate state switch to USER_SPEAKING
        assertEquals(VoiceUiState.USER_SPEAKING, viewModel.uiState.value.voiceState)
        assertTrue(playbackEngine.stopAndFlushCallCount >= 1)
        assertFalse(playbackEngine.isPlaying.value)

        // 6. User continues conversation
        viewModel.sendTextMessage("We will migrate the database in phases to reduce downtime.")
        advanceUntilIdle()

        val updatedTranscript = viewModel.uiState.value.transcript
        val userTurn = updatedTranscript.find { it.role == TranscriptRole.USER }
        assertNotNull(userTurn)
        assertTrue(userTurn!!.text.contains("migrate the database"))

        // 7. Pause and Resume session
        viewModel.togglePause()
        advanceUntilIdle()
        assertEquals(VoiceUiState.PAUSED, viewModel.uiState.value.voiceState)
        assertFalse(captureEngine.isRecording.value)

        viewModel.togglePause()
        advanceUntilIdle()
        assertEquals(VoiceUiState.LISTENING, viewModel.uiState.value.voiceState)
        assertTrue(captureEngine.isRecording.value)

        // 8. End Session Cleanly
        viewModel.endSession()
        advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertEquals(VoiceUiState.ENDED, finalState.voiceState)
        assertFalse(finalState.isSessionActive)
        assertFalse(captureEngine.isRecording.value)
    }

    @Test
    fun voiceStatePill_supportsAllElevenAgentsMdStates() {
        val allStates = VoiceUiState.values()
        assertEquals(11, allStates.size)

        val expectedStates = setOf(
            "IDLE", "CONNECTING", "LISTENING", "USER_SPEAKING",
            "PROCESSING", "COACH_SPEAKING", "PAUSED", "RECONNECTING",
            "ENDING", "ENDED", "ERROR"
        )
        assertEquals(expectedStates, allStates.map { it.name }.toSet())
    }
}
