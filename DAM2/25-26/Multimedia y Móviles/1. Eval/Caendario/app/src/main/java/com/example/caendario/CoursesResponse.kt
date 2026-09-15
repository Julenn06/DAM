package com.example.caendario

data class CoursesResponse(
    val courses: List<Course>
)

data class Course(
    val id: String,
    val name: String,
    val section: String?
)
