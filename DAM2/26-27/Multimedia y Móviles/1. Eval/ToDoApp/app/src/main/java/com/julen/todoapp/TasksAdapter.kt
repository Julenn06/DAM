package com.julen.todoapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TasksAdapter(
    var tasks: List<Task>,
    private val onTaskSelected: (Int) -> Unit,
    private val onTaskLongClick: (Int) -> Unit // Exercise 1
) : RecyclerView.Adapter<TasksViewHolder>() {

    override fun getItemCount() = tasks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_todo_task, parent, false)
        return TasksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.render(tasks[position])
        holder.itemView.setOnClickListener { onTaskSelected(position) }
        holder.itemView.setOnLongClickListener {
            onTaskLongClick(position)
            true
        }
    }
}
