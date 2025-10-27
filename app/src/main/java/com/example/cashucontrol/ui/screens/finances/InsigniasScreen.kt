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
fun InsigniasScreen(volver: () -> Unit) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Encabezado("Ana Sofía", Color(0xFF1A237E), volver)
        Spacer(Modifier.height(12.dp))
        Text("Mis insignias", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(20.dp))
        for (i in 0 until 3) {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Insignia("Nivel ${i * 2 + 1}", i < 1)
                Spacer(Modifier.width(20.dp))
                Insignia("Nivel ${i * 2 + 2}", i < 1)
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}
