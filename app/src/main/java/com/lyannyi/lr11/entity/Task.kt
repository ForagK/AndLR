package com.lyannyi.lr11.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val taskId: Int = 0,
    var projectPartId: Int,
    val name: String,
    val status: Boolean,
    val deadline: Date
)