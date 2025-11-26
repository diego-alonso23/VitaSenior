package com.dalonso.vitasenior.ui.viewmodel

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

sealed class AssistantState {
    object Idle : AssistantState()
    object Listening : AssistantState()
    data class Processing(val text: String) : AssistantState()
    data class Error(val message: String) : AssistantState()
}


data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

class AssistantViewModel(application: Application) : AndroidViewModel(application) {

    private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(application)

    private val _state = MutableStateFlow<AssistantState>(AssistantState.Idle)
    val state: StateFlow<AssistantState> = _state.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(listOf(
        ChatMessage("Hola, soy Vita. ¿En qué puedo ayudarte hoy?", false)
    ))
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()


    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent.asStateFlow()

    fun startListening() {
        _state.value = AssistantState.Listening
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() { _state.value = AssistantState.Processing("Procesando...") }

            override fun onError(error: Int) {
                _state.value = AssistantState.Idle
                addMessage("No te escuché bien, ¿puedes repetir?", false)
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val text = matches?.firstOrNull() ?: ""
                processCommand(text)
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        speechRecognizer.startListening(intent)
    }

    fun stopListening() {
        speechRecognizer.stopListening()
        _state.value = AssistantState.Idle
    }

    private fun processCommand(command: String) {
        _state.value = AssistantState.Idle
        addMessage(command, true)

        val lowerCommand = command.lowercase()

        when {

            lowerCommand.contains("ejercicio") || lowerCommand.contains("entrenar") -> {
                addMessage("Abriendo la sección de ejercicios...", false)
                _navigationEvent.value = "exercises"
            }

            lowerCommand.contains("progreso") || lowerCommand.contains("logro") || lowerCommand.contains("estadística") -> {
                addMessage("Vamos a ver cómo vas. Abriendo progreso.", false)
                _navigationEvent.value = "progress"
            }

            lowerCommand.contains("recordatorio") || lowerCommand.contains("alarma") || lowerCommand.contains("medicina") -> {
                addMessage("Abriendo tus recordatorios.", false)
                _navigationEvent.value = "reminders"
            }

            lowerCommand.contains("consejo") || lowerCommand.contains("tip") -> {
                addMessage("Aquí tienes algunos consejos saludables.", false)
                _navigationEvent.value = "tips"
            }

            lowerCommand.contains("hola") || lowerCommand.contains("ayuda") -> {
                addMessage("Puedes decirme 'Ir a ejercicios', 'Ver mi progreso' o 'Crear recordatorio'.", false)
            }
            else -> {
                addMessage("Lo siento, no entendí eso. Intenta decir 'Ir a ejercicios' o 'Ver recordatorios'.", false)
            }
        }
    }

    private fun addMessage(text: String, isUser: Boolean) {
        val newList = _messages.value.toMutableList()
        newList.add(ChatMessage(text, isUser))
        _messages.value = newList
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }

    override fun onCleared() {
        super.onCleared()
        speechRecognizer.destroy()
    }
}
