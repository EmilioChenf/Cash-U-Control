package com.example.cashucontrol.ui.screens.finances

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.cashucontrol.viewmodel.IngresosViewModel
import com.example.cashucontrol.viewmodel.GastosViewModel
import com.example.cashucontrol.viewmodel.AhorrosViewModel
import com.example.cashucontrol.viewmodel.MonthlyGoalViewModel
import com.example.cashucontrol.viewmodel.MonthlyGoalGastosViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.max
import androidx.compose.ui.platform.LocalContext
import com.example.cashucontrol.notifications.scheduleDailyNotification
import java.util.Calendar
import kotlin.math.max


// =======================================================================
//                          DASHBOARD COMPLETO
// =======================================================================

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DashboardScreen(
    onOpenIngresos: () -> Unit,
    onOpenGastos: () -> Unit,
    onOpenAhorro: () -> Unit,
    onOpenNotificaciones: () -> Unit,
    onEditProfile: () -> Unit,
    onViewInsignias: () -> Unit,
    onHelpCenter: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    // ===================  VIEWMODELS  =====================
    val ingresosVM = remember { IngresosViewModel() }
    val gastosVM = remember { GastosViewModel() }
    val ahorroVM = remember { AhorrosViewModel() }
    val goalIncomeVM = remember { MonthlyGoalViewModel() }
    val goalGastosVM = remember { MonthlyGoalGastosViewModel() }

    val ingresosMes by ingresosVM.ingresosMes.collectAsState()
    val gastosMes by gastosVM.gastosMes.collectAsState()
    val ahorros by ahorroVM.ahorros.collectAsState()

    val metaIngresos = goalIncomeVM.goal.collectAsState().value?.goalAmount ?: 0.0
    val metaGastos = goalGastosVM.goal.collectAsState().value?.goalAmount ?: 0.0

    // =================== CALCULOS =========================
    val totalIngresos = ingresosMes.sumOf { it.amount }
    val totalGastos = gastosMes.sumOf { it.amount }
    val saldoActual = totalIngresos - totalGastos

    // MOVIMIENTOS
    val movimientos = remember(ingresosMes, gastosMes) {
        (ingresosMes.map {
            Movimiento(it.name, it.amount, it.date, "ingreso")
        } + gastosMes.map {
            Movimiento(it.name, it.amount, it.date, "gasto")
        }).sortedByDescending { parseFechaDDMMYYYY(it.date) }
    }

    var selectedTab by remember { mutableStateOf("Ingresos") }
    var showContent by remember { mutableStateOf(false) }
    var showProfileMenu by remember { mutableStateOf(false) }
    var showAllMovimientos by remember { mutableStateOf(false) }
    var showChart by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { showContent = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

            HeaderSection(
                saldoActual = saldoActual,
                onOpenNotificaciones = onOpenNotificaciones,
                onProfileClick = { showProfileMenu = !showProfileMenu }
            )

            Spacer(modifier = Modifier.height(20.dp))

            AnimatedVisibility(
                visible = showContent,
                enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(tween(700))
            ) {
                TopCardsSection(
                    ingresos = totalIngresos,
                    gastos = totalGastos,
                    ahorro = ahorros.sumOf { it.totalSaved },
                    onIngresosClick = onOpenIngresos,
                    onGastosClick = onOpenGastos,
                    onAhorroClick = onOpenAhorro
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // =================== TABS ===================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {

                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color(0xFFE0E0E0))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TabButton("Ingresos", selectedTab == "Ingresos", Color(0xFF00C853)) {
                        selectedTab = "Ingresos"
                    }
                    TabButton("Gastos", selectedTab == "Gastos", Color(0xFFFF5252)) {
                        selectedTab = "Gastos"
                    }
                    TabButton("Ahorro", selectedTab == "Ahorro", Color(0xFFFFEB3B)) {
                        selectedTab = "Ahorro"
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        slideInHorizontally { if (targetState == "Gastos") it else -it } +
                                fadeIn(tween(500)) togetherWith
                                slideOutHorizontally { if (targetState == "Gastos") -it else it } +
                                fadeOut(tween(500))
                    },
                    label = ""
                ) { tab ->
                    when (tab) {
                        "Ingresos" -> IngresosDashboardContent(totalIngresos, metaIngresos, ingresosMes)
                        "Gastos" -> GastosDashboardContent(totalGastos, metaGastos, gastosMes)
                        "Ahorro" -> AhorroDashboardContent(ahorros)
                    }
                }

                Spacer(Modifier.height(20.dp))

                // ================= MOVIMIENTOS =================
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Movimientos",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        "Ver gráfica",
                        color = Color.Gray,
                        modifier = Modifier
                            .clickable { showChart = true }
                    )

                    Spacer(Modifier.width(16.dp))

                    Text(
                        "Ver todo",
                        color = Color.Gray,
                        modifier = Modifier
                            .clickable { showAllMovimientos = true }
                    )
                }


                Spacer(Modifier.height(12.dp))

                movimientos.take(5).forEach {
                    MovimientoRowC(it)
                    Spacer(Modifier.height(10.dp))
                }
            }
            // 🔔 BOTÓN DE PRUEBA: NOTIFICACIÓN EN 1 MINUTO
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val cal = Calendar.getInstance().apply { add(Calendar.MINUTE, 1) }

                    scheduleDailyNotification(
                        context = context,
                        hour = cal.get(Calendar.HOUR_OF_DAY),
                        minute = cal.get(Calendar.MINUTE),
                        title = "Test notificación",
                        message = "Si ves esto, el sistema de notificaciones funciona 😉",
                        category = "Prueba"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text("Probar notificación en 1 minuto")
            }

            Spacer(Modifier.height(16.dp))



        }

        if (showProfileMenu) {
            ProfilePopup(
                onDismiss = { showProfileMenu = false },
                onEditProfile = { showProfileMenu = false; onEditProfile() },
                onViewInsignias = { showProfileMenu = false; onViewInsignias() },
                onHelpCenter = { showProfileMenu = false; onHelpCenter() },
                onLogout = { showProfileMenu = false; onLogout() }
            )
        }

        if (showAllMovimientos) {
            AllMovimientosDialog(movimientos) {
                showAllMovimientos = false
            }
        }
        if (showChart) {
            IngresosGastosChartDialog(
                ingresos = totalIngresos,
                gastos = totalGastos,
                onDismiss = { showChart = false }
            )
        }


    }
}

