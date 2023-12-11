package com.ca214.kemah.data.models.responses

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class RegisterResponse (
    @SerializedName("id")
    var id: UUID = UUID.randomUUID(),
    @SerializedName("username")
    var username: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("isAdmin")
    var isAdmin: Boolean = false
)