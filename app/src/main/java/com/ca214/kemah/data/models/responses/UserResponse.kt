package com.ca214.kemah.data.models.responses

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class UserResponse(
    @SerializedName("id")
    val id: UUID = UUID.randomUUID(),
    @SerializedName("username")
    val username: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("isAdmin")
    val isAdmin: Boolean = false,
)
