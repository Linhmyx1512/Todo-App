package com.example.todoapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Task
import com.example.todoapp.data.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val taskRepository = TaskRepository(application)

    private val _tasks: MutableLiveData<List<Task>> = MutableLiveData()
    val tasks: LiveData<List<Task>> = _tasks


    fun setSortBy(sort: Pair<String, Boolean>) {
        viewModelScope.launch(Dispatchers.IO) {
            _tasks.postValue(taskRepository.setSortBy(sort))
        }
    }

    fun getAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            _tasks.postValue(taskRepository.getAllTasks())
        }
    }

    fun insertTask(task: Task) = viewModelScope.launch {
        if (taskRepository.insertTask(task) != -1L) {
            getAllTasks()
        }
    }

    fun deleteTask(taskId: Int) = viewModelScope.launch {
        taskRepository.deleteTask(taskId)
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        taskRepository.updateTask(task)
    }

    fun searchTask(query: String) = viewModelScope.launch {
        taskRepository.searchTask(query)
    }

}