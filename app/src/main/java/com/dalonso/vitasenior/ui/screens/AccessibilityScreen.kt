package com.dalonso.vitasenior.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dalonso.vitasenior.ui.viewmodel.AccessibilityUiState
import com.dalonso.vitasenior.ui.viewmodel.DaltonismoType
import com.dalonso.vitasenior.ui.theme.VitaSeniorTheme

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
    onSave: () -> Unit
) {
    var dropdownExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.Companion.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier.Companion
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .graphicsLayer {
                    if (state.isColorBlindMode) {
                        colorFilter = ColorFilter.Companion.colorMatrix(state.daltonismoType.matrix)
                    }
                },
            horizontalAlignment = Alignment.Companion.CenterHorizontally
        ) {


            Text(
                text = "Accesibilidad",
                fontSize = 24.sp,
                fontWeight = FontWeight.Companion.Bold,
                modifier = Modifier.Companion
                    .padding(bottom = 8.dp)
                    .clickable { onSpeakRequest("Pantalla de Accesibilidad") }
            )

            Text(
                text = "Configure su aplicación para las necesidades que requiera",
                textAlign = TextAlign.Companion.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.Companion
                    .padding(bottom = 24.dp)
                    .clickable { onSpeakRequest("Configure su aplicación para las necesidades que requiera") }
            )

            // --- 1. Tamaño de Texto ---
            Text(
                text = "Tamaño de texto",
                fontSize = 18.sp,
                fontWeight = FontWeight.Companion.Bold,
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .clickable { onSpeakRequest("Sección Tamaño de texto") }
            )
            Row(
                verticalAlignment = Alignment.Companion.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.Companion.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Texto de ejemplo",
                    fontSize = 16.sp * state.fontSizeScale,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.Companion.clickable { onSpeakRequest("Así se escucha el texto de ejemplo") }
                )
                Spacer(modifier = Modifier.Companion.width(16.dp))
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ) {
                    Row {
                        IconButton(onClick = { onFontSizeChanged(1.0f) }) { Text("A") }
                        IconButton(onClick = { onFontSizeChanged(1.3f) }) { Text("A+") }
                    }
                }
            }

            // --- 2. Daltonismo ---
            Text(
                text = "Daltonismo",
                fontSize = 18.sp,
                fontWeight = FontWeight.Companion.Bold,
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .clickable { onSpeakRequest("Sección Daltonismo") }
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.Companion.padding(bottom = 16.dp)
            ) {
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
                Box(modifier = Modifier.Companion.padding(bottom = 24.dp)) {
                    ElevatedAssistChip(
                        onClick = { dropdownExpanded = true },
                        label = { Text(state.daltonismoType.label) },
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        }
                    )

                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        DaltonismoType.values().filter { it != DaltonismoType.NINGUNO }
                            .forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.label) },
                                    onClick = {
                                        onDaltonismoTypeChanged(type)
                                        dropdownExpanded = false
                                    }
                                )
                            }
                    }
                }
            } else {
                Spacer(modifier = Modifier.Companion.height(24.dp))
            }

            // --- 3. Lectura de texto ---
            Text(
                text = "Lectura de texto",
                fontSize = 18.sp,
                fontWeight = FontWeight.Companion.Bold,
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .clickable { onSpeakRequest("Sección Lectura de texto. Actívelo para escuchar la interfaz.") }
            )
            FilterChip(
                selected = state.isTtsEnabled,
                onClick = { onTtsChanged(!state.isTtsEnabled) },
                label = { Text(if (state.isTtsEnabled) "Activado" else "Desactivado") },
                modifier = Modifier.Companion.padding(bottom = 24.dp)
            )

            // --- 4. Tamaño de Botones ---
            Text(
                text = "Tamaño de Botones",
                fontSize = 18.sp,
                fontWeight = FontWeight.Companion.Bold,
                modifier = Modifier.Companion.fillMaxWidth().padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .height(150.dp),
                verticalAlignment = Alignment.Companion.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { onSpeakRequest("Botón de ejemplo Aceptar") },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.Companion
                        .weight(1f)
                        .graphicsLayer {
                            scaleX = state.buttonScale
                            scaleY = state.buttonScale
                        }
                ) {
                    Text("Aceptar")
                }

                Spacer(Modifier.Companion.width(24.dp))

                val sliderProgress = (state.buttonScale - 0.8f) / 0.7f
                Slider(
                    value = sliderProgress.coerceIn(0f, 1f),
                    onValueChange = { onButtonSizeChanged(it) },
                    modifier = Modifier.Companion
                        .width(150.dp)
                        .rotate(270f)
                )
            }

            Spacer(modifier = Modifier.Companion.weight(1f))

            Button(
                onClick = onSave,
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Continue")
                Spacer(Modifier.Companion.width(8.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = "Continuar")
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 380, heightDp = 800)
@Composable
fun PreviewAccessibilityScreen() {
    VitaSeniorTheme {
        AccessibilityScreen(
            state = AccessibilityUiState(
                isColorBlindMode = true,
                daltonismoType = DaltonismoType.PROTANOPIA
            ),
            onFontSizeChanged = {},
            onColorBlindChanged = {},
            onDaltonismoTypeChanged = {},
            onTtsChanged = {},
            onButtonSizeChanged = {},
            onSpeakRequest = {},
            onSave = {}
        )
    }
}