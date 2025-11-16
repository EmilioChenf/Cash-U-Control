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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IngresosScreen(
    onBackClick: () -> Unit,
    onAddIngresoClick: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var inputValue by remember { mutableStateOf("Q. 2,500.00") }
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
        // 🔵 Encabezado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF00C853), RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                .padding(vertical = 25.dp, horizontal = 20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                // 🔙 Flecha con animación
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

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color.White)
                        .padding(horizontal = 45.dp, vertical = 10.dp)
                ) {
                    Text("Ingresos", color = Color(0xFF1A237E), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        // 💵 Contenido principal
        Column(modifier = Modifier.padding(horizontal = 25.dp)) {
            Text("Ingreso mensual asignado", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEDEDED))
                    .padding(horizontal = 18.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(inputValue, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFD9D9D9))
                        .clickable { showDialog = true }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("Cambiar", fontSize = 12.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            Text("Añade tus ingresos", fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(15.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IngresoChip(Icons.Default.Work, title = "Trabajo medio tiempo", amount = "Q.1000")
                IngresoChip(Icons.Default.LaptopMac, title = "Freelancer", amount = "Q.500")
                IngresoChip(Icons.Default.AccountBalanceWallet, title = "Mesada", amount = "Q.500")
            }

            Spacer(modifier = Modifier.height(25.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total acumulado", fontWeight = FontWeight.Bold, color = Color.Gray)
                Text("Q.2000", fontWeight = FontWeight.Bold, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(25.dp))

            Text("Meta mensual 2,500.00", fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color(0xFFF5F5F5))
                    .padding(10.dp)
            ) {
                Column {
                    LinearProgressIndicator(
                        progress = 0.9f,
                        color = Color(0xFF00C853),
                        trackColor = Color(0xFFE0E0E0),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Alcanzado un 90%", color = Color.Gray, fontSize = 13.sp)
                        Text("¡Vas muy bien, sigue así!", color = Color.Gray, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // 🟢 Botón Añadir Ingreso
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF00C853))
                    .clickable { onAddIngresoClick() }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Añadir ingreso", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(25.dp))
        }
    }

    // 🪟 Diálogo de “Cambiar cantidad”
    if (showDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(20.dp)
                    .width(260.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable { showDialog = false }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = inputValue,
                        onValueChange = { inputValue = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF00C853))
                            .clickable { showDialog = false }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Guardar cantidad", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// 🔹 CHIP reutilizable (única versión)
@Composable
fun IngresoChip(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, amount: String) {
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
        Text(title, fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.width(6.dp))
        Text(amount, color = Color.Gray, fontSize = 12.sp)
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewIngresosScreen() {
    IngresosScreen(
        onBackClick = {},
        onAddIngresoClick = {}
    )
}