package com.example.augmentedreality

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CancellationException
import java.text.DateFormat
import java.util.Date

@Composable
fun GalleryScreen(
    viewModel: GalleryViewModel,
    username: String,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedId by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) { viewModel.refresh() }

    // Back closes the full-size viewer first, then leaves the gallery.
    BackHandler { if (selectedId != null) selectedId = null else onBack() }

    val selected = state.photos.firstOrNull { it.id == selectedId }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)) {
            GalleryHeader(
                username = username,
                loading = state.loading,
                onBack = onBack,
                onRefresh = viewModel::refresh,
                onLogout = onLogout
            )

            // A problem that doesn't replace the grid (e.g. a failed delete) shows as a line of text.
            if (state.error != null && state.photos.isNotEmpty()) {
                Text(
                    state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            when {
                state.error != null && state.photos.isEmpty() -> Column(
                    Modifier.fillMaxSize().padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(state.error!!, color = MaterialTheme.colorScheme.error)
                    Button(onClick = viewModel::refresh) { Text("Try again") }
                }

                state.loading && state.photos.isEmpty() -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }

                state.photos.isEmpty() -> Box(
                    Modifier.fillMaxSize().padding(24.dp),
                    contentAlignment = Alignment.Center
                ) { Text("No photos yet. Take one with the camera and it will show up here.") }

                else -> LazyVerticalGrid(
                    columns = GridCells.Adaptive(110.dp),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.photos, key = { it.id }) { photo ->
                        PhotoImage(
                            loader = viewModel.loader,
                            photoId = photo.id,
                            maxDimension = 360,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clickable { selectedId = photo.id }
                        )
                    }
                }
            }
        }

        if (selected != null) {
            PhotoViewer(
                photo = selected,
                loader = viewModel.loader,
                onClose = { selectedId = null },
                onDelete = {
                    viewModel.delete(selected.id)
                    selectedId = null
                }
            )
        }
    }
}

@Composable
private fun GalleryHeader(
    username: String,
    loading: Boolean,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onLogout: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = onBack) { Text("Camera") }
        Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Your photos", style = MaterialTheme.typography.titleMedium, maxLines = 1)
            Text(
                username,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        TextButton(onClick = onRefresh, enabled = !loading) { Text("Refresh") }
        TextButton(onClick = onLogout) { Text("Log out") }
    }
}

@Composable
private fun PhotoViewer(
    photo: PhotoDto,
    loader: PhotoImageLoader,
    onClose: () -> Unit,
    onDelete: () -> Unit
) {
    var confirmDelete by remember { mutableStateOf(false) }
    val taken = remember(photo.createdAt) {
        DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(photo.createdAt))
    }

    Box(Modifier.fillMaxSize().background(Color.Black)) {
        PhotoImage(
            loader = loader,
            photoId = photo.id,
            maxDimension = 2048,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)
        )
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = onClose) { Text("Close") }
            Text(taken, color = Color.White, style = MaterialTheme.typography.bodySmall)
            TextButton(onClick = { confirmDelete = true }) {
                Text("Delete", color = MaterialTheme.colorScheme.error)
            }
        }
    }

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Delete this photo?") },
            text = { Text("It will be removed from the server. This can't be undone.") },
            confirmButton = {
                TextButton(onClick = { confirmDelete = false; onDelete() }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { confirmDelete = false }) { Text("Cancel") }
            }
        )
    }
}

private sealed interface ImageLoad {
    data object Loading : ImageLoad
    data class Loaded(val image: ImageBitmap) : ImageLoad
    data object Failed : ImageLoad
}

/** Shows one server photo: a spinner while it downloads, the image when ready, or an error note. */
@Composable
fun PhotoImage(
    loader: PhotoImageLoader,
    photoId: String,
    maxDimension: Int,
    contentScale: ContentScale,
    modifier: Modifier = Modifier
) {
    val load by produceState<ImageLoad>(ImageLoad.Loading, photoId, maxDimension) {
        value = try {
            loader.load(photoId, maxDimension)?.let { ImageLoad.Loaded(it) } ?: ImageLoad.Failed
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            ImageLoad.Failed
        }
    }

    Box(
        modifier.background(if (load is ImageLoad.Loaded) Color.Transparent else Color.DarkGray.copy(alpha = 0.4f)),
        contentAlignment = Alignment.Center
    ) {
        when (val current = load) {
            is ImageLoad.Loaded -> Image(
                bitmap = current.image,
                contentDescription = "Photo",
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize()
            )
            ImageLoad.Loading -> CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp)
            ImageLoad.Failed -> Text("Couldn't load", style = MaterialTheme.typography.bodySmall)
        }
    }
}