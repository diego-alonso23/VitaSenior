package com.dalonso.vitasenior.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dalonso.vitasenior.data.local.ExerciseLog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun ProgressScreen(
    onSpeakRequest: (String) -> Unit,
    onBackClick: () -> Unit,
    completedExercisesCount: Int,
    recentHistory: List<ExerciseLog>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .background(Color.LightGray.copy(alpha = 0.2f), CircleShape)
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver al menú principal",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Mi Progreso",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onSpeakRequest("Esta es tu pantalla de progreso") }
            )
        }


        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            StatCard(
                icon = Icons.Default.CheckCircle,
                value = completedExercisesCount.toString(),
                label = "Ejercicios",
                color = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f),
                onSpeak = onSpeakRequest
            )
            Spacer(modifier = Modifier.width(8.dp))
            StatCard(
                icon = Icons.Default.AccessTime,
                value = "${completedExercisesCount * 5}m",
                label = "Tiempo",
                color = Color(0xFF2196F3),
                modifier = Modifier.weight(1f),
                onSpeak = onSpeakRequest
            )
            Spacer(modifier = Modifier.width(8.dp))
            StatCard(
                icon = Icons.Default.LocalFireDepartment,
                value = "${completedExercisesCount * 25}",
                label = "Calorías",
                color = Color(0xFFFF9800),
                modifier = Modifier.weight(1f),
                onSpeak = onSpeakRequest
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Actividad Semanal",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .clickable { onSpeakRequest("Gráfico de tu actividad semanal") }
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .height(150.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {

                val days = listOf("D", "L", "M", "M", "J", "V", "S")

                val countsByDay = IntArray(7) { 0 }

                val calendar = Calendar.getInstance()
                recentHistory.forEach { log ->
                    calendar.timeInMillis = log.timestamp
                    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
                    if (dayOfWeek in 0..6) {
                        countsByDay[dayOfWeek]++
                    }
                }

                val maxCount = countsByDay.maxOrNull() ?: 1
                val maxScale = if (maxCount == 0) 1 else maxCount

                days.forEachIndexed { index, day ->
                    val barHeightRatio = (countsByDay[index].toFloat() / maxScale.toFloat()).coerceAtLeast(0.05f)
                    val isToday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1 == index
                    val barColor = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            val ejercicios = countsByDay[index]
                            onSpeakRequest("Día $day: hiciste $ejercicios ejercicios")
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .width(20.dp)
                                .fillMaxHeight(barHeightRatio)
                                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                                .background(barColor)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = day,
                            fontWeight = if(isToday) FontWeight.Bold else FontWeight.Normal,
                            color = if(isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Historial Reciente",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp).clickable { onSpeakRequest("Historial reciente") }
        )

        if (recentHistory.isEmpty()) {
            Text("Aún no has completado ejercicios.", color = Color.Gray, modifier = Modifier.padding(8.dp))
        } else {
            recentHistory.take(5).forEach { log ->
                val dateStr = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault()).format(Date(log.timestamp))
                AchievementItem(
                    title = "Completaste '${log.exerciseTitle}'",
                    date = dateStr,
                    onSpeak = onSpeakRequest
                )
            }
        }
    }
}


@Composable
fun StatCard(
    icon: ImageVector,
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
    onSpeak: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.15f)),
        modifier = modifier
            .height(110.dp)
            .clickable { onSpeak("Has completado $value $label") }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = label, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun AchievementItem(title: String, date: String, onSpeak: (String) -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clickable { onSpeak("$title, fecha: $date") }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFD700).copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFBC02D))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(text = date, color = Color.Gray, fontSize = 14.sp)
            }
        }
    }
}
