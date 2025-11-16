package com.example.cashucontrol.models

data class Ingreso(
    val id: String = "",
    val name: String = "",
    val amount: Double = 0.0,
    val date: String = "",
    val type: String = "" // icono según tipo
)
