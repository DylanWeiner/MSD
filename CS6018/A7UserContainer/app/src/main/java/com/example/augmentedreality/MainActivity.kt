package com.example.augmentedreality

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class MainActivity : ComponentActivity() {

    // applicationContext everywhere, so nothing long-lived holds the Activity.
    private val sessionStore by lazy { SessionStore(applicationContext) }
    private val photoApi by lazy { PhotoApi(sessionStore) }
    private val imageLoader by lazy { PhotoImageLoader(photoApi) }

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

    private val authViewModel: AuthViewModel by viewModels {
        viewModelFactory { initializer { AuthViewModel(photoApi, sessionStore) } }
    }

    private val cameraViewModel: CameraViewModel by viewModels {
        viewModelFactory {
            initializer {
                CameraViewModel(
                    cameraManager = cameraManager,
                    uploader = PhotoUploader(applicationContext, photoApi),
                    store = sessionStore
                )
            }
        }
    }

    private val galleryViewModel: GalleryViewModel by viewModels {
        viewModelFactory { initializer { GalleryViewModel(photoApi, sessionStore, imageLoader) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Black bars extend behind the system bars; dark style keeps status/nav icons light.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.BLACK),
            navigationBarStyle = SystemBarStyle.dark(Color.BLACK)
        )
        setContent {
            // Dark theme throughout, so the light status-bar icons stay readable on every screen.
            MaterialTheme(colorScheme = darkColorScheme()) {
                Surface(Modifier.fillMaxSize()) {
                    AppRoot(
                        auth = authViewModel,
                        camera = cameraViewModel,
                        gallery = galleryViewModel
                    )
                }
            }
        }
    }
}