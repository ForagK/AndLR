package com.lyannyi.lr11.data

import android.app.Application
import androidx.room.Room
import com.google.firebase.FirebaseApp
import com.lyannyi.lr11.database.AppDatabase

class App : Application() {
    var database: AppDatabase? = null
        private set

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
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
