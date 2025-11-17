package com.example.cashucontrol.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cashucontrol.models.Insignia
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class InsigniasViewModel : ViewModel() {

    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    private val dbRef: DatabaseReference? =
        if (userId != null)
            FirebaseDatabase.getInstance().reference
                .child("users")
                .child(userId)
                .child("insignias")
        else null

    private val _insignias = MutableStateFlow<List<Insignia>>(emptyList())
    val insignias: StateFlow<List<Insignia>> = _insignias

    init {
        if (dbRef != null) {
            verificarOCrearInsignias()
            escucharCambios()
        }
    }

    // =========================================================
    // 🔵 1. CREA 15 INSIGNIAS AL USUARIO (SOLO SI NO EXISTEN)
    // =========================================================
    private fun verificarOCrearInsignias() {
        dbRef ?: return // si dbRef es null → usuario no logueado

        dbRef.get().addOnSuccessListener { snap ->
            if (!snap.exists()) {
                crearInsigniasDefault()
            }
        }.addOnFailureListener {
            // Si falla, no crashea
        }
    }

    private fun crearInsigniasDefault() {
        dbRef ?: return

        val lista = listOf(
            Insignia("1", "Meta de Ingresos", "Cumple tu meta mensual de ingresos", 0.0, false),
            Insignia("2", "Meta de Gastos", "Gasta menos del límite mensual definido", 0.0, false),
            Insignia("3", "Meta de Ahorro", "Alcanza la meta mensual de ahorro", 0.0, false),
            Insignia("4", "Racha 3 días", "Registra ingresos o gastos 3 días seguidos", 0.0, false),
            Insignia("5", "Racha 7 días", "Registra ingresos o gastos 7 días seguidos", 0.0, false),
            Insignia("6", "Primer ingreso", "Registra tu primer ingreso", 0.0, false),
            Insignia("7", "Primer gasto", "Registra tu primer gasto", 0.0, false),
            Insignia("8", "Primer ahorro", "Crea tu primer objetivo de ahorro", 0.0, false),
            Insignia("9", "Racha 5 días", "Registra actividad 5 días consecutivos", 0.0, false),
            Insignia("10", "2 metas creadas", "Crea al menos 2 objetivos de ahorro", 0.0, false),
            Insignia("11", "5 metas creadas", "Crea al menos 5 objetivos de ahorro", 0.0, false),
            Insignia("12", "Ahorro Q.500", "Ahorra más de Q.500 en total", 0.0, false),
            Insignia("13", "Ahorro Q.1000", "Ahorra más de Q.1000 en total", 0.0, false),
            Insignia("14", "Explorador", "Abre el Dashboard 10 veces", 0.0, false),
            Insignia("15", "Maestro Financiero", "Desbloquea al menos 10 insignias", 0.0, false)
        )

        lista.forEach { ins ->
            dbRef.child(ins.id).setValue(ins)
        }
    }

    // =========================================================
    // 🔵 2. ESCUCHAR CAMBIOS EN TIEMPO REAL
    // =========================================================
    private fun escucharCambios() {
        dbRef ?: return

        dbRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val lista = snapshot.children.mapNotNull { data ->
                    try {
                        val ins = data.getValue(Insignia::class.java)

                        // Protección contra nulls
                        if (ins != null) {
                            Insignia(
                                id = ins.id ?: "",
                                name = ins.name ?: "Sin nombre",
                                description = ins.description ?: "Sin descripción",
                                progress = ins.progress ?: 0.0,
                                completed = ins.completed ?: false
                            )
                        } else null

                    } catch (e: Exception) {
                        null
                    }
                }.sortedBy { it.id.toIntOrNull() ?: 0 }

                _insignias.value = lista
            }

            override fun onCancelled(error: DatabaseError) {
                // ignora error, no crashea
            }
        })
    }

    // =========================================================
    // 🔵 3. ACTUALIZAR PROGRESO — SEGURO
    // =========================================================
    fun actualizarInsignia(id: String, progreso: Double) {
        dbRef ?: return

        val map = mapOf(
            "progress" to progreso,
            "completed" to (progreso >= 1.0)
        )

        dbRef.child(id).updateChildren(map)
    }

    // =========================================================
    // 🔵 4. DESBLOQUEAR — SEGURO
    // =========================================================
    fun desbloquear(id: String) {
        dbRef ?: return

        val map = mapOf(
            "completed" to true,
            "progress" to 1.0
        )

        dbRef.child(id).updateChildren(map)
    }
}