@Composable
fun IngresosGastosChartDialog(
    ingresos: Double,
    gastos: Double,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ingresos vs gastos") },
        text = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (ingresos == 0.0 && gastos == 0.0) {
                    Text(
                        "Aún no hay datos para mostrar.",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                } else {
                    val maxVal = max(ingresos, gastos)
                    val ingresosRatio = if (maxVal > 0) (ingresos / maxVal).toFloat() else 0f
                    val gastosRatio = if (maxVal > 0) (gastos / maxVal).toFloat() else 0f

                    BarChartRow(
                        label = "Ingresos",
                        amount = ingresos,
                        ratio = ingresosRatio,
                        color = Color(0xFF00C853)
                    )

                    Spacer(Modifier.height(16.dp))

                    BarChartRow(
                        label = "Gastos",
                        amount = gastos,
                        ratio = gastosRatio,
                        color = Color(0xFFFF5252)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
fun BarChartRow(
    label: String,
    amount: Double,
    ratio: Float,
    color: Color
) {
    Column(Modifier.fillMaxWidth()) {

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontWeight = FontWeight.Bold)
            Text("Q${String.format("%.2f", amount)}", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFE0E0E0))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(ratio.coerceIn(0f, 1f))
                    .background(color)
            )
        }
    }
}

