package com.ca214.kemah.data.models.responses

import com.google.gson.annotations.SerializedName
import java.util.UUID

 class CampgroundResponse {
    @SerializedName("id")
    val id: UUID = UUID.randomUUID()
    @SerializedName("creatorId")
    val creatorId: UUID = UUID.randomUUID()
    @SerializedName("creatorUsername")
    val creatorUsername: String = ""
    @SerializedName("name")
    val name: String = ""
    @SerializedName("location")
    val location: String = ""
    @SerializedName("address")
    val address: String = ""
    @SerializedName("price")
    val price: Int = 0
    @SerializedName("description")
    val description: String = ""
    @SerializedName("imageUrl")
    val imageUrl: String = ""
    @SerializedName("longitude")
    val longitude: Double = 0.0
    @SerializedName("latitude")
    val latitude: Double = 0.0
}