package com.route.todoapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.route.todoapp.database.dao.TaskDao
import com.route.todoapp.models.TaskDM

@Database(entities = [TaskDM::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        private var database: MyDatabase? = null

        fun init(context: android.content.Context) {
            if (database == null) {
                database = androidx.room.Room.databaseBuilder(
                    context.applicationContext, MyDatabase::class.java, "TasksDB"
                ).fallbackToDestructiveMigration(true).allowMainThreadQueries().build()
            }
        }

        fun getInstance(): MyDatabase {

            return database!!
        }
    }

}