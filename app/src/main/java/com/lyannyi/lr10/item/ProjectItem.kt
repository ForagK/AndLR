package com.lyannyi.lr10.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lyannyi.lr10.entity.ProjectWithTasks
import com.lyannyi.lr10.viewmodel.ProjectsViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ProjectItem(
    viewModel: ProjectsViewModel,
    project: ProjectWithTasks,
    onDeleteClick: (ProjectWithTasks) -> Unit,
    onEditClick: (ProjectWithTasks) -> Unit) {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(project.project.deadline)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Name: ${project.project.name}")
            Text("Deadline: $formattedDate")
            Text("Responsible: ${project.project.responsible}")

            project.tasks.forEach { task ->
                HorizontalDivider()
                TaskItem(
                    task,
                    onDeleteClick = { viewModel.deleteTask(task) },
                    onEditClick = { updatedTask -> viewModel.updateTask(updatedTask) }
                )
            }
        }
        VerticalDivider(
            color = Color.Gray,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .padding(horizontal = 8.dp)
        )
        Row {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp)
                    .clickable { onEditClick(project) }
            )
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onDeleteClick(project) }
            )
        }

    }
}