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
            it.copy(lensFacing = next, highlightPoint = null)
        }
    }

    /** Suspends until cancelled; cancelling unbinds the camera (see CameraManager.bind). */
    suspend fun bindCamera(lifecycleOwner: LifecycleOwner) {
        cameraManager.bind(
            lifecycleOwner = lifecycleOwner,
            lensFacing = _uiState.value.lensFacing,
            onSurfaceRequest = { request ->
                _uiState.update { it.copy(surfaceRequest = request) }
            },
            onAnalysis = ::onBrightestPoint
        )
    }

    fun capture() {
        _uiState.update { it.copy(saveStatus = SaveStatus.Saving) }
        cameraManager.captureImage(
            onSaved = { _uiState.update { it.copy(saveStatus = SaveStatus.Success) } },
            onFailure = { _uiState.update { it.copy(saveStatus = SaveStatus.Error) } }
        )
    }

    fun onBrightestPoint(result: AnalysisResult) {
        _uiState.update { it.copy(highlightPoint = result) }
    }
}

