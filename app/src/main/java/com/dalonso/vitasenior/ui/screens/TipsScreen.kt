package com.dalonso.vitasenior.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.EmojiFoodBeverage
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class Tip(
    val title: String,
    val description: String,
    val category: String,
    val icon: ImageVector,
    val color: Color
)

val tipsList = listOf(
    Tip(
        "Hidratación Constante",
        "Beba agua frecuentemente, incluso si no tiene sed. La deshidratación es común y puede causar mareos.",
        "Salud",
        Icons.Default.LocalDrink,
        Color(0xFF2196F3)
    ),
    Tip(
        "Prevención de Caídas",
        "Retire alfombras sueltas y asegúrese de tener buena iluminación en pasillos y baños para evitar tropezones.",
        "Seguridad",
        Icons.Default.Lightbulb,
        Color(0xFFFFC107)
    ),
    Tip(
        "Alimentación Balanceada",
        "Incluya más fibra en su dieta (frutas, verduras) para mejorar la digestión y mantener la energía.",
        "Nutrición",
        Icons.Default.EmojiFoodBeverage,
        Color(0xFF4CAF50)
    ),
    Tip(
        "Actividad Mental",
        "Realice crucigramas, lea o aprenda algo nuevo cada día para mantener su mente ágil y activa.",
        "Bienestar",
        Icons.Default.SelfImprovement,
        Color(0xFF9C27B0)
    ),
    Tip(
        "Sueño Reparador",
        "Intente acostarse y levantarse a la misma hora. Evite pantallas antes de dormir para descansar mejor.",
        "Descanso",
        Icons.Default.Bedtime,
        Color(0xFF3F51B5)
    ),
    Tip(
        "Cuidado del Corazón",
        "Camine al menos 20 minutos al día. El ejercicio moderado fortalece su corazón y mejora la circulación.",
        "Cardio",
        Icons.Default.Favorite,
        Color(0xFFE91E63)
    )
)


@Composable
fun TipsScreen(
    onBackClick: () -> Unit,
    onSpeakRequest: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primary.copy(alpha = 0.8f))
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver al menú principal",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Consejos Saludables",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.clickable { onSpeakRequest("Consejos Saludables") }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Pequeños cambios para vivir mejor",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.padding(start = 56.dp)
                        .clickable { onSpeakRequest("Pequeños cambios para vivir mejor") }
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(tipsList) { tip ->
                TipCard(tip, onSpeakRequest)
            }
        }
    }
}

@Composable
fun TipCard(
    tip: Tip,
    onSpeak: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSpeak("Consejo de ${tip.category}: ${tip.title}. ${tip.description}")
            }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = CircleShape,
                color = tip.color.copy(alpha = 0.1f),
                modifier = Modifier.size(64.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = tip.icon,
                        contentDescription = null,
                        tint = tip.color,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))


            Column {
                Surface(
                    color = tip.color.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Text(
                        text = tip.category.uppercase(),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        color = tip.color,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Text(
                    text = tip.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = tip.description,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}

