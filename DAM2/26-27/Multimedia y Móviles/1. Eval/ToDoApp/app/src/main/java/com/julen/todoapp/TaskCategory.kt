package com.julen.todoapp

sealed class TaskCategory(var isSelected: Boolean = true) {
    object Business : TaskCategory()
    object Personal : TaskCategory()
    object Other : TaskCategory()
    object Urgent : TaskCategory()
}
