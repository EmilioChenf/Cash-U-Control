package com.example.cashucontrol.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.cashucontrol.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit   // 👈 NUEVO
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val viewModel = remember { AuthViewModel() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(80.dp))

        Text(
            "Iniciar Sesión",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF001F54)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- Correo
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- BOTÓN ENTRAR
        Button(
            onClick = {
                viewModel.loginUser(
                    email = email,
                    password = password,
                    onSuccess = { onLoginSuccess() },
                    onError = { println("Error al iniciar sesión") }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001F54)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Entrar", color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ⭐⭐⭐ BOTÓN NUEVO: IR A REGISTRO ⭐⭐⭐
        TextButton(onClick = { onGoToRegister() }) {
            Text(
                "¿No tienes cuenta? Crear cuenta",
                color = Color(0xFF001F54),
                fontWeight = FontWeight.Medium
            )
        }
    }
}
