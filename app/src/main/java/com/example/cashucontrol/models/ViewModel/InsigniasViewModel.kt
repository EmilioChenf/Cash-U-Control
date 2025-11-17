package com.example.cashucontrol.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cashucontrol.models.Insignia
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class InsigniasViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val userId: String?
        get() = auth.currentUser?.uid

    private val dbRef: DatabaseReference?
        get() = userId?.let {
            FirebaseDatabase.getInstance()
                .reference
                .child("users")
                .child(it)
                .child("insignias")
        }

    private val _insignias = MutableStateFlow<List<Insignia>>(emptyList())
    val insignias: StateFlow<List<Insignia>> = _insignias


    init {
        crearInsigniasSiNoExisten()
        escucharCambios()
    }

    // ======================================================
    // CREAR INSIGNIAS DEFAULT
    // ======================================================
    private fun crearInsigniasSiNoExisten() {
        val ref = dbRef ?: return

        ref.get().addOnSuccessListener { snap ->
            if (!snap.exists()) insertarInsigniasDefault()
        }
    }

    private fun insertarInsigniasDefault() {
        val ref = dbRef ?: return

        val lista = listOf(
            Insignia("1", "Meta de Ingresos", "Cumple tu meta mensual de ingresos.", 0.0, false),
            Insignia("2", "Meta de Gastos", "Mantén tus gastos por debajo de tu límite mensual.", 0.0, false),
            Insignia("3", "Meta de Ahorro", "Alcanza tu meta mensual de ahorro.", 0.0, false),

            Insignia("4", "Racha 3 días", "Registra ingresos o gastos 3 días seguidos.", 0.0, false),
            Insignia("5", "Racha 7 días", "Registra actividad 7 días consecutivos.", 0.0, false),

            Insignia("6", "Primer ingreso", "Registra tu primer ingreso en la app.", 0.0, false),
            Insignia("7", "Primer gasto", "Registra tu primer gasto.", 0.0, false),
            Insignia("8", "Primer ahorro", "Crea tu primer objetivo de ahorro.", 0.0, false),

            Insignia("9", "Racha 5 días", "Registra actividad por 5 días seguidos.", 0.0, false),

            Insignia("10", "Dos metas creadas", "Crea al menos 2 metas de ahorro.", 0.0, false),
            Insignia("11", "Cinco metas creadas", "Crea al menos 5 metas de ahorro.", 0.0, false),

            Insignia("12", "Ahorro Q500", "Ahorra un total acumulado de Q500.", 0.0, false),
            Insignia("13", "Ahorro Q1000", "Ahorra un total acumulado de Q1000.", 0.0, false),

            Insignia("14", "Explorador", "Abre el dashboard 10 veces.", 0.0, false),

            Insignia("15", "Maestro Financiero", "Desbloquea 10 insignias en total.", 0.0, false)
        )

        val map = lista.associate { it.id to it }
        ref.updateChildren(map)
    }

    // ======================================================
    // LECTURA EN TIEMPO REAL
    // ======================================================
    private fun escucharCambios() {
        val ref = dbRef ?: return

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val lista = snapshot.children.mapNotNull { node ->
                    val ins = node.getValue(Insignia::class.java)
                    if (ins?.id.isNullOrBlank()) null else ins
                }.sortedBy { it.id.toInt() }

                _insignias.value = lista
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
