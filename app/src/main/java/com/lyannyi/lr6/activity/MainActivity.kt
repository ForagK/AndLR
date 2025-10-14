package com.lyannyi.lr6.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.lyannyi.lr6.ui.screen.MainScreen
import com.lyannyi.lr6.viewmodel.MainViewModel
import kotlin.getValue

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
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