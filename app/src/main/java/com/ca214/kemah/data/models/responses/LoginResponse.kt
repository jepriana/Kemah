package com.ca214.kemah.data.models.responses

import com.google.gson.annotations.SerializedName

class LoginResponse {
    @SerializedName("accessToken")
    var accessToken: String = ""
    @SerializedName("refreshToken")
    var refreshToken: String = ""
}