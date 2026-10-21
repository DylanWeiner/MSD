package com.example.augmentedreality

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val checking: Boolean = true,   // true while a saved token is being verified at startup
    val username: String? = null,   // non-null means logged in
    val busy: Boolean = false,      // a login/register request is in flight
    val error: String? = null
)

class AuthViewModel(
    private val api: PhotoApi,
    private val store: SessionStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AuthUiState(
            checking = store.session.value != null,
            username = store.session.value?.username
        )
    )
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        // Mirror the session store: login, logout, and a rejected token all flow through here.
        viewModelScope.launch {
            store.session.collect { session ->
                _uiState.update { it.copy(username = session?.username) }
            }
        }
        verifySavedSession()
    }

    /** If the app was closed while logged in, check the saved token is still good. */
    private fun verifySavedSession() {
        if (store.session.value == null) {
            _uiState.update { it.copy(checking = false) }
            return
        }
        viewModelScope.launch {
            try {
                api.me()
            } catch (e: ApiError) {
                // A 401 already cleared the session inside PhotoApi. Any other failure
                // (e.g. server offline) keeps the user signed in; later calls will report it.
            }
            _uiState.update { it.copy(checking = false) }
        }
    }

    fun submit(register: Boolean, username: String, password: String) {
        if (_uiState.value.busy) return

        val user = username.trim()
        if (user.isEmpty() || password.isEmpty()) {
            _uiState.update { it.copy(error = "Enter a username and password") }
            return
        }

        _uiState.update { it.copy(busy = true, error = null) }

        viewModelScope.launch {
            try {
                val auth = if (register) api.register(user, password) else api.login(user, password)
                store.save(Session(auth.token, auth.username))
                _uiState.update { it.copy(busy = false) }
            } catch (e: ApiError) {
                _uiState.update { it.copy(busy = false, error = e.message) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                api.logout() // ends the session on the server, so the token stops working everywhere
            } catch (e: ApiError) {
                // Best effort: even if the server can't be reached, log out locally.
            }
            store.clear()
        }
    }
}