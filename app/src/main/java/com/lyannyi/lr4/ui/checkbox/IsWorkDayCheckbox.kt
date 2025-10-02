package com.lyannyi.lr4.ui.checkbox

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun IsWorkDayCheckbox(isWorkDay: Boolean, onWorkDayChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = isWorkDay,
            onCheckedChange = onWorkDayChange
        )
        Text("Робочий")
    }
}