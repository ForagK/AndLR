package com.lyannyi.lr10.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lyannyi.lr10.entity.Task
import com.lyannyi.lr10.ui.taskDialogs.EditTaskDialog
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TaskItem(
    task: Task,
    onDeleteClick: (Task) -> Unit,
    onEditClick: (Task) -> Unit)
{
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(task.deadline)
    var showTaskEditDialog by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Id: ${task.taskId}")
            Text("Name: ${task.name}")
            Text("Status: " + if (task.status) "completed" else "not completed")
            Text("Deadline: $formattedDate")
        }
        Row {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp)
                    .clickable { showTaskEditDialog = true }
            )
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onDeleteClick(task) }
            )
        }
        EditTaskDialog(
            showDialog = showTaskEditDialog,
            task = task,
            onDismiss = { showTaskEditDialog = false },
            onConfirm = { updatedTask -> onEditClick(updatedTask) }
        )
    }
}