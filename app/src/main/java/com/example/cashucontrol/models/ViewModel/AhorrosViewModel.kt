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

    init {
        loadAhorros()
    }

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

    fun updateAhorro(id: String, name: String, goalAmount: Double, plazo: String, deadline: String) {
        val map = mapOf(
            "name" to name,
            "goalAmount" to goalAmount,
            "plazo" to plazo,
            "deadline" to deadline
        )
        dbRef.child(id).updateChildren(map)
    }

    fun deleteAhorro(id: String) {
        dbRef.child(id).removeValue()
    }

    fun addAporte(ahorroId: String, amount: Double, date: String) {
        val aporteId = dbRef.child(ahorroId).child("aportes").push().key ?: return

        val aporte = Aporte(aporteId, amount, date)

        dbRef.child(ahorroId).child("aportes").child(aporteId).setValue(aporte)

        // Actualizar total ahorrado
        dbRef.child(ahorroId).child("totalSaved")
            .get()
            .addOnSuccessListener {
                val current = it.value.toString().toDoubleOrNull() ?: 0.0
                dbRef.child(ahorroId).child("totalSaved").setValue(current + amount)
            }
    }

    private fun loadAhorros() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val list = snapshot.children.mapNotNull { it.getValue(Ahorro::class.java) }
                _ahorros.value = list
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
