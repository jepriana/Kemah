package com.ca214.kemah.data.models.requests

import com.google.gson.annotations.SerializedName

class LoginRequest {
    @SerializedName("username")
    var username: String = ""
    @SerializedName("password")
    var password: String = ""
}