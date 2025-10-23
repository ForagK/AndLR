package com.lyannyi.lr7.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.lyannyi.lr7.ui.screen.FileManagerScreen
import com.lyannyi.lr7.viewmodel.FileManagerViewModel
import kotlin.getValue

class FileManagerActivity : ComponentActivity() {
    private val viewModel: FileManagerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FileManagerScreen(viewModel)
        }
    }
}