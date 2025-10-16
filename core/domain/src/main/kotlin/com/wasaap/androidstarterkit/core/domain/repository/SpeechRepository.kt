package com.wasaap.androidstarterkit.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface SpeechRepository {
    val recognizedText: Flow<String>
    val isListening: Flow<Boolean>
    fun startListening()
}