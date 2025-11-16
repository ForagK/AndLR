package com.lyannyi.lr11.activity

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.lyannyi.lr11.ui.screen.FileManagerScreen
import com.lyannyi.lr11.viewmodel.FileManagerViewModel
import kotlin.getValue

class FileManagerActivity() : ComponentActivity() {
    private val viewModel: FileManagerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            0
        )
        setContent {
            FileManagerScreen(viewModel, this)
        }
    }
}