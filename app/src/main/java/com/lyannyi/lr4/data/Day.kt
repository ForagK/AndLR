package com.lyannyi.lr4.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class Day(
    val initialSchedule: String = "",
    val initialIsWorkDay: Boolean = false
) {
    var schedule by mutableStateOf(initialSchedule)
    var isWorkDay by mutableStateOf(initialIsWorkDay)
}