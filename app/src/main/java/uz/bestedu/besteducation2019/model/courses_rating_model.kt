package uz.bestedu.besteducation2019.model
import com.google.gson.annotations.SerializedName


data class courses_rating_model (

  @SerializedName("status" ) var status : String? = null,
  @SerializedName("errors" ) var errors : Any? = Any(),
  @SerializedName("data"   ) var data   : Data77   = Data77()

)

data class Courses77 (

  @SerializedName("id"   ) var id   : Int?    = null,
  @SerializedName("name" ) var name : String? = null

)

data class Data77 (

  @SerializedName("courses" ) var courses : ArrayList<Courses77> = arrayListOf()

)