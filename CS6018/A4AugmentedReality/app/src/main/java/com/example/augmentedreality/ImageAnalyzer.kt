package com.example.augmentedreality

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy

class ImageAnalyzer(
    private val onResult: (AnalysisResult) -> Unit
) : ImageAnalysis.Analyzer {

    override fun analyze(image: ImageProxy) {
        try {
            onResult(findBrightestPoint(image))
        } finally {
            image.close() // always, and only after the pixels have been read
        }
    }

    /** Scans the Y (luminance) plane. Finds only; drawing is the UI's job. */
    fun findBrightestPoint(image: ImageProxy): AnalysisResult {
        val plane = image.planes[0]
        val buffer = plane.buffer
        val rowStride = plane.rowStride
        val pixelStride = plane.pixelStride
        val width = image.width
        val height = image.height

        var maxValue = -1
        var maxX = 0
        var maxY = 0

        for (y in 0 until height step STEP) {
            val rowStart = y * rowStride
            for (x in 0 until width step STEP) {
                val v = buffer.get(rowStart + x * pixelStride).toInt() and 0xFF
                if (v > maxValue) {
                    maxValue = v
                    maxX = x
                    maxY = y
                }
            }
        }

        return AnalysisResult(
            normalizedX = (maxX + 0.5f) / width,
            normalizedY = (maxY + 0.5f) / height,
            imageWidth = width,
            imageHeight = height,
            rotationDegrees = image.imageInfo.rotationDegrees
        )
    }

    private companion object {
        const val STEP = 4 // sample every 4th pixel; set to 1 for an exact full-frame scan
    }
}
