package com.example.augmentedreality

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlin.collections.associateBy

class MainActivity : ComponentActivity() {

    // applicationContext so nothing long-lived holds the Activity.
    // Both detectors exist up front; EfficientDet loads its model lazily on first frame.
    private val cameraManager by lazy {
        CameraManager(
            context = applicationContext,
            storage = ImageStorage(applicationContext),
            detectors = listOf<FrameDetector>(
                MlKitDetector(),
                EfficientDetDetector(applicationContext)
            ).associateBy { it.type }
        )
    }

    private val viewModel: CameraViewModel by viewModels {
        viewModelFactory { initializer { CameraViewModel(cameraManager) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CameraScreen(viewModel)
            }
        }
    }
}