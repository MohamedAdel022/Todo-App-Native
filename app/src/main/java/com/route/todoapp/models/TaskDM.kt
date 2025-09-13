package com.route.todoapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tasks")
data class TaskDM(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo
    var title: String ,
    @ColumnInfo
    var description: String = "",
    @ColumnInfo
    var dueDate: Long,
    @ColumnInfo
    var isCompleted: Boolean
)
