package com.example.cashucontrol.ui.screens.finances

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NotificacionesScreen(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // 🔷 Encabezado azul
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF001F6B), RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                .padding(vertical = 25.dp, horizontal = 20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                    Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White)
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color.White)
                        .padding(horizontal = 50.dp, vertical = 10.dp)
                ) {
                    Text(
                        "Bandeja de entrada",
                        color = Color(0xFF001F6B),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text("Borrar todo", color = Color.Black, fontWeight = FontWeight.Medium)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 📩 Notificaciones
        val notificaciones = listOf(
            "¡Felicidades! Has desbloqueado una nueva insignia.",
            "Has registrado tus gastos e ingresos durante 7 días seguidos. ¡Gran disciplina!",
            "¡Lo lograste! Has cumplido tu meta de ahorro. Cada esfuerzo valió la pena."
        )

        notificaciones.forEach { texto ->
            NotificacionItem(texto)
        }
    }
}

@Composable
fun NotificacionItem(texto: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF2F2F2))
            .padding(14.dp)
    ) {
        Column {
            Text("Notificación del sistema", fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Insignias", fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(2.dp))
            Text(texto, color = Color.Black, fontSize = 13.sp)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewNotificacionesScreen() {
    NotificacionesScreen(
        onBackClick = {}
    )
}

