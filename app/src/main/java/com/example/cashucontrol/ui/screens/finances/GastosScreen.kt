package com.example.cashucontrol.ui.screens.finances

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cashucontrol.ui.screens.components.*

@Composable
fun GastosScreen(volver: () -> Unit) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Encabezado("Gastos", Color(0xFFFF6F61), volver)
        Spacer(Modifier.height(16.dp))
        Text("Presupuesto asignado para gastos", color = Color.Gray)
        CajaTexto("Q. 2000.00", "Cambiar")
        Spacer(Modifier.height(10.dp))
        Text("Añade tus gastos", fontWeight = FontWeight.Bold)
        ListaItems(
            listOf(
                "🎓 Universidad" to "Q.1000",
                "🍔 Comida" to "Q.500",
                "🚌 Transporte" to "Q.300",
                "🎵 Spotify" to "Q.45"
            ),
            "Total gastado Q.1845     Restante Q.155"
        )
        Spacer(Modifier.height(20.dp))
        Text("Límite mensual Q2000.00", fontWeight = FontWeight.Bold)
        BarraProgreso("Usado un 85%", "¡Ya casi llegas al límite!", 0.85f, Color(0xFFFF6F61))
        Spacer(Modifier.height(20.dp))
        Boton("Añadir gasto", Color(0xFFFF6F61))
    }
}
