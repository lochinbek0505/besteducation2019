package com.example.besteducation2019.model

import com.google.gson.annotations.SerializedName

data class Module(
    @SerializedName("id") var id : Int,
    @SerializedName("name") var name : String,
    @SerializedName("required") var required : String,
    @SerializedName("video_length") var videoLength : Int,
    @SerializedName("count_students") var countStudents : Int,
    @SerializedName("count_finishers") var countFinishers : Int,
    @SerializedName("count_lessons") var countLessons : Int,
    @SerializedName("students") var students : Any,
    @SerializedName("finishers") var finishers : Any,
    @SerializedName("lessons") var lessons : List<Lesson>,
    @SerializedName("is_open") var isOpen : Boolean

)
