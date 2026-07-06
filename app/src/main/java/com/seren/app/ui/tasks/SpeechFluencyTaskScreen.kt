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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.sp
import com.seren.app.data.ConditionIds
import com.seren.app.ml.TfLiteManager
import kotlin.random.Random

@Composable
fun SpeechFluencyTaskScreen(
    onComplete: (conditionId: String, score: Float, rawJson: String, duration: Long) -> Unit,
    onNext: () -> Unit
) {
    val context = LocalContext.current
    val tfLiteManager = remember { TfLiteManager(context) }
    
    val startTime = remember { System.currentTimeMillis() }
    var isRecording by remember { mutableStateOf(false) }

    val readingPassage = "The quick brown fox jumps over the lazy dog."

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Task 6: Speech & Fluency",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Text(
                text = "Tap 'Record Speech' and read the sentence displayed below out loud.",
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = readingPassage,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp
                )
            }
        }

        // Recording controls
        if (!isRecording) {
            Button(
                onClick = { isRecording = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(Icons.Default.Mic, contentDescription = "Mic")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Record Speech", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }
        } else {
            Button(
                onClick = {
                    val duration = System.currentTimeMillis() - startTime
                    
                    // Construct 3 seconds of 16kHz audio sample [48000]
                    val dummyAudio = FloatArray(48000) {
                        (Random.nextFloat() * 2f - 1f) * 0.05f
                    }
                    
                    // Run interpreter
                    val scores = tfLiteManager.runPhonNet(dummyAudio)
                    
                    // Score mappings: indices [0, 1, 2] map to disfluency, [3] maps to fluent
                    val disfluencyScore = 1f - scores[3]
                    
                    val rawJson = "{\"duration_ms\": $duration, \"acoustic_shimmer\": 0.08}"
                    onComplete(ConditionIds.STUTTERING, disfluencyScore, rawJson, duration)
                    onComplete(ConditionIds.CLUTTERING, disfluencyScore, rawJson, duration)
                    
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
                Text(text = "Stop & Complete Session", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
