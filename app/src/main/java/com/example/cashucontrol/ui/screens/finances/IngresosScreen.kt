package com.example.cashucontrol.ui.screens.finances

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cashucontrol.viewmodel.IngresosViewModel
import com.example.cashucontrol.viewmodel.MonthlyGoalViewModel

@Composable
fun IngresosScreen(
    onBackClick: () -> Unit,
    onAddIngresoClick: () -> Unit
) {
    val ingresosVM = remember { IngresosViewModel() }
    val goalVM = remember { MonthlyGoalViewModel() }

    val ingresosMes by ingresosVM.ingresosMes.collectAsState()
    val goal by goalVM.goal.collectAsState()

    val totalMes = ingresosMes.sumOf { it.amount }
    val meta = goal?.goalAmount ?: 0.0
    val progreso = if (meta > 0) (totalMes / meta).coerceIn(0.0, 1.0) else 0.0

    var showEditGoal by remember { mutableStateOf(false) }
    var newGoal by remember { mutableStateOf(meta.toString()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {

        // HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF00C853), RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                .padding(20.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, "", tint = Color.Black)
            }
            Text(
                "Ingresos",
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF1A237E),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Spacer(Modifier.height(20.dp))

        Column(Modifier.padding(horizontal = 25.dp)) {

            // META MENSUAL PANEL
            Text("Meta mensual (${goalVM.currentMonth})", fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF5F5F5))
                    .padding(16.dp)
            ) {
                Column {

                    // Numeritos
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Acumulado: Q$totalMes", fontWeight = FontWeight.Bold)
                        Text("Meta: Q$meta", fontWeight = FontWeight.Bold)
                    }

                    Spacer(Modifier.height(14.dp))

                    // PROGRESS BAR PROFESIONAL
                    LinearProgressIndicator(
                        progress = progreso.toFloat(),
                        color = Color(0xFF00C853),
                        trackColor = Color(0xFFE0E0E0),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )

                    Spacer(Modifier.height(10.dp))

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
                            color = if (meta > totalMes) Color.Gray else Color(0xFF00C853),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(14.dp))

                    // BOTÓN CAMBIAR META
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFD9D9D9))
                            .clickable { showEditGoal = true }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Cambiar meta", fontSize = 13.sp)
                    }
                }
            }

            Spacer(Modifier.height(25.dp))

            // INGRESOS DEL MES
            Text("Ingresos del mes", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))

            ingresosMes.forEach { ingreso ->
                IngresoChip(
                    icon = Icons.Default.Money,
                    title = ingreso.name,
                    amount = "Q${ingreso.amount}"
                )
                Spacer(Modifier.height(10.dp))
            }

            Spacer(Modifier.height(25.dp))

            // BOTÓN AÑADIR INGRESO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF00C853))
                    .clickable { onAddIngresoClick() }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Añadir ingreso", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }

    // DIÁLOGO CAMBIAR META
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
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditGoal = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun IngresoChip(
    icon: ImageVector,
    title: String,
    amount: String
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(25.dp))
            .background(Color(0xFFEFEFEF))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Color(0xFF00C853)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
        }

        Spacer(modifier = Modifier.width(6.dp))
        Text(title, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.width(6.dp))
        Text(amount, fontSize = 12.sp, color = Color.Gray)
    }
}
