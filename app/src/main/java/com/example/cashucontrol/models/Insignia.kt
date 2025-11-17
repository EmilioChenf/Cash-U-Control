package com.example.cashucontrol.models

data class Insignia(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val progress: Double = 0.0,
    val completed: Boolean = false
)
