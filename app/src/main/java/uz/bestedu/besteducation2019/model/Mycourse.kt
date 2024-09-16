package uz.bestedu.besteducation2019.model

import com.google.gson.annotations.SerializedName


data class Mycourse(

    @SerializedName("status") var status: String? = null,
    @SerializedName("errors") var errors: Any? = Any(),
    @SerializedName("data") var data: Data9? = Data9()

)

data class Coursesa(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("percentage") var percentage: Double? = null,
    @SerializedName("author") var author: Author? = null,
    @SerializedName("name") var name: String? = null


)

data class Data9(

    @SerializedName("courses") var courses: ArrayList<Coursesa> = arrayListOf()

)