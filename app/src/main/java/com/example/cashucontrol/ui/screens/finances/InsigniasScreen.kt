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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cashucontrol.viewmodel.InsigniasViewModel
import com.example.cashucontrol.models.Insignia

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsigniasScreen(onBackClick: () -> Unit) {

    // 🔵 ViewModel seguro
    val insigniasVM: InsigniasViewModel = viewModel()
    val insignias by insigniasVM.insignias.collectAsState()

    var insigniaSeleccionada by remember { mutableStateOf<Insignia?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 🔵 ENCABEZADO
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFF001F6B),
                    RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                )
                .padding(vertical = 40.dp)
        ) {
            IconButton(
                onClick = { onBackClick() },
                modifier = Modifier.padding(start = 10.dp, top = 10.dp)
            ) {
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
                Text(
                    "Mis insignias",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // TÍTULO
        Text(
            "Insignias obtenidas y por desbloquear",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 🔹 GRID DINÁMICO — 2 POR FILA
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val rows = insignias.chunked(2)

            rows.forEach { fila ->

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    fila.forEach { ins ->
                        InsigniaItem(
                            insignia = ins,
                            onClick = { insigniaSeleccionada = ins }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(25.dp))
            }
        }
    }

    // ======================================
    // 🟢 DIÁLOGO — DETALLES DE INSIGNIA
    // ======================================
    if (insigniaSeleccionada != null) {

        val ins = insigniaSeleccionada!!

        Dialog(onDismissRequest = { insigniaSeleccionada = null }) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.90f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(20.dp)
            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    // ICONO
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(
                                if (ins.completed) Color(0xFFE0F7FA)
                                else Color(0xFFE0E0E0)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            if (ins.completed) "🏅" else "🔒",
                            fontSize = 36.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // NOMBRE
                    Text(
                        ins.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // PROGRESO
                    LinearProgressIndicator(
                        progress = ins.progress.toFloat(),
                        color = if (ins.completed) Color(0xFF00C853) else Color.Gray,
                        trackColor = Color(0xFFE0E0E0),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${(ins.progress * 100).toInt()}%", color = Color.Gray)
                        Text(
                            if (ins.completed) "Completado" else "Bloqueado",
                            color = if (ins.completed) Color(0xFF00C853) else Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    // DESCRIPCIÓN
                    Text(
                        text = ins.description,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // BOTÓN CERRAR
                    Button(
                        onClick = { insigniaSeleccionada = null },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0))
                    ) {
                        Text("Cerrar", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}


// =========================================================
// 🔵 COMPONENTE — ITEM DE INSIGNIA
// =========================================================
// =========================================================
// 🔵 COMPONENTE — ITEM DE INSIGNIA (con descripción visible)
// =========================================================
@Composable
fun InsigniaItem(insignia: Insignia, onClick: () -> Unit) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(130.dp)
    ) {

        // ICONO
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(
                    if (insignia.completed) Color(0xFFE0F7FA)
                    else Color(0xFFE0E0E0)
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                if (insignia.completed) "🏅" else "🔒",
                fontSize = 36.sp
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // NOMBRE
        Text(
            insignia.name,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = if (insignia.completed) Color.Black else Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        // DESCRIPCIÓN
        Text(
            insignia.description,
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}
