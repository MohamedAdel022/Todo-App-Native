package com.route.todoapp.models

data class TaskDM(
    val id: Int,
    val title: String,
    val description: String,
    val dueDate: Long,
    val isCompleted: Boolean
)
