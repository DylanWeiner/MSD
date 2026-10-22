package com.example.augmentedreality.imageanalysis

import android.content.Context
import android.net.Uri
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.augmentedreality.screenstate.DetectionResult
import com.example.augmentedreality.screenstate.ModelType
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

    // All three use cases share a 4:3 aspect ratio, so the saved photo matches the preview
    // and the overlay's crop math stays simple.
    private val previewAndCaptureSelector = ResolutionSelector.Builder()
        .setAspectRatioStrategy(AspectRatioStrategy.RATIO_4_3_FALLBACK_AUTO_STRATEGY)
        .build()

    // Analysis frames are capped near 640x480: plenty for models that resize to 320-448 anyway.
    private val analysisSelector = ResolutionSelector.Builder()
        .setAspectRatioStrategy(AspectRatioStrategy.RATIO_4_3_FALLBACK_AUTO_STRATEGY)
        .setResolutionStrategy(
            ResolutionStrategy(
                Size(640, 480),
                ResolutionStrategy.FALLBACK_RULE_CLOSEST_LOWER_THEN_HIGHER
            )
        )
        .build()

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

        val preview = Preview.Builder()
            .setResolutionSelector(previewAndCaptureSelector)
            .build()
            .apply { setSurfaceProvider { request -> onSurfaceRequest(request) } }

        val capture = ImageCapture.Builder()
            .setResolutionSelector(previewAndCaptureSelector)
            .build()

        val analysis = ImageAnalysis.Builder()
            .setResolutionSelector(analysisSelector)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            // RGBA makes ImageProxy.toBitmap() a cheap copy instead of a YUV->JPEG->bitmap round trip.
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
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

    /** onSaved receives the Uri of the photo CameraX wrote to the gallery (MediaStore). */
    fun captureImage(onSaved: (Uri?) -> Unit, onFailure: (Exception) -> Unit) {
        val capture = imageCapture
        if (capture == null) {
            onFailure(IllegalStateException("Camera not bound"))
            return
        }
        capture.takePicture(
            storage.createOutputOptions(),
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) = onSaved(output.savedUri)
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