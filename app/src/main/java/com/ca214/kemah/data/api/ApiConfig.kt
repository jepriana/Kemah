package com.ca214.kemah.data.api

import com.ca214.kemah.utils.Constants.BASE_URL
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object ApiConfig {

    private val client: Retrofit get() {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
//            .addInterceptor { chain ->
//                val newRequest = chain.request().newBuilder()
//                    .addHeader("Authorization", "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJuYW1laWQiOiJjYmU0NWM4NS1kZDQ4LTQyOWItODVlNi0xYTc4MTBkNjYzZDEiLCJ1bmlxdWVfbmFtZSI6ImplcHJpIiwidG9rZW5fdHlwZSI6IkFjY2VzcyIsInJvbGUiOiJVc2VyIiwibmJmIjoxNzAyODk2NDYxLCJleHAiOjE3MDI5ODI4NjEsImlhdCI6MTcwMjg5NjQ2MX0.XH9ISQh6vk4n7-QvtVJ4LGx25iP4JM9ffshbugPDSKDj0JbQZryj3F0BTM7x22gXgqyeNAyT1GqCtJD4w9I6ig")
//                    .build()
//                chain.proceed(newRequest)
//            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    val instanceRetrofit: ApiService get() = client.create(ApiService::class.java)
    val instanceCampgroundRetrofit : CampgroundService get() = client.create(CampgroundService::class.java)
    val instanceCommentRetrofit: CommentService get() = client.create(CommentService::class.java)
}