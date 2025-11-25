package com.lyannyi.lr11.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class AuthViewModel : ViewModel() {
    private var auth: FirebaseAuth = Firebase.auth

    val user = mutableStateOf(auth.currentUser)
    val errorMessage = mutableStateOf("")

    fun getUser(): FirebaseUser? {
        return user.value
    }

    fun createAccount(email: String, password: String) {
        if (email.isNotBlank() && password.isNotBlank()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        user.value = auth.currentUser
                    } else {
                        errorMessage.value = "Registration failed"
                    }
                }
        }
    }

    fun signIn(email: String, password: String) {
        if (email.isNotBlank() && password.isNotBlank()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        user.value = auth.currentUser
                    } else {
                        errorMessage.value = "Login failed"
                    }
                }
        }
    }

    fun signInAnonymously() {
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user.value = auth.currentUser
                }
                else {
                    errorMessage.value = "Login failed"
                }
            }
    }
}