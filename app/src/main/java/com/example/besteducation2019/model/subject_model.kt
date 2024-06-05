package com.example.besteducation2019.model

data class subject_model (
    val data: Data8,
    val errors: Errors,
    val status: String
)

data class Data8 (
    val subjects: List<Subject>
)

data class Subject (
    val name: String,
    val id: Int
)

