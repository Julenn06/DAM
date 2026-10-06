package com.julen.todoapp

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TodoActivity : AppCompatActivity() {
    private val categories = CategoryProvider.getCategories()
    private val tasks = TaskProvider.getTasks()

    private lateinit var rvCategories: RecyclerView
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var rvTasks: RecyclerView
    private lateinit var tasksAdapter: TasksAdapter
    private lateinit var fabAddTask: FloatingActionButton
    private lateinit var tvPendingCount: TextView // Exercise 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)
        initComponent()
        initUI()
        initListeners()
        updateTasks()

        // Exercise 5: Load task count from SharedPreferences and show Toast
        val prefs = getSharedPreferences("todo_prefs", MODE_PRIVATE)
        val taskCount = prefs.getInt("task_count", tasks.size)
        Toast.makeText(this, "Tareas guardadas anteriormente: $taskCount", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Exercise 5: Save task count on destroy
        val prefs = getSharedPreferences("todo_prefs", MODE_PRIVATE)
        prefs.edit().putInt("task_count", tasks.size).apply()
    }

    private fun initComponent() {
        rvCategories = findViewById(R.id.rvCategories)
        rvTasks = findViewById(R.id.rvTasks)
        fabAddTask = findViewById(R.id.fabAddTask)
        tvPendingCount = findViewById(R.id.tvPendingCount)
    }

    private fun initListeners() {
        fabAddTask.setOnClickListener { showDialog() }
    }

    private fun initUI() {
        categoriesAdapter = CategoriesAdapter(categories) { position ->
            onCategorySelected(position)
        }
        rvCategories.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false
        )
        rvCategories.adapter = categoriesAdapter

        tasksAdapter = TasksAdapter(
            tasks,
            onTaskSelected = { position -> onTaskSelected(position) },
            onTaskLongClick = { position -> onTaskLongClick(position) } // Exercise 1
        )
        rvTasks.layoutManager = LinearLayoutManager(this)
        rvTasks.adapter = tasksAdapter
    }

    private fun onTaskSelected(position: Int) {
        tasks[position].isSelected = !tasks[position].isSelected
        tasksAdapter.notifyDataSetChanged()
        updatePendingCount() // Exercise 2
    }

    private fun onTaskLongClick(position: Int) {
        // Exercise 1: Delete task on long click
        tasks.removeAt(position)
        updateTasks()
    }

    private fun onCategorySelected(position: Int) {
        categories[position].isSelected = !categories[position].isSelected
        categoriesAdapter.notifyItemChanged(position)
        updateTasks()
    }

    private fun updateTasks() {
        val selectedCategories = categories.filter { it.isSelected }
        val filteredTasks = tasks.filter { selectedCategories.contains(it.category) }
        tasksAdapter.tasks = filteredTasks
        tasksAdapter.notifyDataSetChanged()
        updatePendingCount() // Exercise 2
    }

    private fun updatePendingCount() {
        // Exercise 2: Update pending tasks count
        val pending = tasks.count { !it.isSelected }
        tvPendingCount.text = "$pending tareas pendientes"
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_task)

        val btnAddTask: Button = dialog.findViewById(R.id.btnAddTask)
        val etTask: EditText = dialog.findViewById(R.id.etTask)
        val rgCategories: RadioGroup = dialog.findViewById(R.id.rgCategories)

        // Exercise 3: Improved validation in Dialog
        btnAddTask.isEnabled = false
        etTask.addTextChangedListener {
            btnAddTask.isEnabled = it.toString().isNotEmpty()
        }

        btnAddTask.setOnClickListener {
            val taskName = etTask.text.toString()
            if (taskName.isNotEmpty()) {
                val selectedId = rgCategories.checkedRadioButtonId
                val selectedRB: RadioButton = dialog.findViewById(selectedId)
                // Exercise 4: Handle Urgent category
                val category: TaskCategory = when (selectedRB.text.toString()) {
                    getString(R.string.todo_dialog_category_business) -> TaskCategory.Business
                    getString(R.string.todo_dialog_category_personal) -> TaskCategory.Personal
                    getString(R.string.todo_dialog_category_urgent) -> TaskCategory.Urgent
                    else -> TaskCategory.Other
                }
                tasks.add(Task(taskName, category))
                updateTasks()
                dialog.hide()
            }
        }
        dialog.show()
    }
}
