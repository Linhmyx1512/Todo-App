package com.example.todoapp.data.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.data.Task
import com.example.todoapp.data.TaskDao
import com.example.todoapp.data.TaskDatabase
import com.example.todoapp.utils.Resource
import com.example.todoapp.utils.Resource.Error
import com.example.todoapp.utils.Resource.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class TaskRepository(application: Application) {
    private val taskDao: TaskDao = TaskDatabase.getInstance(application).taskDao

    fun getTaskList() = flow {
        try {
            val result = taskDao.getTaskList()
            emit(Success(result))
        } catch (e: Exception) {
            emit(Error(e.message.toString()))
        }
    }


    fun insertTask(task: Task) = MutableLiveData<Resource<Long>>().apply {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val result = taskDao.insertTask(task)
                postValue(Success(result))
            }
        } catch (e: Exception) {
            postValue(Error(e.message.toString()))
        }
    }

    fun deleteTask(taskId: Int) = MutableLiveData<Resource<Int>>().apply {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val result = taskDao.deleteTask(taskId)
                postValue(Success(result))
            }
        } catch (e: Exception) {
            postValue(Error(e.message.toString()))
        }
    }
}