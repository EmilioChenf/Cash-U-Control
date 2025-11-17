package com.example.cashucontrol.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cashucontrol.models.AppNotification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NotificacionesViewModel : ViewModel() {

    private val _notificaciones = MutableStateFlow<List<AppNotification>>(emptyList())
    val notificaciones: StateFlow<List<AppNotification>> = _notificaciones

    private val dbRef: DatabaseReference?

    init {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        dbRef = if (uid != null) {
            FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .child("notificaciones")
        } else null

        dbRef?.orderByChild("timestamp")
            ?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<AppNotification>()
                    for (child in snapshot.children) {
                        val n = child.getValue(AppNotification::class.java)
                        if (n != null) list.add(n)
                    }
                    // Orden más reciente primero
                    _notificaciones.value = list.sortedByDescending { it.timestamp }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun borrarTodo() {
        dbRef?.setValue(null)
    }
}
