package com.example.augmentedreality.serverinteraction

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json
import java.io.IOException

/**
 * Every call to the server goes through here. Failures always surface as [ApiError]
 * with a message that is safe to show to the user.
 */
class PhotoApi(private val store: SessionStore) {

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 10_000
            requestTimeoutMillis = 60_000
            socketTimeoutMillis = 60_000
        }
    }

    private val base: String get() = store.serverUrl

    /** Attaches the saved login token, if there is one. */
    private fun HttpRequestBuilder.authorize() {
        store.session.value?.let { header(HttpHeaders.Authorization, "Bearer ${it.token}") }
    }

    private suspend fun HttpResponse.ensureSuccess() {
        if (status.isSuccess()) return
        val message = try {
            body<ErrorResponse>().error
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            status.description
        }
        throw ApiError(status.value, message)
    }

    private suspend fun <T> call(block: suspend () -> T): T =
        try {
            block()
        } catch (e: ApiError) {
            // The server rejected our token (expired, or logged out elsewhere): drop the session
            // so the UI returns to the login screen. A wrong password at login has no session yet.
            if (e.status == 401 && store.session.value != null) store.clear()
            throw e
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            throw ApiError(0, "Can't reach the server at ${store.serverUrl}")
        } catch (e: Exception) {
            throw ApiError(0, e.message ?: "Unexpected error")
        }

    // --- accounts ---

    suspend fun register(username: String, password: String): AuthResponse =
        authCall("/auth/register", username, password)

    suspend fun login(username: String, password: String): AuthResponse =
        authCall("/auth/login", username, password)

    private suspend fun authCall(path: String, username: String, password: String): AuthResponse = call {
        val response = client.post("$base$path") {
            contentType(ContentType.Application.Json)
            setBody(Credentials(username, password))
        }
        response.ensureSuccess()
        response.body<AuthResponse>()
    }

    suspend fun me(): MeResponse = call {
        val response = client.get("$base/auth/me") { authorize() }
        response.ensureSuccess()
        response.body<MeResponse>()
    }

    suspend fun logout() = call {
        val response = client.post("$base/auth/logout") { authorize() }
        response.ensureSuccess()
    }

    // --- photos ---

    suspend fun listPhotos(): List<PhotoDto> = call {
        val response = client.get("$base/photos") { authorize() }
        response.ensureSuccess()
        response.body<List<PhotoDto>>()
    }

    suspend fun uploadPhoto(bytes: ByteArray): PhotoDto = call {
        val response = client.post("$base/photos") {
            authorize()
            contentType(ContentType.Image.JPEG)
            setBody(bytes)
        }
        response.ensureSuccess()
        response.body<PhotoDto>()
    }

    suspend fun downloadImage(photoId: String): ByteArray = call {
        val response = client.get("$base/photos/$photoId/image") { authorize() }
        response.ensureSuccess()
        response.body<ByteArray>()
    }

    suspend fun deletePhoto(photoId: String) = call {
        val response = client.delete("$base/photos/$photoId") { authorize() }
        response.ensureSuccess()
    }
}