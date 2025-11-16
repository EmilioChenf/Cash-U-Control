package com.example.cashucontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cashucontrol.navigation.AppNavigation
import com.example.cashucontrol.ui.theme.CashUControlTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            CashUControlTheme {

                // 🟦 Si hay sesión iniciada → ir al Dashboard directamente
                val user = FirebaseAuth.getInstance().currentUser
                val start = if (user != null) {
                    com.example.cashucontrol.navigation.Dashboard
                } else {
                    com.example.cashucontrol.navigation.Register
                }

                AppNavigation(startDestination = start)
            }
        }
    }
}
