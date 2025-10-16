package com.wasaap.androidstarterkit.core.speech

import kotlinx.coroutines.flow.Flow

interface SpeechRecognizerManager {
    val recognizedText: Flow<String>
    val isListening: Flow<Boolean>
    fun startListening()
}