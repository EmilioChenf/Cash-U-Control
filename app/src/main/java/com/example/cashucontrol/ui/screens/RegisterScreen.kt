package com.example.cashucontrol.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.database.FirebaseDatabase
import com.example.cashucontrol.viewmodel.AuthViewModel

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
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (step == 1) {
            // Paso 1: Datos del usuario
            Text(
                text = "Crear cuenta",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                        step = 2
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Siguiente")
            }
        }

        if (step == 2) {
            // Paso 2: Selección de moneda
            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Realice un seguimiento de sus gastos, establezca objetivos y reciba recordatorios.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Moneda", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))

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
                    modifier = Modifier.fillMaxWidth()
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

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Crear usuario en Firebase
                    // En RegisterScreen.kt, dentro del onClick del botón "Iniciar"
                    viewModel.registerUser(
                        email = email,
                        password = password,
                        onSuccess = { userId -> // <- 'userId' es recibido aquí directamente
                            // No necesitas llamar a getCurrentUserId()
                            val db = FirebaseDatabase.getInstance().getReference("users")
                            val userMap = mapOf(
                                "name" to name,
                                "email" to email,
                                "currency" to selectedCurrency
                            )
                            db.child(userId).setValue(userMap) // <- Usas el userId directamente
                            onRegisterComplete(name)
                        },
                        onError = { error ->
                            println("Error al registrar usuario: $error")
                        }
                    )

                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar")
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = { step = 1 }) {
                Text("Volver")
            }
        }
    }
}
