package com.dalonso.vitasenior.ui.screens

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dalonso.vitasenior.ui.theme.VitaSeniorTheme


data class MenuOption(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val route: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    userName: String,
    userPhotoUri: String?,
    onOptionSelected: (String) -> Unit,
    onSpeakRequest: (String) -> Unit,
    onEmergencyClick: () -> Unit
) {
    val context = LocalContext.current
    val firstName = userName.split(" ").firstOrNull() ?: "Usuario"

    val options = listOf(
        MenuOption("Ejercicios", Icons.Default.DirectionsRun, Color(0xFF4CAF50), "exercises"),
        MenuOption("Progreso", Icons.Default.TrendingUp, Color(0xFF2196F3), "progress"),
        MenuOption("Asistente", Icons.Default.Face, Color(0xFFFF9800), "assistant"),
        MenuOption("Recordatorios", Icons.Default.Notifications, Color(0xFFE91E63), "reminders"),
        MenuOption("Consejos", Icons.Default.Lightbulb, Color(0xFF9C27B0), "tips"),
        MenuOption("Ajustes", Icons.Default.Settings, Color(0xFF607D8B), "settings")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Hola, $firstName",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onSpeakRequest("Hola, $firstName, bienvenido a Vita Senior") }
                )
                Text(
                    text = "¿Qué haremos hoy?",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    modifier = Modifier.clickable { onSpeakRequest("¿Qué te gustaría hacer hoy?") }
                )
            }

            if (userPhotoUri != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(userPhotoUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Tu foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                        .clickable { onOptionSelected("settings") }
                )
            } else {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    modifier = Modifier
                        .size(60.dp)
                        .clickable { onOptionSelected("settings") }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }


        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            modifier = Modifier.weight(2f)
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

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFFD32F2F),
                    shadowElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .combinedClickable(
                            onClick = {
                                onSpeakRequest("Mantén presionado para llamar a emergencias")
                                Toast.makeText(context, "Mantén presionado para llamar", Toast.LENGTH_SHORT).show()
                            },
                            onLongClick = {
                                onEmergencyClick()
                            }
                        )
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "SOS EMERGENCIA",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Mantener presionado",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
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
        modifier = Modifier
            .height(160.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = option.color.copy(alpha = 0.2f),
                modifier = Modifier.size(64.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = option.icon,
                        contentDescription = null,
                        tint = option.color,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = option.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }
    }
}