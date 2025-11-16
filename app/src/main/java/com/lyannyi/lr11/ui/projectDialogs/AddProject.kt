package com.lyannyi.lr11.ui.projectDialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lyannyi.lr11.entity.Project
import com.lyannyi.lr11.entity.ProjectWithTasks
import com.lyannyi.lr11.entity.Task
import com.lyannyi.lr11.item.TaskItem
import com.lyannyi.lr11.ui.taskDialogs.AddTaskDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProjectDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onAddProjectWithTasks: (ProjectWithTasks, List<Task>) -> Unit
) {
    if (!showDialog) return

    val dateFormat = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }

    var projectName by remember { mutableStateOf("") }
    var projectDeadline by remember { mutableStateOf<Date?>(null) }
    var responsible by remember { mutableStateOf("") }

    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }

    var nextTaskId by remember { mutableStateOf(1) }

    var showProjectDateDialog by remember { mutableStateOf(false) }
    var showAddTaskDialog by remember { mutableStateOf(false) }

    if (showProjectDateDialog) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showProjectDateDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        projectDeadline = Date(millis)
                    }
                    showProjectDateDialog = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showProjectDateDialog = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Project") },
        text = {
            Column {
                OutlinedTextField(
                    value = projectName,
                    onValueChange = { projectName = it },
                    label = { Text("Project Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = responsible,
                    onValueChange = { responsible = it },
                    label = { Text("Responsible Person") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = projectDeadline?.let { dateFormat.format(it) } ?: "",
                    onValueChange = {},
                    label = { Text("Project Deadline") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showProjectDateDialog = true },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showProjectDateDialog = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = null)
                        }
                    }
                )

                Button(
                    onClick = { showAddTaskDialog = true }
                ) { Text("Add Task") }

                AddTaskDialog(
                    showDialog = showAddTaskDialog,
                    onDismiss = { showAddTaskDialog = false },
                    onAddTask = { task -> tasks = tasks + task.copy(taskId = nextTaskId++) }
                )

                if (tasks.isNotEmpty()) {
                    Text("Tasks Added:", modifier = Modifier.padding(top = 8.dp))
                    tasks.forEach {
                        TaskItem(
                            task = it,
                            onDeleteClick = { taskToDelete ->
                                tasks = tasks.filterNot { it == taskToDelete }
                            },
                            onEditClick = { updatedTask ->
                                tasks = tasks.map { if (it.taskId == updatedTask.taskId) updatedTask else it }
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (projectName.isNotBlank() && projectDeadline != null) {
                        val project = Project(
                            name = projectName,
                            deadline = projectDeadline!!,
                            responsible = responsible
                        )

                        val tasksToSave = tasks.map { it.copy(taskId = 0) }

                        val projectWithTasks = ProjectWithTasks(
                            project = project,
                            tasks = tasksToSave
                        )
                        onAddProjectWithTasks(projectWithTasks, tasksToSave)
                        onDismiss()
                    }
                }
            ) { Text("Save Project") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}