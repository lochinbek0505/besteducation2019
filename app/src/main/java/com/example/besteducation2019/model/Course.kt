package com.example.besteducation2019.model

import android.health.connect.datatypes.units.Length

data class Course (
    val feedback: Long,
    val count_students: Long,
    val image: String,
    val author_: Author,
    val subject: String,
    val is_open: Boolean,
    val price: Long,
    val count_lessons:Long,
    val length: Long,
    val count_modules: Long,
    val name: String,
    val description: String,
    val id: Long
)
