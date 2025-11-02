# 💸 CashU Control
> _Tu asistente financiero personal para gestionar ingresos, gastos y metas de ahorro con facilidad._

---

## 🧭 Descripción General

**CashU Control** es una aplicación móvil desarrollada en **Kotlin** con **Jetpack Compose**, enfocada en la educación y organización financiera personal.  
Su objetivo es brindar a los usuarios una forma simple, moderna y segura de controlar sus **ingresos**, **gastos** y **metas de ahorro**, integrando una experiencia visual atractiva y dinámica.

La aplicación utiliza **Firebase Authentication** y **Firestore** para la gestión de usuarios y datos en tiempo real, además de una **base local fake** para pruebas sin conexión.

---

## 🚀 Funcionalidades Principales

- 🔐 Autenticación de usuario con Firebase.
- 💰 Registro, consulta y edición de **ingresos** y **gastos**.
- 💡 Visualización de progreso en **metas de ahorro**.
- 🏆 Sistema de **insignias** que motiva la constancia financiera.
- 🔔 Panel de **notificaciones personalizadas**.
- 👤 Edición de perfil y configuración del usuario.
- 💬 Sección de ayuda y guía para principiantes.
- 🌐 Navegación **type-safe** implementada con **Jetpack Navigation Compose**.

---

## 🧭 Estructura de Navegación

> Implementada con **Navigation Compose (2.8.3)** usando `NavHost` y `NavController`.

```bash
NavHost(
    navController = navController,
    startDestination = "welcome"
) {
    composable("welcome") { WelcomeScreen() }
    composable("login") { LoginScreen() }
    composable("register") { RegisterScreen() }
    composable("dashboard") { DashboardScreen() }
    composable("ingresos") { IngresosScreen() }
    composable("gastos") { GastosScreen() }
    composable("ahorro") { AhorroScreen() }
    composable("nuevoIngreso") { NuevoIngresoFormScreen() }
    composable("nuevoGasto") { NuevoGastoFormScreen() }
    composable("notificaciones") { NotificacionesScreen() }
    composable("editarPerfil") { EditarPerfilScreen() }
    composable("insignias") { InsigniasScreen() }
    composable("centroAyuda") { CentroAyudaScreen() }
}
```

📱 Flujo principal:

css
Copiar código
Welcome → Login/Register → Dashboard → [Ingresos | Gastos | Ahorro]
                                       ↳ Notificaciones
                                       ↳ Editar Perfil
                                       ↳ Ver Insignias
                                       ↳ Centro de Ayuda
🔌 Servicios Externos
Servicio	Descripción	Uso en la app
Firebase Authentication	Autenticación segura de usuarios.	Registro e inicio de sesión.
Firebase Firestore	Base de datos NoSQL en la nube.	Almacenamiento de ingresos, gastos y metas.
Firebase Realtime Database	Fuente de datos fake.	Simulación de información y pruebas offline.

✅ Nota: En caso de no contar con internet, la app usa datos locales simulados para mantener la funcionalidad completa.

📚 Librerías Utilizadas
```bash
Copiar código
// --- Jetpack Compose ---
implementation(platform(libs.androidx.compose.bom))
implementation(libs.androidx.ui)
implementation(libs.androidx.material3)
implementation("androidx.navigation:navigation-compose:2.8.3")

// --- Firebase ---
implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
implementation("com.google.firebase:firebase-auth-ktx")
implementation("com.google.firebase:firebase-firestore-ktx")
implementation("com.google.firebase:firebase-database-ktx")

// --- Core Android ---
implementation(libs.androidx.core.ktx)
implementation(libs.androidx.lifecycle.runtime.ktx)
implementation(libs.androidx.activity.compose)
Librería	Función	Versión
Jetpack Compose	UI declarativa moderna.	1.7.0
Material 3	Componentes visuales adaptativos.	1.3.0
Navigation Compose	Sistema de rutas entre pantallas.	2.8.3
Firebase BoM	Control de dependencias Firebase.	33.1.2
Lifecycle Runtime	Manejo del ciclo de vida en Compose.	2.7.0
```

🧩 Estructura del Proyecto
```bash
com.example.cashucontrol
│
├── ui/
│   ├── screens/
│   │   ├── finances/
│   │   │   ├── DashboardScreen.kt
│   │   │   ├── IngresosScreen.kt
│   │   │   ├── GastosScreen.kt
│   │   │   ├── AhorroScreen.kt
│   │   │   ├── NotificacionesScreen.kt
│   │   │   ├── EditarPerfilScreen.kt
│   │   │   ├── InsigniasScreen.kt
│   │   │   └── CentroAyudaScreen.kt
│   └── theme/
│       └── CashUControlTheme.kt
│
├── MainActivity.kt
└── build.gradle.kts
```


⚙️ Requisitos Técnicos
Requisito	Versión mínima
Android Studio	Koala o superior
Kotlin	1.9+
minSdk	26
targetSdk	36
JVM Target	11
Firebase BoM	33.1.2

📂 Se requiere agregar el archivo:

```bash
app/google-services.json
```


🧾 Datos de Prueba
Para esta entrega, la app contiene datos falsos que permiten probar todas las funcionalidades sin conexión ni autenticación real.

Ejemplo de registros locales:

```bash
{
  "ingresos": [
    { "nombre": "Trabajo medio tiempo", "monto": 1000 },
    { "nombre": "Freelance", "monto": 500 }
  ],
  "gastos": [
    { "nombre": "Universidad", "monto": 1000 },
    { "nombre": "Comida", "monto": 500 }
  ],
  "ahorro": {
    "meta": "Teléfono nuevo",
    "objetivo": 800,
    "actual": 300
  }
}
```

🧪 Compilación y Ejecución
🔧 Clonar el repositorio
```bash
git clone https://github.com/<usuario>/CashUControl.git
```

🧩 Compilar la app
```bash
./gradlew assembleDebug
```

📱 Instalar APK
El archivo generado estará disponible en:

bash
Copiar código
CashUControl/app/APK/
📘 Rúbrica de Evaluación
Criterio	Descripción	Ponderación
✅ Navegación funcional	Todas las pantallas conectadas y navegables.	80%
🧩 README documentado	Incluye servicios y librerías correctamente explicados.	20%


🏁 Conclusión
CashU Control promueve la educación financiera mediante una experiencia digital atractiva e interactiva.
Su diseño minimalista, acompañado de un sistema de logros e indicadores visuales, motiva la constancia en los hábitos de ahorro.

💬 “Pequeños pasos crean grandes logros.”

🛠️ Hecho con
❤️ Kotlin · 🧱 Jetpack Compose · ☁️ Firebase · 🧭 Navigation Compose



