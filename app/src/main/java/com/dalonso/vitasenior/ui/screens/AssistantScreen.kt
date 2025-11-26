package com.dalonso.vitasenior.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dalonso.vitasenior.ui.viewmodel.AssistantState
import com.dalonso.vitasenior.ui.viewmodel.ChatMessage

@Composable
fun AssistantScreen(
    messages: List<ChatMessage>,
    assistantState: AssistantState,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit,
    onBackClick: () -> Unit,
    onSpeakRequest: (String) -> Unit
) {
    val listState = rememberLazyListState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                onStartListening()
            } else {
                onSpeakRequest("Necesito permiso del micrófono para escucharte")
            }
        }
    )

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (assistantState is AssistantState.Listening) 1.2f else 1.0f,
        animationSpec = tween(500),
        label = "micScale"
    )

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
                    text = "Asistente Virtual",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.clickable { onSpeakRequest("Soy tu asistente virtual") }
                )
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { message ->
                ChatBubble(message)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = when (assistantState) {
                    is AssistantState.Listening -> "Escuchando..."
                    is AssistantState.Processing -> "Procesando..."
                    else -> "Toca para hablar"
                },
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            FloatingActionButton(
                onClick = {
                    if (assistantState is AssistantState.Listening) {
                        onStopListening()
                    } else {
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                },
                modifier = Modifier
                    .size(80.dp)
                    .scale(scale),
                containerColor = if (assistantState is AssistantState.Listening) Color.Red else MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = if (assistantState is AssistantState.Listening) Icons.Default.Stop else Icons.Default.Mic,
                    contentDescription = "Hablar",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val alignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart
    val color = if (message.isUser) MaterialTheme.colorScheme.primaryContainer else Color.White
    val textColor = if (message.isUser) MaterialTheme.colorScheme.onPrimaryContainer else Color.Black
    val shape = if (message.isUser)
        RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp)
    else
        RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Surface(
            color = color,
            shape = shape,
            shadowElevation = 2.dp,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(16.dp),
                color = textColor,
                fontSize = 16.sp
            )
        }
    }
}

