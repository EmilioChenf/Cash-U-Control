package com.example.cashucontrol.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cashucontrol.models.Ingreso
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class IngresosViewModel : ViewModel() {

    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val dbRef = FirebaseDatabase.getInstance().reference
        .child("users")
        .child(userId ?: "")
        .child("ingresos")

    // 🔵 INGRESOS COMPLETOS
    private val _ingresos = MutableStateFlow<List<Ingreso>>(emptyList())
    val ingresos: StateFlow<List<Ingreso>> = _ingresos

    // 🔵 INGRESOS SOLO DEL MES ACTUAL
    private val _ingresosMes = MutableStateFlow<List<Ingreso>>(emptyList())
    val ingresosMes: StateFlow<List<Ingreso>> = _ingresosMes

    init {
        loadIngresos()
    }

    // ✔ GUARDAR INGRESO
    fun saveIngreso(name: String, amount: Double, date: String, type: String) {
        val id = dbRef.push().key ?: return
        val ingreso = Ingreso(id, name, amount, date, type)
        dbRef.child(id).setValue(ingreso)
    }

    // ✔ ACTUALIZAR INGRESO
    fun updateIngreso(id: String, name: String, amount: Double, date: String, type: String) {
        val map = mapOf(
            "id" to id,
            "name" to name,
            "amount" to amount,
            "date" to date,
            "type" to type
        )
        dbRef.child(id).updateChildren(map)
    }

    // ✔ ELIMINAR INGRESO
    fun deleteIngreso(id: String) {
        dbRef.child(id).removeValue()
    }

    // ✔ CARGAR INGRESOS Y FILTRAR POR MES
    private fun loadIngresos() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                // 🔹 Convertir Firebase → lista
                val list = snapshot.children.mapNotNull { it.getValue(Ingreso::class.java) }
                _ingresos.value = list

                // 🔹 Fecha actual
                val month = java.time.LocalDate.now().monthValue
                val year = java.time.LocalDate.now().year

                // 🔹 Filtrar por mes actual
                val filtrados = list.filter { ingreso ->
                    try {
                        val p = ingreso.date.split("/")
                        val m = p[1].toInt()
                        val y = p[2].toInt()
                        m == month && y == year
                    } catch (e: Exception) {
                        false
                    }
                }

                _ingresosMes.value = filtrados
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
