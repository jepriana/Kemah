package com.ca214.kemah.data.models

import java.util.UUID

data class Creator(
    val id: UUID,
    val username: String,
    val email: String,
    val isAdmin: Boolean = false
    )
