package com.lyannyi.lr11.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import kotlin.getValue
import com.lyannyi.lr11.ui.screen.ProjectsScreen
import com.lyannyi.lr11.viewmodel.ProjectsViewModel

class ProjectsActivity : ComponentActivity() {
    private val viewModel: ProjectsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectsScreen(viewModel)
        }
    }
}