@Composable
fun TopCardsSection(
    ingresos: Double,
    gastos: Double,
    ahorro: Double,
    onIngresosClick: () -> Unit = {},
    onGastosClick: () -> Unit = {},
    onAhorroClick: () -> Unit = {}
) {
    Column(
        Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FinanceCard("Ingresos", Icons.Default.AttachMoney, Color(0xFF00C853),
                "Q. $ingresos", onIngresosClick)

            FinanceCard("Gastos", Icons.Default.MoneyOff, Color(0xFFFF5252),
                "Q. $gastos", onGastosClick)
        }

        Spacer(Modifier.height(12.dp))

        Box(
            Modifier
                .width(160.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color(0xFFFFF59D))
                .clickable { onAhorroClick() }
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Savings, contentDescription = null, tint = Color(0xFF001F6B))
                Spacer(Modifier.width(6.dp))
                Text("Ahorro", color = Color(0xFF001F6B), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun FinanceCard(
    title: String,
    icon: ImageVector,
    color: Color,
    amount: String,
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(vertical = 14.dp, horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ICONO
        Icon(
            icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(30.dp)
        )

        Spacer(Modifier.height(6.dp))

        // TÍTULO
        Text(
            title,
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )

        Spacer(Modifier.height(6.dp))

        // TEXTO "Asignado"
        Text(
            "Asignado:",
            color = Color.Gray,
            fontSize = 12.sp
        )

        // CANTIDAD
        Text(
            amount,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 15.sp
        )
    }
}

@Composable
fun HeaderSection(
    saldoActual: Double,
    onOpenNotificaciones: () -> Unit,
    onProfileClick: () -> Unit
) {
    var userName by remember { mutableStateOf("Cargando...") }

    val userId = FirebaseAuth.getInstance().currentUser?.uid
    LaunchedEffect(userId) {
        if (userId != null) {
            FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("name")
                .get()
                .addOnSuccessListener {
                    userName = it.getValue(String::class.java) ?: "Usuario"
                }
        }
    }

    Box(
        Modifier
            .fillMaxWidth()
            .background(Color(0xFF001F6B), RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
            .padding(20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White,
                    modifier = Modifier.clickable { onOpenNotificaciones() })

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Hola ", color = Color.White, fontSize = 20.sp)
                    Text(userName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }

                Icon(
                    Icons.Default.AccountCircle, contentDescription = null, tint = Color.White,
                    modifier = Modifier.size(32.dp).clickable { onProfileClick() }
                )
            }

            Spacer(Modifier.height(20.dp))

            Box(
                Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(vertical = 14.dp, horizontal = 30.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Saldo actual", color = Color.Gray)
                    Text("Q. $saldoActual", fontWeight = FontWeight.Bold, fontSize = 22.sp)
                }
            }
        }
    }
}


// =======================================================================
//                     COMPONENTE — LINEROW (INGRESOS)
// =======================================================================

@Composable
fun IngresoRowDashboard(name: String, date: String, amount: Double) {

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            // Icono
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF00C853)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.AttachMoney, contentDescription = null, tint = Color.White)
            }

            Spacer(Modifier.width(10.dp))

            Column {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(date, fontSize = 12.sp, color = Color.Gray)
            }
        }

        Text(
            "+ Q${amount}",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00C853)
        )
    }

    Spacer(Modifier.height(6.dp))

    // Barra bonita
    LinearProgressIndicator(
        progress = 1f,
        color = Color(0xFF00C853),
        trackColor = Color(0xFFE0E0E0),
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(6.dp))
    )

    Spacer(Modifier.height(15.dp))
}

// =======================================================================
//                     COMPONENTE — LINEROW (GASTOS)
// =======================================================================

@Composable
fun GastoRowDashboard(name: String, date: String, amount: Double) {

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF5252)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.MoneyOff, contentDescription = null, tint = Color.White)
            }

            Spacer(Modifier.width(10.dp))

            Column {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(date, fontSize = 12.sp, color = Color.Gray)
            }
        }

        Text(
            "- Q${amount}",
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF5252)
        )
    }

    Spacer(Modifier.height(6.dp))

    LinearProgressIndicator(
        progress = 1f,
        color = Color(0xFFFF5252),
        trackColor = Color(0xFFE0E0E0),
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(6.dp))
    )

    Spacer(Modifier.height(15.dp))
}

