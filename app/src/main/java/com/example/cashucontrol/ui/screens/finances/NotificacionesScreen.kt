package com.example.cashucontrol.ui.screens.finances

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.AccountCircle
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cashucontrol.viewmodel.NotificacionesViewModel


@Composable
fun NotificacionesScreen(onBackClick: () -> Unit) {

    val vm: NotificacionesViewModel = viewModel()
    val notificaciones by vm.notificaciones.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // ... header igual ...

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                "Borrar todo",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { vm.borrarTodo() }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (notificaciones.isEmpty()) {
            Text(
                "Aún no hay notificaciones",
                modifier = Modifier.padding(horizontal = 25.dp),
                color = Color.Gray
            )
        } else {
            notificaciones.forEach { n ->
                NotificacionItem(
                    titulo = n.title,
                    categoria = n.category,
                    texto = n.message
                )
            }
        }
    }
}

@Composable
fun NotificacionItem(
    titulo: String,
    categoria: String,
    texto: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF2F2F2))
            .padding(14.dp)
    ) {
        Column {
            Text("Notificación del sistema", fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(categoria, fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(2.dp))
            Text(texto, color = Color.Black, fontSize = 13.sp)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewNotificacionesScreen() {
    NotificacionesScreen(
        onBackClick = {}
    )
}

