package com.example.todoapp.ui.task

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.adapters.TaskRecyclerViewAdapter
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.FragmentAllTaskBinding
import com.example.todoapp.ui.add.AddTaskBottomSheet
import com.example.todoapp.ui.add.CallBack
import com.example.todoapp.ui.update.UpdateTaskBottomSheet
import com.example.todoapp.utils.Status
import com.example.todoapp.utils.StatusResult
import com.example.todoapp.utils.StatusResult.Added
import com.example.todoapp.utils.StatusResult.Deleted
import com.example.todoapp.utils.StatusResult.Updated
import com.example.todoapp.utils.hideKeyBoard
import com.example.todoapp.utils.longToastShow
import com.example.todoapp.viewmodels.TaskViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AllTaskFragment : Fragment() {

    private lateinit var taskBinding: FragmentAllTaskBinding
    private lateinit var taskRecyclerViewAdapter: TaskRecyclerViewAdapter

    private val taskViewModel: TaskViewModel by lazy {
        ViewModelProvider(this)[TaskViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        taskBinding = FragmentAllTaskBinding.inflate(layoutInflater, container, false)

        return taskBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskBinding = FragmentAllTaskBinding.bind(view)
        setUpTaskRecyclerViewAdapter()
        taskBinding.listTask.adapter = taskRecyclerViewAdapter
        taskRecyclerViewAdapter.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                taskBinding.listTask.smoothScrollToPosition(positionStart)
            }
        })
        callGetTaskList()
        taskViewModel.getTaskList()
        statusCallback()
        setOnClickListener()

    }

    private fun statusCallback() {
        taskViewModel
            .statusLiveData
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.LOADING -> {
                    }

                    Status.SUCCESS -> {
                        when (it.data as StatusResult) {
                            Added -> {
                                Log.d("StatusResult", "Added")
                            }

                            Deleted -> {
                                Log.d("StatusResult", "Deleted")
                            }

                            Updated -> {
                                Log.d("StatusResult", "Updated")
                            }
                        }
                        it.message?.let { it1 ->
                            requireContext().longToastShow(it1)
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

    private fun setUpTaskRecyclerViewAdapter() {
        taskRecyclerViewAdapter = TaskRecyclerViewAdapter { type, position, inputTask ->
            if (type == "delete") {
                taskViewModel
                    .deleteTask(inputTask.id)


            } else if (type == "update") {
                UpdateTaskBottomSheet.newInstance(inputTask, object : CallBack {
                    override fun save(task: Task) {
                        taskViewModel.updateTask(task)
                    }
                }).show(this.childFragmentManager, "Show update task dialog")
            }
        }
    }


    private fun setOnClickListener() {

        // Press btn add to show dialog add new task
        taskBinding.btnAdd.setOnClickListener {
            AddTaskBottomSheet(object : CallBack {
                override fun save(task: Task) {
                    requireContext().hideKeyBoard(it)
                    taskViewModel.insertTask(task)
                }
            }).show(childFragmentManager, "Show dialog add task")
        }
    }

    private fun callGetTaskList() {
        CoroutineScope(Dispatchers.Main).launch {
            taskViewModel.taskStateFlow.collectLatest {
                Log.d("status", it.status.toString())
                when (it.status) {
                    Status.LOADING -> {
                    }

                    Status.SUCCESS -> {
                        it.data?.collect { taskList ->
                            taskRecyclerViewAdapter.submitList(taskList)
                        }
                    }
                    Status.ERROR -> {
                        it.message?.let { it1 -> requireContext().longToastShow(it1) }
                    }
                }
            }
        }
    }
}