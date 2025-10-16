package com.wasaap.androidstarterkit.core.speech.di

import com.wasaap.androidstarterkit.core.speech.AndroidSpeechRecognizerManager
import com.wasaap.androidstarterkit.core.speech.SpeechRecognizerManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SpeechModule {

    @Binds
    @Singleton
    abstract fun bindSpeechRecognizerManager(
        impl: AndroidSpeechRecognizerManager
    ): SpeechRecognizerManager
}