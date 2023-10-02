package com.example.todoapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.todoapp.data.Task
import com.example.todoapp.data.repository.TaskRepository

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val taskRepository = TaskRepository(application)

    val taskStateFlow get() = taskRepository.taskStateFlow
    val statusLiveData get() = taskRepository.statusLiveData
    val sortByLiveData get() = taskRepository.sortByLiveData
    fun setSortBy(sort: Pair<String, Boolean>) {
        taskRepository.setSortBy(sort)
    }
    fun getTaskList(isAcc: Boolean, sortByName: String) {
        taskRepository.getTaskList(isAcc, sortByName)
    }

    fun insertTask(task: Task) {
        return taskRepository.insertTask(task)
    }

    fun deleteTask(taskId: Int) {
        return taskRepository.deleteTask(taskId)
    }

    fun updateTask(task: Task) {
        return taskRepository.updateTask(task)
    }

    fun searchTask(query: String) {
        taskRepository.searchTask(query)

    }

}