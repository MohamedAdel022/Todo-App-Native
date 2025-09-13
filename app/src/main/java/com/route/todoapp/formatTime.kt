package com.route.todoapp

import java.text.SimpleDateFormat
import java.util.*

fun formatTime(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}
