package com.example.photoserver

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.contentLength
import io.ktor.server.request.receive
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import java.util.UUID

private const val MAX_PHOTO_BYTES = 15L * 1024 * 1024 // 15 MB

private fun looksLikeJpeg(b: ByteArray) =
    b.size > 3 && b[0] == 0xFF.toByte() && b[1] == 0xD8.toByte() && b[2] == 0xFF.toByte()

private fun parsePhotoId(raw: String?): String =
    try {
        UUID.fromString(raw.orEmpty()).toString()
    } catch (e: IllegalArgumentException) {
        // Malformed id: same answer as "not yours", so nothing is revealed.
        throw ApiException(HttpStatusCode.NotFound, "Photo not found")
    }

/**
 * Every query below filters on the authenticated user's id (taken from the validated token,
 * never from the request). Someone else's photo behaves exactly like one that doesn't exist: 404.
 */
fun Route.photoRoutes(storage: PhotoStorage) {
    authenticate(AUTH_BEARER) {
        route("/photos") {

            // List the caller's own photos, newest first.
            get {
                val user = call.principal<UserPrincipal>()!!
                val photos = dbQuery {
                    Photos.selectAll()
                        .where { Photos.ownerId eq user.userId }
                        .orderBy(Photos.createdAt, SortOrder.DESC)
                        .map { PhotoDto(it[Photos.id], it[Photos.createdAt], it[Photos.sizeBytes]) }
                }
                call.respond(photos)
            }

            // Upload: the request body IS the JPEG (Content-Type: image/jpeg).
            post {
                val user = call.principal<UserPrincipal>()!!

                val declared = call.request.contentLength()
                    ?: throw ApiException(HttpStatusCode.LengthRequired, "Content-Length header required")
                if (declared > MAX_PHOTO_BYTES) {
                    throw ApiException(HttpStatusCode.PayloadTooLarge, "Photo exceeds ${MAX_PHOTO_BYTES / 1024 / 1024} MB")
                }

                val bytes = call.receive<ByteArray>()
                if (bytes.size > MAX_PHOTO_BYTES) {
                    throw ApiException(HttpStatusCode.PayloadTooLarge, "Photo too large")
                }
                if (!looksLikeJpeg(bytes)) {
                    throw ApiException(HttpStatusCode.UnsupportedMediaType, "Only JPEG images are accepted")
                }

                val photoId = UUID.randomUUID().toString()
                val createdAt = System.currentTimeMillis()

                storage.save(user.userId, photoId, bytes) // file first, then the row that points to it
                try {
                    dbQuery {
                        Photos.insert {
                            it[id] = photoId
                            it[ownerId] = user.userId
                            it[Photos.createdAt] = createdAt
                            it[sizeBytes] = bytes.size.toLong()
                        }
                    }
                } catch (e: Exception) {
                    storage.delete(user.userId, photoId) // don't leave an orphan file behind
                    throw e
                }

                call.respond(HttpStatusCode.Created, PhotoDto(photoId, createdAt, bytes.size.toLong()))
            }

            // Download one of the caller's photos.
            get("/{id}/image") {
                val user = call.principal<UserPrincipal>()!!
                val photoId = parsePhotoId(call.parameters["id"])

                val owned = dbQuery {
                    Photos.selectAll()
                        .where { (Photos.id eq photoId) and (Photos.ownerId eq user.userId) }
                        .limit(1)
                        .any()
                }
                val bytes = (if (owned) storage.read(user.userId, photoId) else null)
                    ?: throw ApiException(HttpStatusCode.NotFound, "Photo not found")

                // A photo never changes once uploaded, so clients may cache it.
                // "private" keeps shared proxies from storing someone's photo.
                call.response.header(HttpHeaders.CacheControl, "private, max-age=86400")
                call.respondBytes(bytes, ContentType.Image.JPEG)
            }

            delete("/{id}") {
                val user = call.principal<UserPrincipal>()!!
                val photoId = parsePhotoId(call.parameters["id"])

                val deleted = dbQuery {
                    Photos.deleteWhere { (Photos.id eq photoId) and (Photos.ownerId eq user.userId) }
                }
                if (deleted == 0) throw ApiException(HttpStatusCode.NotFound, "Photo not found")

                storage.delete(user.userId, photoId)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}