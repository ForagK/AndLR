package com.lyannyi.lr11.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.lyannyi.lr11.ui.screen.AuthScreen
import com.lyannyi.lr11.ui.screen.MainScreen
import com.lyannyi.lr11.viewmodel.AuthViewModel
import kotlin.getValue

class AuthActivity : ComponentActivity() {
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthScreen(viewModel)
        }
    }
}