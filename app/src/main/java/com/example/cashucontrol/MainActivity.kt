package com.example.cashucontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cashucontrol.navigation.AppNavigation
import com.example.cashucontrol.ui.theme.CashUControlTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            CashUControlTheme {
                AppNavigation()
            }
        }
    }
}
