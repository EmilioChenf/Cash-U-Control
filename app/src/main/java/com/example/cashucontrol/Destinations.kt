package com.example.cashucontrol.navigation

import kotlinx.serialization.Serializable

@Serializable data object Welcome
@Serializable data object Login
@Serializable data object Register
@Serializable data object Dashboard

@Serializable data object Ingresos
@Serializable data object NuevoIngreso

@Serializable data object Gastos
@Serializable data object NuevoGasto

@Serializable data object Ahorro
@Serializable data class NuevoObjetivo(val plazoSeleccionado: String)

@Serializable data object Notificaciones
@Serializable data object EditarPerfil
@Serializable data object Insignias
@Serializable data object CentroAyuda
