package com.example.maracas

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MaracaViewModel(
    private val repository: ShakeRepository,
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _shakes = MutableStateFlow<List<ShakeEntity>>(emptyList())
    val shakes: StateFlow<List<ShakeEntity>> = _shakes.asStateFlow()

    private val _stats = MutableStateFlow(ShakeStats(0, 0f, 0L to 0L))
    val stats: StateFlow<ShakeStats> = _stats.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private var detector: ShakeDetector? = null

    init {
        viewModelScope.launch {
            repository.getAllShakes().collect { _shakes.value = it }
        }
        viewModelScope.launch {
            repository.getStats().collect { _stats.value = it }
        }
    }

    fun startListening() {
        if (detector == null) {
            detector = ShakeDetector(context) { shake ->
                viewModelScope.launch { repository.insertShake(shake) }
            }
        }
        detector?.start()
        _isListening.value = true
        _uiState.value = UiState.Listening
    }

    fun stopListening() {
        detector?.stop()
        _isListening.value = false
        _uiState.value = UiState.Idle
    }

    fun deleteShakesOlderThan(daysAgo: Int) {
        viewModelScope.launch {
            repository.deleteShakesOlderThan(daysAgo)
            _uiState.value = UiState.DeletedOldShakes(daysAgo)
        }
    }

    fun deleteShake(id: Int) {
        viewModelScope.launch { repository.deleteShake(id) }
    }

    override fun onCleared() {
        super.onCleared()
        detector?.stop()
    }

    sealed class UiState {
        object Idle : UiState()
        object Listening : UiState()
        data class DeletedOldShakes(val daysAgo: Int) : UiState()
    }
}

class MaracaViewModelFactory(
    private val dao: ShakeDAO,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MaracaViewModel(ShakeRepository(dao), context) as T
    }
}