package com.seanora.fluentai.core.audio

import kotlinx.coroutines.flow.StateFlow

data class PlaybackState(
    val isPlaying: Boolean = false,
    val currentPositionMs: Long = 0L,
    val durationMs: Long = 0L,
    val currentMediaUri: String? = null,
    val isCompleted: Boolean = false,
    val errorMessage: String? = null
)

interface AudioPlaybackEngine {
    val playbackState: StateFlow<PlaybackState>
    fun play(uri: String, startPositionMs: Long = 0L)
    fun pause()
    fun resume()
    fun seekTo(positionMs: Long)
    fun stop()
    fun release()
}
