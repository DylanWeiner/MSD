package com.example.photoserver

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import java.sql.SQLException

private val USERNAME_REGEX = Regex("^[a-z0-9_]{3,32}$")
private const val MIN_PASSWORD_LENGTH = 8
private const val MAX_PASSWORD_LENGTH = 72 // bcrypt only uses the first 72 bytes
private const val SESSION_LIFETIME_MS = 30L * 24 * 60 * 60 * 1000 // 30 days

// Verified against when the username doesn't exist, so "no such user" and
// "wrong password" take about the same time and can't be told apart by timing.
private val DUMMY_HASH: String by lazy { Passwords.hash("dummy-password-for-timing") }

suspend inline fun <reified T : Any> ApplicationCall.receiveOrBadRequest(): T =
    try {
        receive<T>()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        throw ApiException(HttpStatusCode.BadRequest, "Invalid request body")
    }

/** Must be called inside a dbQuery/transaction. */
private fun createSession(userId: Long, username: String): AuthResponse {
    val token = Tokens.generate()
    val now = System.currentTimeMillis()
    val expiresAt = now + SESSION_LIFETIME_MS
    Sessions.insert {
        it[tokenHash] = Tokens.hash(token)
        it[Sessions.userId] = userId
        it[createdAt] = now
        it[Sessions.expiresAt] = expiresAt
    }
    return AuthResponse(token = token, username = username, expiresAt = expiresAt)
}

fun Route.authRoutes() {
    route("/auth") {

        post("/register") {
            val creds = call.receiveOrBadRequest<Credentials>()
            val username = creds.username.trim().lowercase()

            if (!USERNAME_REGEX.matches(username)) {
                throw ApiException(
                    HttpStatusCode.BadRequest,
                    "Username must be 3-32 characters: letters, digits, or underscore"
                )
            }
            if (creds.password.length < MIN_PASSWORD_LENGTH || creds.password.length > MAX_PASSWORD_LENGTH) {
                throw ApiException(
                    HttpStatusCode.BadRequest,
                    "Password must be $MIN_PASSWORD_LENGTH-$MAX_PASSWORD_LENGTH characters"
                )
            }

            // bcrypt is deliberately slow (CPU-heavy), so keep it off the request thread.
            val hash = withContext(Dispatchers.Default) { Passwords.hash(creds.password) }

            val response = dbQuery {
                val taken = Users.selectAll().where { Users.username eq username }.limit(1).any()
                if (taken) throw ApiException(HttpStatusCode.Conflict, "Username already taken")

                val userId = try {
                    Users.insert {
                        it[Users.username] = username
                        it[passwordHash] = hash
                        it[createdAt] = System.currentTimeMillis()
                    }[Users.id]
                } catch (e: SQLException) {
                    // Two simultaneous registrations for the same name: the unique index catches the loser.
                    throw ApiException(HttpStatusCode.Conflict, "Username already taken")
                }
                createSession(userId, username)
            }
            call.respond(HttpStatusCode.Created, response)
        }

        post("/login") {
            val creds = call.receiveOrBadRequest<Credentials>()
            val username = creds.username.trim().lowercase()

            val user = dbQuery {
                Users.selectAll().where { Users.username eq username }.singleOrNull()
                    ?.let { it[Users.id] to it[Users.passwordHash] }
            }
            val passwordOk = withContext(Dispatchers.Default) {
                Passwords.verify(creds.password, user?.second ?: DUMMY_HASH)
            }
            if (user == null || !passwordOk) {
                throw ApiException(HttpStatusCode.Unauthorized, "Invalid username or password")
            }

            call.respond(dbQuery { createSession(user.first, username) })
        }

        authenticate(AUTH_BEARER) {

            // Lets the app check at startup whether its saved token is still valid.
            get("/me") {
                val principal = call.principal<UserPrincipal>()!!
                val username = dbQuery {
                    Users.selectAll().where { Users.id eq principal.userId }.singleOrNull()?.get(Users.username)
                } ?: throw ApiException(HttpStatusCode.Unauthorized, "Account no longer exists")
                call.respond(MeResponse(username))
            }

            post("/logout") {
                val principal = call.principal<UserPrincipal>()!!
                dbQuery { Sessions.deleteWhere { Sessions.tokenHash eq principal.tokenHash } }
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}