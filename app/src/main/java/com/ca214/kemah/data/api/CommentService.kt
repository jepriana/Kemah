package com.ca214.kemah.data.api

import com.ca214.kemah.data.models.requests.CommentRequest
import com.ca214.kemah.data.models.responses.CommentResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface CommentService {

    @GET("campgrounds/{campgroundId}/comments/all")
    fun getComments(
        @Path("campgroundId") campgroundId: UUID
    ) : Call<List<CommentResponse>>

    @POST("campgrounds/{campgroundId}/comments")
    fun addComment(
        @Path("campgroundId") campgroundId: UUID,
        @Body campgroundRequest: CommentRequest
    ) : Call<CommentResponse>

    @PUT("campgrounds/{campgroundId}/comments/{id}")
    fun updateComment(
        @Path("campgroundId") campgroundId: UUID,
        @Path("id") id: UUID,
        @Body campgroundRequest: CommentRequest
    ) : Call<String>

    @DELETE("campgrounds/{campgroundId}/comments/{id}")
    fun deleteComment(
        @Path("campgroundId") campgroundId: UUID,
        @Path("id") id: UUID
    ) : Call<Boolean>
}