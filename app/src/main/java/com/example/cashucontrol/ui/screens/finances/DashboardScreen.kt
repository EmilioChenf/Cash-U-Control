package com.example.cashucontrol.ui.screens.finances

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.cashucontrol.ui.theme.CashUControlTheme
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DashboardScreen(
    onOpenIngresos: () -> Unit = {},
    onOpenGastos: () -> Unit = {},
    onOpenAhorro: () -> Unit = {},
    onOpenNotificaciones: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onViewInsignias: () -> Unit = {},
    onHelpCenter: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf("Ingresos") }
    var showContent by remember { mutableStateOf(false) }
    var showProfileMenu by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { showContent = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            HeaderSection(
                onOpenNotificaciones = onOpenNotificaciones,
                onProfileClick = { showProfileMenu = !showProfileMenu }
            )

            Spacer(modifier = Modifier.height(20.dp))

            AnimatedVisibility(
                visible = showContent,
                enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(animationSpec = tween(700))
            ) {
                TopCardsSection(
                    onIngresosClick = onOpenIngresos,
                    onGastosClick = onOpenGastos,
                    onAhorroClick = onOpenAhorro
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔽 Tabs inferiores
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color(0xFFE0E0E0))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TabButton("Ingresos", selectedTab == "Ingresos", Color(0xFF00C853)) { selectedTab = "Ingresos" }
                    TabButton("Gastos", selectedTab == "Gastos", Color(0xFFFF5252)) { selectedTab = "Gastos" }
                    TabButton("Ahorro", selectedTab == "Ahorro", Color(0xFFFFEB3B)) { selectedTab = "Ahorro" }
                }

                Spacer(modifier = Modifier.height(20.dp))

                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        slideInHorizontally(initialOffsetX = { if (targetState == "Gastos") it else -it }) +
                                fadeIn(animationSpec = tween(500)) togetherWith
                                slideOutHorizontally(targetOffsetX = { if (targetState == "Gastos") -it else it }) +
                                fadeOut(animationSpec = tween(500))
                    },
                    label = ""
                ) { tab ->
                    when (tab) {
                        "Ingresos" -> IngresosContent()
                        "Gastos" -> GastosContent()
                        "Ahorro" -> AhorroPreviewContent()
                    }
                }
            }
        }

        // 🧩 Popup flotante del perfil
        if (showProfileMenu) {
            ProfilePopup(
                onDismiss = { showProfileMenu = false },
                onEditProfile = {
                    showProfileMenu = false
                    onEditProfile()
                },
                onViewInsignias = {
                    showProfileMenu = false
                    onViewInsignias()
                },
                onHelpCenter = {
                    showProfileMenu = false
                    onHelpCenter()
                },
                onLogout = {
                    showProfileMenu = false
                    onLogout()
                }
            )
        }
    }
}

// -------------------- ENCABEZADO --------------------

@Composable
fun HeaderSection(
    onOpenNotificaciones: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF001F6B), RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
            .padding(horizontal = 20.dp, vertical = 25.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    tint = Color.White,
                    modifier = Modifier.clickable { onOpenNotificaciones() }
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Hola ", color = Color.White, fontSize = 20.sp)
                    Text("Ana Sofía!", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = "Perfil",
                    tint = Color.White,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onProfileClick() } // ✅ abre menú flotante
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(vertical = 14.dp, horizontal = 30.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Saldo actual", color = Color.Gray, fontSize = 14.sp)
                    Text("Q. 3,300.00", color = Color.Black, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text("Pequeños pasos crean grandes logros.", color = Color(0xFFD0D0D0), fontSize = 13.sp)
        }
    }
}
@Composable
fun ProfilePopup(
    onDismiss: () -> Unit,
    onEditProfile: () -> Unit,
    onViewInsignias: () -> Unit,
    onHelpCenter: () -> Unit,
    onLogout: () -> Unit
) {
    Popup(
        alignment = Alignment.TopEnd,
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true)
    ) {
        Column(
            modifier = Modifier
                .padding(top = 90.dp, end = 20.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White)
                .border(1.dp, Color.LightGray, RoundedCornerShape(15.dp))
        ) {
            ProfileMenuItem(Icons.Default.Person, "Editar perfil", onEditProfile)
            ProfileMenuItem(Icons.Default.EmojiEvents, "Ver insignias", onViewInsignias)
            ProfileMenuItem(Icons.Default.HeadsetMic, "Centro de ayuda", onHelpCenter)
            Divider(color = Color.LightGray, thickness = 1.dp)
            ProfileMenuItem(Icons.Default.ExitToApp, "Cerrar sesión", onLogout)
        }
    }
}

