package com.ca214.kemah.data.api

import com.ca214.kemah.data.models.requests.CommentRequest
import com.ca214.kemah.data.models.responses.CommentResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface CommentService {
    @GET("campgrounds/{campgroundId}/comments/all")
    fun getAllComments(
        @Header("Authorization") authHeader: String,
        @Path("campgroundId") campgroundId: UUID,
    ) : Call<List<CommentResponse>>

    @POST("campgrounds/{campgroundId}/comments")
    fun addComment(
        @Header("Authorization") authHeader: String,
        @Path("campgroundId") campgroundId: UUID,
        @Body newComment: CommentRequest
    ) : Call<CommentResponse>

    @PUT("campgrounds/{campgroundId}/comments/{id}")
    fun updateComment(
        @Header("Authorization") authHeader: String,
        @Path("campgroundId") campgroundId: UUID,
        @Path("id") id: UUID,
        @Body updatedComment: CommentRequest
    ) : Call<String>

    @DELETE("campgrounds/{campgroundId}/comments/{id}")
    fun deleteComment(
        @Header("Authorization") authHeader: String,
        @Path("campgroundId") campgroundId: UUID,
        @Path("id") id: UUID
    ) : Call<String>
}