package com.example.navigationdemo2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import com.example.navigationdemo2.ui.theme.NavigationDemo2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationDemo2Theme {
//                Scaffold(modifier = Modifier.fillMaxSize()) {
//
//                }
            }
        }
    }
}

@Composable
fun AdaptiveLayout(windowSizeClass: WindowSizeClass) {
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            Column(Modifier.statusBarsPadding()) {
                TaskList
            }
        }
        WindowWidthSizeClass.Expanded -> {
            Row(Modifier.statusBarsPadding()) { TaskListScaffolding
            }
        }
    }
    Layout(windowSizeClass.widthSizeClass)
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    NavigationDemo2Theme {
//        Greeting("Android")
//    }
//}