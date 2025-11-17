package com.example.cashucontrol.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EditarPerfilViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser
    private val uid = user?.uid

    private val dbRef = FirebaseDatabase.getInstance()
        .getReference("users")
        .child(uid ?: "")

    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre

    private val _correo = MutableStateFlow("")
    val correo: StateFlow<String> = _correo

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje

    init {
        cargarDatos()
    }

    // Cargar datos actuales del usuario desde Firebase
    private fun cargarDatos() {
        if (uid == null) return

        dbRef.child("name").get().addOnSuccessListener {
            _nombre.value = it.getValue(String::class.java) ?: ""
        }

        _correo.value = user?.email ?: ""
    }

    fun actualizarPerfil(nuevoNombre: String, nuevoCorreo: String, nuevaContrasena: String?) {

        if (uid == null) return

        // 1️⃣ Actualizar nombre en la base de datos
        dbRef.child("name").setValue(nuevoNombre)

        // 2️⃣ Actualizar correo
        user?.updateEmail(nuevoCorreo)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _correo.value = nuevoCorreo
            } else {
                _mensaje.value = "Error al actualizar correo"
            }
        }

        // 3️⃣ Actualizar contraseña (opcional)
        if (!nuevaContrasena.isNullOrEmpty()) {
            user?.updatePassword(nuevaContrasena)?.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    _mensaje.value = "Error al actualizar contraseña"
                }
            }
        }

        _mensaje.value = "Perfil actualizado correctamente"
    }
}
