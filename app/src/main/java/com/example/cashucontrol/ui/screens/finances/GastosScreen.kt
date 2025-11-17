package com.example.cashucontrol.ui.screens.finances

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cashucontrol.viewmodel.GastosViewModel
import com.example.cashucontrol.viewmodel.MonthlyGoalGastosViewModel

@Composable
fun GastosScreen(
    onBackClick: () -> Unit,
    onAddGastoClick: () -> Unit
) {
    val gastosVM = remember { GastosViewModel() }
    val goalVM = remember { MonthlyGoalGastosViewModel() }

    val gastosMes by gastosVM.gastosMes.collectAsState()
    val goal by goalVM.goal.collectAsState()

    val totalMes = gastosMes.sumOf { it.amount }
    val meta = goal?.goalAmount ?: 0.0
    val progreso = if (meta > 0) (totalMes / meta).coerceIn(0.0, 1.0) else 0.0

    var showEditGoal by remember { mutableStateOf(false) }
    var newGoal by remember { mutableStateOf(meta.toString()) }

    // Obtener mes actual como texto (Enero 2025)
    val meses = listOf(
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    )
    val hoy = remember { java.util.Calendar.getInstance() }
    val mesActual = meses[hoy.get(java.util.Calendar.MONTH)]
    val añoActual = hoy.get(java.util.Calendar.YEAR)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {

        // ====================================================
        // HEADER — Igual que ingresos pero rojo
        // ====================================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFF5252), RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                .padding(20.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "", tint = Color.Black)
            }
            Text(
                "Gastos",
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF1A237E),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Spacer(Modifier.height(20.dp))

        // ====================================================
        // PANEL DE META — EXACTAMENTE IGUAL A INGRESOS
        // ====================================================
        Column(Modifier.padding(horizontal = 25.dp)) {

            // Fecha (Enero 2025)
            Text(
                "$mesActual $añoActual",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(6.dp))

            Text(
                "Presupuesto mensual de gastos",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A237E),
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(10.dp))

            // Caja tipo "Q. 2500 Cambiar"
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFEDEDED))
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Q. $meta",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A237E)
                    )
                    Text(
                        text = "Cambiar",
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable { showEditGoal = true }
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ====================================================
            // PANEL DE PROGRESO — MISMO DISEÑO AL 100%
            // ====================================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF5F5F5))
                    .padding(16.dp)
            ) {

                Column {

                    // Acumulado y Meta
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Acumulado: Q$totalMes", fontWeight = FontWeight.Bold)
                        Text("Meta: Q$meta", fontWeight = FontWeight.Bold)
                    }

                    Spacer(Modifier.height(14.dp))

                    // Barra profesional redondeada como ingresos
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color(0xFFE0E0E0))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progreso.toFloat())
                                .height(20.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFFFF5252))
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    // Porcentaje + mensaje
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${(progreso * 100).toInt()}%", color = Color.Gray)

                        Text(
                            if (meta > totalMes)
                                "Te falta Q${(meta - totalMes)}"
                            else
                                "Meta alcanzada 🎉",
                            color = if (meta > totalMes) Color.Gray else Color(0xFFFF5252),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(25.dp))

            // ====================================================
            // LISTA DE GASTOS — IGUAL A INGRESOS PERO ROJO
            // ====================================================
            Text("Gastos del mes", fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(Modifier.height(15.dp))

            gastosMes.forEach { gasto ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF5252)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoneyOff,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(Modifier.width(8.dp))

                        Text(
                            gasto.name,
                            fontSize = 14.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Text(
                        "Q.${gasto.amount}",
                        fontSize = 14.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(10.dp))

            // Total Gastado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total gastado", fontWeight = FontWeight.Bold, color = Color.Gray)
                Text("Q.$totalMes", fontWeight = FontWeight.Bold, color = Color.Black)
            }

            Spacer(Modifier.height(25.dp))

            // ====================================================
            // BOTÓN AÑADIR GASTO — Igual al de ingresos
            // ====================================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFF5252))
                    .clickable { onAddGastoClick() }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Añadir gasto", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(30.dp))
        }
    }

    // ====================================================
    // DIÁLOGO CAMBIAR META
    // ====================================================
    if (showEditGoal) {
        AlertDialog(
            onDismissRequest = { showEditGoal = false },
            title = { Text("Editar meta mensual") },
            text = {
                OutlinedTextField(
                    value = newGoal,
                    onValueChange = { newGoal = it },
                    label = { Text("Meta (Q)") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val m = newGoal.toDoubleOrNull()
                    if (m != null) goalVM.saveGoal(m)
                    showEditGoal = false
                }) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { showEditGoal = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
