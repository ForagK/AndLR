package com.lyannyi.lr11.ui.screen

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lyannyi.lr11.entity.Task
import com.lyannyi.lr11.viewmodel.DBViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DBScreen(viewModel: DBViewModel) {
    val tasks by viewModel.tasks

    var showAddDialog by remember { mutableStateOf(false) }
    var showDateDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    var selectedTask by remember { mutableStateOf<Task?>(null) }

    var newTaskName by remember { mutableStateOf("") }
    var newTaskStatus by remember { mutableStateOf(false) }
    var newTaskDeadline by remember { mutableStateOf<Date?>(null) }

    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    var showAll by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.loadTasks()
    }

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
//                    TaskItem(
//                        task,
//                        onDeleteClick = { viewModel.deleteTask(task) },
////                        onEditClick = {
////                            selectedTask = task
////                            newTaskName = task.name
////                            newTaskStatus = task.status
////                            newTaskDeadline = task.deadline
////                            showEditDialog = true
////                        }
//                    )
                    HorizontalDivider()
                }
            }

            if (showDateDialog) {
                val datePickerState = rememberDatePickerState()

                DatePickerDialog(
                    onDismissRequest = { showDateDialog = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    newTaskDeadline = Date(millis)
                                }
                                showDateDialog = false
                            }
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDateDialog = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            if (showAddDialog) {
                AlertDialog(
                    onDismissRequest = { showAddDialog = false },
                    title = { Text(text = "Add New Task") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = newTaskName,
                                onValueChange = { newTaskName = it },
                                label = { Text("Name") }
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text("Completed")
                                Switch(
                                    checked = newTaskStatus,
                                    onCheckedChange = { newTaskStatus = it },
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                            OutlinedTextField(
                                value = newTaskDeadline?.let { dateFormat.format(it) } ?: "",
                                onValueChange = {},
                                label = { Text("Deadline") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showDateDialog = true },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { showDateDialog = true }) {
                                        Icon(
                                            imageVector = Icons.Default.DateRange,
                                            contentDescription = "Pick date"
                                        )
                                    }
                                }
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                if (newTaskName.isNotBlank() && newTaskDeadline != null) {
                                    viewModel.db?.let {
                                        val newTask = Task(
                                            name = newTaskName,
                                            status = newTaskStatus,
                                            deadline = newTaskDeadline!!,
                                            projectPartId = 0
                                        )
                                        viewModel.addTask(newTask)
                                    }
                                    showAddDialog = false
                                    newTaskName = ""
                                    newTaskStatus = false
                                    newTaskDeadline = null
                                }
                            }
                        ) {
                            Text("Add")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showAddDialog = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }

            if (showEditDialog && selectedTask != null) {
                AlertDialog(
                    onDismissRequest = { showEditDialog = false },
                    title = { Text(text = "Edit Task") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = newTaskName,
                                onValueChange = { newTaskName = it },
                                label = { Text("Name") }
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text("Completed")
                                Switch(
                                    checked = newTaskStatus,
                                    onCheckedChange = { newTaskStatus = it },
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                            OutlinedTextField(
                                value = newTaskDeadline?.let { dateFormat.format(it) } ?: "",
                                onValueChange = {},
                                label = { Text("Deadline") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showDateDialog = true },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { showDateDialog = true }) {
                                        Icon(
                                            imageVector = Icons.Default.DateRange,
                                            contentDescription = "Pick date"
                                        )
                                    }
                                }
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                if (newTaskName.isNotBlank() && newTaskDeadline != null) {
                                    viewModel.db?.let {
                                        val updatedTask = selectedTask!!.copy(
                                            name = newTaskName,
                                            status = newTaskStatus,
                                            deadline = newTaskDeadline!!
                                        )
                                        viewModel.updateTask(updatedTask)
                                    }
                                }
                                showEditDialog = false
                                newTaskName = ""
                                newTaskStatus = false
                                newTaskDeadline = null
                            }
                        ) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showEditDialog = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
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