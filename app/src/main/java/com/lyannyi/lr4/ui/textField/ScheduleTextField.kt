package com.lyannyi.lr4.ui.textField

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ScheduleTextField(schedule: String, onScheduleChange: (String) -> Unit) {
    OutlinedTextField(
        value = schedule,
        onValueChange = onScheduleChange,
        label = { Text("Розклад") },
        modifier = Modifier.fillMaxSize()
    )
}