@Composable
fun ProfileMenuItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 15.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color.Gray)
        Spacer(modifier = Modifier.width(10.dp))
        Text(text, color = Color.Gray, fontWeight = FontWeight.Medium)
    }
}

// -------------------- TARJETAS SUPERIORES --------------------

@Composable
fun TopCardsSection(
    onIngresosClick: () -> Unit = {},
    onGastosClick: () -> Unit = {},
    onAhorroClick: () -> Unit = {} // ✅ añadido
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FinanceCard("Ingresos", Icons.Default.AttachMoney, Color(0xFF00C853), "Q. 2,500.00", onClick = onIngresosClick)
            FinanceCard("Gastos", Icons.Default.MoneyOff, Color(0xFFFF5252), "Q. 1,250.00", onClick = onGastosClick)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🟡 Tarjeta de ahorro clickeable
        Box(
            modifier = Modifier
                .width(160.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color(0xFFFFF59D))
                .clickable { onAhorroClick() } // ✅ ahora abre la pantalla de ahorro
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Savings, contentDescription = null, tint = Color(0xFF001F6B))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Ahorro", color = Color(0xFF001F6B), fontWeight = FontWeight.Bold)
            }
        }
    }
}

// -------------------- CONTENIDOS --------------------

@Composable
fun IngresosContent() {
    Column {
        IncomeItem(Icons.Default.Work, "Trabajo medio tiempo", "Q.1000")
        IncomeItem(Icons.Default.LaptopMac, "Freelancer", "Q.500")
        IncomeItem(Icons.Default.AccountBalanceWallet, "Mesada", "Q.500")

        Spacer(modifier = Modifier.height(20.dp))
        Text("Llevas 17 días registrando", color = Color.Gray, fontSize = 13.sp)
        Text("Meta mensual Q.2,500.00", fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        ProgressCard("Alcanzado un 90%", "¡Vas muy bien, sigue así!", 0.9f, Color(0xFF00C853))
        Spacer(modifier = Modifier.height(20.dp))
        MovimientosSection()
    }
}

@Composable
fun GastosContent() {
    Column {
        IncomeItem(Icons.Default.School, "Universidad", "Q.1000", color = Color(0xFFFF5252))
        IncomeItem(Icons.Default.Fastfood, "Comida", "Q.500", color = Color(0xFFFF5252))
        IncomeItem(Icons.Default.DirectionsBus, "Transporte", "Q.300", color = Color(0xFFFF5252))
        IncomeItem(Icons.Default.MusicNote, "Spotify", "Q.45", color = Color(0xFFFF5252))

        Spacer(modifier = Modifier.height(20.dp))
        Text("Llevas 17 días registrando", color = Color.Gray, fontSize = 13.sp)
        Text("Presupuesto mensual", fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        ProgressCard("Usado 85%", "¡Ya casi llegas al límite!", 0.85f, Color(0xFFFF5252))
        Spacer(modifier = Modifier.height(20.dp))
        MovimientosSection()
    }
}

@Composable
fun AhorroPreviewContent() {
    Column {
        Text("Metas activas", fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))
        Spacer(modifier = Modifier.height(10.dp))
        MetaCard("Teléfono nuevo → Q.800", 0.37f, "Q300.00 / 800.00", "37%")
        Spacer(modifier = Modifier.height(20.dp))
        ProgressCard("Ahorrado un 37%", "¡Sigue con tu meta!", 0.37f, Color(0xFF00C853))
        Spacer(modifier = Modifier.height(20.dp))
        MovimientosSection()
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AhorroContent() {
    var metaTab by remember { mutableStateOf("Mediano plazo") }

    Column {
        Text("Metas", fontWeight = FontWeight.Bold)
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
            TabButton("Corto plazo", metaTab == "Corto plazo", Color(0xFFFFEB3B)) { metaTab = "Corto plazo" }
            TabButton("Mediano plazo", metaTab == "Mediano plazo", Color(0xFFFFEB3B)) { metaTab = "Mediano plazo" }
            TabButton("Largo plazo", metaTab == "Largo plazo", Color(0xFFFFEB3B)) { metaTab = "Largo plazo" }
        }

        Spacer(modifier = Modifier.height(20.dp))

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
                "Corto plazo" -> MetaCard("Nuevo teclado → Q.300 / Q.500.00", 0.6f, "Q300.00 / 500.00", "60%")
                "Mediano plazo" -> MetaCard("Teléfono nuevo → Q.800 / Q.2,000.00", 0.37f, "Q300.00 / 800.00", "37%")
                "Largo plazo" -> MetaCard("Computadora nueva → Q.3,000 / Q.8,000.00", 0.25f, "Q3,000.00 / 8,000.00", "25%")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        MovimientosSection()
    }
}

// -------------------- COMPONENTES --------------------

@Composable
fun MetaCard(text: String, progress: Float, leftText: String, rightText: String) {
    Text("Objetivos", fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))
    Spacer(modifier = Modifier.height(10.dp))

    // Tarjeta rosada con candado y texto
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
            Text(text, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }

    Spacer(modifier = Modifier.height(15.dp))

    // Barra de progreso con texto debajo
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFFFEBEE))
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Column {
            LinearProgressIndicator(
                progress = progress,
                color = Color(0xFF00C853),
                trackColor = Color(0xFFE0E0E0),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(6.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(leftText, color = Color.Black, fontWeight = FontWeight.Medium)
                Text(rightText, color = Color(0xFF00C853), fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Composable
fun FinanceCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    amount: String,
    onClick: (() -> Unit)? = null // 👈 Nuevo parámetro
) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .clickable(enabled = onClick != null) { onClick?.invoke() } // 👈 Hace clic si hay acción
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = null, tint = color)
        Text(title, color = color, fontWeight = FontWeight.Bold)
        Text("Asignado:", color = Color.Gray, fontSize = 12.sp)
        Text(amount, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}

@Composable
fun TabButton(text: String, active: Boolean, color: Color, onClick: () -> Unit) {
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
fun IncomeItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, amount: String, color: Color = Color(0xFF00C853)) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(32.dp).clip(CircleShape).background(color),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, fontSize = 14.sp)
        }
        Text(amount, color = Color.Black, fontWeight = FontWeight.Bold)
    }
    Spacer(modifier = Modifier.height(8.dp))
    LinearProgressIndicator(
        progress = 1f,
        color = color,
        trackColor = Color(0xFFE0E0E0),
        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(6.dp))
    )
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun ProgressCard(leftText: String, rightText: String, progress: Float, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFF5F5F5))
            .padding(10.dp)
    ) {
        Column {
            LinearProgressIndicator(
                progress = progress,
                color = color,
                trackColor = Color(0xFFE0E0E0),
                modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(10.dp))
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(leftText, color = Color.Gray, fontSize = 13.sp)
                Text(rightText, color = color, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun MovimientosSection() {
    Spacer(modifier = Modifier.height(10.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("Movimientos", fontWeight = FontWeight.Bold)
        Text("Ver todo", color = Color.Gray, fontSize = 13.sp)
    }
    Spacer(modifier = Modifier.height(10.dp))
    MovementItem("Suscripción de Spotify", "31 de enero", "-Q.45.00", Color(0xFFFF5252))
    MovementItem("Mesada", "15 de enero", "+Q.1000.00", Color(0xFF00C853))
}

@Composable
fun MovementItem(title: String, date: String, amount: String, color: Color) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text(title, color = color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(date, color = Color.Gray, fontSize = 12.sp)
        }
        Text(amount, color = color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Preview(showBackground = true)
@Composable
fun PreviewDashboardScreen() {
    CashUControlTheme {
        DashboardScreen()
    }
}
