package com.example.todoapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.Task
import java.text.SimpleDateFormat
import java.util.Locale

class TaskRecyclerViewAdapter(
    private val deleteUpdateCallback: (type: String, position: Int, task: Task) -> Unit
) :
    RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder>() {

    private val taskList: ArrayList<Task> = arrayListOf<Task>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskName: TextView = itemView.findViewById(R.id.task_name)
        private val taskDescription: TextView = itemView.findViewById(R.id.task_description)
        private val taskTime: TextView = itemView.findViewById(R.id.task_time)
        private val taskPriority: TextView = itemView.findViewById(R.id.task_priority)
        val btnDelete: ImageView = itemView.findViewById(R.id.btn_delete)
        val btnEdit: ImageView = itemView.findViewById(R.id.btn_edit)

        fun bind(task: Task) {
            taskName.text = task.title
            taskDescription.text = task.description
            taskTime.text =
                SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault()).format(task.dueTime)
            taskPriority.text = task.priority

        }
    }

    fun addAllTask(newTaskList: List<Task>) {
        taskList.clear()
        taskList.addAll(newTaskList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_task, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = taskList[position]
        holder.bind(task)
        holder.btnDelete.setOnClickListener {
            if (holder.adapterPosition != -1) {
                deleteUpdateCallback("delete", holder.adapterPosition, task)
            }
        }
        holder.btnEdit.setOnClickListener {
            if (holder.adapterPosition != -1) {
                deleteUpdateCallback("update", holder.adapterPosition, task)
            }
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

}