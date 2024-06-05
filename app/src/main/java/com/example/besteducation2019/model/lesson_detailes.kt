
package com.example.besteducation2019.model

import com.google.gson.annotations.SerializedName

data class lesson_datailes (

    @SerializedName("status") var status : String,
    @SerializedName("errors") var errors : Errors,
    @SerializedName("data") var data : Data5
)

data class Data5(
    @SerializedName("lesson") var lesson : Lesson2
)

data class Lesson2(

    @SerializedName("id") var id : Int,
    @SerializedName("name") var name : String,
    @SerializedName("type") var type : String,
    @SerializedName("video") var video : String,
    @SerializedName("duration") var duration : String,
    @SerializedName("resource") var resource : String,
    @SerializedName("quiz") var quiz : Quiz,
    @SerializedName("previous") var previous : Previous,
    @SerializedName("next") var next : String,
//    @SerializedName("finishers") var finishers : ArrayList<String>,
    @SerializedName("is_open") var isOpen : Boolean
)


data class Previous (

    @SerializedName("id") var id : Int,
    @SerializedName("name") var name : String,
    @SerializedName("type") var type : String,
    @SerializedName("duration") var duration : Int,
    @SerializedName("quiz") var quiz : String,
    @SerializedName("is_open") var isOpen : Boolean

)
