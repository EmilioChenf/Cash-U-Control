package com.example.cashucontrol.models

data class Ahorro(
    val id: String = "",
    val name: String = "",
    val goalAmount: Double = 0.0,
    val plazo: String = "",
    val deadline: String = "",
    val totalSaved: Double = 0.0
)

data class Aporte(
    val id: String = "",
    val amount: Double = 0.0,
    val date: String = ""
)
