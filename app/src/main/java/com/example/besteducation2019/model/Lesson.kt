package com.example.besteducation2019.model

import com.google.gson.annotations.SerializedName

data class Lesson (

    @SerializedName("id") var id : Int,
    @SerializedName("name") var name : String,
    @SerializedName("type") var type : String,
    @SerializedName("duration") var duration : Int,
    @SerializedName("quiz") var quiz : String,
    @SerializedName("is_open") var isOpen : Boolean

)