package com.example.augmentedreality

class CameraManager {
    // Owns ProcessCameraProvider, Preview, ImageCapture, ImageAnalysis, analysis executor

    fun initializeCamera(lifecycleOwner: LifecycleOwner) {
        // Gets the provider
        // Builds Preview and forwards its SurfaceRequest to the ViewModel
        // Builds ImageCapture
        // Builds ImageAnalysis, sets ImageAnalyzer on the background executor
        // Unbinds everything, then bindToLifecycle with the current CameraSelector
    }

    fun CameraXSwap() {
        // Toggles front/back CameraSelector
        // Calls initializeCamera() again to rebind
    }

    fun captureImage(onSaved, onError) {
        // Gets output options from ImageStorage.setStorage()
        // Calls ImageCapture.takePicture(outputOptions, ...)
        // Callback: onSaved or onError
    }
}