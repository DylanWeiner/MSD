package com.example.schedulebuilder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.schedulebuilder.ui.theme.ScheduleBuilderTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyToDoViewModel : ViewModel()
{
    // MyModel
    private val tasks = MutableStateFlow(listOf<String>())

    val taskPublic : StateFlow<List<String>> = tasks // This is so other classes can access this information.

    fun addTask(strTask : String) {
        tasks.value += strTask
    }
    // methods to update the model
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScheduleBuilderTheme {
                val myVM: MyToDoViewModel = viewModel()
                courseNameInput(myVM)
            }
        }
    }
}


@Composable
fun courseNameInput(myVM : MyToDoViewModel) {
    val tasks by myVM.taskPublic.collectAsState()

    Column(Modifier.fillMaxWidth().padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        var courseName by remember { mutableStateOf("") }
        var courseId by remember { mutableStateOf("") }
        Text("Course Input", fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, color = Color.Blue)
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
            Button(onClick = {myVM.addTask(courseName + " " + courseId)})
            Text("Add Item")
        }
        }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    ScheduleBuilderTheme {
////        courseNameInput()
//    }
//}