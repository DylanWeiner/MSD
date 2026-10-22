package com.example.augmentedreality.screenstate

import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.augmentedreality.serverinteraction.PhotoUploader
import com.example.augmentedreality.imageanalysis.CameraManager
import com.example.augmentedreality.serverinteraction.ApiError
import com.example.augmentedreality.serverinteraction.SessionStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class CameraViewModel(
    private val cameraManager: CameraManager,
    private val uploader: PhotoUploader,
    store: SessionStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    init {
        // Logging out wipes the upload status so the next user doesn't see the last one's.
        viewModelScope.launch {
            store.session.collect { session ->
                if (session == null) {
                    _uiState.update { it.copy(uploadStatus = UploadStatus.Idle, uploadMessage = null) }
                }
            }
        }
    }

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

    /** Save to the phone's gallery first, then send the same photo to the server. */
    fun capture() {
        _uiState.update { it.copy(saveStatus = SaveStatus.Saving) }
        cameraManager.captureImage(
            onSaved = { uri ->
                _uiState.update { it.copy(saveStatus = SaveStatus.Success) }
                if (uri != null) {
                    upload(uri)
                } else {
                    _uiState.update {
                        it.copy(uploadStatus = UploadStatus.Failed, uploadMessage = "No saved photo to upload")
                    }
                }
            },
            onFailure = { _uiState.update { it.copy(saveStatus = SaveStatus.Error) } }
        )
    }

    private fun upload(uri: Uri) {
        _uiState.update { it.copy(uploadStatus = UploadStatus.Uploading, uploadMessage = null) }
        viewModelScope.launch {
            try {
                uploader.upload(uri)
                _uiState.update { it.copy(uploadStatus = UploadStatus.Done) }
            } catch (e: ApiError) {
                _uiState.update { it.copy(uploadStatus = UploadStatus.Failed, uploadMessage = e.message) }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(uploadStatus = UploadStatus.Failed, uploadMessage = "Couldn't read the saved photo")
                }
            }
        }
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