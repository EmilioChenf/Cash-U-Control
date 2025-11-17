package com.example.cashucontrol.models

data class AppNotification(
    val id: String = "",
    val title: String = "",
    val category: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
