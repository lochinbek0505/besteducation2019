package uz.bestedu.besteducation2019.model

import com.google.gson.annotations.SerializedName

data class login_response(
    val data: Data2,
    val errors: Any,
    val status: String
)

data class Data2(
    @SerializedName("token") var token: String,
    @SerializedName("first_name") var firstName: String,
    @SerializedName("last_name") var lastName: String,
    @SerializedName("id") var id: Int,
    @SerializedName("image") var image: String
)

class Errors2()
