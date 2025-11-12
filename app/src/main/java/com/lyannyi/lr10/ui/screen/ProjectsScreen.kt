package com.lyannyi.lr10.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lyannyi.lr10.entity.ProjectWithTasks
import com.lyannyi.lr10.item.ProjectItem
import com.lyannyi.lr10.ui.projectDialogs.AddProjectDialog
import com.lyannyi.lr10.ui.projectDialogs.EditProjectDialog
import com.lyannyi.lr10.viewmodel.ProjectsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(viewModel: ProjectsViewModel) {
    val projects by viewModel.projectsWithTasks

    var showAddProjectDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    var selectedProject by remember { mutableStateOf<ProjectWithTasks?>(null) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadProjectsWithTasks()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            LazyColumn(Modifier.fillMaxWidth()) {
                items(projects) { project ->
                    ProjectItem(
                        viewModel,
                        project,
                        onDeleteClick = { viewModel.deleteProjects(project.project) },
                        onEditClick = {
                            selectedProject = project
                            showEditDialog = true
                        }
                    )
                    HorizontalDivider(thickness = 3.dp)
                }
            }

            if (showAddProjectDialog) {
                AddProjectDialog(
                    showDialog = showAddProjectDialog,
                    onDismiss = { showAddProjectDialog = false },
                    onAddProjectWithTasks = { projectWithTasks, tasks ->
                        coroutineScope.launch {
                            val projectId = viewModel.addProject(projectWithTasks.project)
                            tasks.forEach { task ->
                                val newTask = task.copy(projectPartId = projectId.toInt())
                                viewModel.addTask(newTask)
                            }
                        }
                    }
                )
            }

            if (showEditDialog) {
                EditProjectDialog(
                    showDialog = showEditDialog,
                    onDismiss = { showEditDialog = false },
                    onEditProjectWithTasks = { projectWithTasks ->
                        coroutineScope.launch {
                            viewModel.updateProjectsWithTasks(projectWithTasks)
                        }
                    },
                    project = selectedProject
                )
            }
        }


        FloatingActionButton(
            onClick = { showAddProjectDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
}