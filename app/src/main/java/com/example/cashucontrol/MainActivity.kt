package com.example.cashucontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.cashucontrol.ui.screens.WelcomeScreen
import com.example.cashucontrol.ui.screens.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showRegister by remember { mutableStateOf(false) }

            if (showRegister) {
                RegisterScreen()
            } else {
                WelcomeScreen()
            }
        }
    }
}
