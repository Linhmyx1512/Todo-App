package com.example.todoapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.todoapp.data.Task
import com.example.todoapp.data.TaskDao
import com.example.todoapp.data.TaskDatabase

class TaskRepository(application: Application) {
    private val taskDao: TaskDao = TaskDatabase.getInstance(application).taskDao
    fun getAllTasks() = taskDao.getAllTasks()

    fun getNotDoneTaskList(): List<Task> = taskDao.getNotDoneTaskList()
    fun getDoneTaskList(): List<Task> = taskDao.getDoneTaskList()

    fun setSortBy(sort: Pair<String, Boolean>): List<Task> {
        return when (sort.first) {
            "title" -> taskDao.getTaskListSortByTaskTitle(sort.second)
            "date" -> taskDao.getTaskListSortByTaskDueTime(sort.second)
            else -> taskDao.getAllTasks()
        }
    }

    fun searchTask(query: String): LiveData<List<Task>> = taskDao.searchTask("%${query}%")
    suspend fun insertTask(task: Task) = taskDao.insertTask(task)
    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)
    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

}