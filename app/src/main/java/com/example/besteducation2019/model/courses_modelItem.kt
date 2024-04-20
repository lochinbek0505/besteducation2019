package com.example.besteducation2019.model

data class courses_modelItem(
    val author: Author,
    val count_lessons: Int,
    val count_quizzes: Int,
    val created_at: String,
    val description: String,
    val feedback: Double,
    val id: Int,
    val name: String,
    val price: Int,
    val subject: String
)