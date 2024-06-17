package com.example.besteducation2019.model

import com.google.gson.annotations.SerializedName

data class course_detailes_full(
    @SerializedName("status") var status : String,
    @SerializedName("errors") var errors : Any,
    @SerializedName("data") var data : Data4
)

data class Data4 (
    val course: Course2
)

data class Course2(
    @SerializedName("name") var name : String,
    @SerializedName("author_") var author_ : Author,
    @SerializedName("image") var image : String,
    @SerializedName("subject_") var subject_ : String,
    @SerializedName("description") var description : String,
    @SerializedName("price") var price : Int,
    @SerializedName("feedback") var feedback : Int,
    @SerializedName("count_modules") var countModules : Int,
    @SerializedName("count_students") var countStudents : Int,
    @SerializedName("students") var students : Any,
    @SerializedName("count_lessons") var countLessons : Int,
    @SerializedName("length") var length : Int,
    @SerializedName("feedbackers") var feedbackers : Any,
    @SerializedName("modules") var modules : List<Module>,
    @SerializedName("is_open") var isOpen : Boolean
)







