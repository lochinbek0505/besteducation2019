package com.example.besteducation2019.model

data class buy_response_model(
    val data: Data11,
    val errors: Any,
    val status: String
)

data class Data11(
    val link: String
)

