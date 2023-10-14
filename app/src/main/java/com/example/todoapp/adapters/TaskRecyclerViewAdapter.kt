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
    private val updateCallback: (type: String, position: Int, task: Task) -> Unit
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
                    "Medium" -> Color.parseColor("#bcc906")
                    "Low" -> Color.BLUE
                    else -> Color.RED
                }
                taskPriority.setTextColor(textColor)
                taskCheckbox.isChecked = task.isDone
                val backgroundColor = if (task.isDone) {
                    Color.parseColor("#5fed66")
                } else {
                    Color.WHITE
                }
                taskLayout.setBackgroundColor(backgroundColor)
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
        holder.itemTaskBinding.taskLayout.setOnClickListener {
            if (holder.adapterPosition != -1) {
                updateCallback("update", holder.adapterPosition, task)
            }
        }
        holder.itemTaskBinding.taskCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (holder.adapterPosition != -1) {
                task.isDone = isChecked
                updateCallback("complete", holder.adapterPosition, task)

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