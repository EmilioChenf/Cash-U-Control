package com.example.cashucontrol.ui.screens.finances

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cashucontrol.ui.screens.components.*

@Composable
fun BandejaScreen(volver: () -> Unit) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Encabezado("Bandeja de entrada", Color(0xFF1A237E), volver)
        Spacer(Modifier.height(16.dp))
        Notificacion("¡Felicidades! Has desbloqueado una nueva insignia")
        Notificacion("Has registrado tus gastos e ingresos durante 7 días seguidos. ¡Gran disciplina!")
        Notificacion("¡Lo lograste! Has cumplido tu meta de ahorro. Disfruta tu recompensa.")
    }
}
