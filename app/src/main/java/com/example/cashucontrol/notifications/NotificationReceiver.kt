package com.example.cashucontrol.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.cashucontrol.R
import com.example.cashucontrol.models.AppNotification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        // Datos que mandamos desde el scheduler
        val title = intent.getStringExtra("title") ?: "Recordatorio"
        val message = intent.getStringExtra("message")
            ?: "No olvides revisar tus ingresos y gastos de hoy."
        val category = intent.getStringExtra("category") ?: "Sistema"

        val channelId = "recordatorios_channel"

        // Crear canal (obligatorio desde Android 8.0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Recordatorios",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        // En Android 13+ hay que verificar que el permiso POST_NOTIFICATIONS esté concedido
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                // Si no hay permiso, no mostramos la notificación del sistema.
                // Igualmente podemos guardar el registro en Firebase.
                saveNotificationToFirebase(title, message, category)
                return
            }
        }

        // Construir la notificación del sistema
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)   // usa tu icono existente
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(message)
            )
            .setAutoCancel(true)
            .build()

        // Mostrarla
        NotificationManagerCompat.from(context)
            .notify(System.currentTimeMillis().toInt(), notification)

        // Guardar también en Firebase para que aparezca en NotificacionesScreen
        saveNotificationToFirebase(title, message, category)
    }

    // Guarda la notificación bajo: users/{uid}/notificaciones/{id}
    private fun saveNotificationToFirebase(
        title: String,
        message: String,
        category: String
    ) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val ref = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(uid)
            .child("notificaciones")

        val id = ref.push().key ?: return

        val notification = AppNotification(
            id = id,
            title = title,
            category = category,
            message = message,
            timestamp = System.currentTimeMillis()
        )

        ref.child(id).setValue(notification)
    }
}
