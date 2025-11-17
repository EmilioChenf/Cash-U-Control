package com.example.cashucontrol.models

data class Movement(
    val id: String = "",
    val title: String = "",
    val date: String = "",
    val amount: Double = 0.0,
    val type: String = "" // "ingreso", "gasto", "aporte"
)
