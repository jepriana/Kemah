package com.ca214.kemah.data.api

import com.ca214.kemah.data.models.requests.LoginRequest
import com.ca214.kemah.data.models.responses.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    fun login(
        @Body loginRequest: LoginRequest
    ) : Call<LoginResponse>
}