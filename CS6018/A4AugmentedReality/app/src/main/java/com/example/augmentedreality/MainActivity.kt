package com.example.augmentedreality

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class MainActivity : ComponentActivity() {

    // applicationContext so nothing long-lived holds the Activity
    private val cameraManager by lazy {
        CameraManager(applicationContext, ImageStorage(applicationContext))
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