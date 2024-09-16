package uz.bestedu.besteducation2019.model

import com.google.gson.annotations.SerializedName


data class get_rating_model (

  @SerializedName("status" ) var status : String? = null,
  @SerializedName("errors" ) var errors : Any? = Any(),
  @SerializedName("data"   ) var data   : Data35?   = Data35()

)

data class Data35(

  @SerializedName("ratings" ) var ratings : ArrayList<Ratings> = arrayListOf()


)




data class Course47 (

  @SerializedName("id"    ) var id    : Int?    = null,
  @SerializedName("name"  ) var name  : String? = null,
  @SerializedName("price" ) var price : Int?    = null

)

data class Ratings (

  @SerializedName("author" ) var author : Author,
  @SerializedName("course" ) var course : Course47,
  @SerializedName("score"  ) var score  : Int

)