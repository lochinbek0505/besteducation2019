package com.example.besteducation2019.model

import com.google.gson.annotations.SerializedName

data class Author(
    @SerializedName("id") var id : Int,
    @SerializedName("phone") var phone : String,
    @SerializedName("first_name") var firstName : String,
    @SerializedName("last_name") var lastName : String,
    @SerializedName("middle_name") var middleName : String,
    @SerializedName("image") var image : String
)