package com.example.cashucontrol.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth


class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    // En AuthViewModel.kt
    fun registerUser(email: String, password: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Si el registro es exitoso, obtén el UID del usuario y pásalo a la función onSuccess
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        onSuccess(userId) // <- Pasa el UID aquí
                    } else {
                        onError("No se pudo obtener el ID del usuario.")
                    }
                } else {
                    onError(task.exception?.message ?: "Error desconocido")
                }
            }
    }


    fun loginUser(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.message ?: "Error al iniciar sesión") }
    }
}
