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

        // META MENSUAL PANEL (DISEÑO ACTUALIZADO)
        // ======================
//   PANEL DE META
// ======================
        Column(Modifier.padding(horizontal = 25.dp)) {

            Spacer(Modifier.height(10.dp))
// 📅 Obtener mes actual en texto
            val meses = listOf(
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
            )

            val hoy = remember { java.util.Calendar.getInstance() }
            val mesActual = meses[hoy.get(java.util.Calendar.MONTH)]
            val añoActual = hoy.get(java.util.Calendar.YEAR)

// 📌 Mostrar fecha arriba del título
            Text(
                "$mesActual $añoActual",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(6.dp))
            // 🔵 Título como tu diseño original
            Text(
                "Ingreso mensual asignado",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A237E),
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(10.dp))

            // 🔵 Caja estilo (Q. 2,500.00 Cambiar)
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

            // ======================
            //   PANEL DE PROGRESO
            // ======================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF5F5F5))
                    .padding(16.dp)
            ) {

                Column {

                    // 🔹 Parte superior (Acumulado / Meta)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Acumulado: Q$totalMes", fontWeight = FontWeight.Bold)
                        Text("Meta: Q$meta", fontWeight = FontWeight.Bold)
                    }

                    Spacer(Modifier.height(14.dp))

                    // 🔥 Barra de progreso ultra redondeada
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
                                .background(Color(0xFF00C853))
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    // 🔹 Porcentaje + mensaje derecha
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
                }
            }

            Spacer(Modifier.height(25.dp))

            // ======================
            //   LISTA DE INGRESOS DEL MES
            // ======================
            // ---------------------------
// 🔹 LISTA DE INGRESOS TIPO DISEÑO PROFESIONAL
// ---------------------------
            Text("Añade tus ingresos", fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(Modifier.height(15.dp))

            ingresosMes.forEach { ingreso ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // 🔵 IZQUIERDA (Icono + nombre)
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF00C853)),   // VERDE
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AttachMoney,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(Modifier.width(8.dp))

                        Text(
                            ingreso.name,
                            fontSize = 14.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // 🔵 DERECHA (Monto)
                    Text(
                        "Q.${ingreso.amount}",
                        fontSize = 14.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(12.dp))
            }

// ---------------------------
// 🔹 TOTAL ACUMULADO
// ---------------------------
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total acumulado", fontWeight = FontWeight.Bold, color = Color.Gray)
                Text("Q.$totalMes", fontWeight = FontWeight.Bold, color = Color.Black)
            }
            Spacer(Modifier.height(25.dp))


            // ======================
            //   BOTÓN AÑADIR INGRESO
            // ======================
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

            Spacer(Modifier.height(30.dp))
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
