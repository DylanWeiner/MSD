package com.example.augmentedreality

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.CameraSelector
import androidx.camera.core.SurfaceRequest
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlin.math.max

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
        // Classic camera-app layout: black top bar, 4:3 viewfinder, black bottom bar.
        Column(Modifier.fillMaxSize().background(Color.Black)) {

            // Top bar. The inset padding keeps it below the status bar and camera cutout.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Top + WindowInsetsSides.Horizontal
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                displayModelSelector(
                    selected = state.selectedModel,
                    onSelect = viewModel::selectModel
                )
                displayFlipCamera(onClick = viewModel::flipCamera)
            }

            // Viewfinder: 4:3 portrait (3 wide : 4 tall), matching the CameraX use cases.
            Box(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f)
                    .clipToBounds()
            ) {
                displayCamera(state.surfaceRequest)                // bottom layer
                displayDetections(                                 // on top of viewfinder
                    result = state.detectionResult,
                    mirror = state.lensFacing == CameraSelector.LENS_FACING_FRONT
                )
                displayStatus(
                    state = state,
                    modifier = Modifier.align(Alignment.BottomStart).padding(8.dp)
                )
            }

            // Bottom bar takes the remaining height; shutter sits centered above the nav bar.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                displayCaptureButton(onClick = viewModel::capture)
            }
        }
    } else {
        Column(
            Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
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
fun displayModelSelector(
    selected: ModelType,
    onSelect: (ModelType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        ModelType.entries.forEach { model ->
            FilterChip(
                selected = model == selected,
                onClick = { onSelect(model) },
                label = { Text(model.displayName) },
                // Chip color matches that model's box color, and stays readable on black.
                colors = FilterChipDefaults.filterChipColors(
                    labelColor = Color.White,
                    selectedContainerColor = model.boxColor(),
                    selectedLabelColor = Color.Black
                )
            )
        }
    }
}

@Composable
fun displayFlipCamera(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(onClick = onClick, modifier = modifier) { Text("Flip") }
}

/** Round shutter button: white ring with a white disc inside. */
@Composable
fun displayCaptureButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier
            .size(76.dp)
            .border(4.dp, Color.White, CircleShape)
            .padding(8.dp)
            .clip(CircleShape)
            .background(Color.White)
            .clickable(onClickLabel = "Capture photo", onClick = onClick)
    )
}

@Composable
fun displayStatus(state: CameraUiState, modifier: Modifier = Modifier) {
    val result = state.detectionResult
    val detectionText = if (result != null) {
        "${state.selectedModel.displayName} · ${result.inferenceMs} ms · ${result.detections.size} objects"
    } else {
        "${state.selectedModel.displayName} · waiting for detections"
    }
    Column(modifier.background(Color.Black.copy(alpha = 0.5f)).padding(horizontal = 8.dp, vertical = 4.dp)) {
        Text(detectionText, color = Color.White, fontSize = 12.sp)
        if (state.saveStatus != SaveStatus.Idle) {
            Text("Save: ${state.saveStatus}", color = Color.White, fontSize = 12.sp)
        }
    }
}

@Composable
fun displayDetections(result: DetectionResult?, mirror: Boolean, modifier: Modifier = Modifier) {
    val textMeasurer = rememberTextMeasurer()
    Canvas(modifier.fillMaxSize()) {
        if (result == null || result.frameWidth <= 0 || result.frameHeight <= 0) return@Canvas

        val color = result.model.boxColor()
        val stroke = Stroke(width = 3.dp.toPx())
        val labelStyle = TextStyle(color = Color.Black, fontSize = 14.sp)

        for (d in result.detections) {
            val rect = d.toViewRect(result, size, mirror)
            drawRect(color, topLeft = rect.topLeft, size = rect.size, style = stroke)

            val text = d.confidence?.let { "${d.label} ${(it * 100).toInt()}%" } ?: d.label
            val layout = textMeasurer.measure(text, labelStyle)
            val labelTopLeft = Offset(rect.left, (rect.top - layout.size.height).coerceAtLeast(0f))
            drawRect(color, topLeft = labelTopLeft, size = layout.size.toSize())
            drawText(layout, topLeft = labelTopLeft)
        }
    }
}

private fun ModelType.boxColor(): Color = when (this) {
    ModelType.MLKIT -> Color(0xFFFF9800)         // orange
    ModelType.EFFICIENTDET -> Color(0xFF00E5FF)  // cyan
}

/**
 * Normalized upright box -> on-screen rect. Rotation is already handled by the detectors,
 * so this only mirrors (front camera) and reproduces the viewfinder's crop scaling.
 */
private fun Detection.toViewRect(result: DetectionResult, view: Size, mirror: Boolean): Rect {
    val scale = max(view.width / result.frameWidth, view.height / result.frameHeight)
    val drawnW = result.frameWidth * scale
    val drawnH = result.frameHeight * scale
    val offX = (view.width - drawnW) / 2f
    val offY = (view.height - drawnH) / 2f

    // Mirroring flips which edge is left and which is right, so swap them.
    val l = if (mirror) 1f - right else left
    val r = if (mirror) 1f - left else right

    return Rect(
        left = offX + l * drawnW,
        top = offY + top * drawnH,
        right = offX + r * drawnW,
        bottom = offY + bottom * drawnH
    )
}