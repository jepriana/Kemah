package com.ca214.kemah.data.models

import java.util.UUID

data class Comment(
    val id: UUID,
    val creatorId: UUID,
    val creatorUsername: String,
    val content: String,
)
