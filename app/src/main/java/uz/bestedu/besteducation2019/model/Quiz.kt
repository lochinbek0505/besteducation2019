package uz.bestedu.besteducation2019.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Quiz(

    @SerializedName("name") var name: String,
    @SerializedName("questions") var questions: List<Question>
) : Serializable