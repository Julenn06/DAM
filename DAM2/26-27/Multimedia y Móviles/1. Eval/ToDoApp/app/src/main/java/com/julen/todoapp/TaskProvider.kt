package com.julen.todoapp

class TaskProvider {
    companion object {
        fun getTasks(): MutableList<Task> = mutableListOf(
            Task("Tarea de negocios", TaskCategory.Business),
            Task("Tarea personal", TaskCategory.Personal),
            Task("Otra tarea", TaskCategory.Other),
            Task("Tarea urgente", TaskCategory.Urgent)
        )
    }
}
