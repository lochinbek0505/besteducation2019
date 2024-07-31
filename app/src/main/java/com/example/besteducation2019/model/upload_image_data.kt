package com.example.besteducation2019.model

data class upload_image_data(
    val data: Data23,
    val errors: Any,
    val status: String
)



data class Data23(
    val image: Image,
    val message: String
)