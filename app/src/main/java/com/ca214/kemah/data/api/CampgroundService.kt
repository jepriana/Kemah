package com.ca214.kemah.data.api

import com.ca214.kemah.data.models.requests.CampgroundRequest
import com.ca214.kemah.data.models.responses.CampgroundDetailResponse
import com.ca214.kemah.data.models.responses.CampgroundResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface CampgroundService {
    @GET("campgrounds/all")
    fun getAllCampgrounds(
        @Header("Authorization") authHeader: String,
    ) : Call<List<CampgroundResponse>>

    @GET("campgrounds/{id}")
    fun getCampgroundById(
        @Header("Authorization") authHeader: String,
        @Path("id") id: UUID
    ) : Call<CampgroundDetailResponse>

    @POST("campgrounds")
    fun addCampground(
        @Header("Authorization") authHeader: String,
        @Body newCampground: CampgroundRequest
    ) : Call<CampgroundResponse>

    @PUT("campgrounds/{id}")
    fun updateCampground(
        @Header("Authorization") authHeader: String,
        @Path("id") id: UUID,
        @Body updatedCampground: CampgroundRequest
    ) : Call<String>

    @DELETE("campgrounds/{id}")
    fun deleteCampground(
        @Header("Authorization") authHeader: String,
        @Path("id") id: UUID
    ) : Call<String>
}