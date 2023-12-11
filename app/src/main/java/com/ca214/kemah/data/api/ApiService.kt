package com.ca214.kemah.data.api

import com.ca214.kemah.data.models.requests.LoginRequest
import com.ca214.kemah.data.models.requests.RegisterRequest
import com.ca214.kemah.data.models.responses.LoginResponse
import com.ca214.kemah.data.models.responses.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    fun login(
        @Body loginRequest: LoginRequest
    ) : Call<LoginResponse>

    @POST("auth/register")
    fun register(
        @Body registerRequest: RegisterRequest
    ) : Call<RegisterResponse>
}