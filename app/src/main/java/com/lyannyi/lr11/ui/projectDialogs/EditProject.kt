package com.lyannyi.lr11.ui.projectDialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.lyannyi.lr11.entity.ProjectWithTasks
import com.lyannyi.lr11.ui.taskDialogs.AddTaskDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProjectDialog(
    showDialog: Boolean,
    project: ProjectWithTasks?,
    onDismiss: () -> Unit,
    onEditProjectWithTasks: (ProjectWithTasks) -> Unit
) {
    if (!showDialog || project == null) return

    val dateFormat = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }

    var projectName by remember { mutableStateOf(project.project.name) }
    var projectDeadline by remember { mutableStateOf<Date?>(project.project.deadline) }
    var responsible by remember { mutableStateOf(project.project.responsible) }

    var tasks by remember { mutableStateOf(project.tasks) }

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
        title = { Text("Edit Project") },
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


            }
            AddTaskDialog(
                showDialog = showAddTaskDialog,
                onDismiss = { showAddTaskDialog = false },
                onAddTask = { task ->
                    val newTaskWithId = task.copy(projectPartId = project.project.projectId)
                    tasks = tasks + newTaskWithId
                }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (projectName.isNotBlank() && projectDeadline != null) {
                        val updatedProject = project.project.copy(
                            name = projectName,
                            deadline = projectDeadline!!,
                            responsible = responsible
                        )

                        val updatedProjectWithTasks = project.copy(
                            project = updatedProject,
                            tasks = tasks
                        )

                        onEditProjectWithTasks(updatedProjectWithTasks)
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