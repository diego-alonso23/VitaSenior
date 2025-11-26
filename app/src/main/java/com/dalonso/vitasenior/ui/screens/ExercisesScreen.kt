package com.dalonso.vitasenior.ui.screens

import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SelfImprovement
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
import androidx.compose.ui.viewinterop.AndroidView


data class Exercise(
    val id: Int,
    val title: String,
    val steps: List<String>,
    val videoId: String,
    val duration: String,
    val category: String,
    val color: Color
)


val exerciseList = listOf(
    Exercise(
        1, "Levantamiento de brazos",
        listOf(
            "Siéntese derecho en una silla con los pies apoyados en el suelo.",
            "Tome unas mancuernas ligeras o botellas de agua (opcional).",
            "Levante ambos brazos lentamente hacia el techo.",
            "Baje los brazos suavemente controlando el movimiento.",
            "Repita 10 veces."
        ),
        "5GCkAWIe6uU", "10 min", "Fuerza", Color(0xFF4CAF50)
    ),
    Exercise(
        2, "Rotación de cuello",
        listOf(
            "Siéntese o párese con la espalda recta y hombros relajados.",
            "Gire suavemente la cabeza hacia la derecha hasta sentir un leve estiramiento.",
            "Mantenga por 3 segundos.",
            "Gire suavemente hacia la izquierda.",
            "Repita 5 veces por lado sin forzar."
        ),
        "HKbeLfkdhec", "3 min", "Relax", Color(0xFF03A9F4)
    ),
    Exercise(
        3, "Extensión de piernas",
        listOf(
            "Siéntese en una silla firme, sin recostarse completamente.",
            "Extienda una pierna hacia adelante hasta que esté recta.",
            "Mantenga la posición durante 3 segundos apretando el muslo.",
            "Baje la pierna lentamente.",
            "Alterne con la otra pierna y repita 10 veces."
        ),
        "lxiC66EEI4E", "8 min", "Fuerza", Color(0xFFFF9800)
    ),
    Exercise(
        4, "Marcha en el sitio",
        listOf(
            "Póngase de pie cerca de una silla o pared para apoyarse si es necesario.",
            "Simule que está caminando sin moverse del lugar.",
            "Levante las rodillas alternadamente de forma rítmica.",
            "Mantenga la espalda recta y mire al frente.",
            "Continúe durante 2 minutos a un ritmo cómodo."
        ),
        "jc79zAuwmJI", "5 min", "Cardio", Color(0xFFE91E63)
    ),
    Exercise(
        5, "Flexiones en pared",
        listOf(
            "Párese frente a una pared a un brazo de distancia.",
            "Apoye las palmas de las manos en la pared a la altura de los hombros.",
            "Doble los codos lentamente acercando el pecho a la pared (sin tocarla).",
            "Empuje con las manos para volver a la posición inicial.",
            "Mantenga el cuerpo recto como una tabla."
        ),
        "SN1aCwLP5L4", "4 min", "Fuerza", Color(0xFF9C27B0)
    ),
    Exercise(
        6, "Estiramiento de hombros",
        listOf(
            "Sentado o de pie, eleve un brazo y crúcelo sobre el pecho.",
            "Use el otro brazo para sujetarlo y presionar suavemente hacia su cuerpo.",
            "Debe sentir el estiramiento en la parte posterior del hombro.",
            "Mantenga 15 segundos y cambie de brazo."
        ),
        "u7gVBBueHLE", "6 min", "Flexibilidad", Color(0xFF009688)
    )
)

@Composable
fun YoutubePlayer(videoId: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                settings.javaScriptEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                webChromeClient = WebChromeClient()
                webViewClient = WebViewClient()
                // El ?rel=0 evita que salgan videos sugeridos de otros canales al final
                loadData(
                    """<iframe width="100%" height="100%" src="https://www.youtube.com/embed/$videoId?rel=0" frameborder="0" allow="fullscreen"></iframe>""",
                    "text/html", "utf-8"
                )
            }
        }
    )
}

@Composable
fun ExercisesListScreen(
    onExerciseClick: (Int) -> Unit,
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
                        text = "Biblioteca de Ejercicios",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.clickable { onSpeakRequest("Biblioteca de Ejercicios") }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Mantente activo y saludable",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.padding(start = 56.dp)
                        .clickable { onSpeakRequest("Mantente activo y saludable") }
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(exerciseList) { exercise ->
                ExerciseCard(exercise, onExerciseClick, onSpeakRequest)
            }
        }
    }
}

@Composable
fun ExerciseCard(
    exercise: Exercise,
    onClick: (Int) -> Unit,
    onSpeak: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSpeak("Ejercicio: ${exercise.title}, duración ${exercise.duration}")
                onClick(exercise.id)
            }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = exercise.color.copy(alpha = 0.1f),
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if(exercise.category == "Relax") Icons.Default.SelfImprovement else Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = exercise.color,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exercise.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = exercise.duration, fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(12.dp))
                    Surface(
                        color = exercise.color.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = exercise.category,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 12.sp,
                            color = exercise.color,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.LightGray)
        }
    }
}

@Composable
fun ExerciseDetailScreen(
    exerciseId: Int,
    onBack: () -> Unit,
    onSpeakRequest: (String) -> Unit,
    onMarkComplete: (String) -> Unit
) {
    val exercise = exerciseList.find { it.id == exerciseId } ?: return

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onMarkComplete(exercise.title) },
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White,
                icon = { Icon(Icons.Default.CheckCircle, contentDescription = null) },
                text = { Text("MARCAR COMO COMPLETADO", fontWeight = FontWeight.Bold) },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .background(Color.LightGray.copy(alpha = 0.2f), CircleShape)
                        .size(48.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver atrás", tint = Color.DarkGray)
                }
            }

            // Video
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(220.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                YoutubePlayer(videoId = exercise.videoId)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Row {
                    SuggestionChip(
                        onClick = { onSpeakRequest(exercise.duration) },
                        label = { Text(exercise.duration) },
                        icon = { Icon(Icons.Default.AccessTime, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    SuggestionChip(
                        onClick = { onSpeakRequest(exercise.category) },
                        label = { Text(exercise.category) },
                        colors = SuggestionChipDefaults.suggestionChipColors(labelColor = exercise.color)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = exercise.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 34.sp,
                    modifier = Modifier.clickable { onSpeakRequest(exercise.title) }
                )

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Color.LightGray.copy(alpha = 0.3f))
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Instrucciones",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                exercise.steps.forEachIndexed { index, step ->
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .clickable { onSpeakRequest("Paso ${index + 1}: $step") }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = step,
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}