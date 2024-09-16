package uz.bestedu.besteducation2019.model

import com.google.gson.annotations.SerializedName

data class rate_response(



    @SerializedName("status" ) var status : String? = null,
    @SerializedName("errors" ) var errors : Any? = Any(),
    @SerializedName("data"   ) var data   : Data30?   = Data30()

)

data class Data30(


    @SerializedName("message" ) var message : String? = null

)