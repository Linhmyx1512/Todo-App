package com.example.todoapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.ItemTaskBinding
import com.example.todoapp.utils.format

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
                taskTime.text = task.dueTime.format() // https://kotlinlang.org/docs/extensions.html#extensions-are-resolved-statically
                taskPriority.text = task.priority

                val textColor = when (task.priority) {
                    "Medium" -> Color.parseColor("#B8A30C")
                    "Low" -> Color.BLUE
                    else -> Color.RED
                }
                taskPriority.setTextColor(textColor)
                taskCheckbox.isChecked = task.isDone
                val backgroundColor = if (task.isDone) {
                    Color.parseColor("#97f089")
                } else {
                    Color.WHITE
                }
//                taskLayout.setBackgroundColor(backgroundColor)
                taskLayout.setCardBackgroundColor(backgroundColor)
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
                updateCallback("update", holder.adapterPosition, task) // hard code - nên tạo 1 enum
            }
        }
        holder.itemTaskBinding.taskCheckbox.setOnClickListener {
            if (holder.adapterPosition != -1) {
                updateCallback(
                    "complete",
                    holder.adapterPosition,
                    task.copy(isDone = !task.isDone),
                )

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