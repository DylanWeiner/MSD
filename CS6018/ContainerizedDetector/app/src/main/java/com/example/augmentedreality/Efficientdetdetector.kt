package com.example.augmentedreality

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.SystemClock
import android.util.Log
import androidx.camera.core.ImageProxy
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.objectdetector.ObjectDetector
import java.io.FileNotFoundException
import java.nio.ByteBuffer

/**
 * EfficientDet through MediaPipe Tasks (16 KB page-size compatible, unlike the TFLite Task Library).
 * Uses the same metadata-bearing .tflite model. Runs synchronously on the analysis thread.
 *
 * The GPU delegate must be created and used on the same thread. That holds here because
 * ImageAnalysis runs on a single-thread executor and the detector is built lazily on it.
 */
class EfficientDetDetector(private val context: Context) : FrameDetector {

    override val type = ModelType.EFFICIENTDET

    private var detector: ObjectDetector? = null
    private var useGpu = true

    private fun getDetector(): ObjectDetector =
        detector ?: createDetector().also { detector = it }

    /**
     * Reads the model through Android's AssetManager ourselves and hands MediaPipe the bytes,
     * instead of a path. That skips MediaPipe's native asset lookup (the source of the
     * "doesn't have a slash in it" error), and a genuinely missing file now fails here
     * with a clear message.
     */
    private fun loadModel(): ByteBuffer {
        try {
            val bytes = context.assets.open(MODEL_FILE).use { it.readBytes() }
            // setModelAssetBuffer requires a DIRECT buffer.
            return ByteBuffer.allocateDirect(bytes.size).apply {
                put(bytes)
                rewind()
            }
        } catch (e: FileNotFoundException) {
            Log.e(
                TAG,
                "Model '$MODEL_FILE' is not in assets. Assets root contains: " +
                        "${context.assets.list("")?.joinToString()}"
            )
            throw e
        }
    }

    private fun createDetector(): ObjectDetector {
        val baseOptions = BaseOptions.builder()
            .setModelAssetBuffer(loadModel())
            .setDelegate(if (useGpu) Delegate.GPU else Delegate.CPU)
            .build()
        val options = ObjectDetector.ObjectDetectorOptions.builder()
            .setBaseOptions(baseOptions)
            .setRunningMode(RunningMode.IMAGE)
            .setMaxResults(MAX_RESULTS)
            .setScoreThreshold(SCORE_THRESHOLD)
            .build()
        return ObjectDetector.createFromOptions(context, options)
    }

    override fun detect(image: ImageProxy, onFinished: (DetectionResult?) -> Unit) {
        val result = try {
            val activeDetector = getDetector() // load outside the timed section
            val start = SystemClock.elapsedRealtime()

            // Frames arrive as RGBA (see CameraManager), so toBitmap() is a cheap copy.
            // Rotate to upright so boxes come back in upright coordinates.
            val bitmap = image.toBitmap().rotated(image.imageInfo.rotationDegrees)
            val w = bitmap.width
            val h = bitmap.height

            val mpResult = activeDetector.detect(BitmapImageBuilder(bitmap).build())

            val detections = mpResult.detections().mapNotNull { d ->
                val category = d.categories().firstOrNull() ?: return@mapNotNull null
                val box = d.boundingBox() // pixels in the bitmap we passed in
                Detection(
                    label = category.categoryName(),
                    confidence = category.score(),
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
            Log.e(TAG, "EfficientDet detection failed (useGpu=$useGpu)", e)
            // A missing model file isn't a GPU problem, so don't switch delegates for it.
            if (useGpu && e !is FileNotFoundException) {
                // GPU unsupported or failed: drop this frame, rebuild on CPU for the next one.
                useGpu = false
                closeDetector()
            }
            null
        }
        onFinished(result)
    }

    override fun close() = closeDetector()

    private fun closeDetector() {
        try { detector?.close() } catch (e: Exception) { Log.w(TAG, "close failed", e) }
        detector = null
    }

    private fun Bitmap.rotated(degrees: Int): Bitmap {
        if (degrees == 0) return this
        val matrix = Matrix().apply { postRotate(degrees.toFloat()) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    private companion object {
        const val TAG = "EfficientDetDetector"

        // File in app/src/main/assets/. Lite0 is far faster than Lite4.
        // Must match the real filename exactly (case-sensitive).
        const val MODEL_FILE = "model.tflite"

        const val MAX_RESULTS = 8
        const val SCORE_THRESHOLD = 0.4f
    }
}