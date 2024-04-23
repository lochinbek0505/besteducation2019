package com.example.besteducation2019.network// RetrofitBuilder.kt
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private val BASE_URL="https://besteducation.pythonanywhere.com/api/"

    fun create(token:String): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClientProvider.getClient(token))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
