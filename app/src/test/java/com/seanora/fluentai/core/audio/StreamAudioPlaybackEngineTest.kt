package com.seanora.fluentai.core.audio

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StreamAudioPlaybackEngineTest {

    @Test
    fun writeChunk_marksPlayingAndStoresData() = runTest {
        val engine = FakeStreamAudioPlaybackEngine()
        assertFalse(engine.isPlaying.value)

        val chunk = byteArrayOf(0x01, 0x02, 0x03, 0x04)
        engine.writeChunk(chunk)

        assertTrue(engine.isPlaying.value)
        assertEquals(1, engine.writtenChunks.size)
        assertTrue(engine.amplitudeFlow.value > 0f)
    }

    @Test
    fun stopAndFlush_immediatelyHaltsPlaybackAndClearsQueue() = runTest {
        val engine = FakeStreamAudioPlaybackEngine()
        engine.writeChunk(byteArrayOf(0x10, 0x20))
        engine.writeChunk(byteArrayOf(0x30, 0x40))
        assertEquals(2, engine.writtenChunks.size)
        assertTrue(engine.isPlaying.value)

        engine.stopAndFlush()

        assertFalse(engine.isPlaying.value)
        assertEquals(0f, engine.amplitudeFlow.value, 0.001f)
        assertEquals(0, engine.writtenChunks.size)
        assertEquals(1, engine.stopAndFlushCallCount)
    }

    @Test
    fun simulatePlaybackComplete_resetsPlayingState() = runTest {
        val engine = FakeStreamAudioPlaybackEngine()
        engine.writeChunk(byteArrayOf(0x11, 0x22))
        assertTrue(engine.isPlaying.value)

        engine.simulatePlaybackComplete()
        assertFalse(engine.isPlaying.value)
        assertEquals(0f, engine.amplitudeFlow.value, 0.001f)
    }

    @Test
    fun pcmPlaybackBuffer_coalescesTinyChunksWithoutChangingByteOrder() {
        val buffer = PcmPlaybackBuffer(prebufferBytes = 6, minimumWriteBytes = 4)

        assertTrue(buffer.offer(byteArrayOf(1, 2)).isEmpty())
        assertTrue(buffer.offer(byteArrayOf(3, 4)).isEmpty())
        assertArrayEquals(byteArrayOf(1, 2, 3, 4, 5, 6), buffer.offer(byteArrayOf(5, 6)).single())
        assertTrue(buffer.offer(byteArrayOf(7, 8)).isEmpty())
        assertArrayEquals(byteArrayOf(7, 8, 9, 10), buffer.offer(byteArrayOf(9, 10)).single())
        assertTrue(buffer.drainTail().isEmpty())
    }

    @Test
    fun pcmPlaybackBuffer_drainsShortTurnTailAndClearDropsStaleBytes() {
        val buffer = PcmPlaybackBuffer(prebufferBytes = 8, minimumWriteBytes = 4)

        buffer.offer(byteArrayOf(1, 2, 3, 4))
        assertArrayEquals(byteArrayOf(1, 2, 3, 4), buffer.drainTail().single())

        buffer.offer(byteArrayOf(5, 6))
        buffer.clear()
        assertTrue(buffer.drainTail().isEmpty())
    }

    @Test
    fun pcmPlaybackBuffer_rebuffersAfterMidTurnStarvation() {
        val buffer = PcmPlaybackBuffer(prebufferBytes = 6, minimumWriteBytes = 2)

        assertArrayEquals(byteArrayOf(1, 2, 3, 4, 5, 6), buffer.offer(byteArrayOf(1, 2, 3, 4, 5, 6)).single())
        buffer.restartPrebuffering()

        assertTrue(buffer.offer(byteArrayOf(7, 8)).isEmpty())
        assertTrue(buffer.offer(byteArrayOf(9, 10)).isEmpty())
        assertArrayEquals(byteArrayOf(7, 8, 9, 10, 11, 12), buffer.offer(byteArrayOf(11, 12)).single())
    }

    @Test
    fun writePcmFully_retriesPartialWritesUntilEveryByteIsWritten() {
        val offsets = mutableListOf<Int>()
        val lengths = mutableListOf<Int>()

        val written = writePcmFully(byteArrayOf(1, 2, 3, 4, 5, 6)) { _, offset, length ->
            offsets += offset
            lengths += length
            minOf(2, length)
        }

        assertEquals(6, written)
        assertEquals(listOf(0, 2, 4), offsets)
        assertEquals(listOf(6, 4, 2), lengths)
    }
}
