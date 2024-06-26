package com.example.besteducation2019.model

import com.google.gson.annotations.SerializedName

data class buy_model(

    @SerializedName("order_id") val order_id:String,
    @SerializedName("course") val course:String

)
