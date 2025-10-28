package com.lyannyi.lr8.activity

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.lyannyi.lr8.ui.screen.CameraScreen
import com.lyannyi.lr8.viewmodel.CameraViewModel
import kotlin.getValue

class CameraActivity : ComponentActivity() {
    private val viewModel: CameraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            0
        )

        setContent {
            CameraScreen(viewModel)
        }
    }
}