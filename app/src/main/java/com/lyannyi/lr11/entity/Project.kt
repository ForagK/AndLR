package com.lyannyi.lr11.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true) val projectId: Int = 0,
    val name: String,
    val deadline: Date,
    val responsible: String,
)
