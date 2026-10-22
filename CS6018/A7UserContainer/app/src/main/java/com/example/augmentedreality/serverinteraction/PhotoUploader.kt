package com.example.augmentedreality.serverinteraction

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

/** Reads a photo CameraX just saved (a MediaStore Uri) and sends it to the server. */
class PhotoUploader(context: Context, private val api: PhotoApi) {

    private val appContext = context.applicationContext

    suspend fun upload(uri: Uri): PhotoDto {
        val bytes = withContext(Dispatchers.IO) {
            appContext.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                ?: throw IOException("Couldn't read the saved photo")
        }
        return api.uploadPhoto(bytes)
    }
}