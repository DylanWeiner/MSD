package com.example.augmentedreality

import androidx.camera.core.CameraSelector
import androidx.camera.core.SurfaceRequest

enum class SaveStatus { Idle, Saving, Success, Error }

/**
 * Brightest point reported by the analyzer.
 * normalizedX/Y are 0..1 in the *unrotated* analysis buffer.
 * The UI is responsible for rotation, crop scaling, and mirroring.
 */
data class AnalysisResult(
    val normalizedX: Float,
    val normalizedY: Float,
    val imageWidth: Int,
    val imageHeight: Int,
    val rotationDegrees: Int
)

data class CameraUiState(
    val lensFacing: Int = CameraSelector.LENS_FACING_BACK,
    val permissionGranted: Boolean = false,
    val surfaceRequest: SurfaceRequest? = null,
    val highlightPoint: AnalysisResult? = null,
    val saveStatus: SaveStatus = SaveStatus.Idle
)
