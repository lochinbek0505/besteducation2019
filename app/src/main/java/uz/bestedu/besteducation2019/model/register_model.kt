package uz.bestedu.besteducation2019.model

data class register_model(

    val username: String,
    val first_name: String,
    val middle_name: String,
    val last_name: String,
    val password: String,
    val is_student: String = "true",
    val bio: String = ""


)
