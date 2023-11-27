package com.ca214.kemah.data.api

import com.ca214.kemah.data.models.LoginRequest
import com.ca214.kemah.data.models.ResponseLogin
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    fun login(
        @Body loginRequest: LoginRequest
    ) : Call<ResponseLogin>
}