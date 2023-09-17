package com.example.todoapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.data.Task
import com.example.todoapp.data.repository.TaskRepository
import com.example.todoapp.utils.Resource

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val taskRepository = TaskRepository(application)

    fun getTaskList() = taskRepository.getTaskList()

    fun insertTask(task: Task): MutableLiveData<Resource<Long>> {
        return taskRepository.insertTask(task)
    }

    fun deleteTask(taskId: Int): MutableLiveData<Resource<Int>> {
        return taskRepository.deleteTask(taskId)
    }
}