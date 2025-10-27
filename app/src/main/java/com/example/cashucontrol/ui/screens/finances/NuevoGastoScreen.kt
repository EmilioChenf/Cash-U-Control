package com.example.cashucontrol.ui.screens.finances

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
fun NuevoGastoScreen(volver: () -> Unit) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Encabezado("Gastos", Color(0xFFFF6F61), volver)
        Spacer(Modifier.height(20.dp))
        Campo("Q.0.00")
        Campo("Nombre del gasto")
        Campo("Fecha (día/mes/año)")
        Spacer(Modifier.height(20.dp))
        Boton("Guardar gasto", Color(0xFFFF6F61))
        Spacer(Modifier.height(20.dp))
        Text("Mis gastos guardados", fontWeight = FontWeight.Bold)
        ListaItems(
            listOf(
                "🎓 Universidad" to "Q.1000",
                "🍔 Comida" to "Q.500",
                "🚌 Transporte" to "Q.300",
                "🎵 Spotify" to "Q.45"
            )
        )
    }
}
