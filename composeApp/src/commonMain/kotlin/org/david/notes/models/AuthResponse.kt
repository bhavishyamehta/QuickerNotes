package org.david.notes.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val accessToken: String,
    val email: String,
    val refreshToken: String,
    val userId: String
)