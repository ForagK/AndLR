package com.lyannyi.lr4.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lyannyi.lr4.viewmodel.CalendarViewModel
import com.lyannyi.lr4.ui.item.DayItem
import com.lyannyi.lr4.ui.counter.WorkDayCounter

@Composable
fun CalendarScreen(viewModel: CalendarViewModel) {
    val dayStates = viewModel.dayStates
    val workDaysCount = viewModel.workDaysCount

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        WorkDayCounter(workDaysCount)

        LazyRow(modifier = Modifier.fillMaxSize()) {
            items(dayStates.keys.toList()) { day ->
                val state = dayStates[day]!!
                DayItem(
                    day = day,
                    schedule = state.schedule,
                    isWorkDay = state.isWorkDay,
                    onScheduleChange = { viewModel.updateSchedule(day, it) },
                    onWorkDayChange = { viewModel.updateWorkDay(day, it) }
                )
            }
        }
    }
}