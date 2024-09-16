package uz.bestedu.besteducation2019.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Json(

    @SerializedName("question") var question: String,
    @SerializedName("type") var type: String,
    @SerializedName("answers") var answers: List<Answer>
) : Serializable