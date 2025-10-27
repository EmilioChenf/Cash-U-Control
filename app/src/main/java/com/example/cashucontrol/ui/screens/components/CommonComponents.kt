package com.example.cashucontrol.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Encabezado(titulo: String, color: Color, volver: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(color, RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "←",
                color = Color.White,
                fontSize = 22.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 16.dp)
                    .clickable { volver() }
            )
            Spacer(Modifier.height(10.dp))
            Box(
                Modifier
                    .background(Color.White, RoundedCornerShape(30.dp))
                    .padding(horizontal = 25.dp, vertical = 6.dp)
            ) {
                Text(titulo, color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun Campo(texto: String) {
    Box(
        Modifier
            .fillMaxWidth(0.9f)
            .background(Color(0xFFF2F2F2), RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) { Text(texto, color = Color.Gray) }
}

@Composable
fun CajaTexto(texto: String, accion: String = "") {
    Box(
        Modifier
            .fillMaxWidth(0.9f)
            .background(Color(0xFFF2F2F2), RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(texto, fontWeight = FontWeight.Bold)
            if (accion.isNotEmpty()) Text(accion, color = Color.Gray)
        }
    }
}

@Composable
fun ListaItems(lista: List<Pair<String, String>>, extra: String = "") {
    Column(Modifier.fillMaxWidth(0.9f)) {
        lista.forEach {
            Row(Modifier.padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(it.first)
                Text(it.second, fontWeight = FontWeight.Bold)
            }
        }
        if (extra.isNotEmpty()) Text(extra, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun BarraProgreso(txt1: String, txt2: String, progreso: Float, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        LinearProgressIndicator(
            progress = progreso,
            color = color,
            trackColor = Color(0xFFE0E0E0),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(10.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(Modifier.height(6.dp))
        Row(Modifier.fillMaxWidth(0.9f), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(txt1)
            Text(txt2)
        }
    }
}

@Composable
fun Boton(texto: String, color: Color, onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth(0.7f)
    ) { Text(texto, color = Color.White, fontWeight = FontWeight.Bold) }
}

@Composable
fun Pestaña(texto: String, activa: Boolean) {
    Box(
        Modifier
            .background(if (activa) Color(0xFFFFEB3B) else Color(0xFFE0E0E0), RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) { Text(texto, color = if (activa) Color.Black else Color.Gray) }
}

@Composable
fun Insignia(nivel: String, activa: Boolean) {
    Box(
        Modifier
            .size(90.dp)
            .background(if (activa) Color(0xFF00BFA6) else Color(0xFFE0E0E0), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(if (activa) "🏅" else "🔒", fontSize = 26.sp)
            Text(nivel, color = if (activa) Color.White else Color.Gray, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun Notificacion(texto: String) {
    Box(
        Modifier
            .fillMaxWidth(0.9f)
            .background(Color(0xFFF5F5F5), RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) { Text(texto) }
}
