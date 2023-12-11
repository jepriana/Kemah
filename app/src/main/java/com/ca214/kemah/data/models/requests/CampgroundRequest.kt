package com.ca214.kemah.data.models.requests

import com.google.gson.annotations.SerializedName

data class CampgroundRequest (
    @SerializedName("name")
    val name: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("price")
    val price: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("imageUrl")
    val imageURL: String?
)