package com.example.cashucontrol.viewmodel

import com.example.cashucontrol.models.Ahorro
import com.example.cashucontrol.models.Gasto
import com.example.cashucontrol.models.Ingreso
import kotlin.math.min

class AutoInsigniasManager(
    private val insigniasVM: InsigniasViewModel
) {

    fun procesar(
        ingresos: List<Ingreso>,
        gastos: List<Gasto>,
        ahorros: List<Ahorro>,
        metaIngresos: Double,
        metaGastos: Double,
        metaAhorro: Double,
        diasActivos: Int,
        dashboardViews: Int
    ) {

        val totalIngresos = ingresos.sumOf { it.amount }
        val totalGastos = gastos.sumOf { it.amount }
        val totalAhorros = ahorros.sumOf { it.totalSaved }

        // Meta ingresos
        if (metaIngresos > 0)
            insigniasVM.actualizarInsignia("1", min(totalIngresos / metaIngresos, 1.0))

        // Meta gastos
        if (metaGastos > 0)
            insigniasVM.actualizarInsignia("2", min((metaGastos - totalGastos) / metaGastos, 1.0))

        // Meta ahorro
        if (metaAhorro > 0)
            insigniasVM.actualizarInsignia("3", min(totalAhorros / metaAhorro, 1.0))

        // Rachas
        insigniasVM.actualizarInsignia("4", min(diasActivos / 3.0, 1.0))
        insigniasVM.actualizarInsignia("5", min(diasActivos / 7.0, 1.0))
        insigniasVM.actualizarInsignia("9", min(diasActivos / 5.0, 1.0))

        // Primer ingreso/gasto/ahorro
        if (ingresos.isNotEmpty()) insigniasVM.desbloquear("6")
        if (gastos.isNotEmpty()) insigniasVM.desbloquear("7")
        if (ahorros.isNotEmpty()) insigniasVM.desbloquear("8")

        // Metas creadas
        insigniasVM.actualizarInsignia("10", min(ahorros.size / 2.0, 1.0))
        insigniasVM.actualizarInsignia("11", min(ahorros.size / 5.0, 1.0))

        // Ahorro total
        insigniasVM.actualizarInsignia("12", min(totalAhorros / 500.0, 1.0))
        insigniasVM.actualizarInsignia("13", min(totalAhorros / 1000.0, 1.0))

        // Abre dashboard
        insigniasVM.actualizarInsignia("14", min(dashboardViews / 10.0, 1.0))

        // Maestro financiero
        val completas = insigniasVM.insignias.value.count { it.completed }
        insigniasVM.actualizarInsignia("15", min(completas / 10.0, 1.0))
    }
}
