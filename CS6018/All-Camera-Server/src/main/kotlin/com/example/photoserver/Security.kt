package com.example.photoserver

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.bearer
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.jdbc.selectAll
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

const val AUTH_BEARER = "auth-bearer"

/** What a route sees after the bearer token has been validated. */
data class UserPrincipal(val userId: Long, val tokenHash: String)

object Passwords {
    private const val COST = 12

    fun hash(password: String): String =
        at.favre.lib.crypto.bcrypt.BCrypt.withDefaults().hashToString(COST, password.toCharArray())

    fun verify(password: String, hash: String): Boolean =
        at.favre.lib.crypto.bcrypt.BCrypt.verifyer().verify(password.toCharArray(), hash).verified
}

object Tokens {
    private val random = SecureRandom()

    /** 256 random bits, URL-safe. The client keeps this; the server only keeps its hash. */
    fun generate(): String {
        val bytes = ByteArray(32)
        random.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }

    fun hash(token: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(token.toByteArray())
            .joinToString("") { "%02x".format(it) }
}

fun Application.configureSecurity() {
    install(Authentication) {
        bearer(AUTH_BEARER) {
            realm = "photo-server"
            authenticate { credential ->
                val hash = Tokens.hash(credential.token)
                val now = System.currentTimeMillis()
                val userId = dbQuery {
                    Sessions.selectAll()
                        .where { (Sessions.tokenHash eq hash) and (Sessions.expiresAt greater now) }
                        .singleOrNull()
                        ?.get(Sessions.userId)
                }
                userId?.let { UserPrincipal(it, hash) }
            }
        }
    }
}