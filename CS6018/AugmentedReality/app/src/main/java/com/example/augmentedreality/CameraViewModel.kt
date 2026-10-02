package com.example.augmentedreality

class CameraViewModel {
    // Holds CameraUiState as observable state

    fun onPermissionResult(granted: Boolean) {
        // Updates permissionGranted in state
    }

    fun flipCamera() {
        // Calls CameraManager.CameraXSwap()
        // Updates lensFacing in state
    }

    fun capture() {
        // Sets saveStatus = saving
        // Calls CameraManager.captureImage(onSaved, onError)
        // Callbacks update saveStatus to success or error
    }

    fun onBrightestPoint(result: AnalysisResult) {
        // Renamed from pixelSelector()
        // Stores result as highlightPoint in state
    }

    // Also: receives the SurfaceRequest from CameraManager and stores it in state
}