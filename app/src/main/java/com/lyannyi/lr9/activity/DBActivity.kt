package com.lyannyi.lr9.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import kotlin.getValue
import com.lyannyi.lr9.ui.screen.DBScreen
import com.lyannyi.lr9.viewmodel.DBViewModel

class DBActivity : ComponentActivity() {
    private val viewModel: DBViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DBScreen(viewModel)
        }
    }
}