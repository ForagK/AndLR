package com.lyannyi.lr11.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.lyannyi.lr11.ui.screen.RetrofitScreen
import com.lyannyi.lr11.viewmodel.RetrofitViewModel
import kotlin.getValue

class RetrofitActivity : ComponentActivity() {
    private val viewModel: RetrofitViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RetrofitScreen(viewModel)
        }
    }
}