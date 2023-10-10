package com.example.todoapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Task
import com.example.todoapp.data.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val taskRepository = TaskRepository(application)


    fun setSortBy(sort: Pair<String, Boolean>) {
        taskRepository.setSortBy(sort)
    }

    fun getAllTasks(): LiveData<List<Task>> = taskRepository.getAllTasks()

    fun insertTask(task: Task) = viewModelScope.launch {
        taskRepository.insertTask(task)
    }

    fun deleteTask(taskId: Int) = viewModelScope.launch {
        taskRepository.deleteTask(taskId)
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        taskRepository.updateTask(task)
    }

    fun searchTask(query: String) {
        taskRepository.searchTask(query)
    }

}