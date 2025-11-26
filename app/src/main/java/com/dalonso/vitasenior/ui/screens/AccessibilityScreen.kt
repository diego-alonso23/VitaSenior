package com.dalonso.vitasenior.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dalonso.vitasenior.ui.viewmodel.AccessibilityUiState
import com.dalonso.vitasenior.ui.viewmodel.DaltonismoType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessibilityScreen(
    state: AccessibilityUiState,
    onFontSizeChanged: (Float) -> Unit,
    onColorBlindChanged: (Boolean) -> Unit,
    onDaltonismoTypeChanged: (DaltonismoType) -> Unit,
    onTtsChanged: (Boolean) -> Unit,
    onButtonSizeChanged: (Float) -> Unit,
    onSpeakRequest: (String) -> Unit,
    onSave: () -> Unit,
    isSettingsMode: Boolean = false,
    onBackClick: () -> Unit = {}
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    val currentSliderValue = ((state.buttonScale - 0.8f) / 0.6f).coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        if (isSettingsMode) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.background(Color.LightGray.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text("Ajustes de Accesibilidad", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
        } else {
            Text(
                text = "Accesibilidad",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Configure su aplicación para las necesidades que requiera",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        Text(
            text = "Tamaño de texto",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("A", style = MaterialTheme.typography.bodySmall)
            Slider(
                value = state.fontSizeScale,
                onValueChange = { onFontSizeChanged(it) },
                valueRange = 0.8f..1.4f,
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
            )
            Text("A", style = MaterialTheme.typography.headlineMedium)
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text(
                text = "Así se verá el texto en la aplicación.",
                modifier = Modifier.padding(16.dp),
                fontSize = 16.sp * state.fontSizeScale
            )
        }

        Text(
            text = "Daltonismo",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 8.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = state.isColorBlindMode,
                onClick = { onColorBlindChanged(true) },
                label = { Text("Si") }
            )
            FilterChip(
                selected = !state.isColorBlindMode,
                onClick = { onColorBlindChanged(false) },
                label = { Text("No") }
            )
        }

        if (state.isColorBlindMode) {
            Box(modifier = Modifier.padding(vertical = 16.dp)) {
                ElevatedAssistChip(
                    onClick = { dropdownExpanded = true },
                    label = { Text(state.daltonismoType.label) }
                )
                DropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }
                ) {
                    DaltonismoType.values().filter { it != DaltonismoType.NINGUNO }.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.label) },
                            onClick = { onDaltonismoTypeChanged(type); dropdownExpanded = false }
                        )
                    }
                }
            }
        }

        Text(
            text = "Lectura de texto",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 8.dp)
        )
        FilterChip(
            selected = state.isTtsEnabled,
            onClick = { onTtsChanged(!state.isTtsEnabled) },
            label = { Text(if (state.isTtsEnabled) "Activado" else "Desactivado") }
        )

        Text(
            text = "Tamaño de Botones",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth().height(100.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Button(
                    onClick = { onSpeakRequest("Botón de prueba") },
                    modifier = Modifier.scale(state.buttonScale)
                ) {
                    Text("Prueba")
                }
            }

            Spacer(Modifier.width(24.dp))

            Slider(
                value = currentSliderValue,
                onValueChange = { onButtonSizeChanged(it) },
                modifier = Modifier
                    .width(50.dp)
                    .fillMaxHeight()
                    .rotate(270f)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (!isSettingsMode) {
            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            ) {
                Text("Continuar")
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null)
            }
        }
    }
}