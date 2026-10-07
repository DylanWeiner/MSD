package com.example.augmentedreality

import androidx.camera.core.CameraSelector
import androidx.camera.core.SurfaceRequest

enum class SaveStatus { Idle, Saving, Success, Error }

enum class ModelType(val displayName: String) {
    MLKIT("ML Kit"),
    EFFICIENTDET("EfficientDet")
}

/**
 * One detected object. Box coordinates are normalized (0..1) in UPRIGHT space,
 * meaning rotation has already been applied. The UI only handles crop-scaling and mirroring.
 */
data class Detection(
    val label: String,
    val confidence: Float?,   // null when the model gave no label score
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
)

/**
 * Everything one frame produced. frameWidth/Height are the upright frame size in pixels,
 * needed so the overlay can reproduce the viewfinder's crop scaling.
 */
data class DetectionResult(
    val model: ModelType,
    val detections: List<Detection>,
    val frameWidth: Int,
    val frameHeight: Int,
    val inferenceMs: Long
)

data class CameraUiState(
    val lensFacing: Int = CameraSelector.LENS_FACING_BACK,
    val permissionGranted: Boolean = false,
    val surfaceRequest: SurfaceRequest? = null,
    val selectedModel: ModelType = ModelType.MLKIT,
    val detectionResult: DetectionResult? = null,
    val saveStatus: SaveStatus = SaveStatus.Idle
)