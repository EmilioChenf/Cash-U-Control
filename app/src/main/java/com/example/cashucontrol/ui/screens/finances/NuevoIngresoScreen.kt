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
fun NuevoIngresoScreen(volver: () -> Unit) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Encabezado("Nuevo ingreso", Color(0xFF00C853), volver)
        Spacer(Modifier.height(20.dp))
        Campo("Q.0.00")
        Campo("Nombre del ingreso")
        Campo("Fecha (día/mes/año)")
        Spacer(Modifier.height(20.dp))
        Boton("Guardar ingreso", Color(0xFF00C853))
        Spacer(Modifier.height(20.dp))
        Text("Mis ingresos guardados", fontWeight = FontWeight.Bold)
        ListaItems(
            listOf(
                "💼 Trabajo medio tiempo" to "Q.1000",
                "💻 Freelancer" to "Q.500",
                "💰 Mesada" to "Q.500"
            )
        )
    }
}
