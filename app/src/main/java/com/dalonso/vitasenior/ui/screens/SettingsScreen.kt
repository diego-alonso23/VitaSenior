package com.dalonso.vitasenior.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(
    userName: String,
    onBackClick: () -> Unit,
    onNavigateTo: (String) -> Unit,
    onSpeakRequest: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
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
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Configuración",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.clickable { onSpeakRequest("Pantalla de Configuración") }
                    )
                }
            }
        }


        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = userName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onSpeakRequest("Usuario: $userName") }
                    )
                    Text(
                        text = "Editar perfil",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            onSpeakRequest("Editar perfil")
                            onNavigateTo("edit_profile")
                        }
                    )
                }

                IconButton(onClick = { onNavigateTo("edit_profile") }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Gray)
                }
            }
        }


        Column(modifier = Modifier.padding(horizontal = 16.dp)) {

            SectionTitle("General", onSpeakRequest)

            SettingsItem(
                title = "Accesibilidad",
                subtitle = "Tamaño de letra, daltonismo, voz",
                icon = Icons.Default.AccessibilityNew,
                color = Color(0xFF4CAF50),
                onClick = { onNavigateTo("accessibility") },
                onSpeak = onSpeakRequest
            )

            SettingsItem(
                title = "Notificaciones",
                subtitle = "Gestionar avisos y alarmas",
                icon = Icons.Default.Notifications,
                color = Color(0xFFFF9800),
                onClick = { onSpeakRequest("Configuración de notificaciones no disponible aún") },
                onSpeak = onSpeakRequest
            )

            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle("Soporte", onSpeakRequest)

            SettingsItem(
                title = "Ayuda y Soporte",
                subtitle = "Preguntas frecuentes",
                icon = Icons.Default.Help,
                color = Color(0xFF2196F3),
                onClick = { onNavigateTo("tips") },
                onSpeak = onSpeakRequest
            )

            SettingsItem(
                title = "Privacidad",
                subtitle = "Términos y condiciones",
                icon = Icons.Default.PrivacyTip,
                color = Color(0xFF9C27B0),
                onClick = { onSpeakRequest("Información de privacidad") },
                onSpeak = onSpeakRequest
            )

            Spacer(modifier = Modifier.height(32.dp))

        }
    }
}

@Composable
fun SectionTitle(title: String, onSpeak: (String) -> Unit) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier
            .padding(bottom = 8.dp)
            .clickable { onSpeak("Sección $title") }
    )
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    onSpeak: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable {
                onSpeak("Opción: $title. $subtitle")
                onClick()
            }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = color.copy(alpha = 0.1f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = color)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text(text = subtitle, fontSize = 14.sp, color = Color.Gray)
            }

            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

