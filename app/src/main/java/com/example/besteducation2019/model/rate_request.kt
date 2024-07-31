package com.example.besteducation2019.model

data class rate_request(
    val course: String,
    val lesson: String,
    val module: String,
    val percent: Int,
    val score: Int
)