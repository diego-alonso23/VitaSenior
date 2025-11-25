package com.dalonso.vitasenior.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dalonso.vitasenior.ui.theme.VitaSeniorTheme


data class MenuOption(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val route: String
)

@Composable
fun HomeScreen(
    userName: String,
    onOptionSelected: (String) -> Unit,
    onSpeakRequest: (String) -> Unit
) {

    val options = listOf(
        MenuOption("Ejercicios", Icons.Default.DirectionsRun, Color(0xFF4CAF50), "exercises"),
        MenuOption("Progreso", Icons.Default.TrendingUp, Color(0xFF2196F3), "progress"),
        MenuOption("Asistente", Icons.Default.Face, Color(0xFFFF9800), "assistant"),
        MenuOption("Recordatorios", Icons.Default.Notifications, Color(0xFFE91E63), "reminders"),
        MenuOption("Consejos", Icons.Default.Lightbulb, Color(0xFF9C27B0), "tips")
    )

    Column(
        modifier = Modifier.Companion
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Hola, $userName",
            fontSize = 28.sp,
            fontWeight = FontWeight.Companion.Bold,
            modifier = Modifier.Companion
                .padding(bottom = 8.dp)
                .clickable { onSpeakRequest("Hola, bienvenido a Vita Senior") }
        )
        Text(
            text = "Hola, $userName",
            fontSize = 18.sp,
            color = Color.Companion.Gray,
            modifier = Modifier.Companion
                .padding(bottom = 24.dp)
                .clickable { onSpeakRequest("¿Qué te gustaría hacer hoy?") }
        )


        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2 columnas
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(options) { option ->
                MenuOptionCard(
                    option = option,
                    onClick = {
                        onSpeakRequest("Entrando a ${option.title}")
                        onOptionSelected(option.route)
                    }
                )
            }
        }
    }
}

@Composable
fun MenuOptionCard(
    option: MenuOption,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = option.color.copy(alpha = 0.15f)),
        modifier = Modifier.Companion
            .height(160.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Companion.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Surface(
                shape = androidx.compose.foundation.shape.RoundedCornerShape(50),
                color = option.color.copy(alpha = 0.2f),
                modifier = Modifier.Companion.size(64.dp)
            ) {
                Box(contentAlignment = Alignment.Companion.Center) {
                    Icon(
                        imageVector = option.icon,
                        contentDescription = null,
                        tint = option.color,
                        modifier = Modifier.Companion.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.Companion.height(12.dp))

            // Texto
            Text(
                text = option.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Companion.SemiBold,
                textAlign = TextAlign.Companion.Center,
                color = Color.Companion.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    VitaSeniorTheme {
        val nameToShow = "Pepito"
        HomeScreen(
            userName = nameToShow,
            onOptionSelected = {},
            onSpeakRequest = {}
        )
    }
}