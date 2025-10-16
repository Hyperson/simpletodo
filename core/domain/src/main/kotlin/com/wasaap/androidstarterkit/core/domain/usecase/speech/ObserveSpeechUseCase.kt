package com.wasaap.androidstarterkit.core.domain.usecase.speech

import com.wasaap.androidstarterkit.core.domain.repository.SpeechRepository
import com.wasaap.androidstarterkit.core.model.SpeechState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveSpeechUseCase @Inject constructor(
    private val speechRepository: SpeechRepository
) {
    operator fun invoke(): Flow<SpeechState> =
        combine(
            speechRepository.recognizedText,
            speechRepository.isListening
        ) { text, listening ->
            SpeechState(
                recognizedText = text,
                isListening = listening
            )
        }
}