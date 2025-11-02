package com.example.cashucontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cashucontrol.ui.screens.*
import com.example.cashucontrol.ui.screens.finances.DashboardScreen
import com.example.cashucontrol.ui.screens.finances.IngresosScreen
import com.example.cashucontrol.ui.screens.finances.NuevoIngresoFormScreen
import com.example.cashucontrol.ui.theme.CashUControlTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            CashUControlTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        // 🔹 Pantalla de bienvenida
        composable("welcome") {
            WelcomeScreen(
                onLoginClick = { navController.navigate("login") },
                onRegisterClick = { navController.navigate("register") }
            )
        }

        // 🔹 Registro
        composable("register") {
            RegisterScreen(
                onRegisterComplete = { _ ->
                    navController.navigate("dashboard") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }

        // 🔹 Login
        composable("login") {
            LoginScreen(
                onLoginSuccess = { _ ->
                    navController.navigate("dashboard") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }

        // 🏠 Dashboard principal
        composable("dashboard") {
            DashboardScreen(
                onOpenIngresos = { navController.navigate("ingresos") } // ✅ Conecta con Ingresos
            )
        }

        // 💰 Pantalla de Ingresos
        composable("ingresos") {
            IngresosScreen(
                onBackClick = { navController.popBackStack() }, // ← vuelve al Dashboard
                onAddIngresoClick = { navController.navigate("nuevoIngreso") } // ✅ Conecta con formulario
            )
        }

        // ➕ Formulario de nuevo ingreso
        composable("nuevoIngreso") {
            NuevoIngresoFormScreen(
                onBackClick = { navController.popBackStack() } // ← vuelve a Ingresos
            )
        }
    }
}
