package com.example.cashucontrol.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cashucontrol.models.Gasto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.*

class GastosViewModel : ViewModel() {

    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val dbRef = FirebaseDatabase.getInstance().reference
        .child("users")
        .child(userId ?: "")
        .child("gastos")

    private val _gastosMes = MutableStateFlow<List<Gasto>>(emptyList())
    val gastosMes: StateFlow<List<Gasto>> = _gastosMes

    init {
        loadGastos()
    }

    fun saveGasto(name: String, amount: Double, date: String) {
        val id = dbRef.push().key ?: return
        val gasto = Gasto(id, name, amount, date)
        dbRef.child(id).setValue(gasto)
    }

    fun updateGasto(id: String, name: String, amount: Double, date: String) {
        val map = mapOf(
            "id" to id,
            "name" to name,
            "amount" to amount,
            "date" to date
        )
        dbRef.child(id).updateChildren(map)
    }

    fun deleteGasto(id: String) {
        dbRef.child(id).removeValue()
    }

    private fun loadGastos() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val current = Calendar.getInstance()
                val mm = current.get(Calendar.MONTH) + 1
                val yyyy = current.get(Calendar.YEAR)

                val filtered = snapshot.children.mapNotNull {
                    it.getValue(Gasto::class.java)
                }.filter { gasto ->
                    try {
                        val parts = gasto.date.split("/")
                        val day = parts[0].toInt()
                        val month = parts[1].toInt()
                        val year = parts[2].toInt()
                        month == mm && year == yyyy
                    } catch (_: Exception) {
                        false
                    }
                }

                _gastosMes.value = filtered
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
