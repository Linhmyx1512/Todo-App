package com.example.todoapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getAllTasks(): List<Task>


    @Query(
        """SELECT * FROM Task ORDER BY
            CASE WHEN :isAcc == 1 THEN TaskTitle END ASC,
            CASE WHEN :isAcc == 0 THEN TaskTitle END DESC"""
    )
    fun getTaskListSortByTaskTitle(isAcc: Boolean): List<Task>

    @Query(
        """SELECT * FROM Task ORDER BY
            CASE WHEN :isAcc == 1 THEN dueTime END ASC,
            CASE WHEN :isAcc == 0 THEN dueTime END DESC"""
    )
    fun getTaskListSortByTaskDueTime(isAcc: Boolean): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun updateTask(task: Task): Int

    @Query("SELECT * FROM Task WHERE taskTitle LIKE :query ORDER BY dueTime ASC")
    fun searchTask(query: String): LiveData<List<Task>>
}