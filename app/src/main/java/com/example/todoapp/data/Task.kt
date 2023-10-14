package com.example.todoapp.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "taskId")
    val id: Int,
    @ColumnInfo(name = "taskTitle")
    var title: String,
    val description: String,
    val dueTime: Date,
    val priority: String,
    var isDone: Boolean = false
):Parcelable