package com.example.besteducation2019.model

import com.google.gson.annotations.SerializedName

data class get_rating_request(

    @SerializedName("course" ) var course : Int? = null,
    @SerializedName("type" ) var lesson : String? = "",

)
