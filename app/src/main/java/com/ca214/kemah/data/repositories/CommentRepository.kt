package com.ca214.kemah.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ca214.kemah.data.api.ApiConfig
import com.ca214.kemah.data.models.Comment
import com.ca214.kemah.data.models.responses.CommentResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class CommentRepository (private val accessToken: String){
    private val apiService = ApiConfig.instanceCommentRetrofit
    private val bearerToken = "Bearer $accessToken"

    fun getComments(campgroundId: UUID) : LiveData<List<Comment>> {
        val data = MutableLiveData<List<Comment>>()

        apiService.getAllComments(bearerToken, campgroundId)
            .enqueue(object : Callback<List<CommentResponse>> {
                override fun onResponse(
                    call: Call<List<CommentResponse>>,
                    response: Response<List<CommentResponse>>
                ) {
                    if (response.isSuccessful) {
                        val responseData = response.body()
                        if (!responseData.isNullOrEmpty()) {
                            val result = responseData.map { comment ->
                                Comment(
                                    id = comment.id,
                                    creatorId = comment.creatorId,
                                    creatorUsername = comment.creatorUsername,
                                    content = comment.content,
                                )
                            }
                            data.value = result
                        }
                    }
                }

                override fun onFailure(call: Call<List<CommentResponse>>, t: Throwable) {
                    Log.d("Load Campground Comment", t.localizedMessage)
                }

            })

        return data
    }
}