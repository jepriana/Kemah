package com.ca214.kemah.data.api

import com.ca214.kemah.data.models.requests.CampgroundRequest
import com.ca214.kemah.data.models.responses.CampgroundDetailResponse
import com.ca214.kemah.data.models.responses.CampgroundResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface CampgroundService {

    @GET("campgrounds/all")
    fun getCampgrounds(
    ) : Call<List<CampgroundResponse>>

    @POST("campgrounds")
    fun addCampground(
        @Body campgroundRequest: CampgroundRequest
    ) : Call<CampgroundResponse>

    @GET("campgrounds/{id}")
    fun getCampgroundById(
        @Path("id") id: UUID
    ) : Call<CampgroundDetailResponse?>

    @PUT("campgrounds/{id}")
    fun updateCampground(
        @Path("id") id: UUID,
        @Body campgroundRequest: CampgroundRequest
    ) : Call<String>

    @DELETE("campgrounds/{id}")
    fun deleteCampground(@Path("id") id: UUID) : Call<Boolean>
}