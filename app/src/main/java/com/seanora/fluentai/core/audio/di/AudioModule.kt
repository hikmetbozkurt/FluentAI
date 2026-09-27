package com.seanora.fluentai.core.audio.di

import com.seanora.fluentai.core.audio.AndroidAudioCaptureEngine
import com.seanora.fluentai.core.audio.AndroidStreamAudioPlaybackEngine
import com.seanora.fluentai.core.audio.AudioCaptureEngine
import com.seanora.fluentai.core.audio.AudioPlaybackEngine
import com.seanora.fluentai.core.audio.Media3AudioPlaybackEngine
import com.seanora.fluentai.core.audio.StreamAudioPlaybackEngine
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AudioModule {

    @Binds
    @Singleton
    abstract fun bindAudioPlaybackEngine(
        impl: Media3AudioPlaybackEngine
    ): AudioPlaybackEngine

    @Binds
    @Singleton
    abstract fun bindAudioCaptureEngine(
        impl: AndroidAudioCaptureEngine
    ): AudioCaptureEngine

    @Binds
    @Singleton
    abstract fun bindStreamAudioPlaybackEngine(
        impl: AndroidStreamAudioPlaybackEngine
    ): StreamAudioPlaybackEngine
}
