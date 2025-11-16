package com.example.cashucontrol.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class MonthlyGoal(
    val month: String = "",
    val goalAmount: Double = 0.0
)

class MonthlyGoalViewModel : ViewModel() {

    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val monthKey = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))

    private val dbRef = FirebaseDatabase.getInstance().reference
        .child("users")
        .child(userId ?: "")
        .child("income_goals")
        .child(monthKey)

    private val _goal = MutableStateFlow<MonthlyGoal?>(null)
    val goal: StateFlow<MonthlyGoal?> = _goal

    val currentMonth = monthKey

    init {
        loadGoal()
    }

    fun saveGoal(goalAmount: Double) {
        val map = MonthlyGoal(monthKey, goalAmount)
        dbRef.setValue(map)
    }

    private fun loadGoal() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _goal.value = snapshot.getValue(MonthlyGoal::class.java)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
