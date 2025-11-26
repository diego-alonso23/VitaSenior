package com.dalonso.vitasenior

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dalonso.vitasenior.ui.screens.*
import com.dalonso.vitasenior.ui.theme.LocalButtonScale
import com.dalonso.vitasenior.ui.theme.VitaSeniorTheme
import com.dalonso.vitasenior.ui.viewmodel.AccessibilityViewModel
import com.dalonso.vitasenior.ui.viewmodel.AssistantViewModel
import com.dalonso.vitasenior.ui.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        setContent {
            VitaSeniorTheme {
                val context = LocalContext.current
                val navController = rememberNavController()

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        if (!isGranted) {
                            Toast.makeText(context, "Las notificaciones son importantes para los recordatorios", Toast.LENGTH_LONG).show()
                        }
                    }
                )

                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }

                val accessViewModel: AccessibilityViewModel = viewModel()
                val userViewModel: UserViewModel = viewModel()
                val uiState by accessViewModel.uiState.collectAsState()

                val currentDensity = LocalDensity.current
                val customDensity = Density(
                    density = currentDensity.density,
                    fontScale = uiState.fontSizeScale
                )

                CompositionLocalProvider(
                    LocalDensity provides customDensity,
                    LocalButtonScale provides uiState.buttonScale
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                if (uiState.isColorBlindMode) {
                                    colorFilter = ColorFilter.colorMatrix(uiState.daltonismoType.matrix)
                                }
                            },
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavHost(navController = navController, startDestination = "splash") {

                            composable("splash") {
                                val isLoading by userViewModel.isLoading.collectAsState()
                                val user by userViewModel.currentUser.collectAsState()

                                LaunchedEffect(isLoading, user) {
                                    if (!isLoading) {
                                        if (user != null) {
                                            navController.navigate("home") { popUpTo("splash") { inclusive = true } }
                                        } else {
                                            navController.navigate("accessibility") { popUpTo("splash") { inclusive = true } }
                                        }
                                    }
                                }
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator()
                                }
                            }

                            composable(
                                route = "accessibility?fromSettings={fromSettings}",
                                arguments = listOf(navArgument("fromSettings") {
                                    type = NavType.BoolType
                                    defaultValue = false
                                })
                            ) { backStackEntry ->
                                val fromSettings = backStackEntry.arguments?.getBoolean("fromSettings") ?: false

                                AccessibilityScreen(
                                    state = uiState,
                                    onFontSizeChanged = { accessViewModel.updateFontSize(it) },
                                    onColorBlindChanged = { accessViewModel.toggleColorBlindMode(it) },
                                    onDaltonismoTypeChanged = { accessViewModel.setDaltonismoType(it) },
                                    onTtsChanged = { accessViewModel.toggleTts(it) },
                                    onButtonSizeChanged = { accessViewModel.updateButtonSize(it) },
                                    onSpeakRequest = { accessViewModel.speakText(it) },
                                    isSettingsMode = fromSettings,
                                    onBackClick = {
                                        if (fromSettings) navController.popBackStack()
                                    },
                                    onSave = {
                                        accessViewModel.saveSettings()
                                        navController.navigate("register")
                                    }
                                )
                            }

                            composable("settings") {
                                val user by userViewModel.currentUser.collectAsState()
                                SettingsScreen(
                                    userName = user?.name ?: "Usuario",
                                    onBackClick = { navController.popBackStack() },
                                    onNavigateTo = { route ->
                                        if (route == "accessibility") {
                                            navController.navigate("accessibility?fromSettings=true")
                                        } else {
                                            navController.navigate(route)
                                        }
                                    },

                                    onSpeakRequest = { accessViewModel.speakText(it) }
                                )
                            }

                            composable("register") {
                                RegistroScreen(
                                    onRegisterClick = { n, e, num ->
                                        userViewModel.registerUser(n, e, num) {
                                            Toast.makeText(context, "¡Registro exitoso!", Toast.LENGTH_SHORT).show()
                                            accessViewModel.speakText("Registro exitoso, bienvenido $n")
                                            navController.navigate("home") { popUpTo("splash") { inclusive = true } }
                                        }
                                    },
                                    onSpeakRequest = { accessViewModel.speakText(it) }
                                )
                            }

                            composable("home") {
                                val user by userViewModel.currentUser.collectAsState()
                                val nameToShow = user?.name ?: "Usuario"
                                val photoUri = user?.profilePictureUri
                                val emergencyNumber = user?.emergencyNumber ?: "112"

                                HomeScreen(
                                    userName = nameToShow,
                                    userPhotoUri = photoUri,
                                    onOptionSelected = { navController.navigate(it) },
                                    onSpeakRequest = { accessViewModel.speakText(it) },
                                    onEmergencyClick = {
                                        accessViewModel.speakText("Llamando a emergencias")
                                        val intent = Intent(Intent.ACTION_DIAL).apply {
                                            data = "tel:$emergencyNumber".toUri()
                                        }
                                        context.startActivity(intent)
                                    }
                                )
                            }

                            composable("exercises") {
                                ExercisesListScreen(
                                    onExerciseClick = { exerciseId -> navController.navigate("exercise_detail/$exerciseId") },
                                    onBackClick = { navController.popBackStack() },
                                    onSpeakRequest = { accessViewModel.speakText(it) }
                                )
                            }

                            composable(
                                route = "exercise_detail/{exerciseId}",
                                arguments = listOf(navArgument("exerciseId") { type = NavType.IntType })
                            ) { backStackEntry ->
                                val exerciseId = backStackEntry.arguments?.getInt("exerciseId") ?: 0
                                ExerciseDetailScreen(
                                    exerciseId = exerciseId,
                                    onBack = { navController.popBackStack() },
                                    onSpeakRequest = { accessViewModel.speakText(it) },
                                    onMarkComplete = { title ->
                                        userViewModel.markExerciseAsCompleted(exerciseId, title)
                                        Toast.makeText(context, "¡Bien hecho! Guardado.", Toast.LENGTH_SHORT).show()
                                        accessViewModel.speakText("¡Felicidades! Ejercicio completado.")
                                        navController.popBackStack()
                                    }
                                )
                            }

                            composable("progress") {
                                val count by userViewModel.completedCount.collectAsState()
                                val history by userViewModel.recentLogs.collectAsState()

                                ProgressScreen(
                                    onSpeakRequest = { accessViewModel.speakText(it) },
                                    onBackClick = { navController.popBackStack() },
                                    completedExercisesCount = count,
                                    recentHistory = history
                                )
                            }

                            composable("reminders") {
                                val reminders by userViewModel.reminders.collectAsState()

                                RemindersScreen(
                                    remindersList = reminders,
                                    onAddReminder = { title, time, type ->
                                        userViewModel.addReminder(title, time, type)
                                        accessViewModel.speakText("Recordatorio programado para las $time")
                                    },
                                    onDeleteReminder = { reminder ->
                                        userViewModel.deleteReminder(reminder)
                                        accessViewModel.speakText("Recordatorio eliminado")
                                    },
                                    onBackClick = { navController.popBackStack() },
                                    onSpeakRequest = { accessViewModel.speakText(it) }
                                )
                            }

                            composable("tips") {
                                TipsScreen(
                                    onBackClick = { navController.popBackStack() },
                                    onSpeakRequest = { accessViewModel.speakText(it) }
                                )
                            }

                            composable("assistant") {
                                val assistantViewModel: AssistantViewModel = viewModel()
                                val messages by assistantViewModel.messages.collectAsState()
                                val assistantState by assistantViewModel.state.collectAsState()
                                val navEvent by assistantViewModel.navigationEvent.collectAsState()

                                LaunchedEffect(navEvent) {
                                    navEvent?.let { route ->
                                        navController.navigate(route)
                                        assistantViewModel.clearNavigationEvent()
                                    }
                                }

                                AssistantScreen(
                                    messages = messages,
                                    assistantState = assistantState,
                                    onStartListening = { assistantViewModel.startListening() },
                                    onStopListening = { assistantViewModel.stopListening() },
                                    onBackClick = { navController.popBackStack() },
                                    onSpeakRequest = { accessViewModel.speakText(it) }
                                )
                            }

                            composable("edit_profile") {
                                val user by userViewModel.currentUser.collectAsState()

                                EditProfileScreen(
                                    currentUser = user,
                                    onBackClick = { navController.popBackStack() },
                                    onSaveClick = { name, number, photoUri ->
                                        userViewModel.updateUserProfile(name, number, photoUri) {
                                            Toast.makeText(context, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                                            accessViewModel.speakText("Datos actualizados correctamente")
                                            navController.popBackStack()
                                        }
                                    },
                                    onSpeakRequest = { accessViewModel.speakText(it) }
                                )
                            }

                            composable("login") { PlaceholderScreen("Login") }
                        }
                    }
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Recordatorios VitaSenior"
            val descriptionText = "Canal para avisos de medicación y ejercicios"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("vita_channel_id", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Pantalla de $title")
    }
}