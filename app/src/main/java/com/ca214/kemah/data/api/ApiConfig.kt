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
                    .addHeader("Authorization", "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJuYW1laWQiOiIyM2M2ZjM0MC1kYTkzLTQ5MWEtYmUzNS02NDZhNjllYThkMmIiLCJ1bmlxdWVfbmFtZSI6ImplcHJpIiwidG9rZW5fdHlwZSI6IkFjY2VzcyIsInJvbGUiOiJVc2VyIiwibmJmIjoxNzAxNjgzMjE1LCJleHAiOjE3MDE3Njk2MTUsImlhdCI6MTcwMTY4MzIxNX0.I50BrPatwqPL-jcdZYfv9uozzlsm_AsHEiXjQPjZvuvOD06GxQrJnFzt7t1ZW1b3BoPyw1XrmZ6czWNA95QC1g")
                    .build()
                chain.proceed(newRequest)
            }
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
    val instanceCampgroundService: CampgroundService get() = client.create(CampgroundService::class.java)

}