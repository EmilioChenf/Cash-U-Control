package com.example.cashucontrol.ui.screens.finances

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfilScreen(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🔵 Encabezado azul con ícono
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF001F6B), RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                .padding(vertical = 40.dp),
            contentAlignment = Alignment.TopStart
        ) {
            IconButton(onClick = { onBackClick() }, modifier = Modifier.padding(start = 10.dp, top = 10.dp)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = Color.LightGray,
                    modifier = Modifier.size(90.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Campos del formulario
        var nombre by remember { mutableStateOf("Ana Sofía Lopez") }
        var correo by remember { mutableStateOf("anasofia02@gmail.com") }
        var contrasena by remember { mutableStateOf("123456") }

        Column(modifier = Modifier.padding(horizontal = 35.dp)) {

            // Nombre
            Text("Nombre", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 14.sp)
            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF2F2F2),
                    unfocusedContainerColor = Color(0xFFF2F2F2),
                    disabledContainerColor = Color(0xFFF2F2F2),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Correo
            Text("Correo electrónico", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 14.sp)
            TextField(
                value = correo,
                onValueChange = { correo = it },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF2F2F2),
                    unfocusedContainerColor = Color(0xFFF2F2F2),
                    disabledContainerColor = Color(0xFFF2F2F2),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Contraseña
            Text("Contraseña", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 14.sp)
            TextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF2F2F2),
                    unfocusedContainerColor = Color(0xFFF2F2F2),
                    disabledContainerColor = Color(0xFFF2F2F2),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Botón guardar
            Button(
                onClick = { /* Acción de guardar */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001F6B))
            ) {
                Text("Guardar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
