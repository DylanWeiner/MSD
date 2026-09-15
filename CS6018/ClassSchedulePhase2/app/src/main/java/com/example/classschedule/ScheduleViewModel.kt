package com.example.classschedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.coroutines.launch

@Serializable
data class DegreePlansResponse(
    val plans: List<DegreePlan>
)

@Serializable
data class DegreePlan(
    val name: String,
    val path: String
)

@Serializable
data class DegreeRequirementsResponse(
    val name: String,
    val requirements: List<Requirement>
)

@Serializable
data class Requirement(
    val type: String,
    val course: CourseInfo? = null,
    val courses: List<CourseInfo>? = null
)

@Serializable
data class CourseInfo(
    val department: String,
    val number: String
)

fun CourseInfo.toDisplayString(): String = "$department $number"

class ScheduleViewModel : ViewModel() {
    private val classes = MutableStateFlow(listOf<String>())
    private val reqs = MutableStateFlow(listOf<String>())
    private val _availablePlans = MutableStateFlow<List<DegreePlan>>(emptyList())
    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    val classesPublic: StateFlow<List<String>> = classes
    val reqPublic: StateFlow<List<String>> = reqs
    val availablePlans: StateFlow<List<DegreePlan>> = _availablePlans
    val isLoading: StateFlow<Boolean> = _isLoading
    val errorMessage: StateFlow<String?> = _errorMessage

    private val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    /**
     * Fetch the list of available degree plans from the server.
     * Call this from MainActivity, NOT from init{}.
     */
    fun fetchDegreePlans() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response: String = httpClient.get(
                    "https://msd2026.github.io/degreePlans/degreePlans.json"
                ).body()

                android.util.Log.d("ScheduleVM", "Raw response: $response")

                try {
                    val plansResponse = Json.decodeFromString<DegreePlansResponse>(response)
                    _availablePlans.value = plansResponse.plans
                    android.util.Log.d("ScheduleVM", "Successfully parsed ${plansResponse.plans.size} plans")
                } catch (parseError: Exception) {
                    android.util.Log.e("ScheduleVM", "Parse error: ${parseError.message}")
                    _errorMessage.value = "Parse error: ${parseError.message}"
                }
            } catch (e: Exception) {
                android.util.Log.e("ScheduleVM", "Network error: ${e.message}")
                _errorMessage.value = "Failed to load degree plans: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Fetch a specific degree plan from the server.
     */
    private suspend fun fetchDegreeRequirements(planPath: String): List<String> {
        return try {
            android.util.Log.d("ScheduleVM", "Fetching requirements from: $planPath")

            val response: String = httpClient.get(
                "https://msd2026.github.io/degreePlans/$planPath"
            ).body()

            android.util.Log.d("ScheduleVM", "Requirements response: $response")

            val degreeReqs = Json.decodeFromString<DegreeRequirementsResponse>(response)

            // Extract all course requirements and convert to display strings
            val courseList = mutableListOf<String>()

            for (req in degreeReqs.requirements) {
                when (req.type) {
                    "requiredCourse" -> {
                        if (req.course != null) {
                            courseList.add(req.course.toDisplayString())
                        }
                    }
                    "oneOf" -> {
                        if (req.courses != null) {
                            for (course in req.courses) {
                                courseList.add(course.toDisplayString())
                            }
                        }
                    }
                    // Add other types as needed
                }
            }

            android.util.Log.d("ScheduleVM", "Parsed requirements: ${courseList.size} courses")
            courseList
        } catch (e: Exception) {
            android.util.Log.e("ScheduleVM", "Error fetching requirements: ${e.message}")
            emptyList()
        }
    }

    /**
     * Add a class to the schedule.
     * If a matching requirement exists, mark it as "(DONE)".
     */
    fun addClass(strVal: String) {
        if (strVal in reqs.value) {
            reqs.value = reqs.value.map { if (it == strVal) "$strVal (DONE)" else it }
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
     * Only changes the course name in the classes list.
     * Automatically updates requirement marks based on whether the NEW course matches.
     */
    fun editClass(oldCourseName: String, newCourseName: String) {
        // Step 1: Check if old course was marking a requirement as DONE
        val oldWasMarked = "$oldCourseName (DONE)" in reqs.value
        val oldUnmarkedReq = if (oldWasMarked) oldCourseName else null

        // Step 2: Update the course name in classes list
        classes.value = classes.value.map {
            if (it == oldCourseName) newCourseName else it
        }

        // Step 3: Unmark the old requirement if it was marked
        if (oldUnmarkedReq != null) {
            reqs.value = reqs.value.map {
                if (it == "$oldUnmarkedReq (DONE)") oldUnmarkedReq else it
            }
        }

        // Step 4: Check if new course matches any requirement and mark it
        if (newCourseName in reqs.value) {
            reqs.value = reqs.value.map {
                if (it == newCourseName) "$newCourseName (DONE)" else it
            }
        }
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
     * Select a major and populate its requirements.
     * Fetches from server.
     * Classes are preserved (not cleared).
     * Existing classes are checked against new requirements.
     */
    fun selectMajor(major: String) {
        if (major == "None") {
            clearReqs()
            return
        }

        viewModelScope.launch {
            try {
                // Find the plan that matches the major name
                val selectedPlan = _availablePlans.value.find { it.name == major }

                if (selectedPlan == null) {
                    _errorMessage.value = "Major not found: $major"
                    return@launch
                }

                // Use the path from the plan, not derived from the name
                val requirements = fetchDegreeRequirements(selectedPlan.path)

                clearReqs()
                requirements.forEach { req ->
                    addReq(req)
                }

                // Check existing classes against new requirements and mark if they match
                classes.value.forEach { course ->
                    if (course in requirements) {
                        reqs.value = reqs.value.map {
                            if (it == course) "$course (DONE)" else it
                        }
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load major requirements: ${e.message}"
                android.util.Log.e("ScheduleVM", "selectMajor error: ${e.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        httpClient.close()
    }
}