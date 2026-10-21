package com.example.augmentedreality

import kotlinx.serialization.Serializable

// These mirror the server's JSON exactly.

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

/**
 * Anything that goes wrong talking to the server. status is the HTTP status code,
 * or 0 when the request never got an answer (server unreachable, timeout, etc.).
 */
class ApiError(val status: Int, message: String) : Exception(message)
