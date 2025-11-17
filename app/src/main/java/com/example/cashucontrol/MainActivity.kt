package com.example.cashucontrol

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cashucontrol.navigation.AppNavigation
import com.example.cashucontrol.ui.theme.CashUControlTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        // 🔔 PEDIR PERMISO DE NOTIFICACIONES (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }

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
