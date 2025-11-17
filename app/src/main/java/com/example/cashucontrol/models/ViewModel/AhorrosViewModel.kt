package com.example.cashucontrol.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cashucontrol.models.Ahorro
import com.example.cashucontrol.models.Aporte
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AhorrosViewModel : ViewModel() {

    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private val dbRef = FirebaseDatabase.getInstance().reference
        .child("users")
        .child(userId)
        .child("ahorros")

    private val _ahorros = MutableStateFlow<List<Ahorro>>(emptyList())
    val ahorros: StateFlow<List<Ahorro>> = _ahorros

    // 🔥 NUEVO: lista unificada de aportes
    private val _aportes = MutableStateFlow<List<Aporte>>(emptyList())
    val aportes: StateFlow<List<Aporte>> = _aportes

    init {
        loadAhorros()
        loadAportes()   // ← Cargar aportes también
    }

    // ================================
    // 🔵 Crear nuevo objetivo
    // ================================
    fun saveAhorro(name: String, goalAmount: Double, plazo: String, deadline: String) {
        val id = dbRef.push().key ?: return
        val ahorro = Ahorro(
            id = id,
            name = name,
            goalAmount = goalAmount,
            plazo = plazo,
            deadline = deadline,
            totalSaved = 0.0
        )
        dbRef.child(id).setValue(ahorro)
    }

    // ================================
    // ✏️ Editar objetivo
    // ================================
    fun updateAhorro(id: String, name: String, goalAmount: Double, plazo: String, deadline: String) {
        val map = mapOf(
            "name" to name,
            "goalAmount" to goalAmount,
            "plazo" to plazo,
            "deadline" to deadline
        )
        dbRef.child(id).updateChildren(map)
    }

    // ================================
    // ❌ Eliminar objetivo
    // ================================
    fun deleteAhorro(id: String) {
        dbRef.child(id).removeValue()
    }

    // ================================
    // ➕ Agregar aporte
    // ================================
    fun addAporte(ahorroId: String, amount: Double, date: String) {
        val aporteId = dbRef.child(ahorroId).child("aportes").push().key ?: return

        val aporte = Aporte(aporteId, amount, date)

        // Guardar aporte
        dbRef.child(ahorroId).child("aportes").child(aporteId).setValue(aporte)

        // Actualizar totalSaved
        dbRef.child(ahorroId).child("totalSaved")
            .get()
            .addOnSuccessListener {
                val current = it.value.toString().toDoubleOrNull() ?: 0.0
                dbRef.child(ahorroId).child("totalSaved").setValue(current + amount)
            }

        // Recargar aportes
        loadAportes()
    }

    // ================================
    // 📥 Cargar TODOS los ahorros
    // ================================
    private fun loadAhorros() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.getValue(Ahorro::class.java) }
                _ahorros.value = list
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // ================================
    // 📥 Cargar TODOS los aportes (de todos los ahorros)
    // ================================
    private fun loadAportes() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val result = mutableListOf<Aporte>()

                snapshot.children.forEach { ahorroNode ->
                    ahorroNode.child("aportes").children.forEach { aporteNode ->
                        val aporte = aporteNode.getValue(Aporte::class.java)
                        if (aporte != null) result.add(aporte)
                    }
                }

                // Ordenar aportes por fecha descendente (últimos arriba)
                _aportes.value = result.sortedByDescending { it.date }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
