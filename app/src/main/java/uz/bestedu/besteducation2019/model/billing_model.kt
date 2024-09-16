package uz.bestedu.besteducation2019.model

import com.google.gson.annotations.SerializedName

data class billing_model (


    @SerializedName("status" ) var status : String?         = null,
    @SerializedName("errors" ) var errors : Any?         = Any(),
    @SerializedName("data"   ) var data   : ArrayList<Data100> = arrayListOf()
)

data class Data100 (

    @SerializedName("author"  ) var author  : Author? =null ,
    @SerializedName("course"  ) var course  : Course100? = Course100(),
    @SerializedName("order"   ) var order   : Order?  = Order(),
    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("created" ) var created : String? = null

)


data class Course100 (

    @SerializedName("id"    ) var id    : Int?    = null,
    @SerializedName("name"  ) var name  : String? = null,
    @SerializedName("price" ) var price : Int?    = null

)



data class Order (

    @SerializedName("id"     ) var id     : Int? = null,
    @SerializedName("amount" ) var amount : Int? = null

)