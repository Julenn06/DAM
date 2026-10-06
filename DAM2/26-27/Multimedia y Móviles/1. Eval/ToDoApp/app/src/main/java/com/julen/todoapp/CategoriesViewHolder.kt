package com.julen.todoapp

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class CategoriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvCategoryName: TextView = view.findViewById(R.id.tvCategoryName)
    private val divider: View = view.findViewById(R.id.divider)
    private val viewContainer: CardView = view.findViewById(R.id.viewContainer)

    fun render(taskCategory: TaskCategory, onCategorySelected: (Int) -> Unit) {
        val bgColor = if (taskCategory.isSelected) R.color.todo_background_card
        else R.color.todo_background_disabled
        viewContainer.setCardBackgroundColor(
            ContextCompat.getColor(viewContainer.context, bgColor)
        )
        itemView.setOnClickListener { onCategorySelected(layoutPosition) }
        when (taskCategory) {
            TaskCategory.Business -> {
                tvCategoryName.text = "Negocios"
                divider.setBackgroundColor(
                    ContextCompat.getColor(divider.context, R.color.todo_business_category)
                )
            }

            TaskCategory.Personal -> {
                tvCategoryName.text = "Personal"
                divider.setBackgroundColor(
                    ContextCompat.getColor(divider.context, R.color.todo_personal_category)
                )
            }

            TaskCategory.Other -> {
                tvCategoryName.text = "Otros"
                divider.setBackgroundColor(
                    ContextCompat.getColor(divider.context, R.color.todo_other_category)
                )
            }

            TaskCategory.Urgent -> {
                tvCategoryName.text = "Urgente"
                divider.setBackgroundColor(
                    ContextCompat.getColor(divider.context, R.color.todo_urgent_category)
                )
            }
        }
    }
}
