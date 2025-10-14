package com.lyannyi.lr6.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.lyannyi.lr6.ui.screen.PlayerScreen
import com.lyannyi.lr6.viewmodel.PlayerViewModel

class PlayerActivity : ComponentActivity() {
    private val viewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlayerScreen(viewModel)
        }
    }
}