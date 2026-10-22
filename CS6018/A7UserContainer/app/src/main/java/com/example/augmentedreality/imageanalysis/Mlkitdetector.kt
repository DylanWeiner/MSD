package com.example.augmentedreality.imageanalysis

import android.os.SystemClock
import android.util.Log
import androidx.camera.core.ImageProxy
import com.example.augmentedreality.screenstate.Detection
import com.example.augmentedreality.screenstate.DetectionResult
import com.example.augmentedreality.screenstate.ModelType
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

class MlKitDetector : FrameDetector {

    override val type = ModelType.MLKIT

    private val detector = ObjectDetection.getClient(
        ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.STREAM_MODE) // live video
            .enableMultipleObjects()
            .enableClassification()                              // needed to get labels at all
            .build()
    )

    override fun detect(image: ImageProxy, onFinished: (DetectionResult?) -> Unit) {
        val rotation = image.imageInfo.rotationDegrees

        // Frames are RGBA now (shared with EfficientDet), so ML Kit gets a bitmap plus rotation.
        val bitmap = try {
            image.toBitmap()
        } catch (e: Exception) {
            Log.e(TAG, "Bitmap conversion failed", e)
            onFinished(null)
            return
        }

        val uprightW = if (rotation % 180 == 0) bitmap.width else bitmap.height
        val uprightH = if (rotation % 180 == 0) bitmap.height else bitmap.width

        val input = InputImage.fromBitmap(bitmap, rotation)
        val start = SystemClock.elapsedRealtime()

        // Async: the frame stays open until one of these listeners runs.
        detector.process(input)
            .addOnSuccessListener { objects ->
                val result = try {
                    val detections = objects.map { obj ->
                        val box = obj.boundingBox
                        val best = obj.labels.maxByOrNull { it.confidence }
                        Detection(
                            label = best?.text ?: "Object",
                            confidence = best?.confidence,
                            left = (box.left.toFloat() / uprightW).coerceIn(0f, 1f),
                            top = (box.top.toFloat() / uprightH).coerceIn(0f, 1f),
                            right = (box.right.toFloat() / uprightW).coerceIn(0f, 1f),
                            bottom = (box.bottom.toFloat() / uprightH).coerceIn(0f, 1f)
                        )
                    }
                    DetectionResult(
                        model = type,
                        detections = detections,
                        frameWidth = uprightW,
                        frameHeight = uprightH,
                        inferenceMs = SystemClock.elapsedRealtime() - start
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to build ML Kit result", e)
                    null
                }
                onFinished(result)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "ML Kit detection failed", e)
                onFinished(null)
            }
    }

    override fun close() {
        try { detector.close() } catch (e: Exception) { Log.w(TAG, "close failed", e) }
    }

    private companion object {
        const val TAG = "MlKitDetector"
    }
}