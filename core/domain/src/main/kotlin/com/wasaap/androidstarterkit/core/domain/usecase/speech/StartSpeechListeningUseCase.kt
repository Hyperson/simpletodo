package com.wasaap.androidstarterkit.core.domain.usecase.speech

import com.wasaap.androidstarterkit.core.domain.repository.SpeechRepository
import javax.inject.Inject

class StartSpeechListeningUseCase @Inject constructor(
    private val speechRepository: SpeechRepository
) {
    operator fun invoke() = speechRepository.startListening()
}