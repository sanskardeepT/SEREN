package com.seren.app.ui.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.seren.app.data.ConditionIds
import kotlin.random.Random

@Composable
fun NumberTaskScreen(
    onComplete: (conditionId: String, score: Float, rawJson: String, duration: Long) -> Unit,
    onNext: () -> Unit
) {
    var step by remember { mutableStateOf(1) } // Step 1: dot subitizing, Step 2: comparison
    val startTime = remember { mutableStateOf(System.currentTimeMillis()) }
    
    // Task 1: Subitizing configuration
    val dotCount = remember { Random.nextInt(3, 8) }
    var subitizingResponseTime by remember { mutableStateOf(0L) }
    var isSubitizingCorrect by remember { mutableStateOf(false) }

    // Task 2: Number comparison configuration
    val numLeft = remember { Random.nextInt(12, 99) }
    val numRight = remember { 
        var n = Random.nextInt(12, 99)
        while (n == numLeft) {
            n = Random.nextInt(12, 99)
        }
        n
    }
    var comparisonResponseTime by remember { mutableStateOf(0L) }
    var isComparisonCorrect by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Task 2: Number Operations",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        if (step == 1) {
            // Step 1: Subitizing (Dot enumeration)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Text(
                    text = "Count the dots below and select the correct number as fast as possible.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Render Dot Grid
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(dotCount) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                        )
                    }
                }
            }

            // Options Selection Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val base = maxOf(3, dotCount - 2)
                val options = (base..base + 3).toList().shuffled()
                
                options.forEach { option ->
                    Button(
                        onClick = {
                            subitizingResponseTime = System.currentTimeMillis() - startTime.value
                            isSubitizingCorrect = (option == dotCount)
                            
                            // Advance to comparison step
                            step = 2
                            startTime.value = System.currentTimeMillis()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Text(text = "$option", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        } else {
            // Step 2: Number Comparison
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Text(
                    text = "Tap on the LARGER number below as quickly as you can.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Option Card
                Card(
                    onClick = {
                        comparisonResponseTime = System.currentTimeMillis() - startTime.value
                        isComparisonCorrect = (numLeft > numRight)
                        submitHeuristicResult(
                            subitizingResponseTime, isSubitizingCorrect,
                            comparisonResponseTime, isComparisonCorrect,
                            onComplete, onNext
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(140.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "$numLeft", style = MaterialTheme.typography.displayLarge, color = MaterialTheme.colorScheme.primary)
                    }
                }

                // Right Option Card
                Card(
                    onClick = {
                        comparisonResponseTime = System.currentTimeMillis() - startTime.value
                        isComparisonCorrect = (numRight > numLeft)
                        submitHeuristicResult(
                            subitizingResponseTime, isSubitizingCorrect,
                            comparisonResponseTime, isComparisonCorrect,
                            onComplete, onNext
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(140.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "$numRight", style = MaterialTheme.typography.displayLarge, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            Spacer(modifier = Modifier.height(56.dp)) // Maintain layouts spacing alignment
        }
    }
}

private fun submitHeuristicResult(
    rt1: Long,
    correct1: Boolean,
    rt2: Long,
    correct2: Boolean,
    onComplete: (conditionId: String, score: Float, rawJson: String, duration: Long) -> Unit,
    onNext: () -> Unit
) {
    val duration = rt1 + rt2
    val averageRT = (rt1 + rt2) / 2f
    val accuracy = (if (correct1) 0.5f else 0f) + (if (correct2) 0.5f else 0f)
    
    // Heuristic z-score calculation (normative standard: normal reading RT < 1200ms)
    // Map RT delays and errors directly to Dyscalculia risk probability (0.0 to 1.0)
    val riskScore = when {
        accuracy == 0f -> 0.9f
        accuracy == 0.5f && averageRT > 1800f -> 0.8f
        accuracy == 1f && averageRT > 2000f -> 0.6f
        accuracy == 1f && averageRT > 1300f -> 0.4f
        else -> 0.15f
    }

    val rawJson = "{\"subitizing_rt\": $rt1, \"subitizing_correct\": $correct1, \"comparison_rt\": $rt2, \"comparison_correct\": $correct2}"
    onComplete(ConditionIds.DYSCALCULIA, riskScore, rawJson, duration)
    onComplete(ConditionIds.PROCESSING_SPEED, riskScore, rawJson, duration)
    onNext()
}
