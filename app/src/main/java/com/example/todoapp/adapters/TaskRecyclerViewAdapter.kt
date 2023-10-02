package com.example.todoapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.ItemTaskBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TaskRecyclerViewAdapter(
    private val updateCallback: (position: Int, task: Task) -> Unit
) :
    ListAdapter<Task, TaskRecyclerViewAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(val itemTaskBinding: ItemTaskBinding) :
        RecyclerView.ViewHolder(itemTaskBinding.root) {

        fun bind(task: Task) {
            itemTaskBinding.apply {
                taskName.text = task.title
                taskDescription.text = task.description
                taskTime.text =
                    SimpleDateFormat(
                        "dd-MMM-yyyy HH:mm",
                        Locale.getDefault()
                    ).format(task.dueTime)
                taskPriority.text = task.priority

                val textColor = when (task.priority) {
                    "High" -> Color.RED
                    "Medium" -> Color.GREEN
                    "Low" -> Color.BLUE
                    else -> Color.RED
                }
                taskPriority.setTextColor(textColor)
            }

        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
        holder.itemTaskBinding.task.setOnClickListener {
            if (holder.adapterPosition != -1) {
                updateCallback( holder.adapterPosition, task)
            }
        }
    }


    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }

    }
}