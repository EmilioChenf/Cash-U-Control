package com.example.cashucontrol.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.cashucontrol.ui.screens.*
import com.example.cashucontrol.ui.screens.finances.*

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    startDestination: Any = Register // 👈 ahora admite pantalla inicial dinámica
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable<Welcome> {
            WelcomeScreen(
                onLoginClick = { navController.navigate(Login) },
                onRegisterClick = { navController.navigate(Register) }
            )
        }

        // --- LOGIN (MOVERLO AQUÍ) ---
        composable<Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Dashboard) {
                        popUpTo(Register) { inclusive = true }
                    }
                },
                onGoToRegister = { navController.navigate(Register) }
            )
        }

        // --- REGISTER ---
        composable<Register> {
            RegisterScreen(
                onRegisterComplete = {
                    navController.navigate(Dashboard) {
                        popUpTo(Register) { inclusive = true }
                    }
                },
                onGoToLogin = { navController.navigate(Login) }
            )
        }

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

        composable<Ahorro> {
            AhorroScreen(
                onBackClick = { navController.popBackStack() },
                onAddObjetivoClick = { plazo ->
                    navController.navigate(NuevoObjetivo(plazo))
                }
            )
        }

        composable<NuevoObjetivo> { entry ->
            val args = entry.toRoute<NuevoObjetivo>()
            NuevoObjetivoScreen(
                selectedPlazo = args.plazoSeleccionado,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<Notificaciones> {
            NotificacionesScreen(onBackClick = { navController.popBackStack() })
        }

        composable<EditarPerfil> {
            EditarPerfilScreen(onBackClick = { navController.popBackStack() })
        }

        composable<Insignias> {
            InsigniasScreen(onBackClick = { navController.popBackStack() })
        }

        composable<CentroAyuda> {
            CentroAyudaScreen(onBackClick = { navController.popBackStack() })
        }






    }
}
