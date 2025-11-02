package com.example.cashucontrol.ui.screens.finances

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsigniasScreen(onBackClick: () -> Unit) {
    var insigniaSeleccionada by remember { mutableStateOf<String?>(null) }
    var completada by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🔵 Encabezado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF001F6B), RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                .padding(vertical = 40.dp),
            contentAlignment = Alignment.TopStart
        ) {
            IconButton(onClick = { onBackClick() }, modifier = Modifier.padding(start = 10.dp, top = 10.dp)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = Color.LightGray,
                    modifier = Modifier.size(90.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Ana Sofía", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("Mis insignias", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 18.sp)

        Spacer(modifier = Modifier.height(15.dp))

        // 🔹 Grid de insignias
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp)) {
                InsigniaItem("Nivel 1", true) {
                    insigniaSeleccionada = "Nivel 1"
                    completada = true
                }
                InsigniaItem("Nivel 2", true) {
                    insigniaSeleccionada = "Nivel 2"
                    completada = true
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp)) {
                InsigniaItem("Nivel 3", false) {
                    insigniaSeleccionada = "Nivel 3"
                    completada = false
                }
                InsigniaItem("Nivel 4", false) {
                    insigniaSeleccionada = "Nivel 4"
                    completada = false
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp)) {
                InsigniaItem("Nivel 5", false) {
                    insigniaSeleccionada = "Nivel 5"
                    completada = false
                }
                InsigniaItem("Nivel 6", false) {
                    insigniaSeleccionada = "Nivel 6"
                    completada = false
                }
            }
        }
    }

    // 🟢 Dialogo de insignia seleccionada
    if (insigniaSeleccionada != null) {
        Dialog(onDismissRequest = { insigniaSeleccionada = null }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Icono
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(if (completada) Color(0xFFE0F7FA) else Color(0xFFE0E0E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            if (completada) "🏅" else "🔒",
                            fontSize = 32.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(insigniaSeleccionada ?: "", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                    Spacer(modifier = Modifier.height(10.dp))

                    // Barra de progreso
                    LinearProgressIndicator(
                        progress = if (completada) 1f else 0.5f,
                        color = if (completada) Color(0xFF00C853) else Color.Gray,
                        trackColor = Color(0xFFE0E0E0),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            if (completada) "100%" else "50%",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                        Text(
                            if (completada) "Completado" else "Bloqueado",
                            color = if (completada) Color(0xFF00C853) else Color.Gray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    // Descripción
                    Text(
                        text = if (completada)
                            "Has registrado tus gastos e ingresos durante 7 días seguidos.\n¡Gran disciplina, sigue construyendo tu hábito financiero!"
                        else
                            "Racha de 30 días de registro de ingresos y gastos",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { insigniaSeleccionada = null },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0))
                    ) {
                        Text("Cerrar", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun InsigniaItem(texto: String, completado: Boolean, onClick: () -> Unit) {
    val fondo = if (completado) Color(0xFFE0F7FA) else Color(0xFFE0E0E0)
    val textoColor = if (completado) Color.Black else Color.Gray

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(fondo)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(if (completado) "🏅" else "🔒", fontSize = 30.sp)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(texto, fontSize = 14.sp, color = textoColor, fontWeight = FontWeight.Medium)
    }
}
