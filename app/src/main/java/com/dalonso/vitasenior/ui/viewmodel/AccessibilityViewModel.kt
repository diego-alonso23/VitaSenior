package com.dalonso.vitasenior.ui.viewmodel

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.compose.ui.graphics.ColorMatrix
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

enum class DaltonismoType(val label: String, val matrix: ColorMatrix) {
    NINGUNO("Normal", ColorMatrix()),
    PROTANOPIA("Protanopia (Rojo)",
        ColorMatrix(
            floatArrayOf(
                0.567f,
                0.433f,
                0.0f,
                0.0f,
                0.0f,
                0.558f,
                0.442f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.242f,
                0.758f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                1.0f,
                0.0f
            )
        )
    ),
    DEUTERANOPIA("Deuteranopia (Verde)",
        ColorMatrix(
            floatArrayOf(
                0.625f,
                0.375f,
                0.0f,
                0.0f,
                0.0f,
                0.7f,
                0.3f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.3f,
                0.7f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                1.0f,
                0.0f
            )
        )
    ),
    TRITANOPIA("Tritanopia (Azul)",
        ColorMatrix(
            floatArrayOf(
                0.95f,
                0.05f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.433f,
                0.567f,
                0.0f,
                0.0f,
                0.0f,
                0.475f,
                0.525f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                1.0f,
                0.0f
            )
        )
    )
}


data class AccessibilityUiState(
    val fontSizeScale: Float = 1.0f,
    val isColorBlindMode: Boolean = false,
    val daltonismoType: DaltonismoType = DaltonismoType.NINGUNO,
    val isTtsEnabled: Boolean = false,
    val buttonScale: Float = 1.0f
)


class AccessibilityViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    private val _uiState = MutableStateFlow(AccessibilityUiState())
    val uiState: StateFlow<AccessibilityUiState> = _uiState.asStateFlow()

    private var tts: TextToSpeech? = null

    init {
        tts = TextToSpeech(application, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale("es", "ES")
        }
    }

    fun speakText(text: String) {
        if (_uiState.value.isTtsEnabled) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun updateFontSize(newScale: Float) {
        _uiState.update { it.copy(fontSizeScale = newScale) }
        speakText("Tamaño de texto ajustado")
    }

    fun toggleColorBlindMode(isEnabled: Boolean) {
        _uiState.update {
            it.copy(
                isColorBlindMode = isEnabled,
                daltonismoType = if (isEnabled) DaltonismoType.PROTANOPIA else DaltonismoType.NINGUNO
            )
        }
        speakText(if (isEnabled) "Modo daltonismo activado" else "Modo daltonismo desactivado")
    }

    fun setDaltonismoType(type: DaltonismoType) {
        _uiState.update { it.copy(daltonismoType = type) }
        speakText("Filtro ${type.label} seleccionado")
    }

    fun toggleTts(isEnabled: Boolean) {
        _uiState.update { it.copy(isTtsEnabled = isEnabled) }
        val message = if (isEnabled) "Lectura de texto activada" else "Lectura de texto desactivada"
        tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun updateButtonSize(sliderValue: Float) {
        val scale = 0.8f + (sliderValue * 0.7f)
        _uiState.update { it.copy(buttonScale = scale) }
    }

    fun saveSettings() {
        speakText("Guardando configuración y continuando")
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
    }
}