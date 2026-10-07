package com.example.augmentedreality

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.SystemClock
import android.util.Log
import androidx.camera.core.ImageProxy
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.detector.ObjectDetector as TfObjectDetector

/**
 * EfficientDet-Lite4 through the TFLite Task Library, which reads the model's metadata
 * (input size, normalization, labels) so no manual preprocessing is needed.
 * Runs synchronously on the analysis thread.
 */
class EfficientDetDetector(private val context: Context) : FrameDetector {

    override val type = ModelType.EFFICIENTDET

    // Lazy so the model loads on the analysis thread at first use, not on the main thread.
    private val detector: TfObjectDetector by lazy {
        val options = TfObjectDetector.ObjectDetectorOptions.builder()
            .setMaxResults(MAX_RESULTS)
            .setScoreThreshold(SCORE_THRESHOLD)
            .build()
        TfObjectDetector.createFromFileAndOptions(context, MODEL_FILE, options)
    }

    override fun detect(image: ImageProxy, onFinished: (DetectionResult?) -> Unit) {
        val result = try {
            val start = SystemClock.elapsedRealtime()

            // Rotate to upright first so the boxes come back in upright coordinates.
            val bitmap = image.toBitmap().rotated(image.imageInfo.rotationDegrees)
            val w = bitmap.width
            val h = bitmap.height

            val detections = detector.detect(TensorImage.fromBitmap(bitmap)).mapNotNull { d ->
                val category = d.categories.firstOrNull() ?: return@mapNotNull null
                val box = d.boundingBox // pixels in the bitmap we passed in
                Detection(
                    label = category.label,
                    confidence = category.score,
                    left = (box.left / w).coerceIn(0f, 1f),
                    top = (box.top / h).coerceIn(0f, 1f),
                    right = (box.right / w).coerceIn(0f, 1f),
                    bottom = (box.bottom / h).coerceIn(0f, 1f)
                )
            }

            DetectionResult(
                model = type,
                detections = detections,
                frameWidth = w,
                frameHeight = h,
                inferenceMs = SystemClock.elapsedRealtime() - start
            )
        } catch (e: Exception) {
            Log.e(TAG, "EfficientDet detection failed", e)
            null
        }
        onFinished(result)
    }

    override fun close() {
        try { detector.close() } catch (e: Exception) { Log.w(TAG, "close failed", e) }
    }

    private fun Bitmap.rotated(degrees: Int): Bitmap {
        if (degrees == 0) return this
        val matrix = Matrix().apply { postRotate(degrees.toFloat()) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    private companion object {
        const val TAG = "EfficientDetDetector"
        const val MODEL_FILE = "model.tflite" // in app/src/main/assets/
        const val MAX_RESULTS = 8
        const val SCORE_THRESHOLD = 0.4f
    }
}