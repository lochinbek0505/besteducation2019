package com.example.besteducation2019.network

import com.example.besteducation2019.model.courses_model
import com.example.besteducation2019.model.login_model
import com.example.besteducation2019.model.login_respons
import com.example.besteducation2019.model.register_model
import com.example.besteducation2019.model.register_respons
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("auth/signup/")
    fun register(@Body dataModel: register_model?): Call<register_respons>

    @POST("auth/login/")
    fun login(@Body dataModel: login_model?): Call<login_respons>


    @GET("courses/")
    fun courses(): Call<courses_model>

}
