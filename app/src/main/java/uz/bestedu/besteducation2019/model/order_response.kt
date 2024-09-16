package uz.bestedu.besteducation2019.model

data class order_response(
    val data: Data10,
    val errors: Any,
    val status: String
)

data class Data10(
    val order_id: Long
)

