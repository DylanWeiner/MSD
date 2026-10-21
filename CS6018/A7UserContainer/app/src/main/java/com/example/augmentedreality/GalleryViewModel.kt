package com.example.augmentedreality

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GalleryUiState(
    val photos: List<PhotoDto> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

class GalleryViewModel(
    private val api: PhotoApi,
    store: SessionStore,
    val loader: PhotoImageLoader
) : ViewModel() {

    private val _uiState = MutableStateFlow(GalleryUiState())
    val uiState: StateFlow<GalleryUiState> = _uiState.asStateFlow()

    // Bumped on every logout. A request that was in flight when the user logged out
    // must not repopulate the list, or the next person to log in could briefly see it.
    private var generation = 0

    init {
        viewModelScope.launch {
            store.session.collect { session ->
                if (session == null) {
                    generation++
                    _uiState.value = GalleryUiState()
                    loader.clear()
                }
            }
        }
    }

    fun refresh() {
        val startedIn = generation
        _uiState.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            try {
                val photos = api.listPhotos()
                if (startedIn == generation) {
                    _uiState.update { it.copy(photos = photos, loading = false) }
                }
            } catch (e: ApiError) {
                if (startedIn == generation) {
                    _uiState.update { it.copy(loading = false, error = e.message) }
                }
            }
        }
    }

    fun delete(photoId: String) {
        val startedIn = generation
        viewModelScope.launch {
            try {
                api.deletePhoto(photoId)
                if (startedIn == generation) {
                    _uiState.update { state -> state.copy(photos = state.photos.filterNot { it.id == photoId }) }
                }
            } catch (e: ApiError) {
                if (startedIn == generation) {
                    _uiState.update { it.copy(error = e.message) }
                }
            }
        }
    }
}