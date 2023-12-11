package com.ca214.kemah.data.models.responses

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class CommentResponse(
    @SerializedName("id")
    val id: UUID,
    @SerializedName("creatorId")
    val creatorId: UUID,
    @SerializedName("creatorUsername")
    val creatorUsername: String,
    @SerializedName("content")
    val content: String,
)
