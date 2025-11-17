package com.example.cashucontrol.models

data class Achievement(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val progress: Double = 0.0,
    val completed: Boolean = false
)
