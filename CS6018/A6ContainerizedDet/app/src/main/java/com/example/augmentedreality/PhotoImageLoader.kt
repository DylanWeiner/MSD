package com.example.augmentedreality

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.LruCache
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import kotlin.math.max

/**
 * Downloads a photo from the (authenticated) server and decodes it at a size that suits the
 * screen, with a small in-memory cache. Cleared on logout so one user's photos never linger
 * for the next user.
 *
 * Note: the server sends the full-size JPEG even for thumbnails (no server-side thumbnails
 * yet), so large galleries cost bandwidth on first load.
 */
class PhotoImageLoader(private val api: PhotoApi) {

    private val cache = object : LruCache<String, ImageBitmap>(
        (Runtime.getRuntime().maxMemory() / 1024 / 8).toInt() // an eighth of the heap, in KB
    ) {
        override fun sizeOf(key: String, value: ImageBitmap): Int = value.width * value.height * 4 / 1024
    }

    suspend fun load(photoId: String, maxDimension: Int): ImageBitmap? {
        val key = "$photoId@$maxDimension"
        cache.get(key)?.let { return it }

        val bytes = api.downloadImage(photoId)
        val image = withContext(Dispatchers.Default) { decode(bytes, maxDimension) } ?: return null
        cache.put(key, image)
        return image
    }

    fun clear() = cache.evictAll()

    private fun decode(bytes: ByteArray, maxDimension: Int): ImageBitmap? {
        // First pass: read only the size, so we can decode a smaller copy and save memory.
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, bounds)
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null

        var sample = 1
        while (max(bounds.outWidth, bounds.outHeight) / (sample * 2) >= maxDimension) sample *= 2

        val options = BitmapFactory.Options().apply { inSampleSize = sample }
        var bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options) ?: return null

        // CameraX records the phone's rotation in EXIF instead of rotating the pixels.
        // BitmapFactory ignores EXIF, so apply it here or portrait photos show up sideways.
        val rotation = exifRotation(bytes)
        if (rotation != 0) {
            val matrix = Matrix().apply { postRotate(rotation.toFloat()) }
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
        return bitmap.asImageBitmap()
    }

    private fun exifRotation(bytes: ByteArray): Int =
        try {
            val exif = ExifInterface(ByteArrayInputStream(bytes))
            when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        } catch (e: Exception) {
            0
        }
}
