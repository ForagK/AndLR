package com.lyannyi.lr4.ui.counter

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun WorkDayCounter(workDaysCount: Int) {
    Text(text = "Робочих днів: $workDaysCount")
}