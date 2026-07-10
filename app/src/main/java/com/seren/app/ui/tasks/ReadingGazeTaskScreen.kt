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
import androidx.compose.ui.unit.sp
import com.seren.app.data.ConditionIds

@Composable
fun ReadingGazeTaskScreen(
    onComplete: (conditionId: String, score: Float, rawJson: String, duration: Long) -> Unit,
    onNext: () -> Unit
) {
    val startTime = remember { System.currentTimeMillis() }

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
            text = "Task 3: Silent Reading Speed",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Read the passage below silently and tap 'Complete' when finished.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Reading latency evaluation active. Camera eye-tracking version coming soon.",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
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

        Button(
            onClick = {
                val duration = System.currentTimeMillis() - startTime
                
                val wordCount = passage.split("\\s+".toRegex()).filter { it.isNotBlank() }.size
                val durationSeconds = duration / 1000f
                val wpm = if (durationSeconds > 0.1f) (wordCount / durationSeconds) * 60f else 0f
                
                val riskScore = when {
                    wpm < 10f -> 0.90f
                    wpm < 60f -> 0.75f
                    wpm < 100f -> 0.55f
                    wpm < 140f -> 0.30f
                    wpm < 250f -> 0.12f
                    wpm >= 600f -> 0.80f // Flagged as skipping/skimming text
                    else -> 0.15f
                }
                
                val rawJson = "{\"duration_ms\": $duration, \"wpm\": $wpm, \"word_count\": $wordCount}"
                onComplete(ConditionIds.DYSLEXIA_PHONOLOGICAL, riskScore, rawJson, duration)
                onComplete(ConditionIds.DYSLEXIA_MIXED, riskScore, rawJson, duration)
                onComplete(ConditionIds.ADULT_DYSLEXIA, riskScore, rawJson, duration)
                
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
