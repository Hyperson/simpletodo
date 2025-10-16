package com.wasaap.androidstarterkit.core.speech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.wasaap.androidstarterkit.core.common.network.Dispatcher
import com.wasaap.androidstarterkit.core.common.network.TodoDispatchers
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidSpeechRecognizerManager @Inject constructor(
    @ApplicationContext context: Context,
    @param:Dispatcher(TodoDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : SpeechRecognizerManager {

    private val _recognizedText = MutableStateFlow("")
    override val recognizedText: StateFlow<String> = _recognizedText

    private val _isListening = MutableStateFlow(false)
    override val isListening: StateFlow<Boolean> = _isListening

    private val recognizer: SpeechRecognizer =
        SpeechRecognizer.createSpeechRecognizer(context)

    private val coroutineScope = CoroutineScope(SupervisorJob() + ioDispatcher)

    private val listener = object : RecognitionListener {

        override fun onReadyForSpeech(params: Bundle?) {
            _isListening.value = true
        }

        override fun onEndOfSpeech() {
            _isListening.value = false
        }

        override fun onResults(results: Bundle?) {
            val text = results
                ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                ?.firstOrNull() ?: ""

            coroutineScope.launch {
                _recognizedText.emit(text)
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {
            val text = partialResults
                ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                ?.firstOrNull()

            if (!text.isNullOrBlank()) {
                coroutineScope.launch { _recognizedText.emit(text) }
            }
        }

        override fun onError(error: Int) {
            _isListening.value = false
        }

        // Unused callbacks
        override fun onBeginningOfSpeech() {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEvent(eventType: Int, params: Bundle?) {}
        override fun onRmsChanged(rmsdB: Float) {}
    }

    init {
        recognizer.setRecognitionListener(listener)
    }

    override fun startListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)

//            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 8000L)// wait 8 s after silence
//            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 8000L)
//            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 3000L)// at least 3 s active
        }

        recognizer.startListening(intent)
    }
}