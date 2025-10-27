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
fun AhorroScreen(volver: () -> Unit) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Encabezado("Ahorro 🏦", Color(0xFFFFEB3B), volver)
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Pestaña("Corto plazo", false)
            Pestaña("Mediano plazo", true)
            Pestaña("Largo plazo", false)
        }
        Spacer(Modifier.height(16.dp))
        Text("Objetivos", fontWeight = FontWeight.Bold)
        CajaTexto("📱 Teléfono nuevo – Q.800")
        Spacer(Modifier.height(10.dp))
        Boton("Añadir objetivos", Color(0xFFFF6F61))
        Spacer(Modifier.height(16.dp))
        Text("Total", fontWeight = FontWeight.Bold)
        BarraProgreso("Q300.00 / 800.00", "37%", 0.37f, Color(0xFFFFEB3B))
    }
}
