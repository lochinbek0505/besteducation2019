package uz.bestedu.besteducation2019.model

import com.google.gson.annotations.SerializedName

data class achiement_model(


    @SerializedName("status" ) var status : String? = null,
    @SerializedName("errors" ) var errors : Any? = Any(),
    @SerializedName("data"   ) var data   : Data200?   = Data200()

)


data class Data200 (

    @SerializedName("ratings" ) var ratings : ArrayList<Ratings200> = arrayListOf()

)


data class Ratings200 (

    @SerializedName("course" ) var course : Course200? = Course200(),
    @SerializedName("module" ) var module : Int?    = null,
    @SerializedName("lesson" ) var lesson : Int?    = null,
    @SerializedName("score"  ) var score  : Int?    = null

)

data class Course200 (

    @SerializedName("id"    ) var id    : Int?    = null,
    @SerializedName("name"  ) var name  : String? = null,
    @SerializedName("price" ) var price : Int?    = null

)