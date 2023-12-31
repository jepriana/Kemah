package com.ca214.kemah.data.models

import java.util.UUID

data class CampgroundDetail(
    val id: UUID,
    val name: String,
    val location: String,
    val address: String,
    val price: Int,
    val description: String,
    val imageUrl: String?,
    val longitude: Double?,
    val latitude: Double?,
    val creator: Creator?,
    val comments: List<Comment>
)
