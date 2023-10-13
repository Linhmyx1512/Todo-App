package com.example.todoapp.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.adapters.TaskRecyclerViewAdapter
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.FragmentOnProgressBinding
import com.example.todoapp.ui.add.CallBack
import com.example.todoapp.ui.update.UpdateTaskBottomSheet
import com.example.todoapp.viewmodels.TaskViewModel
import com.google.android.material.snackbar.Snackbar


class OnProgressFragment : Fragment() {

    private lateinit var taskRecyclerViewAdapter: TaskRecyclerViewAdapter
    private lateinit var binding: FragmentOnProgressBinding

    private val taskViewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnProgressBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpTaskRecyclerViewAdapter()
        binding.listTask.adapter = taskRecyclerViewAdapter

        taskRecyclerViewAdapter.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.listTask.smoothScrollToPosition(positionStart)
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                binding.listTask.smoothScrollToPosition(0)

            }
        })
        swipeToDeleteTask()
        callGetTaskList()

    }

    private fun setUpTaskRecyclerViewAdapter() {
        taskRecyclerViewAdapter = TaskRecyclerViewAdapter { _, inputTask ->
            UpdateTaskBottomSheet.newInstance(inputTask, object : CallBack {
                override fun save(task: Task) {
                    taskViewModel.updateTask(task)
                }
            }).show(this.childFragmentManager, "Show update task dialog")
        }
    }

    private fun restoreDeletedTask(deletedTask: Task) {
        val snackBar = Snackbar.make(
            binding.root, "Deleted '${deletedTask.title}'",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            taskViewModel.insertTask(deletedTask)
        }
        snackBar.show()
    }

    private fun callGetTaskList() {
        taskViewModel.getAllTasks()
        taskViewModel.tasks.observe(viewLifecycleOwner) {
            taskRecyclerViewAdapter.submitList(it)
        }
    }

    private fun swipeToDeleteTask() {
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val task = taskRecyclerViewAdapter.currentList[position]
                taskViewModel.deleteTask(task.id)
                restoreDeletedTask(task)
            }
        }).attachToRecyclerView(binding.listTask)
    }
}