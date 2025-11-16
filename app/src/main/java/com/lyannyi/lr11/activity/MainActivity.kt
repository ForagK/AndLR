package com.lyannyi.lr11.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.lyannyi.lr11.ui.screen.AuthScreen
import com.lyannyi.lr11.ui.screen.MainScreen
import com.lyannyi.lr11.viewmodel.MainViewModel
import kotlin.getValue

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        val currentUser = auth.currentUser
        if (currentUser == null) {
            val intent = Intent(this, AuthActivity::class.java)
            this.startActivity(intent)
            finish()
        }
        else {
            setContent {
                MainScreen()
            }
        }
    }
    override fun onStart() {
        super.onStart()

        viewModel.startSound(this)
    }

    override fun onStop() {
        super.onStop()
        if (!isChangingConfigurations) viewModel.stopSound()
    }
}