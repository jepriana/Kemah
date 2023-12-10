package com.ca214.kemah.data.api

import android.content.Context
import com.ca214.kemah.MainActivity
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {
    private const val BASE_URL = "https://kemah-api-ezyppnm64a-as.a.run.app/"

    private val client: Retrofit get() {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJuYW1laWQiOiJjYmU0NWM4NS1kZDQ4LTQyOWItODVlNi0xYTc4MTBkNjYzZDEiLCJ1bmlxdWVfbmFtZSI6ImplcHJpIiwidG9rZW5fdHlwZSI6IkFjY2VzcyIsInJvbGUiOiJVc2VyIiwibmJmIjoxNzAyMjA4Nzk0LCJleHAiOjE3MDIyOTUxOTQsImlhdCI6MTcwMjIwODc5NH0.i2kOgq-SQKwdu3c5pXeXQdOFwY1FVyS3Lk9clUiBTvWd_HMJOAQUgvhiZLl4L_e3T7WrIQk_Nh6mwLcvj2gRZw")
                    .build()
                chain.proceed(newRequest)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

//        fun<T> buildService(service: Class<T>): T {
//            return retrofit.create(service)
//        }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()


    }

    val instanceRetrofit: ApiService get() = client.create(ApiService::class.java)
    val instanceCampgroundService: CampgroundService get() = client.create(CampgroundService::class.java)

}