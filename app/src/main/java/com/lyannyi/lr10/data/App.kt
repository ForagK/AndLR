package com.lyannyi.lr10.data

import android.app.Application
import androidx.room.Room
import com.lyannyi.lr10.database.AppDatabase

class App : Application() {
    var database: AppDatabase? = null
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "tasks-db"
        ).build()
    }

    companion object {
        var instance: App? = null
    }
}
