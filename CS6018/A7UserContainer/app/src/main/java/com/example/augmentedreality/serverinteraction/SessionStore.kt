package com.example.augmentedreality.serverinteraction

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Session(val token: String, val username: String)

/**
 * Remembers the login token across app restarts.
 * Kept in app-private SharedPreferences, which is fine for this assignment;
 * a production app would encrypt the token with the Android Keystore.
 *
 * [session] is a flow so the whole UI reacts the moment the user logs out or the
 * server rejects the token (the app drops back to the login screen).
 */
class SessionStore(context: Context) {

    private val prefs = context.applicationContext.getSharedPreferences("session", Context.MODE_PRIVATE)

    private val _session = MutableStateFlow(load())
    val session: StateFlow<Session?> = _session.asStateFlow()

    /** Fixed in ServerConfig.kt. Not user input. */
    val serverUrl: String get() = "http://10.0.2.2:8080"

    fun save(session: Session) {
        prefs.edit()
            .putString(KEY_TOKEN, session.token)
            .putString(KEY_USERNAME, session.username)
            .apply()
        _session.value = session
    }

    fun clear() {
        prefs.edit().remove(KEY_TOKEN).remove(KEY_USERNAME).apply()
        _session.value = null
    }

    private fun load(): Session? {
        val token = prefs.getString(KEY_TOKEN, null) ?: return null
        val username = prefs.getString(KEY_USERNAME, null) ?: return null
        return Session(token, username)
    }

    private companion object {
        const val KEY_TOKEN = "token"
        const val KEY_USERNAME = "username"
    }
}