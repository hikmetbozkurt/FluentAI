package com.seanora.fluentai.core.audio

import android.content.Context
import android.net.Uri
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.PlaybackException
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Media3AudioPlaybackEngine @Inject constructor(
    @ApplicationContext private val context: Context
) : AudioPlaybackEngine {

    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var progressJob: Job? = null

    private val player: ExoPlayer by lazy {
        ExoPlayer.Builder(context).build().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(C.AUDIO_CONTENT_TYPE_SPEECH)
                    .setUsage(C.USAGE_MEDIA)
                    .build(),
                true,
            )
            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _playbackState.update { it.copy(isPlaying = isPlaying) }
                    if (isPlaying) {
                        startProgressUpdates()
                    } else {
                        stopProgressUpdates()
                    }
                }

                override fun onPlaybackStateChanged(state: Int) {
                    when (state) {
                        Player.STATE_READY -> {
                            _playbackState.update {
                                it.copy(
                                    durationMs = duration.coerceAtLeast(0L),
                                    currentPositionMs = currentPosition.coerceAtLeast(0L)
                                )
                            }
                        }
                        Player.STATE_ENDED -> {
                            _playbackState.update {
                                it.copy(
                                    isPlaying = false,
                                    isCompleted = true,
                                    currentPositionMs = duration.coerceAtLeast(0L)
                                )
                            }
                            stopProgressUpdates()
                        }
                        else -> Unit
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    stopProgressUpdates()
                    _playbackState.update {
                        it.copy(
                            isPlaying = false,
                            errorMessage = "This lesson's audio is not available on this device. You can still use the transcript and questions."
                        )
                    }
                }
            })
        }
    }

    private val _playbackState = MutableStateFlow(PlaybackState())
    override val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    override fun play(uri: String, startPositionMs: Long) {
        val parsedUri = if (uri.startsWith("audio/") || !uri.contains("://")) {
            Uri.parse("asset:///$uri")
        } else {
            Uri.parse(uri)
        }

        val mediaItem = MediaItem.fromUri(parsedUri)
        player.setMediaItem(mediaItem)
        player.prepare()
        if (startPositionMs > 0) {
            player.seekTo(startPositionMs)
        }
        player.play()

        _playbackState.update {
            it.copy(
                currentMediaUri = uri,
                isCompleted = false,
                errorMessage = null,
                currentPositionMs = startPositionMs
            )
        }
    }

    override fun pause() {
        player.pause()
    }

    override fun resume() {
        player.play()
    }

    override fun seekTo(positionMs: Long) {
        player.seekTo(positionMs)
        _playbackState.update { it.copy(currentPositionMs = positionMs) }
    }

    override fun stop() {
        player.stop()
        stopProgressUpdates()
        _playbackState.update {
            it.copy(
                isPlaying = false,
                currentPositionMs = 0L,
                errorMessage = null
            )
        }
    }

    override fun release() {
        stopProgressUpdates()
        player.release()
    }

    private fun startProgressUpdates() {
        stopProgressUpdates()
        progressJob = scope.launch {
            while (isActive) {
                if (player.isPlaying) {
                    _playbackState.update {
                        it.copy(
                            currentPositionMs = player.currentPosition.coerceAtLeast(0L),
                            durationMs = player.duration.coerceAtLeast(0L)
                        )
                    }
                }
                delay(200)
            }
        }
    }

    private fun stopProgressUpdates() {
        progressJob?.cancel()
        progressJob = null
    }
}
