package com.example.augmentedreality

import androidx.camera.core.CameraSelector
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CameraViewModel(private val cameraManager: CameraManager) : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    fun onPermissionResult(granted: Boolean) {
        _uiState.update { it.copy(permissionGranted = granted) }
    }

    /** Only changes state. The screen's LaunchedEffect sees the new lens and rebinds. */
    fun flipCamera() {
        _uiState.update {
            val next = if (it.lensFacing == CameraSelector.LENS_FACING_BACK)
                CameraSelector.LENS_FACING_FRONT else CameraSelector.LENS_FACING_BACK
            it.copy(lensFacing = next, detectionResult = null)
        }
    }

    /** Switches the active model at runtime. No camera rebind needed. */
    fun selectModel(model: ModelType) {
        if (model == _uiState.value.selectedModel) return
        cameraManager.setModel(model)
        _uiState.update { it.copy(selectedModel = model, detectionResult = null) } // clear stale boxes
    }

    /** Suspends until cancelled; cancelling unbinds the camera (see CameraManager.bind). */
    suspend fun bindCamera(lifecycleOwner: LifecycleOwner) {
        cameraManager.bind(
            lifecycleOwner = lifecycleOwner,
            lensFacing = _uiState.value.lensFacing,
            onSurfaceRequest = { request ->
                _uiState.update { it.copy(surfaceRequest = request) }
            },
            onDetections = ::onDetections
        )
    }

    fun capture() {
        _uiState.update { it.copy(saveStatus = SaveStatus.Saving) }
        cameraManager.captureImage(
            onSaved = { _uiState.update { it.copy(saveStatus = SaveStatus.Success) } },
            onFailure = { _uiState.update { it.copy(saveStatus = SaveStatus.Error) } }
        )
    }

    fun onDetections(result: DetectionResult) {
        _uiState.update {
            // A frame still in flight from the previous model can land after a switch; drop it.
            if (result.model == it.selectedModel) it.copy(detectionResult = result) else it
        }
    }

    override fun onCleared() {
        cameraManager.close()
    }
}