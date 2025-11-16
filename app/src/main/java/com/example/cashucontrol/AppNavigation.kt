package com.example.cashucontrol.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.cashucontrol.ui.screens.WelcomeScreen
import com.example.cashucontrol.ui.screens.*
import com.example.cashucontrol.ui.screens.finances.*

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Welcome,
        modifier = modifier
    ) {

        // --- Welcome ---
        composable<Welcome> {
            WelcomeScreen(
                onLoginClick = { navController.navigate(Login) },
                onRegisterClick = { navController.navigate(Register) }
            )
        }

        // --- Login ---
        composable<Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Dashboard) {
                        popUpTo(Welcome) { inclusive = true }
                    }
                }
            )
        }

        // --- Registro ---
        composable<Register> {
            RegisterScreen(
                onRegisterComplete = {
                    navController.navigate(Dashboard) {
                        popUpTo(Welcome) { inclusive = true }
                    }
                }
            )
        }

        // --- Dashboard ---
        composable<Dashboard> {
            DashboardScreen(
                onOpenIngresos = { navController.navigate(Ingresos) },
                onOpenGastos = { navController.navigate(Gastos) },
                onOpenAhorro = { navController.navigate(Ahorro) },
                onOpenNotificaciones = { navController.navigate(Notificaciones) },
                onEditProfile = { navController.navigate(EditarPerfil) },
                onViewInsignias = { navController.navigate(Insignias) },
                onHelpCenter = { navController.navigate(CentroAyuda) },
                onLogout = {
                    navController.navigate(Login) {
                        popUpTo(0)
                    }
                }
            )
        }

        // --- Ingresos ---
        composable<Ingresos> {
            IngresosScreen(
                onBackClick = { navController.popBackStack() },
                onAddIngresoClick = { navController.navigate(NuevoIngreso) }
            )
        }

        composable<NuevoIngreso> {
            NuevoIngresoFormScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- Gastos ---
        composable<Gastos> {
            GastosScreen(
                onBackClick = { navController.popBackStack() },
                onAddGastoClick = { navController.navigate(NuevoGasto) }
            )
        }

        composable<NuevoGasto> {
            NuevoGastoFormScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- Ahorro ---
        composable<Ahorro> {
            AhorroScreen(
                onBackClick = { navController.popBackStack() },
                onAddObjetivoClick = { plazo ->
                    navController.navigate(NuevoObjetivo(plazo))
                }
            )
        }

        // --- Nuevo Objetivo (con argumento) ---
        composable<NuevoObjetivo> { backStackEntry ->
            val args = backStackEntry.toRoute<NuevoObjetivo>()

            NuevoObjetivoScreen(
                selectedPlazo = args.plazoSeleccionado,
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- Notificaciones ---
        composable<Notificaciones> {
            NotificacionesScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- Editar Perfil ---
        composable<EditarPerfil> {
            EditarPerfilScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- Insignias ---
        composable<Insignias> {
            InsigniasScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- Centro de Ayuda ---
        composable<CentroAyuda> {
            CentroAyudaScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
