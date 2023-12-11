package com.ca214.kemah.data.models.responses

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class CampgroundResponse (
    @SerializedName("id")
    val id: UUID,
    @SerializedName("creatorId")
    val creatorID: UUID,
    @SerializedName("creatorUsername")
    val creatorUsername: String,
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
    val longitude: Double,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("imageUrl")
    val imageURL: String
)
