package com.wasaap.androidstarterkit.core.model

data class SpeechState(
    val recognizedText: String = "",
    val isListening: Boolean = false
)