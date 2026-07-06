package com.seren.app.ui.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.seren.app.data.ConditionIds
import com.seren.app.ml.TfLiteManager
import kotlin.random.Random

@Composable
fun ReadingGazeTaskScreen(
    onComplete: (conditionId: String, score: Float, rawJson: String, duration: Long) -> Unit,
    onNext: () -> Unit
) {
    val context = LocalContext.current
    val tfLiteManager = remember { TfLiteManager(context) }
    
    val startTime = remember { System.currentTimeMillis() }
    var mockGazeTrackingActive by remember { mutableStateOf(false) }
    var mockTrackingProgress by remember { mutableStateOf(0f) }

    val passage = """
        The little boy went to the forest to find his lost dog. 
        He looked behind the old oak trees, near the calm river, 
        and under the green bushes. Finally, he heard a friendly bark!
    """.trimIndent()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Task 3: Silent Reading Gaze",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Text(
                text = "Keep your face visible to the front camera and read the text below silently. Tap 'Complete' when finished.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Passage Display Box
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = passage,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 32.sp
                )
            }
        }

        // Camera feed status indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "● Front Camera Tracking Active",
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = {
                val duration = System.currentTimeMillis() - startTime
                
                // Construct mock gaze coordinates array [1, 100, 6] to feed to GazeNet LSTM
                // Gaze features: [x, y, dx, dy, speed, is_fixation]
                val mockGazeData = Array(1) { Array(100) { FloatArray(6) } }
                
                // Simulate eye scanning coordinates (scanning left to right, line by line)
                var x = 100f
                var y = 200f
                val randomDyslexiaIndicator = Random.nextBoolean()
                
                for (i in 0 until 100) {
                    if (randomDyslexiaIndicator && i % 15 == 0) {
                        // Simulate regression (eye backing up to check word)
                        x -= 80f
                    } else {
                        x += 30f
                    }
                    
                    if (x > 800f) {
                        x = 100f
                        y += 100f // Next line
                    }
                    
                    mockGazeData[0][i][0] = x
                    mockGazeData[0][i][1] = y
                    mockGazeData[0][i][2] = if (i > 0) x - mockGazeData[0][i-1][0] else 0f
                    mockGazeData[0][i][3] = if (i > 0) y - mockGazeData[0][i-1][1] else 0f
                    mockGazeData[0][i][4] = Random.nextFloat() * 15f
                    mockGazeData[0][i][5] = if (Random.nextFloat() > 0.3f) 1.0f else 0.0f
                }
                
                // Run GazeNet inference
                val riskScore = tfLiteManager.runGazeNet(mockGazeData[0])
                
                val rawJson = "{\"duration_ms\": $duration, \"regressions_injected\": $randomDyslexiaIndicator}"
                onComplete(ConditionIds.DYSLEXIA, riskScore, rawJson, duration)
                
                onNext()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Text(text = "Complete Silent Reading", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        }
    }
}
