package com.ca214.kemah.data.models.requests

import com.google.gson.annotations.SerializedName

data class CommentRequest(
    @SerializedName("content")
    val content: String = ""
)
