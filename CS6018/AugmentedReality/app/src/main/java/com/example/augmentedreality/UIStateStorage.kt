package com.example.augmentedreality


data class UIStateStorage(
    // lensFacing: front or back
    // permissionGranted: Boolean
    // surfaceRequest: SurfaceRequest? (from Preview, consumed by the viewfinder)
    // highlightPoint: AnalysisResult? (latest brightest point)
    // saveStatus: idle / saving / success / error
)

data class AnalysisResult(
    // normalizedX, normalizedY (0 to 1)
    // imageWidth, imageHeight
    // rotationDegrees
)