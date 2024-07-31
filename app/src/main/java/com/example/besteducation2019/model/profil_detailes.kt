package com.example.besteducation2019.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("first_name") var firstName: String? = null,
    @SerializedName("last_name") var lastName: String? = null,
    @SerializedName("middle_name") var middleName: String? = null,
    @SerializedName("bio") var bio: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("is_student") var isStudent: String? = null
)


data class Data50(

    @SerializedName("user") var user: User? = User()

)

data class profil_detailes(

    @SerializedName("status") var status: String? = null,
    @SerializedName("errors") var errors: Any? = Any(),
    @SerializedName("data") var data: Data50? = Data50()
)
