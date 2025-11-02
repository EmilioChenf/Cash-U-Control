package com.example.cashucontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cashucontrol.ui.screens.*
import com.example.cashucontrol.ui.screens.finances.*
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
        // 🏁 Pantalla de bienvenida
        composable("welcome") {
            WelcomeScreen(
                onLoginClick = { navController.navigate("login") },
                onRegisterClick = { navController.navigate("register") }
            )
        }

        // 📝 Registro
        composable("register") {
            RegisterScreen(
                onRegisterComplete = {
                    navController.navigate("dashboard") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }

        // 🔑 Login
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }

        // 🏠 Dashboard principal
        composable("dashboard") {
            DashboardScreen(
                onOpenIngresos = { navController.navigate("ingresos") },
                onOpenGastos = { navController.navigate("gastos") },
                onOpenAhorro = { navController.navigate("ahorro") },
                onOpenNotificaciones = { navController.navigate("notificaciones") },
                onEditProfile = { navController.navigate("editarPerfil") },
                onViewInsignias = { navController.navigate("insignias") },
                onHelpCenter = { navController.navigate("ayuda") },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            )
        }

        // 💵 Ingresos
        composable("ingresos") {
            IngresosScreen(
                onBackClick = { navController.popBackStack() },
                onAddIngresoClick = { navController.navigate("nuevoIngreso") }
            )
        }

        // ❤️ Gastos
        composable("gastos") {
            GastosScreen(
                onBackClick = { navController.popBackStack() },
                onAddGastoClick = { navController.navigate("nuevoGasto") }
            )
        }

        // 🧾 Nuevo ingreso
        composable("nuevoIngreso") {
            NuevoIngresoFormScreen(onBackClick = { navController.popBackStack() })
        }

        // 🧾 Nuevo gasto
        composable("nuevoGasto") {
            NuevoGastoFormScreen(onBackClick = { navController.popBackStack() })
        }

        // 💛 Ahorro
        composable("ahorro") {
            AhorroScreen(
                onBackClick = { navController.popBackStack() },
                onAddObjetivoClick = { plazoSeleccionado ->
                    navController.navigate("nuevoObjetivo/$plazoSeleccionado")
                }
            )
        }

        // 🌟 Nuevo objetivo de ahorro
        composable("nuevoObjetivo/{plazoSeleccionado}") { backStackEntry ->
            val plazoSeleccionado = backStackEntry.arguments?.getString("plazoSeleccionado") ?: "Mediano plazo"
            NuevoObjetivoScreen(
                selectedPlazo = plazoSeleccionado,
                onBackClick = { navController.popBackStack() }
            )
        }

        // 🔔 Notificaciones
        composable("notificaciones") {
            NotificacionesScreen(onBackClick = { navController.popBackStack() })
        }

        // 👤 Editar perfil
        composable("editarPerfil") {
            EditarPerfilScreen(onBackClick = { navController.popBackStack() })
        }

        // 🏅 Ver insignias
        composable("insignias") {
            InsigniasScreen(onBackClick = { navController.popBackStack() })
        }

        // 💬 Centro de ayuda
        composable("ayuda") {
            CentroAyudaScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
