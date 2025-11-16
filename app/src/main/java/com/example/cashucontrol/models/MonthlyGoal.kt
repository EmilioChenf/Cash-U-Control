package com.example.cashucontrol.models

data class MonthlyGoal(
    val yearMonth: String = "",   // Ej: "2025-02"
    val goalAmount: Double = 0.0  // Monto meta
)
