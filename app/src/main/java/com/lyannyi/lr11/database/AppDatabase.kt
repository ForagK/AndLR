package com.lyannyi.lr11.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lyannyi.lr11.dao.ProjectDao
import com.lyannyi.lr11.dao.TaskDao
import com.lyannyi.lr11.entity.Task
import com.lyannyi.lr11.data.Converters
import com.lyannyi.lr11.entity.Project

@Database(entities = [Task::class, Project::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun projectDao(): ProjectDao
}
