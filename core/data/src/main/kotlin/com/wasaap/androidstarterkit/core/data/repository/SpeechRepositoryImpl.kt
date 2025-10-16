package com.wasaap.androidstarterkit.core.data.repository

import com.wasaap.androidstarterkit.core.common.network.Dispatcher
import com.wasaap.androidstarterkit.core.common.network.TodoDispatchers
import com.wasaap.androidstarterkit.core.domain.repository.SpeechRepository
import com.wasaap.androidstarterkit.core.speech.SpeechRecognizerManager
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class SpeechRepositoryImpl @Inject constructor(
    private val speechManager: SpeechRecognizerManager,
    @param:Dispatcher(TodoDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : SpeechRepository {

    override val recognizedText: Flow<String> =
        speechManager.recognizedText
            .flowOn(ioDispatcher)

    override fun startListening() {
        speechManager.startListening()
    }

    override val isListening: Flow<Boolean> =
        speechManager.isListening.flowOn(ioDispatcher)
}