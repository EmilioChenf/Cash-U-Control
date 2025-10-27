package com.example.cashucontrol.ui.screens.finances

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cashucontrol.ui.screens.components.*

@Composable
fun IngresosScreen(volver: () -> Unit) {
    Column(
        Modifier.fillMaxSize().background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Encabezado("Ingresos", Color(0xFF00C853), volver)
        Spacer(Modifier.height(16.dp))
        Text("Ingreso mensual asignado", color = Color.Gray)
        CajaTexto("Q. 2,500.00", "Cambiar")
        Spacer(Modifier.height(16.dp))
        Text("Añade tus ingresos", fontWeight = FontWeight.Bold)
        ListaItems(
            listOf(
                "💼 Trabajo medio tiempo" to "Q.1000",
                "💻 Freelancer" to "Q.500",
                "💰 Mesada" to "Q.500"
            ),
            "Total acumulado Q.2000"
        )
        Spacer(Modifier.height(20.dp))
        Text("Meta mensual 2,500.00", fontWeight = FontWeight.Bold)
        BarraProgreso("Alcanzado un 90%", "¡Vas muy bien, sigue así!", 0.9f, Color(0xFF00C853))
        Spacer(Modifier.height(20.dp))
        Boton("Añadir ingreso", Color(0xFF00C853))
    }
}
