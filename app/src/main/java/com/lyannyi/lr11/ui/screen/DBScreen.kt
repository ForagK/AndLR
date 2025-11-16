package com.lyannyi.lr11.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lyannyi.lr11.item.FirestoreTaskItem
import com.lyannyi.lr11.ui.firestoreTaskDialogs.AddFirestoreTaskDialog
import com.lyannyi.lr11.viewmodel.DBViewModel

@Composable
fun DBScreen(viewModel: DBViewModel) {
    val tasks by viewModel.tasks

    var showAddDialog by remember { mutableStateOf(false) }

    var showAll by remember { mutableStateOf(true) }

    LaunchedEffect(showAll) {
        if (showAll) viewModel.loadTasks() else viewModel.loadUnfinishedTasks()
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text("Show Completed")
                Switch(
                    checked = showAll,
                    onCheckedChange = { showAll = it },
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            LazyColumn(Modifier.fillMaxWidth()) {
                items(tasks) { task ->
                    FirestoreTaskItem(
                        task,
                        onDeleteClick = { viewModel.deleteTask(task) },
                        onEditClick = {
                            updatedTask -> viewModel.updateTask(updatedTask)
                        }
                    )
                    HorizontalDivider()
                }
            }

            AddFirestoreTaskDialog(
                showDialog = showAddDialog,
                onDismiss = { showAddDialog = false },
                onAddTask = { task ->
                    viewModel.addTask(task)
                }
                )
        }
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
}