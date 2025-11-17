package com.example.cashucontrol.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class MonthlyGoalGastos(
    val month: String = "",
    val goalAmount: Double = 0.0
)

class MonthlyGoalGastosViewModel : ViewModel() {

    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val monthKey = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))

    private val dbRef = FirebaseDatabase.getInstance().reference
        .child("users")
        .child(userId ?: "")
        .child("expense_goals")   // ⛔️ AQUÍ LA DIFERENCIA
        .child(monthKey)

    private val _goal = MutableStateFlow<MonthlyGoalGastos?>(null)
    val goal: StateFlow<MonthlyGoalGastos?> = _goal

    val currentMonth = monthKey

    init {
        loadGoal()
    }

    fun saveGoal(goalAmount: Double) {
        val map = MonthlyGoalGastos(monthKey, goalAmount)
        dbRef.setValue(map)
    }

    private fun loadGoal() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _goal.value = snapshot.getValue(MonthlyGoalGastos::class.java)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