// =======================================================================
//                  CONTENIDO INGRESOS / GASTOS / AHORRO
// =======================================================================

@Composable
fun IngresosDashboardContent(
    total: Double,
    meta: Double,
    lista: List<com.example.cashucontrol.models.Ingreso>
) {
    val progreso = if (meta > 0) (total / meta).coerceIn(0.0, 1.0) else 0.0
    val porcentaje = (progreso * 100).toInt()

    Column {
        // 1) LISTA DE INGRESOS
        lista.forEach {
            IngresoRowDashboard(it.name, it.date, it.amount)
        }

        if (lista.isEmpty()) {
            Text(
                "Aún no has registrado ingresos este mes",
                color = Color.Gray,
                fontSize = 13.sp
            )
        }

        Spacer(Modifier.height(16.dp))

        // 2) TEXTO "LLEVAS X DÍAS REGISTRANDO"
        Text(
            "Llevas 17 días registrando",
            color = Color.Gray,
            fontSize = 13.sp
        )

        Spacer(Modifier.height(10.dp))

        // 3) META MENSUAL + TARJETA DE PROGRESO
        Text(
            "Meta mensual  Q.$meta",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color(0xFF1A237E)
        )

        Spacer(Modifier.height(8.dp))

        ProgressCard(
            leftText = "Alcanzado un $porcentaje%",
            rightText = when {
                progreso >= 1f -> "¡Meta alcanzada!"
                progreso >= 0.5f -> "¡Vas muy bien, sigue así!"
                else -> "¡Buen inicio, continúa!"
            },
            progress = progreso.toFloat(),
            color = Color(0xFF00C853)
        )
    }
}

@Composable
fun GastosDashboardContent(
    total: Double,
    meta: Double,
    lista: List<com.example.cashucontrol.models.Gasto>
) {
    val progreso = if (meta > 0) (total / meta).coerceIn(0.0, 1.0) else 0.0
    val porcentaje = (progreso * 100).toInt()

    Column {
        // 1) LISTA DE GASTOS
        lista.forEach {
            GastoRowDashboard(it.name, it.date, it.amount)
        }

        if (lista.isEmpty()) {
            Text(
                "Aún no has registrado gastos este mes",
                color = Color.Gray,
                fontSize = 13.sp
            )
        }

        Spacer(Modifier.height(16.dp))

        // 2) TEXTO "LLEVAS X DÍAS REGISTRANDO"
        Text(
            "Llevas 17 días registrando",
            color = Color.Gray,
            fontSize = 13.sp
        )

        Spacer(Modifier.height(10.dp))

        // 3) PRESUPUESTO MENSUAL + TARJETA DE PROGRESO
        Text(
            "Presupuesto mensual",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color(0xFF1A237E)
        )

        Spacer(Modifier.height(8.dp))

        ProgressCard(
            leftText = "Usado $porcentaje%",
            rightText = when {
                progreso >= 1f -> "¡Te pasaste del límite!"
                progreso >= 0.85f -> "¡Ya casi llegas al límite!"
                else -> "Aún tienes margen."
            },
            progress = progreso.toFloat(),
            color = Color(0xFFFF5252)
        )
    }
}


@Composable
fun ProgressCard(
    leftText: String,
    rightText: String,
    progress: Float,
    color: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))          // Igual que GastoRow
            .background(Color(0xFFF5F5F5))           // Igual que GastoRow
            .padding(12.dp)                          // Igual que GastoRow
    ) {

        // Fila de textos (similar a la info del gasto)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                leftText,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                rightText,
                color = color,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }

        Spacer(Modifier.height(8.dp))

        // Barra de progreso dentro de la misma “tarjeta”
        LinearProgressIndicator(
            progress = progress,
            color = color,
            trackColor = Color(0xFFE0E0E0),
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(6.dp))
        )
    }
}

