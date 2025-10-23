package com.lyannyi.lr7.activity

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.lyannyi.lr7.ui.screen.PlayerScreen
import com.lyannyi.lr7.viewmodel.PlayerViewModel

class PlayerActivity : ComponentActivity() {
    private val viewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            0
        )
        setContent {
            PlayerScreen(viewModel)
        }
    }
}