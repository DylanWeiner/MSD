package com.example.photoserver

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Image bytes on disk: <root>/<userId>/<photoId>.jpg
 * File names are built only from a numeric user id and a server-generated UUID,
 * never from anything the client sent, so path traversal isn't possible.
 */
class PhotoStorage(root: File) {

    private val root: File = root.canonicalFile.also { it.mkdirs() }

    private fun fileFor(userId: Long, photoId: String): File {
        val file = File(File(root, userId.toString()), "$photoId.jpg").canonicalFile
        check(file.path.startsWith(root.path + File.separator)) { "Path escaped storage root" }
        return file
    }

    suspend fun save(userId: Long, photoId: String, bytes: ByteArray) = withContext(Dispatchers.IO) {
        val file = fileFor(userId, photoId)
        file.parentFile.mkdirs()
        file.writeBytes(bytes)
    }

    suspend fun read(userId: Long, photoId: String): ByteArray? = withContext(Dispatchers.IO) {
        val file = fileFor(userId, photoId)
        if (file.isFile) file.readBytes() else null
    }

    suspend fun delete(userId: Long, photoId: String) {
        withContext(Dispatchers.IO) { fileFor(userId, photoId).delete() }
    }
}