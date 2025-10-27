package com.example.cashucontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.cashucontrol.ui.screens.*
import com.example.cashucontrol.ui.screens.finances.*
import com.google.firebase.FirebaseApp
import com.example.cashucontrol.ui.theme.CashUControlTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            CashUControlTheme  {
                var currentScreen by remember { mutableStateOf("welcome") }
                var userName by remember { mutableStateOf("") }

                when (currentScreen) {
                    // 🔹 Pantallas de autenticación
                    "welcome" -> WelcomeScreen(
                        onLoginClick = { currentScreen = "login" },
                        onRegisterClick = { currentScreen = "register" }
                    )

                    "register" -> RegisterScreen(
                        onRegisterComplete = { name ->
                            userName = name
                            currentScreen = "home"
                        }
                    )

                    "login" -> LoginScreen(
                        onLoginSuccess = { name ->
                            userName = name
                            currentScreen = "home"
                        }
                    )

                    // 🔹 Home
                    "home" -> HomeScreen(
                        name = userName,
                        onLogout = { currentScreen = "welcome" },
                        onEnterFinances = { currentScreen = "finances" }
                    )

                    // 🔹 Nueva navegación principal de finanzas
                    "finances" -> FinancesMainScreen(
                        onBackToHome = { currentScreen = "home" }
                    )
                }
            }
        }
    }
}
