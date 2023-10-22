package com.example.todoapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.format(): String {
    return SimpleDateFormat(
        "dd/MM/yyyy HH:mm",
        Locale.getDefault()
    ).format(this)
}