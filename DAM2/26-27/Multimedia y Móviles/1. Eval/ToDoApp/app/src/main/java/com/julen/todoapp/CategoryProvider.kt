package com.julen.todoapp

class CategoryProvider {
    companion object {
        fun getCategories(): List<TaskCategory> = listOf(
            TaskCategory.Business, TaskCategory.Personal, TaskCategory.Other, TaskCategory.Urgent
        )
    }
}
