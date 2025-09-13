package com.route.todoapp

import android.app.Application
import com.route.todoapp.database.MyDatabase

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize application-wide resources here if needed
        MyDatabase.init(this)
    }
}