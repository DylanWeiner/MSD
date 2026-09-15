package com.example.classschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.classschedule.ui.theme.ClassScheduleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClassScheduleTheme {
                val scheduleVM: ScheduleViewModel = viewModel()
                scheduleVM.fetchDegreePlans()
                CourseNameInput(scheduleVM)
            }
        }
    }
}

/**
 * Edit course dialog.
 */
@Composable
fun EditCourseDialog(
    courseName: String,
    onConfirm: (newCourseName: String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var editedCourseName by remember { mutableStateOf(courseName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Course") },
        text = {
            Column {
                Text("Update the course name:")
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = editedCourseName,
                    onValueChange = { editedCourseName = it },
                    label = { Text("Course name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (editedCourseName.isNotBlank()) {
                        onConfirm(editedCourseName)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        modifier = modifier
    )
}

/**
 * Single course row with edit and remove buttons.
 */
@Composable
fun CourseRow(
    courseName: String,
    onEdit: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            courseName,
            fontSize = 20.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Button(onClick = onEdit) {
            Text("Edit Item")
        }
        Button(onClick = onRemove) {
            Text("Remove Item")
        }
    }
}

/**
 * Single requirement row (read-only).
 */
@Composable
fun RequirementRow(
    requirement: String,
    modifier: Modifier = Modifier
) {
    Text(
        requirement,
        fontSize = 20.sp,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(8.dp)
    )
}

/**
 * The course input form.
 */
@Composable
fun CourseInputForm(
    onAddCourse: (department: String, courseNumber: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var courseName by remember { mutableStateOf("") }
    var courseId by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        Text(
            "Course Input",
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Blue
        )
        TextField(
            value = courseName,
            onValueChange = { newText -> courseName = newText },
            label = { Text("Enter your course Department (i.e. CS)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = courseId,
            onValueChange = { newText -> courseId = newText },
            label = { Text("Enter your course number (i.e. 1010)") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                if (courseName.isNotBlank() && courseId.isNotBlank()) {
                    onAddCourse(courseName, courseId)
                    courseName = ""
                    courseId = ""
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Add Item")
        }
    }
}

/**
 * Major dropdown selector (fetches from server, falls back to hardcoded).
 */
@Composable
fun MajorDropdown(
    selectedMajor: String,
    onMajorSelected: (major: String) -> Unit,
    availablePlans: List<DegreePlan>,
    isLoading: Boolean,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Select Major",
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Blue
        )

        if (isLoading) {
            Text("Loading majors...", fontSize = 14.sp, color = Color.Gray)
        }

        if (errorMessage != null) {
            Text(
                errorMessage,
                fontSize = 12.sp,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        DropdownMenu(
            expanded = expanded && !isLoading,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("None") },
                onClick = {
                    onMajorSelected("None")
                    expanded = false
                }
            )
            availablePlans.forEach { plan ->
                DropdownMenuItem(
                    text = { Text(plan.name) },
                    onClick = {
                        onMajorSelected(plan.name)
                        expanded = false
                    }
                )
            }
        }

        Button(
            onClick = { if (!isLoading) expanded = !expanded },
            enabled = !isLoading
        ) {
            Text(selectedMajor)
        }
    }
}

/**
 * Container for the class schedule list.
 */
@Composable
fun ClassScheduleList(
    classes: List<String>,
    onEditCourse: (oldName: String, newName: String) -> Unit,
    onRemoveCourse: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var courseBeingEdited by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier) {
        Text(
            "Class Schedule",
            fontSize = 23.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Blue
        )
        if (classes.isEmpty()) {
            Text(
                "No classes added yet",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(8.dp)
            )
        } else {
            LazyColumn {
                items(classes) { course ->
                    CourseRow(
                        courseName = course,
                        onEdit = { courseBeingEdited = course },
                        onRemove = { onRemoveCourse(course) }
                    )
                }
            }
        }
    }

    // Show edit dialog if a course is selected
    courseBeingEdited?.let { course ->
        EditCourseDialog(
            courseName = course,
            onConfirm = { newCourseName ->
                onEditCourse(course, newCourseName)
                courseBeingEdited = null
            },
            onDismiss = { courseBeingEdited = null }
        )
    }
}

/**
 * Container for the requirements list.
 */
@Composable
fun RequirementsList(
    requirements: List<String>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            "Degree Requirements",
            fontSize = 23.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Blue
        )
        if (requirements.isEmpty()) {
            Text(
                "Select a major to see requirements",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(8.dp)
            )
        } else {
            LazyColumn {
                items(requirements) { req ->
                    RequirementRow(requirement = req)
                }
            }
        }
    }
}

/**
 * Main composable that orchestrates the app.
 */
@Composable
fun CourseNameInput(scheduleVM: ScheduleViewModel) {
    val classes by scheduleVM.classesPublic.collectAsState()
    val reqs by scheduleVM.reqPublic.collectAsState()
    val availablePlans by scheduleVM.availablePlans.collectAsState()
    val isLoading by scheduleVM.isLoading.collectAsState()
    val errorMessage by scheduleVM.errorMessage.collectAsState()

    var selectedMajor by remember { mutableStateOf("None") }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Major selector with dynamic plans
        MajorDropdown(
            selectedMajor = selectedMajor,
            onMajorSelected = { major ->
                selectedMajor = major
                scheduleVM.selectMajor(major)
            },
            availablePlans = availablePlans,
            isLoading = isLoading,
            errorMessage = errorMessage
        )

        Spacer(Modifier.height(20.dp))

        // Course input form
        CourseInputForm(
            onAddCourse = { dept, number ->
                scheduleVM.addClass("$dept $number")
            }
        )

        Spacer(Modifier.height(20.dp))

        // Class schedule list
        ClassScheduleList(
            classes = classes,
            onEditCourse = { oldName, newName ->
                scheduleVM.editClass(oldName, newName)
            },
            onRemoveCourse = { scheduleVM.removeClass(it) }
        )

        Spacer(Modifier.height(20.dp))

        // Requirements list
        RequirementsList(requirements = reqs)
    }
}