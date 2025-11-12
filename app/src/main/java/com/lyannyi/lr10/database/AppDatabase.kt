package com.lyannyi.lr10.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lyannyi.lr10.dao.ProjectDao
import com.lyannyi.lr10.dao.TaskDao
import com.lyannyi.lr10.entity.Task
import com.lyannyi.lr10.data.Converters
import com.lyannyi.lr10.entity.Project

@Database(entities = [Task::class, Project::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun projectDao(): ProjectDao
}
