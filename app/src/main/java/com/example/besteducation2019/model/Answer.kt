package com.example.besteducation2019.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Answer(

    @SerializedName("value_1") var value1: String,
    @SerializedName("value_2") var value2: String,
    @SerializedName("is_correct") var isCorrect: Boolean

) : Serializable