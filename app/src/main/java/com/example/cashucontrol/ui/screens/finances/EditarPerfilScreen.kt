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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cashucontrol.viewmodel.EditarPerfilViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfilScreen(onBackClick: () -> Unit) {

    val vm = remember { EditarPerfilViewModel() }

    val nombre by vm.nombre.collectAsState()
    val correo by vm.correo.collectAsState()
    val mensaje by vm.mensaje.collectAsState()

    var nombreEdit by remember { mutableStateOf("") }
    var correoEdit by remember { mutableStateOf("") }
    var contrasenaEdit by remember { mutableStateOf("") }

    // Sincronizar datos iniciales
    LaunchedEffect(nombre, correo) {
        if (nombreEdit.isEmpty()) nombreEdit = nombre
        if (correoEdit.isEmpty()) correoEdit = correo
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🔵 Encabezado azul
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

        // Formularios
        Column(modifier = Modifier.padding(horizontal = 35.dp)) {

            // Nombre
            Text("Nombre", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 14.sp)
            TextField(
                value = nombreEdit,
                onValueChange = { nombreEdit = it },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF2F2F2),
                    unfocusedContainerColor = Color(0xFFF2F2F2),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Correo
            Text("Correo electrónico", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 14.sp)
            TextField(
                value = correoEdit,
                onValueChange = { correoEdit = it },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF2F2F2),
                    unfocusedContainerColor = Color(0xFFF2F2F2),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Contraseña
            Text("Contraseña", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 14.sp)
            TextField(
                value = contrasenaEdit,
                onValueChange = { contrasenaEdit = it },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF2F2F2),
                    unfocusedContainerColor = Color(0xFFF2F2F2),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Botón guardar
            Button(
                onClick = {
                    vm.actualizarPerfil(nombreEdit, correoEdit, contrasenaEdit.ifEmpty { null })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001F6B))
            ) {
                Text("Guardar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            if (mensaje.isNotEmpty()) {
                Spacer(Modifier.height(10.dp))
                Text(mensaje, color = Color(0xFF00C853), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEditarPerfilScreen() {
    EditarPerfilScreen(
        onBackClick = {}
    )
}

