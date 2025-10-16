package com.example.cashucontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.cashucontrol.ui.screens.*
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        setContent {
            var currentScreen by remember { mutableStateOf("welcome") }
            var userName by remember { mutableStateOf("") }

            when (currentScreen) {
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

                "home" -> HomeScreen(
                    name = userName,
                    onLogout = { currentScreen = "welcome" }
                )
            }
        }
    }
}

