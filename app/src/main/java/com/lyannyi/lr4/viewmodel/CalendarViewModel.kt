package com.lyannyi.lr4.viewmodel

import androidx.lifecycle.ViewModel
import com.lyannyi.lr4.data.Day

class CalendarViewModel() : ViewModel() {
    private val days = listOf("Понеділок", "Вівторок", "Середа", "Четвер", "П’ятниця", "Субота", "Неділя")

    val dayStates: Map<String, Day> = days.associateWith { Day() }

    val workDaysCount: Int
        get() = dayStates.values.count { it.isWorkDay }

    fun updateSchedule(day: String, schedule: String) {
        dayStates[day]?.schedule = schedule
    }

    fun updateWorkDay(day: String, isWorkDay: Boolean) {
        dayStates[day]?.isWorkDay = isWorkDay
    }
}