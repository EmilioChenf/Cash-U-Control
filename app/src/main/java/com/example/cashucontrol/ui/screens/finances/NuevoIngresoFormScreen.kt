package com.example.cashucontrol.ui.screens.finances

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ✅ IMPORTA la función IngresoChip desde IngresosScreen.kt
import com.example.cashucontrol.ui.screens.finances.IngresoChip

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NuevoIngresoFormScreen(onBackClick: () -> Unit) {
    var amount by remember { mutableStateOf(TextFieldValue("Q.0.00")) }
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var date by remember { mutableStateOf(TextFieldValue("")) }

    var isPressed by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val scaleAnim by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "Arrow Animation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // 🟩 Encabezado verde con flecha y título
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF00C853), RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                .padding(vertical = 25.dp, horizontal = 20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {

                // 🔙 Flecha con animación y retraso
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val interactionSource = remember { MutableInteractionSource() }

                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(32.dp)
                            .scale(scaleAnim)
                            .clickable(interactionSource = interactionSource, indication = null) {
                                isPressed = true
                                coroutineScope.launch {
                                    delay(150)
                                    isPressed = false
                                    onBackClick()
                                }
                            }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 🔲 Botón blanco con texto
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color.White)
                        .padding(horizontal = 45.dp, vertical = 10.dp)
                ) {
                    Text(
                        "Nuevo ingreso",
                        color = Color(0xFF1A237E),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        // 🧾 Campos de ingreso
        Column(modifier = Modifier.padding(horizontal = 25.dp)) {
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, color = Color.Black)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Nombre del ingreso", color = Color.Gray, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("Escribe aquí", color = Color(0xFFB0B0B0)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Fecha", color = Color.Gray, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                placeholder = { Text("Día/mes/año", color = Color(0xFFB0B0B0)) },
                singleLine = true,
                trailingIcon = {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Calendario", tint = Color.Gray)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🟢 Botón verde
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF00C853))
                    .clickable { /* guardar ingreso */ }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Guardar ingreso", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(25.dp))

            // 📋 Lista de ingresos guardados
            Text("Mis ingresos guardados", fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(10.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IngresoChip(Icons.Default.Work, "Trabajo medio tiempo", "Q.1000")
                IngresoChip(Icons.Default.LaptopMac, "Freelancer", "Q.500")
                IngresoChip(Icons.Default.AccountBalanceWallet, "Mesada", "Q.500")
            }

            Spacer(modifier = Modifier.height(25.dp))
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewNuevoIngresoForm() {
    NuevoIngresoFormScreen(
        onBackClick = {}
    )
}
