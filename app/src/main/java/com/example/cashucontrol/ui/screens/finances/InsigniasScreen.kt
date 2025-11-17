package com.example.cashucontrol.ui.screens.finances

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cashucontrol.models.Achievement
import com.example.cashucontrol.viewmodel.AchievementsViewModel

@Composable
fun AchievementsScreen(onBackClick: () -> Unit) {

    val vm: AchievementsViewModel = viewModel()
    val achievements by vm.achievements.collectAsState()

    var selected by remember { mutableStateOf<Achievement?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Encabezado simple
        Row(
            Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "")
            }
            Text("Mis Logros", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // GRID DINÁMICO
        val rows = achievements.chunked(2)

        rows.forEach { row ->
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { ach ->
                    AchievementItem(
                        achievement = ach,
                        onClick = { selected = ach }
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Detalles
    selected?.let { ach ->
        Dialog(onDismissRequest = { selected = null }) {
            AchievementDialog(achievement = ach) {
                selected = null
            }
        }
    }
}

@Composable
fun AchievementItem(achievement: Achievement, onClick: () -> Unit) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(
                    if (achievement.completed) Color(0xFFE0F7FA)
                    else Color(0xFFE0E0E0)
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(if (achievement.completed) "🏅" else "🔒", fontSize = 32.sp)
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            achievement.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            achievement.description,
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
    }
}

@Composable
fun AchievementDialog(achievement: Achievement, onClose: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(if (achievement.completed) "🏅" else "🔒", fontSize = 50.sp)

            Spacer(modifier = Modifier.height(10.dp))

            Text(achievement.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = achievement.progress.toFloat(),
                modifier = Modifier.fillMaxWidth().height(10.dp),
                color = if (achievement.completed) Color(0xFF00C853) else Color.Gray
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                "${(achievement.progress * 100).toInt()}%",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                achievement.description,
                textAlign = TextAlign.Center,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = onClose) {
                Text("Cerrar")
            }
        }
    }
}
