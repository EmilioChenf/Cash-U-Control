package com.example.cashucontrol.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cashucontrol.models.Achievement
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AchievementsViewModel : ViewModel() {

    private val userId =
        FirebaseAuth.getInstance().currentUser?.uid
            ?: throw IllegalStateException("Usuario no autenticado")

    private val dbRef =
        FirebaseDatabase.getInstance()
            .reference
            .child("users")
            .child(userId)
            .child("achievements")

    private val _achievements = MutableStateFlow<List<Achievement>>(emptyList())
    val achievements: StateFlow<List<Achievement>> = _achievements

    init {
        ensureDefaultAchievements()
        listenForChanges()
    }

    // --------------------------------------------------------------
    // 1️⃣ CREAR LOGROS SI NO EXISTEN
    // --------------------------------------------------------------
    private fun ensureDefaultAchievements() {
        val defaults = listOf(
            Achievement(
                id = "1",
                title = "Meta de ingresos",
                description = "Alcanza tu meta mensual de ingresos",
                progress = 0.0,
                completed = false
            ),
            Achievement(
                id = "2",
                title = "Meta de ahorro",
                description = "Alcanza tu meta mensual de ahorro",
                progress = 0.0,
                completed = false
            ),
            Achievement(
                id = "3",
                title = "Primer gasto",
                description = "Registra tu primer gasto",
                progress = 0.0,
                completed = false
            ),
            Achievement(
                id = "4",
                title = "Primer ingreso",
                description = "Registra tu primer ingreso",
                progress = 0.0,
                completed = false
            ),
            Achievement(
                id = "5",
                title = "Racha de 3 días",
                description = "Registra movimientos 3 días seguidos",
                progress = 0.0,
                completed = false
            ),
            Achievement(
                id = "6",
                title = "Explorador",
                description = "Abre el dashboard 10 veces",
                progress = 0.0,
                completed = false
            )
        )

        defaults.forEach { ach ->
            dbRef.child(ach.id).get().addOnSuccessListener {
                if (!it.exists()) {
                    dbRef.child(ach.id).setValue(ach)
                }
            }
        }
    }

    // --------------------------------------------------------------
    // 2️⃣ ESCUCHAR CAMBIOS EN TIEMPO REAL
    // --------------------------------------------------------------
    private fun listenForChanges() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Achievement>()
                snapshot.children.forEach { node ->
                    val ach = node.getValue(Achievement::class.java)
                    if (ach != null) list.add(ach)
                }
                _achievements.value = list
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // --------------------------------------------------------------
    // 3️⃣ ACTUALIZAR PROGRESO
    // --------------------------------------------------------------
    fun updateProgress(id: String, progress: Double) {
        val fixed = progress.coerceIn(0.0, 1.0)
        dbRef.child(id).child("progress").setValue(fixed)
        if (fixed >= 1.0) {
            dbRef.child(id).child("completed").setValue(true)
        }
    }

    // --------------------------------------------------------------
    // 4️⃣ MARCAR COMO COMPLETADO
    // --------------------------------------------------------------
    fun complete(id: String) {
        dbRef.child(id).child("completed").setValue(true)
        dbRef.child(id).child("progress").setValue(1.0)
    }
}
