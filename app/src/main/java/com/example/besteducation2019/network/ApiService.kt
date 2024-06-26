package com.example.besteducation2019.network

import com.example.besteducation2019.model.Mycourse
import com.example.besteducation2019.model.buy_model
import com.example.besteducation2019.model.buy_response_model
import com.example.besteducation2019.model.course_detailes_full
import com.example.besteducation2019.model.course_model
import com.example.besteducation2019.model.end_model
import com.example.besteducation2019.model.lesson_datailes
import com.example.besteducation2019.model.login_model
import com.example.besteducation2019.model.login_response
import com.example.besteducation2019.model.order_model
import com.example.besteducation2019.model.order_response
import com.example.besteducation2019.model.register_model
import com.example.besteducation2019.model.register_respons
import com.example.besteducation2019.model.request_end
import com.example.besteducation2019.model.subject_model
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("users/signup/")
    fun register(@Body dataModel: register_model?): Call<register_respons>

    @POST("users/login/")
    fun login(@Body dataModel: login_model?): Call<login_response>


    @POST("courses/order/")
    suspend fun order(@Body course: order_model): Response<order_response>

    @POST("courses/buy/")
    suspend fun buy(@Body model: buy_model) :Response<buy_response_model>
    @GET("courses/")
    suspend fun courses(): Response<course_model>


    @GET("courses/my/")
    suspend fun myCourses(): Response<Mycourse>

    @POST("courses/end/")
    suspend fun endLessons(@Body id: request_end): Response<end_model>

    @GET("courses/")
    suspend fun getSubjectCourse(@Query("subject") id: String): Response<course_model>

    @GET("courses/subjects/")
    suspend fun getSubjects(): Response<subject_model>

    @POST("users/logout/")
    suspend fun logout(): Response<end_model>

    //    http://147.45.158.162:9060/courses/subjects
    //    https://besteducation.pythonanywhere.com/api/courses/course/[id]/
    //  http://147.45.158.162:9060/courses/course/1/
    @GET("courses/course/{id}/")
    suspend fun courDetailes(@Path("id") id: String): Response<course_detailes_full>

    //    /courses/course/1/modules/module/1/lessons/lesson/1/
    @GET("courses/course/{id1}/modules/module/{id2}/lessons/lesson/{id3}/")
    suspend fun lessonDeatile(
        @Path("id1") id1: String,
        @Path("id2") id2: String,
        @Path("id3") id3: String
    ): Response<lesson_datailes>

//    @GET("courses/course/{id}/lesson/{id2}/")
//    fun lesson(@Path("id") id: String, @Path("id2") id2: String): Call<LessonX>


}
