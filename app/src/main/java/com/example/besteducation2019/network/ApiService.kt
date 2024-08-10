package com.example.besteducation2019.network

import com.example.besteducation2019.model.Mycourse
import com.example.besteducation2019.model.billing_model
import com.example.besteducation2019.model.buy_model
import com.example.besteducation2019.model.buy_response_model
import com.example.besteducation2019.model.change_password_model
import com.example.besteducation2019.model.change_password_response
import com.example.besteducation2019.model.course_detailes_full
import com.example.besteducation2019.model.course_model
import com.example.besteducation2019.model.courses_rating_model
import com.example.besteducation2019.model.end_model
import com.example.besteducation2019.model.get_rating_model
import com.example.besteducation2019.model.get_rating_request
import com.example.besteducation2019.model.lesson_datailes
import com.example.besteducation2019.model.login_model
import com.example.besteducation2019.model.login_response
import com.example.besteducation2019.model.order_model
import com.example.besteducation2019.model.order_response
import com.example.besteducation2019.model.profil_detailes
import com.example.besteducation2019.model.rate_request
import com.example.besteducation2019.model.rate_response
import com.example.besteducation2019.model.register_model
import com.example.besteducation2019.model.register_respons
import com.example.besteducation2019.model.request_end
import com.example.besteducation2019.model.subject_model
import com.example.besteducation2019.model.upload_image_data
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("users/signup/")
    fun register(@Body dataModel: register_model?): Call<register_respons>

    @POST("users/login/")
    fun login(@Body dataModel: login_model?): Call<login_response>

    @GET("courses/billing_reports/")
    suspend fun getBillingReport(): Response<billing_model>

    @POST("courses/order/")
    suspend fun order(@Body course: order_model): Response<order_response>

    @POST("courses/buy/")
    suspend fun buy(@Body model: buy_model): Response<buy_response_model>

    @POST("users/change_password/")
    suspend fun change_password(@Body model: change_password_model): Response<change_password_response>

    @GET("courses/")
    suspend fun courses(): Response<course_model>

    @POST("courses/rate/")
    suspend fun saveRating(@Body model: rate_request): Response<rate_response>

    @POST("courses/ratings/")
    suspend fun getRating(@Body model: get_rating_request): Response<get_rating_model>

    @GET("courses/my/")
    suspend fun myCourses(): Response<Mycourse>

    @POST("courses/end/")
    suspend fun endLessons(@Body id: request_end): Response<end_model>

    @GET("courses/")
    suspend fun getSubjectCourse(@Query("subject") id: String): Response<course_model>

    @GET("courses/subjects/")
    suspend fun getSubjects(): Response<subject_model>

//    https://bestedu.uz/users/user/1/

    @GET("users/user/{id}/")
    suspend fun profilInformation(@Path("id") id: String): Response<profil_detailes>

    @POST("users/logout/")
    suspend fun logout(): Response<end_model>

    //    http://147.45.158.162:9060/courses/subjects
    //    https://besteducation.pythonanywhere.com/api/courses/course/[id]/
    //  http://147.45.158.162:9060/courses/course/1/
    @GET("courses/course/{id}/")
    suspend fun courDetailes(@Path("id") id: String): Response<course_detailes_full>

    @Multipart
    @POST("users/user/{id}/edit/")
    suspend fun createUser(
        @Path("id") id: String,
        @Part("username") username: RequestBody,
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("middle_name") middle_name: RequestBody?,
        @Part("bio") bio: RequestBody,
        @Part("is_student") is_student: RequestBody
    ): Response<Any>

    @Multipart
    @POST("users/upload_image/")
    fun uploadImage(@Part image: MultipartBody.Part): Call<upload_image_data>

    //    /courses/course/1/modules/module/1/lessons/lesson/1/
    @GET("courses/course/{id1}/modules/module/{id2}/lessons/lesson/{id3}/")
    suspend fun lessonDeatile(
        @Path("id1") id1: String,
        @Path("id2") id2: String,
        @Path("id3") id3: String
    ): Response<lesson_datailes>

    @GET("courses/for_rating/")
    suspend fun getCoursesRating(): Response<courses_rating_model>
//    @GET("courses/course/{id}/lesson/{id2}/")
//    fun lesson(@Path("id") id: String, @Path("id2") id2: String): Call<LessonX>


}
