package com.example.todoapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM Task ORDER BY dueTime DESC")
    fun getTaskList(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Query("DELETE FROM Task WHERE taskId == :taskId")
    suspend fun deleteTask(taskId: Int): Int

    @Update
    suspend fun updateTask(task: Task): Int

    @Query("UPDATE Task SET taskTitle=:title, description = :description, priority=:priority WHERE taskId = :taskId")
    suspend fun updateTaskParticularField(taskId: String, title: String, description: String, priority: String): Int
}