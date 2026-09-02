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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.classschedule.ui.theme.ClassScheduleTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MyToDoViewModel : ViewModel()
{
    // MyModel
    private val tasks = MutableStateFlow(listOf<String>())
    private val reqs = MutableStateFlow(listOf<String>())

    val taskPublic : StateFlow<List<String>> = tasks // This is so other classes can access this information.
    val reqPublic : StateFlow<List<String>> = reqs // This is so other classes can access this information.

    fun addTask(strTask : String) {
        val target = strTask
        if (target in reqs.value) {
            reqs.value = reqs.value.map { if (it == target) "$strTask (DONE)" else it }
        }
        tasks.value += strTask
    }

    fun removeTask(strTask : String) {
        val target = "$strTask (DONE)"
        if (target in reqs.value) {
            reqs.value = reqs.value.map { if (it == target) strTask else it }
        }
        tasks.value -= strTask
    }

    fun addReq(strTask : String) {
        reqs.value += strTask
    }

    fun clearReqs() {
        reqs.value = listOf<String>()
    }
    // methods to update the model
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClassScheduleTheme {
                val classVM: MyToDoViewModel = viewModel()
                CourseNameInput(classVM)
            }
        }
    }
}

@Composable
fun MajorDropdown(classVM: MyToDoViewModel) {
    val courseReqs = mapOf(
        "Computer Science" to listOf("CS 1010 OR CS 1011", "CS 2010", "CS 3010", "CS 4010"),
        "Languages" to listOf("LNG 1010 OR LNG 1011", "LNG 2010", "LNG 3010", "LNG 4010"),
        "Biology" to listOf("BIO 1010 OR BIO 1011", "BIO 2010", "BIO 3010", "BIO 4010"),
        "Chemistry" to listOf("CHEM 1010 OR CHEM 1011", "CHEM 2010", "CHEM 3010", "CHEM 4010"),
        "History" to listOf("HS 1010 OR HS 1011", "HS 2010", "HS 3010", "HS 4010"),
        "Art" to listOf("ART 1010 OR ART 1011", "ART 2010", "ART 3010", "ART 4010")
    )
    val majors = listOf("None", "Computer Science", "Languages", "Biology", "Chemistry", "History", "Art")

    var expanded by remember { mutableStateOf(false) }
    var selectedMajor by remember { mutableStateOf("None") }

    Column(Modifier.fillMaxWidth().padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(
            "Select Major",
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Blue
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            majors.forEach { major ->
                DropdownMenuItem(
                    text = { Text(text = major) },
                    onClick = {
                        selectedMajor = major
                        expanded = false

                        val requirements = courseReqs[selectedMajor] ?: emptyList()
                        classVM.clearReqs()
                        requirements.forEach {
                            classVM.addReq(it)
                        }
                    }
                )
            }
        }
        Button(
            onClick = {
                expanded = !expanded
            }) {
            Text(selectedMajor)
        }
    }
}

@Composable
fun CourseNameInput(classVM : MyToDoViewModel) {
    val tasks by classVM.taskPublic.collectAsState()
    val reqs by classVM.reqPublic.collectAsState()

    Column(Modifier.fillMaxWidth().padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        var courseName by remember { mutableStateOf("") }
        var courseId by remember { mutableStateOf("") }

        MajorDropdown(classVM)

        Text(
            "Course Input",
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Blue
        )
        TextField(
            value = courseName,
            onValueChange = { newText -> courseName = newText },
            label = { Text("Enter your course Department (i.e. CS)") }
        )
        TextField(
            value = courseId,
            onValueChange = { newText -> courseId = newText },
            label = { Text("Enter your course number (i.e. 1010)") }
        )
        Row {
            var courseInfo = "$courseName $courseId"
            Button(
                onClick = { classVM.addTask(courseInfo) }) {
                Text("Add Item")
            }
        }

        Spacer(Modifier.height(20.dp))
        Text("Class Schedule", fontSize = 23.sp, fontWeight = FontWeight.ExtraBold, color = Color.Blue)
        Row{
            LazyColumn() {
                items(tasks){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            it,
                            fontSize = 20.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = { classVM.removeTask(it) }) {
                            Text("Remove Item")
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        Text("Degree Requirements", fontSize = 23.sp, fontWeight = FontWeight.ExtraBold, color = Color.Blue)
        Row{
            LazyColumn() {
                items(reqs){
                    Text(it, fontSize = 20.sp, fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}