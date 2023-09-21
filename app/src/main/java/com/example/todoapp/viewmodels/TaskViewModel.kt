package com.example.todoapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.todoapp.data.Task
import com.example.todoapp.data.repository.TaskRepository

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val taskRepository = TaskRepository(application)

    val taskStateFlow get() = taskRepository.taskStateFlow
    val statusLiveData get() = taskRepository.statusLiveData

    fun getTaskList() = taskRepository.getTaskList()

    fun insertTask(task: Task) {
        return taskRepository.insertTask(task)
    }

    fun deleteTask(taskId: Int) {
        return taskRepository.deleteTask(taskId)
    }

    fun updateTask(task: Task) {
        return taskRepository.updateTask(task)
    }

}