package com.dalonso.vitasenior

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dalonso.vitasenior.ui.theme.VitaSeniorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessibilityScreen(onRegisterClick: () -> Unit, onLoginClick: () -> Unit) {

    // --- Estados para controlar los componentes ---

    // Estado para el slider (0.0f a 1.0f). Empezamos en 0.5f (medio)
    var sliderPosition by remember { mutableFloatStateOf(0.5f) }

    // Estado para los chips de Daltonismo ("Si" o "No")
    var daltonismoSelection by remember { mutableStateOf<String?>("Si") }

    // Estado para el chip de Lectura de texto (Activado/Desactivado)
    var lecturaActivada by remember { mutableStateOf(true) }

    // --- Estructura de la Pantalla ---

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Permite deslizar si el contenido es muy largo
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- Título ---
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

        // --- Sección Tamaño de texto ---
        Text(
            text = "Tamaño de texto",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text(text = "Texto de ejemplo", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.width(16.dp))
            Surface( // Fondo para el número "14"
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                Text(
                    text = "14",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        // --- Sección Daltonismo ---
        Text(
            text = "Daltonismo",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            // Chip "Si"
            FilterChip(
                selected = daltonismoSelection == "Si",
                onClick = { daltonismoSelection = "Si" },
                label = { Text("Si") },
                // Usamos los colores por defecto de FilterChip de M3
            )
            // Chip "No"
            FilterChip(
                selected = daltonismoSelection == "No",
                onClick = { daltonismoSelection = "No" },
                label = { Text("No") },
            )
        }

        // Chip "Tipo de daltonismo"
        ElevatedAssistChip(
            onClick = { /* TODO: Abrir selector de tipo */ },
            label = { Text("Tipo de daltonismo") },
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // --- Sección Lectura de texto ---
        Text(
            text = "Lectura de texto",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        FilterChip(
            selected = lecturaActivada,
            onClick = { lecturaActivada = !lecturaActivada },
            label = { Text(if (lecturaActivada) "Activado" else "Desactivado") },
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // --- Sección Tamaño de Botones ---
        Text(
            text = "Tamaño de Botones",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min), // Hace que ambos elementos tengan la misma altura
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // Lógica para escalar el botón
            // Mapeamos el valor del slider (0f-1f) a una escala (ej. 0.8f a 1.2f)
            val buttonScale = 0.8f + (sliderPosition * 0.4f)

            // Botón "Aceptar"
            Button(
                onClick = { /* ... */ },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight() // Ocupa la altura definida por IntrinsicSize.Min
                    // Aplicamos la escala dinámicamente
                    .graphicsLayer {
                        scaleX = buttonScale
                        scaleY = buttonScale
                    }
            ) {
                Text("Aceptar")
            }

            Spacer(Modifier.width(24.dp))

            // Slider vertical
            Slider(
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                modifier = Modifier
                    .weight(0.5f) // Le da espacio relativo
                    .fillMaxHeight() // Ocupa la altura
                    .rotate(270f) // ¡El truco para hacerlo vertical!
            )
        }

        // --- Botón de Continuar ---
        Button(
            onClick = { /* TODO: Acción de continuar */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 150.dp, bottom = 16.dp) // Espacio arriba y abajo
        ) {
            Text("Continue")
            Spacer(Modifier.width(8.dp)) // Espacio entre texto e ícono
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Continuar"
            )
        }
    }
}

// --- Función de Previsualización ---
// Esta función te permite ver tu Composable en la pestaña "Split" o "Design"
@Preview(showBackground = true, widthDp = 380, heightDp = 800)
@Composable
fun PreviewAccessibilityScreen() {
    // Es **esencial** envolver tu preview en un MaterialTheme
    // para que se apliquen los colores, formas y tipografías correctas de M3.
    // Reemplaza "YourAppTheme" con el nombre real de tu tema si tienes uno.
    VitaSeniorTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

        }
    }
}