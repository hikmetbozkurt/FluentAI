package com.seanora.fluentai.core.audio

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AudioCaptureEngineTest {

    @Test
    fun startCapture_transitionsIsRecordingToTrue() = runTest {
        val engine = FakeAudioCaptureEngine()
        assertFalse(engine.isRecording.value)

        val result = engine.startCapture()
        assertTrue(result.isSuccess)
        assertTrue(engine.isRecording.value)

        engine.stopCapture()
        assertFalse(engine.isRecording.value)
    }

    @Test
    fun startCapture_handlesFailureCorrectly() = runTest {
        val engine = FakeAudioCaptureEngine().apply {
            shouldFailStart = true
        }

        val result = engine.startCapture()
        assertTrue(result.isFailure)
        assertFalse(engine.isRecording.value)
    }

    @Test
    fun emitChunk_updatesAmplitudeAndEmitsData() = runTest {
        val engine = FakeAudioCaptureEngine()
        engine.startCapture()

        val sampleChunk = AudioChunk(
            data = byteArrayOf(0x00, 0x10, 0x20, 0x30),
            normalizedAmplitude = 0.75f
        )

        engine.emitChunk(sampleChunk)

        assertEquals(0.75f, engine.amplitudeFlow.value, 0.001f)
        val emitted = engine.audioChunks.first()
        assertEquals(sampleChunk, emitted)
    }

    @Test
    fun stopCapture_resetsAmplitudeToZero() = runTest {
        val engine = FakeAudioCaptureEngine()
        engine.startCapture()
        engine.setAmplitude(0.8f)
        assertEquals(0.8f, engine.amplitudeFlow.value, 0.001f)

        engine.stopCapture()
        assertEquals(0f, engine.amplitudeFlow.value, 0.001f)
        assertFalse(engine.isRecording.value)
    }
}
