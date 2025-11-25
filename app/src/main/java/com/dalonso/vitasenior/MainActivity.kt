package com.dalonso.vitasenior

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dalonso.vitasenior.ui.screens.AccessibilityScreen
import com.dalonso.vitasenior.ui.screens.HomeScreen
import com.dalonso.vitasenior.ui.screens.RegistroScreen
import com.dalonso.vitasenior.ui.theme.VitaSeniorTheme
import com.dalonso.vitasenior.ui.viewmodel.AccessibilityViewModel
import com.dalonso.vitasenior.ui.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VitaSeniorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viewModel: AccessibilityViewModel = viewModel()
                    val uiState by viewModel.uiState.collectAsState()
                    val userViewModel: UserViewModel = viewModel()


                    NavHost(
                        navController = navController,
                        startDestination = "accessibility"
                    ) {

                        // --- 1. ACCESIBILIDAD ---
                        composable("accessibility") {
                            AccessibilityScreen(
                                state = uiState,
                                onFontSizeChanged = { viewModel.updateFontSize(it) },
                                onColorBlindChanged = { viewModel.toggleColorBlindMode(it) },
                                onDaltonismoTypeChanged = { viewModel.setDaltonismoType(it) },
                                onTtsChanged = { viewModel.toggleTts(it) },
                                onButtonSizeChanged = { viewModel.updateButtonSize(it) },
                                onSpeakRequest = { viewModel.speakText(it) },
                                onSave = {
                                    viewModel.saveSettings()
                                    navController.navigate("register")
                                }
                            )
                        }

                        // --- 2. REGISTRO ---
                        composable("register") {
                            RegistroScreen(
                                onRegisterClick = { nombre, edad, numero ->
                                    userViewModel.registerUser(nombre, edad, numero) {
                                        viewModel.speakText("Registro exitoso")
                                        navController.navigate("home")
                                    }
                                },
                                onSpeakRequest = { viewModel.speakText(it) }
                            )
                        }

                        // --- 4. HOME (Menú Principal) ---
                        composable("home") {
                            val user by userViewModel.currentUser.collectAsState()
                            val nameToShow = user?.name ?: "Usuario"
                            HomeScreen(
                                userName = nameToShow,
                                onOptionSelected = { route ->

                                    navController.navigate(route)
                                },
                                onSpeakRequest = { viewModel.speakText(it) }
                            )
                        }


                        composable("exercises") { PlaceholderScreen("Ejercicios") }
                        composable("progress") { PlaceholderScreen("Progreso") }
                        composable("assistant") { PlaceholderScreen("Asistente") }
                        composable("reminders") { PlaceholderScreen("Recordatorios") }
                        composable("tips") { PlaceholderScreen("Consejos") }
                    }
                }
            }
        }
    }
}


@androidx.compose.runtime.Composable
fun PlaceholderScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Pantalla de $title")
    }
}