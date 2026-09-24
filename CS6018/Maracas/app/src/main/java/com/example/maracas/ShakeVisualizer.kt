package com.example.maracas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas as ComposeCanvas
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MaracasTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}

@Composable
fun MaracaScreen(viewModel: MaracaViewModel) {
    val shakes by viewModel.shakes.collectAsState()
    val stats by viewModel.stats.collectAsState()
    val isListening by viewModel.isListening.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedThreshold by remember { mutableIntStateOf(7) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F1E))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("🎵 Maraca", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1A1A2E), RoundedCornerShape(8.dp))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem("Shakes", stats.totalShakes.toString())
            StatItem("Max Intensity", "%.1f".format(stats.maxIntensity))
        }

        ShakeVisualizerCanvas(shakes, stats)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { viewModel.startListening() }, enabled = !isListening, modifier = Modifier.weight(1f)) {
                Text("Start")
            }
            Button(
                onClick = { viewModel.stopListening() },
                enabled = isListening,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B6B))
            ) {
                Text("Stop")
            }
        }

        Button(
            onClick = { showDeleteDialog = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA94D))
        ) {
            Text("Delete Old Shakes")
        }

        Text("Recent Shakes", fontSize = 14.sp, color = Color.Gray)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(shakes.take(10), key = { it.id }) { shake ->
                ShakeItem(shake) { viewModel.deleteShake(it) }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete shakes older than...") },
            text = {
                Column {
                    Slider(value = selectedThreshold.toFloat(), onValueChange = { selectedThreshold = it.toInt() }, valueRange = 1f..30f, steps = 29)
                    Text("$selectedThreshold days", fontSize = 12.sp)
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.deleteShakesOlderThan(selectedThreshold)
                    showDeleteDialog = false
                }) { Text("Delete") }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun ShakeVisualizerCanvas(shakes: List<ShakeEntity>, stats: ShakeStats) {
    if (shakes.isEmpty()) {
        androidx.compose.foundation.layout.Box(
            Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFF1A1A2E)),
            contentAlignment = Alignment.Center
        ) {
            Text("No shakes yet. Start listening!")
        }
        return
    }

    ComposeCanvas(
        Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(Color(0xFF1A1A2E))
    ) {
        val padding = 40f
        val graphWidth = size.width - 2 * padding
        val graphHeight = size.height - 2 * padding

        drawLine(Color.Gray, Offset(padding, size.height - padding), Offset(size.width - padding, size.height - padding), 2f)
        drawLine(Color.Gray, Offset(padding, padding), Offset(padding, size.height - padding), 2f)

        val (earliestTime, latestTime) = stats.timeRange
        val timeSpan = (latestTime - earliestTime).coerceAtLeast(1L)

        shakes.forEach { shake ->
            val xPos = padding + ((shake.timestamp - earliestTime).toFloat() / timeSpan) * graphWidth
            val intensityFraction = (shake.intensity / stats.maxIntensity.coerceAtLeast(1f)).coerceIn(0f, 1f)
            val barHeight = intensityFraction * graphHeight
            val hue = 120f - (intensityFraction * 60f)
            val color = Color.hsv(hue, 0.8f, 0.9f)

            drawRect(color, Offset(xPos - 6f, size.height - padding - barHeight), Size(12f, barHeight))
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
fun ShakeItem(shake: ShakeEntity, onDelete: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1A2E), RoundedCornerShape(4.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(shake.timestamp), fontSize = 12.sp, color = Color.White)
            Text("Intensity: %.1f".format(shake.intensity), fontSize = 10.sp, color = Color.Gray)
        }
        IconButton(onClick = { onDelete(shake.id) }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
        }
    }
}