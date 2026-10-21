package com.example.photoserver

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.io.File

object Users : Table("users") {
    val id = long("id").autoIncrement()
    val username = varchar("username", 32).uniqueIndex()
    val passwordHash = varchar("password_hash", 100)
    val createdAt = long("created_at")

    override val primaryKey = PrimaryKey(id)
}

/** One row per logged-in device. Deleting the row IS logging out. Only a hash of the token is stored. */
object Sessions : Table("sessions") {
    val tokenHash = varchar("token_hash", 64)
    val userId = long("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE).index()
    val createdAt = long("created_at")
    val expiresAt = long("expires_at")

    override val primaryKey = PrimaryKey(tokenHash)
}

/** Photo metadata. The image bytes live on disk (see PhotoStorage). */
object Photos : Table("photos") {
    val id = varchar("id", 36)
    val ownerId = long("owner_id").references(Users.id, onDelete = ReferenceOption.CASCADE).index()
    val createdAt = long("created_at")
    val sizeBytes = long("size_bytes")

    override val primaryKey = PrimaryKey(id)
}

fun initDatabase(dataDir: File) {
    dataDir.mkdirs()
    // Forward slashes keep the JDBC URL valid on Windows as well.
    val dbFile = File(dataDir, "photoserver").absolutePath.replace('\\', '/')
    Database.connect(
        url = "jdbc:h2:file:$dbFile;DB_CLOSE_DELAY=-1",
        driver = "org.h2.Driver",
        user = "sa",
        password = ""
    )
    transaction {
        SchemaUtils.create(Users, Sessions, Photos)
    }
}

/** Exposed's JDBC transactions block, so run them off the request coroutine's thread. */
suspend fun <T> dbQuery(block: () -> T): T =
    withContext(Dispatchers.IO) { transaction { block() } }