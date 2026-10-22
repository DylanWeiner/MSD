package com.example.photoserver

import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable

@Serializable
data class Credentials(val username: String, val password: String)

@Serializable
data class AuthResponse(val token: String, val username: String, val expiresAt: Long)

@Serializable
data class MeResponse(val username: String)

@Serializable
data class PhotoDto(val id: String, val createdAt: Long, val sizeBytes: Long)

@Serializable
data class ErrorResponse(val error: String)

/** Thrown anywhere in a route; StatusPages turns it into a JSON error with this status. */
class ApiException(val status: HttpStatusCode, message: String) : RuntimeException(message)