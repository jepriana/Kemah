package com.ca214.kemah.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ca214.kemah.data.api.ApiConfig
import com.ca214.kemah.data.models.Comment
import com.ca214.kemah.data.models.requests.CommentRequest
import com.ca214.kemah.data.models.responses.CommentResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class CommentRepository {
    private val apiService = ApiConfig.instanceCommentService

    fun getComments(campgroundId: UUID): LiveData<List<Comment>> {
        val data = MutableLiveData<List<Comment>>()

        apiService.getComments(campgroundId = campgroundId) .enqueue(object : Callback<List<CommentResponse>> {
            override fun onResponse(call: Call<List<CommentResponse>>, response: Response<List<CommentResponse>>) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (!responseData.isNullOrEmpty()) {
                        val result = responseData.map { comment ->
                            Comment(
                                id = comment.id,
                                creatorId = comment.creatorId,
                                creatorUsername = comment.creatorUsername,
                                content = comment.content
                            )
                        }
                        data.value = result
                    }
                }
            }

            override fun onFailure(call: Call<List<CommentResponse>>, t: Throwable) {
                Log.e("Load data comment failed", t.localizedMessage)
            }
        })

        return data
    }

    fun addComment(campgroundId: UUID, comment: Comment): LiveData<Comment> {
        val data = MutableLiveData<Comment>()
        val commentRequest = CommentRequest (
            content = comment.content
        )

        apiService.addComment(campgroundId = campgroundId, commentRequest).enqueue(object : Callback<CommentResponse> {
            override fun onResponse(call: Call<CommentResponse>, response: Response<CommentResponse>) {
                if (response.isSuccessful) {
                    val commentResponse = response.body()
                    if (commentResponse != null) {
                        data.value = Comment(
                            id = comment.id,
                            creatorId = comment.creatorId,
                            creatorUsername = comment.creatorUsername,
                            content = comment.content
                        );
                    }
                }
            }

            override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                data.value = null
            }
        })

        return data
    }

    fun updateComment(campgroundId: UUID, id: UUID, comment: Comment): LiveData<Comment> {
        val data = MutableLiveData<Comment>()

        val commentRequest = CommentRequest (
            content = comment.content
        )

        apiService.updateComment(campgroundId, id, commentRequest).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    data.value = comment
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                data.value = null
            }
        })

        return data
    }

    fun deleteComment(campgroundId: UUID, id: UUID): LiveData<Boolean> {
        val data = MutableLiveData<Boolean>()

        apiService.deleteComment(campgroundId, id).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                data.value = response.isSuccessful
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                data.value = false
            }
        })

        return data
    }
}