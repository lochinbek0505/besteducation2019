package com.example.besteducation2019.model

import java.io.Serializable

data class courses_modelItem(
    val id: Int=0,
    val name: String="",
    val subject: String="",
    val author: Author,
    val count_lessons: Int=0,
    val count_quizzes: Int=0,
    val created_at: String="",
    val description: String="",
    val feedback: Double=0.0,
    val price: Int=0,
):Serializable