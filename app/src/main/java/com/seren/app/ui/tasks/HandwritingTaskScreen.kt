package com.seren.app.ui.tasks

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.seren.app.data.ConditionIds
import com.seren.app.ml.TfLiteManager

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HandwritingTaskScreen(
    onComplete: (conditionId: String, score: Float, rawJson: String, duration: Long) -> Unit,
    onNext: () -> Unit
) {
    val context = LocalContext.current
    val tfLiteManager = remember { TfLiteManager(context) }
    
    val points = remember { mutableStateListOf<Offset>() }
    var startTime by remember { mutableStateOf(System.currentTimeMillis()) }
    var strokeCount by remember { mutableStateOf(0) }
    var totalDistance by remember { mutableStateOf(0f) }
    var lastPoint by remember { mutableStateOf<Offset?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Task 1: Letter Copying",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Text(
                text = "Draw the lowercase letter 'd' as cleanly as possible inside the box below. Do not lift your finger until complete.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Draw Canvas Box
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .background(Color.White)
                .pointerInteropFilter { event ->
                    val x = event.x
                    val y = event.y
                    val point = Offset(x, y)
                    
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            points.clear()
                            points.add(point)
                            lastPoint = point
                            strokeCount++
                        }
                        MotionEvent.ACTION_MOVE -> {
                            points.add(point)
                            lastPoint?.let { lp ->
                                val dist = kotlin.math.sqrt((x - lp.x) * (x - lp.x) + (y - lp.y) * (y - lp.y))
                                totalDistance += dist
                            }
                            lastPoint = point
                        }
                        MotionEvent.ACTION_UP -> {
                            lastPoint = null
                        }
                    }
                    true
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                if (points.size > 1) {
                    val path = Path()
                    path.moveTo(points[0].x, points[0].y)
                    for (i in 1 until points.size) {
                        path.lineTo(points[i].x, points[i].y)
                    }
                    drawPath(
                        path = path,
                        color = Color.Black,
                        style = Stroke(width = 8f)
                    )
                }
            }

            if (points.isEmpty()) {
                Text(
                    text = "Draw Here",
                    color = Color.Gray.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        // Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    points.clear()
                    totalDistance = 0f
                    strokeCount = 0
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(8.dp))
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Clear", tint = MaterialTheme.colorScheme.onErrorContainer)
            }

            Button(
                onClick = {
                    val duration = System.currentTimeMillis() - startTime
                    
                    // Construct mock image array [1, 224, 224, 3] to run DrawNet model
                    val dummyInput = Array(1) { Array(224) { Array(224) { FloatArray(3) { 0f } } } }
                    
                    // Simple simulation to fill pixels along points
                    points.forEach { pt ->
                        val px = (pt.x.toInt() * 224 / 1000).coerceIn(0, 223)
                        val py = (pt.y.toInt() * 224 / 1000).coerceIn(0, 223)
                        dummyInput[0][px][py][0] = 1.0f // Set draw pixel active
                    }
                    
                    // Run interpreter
                    val scores = tfLiteManager.runDrawNet(dummyInput)
                    
                    // Dyslexia score is based on the Reversal index (index 1)
                    val reversalScore = scores[1]
                    
                    // Save result JSON metadata
                    val rawJson = "{\"strokes\": $strokeCount, \"total_distance\": $totalDistance, \"duration_ms\": $duration}"
                    onComplete(ConditionIds.DYSLEXIA_SURFACE, reversalScore, rawJson, duration)
                    onComplete(ConditionIds.DYSLEXIA_MIXED, reversalScore, rawJson, duration)
                    onComplete(ConditionIds.DYSGRAPHIA_PHONOLOGICAL, reversalScore, rawJson, duration)
                    onComplete(ConditionIds.DYSGRAPHIA_MOTOR, reversalScore, rawJson, duration)
                    onComplete(ConditionIds.SLD_DYSLEXIA_DYSCALCULIA, reversalScore, rawJson, duration)
                    onComplete(ConditionIds.SLD_DYSLEXIA_DYSGRAPHIA, reversalScore, rawJson, duration)
                    onComplete(ConditionIds.ADHD_DYSLEXIA_COMORBID, reversalScore, rawJson, duration)
                    onComplete(ConditionIds.DCD, reversalScore, rawJson, duration)
                    onComplete(ConditionIds.DYSPRAXIA, reversalScore, rawJson, duration)
                    onComplete(ConditionIds.VMI, reversalScore, rawJson, duration)
                    
                    onNext()
                },
                enabled = points.size > 10,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Done, contentDescription = "Done")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Submit & Continue", fontWeight = FontWeight.Bold)
            }
        }
    }
}
