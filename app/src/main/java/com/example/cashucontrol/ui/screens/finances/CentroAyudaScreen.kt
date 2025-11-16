package com.example.cashucontrol.ui.screens.finances

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CentroAyudaScreen(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🔵 Encabezado
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
                Text(
                    "Guía para principiantes",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
        Text(
            "Tu mejor aliado financiero:\n" +
                    "descarga la guía y comienza a usar la app como un experto.",
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontSize = 15.sp,
            modifier = Modifier.padding(horizontal = 30.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = { /* Acción de descarga */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0))
        ) {
            Text("Descargar", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(25.dp))

        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFE0E0E0)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Download, contentDescription = "Descargar", tint = Color.Black, modifier = Modifier.size(35.dp))
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewCentroAyudaScreen() {
    CentroAyudaScreen(
        onBackClick = {}
    )
}

