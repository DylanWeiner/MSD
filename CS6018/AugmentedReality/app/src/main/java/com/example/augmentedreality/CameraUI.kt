package com.example.augmentedreality

import androidx.compose.runtime.Composable

// CameraUI file: top-level composables, no wrapper class

@Composable
fun CameraScreen(viewModel: CameraViewModel) {
    // Collects state from the ViewModel
    // Permission launcher via rememberLauncherForActivityResult
    // On first composition: check if already granted, otherwise launch request
    // Launcher callback calls viewModel.onPermissionResult()

    // val lifecycleOwner = LocalLifecycleOwner.current

    if (state.permissionGranted) {
        // LaunchedEffect(permissionGranted, lensFacing) { CameraManager.initializeCamera(lifecycleOwner) }
        Box {
            displayCamera(state.surfaceRequest)   // bottom layer
            displayBrightestPixel(state.highlightPoint)   // on top of the viewfinder
            displayFlipCamera(onClick = viewModel::flipCamera)
            displayCaptureButton(onClick = viewModel::capture)
        }
    } else {
        // Display error message
    }
}

@Composable
fun displayCamera(surfaceRequest: SurfaceRequest?) {
    // Hosts CameraXViewfinder and feeds it the SurfaceRequest
}

@Composable
fun displayFlipCamera(onClick) {
    // Flip button
}

@Composable
fun displayCaptureButton(onClick) {
    // Capture button
}

@Composable
fun displayBrightestPixel(point: AnalysisResult?) {
    // Maps normalized coordinates to screen coordinates
    // (account for rotation, scaling/cropping, front camera mirroring)
    // Draws circle or crosshair at that spot
}