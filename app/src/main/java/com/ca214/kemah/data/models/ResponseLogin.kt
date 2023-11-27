package com.ca214.kemah.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseLogin (
    @SerializedName("accessToken")
    @Expose
    val accessToken: String = "",
    @SerializedName("refreshToken")
    @Expose
    val refreshToken: String = ""
)