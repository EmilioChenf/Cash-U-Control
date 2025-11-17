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
import com.example.cashucontrol.models.Gasto
import com.example.cashucontrol.viewmodel.GastosViewModel
import java.util.*

@Composable
fun NuevoGastoFormScreen(onBackClick: () -> Unit) {

    val vm = remember { GastosViewModel() }
    val gastosList by vm.gastosMes.collectAsState()

    var amount by remember { mutableStateOf(TextFieldValue("")) }
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var date by remember { mutableStateOf(TextFieldValue("")) }

    var editing by remember { mutableStateOf<Gasto?>(null) }

    val ctx = LocalContext.current

    fun openCalendar() {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            ctx,
            { _, y, m, d -> date = TextFieldValue("$d/${m + 1}/$y") },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).background(Color.White)
    ) {

        // HEADER
        Box(
            Modifier.fillMaxWidth().background(Color(0xFFFF5252),
                RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
            ).padding(20.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, "", tint = Color.Black)
            }
            Text(
                "Gastos",
                Modifier.align(Alignment.Center),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF1A237E)
            )
        }

        Spacer(Modifier.height(20.dp))

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
                label = { Text("Nombre del gasto") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Fecha") },
                trailingIcon = {
                    IconButton(onClick = { openCalendar() }) {
                        Icon(Icons.Default.CalendarMonth, "")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    if (name.text.isBlank() || amount.text.isBlank()) return@Button

                    if (editing == null) {
                        vm.saveGasto(name.text, amount.text.toDouble(), date.text)
                    } else {
                        vm.updateGasto(editing!!.id, name.text, amount.text.toDouble(), date.text)
                        editing = null
                    }

                    name = TextFieldValue("")
                    amount = TextFieldValue("")
                    date = TextFieldValue("")
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFFF5252))
            ) {
                Text(if (editing == null) "Guardar gasto" else "Actualizar gasto", color = Color.White)
            }

            Spacer(Modifier.height(30.dp))

            Text("Mis gastos del mes", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))

            gastosList.forEach { gasto ->
                GastoRow(
                    gasto = gasto,
                    onClick = {
                        editing = gasto
                        name = TextFieldValue(gasto.name)
                        amount = TextFieldValue(gasto.amount.toString())
                        date = TextFieldValue(gasto.date)
                    },
                    onDelete = { vm.deleteGasto(gasto.id) }
                )
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun GastoRow(gasto: Gasto, onClick: () -> Unit, onDelete: () -> Unit) {

    Row(
        Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF5F5F5))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(Modifier.weight(1f)) {
            Text(gasto.name, fontWeight = FontWeight.Bold)
            Text(gasto.date, fontSize = 12.sp, color = Color.Gray)
        }

        Text("Q${gasto.amount}", fontWeight = FontWeight.Bold)

        IconButton(onClick = onClick) {
            Icon(Icons.Default.Edit, "")
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, tint = Color.Red, contentDescription = "")
        }
    }
}
