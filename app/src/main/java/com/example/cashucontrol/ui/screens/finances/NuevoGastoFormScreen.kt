package com.example.cashucontrol.ui.screens.finances

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NuevoGastoFormScreen(
    onBackClick: () -> Unit
) {
    var cantidad by remember { mutableStateOf(TextFieldValue("Q.0.00")) }
    var nombreGasto by remember { mutableStateOf(TextFieldValue("")) }
    var fecha by remember { mutableStateOf(TextFieldValue("")) }
    var isPressed by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val scaleAnim by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "ArrowScale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // 🔴 Encabezado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFF5252), RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                .padding(vertical = 25.dp, horizontal = 20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {

                // 🔙 Flecha animada
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
                                    onBackClick() // ← regresa a GastosScreen
                                }
                            }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color.White)
                        .padding(horizontal = 50.dp, vertical = 10.dp)
                ) {
                    Text("Gastos", color = Color(0xFF1A237E), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        // 📋 Formulario principal
        Column(modifier = Modifier.padding(horizontal = 25.dp)) {
            // Cantidad
            Text("Cantidad", fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = cantidad,
                onValueChange = { cantidad = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF5252),
                    unfocusedBorderColor = Color(0xFFDDDDDD)
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Nombre del gasto
            Text("Nombre del gasto", fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = nombreGasto,
                onValueChange = { nombreGasto = it },
                placeholder = { Text("Escribe aquí", color = Color.LightGray) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF5252),
                    unfocusedBorderColor = Color(0xFFDDDDDD)
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Fecha
            Text("Fecha", fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                placeholder = { Text("Día/mes/año", color = Color.LightGray) },
                singleLine = true,
                trailingIcon = {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color.Gray)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF5252),
                    unfocusedBorderColor = Color(0xFFDDDDDD)
                )
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Botón guardar gasto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFF5252))
                    .clickable { /* lógica para guardar */ }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Guardar gasto", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text("Mis gastos guardados", fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                GastoChip(Icons.Default.School, "Universidad", "Q.1000")
                GastoChip(Icons.Default.Fastfood, "Comida", "Q.500")
                GastoChip(Icons.Default.DirectionsBus, "Transporte", "Q.300")
                GastoChip(Icons.Default.MusicNote, "Spotify", "Q.45")
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
