package com.lyannyi.lr4.ui.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lyannyi.lr4.ui.checkbox.IsWorkDayCheckbox
import com.lyannyi.lr4.ui.textField.ScheduleTextField

@Composable
fun DayItem(
    day: String,
    schedule: String,
    isWorkDay: Boolean,
    onScheduleChange: (String) -> Unit,
    onWorkDayChange: (Boolean) -> Unit
) {
    Column(modifier = Modifier.width(250.dp).fillMaxHeight().padding(12.dp)) {
        Text(text = day)

        IsWorkDayCheckbox(isWorkDay = isWorkDay, onWorkDayChange = onWorkDayChange)

        ScheduleTextField(schedule = schedule, onScheduleChange = onScheduleChange)
    }
}