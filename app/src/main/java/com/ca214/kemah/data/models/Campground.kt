package com.ca214.kemah.data.models

import java.util.UUID

data class Campground(
    val id: UUID,
    val name: String,
    val location: String,
    val address: String,
    val price: Int,
    val description: String,
    val imageUrl: String?,
    val longitude: Double?,
    val latitude: Double?,
    val creatorId: UUID,
    val creatorUsername: String
)
