package com.example.augmentedreality

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.CameraSelector
import androidx.camera.core.SurfaceRequest
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlin.math.max


// CameraUI file: top-level composables, no wrapper class

@Composable
fun CameraScreen(viewModel: CameraViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> viewModel.onPermissionResult(granted) }

    // On first composition: already granted? otherwise ask.
    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        if (granted) viewModel.onPermissionResult(true)
        else permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    // Bridge between manager and screen: (re)binds on grant and on every lens change.
    LaunchedEffect(state.permissionGranted, state.lensFacing) {
        if (state.permissionGranted) viewModel.bindCamera(lifecycleOwner)
    }

    if (state.permissionGranted) {
        Box(Modifier.fillMaxSize()) {
            displayCamera(state.surfaceRequest)                    // bottom layer
            displayBrightestPixel(                                 // on top of viewfinder
                point = state.highlightPoint,
                mirror = state.lensFacing == CameraSelector.LENS_FACING_FRONT
            )
            displayFlipCamera(
                onClick = viewModel::flipCamera,
                modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
            )
            Column(
                modifier = Modifier.align(Alignment.BottomCenter).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.saveStatus != SaveStatus.Idle) {
                    Text("Save: ${state.saveStatus}", color = Color.White)
                }
                displayCaptureButton(onClick = viewModel::capture)
            }
        }
    } else {
        Column(
            Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Camera permission is required to use this app.")
            Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) {
                Text("Grant permission")
            }
        }
    }
}

@Composable
fun displayCamera(surfaceRequest: SurfaceRequest?) {
    surfaceRequest?.let {
        // Default content scale is Crop (fill-center), which the overlay math assumes.
        CameraXViewfinder(surfaceRequest = it, modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun displayFlipCamera(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(onClick = onClick, modifier = modifier) { Text("Flip") }
}

@Composable
fun displayCaptureButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(onClick = onClick, modifier = modifier) { Text("Capture") }
}

@Composable
fun displayBrightestPixel(point: AnalysisResult?, mirror: Boolean, modifier: Modifier = Modifier) {
    Canvas(modifier.fillMaxSize()) {
        if (point == null) return@Canvas
        val c = point.toViewOffset(size, mirror)
        val r = 28.dp.toPx()
        val stroke = Stroke(width = 3.dp.toPx())
        drawCircle(Color.Red, radius = r, center = c, style = stroke)
        drawLine(Color.Red, Offset(c.x - r * 1.6f, c.y), Offset(c.x + r * 1.6f, c.y), strokeWidth = stroke.width)
        drawLine(Color.Red, Offset(c.x, c.y - r * 1.6f), Offset(c.x, c.y + r * 1.6f), strokeWidth = stroke.width)
    }
}

/** Analysis-buffer coords -> on-screen coords: rotate, mirror (front cam), then crop-scale. */
private fun AnalysisResult.toViewOffset(view: Size, mirror: Boolean): Offset {
    val nx = normalizedX
    val ny = normalizedY
    val (rx, ry) = when (rotationDegrees) {
        90 -> (1f - ny) to nx
        180 -> (1f - nx) to (1f - ny)
        270 -> ny to (1f - nx)
        else -> nx to ny
    }
    val upW = if (rotationDegrees % 180 == 0) imageWidth else imageHeight
    val upH = if (rotationDegrees % 180 == 0) imageHeight else imageWidth

    val scale = max(view.width / upW, view.height / upH)
    val drawnW = upW * scale
    val drawnH = upH * scale
    val offX = (view.width - drawnW) / 2f
    val offY = (view.height - drawnH) / 2f

    val fx = if (mirror) 1f - rx else rx
    return Offset(offX + fx * drawnW, offY + ry * drawnH)
}
