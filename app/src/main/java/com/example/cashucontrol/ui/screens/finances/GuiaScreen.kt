package com.example.cashucontrol.ui.screens.finances

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cashucontrol.ui.screens.components.*

@Composable
fun GuiaScreen(volver: () -> Unit) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Encabezado("Guía para principiantes", Color(0xFF1A237E), volver)
        Spacer(Modifier.height(30.dp))
        Text(
            "Tu mejor aliado financiero:\ndescarga la guía y comienza a usar la app como un experto.",
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(20.dp))
        Boton("Descargar", Color(0xFFBDBDBD))
        Spacer(Modifier.height(30.dp))
        Text("⬇️", fontSize = 40.sp)
    }
}
