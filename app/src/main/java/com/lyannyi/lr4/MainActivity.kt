package com.lyannyi.lr4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.lyannyi.lr4.viewmodel.CalendarViewModel
import com.lyannyi.lr4.ui.screen.CalendarScreen

class MainActivity : ComponentActivity() {
    private val viewModel: CalendarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalendarScreen(viewModel)
        }
    }
}