package com.example.besteducation2019.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Question(
    @SerializedName("json") var json : Json
): Serializable