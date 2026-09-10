package com.example.classschedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi

@InternalSerializationApi @Serializable
data class DegreePlan(
    val name: String,
    val path: String
)

@InternalSerializationApi @Serializable
data class DegreeRequirements(
    val requirements: List<String>
)



@OptIn(InternalSerializationApi::class)
class ScheduleViewModel : ViewModel() {
    private val classes = MutableStateFlow(listOf<String>())
    private val reqs = MutableStateFlow(listOf<String>())
    private val _availablePlans = MutableStateFlow<List<DegreePlan>>(emptyList())
    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    // Public read-only exports
    val classesPublic: StateFlow<List<String>> = classes
    val reqPublic: StateFlow<List<String>> = reqs
    val availablePlans: StateFlow<List<DegreePlan>> = _availablePlans
    val isLoading: StateFlow<Boolean> = _isLoading
    val errorMessage: StateFlow<String?> = _errorMessage

    // HTTP Client
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    // Hardcoded fallback requirements (in case HTTP fails)
    private val courseReqs = mapOf(
        "Computer Science" to listOf("CS 1010", "CS 2010", "CS 3010", "CS 4010"),
        "Languages" to listOf("LNG 1010", "LNG 2010", "LNG 3010", "LNG 4010"),
        "Biology" to listOf("BIO 1010", "BIO 2010", "BIO 3010", "BIO 4010"),
        "Chemistry" to listOf("CHEM 1010", "CHEM 2010", "CHEM 3010", "CHEM 4010"),
        "History" to listOf("HS 1010", "HS 2010", "HS 3010", "HS 4010"),
        "Art" to listOf("ART 1010", "ART 2010", "ART 3010", "ART 4010")
    )

    init {
        // Fetch degree plans on initialization
        fetchDegreePlans()
    }

    /**
     * Fetch the list of available degree plans from the server.
     */
    private fun fetchDegreePlans() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response: String = httpClient.get(
                    "https://msd2026.github.io/degreePlans/degreePlans.json"
                ).body()

                // Parse the JSON response
                val plans = Json.decodeFromString<List<DegreePlan>>(response)
                _availablePlans.value = plans
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load degree plans: ${e.message}"
                // Fallback: create plans from hardcoded majors
                _availablePlans.value = courseReqs.keys.map { major ->
                    DegreePlan(name = major, path = major.lowercase().replace(" ", "-"))
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Fetch a specific degree plan from the server.
     * If the fetch fails, falls back to hardcoded requirements.
     */
    private suspend fun fetchDegreeRequirements(planPath: String): List<String> {
        return try {
            val response: String = httpClient.get(
                "https://msd2026.github.io/degreePlans/$planPath"
            ).body()

            val degreeReqs = Json.decodeFromString<DegreeRequirements>(response)
            degreeReqs.requirements
        } catch (e: Exception) {
            // Fallback to hardcoded if fetch fails
            val majorName = courseReqs.keys.find { it.lowercase().replace(" ", "-") == planPath }
            courseReqs[majorName] ?: emptyList()
        }
    }

    /**
     * Add a class to the schedule.
     * If a matching requirement exists, mark it as "(DONE)".
     */
    fun addClass(strVal: String) {
        val target = strVal
        if (target in reqs.value) {
            reqs.value = reqs.value.map { if (it == target) "$strVal (DONE)" else it }
        }
        classes.value += strVal
    }

    /**
     * Remove a class from the schedule.
     * If a matching requirement was marked "(DONE)", unmark it.
     */
    fun removeClass(strTask: String) {
        val target = "$strTask (DONE)"
        if (target in reqs.value) {
            reqs.value = reqs.value.map { if (it == target) strTask else it }
        }
        classes.value -= strTask
    }

    /**
     * Edit an existing class.
     * Replaces oldCourseName with newCourseName.
     * Updates requirements based on whether new course satisfies them.
     */
    fun editClass(oldCourseName: String, newCourseName: String) {
        // Step 1: Remove the old course (unmarks any matching requirement)
        removeClass(oldCourseName)

        // Step 2: Add the new course (marks if it matches a requirement)
        addClass(newCourseName)
    }

    /**
     * Add a single requirement to the list.
     */
    fun addReq(strTask: String) {
        reqs.value += strTask
    }

    /**
     * Clear all requirements (called when switching majors).
     */
    fun clearReqs() {
        reqs.value = listOf()
    }

    /**
     * Select a major and populate its requirements from the server.
     * Classes are preserved (not cleared).
     */
    fun selectMajor(major: String) {
        if (major == "None") {
            clearReqs()
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val planPath = major.lowercase().replace(" ", "-")
                val requirements = fetchDegreeRequirements("$planPath.json")

                clearReqs()
                requirements.forEach { req ->
                    addReq(req)
                    // If we have a matching class, mark it as DONE
                    if (req in classes.value) {
                        reqs.value = reqs.value.map { if (it == req) "$req (DONE)" else it }
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load major requirements: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        httpClient.close()
    }
}