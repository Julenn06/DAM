package com.example.caendario

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CoursesAdapter : RecyclerView.Adapter<CoursesAdapter.CourseViewHolder>() {

    private var courses = listOf<Course>()

    fun submitList(list: List<Course>) {
        courses = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return CourseViewHolder(view)
    }

    override fun getItemCount() = courses.size

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        holder.name.text = course.name
        holder.section.text = course.section
    }

    class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(android.R.id.text1)
        val section: TextView = view.findViewById(android.R.id.text2)
    }
}
