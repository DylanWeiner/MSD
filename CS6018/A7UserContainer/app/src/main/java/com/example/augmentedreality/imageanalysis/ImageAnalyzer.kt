package com.example.augmentedreality.imageanalysis

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.augmentedreality.screenstate.DetectionResult

/**
 * Thin dispatcher: hands each frame to whichever detector is active, and closes the
 * frame only when that detector says it's finished (ML Kit is asynchronous).
 */
class ImageAnalyzer(
    private val detectorProvider: () -> FrameDetector,
    private val onResult: (DetectionResult) -> Unit
) : ImageAnalysis.Analyzer {

    override fun analyze(image: ImageProxy) {
        try {
            detectorProvider().detect(image) { result ->
                image.close()
                result?.let(onResult)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Detector threw", e)
            image.close() // never let a failure stall the pipeline
        }
    }

    private companion object {
        const val TAG = "ImageAnalyzer"
    }
}