package com.example.todoapp.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.adapters.TaskRecyclerViewAdapter
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.FragmentAllTaskBinding
import com.example.todoapp.ui.add.AddTaskBottomSheet
import com.example.todoapp.ui.add.CallBack
import com.example.todoapp.utils.Status
import com.example.todoapp.utils.longToastShow
import com.example.todoapp.viewmodels.TaskViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date


class AllTaskFragment : Fragment() {

    private lateinit var taskBinding: FragmentAllTaskBinding

    private val taskViewModel: TaskViewModel by lazy {
        ViewModelProvider(this)[TaskViewModel::class.java]
    }

    private val taskRecyclerViewAdapter: TaskRecyclerViewAdapter by lazy {
        TaskRecyclerViewAdapter { position, task ->
            taskViewModel
                .deleteTask(task.id)
                .observe(this) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            if (it.data != -1) {
                                requireContext().longToastShow("Task deleted successfully")
                            }
                        }

                        Status.ERROR -> {
                            it.message?.let { it1 ->
                                requireContext().longToastShow(it1)
                            }
                        }
                    }
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        taskBinding = FragmentAllTaskBinding.inflate(layoutInflater, container, false)
        taskBinding.listTask.adapter = taskRecyclerViewAdapter

        return taskBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callGetTaskList()
        setOnClickListener()

    }


    private fun setOnClickListener() {

        // Press btn add to show dialog add new task
        taskBinding.btnAdd.setOnClickListener {
            AddTaskBottomSheet(object : CallBack {
                override fun save(name: String, description: String, time: Date, priority: String) {
                    val newTask = Task(
                        0,
                        name.trim(),
                        description.trim(),
                        time,
                        priority
                    )
                    taskViewModel.insertTask(newTask).observe(viewLifecycleOwner) {
                        when (it.status) {
                            Status.SUCCESS -> {
                                if (it?.data?.toInt() != -1) {
                                    requireContext().longToastShow("Task added successfully")
                                }
                            }

                            Status.ERROR -> {
                                it.message?.let { it1 ->
                                    requireContext().longToastShow(it1)
                                }
                            }
                        }
                    }
                }
            }).show(childFragmentManager, "Show dialog add task")
        }
    }

    private fun callGetTaskList() {
        CoroutineScope(Dispatchers.Main).launch {
            taskViewModel.getTaskList().collect {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.collect { taskList ->
                            taskRecyclerViewAdapter.addAllTask(taskList)

                        }
                    }

                    Status.ERROR -> {
                        it.message?.let { it1 ->
                            requireContext().longToastShow(it1)
                        }
                    }
                }

            }
        }
    }
}