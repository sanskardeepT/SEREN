package com.seren.app.ui.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import android.content.Context
import android.os.Vibrator
import android.os.VibrationEffect
import android.os.Build
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seren.app.data.ConditionIds
import com.seren.app.ml.TfLiteManager
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun AttentionTaskScreen(
    onComplete: (conditionId: String, score: Float, rawJson: String, duration: Long) -> Unit,
    onNext: () -> Unit
) {
    val context = LocalContext.current
    val tfLiteManager = remember { TfLiteManager(context) }
    
    val startTime = remember { System.currentTimeMillis() }
    
    var trialIndex by remember { mutableStateOf(0) }
    val totalTrials = 12
    var currentLetter by remember { mutableStateOf("A") }
    var letterVisible by remember { mutableStateOf(true) }
    
    // Feature trackers
    var misses by remember { mutableStateOf(0) }
    var falseAlarms by remember { mutableStateOf(0) }
    val responseTimes = remember { mutableStateListOf<Long>() }
    var trialStartTime by remember { mutableStateOf(0L) }
    var hasTappedInTrial by remember { mutableStateOf(false) }

    // Spam/Masti detection trackers
    var lastTapTime by remember { mutableStateOf(0L) }
    var spamTapCount by remember { mutableStateOf(0) }
    var showSpamAlert by remember { mutableStateOf(false) }

    // Run Go/No-Go loop
    LaunchedEffect(trialIndex) {
        if (trialIndex >= totalTrials) {
            submitAttentionResults(misses, falseAlarms, responseTimes, tfLiteManager, startTime, onComplete, onNext)
            return@LaunchedEffect
        }
        
        // Randomly set Go or No-Go target (No-Go target letter is 'X')
        currentLetter = if (Random.nextFloat() > 0.7f) "X" else listOf("A", "B", "C", "D", "E").random()
        letterVisible = true
        hasTappedInTrial = false
        trialStartTime = System.currentTimeMillis()

        // Visible period (800ms)
        delay(850)
        
        // Handle no-taps on Go trials (represents attentional omission/miss)
        if (!hasTappedInTrial && currentLetter != "X") {
            misses++
        }
        
        letterVisible = false
        delay(200) // Inter-stimulus interval
        
        trialIndex++
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Task 5: Go/No-Go Attention",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Text(
                text = "Tap the screen as fast as possible when any letter appears, EXCEPT if the letter is 'X'. For 'X', do not tap.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Target Board Area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .clickable {
                    val now = System.currentTimeMillis()
                    if (now - lastTapTime < 250) {
                        spamTapCount++
                        if (spamTapCount >= 3) {
                            showSpamAlert = true
                            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                            vibrator?.let { v ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    v.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
                                } else {
                                    v.vibrate(300)
                                }
                            }
                        }
                    } else {
                        spamTapCount = 0
                    }
                    lastTapTime = now

                    if (!hasTappedInTrial && trialIndex < totalTrials) {
                        hasTappedInTrial = true
                        val rt = System.currentTimeMillis() - trialStartTime
                        responseTimes.add(rt)
                        
                        if (currentLetter == "X") {
                            // Commision error (false alarm on No-Go target)
                            falseAlarms++
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            if (letterVisible && trialIndex < totalTrials) {
                Text(
                    text = currentLetter,
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 110.sp),
                    color = if (currentLetter == "X") MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Progress Details
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Trial: $trialIndex / $totalTrials",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Errors: ${misses + falseAlarms}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (showSpamAlert) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { 
                    showSpamAlert = false
                    spamTapCount = 0
                },
                title = { Text("Response Consistency Alert", fontWeight = FontWeight.Bold) },
                text = { Text("We detected rapid screen inputs that may affect the accuracy of the assessment. Please read the instructions carefully and respond only when target stimuli appear.") },
                confirmButton = {
                    Button(
                        onClick = { 
                            showSpamAlert = false
                            spamTapCount = 0
                        }
                    ) {
                        Text("Resume Test")
                    }
                }
            )
        }
    }
}

private fun submitAttentionResults(
    misses: Int,
    falseAlarms: Int,
    responseTimes: List<Long>,
    tfLiteManager: TfLiteManager,
    startTime: Long,
    onComplete: (conditionId: String, score: Float, rawJson: String, duration: Long) -> Unit,
    onNext: () -> Unit
) {
    val duration = System.currentTimeMillis() - startTime
    
    // Normalize metrics
    val missRate = misses.toFloat() / 9f // 9 Go trials on average
    val commRate = falseAlarms.toFloat() / 3f // 3 No-Go trials on average
    val rtMean = if (responseTimes.isNotEmpty()) responseTimes.average() else 350.0
    val rtStd = if (responseTimes.size > 1) {
        val mean = rtMean
        kotlin.math.sqrt(responseTimes.map { (it - mean) * (it - mean) }.sum() / (responseTimes.size - 1))
    } else {
        45.0
    }
    
    val rtVar = (rtStd / rtMean).toFloat()
    val mockGazeOffTask = Random.nextFloat() * 0.15f // simulated gaze indicator
    
    // Prepare input vector: [miss_rate, commission_rate, rt_variability, gaze_off_task_pct]
    val inputStats = floatArrayOf(
        missRate.coerceIn(0f, 1f),
        commRate.coerceIn(0f, 1f),
        rtVar.coerceIn(0f, 1f),
        mockGazeOffTask
    )
    
    // Run inference
    val predictions = tfLiteManager.runAttentNet(inputStats)
    
    // Predictions array mapping classes: [Control, Inattentive, Hyperactive, Combined]
    val rawJson = "{\"misses\": $misses, \"false_alarms\": $falseAlarms, \"rt_mean\": ${"%.1f".format(rtMean)}, \"rt_std\": ${"%.1f".format(rtStd)}, \"duration_ms\": $duration}"
    
    // Batch 1 Conditions
    onComplete(ConditionIds.ADHD_INATTENTIVE, predictions[1], rawJson, duration)
    onComplete(ConditionIds.ADHD_HYPERACTIVE, predictions[2], rawJson, duration)
    onComplete(ConditionIds.ADHD_COMBINED, predictions[3], rawJson, duration)
    
    // Batch 2 Conditions
    onComplete(ConditionIds.DEPRESSION, predictions[1] * 0.7f, rawJson, duration)
    onComplete(ConditionIds.EMOTIONAL_DYSREGULATION, predictions[2] * 0.8f, rawJson, duration)
    onComplete(ConditionIds.TEST_ANXIETY, predictions[1] * 0.6f, rawJson, duration)
    
    // Batch 3 / Adults / Others
    onComplete(ConditionIds.ADULT_ADHD, predictions[3], rawJson, duration)
    onComplete(ConditionIds.PROCESSING_SPEED, predictions[3], rawJson, duration)
    onComplete(ConditionIds.VMI, predictions[2] * 0.8f, rawJson, duration)
    onComplete(ConditionIds.ADULT_PROCESSING_SPEED, predictions[3], rawJson, duration)
    onComplete(ConditionIds.SENSORY_PROCESSING, predictions[2], rawJson, duration)
    onComplete(ConditionIds.SENSORY_DEFENSIVENESS, predictions[2], rawJson, duration)
    onComplete(ConditionIds.DECISION_PARALYSIS, predictions[1], rawJson, duration)
    
    onNext()
}
