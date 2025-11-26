package com.dalonso.vitasenior.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dalonso.vitasenior.data.local.Reminder

@Composable
fun RemindersScreen(
    remindersList: List<Reminder>,
    onAddReminder: (String, String, String) -> Unit,
    onDeleteReminder: (Reminder) -> Unit,
    onBackClick: () -> Unit,
    onSpeakRequest: (String) -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog = true
                    onSpeakRequest("Agregar nuevo recordatorio")
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.size(70.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar", modifier = Modifier.size(32.dp))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.background(Color.LightGray.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Mis Recordatorios",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onSpeakRequest("Tus recordatorios guardados") }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (remindersList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No tienes recordatorios.\nPulsa el botón + para agregar uno.",
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontSize = 18.sp,
                        color = Color.Gray,
                        modifier = Modifier.clickable { onSpeakRequest("No tienes recordatorios. Pulsa el botón más para agregar uno.") }
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(remindersList) { reminder ->
                        ReminderCard(reminder, onDeleteReminder, onSpeakRequest)
                    }
                }
            }
        }

        if (showDialog) {
            AddReminderDialog(
                onDismiss = { showDialog = false },
                onConfirm = { title, time, type ->
                    onAddReminder(title, time, type)
                    showDialog = false
                },
                onSpeakRequest = onSpeakRequest
            )
        }
    }
}

@Composable
fun ReminderCard(
    reminder: Reminder,
    onDelete: (Reminder) -> Unit,
    onSpeak: (String) -> Unit
) {
    val (icon, color) = when (reminder.type) {
        "MEDICINE" -> Pair(Icons.Default.MedicalServices, Color(0xFFE91E63))
        "EXERCISE" -> Pair(Icons.Default.FitnessCenter, Color(0xFF4CAF50))
        else -> Pair(Icons.Default.Notifications, Color(0xFFFF9800))
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth().clickable {
            onSpeak("Recordatorio: ${reminder.title}, a las ${reminder.time}")
        }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = color.copy(alpha = 0.15f),
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = reminder.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "Hora: ${reminder.time}", fontSize = 16.sp, color = Color.Gray)
            }

            IconButton(onClick = { onDelete(reminder) }) {
                Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.Gray)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
    onSpeakRequest: (String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("GENERAL") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Recordatorio") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("¿Qué recordar?") },
                    placeholder = { Text("Ej: Pastilla presión") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Hora") },
                    placeholder = { Text("Ej: 09:00 AM") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Tipo:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = selectedType == "MEDICINE",
                        onClick = { selectedType = "MEDICINE"; onSpeakRequest("Medicamento") },
                        label = { Text("Medicamento") },
                        leadingIcon = { if (selectedType == "MEDICINE") Icon(Icons.Default.MedicalServices, null) else null }
                    )
                    FilterChip(
                        selected = selectedType == "EXERCISE",
                        onClick = { selectedType = "EXERCISE"; onSpeakRequest("Ejercicio") },
                        label = { Text("Ejercicio") },
                        leadingIcon = { if (selectedType == "EXERCISE") Icon(Icons.Default.FitnessCenter, null) else null }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotEmpty() && time.isNotEmpty()) {
                        onConfirm(title, time, selectedType)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

