package com.julen.todoapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CategoriesAdapter(
    private val categories: List<TaskCategory>, private val onCategorySelected: (Int) -> Unit
) : RecyclerView.Adapter<CategoriesViewHolder>() {

    override fun getItemCount() = categories.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_task_category, parent, false)
        return CategoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.render(categories[position], onCategorySelected)
    }
}
