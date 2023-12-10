package com.ca214.kemah.data.models.responses

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class CommentResponse(
    @SerializedName("id")
    val id: UUID = UUID.randomUUID(),
    @SerializedName("creatorId")
    val creatorId: UUID = UUID.randomUUID(),
    @SerializedName("creatorUsername")
    val creatorUsername: String = "",
    @SerializedName("content")
    val content: String = "",
)
