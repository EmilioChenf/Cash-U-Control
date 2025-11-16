package com.example.cashucontrol.ui.screens.finances

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NuevoObjetivoScreen(selectedPlazo: String, onBackClick: () -> Unit) {
    var plazo by remember { mutableStateOf(selectedPlazo) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // 🟡 Encabezado amarillo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFEB3B), RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                .padding(vertical = 25.dp, horizontal = 20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = { onBackClick() }, modifier = Modifier.align(Alignment.Start)) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.Black)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color.White)
                        .padding(horizontal = 40.dp, vertical = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Nuevo objetivo de ahorro", color = Color(0xFF1A237E), fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Default.Savings, contentDescription = null, tint = Color(0xFF1A237E))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        Column(modifier = Modifier.padding(horizontal = 25.dp)) {
            Text("Nombre del objetivo", fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = "", onValueChange = {}, placeholder = { Text("Escribe aquí") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(15.dp))
            Text("Monto de meta", fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = "Q.0.00", onValueChange = {}, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(15.dp))
            Text("Plazo", fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color(0xFFE0E0E0))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("Corto plazo", "Mediano plazo", "Largo plazo").forEach {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (plazo == it) Color(0xFFFFEB3B) else Color(0xFFE0E0E0))
                            .clickable { plazo = it }
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(it, color = if (plazo == it) Color.Black else Color.Gray, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))
            Text("Fecha límite (Opcional)", fontWeight = FontWeight.Bold, color = Color.Gray.copy(alpha = 0.8f))
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = "", onValueChange = {}, placeholder = { Text("Día/mes/año") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFFEB3B))
                    .clickable { /* Guardar objetivo */ }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Guardar objetivo", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewNuevoObjetivoScreen() {
    NuevoObjetivoScreen(
        selectedPlazo = "Mediano plazo",
        onBackClick = {}
    )
}

