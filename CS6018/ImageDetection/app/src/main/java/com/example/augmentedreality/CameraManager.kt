package com.example.augmentedreality

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.awaitCancellation
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraManager(
    private val context: Context,
    private val storage: ImageStorage,
    private val detectors: Map<ModelType, FrameDetector>
) {
    private val analysisExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private var imageCapture: ImageCapture? = null

    // Read on the analysis thread every frame, so switching models needs no rebind.
    @Volatile
    private var activeModel: ModelType = ModelType.MLKIT

    fun setModel(model: ModelType) {
        activeModel = model
    }

    /**
     * Binds Preview + ImageCapture + ImageAnalysis, then suspends.
     * Cancelling the calling coroutine (lens flip, screen leaves composition) unbinds.
     */
    suspend fun bind(
        lifecycleOwner: LifecycleOwner,
        lensFacing: Int,
        onSurfaceRequest: (SurfaceRequest) -> Unit,
        onDetections: (DetectionResult) -> Unit
    ) {
        val provider = ProcessCameraProvider.awaitInstance(context)

        val preview = Preview.Builder().build().apply {
            setSurfaceProvider { request -> onSurfaceRequest(request) }
        }
        val capture = ImageCapture.Builder().build()
        val analysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .apply {
                setAnalyzer(
                    analysisExecutor,
                    ImageAnalyzer(
                        detectorProvider = { detectors.getValue(activeModel) },
                        onResult = onDetections
                    )
                )
            }

        val selector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        provider.unbindAll()
        provider.bindToLifecycle(lifecycleOwner, selector, preview, capture, analysis)
        imageCapture = capture

        try {
            awaitCancellation()
        } finally {
            imageCapture = null
            provider.unbindAll()
        }
    }

    fun captureImage(onSaved: () -> Unit, onFailure: (Exception) -> Unit) {
        val capture = imageCapture
        if (capture == null) {
            onFailure(IllegalStateException("Camera not bound"))
            return
        }
        capture.takePicture(
            storage.createOutputOptions(),
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) = onSaved()
                override fun onError(exception: ImageCaptureException) = onFailure(exception)
            }
        )
    }

    /** Called from the ViewModel's onCleared(). */
    fun close() {
        analysisExecutor.shutdown()
        detectors.values.forEach { it.close() }
    }
}