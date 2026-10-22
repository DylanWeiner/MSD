package com.example.augmentedreality.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.augmentedreality.screenstate.AuthViewModel
import com.example.augmentedreality.screenstate.GalleryViewModel
import com.example.augmentedreality.screenstate.CameraViewModel

enum class AppScreen { Camera, Gallery }

/**
 * Decides which screen is showing:
 *   checking a saved login -> spinner
 *   logged out             -> login / create account
 *   logged in              -> camera, or the gallery of the user's server photos
 */
@Composable
fun AppRoot(
    auth: AuthViewModel,
    camera: CameraViewModel,
    gallery: GalleryViewModel
) {
    val authState by auth.uiState.collectAsStateWithLifecycle()
    var screen by rememberSaveable { mutableStateOf(AppScreen.Camera) }

    // After any logout (including an expired token), the next login starts on the camera.
    LaunchedEffect(authState.username) {
        if (authState.username == null) screen = AppScreen.Camera
    }

    val username = authState.username
    when {
        authState.checking -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        username == null -> LoginScreen(
            state = authState,
            onSubmit = auth::submit,
            onDismissError = auth::clearError
        )

        else -> {
            if (screen == AppScreen.Gallery) {
                GalleryScreen(
                    viewModel = gallery,
                    username = username,
                    onBack = { screen = AppScreen.Camera },
                    onLogout = auth::logout
                )
            } else {
                CameraScreen(
                    viewModel = camera,
                    onOpenGallery = { screen = AppScreen.Gallery }
                )
            }
        }
    }
}