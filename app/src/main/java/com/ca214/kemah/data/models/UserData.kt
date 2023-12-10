package com.ca214.kemah.data.models

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class UserData(
    val id: UUID = UUID.randomUUID(),
    val username: String = "",
    val email: String = "",
    val isAdmin: Boolean = false,
)
