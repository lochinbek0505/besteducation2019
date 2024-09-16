package uz.bestedu.besteducation2019.model

import com.google.gson.annotations.SerializedName

data class change_password_model(

    val old_password:String,
    val new_password:String,

)

data class change_password_response(


    @SerializedName("status" ) var status : String? = null,
    @SerializedName("errors" ) var errors : Any? = Any(),
    @SerializedName("data"   ) var data   : Any?   = Any()


)