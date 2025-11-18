package com.dalonso.vitasenior

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dalonso.vitasenior.ui.theme.VitaSeniorTheme

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

                    NavHost(
                        navController = navController,
                        // Make sure your startDestination is the screen you want to show first
                        startDestination = "accessibility"
                    ) {

                        // Route for the initial Accessibility screen
                        composable(route = "accessibility") {
                            // Pass lambdas to navigate to the other screens
                            AccessibilityScreen(
                                onLoginClick = { navController.navigate("login") },
                                onRegisterClick = { navController.navigate("register") }
                            )
                        }

                        // Route for the Login screen
                        composable(route = "login") {
                            // Assuming you have a LoginScreen composable
                            // LoginScreen(
                            //     onNavigateToRegister = { navController.navigate("register") },
                            //     onNavigateBack = { navController.popBackStack() }
                            // )
                        }

                        // Route for the Register screen
                        composable(route = "register") {
                            // Assuming you have a RegisterScreen composable
                            // RegisterScreen(
                            //     onNavigateToLogin = { navController.navigate("login") },
                            //     onNavigateBack = { navController.popBackStack() }
                            // )
                        }
                    }
                }
            }
        }
    }
}
