package com.example.besteducation2019.model

data class register_respons (
    val data: Data3,
    val errors: Errors3,
    val status: String
)

data class Data3(
    val lastName: String,
    val firstName: String,
    val token: String
)

class Errors3()

