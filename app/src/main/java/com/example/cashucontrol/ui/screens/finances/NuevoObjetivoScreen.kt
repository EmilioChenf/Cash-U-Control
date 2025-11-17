package com.example.cashucontrol.ui.screens.finances

import android.app.DatePickerDialog
import androidx.compose.foundation.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cashucontrol.models.Ahorro
import com.example.cashucontrol.viewmodel.AhorrosViewModel
import java.util.*

@Composable
fun NuevoObjetivoScreen(selectedPlazo: String, onBackClick: () -> Unit) {

    val vm = remember { AhorrosViewModel() }
    val ahorrosList by vm.ahorros.collectAsState()

    var name by remember { mutableStateOf(TextFieldValue("")) }
    var goalAmount by remember { mutableStateOf(TextFieldValue("")) }
    var plazo by remember { mutableStateOf(selectedPlazo) }
    var deadline by remember { mutableStateOf(TextFieldValue("")) }

    var editing by remember { mutableStateOf<Ahorro?>(null) }

    val ctx = LocalContext.current

    // 📅 Calendario
    fun openCalendar() {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            ctx,
            { _, y, m, d -> deadline = TextFieldValue("$d/${m + 1}/$y") },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
    ) {

        // =======================
        // 🟡 HEADER
        // =======================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFEB3B), RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                .padding(20.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "", tint = Color.Black)
            }
            Text(
                text = if (editing == null) "Nuevo objetivo" else "Editar objetivo",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A237E)
            )
        }

        Spacer(Modifier.height(20.dp))

        Column(Modifier.padding(horizontal = 25.dp)) {

            // =======================
            // 📝 FORMULARIO
            // =======================

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del objetivo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = goalAmount,
                onValueChange = { goalAmount = it },
                label = { Text("Monto meta (Q)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            Text("Plazo", fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(Modifier.height(8.dp))

            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE0E0E0))
                    .padding(6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("Corto plazo", "Mediano plazo", "Largo plazo").forEach {
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (plazo == it) Color(0xFFFFEB3B) else Color.Transparent)
                            .clickable { plazo = it }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(it, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = deadline,
                onValueChange = { deadline = it },
                label = { Text("Fecha límite (Opcional)") },
                trailingIcon = {
                    IconButton(onClick = { openCalendar() }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            // =======================
            // 🟡 BOTÓN GUARDAR
            // =======================
            Button(
                onClick = {
                    if (name.text.isBlank() || goalAmount.text.isBlank()) return@Button

                    if (editing == null) {
                        vm.saveAhorro(name.text, goalAmount.text.toDouble(), plazo, deadline.text)
                    } else {
                        vm.updateAhorro(editing!!.id, name.text, goalAmount.text.toDouble(), plazo, deadline.text)
                        editing = null
                    }

                    name = TextFieldValue("")
                    goalAmount = TextFieldValue("")
                    deadline = TextFieldValue("")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color(0xFFFFEB3B))
            ) {
                Text(if (editing == null) "Guardar objetivo" else "Actualizar objetivo", color = Color.Black)
            }

            Spacer(Modifier.height(30.dp))

            // =======================
            // 📋 LISTA DE OBJETIVOS
            // =======================
            Text("Mis objetivos de ahorro", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))

            ahorrosList.forEach { ahorro ->
                ObjetivoRow(
                    ahorro = ahorro,
                    onEdit = {
                        editing = ahorro
                        name = TextFieldValue(ahorro.name)
                        goalAmount = TextFieldValue(ahorro.goalAmount.toString())
                        plazo = ahorro.plazo
                        deadline = TextFieldValue(ahorro.deadline)
                    },
                    onDelete = {
                        vm.deleteAhorro(ahorro.id)
                    }
                )
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun ObjetivoRow(ahorro: Ahorro, onEdit: () -> Unit, onDelete: () -> Unit) {

    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFFF9C4))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(Modifier.weight(1f)) {
            Text(ahorro.name, fontWeight = FontWeight.Bold)
            Text("Meta: Q${ahorro.goalAmount}", fontSize = 12.sp, color = Color.Gray)
            if (ahorro.deadline.isNotBlank())
                Text("Fecha límite: ${ahorro.deadline}", fontSize = 12.sp, color = Color.Gray)
            Text("Plazo: ${ahorro.plazo}", fontSize = 12.sp, color = Color.Gray)
        }

        IconButton(onClick = onEdit) {
            Icon(Icons.Default.Edit, "", tint = Color.Black)
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, "", tint = Color.Red)
        }
    }
}
