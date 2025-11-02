package com.example.cashucontrol.ui.screens.finances

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AhorroScreen(
    onBackClick: () -> Unit,
    onAddObjetivoClick: (String) -> Unit // 👈 agregado para navegación
) {
    var metaTab by remember { mutableStateOf("Mediano plazo") }
    var isPressed by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val scaleAnim by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "ArrowScale"
    )

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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val interactionSource = remember { MutableInteractionSource() }

                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(32.dp)
                            .scale(scaleAnim)
                            .clickable(interactionSource = interactionSource, indication = null) {
                                isPressed = true
                                coroutineScope.launch {
                                    delay(150)
                                    isPressed = false
                                    onBackClick()
                                }
                            }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color.White)
                        .padding(horizontal = 50.dp, vertical = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Ahorro", color = Color(0xFF1A237E), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.Savings, contentDescription = null, tint = Color(0xFF1A237E))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        // 📊 Contenido principal
        Column(modifier = Modifier.padding(horizontal = 25.dp)) {
            Text("Metas", fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))
            Spacer(modifier = Modifier.height(10.dp))

            // Tabs metas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color(0xFFE0E0E0))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MetaTabButton("Corto plazo", metaTab == "Corto plazo", Color(0xFFFFEB3B)) { metaTab = "Corto plazo" }
                MetaTabButton("Mediano plazo", metaTab == "Mediano plazo", Color(0xFFFFEB3B)) { metaTab = "Mediano plazo" }
                MetaTabButton("Largo plazo", metaTab == "Largo plazo", Color(0xFFFFEB3B)) { metaTab = "Largo plazo" }
            }

            Spacer(modifier = Modifier.height(25.dp))

            AnimatedContent(
                targetState = metaTab,
                transitionSpec = {
                    slideInVertically(initialOffsetY = { fullHeight -> if (targetState == "Largo plazo") fullHeight else -fullHeight }) +
                            fadeIn(animationSpec = tween(500)) togetherWith
                            slideOutVertically(targetOffsetY = { fullHeight -> if (targetState == "Largo plazo") -fullHeight else fullHeight }) +
                            fadeOut(animationSpec = tween(500))
                }, label = ""
            ) { tab ->
                when (tab) {
                    "Corto plazo" -> MetaSection("Corto plazo", "Nuevo teclado → Q.500", "Q200.00 / 500.00", 0.4f, "40%") {
                        onAddObjetivoClick("Corto plazo")
                    }
                    "Mediano plazo" -> MetaSection("Mediano plazo", "Teléfono nuevo → Q.800", "Q300.00 / 800.00", 0.37f, "37%") {
                        onAddObjetivoClick("Mediano plazo")
                    }
                    "Largo plazo" -> MetaSection("Largo plazo", "Computadora nueva → Q.8000", "Q3000.00 / 8000.00", 0.25f, "25%") {
                        onAddObjetivoClick("Largo plazo")
                    }
                }
            }

            Spacer(modifier = Modifier.height(25.dp))
        }
    }
}

@Composable
fun MetaTabButton(text: String, active: Boolean, color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (active) color else Color(0xFFE0E0E0))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(
            text,
            color = if (active) Color.Black else Color.Gray,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
    }
}

@Composable
fun MetaSection(
    title: String,
    objetivo: String,
    metaTexto: String,
    progreso: Float,
    porcentaje: String,
    onAddObjetivoClick: () -> Unit
) {
    Text("Objetivos", fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))
    Spacer(modifier = Modifier.height(10.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFFFEBEE))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(objetivo, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }

    Spacer(modifier = Modifier.height(15.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(12.dp)
            .border(1.dp, Color(0xFFDDDDDD), RoundedCornerShape(10.dp))
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // 🔴 Botón que abre la nueva pantalla
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFF5252))
                    .clickable { onAddObjetivoClick() }
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("Añadir objetivos", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Total", fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5F5F5))
                    .padding(horizontal = 15.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Añadir cantidad", color = Color(0xFF1A237E), fontWeight = FontWeight.Bold)
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFDDDDDD)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("+", fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            LinearProgressIndicator(
                progress = progreso,
                color = Color(0xFF00C853),
                trackColor = Color(0xFFE0E0E0),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(metaTexto, color = Color.Black, fontWeight = FontWeight.Medium)
                Text(porcentaje, color = Color.Black, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF5F5F5))
                    .padding(horizontal = 15.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total", color = Color.Gray, fontWeight = FontWeight.Bold)
                Text("Q300.00", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}
