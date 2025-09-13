package com.route.todoapp.database.dao

import androidx.room.Dao
import androidx.room.*
import com.route.todoapp.models.TaskDM

@Dao
interface TaskDao {
   @Update
   fun updateTask(task: TaskDM)
    @Delete
  fun  deleteTask( task: TaskDM)
    @Insert
   fun insertTask( task: TaskDM)
    @Query("SELECT * FROM Tasks")
  fun  getAllTasks() : List<TaskDM>
    @Query("SELECT * FROM Tasks WHERE dueDate = :date")
    fun getTaskByDate(date: Long): List<TaskDM>
}