@Composable
fun AhorroDashboardContent(lista: List<com.example.cashucontrol.models.Ahorro>) {

    Text("Ahorro activo", fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))
    Spacer(Modifier.height(10.dp))

    lista.forEach {
        MetaCard(
            text = it.name,
            progress = (it.totalSaved / it.goalAmount).toFloat().coerceIn(0f, 1f),
            leftText = "Q${it.totalSaved} / ${it.goalAmount}",
            rightText = "${((it.totalSaved / it.goalAmount) * 100).toInt()}%"
        )
        Spacer(Modifier.height(15.dp))
    }
}

// =======================================================================
// 🟦 Meta Card
// =======================================================================

@Composable
fun MetaCard(text: String, progress: Float, leftText: String, rightText: String) {

    Text("Objetivos", fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))
    Spacer(Modifier.height(10.dp))

    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFFFEBEE))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Black)
            Spacer(Modifier.width(6.dp))
            Text(text, fontWeight = FontWeight.Bold)
        }
    }

    Spacer(Modifier.height(10.dp))

    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFFFEBEE))
            .padding(10.dp)
    ) {
        Column {
            LinearProgressIndicator(
                progress = progress,
                color = Color(0xFF00C853),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(6.dp)),
                trackColor = Color(0xFFE0E0E0)
            )
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(leftText)
                Text(rightText, color = Color(0xFF00C853), fontWeight = FontWeight.Bold)
            }
        }
    }
}

// =======================================================================
// PARSER FECHAS
// =======================================================================

fun parseFechaDDMMYYYY(fecha: String): Long {
    return try {
        val p = fecha.split("/")
        p[2].toInt() * 10000L + p[1].toInt() * 100 + p[0].toInt()
    } catch (e: Exception) {
        0
    }
}

// =======================================================================
// MOVIMIENTO ROW
// =======================================================================

data class Movimiento(
    val name: String,
    val amount: Double,
    val date: String,
    val tipo: String
)

@Composable
fun MovimientoRowC(mov: Movimiento) {

    val color = if (mov.tipo == "ingreso") Color(0xFF00C853) else Color(0xFFFF5252)
    val sign = if (mov.tipo == "ingreso") "+" else "-"

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(mov.name, color = color, fontWeight = FontWeight.Bold)
            Text(mov.date, color = Color.Gray, fontSize = 12.sp)
        }
        Text("$sign Q${mov.amount}", color = color, fontWeight = FontWeight.Bold)
    }
}

// =======================================================================
// DIALOGO — VER TODOS
// =======================================================================

@Composable
fun AllMovimientosDialog(
    movimientos: List<Movimiento>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Todos los movimientos") },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                movimientos.forEach {
                    MovimientoRowC(it)
                    Spacer(Modifier.height(10.dp))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cerrar") }
        }
    )
}

// =======================================================================
// TAB BUTTON
// =======================================================================

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
            fontWeight = FontWeight.Bold
        )
    }
}

// =======================================================================
// PROFILE POPUP
// =======================================================================

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
            Modifier
                .padding(top = 100.dp, end = 20.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
        ) {

            ProfileMenuItem(Icons.Default.Person, "Editar perfil") { onEditProfile() }
            ProfileMenuItem(Icons.Default.EmojiEvents, "Ver insignias") { onViewInsignias() }
            ProfileMenuItem(Icons.Default.HeadsetMic, "Centro de ayuda") { onHelpCenter() }

            Divider()

            ProfileMenuItem(Icons.Default.ExitToApp, "Cerrar sesión") { onLogout() }
        }
    }
}

@Composable
fun ProfileMenuItem(icon: ImageVector, text: String, onClick: () -> Unit) {

    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color.Gray)
        Spacer(Modifier.width(8.dp))
        Text(text, color = Color.Gray, fontWeight = FontWeight.Medium)
    }
}
