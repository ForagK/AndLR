package com.lyannyi.lr9.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lyannyi.lr9.dao.TaskDao
import com.lyannyi.lr9.entity.Task
import com.lyannyi.lr9.data.Converters

@Database(entities = [Task::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
