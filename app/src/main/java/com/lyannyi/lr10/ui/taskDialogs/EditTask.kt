package com.lyannyi.lr10.ui.taskDialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lyannyi.lr10.entity.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskDialog(
    showDialog: Boolean,
    task: Task?,
    onDismiss: () -> Unit,
    onConfirm: (updatedTask: Task) -> Unit
) {
    if (!showDialog || task == null) return

    val dateFormat = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }
    var showDateDialog by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf(task.name) }
    var status by remember { mutableStateOf(task.status) }
    var deadline by remember { mutableStateOf(task.deadline) }

    if (showDateDialog) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = deadline.time)

        DatePickerDialog(
            onDismissRequest = { showDateDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        deadline = Date(millis)
                    }
                    showDateDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDateDialog = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit Task") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Completed")
                    Switch(
                        checked = status,
                        onCheckedChange = { status = it },
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                OutlinedTextField(
                    value = deadline.let { dateFormat.format(it) },
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
            TextButton(onClick = {
                if (name.isNotBlank() && deadline != null) {
                    val updatedTask = task.copy(
                        name = name,
                        status = status,
                        deadline = deadline
                    )
                    onConfirm(updatedTask)
                    onDismiss()
                }
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}