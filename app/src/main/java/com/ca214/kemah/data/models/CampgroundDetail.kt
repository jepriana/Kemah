package com.ca214.kemah.data.models
import java.util.UUID

data class CampgroundDetail(
    val id: UUID?,
    val creator: UserData?,
    val name: String,
    val location: String,
    val address: String,
    val price: Int,
    val description: String,
    val imageUrl: String?,
    val longitude: Double?,
    val latitude: Double?,
    val comments: List<Comment>
)
