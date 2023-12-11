package com.ca214.kemah.data.models.requests

import com.google.gson.annotations.SerializedName

data class RegisterRequest (
    @SerializedName("email")
    var email: String = "",
    @SerializedName("username")
    var username: String = "",
    @SerializedName("password")
    var password: String = ""
)