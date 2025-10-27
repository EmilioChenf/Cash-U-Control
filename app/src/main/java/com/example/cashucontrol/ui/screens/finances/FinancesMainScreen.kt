package com.example.cashucontrol.ui.screens.finances

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FinancesMainScreen(onBackToHome: () -> Unit) {
    var selectedTab by remember { mutableStateOf("ingresos") }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 6.dp
            ) {
                val tabs = listOf(
                    "ingresos" to Icons.Default.Home,
                    "gastos" to Icons.Default.List,
                    "ahorro" to Icons.Default.AccountCircle,
                    "insignias" to Icons.Default.Notifications,
                    "bandeja" to Icons.Default.Settings
                )

                tabs.forEach { (key, icon) ->
                    NavigationBarItem(
                        selected = selectedTab == key,
                        onClick = { selectedTab = key },
                        icon = { Icon(icon, contentDescription = key) },
                        label = { Text(key.replaceFirstChar { it.uppercase() }) },
                        alwaysShowLabel = true
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                "ingresos" -> IngresosScreen(volver = onBackToHome)
                "gastos" -> GastosScreen(volver = onBackToHome)
                "ahorro" -> AhorroScreen(volver = onBackToHome)
                "insignias" -> InsigniasScreen(volver = onBackToHome)
                "bandeja" -> BandejaScreen(volver = onBackToHome)
            }
        }
    }
}
