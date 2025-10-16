package com.example.cashucontrol.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.FirebaseDatabase
import com.example.cashucontrol.viewmodel.AuthViewModel


// Define tu color azul principal
val NavyBlue = Color(0xFF001F54)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterComplete: (String) -> Unit
) {
    val viewModel = remember { AuthViewModel() }

    var step by remember { mutableStateOf(1) } // Paso 1 o 2

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedCurrency by remember { mutableStateOf("GTQ") }
    var expanded by remember { mutableStateOf(false) }
    val currencies = listOf("GTQ", "USD", "EUR", "MXN", "COP", "CRC")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header (igual estilo que el ejemplo)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    NavyBlue,
                    RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .height(120.dp)
        ) {
            if (step == 2) {
                IconButton(
                    onClick = { step = 1 },
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
            }

            Text(
                text = if (step == 1) "Crear cuenta" else "Seleccionar moneda",
                fontSize = 28.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            if (step == 1) {
                // Paso 1: Datos del usuario
                // Nombre
                Text("Nombre", fontSize = 14.sp, color = Color.Gray)
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = NavyBlue,
                        unfocusedIndicatorColor = Color.LightGray,
                        focusedLabelColor = NavyBlue,
                        cursorColor = NavyBlue
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email
                Text("Correo electrónico", fontSize = 14.sp, color = Color.Gray)
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = NavyBlue,
                        unfocusedIndicatorColor = Color.LightGray,
                        focusedLabelColor = NavyBlue,
                        cursorColor = NavyBlue
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Contraseña
                Text("Contraseña", fontSize = 14.sp, color = Color.Gray)
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = NavyBlue,
                        unfocusedIndicatorColor = Color.LightGray,
                        focusedLabelColor = NavyBlue,
                        cursorColor = NavyBlue
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Botón siguiente
                Button(
                    onClick = {
                        if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                            step = 2
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Siguiente", fontSize = 16.sp)
                }
            } else {
                // Paso 2: Selección de moneda
                Text(
                    text = "Bienvenido, $name 👋",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Realiza un seguimiento de tus gastos y alcanza tus metas financieras.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Moneda preferida", fontSize = 14.sp, color = Color.Gray)
                Box {
                    OutlinedTextField(
                        value = selectedCurrency,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(
                                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                                    contentDescription = "Seleccionar moneda"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = NavyBlue,
                            unfocusedIndicatorColor = Color.LightGray,
                            focusedLabelColor = NavyBlue,
                            cursorColor = NavyBlue
                        )
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        currencies.forEach { currency ->
                            DropdownMenuItem(
                                text = { Text(currency) },
                                onClick = {
                                    selectedCurrency = currency
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Botón Iniciar
                Button(
                    onClick = {
                        viewModel.registerUser(
                            email = email,
                            password = password,
                            onSuccess = { userId ->
                                val db = FirebaseDatabase.getInstance().getReference("users")
                                val userMap = mapOf(
                                    "name" to name,
                                    "email" to email,
                                    "currency" to selectedCurrency
                                )
                                db.child(userId).setValue(userMap)
                                onRegisterComplete(name)
                            },
                            onError = { error ->
                                println("Error al registrar usuario: $error")
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Iniciar", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(onClick = { step = 1 }) {
                    Text("Volver", color = NavyBlue)
                }
            }
        }
    }
}
