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
import com.example.cashucontrol.models.Ingreso
import com.example.cashucontrol.viewmodel.IngresosViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun NuevoIngresoFormScreen(onBackClick: () -> Unit) {

    val vm = remember { IngresosViewModel() }
    val ingresosList by vm.ingresos.collectAsState()

    var amount by remember { mutableStateOf(TextFieldValue("")) }
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var date by remember { mutableStateOf(TextFieldValue("")) }
    var type by remember { mutableStateOf("work") }

    var showEditDialog by remember { mutableStateOf(false) }
    var selectedIngreso by remember { mutableStateOf<Ingreso?>(null) }

    val context = LocalContext.current

    // Calendario nativo Android
    fun openCalendar() {
        val calendar = Calendar.getInstance()
        val dialog = DatePickerDialog(
            context,
            { _, y, m, d ->
                date = TextFieldValue("$d/${m + 1}/$y")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dialog.show()
    }

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
                Icon(Icons.Default.ArrowBack, contentDescription = "", tint = Color.Black)
            }
            Text(
                "Nuevo ingreso",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A237E)
            )
        }

        Spacer(Modifier.height(20.dp))

        // FORMULARIO
        Column(Modifier.padding(horizontal = 25.dp)) {

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Cantidad (Q)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del ingreso") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Fecha") },
                trailingIcon = {
                    IconButton(onClick = { openCalendar() }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    if (name.text.isNotBlank() && amount.text.isNotBlank()) {
                        vm.saveIngreso(
                            name.text,
                            amount.text.toDouble(),
                            date.text,
                            type
                        )
                        name = TextFieldValue("")
                        amount = TextFieldValue("")
                        date = TextFieldValue("")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color(0xFF00C853))
            ) {
                Text("Guardar ingreso", color = Color.White)
            }

            Spacer(Modifier.height(30.dp))

            Text("Mis ingresos guardados", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(10.dp))

            ingresosList.forEach { ingreso ->
                IngresoItem(ingreso = ingreso, onClick = {
                    selectedIngreso = ingreso
                    showEditDialog = true
                })
                Spacer(Modifier.height(10.dp))
            }
        }
    }

    // DIALOGO DE EDITAR
    if (showEditDialog && selectedIngreso != null) {
        EditIngresoDialog(
            ingreso = selectedIngreso!!,
            onDismiss = { showEditDialog = false },
            onDelete = {
                vm.deleteIngreso(selectedIngreso!!.id)
                showEditDialog = false
            },
            onSave = { n, a, d, t ->
                vm.updateIngreso(selectedIngreso!!.id, n, a, d, t)
                showEditDialog = false
            }
        )
    }
}

@Composable
fun IngresoItem(ingreso: Ingreso, onClick: () -> Unit) {

    val icon = when (ingreso.type) {
        "freelance" -> Icons.Default.LaptopMac
        "wallet" -> Icons.Default.AccountBalanceWallet
        else -> Icons.Default.Work
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF1F1F1))
            .clickable { onClick() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFF00C853)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, "", tint = Color.White)
            }
            Spacer(Modifier.width(8.dp))
            Column {
                Text(ingreso.name, fontWeight = FontWeight.Bold)
                Text(ingreso.date, fontSize = 12.sp, color = Color.Gray)
            }
        }
        Text("Q${ingreso.amount}", fontWeight = FontWeight.Bold)
    }
}

// CHIP DE TIPO
@Composable
fun TipoChip(value: String, label: String, selected: String, onSelect: (String) -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected == value) Color(0xFF00C853) else Color(0xFFE0E0E0))
            .clickable { onSelect(value) }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(label, color = if (selected == value) Color.White else Color.Black)
    }
}

// DIALOGO DE EDICIÓN PROFESIONAL
@Composable
fun EditIngresoDialog(
    ingreso: Ingreso,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onSave: (String, Double, String, String) -> Unit
) {

    var name by remember { mutableStateOf(ingreso.name) }
    var amount by remember { mutableStateOf(ingreso.amount.toString()) }
    var date by remember { mutableStateOf(ingreso.date) }
    var type by remember { mutableStateOf(ingreso.type) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar ingreso") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Monto") })

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Fecha") })

                Spacer(Modifier.height(10.dp))

                Text("Tipo de ingreso")

            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(name, amount.toDoubleOrNull() ?: 0.0, date, type)
            }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDelete) { Text("Eliminar", color = Color.Red) }
        }
    )
}
