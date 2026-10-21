package com.example.maracas

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

data class ShakeEntity(
    val id: Int = 0,
    val timestamp: Long,
    val intensity: Float,
    val accelerationMagnitude: Float
)

data class ShakeStats(
    val totalShakes: Int,
    val maxIntensity: Float,
    val timeRange: Pair<Long, Long>
)

class ShakeDatabase(context: Context) {
    private val shakeDAO = ShakeDatabaseImpl(context)
    fun shakeDAO(): ShakeDAO = shakeDAO
}

interface ShakeDAO {
    suspend fun insertShake(shake: ShakeEntity)
    fun getAllShakes(): Flow<List<ShakeEntity>>
    suspend fun deleteShakesOlderThan(cutoffTime: Long)
    suspend fun deleteShake(id: Int)
    fun getShakeCount(): Flow<Int>
    fun getMaxIntensity(): Flow<Float?>
}

class ShakeDatabaseImpl(context: Context) : ShakeDAO {
    private val prefs: SharedPreferences = context.getSharedPreferences("shakes_db", Context.MODE_PRIVATE)
    private val shakesFlow = MutableStateFlow<List<ShakeEntity>>(loadShakes())

    private fun loadShakes(): List<ShakeEntity> {
        val json = prefs.getString("shakes_list", "[]") ?: "[]"
        return try {
            json.split("|").filter { it.isNotBlank() }.map { parseShake(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveShakes(shakes: List<ShakeEntity>) {
        val json = shakes.joinToString("|") { shake ->
            "${shake.id},${shake.timestamp},${shake.intensity},${shake.accelerationMagnitude}"
        }
        prefs.edit().putString("shakes_list", json).apply()
    }

    private fun parseShake(line: String): ShakeEntity {
        val parts = line.split(",")
        return ShakeEntity(
            id = parts[0].toInt(),
            timestamp = parts[1].toLong(),
            intensity = parts[2].toFloat(),
            accelerationMagnitude = parts[3].toFloat()
        )
    }

    override suspend fun insertShake(shake: ShakeEntity) {
        val currentShakes = shakesFlow.value.toMutableList()
        val newId = (currentShakes.maxOfOrNull { it.id } ?: 0) + 1
        val newShake = shake.copy(id = newId)
        currentShakes.add(0, newShake)
        shakesFlow.value = currentShakes
        saveShakes(currentShakes)
    }

    override fun getAllShakes(): Flow<List<ShakeEntity>> = shakesFlow

    override suspend fun deleteShakesOlderThan(cutoffTime: Long) {
        val filtered = shakesFlow.value.filter { it.timestamp >= cutoffTime }
        shakesFlow.value = filtered
        saveShakes(filtered)
    }

    override suspend fun deleteShake(id: Int) {
        val filtered = shakesFlow.value.filter { it.id != id }
        shakesFlow.value = filtered
        saveShakes(filtered)
    }

    override fun getShakeCount(): Flow<Int> = shakesFlow.map { it.size }

    override fun getMaxIntensity(): Flow<Float?> = shakesFlow.map { shakes ->
        shakes.maxOfOrNull { it.intensity }
    }
}

class ShakeRepository(private val dao: ShakeDAO) {
    fun getAllShakes(): Flow<List<ShakeEntity>> = dao.getAllShakes()

    suspend fun insertShake(shake: ShakeEntity) = dao.insertShake(shake)

    suspend fun deleteShakesOlderThan(daysAgo: Int) {
        val cutoff = System.currentTimeMillis() - (daysAgo * 24 * 60 * 60 * 1000L)
        dao.deleteShakesOlderThan(cutoff)
    }

    suspend fun deleteShake(id: Int) = dao.deleteShake(id)

    fun getStats() = combine(dao.getShakeCount(), dao.getMaxIntensity(), getAllShakes())
    { count, maxIntensity, shakes ->
        ShakeStats(
            totalShakes = count,
            maxIntensity = maxIntensity ?: 0f,
            timeRange = if (shakes.isNotEmpty()) shakes.last().timestamp to shakes.first().timestamp else 0L to 0L
        )
    }
}