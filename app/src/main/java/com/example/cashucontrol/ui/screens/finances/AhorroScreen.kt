package com.example.cashucontrol.ui.screens.finances

import android.app.DatePickerDialog
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cashucontrol.viewmodel.AhorrosViewModel
import com.example.cashucontrol.models.Ahorro
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AhorroScreen(
    onBackClick: () -> Unit,
    onAddObjetivoClick: (String) -> Unit
) {
    val vm = remember { AhorrosViewModel() }
    val ahorros by vm.ahorros.collectAsState()

    var metaTab by remember { mutableStateOf("Mediano plazo") }
    var showAporteDialog by remember { mutableStateOf<Ahorro?>(null) }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
    ) {

        // ============================================================
        // HEADER con CERDITO (DISEÑO PROFESIONAL)
        // ============================================================
        Box(
            Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFFFFEB3B),
                    RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
                )
                .padding(vertical = 25.dp, horizontal = 20.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Icon(Icons.Default.ArrowBack, "", tint = Color.Black)
                }

                Spacer(Modifier.height(12.dp))

                // Caja blanca con texto + cerdito
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color.White)
                        .padding(horizontal = 40.dp, vertical = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Text(
                            "Ahorro",
                            color = Color(0xFF1A237E),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        Spacer(Modifier.width(8.dp))

                        Icon(
                            Icons.Default.Savings,
                            contentDescription = null,
                            tint = Color(0xFF1A237E),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(25.dp))

        Column(Modifier.padding(horizontal = 25.dp)) {

            // ============================================================
            // TABS
            // ============================================================
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color(0xFFE0E0E0))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("Corto plazo", "Mediano plazo", "Largo plazo").forEach { tab ->

                    Box(
                        Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (metaTab == tab) Color(0xFFFFEB3B)
                                else Color.Transparent
                            )
                            .clickable { metaTab = tab }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            tab,
                            fontWeight = FontWeight.Bold,
                            color = if (metaTab == tab) Color.Black else Color.Gray
                        )
                    }
                }
            }

            Spacer(Modifier.height(25.dp))

            // ============================================================
            // ANIMACIÓN ENTRE PESTAÑAS
            // ============================================================
            AnimatedContent(
                targetState = metaTab,
                transitionSpec = {
                    slideInVertically(
                        animationSpec = tween(300),
                        initialOffsetY = { fullHeight ->
                            when (targetState) {
                                "Corto plazo" -> -fullHeight
                                "Largo plazo" -> fullHeight
                                else -> fullHeight / 3
                            }
                        }
                    ) + fadeIn(animationSpec = tween(300)) togetherWith
                            slideOutVertically(
                                animationSpec = tween(300),
                                targetOffsetY = { fullHeight ->
                                    when (initialState) {
                                        "Corto plazo" -> fullHeight
                                        "Largo plazo" -> -fullHeight
                                        else -> -fullHeight / 3
                                    }
                                }
                            ) + fadeOut(animationSpec = tween(300))
                },
                label = ""
            ) { tab ->

                val metas = ahorros.filter { it.plazo == tab }

                Column {

                    Text("Objetivos de $tab", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(15.dp))

                    metas.forEach { objetivo ->

                        AhorroCard(
                            ahorro = objetivo,
                            onAporteClick = { showAporteDialog = objetivo }
                        )

                        Spacer(Modifier.height(20.dp))
                    }
                }
            }

            Spacer(Modifier.height(30.dp))

            // ============================================================
            // BOTÓN AÑADIR NUEVO OBJETIVO
            // ============================================================
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFFEB3B))
                    .clickable { onAddObjetivoClick(metaTab) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Añadir nuevo objetivo", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(60.dp))
        }
    }

    // ============================================================
    // DIÁLOGO PARA AGREGAR AHORRO
    // ============================================================
    if (showAporteDialog != null) {
        DialogAgregarAporte(
            ahorro = showAporteDialog!!,
            onDismiss = { showAporteDialog = null },
            onSave = { amount, date ->
                vm.addAporte(showAporteDialog!!.id, amount, date)
                showAporteDialog = null
            }
        )
    }
}

@Composable
fun AhorroCard(ahorro: Ahorro, onAporteClick: () -> Unit) {

    val progreso = (ahorro.totalSaved / ahorro.goalAmount).coerceIn(0.0, 1.0)

    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFFFFDE7))
            .padding(16.dp)
            .border(1.dp, Color(0xFFE6E6E6), RoundedCornerShape(20.dp))
    ) {

        Column {

            Text(ahorro.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("Meta: Q${ahorro.goalAmount}", color = Color.Gray)
            if (ahorro.deadline.isNotBlank())
                Text("Límite: ${ahorro.deadline}", color = Color.Gray)

            Spacer(Modifier.height(12.dp))

            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFFEB3B))
                    .clickable { onAporteClick() }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("+ Agregar ahorro", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(15.dp))

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(18.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFE0E0E0))
            ) {
                Box(
                    Modifier
                        .fillMaxWidth(progreso.toFloat())
                        .height(18.dp)
                        .background(Color(0xFF00C853))
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Ahorrado: Q${ahorro.totalSaved}", fontWeight = FontWeight.Bold)
                Text("${(progreso * 100).toInt()}%", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun DialogAgregarAporte(
    ahorro: Ahorro,
    onDismiss: () -> Unit,
    onSave: (Double, String) -> Unit
) {

    var cantidad by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }

    val ctx = LocalContext.current

    fun openCalendar() {
        val c = Calendar.getInstance()
        DatePickerDialog(
            ctx,
            { _, y, m, d -> fecha = "$d/${m + 1}/$y" },
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar ahorro") },
        text = {
            Column {

                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { cantidad = it },
                    label = { Text("Cantidad (Q)") }
                )

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = fecha,
                    onValueChange = { fecha = it },
                    label = { Text("Fecha") },
                    trailingIcon = {
                        IconButton(onClick = { openCalendar() }) {
                            Icon(Icons.Default.CalendarMonth, "")
                        }
                    }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(cantidad.toDouble(), fecha)
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
